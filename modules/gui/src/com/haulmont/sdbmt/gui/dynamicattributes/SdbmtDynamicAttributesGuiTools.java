/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.gui.dynamicattributes;

import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.sdbmt.entity.SdbmtCategoryAttribute;

import javax.inject.Inject;
import java.util.Objects;

public class SdbmtDynamicAttributesGuiTools extends DynamicAttributesGuiTools {

    @Inject
    protected UserSessionSource uss;

    @Override
    protected boolean attributeShouldBeShownOnTheScreen(String screen, String component, CategoryAttribute attribute) {
        SdbmtCategoryAttribute mtAttribute = (SdbmtCategoryAttribute) attribute;
        String tenantId = uss.getUserSession().getAttribute("tenant_id");
        if (mtAttribute.getTenantId() != null && !Objects.equals(tenantId, mtAttribute.getTenantId())) {
            return false;
        }
        return super.attributeShouldBeShownOnTheScreen(screen, component, attribute);
    }
}
