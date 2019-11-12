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
package com.haulmont.addon.sdbmt.gui.app.security.role.edit;

import com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider;
import com.haulmont.cuba.gui.app.security.role.edit.RoleEditor;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.security.entity.RoleType;

import javax.inject.Inject;
import java.util.Arrays;

public class SdbmtRoleEditor extends RoleEditor {

    public static final String SPECIFIC_PERMISSIONS_TAB = "specificPermissionsTab";
    public static final String UI_PERMISSIONS_TAB = "uiPermissionsTab";

    @Inject
    protected TenantProvider tenantProvider;

    @Inject
    private TabSheet permissionsTabsheet;

    @Inject
    private LookupField<RoleType> typeLookup;

    @Override
    protected void postInit() {
        super.postInit();
        applyTenantChanges();
    }

    protected void applyTenantChanges() {
        String tenantId = tenantProvider.getTenantId();
        if (tenantId != null) {
            permissionsTabsheet.getTab(SPECIFIC_PERMISSIONS_TAB).setVisible(false);
            permissionsTabsheet.getTab(UI_PERMISSIONS_TAB).setVisible(false);

            //do not allow tenants to create Super users
            typeLookup.setOptionsList(Arrays.asList(RoleType.STANDARD, RoleType.READONLY, RoleType.DENYING));
        }
    }
}
