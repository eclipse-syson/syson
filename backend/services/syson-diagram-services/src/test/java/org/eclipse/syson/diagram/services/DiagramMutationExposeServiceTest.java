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

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.syson.model.services.ModelQueryElementService;
import org.eclipse.syson.services.api.SiriusWebCoreServices;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DiagramMutationExposeService}.
 *
 * @author arichard
 */
public class DiagramMutationExposeServiceTest {

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

        var editingContext = mock(IEMFEditingContext.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        when(editingContext.getDomain().getResourceSet()).thenReturn(resourceSet);
        var modelQueryElementService = mock(ModelQueryElementService.class);
        when(modelQueryElementService.isExposable(any())).thenReturn(true);
        var coreServices = mock(SiriusWebCoreServices.class);
        when(coreServices.objectSearchService()).thenReturn(mock(IObjectSearchService.class));
        var service = spy(new DiagramMutationExposeService(coreServices, mock(DiagramMutationElementService.class), mock(DiagramQueryElementService.class), modelQueryElementService));
        doAnswer(invocation -> invocation.getArgument(0)).when(service).expose(any(), same(editingContext), isNull(), isNull(), anyMap());

        service.addExistingConnectedElements(nestedUsage, editingContext, null, Map.of());
        service.addExistingConnectedElements(definition, editingContext, null, Map.of());
        service.addExistingConnectedElements(comment, editingContext, null, Map.of());

        verify(service).expose(definition, editingContext, null, null, Map.of());
        verify(service).expose(annotatedElement, editingContext, null, null, Map.of());
        verify(service).expose(redefinedFeature, editingContext, null, null, Map.of());
        verify(service).expose(subsettedFeature, editingContext, null, null, Map.of());
        verify(service).expose(nestedUsage, editingContext, null, null, Map.of());
        verify(service).expose(superclassifier, editingContext, null, null, Map.of());
    }
}
