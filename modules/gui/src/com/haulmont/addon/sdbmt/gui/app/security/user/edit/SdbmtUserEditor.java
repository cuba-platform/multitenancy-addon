/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.addon.sdbmt.gui.app.security.user.edit;

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.addon.sdbmt.MultiTenancyTools;
import com.haulmont.addon.sdbmt.entity.HasTenant;
import com.haulmont.addon.sdbmt.entity.Tenant;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SdbmtUserEditor extends UserEditor {

    @Inject
    private CollectionDatasource<Tenant, UUID> tenantsDs;

    @Inject
    private ComponentsFactory componentsFactory;

    @Inject
    private MultiTenancyTools multiTenancyTools;

    @Override
    public void ready() {
        initTenantIdField();
    }

    protected void initTenantIdField() {
        LookupPickerField tenantIdField = componentsFactory.createComponent(LookupPickerField.class);
        tenantIdField.setOptionsDatasource(tenantsDs);
        String userTenantId = ((HasTenant)getItem()).getTenantId();
        if (userTenantId != null) {
            Tenant tenant = tenantsDs.getItems().stream()
                    .filter(e -> userTenantId.equals(e.getTenantId()))
                    .findFirst()
                    .orElse(null);
            tenantIdField.setValue(tenant);
        }
        tenantIdField.addLookupAction();
        tenantIdField.addValueChangeListener(v -> {
            User user = getItem();
            if (v.getValue() != null) {
                Tenant tenant = (Tenant) v.getValue();
                ((HasTenant)user).setTenantId(tenant.getTenantId());
                user.setGroup(tenant.getGroup());
            } else {
                ((HasTenant)user).setTenantId(null);
            }
        });

        fieldGroupRight.getFieldNN("tenantId").setComponent(tenantIdField);

        //do not display tenant ID field if the current user is also a tenant
        String tenantId = multiTenancyTools.getCurrentUserTenantId();
        if (tenantId != null) {
            tenantIdField.setVisible(false);
        }
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);
    }

    protected void addDefaultRoles(User user) {
        List<Role> defaultRoles = loadDefaultRoles();
        List<UserRole> newRoles = new ArrayList<>();
        if (user.getUserRoles() != null) {
            newRoles.addAll(user.getUserRoles());
        }

        MetaClass metaClass = rolesDs.getMetaClass();
        for (Role role : defaultRoles) {
            UserRole userRole = dataSupplier.newInstance(metaClass);
            userRole.setRole(role);
            userRole.setUser(user);
            newRoles.add(userRole);
        }

        user.setUserRoles(newRoles);
    }

    protected List<Role> loadDefaultRoles() {
        LoadContext<Role> ctx = new LoadContext<>(Role.class);

        String tenantId = multiTenancyTools.getCurrentUserTenantId();
        if (Strings.isNullOrEmpty(tenantId)) {
            ctx.setQueryString("select r from sec$Role r where r.defaultRole = true and r.tenantId is null");
        } else {
            ctx.setQueryString("select r from sec$Role r where r.defaultRole = true");
        }

        return dataSupplier.loadList(ctx);
    }
}
