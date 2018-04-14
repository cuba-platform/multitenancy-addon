/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.addon.sdbmt.gui.app.security.session.browse;

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.app.security.session.browse.SessionBrowser;
import com.haulmont.cuba.gui.app.security.session.browse.UserSessionsDatasource;
import com.haulmont.addon.sdbmt.MultiTenancyTools;
import com.haulmont.addon.sdbmt.entity.HasTenant;

import javax.inject.Inject;
import java.util.Map;

public class SdbmtSessionBrowser extends SessionBrowser {

    @Inject
    protected UserSessionsDatasource sessionsDs;

    @Inject
    protected MultiTenancyTools multiTenancyTools;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        applyTenantChanges();
    }

    protected void applyTenantChanges() {
        String tenantId = multiTenancyTools.getCurrentUserTenantId();
        if (!Strings.isNullOrEmpty(tenantId)) {
            sessionsDs.setSessionFilter(e -> tenantId.equals(((HasTenant)e).getTenantId()));
        }
    }
}
