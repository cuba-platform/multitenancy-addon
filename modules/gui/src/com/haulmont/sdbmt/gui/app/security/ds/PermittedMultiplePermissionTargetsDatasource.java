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
import com.haulmont.cuba.gui.app.security.ds.MultiplePermissionTargetsDatasource;
import com.haulmont.cuba.gui.app.security.entity.MultiplePermissionTarget;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;

public class PermittedMultiplePermissionTargetsDatasource extends MultiplePermissionTargetsDatasource {

    private UserSession session = AppBeans.get(UserSessionSource.class).getUserSession();
    private Metadata metadata = AppBeans.get(Metadata.class);

    @SuppressWarnings("ConstantConditions")
    private Predicate<MultiplePermissionTarget> permittedEntityFilter = target -> {
        MetaClass metaClass = metadata.getClass(target.getMetaClassName());
        return session.isEntityOpPermitted(metaClass, EntityOp.READ)
                || session.isEntityOpPermitted(metaClass, EntityOp.CREATE)
                || session.isEntityOpPermitted(metaClass, EntityOp.DELETE)
                || session.isEntityOpPermitted(metaClass, EntityOp.UPDATE);
    };

    public PermittedMultiplePermissionTargetsDatasource() {
        super();
        setFilter(permittedEntityFilter);
    }

    @Override
    public void setFilter(Predicate<MultiplePermissionTarget> filter) {
        if (filter == null) {
            super.setFilter(permittedEntityFilter);
        } else {
            super.setFilter(t -> filter.apply(t) && permittedEntityFilter.apply(t));
        }
    }
}
