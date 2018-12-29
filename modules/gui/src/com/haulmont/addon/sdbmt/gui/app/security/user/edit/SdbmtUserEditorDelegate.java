/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.gui.app.security.user.edit;

import com.haulmont.addon.sdbmt.MultiTenancyTools;
import com.haulmont.addon.sdbmt.entity.HasTenant;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class SdbmtUserEditorDelegate<T extends User & HasTenant> {

    @Inject
    private MultiTenancyTools multiTenancyTools;

    @Inject
    private DataManager dataManager;

    public void ready(SdbmtUserScreen<T> userScreen) {
        String tenantId = multiTenancyTools.getCurrentUserTenantId();
        if (PersistenceHelper.isNew(userScreen.getUser())) {
            userScreen.getUser().setTenantId(tenantId);
        }

        if (tenantId != null) {
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
                .setQuery(LoadContext.createQuery("select e from cubasdbmt$Tenant e"))
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
