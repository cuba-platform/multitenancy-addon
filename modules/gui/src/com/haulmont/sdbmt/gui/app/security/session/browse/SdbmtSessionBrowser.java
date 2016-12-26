/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.gui.app.security.session.browse;

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.app.security.session.browse.SessionBrowser;
import com.haulmont.cuba.gui.app.security.session.browse.UserSessionsDatasource;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.sdbmt.entity.SdbmtUserSessionEntity;

import javax.inject.Inject;
import java.util.Map;

public class SdbmtSessionBrowser extends SessionBrowser {

    @Inject
    protected UserSessionsDatasource sessionsDs;

    @Inject
    private UserSession userSession;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        String tenantId = userSession.getAttribute("tenant_id");
        if (!Strings.isNullOrEmpty(tenantId)) {
            sessionsDs.setSessionFilter(e -> tenantId.equals(((SdbmtUserSessionEntity)e).getTenantId()));
        }
    }
}
