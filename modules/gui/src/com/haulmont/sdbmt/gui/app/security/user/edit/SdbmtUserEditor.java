/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.gui.app.security.user.edit;

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class SdbmtUserEditor extends UserEditor {

    @Inject
    private UserSessionSource uss;

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

    private List<Role> loadDefaultRoles() {
        LoadContext<Role> ctx = new LoadContext<>(Role.class);

        String tenantId = uss.getUserSession().getAttribute("tenant_id");
        if (Strings.isNullOrEmpty(tenantId)) {
            ctx.setQueryString("select r from sec$Role r where r.defaultRole = true and r.tenantId is null");
        } else {
            ctx.setQueryString("select r from sec$Role r where r.defaultRole = true");
        }

        return dataSupplier.loadList(ctx);
    }
}
