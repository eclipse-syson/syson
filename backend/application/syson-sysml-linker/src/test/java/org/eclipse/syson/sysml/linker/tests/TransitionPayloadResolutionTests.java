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
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.linker.ContextNamespaceProvider;
import org.eclipse.syson.sysml.linker.QualifiedName;
import org.eclipse.syson.sysml.linker.SysMLLinker;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureDirectionKind;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TransitionFeatureKind;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests references to a transition's trigger payload from its effect action. The relevant SysML shape is:
 * <pre>{@code
 * attribute def Message { attribute itms; }
 * state behavior {
 *     state waiting;
 *     transition first waiting
 *         accept cl : Message
 *         do action { in value = cl; }
 *         then waiting;
 * }
 * }</pre>
 * The tests construct only the transition, trigger, effect and reference expression needed for lookup. They
 * distinguish the trigger's explicitly named {@code cl} parameter from the transition's unnamed payload
 * parameter, which acquires that name under SysML 8.3.6.3.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class TransitionPayloadResolutionTests {

    @BeforeClass
    public static void beforeAll() {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * Resolves {@code cl} from the effect action to the transition's second input parameter, rather than
     * returning the accept action's parameter that supplies its name.
     */
    @Test
    public void effectResolvesTransitionPayloadByTriggerPayloadName() {
        this.checkPayloadReference(false);
    }

    /**
     * Resolves {@code cl::itms} from the effect to {@code Message::itms}. This additionally exercises navigation
     * from the transition payload to the trigger payload's type while that typing reference is still pending.
     */
    @Test
    public void effectResolvesMemberOfTransitionPayload() {
        this.checkPayloadReference(true);
    }

    /**
     * Builds a transition with source and payload input parameters and an accept trigger typed by {@code Message}.
     * The effect reference and trigger typing are both submitted as unresolved links; the context must be the
     * effect action and resolution must reach the expected element without prelinking the typing.
     *
     * @param qualified
     *            whether to resolve {@code cl::itms} instead of the payload name {@code cl}
     */
    private void checkPayloadReference(boolean qualified) {
        var root = SysMLFactory.eINSTANCE.createPackage();
        var definition = SysMLFactory.eINSTANCE.createAttributeDefinition();
        definition.setDeclaredName("Message");
        this.addMember(root, definition);
        var item = SysMLFactory.eINSTANCE.createAttributeUsage();
        item.setDeclaredName("itms");
        this.addMember(definition, item);
        var transition = SysMLFactory.eINSTANCE.createTransitionUsage();
        this.addMember(root, transition);
        var source = SysMLFactory.eINSTANCE.createReferenceUsage();
        this.addParameter(transition, source);
        var payload = SysMLFactory.eINSTANCE.createReferenceUsage();
        this.addParameter(transition, payload);

        var trigger = SysMLFactory.eINSTANCE.createAcceptActionUsage();
        var triggerMembership = SysMLFactory.eINSTANCE.createTransitionFeatureMembership();
        triggerMembership.setKind(TransitionFeatureKind.TRIGGER);
        triggerMembership.getOwnedRelatedElement().add(trigger);
        transition.getOwnedRelationship().add(triggerMembership);
        var triggerPayload = SysMLFactory.eINSTANCE.createReferenceUsage();
        triggerPayload.setDeclaredName("cl");
        this.addParameter(trigger, triggerPayload);
        var typing = SysMLFactory.eINSTANCE.createFeatureTyping();
        triggerPayload.getOwnedRelationship().add(typing);

        var effect = SysMLFactory.eINSTANCE.createActionUsage();
        var effectMembership = SysMLFactory.eINSTANCE.createTransitionFeatureMembership();
        effectMembership.setKind(TransitionFeatureKind.EFFECT);
        effectMembership.getOwnedRelatedElement().add(effect);
        transition.getOwnedRelationship().add(effectMembership);
        var expression = SysMLFactory.eINSTANCE.createFeatureReferenceExpression();
        this.addMember(effect, expression);
        Membership lookup = SysMLFactory.eINSTANCE.createMembership();
        expression.getOwnedRelationship().add(lookup);

        Map<EObject, Map<EReference, Set<QualifiedName>>> links = new LinkedHashMap<>();
        String name = "cl";
        Element expected = payload;
        if (qualified) {
            name = "cl::itms";
            expected = item;
        }
        links.put(lookup, new LinkedHashMap<>(Map.of(SysMLPackage.eINSTANCE.getMembership_MemberElement(), Set.of(new QualifiedName(name)))));
        links.put(typing, new LinkedHashMap<>(Map.of(SysMLPackage.eINSTANCE.getFeatureTyping_Type(), Set.of(new QualifiedName("Message")))));
        SysMLLinker linker = new SysMLLinker(root, links);
        assertSame(effect, new ContextNamespaceProvider(linker).getContextNamespace(lookup, SysMLPackage.eINSTANCE.getMembership_MemberElement()));
        linker.resolveAll();
        assertSame(expected, lookup.getMemberElement());
    }

    private void addMember(Namespace namespace, Element element) {
        var membership = SysMLFactory.eINSTANCE.createOwningMembership();
        membership.getOwnedRelatedElement().add(element);
        namespace.getOwnedRelationship().add(membership);
    }

    private void addParameter(Namespace namespace, Feature feature) {
        feature.setDirection(FeatureDirectionKind.IN);
        var membership = SysMLFactory.eINSTANCE.createParameterMembership();
        membership.getOwnedRelatedElement().add(feature);
        namespace.getOwnedRelationship().add(membership);
    }
}
