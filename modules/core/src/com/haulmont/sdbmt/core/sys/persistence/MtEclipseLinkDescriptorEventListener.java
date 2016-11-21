/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.core.sys.persistence;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.persistence.EclipseLinkDescriptorEventListener;
import com.haulmont.sdbmt.core.HasTenant;
import org.eclipse.persistence.descriptors.DescriptorEvent;

public class MtEclipseLinkDescriptorEventListener extends EclipseLinkDescriptorEventListener {
    @Override
    public void prePersist(DescriptorEvent event) {
        super.prePersist(event);
        Entity entity = (Entity) event.getObject();
        if (entity instanceof HasTenant) {
            assignTenantId((HasTenant) entity);
        }
    }

    private void assignTenantId(HasTenant entity) {
        String tenantId = AppBeans.get(UserSessionSource.class).getUserSession().getAttribute("tenant_id");
        if (tenantId != null) {
            entity.setTenantId(tenantId);
        }
    }
}
