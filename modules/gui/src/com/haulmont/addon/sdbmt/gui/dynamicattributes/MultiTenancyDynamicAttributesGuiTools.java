/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.gui.dynamicattributes;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.addon.sdbmt.MultiTenancyTools;
import com.haulmont.addon.sdbmt.entity.HasTenant;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MultiTenancyDynamicAttributesGuiTools extends DynamicAttributesGuiTools {

    @Inject
    protected MultiTenancyTools multiTenancyTools;

    @Override
    public Set<CategoryAttribute> getAttributesToShowOnTheScreen(MetaClass metaClass, String screen, @Nullable String component) {
        Set<CategoryAttribute> attributesToShow = super.getAttributesToShowOnTheScreen(metaClass, screen, component);
        if (attributesToShow == null) {
            return null;
        }

        if (!isCategoryAttributeTenantSpecific()) {
            return attributesToShow;
        }

        String tenantId = multiTenancyTools.getCurrentUserTenantId();
        return attributesToShow.stream()
                .filter(a -> sameTenant(a, tenantId))
                .collect(Collectors.toSet());
    }

    private boolean isCategoryAttributeTenantSpecific() {
        Class categoryAttributeActualClass = metadata.getClassNN("sys$CategoryAttribute").getJavaClass();
        return HasTenant.class.isAssignableFrom(categoryAttributeActualClass);
    }

    protected boolean sameTenant(CategoryAttribute attribute, String tenantId) {
        String attrTenantId = ((HasTenant) attribute).getTenantId();
        return Objects.equals(tenantId, attrTenantId);
    }
}
