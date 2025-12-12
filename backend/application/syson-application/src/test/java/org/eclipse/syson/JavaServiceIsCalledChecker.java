/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.acceleo.query.ast.Call;
import org.eclipse.acceleo.query.parser.AstResult;
import org.eclipse.acceleo.query.runtime.IQueryEnvironment;
import org.eclipse.acceleo.query.runtime.IService;
import org.eclipse.acceleo.query.runtime.Query;
import org.eclipse.acceleo.query.runtime.QueryParsing;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.interpreter.SimpleCrossReferenceProvider;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.ViewPackage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.AQLConstants;

/**
 * Checks that a {@link Method} is called by at least one AQL expression in the given {@link View}.
 *
 * @author gdaniel
 */
public class JavaServiceIsCalledChecker {

    private final Set<String> aqlServiceNames;

    public JavaServiceIsCalledChecker(View view) {
        IQueryEnvironment environment = Query.newEnvironmentWithDefaultServices(new SimpleCrossReferenceProvider());
        Set<String> aqlDefaultServiceNames = environment.getLookupEngine().getRegisteredServices().stream()
                .map(IService::getName)
                .collect(Collectors.toSet());


        this.aqlServiceNames = new HashSet<>();
        Collection<String> aqlExpressions = this.collectAQLExpressions(view);

        List<String> aqlBodyExpressions = aqlExpressions.stream()
                .filter(expression -> expression != null && !expression.isBlank())
                .filter(expression -> expression.startsWith(AQLConstants.AQL))
                .map(expression -> expression.substring(AQLConstants.AQL.length()))
                .toList();

        for (String aqlBodyExpression : aqlBodyExpressions) {
            AstResult astResult = QueryParsing.newBuilder().build(aqlBodyExpression);
            assertThat(astResult.getErrors()).isEmpty();
            List<String> calledServices = EMFUtils.allContainedObjectOfType(astResult.getAst(), Call.class)
                    .map(Call::getServiceName)
                    .filter(name -> name != null && !name.isBlank())
                    // Exclude AQL default services, they cannot be associated to a service method.
                    .filter(name -> !aqlDefaultServiceNames.contains(name))
                    .toList();

            this.aqlServiceNames.addAll(calledServices);
        }
    }

    public void check(Method service) {
        assertThat(this.aqlServiceNames).as("The service method %s.%s should be called at least once in an AQL expression", service.getDeclaringClass().getSimpleName(), service.getName())
                .contains(service.getName());
    }

    private Collection<String> collectAQLExpressions(EObject eObject) {
        Set<String> expressions = new HashSet<>();
        this.collectAQLExpressions(eObject, expressions);
        return expressions;
    }

    private void collectAQLExpressions(EObject eObject, Set<String> expressions) {
        // All interpreted expressions on the eOject itself
        eObject.eClass().getEAllStructuralFeatures().stream()
                .filter(EAttribute.class::isInstance)
                .map(EAttribute.class::cast)
                .filter(attr -> attr.getEType() == ViewPackage.Literals.INTERPRETED_EXPRESSION)
                .forEach(expressionAttribute -> expressions.add((String) eObject.eGet(expressionAttribute)));
        // Recurse on all its descendants
        eObject.eAllContents().forEachRemaining(o -> this.collectAQLExpressions(o, expressions));
    }
}
