/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.core;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;

import javax.annotation.PostConstruct;

public interface HasTenant {
    String getTenantId();
    void setTenantId(String tenantId);

    @PostConstruct
    default void initTenantId() {
        String tenantId = AppBeans.get(UserSessionSource.class).getUserSession().getAttribute("tenant_id");
        setTenantId(tenantId);
    }
}
