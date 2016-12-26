/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.entity;

import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.sdbmt.core.HasTenant;
import com.haulmont.sdbmt.core.TenantId;

import javax.persistence.*;

@DiscriminatorValue("SDBMT")
@Entity(name = "sdbmt$SdbmtCategoryAttribute")
@Extends(CategoryAttribute.class)
public class SdbmtCategoryAttribute extends CategoryAttribute implements HasTenant {

    @TenantId
    @Column(name = "TENANT_ID")
    protected String tenantId;

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
