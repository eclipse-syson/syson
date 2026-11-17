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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.linker.QualifiedName;
import org.eclipse.syson.sysml.linker.SysMLLinker;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.sysml.lang.sysml.AttributeUsage;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChaining;
import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.PartDefinition;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Verifies that recursive link resolution can complete the containment graph without invalidating active EMF iterators.
 *
 * @author Arthur Daussy
 */
public class SysMLLinkerConcurrentModificationTests {

    @BeforeClass
    public static void beforeAll() {
        SysMLLogicStandaloneSetup.doSetup();
        SysMLPackage.eINSTANCE.getName();
    }

    @Test
    public void resolveLocallyUsesMembershipSnapshotWhenNestedResolutionAddsMember() {
        ChainingFixture fixture = this.createChainingFixture();
        AtomicBoolean membershipAdded = new AtomicBoolean();
        fixture.chaining().eAdapters().add(new AdapterImpl() {
            @Override
            public void notifyChanged(Notification notification) {
                if (notification.getFeature() == SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature()
                        && membershipAdded.compareAndSet(false, true)) {
                    // Reproduce the lazy model completion seen in the pilot logic while a local
                    // membership lookup is still computing an effective member name.
                    addOwnedMember(fixture.root(), namedFeature("addedDuringResolution"));
                }
            }
        });

        fixture.linker().get(fixture.lookup(), SysMLPackage.eINSTANCE.getMembership_MemberElement());

        assertTrue(membershipAdded.get());
        assertEquals(4, fixture.root().getOwnedMembership().size());
    }

    @Test
    public void effectiveNameUsesRelationshipSnapshotWhenNestedResolutionAddsRelationship() {
        ChainingFixture fixture = this.createChainingFixture();
        AtomicBoolean relationshipAdded = new AtomicBoolean();
        fixture.chaining().eAdapters().add(new AdapterImpl() {
            @Override
            public void notifyChanged(Notification notification) {
                if (notification.getFeature() == SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature()
                        && relationshipAdded.compareAndSet(false, true)) {
                    // getLastChainingFeature keeps scanning after linker.get(); adding a relationship
                    // here verifies that this scan is isolated from reentrant containment changes.
                    fixture.chainOwner().getOwnedRelationship().add(SysMLFactory.eINSTANCE.createFeatureChaining());
                }
            }
        });

        fixture.linker().get(fixture.lookup(), SysMLPackage.eINSTANCE.getMembership_MemberElement());

        assertTrue(relationshipAdded.get());
        assertEquals(2, fixture.chainOwner().getOwnedRelationship().size());
    }

    @Test
    public void redefinedMembershipsUseRelationshipSnapshotWhenNestedResolutionAddsRelationship() {
        PartDefinition root = SysMLFactory.eINSTANCE.createPartDefinition();

        AttributeUsage target = namedFeature("target");
        addOwnedMember(root, target);

        AttributeUsage redefiningFeature = unnamedFeature();
        Redefinition redefinition = SysMLFactory.eINSTANCE.createRedefinition();
        redefinition.setRedefiningFeature(redefiningFeature);
        redefiningFeature.getOwnedRelationship().add(redefinition);
        addOwnedFeature(root, redefiningFeature);
        assertEquals(1, redefiningFeature.getOwnedRedefinition().size());

        addOwnedMember(root, namedFeature("existingAfterRedefinition"));

        Map<EObject, Map<EReference, Set<QualifiedName>>> toResolve = new LinkedHashMap<>();
        Membership lookup = addUnresolvedLookup(root, toResolve);
        // A global name makes the fixture independent of inherited-feature semantics: the purpose
        // here is to force linker.get() while getRedefinedMemberships is traversing the root Type.
        addLinkToResolve(toResolve, redefinition, SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature(), "$::target");
        AtomicBoolean relationshipAdded = new AtomicBoolean();
        SysMLLinker linker = new SysMLLinker(root, toResolve) {
            @Override
            public Set<Element> get(EObject object, EReference reference) {
                if (object == redefinition
                        && reference == SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature()
                        && relationshipAdded.compareAndSet(false, true)) {
                    // getRedefinedMemberships invokes linker.get() from inside an ownedRelationship
                    // stream; mutating here precisely emulates an implicit member created by the
                    // nested resolution without depending on EMF notification implementation details.
                    addOwnedMember(root, namedFeature("addedDuringRedefinition"));
                }
                return super.get(object, reference);
            }
        };

        linker.get(lookup, SysMLPackage.eINSTANCE.getMembership_MemberElement());

        assertSame(target, redefinition.getRedefinedFeature());
        assertTrue(relationshipAdded.get());
        assertEquals(5, root.getOwnedMembership().size());
    }

    private ChainingFixture createChainingFixture() {
        Namespace root = SysMLFactory.eINSTANCE.createPackage();
        AttributeUsage chainOwner = unnamedFeature();
        FeatureChaining chaining = SysMLFactory.eINSTANCE.createFeatureChaining();
        chainOwner.getOwnedRelationship().add(chaining);
        addOwnedMember(root, chainOwner);

        AttributeUsage target = namedFeature("target");
        addOwnedMember(root, target);

        Map<EObject, Map<EReference, Set<QualifiedName>>> toResolve = new LinkedHashMap<>();
        Membership lookup = addUnresolvedLookup(root, toResolve);
        addLinkToResolve(toResolve, chaining, SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature(), "target");
        return new ChainingFixture(root, chainOwner, chaining, lookup, new SysMLLinker(root, toResolve));
    }

    private static Membership addUnresolvedLookup(Namespace namespace,
            Map<EObject, Map<EReference, Set<QualifiedName>>> linksToResolve) {
        Membership lookup = SysMLFactory.eINSTANCE.createMembership();
        lookup.setMemberName("lookup");
        namespace.getOwnedRelationship().add(0, lookup);
        // Keep this lookup unresolved so the lookup performs a complete local scan, during which
        // the effective name of the chaining/redefining Feature triggers the nested resolution.
        addLinkToResolve(linksToResolve, lookup, SysMLPackage.eINSTANCE.getMembership_MemberElement(), "missing");
        return lookup;
    }

    private static void addLinkToResolve(Map<EObject, Map<EReference, Set<QualifiedName>>> linksToResolve,
            EObject owner, EReference reference, String qualifiedName) {
        linksToResolve.computeIfAbsent(owner, unused -> new LinkedHashMap<>())
                .computeIfAbsent(reference, unused -> new LinkedHashSet<>())
                .add(new QualifiedName(qualifiedName));
    }

    private static OwningMembership addOwnedMember(Namespace namespace, Feature member) {
        OwningMembership membership = SysMLFactory.eINSTANCE.createOwningMembership();
        membership.setOwnedMemberElement(member);
        namespace.getOwnedRelationship().add(membership);
        return membership;
    }

    private static FeatureMembership addOwnedFeature(PartDefinition type, Feature feature) {
        FeatureMembership membership = SysMLFactory.eINSTANCE.createFeatureMembership();
        membership.setOwnedMemberFeature(feature);
        type.getOwnedRelationship().add(membership);
        return membership;
    }

    private static AttributeUsage namedFeature(String name) {
        AttributeUsage feature = unnamedFeature();
        feature.setDeclaredName(name);
        return feature;
    }

    private static AttributeUsage unnamedFeature() {
        return SysMLFactory.eINSTANCE.createAttributeUsage();
    }

    /**
     * Model elements used by feature-chaining tests.
     *
     * @param root
     *            root namespace
     * @param chainOwner
     *            feature that owns the chaining relationship
     * @param chaining
     *            chaining relationship
     * @param lookup
     *            membership used for lookup
     * @param linker
     *            linker under test
     */
    private record ChainingFixture(Namespace root, AttributeUsage chainOwner, FeatureChaining chaining,
            Membership lookup, SysMLLinker linker) {
    }

}
