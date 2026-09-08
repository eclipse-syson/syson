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
package org.eclipse.syson.diagram.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.Map;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.syson.services.api.SiriusWebCoreServices;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DiagramMutationExposeService}.
 *
 * @author cbrun
 */
public class DiagramMutationExposeServiceTest {

    /**
     * Verify exposure through composition, annotation and specialization relationships.
     */
    @Test
    void testAddExistingConnectedElements() {
        var resourceSet = new ResourceSetImpl();
        var resource = new ResourceImpl(URI.createURI("test:/model.sysml"));
        resourceSet.getResources().add(resource);

        var definition = SysmlFactory.eINSTANCE.createPartDefinition();
        var nestedUsage = SysmlFactory.eINSTANCE.createPartUsage();
        var membership = SysmlFactory.eINSTANCE.createOwningMembership();
        membership.getOwnedRelatedElement().add(nestedUsage);
        definition.getOwnedRelationship().add(membership);

        var comment = SysmlFactory.eINSTANCE.createComment();
        var annotatedElement = SysmlFactory.eINSTANCE.createPartUsage();
        var annotation = SysmlFactory.eINSTANCE.createAnnotation();
        annotation.setAnnotatedElement(annotatedElement);
        comment.getOwnedRelationship().add(annotation);

        var redefinedFeature = SysmlFactory.eINSTANCE.createPartUsage();
        var redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        redefinition.setRedefiningFeature(nestedUsage);
        redefinition.setRedefinedFeature(redefinedFeature);
        nestedUsage.getOwnedRelationship().add(redefinition);

        var subsettedFeature = SysmlFactory.eINSTANCE.createPartUsage();
        var subsetting = SysmlFactory.eINSTANCE.createSubsetting();
        subsetting.setSubsettingFeature(nestedUsage);
        subsetting.setSubsettedFeature(subsettedFeature);
        nestedUsage.getOwnedRelationship().add(subsetting);

        var superclassifier = SysmlFactory.eINSTANCE.createPartDefinition();
        var subclassification = SysmlFactory.eINSTANCE.createSubclassification();
        subclassification.setSubclassifier(definition);
        subclassification.setSuperclassifier(superclassifier);
        definition.getOwnedRelationship().add(subclassification);

        resource.getContents().add(definition);
        resource.getContents().add(comment);
        resource.getContents().add(annotatedElement);
        resource.getContents().add(redefinedFeature);
        resource.getContents().add(subsettedFeature);
        resource.getContents().add(superclassifier);
        List.of(definition, nestedUsage, annotatedElement, redefinedFeature, subsettedFeature, superclassifier).forEach(element -> element.setDeclaredName("element"));

        var editingContext = mock(IEMFEditingContext.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        when(editingContext.getDomain().getResourceSet()).thenReturn(resourceSet);
        var coreServices = mock(SiriusWebCoreServices.class);
        when(coreServices.objectSearchService()).thenReturn(mock(IObjectSearchService.class));
        var service = spy(new DiagramMutationExposeService(coreServices, mock(DiagramMutationElementService.class), mock(DiagramQueryElementService.class), mock(DiagramQueryExposeService.class)));
        doAnswer(invocation -> invocation.getArgument(0)).when(service).expose(any(), same(editingContext), isNull(), isNull(), anyMap());

        service.addExistingConnectedElements(List.of(nestedUsage), editingContext, null, List.of(), Map.of());
        service.addExistingConnectedElements(List.of(definition), editingContext, null, List.of(), Map.of());
        service.addExistingConnectedElements(List.of(comment), editingContext, null, List.of(), Map.of());

        verify(service).expose(definition, editingContext, null, null, Map.of());
        verify(service).expose(annotatedElement, editingContext, null, null, Map.of());
        verify(service).expose(redefinedFeature, editingContext, null, null, Map.of());
        verify(service).expose(subsettedFeature, editingContext, null, null, Map.of());
        verify(service).expose(nestedUsage, editingContext, null, null, Map.of());
        verify(service).expose(superclassifier, editingContext, null, null, Map.of());
    }

    /**
     * Filter against the entire selection before exposure, regardless of selection order.
     */
    @Test
    void filtersAgainstEverySelectedNode() {
        var definition = SysmlFactory.eINSTANCE.createPartDefinition();
        var comment = SysmlFactory.eINSTANCE.createComment();
        var nestedUsage = SysmlFactory.eINSTANCE.createPartUsage();
        var superclassifier = SysmlFactory.eINSTANCE.createPartDefinition();
        var annotatedElement = SysmlFactory.eINSTANCE.createPartUsage();
        var resourceSet = new ResourceSetImpl();
        var resource = new ResourceImpl(URI.createURI("test:/selection.sysml"));
        resourceSet.getResources().add(resource);
        resource.getContents().addAll(List.of(definition, comment, nestedUsage, superclassifier, annotatedElement));
        List.of(definition, nestedUsage, superclassifier, annotatedElement).forEach(element -> element.setDeclaredName("element"));
        var editingContext = mock(IEMFEditingContext.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        when(editingContext.getDomain().getResourceSet()).thenReturn(resourceSet);
        var coreServices = mock(SiriusWebCoreServices.class);
        when(coreServices.objectSearchService()).thenReturn(mock(IObjectSearchService.class));
        var service = spy(new DiagramMutationExposeService(coreServices, mock(DiagramMutationElementService.class), mock(DiagramQueryElementService.class), mock(DiagramQueryExposeService.class)));
        var dependency = SysmlFactory.eINSTANCE.createDependency();
        dependency.getClient().add(comment);
        dependency.getSupplier().addAll(List.of(nestedUsage, superclassifier, annotatedElement));
        comment.getOwnedRelationship().add(dependency);
        var node = mock(Node.class);
        when(node.getId()).thenReturn("definitionNode");
        when(node.getTargetObjectId()).thenReturn("definition");
        when(node.getDescriptionId()).thenReturn("definitionDescription");
        var diagram = mock(Diagram.class);
        when(diagram.getNodes()).thenReturn(List.of(node));
        var context = mock(DiagramContext.class);
        when(context.diagram()).thenReturn(diagram);
        when(coreServices.objectSearchService().getObject(editingContext, "definition")).thenReturn(Optional.of(definition));
        var description = mock(NodeDescription.class);
        when(description.getId()).thenReturn("definitionDescription");
        var child = mock(NodeDescription.class);
        when(child.getSemanticElementsProvider()).thenReturn(variables -> List.of(nestedUsage));
        when(child.getShouldRenderPredicate()).thenReturn(variables -> true);
        when(description.getChildNodeDescriptions()).thenReturn(List.of(child));
        var descriptions = Map.of(org.eclipse.sirius.components.view.diagram.DiagramFactory.eINSTANCE.createNodeDescription(), description);
        doAnswer(invocation -> invocation.getArgument(0)).when(service).expose(any(), same(editingContext), same(context), isNull(), anyMap());
        clearInvocations(service);

        service.addExistingConnectedElements(List.of(comment, definition), editingContext, context, List.of(node), descriptions);
        service.addExistingConnectedElements(List.of(definition, comment), editingContext, context, List.of(node), descriptions);

        verify(service, never()).expose(nestedUsage, editingContext, context, null, descriptions);
        verify(service, times(2)).expose(superclassifier, editingContext, context, null, descriptions);
        verify(service, times(2)).expose(annotatedElement, editingContext, context, null, descriptions);
    }
}
