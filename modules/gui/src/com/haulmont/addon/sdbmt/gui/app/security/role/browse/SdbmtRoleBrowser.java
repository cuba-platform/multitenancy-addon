/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.addon.sdbmt.gui.app.security.role.browse;

import com.haulmont.addon.sdbmt.core.tools.MultiTenancyHelperService;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.app.security.role.browse.RoleBrowser;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SdbmtRoleBrowser extends RoleBrowser {

    @Inject
    protected MultiTenancyHelperService multiTenancyHelperService;

    @Inject
    protected Notifications notifications;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        initRemoveAction();
    }

    @Override
    protected void assignRoleUsers(Role role, Collection<User> items) {
        if (!multiTenancyHelperService.isAccessEntity(role) && !role.isPredefined()) {
            notifications.create(Notifications.NotificationType.HUMANIZED)
                    .withCaption(getMessage("globalRoleAssign.message"))
                    .show();
            return;
        }

        super.assignRoleUsers(role, items);
    }

    protected void initRemoveAction() {
        removeRolesAction.setBeforeActionPerformedHandler(() -> {
            Set<Role> selectedRoles = rolesTable.getSelected();
            for (Role role : selectedRoles) {
                if (role.isPredefined()) {
                    notifications.create()
                            .withCaption(getMessage("predefinedRoleDeletion"))
                            .show();
                    return false;
                }
                if (!multiTenancyHelperService.isAccessEntity(role)) {
                    notifications.create()
                            .withCaption(getMessage("globalRoleDeletion.message"))
                            .show();
                    return false;
                }
            }
            return true;
        });
    }
}
