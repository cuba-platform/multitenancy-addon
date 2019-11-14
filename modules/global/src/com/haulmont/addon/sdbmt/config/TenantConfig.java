/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import com.haulmont.cuba.security.entity.User;

@Source(type = SourceType.DATABASE)
public interface TenantConfig extends Config {

    /**
     * @return This admin user.
     */
    @Property("cubasdbmt.adminUser")
    @Source(type = SourceType.APP)
    @Default("sec$User-60885987-1b61-4247-94c7-dff348347f93")
    User getAdminUser();

    /**
     * @return This anonymous user.
     */
    @Property("cubasdbmt.anonymousUser")
    @Source(type = SourceType.APP)
    @Default("sec$User-a405db59-e674-4f63-8afe-269dda788fe8")
    User getAnonymousUser();

    /**
     * @return This session option name for the tenant ID.
     */
    @Property("cubasdbmt.tenantIdName")
    @DefaultString("tenant_id")
    String getTenantIdName();

    /**
     * @return This URL parameter name for the tenant ID.
     */
    @Property("cubasdbmt.tenantIdUrlParamName")
    @DefaultString("tenantId")
    String getTenantIdUrlParamName();

    /**
     * @return This default tenant role.
     */
    @Property("cubasdbmt.defaultTenantRole")
    @Default("sec$Role-6ebff3a8-2179-b2a0-f2f3-b0f766680a67")
    Role getDefaultTenantRole();

    void setDefaultTenantRole(Role role);

    /**
     * @return This default tenant parent group.
     */
    @Property("cubasdbmt.defaultTenantParentGroup")
    @Default("sec$Group-0fa2b1a5-1d68-4d69-9fbd-dff348347f93")
    Group getDefaultTenantParentGroup();

    void setDefaultTenantParentGroup(Group group);
}
