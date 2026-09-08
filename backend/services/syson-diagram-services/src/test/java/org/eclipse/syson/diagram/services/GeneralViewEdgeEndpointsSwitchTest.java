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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests dispatch and missing endpoints in the General View edge switch.
 *
 * @author cbrun
 */
public class GeneralViewEdgeEndpointsSwitchTest {

    /**
     * Preserve missing endpoints and the self-target default of satisfy usages.
     */
    @Test
    void incompleteRelationshipsPreserveEndpointDefaults() {
        var factory = SysmlFactory.eINSTANCE;
        var edgeSwitch = new GeneralViewEdgeEndpointsSwitch();
        for (var element : List.of(factory.createAllocationUsage(), factory.createFlowUsage(), factory.createConnectionUsage(),
                factory.createTransitionUsage(), factory.createIncludeUseCaseUsage(),
                factory.createFeatureValue(), factory.createFramedConcernMembership(), factory.createRequirementConstraintMembership(),
                factory.createAnnotation(), factory.createDependency(), factory.createFeatureTyping(), factory.createRedefinition(),
                factory.createReferenceSubsetting(), factory.createSubsetting(), factory.createSubclassification(), factory.createPartUsage())) {
            var endpoints = edgeSwitch.doSwitch(element);
            assertThat(endpoints.sources()).as("sources of %s", element.eClass().getName()).isEmpty();
            assertThat(endpoints.targets()).as("targets of %s", element.eClass().getName()).isEmpty();
        }
        var satisfy = factory.createSatisfyRequirementUsage();
        var endpoints = edgeSwitch.doSwitch(satisfy);
        assertThat(endpoints.sources()).isEmpty();
        assertThat(endpoints.targets()).containsExactly(satisfy);
    }
}
