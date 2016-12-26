/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.security.app;

import com.haulmont.cuba.security.app.UserSessions;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.sdbmt.entity.SdbmtUserSessionEntity;

import java.util.ArrayList;
import java.util.Collection;

public class SdbmtUserSessions extends UserSessions {

    @Override
    public Collection<UserSessionEntity> getUserSessionInfo() {
        ArrayList<UserSessionEntity> sessionInfoList = new ArrayList<>();
        for (UserSessionInfo nfo : cache.values()) {
            UserSessionEntity use = createUserSessionEntity(nfo.getSession(), nfo.getSince(), nfo.getLastUsedTs());
            String tenantId = nfo.getSession().getAttribute("tenant_id");
            ((SdbmtUserSessionEntity)use).setTenantId(tenantId);
            sessionInfoList.add(use);
        }
        return sessionInfoList;
    }
}
