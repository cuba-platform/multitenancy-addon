/*
 * Copyright (c) 2008-2020 Haulmont.
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

import com.haulmont.cuba.core.entity.TenantEntity;
import com.haulmont.cuba.core.sys.persistence.mapping.AbstractJoinExpressionProvider;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.mappings.ManyToManyMapping;
import org.eclipse.persistence.mappings.ManyToOneMapping;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.Arrays;

@Component("cuba_MultiTenantJoinExpressionProvider")
public class MultiTenantJoinExpressionProvider extends AbstractJoinExpressionProvider {

    @Override
    protected Expression processOneToManyMapping(OneToManyMapping mapping) {
        return null;
    }

    @Override
    protected Expression processOneToOneMapping(OneToOneMapping mapping) {
        return createToOneJoinExpression(mapping);
    }

    @Override
    protected Expression processManyToOneMapping(ManyToOneMapping mapping) {
        return createToOneJoinExpression(mapping);
    }

    @Override
    protected Expression processManyToManyMapping(ManyToManyMapping mapping) {
        return null;
    }

    private Expression createToOneJoinExpression(OneToOneMapping oneToOneMapping) {
        ClassDescriptor descriptor = oneToOneMapping.getDescriptor();
        Class<?> referenceClass = oneToOneMapping.getReferenceClass();
        if (isMultiTenant(referenceClass) && isMultiTenant(descriptor.getJavaClass())) {
            Field tenantIdField = getTenantField(referenceClass);
            Field parentTenantId = getTenantField(descriptor.getJavaClass());
            if (tenantIdField != null && parentTenantId != null) {
                String columnName = tenantIdField.getName();
                ExpressionBuilder builder = new ExpressionBuilder();
                Expression tenantColumExpression = builder.get(columnName);
                return tenantColumExpression.equal(
                        builder.getParameter(
                                parentTenantId.getAnnotation(Column.class).name()));
            }
        }
        return null;
    }

    private boolean isMultiTenant(Class<?> referenceClass) {
        //We need to exclude deprecated classes. Otherwise there is a confusion with classes like {@link com.haulmont.addon.sdbmt.entity.TenantUser}
        return (TenantEntity.class.isAssignableFrom(referenceClass)) && (referenceClass.getAnnotation(Deprecated.class) == null);
    }

    private Field getTenantField(Class<?> referenceClass) {
        return Arrays.stream(FieldUtils.getAllFields(referenceClass))
                .filter(f -> f.isAnnotationPresent(Column.class))
                .filter(f -> {
                    String name = f.getAnnotation(Column.class).name();
                    return name.equals("SYS_TENANT_ID") || name.equals("TENANT_ID");
                })
                .findFirst().orElse(null);
    }

}
