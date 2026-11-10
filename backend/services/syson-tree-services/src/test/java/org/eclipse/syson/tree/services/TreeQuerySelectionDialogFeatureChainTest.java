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
package org.eclipse.syson.tree.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.syson.application.services.SysONResourceService;
import org.eclipse.syson.services.ContextualFeatureChain;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.tree.explorer.services.LibrarySearchServiceNoOp;
import org.eclipse.syson.tree.explorer.services.SysONExplorerFilterService;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests the feature chain collection provided to selection dialogs.
 *
 * @author Jerome Gout
 */
public class TreeQuerySelectionDialogFeatureChainTest {

    private final TreeQuerySelectionDialogService service = new TreeQuerySelectionDialogService(new ISysONExplorerService.NoOp(),
            new SysONExplorerFilterService(new SysONResourceService(new LibrarySearchServiceNoOp())));

    @Test
    @DisplayName("GIVEN inherited and local features, WHEN collecting selectable chains, THEN inherited chains are included and local features are excluded")
    public void collectsInheritedChainsAndExcludesLocalFeatures() {
        // SysML sketch with illustrative names for the unnamed fixture elements:
        // part def Definition {
        //     part part { attribute attribute; }
        // }
        // part context : Definition { attribute localAttribute; }
        var definition = SysmlFactory.eINSTANCE.createPartDefinition();
        var context = SysmlFactory.eINSTANCE.createPartUsage();
        this.addType(context, definition);
        var part = SysmlFactory.eINSTANCE.createPartUsage();
        var attribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(definition, part);
        this.addFeature(part, attribute);
        this.addFeature(context, SysmlFactory.eINSTANCE.createAttributeUsage());

        assertThat(this.service.getFeatureChainSelectionDialogElements(context))
                .containsExactly(new ContextualFeatureChain(context, List.of(part)), new ContextualFeatureChain(context, List.of(part, attribute)));
    }

    @ParameterizedTest
    @DisplayName("GIVEN standard library and project features, WHEN collecting selectable chains, THEN only project features are included")
    @ValueSource(strings = { "sysmllibrary:///test", "kermllibrary:///test" })
    public void excludesStandardLibraryBranchesButKeepsProjectFeatures(String libraryUri) {
        // SysML sketch with illustrative names for the unnamed fixture elements:
        // In the standard library resource identified by libraryUri:
        // part def LibraryDefinition {
        //     part libraryPart { attribute libraryAttribute; }
        // }
        // In the project (detached elements in this fixture):
        // part def ProjectDefinition { attribute projectAttribute; }
        // part context : LibraryDefinition, ProjectDefinition;
        var libraryDefinition = SysmlFactory.eINSTANCE.createPartDefinition();
        var libraryPart = SysmlFactory.eINSTANCE.createPartUsage();
        this.addFeature(libraryDefinition, libraryPart);
        this.addFeature(libraryPart, SysmlFactory.eINSTANCE.createAttributeUsage());
        var library = new ResourceImpl(URI.createURI(libraryUri));
        library.getContents().add(libraryDefinition);
        var projectDefinition = SysmlFactory.eINSTANCE.createPartDefinition();
        var projectAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(projectDefinition, projectAttribute);
        var context = SysmlFactory.eINSTANCE.createPartUsage();
        this.addType(context, libraryDefinition);
        this.addType(context, projectDefinition);

        assertThat(this.service.getFeatureChainSelectionDialogElements(context)).containsExactly(new ContextualFeatureChain(context, List.of(projectAttribute)));
    }

    @Test
    @DisplayName("GIVEN features requiring later validation, WHEN collecting selectable chains, THEN directed attributes, parameters and generic features are included")
    public void leavesUnsupportedFeatureValidationToTheRedefinitionService() {
        // SysML/KerML sketch with illustrative names for the unnamed fixture elements:
        // part def Definition {
        //     in attribute directed;
        //     attribute parameter; // Attached through ParameterMembership in this fixture.
        //     feature feature; // Generic KerML Feature, rather than a SysML Usage.
        // }
        // part context : Definition;
        // This deliberately mixed fixture tests collection without redefinition validation.
        var definition = SysmlFactory.eINSTANCE.createPartDefinition();
        var context = SysmlFactory.eINSTANCE.createPartUsage();
        this.addType(context, definition);
        var directed = SysmlFactory.eINSTANCE.createAttributeUsage();
        directed.setDirection(FeatureDirectionKind.IN);
        this.addFeature(definition, directed);
        var parameter = SysmlFactory.eINSTANCE.createAttributeUsage();
        var parameterMembership = SysmlFactory.eINSTANCE.createParameterMembership();
        definition.getOwnedRelationship().add(parameterMembership);
        parameterMembership.getOwnedRelatedElement().add(parameter);
        var feature = SysmlFactory.eINSTANCE.createFeature();
        this.addFeature(definition, feature);

        assertThat(this.service.getFeatureChainSelectionDialogElements(context))
                .containsExactly(new ContextualFeatureChain(context, List.of(directed)), new ContextualFeatureChain(context, List.of(parameter)), new ContextualFeatureChain(context, List.of(feature)));
    }

    @Test
    @DisplayName("GIVEN an empty context, WHEN collecting selectable chains, THEN no chains are returned")
    public void emptyContextHasNoSelectableChains() {
        // SysML sketch with an illustrative name for the unnamed fixture element:
        // part def EmptyContext;
        assertThat(this.service.getFeatureChainSelectionDialogElements(SysmlFactory.eINSTANCE.createPartDefinition())).isEmpty();
    }

    private void addFeature(Type owner, Feature feature) {
        var membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        owner.getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(feature);
    }

    private void addType(Feature feature, Type type) {
        var typing = SysmlFactory.eINSTANCE.createFeatureTyping();
        feature.getOwnedRelationship().add(typing);
        typing.setType(type);
        typing.setTypedFeature(feature);
    }
}
