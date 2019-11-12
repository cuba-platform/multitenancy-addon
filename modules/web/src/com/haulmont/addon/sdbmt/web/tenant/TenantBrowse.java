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
package com.haulmont.addon.sdbmt.web.tenant;

import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.screen.Subscribe;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TenantBrowse extends AbstractLookup {

    @Inject
    private Table<Tenant> tenantsTable;

    @Inject
    protected TenantConfig tenantConfig;

    @Named("tenantsTable.remove")
    protected RemoveAction removeAction;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        initEnabledRules();
    }

    protected void initEnabledRules(){
        removeAction.addEnabledRule(() -> {
            Set<Tenant> selected = tenantsTable.getSelected();
            if (selected.isEmpty())
                return false;

            List<String> tenants = selected.stream()
                    .map(Tenant::getTenantId)
                    .collect(Collectors.toList());

            return isTenantsRemovingAllowed(tenants);
        });
    }

    protected boolean isTenantsRemovingAllowed(Collection<String> tenants) {
        return !tenants.contains(TenantProvider.NO_TENANT);
    }
}