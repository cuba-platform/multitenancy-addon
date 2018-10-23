/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.security.app;

import com.haulmont.cuba.security.app.UserSessions;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.addon.sdbmt.entity.HasTenant;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class SdbmtUserSessions extends UserSessions {

    @Inject
    protected TenantConfig tenantConfig;

    @Override
    public Collection<UserSessionEntity> getUserSessionInfo() {
        ArrayList<UserSessionEntity> sessionInfoList = new ArrayList<>();
        for (UserSessionInfo nfo : cache.values()) {
            UserSessionEntity use = createUserSessionEntity(nfo.getSession(), nfo.getSince(), nfo.getLastUsedTs());
            String tenantId = nfo.getSession().getAttribute(tenantConfig.getTenantIdName());
            ((HasTenant) use).setTenantId(tenantId);
            sessionInfoList.add(use);
        }
        return sessionInfoList;
    }

    @Override
    public Stream<UserSessionEntity> getUserSessionEntitiesStream() {
        return getUserSessionInfo().stream();
    }
}
