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

import static org.junit.Assert.assertSame;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.syson.sysml.linker.QualifiedName;
import org.eclipse.syson.sysml.linker.SysMLLinker;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.PartDefinition;
import org.omg.sysml.lang.sysml.PartUsage;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests member lookup when different specializations expose a feature and its redefinition.
 * The directly constructed model corresponds to this SysML outline; definition names are illustrative because
 * the tests leave those definitions unnamed:
 * <pre>{@code
 * part def Design {
 *     part systems;
 * }
 * part def Detailed :> Design {
 *     part :>> systems;
 * }
 * part def Root {
 *     part originalClouds : Design;
 *     part clouds : Detailed redefines originalClouds;
 * }
 * }</pre>
 * Lookup of {@code clouds::systems} must select the unnamed redefinition owned by {@code Detailed}, even when
 * the path through {@code originalClouds} first exposes {@code Design::systems}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SysMLLinkerInheritedRedefinitionTests {

    @BeforeClass
    public static void beforeAll() {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * Visits the redefinition of {@code originalClouds} before the typing by {@code Detailed}. The base
     * {@code systems} candidate must be discarded once the more specific redefinition is found.
     */
    @Test
    public void refinedTypeOverridesMemberReachedThroughRedefinedUsage() {
        this.checkRefinedType(false, false, false);
    }

    /**
     * Reverses the relationship insertion order so the typing is visited before the usage redefinition.
     * The selected {@code systems} must remain the same, independent of specialization order.
     */
    @Test
    public void refinedTypeOverridesMemberRegardlessOfSpecializationOrder() {
        this.checkRefinedType(true, false, false);
    }

    /**
     * Adds an intermediate definition so the most specific {@code systems} redefines the base feature indirectly:
     * <pre>{@code
     * part def Intermediate :> Design { part :>> systems; }
     * part def Detailed :> Intermediate { part :>> systems; }
     * }</pre>
     * Lookup must select the redefinition in {@code Detailed}, excluding the original through the full
     * redefinition chain rather than considering only direct redefinitions.
     */
    @Test
    public void transitivelyRedefinedMemberIsNotInherited() {
        this.checkRefinedType(false, true, false);
    }

    /**
     * Resolves an actual redefinition target, rather than an ordinary member lookup:
     * <pre>{@code
     * part clouds : Detailed redefines originalClouds { part :>> systems; }
     * }</pre>
     * KerML 8.3.3.1.10 inheritedMemberships/removeRedefinedFeatures removes the base candidate even when it is
     * encountered before the more specific member from Detailed.
     */
    @Test
    public void redefinitionTargetUsesMostSpecificGeneralMember() {
        this.checkRefinedType(false, false, true);
    }

    /**
     * Reverses specialization order in the preceding snippet; redefinition target selection must be unchanged.
     */
    @Test
    public void redefinitionTargetIsIndependentOfSpecializationOrder() {
        this.checkRefinedType(true, false, true);
    }

    /**
     * The target of {@code part :>> systems} must also discard an indirectly redefined base candidate when Detailed
     * inherits through Intermediate, as required by KerML 8.3.3.1.10 removeRedefinedFeatures.
     */
    @Test
    public void redefinitionTargetDiscardsTransitivelyRedefinedMember() {
        this.checkRefinedType(false, true, true);
    }

    /**
     * Builds the competing inheritance paths and resolves {@code clouds::systems} by element identity.
     *
     * @param typingFirst
     *            whether the typing relationship precedes the usage redefinition in containment order
     * @param transitive
     *            whether an intermediate definition adds another level of redefinition
     * @param resolveRedefinition
     *            whether to resolve a dangling redefinition target instead of an ordinary member reference
     */
    private void checkRefinedType(boolean typingFirst, boolean transitive, boolean resolveRedefinition) {
        PartDefinition design = SysMLFactory.eINSTANCE.createPartDefinition();
        PartUsage systems = this.addFeature(design, "systems");
        PartDefinition detailed = SysMLFactory.eINSTANCE.createPartDefinition();
        var subclassification = SysMLFactory.eINSTANCE.createSubclassification();
        detailed.getOwnedRelationship().add(subclassification);
        subclassification.setSuperclassifier(design);
        PartUsage detailedSystems = this.addFeature(detailed, null);
        this.redefine(detailedSystems, systems);
        if (transitive) {
            PartDefinition intermediate = detailed;
            Feature intermediateSystems = detailedSystems;
            detailed = SysMLFactory.eINSTANCE.createPartDefinition();
            var further = SysMLFactory.eINSTANCE.createSubclassification();
            detailed.getOwnedRelationship().add(further);
            further.setSuperclassifier(intermediate);
            detailedSystems = this.addFeature(detailed, null);
            this.redefine(detailedSystems, intermediateSystems);
        }
        PartDefinition root = SysMLFactory.eINSTANCE.createPartDefinition();
        PartUsage originalClouds = this.addFeature(root, "originalClouds");
        this.type(originalClouds, design);
        PartUsage clouds = this.addFeature(root, "clouds");
        if (typingFirst) {
            this.type(clouds, detailed);
            this.redefine(clouds, originalClouds);
        } else {
            this.redefine(clouds, originalClouds);
            this.type(clouds, detailed);
        }
        if (resolveRedefinition) {
            Feature local = this.addFeature(clouds, null);
            var redefinition = SysMLFactory.eINSTANCE.createRedefinition();
            local.getOwnedRelationship().add(redefinition);
            EReference reference = SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature();
            Map<EObject, Map<EReference, Set<QualifiedName>>> links = new LinkedHashMap<>();
            links.put(redefinition, new LinkedHashMap<>(Map.of(reference, new LinkedHashSet<>(Set.of(new QualifiedName("systems"))))));
            SysMLLinker linker = new SysMLLinker(root, links);
            assertSame(detailedSystems, linker.get(redefinition, reference).iterator().next());
        } else {
            SysMLLinker linker = new SysMLLinker(root, new LinkedHashMap<>());
            var resolved = linker.resolve(root, new QualifiedName("clouds::systems"));
            assertSame(detailedSystems, linker.getMemberElement(resolved.orElseThrow()));
        }
    }

    private PartUsage addFeature(Namespace owner, String name) {
        PartUsage feature = SysMLFactory.eINSTANCE.createPartUsage();
        feature.setDeclaredName(name);
        var membership = SysMLFactory.eINSTANCE.createFeatureMembership();
        owner.getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(feature);
        return feature;
    }

    private void type(PartUsage feature, PartDefinition type) {
        var typing = SysMLFactory.eINSTANCE.createFeatureTyping();
        feature.getOwnedRelationship().add(typing);
        typing.setType(type);
    }

    private void redefine(Feature feature, Feature redefined) {
        var redefinition = SysMLFactory.eINSTANCE.createRedefinition();
        feature.getOwnedRelationship().add(redefinition);
        redefinition.setRedefinedFeature(redefined);
    }
}
