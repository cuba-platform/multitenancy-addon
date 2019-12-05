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
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;

@Extends(User.class)
@Entity(name = "cubasdbmt$TenantUser")
@Listeners("cubasdbmt_SdbmtUserEntityListener")
@NamePattern("#getCaption|login,name,tenantId")
public class TenantUser extends User implements HasTenant {
    private static final long serialVersionUID = -2598354095282194816L;

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