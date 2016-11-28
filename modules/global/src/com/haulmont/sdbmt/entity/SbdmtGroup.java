/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.entity;

import javax.persistence.*;

import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.sdbmt.core.HasTenant;
import com.haulmont.sdbmt.core.TenantId;

@DiscriminatorValue("MT")
@Extends(Group.class)
@Entity(name = "sdbmt$SbdmtGroup")
public class SbdmtGroup extends Group implements HasTenant {
    private static final long serialVersionUID = 8099135318007454563L;

    @TenantId
    @Column(name = "TENANT_ID")
    protected String tenantId;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "accessGroup")
    protected Tenant tenant;

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }
}