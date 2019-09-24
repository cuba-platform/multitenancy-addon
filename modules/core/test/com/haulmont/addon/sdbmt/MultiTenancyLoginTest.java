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

import com.haulmont.addon.sdbmt.core.MultiTenancyTestContainer;
import com.haulmont.addon.sdbmt.core.MultiTenancyUserSessionSource;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.auth.AuthenticationManager;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Tenant;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

public class MultiTenancyLoginTest {
    @ClassRule
    public static MultiTenancyTestContainer cont = MultiTenancyTestContainer.Common.INSTANCE;

    public static final String PASSWORD = "password";

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

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            groupA = new Group();
            groupA.setName("group-tenant-a");
            groupA.setTenantId("tenant-a");
            em.persist(groupA);

            groupB = new Group();
            groupB.setName("group-tenant-b");
            groupB.setTenantId("tenant-b");
            em.persist(groupB);

            userA = new User();
            userA.setName("User A");
            userA.setLogin("userA");
            userA.setPassword(passwordEncryption.getPasswordHash(userA.getId(), PASSWORD));
            userA.setGroup(groupA);
            userA.setTenantId("tenant-a");
            em.persist(userA);

            userB = new User();
            userB.setName("User B");
            userB.setLogin("userB");
            userB.setPassword(passwordEncryption.getPasswordHash(userB.getId(), PASSWORD));
            userB.setGroup(groupB);
            userB.setTenantId("tenant-b");
            em.persist(userB);

            tenantA = new Tenant();
            tenantA.setName("Tenant A");
            tenantA.setGroup(groupA);
            tenantA.setAdmin(userA);
            tenantA.setTenantId("tenant-a");
            em.persist(tenantA);

            tenantB = new Tenant();
            tenantB.setName("tenant-b");
            tenantB.setGroup(groupB);
            tenantB.setAdmin(userB);
            tenantB.setTenantId("tenant-b");
            em.persist(tenantB);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() {
        cont.deleteRecord("SEC_TENANT", tenantA.getId(), tenantB.getId());

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
    public void testAuthenticationWithTenant() {
        AuthenticationManager lw = AppBeans.get(AuthenticationManager.NAME);
        Credentials credentials = new LoginPasswordCredentials("userA", PASSWORD, Locale.ENGLISH);
        try {
            lw.login(credentials);
            fail();
        } catch (LoginException e) {
            assertThat(e.getMessage(), containsString("Unknown login name or bad password 'userA'"));
        }
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
        Credentials credentials = new LoginPasswordCredentials("userA", PASSWORD, Locale.getDefault(), "tenant-a");
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
