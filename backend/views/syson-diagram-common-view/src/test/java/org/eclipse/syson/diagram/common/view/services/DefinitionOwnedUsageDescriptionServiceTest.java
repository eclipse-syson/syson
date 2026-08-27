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
package org.eclipse.syson.diagram.common.view.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefinitionOwnedUsageDescriptionService}.
 *
 * @author arichard
 */
public class DefinitionOwnedUsageDescriptionServiceTest {

    /**
     * Verifies that a satisfy requirement usage uses the requirement-specific Definition ownership reference.
     */
    @Test
    @DisplayName("GIVEN a SatisfyRequirementUsage, WHEN resolving its Definition owner reference, THEN ownedRequirement is selected")
    void satisfyRequirementUsageUsesOwnedRequirement() {
        var service = new DefinitionOwnedUsageDescriptionService();

        var ownedUsageReference = service.getOwnedUsageReference(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage());

        assertThat(ownedUsageReference).contains(SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement());
    }
}
