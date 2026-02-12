/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.tests.architecture;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Predicate that checks that a {@link JavaClass} complies to the services dependency rules defined by Syson architecture.
 *
 * @author Arthur Daussy
 */
public final class SysONServiceDependencyPredicate extends DescribedPredicate<JavaClass> {

    private final String acceptableDependentServicePattern;

    public SysONServiceDependencyPredicate(List<String> authorizedScopes, String operationType) {
        super("only depend on service classes of scope '%s' with operation of type '%s'", String.join(", ", authorizedScopes), operationType);
        this.acceptableDependentServicePattern = authorizedScopes.stream()
                .collect(Collectors.joining("|", "(", ")")) + operationType + ".*Service";
    }

    @Override
    public boolean test(JavaClass javaClass) {
        String simpleName = javaClass.getSimpleName();

        if (!simpleName.endsWith("Service") || !javaClass.getName().contains("syson")) {
            return true;
        }
        return simpleName.matches(this.acceptableDependentServicePattern);
    }
}
