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

package com.haulmont.addon.sdbmt.core.global;

import com.haulmont.addon.sdbmt.core.TenantId;
import com.haulmont.addon.sdbmt.entity.HasTenant;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.TenantEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Helper for working with Tenant.
 */
@Component("cubasdbmt_TenantEntityOperation")
public class TenantEntityOperation {

    /**
     * System tenant id name attribute
     */
    public static final String SYS_TENANT_ID = "sysTenantId";

    @Inject
    protected Metadata metadata;

    @Inject
    protected DataManager dataManager;

    /**
     * Returns MetaProperty of Tenant Id for some class
     *
     * @param entityClass entity class
     * @return MetaProperty instance. Throws exception if not found.
     */
    public MetaProperty getTenantMetaProperty(Class entityClass) {
        MetaClass metaClass = metadata.getClass(entityClass);

        if (metaClass == null) {
            throw new IllegalArgumentException(String.format("MetaClass not found for %s", entityClass.getName()));
        }

        if (TenantEntity.class.isAssignableFrom(entityClass) || HasTenant.class.isAssignableFrom(entityClass)) {
            MetaProperty sysTenantIdProperty = metaClass.getProperty(SYS_TENANT_ID);
            if (sysTenantIdProperty != null) {
                return sysTenantIdProperty;
            }

            MetaProperty tenantIdProperty = metaClass.getProperties().stream()
                    .filter(property -> property.getAnnotatedElement().getAnnotation(TenantId.class) != null)
                    .findFirst().orElse(null);

            checkNotFoundTenantIdProperty(entityClass, tenantIdProperty);
            return tenantIdProperty;
        } else {
            throw new IllegalArgumentException(
                    String.format("Entity %s does not implement an interface TenantEntity or HasTenant", metaClass.getName()));
        }
    }


    /**
     * Set the Tenant Id for some entity
     *
     * @param entity   instance
     * @param tenantId tenant id
     */
    public void setTenant(Entity entity, String tenantId) {
        MetaProperty property = getTenantMetaProperty(entity.getClass());
        checkNotFoundTenantIdProperty(entity.getClass(), property);

        entity.setValue(property.getName(), tenantId);
    }


    /**
     * Returns the Tenant for some entity
     *
     * @param entity instance
     * @return Tenant instance.
     */
    @Nullable
    public Tenant getTenant(Entity entity) {
        MetaProperty property = getTenantMetaProperty(entity.getClass());
        checkNotFoundTenantIdProperty(entity.getClass(), property);

        String tenantId = entity.getValue(property.getName());
        return dataManager.load(Tenant.class)
                .query("select t from cubasdbmt$Tenant t where t.tenantId = :tenantId")
                .parameter("tenantId", tenantId)
                .optional()
                .orElse(null);
    }

    /**
     * Returns the Tenant Id for some entity
     *
     * @param entity instance
     * @return String the value of Tenant Id.
     */
    @Nullable
    public String getTenantId(Entity entity) {
        MetaProperty property = getTenantMetaProperty(entity.getClass());
        checkNotFoundTenantIdProperty(entity.getClass(), property);

        return entity.getValue(property.getName());
    }

    private void checkNotFoundTenantIdProperty(Class entityClass, MetaProperty property) {
        if (property == null) {
            throw new IllegalArgumentException(String.format("Field tenantId not found for %s", entityClass.getName()));
        }
    }
}
