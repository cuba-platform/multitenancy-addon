/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.security;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.security.entity.Role;

@Source(type = SourceType.DATABASE)
public interface SecurityConfig extends Config {

    @Property("sdbmt.security.defaultTenantRole")
    @Default("sdbmt$SdbmtRole-6ebff3a8-2179-b2a0-f2f3-b0f766680a67")
    Role getDefaultTenantRole();

    void setDefaultTenantRole(Role role);
}
