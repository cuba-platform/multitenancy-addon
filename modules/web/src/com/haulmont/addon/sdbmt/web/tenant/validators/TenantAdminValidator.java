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

package com.haulmont.addon.sdbmt.web.tenant.validators;

import com.haulmont.cuba.core.app.multitenancy.TenantProvider;
import com.haulmont.cuba.core.entity.HasTenant;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Tenant;

import java.util.Objects;
import java.util.function.Consumer;

public class TenantAdminValidator implements Consumer<HasTenant> {

    private Messages messages = AppBeans.get(Messages.class);
    private Datasource<Tenant> tenantDs;

    public TenantAdminValidator(Datasource<Tenant> tenantDs) {
        this.tenantDs = tenantDs;
    }

    @Override
    public void accept(HasTenant value) throws ValidationException {
        if (value == null) {
            return;
        }

        String adminTenantId = value.getTenantId();
        if (adminTenantId != null && !adminTenantId.equals(TenantProvider.TENANT_ADMIN) && !Objects.equals(adminTenantId, tenantDs.getItem().getTenantId())) {
            throw new ValidationException(messages.getMessage(TenantAdminValidator.class, "validation.userBelongsToDifferentTenant"));
        }
    }
}
