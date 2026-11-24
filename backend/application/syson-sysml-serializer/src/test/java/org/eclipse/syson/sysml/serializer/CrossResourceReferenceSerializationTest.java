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
package org.eclipse.syson.sysml.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.serializer.model.DanglingReferenceIndex;
import org.eclipse.syson.sysml.serializer.model.SerializerModelQueries;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.AttributeDefinition;
import org.omg.sysml.lang.sysml.AttributeUsage;
import org.omg.sysml.lang.sysml.DataType;
import org.omg.sysml.lang.sysml.Dependency;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChaining;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.VisibilityKind;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Names imports, typing, aliases, and feature chains whose targets live in another EMF Resource. The serializer never
 * resolves the proxies that hold those targets: it names them through the caller's {@link ReferenceNameResolver},
 * which may resolve them on its own terms, and otherwise by their URI.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class CrossResourceReferenceSerializationTest {

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * An EMF-built membership import of a cross-resource proxy must emit the name the caller gives the proxy, without
     * parser text, and leave the proxy installed; without a caller name it must fall back to the proxy URI fragment.
     */
    @Test
    void namesCrossResourceMembershipImportThroughTheCaller() {
        final Namespace libraryRoot = SysMLFactory.eINSTANCE.createNamespace();
        final Namespace collections = SysMLFactory.eINSTANCE.createPackage();
        collections.setDeclaredName("Collections");
        addOwnedMember(libraryRoot, collections);
        final Element array = SysMLFactory.eINSTANCE.createDataType();
        array.setDeclaredName("Array");
        final Membership arrayMembership = addOwnedMember(collections, array);
        final XMIResourceImpl libraryResource =
                new XMIResourceImpl(URI.createURI("memory:/library.xmi"));
        libraryResource.getContents().add(libraryRoot);
        libraryResource.setID(arrayMembership, "array-membership");

        final Namespace projectRoot = SysMLFactory.eINSTANCE.createNamespace();
        final Namespace measurementReferences =
                SysMLFactory.eINSTANCE.createPackage();
        measurementReferences.setDeclaredName("MeasurementReferences");
        addOwnedMember(projectRoot, measurementReferences);
        final var importRelationship =
                SysMLFactory.eINSTANCE.createMembershipImport();
        importRelationship.setVisibility(VisibilityKind.PRIVATE);
        final Membership proxy = SysMLFactory.eINSTANCE.createMembership();
        ((InternalEObject) proxy).eSetProxyURI(
                URI.createURI("memory:/library.xmi#array-membership"));
        importRelationship.setImportedMembership(proxy);
        measurementReferences.getOwnedRelationship().add(importRelationship);
        final XMIResourceImpl projectResource =
                new XMIResourceImpl(URI.createURI("memory:/project.xmi"));
        projectResource.getContents().add(projectRoot);
        final ResourceSetImpl resourceSet = new ResourceSetImpl();
        resourceSet.getResources().add(libraryResource);
        resourceSet.getResources().add(projectResource);

        final String serialized = new SysMLSerializer().serialize(SerializationRequest.forElement(projectRoot)
                .withContextualNames(CrossResourceReferenceSerializationTest::callerName));
        final String uncontextual = new SysMLSerializer().serialize(projectRoot);

        assertTrue(serialized.contains("private import Collections::Array;"), serialized);
        assertTrue(uncontextual.contains("private import 'array-membership';"), uncontextual);
        assertSame(proxy, importRelationship.eGet(SysMLPackage.eINSTANCE.getMembershipImport_ImportedMembership(), false));
    }

    /**
     * Cross-resource typing must emit the caller's name for the proxy in text and HTML, pass the proxy to the HTML link
     * resolver, and leave the proxy installed.
     */
    @Test
    void namesCrossResourceFeatureTypingForTextAndHtml() {
        final Namespace libraryRoot = SysMLFactory.eINSTANCE.createNamespace();
        final Namespace isqBase = SysMLFactory.eINSTANCE.createPackage();
        isqBase.setDeclaredName("ISQBase");
        addOwnedMember(libraryRoot, isqBase);
        final AttributeDefinition lengthUnit =
                SysMLFactory.eINSTANCE.createAttributeDefinition();
        lengthUnit.setDeclaredName("LengthUnit");
        addOwnedMember(isqBase, lengthUnit);
        final XMIResourceImpl libraryResource =
                new XMIResourceImpl(URI.createURI("memory:/ISQBase.sysmlx"));
        libraryResource.getContents().add(libraryRoot);
        libraryResource.setID(lengthUnit,
                "e36901f2-5656-5f6e-8e99-c9ee71043958");

        final Namespace projectRoot = SysMLFactory.eINSTANCE.createNamespace();
        final Namespace si = SysMLFactory.eINSTANCE.createPackage();
        si.setDeclaredName("SI");
        addOwnedMember(projectRoot, si);
        final AttributeUsage metre =
                SysMLFactory.eINSTANCE.createAttributeUsage();
        metre.setDeclaredShortName("m");
        metre.setDeclaredName("metre");
        addOwnedMember(si, metre);
        final FeatureTyping typing =
                SysMLFactory.eINSTANCE.createFeatureTyping();
        typing.setTypedFeature(metre);
        final AttributeDefinition proxy =
                SysMLFactory.eINSTANCE.createAttributeDefinition();
        ((InternalEObject) proxy).eSetProxyURI(URI.createURI(
                "memory:/ISQBase.sysmlx#e36901f2-5656-5f6e-8e99-c9ee71043958"));
        typing.setType(proxy);
        metre.getOwnedRelationship().add(typing);
        final XMIResourceImpl projectResource =
                new XMIResourceImpl(URI.createURI("memory:/SI.sysmlx"));
        projectResource.getContents().add(projectRoot);
        final ResourceSetImpl resourceSet = new ResourceSetImpl();
        resourceSet.getResources().add(libraryResource);
        resourceSet.getResources().add(projectResource);

        final SysMLSerializer serializer = new SysMLSerializer();
        final SerializationRequest request = SerializationRequest.forElement(projectRoot)
                .withContextualNames(CrossResourceReferenceSerializationTest::callerName);
        final String html = serializer.serializeToHtml(request
                .withHtmlLinks(target -> {
                    if (target == proxy) {
                        return Optional
                                .of("ISQBase.html#LengthUnit");
                    }
                    return Optional.empty();
                }));
        final String serialized = serializer.serialize(request);

        assertTrue(serialized.contains(
                "attribute <m> metre : ISQBase::LengthUnit;"), serialized);
        assertTrue(html.contains(
                "href=\"ISQBase.html#LengthUnit\""), html);
        assertSame(proxy, typing.eGet(SysMLPackage.eINSTANCE.getFeatureTyping_Type(), false));
    }

    /**
     * Model queries for aliases and feature chains must name targets in another Resource through the caller, without
     * resolving their proxies.
     */
    @Test
    void namesCrossResourceAliasAndFeatureChainTargetsThroughTheCaller() {
        final Namespace libraryRoot = SysMLFactory.eINSTANCE.createNamespace();
        final Namespace base = SysMLFactory.eINSTANCE.createPackage();
        base.setDeclaredName("Base");
        addOwnedMember(libraryRoot, base);
        final DataType dataValue = SysMLFactory.eINSTANCE.createDataType();
        dataValue.setDeclaredName("DataValue");
        addOwnedMember(base, dataValue);
        final Feature startShot = SysMLFactory.eINSTANCE.createFeature();
        startShot.setDeclaredName("startShot");
        addOwnedMember(base, startShot);
        final XMIResourceImpl libraryResource =
                new XMIResourceImpl(URI.createURI("memory:/Base.kermlx"));
        libraryResource.getContents().add(libraryRoot);
        libraryResource.setID(dataValue, "data-value-id");
        libraryResource.setID(startShot, "start-shot-id");

        final Namespace projectRoot = SysMLFactory.eINSTANCE.createNamespace();
        final Namespace attributes = SysMLFactory.eINSTANCE.createPackage();
        attributes.setDeclaredName("Attributes");
        addOwnedMember(projectRoot, attributes);
        final Membership alias = SysMLFactory.eINSTANCE.createMembership();
        alias.setMemberName("AttributeValue");
        final DataType dataValueProxy = SysMLFactory.eINSTANCE.createDataType();
        ((InternalEObject) dataValueProxy).eSetProxyURI(
                URI.createURI("memory:/Base.kermlx#data-value-id"));
        alias.setMemberElement(dataValueProxy);
        attributes.getOwnedRelationship().add(alias);
        final Feature path = SysMLFactory.eINSTANCE.createFeature();
        addOwnedMember(attributes, path);
        final FeatureChaining chaining =
                SysMLFactory.eINSTANCE.createFeatureChaining();
        final Feature startShotProxy = SysMLFactory.eINSTANCE.createFeature();
        ((InternalEObject) startShotProxy).eSetProxyURI(
                URI.createURI("memory:/Base.kermlx#start-shot-id"));
        chaining.setChainingFeature(startShotProxy);
        path.getOwnedRelationship().add(chaining);
        final XMIResourceImpl projectResource =
                new XMIResourceImpl(URI.createURI("memory:/Attributes.sysmlx"));
        projectResource.getContents().add(projectRoot);
        final ResourceSetImpl resourceSet = new ResourceSetImpl();
        resourceSet.getResources().add(libraryResource);
        resourceSet.getResources().add(projectResource);

        final SerializerModelQueries queries = new SerializerModelQueries(false, CrossResourceReferenceSerializationTest::callerName);
        final String aliasTarget = queries.referenceTargets().aliasTargetName(alias,
                DanglingReferenceIndex.empty());
        final List<String> chainedNames = queries.nestedTargets().chainedFeatureNamesInSubtree(
                path, DanglingReferenceIndex.empty());

        assertEquals("Base::DataValue", aliasTarget);
        assertEquals(List.of("Base::startShot"), chainedNames);
        assertSame(dataValueProxy, alias.eGet(SysMLPackage.eINSTANCE.getMembership_MemberElement(), false));
        assertSame(startShotProxy, chaining.eGet(SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature(), false));
    }

    /**
     * Reading a reference must not modify the model: a cross-resource proxy held by a many-valued reference must stay
     * installed after the dangling-reference probe and after serialization, and the text must still name its target.
     */
    @Test
    void readsProxyListsWithoutResolvingThemInPlace() {
        final Namespace libraryRoot = SysMLFactory.eINSTANCE.createNamespace();
        final Element known = SysMLFactory.eINSTANCE.createPartDefinition();
        known.setDeclaredName("Known");
        addOwnedMember(libraryRoot, known);
        final XMIResourceImpl libraryResource = new XMIResourceImpl(URI.createURI("memory:/known.xmi"));
        libraryResource.getContents().add(libraryRoot);
        libraryResource.setID(known, "known-id");

        final Namespace projectRoot = SysMLFactory.eINSTANCE.createNamespace();
        final Element local = SysMLFactory.eINSTANCE.createPartDefinition();
        local.setDeclaredName("Local");
        addOwnedMember(projectRoot, local);
        final Dependency dependency = SysMLFactory.eINSTANCE.createDependency();
        final Element proxy = SysMLFactory.eINSTANCE.createPartDefinition();
        ((InternalEObject) proxy).eSetProxyURI(URI.createURI("memory:/known.xmi#known-id"));
        dependency.getSupplier().add(local);
        addOwnedMember(projectRoot, dependency);
        dependency.getClient().add(proxy);
        final XMIResourceImpl projectResource = new XMIResourceImpl(URI.createURI("memory:/project.xmi"));
        projectResource.getContents().add(projectRoot);
        final ResourceSetImpl resourceSet = new ResourceSetImpl();
        resourceSet.getResources().add(libraryResource);
        resourceSet.getResources().add(projectResource);
        final DanglingReferenceIndex references = DanglingReferenceIndex.from(Set.of(new DanglingLink(dependency,
                SysMLPackage.eINSTANCE.getDependency_Client(), SysMLPackage.eINSTANCE.getElement(), List.of("Known"))));

        final boolean resolvedTargets = references.hasResolvedTargets();
        final String serialized = new SysMLSerializer().serialize(SerializationRequest.forElement(projectRoot)
                .withDanglingReferences(references));

        assertFalse(resolvedTargets);
        assertSame(proxy, ((InternalEList<?>) dependency.getClient()).basicGet(0));
        assertTrue(serialized.contains("dependency from Known to Local;"), serialized);
    }

    /**
     * Names a proxy the way a caller that knows its resources would: by resolving it itself, outside the serializer,
     * without installing the result. Non-proxy targets are left to the serializer.
     */
    private static String callerName(final Element target, final Element context) {
        String name = "";
        if (target.eIsProxy()) {
            EObject resolved = EcoreUtil.resolve(target, context);
            if (resolved instanceof final Membership membership && !resolved.eIsProxy()) {
                resolved = membership.getMemberElement();
            }
            if (resolved instanceof final Element element && !element.eIsProxy()) {
                name = element.getQualifiedName();
            }
        }
        return name;
    }

    private static OwningMembership addOwnedMember(final Namespace namespace,
            final Element member) {
        final OwningMembership membership =
                SysMLFactory.eINSTANCE.createOwningMembership();
        membership.setOwnedMemberElement(member);
        namespace.getOwnedRelationship().add(membership);
        return membership;
    }
}
