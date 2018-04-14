/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
 */
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

    @OneToOne(fetch = FetchType.LAZY)
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