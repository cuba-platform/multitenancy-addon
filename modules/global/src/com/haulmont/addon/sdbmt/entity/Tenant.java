/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haulmont.addon.sdbmt.entity;

import javax.persistence.*;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;

/**
 * There is no need to extends StandardTenantEntity per se - we don't need to hide tenantId in the UI since tenants
 * don't have access to Tenant entity at all. However we have to have a descendant from MappedSuperclass defined in
 * the application component
 * @see <a href="https://youtrack.cuba-platform.com/issue/PL-10474">PL-10474</a>
 *
 * @deprecated Use {@link com.haulmont.cuba.security.entity.Tenant} instead
 */
@Deprecated
@Table(name = "CUBASDBMT_TENANT")
@Entity(name = "cubasdbmt$Tenant")
@Listeners("cubasdbmt_TenantListener")
@NamePattern("%s|name")
@AttributeOverride(name = "tenantId", column = @Column(name = "TENANT_ID", nullable = false, unique = true))
public class Tenant extends StandardTenantEntity {
    private static final long serialVersionUID = 4892183772427391265L;

    @Column(name = "NAME", nullable = false, unique = true)
    protected String name;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ACCESS_GROUP_ID", unique = true)
    protected Group group;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ADMIN_ID", unique = true)
    protected User admin;

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public User getAdmin() {
        return admin;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}