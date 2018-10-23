/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.entity;

import com.haulmont.addon.sdbmt.core.TenantId;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.security.entity.UserRole;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Extends(UserRole.class)
@Entity(name = "cubasdbmt$TenantUserRole")
public class TenantUserRole extends UserRole implements HasTenant {
    private static final long serialVersionUID = -3685131016689459387L;

    @TenantId
    @Column(name = "TENANT_ID")
    protected String tenantId;

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }
}