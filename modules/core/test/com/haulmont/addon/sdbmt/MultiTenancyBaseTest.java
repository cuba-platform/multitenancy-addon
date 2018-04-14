/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt;

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
        uss.getUserSession().setAttribute("tenant_id", "tenant1");
    }
}
