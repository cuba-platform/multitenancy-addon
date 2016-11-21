/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.core.sys.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.filter.ParametersHelper;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.haulmont.sdbmt.entity.MtGroup;
import com.haulmont.sdbmt.entity.Tenant;
import org.springframework.stereotype.Component;

@Component("sdbmt_TenantListener")
public class TenantListener implements BeforeUpdateEntityListener<Tenant>, BeforeInsertEntityListener<Tenant> {

    @Override
    public void onBeforeInsert(Tenant entity, EntityManager entityManager) {
        if (PersistenceHelper.isLoaded(entity, "accessGroup") && entity.getAccessGroup() != null) {
            updateAccessGroupTenantId(entity.getAccessGroup(), entity.getTenantId(), entityManager);
        }
    }

    @Override
    public void onBeforeUpdate(Tenant entity, EntityManager entityManager) {
        if (PersistenceHelper.isLoaded(entity, "accessGroup") && entity.getAccessGroup() != null) {
            updateAccessGroupTenantId(entity.getAccessGroup(), entity.getTenantId(), entityManager);
        }
    }

    private void updateAccessGroupTenantId(MtGroup accessGroup, String tenantId, EntityManager em) {
        accessGroup = em.reload(accessGroup, "group-with-tenantId");
        if (accessGroup != null) {
            accessGroup.setTenantId(tenantId);
        }
    }
}
