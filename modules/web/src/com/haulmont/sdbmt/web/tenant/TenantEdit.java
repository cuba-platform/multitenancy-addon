/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.web.tenant;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.sdbmt.entity.Tenant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

public class TenantEdit extends AbstractEditor<Tenant> {

    @Inject
    private Datasource<Tenant> tenantDs;
    @Named("fieldGroup.group")
    private PickerField groupField;
    @Named("fieldGroup.admin")
    private PickerField adminField;
    @Named("fieldGroup.tenantId")
    private TextField tenantIdField;

    @Override
    protected void postInit() {
        groupField.addValidator(new TenantRootAccessGroupValidator(getItem()));
        adminField.addValidator(new TenantAdminValidator(tenantDs));
        if (getItem().getTenantId() != null) {
            tenantIdField.setEditable(false);
        }
    }
}