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

package com.haulmont.addon.sdbmt;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.addon.sdbmt.config.TenantConfig;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @deprecated Use {@link com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider} instead
 */
@Deprecated
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
