/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.core.sys;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import com.haulmont.sdbmt.entity.MtGroup;
import com.haulmont.sdbmt.entity.Tenant;

public class MtUserSessionManager extends UserSessionManager {
    @Override
    protected void compileSessionAttributes(UserSession session, Group group) {
        super.compileSessionAttributes(session, group);
        setTenantIdAttribute(session, group);
    }

    private void setTenantIdAttribute(UserSession session, Group group) {
        Tenant tenant = findGroupTenant(group);
        if (tenant != null) {
            session.setAttribute("tenant_id", tenant.getTenantId());
        }
    }

    private Tenant findGroupTenant(Group group) {
        if (((MtGroup)group).getTenant() != null) {
            return ((MtGroup)group).getTenant();
        }
        EntityManager em = persistence.getEntityManager();
        TypedQuery<Tenant> q = em.createQuery("select t from sec$GroupHierarchy h join h.parent.tenant t " +
                "where h.group.id = ?1", Tenant.class);
        q.setParameter(1, group);
        return q.getFirstResult();
    }
}
