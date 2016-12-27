/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.security.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.sdbmt.entity.SdbmtUser;
import com.haulmont.sdbmt.security.SecurityConfig;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("sdbmt_SdbmtUserEntityListener")
public class SdbmtUserEntityListener implements BeforeInsertEntityListener<SdbmtUser> {
    @Inject
    protected SecurityConfig securityConfig;

    @Inject
    protected Metadata metadata;

    @Override
    public void onBeforeInsert(SdbmtUser user, EntityManager entityManager) {
        if (user.getTenantId() != null) {
            Role tenantDefaultRole = securityConfig.getDefaultTenantRole();
            if (tenantDefaultRole == null) {
                return;
            }

            if (!userHasRole(user, tenantDefaultRole)) {
                UserRole userRole = metadata.create(UserRole.class);
                userRole.setUser(user);
                userRole.setRole(tenantDefaultRole);
                entityManager.persist(userRole);
                user.getUserRoles().add(userRole);
            }
        }
    }

    private boolean userHasRole(SdbmtUser user, Role role) {
        if (user.getUserRoles() == null || user.getUserRoles().isEmpty()) {
            return false;
        }
        for (UserRole userRole : user.getUserRoles()) {
            if (role.equals(userRole.getRole())) {
                return true;
            }
        }
        return false;
    }
}
