/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.core.sys.persistence;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.listener.BeforeCommitTransactionListener;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.addon.sdbmt.MultiTenancyTools;
import com.haulmont.addon.sdbmt.entity.HasTenant;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;

@Component("cubasdbmt_MtTransactionListener")
public class MtTransactionListener implements BeforeCommitTransactionListener {

    @Inject
    protected MultiTenancyTools multiTenancyTools;

    @Override
    public void beforeCommit(EntityManager entityManager, Collection<Entity> managedEntities) {
        if (AppContext.getSecurityContext() == null) {
            return;
        }

        String tenantId = multiTenancyTools.getCurrentUserTenantId();
        if (tenantId == null) {
            return;
        }

        managedEntities.stream()
                .filter(e -> e instanceof HasTenant)
                .filter(PersistenceHelper::isNew)
                .map(e -> (HasTenant) e)
                .forEach(e -> e.setTenantId(tenantId));
    }
}
