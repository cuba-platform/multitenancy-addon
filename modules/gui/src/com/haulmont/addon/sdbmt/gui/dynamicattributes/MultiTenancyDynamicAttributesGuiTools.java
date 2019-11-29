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

package com.haulmont.addon.sdbmt.gui.dynamicattributes;

import com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider;
import com.haulmont.addon.sdbmt.core.global.TenantEntityOperation;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.TenantEntity;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MultiTenancyDynamicAttributesGuiTools extends DynamicAttributesGuiTools {

    public static final String SYS_CATEGORY_ATTRIBUTE = "sys$CategoryAttribute";

    @Inject
    protected TenantProvider tenantProvider;

    @Inject
    protected TenantEntityOperation tenantEntityOperation;

    @Override
    public Set<CategoryAttribute> getAttributesToShowOnTheScreen(MetaClass metaClass, String screen, @Nullable String component) {
        Set<CategoryAttribute> attributesToShow = super.getAttributesToShowOnTheScreen(metaClass, screen, component);
        if (attributesToShow == null) {
            return null;
        }

        if (!isCategoryAttributeTenantSpecific()) {
            return attributesToShow;
        }

        String tenantId = tenantProvider.getCurrentUserTenantId();
        return attributesToShow.stream()
                .filter(a -> sameTenant(a, tenantId))
                .collect(Collectors.toSet());
    }

    private boolean isCategoryAttributeTenantSpecific() {
        Class categoryAttributeActualClass = metadata.getClassNN(SYS_CATEGORY_ATTRIBUTE).getJavaClass();
        return TenantEntity.class.isAssignableFrom(categoryAttributeActualClass);
    }

    protected boolean sameTenant(CategoryAttribute attribute, String tenantId) {
        String attrTenantId = tenantEntityOperation.getTenantId(attribute);
        return Objects.equals(tenantId, attrTenantId);
    }
}
