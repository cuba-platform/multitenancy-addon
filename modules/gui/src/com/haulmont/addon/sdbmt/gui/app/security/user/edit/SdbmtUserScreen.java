/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.gui.app.security.user.edit;

import com.haulmont.addon.sdbmt.entity.HasTenant;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.security.entity.User;

public interface SdbmtUserScreen<T extends User & HasTenant> {

    OptionsField getTenantField();

    T getUser();

}
