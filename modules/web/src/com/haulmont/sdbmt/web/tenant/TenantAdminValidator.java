/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.web.tenant;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.sdbmt.entity.SdbmtUser;
import com.haulmont.sdbmt.entity.Tenant;

import java.util.Objects;

public class TenantAdminValidator implements Field.Validator {

    private Messages messages = AppBeans.get(Messages.class);
    private Datasource<Tenant> tenantDs;

    public TenantAdminValidator(Datasource<Tenant> tenantDs) {
        this.tenantDs = tenantDs;
    }

    @Override
    public void validate(Object value) throws ValidationException {
        if (value == null) {
            return;
        }

        String adminTenantId = ((SdbmtUser) value).getTenantId();
        if (adminTenantId != null && !Objects.equals(adminTenantId, tenantDs.getItem().getTenantId())) {
            throw new ValidationException(messages.getMessage(TenantAdminValidator.class, "validation.userBelongsToDifferentTenant"));
        }
    }
}
