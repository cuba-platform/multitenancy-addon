/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.entity;

import com.haulmont.addon.sdbmt.core.TenantId;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.security.entity.UserSessionEntity;

@Extends(UserSessionEntity.class)
@MetaClass(name = "cubasdbmt$TenantUserSessionEntity")
public class TenantUserSessionEntity extends UserSessionEntity implements HasTenant {
    private static final long serialVersionUID = 3586071784104335837L;

    @TenantId
    @MetaProperty
    protected String tenantId;

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }
}