[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

# Implementation of a single database multi-tenancy support for Cuba applications.

They key idea is to use a single application instance to serve multiple tenants - groups of users invisible to each other which don't share any data they have **write** access to.

The application supports two types of data - common data (shared across tenants), and tenant-specific data.
Tenants have read-only access to common data and full access to tenant-specific data. All the tenants have their own admin users which can create tenant users and assign tenant-specific roles and permissions.

This is single database/single schema implementation of multi-tenancy. Tenant-specific data owner is specified by the means of column `TENANT_ID` in tenant tables.

All tenant-specific entities implement `HasTenant` interface, which simply states that entity should have getter and setter for tenant id attribute.

Sample application, using this component can be found here: https://github.com/igor-korotkov/singledb-multitenancy

## Installation

1. Open component in CUBA studio and invoke Run > Install app component
1. Open your application in CUBA studio and in project properties in 'Advanced' tab enable 'Use local Maven repository'
1. Select a version of the add-on which is compatible with the platform version used in your project:

| Platform Version | Add-on Version |
| ---------------- | -------------- |
| 6.8.x            | 0.7-SNAPSHOT |


The latest version is: 0.7-SNAPSHOT

Add custom application component to your project:

* Artifact group: `com.haulmont.addon.sdbmt`
* Artifact name: `sdbmt-global`
* Version: *add-on version*

# Managing tenants
Tenants are being created and managed by global admins - users which don't belong to any tenant.  
Tenants can be managed using Tenant management -> Tenants screen.  
**Each tenant should have unique tenant id, root access group and default administrator assigned**.

Tenant access group serves a role of a root access group for tenant admins. Think **Company** access group, but for tenants.  
Tenant root access group can't be a parent of any other tenant's group, i.e. **sub-tenants are not supported**.


# Tenant permissions
Tenant permissions are being handled by Cuba security subsystem. Tenant permissions are being compiled at runtime during user login and being stored in the user session.

(For implementation see `LoginEventListener`, `MultiTenancySecurityHandler`)

All the tenants are implicitly assigned with a default tenant role. Default role's purpose is to hide system functionality which no tenant should not have access to (JMX console, Server log etc).
Default tenant role is specified in `TenantConfig` (`cubasdbmt.defaultTenantRole`). It is assigned to all tenant users automatically in `SdbmtUserEntityListener`.

Tenants can create their own user Roles, so role editor has been modified. Additionally to Cuba requirement for users to have access to Permission entity, system now allows the user to give only those permissions which he owns himself.  
Meaning if the user has read-only access to some entity, he can't permit other users to modify it, however, he can prohibit users from reading it.  
**Specific** and **UI** permissions have been hidden from tenants.


# Read-only access to shared data
Tenants have read-only access to all persistent entities which don't implement the `HasTenant` interface.  
This is implemented via Cuba security subsystem and compiled at runtime as described above. 


# Tenant-specific data
All tenant-specific tables have additional column `TENANT_ID` to specify the owner of the data.

In order for an entity to be tenant-specific, it should implement the `HasTenant` interface.  
In order to make Cuba entity tenant-specific, a developer should extend it in the project and make it implement `HasTenant` interface. SQL update scripts can be generated either by Cuba Studio or manually.

Whenever tenant user reads tenant-specific data, the system adds an additional **where** condition on tenant_id to JPQL query in order to read the data of current tenant only. Data with no tenant id or with tenant id different from the tenant id of current user will be omitted.

**There is no automatic filtering for native SQL - thus tenants should not have access to any functionality ginig access to write native SQL or Groovy code (JMX Console, SQL/Groovy bands in reports etc)**.

There is no need to assign tenant id to entities manually - it is being handled automatically.  
During login, tenant user session receives tenant id from the Tenant entity. Whenever tenant user creates tenant-specific entity system assigns tenant id to the newly created entity automatically.

(For implementation see `MtTransactionListener`)

Developer can add `tenantId` attribute to tenant-specific entities screens, which can be useful for QA and for global administrators.  
`tenantId` column/field will be hidden from tenant users as long as `tenantId` attribute is marked with `@TenantId` annotation in the entity code.