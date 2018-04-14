/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.addon.sdbmt.config.TenantConfig;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(MultiTenancyTools.NAME)
public class MultiTenancyTools {

    public static final String NAME = "cubasdbmt_MultiTenancyTools";

    @Inject
    protected UserSessionSource uss;

    @Inject
    protected TenantConfig tenantConfig;

    /**
     * Returns the tenant ID of a logged in user.
     * @return tenant ID of a logged in user, null if the user doesn't have a tenant ID
     * @throws IllegalStateException if there is no active user session
     */
    public String getCurrentUserTenantId() {
        return uss.getUserSession().getAttribute(tenantConfig.getTenantIdName());
    }
}
