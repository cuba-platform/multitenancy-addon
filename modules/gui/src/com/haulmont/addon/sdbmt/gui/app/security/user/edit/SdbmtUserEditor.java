/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.addon.sdbmt.gui.app.security.user.edit;

import com.haulmont.addon.sdbmt.entity.HasTenant;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;

public class SdbmtUserEditor<T extends User & HasTenant> extends UserEditor implements SdbmtUserScreen<T> {

    @Inject
    private SdbmtUserEditorDelegate<T> sdbmtUserEditorDelegate;

    @Inject
    private LookupField<Tenant> tenantId;

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
}
