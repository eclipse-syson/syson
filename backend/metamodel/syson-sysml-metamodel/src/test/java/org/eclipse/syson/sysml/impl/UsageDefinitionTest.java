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
package org.eclipse.syson.sysml.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests usage definition references derived from owned feature typings.
 *
 * @author arichard
 */
public class UsageDefinitionTest {

    /**
     * Provides definition references and the other references that depend on their values.
     *
     * @return the definition references to test
     */
    private static Stream<Arguments> definitionReferences() {
        var sysml = SysmlPackage.eINSTANCE;
        return Stream.of(
                Arguments.of(sysml.getActionUsage_ActionDefinition(), List.of(sysml.getStep_Behavior(), sysml.getOccurrenceUsage_OccurrenceDefinition())),
                Arguments.of(sysml.getAttributeUsage_AttributeDefinition(), List.of(sysml.getUsage_Definition())),
                Arguments.of(sysml.getAllocationUsage_AllocationDefinition(), List.of(sysml.getConnectionUsage_ConnectionDefinition())),
                Arguments.of(sysml.getFlowUsage_FlowDefinition(), List.of(sysml.getActionUsage_ActionDefinition())),
                Arguments.of(sysml.getInterfaceUsage_InterfaceDefinition(), List.of(sysml.getConnectionUsage_ConnectionDefinition())),
                Arguments.of(sysml.getStateUsage_StateDefinition(), List.of(sysml.getActionUsage_ActionDefinition())));
    }

    /**
     * Checks reflective access, type filtering, ordering, dependent references and read-only list behavior.
     *
     * @param reference
     *            the derived definition reference
     * @param dependentReferences
     *            the references that must expose the same targets
     */
    @ParameterizedTest
    @MethodSource("definitionReferences")
    public void definitionReflectsOwnedTyping(EReference reference, List<EReference> dependentReferences) {
        var factory = SysmlFactory.eINSTANCE;
        var usage = (Usage) factory.create(reference.getEContainingClass());
        assertThat((List<?>) usage.eGet(reference)).isEmpty();
        assertThat(usage.eIsSet(reference)).isFalse();

        var unrelatedTyping = factory.createFeatureTyping();
        usage.getOwnedRelationship().add(unrelatedTyping);
        unrelatedTyping.setTypedFeature(usage);
        unrelatedTyping.setType(factory.createPartDefinition());
        var unsetTyping = factory.createFeatureTyping();
        usage.getOwnedRelationship().add(unsetTyping);
        unsetTyping.setTypedFeature(usage);
        usage.getOwnedRelationship().add(factory.createFeatureMembership());
        assertThat((List<?>) usage.eGet(reference)).isEmpty();
        assertThat(usage.eIsSet(reference)).isFalse();

        var firstDefinition = (Type) factory.create(reference.getEReferenceType());
        var secondDefinition = (Type) factory.create(reference.getEReferenceType());
        for (var definition : List.of(firstDefinition, secondDefinition)) {
            var typing = factory.createFeatureTyping();
            usage.getOwnedRelationship().add(typing);
            typing.setTypedFeature(usage);
            typing.setType(definition);
        }

        assertThat((List<?>) usage.eGet(reference)).isEqualTo(List.of(firstDefinition, secondDefinition));
        assertThat(usage.eIsSet(reference)).isTrue();
        for (var dependentReference : dependentReferences) {
            assertThat((List<?>) usage.eGet(dependentReference)).isEqualTo(List.of(firstDefinition, secondDefinition));
        }
        assertThatThrownBy(() -> ((List<?>) usage.eGet(reference)).clear()).isInstanceOf(UnsupportedOperationException.class);
    }
}
