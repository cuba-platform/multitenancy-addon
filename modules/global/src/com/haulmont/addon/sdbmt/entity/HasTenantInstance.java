/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.entity;

/**
 * Interface for entities that have tenant instance.
 * Implementing this interface doesn't make entities tenant-specific, use {@link HasTenant} for that.
 */
public interface HasTenantInstance {
    void setTenant(Tenant tenant);
    Tenant getTenant();
}
