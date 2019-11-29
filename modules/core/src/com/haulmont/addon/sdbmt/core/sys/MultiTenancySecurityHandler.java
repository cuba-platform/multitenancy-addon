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

package com.haulmont.addon.sdbmt.core.sys;

import com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider;
import com.haulmont.addon.sdbmt.entity.HasTenant;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
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

    @Inject
    protected Metadata metadata;

    @Inject
    protected Persistence persistence;

    @Inject
    protected DataManager dataManager;

    @PostConstruct
    public void init() {
        AppContext.addListener(this);
    }

    @Override
    public void applicationStarted() {
    }

    @Override
    public void applicationStopped() {
        // do nothing
    }

    public void compileSessionAttributes(UserSession session) {
        Preconditions.checkNotNullArgument(session);
        Preconditions.checkNotNullArgument(session.getUser());

        setTenantIdAttribute(session, session.getCurrentOrSubstitutedUser());
    }

    public void setTenantIdAttribute(UserSession userSession, User user) {
        userSession.setAttribute(TenantProvider.TENANT_ID_ATTRIBUTE_NAME, getTenantIdAttribute(user));
    }

    protected String getTenantIdAttribute(User user) {
        return user.getSysTenantId() == null
                ? TenantProvider.NO_TENANT
                : user.getSysTenantId();
    }

    public void compilePermissions(UserSession session) {
        compileTenantPermissions(session);
    }

    protected void compileTenantPermissions(UserSession session) {
        Tenant tenant = getGroupTenant(session.getUser().getGroup());
        if (tenant == null) {
            return;
        }

        createEntityWritePermissions(session);
    }

    protected void createEntityWritePermissions(UserSession session) {
        Collection<MetaClass> readOnlyEntities = getEntitiesWithoutTenant();
        for (MetaClass e : readOnlyEntities) {
            addProhibitEntityCreatePermission(session, e);
            addProhibitEntityUpdatePermission(session, e);
            addProhibitEntityDeletePermission(session, e);
        }
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

    protected Tenant getGroupTenant(Group group) {
        //to prevent user from having to create a new Group view that includes Tenant
        Group tenantGroup = persistence.createTransaction().execute((Transaction.Callable<Group>) em ->
                em.find(Group.class, group.getId(), "group-tenant-and-hierarchy"));
        if (tenantGroup.getSysTenantId() == null && tenantGroup.getParent() != null) {
            return getGroupTenant(tenantGroup.getParent());
        }
        return dataManager.load(Tenant.class)
                .query("select e from cubasdbmt$Tenant e where e.tenantId = :tenantId")
                .parameter("tenantId", group.getSysTenantId())
                .optional()
                .orElse(null);
    }
}
