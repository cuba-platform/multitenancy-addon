/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.entity;

/**
 * Interface to be implemented by entities that have to be tenant-specific
 */
public interface HasTenant {
    String getTenantId();
    void setTenantId(String tenantId);
}
