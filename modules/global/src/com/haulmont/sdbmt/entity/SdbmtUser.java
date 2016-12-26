/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.entity;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import com.haulmont.cuba.core.entity.annotation.Extends;
import javax.persistence.Column;

import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.sdbmt.core.HasTenant;
import com.haulmont.sdbmt.core.TenantId;

@Extends(User.class)
@DiscriminatorValue("SDBMT")
@Entity(name = "sdbmt$SdbmtUser")
@Listeners("sdbmt_SdbmtUserEntityListener")
public class SdbmtUser extends User implements HasTenant {
    private static final long serialVersionUID = 5044824063142871921L;

    @TenantId
    @Column(name = "TENANT_ID")
    protected String tenantId;

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }
}