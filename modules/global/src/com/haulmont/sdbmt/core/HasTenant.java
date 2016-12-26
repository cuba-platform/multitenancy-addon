/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.core;

public interface HasTenant {
    String getTenantId();
    void setTenantId(String tenantId);
}
