/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.gui.app.security.role.edit;

import com.haulmont.cuba.gui.app.security.role.edit.RoleEditor;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.security.entity.RoleType;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Arrays;

public class SdbmtRoleEditor extends RoleEditor {

    @Inject
    private UserSession userSession;

    @Inject
    private TabSheet permissionsTabsheet;

    @Inject
    private LookupField typeLookup;

    @Override
    protected void postInit() {
        super.postInit();

        String tenantId = userSession.getAttribute("tenant_id");
        if (tenantId != null) {
            permissionsTabsheet.removeTab("specificPermissionsTab");
            permissionsTabsheet.removeTab("uiPermissionsTab");

            //do not allow creating Super users
            typeLookup.setOptionsList(Arrays.asList(RoleType.STANDARD, RoleType.READONLY, RoleType.DENYING));
        }
    }
}
