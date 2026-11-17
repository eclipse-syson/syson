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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.linker.AdditionalMemberCompletionService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.Multiplicity;
import org.omg.sysml.lang.sysml.MultiplicityRange;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.lang.sysml.Usage;
import org.omg.sysml.lang.sysml.impl.AttributeUsageImpl;
import org.omg.sysml.lang.sysml.impl.RedefinitionImpl;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Checks the default multiplicity preparation ported from the pilot implementation parser post-processors (ST6RI-947
 * and ST6RI-774).
 *
 * @author Arthur Daussy
 */
public class AdditionalMemberCompletionServiceMultiplicityTests {

    private static final SysMLFactory FACTORY = SysMLFactory.eINSTANCE;

    private final AdditionalMemberCompletionService service = new AdditionalMemberCompletionService(
            AdditionalMemberCompletionService.CompletionType.PRE_LINK_ONLY);

    /**
     * Installs OMG logic delegates before tests create SysML elements.
     */
    @BeforeClass
    public static void beforeClass() {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * GIVEN end usages without a multiplicity, WHEN preparation runs twice, THEN each owns exactly one plain Multiplicity.
     * <pre>
     * connection def Holder { end item value; }
     * </pre>
     * The fixture sets isEnd directly on several usage kinds, independently of syntax restrictions.
     */
    @Test
    public void endUsagesReceiveOneMultiplicity() {
        Namespace holder = FACTORY.createPartDefinition();
        List<Usage> usages = List.of(FACTORY.createAttributeUsage(), FACTORY.createItemUsage(), FACTORY.createPortUsage(),
                FACTORY.createReferenceUsage(), FACTORY.createConnectionUsage());
        for (Usage usage : usages) {
            usage.setIsEnd(true);
            this.addFeature(holder, usage);
            // Repeated preparation must not duplicate the implicit end multiplicity.
            this.service.complete(usage);
            this.service.complete(usage);
            List<Multiplicity> multiplicities = this.ownedMultiplicities(usage);
            assertEquals(usage.eClass().getName(), 1, multiplicities.size());
            assertEquals(SysMLPackage.Literals.MULTIPLICITY, multiplicities.get(0).eClass());
        }
    }

    /**
     * GIVEN an end with an existing range, WHEN preparation runs twice, THEN its relationships and range identity are preserved.
     * <pre>
     * connection def Holder { end item value[0..*]; }
     * </pre>
     * Bounds are omitted from the programmatic range because this test only checks containment preservation.
     */
    @Test
    public void explicitMultiplicityIsPreserved() {
        Namespace holder = FACTORY.createPartDefinition();
        Usage usage = FACTORY.createItemUsage();
        usage.setIsEnd(true);
        this.addFeature(holder, usage);
        MultiplicityRange range = FACTORY.createMultiplicityRange();
        OwningMembership membership = FACTORY.createOwningMembership();
        membership.setOwnedMemberElement(range);
        usage.getOwnedRelationship().add(membership);
        // Capture identities before preparation: even an end must keep its explicit range unchanged.
        List<Relationship> relationships = new ArrayList<>(usage.getOwnedRelationship());

        this.service.complete(usage);
        this.service.complete(usage);

        assertEquals(relationships, usage.getOwnedRelationship());
        assertEquals(1, this.ownedMultiplicities(usage).size());
        assertSame(range, this.ownedMultiplicities(usage).get(0));
    }

    /**
     * GIVEN usages with subsettings or explicit redefinitions, WHEN prepared, THEN only the subsetting usages receive defaults.
     * <pre>
     * part def Base { attribute values; }
     * part def Derived {
     *     attribute subsetValues subsets Base::values;
     *     attribute redefinedValues redefines Base::values;
     * }
     * </pre>
     * The same structural subsetting case is also exercised with item, part and port usages; typing is not evaluated.
     */
    @Test
    public void subsettingsAllowDefaultsButExplicitRedefinitionsDoNot() {
        Namespace base = FACTORY.createPartDefinition();
        Feature values = FACTORY.createAttributeUsage();
        this.addFeature(base, values);
        Namespace derived = FACTORY.createPartDefinition();

        List<Usage> usages = List.of(FACTORY.createAttributeUsage(), FACTORY.createItemUsage(), FACTORY.createPartUsage(),
                FACTORY.createPortUsage());
        // Subsetting the existing feature does not suppress the default for any eligible usage kind.
        for (Usage usage : usages) {
            this.addFeature(derived, usage);
            Subsetting subsetting = FACTORY.createSubsetting();
            subsetting.setSubsettedFeature(values);
            usage.getOwnedRelationship().add(subsetting);
            this.service.complete(usage);
            assertEquals(usage.eClass().getName(), 1, this.ownedMultiplicities(usage).size());
        }

        // Redefining the same target explicitly must suppress the default instead.
        Usage redefining = FACTORY.createAttributeUsage();
        this.addFeature(derived, redefining);
        Redefinition redefinition = FACTORY.createRedefinition();
        redefinition.setRedefinedFeature(values);
        redefining.getOwnedRelationship().add(redefinition);
        this.service.complete(redefining);
        assertTrue(this.ownedMultiplicities(redefining).isEmpty());
    }

    /**
     * GIVEN an explicit redefinition on a non-end usage, WHEN pre-link completion runs twice,
     * THEN no default multiplicity is added and the target remains unresolved.
     * <pre>
     * part def Holder {
     *     attribute value redefines baseValue;
     * }
     * </pre>
     * The helper also exercises item, part and port usages with the same flags.
     */
    @Test
    public void explicitRedefinitionWithoutEndPreventsDefaultWithoutReadingTarget() {
        // An explicit redefinition suppresses the default for each eligible non-end usage kind.
        this.checkRedefinitionDefaultWithoutReadingTarget(false, false, 0);
    }

    /**
     * GIVEN an explicit redefinition on an end usage, WHEN pre-link completion runs twice,
     * THEN exactly one default multiplicity is added without resolving the target.
     * <pre>
     * connection def Holder {
     *     end item value redefines baseValue;
     * }
     * </pre>
     * The helper sets isEnd directly for each eligible usage kind to isolate the end exception.
     */
    @Test
    public void explicitRedefinitionWithEndReceivesDefaultWithoutReadingTarget() {
        // Ends receive their default even when the redefinition is explicit.
        this.checkRedefinitionDefaultWithoutReadingTarget(false, true, 1);
    }

    /**
     * GIVEN an implied redefinition on a non-end usage, WHEN pre-link completion runs twice,
     * THEN exactly one default multiplicity is added without resolving the target.
     * <pre>
     * part def Holder {
     *     attribute value;
     * }
     * </pre>
     * An implied redefinition has no explicit textual notation: the helper attaches one programmatically.
     */
    @Test
    public void impliedRedefinitionWithoutEndReceivesDefaultWithoutReadingTarget() {
        // A generated redefinition must not be mistaken for an explicit redefinition.
        this.checkRedefinitionDefaultWithoutReadingTarget(true, false, 1);
    }

    /**
     * GIVEN an implied redefinition on an end usage, WHEN pre-link completion runs twice,
     * THEN exactly one default multiplicity is added without resolving the target.
     * <pre>
     * connection def Holder {
     *     end item value;
     * }
     * </pre>
     * The helper sets isEnd and attaches an implied redefinition programmatically for each usage kind.
     */
    @Test
    public void impliedRedefinitionWithEndReceivesDefaultWithoutReadingTarget() {
        // Combining an end with an implied redefinition must still produce only one default.
        this.checkRedefinitionDefaultWithoutReadingTarget(true, true, 1);
    }

    /**
     * GIVEN a redefining attribute with an explicit range, WHEN completion runs twice,
     * THEN the original range remains the only owned multiplicity.
     * <pre>
     * part def Holder {
     *     attribute value[0..*] redefines baseValue;
     * }
     * </pre>
     * The fixture retains only the range container; bounds and the redefinition target are omitted
     * because this test checks preservation of the existing range identity, not bound evaluation.
     */
    @Test
    public void redefiningUsagePreservesExplicitRange() {
        // Attach the explicit relationship and range before invoking preparation.
        Usage usage = FACTORY.createAttributeUsage();
        this.addFeature(FACTORY.createPartDefinition(), usage);
        usage.getOwnedRelationship().add(FACTORY.createRedefinition());
        MultiplicityRange range = FACTORY.createMultiplicityRange();
        OwningMembership membership = FACTORY.createOwningMembership();
        membership.getOwnedRelatedElement().add(range);
        usage.getOwnedRelationship().add(membership);
        // Repeated preparation must neither remove the explicit range nor add a default alongside it.
        this.service.complete(usage);
        this.service.complete(usage);
        assertEquals(List.of(range), this.ownedMultiplicities(usage));
    }

    /**
     * GIVEN an attribute first owned through OwningMembership, WHEN it is moved into FeatureMembership,
     * THEN completion changes from no default to one default without consulting derived owningType.
     * <pre>
     * part def Holder {
     *     attribute value;
     * }
     * </pre>
     * The snippet represents the final containment. The initial OwningMembership beneath a PartDefinition
     * is a programmatic intermediate state, not the feature membership produced by this syntax.
     */
    @Test
    public void ownershipUsesContainmentInsteadOfDerivedReferences() {
        // Fail precisely if preparation tries the derived reference instead of inspecting containment.
        Usage usage = new AttributeUsageImpl() {
            @Override
            public Type getOwningType() {
                fail("Pre-link completion must not navigate owningType");
                return null;
            }
        };
        Namespace holder = FACTORY.createPartDefinition();
        OwningMembership membership = FACTORY.createOwningMembership();
        membership.getOwnedRelatedElement().add(usage);
        holder.getOwnedRelationship().add(membership);
        // An ordinary owning membership must not be treated as feature ownership.
        this.service.complete(usage);
        assertTrue(this.ownedMultiplicities(usage).isEmpty());
        // Reparent the same attribute into a feature membership; it now qualifies for one default.
        this.addFeature(holder, usage);
        this.service.complete(usage);
        assertEquals(1, this.ownedMultiplicities(usage).size());
    }

    /**
     * GIVEN an attribute with an unresolved subsetting target, WHEN prepared, THEN it receives a default and retains the proxy.
     * <pre>
     * part def Holder { attribute value subsets target; }
     * </pre>
     * The unresolved name is represented by an EMF proxy rather than a parsed dangling link.
     */
    @Test
    public void subsettingTargetsAreNotResolved() {
        Namespace holder = FACTORY.createPartDefinition();
        Usage usage = FACTORY.createAttributeUsage();
        this.addFeature(holder, usage);
        Feature proxy = FACTORY.createAttributeUsage();
        ((InternalEObject) proxy).eSetProxyURI(URI.createURI("memory:/unresolved#target"));
        Subsetting subsetting = FACTORY.createSubsetting();
        subsetting.setSubsettedFeature(proxy);
        usage.getOwnedRelationship().add(subsetting);

        this.service.complete(usage);

        // Inspect without resolving: the exact proxy must survive preparation.
        Object target = subsetting.eGet(SysMLPackage.Literals.SUBSETTING__SUBSETTED_FEATURE, false);
        assertSame(proxy, target);
        assertTrue(((EObject) target).eIsProxy());
        assertEquals(1, this.ownedMultiplicities(usage).size());
    }

    /**
     * GIVEN package-owned and type-owned usages, WHEN prepared, THEN only eligible owned features receive defaults.
     * <pre>
     * package P {
     *     attribute outside;
     *     part def Holder {
     *         attribute value;
     *         item object;
     *         part component;
     *         port endpoint;
     *         ref reference;
     *     }
     * }
     * </pre>
     * The fixture also creates an incomplete ConnectionUsage without ends to check its exclusion from non-end defaults.
     */
    @Test
    public void defaultMultiplicityDependsOnKindAndOwner() {
        // Package containment is not ownership by a type, regardless of the usage kind.
        Namespace pack = FACTORY.createPackage();
        for (Usage usage : List.of(FACTORY.createAttributeUsage(), FACTORY.createItemUsage(), FACTORY.createPartUsage(),
                FACTORY.createPortUsage())) {
            OwningMembership membership = FACTORY.createOwningMembership();
            membership.setOwnedMemberElement(usage);
            pack.getOwnedRelationship().add(membership);
            this.service.complete(usage);
            assertTrue(usage.eClass().getName(), this.ownedMultiplicities(usage).isEmpty());
        }

        // Feature membership in a type enables defaults for attribute, item, part and port usages.
        Namespace holder = FACTORY.createPartDefinition();
        for (Usage usage : List.of(FACTORY.createAttributeUsage(), FACTORY.createItemUsage(), FACTORY.createPartUsage(),
                FACTORY.createPortUsage())) {
            this.addFeature(holder, usage);
            this.service.complete(usage);
            List<Multiplicity> multiplicities = this.ownedMultiplicities(usage);
            assertEquals(usage.eClass().getName(), 1, multiplicities.size());
            assertEquals(SysMLPackage.Literals.MULTIPLICITY, multiplicities.get(0).eClass());
        }
        // Generic references and connections still require isEnd to receive a default.
        for (Usage usage : List.of(FACTORY.createReferenceUsage(), FACTORY.createConnectionUsage())) {
            this.addFeature(holder, usage);
            this.service.complete(usage);
            assertTrue(usage.eClass().getName(), this.ownedMultiplicities(usage).isEmpty());
        }
    }

    /**
     * Checks one implied/end combination across all eligible usage kinds. The unresolved target and
     * throwing getter model the pre-link state, which cannot be represented completely in SysML text.
     */
    private void checkRedefinitionDefaultWithoutReadingTarget(boolean implied, boolean end, int expectedMultiplicityCount) {
        for (Usage usage : List.of(FACTORY.createAttributeUsage(), FACTORY.createItemUsage(), FACTORY.createPartUsage(), FACTORY.createPortUsage())) {
            // Keep ownership identical across kinds and vary only the flags supplied by the named test.
            this.addFeature(FACTORY.createPartDefinition(), usage);
            usage.setIsEnd(end);
            Feature proxy = FACTORY.createFeature();
            ((InternalEObject) proxy).eSetProxyURI(URI.createURI("memory:/unresolved#redefined"));
            Redefinition redefinition = new RedefinitionImpl() {
                @Override
                public Feature getRedefinedFeature() {
                    fail("Pre-link completion must not navigate redefinedFeature");
                    return null;
                }
            };
            redefinition.setRedefinedFeature(proxy);
            redefinition.setIsImplied(implied);
            usage.getOwnedRelationship().add(redefinition);

            // Repeat completion to check idempotence as well as the default/no-default decision.
            this.service.complete(usage);
            this.service.complete(usage);

            // The caller supplies the expected count independently of the implementation's condition.
            assertEquals(usage.eClass().getName(), expectedMultiplicityCount, this.ownedMultiplicities(usage).size());
            assertSame(proxy, redefinition.eGet(SysMLPackage.Literals.REDEFINITION__REDEFINED_FEATURE, false));
            assertTrue(proxy.eIsProxy());
        }
    }

    private void addFeature(Namespace owner, Feature feature) {
        FeatureMembership membership = FACTORY.createFeatureMembership();
        membership.setOwnedMemberFeature(feature);
        owner.getOwnedRelationship().add(membership);
    }

    private List<Multiplicity> ownedMultiplicities(Usage usage) {
        return usage.getOwnedRelationship().stream()
                .map(Relationship::getOwnedRelatedElement)
                .flatMap(List<Element>::stream)
                .filter(Multiplicity.class::isInstance)
                .map(Multiplicity.class::cast)
                .toList();
    }
}
