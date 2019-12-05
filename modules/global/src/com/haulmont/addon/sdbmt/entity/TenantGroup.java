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
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.security.entity.Group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Extends(Group.class)
@Entity(name = "cubasdbmt$TenantGroup")
public class TenantGroup extends Group implements HasTenant {
    private static final long serialVersionUID = 5896941079669582519L;

    @TenantId
    @Column(name = "TENANT_ID")
    protected String tenantId;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "group")
    protected Tenant tenant;

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

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