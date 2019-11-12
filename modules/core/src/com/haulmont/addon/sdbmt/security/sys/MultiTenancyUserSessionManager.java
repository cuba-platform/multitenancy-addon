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

package com.haulmont.addon.sdbmt.security.sys;

import com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;

import java.util.Locale;
import java.util.UUID;

public class MultiTenancyUserSessionManager extends UserSessionManager {

    @Override
    public UserSession createSession(UserSession src, User user) {
        UserSession userSession = super.createSession(src, user);
        userSession.setAttribute(TenantProvider.TENANT_ID_ATTRIBUTE_NAME, getTenantIdAttribute(user));
        return userSession;
    }

    @Override
    public UserSession createSession(UUID sessionId, User user, Locale locale, boolean system) {
        UserSession userSession = super.createSession(sessionId, user, locale, system);
        userSession.setAttribute(TenantProvider.TENANT_ID_ATTRIBUTE_NAME, getTenantIdAttribute(user));
        return userSession;
    }

    protected String getTenantIdAttribute(User user) {
        return user.getTenantId() == null
                ? TenantProvider.NO_TENANT
                : user.getTenantId();
    }
}
