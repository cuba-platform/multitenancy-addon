/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.web.tenant.validators;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.GroupHierarchy;
import com.haulmont.addon.sdbmt.entity.HasTenantInstance;
import com.haulmont.addon.sdbmt.entity.Tenant;

import java.util.List;

public class TenantRootAccessGroupValidator implements Field.Validator {

    private Messages messages = AppBeans.get(Messages.class);
    private DataManager dataManager = AppBeans.get(DataManager.class);
    private Tenant tenant;

    public TenantRootAccessGroupValidator(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public void validate(Object value) throws ValidationException {
        if (value == null) {
            return;
        }

        DataManager dm = AppBeans.get(DataManager.class);
        Group group = dm.reload((Group)value, "group-tenant-and-hierarchy");
        Tenant groupTenant = ((HasTenantInstance) group).getTenant();
        if (groupTenant != null && !groupTenant.equals(tenant)) {
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
        ctx.setQueryString("select e.group from sec$GroupHierarchy e where e.parent = :group and e.group.tenant is not null")
                .setParameter("group", group);

        return dataManager.getCount(ctx) > 0;
    }

    private boolean subgroupOfOtherTenantGroup(Group group) {
        List<GroupHierarchy> hierarchyList = group.getHierarchyList();
        for (GroupHierarchy hierarchy : hierarchyList) {
            Tenant groupTenant = ((HasTenantInstance)hierarchy.getParent()).getTenant();
            if (groupTenant != null && !groupTenant.equals(tenant)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRootGroup(Group group) {
        return group.getParent() == null;
    }
}
