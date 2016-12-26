/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.sdbmt.core.TenantId;

@MetaClass(name = "sec$UserSessionEntity")
@Extends(UserSessionEntity.class)
public class SdbmtUserSessionEntity extends UserSessionEntity {

    @TenantId
    @MetaProperty
    private String tenantId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
