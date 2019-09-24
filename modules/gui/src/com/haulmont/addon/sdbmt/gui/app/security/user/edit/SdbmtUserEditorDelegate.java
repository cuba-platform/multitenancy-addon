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

package com.haulmont.addon.sdbmt.gui.app.security.user.edit;

import com.haulmont.cuba.core.app.multitenancy.TenantProvider;
import com.haulmont.cuba.core.entity.HasTenant;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.security.entity.Tenant;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class SdbmtUserEditorDelegate<T extends User & HasTenant> {

    @Inject
    private TenantProvider tenantProvider;

    @Inject
    private DataManager dataManager;

    public void ready(SdbmtUserScreen<T> userScreen) {
        String tenantId = tenantProvider.getTenantId();
        if (PersistenceHelper.isNew(userScreen.getUser())) {
            userScreen.getUser().setTenantId(tenantId);
        }

        if (!tenantId.equals(TenantProvider.TENANT_ADMIN)) {
            userScreen.getTenantField().setVisible(false);
            return; //do not init tenant ID field if the current user is also a tenant
        }

        initTenantField(userScreen.getTenantField(), userScreen.getUser());
    }

    private void initTenantField(OptionsField<Tenant,Tenant> tenantField, T user) {
        List<Tenant> tenants = createOptionList();
        tenantField.setOptionsList(tenants);
        tenantField.setValue(findTenantByTenantId(tenants, user.getTenantId()));

        tenantField.addValueChangeListener(v -> {
            Tenant tenant = v.getValue();
            user.setTenantId(Optional.ofNullable(tenant).map(Tenant::getTenantId).orElse(null));
            if (tenant != null) {
                user.setGroup(tenant.getGroup());
            }
        });
    }

    private List<Tenant> createOptionList() {
        return dataManager.loadList(LoadContext.create(Tenant.class)
                .setQuery(LoadContext.createQuery("select e from sec$Tenant e"))
                .setView("tenant-with-group"));
    }

    private Tenant findTenantByTenantId(List<Tenant> tenants, String tenantId) {
        if (tenantId == null) return null;

        return tenants.stream()
                .filter(t -> Objects.equals(t.getTenantId(), tenantId))
                .findFirst()
                .orElse(null);
    }
}
