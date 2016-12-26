/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.entity;

import javax.persistence.*;

import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "SDBMT_TENANT")
@Entity(name = "sdbmt$Tenant")
@Listeners("sdbmt_TenantListener")
public class Tenant extends StandardEntity {
    private static final long serialVersionUID = 4892183772427391265L;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "TENANT_ID", nullable = false, unique = true)
    protected String tenantId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ACCESS_GROUP_ID", unique = true)
    protected SdbmtGroup group;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_ID", unique = true)
    protected SdbmtUser admin;

    public void setAdmin(SdbmtUser admin) {
        this.admin = admin;
    }

    public SdbmtUser getAdmin() {
        return admin;
    }


    public SdbmtGroup getGroup() {
        return group;
    }

    public void setGroup(SdbmtGroup group) {
        this.group = group;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }
}