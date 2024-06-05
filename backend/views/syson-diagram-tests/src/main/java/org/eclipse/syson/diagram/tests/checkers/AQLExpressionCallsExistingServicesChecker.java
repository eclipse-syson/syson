/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.diagram.tests.checkers;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.acceleo.query.ast.Call;
import org.eclipse.acceleo.query.parser.AstResult;
import org.eclipse.acceleo.query.runtime.IQueryEnvironment;
import org.eclipse.acceleo.query.runtime.IService;
import org.eclipse.acceleo.query.runtime.Query;
import org.eclipse.acceleo.query.runtime.QueryParsing;
import org.eclipse.sirius.components.interpreter.SimpleCrossReferenceProvider;
import org.eclipse.syson.sysml.helper.EMFUtils;

/**
 * Checks that the provided AQL expression contains services that are either default services or diagram services.
 *
 * @author gdaniel
 */
public class AQLExpressionCallsExistingServicesChecker extends AbstractChecker<String> {

    private static final String AQL_PREFIX = "aql:";

    private final Set<String> aqlDefaultServiceNames;

    private final List<Class<?>> diagramServiceClasses;

    public AQLExpressionCallsExistingServicesChecker(List<Class<?>> diagramServiceClasses) {
        IQueryEnvironment environment = Query.newEnvironmentWithDefaultServices(new SimpleCrossReferenceProvider());
        this.aqlDefaultServiceNames = environment.getLookupEngine().getRegisteredServices().stream()
                .map(IService::getName)
                .collect(Collectors.toSet());
        this.diagramServiceClasses = diagramServiceClasses;
    }

    @Override
    public void check(String expression) {
        assertThat(expression).startsWith(AQL_PREFIX);
        String expressionBody = expression.substring(AQL_PREFIX.length());
        AstResult astResult = QueryParsing.newBuilder().build(expressionBody);
        assertThat(astResult.getErrors()).isEmpty();
        List<String> calledServices = EMFUtils.allContainedObjectOfType(astResult.getAst(), Call.class)
                    .map(Call::getServiceName)
                    .filter(name -> name != null && !name.isBlank())
                    .toList();
        assertThat(calledServices)
                .as("Each service called in AQL should be a default service or a diagram service")
                .allMatch(serviceName -> this.aqlDefaultServiceNames.contains(serviceName) || this.isDiagramServiceClassMethod(serviceName));
    }

    private boolean isDiagramServiceClassMethod(String serviceName) {
        for (Class<?> diagramServiceClass : this.diagramServiceClasses) {
            for (Method method : diagramServiceClass.getMethods()) {
                if (Modifier.isPublic(method.getModifiers())) {
                    if (method.getName().equals(serviceName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
