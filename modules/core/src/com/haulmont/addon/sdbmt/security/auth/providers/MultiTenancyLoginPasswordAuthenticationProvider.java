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

package com.haulmont.addon.sdbmt.security.auth.providers;

import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.auth.providers.LoginPasswordAuthenticationProvider;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class MultiTenancyLoginPasswordAuthenticationProvider extends LoginPasswordAuthenticationProvider {

    private final Logger log = LoggerFactory.getLogger(MultiTenancyLoginPasswordAuthenticationProvider.class);

    @Inject
    protected TenantConfig tenantConfig;

    @Inject
    public MultiTenancyLoginPasswordAuthenticationProvider(Persistence persistence, Messages messages) {
        super(persistence, messages);
    }

    @Nullable
    @Override
    protected User loadUser(String login, Map<String, Object> params) throws LoginException {
        if (login == null) {
            throw new IllegalArgumentException("Login is null");
        }

        Query q = createQuery(login, params);

        List list = q.getResultList();
        if (list.isEmpty()) {
            log.debug("Unable to find user: {}", login);
            return null;
        } else {
            //noinspection UnnecessaryLocalVariable
            User user = (User) list.get(0);
            return user;
        }
    }

    private Query createQuery(String login, Map<String, Object> params) {
        EntityManager em = persistence.getEntityManager();

        Query q;
        if (tenantConfig.getLoginByTenantParamEnabled()) {
            Object tenantId = params.get(tenantConfig.getTenantIdUrlParamName());
            String queryStr = "select u from sec$User u where ((:tenantId is null and u.sysTenantId is null) or u.sysTenantId = :tenantId) " +
                    "and u.loginLowerCase = :login and (u.active = true or u.active is null)";

            q = em.createQuery(queryStr);
            q.setParameter("tenantId", tenantId);
            q.setParameter("login", login.toLowerCase());
        } else {
            String queryStr = "select u from sec$User u where u.loginLowerCase = :login and (u.active = true or u.active is null)";

            q = em.createQuery(queryStr);
            q.setParameter("login", login.toLowerCase());
        }
        return q;
    }
}
