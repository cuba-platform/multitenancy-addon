/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt.core;

import com.haulmont.cuba.security.auth.events.UserLoggedInEvent;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.addon.sdbmt.core.sys.MultiTenancySecurityHandler;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class LoginEventListener implements ApplicationListener<UserLoggedInEvent> {

    @Inject
    protected MultiTenancySecurityHandler multiTenancySecurityHandler;

    @Override
    public void onApplicationEvent(UserLoggedInEvent event) {
        UserSession userSession = event.getUserSession();
        if (userSession != null) {
            multiTenancySecurityHandler.compileSessionAttributes(userSession);
            multiTenancySecurityHandler.compilePermissions(userSession);
            multiTenancySecurityHandler.compileConstraints(userSession);
        }
    }
}
