/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.gui.app.security.role.edit.tabs;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.EntityPermissionsFrame;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;

import javax.persistence.Entity;

public class SdbmtEntityPermissionsFrame extends EntityPermissionsFrame {

    private UserSession session = AppBeans.get(UserSessionSource.class).getUserSession();

    protected class SdbmtEntityOperationControl extends EntityOperationControl {

        public SdbmtEntityOperationControl(EntityOp operation, String metaProperty, String operationLabel, String allowChecker, String denyChecker) {
            super(operation, metaProperty, operationLabel, allowChecker, denyChecker);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public boolean applicableToEntity(Class javaClass) {
            MetaClass metaClass = metadata.getClass(javaClass);
            return session.isEntityOpPermitted(metaClass, operation);
        }
    }

    protected void initCheckBoxesControls() {
        operationControls = new SdbmtEntityOperationControl[]{
                new SdbmtEntityOperationControl(EntityOp.CREATE, "createPermissionVariant", "createOpLabel",
                        "createAllowCheck", "createDenyCheck") {
                    @Override
                    public boolean applicableToEntity(Class javaClass) {
                        return javaClass.isAnnotationPresent(Entity.class) && super.applicableToEntity(javaClass);
                    }
                },
                new SdbmtEntityOperationControl(EntityOp.READ, "readPermissionVariant", "readOpLabel",
                        "readAllowCheck", "readDenyCheck"),
                new SdbmtEntityOperationControl(EntityOp.UPDATE, "updatePermissionVariant", "updateOpLabel",
                        "updateAllowCheck", "updateDenyCheck"),
                new SdbmtEntityOperationControl(EntityOp.DELETE, "deletePermissionVariant", "deleteOpLabel",
                        "deleteAllowCheck", "deleteDenyCheck") {
                    @Override
                    public boolean applicableToEntity(Class javaClass) {
                        return javaClass.isAnnotationPresent(Entity.class) && super.applicableToEntity(javaClass);
                    }
                }
        };

        attachAllCheckBoxListener(allAllowCheck, PermissionVariant.ALLOWED);
        attachAllCheckBoxListener(allDenyCheck, PermissionVariant.DISALLOWED);

        for (EntityOperationControl control : operationControls) {
            // Allow checkbox
            attachCheckBoxListener(control.getAllowChecker(), control.getMetaProperty(), control.getOperation(),
                    PermissionVariant.ALLOWED);
            // Deny checkbox
            attachCheckBoxListener(control.getDenyChecker(), control.getMetaProperty(), control.getOperation(),
                    PermissionVariant.DISALLOWED);
        }
    }
}
