/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.syson.sysml.services.SysMLParsingService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.application.configuration.SysONDefaultLibrariesConfiguration;
import org.eclipse.syson.application.configuration.SysONLoadDefaultLibrariesOnApplicationStartConfiguration;
import org.eclipse.syson.services.UtilService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Semantic import tests using SysML textual fixtures.
 *
 * @author gescande
 */
public class TextualModelImportTests {

    private static List<Resource> standardLibraries;

    private final SysMLParsingService parsingService = new SysMLParsingService();

    private List<Message> messages;

    private final UtilService utilService = new UtilService();

    @AfterAll
    public static void afterAll() {
        standardLibraries = null;
    }

    @BeforeAll
    public static void beforeAll() {
        standardLibraries = new ArrayList<>(new SysONDefaultLibrariesConfiguration(new SysONLoadDefaultLibrariesOnApplicationStartConfiguration()).getLibrariesResourceSet().getResources());
    }

    @DisplayName("GIVEN boolean literals and operators, WHEN importing text, THEN their semantic values and expressions are preserved")
    @Test
    void convertBooleanValuesTest() {
        Resource testResource = this.getResourceFromFile("TextualModelImportTests/convertBooleanTest/boolean.sysml", true);
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
        // isAlias is present in the intermediary .sysml format, so this blurb was introduced.

        // test isInitial
        assertInstanceOf(AttributeUsage.class, packageObject.getOwnedMember().get(7));
        AttributeUsage isInitialTestAttribute = (AttributeUsage) packageObject.getOwnedMember().get(7);
        assertTrue(this.utilService.getValuation(isInitialTestAttribute).isIsInitial());

        // test isVariation
        assertInstanceOf(PartDefinition.class, packageObject.getOwnedMember().get(8));
        OccurrenceDefinition isVariationPartDef = (PartDefinition) packageObject.getOwnedMember().get(8);
        assertTrue(isVariationPartDef.isIsVariation());

        // test isIndividual
        assertInstanceOf(OccurrenceDefinition.class, packageObject.getOwnedMember().get(9));
        OccurrenceDefinition isIndividualOccurenceDef = (OccurrenceDefinition) packageObject.getOwnedMember().get(9);
        assertTrue(isIndividualOccurenceDef.isIsIndividual());

        // test isDefault
        assertInstanceOf(AttributeUsage.class, packageObject.getOwnedMember().get(10));
        AttributeUsage isDefaultTestAttribute = (AttributeUsage) packageObject.getOwnedMember().get(10);
        assertInstanceOf(FeatureValue.class, this.utilService.getValuation(isDefaultTestAttribute));
        assertTrue(this.utilService.getValuation(isDefaultTestAttribute).isIsDefault());

        // test isParallel
        assertInstanceOf(StateUsage.class, packageObject.getOwnedMember().get(11));
        StateUsage isParallelState = (StateUsage) packageObject.getOwnedMember().get(11);
        assertTrue(isParallelState.isIsParallel());

        // test isAbstract
        assertInstanceOf(ItemDefinition.class, packageObject.getOwnedMember().get(12));
        ItemDefinition isAbstractTestItemDef = (ItemDefinition) packageObject.getOwnedMember().get(12);
        assertTrue(isAbstractTestItemDef.isIsAbstract());

        // test isEnd
        assertInstanceOf(ReferenceUsage.class, packageObject.getOwnedMember().get(13));
        ReferenceUsage isEndTestReference = (ReferenceUsage) packageObject.getOwnedMember().get(13);
        assertTrue(isEndTestReference.isIsEnd());

        // test isConstant
        assertInstanceOf(AttributeUsage.class, packageObject.getOwnedMember().get(14));
        AttributeUsage isConstantTestAttribute = (AttributeUsage) packageObject.getOwnedMember().get(14);
        assertTrue(isConstantTestAttribute.isIsConstant());

        // test isDerivedS
        assertInstanceOf(AttributeUsage.class, packageObject.getOwnedMember().get(15));
        AttributeUsage isDerivedTestAttribute = (AttributeUsage) packageObject.getOwnedMember().get(15);
        assertTrue(isDerivedTestAttribute.isIsDerived());
    }

    @DisplayName("GIVEN empty SysML text, WHEN importing, THEN a root namespace is created")
    @Test
    void convertEmptyNamespareTest() {

        Resource result = this.getResourceFromText("", new ResourceSetImpl());

        var content = result.getAllContents();
        assertTrue(content.hasNext());
        EObject object = content.next();
        assertFalse(content.hasNext());
        assertInstanceOf(Namespace.class, object);
    }

    @DisplayName("GIVEN a package declaration, WHEN importing text, THEN it is contained in the root namespace")
    @Test
    void convertNamespareWithPackageTest() {
        Resource result = this.getResourceFromText("package 'package';", new ResourceSetImpl());

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
        assertEquals("'package'", ((Package) packageObject).getQualifiedName());
    }

    @DisplayName("GIVEN an unknown type, WHEN importing text, THEN unresolved references produce warnings")
    @Test
    void testProxyResolutionFailure() {
        // One error expected from failing resolution
        Resource testResource = this.getResourceFromFile("TextualModelImportTests/convertProjectWithErrors/proxyResolutionError.sysml", false);
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
        assertEquals(1, this.messages.size());
        Message errorMessage = this.messages.get(0);
        assertEquals(MessageLevel.WARNING, errorMessage.level());
        assertTrue(errorMessage.body().contains("FakeType"));

    }

    @DisplayName("GIVEN a typed feature, WHEN importing text, THEN its type is resolved")
    @Test
    void convertFeatureTypingTest() {
        Resource testResource = this.getResourceFromFile("TextualModelImportTests/convertFeatureTypingTest/featureTyping.sysml", true);
        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);
        Package packageObject = (Package) namespace.getMember().get(0);

        EObject attributeDefinition = packageObject.getMember().get(0);
        assertInstanceOf(AttributeDefinition.class, attributeDefinition);
        AttributeDefinition typedAttributeDefinition = (AttributeDefinition) attributeDefinition;
        assertEquals("attributeDefinition", typedAttributeDefinition.getDeclaredName());
        assertEquals("'package'::attributeDefinition", typedAttributeDefinition.getQualifiedName());

        EObject portDefinition = packageObject.getMember().get(1);
        assertInstanceOf(PortDefinition.class, portDefinition);
        PortDefinition typedPortDefinition = (PortDefinition) portDefinition;
        assertEquals("portDefinition", typedPortDefinition.getDeclaredName());

        assertEquals(2, typedPortDefinition.getOwnedMember().size());
        EObject referenceUsage = typedPortDefinition.getOwnedMember().get(0);
        assertInstanceOf(ReferenceUsage.class, referenceUsage);
        ReferenceUsage typedReferenceUsage = (ReferenceUsage) referenceUsage;
        assertEquals("referenceUsage", typedReferenceUsage.getDeclaredName());
        EObject conjugatedPortDefinition = typedPortDefinition.getOwnedMember().get(1);
        assertInstanceOf(ConjugatedPortDefinition.class, conjugatedPortDefinition);

        assertEquals(1, typedReferenceUsage.getOwnedTyping().size());
        FeatureTyping featureTyping = typedReferenceUsage.getOwnedTyping().get(0);

        assertEquals(typedReferenceUsage, featureTyping.getTypedFeature());
        assertEquals(typedAttributeDefinition, featureTyping.getType());

        assertEquals(1, typedReferenceUsage.getOwnedRelationship().size());
        assertEquals(1, typedReferenceUsage.getOwnedSpecialization().size());
        Specialization specialisation = typedReferenceUsage.getOwnedSpecialization().get(0);
        assertEquals(typedAttributeDefinition, specialisation.getGeneral());
    }

    @DisplayName("GIVEN an inherited feature, WHEN importing a redefinition, THEN the redefined feature is resolved")
    @Test
    void convertRedefinesTest() {
        Resource testResource = this.getResourceFromFile("TextualModelImportTests/convertRedefinesTest/redefines.sysml", true);
        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);
        Package packageObject = (Package) namespace.getMember().get(0);

        EObject attributeUsage = ((PartDefinition) packageObject.getMember().get(0)).getOwnedAttribute().getFirst();
        assertInstanceOf(AttributeUsage.class, attributeUsage);
        AttributeUsage typedAttributeUsage = (AttributeUsage) attributeUsage;
        assertEquals("Packets::Base::packetHeader", typedAttributeUsage.getQualifiedName());

        EObject redefinedAttributeUsage = ((PartDefinition) packageObject.getMember().get(1)).getOwnedAttribute().getFirst();
        assertInstanceOf(AttributeUsage.class, redefinedAttributeUsage);
        AttributeUsage typedRedefinedAttributeUsage = (AttributeUsage) redefinedAttributeUsage;
        assertEquals("Packets::Packet::packetSecondaryHeader", typedRedefinedAttributeUsage.getQualifiedName());

        assertEquals(1, typedRedefinedAttributeUsage.getOwnedRedefinition().size());
        Redefinition redefinition = typedRedefinedAttributeUsage.getOwnedRedefinition().get(0);

        assertEquals(typedAttributeUsage, redefinition.getRedefinedFeature());
        assertEquals(typedRedefinedAttributeUsage, redefinition.getRedefiningFeature());
    }

    @DisplayName("GIVEN explicit visibility modifiers, WHEN importing text, THEN membership visibility is preserved")
    @Test
    void convertVisibilityTest() {

        Resource testResource = this.getResourceFromFile("TextualModelImportTests/convertVisibilityTest/visibility.sysml", true);

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

        assertEquals(1, delta.getOwnedTyping().size());
        FeatureTyping deltaFeatureTyping = delta.getOwnedTyping().getFirst();
        assertNotNull(deltaFeatureTyping.getGeneral());
        assertNotNull(deltaFeatureTyping.getSpecific());

    }

    @DisplayName("GIVEN a namespace import, WHEN importing text, THEN its target namespace is resolved")
    @Test
    void convertNamespaceImportTest() {
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource namespaceResource = this.getResourceFromFile("TextualModelImportTests/convertNamespaceImportTest/namespace.sysml", resourceSet, true);
        assertNotNull(namespaceResource);

        Resource testResource = this.getResourceFromFile("TextualModelImportTests/convertNamespaceImportTest/model.sysml", resourceSet, true);
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

    @DisplayName("GIVEN a nonunique feature, WHEN importing text, THEN uniqueness and multiplicity are preserved")
    @Test
    void nonUniqueFeatureTest() {
        Resource testResource = this.getResourceFromFile("TextualModelImportTests/isUniqueFeature/model.sysml", false);

        assertNotNull(testResource);

        Namespace namespace = (Namespace) testResource.getContents().get(0);

        Package owner = (Package) namespace.getMember().getFirst();
        Element secondMember = owner.getOwnedMember().getLast();
        assertTrue(secondMember instanceof PartUsage);
        assertFalse(((PartUsage) secondMember).isIsUnique());

    }

    @DisplayName("GIVEN a subclassification, WHEN importing text, THEN its superclass is resolved")
    @Test
    void convertSubclassificationTest() {
        Resource testResource = this.getResourceFromFile("TextualModelImportTests/convertSubclassificationTest/subclassification.sysml", true);
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

    /**
     * Importing a renamed redefinition hides the original from the usage's namespace while preserving its target.
     * <pre>{@code
     * part def Def {
     *     part a;
     * }
     * part p : Def {
     *     b :>> a;
     * }
     * }</pre>
     */
    @DisplayName("GIVEN inherited types, WHEN importing text, THEN specialization relationships are preserved")
    @Test
    void convertInheritanceTest() {
        Resource testResource = this.getResourceFromFile("TextualModelImportTests/convertInheritanceTest/inheritance.sysml", true);
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
        // KerML 7.3.4.5: the redefined feature is not inherited into p's namespace; b replaces it.
        assertNull(partUsageP.resolve("a"));
        assertThat(partUsageP.resolve("b")).isNotNull()
                .extracting(Membership::getMemberElement).isSameAs(referenceUsageB);
        // Resolving the owned redefinition starts from the supertypes, so its target remains Def::a.
        assertEquals(partUsageA, referenceUsageB.getOwnedRedefinition().get(0).getRedefinedFeature());
    }

    @DisplayName("GIVEN an alias, WHEN importing text, THEN its original member is resolved")
    @Test
    void convertAliasTest() {
        Resource testResource = this.getResourceFromFile("TextualModelImportTests/convertAliasTest/alias.sysml", true);
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

    @DisplayName("GIVEN an imported namespace, WHEN importing a subclassification, THEN its superclass is resolved")
    @Test
    void convertNamespaceImportValueTest() {
        ResourceSet resourceSet = new ResourceSetImpl();

        Resource namespaceResource = this.getResourceFromFile("TextualModelImportTests/convertNamespaceImportValueTest/namespace.sysml", resourceSet, true);
        assertNotNull(namespaceResource);

        Resource testResource = this.getResourceFromFile("TextualModelImportTests/convertNamespaceImportValueTest/model.sysml", resourceSet, true);
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

    @DisplayName("GIVEN membership and namespace imports, WHEN importing text, THEN import targets are resolved")
    @Test
    void convertImportTest() {
        Resource namespaceResource = this.getResourceFromFile("TextualModelImportTests/convertImportTest/import.sysml", true);
        assertNotNull(namespaceResource);

        Namespace testNamespace = (Namespace) namespaceResource.getContents().get(0);

        PartUsage p11 = (PartUsage) testNamespace.resolve("ImportTest::Pkg1::p11").getMemberElement();
        PartDefinition p211 = (PartDefinition) testNamespace.resolve("ImportTest::Pkg2::Pkg21::Pkg211::P211").getMemberElement();
        PartDefinition p12 = (PartDefinition) testNamespace.resolve("ImportTest::Pkg1::P12").getMemberElement();

        assertEquals(p211, p11.getOwnedSpecialization().get(0).getGeneral());
        assertEquals(p12, p211.getOwnedSubclassification().get(0).getSuperclassifier());
    }

    @DisplayName("GIVEN documentation, WHEN importing text, THEN its body is preserved")
    @Test
    void convertDocumentationTest() {
        Resource result = this.getResourceFromText("doc /*TEST*/", new ResourceSetImpl());

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

    @DisplayName("GIVEN an assignment, WHEN importing text, THEN the target feature and value are preserved")
    @Test
    void convertAssignment1Test() {
        Resource namespaceResource = this.getResourceFromFile("TextualModelImportTests/convertAssignmentTest/assignment1.sysml", true);
        assertNotNull(namespaceResource);

        Namespace namespace = (Namespace) namespaceResource.getContents().get(0);
        Package packageAssignment1 = (Package) namespace.getMember().get(0);

        PartDefinition partDefinitionCounter = (PartDefinition) packageAssignment1.getMember().get(0);

        AttributeUsage attributeUsageCount = (AttributeUsage) partDefinitionCounter.getMember().get(0);

        assertEquals(0, ((LiteralInteger) this.utilService.getValuation(attributeUsageCount).getValue()).getValue());

        ActionUsage actionUsageincr = (ActionUsage) partDefinitionCounter.getMember().get(1);

        AssignmentActionUsage assignmentActionUsage = (AssignmentActionUsage) actionUsageincr.getMember().get(0);

        assertEquals(attributeUsageCount, assignmentActionUsage.getReferent());

        Expression expression = assignmentActionUsage.getValueExpression(); // Expression -> 1

        assertEquals(1, ((LiteralInteger) expression).getValue());

    }

    @DisplayName("GIVEN an attribute with an initial value, WHEN importing an assignment, THEN its target and assigned value are preserved")
    @Test
    void convertAssignment2Test() {
        Resource namespaceResource = this.getResourceFromFile("TextualModelImportTests/convertAssignmentTest/assignment2.sysml", true);
        assertNotNull(namespaceResource);

        Namespace namespace = (Namespace) namespaceResource.getContents().get(0);
        Package packageAssignment1 = (Package) namespace.getMember().get(0);

        ActionUsage actionUsageCounter = (ActionUsage) packageAssignment1.getMember().get(0);
        // A package-owned usage has no featuring type, so structural completion makes it non-composite.
        assertTrue(actionUsageCounter.isIsReference());
        assertFalse(actionUsageCounter.isIsComposite());
        AttributeUsage attributeUsagecount = (AttributeUsage) actionUsageCounter.getMember().get(0);
        AssignmentActionUsage assignmentActionUsage = (AssignmentActionUsage) actionUsageCounter.getMember().get(1);

        assertEquals(attributeUsagecount, assignmentActionUsage.getReferent());

        Expression expression = assignmentActionUsage.getValueExpression(); // Expression -> 1

        assertEquals(1, ((LiteralInteger) expression).getValue());

    }

    @DisplayName("GIVEN an allocation, WHEN importing text, THEN its source and target are resolved")
    @Test
    void convertAllocationTest() {
        Resource namespaceResource = this.getResourceFromFile("TextualModelImportTests/convertAllocationTest/allocation.sysml", true);
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

    /**
     * Loads a textual fixture against the standard libraries.
     *
     * @param testFilePath the classpath resource
     * @param noErrorExpected whether diagnostics should be empty
     * @return the imported resource
     */
    private Resource getResourceFromFile(String testFilePath, boolean noErrorExpected) {
        ResourceSetImpl resourceSet = new ResourceSetImpl();
        // Not the safest way to import libraries but enough for this use case a make the test run faster
        resourceSet.getResources().addAll(standardLibraries);
        return this.getResourceFromFile(testFilePath, resourceSet, noErrorExpected);
    }

    /**
     * Loads a textual fixture against the given project context.
     *
     * @param testFilePath the classpath resource
     * @param resourceSet the resolution context
     * @param noErrorExpected whether diagnostics should be empty
     * @return the imported resource
     */
    private Resource getResourceFromFile(String testFilePath, ResourceSet resourceSet, boolean noErrorExpected) {

        Resource testResource = null;
        try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream(testFilePath)) {
            assertNotNull(stream);
            String fileContent = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            testResource = this.getResourceFromText(fileContent, resourceSet);
            if (noErrorExpected) {
                List<Message> errorMessages = this.messages.stream().filter(m -> m.level() == MessageLevel.ERROR || m.level() == MessageLevel.WARNING).toList();
                assertThat(errorMessages).isEmpty();

            }
        } catch (IOException e) {
            fail(e);
        }
        return testResource;
    }
    /**
     * Parses a textual fixture using the same conversion pipeline as uploads.
     *
     * @param text the SysML source
     * @param resourceSet the resolution context
     * @return the converted resource
     */
    private Resource getResourceFromText(String text, ResourceSet resourceSet) {
        var result = this.parsingService.parseAndLink(resourceSet, null, new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
        this.messages = result.messages();
        assertNotNull(result.root(), () -> result.messages().toString());
        Resource resource = new JSONResourceFactory().createResourceFromPath(null);
        resource.getContents().add(result.root());
        resourceSet.getResources().add(resource);
        return resource;
    }

}
