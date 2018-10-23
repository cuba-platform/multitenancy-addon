/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.entity;

import com.haulmont.addon.sdbmt.core.TenantId;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.security.entity.Group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Extends(Group.class)
@Entity(name = "cubasdbmt$TenantGroup")
public class TenantGroup extends Group implements HasTenant, HasTenantInstance {
    private static final long serialVersionUID = 5896941079669582519L;

    @TenantId
    @Column(name = "TENANT_ID")
    protected String tenantId;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "group")
    protected Tenant tenant;

    @Override
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public Tenant getTenant() {
        return tenant;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }
}