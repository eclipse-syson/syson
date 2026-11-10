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
package org.eclipse.syson.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.representations.RepresentationVariables;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests expression variables used when checking node eligibility outside rendering.
 *
 * @author arichard
 */
public class NodeDescriptionServiceTest {

    /**
     * Both eligibility paths expose explicit null variables to candidate expressions and preconditions.
     *
     * @param explicitAncestors
     *            whether to use the overload receiving graphical ancestors
     */
    @DisplayName("GIVEN optional render state, WHEN checking node eligibility outside rendering, THEN expressions receive an explicit null variable")
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    public void optionalRenderStateOutsideRendering(boolean explicitAncestors) {
        String optionalState = "optionalRenderState";
        var service = new NodeDescriptionService(mock(IObjectSearchService.class), Collections.singletonMap(optionalState, null));
        var owner = SysmlFactory.eINSTANCE.createPartUsage();
        var element = SysmlFactory.eINSTANCE.createAttributeUsage();
        var editingContext = mock(IEditingContext.class);
        var diagram = mock(Diagram.class);
        var context = mock(DiagramContext.class);
        when(context.diagram()).thenReturn(diagram);
        var description = mock(NodeDescription.class);
        when(description.getSemanticElementsProvider()).thenReturn(variables -> {
            assertThat(variables.getVariables()).containsEntry(optionalState, null);
            assertThat(variables.getVariables()).containsEntry(RepresentationVariables.SELF.name(), owner);
            return List.of(element);
        });
        when(description.getShouldRenderPredicate()).thenReturn(variables -> {
            assertThat(variables.getVariables()).containsEntry(optionalState, null);
            assertThat(variables.getVariables()).containsEntry(RepresentationVariables.SELF.name(), element);
            return true;
        });

        boolean canRender;
        if (explicitAncestors) {
            canRender = service.canNodeDescriptionRenderElement(description, element, owner, editingContext, context, List.of(owner));
        } else {
            canRender = service.canNodeDescriptionRenderElement(description, element, owner, editingContext, context);
        }
        assertThat(canRender).isTrue();
    }
}
