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
