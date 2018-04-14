/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.addon.sdbmt.core.TenantId;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class StandardTenantEntity extends StandardEntity implements HasTenant {
    private static final long serialVersionUID = -1215037188627831268L;

    @TenantId
    @Column(name = "TENANT_ID")
    protected String tenantId;

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }
}
