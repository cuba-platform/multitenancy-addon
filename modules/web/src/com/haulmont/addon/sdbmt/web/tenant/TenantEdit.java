/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.addon.sdbmt.web.tenant;

import com.google.common.base.Strings;
import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.addon.sdbmt.entity.TenantUser;
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
    private PickerField<TenantUser> adminField;

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

        nameField.addValueChangeListener(e -> {
            if (Strings.isNullOrEmpty(tenantIdField.getValue())) {
                tenantIdField.setValue(generateTenantId(e.getValue()));
            }
        });

        //allow creating users from lookup screen for convenience
        adminLookupAction.setLookupScreen("sec$User.browse");
        //TODO: allow creating groups from lookup screen for convenience
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
}