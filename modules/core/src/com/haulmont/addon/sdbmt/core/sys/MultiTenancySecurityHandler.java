/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.core.sys;

import com.google.common.base.Strings;
import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.addon.sdbmt.core.TenantId;
import com.haulmont.addon.sdbmt.entity.HasTenant;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.Entity;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Component("cubasdbmt_MultiTenancySecurityHandler")
public class MultiTenancySecurityHandler implements AppContext.Listener {

    public static final int PERMISSON_PROHIBIT = 0;
    public static final int PERMISSON_HIDE = 0;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Persistence persistence;

    @Inject
    protected TenantConfig tenantConfig;

    @PostConstruct
    public void init() {
        AppContext.addListener(this);
    }

    @Override
    public void applicationStarted() {
        //mark tenantId properties with System Level annotation to prevent security subsystem to override values
//        metadata.getClasses().stream()
//                .filter(e -> e instanceof HasTenant)
//                .forEach(metaClass -> metaClass.getPropertyNN("tenantId").getAnnotations()
//                        .put(MetadataTools.SYSTEM_ANN_NAME, true));
    }

    @Override
    public void applicationStopped() {
        // do nothing
    }

    public void compileSessionAttributes(UserSession session) {
        Preconditions.checkNotNullArgument(session);
        Preconditions.checkNotNullArgument(session.getUser());
        Preconditions.checkNotNullArgument(session.getUser().getGroup());

        setTenantIdAttribute(session, session.getUser().getGroup());
    }

    public void compileConstraints(UserSession session) {
        Preconditions.checkNotNullArgument(session);
        Preconditions.checkNotNullArgument(session.getUser());
        Preconditions.checkNotNullArgument(session.getUser().getGroup());

        compileTenantConstraints(session, session.getUser().getGroup());
    }

    public void compilePermissions(UserSession session) {
        compileTenantPermissions(session);
    }

    protected void compileTenantConstraints(UserSession session, Group group) {
        Tenant tenant = findGroupTenant(session.getUser().getGroup());
        if (tenant == null) {
            return;
        }
        String tenantId = tenant.getTenantId();
        if (!validTenantId(tenantId)) {
            throw new IllegalStateException("Tenant id is not valid: " + tenantId);
        }

        Collection<MetaClass> entitiesWithTenant = getEntitiesWithTenant();
        for (MetaClass e : entitiesWithTenant) {
            addTenantIdConstraint(session, group, tenantId, e);
        }
    }

    protected void compileTenantPermissions(UserSession session) {
        if (!groupHasTenant(session.getUser().getGroup())) {
            return;
        }

        createEntityWritePermissions(session);
        createTenantIdPermissions(session);
    }

    protected boolean groupHasTenant(Group group) {
        return findGroupTenant(group) != null;
    }

    protected boolean validTenantId(String tenantId) {
        //only word characters (0-9a-zA-z_) and whitespaces are allowed
        return !Strings.isNullOrEmpty(tenantId) && tenantId.matches("[\\w|\\s]+");
    }

    protected void createTenantIdPermissions(UserSession session) {
        Collection<MetaClass> entitiesWithTenant = getEntitiesWithTenant();
        for (MetaClass e : entitiesWithTenant) {
            addHideTenantIdPermission(session, e);
        }
    }

    protected void addTenantIdConstraint(UserSession session, Group group, String tenantId, MetaClass entityMetaClass) {
        Preconditions.checkNotNullArgument(tenantId);

        entityMetaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(entityMetaClass);

        Constraint c = metadata.create(Constraint.class);
        c.setCheckType(ConstraintCheckType.DATABASE);
        c.setEntityName(entityMetaClass.getName());
        c.setIsActive(true);
        c.setOperationType(ConstraintOperationType.ALL);
        c.setGroup(group);

        //unsafe, but we have already validated tenant id
        c.setWhereClause("{E}.tenantId = '" + tenantId + "'");

        session.addConstraint(c);
    }

    protected void addHideTenantIdPermission(UserSession session, MetaClass metaClass) {
        for (MetaProperty p : metaClass.getProperties()) {
            if (p.getAnnotatedElement().getAnnotation(TenantId.class) != null) {
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                if (originalMetaClass != null) {
                    metaClass = originalMetaClass;
                }
                session.addPermission(PermissionType.ENTITY_ATTR,
                        metaClass.getName() + Permission.TARGET_PATH_DELIMETER + p.getName(),
                        null, PERMISSON_HIDE);
            }
        }
    }

    protected String getExtendedEntityName(MetaClass metaClass) {
        Class extendedClass = metadata.getExtendedEntities().getExtendedClass(metaClass);
        if (extendedClass != null) {
            MetaClass extMetaClass = metadata.getClassNN(extendedClass);
            return extMetaClass.getName();
        }
        return metaClass.getName();
    }

    protected void createEntityWritePermissions(UserSession session) {
        Collection<MetaClass> readOnlyEntities = getEntitiesWithoutTenant();
        for (MetaClass e : readOnlyEntities) {
            addProhibitEntityCreatePermission(session, e);
            addProhibitEntityUpdatePermission(session, e);
            addProhibitEntityDeletePermission(session, e);
        }
    }

    protected Collection<MetaClass> getEntitiesWithTenant() {
        Collection<MetaClass> allEntities = metadata.getClasses();
        return allEntities.stream()
                .filter(this::isEntityWithTenantId)
                .filter(e -> e.getJavaClass().getAnnotation(Entity.class) != null)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    protected Collection<MetaClass> getEntitiesWithoutTenant() {
        Collection<MetaClass> allEntities = metadata.getClasses();
        return allEntities.stream()
                .filter(e -> !isEntityWithTenantId(e))
                .filter(e -> e.getJavaClass().getAnnotation(Entity.class) != null)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    protected boolean isEntityWithTenantId(MetaClass metaClass) {
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

    protected void createEntityOpProhibitPermission(UserSession session, MetaClass metaClass, EntityOp entityOp) {
        session.addPermission(PermissionType.ENTITY_OP,
                metaClass.getName() + Permission.TARGET_PATH_DELIMETER + entityOp.getId(),
                null, PERMISSON_PROHIBIT);
    }

    protected void setTenantIdAttribute(UserSession session, Group group) {
        Tenant tenant = findGroupTenant(group);
        if (tenant != null) {
            session.setAttribute(tenantConfig.getTenantIdName(), tenant.getTenantId());
        }
    }

    protected Tenant findGroupTenant(Group group) {
        Tenant tenant = getGroupTenant(group);
        if (tenant != null) {
            return tenant;
        }

        return persistence.callInTransaction(em -> {
            TypedQuery<Tenant> q = em.createQuery("select t from sec$GroupHierarchy h join h.parent.tenant t " +
                    "where h.group = ?1", Tenant.class);
            q.setParameter(1, group);
            return q.getFirstResult();
        });
    }

    protected Tenant getGroupTenant(Group group) {
        //to prevent user from having to create a new Group view that includes Tenant
        return persistence.callInTransaction(em ->
                em.createQuery("select group.tenant from sec$Group group where group = ?1", Tenant.class)
                .setParameter(1, group)
                .setViewName(View.LOCAL)
                .getFirstResult());
    }
}
