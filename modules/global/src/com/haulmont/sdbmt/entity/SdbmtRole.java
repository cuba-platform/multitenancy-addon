/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.entity;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Column;

import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.sdbmt.core.HasTenant;
import com.haulmont.sdbmt.core.TenantId;

@Extends(Role.class)
@DiscriminatorValue("SDBMT")
@Entity(name = "sdbmt$SdbmtRole")
public class SdbmtRole extends Role implements HasTenant {
    private static final long serialVersionUID = 665164243007597868L;

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