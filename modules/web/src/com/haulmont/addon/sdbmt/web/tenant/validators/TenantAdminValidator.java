/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.web.tenant.validators;

import com.haulmont.addon.sdbmt.entity.HasTenant;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;

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
        if (adminTenantId != null && !Objects.equals(adminTenantId, tenantDs.getItem().getTenantId())) {
            throw new ValidationException(messages.getMessage(TenantAdminValidator.class, "validation.userBelongsToDifferentTenant"));
        }
    }
}
