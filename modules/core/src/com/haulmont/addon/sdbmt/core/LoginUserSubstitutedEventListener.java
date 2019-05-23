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

package com.haulmont.addon.sdbmt.core;

import com.haulmont.addon.sdbmt.core.sys.MultiTenancySecurityHandler;
import com.haulmont.cuba.security.auth.events.UserSubstitutedEvent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class LoginUserSubstitutedEventListener implements ApplicationListener<UserSubstitutedEvent> {

    @Inject
    protected MultiTenancySecurityHandler multiTenancySecurityHandler;

    @Override
    public void onApplicationEvent(UserSubstitutedEvent event) {
        UserSession userSession = event.getSubstitutedSession();
        if (userSession != null) {
            multiTenancySecurityHandler.compileSessionAttributes(userSession);
            multiTenancySecurityHandler.compilePermissions(userSession);
            multiTenancySecurityHandler.compileConstraints(userSession);
        }
    }
}