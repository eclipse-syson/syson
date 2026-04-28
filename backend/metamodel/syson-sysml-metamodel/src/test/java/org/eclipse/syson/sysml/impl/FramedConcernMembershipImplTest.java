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

import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link FramedConcernMembershipImpl}.
 *
 * @author gcoutable
 */
public class FramedConcernMembershipImplTest {

    private ModelBuilder builder;

    @BeforeEach
    public void setUp() {
        this.builder = new ModelBuilder();
    }

    @Test
    public void checkBasicGetOwnedConcern() {
        var framedConcernMembership = SysmlFactory.eINSTANCE.createFramedConcernMembership();

        var requirementUsage = SysmlFactory.eINSTANCE.createRequirementUsage();
        requirementUsage.setDeclaredName("requirement1");
        framedConcernMembership.getOwnedRelatedElement().add(requirementUsage);
        assertThat(framedConcernMembership.getOwnedConcern()).isNull();

        var anotherFramedConcernMembership = SysmlFactory.eINSTANCE.createFramedConcernMembership();

        var concernUsage = SysmlFactory.eINSTANCE.createConcernUsage();
        concernUsage.setDeclaredName("concern1");
        anotherFramedConcernMembership.getOwnedRelatedElement().add(concernUsage);
        assertThat(anotherFramedConcernMembership.getOwnedConcern()).isEqualTo(concernUsage);

        var referencedConcernUsage = SysmlFactory.eINSTANCE.createConcernUsage();
        referencedConcernUsage.setDeclaredName("concern2");
        this.builder.addReferenceSubsetting(concernUsage, referencedConcernUsage);
        assertThat(anotherFramedConcernMembership.getOwnedConcern()).isEqualTo(concernUsage);
    }

    @Test
    public void checkBasicGetReferencedConstraint() {
        var framedConcernMembership = SysmlFactory.eINSTANCE.createFramedConcernMembership();

        var requirementUsage = SysmlFactory.eINSTANCE.createRequirementUsage();
        requirementUsage.setDeclaredName("requirement1");
        framedConcernMembership.getOwnedRelatedElement().add(requirementUsage);
        assertThat(framedConcernMembership.getReferencedConstraint()).isNull();

        var anotherFramedConcernMembership = SysmlFactory.eINSTANCE.createFramedConcernMembership();

        var concernUsage = SysmlFactory.eINSTANCE.createConcernUsage();
        concernUsage.setDeclaredName("concern1");
        anotherFramedConcernMembership.getOwnedRelatedElement().add(concernUsage);
        assertThat(anotherFramedConcernMembership.getReferencedConcern()).isEqualTo(concernUsage);

        var referencedConcernUsage = SysmlFactory.eINSTANCE.createConcernUsage();
        referencedConcernUsage.setDeclaredName("concern2");
        this.builder.addReferenceSubsetting(concernUsage, referencedConcernUsage);
        assertThat(anotherFramedConcernMembership.getReferencedConcern()).isEqualTo(referencedConcernUsage);
    }
}
