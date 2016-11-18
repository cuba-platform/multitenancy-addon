/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.entity;

import javax.persistence.*;

import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "SDBMT_TENANT")
@Entity(name = "sdbmt$Tenant")
public class Tenant extends StandardEntity {
    private static final long serialVersionUID = 4892183772427391265L;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "TENANT_ID", nullable = false, unique = true)
    protected String tenantId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ACCESS_GROUP_ID", unique = true)
    protected MtGroup accessGroup;
    public MtGroup getAccessGroup() {
        return accessGroup;
    }

    public void setAccessGroup(MtGroup accessGroup) {
        this.accessGroup = accessGroup;
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