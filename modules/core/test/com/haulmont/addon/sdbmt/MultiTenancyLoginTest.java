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

import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.addon.sdbmt.core.MultiTenancyTestContainer;
import com.haulmont.addon.sdbmt.core.MultiTenancyUserSessionSource;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.auth.AuthenticationManager;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

public class MultiTenancyLoginTest {

    @ClassRule
    public static MultiTenancyTestContainer cont = MultiTenancyTestContainer.Common.INSTANCE;

    protected TenantConfig tenantConfig = AppBeans.get(Configuration.class).getConfig(TenantConfig.class);

    public static final String PASSWORD = "password";

    private Persistence persistence;
    private Metadata metadata;

    private User userA;
    private User userB;
    private Group groupA;
    private Group groupB;
    private Tenant tenantA;
    private Tenant tenantB;

    private PasswordEncryption passwordEncryption;

    @Before
    public void setUp() {
        passwordEncryption = AppBeans.get(PasswordEncryption.class);
        persistence = cont.persistence();
        metadata = cont.metadata();

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            groupA = metadata.create(Group.class);
            groupA.setName("group-tenant-a");
            groupA.setSysTenantId("tenant-a");
            em.persist(groupA);

            groupB = metadata.create(Group.class);
            groupB.setName("group-tenant-b");
            groupB.setSysTenantId("tenant-b");
            em.persist(groupB);

            userA = metadata.create(User.class);
            userA.setName("User A");
            userA.setLogin("userA");
            userA.setPassword(passwordEncryption.getPasswordHash(userA.getId(), PASSWORD));
            userA.setGroup(groupA);
            userA.setSysTenantId("tenant-a");
            userA.setUserRoles(new ArrayList<>());
            em.persist(userA);

            userB = metadata.create(User.class);
            userB.setName("User B");
            userB.setLogin("userB");
            userB.setPassword(passwordEncryption.getPasswordHash(userB.getId(), PASSWORD));
            userB.setGroup(groupB);
            userB.setSysTenantId("tenant-b");
            userB.setUserRoles(new ArrayList<>());
            em.persist(userB);

            tenantA = metadata.create(Tenant.class);
            tenantA.setName("Tenant A");
            tenantA.setGroup(groupA);
            tenantA.setAdmin(userA);
            tenantA.setTenantId("tenant-a");
            em.persist(tenantA);

            tenantB = metadata.create(Tenant.class);
            tenantB.setName("tenant-b");
            tenantB.setGroup(groupB);
            tenantB.setAdmin(userB);
            tenantB.setTenantId("tenant-b");
            em.persist(tenantB);

            tx.commit();
        }
    }

    @After
    public void tearDown() {
        cont.deleteRecord("CUBASDBMT_TENANT", tenantA.getId(), tenantB.getId());

        for (UserRole userRole : userA.getUserRoles()) {
            cont.deleteRecord("SEC_USER_ROLE", userRole.getId());
        }
        for (UserRole userRole : userB.getUserRoles()) {
            cont.deleteRecord("SEC_USER_ROLE", userRole.getId());
        }

        cont.deleteRecord("SEC_USER", userA.getId(), userB.getId());
        cont.deleteRecord("SEC_GROUP", groupA.getId(), groupB.getId());
    }

    @Test
    public void testAuthenticationWithTenantIdUrlParam() {
        tenantConfig.setLoginByTenantParamEnabled(true);

        AuthenticationManager lw = AppBeans.get(AuthenticationManager.NAME);
        Credentials credentials = new LoginPasswordCredentials("userA", PASSWORD, Locale.ENGLISH);
        try {
            lw.login(credentials);
            fail();
        } catch (LoginException e) {
            assertThat(e.getMessage(), containsString("Unknown login name or bad password 'userA'"));
        }

        tenantConfig.setLoginByTenantParamEnabled(false);
    }

    @Test
    public void testAuthenticationWithoutTenantIdUrlParam() {
        tenantConfig.setLoginByTenantParamEnabled(false);

        AuthenticationManager lw = AppBeans.get(AuthenticationManager.NAME);
        Credentials credentials = new LoginPasswordCredentials("userA", PASSWORD, Locale.ENGLISH);
        UserSession userSession = lw.login(credentials).getSession();

        assertNotNull(userSession);

        tenantConfig.setLoginByTenantParamEnabled(true);
    }

    @Test
    public void testUserLoginWithoutTenant() throws LoginException {
        AuthenticationManager lw = AppBeans.get(AuthenticationManager.NAME);
        Credentials credentials = new LoginPasswordCredentials("admin", "admin", Locale.getDefault());
        UserSession userSession = lw.login(credentials).getSession();

        assertNotNull(userSession);

        DataManager dm = AppBeans.get(DataManager.NAME);
        LoadContext<Group> loadContext = LoadContext.create(Group.class)
                .setQuery(new LoadContext.Query("select g from sec$Group g"));
        List<Group> list = dm.loadList(loadContext);
        assertEquals(3, list.size());
    }

    @Test
    public void testUserLoginWithTenant() throws LoginException {
        AuthenticationManager lw = AppBeans.get(AuthenticationManager.NAME);
        Credentials credentials = new LoginPasswordCredentials("userA", PASSWORD, Locale.getDefault(),
                ParamsMap.of(tenantConfig.getTenantIdUrlParamName(), "tenant-a"));
        UserSession userSession = lw.login(credentials).getSession();

        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((MultiTenancyUserSessionSource) uss).setUserSession(userSession);
        try {
            DataManager dm = AppBeans.get(DataManager.NAME);
            LoadContext<Group> loadContext = LoadContext.create(Group.class)
                    .setQuery(new LoadContext.Query("select g from sec$Group g"));
            List<Group> list = dm.loadList(loadContext);
            assertEquals(1, list.size());

            Group group = list.get(0);
            assertEquals(group, groupA);
        } finally {
            ((MultiTenancyUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

}
