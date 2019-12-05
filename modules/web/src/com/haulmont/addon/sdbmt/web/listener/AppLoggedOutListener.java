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

package com.haulmont.addon.sdbmt.web.listener;

import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.addon.sdbmt.core.app.multitenancy.TenantProvider;
import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.security.events.AppLoggedOutEvent;
import com.haulmont.cuba.web.sys.navigation.UrlTools;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;

@Component("cubasdbmt_AppLoggedOutListener")
public class AppLoggedOutListener implements ApplicationListener<AppLoggedOutEvent> {

    @Inject
    protected TenantConfig tenantConfig;

    @Inject
    protected UrlTools urlTools;

    @Override
    public void onApplicationEvent(AppLoggedOutEvent event) {
        String tenantId = event.getLoggedOutSession().getAttribute(TenantProvider.TENANT_ID_ATTRIBUTE_NAME);
        if (tenantConfig.getTenantIdUrlParamEnabled() && tenantId != null && !tenantId.equals(TenantProvider.NO_TENANT)) {
            NavigationState navigationState = urlTools.parseState(AppUI.getCurrent().getPage().getLocation().getRawFragment());
            NavigationState newNavigationState = new NavigationState(navigationState.getRoot(),
                    navigationState.getStateMark(),
                    navigationState.getNestedRoute(),
                    Collections.singletonMap(tenantConfig.getTenantIdUrlParamName(), tenantId));
            AppUI.getCurrent().getPage().setUriFragment(newNavigationState.asRoute());
        }
    }
}
