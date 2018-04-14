/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultString;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;

@Source(type = SourceType.DATABASE)
public interface TenantConfig extends Config {

    @Property("cubasdbmt.tenantIdName")
    @DefaultString("tenant_id")
    String getTenantIdName();

    @Property("cubasdbmt.defaultTenantRole")
    @Default("sec$Role-6ebff3a8-2179-b2a0-f2f3-b0f766680a67")
    Role getDefaultTenantRole();

    void setDefaultTenantRole(Role role);

    @Property("cubasdbmt.defaultTenantParentGroup")
    @Default("sec$Group-0fa2b1a5-1d68-4d69-9fbd-dff348347f93")
    Group getDefaultTenantParentGroup();

    void setDefaultTenantParentGroup(Group group);
}
