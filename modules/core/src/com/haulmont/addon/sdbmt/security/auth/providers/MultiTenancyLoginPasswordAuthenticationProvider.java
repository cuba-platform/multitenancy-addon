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
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
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

    protected Map<String, Object> params;

    @Inject
    protected TenantConfig tenantConfig;

    public MultiTenancyLoginPasswordAuthenticationProvider(Persistence persistence, Messages messages) {
        super(persistence, messages);
    }

    @Override
    public AuthenticationDetails authenticate(Credentials credentials) throws LoginException {
        LoginPasswordCredentials loginPasswordCredentials = (LoginPasswordCredentials) credentials;
        params = loginPasswordCredentials.getParams();
        return super.authenticate(credentials);
    }

    @Nullable
    @Override
    protected User loadUser(String login) throws LoginException {
        if (login == null) {
            throw new IllegalArgumentException("Login is null");
        }

        Object tenantId = params.get(tenantConfig.getTenantIdUrlParamName());

        EntityManager em = persistence.getEntityManager();
        String queryStr = "select u from sec$User u where ((?1 is null and u.tenantId is null) or u.tenantId = ?1) and u.loginLowerCase = ?2 and (u.active = true or u.active is null)";

        Query q = em.createQuery(queryStr);
        q.setParameter(1, tenantId);
        q.setParameter(2, login.toLowerCase());

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
}
