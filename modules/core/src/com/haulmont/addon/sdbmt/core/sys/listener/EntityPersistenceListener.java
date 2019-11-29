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

package com.haulmont.addon.sdbmt.core.sys.listener;

import com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider;
import com.haulmont.addon.sdbmt.core.global.TenantEntityOperation;
import com.haulmont.addon.sdbmt.entity.HasTenant;
import com.haulmont.cuba.core.app.events.EntityPersistingEvent;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.TenantEntity;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("cubasdbmt_EntityChangedListener")
public class EntityPersistenceListener {

    @Inject
    protected TenantProvider tenantProvider;

    @Inject
    protected TenantEntityOperation tenantEntityOperation;

    @EventListener
    public void beforePersist(EntityPersistingEvent<Entity> event) {
        Entity entity = event.getEntity();
        String tenantId = tenantProvider.getCurrentUserTenantId();
        if ((entity instanceof HasTenant || entity instanceof TenantEntity) && !tenantId.equals(TenantProvider.NO_TENANT)) {
            tenantEntityOperation.setTenant(entity, tenantId);
        }
    }
}
