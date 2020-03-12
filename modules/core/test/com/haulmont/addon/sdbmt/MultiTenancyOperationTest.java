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
import com.haulmont.addon.sdbmt.core.global.TenantEntityOperation;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

public class MultiTenancyOperationTest {

    public static final String SYS_TENANT_ID = "sysTenantId";
    public static final String TENANT_ID = "tenantId";

    @ClassRule
    public static MultiTenancyTestContainer cont = MultiTenancyTestContainer.Common.INSTANCE;

    public static final String PASSWORD = "password";

    private TenantEntityOperation tenantEntityOperation;
    private PasswordEncryption passwordEncryption;

    private Persistence persistence;
    private Metadata metadata;

    private User user;
    private Group group;
    private Tenant tenant;

    @Before
    public void setUp() {
        tenantEntityOperation = AppBeans.get(TenantEntityOperation.class);
        passwordEncryption = AppBeans.get(PasswordEncryption.class);

        persistence = cont.persistence();
        metadata = cont.metadata();

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            group = metadata.create(Group.class);
            group.setName("group-tenant");
            group.setSysTenantId("tenant");
            em.persist(group);

            user = metadata.create(User.class);
            user.setName("User");
            user.setLogin("user");
            user.setPassword(passwordEncryption.getPasswordHash(user.getId(), PASSWORD));
            user.setGroup(group);
            user.setSysTenantId("tenant");
            user.setUserRoles(new ArrayList<>());
            em.persist(user);

            tenant = metadata.create(Tenant.class);
            tenant.setName("tenant");
            tenant.setGroup(group);
            tenant.setAdmin(user);
            tenant.setTenantId("tenant");
            em.persist(tenant);

            tx.commit();
        }
    }

    @After
    public void tearDown() {
        cont.deleteRecord("CUBASDBMT_TENANT", tenant.getId());

        for (UserRole userRole : user.getUserRoles()) {
            cont.deleteRecord("SEC_USER_ROLE", userRole.getId());
        }

        cont.deleteRecord("SEC_USER", user.getId());
        cont.deleteRecord("SEC_GROUP", group.getId());
    }

    @Test
    public void getUserMetaPropertyTest() {
        MetaProperty metaProperty = tenantEntityOperation.getTenantMetaProperty(User.class);

        Assertions.assertEquals(metaProperty.getName(), SYS_TENANT_ID);
    }

    @Test
    public void getUserSessionEntityMetaPropertyTest() {
        MetaProperty metaProperty = tenantEntityOperation.getTenantMetaProperty(UserSessionEntity.class);

        Assertions.assertEquals(metaProperty.getName(), SYS_TENANT_ID);
    }

    @Test
    public void getTenantMetaPropertyTest() {
        MetaProperty metaProperty = tenantEntityOperation.getTenantMetaProperty(Tenant.class);

        Assertions.assertEquals(metaProperty.getName(), TENANT_ID);
    }

    @Test
    public void setTenantMetaPropertyTest() {
        tenantEntityOperation.setTenant(user, "no_tenant");

        Assertions.assertEquals(user.getSysTenantId(), "no_tenant");
    }

    @Test
    public void getTenantTest() {
        Tenant tenant = tenantEntityOperation.getTenant(user);

        Assertions.assertNotNull(tenant);
        Assertions.assertEquals(tenant.getTenantId(), "tenant");
    }

    @Test
    public void getTenantIdTest() {
        String tenantId = tenantEntityOperation.getTenantId(user);

        Assertions.assertEquals(tenantId, "tenant");
    }
}
