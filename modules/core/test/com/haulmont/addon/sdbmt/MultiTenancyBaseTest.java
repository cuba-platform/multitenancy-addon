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

import com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public abstract class MultiTenancyBaseTest {

    public static final String NOT_A_TENANT = "Not a tenant";
    public static final String TENANT_USER = "Tenant user";

    @Parameterized.Parameter
    public String tenantId;

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<?> data() {
        return Arrays.asList(TENANT_USER, NOT_A_TENANT);
    }

    @Before
    public void setupTenantId() {
        if (NOT_A_TENANT.equals(tenantId) || tenantId == null) {
            return;
        }

        UserSessionSource uss = AppBeans.get(UserSessionSource.NAME);
        //actual id value doesn't matter
        uss.getUserSession().setAttribute(TenantProvider.TENANT_ID_ATTRIBUTE_NAME, "tenant1");
    }
}
