/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.gui.app.security.role.edit.tabs;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.app.security.entity.AttributePermissionVariant;
import com.haulmont.cuba.gui.app.security.entity.AttributeTarget;
import com.haulmont.cuba.gui.app.security.entity.MultiplePermissionTarget;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionUiHelper;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.AttributePermissionsFrame;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.GridLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.lang.reflect.Field;

public class SdbmtAttributePermissionsFrame extends AttributePermissionsFrame {

    private UserSession session = AppBeans.get(UserSessionSource.class).getUserSession();
    private Metadata metadata = AppBeans.get(Metadata.class);

    @Inject
    private ComponentsFactory componentsFactory;

    @Override
    protected void compileEditPane(MultiplePermissionTarget item) {
        GridLayout editGrid = componentsFactory.createComponent(GridLayout.class);
        editGrid.setFrame(this);
        editGrid.setId("editGrid");
        editGrid.setWidth("100%");
        editGrid.setColumns(4);
        editGrid.setMargin(true);
        editGrid.setColumnExpandRatio(0, 1f);
        editGrid.setColumnExpandRatio(1, 0);
        editGrid.setColumnExpandRatio(2, 0);
        editGrid.setColumnExpandRatio(3, 0);

        editGrid.setRows(item.getPermissions().size() + 2);

        compileDefaultControls(editGrid);

        initPermissionControls(item, editGrid);

        editGridContainer.add(editGrid);

        applyPermissions(hasPermissionsToModifyPermission);
    }

    private void initPermissionControls(MultiplePermissionTarget item, GridLayout editGrid) {
        int i = 0;

        MetaClass metaClass = metadata.getClass(item.getMetaClassName());
        for (AttributeTarget target : item.getPermissions()) {
            if (!session.isEntityAttrPermitted(metaClass, target.getId(), EntityAttrAccess.VIEW)) {
                continue;
            }

            AttributePermissionControl control = new AttributePermissionControl(item, target.getId());
            int gridRow = i + 2;

            editGrid.add(control.getAttributeLabel(), 0, gridRow);
            editGrid.add(control.getModifyCheckBox(), 1, gridRow);
            editGrid.add(control.getReadOnlyCheckBox(), 2, gridRow);
            editGrid.add(control.getHideCheckBox(), 3, gridRow);

            control.getModifyCheckBox().setAlignment(Alignment.MIDDLE_CENTER);
            control.getReadOnlyCheckBox().setAlignment(Alignment.MIDDLE_CENTER);
            control.getHideCheckBox().setAlignment(Alignment.MIDDLE_CENTER);

            permissionControls.add(control);
            i++;
        }
    }

    @Override
    protected void applyPermissions(boolean editable) {
        allHideCheck.setEditable(editable);
        allModifyCheck.setEditable(editable);
        allReadOnlyCheck.setEditable(editable);

        MultiplePermissionTarget item = attributeTargetsDs.getItem();
        MetaClass metaClass = metadata.getClass(item.getMetaClassName());
        boolean canUpdateEntity = session.isEntityOpPermitted(metaClass, EntityOp.UPDATE);
        for (AttributePermissionControl attributePermissionControl : permissionControls) {
            String attributeName = getAttributeName(attributePermissionControl);

            attributePermissionControl.getHideCheckBox().setEditable(editable);
            attributePermissionControl.getHideCheckBox().setVisible(editable);

            attributePermissionControl.getReadOnlyCheckBox().setEditable(editable);
            attributePermissionControl.getReadOnlyCheckBox().setVisible(editable);

            if (session.isEntityAttrPermitted(metaClass, attributeName, EntityAttrAccess.MODIFY)) {
                attributePermissionControl.getModifyCheckBox().setEditable(canUpdateEntity && editable);
                attributePermissionControl.getModifyCheckBox().setVisible(canUpdateEntity && editable);
            } else {
                attributePermissionControl.getModifyCheckBox().setEditable(false);
                attributePermissionControl.getModifyCheckBox().setVisible(false);
            }
        }
    }

    private String getAttributeName(AttributePermissionControl attributePermissionControl) {
        try {
            Field field = AttributePermissionControl.class.getDeclaredField("attributeName");
            field.setAccessible(true);
            return (String) field.get(attributePermissionControl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
