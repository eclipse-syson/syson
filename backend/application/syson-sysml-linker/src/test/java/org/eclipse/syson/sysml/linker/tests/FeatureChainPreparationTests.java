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

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.linker.AdditionalMemberCompletionService;
import org.eclipse.syson.sysml.linker.AdditionalMemberCompletionService.CompletionType;
import org.eclipse.syson.sysml.parser.internal.StructuralModelCompletionService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureDirectionKind;
import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.impl.FeatureImpl;
import org.omg.sysml.lang.sysml.impl.MembershipImpl;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Ports the Pilot ST6RI-981 preparation cases to both SysON entry points.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@RunWith(Parameterized.class)
public class FeatureChainPreparationTests {

    private static final SysMLFactory FACTORY = SysMLFactory.eINSTANCE;

    private final boolean parser;

    public FeatureChainPreparationTests(boolean parser) {
        this.parser = parser;
    }

    @Parameters(name = "parser={0}")
    public static List<Object[]> parameters() {
        return List.of(new Object[] { true }, new Object[] { false });
    }

    @BeforeClass
    public static void setup() {
        SysMLLogicStandaloneSetup.doSetup();
    }

    private void prepare(FeatureChainExpression chain) {
        if (this.parser) {
            new StructuralModelCompletionService().complete(chain);
        } else {
            new AdditionalMemberCompletionService(CompletionType.PRE_LINK_ONLY).complete(chain);
        }
    }

    @Test
    public void selectsFirstInputAndPreservesLaterInput() {
        FeatureChainExpression chain = FACTORY.createFeatureChainExpression();
        Feature output = this.addFeature(chain, FeatureDirectionKind.OUT);
        Feature source = this.addFeature(chain, FeatureDirectionKind.IN);
        Feature later = this.addFeature(chain, FeatureDirectionKind.IN);
        Feature explicit = this.addFeature(later, null);
        explicit.setDeclaredName("explicitTarget");
        this.prepare(chain);
        Feature target = this.target(source);
        assertEquals("", target.getDeclaredName());
        this.prepare(chain);
        assertSame(target, this.target(source));
        assertEquals(1, source.getOwnedRelationship().size());
        assertTrue(output.getOwnedRelationship().isEmpty());
        assertSame(explicit, this.target(later));
        assertEquals("explicitTarget", explicit.getDeclaredName());
    }

    @Test
    public void preservesExplicitSourceTarget() {
        FeatureChainExpression chain = FACTORY.createFeatureChainExpression();
        Feature source = this.addFeature(chain, FeatureDirectionKind.IN);
        Feature explicit = this.addFeature(source, null);
        explicit.setDeclaredName("explicitTarget");
        this.prepare(chain);
        this.prepare(chain);
        assertSame(explicit, this.target(source));
        assertEquals("explicitTarget", explicit.getDeclaredName());
        assertEquals(1, source.getOwnedRelationship().size());
    }

    @Test
    public void supportsDeferredAndReplacedInputs() {
        FeatureChainExpression chain = FACTORY.createFeatureChainExpression();
        this.prepare(chain);
        Feature source = this.addFeature(chain, FeatureDirectionKind.IN);
        this.prepare(chain);
        Feature target = this.target(source);
        FeatureMembership membership = (FeatureMembership) source.eContainer();
        Feature replacement = FACTORY.createFeature();
        replacement.setDirection(FeatureDirectionKind.IN);
        membership.getOwnedRelatedElement().clear();
        membership.getOwnedRelatedElement().add(replacement);
        this.prepare(chain);
        this.prepare(chain);
        assertEquals(1, replacement.getOwnedRelationship().size());
        assertSame(target, this.target(source));
    }

    @Test
    public void doesNotConsultDerivedOwnedFeatures() {
        FeatureChainExpression chain = FACTORY.createFeatureChainExpression();
        Feature source = new FeatureImpl() {
            @Override
            public org.eclipse.emf.common.util.EList<Feature> getOwnedFeature() {
                fail("Preparation must use containment, not derived references");
                return null;
            }
        };
        source.setDirection(FeatureDirectionKind.IN);
        FeatureMembership membership = FACTORY.createFeatureMembership();
        membership.getOwnedRelatedElement().add(source);
        chain.getOwnedRelationship().add(membership);
        this.prepare(chain);
        this.prepare(chain);
        assertEquals("", this.target(source).getDeclaredName());
    }

    @Test
    public void leavesUnresolvedOperandAndTargetReferencesUntouched() {
        FeatureChainExpression chain = FACTORY.createFeatureChainExpression();
        Feature source = this.addFeature(chain, FeatureDirectionKind.IN);
        for (Feature owner : List.of(chain, source)) {
            Feature proxy = FACTORY.createFeature();
            ((InternalEObject) proxy).eSetProxyURI(URI.createURI("missing.sysml#target"));
            Membership reference = new MembershipImpl() {
                @Override
                public Element getMemberElement() {
                    fail("Preparation must not navigate memberElement");
                    return null;
                }
            };
            reference.setMemberElement(proxy);
            owner.getOwnedRelationship().add(reference);
            this.prepare(chain);
            this.prepare(chain);
            assertSame(proxy, reference.eGet(SysMLPackage.Literals.MEMBERSHIP__MEMBER_ELEMENT, false));
            assertTrue(proxy.eIsProxy());
        }
        assertEquals("", this.target(source).getDeclaredName());
    }

    @Test
    public void postLinkOnlyDoesNotPrepareStructure() {
        FeatureChainExpression chain = FACTORY.createFeatureChainExpression();
        Feature source = this.addFeature(chain, FeatureDirectionKind.IN);
        new AdditionalMemberCompletionService(CompletionType.POST_LINK_ONLY).complete(chain);
        assertTrue(source.getOwnedRelationship().isEmpty());
        assertEquals(1, chain.getOwnedRelationship().size());
    }

    private Feature addFeature(Feature owner, FeatureDirectionKind direction) {
        Feature feature = FACTORY.createFeature();
        feature.setDirection(direction);
        FeatureMembership membership = FACTORY.createFeatureMembership();
        membership.getOwnedRelatedElement().add(feature);
        owner.getOwnedRelationship().add(membership);
        return feature;
    }

    private Feature target(Feature source) {
        return (Feature) source.getOwnedRelationship().get(0).getOwnedRelatedElement().get(0);
    }
}
