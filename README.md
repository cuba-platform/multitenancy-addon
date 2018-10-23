[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

# Implementation of a single database multi-tenancy support for CUBA applications.

The key idea is to use a single application instance to serve multiple tenants - groups of users invisible to each other which don't share any data they have **write** access to.

The application supports two types of data - common data (shared across tenants), and tenant-specific data.
Tenants have read-only access to common data and full access to tenant-specific data. All the tenants have their own admin users which can create tenant users and assign tenant-specific roles and permissions.

This is single database/single schema implementation of multi-tenancy. Tenant-specific data owner is specified by the means of column `TENANT_ID` in tenant tables.

All tenant-specific entities implement `HasTenant` interface, which simply states that an entity should have getter and setter for tenant id attribute.

Sample application, using this component can be found here: https://github.com/cuba-platform/multitenancy-addon-demo

## Installation

1. Open component in CUBA studio and invoke Run > Install app component
1. Open your application in CUBA studio and in project properties in 'Advanced' tab enable 'Use local Maven repository'
1. Select a version of the add-on which is compatible with the platform version used in your project:

| Platform Version | Add-on Version | 
| ---------------- | -------------- | 
| 6.8.x            | 1.0.0          | 
| 6.9.x            | 1.1.0          | 

  The latest version is: 1.1-SNAPSHOT

4. Add custom application component to your project:

  * Artifact group: `com.haulmont.addon.sdbmt`
  * Artifact name: `sdbmt-global`
  * Version: *add-on version*


5. Extend CUBA entity Group in the project. Check the box 'Replace parent' in cuba studio for entity.
   The new entity has to implement HasTenant and HasTenantInstance interfaces. Make sure to add tenantId (String) and tenant (Tenant) attributes to the entity.
   Make sure that tenantId attribute has @TenantId annotation to hide the attribute from all screens where the one may appear.
   Make sure to add an annotation @OneToOne(fetch = FetchType.LAZY, mappedBy = "group") for the field tenant.
   The entity should have a discriminator value (annotation @DiscriminatorValue).
6. Extend CUBA entity User in the project. Check the box 'Replace parent' in cuba studio for entity.
   The new entity has to implement HasTenant interface. Make sure to add tenantId (String) attribute to the entity.
   Make sure that tenantId attribute has @TenantId annotation to hide the attribute from all screens where the one may appear.
   The entity should have a discriminator value (annotation @DiscriminatorValue).
7. For custom User class add `@Listeners("cubasdbmt_SdbmtUserEntityListener")` and `@NamePattern("#getCaption|login,name,tenantId")` annotations

### Optional installation steps
In order to make your entities tenant-specific - either extent StandardTenantEntity instead of the StandardEntity (StandardTenantEntity basically is CUBA'a StandardEntity but with tenantId column), or implement HasTenant interface and add tenantId column manually.

Note that tenants don't have write access to entites without a tenantId attribute. Naturally, that includes CUBA's system entities as well.
Some of them are important for proper user experience: roles and permissions, filters on screens, files in the file storage, emails, search folders.
You can make them tenant-specific simply by extending the entity and implementing HasTenant interface in child classes.

1. Standard functionality and steps to make it tenant-specific:

#### Security roles and permissions: ability for tenants to create their own roles and permissions
This is technically not required, but practically is: this is an ability for tenants to have hierarchy of users, having different access to the system (admins vs users etc).
Extend following CUBA entities and make them implementing HasTenant:

com.haulmont.cuba.security.entity.Role
com.haulmont.cuba.security.entity.UserRole
com.haulmont.cuba.security.entity.Permission
com.haulmont.cuba.security.entity.Constraint
com.haulmont.cuba.security.entity.GroupHierarchy

#### CUBA Filters: ability for tenants to create screen filters
Extend following CUBA entities and make them implementing HasTenant:

com.haulmont.cuba.security.entity.FilterEntity


#### Application folders and Search folders.
Extend following CUBA entities and make them implementing HasTenant:

com.haulmont.cuba.security.entity.SearchFolder
com.haulmont.cuba.security.entity.AppFolder


#### User sessions: for tenants to see the list of logged in users and their sessions
Extend following CUBA entities and make them implementing HasTenant:

com.haulmont.cuba.security.entity.UserSessionEntity

Note that this is a non-persistent entity, so the definition of tenantId entity will have `@MetaProperty` annotation instead of `@Column`:
```java
@TenantId
@MetaProperty
protected String tenantId;
```

add the following line into spring.xml:
```xml
<bean id="cuba_UserSessions" class="com.haulmont.addon.sdbmt.security.app.SdbmtUserSessions"/>
```

#### Dynamic Attributes: ability for tenant admins to add dynamic attributes to tenant-specific entities
Extend following CUBA entities and make them implementing HasTenant:

com.haulmont.cuba.core.entity.Category
com.haulmont.cuba.core.entity.CategoryAttribute
com.haulmont.cuba.core.entity.CategoryAttributeValue

add the following line into web-spring.xml:
```xml
<bean id="cuba_DynamicAttributesGuiTools" class="com.haulmont.addon.sdbmt.gui.dynamicattributes.MultiTenancyDynamicAttributesGuiTools"/>
```


# Managing tenants
Tenants are being created and managed by global admins - users that don't belong to any tenant.  
Tenants can be managed using Tenant management -> Tenants screen.  
**Each tenant should have unique tenant id, root access group and default administrator assigned**.

Tenant access group serves a role of a root access group for tenant admins. Think **Company** access group, but for tenants.  
Tenant root access group can't be a parent of any other tenant's group, i.e. **sub-tenants are not supported**.

During tenant creation process use tenant's admin access group which is the same as `Root Access Group`. In next versions of addon this preconditions will be set automatically

# Tenant permissions
Tenant permissions are being handled by CUBA security subsystem. Tenant permissions are being compiled at runtime during user login and being stored in the user session.

(For implementation see `LoginEventListener`, `MultiTenancySecurityHandler`)

All the tenants are implicitly assigned with a default tenant role. Default role's purpose is to hide system functionality which no tenant should not have access to (JMX console, Server log etc).
Default tenant role is specified in `TenantConfig` (`cubasdbmt.defaultTenantRole`). It is assigned to all tenant users automatically in `SdbmtUserEntityListener`.

Tenants can create their own user Roles, so role editor has been modified. Additionally to CUBA requirement for users to have access to Permission entity, system now allows the user to give only those permissions which he owns himself.  
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
