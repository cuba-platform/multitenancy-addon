/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.sdbmt.core.HasTenant;
import com.haulmont.sdbmt.core.TenantId;

import java.util.Set;
import javax.persistence.OneToMany;

@Table(name = "SDBMT_ORDER")
@Entity(name = "sdbmt$Order")
public class Order extends StandardEntity implements HasTenant {
    private static final long serialVersionUID = 2183938902221451506L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_TIME")
    protected Date dateTime;

    @Column(name = "TOTAL")
    protected Double total;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "order")
    protected Set<OrderItem> items;

    @TenantId
    @Column(name = "TENANT_ID")
    protected String tenantId;

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    public Set<OrderItem> getItems() {
        return items;
    }


    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotal() {
        return total;
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