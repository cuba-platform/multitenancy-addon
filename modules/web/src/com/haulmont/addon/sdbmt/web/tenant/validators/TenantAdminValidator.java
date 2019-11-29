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

import com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider;
import com.haulmont.addon.sdbmt.core.global.TenantEntityOperation;
import com.haulmont.addon.sdbmt.core.tools.MultiTenancyHelperService;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;

import java.util.Objects;
import java.util.function.Consumer;

public class TenantAdminValidator implements Consumer<Entity> {

    private Messages messages = AppBeans.get(Messages.class);
    private MultiTenancyHelperService multiTenancyHelper = AppBeans.get(MultiTenancyHelperService.class);
    private TenantEntityOperation tenantEntityOperation = AppBeans.get(TenantEntityOperation.class);
    private Datasource<Tenant> tenantDs;

    public TenantAdminValidator(Datasource<Tenant> tenantDs) {
        this.tenantDs = tenantDs;
    }

    @Override
    public void accept(Entity value) throws ValidationException {
        if (value == null) {
            return;
        }

        String login = tenantDs.getItem().getAdmin().getLogin();
        if (multiTenancyHelper.isSystemLogin(login)) {
            throw new ValidationException(messages.getMessage(TenantAdminValidator.class, "validation.userIsSystemUser"));
        }

        String adminTenantId = tenantEntityOperation.getTenantId(value);
        if (adminTenantId != null && !adminTenantId.equals(TenantProvider.NO_TENANT) && !Objects.equals(adminTenantId, tenantDs.getItem().getTenantId())) {
            throw new ValidationException(messages.getMessage(TenantAdminValidator.class, "validation.userBelongsToDifferentTenant"));
        }
    }
}
