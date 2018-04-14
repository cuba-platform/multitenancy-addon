/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.addon.sdbmt;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultiTenancyRunner extends Suite {

    private static final List<Runner> NO_RUNNERS = Collections.emptyList();

    private final List<Runner> runners;

    public MultiTenancyRunner(Class<?> klass) throws Throwable {
        super(klass, NO_RUNNERS);
        runners = Collections.unmodifiableList(createRunners(klass));
    }

    private List<Runner> createRunners(Class<?> klass) throws InitializationError {
        BlockJUnit4ClassRunner notTenantRunner = new BlockJUnit4ClassRunner(klass);
        BlockJUnit4ClassRunner tenantRunner = new BlockJUnit4ClassRunner(klass);
        return Collections.unmodifiableList(Arrays.asList(notTenantRunner, tenantRunner));
    }
}
