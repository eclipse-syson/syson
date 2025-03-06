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
package org.eclipse.syson.sysml.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.VariantMembership;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link VariantMembershipImpl}.
 *
 * @author Arthur Daussy
 */
public class VariantMembershipImplTest {

    @Test
    public void checkBasicGetOwnedVariantUsage() {
        VariantMembership variantMembership = SysmlFactory.eINSTANCE.createVariantMembership();

        EnumerationUsage enumUsage = SysmlFactory.eINSTANCE.createEnumerationUsage();
        enumUsage.setDeclaredName("enum1");
        variantMembership.getOwnedRelatedElement().add(enumUsage);

        assertThat(variantMembership.getMemberElement()).isEqualTo(enumUsage);
        assertThat(variantMembership.getMemberName()).isEqualTo("enum1");
    }
}
