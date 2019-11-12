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

package com.haulmont.addon.sdbmt.core.sys.persistence;

import com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider;
import com.haulmont.cuba.core.entity.HasTenant;
import com.haulmont.cuba.core.sys.persistence.AdditionalCriteriaProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * The implementation of additional criteria for multi tenants.
 */
@Component("cubasdbmt_HasTenantAdditionalCriteriaProvider")
public class HasTenantAdditionalCriteriaProvider implements AdditionalCriteriaProvider {

    private static final String TENANT_ID = "tenantId";

    @Inject
    protected TenantProvider tenantProvider;


    @Override
    public boolean requiresAdditionalCriteria(Class entityClass) {
        return HasTenant.class.isAssignableFrom(entityClass);
    }

    @Override
    public String getAdditionalCriteria() {
        return String.format("(:tenantId = '%s' or this.tenantId = :tenantId)", TenantProvider.NO_TENANT);
    }

    @Nullable
    @Override
    public Map<String, Object> getCriteriaParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(TENANT_ID, tenantProvider.getTenantId());
        return parameters;
    }
}
