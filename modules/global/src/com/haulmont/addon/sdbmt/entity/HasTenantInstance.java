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

package com.haulmont.addon.sdbmt.entity;

/**
 * Interface for entities that have tenant instance.
 * Implementing this interface doesn't make entities tenant-specific, use {@link HasTenant} for that.
 *
 * @deprecated Is not required for Multitenancy anymore
 *
 */
@Deprecated
public interface HasTenantInstance {
    void setTenant(Tenant tenant);
    Tenant getTenant();
}
