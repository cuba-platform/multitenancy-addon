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

package com.haulmont.addon.sdbmt.core.tools;

import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.addon.sdbmt.core.global.TenantEntityOperation;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UserSessionSource;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(MultiTenancyHelperService.NAME)
public class MultiTenancyHelperServiceBean implements MultiTenancyHelperService {

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected TenantConfig tenantConfig;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected TenantEntityOperation tenantEntityOperation;

    @Override
    public boolean isSystemLogin(String login) {
        if (login == null) return false;

        return login.equals(tenantConfig.getAdminUser().getLogin())
                || login.equals(tenantConfig.getAnonymousUser().getLogin())
                || login.equals(serverConfig.getAnonymousLogin());
    }

    @Override
    public boolean isAccessEntity(Entity entity) {
        return tenantEntityOperation.getTenant(entity) == null
                && userSessionSource.getUserSession().getUser().getSysTenantId() == null;
    }
}
