/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.core.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.MetadataImpl;
import com.haulmont.sdbmt.core.HasTenant;

public class MtMetadataImpl extends MetadataImpl {
    @Override
    protected <T> T __create(Class<T> entityClass) {
        T entity = super.__create(entityClass);
        if (entity != null && HasTenant.class.isAssignableFrom(entityClass)) {
            assignTenantId(entity);
        }
        return entity;
    }

    private <T> void assignTenantId(T entity) {
        String tenantId = AppBeans.get(UserSessionSource.class).getUserSession().getAttribute("tenant_id");
        if (tenantId == null) {
            throw new IllegalStateException("'tenant_id' user session attribute is absent. Log in as a tenant's user.");
        }
        ((HasTenant) entity).setTenantId(tenantId);
    }
}
