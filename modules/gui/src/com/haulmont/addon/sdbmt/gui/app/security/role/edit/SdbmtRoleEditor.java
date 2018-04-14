/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.addon.sdbmt.gui.app.security.role.edit;

import com.haulmont.cuba.gui.app.security.role.edit.RoleEditor;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.security.entity.RoleType;
import com.haulmont.addon.sdbmt.MultiTenancyTools;

import javax.inject.Inject;
import java.util.Arrays;

public class SdbmtRoleEditor extends RoleEditor {

    @Inject
    protected MultiTenancyTools multiTenancyTools;

    @Inject
    private TabSheet permissionsTabsheet;

    @Inject
    private LookupField typeLookup;

    @Override
    protected void postInit() {
        super.postInit();
        applyTenantChanges();
    }

    protected void applyTenantChanges() {
        String tenantId = multiTenancyTools.getCurrentUserTenantId();
        if (tenantId != null) {
            permissionsTabsheet.getTab("specificPermissionsTab").setVisible(false);
            permissionsTabsheet.getTab("uiPermissionsTab").setVisible(false);

            //do not allow tenants to create Super users
            typeLookup.setOptionsList(Arrays.asList(RoleType.STANDARD, RoleType.READONLY, RoleType.DENYING));
        }
    }
}
