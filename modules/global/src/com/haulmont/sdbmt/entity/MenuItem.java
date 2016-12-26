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
import com.haulmont.sdbmt.core.TenantId;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s %s|name,price")
@Table(name = "SDBMT_MENU_ITEM")
@Entity(name = "sdbmt$MenuItem")
public class MenuItem extends StandardEntity implements HasTenant {
    private static final long serialVersionUID = -1382999361606023359L;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "PRICE")
    protected Double price;

    @Column(name = "HOT")
    protected Boolean hot;

    @TenantId
    @Column(name = "TENANT_ID")
    protected String tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHOTO_ID")
    protected SdbmtFileDescriptor photo;

    public void setPhoto(SdbmtFileDescriptor photo) {
        this.photo = photo;
    }

    public SdbmtFileDescriptor getPhoto() {
        return photo;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }

    public Boolean getHot() {
        return hot;
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