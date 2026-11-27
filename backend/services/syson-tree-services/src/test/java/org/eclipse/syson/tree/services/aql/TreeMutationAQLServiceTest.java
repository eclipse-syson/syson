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
package org.eclipse.syson.tree.services.aql;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.services.ContextualFeatureChain;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.tree.services.TreeMutationSelectionDialogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the adaptation of feature chain redefinition results and feedback for AQL callers.
 *
 * @author Jerome Gout
 */
public class TreeMutationAQLServiceTest {

    private final TreeMutationAQLService service = new TreeMutationAQLService(new TreeMutationSelectionDialogService(new IFeedbackMessageService.NoOp()));

    /**
     * Redefines an inherited two-segment chain, then checks that repeating the operation reuses both local features.
     */
    @Test
    @DisplayName("GIVEN an inherited feature chain, WHEN it is redefined twice, THEN both local features are reused and the source is unchanged")
    public void returnsTheWholeLocalChainAndReusesItOnRepeatedInvocations() {
        // GIVEN a part usage inheriting a part and its nested attribute from its definition
        /*
         * Fixture structure (names added for readability):
         * part def Definition {
         *     part sourcePart {
         *         attribute sourceAttribute;
         *     }
         * }
         * part context : Definition;
         */
        PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
        PartUsage context = SysmlFactory.eINSTANCE.createPartUsage();
        FeatureTyping typing = SysmlFactory.eINSTANCE.createFeatureTyping();
        context.getOwnedRelationship().add(typing);
        typing.setType(definition);
        typing.setTypedFeature(context);
        PartUsage part = SysmlFactory.eINSTANCE.createPartUsage();
        AttributeUsage attribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(definition, part);
        this.addFeature(part, attribute);
        var chain = new ContextualFeatureChain(context, List.of(part, attribute));

        // WHEN the inherited part-attribute chain is redefined in the part usage
        List<Feature> result = this.service.redefineFeatureChain(context, chain);

        // THEN both local features are returned, contained in the context and linked to their redefined source features
        assertThat(result).hasSize(2);
        Feature localPart = result.getFirst();
        Feature localAttribute = result.getLast();
        assertThat(context.getOwnedFeature()).containsExactly(localPart);
        assertThat(localPart.getOwnedFeature()).containsExactly(localAttribute);
        assertThat(localPart.getOwnedRedefinition()).singleElement().satisfies(redefinition -> assertThat(redefinition.getRedefinedFeature()).isSameAs(part));
        assertThat(localAttribute.getOwnedRedefinition()).singleElement().satisfies(redefinition -> assertThat(redefinition.getRedefinedFeature()).isSameAs(attribute));

        // WHEN the same inherited chain is redefined again
        List<Feature> repeatedResult = this.service.redefineFeatureChain(context, chain);

        // THEN the same local features are reused without duplicates and the source chain remains unchanged
        assertThat(repeatedResult).containsExactly(localPart, localAttribute);
        assertThat(context.getOwnedFeature()).containsExactly(localPart);
        assertThat(localPart.getOwnedFeature()).containsExactly(localAttribute);
        assertThat(part.getOwnedFeature()).containsExactly(attribute);
    }

    /**
     * Redefines an inherited action and its input parameter in a part usage and checks that both local features are returned.
     */
    @Test
    @DisplayName("GIVEN a chain ending in an inherited input parameter, WHEN it is redefined, THEN the local action and input parameter are returned in order")
    public void returnsTheLocalActionAndInputParameter() {
        // GIVEN a part usage inheriting an action with an input parameter
        /*
         * Fixture structure (names added for readability):
         * part def Definition {
         *     action sourceAction {
         *         in attribute parameter;
         *     }
         * }
         * part context : Definition;
         */
        PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
        PartUsage context = SysmlFactory.eINSTANCE.createPartUsage();
        FeatureTyping typing = SysmlFactory.eINSTANCE.createFeatureTyping();
        context.getOwnedRelationship().add(typing);
        typing.setType(definition);
        typing.setTypedFeature(context);
        ActionUsage action = SysmlFactory.eINSTANCE.createActionUsage();
        AttributeUsage parameter = SysmlFactory.eINSTANCE.createAttributeUsage();
        parameter.setDirection(FeatureDirectionKind.IN);
        this.addFeature(definition, action);
        ParameterMembership parameterMembership = SysmlFactory.eINSTANCE.createParameterMembership();
        action.getOwnedRelationship().add(parameterMembership);
        parameterMembership.getOwnedRelatedElement().add(parameter);

        // WHEN the action-parameter chain is redefined in the part usage
        List<Feature> result = this.service.redefineFeatureChain(context, new ContextualFeatureChain(context, List.of(action, parameter)));

        // THEN the local action owns the returned input parameter and both redefine their corresponding source features
        assertThat(result).hasSize(2);
        Feature localAction = result.getFirst();
        Feature localParameter = result.getLast();
        assertThat(context.getOwnedFeature()).containsExactly(localAction);
        assertThat(localAction.allRedefinedFeatures()).contains(action);
        assertThat(localAction.getOwnedFeature()).containsExactly(localParameter);
        assertThat(localParameter.getOwningFeatureMembership()).isInstanceOf(ParameterMembership.class);
        assertThat(localParameter.getDirection()).isEqualTo(FeatureDirectionKind.IN);
        assertThat(localParameter.allRedefinedFeatures()).contains(parameter);
    }

    /**
     * Invokes redefinition on an empty definition without a selected chain and checks that the context stays unchanged.
     */
    @Test
    @DisplayName("GIVEN a context without a selected chain, WHEN redefinition is invoked, THEN null is returned and the context is unchanged")
    public void missingSelectionReturnsNullWithoutChangingContext() {
        // GIVEN an empty part definition without a selected feature chain
        PartDefinition context = SysmlFactory.eINSTANCE.createPartDefinition();
        context.setDeclaredName("Context");

        // WHEN redefinition is invoked with a null selection
        List<Feature> result = this.service.redefineFeatureChain(context, null);

        // THEN null is returned and the context still has no owned relationships
        assertThat(result).isNull();
        assertThat(context.getOwnedRelationship()).isEmpty();
    }

    /**
     * Invokes redefinition with a single-feature selection but no target context and checks that it returns null.
     */
    @Test
    @DisplayName("GIVEN a selected feature without a target context, WHEN redefinition is invoked, THEN null is returned")
    public void missingContextReturnsNull() {
        // GIVEN a selected part usage without a target context for redefinition
        PartUsage feature = SysmlFactory.eINSTANCE.createPartUsage();

        // WHEN redefinition is invoked with a null context
        List<Feature> result = this.service.redefineFeatureChain(null, new ContextualFeatureChain(feature, List.of(feature)));

        // THEN null is returned
        assertThat(result).isNull();
    }

    private void addFeature(Type owner, Feature feature) {
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        owner.getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(feature);
    }
}
