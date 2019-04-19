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
