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
package org.eclipse.syson;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.SoftAssertions;
import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.emf.IJavaServiceProvider;
import org.eclipse.syson.diagram.actionflow.view.ActionFlowViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.ActionFlowViewJavaServiceProvider;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GeneralViewJavaServiceProvider;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewForDefinitionDiagramDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewForUsageDiagramDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewJavaServiceProvider;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewJavaServiceProvider;
import org.eclipse.syson.services.ColorProvider;
import org.eclipse.syson.services.UtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Checks SysON Java services.
 *
 * @author gdaniel
 */
public class JavaServiceTests {

    private Set<Class<?>> serviceClasses;

    private View view;

    @BeforeEach
    public void setUp() {
        ViewBuilder viewBuilder = new ViewBuilder();
        this.view = viewBuilder.build();
        IColorProvider colorProvider = new ColorProvider(this.view);
        List<IRepresentationDescriptionProvider> representationDescriptionProviders = List.of(
                new ActionFlowViewDiagramDescriptionProvider(),
                new GeneralViewDiagramDescriptionProvider(),
                new InterconnectionViewForDefinitionDiagramDescriptionProvider(),
                new InterconnectionViewForUsageDiagramDescriptionProvider(),
                new StateTransitionViewDiagramDescriptionProvider()
        );
        representationDescriptionProviders.forEach(provider -> {
            RepresentationDescription description = provider.create(colorProvider);
            this.view.getDescriptions().add(description);
        });

        List<IJavaServiceProvider> javaServiceProviders = List.of(
                new ActionFlowViewJavaServiceProvider(),
                new GeneralViewJavaServiceProvider(),
                new InterconnectionViewJavaServiceProvider(),
                new StateTransitionViewJavaServiceProvider());
        this.serviceClasses = javaServiceProviders.stream()
                .flatMap(provider -> provider.getServiceClasses(this.view).stream())
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("Each public method in service classes is called in an AQL expression")
    public void eachPublicServiceMethodIsCalledInAnAQLExpression() {
        Set<Method> serviceMethods = this.serviceClasses.stream()
                // Exclude UtilService because it contains services called by other Java services and not AQL
                // expressions.
                .filter(clazz -> !clazz.equals(UtilService.class))
                .flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()))
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .collect(Collectors.toSet());

        JavaServiceIsCalledChecker checker = new JavaServiceIsCalledChecker(this.view);

        SoftAssertions softly = new SoftAssertions();
        serviceMethods.forEach(element -> softly.check(() -> checker.check(element)));
        softly.assertAll();
    }

}
