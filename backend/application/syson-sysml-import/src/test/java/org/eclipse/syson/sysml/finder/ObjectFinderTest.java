/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.sysml.finder;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.mapper.MappingElement;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Facilitates the discovery and retrieval of EObjects based on identifiers or patterns.
 *
 * @author gescande
 */
public class ObjectFinderTest {

    public static final String NAMESPACE_CONST = "Namespace::Package::Identifier";

    static MappingElement getDataObject() {

        // Prepare eObject
        Namespace parentNamespace = (Namespace) EcoreUtil.create(SysmlPackage.eINSTANCE.getNamespace());
        Package originPackage = (Package) EcoreUtil.create(SysmlPackage.eINSTANCE.getPackage());
        OwningMembership membership = (OwningMembership) EcoreUtil.create(SysmlPackage.eINSTANCE.getOwningMembership());

        membership.setOwningRelatedElement(parentNamespace);
        originPackage.setOwningRelationship(membership);

        //Prepare Json Node
        ObjectMapper mapper = new ObjectMapper();

        JsonNode packageNode = mapper.createObjectNode();
        JsonNode metaNode = mapper.createObjectNode();

        ((ObjectNode) packageNode).set("$meta", metaNode);
        ((ObjectNode) metaNode).put("elementId", NAMESPACE_CONST);

        // Prepare Mapping
        MappingElement mapping = new MappingElement(packageNode, parentNamespace);
        mapping.setSelf(originPackage);

        return mapping;
    }

    @Test
    void findInstanceDirectSearchTest() {

        // Get Initial data
        MappingElement mapping = getDataObject();

        // Functionnal test
        ObjectFinder finder = new ObjectFinder();

        // Put Element
        finder.putElement(mapping);

        // Find Element
        EObject foundedElement = finder.findObject(mapping, mapping.getMainNode(), SysmlPackage.eINSTANCE.getPackage());

        // Assert direct search
        assertEquals(mapping.getSelf(), foundedElement);
        assertEquals(1, finder.getStatFindObject());
        assertEquals(0, finder.getStatFindReference());
    }

    @Test
    void findReferenceDirectSearchAttributeReferenceTest() {

        // Get Initial data
        MappingElement originalDataMapping = getDataObject();

        // Create other object with containment reference
        OwningMembership membership = (OwningMembership) EcoreUtil.create(SysmlPackage.eINSTANCE.getOwningMembership());
        membership.setMemberElement((Element) originalDataMapping.getSelf());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode referenceNode = mapper.createObjectNode();
        ((ObjectNode) referenceNode).put(AstConstant.REFERENCE_CONST, NAMESPACE_CONST);
        ((ObjectNode) referenceNode).put(AstConstant.TEXT_CONST, "unknown");
        MappingElement mapping = new MappingElement(referenceNode, null);
        mapping.setSelf(membership);

        // Functionnal test
        ObjectFinder finder = new ObjectFinder();

        // Put Element
        finder.putElement(originalDataMapping);

        // Find Element
        EObject foundedElement = finder.findObject(mapping, mapping.getMainNode(), SysmlPackage.eINSTANCE.getPackage());

        // Assert direct search
        assertEquals(originalDataMapping.getSelf(), foundedElement);
        assertEquals(1, finder.getStatFindReference());
        assertEquals(1, finder.getStatFindDirectSearch());
    }

    @Test
    void findReferenceDirectSearchAttributeTextTest() {

        // Get Initial data
        MappingElement originalDataMapping = getDataObject();

        // Create other object with containment reference
        OwningMembership membership = (OwningMembership) EcoreUtil.create(SysmlPackage.eINSTANCE.getOwningMembership());
        membership.setMemberElement((Element) originalDataMapping.getSelf());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode referenceNode = mapper.createObjectNode();
        ((ObjectNode) referenceNode).put(AstConstant.REFERENCE_CONST, NAMESPACE_CONST);
        MappingElement mapping = new MappingElement(referenceNode, null);
        mapping.setSelf(membership);

        // Functionnal test
        ObjectFinder finder = new ObjectFinder();

        // Put Element
        finder.putElement(originalDataMapping);

        // Find Element
        EObject foundedElement = finder.findObject(mapping, mapping.getMainNode(), SysmlPackage.eINSTANCE.getPackage());

        // Assert direct search
        assertEquals(originalDataMapping.getSelf(), foundedElement);
        assertEquals(1, finder.getStatFindReference());
        assertEquals(1, finder.getStatFindDirectSearch());
    }
    
    @Test
    void findReferenceStaticSearchTest() {

        // Get Initial data
        MappingElement originalDataMapping = getDataObject();

        // Create other object with containment reference
        OwningMembership membership = (OwningMembership) EcoreUtil.create(SysmlPackage.eINSTANCE.getOwningMembership());
        membership.setMemberElement((Element) originalDataMapping.getSelf());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode referenceNode = mapper.createObjectNode();
        ((ObjectNode) referenceNode).put(AstConstant.REFERENCE_CONST, "Identifier");
        MappingElement mapping = new MappingElement(referenceNode, null);
        mapping.setSelf(membership);

        // Functionnal test
        ObjectFinder finder = new ObjectFinder();

        // Put Element
        finder.putElement(originalDataMapping);

        // Add import
        finder.addImportMember(NAMESPACE_CONST);

        // Find Element
        EObject foundedElement = finder.findObject(mapping, mapping.getMainNode(), SysmlPackage.eINSTANCE.getPackage());

        // Assert direct search
        assertEquals(originalDataMapping.getSelf(), foundedElement);
        assertEquals(1, finder.getStatFindReference());
        assertEquals(1, finder.getStatFindStaticImport());
    }
    
    @Test
    void findReferenceDynamicSearchTest() {

        // Get Initial data
        MappingElement originalDataMapping = getDataObject();

        // Create other object with containment reference
        OwningMembership membership = (OwningMembership) EcoreUtil.create(SysmlPackage.eINSTANCE.getOwningMembership());
        membership.setMemberElement((Element) originalDataMapping.getSelf());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode referenceNode = mapper.createObjectNode();
        ((ObjectNode) referenceNode).put(AstConstant.REFERENCE_CONST, "Identifier");
        MappingElement mapping = new MappingElement(referenceNode, null);
        mapping.setSelf(membership);

        // Functionnal test
        ObjectFinder finder = new ObjectFinder();

        // Put Element
        finder.putElement(originalDataMapping);

        // Add import
        finder.addImportNamespace("Namespace");

        // Find Element
        EObject foundedElement = finder.findObject(mapping, mapping.getMainNode(), SysmlPackage.eINSTANCE.getPackage());

        // Assert direct search
        assertEquals(originalDataMapping.getSelf(), foundedElement);
        assertEquals(1, finder.getStatFindReference());
        assertEquals(0, finder.getStatFindSimpleName());
        assertEquals(0, finder.getStatFindDirectSearch());
        assertEquals(0, finder.getStatFindStaticImport());
        assertEquals(1, finder.getStatFindDynamicImport());
    }
    
    @Test
    void findReferenceSimpleNameSearchTest() {

        // Get Initial data
        MappingElement originalDataMapping = getDataObject();

        // Create other object with containment reference
        OwningMembership membership = (OwningMembership) EcoreUtil.create(SysmlPackage.eINSTANCE.getOwningMembership());
        membership.setMemberElement((Element) originalDataMapping.getSelf());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode referenceNode = mapper.createObjectNode();
        ((ObjectNode) referenceNode).put(AstConstant.REFERENCE_CONST, "Identifier");
        MappingElement mapping = new MappingElement(referenceNode, null);
        mapping.setSelf(membership);

        // Functionnal test
        ObjectFinder finder = new ObjectFinder();

        // Put Element
        finder.putElement(originalDataMapping);

        // Find Element
        EObject foundedElement = finder.findObject(mapping, mapping.getMainNode(), SysmlPackage.eINSTANCE.getPackage());

        // Assert direct search
        assertEquals(originalDataMapping.getSelf(), foundedElement);
        assertEquals(1, finder.getStatFindReference());
        assertEquals(1, finder.getStatFindSimpleName());
    }
    
    @Test
    void findReferenceAliasSearchTest() {
    
        // Get Initial data
        MappingElement originalDataMapping = getDataObject();
    
        // Create other object with containment reference
        OwningMembership membership = (OwningMembership) EcoreUtil.create(SysmlPackage.eINSTANCE.getOwningMembership());
        membership.setMemberElement((Element) originalDataMapping.getSelf());
    
        ObjectMapper mapper = new ObjectMapper();
        JsonNode referenceNode = mapper.createObjectNode();
        ((ObjectNode) referenceNode).put(AstConstant.REFERENCE_CONST, "Alias");
        MappingElement mapping = new MappingElement(referenceNode, null);
        mapping.setSelf(membership);
    
        // Functionnal test
        ObjectFinder finder = new ObjectFinder();
    
        // Put Element
        finder.putElement(originalDataMapping);
    
        // Set Alias
        finder.addImportAlias("Alias", NAMESPACE_CONST);
    
        // Find Element
        EObject foundedElement = finder.findObject(mapping, mapping.getMainNode(), SysmlPackage.eINSTANCE.getPackage());
    
        // Assert direct search
        assertEquals(originalDataMapping.getSelf(), foundedElement);
        assertEquals(1, finder.getStatFindReference());
        assertEquals(1, finder.getStatFindAliasImport());
        assertEquals(1, finder.getStatFindDirectSearch());
    }
    
    @Test
    void findReferenceAliasSearchDoubleAliasTest() {
    
        // Get Initial data
        MappingElement originalDataMapping = getDataObject();
    
        // Create other object with containment reference
        OwningMembership membership = (OwningMembership) EcoreUtil.create(SysmlPackage.eINSTANCE.getOwningMembership());
        membership.setMemberElement((Element) originalDataMapping.getSelf());
    
        ObjectMapper mapper = new ObjectMapper();
        JsonNode referenceNode = mapper.createObjectNode();
        ((ObjectNode) referenceNode).put(AstConstant.REFERENCE_CONST, "Alias1");
        MappingElement mapping = new MappingElement(referenceNode, null);
        mapping.setSelf(membership);
    
        // Functionnal test
        ObjectFinder finder = new ObjectFinder();
    
        // Put Element
        finder.putElement(originalDataMapping);
    
        // Set Alias
        finder.addImportAlias("Alias1", "Alias2");
        finder.addImportAlias("Alias2", NAMESPACE_CONST);
    
        // Find Element
        EObject foundedElement = finder.findObject(mapping, mapping.getMainNode(), SysmlPackage.eINSTANCE.getPackage());
    
        // Assert direct search
        assertEquals(originalDataMapping.getSelf(), foundedElement);
        assertEquals(1, finder.getStatFindReference());
        assertEquals(2, finder.getStatFindAliasImport());
        assertEquals(1, finder.getStatFindDirectSearch());
    }
    
}