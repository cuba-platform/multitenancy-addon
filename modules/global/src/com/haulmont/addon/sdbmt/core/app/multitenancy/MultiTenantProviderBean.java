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

package com.haulmont.addon.sdbmt.core.app.multitenancy;

import com.haulmont.cuba.core.app.multitenancy.TenantProvider;
import com.haulmont.cuba.core.global.UserSessionSource;

import javax.inject.Inject;

/**
 * The implementation {@link TenantProvider} for Multitenancy.
 */
public class MultiTenantProviderBean implements TenantProvider {

    @Inject
    private UserSessionSource userSessionSource;

    /**
     * Returns the tenant ID of a logged in user.
     *
     * @return tenant ID of a logged in user, 'no_tenant' if the user doesn't have a tenant ID
     */
    @Override
    public String getTenantId() {
        if (userSessionSource.checkCurrentUserSession() && userSessionSource.getUserSession().getAttribute(TENANT_ID_ATTRIBUTE_NAME) != null) {
            return userSessionSource.getUserSession().getAttribute(TENANT_ID_ATTRIBUTE_NAME);
        }
        return NO_TENANT;
    }
}
