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

import com.haulmont.addon.sdbmt.core.tools.MultiTenancyHelperService;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.TenantEntity;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.MapScreenOptions;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;

public class SdbmtUserEditor<T extends User & TenantEntity> extends UserEditor implements SdbmtUserScreen<T> {

    @Inject
    private SdbmtUserEditorDelegate<T> sdbmtUserEditorDelegate;

    @Inject
    private LookupField<Tenant> tenantId;

    @Inject
    protected MultiTenancyHelperService multiTenancyHelperService;

    @Inject
    protected Notifications notifications;

    @Inject
    protected ScreenBuilders screenBuilders;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        SdbmtAddRoleAction addRoleAction = new SdbmtAddRoleAction();
        addRoleAction.setEnabled(security.isEntityOpPermitted(UserRole.class, EntityOp.CREATE));
        rolesTable.addAction(addRoleAction);
        rolesTableAddBtn.setAction(addRoleAction);

        boolean isUserUpdatePermitted = security.isEntityOpPermitted(User.class, EntityOp.UPDATE);
        boolean isUserRoleCreatePermitted = security.isEntityOpPermitted(UserRole.class, EntityOp.CREATE);
        addRoleAction.setEnabled(isUserRoleCreatePermitted && isUserUpdatePermitted);
    }

    @Override
    public void ready() {
        sdbmtUserEditorDelegate.ready(this);
    }

    @Override
    public OptionsField<Tenant, Tenant> getTenantField() {
        return tenantId;
    }

    @Override
    public T getUser() {
        //noinspection unchecked
        return (T) getItem();
    }

    protected class SdbmtAddRoleAction extends AddRoleAction {
        @Override
        public void actionPerform(Component component) {
            AbstractLookup screen = (AbstractLookup) screenBuilders.lookup(Role.class, getFrameOwner())
                    .withOpenMode(OpenMode.THIS_TAB)
                    .withOptions(new MapScreenOptions(ParamsMap.of("windowOpener", "sec$User.edit")))
                    .withSelectHandler(selectedRole -> {
                        Collection<String> existingRoleNames = getExistingRoleNames();
                        rolesDs.suspendListeners();
                        try {
                            for (Role role : selectedRole) {
                                if (!multiTenancyHelperService.isAccessEntity(role) && !role.isPredefined()) {
                                    notifications.create()
                                            .withCaption(getMessage("globalRoleAssign.message"))
                                            .show();
                                    continue;
                                }

                                if (existingRoleNames.contains(role.getName())) {
                                    continue;
                                }

                                MetaClass metaClass = rolesDs.getMetaClass();
                                UserRole userRole = dataSupplier.newInstance(metaClass);
                                userRole.setRole(role);
                                userRole.setUser(userDs.getItem());

                                rolesDs.addItem(userRole);
                                existingRoleNames.add(role.getName());
                            }
                        } finally {
                            rolesDs.resumeListeners();
                        }
                    })
                    .build();
            screen.addAfterCloseListener(actionId -> rolesTable.focus());
            Component lookupComponent = screen.getLookupComponent();
            if (lookupComponent instanceof Table) {
                ((Table<?>) lookupComponent).setMultiSelect(true);
            }
            screen.show();
        }
    }
}
