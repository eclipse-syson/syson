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
import org.omg.sysml.lang.sysml.ConstructorExpression;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.FeatureReferenceExpression;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.FeatureValue;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OperatorExpression;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.ReturnParameterMembership;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests the local namespace rules used to resolve membership targets.
 *
 * @author Arthur Daussy
 */
public class ContextNamespaceProviderTests {

    @BeforeClass
    public static void beforeAll() {
        SysMLLogicStandaloneSetup.doSetup();
        SysMLPackage.eINSTANCE.getName();
    }

    @Test
    public void featureReferenceExpressionUsesNonInvocationNamespace() {
        Namespace outerNamespace = SysMLFactory.eINSTANCE.createPackage();
        OperatorExpression operatorExpression = SysMLFactory.eINSTANCE.createOperatorExpression();
        this.addOwnedMember(outerNamespace, operatorExpression);

        Feature operand = SysMLFactory.eINSTANCE.createFeature();
        ParameterMembership parameterMembership = SysMLFactory.eINSTANCE.createParameterMembership();
        parameterMembership.setOwnedMemberParameter(operand);
        operatorExpression.getOwnedRelationship().add(parameterMembership);

        Membership referent = this.addFeatureReferenceExpression(operand);

        assertSame(outerNamespace, this.getContext(outerNamespace, referent));
    }

    @Test
    public void nonFeatureMembershipInInstantiationExpressionUsesNonInvocationNamespace() {
        Namespace outerNamespace = SysMLFactory.eINSTANCE.createPackage();
        OperatorExpression operatorExpression = SysMLFactory.eINSTANCE.createOperatorExpression();
        this.addOwnedMember(outerNamespace, operatorExpression);

        Membership membership = SysMLFactory.eINSTANCE.createMembership();
        operatorExpression.getOwnedRelationship().add(membership);

        assertSame(outerNamespace, this.getContext(outerNamespace, membership));
    }

    @Test
    public void featureMembershipInInstantiationExpressionUsesInstantiationExpression() {
        Namespace outerNamespace = SysMLFactory.eINSTANCE.createPackage();
        OperatorExpression operatorExpression = SysMLFactory.eINSTANCE.createOperatorExpression();
        this.addOwnedMember(outerNamespace, operatorExpression);

        FeatureMembership membership = SysMLFactory.eINSTANCE.createFeatureMembership();
        membership.setOwnedMemberFeature(SysMLFactory.eINSTANCE.createFeature());
        operatorExpression.getOwnedRelationship().add(membership);

        assertSame(operatorExpression, this.getContext(outerNamespace, membership));
    }

    @Test
    public void featureChainExpressionKeepsItsSpecificContextRule() {
        Namespace outerNamespace = SysMLFactory.eINSTANCE.createPackage();
        FeatureChainExpression featureChainExpression = SysMLFactory.eINSTANCE.createFeatureChainExpression();
        this.addOwnedMember(outerNamespace, featureChainExpression);

        Membership membership = SysMLFactory.eINSTANCE.createMembership();
        featureChainExpression.getOwnedRelationship().add(membership);
        FeatureTyping featureTyping = SysMLFactory.eINSTANCE.createFeatureTyping();
        featureChainExpression.getOwnedRelationship().add(featureTyping);
        Namespace expectedContext = SysMLFactory.eINSTANCE.createPackage();

        SysMLLinker linker = new SysMLLinker(outerNamespace, new LinkedHashMap<>()) {
            @Override
            public java.util.Set<Element> get(EObject object, EReference reference) {
                if (object == featureTyping && reference == SysMLPackage.eINSTANCE.getFeatureTyping_Type()) {
                    return java.util.Set.of(expectedContext);
                }
                return super.get(object, reference);
            }
        };

        assertSame(expectedContext, this.getContext(linker, membership));
    }

    @Test
    public void featureOwnedByConstructorResultIsSkipped() {
        Namespace outerNamespace = SysMLFactory.eINSTANCE.createPackage();
        ConstructorExpression constructorExpression = SysMLFactory.eINSTANCE.createConstructorExpression();
        this.addOwnedMember(outerNamespace, constructorExpression);

        Feature result = SysMLFactory.eINSTANCE.createFeature();
        ReturnParameterMembership returnMembership = SysMLFactory.eINSTANCE.createReturnParameterMembership();
        returnMembership.setOwnedMemberParameter(result);
        constructorExpression.getOwnedRelationship().add(returnMembership);

        Feature resultFeature = SysMLFactory.eINSTANCE.createFeature();
        FeatureMembership resultFeatureMembership = SysMLFactory.eINSTANCE.createFeatureMembership();
        resultFeatureMembership.setOwnedMemberFeature(resultFeature);
        result.getOwnedRelationship().add(resultFeatureMembership);

        Membership referent = this.addFeatureReferenceExpression(resultFeature);

        assertSame(outerNamespace, this.getContext(outerNamespace, referent));
    }

    /**
     * Checks the starting namespace for the argument reference in a send such as:
     * <pre>{@code
     * send new Return_AllItems(itms) via apisc.APIS_MQTT;
     * }</pre>
     * The test builds only the containment path from the send payload through the constructor result and argument
     * to the reference expression. KerML 8.2.3.5.2 requires lookup to skip the constructor-owned features and start
     * at the send payload. This checks the starting context, not the eventual resolution of {@code itms}.
     */
    @Test
    public void constructorArgumentInSendStartsAtSendPayload() {
        Namespace root = SysMLFactory.eINSTANCE.createPackage();
        var send = SysMLFactory.eINSTANCE.createSendActionUsage();
        this.addOwnedMember(root, send);
        var sendPayload = SysMLFactory.eINSTANCE.createReferenceUsage();
        var sendMembership = SysMLFactory.eINSTANCE.createParameterMembership();
        sendMembership.getOwnedRelatedElement().add(sendPayload);
        send.getOwnedRelationship().add(sendMembership);
        var constructor = SysMLFactory.eINSTANCE.createConstructorExpression();
        var value = SysMLFactory.eINSTANCE.createFeatureValue();
        value.getOwnedRelatedElement().add(constructor);
        sendPayload.getOwnedRelationship().add(value);
        var result = SysMLFactory.eINSTANCE.createFeature();
        var resultMembership = SysMLFactory.eINSTANCE.createReturnParameterMembership();
        resultMembership.getOwnedRelatedElement().add(result);
        constructor.getOwnedRelationship().add(resultMembership);
        var argument = SysMLFactory.eINSTANCE.createFeature();
        var argumentMembership = SysMLFactory.eINSTANCE.createParameterMembership();
        argumentMembership.getOwnedRelatedElement().add(argument);
        result.getOwnedRelationship().add(argumentMembership);

        Membership referent = this.addFeatureReferenceExpression(argument);

        // KerML 8.2.3.5.2 skips both the constructor result and its argument features.
        assertSame(sendPayload, this.getContext(root, referent));
    }

    /**
     * Checks the source-end lookup involved in an Arrowhead flow such as:
     * <pre>{@code
     * flow publish_returnallitems of Publish
     *     from apspm.pub to mqget.APIS_MQTT.pub;
     * }</pre>
     * The reduced model contains only one end: it references an alias whose reference subsetting targets a
     * synthetic feature chain ending at a port that owns {@code pub}. The end's nested redefinition of {@code pub}
     * must start from the alias, then follow the chain to the port. Both the starting context and the final target
     * are asserted, preventing lookup from incorrectly starting at the flow end itself.
     */
    @Test
    public void flowEndRedefinitionStartsAtReferencedFeatureAndTraversesItsChain() {
        Namespace root = SysMLFactory.eINSTANCE.createPackage();
        var port = SysMLFactory.eINSTANCE.createPortUsage();
        this.addOwnedMember(root, port);
        var target = SysMLFactory.eINSTANCE.createReferenceUsage();
        target.setDeclaredName("pub");
        this.addOwnedMember(port, target);
        var alias = SysMLFactory.eINSTANCE.createPortUsage();
        this.addOwnedMember(root, alias);
        var aliasSubsetting = SysMLFactory.eINSTANCE.createReferenceSubsetting();
        alias.getOwnedRelationship().add(aliasSubsetting);
        var chain = SysMLFactory.eINSTANCE.createFeature();
        aliasSubsetting.getOwnedRelatedElement().add(chain);
        aliasSubsetting.setReferencedFeature(chain);
        var chaining = SysMLFactory.eINSTANCE.createFeatureChaining();
        chain.getOwnedRelationship().add(chaining);
        chaining.setChainingFeature(port);
        var flow = SysMLFactory.eINSTANCE.createFlowUsage();
        this.addOwnedMember(root, flow);
        var end = SysMLFactory.eINSTANCE.createFlowEnd();
        var endMembership = SysMLFactory.eINSTANCE.createEndFeatureMembership();
        endMembership.getOwnedRelatedElement().add(end);
        flow.getOwnedRelationship().add(endMembership);
        var subsetting = SysMLFactory.eINSTANCE.createReferenceSubsetting();
        end.getOwnedRelationship().add(subsetting);
        subsetting.setReferencedFeature(alias);
        var redefining = SysMLFactory.eINSTANCE.createReferenceUsage();
        var redefiningMembership = SysMLFactory.eINSTANCE.createFeatureMembership();
        redefiningMembership.getOwnedRelatedElement().add(redefining);
        end.getOwnedRelationship().add(redefiningMembership);
        var redefinition = SysMLFactory.eINSTANCE.createRedefinition();
        redefining.getOwnedRelationship().add(redefinition);
        Map<EObject, Map<EReference, Set<QualifiedName>>> links = new LinkedHashMap<>();
        links.put(redefinition, new LinkedHashMap<>(Map.of(SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature(), Set.of(new QualifiedName("pub")))));
        SysMLLinker linker = new SysMLLinker(root, links);

        assertSame(alias, new ContextNamespaceProvider(linker).getContextNamespace(redefinition, SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature()));
        linker.resolveAll();
        assertSame(target, redefinition.getRedefinedFeature());
    }

    private Membership addFeatureReferenceExpression(Feature owner) {
        FeatureReferenceExpression featureReferenceExpression = SysMLFactory.eINSTANCE.createFeatureReferenceExpression();
        Membership referent = SysMLFactory.eINSTANCE.createMembership();
        featureReferenceExpression.getOwnedRelationship().add(referent);
        FeatureValue featureValue = SysMLFactory.eINSTANCE.createFeatureValue();
        featureValue.setOwnedMemberElement(featureReferenceExpression);
        owner.getOwnedRelationship().add(featureValue);
        return referent;
    }

    private OwningMembership addOwnedMember(Namespace namespace, Element element) {
        OwningMembership membership = SysMLFactory.eINSTANCE.createOwningMembership();
        membership.setOwnedMemberElement(element);
        namespace.getOwnedRelationship().add(membership);
        return membership;
    }

    private Namespace getContext(Namespace globalContext, Membership membership) {
        return this.getContext(new SysMLLinker(globalContext, new LinkedHashMap<>()), membership);
    }

    private Namespace getContext(SysMLLinker linker, Membership membership) {
        ContextNamespaceProvider provider = new ContextNamespaceProvider(linker);
        return provider.getContextNamespace(membership, SysMLPackage.eINSTANCE.getMembership_MemberElement());
    }
}
