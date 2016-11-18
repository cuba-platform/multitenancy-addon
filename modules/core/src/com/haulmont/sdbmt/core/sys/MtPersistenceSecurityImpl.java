/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.PersistenceSecurityImpl;
import com.haulmont.sdbmt.core.HasTenant;

import java.util.Set;

public class MtPersistenceSecurityImpl extends PersistenceSecurityImpl {
    @Override
    public boolean applyConstraints(Query query) {
        boolean applied = super.applyConstraints(query);
        return applyTenantConstraint(query) || applied;
    }

    private boolean applyTenantConstraint(Query query) {
        QueryParser parser = QueryTransformerFactory.createParser(query.getQueryString());
        MetaClass metaClass = metadata.getClassNN(parser.getEntityName());
        if (HasTenant.class.isAssignableFrom(metaClass.getJavaClass())) {
            String tenantId = getTenantId();
            if (tenantId != null) {
                QueryTransformer transformer = QueryTransformerFactory.createTransformer(query.getQueryString());
                String tenantParamName = createTenantIdParamName(transformer);
                transformer.addWhere("{E}.tenantId = :" + tenantParamName);
                query.setParameter(tenantParamName, tenantId);
                query.setQueryString(transformer.getResult());
                return true;
            }
        }
        return false;
    }

    private String createTenantIdParamName(QueryTransformer transformer) {
        String tenantParamName = "mt_tenant_id";
        Set<String> existingParams = transformer.getAddedParams();
        if (existingParams.contains(tenantParamName)) {
            //TODO: generate random name
            throw new RuntimeException("Not implemented yet");
        }
        return tenantParamName;
    }

    private String getTenantId() {
        return AppBeans.get(UserSessionSource.class).getUserSession().getAttribute("tenant_id");
    }
}
