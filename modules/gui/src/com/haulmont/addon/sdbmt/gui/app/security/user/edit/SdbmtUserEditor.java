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

import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.cuba.core.entity.HasTenant;
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
