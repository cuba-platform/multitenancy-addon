/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.sdbmt.core.HasTenant;

@Table(name = "SDBMT_COMPANY")
@Entity(name = "sdbmt$Company")
public class Company extends StandardEntity implements HasTenant {
    private static final long serialVersionUID = 6267693191170808539L;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "TEST")
    protected String test;

    @Column(name = "TENANT_ID", nullable = false)
    protected String tenantId;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getTest() {
        return test;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}