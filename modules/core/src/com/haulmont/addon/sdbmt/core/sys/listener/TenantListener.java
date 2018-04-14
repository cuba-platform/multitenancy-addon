/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.core.sys.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.addon.sdbmt.entity.HasTenant;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.addon.sdbmt.config.TenantConfig;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("cubasdbmt_TenantListener")
public class TenantListener implements BeforeUpdateEntityListener<Tenant>, BeforeInsertEntityListener<Tenant> {

    @Inject
    protected TenantConfig tenantConfig;

    @Inject
    protected Metadata metadata;

    @Override
    public void onBeforeInsert(Tenant tenant, EntityManager entityManager) {
        updateAccessGroupTenantId(tenant, entityManager);
        updateTenantAdmin(tenant, entityManager);
    }

    @Override
    public void onBeforeUpdate(Tenant tenant, EntityManager entityManager) {
        updateAccessGroupTenantId(tenant, entityManager);
        updateTenantAdmin(tenant, entityManager);
    }

    private void updateTenantAdmin(Tenant tenant, EntityManager em) {
        User admin = tenant.getAdmin();
        if (admin != null) {
            admin = em.reloadNN(admin, "user.edit");
            ((HasTenant)admin).setTenantId(tenant.getTenantId());
            assignDefaultTenantRole(admin, em);
            tenant.setAdmin(admin);
        }
    }

    private void assignDefaultTenantRole(User user, EntityManager em) {
        Role tenantDefaultRole = tenantConfig.getDefaultTenantRole();
        if (tenantDefaultRole == null) {
            throw new RuntimeException("Default tenant role not found");
        }

        if (!userHasRole(user, tenantDefaultRole)) {
            UserRole userRole = metadata.create(UserRole.class);
            userRole.setUser(user);
            userRole.setRole(tenantDefaultRole);
            em.persist(userRole);
            user.getUserRoles().add(userRole);
        }
    }

    private void updateAccessGroupTenantId(Tenant tenant, EntityManager em) {
        if (tenant.getGroup() != null) {
            Group accessGroup = em.reload(tenant.getGroup(), "group-with-tenantId");
            if (accessGroup != null) {
                ((HasTenant)accessGroup).setTenantId(tenant.getTenantId());
            }
        }
    }

    private boolean userHasRole(User user, Role role) {
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
