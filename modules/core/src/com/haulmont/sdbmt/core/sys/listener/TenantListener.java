/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.core.sys.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.haulmont.sdbmt.entity.SdbmtGroup;
import com.haulmont.sdbmt.entity.SdbmtUser;
import com.haulmont.sdbmt.entity.Tenant;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("sdbmt_TenantListener")
public class TenantListener implements BeforeUpdateEntityListener<Tenant>, BeforeInsertEntityListener<Tenant> {

    @Override
    public void onBeforeInsert(Tenant tenant, EntityManager entityManager) {
        updateAccessGroupTenantId(tenant, entityManager);
        updateAdminTenantId(tenant);
    }

    @Override
    public void onBeforeUpdate(Tenant tenant, EntityManager entityManager) {
        updateAccessGroupTenantId(tenant, entityManager);
        updateAdminTenantId(tenant);
    }

    private void updateAdminTenantId(Tenant tenant) {
        SdbmtUser admin = tenant.getAdmin();
        if (admin != null && !Objects.equals(admin.getTenantId(), tenant.getTenantId())) {
            admin.setTenantId(tenant.getTenantId());
        }
    }

    private void updateAccessGroupTenantId(Tenant tenant, EntityManager em) {
        if (tenant.getGroup() != null) {
            SdbmtGroup accessGroup = em.reload(tenant.getGroup(), "group-with-tenantId");
            if (accessGroup != null) {
                accessGroup.setTenantId(tenant.getTenantId());
            }
        }
    }
}
