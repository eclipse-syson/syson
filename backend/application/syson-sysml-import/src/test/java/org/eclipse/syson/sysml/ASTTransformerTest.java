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
package org.eclipse.syson.sysml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.application.configuration.SysMLStandardLibrariesConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test of ASTTransformer class.
 *
 * @author gescande
 */
public class ASTTransformerTest {

    private static List<Resource> standardLibraries;

    private ASTTransformer transformer;

    @AfterAll
    public static void afterAll() {
        standardLibraries = null;
    }

    @BeforeAll
    public static void beforeAll() {
        standardLibraries = new ArrayList<>(new SysMLStandardLibrariesConfiguration().getLibrariesResourceSet().getResources());
    }

    @BeforeEach
    public void setUp() {
        this.transformer = new ASTTransformer();
    }

    @DisplayName("Test null AST")
    @Test
    void convertNullTest() {
        Resource result = this.transformer.convertResource(null, new ResourceSetImpl());

        assertNull(result);
    }

    @DisplayName("Test empty string")
    @Test
    void convertEmptyStringTest() {
        String fileContent = "";
        Resource result = this.transformer.convertResource(new ByteArrayInputStream(fileContent.getBytes()), new ResourceSetImpl());

        assertFalse(result.getAllContents().hasNext());
    }

    @DisplayName("Test Empty AST")
    @Test
    void convertEmptyJsonTest() {
        String fileContent = "{}";
        Resource result = this.transformer.convertResource(new ByteArrayInputStream(fileContent.getBytes()), new ResourceSetImpl());

        assertFalse(result.getAllContents().hasNext());
    }

    @DisplayName("Test Empty Array AST")
    @Test
    void convertEmptyJsonArrayTest() {
        String fileContent = "[]";
        Resource result = this.transformer.convertResource(new ByteArrayInputStream(fileContent.getBytes()), new ResourceSetImpl());

        assertFalse(result.getAllContents().hasNext());
    }

    @DisplayName("Test Boolean parsing")
    @Test
    void convertBooleanValuesTest() {
        Resource testResource = this.getResourceFromFile("ASTTransformerTest/convertBooleanTest/boolean.ast.json", true);
        assertNotNull(testResource);

        assertEquals(1, testResource.getContents().size());
        Namespace namespace = (Namespace) testResource.getContents().get(0);
        assertEquals(1, namespace.getMember().size());
        Package packageObject = (Package) namespace.getMember().get(0);
        assertEquals(16, packageObject.getOwnedMember().size());

        // test isReference
        assertInstanceOf(ItemDefinition.class, packageObject.getMember().get(1));
        ItemDefinition isReferenceItemDefinition = (ItemDefinition) packageObject.getMember().get(1);
        assertEquals(1, isReferenceItemDefinition.getOwnedMember().size());
        assertInstanceOf(PartUsage.class, isReferenceItemDefinition.getOwnedMember().get(0));
        PartUsage member = (PartUsage) isReferenceItemDefinition.getOwnedMember().get(0);
        assertTrue(member.isIsReference());

        // test isRecursive
        assertEquals(1, packageObject.getOwnedImport().size());
        assertTrue(packageObject.getOwnedImport().get(0).isIsRecursive());

        // test isNegated
        assertInstanceOf(OwningMembership.class, packageObject.getOwnedMembership().get(6));
        Membership isNegatedMembership = packageObject.getOwnedMembership().get(6);
        assertInstanceOf(SatisfyRequirementUsage.class, isNegatedMembership.getMemberElement());
        SatisfyRequirementUsage isNegatedSatisfyRequirementUsage = (SatisfyRequirementUsage) isNegatedMembership.getMemberElement();
        assertTrue(isNegatedSatisfyRequirementUsage.isIsNegated());

        // test isAlias
        // isAlias is not part of the sysml specification and therefore not part of our model.
        // isAlias is present in the intermediary .ast.json format, so this blurb was introduced.

        // test isInitial
        assertInstanceOf(AttributeUsage.class, packageObject.getMember().get(7));
        AttributeUsage isInitialTestAttribute = (AttributeUsage) packageObject.getMember().get(7);
        assertTrue(isInitialTestAttribute.getValuation().isIsInitial());

        // test isVariation
        assertInstanceOf(PartDefinition.class, packageObject.getMember().get(8));
        OccurrenceDefinition isVariationPartDef = (PartDefinition) packageObject.getMember().get(8);
        assertTrue(isVariationPartDef.isIsVariation());

        // test isIndividual
        assertInstanceOf(OccurrenceDefinition.class, packageObject.getMember().get(9));
        OccurrenceDefinition isIndividualOccurenceDef = (OccurrenceDefinition) packageObject.getMember().get(9);
        assertTrue(isIndividualOccurenceDef.isIsIndividual());

        // test isDefault
        assertInstanceOf(AttributeUsage.class, packageObject.getMember().get(10));
        AttributeUsage isDefaultTestAttribute = (AttributeUsage) packageObject.getMember().get(10);
        assertInstanceOf(FeatureValue.class, isDefaultTestAttribute.getValuation());
        assertTrue(isDefaultTestAttribute.getValuation().isIsDefault());

        // test isParallel
        assertInstanceOf(StateUsage.class, packageObject.getMember().get(11));
        StateUsage isParallelState = (StateUsage) packageObject.getMember().get(11);
        assertTrue(isParallelState.isIsParallel());

        // test isAbstract
        assertInstanceOf(ItemDefinition.class, packageObject.getMember().get(12));
        ItemDefinition isAbstractTestItemDef = (ItemDefinition) packageObject.getMember().get(12);
        assertTrue(isAbstractTestItemDef.isIsAbstract());

        // test isEnd
        assertInstanceOf(ReferenceUsage.class, packageObject.getMember().get(13));
        ReferenceUsage isEndTestReference = (ReferenceUsage) packageObject.getMember().get(13);
        assertTrue(isEndTestReference.isIsEnd());

        // test isReadOnly
        assertInstanceOf(AttributeUsage.class, packageObject.getMember().get(14));
        AttributeUsage isReadOnlyTestAttribute = (AttributeUsage) packageObject.getMember().get(14);
        assertTrue(isReadOnlyTestAttribute.isIsReadOnly());

        // test isDerived
        assertInstanceOf(AttributeUsage.class, packageObject.getMember().get(15));
        AttributeUsage isDerivedTestAttribute = (AttributeUsage) packageObject.getMember().get(15);
        assertTrue(isDerivedTestAttribute.isIsDerived());
    }

    @DisplayName("Test Root namespace")
    @Test
    void convertEmptyNamespareTest() {

        String fileContent = """
                {
                    "$type": "Namespace",
                    "children": []
                }
                """;
        Resource result = this.transformer.convertResource(new ByteArrayInputStream(fileContent.getBytes()), new ResourceSetImpl());

        var content = result.getAllContents();
        assertTrue(content.hasNext());
        EObject object = content.next();
        assertFalse(content.hasNext());
        assertInstanceOf(Namespace.class, object);
    }

    @DisplayName("Test Containement reference")
    @Test
    void convertNamespareWithPackageTest() {
        String fileContent = """
                {
                    "$type": "Namespace",
                    "children": [
                        {
                            "$type": "OwningMembership",
                            "target": {
                                "$type": "Package",
                                "declaredName": "package"
                            }
                        }
                    ]
                }
                """;
        Resource result = this.transformer.convertResource(new ByteArrayInputStream(fileContent.getBytes()), new ResourceSetImpl());

        var content = result.getContents();
        assertEquals(1, content.size());
        EObject object = content.get(0);
        assertInstanceOf(Namespace.class, object);

        Namespace namespace = (Namespace) object;
        assertEquals(1, namespace.getOwnedElement().size());
        assertNotNull(namespace.getOwnedRelationship().get(0).getOwnedElement());
        assertEquals(1, namespace.getMember().size());
        EObject packageObject = namespace.getMember().get(0);
        assertInstanceOf(Package.class, packageObject);

        assertEquals("package", ((Package) packageObject).getDeclaredName());
        assertEquals("package", ((Package) packageObject).getQualifiedName());
    }

    @DisplayName("Test that an error is raised when a proxy failed to resolve")
    @Test
    void testProxyResolutionFailure() {
        // One error expected from failing resolution
        Resource testResource = this.getResourceFromFile("ASTTransformerTest/convertProjectWithErrors/proxyResolutionError.ast.json", false);
        assertNotNull(testResource);

        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);
        Package packageObject = (Package) namespace.getMember().get(0);

        assertEquals("p1", packageObject.getName());

        EList<Element> ownedElement = packageObject.getOwnedElement();

        assertEquals(1, ownedElement.size());

        // Failed resolution => No more proxy
        EList<Specialization> specialization = ((PartUsage) ownedElement.get(0)).getOwnedSpecialization();

        // 2 : FeatureTyping + Subsetting
        assertEquals(2, specialization.size());
        // The proxy to the type have unset
        assertNull(((FeatureTyping) specialization.get(0)).getType());

        // On error is raised
        assertEquals(1, this.transformer.getTransformationMessages().size());
        Message errorMessage = this.transformer.getTransformationMessages().get(0);
        assertEquals(MessageLevel.ERROR, errorMessage.level());
        assertTrue(errorMessage.body().contains("FakeType"));

    }

    @DisplayName("Test FeatureTyping")
    @Test
    void convertFeatureTypingTest() {
        Resource testResource = this.getResourceFromFile("ASTTransformerTest/convertFeatureTypingTest/featureTyping.ast.json", true);
        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);
        Package packageObject = (Package) namespace.getMember().get(0);

        EObject attributeDefinition = packageObject.getMember().get(0);
        assertInstanceOf(AttributeDefinition.class, attributeDefinition);
        AttributeDefinition typedAttributeDefinition = (AttributeDefinition) attributeDefinition;
        assertEquals("attributeDefinition", typedAttributeDefinition.getDeclaredName());
        assertEquals("package::attributeDefinition", typedAttributeDefinition.getQualifiedName());

        EObject portDefinition = packageObject.getMember().get(1);
        assertInstanceOf(PortDefinition.class, portDefinition);
        PortDefinition typedPortDefinition = (PortDefinition) portDefinition;
        assertEquals("portDefinition", typedPortDefinition.getDeclaredName());

        assertEquals(1, typedPortDefinition.getOwnedMember().size());
        EObject referenceUsage = typedPortDefinition.getOwnedMember().get(0);
        assertInstanceOf(ReferenceUsage.class, referenceUsage);
        ReferenceUsage typedReferenceUsage = (ReferenceUsage) referenceUsage;
        assertEquals("referenceUsage", typedReferenceUsage.getDeclaredName());

        assertEquals(1, typedReferenceUsage.getOwnedTyping().size());
        FeatureTyping featureTyping = typedReferenceUsage.getOwnedTyping().get(0);

        assertEquals(typedReferenceUsage, featureTyping.getTypedFeature());
        assertEquals(typedAttributeDefinition, featureTyping.getType());

        assertEquals(1, typedReferenceUsage.getOwnedRelationship().size());
        assertEquals(1, typedReferenceUsage.getOwnedSpecialization().size());
        Specialization specialisation = typedReferenceUsage.getOwnedSpecialization().get(0);
        assertEquals(typedAttributeDefinition, specialisation.getGeneral());
    }

    @DisplayName("Test Redefines")
    @Test
    void convertRedefinesTest() {
        Resource testResource = this.getResourceFromFile("ASTTransformerTest/convertRedefinesTest/redefines.ast.json", true);
        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);
        Package packageObject = (Package) namespace.getMember().get(0);

        EObject attributeUsage = packageObject.getMember().get(0);
        assertInstanceOf(AttributeUsage.class, attributeUsage);
        AttributeUsage typedAttributeUsage = (AttributeUsage) attributeUsage;
        assertEquals("Packets::packetHeader", typedAttributeUsage.getQualifiedName());

        EObject redefinedAttributeUsage = packageObject.getMember().get(1);
        assertInstanceOf(AttributeUsage.class, redefinedAttributeUsage);
        AttributeUsage typedRedefinedAttributeUsage = (AttributeUsage) redefinedAttributeUsage;
        assertEquals("Packets::packetSecondaryHeader", typedRedefinedAttributeUsage.getQualifiedName());

        assertEquals(1, typedRedefinedAttributeUsage.getOwnedRelationship().size());
        assertEquals(1, typedRedefinedAttributeUsage.getOwnedRedefinition().size());
        Redefinition redefinition = typedRedefinedAttributeUsage.getOwnedRedefinition().get(0);

        assertEquals(typedAttributeUsage, redefinition.getRedefinedFeature());
        assertEquals(typedRedefinedAttributeUsage, redefinition.getRedefiningFeature());
    }

    @DisplayName("Test Visibility")
    @Test
    void convertVisibilityTest() {

        Resource testResource = this.getResourceFromFile("ASTTransformerTest/convertVisibilityTest/visibility.ast.json", true);

        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);
        Package packageObject = (Package) namespace.getMember().get(0);
        assertEquals(3, packageObject.getMember().size());

        assertInstanceOf(ItemDefinition.class, packageObject.getMember().get(1));
        ItemDefinition alphonse = (ItemDefinition) packageObject.getMember().get(1);
        assertEquals("A", alphonse.getName());
        assertEquals(VisibilityKind.PUBLIC, packageObject.getMembership().get(1).getVisibility());

        assertInstanceOf(ItemDefinition.class, packageObject.getMember().get(2));
        ItemDefinition charlie = (ItemDefinition) packageObject.getMember().get(2);
        assertEquals("C", charlie.getName());
        assertEquals(VisibilityKind.PRIVATE, packageObject.getMembership().get(2).getVisibility());

        assertEquals(1, alphonse.getOwnedMember().size());
        assertInstanceOf(PartUsage.class, alphonse.getOwnedMember().get(0));
        PartUsage delta = (PartUsage) alphonse.getOwnedMember().get(0);
        assertEquals("c", delta.getName());

        assertEquals(1, delta.getOwnedRelationship().size());
        assertInstanceOf(FeatureTyping.class, delta.getOwnedRelationship().get(0));
        FeatureTyping deltaFeatureTyping = (FeatureTyping) delta.getOwnedRelationship().get(0);
        assertNotNull(deltaFeatureTyping.getGeneral());
        assertNotNull(deltaFeatureTyping.getSpecific());

    }

    @DisplayName("Test NamespaceImport")
    @Test
    void convertNamespaceImportTest() {
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource namespaceResource = this.getResourceFromFile("ASTTransformerTest/convertNamespaceImportTest/namespace.ast.json", resourceSet, true);
        assertNotNull(namespaceResource);

        Resource testResource = this.getResourceFromFile("ASTTransformerTest/convertNamespaceImportTest/model.ast.json", resourceSet, true);
        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);
        Package packageObject = (Package) namespace.getMember().get(0);

        EObject namespaceImport = packageObject.getOwnedRelationship().get(0);
        assertInstanceOf(NamespaceImport.class, namespaceImport);
        NamespaceImport typedNamespaceImport = (NamespaceImport) namespaceImport;
        assertEquals("ScalarValues", typedNamespaceImport.getImportedNamespace().getName());

        Namespace importedNamespace = (Namespace) namespaceResource.getContents().get(0);
        Package importedPackage = (Package) importedNamespace.getMember().get(0);
        assertEquals(importedPackage, typedNamespaceImport.getImportedNamespace());
    }

    @DisplayName("Test isNonunique feature")
    @Test
    void nonUniqueFeatureTest() {
        Resource testResource = null;
        try {
            String fileContent = new String(this.getClass().getClassLoader().getResourceAsStream("ASTTransformerTest/isUniqueFeature/model.ast.json").readAllBytes());
            testResource = this.transformer.convertResource(new ByteArrayInputStream(fileContent.getBytes()), new ResourceSetImpl());
        } catch (IOException e) {
            fail(e);
        }

        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);

        Element secondMember = namespace.getMember().get(1);
        assertTrue(secondMember instanceof PartUsage);
        assertFalse(((PartUsage) secondMember).isIsUnique());

    }

    @DisplayName("Test Conjugated")
    @Test
    void convertConjugatedPortTest() {
        Resource testResource = this.getResourceFromFile("ASTTransformerTest/convertConjugatedPortTest/conjugatedPort.ast.json", true);
        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);
        Package packageObject = (Package) namespace.getMember().get(0);

        AttributeDefinition tempAttributeDefinition = (AttributeDefinition) packageObject.getMember().get(0);
        assertEquals("Temp", tempAttributeDefinition.getName());

        PortDefinition tempPortPortDefinition = (PortDefinition) packageObject.getMember().get(1);
        assertEquals("TempPort", tempPortPortDefinition.getName());
        AttributeUsage temperatureAttributeUsage = tempPortPortDefinition.getOwnedAttribute().get(0);
        assertEquals("temperature", temperatureAttributeUsage.getName());
        assertEquals(tempAttributeDefinition, temperatureAttributeUsage.getOwnedSpecialization().get(0).getGeneral());

        PartDefinition tempPortClassicPartDefinition = (PartDefinition) packageObject.getMember().get(2);
        assertEquals("TempPortClassic", tempPortClassicPartDefinition.getName());
        PortUsage tempPortClassicPortUsage = tempPortClassicPartDefinition.getOwnedPort().get(0);
        assertEquals("tempPortClassic", tempPortClassicPortUsage.getName());
        assertEquals(tempPortPortDefinition, tempPortClassicPortUsage.getOwnedSpecialization().get(0).getGeneral());

        PartDefinition tempPortConjPartDefinition = (PartDefinition) packageObject.getMember().get(3);
        assertEquals("TempPortConj", tempPortConjPartDefinition.getName());
        PortUsage tempPortConjPortUsage = tempPortConjPartDefinition.getOwnedPort().get(0);
        assertEquals("tempPortConj", tempPortConjPortUsage.getName());

        assertEquals(1, tempPortConjPortUsage.getOwnedTyping().size());
    }

    @DisplayName("Test Subclassification")
    @Test
    void convertSubclassificationTest() {
        Resource testResource = this.getResourceFromFile("ASTTransformerTest/convertSubclassificationTest/subclassification.ast.json", true);
        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);
        Package packageObject = (Package) namespace.getMember().get(0);

        PartDefinition part1Definition = (PartDefinition) packageObject.getMember().get(0);
        assertEquals("Part1", part1Definition.getName());
        AttributeUsage attribute1AttributeUsage = part1Definition.getOwnedAttribute().get(0);
        assertEquals("attribute1", attribute1AttributeUsage.getName());

        PartDefinition part2Definition = (PartDefinition) packageObject.getMember().get(1);
        assertEquals("Part2", part2Definition.getName());
        AttributeUsage attribute2AttributeUsage = part2Definition.getOwnedAttribute().get(0);
        assertEquals("attribute2", attribute2AttributeUsage.getName());

        assertEquals(1, part2Definition.getOwnedSubclassification().size());
        assertEquals(part1Definition, part2Definition.getOwnedSubclassification().get(0).getSuperclassifier());
        assertEquals(part2Definition, part2Definition.getOwnedSubclassification().get(0).getSubclassifier());

    }

    @DisplayName("Test Inheritance")
    @Test
    void convertInheritanceTest() {
        Resource testResource = this.getResourceFromFile("ASTTransformerTest/convertInheritanceTest/inheritance.ast.json", true);
        assertNotNull(testResource);

        assertEquals(1, testResource.getContents().size());
        Namespace namespace = (Namespace) testResource.getContents().get(0);
        assertEquals(1, namespace.getMember().size());
        Package packageObject = (Package) namespace.getMember().get(0);
        assertEquals(2, packageObject.getMember().size());

        PartDefinition definition = (PartDefinition) packageObject.getMember().get(0);
        assertEquals("Def", definition.getName());
        assertEquals(2, packageObject.getMember().size());
        Element elementA = definition.getOwnedElement().get(0);
        assertInstanceOf(PartUsage.class, elementA);
        PartUsage partUsageA = (PartUsage) elementA;
        assertEquals("a", partUsageA.getName());

        Element elementP = packageObject.getMember().get(1);
        assertInstanceOf(PartUsage.class, elementP);
        PartUsage partUsageP = (PartUsage) elementP;
        assertEquals("p", partUsageP.getName());
        assertEquals(1, partUsageP.getType().size());

        assertEquals(1, partUsageP.getOwnedElement().size());
        Element elementB = partUsageP.getOwnedElement().get(0);
        assertInstanceOf(ReferenceUsage.class, elementB);
        ReferenceUsage referenceUsageB = (ReferenceUsage) elementB;
        assertEquals("b", referenceUsageB.getName());
        assertEquals(partUsageA, referenceUsageB.getOwningNamespace().resolve("a").getMemberElement());
        assertEquals(partUsageA, referenceUsageB.getOwnedRedefinition().get(0).getRedefinedFeature());
    }

    @DisplayName("Test Alias")
    @Test
    void convertAliasTest() {
        Resource testResource = this.getResourceFromFile("ASTTransformerTest/convertAliasTest/alias.ast.json", true);
        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);
        Package packageObject = (Package) namespace.getMember().get(0);
        Package definitionsPackage = (Package) packageObject.getMember().get(0);
        assertEquals("Definitions", definitionsPackage.getName());
        assertEquals("AliasImport::Definitions", definitionsPackage.getQualifiedName());
        PartDefinition vehiculePartDefinition = (PartDefinition) definitionsPackage.getOwnedElement().get(0);
        assertEquals("Vehicle", vehiculePartDefinition.getName());

        Membership carMembership = definitionsPackage.getOwnedMembership().get(1);
        assertEquals("Car", carMembership.getMemberName());
        assertEquals(vehiculePartDefinition, carMembership.getMemberElement());
        assertEquals(definitionsPackage, carMembership.getOwningRelatedElement());
        assertEquals(definitionsPackage, carMembership.getMembershipOwningNamespace());

        assertEquals(definitionsPackage.getOwnedMembership().get(0), namespace.resolve("AliasImport::Definitions::Vehicle"));

        assertEquals(vehiculePartDefinition, namespace.resolve("AliasImport::Definitions::Vehicle").getMemberElement());
        assertEquals(vehiculePartDefinition, namespace.resolve("AliasImport::Definitions::Car").getMemberElement());

        // To check and fix if needed
        // assertEquals("AliasImport::Definitions::Car", carMembership.getQualifiedName());
        // assertEquals(definitionsPackage, carMembership.getOwningNamespace());
        // assertEquals(definitionsPackage, carMembership.getOwner());

        Package usagesPackage = (Package) packageObject.getMember().get(1);
        assertEquals("Usages", usagesPackage.getName());

        PartUsage vehiculePartUsage = (PartUsage) usagesPackage.getOwnedElement().get(0);
        assertEquals("vehicle", vehiculePartUsage.getName());

        assertEquals(vehiculePartDefinition, vehiculePartUsage.getOwnedSpecialization().get(0).getGeneral());
    }

    @DisplayName("Test NamespaceImportValue")
    @Test
    void convertNamespaceImportValueTest() {
        ResourceSet resourceSet = new ResourceSetImpl();

        Resource namespaceResource = this.getResourceFromFile("ASTTransformerTest/convertNamespaceImportValueTest/namespace.ast.json", resourceSet, true);
        assertNotNull(namespaceResource);

        Resource testResource = this.getResourceFromFile("ASTTransformerTest/convertNamespaceImportValueTest/model.ast.json", resourceSet, true);
        assertNotNull(testResource);

        Namespace importedNamespace = (Namespace) namespaceResource.getContents().get(0);
        Package package1Object = (Package) importedNamespace.getMember().get(0);
        PartDefinition part1Definition = (PartDefinition) package1Object.getMember().get(0);

        Namespace testNamespace = (Namespace) testResource.getContents().get(0);
        Package package2Object = (Package) testNamespace.getMember().get(0);

        NamespaceImport namespaceImport = (NamespaceImport) package2Object.getOwnedRelationship().get(0);
        assertEquals(package1Object, namespaceImport.getImportedNamespace());
        assertEquals(package2Object, namespaceImport.getImportOwningNamespace());
        assertNull(namespaceImport.getOwningNamespace());
        assertEquals(testNamespace, namespaceImport.getOwningRelatedElement().getOwningNamespace());
        PartDefinition part2Definition = (PartDefinition) package2Object.getMember().get(0);

        assertEquals(1, part2Definition.getOwnedSubclassification().size());
        assertEquals(part1Definition, part2Definition.getOwnedSubclassification().get(0).getSuperclassifier());
        assertEquals(part2Definition, part2Definition.getOwnedSubclassification().get(0).getSubclassifier());
    }

    @DisplayName("Test Import")
    @Test
    void convertImportTest() {
        Resource namespaceResource = this.getResourceFromFile("ASTTransformerTest/convertImportTest/import.ast.json", true);
        assertNotNull(namespaceResource);

        Namespace testNamespace = (Namespace) namespaceResource.getContents().get(0);

        PartUsage p11 = (PartUsage) testNamespace.resolve("ImportTest::Pkg1::p11").getMemberElement();
        PartDefinition p211 = (PartDefinition) testNamespace.resolve("ImportTest::Pkg2::Pkg21::Pkg211::P211").getMemberElement();
        PartDefinition p12 = (PartDefinition) testNamespace.resolve("ImportTest::Pkg1::P12").getMemberElement();

        assertEquals(p211, p11.getOwnedSpecialization().get(0).getGeneral());
        assertEquals(p12, p211.getOwnedSubclassification().get(0).getSuperclassifier());
    }

    @DisplayName("Test Documentation")
    @Test
    void convertDocumentationTest() {
        String fileContent = """
                {
                    "$type": "Namespace",
                    "children": [
                        {
                            "$type": "OwningMembership",
                            "target": {
                                "$type": "Documentation",
                                "body": "/* TEST */"
                            }
                        }
                    ]
                }
                """;
        Resource result = this.transformer.convertResource(new ByteArrayInputStream(fileContent.getBytes()), new ResourceSetImpl());

        var content = result.getContents();
        assertEquals(1, content.size());
        EObject object = content.get(0);
        assertInstanceOf(Namespace.class, object);

        Namespace namespace = (Namespace) object;
        assertEquals(1, namespace.getOwnedElement().size());
        assertNotNull(namespace.getOwnedRelationship().get(0).getOwnedElement());
        assertEquals(1, namespace.getMember().size());
        EObject documentationObject = namespace.getMember().get(0);
        assertInstanceOf(Documentation.class, documentationObject);

        assertEquals("TEST", ((Documentation) documentationObject).getBody());
    }

    @DisplayName("Test Assignment on AssignmentActionUsage")
    @Test
    void convertAssignment1Test() {
        Resource namespaceResource = this.getResourceFromFile("ASTTransformerTest/convertAssignmentTest/assignment1.ast.json", true);
        assertNotNull(namespaceResource);

        Namespace namespace = (Namespace) namespaceResource.getContents().get(0);
        Package packageAssignment1 = (Package) namespace.getMember().get(0);

        PartDefinition partDefinitionCounter = (PartDefinition) packageAssignment1.getMember().get(0);

        AttributeUsage attributeUsagecount = (AttributeUsage) partDefinitionCounter.getMember().get(0);

        assertEquals(0, ((LiteralInteger) attributeUsagecount.getValuation().getValue()).getValue());

        ActionUsage actionUsageincr = (ActionUsage) partDefinitionCounter.getMember().get(1);

        AssignmentActionUsage assignmentActionUsage = (AssignmentActionUsage) actionUsageincr.getMember().get(0);

        assertEquals(attributeUsagecount, assignmentActionUsage.getReferent());

        // to check and fix if needed
        // assertEquals(1, ((LiteralInteger) assignmentActionUsage.getTargetArgument()).getValue());

        Expression expression = assignmentActionUsage.getValueExpression(); // Expression -> 1

        assertNull(expression);

    }

    @DisplayName("Test Assignment on OperatorExpression")
    @Test
    void convertAssignment2Test() {
        Resource namespaceResource = this.getResourceFromFile("ASTTransformerTest/convertAssignmentTest/assignment2.ast.json", true);
        assertNotNull(namespaceResource);

        Namespace namespace = (Namespace) namespaceResource.getContents().get(0);
        Package packageAssignment1 = (Package) namespace.getMember().get(0);

        ActionUsage actionUsageCounter = (ActionUsage) packageAssignment1.getMember().get(0);
        assertFalse(actionUsageCounter.isIsReference());
        assertTrue(actionUsageCounter.isIsComposite());
        AttributeUsage attributeUsagecount = (AttributeUsage) actionUsageCounter.getMember().get(0);
        AssignmentActionUsage assignmentActionUsage = (AssignmentActionUsage) actionUsageCounter.getMember().get(1);

        assertEquals(attributeUsagecount, assignmentActionUsage.getReferent());

        // to check and fix if needed
        OperatorExpression operatorExpression = (OperatorExpression) assignmentActionUsage.getTargetArgument();
        // assertEquals("+", operatorExpression.getOperator());
        // To implement assertEquals(1, (LiteralInteger) operatorExpression.getOperand().get(0));

        Expression expression = assignmentActionUsage.getValueExpression(); // Expression -> 1

        assertNull(expression);

    }

    @DisplayName("Test Allocation Containment")
    @Test
    void convertAllocationTest() {
        Resource namespaceResource = this.getResourceFromFile("ASTTransformerTest/convertAllocationTest/allocation.ast.json", true);
        assertNotNull(namespaceResource);

        Namespace namespace = (Namespace) namespaceResource.getContents().get(0);
        Package packageAllocation = (Package) namespace.getMember().get(0);
        assertEquals("AllocationTest", packageAllocation.getName());
        assertEquals(2, packageAllocation.getMember().size());

        AllocationDefinition allocationDefinition = (AllocationDefinition) packageAllocation.getMember().get(0);
        assertEquals("A", allocationDefinition.getName());
        // Ensure that the AllocationDefinition is not treated as a Membership or Specialization
        assertNull(allocationDefinition.getOwningRelatedElement());
        assertEquals(packageAllocation, allocationDefinition.getOwningNamespace());
        assertInstanceOf(OwningMembership.class, allocationDefinition.getOwningMembership());

        AllocationUsage allocationUsage = (AllocationUsage) packageAllocation.getMember().get(1);
        assertEquals("allocation1", allocationUsage.getName());
        assertEquals(1, allocationUsage.getOwnedTyping().size());
        FeatureTyping allocationUsageFeatureTyping = allocationUsage.getOwnedTyping().get(0);
        // Ensure that the AllocationDefinition has been correctly resolved
        assertEquals(allocationDefinition, allocationUsageFeatureTyping.getType());
        assertEquals(packageAllocation, allocationUsage.getOwningNamespace());
        assertInstanceOf(OwningMembership.class, allocationUsage.getOwningMembership());

        assertEquals(1, allocationUsage.getOwnedFeatureMembership().size());
        FeatureMembership allocationUsageFeatureMembership = allocationUsage.getOwnedFeatureMembership().get(0);
        assertInstanceOf(PartUsage.class, allocationUsageFeatureMembership.getMemberElement());
        assertEquals(allocationUsage, allocationUsageFeatureMembership.getMemberElement().getOwningNamespace());
    }

    private Resource getResourceFromFile(String testFilePath, boolean noErrorExpected) {
        ResourceSetImpl resourceSet = new ResourceSetImpl();
        // Not the safest way to import libraries but enough for this use case a make the test run faster
        resourceSet.getResources().addAll(standardLibraries);
        return this.getResourceFromFile(testFilePath, resourceSet, noErrorExpected);
    }

    private Resource getResourceFromFile(String testFilePath, ResourceSet resourceSet, boolean noErrorExpected) {

        Resource testResource = null;
        try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream(testFilePath)) {
            assertNotNull(stream);
            String fileContent = new String(stream.readAllBytes());
            testResource = this.transformer.convertResource(new ByteArrayInputStream(fileContent.getBytes()), resourceSet);
            if (noErrorExpected) {
                List<Message> errorMessages = this.transformer.getTransformationMessages().stream().filter(m -> m.level() == MessageLevel.ERROR || m.level() == MessageLevel.WARNING).toList();
                assertThat(errorMessages).isEmpty();

            }
        } catch (IOException e) {
            fail(e);
        }
        return testResource;
    }

}
