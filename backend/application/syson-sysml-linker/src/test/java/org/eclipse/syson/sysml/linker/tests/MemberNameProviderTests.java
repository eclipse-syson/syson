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
package org.eclipse.syson.sysml.linker.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.syson.sysml.linker.MemberNameProvider;
import org.eclipse.syson.sysml.linker.SysMLLinker;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.sysml.lang.sysml.AttributeUsage;
import org.omg.sysml.lang.sysml.ConjugatedPortDefinition;
import org.omg.sysml.lang.sysml.FeatureChaining;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.Package;
import org.omg.sysml.lang.sysml.PartDefinition;
import org.omg.sysml.lang.sysml.PortConjugation;
import org.omg.sysml.lang.sysml.PortDefinition;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.RequirementUsage;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests {@link MemberNameProvider}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MemberNameProviderTests {

    private final SysMLLinker linker = new SysMLLinker(null, Collections.emptyMap());

    private final MemberNameProvider nameProvider = new MemberNameProvider(this.linker, new HashMap<>());

    @BeforeClass
    public static void beforeAll() {
        SysMLLogicStandaloneSetup.doSetup();
        SysMLPackage.eINSTANCE.getName();
    }

    @Test
    public void getMemberNamesForAliasMembership() {
        Membership membership = SysMLFactory.eINSTANCE.createMembership();
        membership.setMemberName("aliasName");
        membership.setMemberShortName("aliasShort");

        assertEquals("aliasName", this.nameProvider.getMemberName(membership));
        assertEquals("aliasShort", this.nameProvider.getMemberShortName(membership));

        Set<String> memberNames = this.nameProvider.getMemberNames(membership);
        assertEquals(Set.of("aliasName", "aliasShort"), memberNames);
    }

    @Test
    public void getMemberNamesForUnnamedNonOwningMembership() {
        Membership membership = SysMLFactory.eINSTANCE.createMembership();
        PartDefinition partDefinition = SysMLFactory.eINSTANCE.createPartDefinition();
        partDefinition.setDeclaredName("Part1");
        partDefinition.setDeclaredShortName("p1");
        membership.setMemberElement(partDefinition);

        assertNull(this.nameProvider.getMemberName(membership));
        assertNull(this.nameProvider.getMemberShortName(membership));
        assertTrue(this.nameProvider.getMemberNames(membership).isEmpty());
    }

    @Test
    public void getMemberNamesForOwningMembershipUsesOwnedMemberElementNames() {
        OwningMembership membership = SysMLFactory.eINSTANCE.createOwningMembership();
        Package owningNamespace = SysMLFactory.eINSTANCE.createPackage();
        owningNamespace.getOwnedRelationship().add(membership);

        RequirementUsage requirementUsage = SysMLFactory.eINSTANCE.createRequirementUsage();
        requirementUsage.setDeclaredName("BrakeForce");
        requirementUsage.setReqId("REQ-001");
        membership.getOwnedRelatedElement().add(requirementUsage);

        assertEquals("BrakeForce", this.nameProvider.getMemberName(membership));
        assertEquals("REQ-001", this.nameProvider.getMemberShortName(membership));

        Set<String> memberNames = this.nameProvider.getMemberNames(membership);
        assertEquals(Set.of("BrakeForce", "REQ-001"), memberNames);
    }

    @Test
    public void getMemberNamesForOwningMembershipUsesRedefinedFeatureNames() {
        AttributeUsage redefinedFeature = SysMLFactory.eINSTANCE.createAttributeUsage();
        redefinedFeature.setDeclaredName("mass");
        redefinedFeature.setDeclaredShortName("m");

        AttributeUsage redefiningFeature = SysMLFactory.eINSTANCE.createAttributeUsage();
        Redefinition redefinition = SysMLFactory.eINSTANCE.createRedefinition();
        redefinition.setRedefiningFeature(redefiningFeature);
        redefinition.setRedefinedFeature(redefinedFeature);
        redefiningFeature.getOwnedRelationship().add(redefinition);

        OwningMembership membership = SysMLFactory.eINSTANCE.createOwningMembership();
        Package owningNamespace = SysMLFactory.eINSTANCE.createPackage();
        owningNamespace.getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(redefiningFeature);

        assertEquals(Set.of("mass", "m"), this.nameProvider.getMemberNames(membership));
    }

    @Test
    public void getMemberNamesForOwningMembershipUsesChainedFeatureNames() {
        AttributeUsage chainedFeature = SysMLFactory.eINSTANCE.createAttributeUsage();
        chainedFeature.setDeclaredName("position");
        chainedFeature.setDeclaredShortName("pos");

        AttributeUsage chainOwner = SysMLFactory.eINSTANCE.createAttributeUsage();
        FeatureChaining featureChaining = SysMLFactory.eINSTANCE.createFeatureChaining();
        featureChaining.setChainingFeature(chainedFeature);
        chainOwner.getOwnedRelationship().add(featureChaining);

        OwningMembership membership = SysMLFactory.eINSTANCE.createOwningMembership();
        Package owningNamespace = SysMLFactory.eINSTANCE.createPackage();
        owningNamespace.getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(chainOwner);

        assertEquals(Set.of("position", "pos"), this.nameProvider.getMemberNames(membership));
    }

    @Test
    public void getMemberNamesForConjugatedPortDefinitionUsesOriginalPortDefinitionName() {
        PortDefinition originalPortDefinition = SysMLFactory.eINSTANCE.createPortDefinition();
        originalPortDefinition.setDeclaredName("fuelPort");
        originalPortDefinition.setDeclaredShortName("fp");

        ConjugatedPortDefinition conjugatedPortDefinition = SysMLFactory.eINSTANCE.createConjugatedPortDefinition();
        PortConjugation portConjugation = SysMLFactory.eINSTANCE.createPortConjugation();
        portConjugation.setOriginalPortDefinition(originalPortDefinition);
        conjugatedPortDefinition.getOwnedRelationship().add(portConjugation);

        OwningMembership membership = SysMLFactory.eINSTANCE.createOwningMembership();
        Package owningNamespace = SysMLFactory.eINSTANCE.createPackage();
        owningNamespace.getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(conjugatedPortDefinition);
        originalPortDefinition.getOwnedRelationship().add(membership);

        assertEquals(Set.of("~fuelPort"), this.nameProvider.getMemberNames(membership));
    }

    /**
     * Checks that an unnamed satisfaction usage takes its name from the satisfaction it redefines, not from the
     * requirement it satisfies. With both referenced elements declared elsewhere, the relevant SysML fragment is:
     * <pre>{@code
     * satisfy satisfiedRequirement redefines satisfaction;
     * }</pre>
     * The test constructs the reference subsetting to {@code satisfiedRequirement} before the redefinition of
     * {@code satisfaction}. This order exposes the regression where the first reference subsetting incorrectly
     * supplied the name and prevented lookup from reaching the redefinition. The only resulting member name must
     * be {@code satisfaction}.
     *
     * KerML 1.0, section 8.3.3.3.4, {@code Feature::namingFeature()}, uses the first owned redefinition as the
     * naming source. SysML 2.0, section 8.3.21.10, does not override that rule for satisfaction usages.
     */
    @Test
    public void satisfactionUsesRedefinitionInsteadOfReferenceSubsettingName() {
        var satisfaction = SysMLFactory.eINSTANCE.createSatisfyRequirementUsage();
        var referenced = SysMLFactory.eINSTANCE.createRequirementUsage();
        referenced.setDeclaredName("satisfiedRequirement");
        var subsetting = SysMLFactory.eINSTANCE.createReferenceSubsetting();
        satisfaction.getOwnedRelationship().add(subsetting);
        subsetting.setReferencedFeature(referenced);
        var redefined = SysMLFactory.eINSTANCE.createSatisfyRequirementUsage();
        redefined.setDeclaredName("satisfaction");
        var redefinition = SysMLFactory.eINSTANCE.createRedefinition();
        satisfaction.getOwnedRelationship().add(redefinition);
        redefinition.setRedefinedFeature(redefined);
        var membership = SysMLFactory.eINSTANCE.createFeatureMembership();
        SysMLFactory.eINSTANCE.createPartDefinition().getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(satisfaction);

        assertEquals(Set.of("satisfaction"), this.nameProvider.getMemberNames(membership));
    }

}
