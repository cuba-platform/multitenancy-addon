/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.gui.app.security.ds;

import com.google.common.base.Predicate;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.app.security.ds.EntityPermissionTargetsDatasource;
import com.haulmont.cuba.gui.app.security.entity.OperationPermissionTarget;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;

public class PermittedEntityPermissionTargetsDatasource extends EntityPermissionTargetsDatasource {

    private UserSession session = AppBeans.get(UserSessionSource.class).getUserSession();
    private Metadata metadata = AppBeans.get(Metadata.class);

    @SuppressWarnings("ConstantConditions")
    private Predicate<OperationPermissionTarget> permittedEntityFilter = target -> {
        MetaClass metaClass = metadata.getClass(target.getMetaClassName());
        return session.isEntityOpPermitted(metaClass, EntityOp.READ)
                || session.isEntityOpPermitted(metaClass, EntityOp.CREATE)
                || session.isEntityOpPermitted(metaClass, EntityOp.DELETE)
                || session.isEntityOpPermitted(metaClass, EntityOp.UPDATE);
    };

    public PermittedEntityPermissionTargetsDatasource() {
        super();
        permissionsFilter = permittedEntityFilter;
    }

    @Override
    public void setFilter(Predicate<OperationPermissionTarget> filter) {
        if (filter == null) {
            this.permissionsFilter = permittedEntityFilter;
        } else {
            this.permissionsFilter = t -> filter.apply(t) && permittedEntityFilter.apply(t);
        }
    }
}
