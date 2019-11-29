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

package com.haulmont.addon.sdbmt.web.tenant.validators;

import com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.GroupHierarchy;

import java.util.List;
import java.util.function.Consumer;

public class TenantRootAccessGroupValidator implements Consumer<Group> {

    private Messages messages = AppBeans.get(Messages.class);
    private DataManager dataManager = AppBeans.get(DataManager.class);
    private Tenant tenant;

    public TenantRootAccessGroupValidator(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public void accept(Group value) throws ValidationException {
        if (value == null) {
            return;
        }

        DataManager dm = AppBeans.get(DataManager.class);
        Group group = dm.reload(value, "group-tenant-and-hierarchy");
        Tenant groupTenant = getTenantGroup(group);
        if (group.getSysTenantId() != null && groupTenant != null && !groupTenant.equals(tenant) && !group.getSysTenantId().equals(TenantProvider.NO_TENANT)) {
            throw new ValidationException(messages.getMessage(TenantRootAccessGroupValidator.class, "validation.hasTenant"));
        } else if (isRootGroup(group)) {
            throw new ValidationException(messages.getMessage(TenantRootAccessGroupValidator.class, "validation.rootGroup"));
        } else {
            if (subgroupOfOtherTenantGroup(group)) {
                throw new ValidationException(messages.getMessage(TenantRootAccessGroupValidator.class, "validation.subgroupOfOtherTenantGroup"));
            } else if (hasOtherTenantSubgroups(group)) {
                throw new ValidationException(messages.getMessage(TenantRootAccessGroupValidator.class, "validation.hasOtherTenantSubgroups"));
            }
        }
    }

    private boolean hasOtherTenantSubgroups(Group group) {
        LoadContext<Group> ctx = new LoadContext<>(Group.class);
        ctx.setQueryString("select e.group from sec$GroupHierarchy e where e.parent = :group and e.group.sysTenantId is not null")
                .setParameter("group", group);

        return dataManager.getCount(ctx) > 0;
    }

    private boolean subgroupOfOtherTenantGroup(Group group) {
        List<GroupHierarchy> hierarchyList = group.getHierarchyList();
        for (GroupHierarchy hierarchy : hierarchyList) {
            Tenant groupTenant = getTenantGroup(hierarchy.getParent());

            if (group.getSysTenantId() != null && groupTenant != null && !groupTenant.equals(tenant)) {
                return true;
            }
        }
        return false;
    }

    private Tenant getTenantGroup(Group group) {
        return dataManager.load(Tenant.class)
                .query("select e from cubasdbmt$Tenant e where :tenantId is not null and e.tenantId = :tenantId")
                .parameter("tenantId", group.getSysTenantId())
                .optional()
                .orElse(null);
    }

    private boolean isRootGroup(Group group) {
        return group.getParent() == null;
    }
}
