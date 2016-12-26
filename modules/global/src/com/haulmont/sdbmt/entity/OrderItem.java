/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.sdbmt.core.HasTenant;
import com.haulmont.sdbmt.core.TenantId;

@Table(name = "SDBMT_ORDER_ITEM")
@Entity(name = "sdbmt$OrderItem")
public class OrderItem extends StandardEntity implements HasTenant {
    private static final long serialVersionUID = -6997113262631605895L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ITEM_ID")
    protected MenuItem menuItem;

    @Column(name = "QUANTITY")
    protected Integer quantity;

    @Column(name = "COMMENTS")
    protected String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    protected Order order;

    @TenantId
    @Column(name = "TENANT_ID")
    protected String tenantId;

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }


    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
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