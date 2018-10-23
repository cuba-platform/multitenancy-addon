/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.entity;

import com.haulmont.addon.sdbmt.core.TenantId;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.security.entity.Role;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Extends(Role.class)
@Entity(name = "cubasdbmt$TenantRole")
public class TenantRole extends Role implements HasTenant {
    private static final long serialVersionUID = 8390488311477541425L;

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