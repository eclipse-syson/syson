/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.tree.explorer.view.duplicate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class of {@link SysMLContainmentMembershipLabelSwitch}.
 *
 * @author Arthur Daussy
 */
public class SysMLContainmentMembershipLabelSwitchTest {

    private final SysMLContainmentMembershipLabelSwitch labelSwitch = new SysMLContainmentMembershipLabelSwitch();

    @DisplayName("GIVEN a Membership subtype, WHEN computing the user-friendly label, THEN a custom label is used for any subtype of Membership")
    @Test
    public void checkMembershipSubTypeLabels() {
        // This test aims to verify that subtype of Membership have a custom label (in case of metamodel future changes)
        List<EClass> membershipSubtypes = EMFUtils.allContainedObjectOfType(SysmlPackage.eINSTANCE, EClass.class)
                .filter(eClass -> eClass.getEAllSuperTypes().contains(SysmlPackage.eINSTANCE.getMembership()) && eClass != SysmlPackage.eINSTANCE.getMembership())
                .toList();

        for (EClass subMembership : membershipSubtypes) {
            assertThat(this.labelSwitch.doSwitch(subMembership))
                    .isNotNull()
                    .isNotEqualTo("Add as a member");
        }
    }

    @DisplayName("GIVEN a Membership, WHEN computing the user-friendly label, THEN a custom label is used for a Membership")
    @Test
    public void checkMembershipLabel() {
        assertThat(this.labelSwitch.doSwitch(SysmlPackage.eINSTANCE.getMembership()))
                .isNotNull()
                .isEqualTo("Add as a member");
    }
}
