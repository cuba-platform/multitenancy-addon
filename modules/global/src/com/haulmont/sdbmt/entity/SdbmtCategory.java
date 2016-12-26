/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.entity;

import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.sdbmt.core.HasTenant;
import com.haulmont.sdbmt.core.TenantId;

import javax.persistence.*;

@DiscriminatorValue("1")
@Extends(Category.class)
@Table(name = "SDBMT_CATEGORY")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@Entity(name = "sdbmt$SdbmtCategory")
public class SdbmtCategory extends Category implements HasTenant {

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
