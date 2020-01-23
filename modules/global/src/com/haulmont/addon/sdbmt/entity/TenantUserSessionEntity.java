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

import com.haulmont.addon.sdbmt.core.TenantId;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.security.entity.UserSessionEntity;

/**
 * Added sysTenantId field to UserSessionEntity entity, instead of tenantId.
 * <p>
 * Use {@link UserSessionEntity} instead
 */
@Deprecated
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