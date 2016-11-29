/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import com.haulmont.sdbmt.core.HasTenant;
import com.haulmont.sdbmt.core.TenantId;
import com.haulmont.sdbmt.entity.SdbmtGroup;
import com.haulmont.sdbmt.entity.Tenant;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MtUserSessionManager extends UserSessionManager {

    public static final int PERMISSON_PROHIBIT = 0;
    public static final int PERMISSON_HIDE = 0;

    @Override
    protected void compileSessionAttributes(UserSession session, Group group) {
        super.compileSessionAttributes(session, group);
        setTenantIdAttribute(session, group);
    }

    @Override
    protected void compilePermissions(UserSession session, List<Role> roles) {
        super.compilePermissions(session, roles);
        compileTenantPermissions(session);
    }

    private void compileTenantPermissions(UserSession session) {
        Tenant tenant = findGroupTenant(session.getUser().getGroup());
        if (tenant == null) {
            return;
        }

        createEntityWritePermissions(session);
        createTenantIdFieldPermissions(session);
    }

    private void createTenantIdFieldPermissions(UserSession session) {
        Collection<MetaClass> entitiesWithTenant = getEntitiesWithTenant();
        for (MetaClass e : entitiesWithTenant) {
            addHideTenantIdPermission(session, e);
        }
    }

    private void addHideTenantIdPermission(UserSession session, MetaClass metaClass) {
        for (MetaProperty p : metaClass.getProperties()) {
            if (p.getAnnotatedElement().getAnnotation(TenantId.class) != null) {
                session.addPermission(PermissionType.ENTITY_ATTR,
                        metaClass.getName() + Permission.TARGET_PATH_DELIMETER + p.getName(),
                        null, PERMISSON_HIDE);
            }
        }
    }

    private void createEntityWritePermissions(UserSession session) {
        Collection<MetaClass> readOnlyEntities = getEntitiesWithoutTenant();
        for (MetaClass e : readOnlyEntities) {
            addProhibitEntityCreatePermission(session, e);
            addProhibitEntityUpdatePermission(session, e);
            addProhibitEntityDeletePermission(session, e);
        }
    }

    private Collection<MetaClass> getEntitiesWithTenant() {
        Collection<MetaClass> allEntities = metadata.getClasses();
        return allEntities.stream()
                .filter(this::isEntityWithTenantId)
                .filter(e -> e.getJavaClass().getAnnotation(Entity.class) != null)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Collection<MetaClass> getEntitiesWithoutTenant() {
        Collection<MetaClass> allEntities = metadata.getClasses();
        return allEntities.stream()
                .filter(e -> !isEntityWithTenantId(e))
                .filter(e -> e.getJavaClass().getAnnotation(Entity.class) != null)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private boolean isEntityWithTenantId(MetaClass metaClass) {
        return HasTenant.class.isAssignableFrom(metaClass.getJavaClass());
    }

    protected void addProhibitEntityUpdatePermission(UserSession session, MetaClass metaClass) {
        createEntityOpProhibitPermission(session, metaClass, EntityOp.UPDATE);
    }

    protected void addProhibitEntityCreatePermission(UserSession session, MetaClass metaClass) {
        createEntityOpProhibitPermission(session, metaClass, EntityOp.CREATE);
    }

    protected void addProhibitEntityDeletePermission(UserSession session, MetaClass metaClass) {
        createEntityOpProhibitPermission(session, metaClass, EntityOp.DELETE);
    }

    private void createEntityOpProhibitPermission(UserSession session, MetaClass metaClass, EntityOp entityOp) {
        session.addPermission(PermissionType.ENTITY_OP,
                metaClass.getName() + Permission.TARGET_PATH_DELIMETER + entityOp.getId(),
                null, PERMISSON_PROHIBIT);
    }

    private void setTenantIdAttribute(UserSession session, Group group) {
        Tenant tenant = findGroupTenant(group);
        if (tenant != null) {
            session.setAttribute("tenant_id", tenant.getTenantId());
        }
    }

    private Tenant findGroupTenant(Group group) {
        if (((SdbmtGroup)group).getTenant() != null) {
            return ((SdbmtGroup)group).getTenant();
        }
        EntityManager em = persistence.getEntityManager();
        TypedQuery<Tenant> q = em.createQuery("select t from sec$GroupHierarchy h join h.parent.tenant t " +
                "where h.group.id = ?1", Tenant.class);
        q.setParameter(1, group);
        return q.getFirstResult();
    }
}
