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
package com.haulmont.addon.sdbmt.web.tenant;

import com.google.common.base.Strings;
import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.addon.sdbmt.web.tenant.validators.TenantAdminValidator;
import com.haulmont.addon.sdbmt.web.tenant.validators.TenantRootAccessGroupValidator;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class TenantEdit extends AbstractEditor<Tenant> {

    protected static final int TENANT_ID_MAX_LENGTH = 255;

    @Inject
    private Datasource<Tenant> tenantDs;

    @Inject
    private TextField<String> nameField;

    @Inject
    private PickerField<Group> groupField;

    @Inject
    private PickerField<User> adminField;

    @Inject
    private TextField<String> tenantIdField;

    @Named("adminField.lookup")
    private PickerField.LookupAction adminLookupAction;

    @Inject
    private Metadata metadata;

    @Inject
    private TenantConfig tenantConfig;

    @Inject
    private DataManager dataManager;

    @Inject
    private Notifications notifications;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        //allow creating users from lookup screen for convenience
        adminLookupAction.setLookupScreen("sec$User.browse");
        //TODO: allow creating groups from lookup screen for convenience
    }

    @Override
    protected void initNewItem(Tenant item) {
        super.initNewItem(item);

        nameField.addValueChangeListener(e -> {
            if (Strings.isNullOrEmpty(tenantIdField.getValue())) {
                tenantIdField.setValue(generateTenantId(e.getValue()));
            }
        });

    }

    private String generateTenantId(String tenantName) {
        String tenantId = Strings.nullToEmpty(tenantName)
                .toLowerCase()
                .replaceAll("\\s+", "_");
        return StringUtils.left(tenantId, TENANT_ID_MAX_LENGTH);
    }

    @Override
    protected void postInit() {
        groupField.addValidator(new TenantRootAccessGroupValidator(getItem()));
        adminField.addValidator(new TenantAdminValidator(tenantDs));

        if (getItem().getTenantId() != null) {
            tenantIdField.setEditable(false);
        }
    }

    public void onCreateTenantRootGroup() {
        String groupName = nameField.getValue();
        if (Strings.isNullOrEmpty(groupName)) {
            notifications.create(Notifications.NotificationType.WARNING)
                    .withCaption(getMessage("validation.cannotGenerateGroupNameIsNull"))
                    .show();
            return;
        }

        Group tenantParentGroup = tenantConfig.getDefaultTenantParentGroup();
        if (tenantParentGroup == null) {
            throw new RuntimeException("Tenants default parent group doesn't exist");
        }

        if (tenantGroupExist(groupName, tenantParentGroup)) {
            notifications.create(Notifications.NotificationType.WARNING)
                    .withCaption(formatMessage("validation.tenantGroupAlreadyExist", groupName))
                    .show();
            return;
        }

        Group group = metadata.create(Group.class);
        group.setParent(tenantParentGroup);
        group.setName(groupName);

        groupField.setValue(dataManager.commit(group));
    }

    private boolean tenantGroupExist(String groupName, Group tenantsParentGroup) {
        LoadContext<Group> ctx = new LoadContext<>(Group.class);
        ctx.setQueryString("select e from sec$Group e where e.parent = :parent and e.name = :name")
                .setParameter("parent", tenantsParentGroup)
                .setParameter("name", groupName);
        return dataManager.getCount(ctx) > 0;
    }

    @Override
    protected boolean preCommit() {
        String tenantId = tenantDs.getItem().getTenantId();

        User admin = tenantDs.getItem().getAdmin();
        Group group = tenantDs.getItem().getGroup();

        admin.setTenantId(tenantId);
        group.setTenantId(tenantId);

        dataManager.commit(admin, group);
        return true;
    }
}