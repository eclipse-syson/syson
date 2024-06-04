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
package org.eclipse.syson.sysml.export;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.DataType;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralInfinity;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.export.utils.NameDeresolver;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.Test;
import org.eclipse.syson.sysml.export.models.AttributeUsageWithBracketOperatorExpressionTestModel;
import org.eclipse.syson.sysml.export.models.AttributeUsageWithFeatureChainExpressionTestModel;
import org.eclipse.syson.sysml.export.models.AttributeUsageWithSequenceExpressionTestModel;
import org.eclipse.syson.sysml.export.models.AttributeUsageWithBinaryOperatorExpressionTestModel;

/**
 * Test class for SysMLElementSerializer.
 *
 * @author Arthur Daussy
 */
public class SysMLModelToTextSwitchTest {

    private static final String PACKAGE_2 = "Package 2";

    private static final String PACKAGE1 = "Package1";

    private static final String ATTRIBUTE1 = "attribute1";

    private static final String SUBSETTING1 = "sub1";

    private static final String SUBSETTING2 = "sub2";

    private ModelBuilder builder = new ModelBuilder();

    private final SysmlFactory fact = SysmlFactory.eINSTANCE;

    @Test
    public void emptyPackage() {
        String content = this.convertToText(this.fact.createPackage());
        assertEquals("package ;", content);
    }

    @Test
    public void emptyPackageWithName() {
        this.assertTextualFormEquals("package Package1;", this.builder.createWithName(Package.class, PACKAGE1));
    }

    /**
     *
     * Check that the name of a package is quoted when containing spaces.
     */
    @Test
    public void emptyPackageWithSpaces() {
        this.assertTextualFormEquals("package <'T T'> 'Package 1';", this.builder.createInWithFullName(Package.class, null, "Package 1", "T T"));
    }

    /**
     * A name need escaping if it contains spaces or it first letter is neither a letter or _.
     */
    @Test
    public void packageWithUnrestrictedName() {
        // Need to escape name since it starts with a digit
        // No need to escape short name _ since it is a valid first character
        this.assertTextualFormEquals("package <_T> '4Package';", this.builder.createInWithFullName(Package.class, null, "4Package", "_T"));

    }

    @Test
    public void emptyPackageWithShortName() {
        this.assertTextualFormEquals("package <T> Package1;", this.builder.createInWithFullName(Package.class, null, "Package1", "T"));
    }

    @Test
    public void packageWithContent() {
        Package pack1 = this.builder.createWithName(Package.class, PACKAGE1);

        Package pack2 = this.builder.createInWithName(Package.class, pack1, PACKAGE_2);

        this.builder.createInWithName(Package.class, pack2, "Package 3");

        this.assertTextualFormEquals("""
                package Package1 {
                    package 'Package 2' {
                        package 'Package 3';
                    }
                }""", pack1);
    }

    @Test
    public void packageWithContentWithRootNamespace() {

        Namespace rootnamespace = this.builder.create(Namespace.class);

        Package pack1 = this.builder.createInWithName(Package.class, rootnamespace, PACKAGE1);

        NamespaceImport namespaceImport = this.builder.createIn(NamespaceImport.class, pack1);

        Package pack2 = this.builder.createInWithName(Package.class, pack1, PACKAGE_2);

        Package pack3 = this.builder.createInWithName(Package.class, pack2, "Package 3");

        namespaceImport.setImportedNamespace(pack3);

        // The root namespace should not be serialized
        this.assertTextualFormEquals("""
                package Package1 {
                    import 'Package 2'::'Package 3'::*;
                    package 'Package 2' {
                        package 'Package 3';
                    }
                }""", pack1);
    }

    @Test
    public void emptyNamespaceImport() {
        this.assertTextualFormEquals("import *;", this.fact.createNamespaceImport());
    }

    @Test
    public void privateNamespaceImport() {
        NamespaceImport nmImport = this.fact.createNamespaceImport();
        nmImport.setVisibility(VisibilityKind.PRIVATE);

        Package pack = this.fact.createPackage();
        pack.setDeclaredName(PACKAGE1);

        nmImport.setImportedNamespace(pack);

        this.assertTextualFormEquals("private import Package1::*;", nmImport);
    }

    @Test
    public void privateRecursiveNamespaceImport() {
        NamespaceImport nmImport = this.fact.createNamespaceImport();
        nmImport.setVisibility(VisibilityKind.PRIVATE);
        nmImport.setIsRecursive(true);
        Package pack = this.fact.createPackage();
        pack.setDeclaredName(PACKAGE1);

        nmImport.setImportedNamespace(pack);

        this.assertTextualFormEquals("private import Package1::*::**;", nmImport);
    }

    /**
     * Check that the full qualified name is used when the imported namespace is not located inside the containment
     * tree.
     */
    @Test
    public void externalNamespaceImport() {
        NamespaceImport nmImport = this.fact.createNamespaceImport();

        Package pack = this.fact.createPackage();
        pack.setDeclaredName(PACKAGE1);
        Package pack2 = this.fact.createPackage();
        pack2.setDeclaredName(PACKAGE_2);
        this.addOwnedMembership(pack, pack2);

        nmImport.setImportedNamespace(pack2);

        this.assertTextualFormEquals("import Package1::'Package 2'::*;", nmImport);
    }

    @Test
    public void importWithContent() {
        NamespaceImport nmImport = this.fact.createNamespaceImport();

        Package pack1 = this.fact.createPackage();
        pack1.setDeclaredName(PACKAGE1);

        Comment comment = this.fact.createComment();
        comment.setBody("A comment");
        this.addOwnedMembership(nmImport, comment);

        nmImport.setImportedNamespace(pack1);

        this.assertTextualFormEquals("""
                import Package1::* {
                    comment
                        /* A comment */
                }""", nmImport);

    }

    /**
     * Check that the 'short' qualified name if used when the imported namespace is located inside the containment tree.
     */
    @Test
    public void internalNamespaceImport() {

        Package pack1 = this.fact.createPackage();
        pack1.setDeclaredName(PACKAGE1);

        Package pack2 = this.fact.createPackage();
        pack2.setDeclaredName(PACKAGE_2);

        Package pack3 = this.fact.createPackage();
        pack3.setDeclaredName("Package 3");

        this.addOwnedMembership(pack1, pack2);
        this.addOwnedMembership(pack2, pack3);

        NamespaceImport nmImport = this.fact.createNamespaceImport();
        nmImport.setImportedNamespace(pack3);
        pack1.getOwnedRelationship().add(nmImport);

        this.assertTextualFormEquals("import 'Package 2'::'Package 3'::*;", nmImport);
    }

    @Test
    public void portDefinition() {
        this.checkBasicDefinitionDeclaration(PortDefinition.class, "port def");

    }

    @Test
    public void conjugatedPortDefinition() {

        Package p1 = this.builder.createWithName(Package.class, PACKAGE1);

        PortDefinition portDef = this.builder.createInWithName(PortDefinition.class, p1, "portDef");
        PortDefinition conjPortDef = this.builder.createInWithName(ConjugatedPortDefinition.class, portDef, LabelConstants.CONJUGATED + "portDef");

        this.assertTextualFormEquals("port def portDef;", portDef);
        // Conjugated port are explicit so they are not present in the textual form
        this.assertTextualFormEquals(null, conjPortDef);

    }

    @Test
    public void attributeDefinition() {
        this.checkBasicDefinitionDeclaration(AttributeDefinition.class, "attribute def");
    }

    @Test
    public void itemDefinition() {
        this.checkBasicDefinitionDeclaration(ItemDefinition.class, "item def");
    }

    @Test
    public void interfaceDefinition() {
        this.checkBasicDefinitionDeclaration(InterfaceDefinition.class, "interface def");
    }

    private <T extends Definition> void checkBasicDefinitionDeclaration(Class<T> type, String keyword) {
        Package p1 = this.builder.createWithName(Package.class, PACKAGE1);

        var superDef = this.builder.createInWithName(type, p1, "SuperDef");
        var subDef = this.builder.createInWithName(type, p1, "SubDef");
        this.builder.createIn(Comment.class, subDef).setBody("A comment");
        this.builder.addSuperType(subDef, superDef);

        this.assertTextualFormEquals("""
                $keyword SubDef :> SuperDef {
                    /* A comment */
                }""".replaceFirst("\\$keyword", keyword), subDef);
    }

    /**
     * Check that the full qualified name is used when the imported member is not located inside the containment tree.
     */
    @Test
    public void externalMemberImport() {
        MembershipImport mImport = this.fact.createMembershipImport();

        Package pack = this.fact.createPackage();
        pack.setDeclaredName(PACKAGE1);
        Package pack2 = this.fact.createPackage();
        pack2.setDeclaredName(PACKAGE_2);
        this.addOwnedMembership(pack, pack2);

        mImport.setImportedMembership((Membership) pack2.eContainer());

        this.assertTextualFormEquals("import Package1::'Package 2';", mImport);
    }

    @Test
    public void privateRecusiveMemberImport() {
        MembershipImport mImport = this.fact.createMembershipImport();
        mImport.setVisibility(VisibilityKind.PRIVATE);
        mImport.setIsRecursive(true);

        Package pack = this.fact.createPackage();
        pack.setDeclaredName(PACKAGE1);
        Package pack2 = this.fact.createPackage();
        pack2.setDeclaredName(PACKAGE_2);
        this.addOwnedMembership(pack, pack2);

        mImport.setImportedMembership((Membership) pack2.eContainer());

        this.assertTextualFormEquals("private import Package1::'Package 2'::**;", mImport);
    }

    /**
     * Check that the 'short' qualified name if used when the imported member is located inside the containment tree.
     */
    @Test
    public void internalMemberImport() {

        Package pack1 = this.fact.createPackage();
        pack1.setDeclaredName(PACKAGE1);

        Package pack2 = this.fact.createPackage();
        pack2.setDeclaredName(PACKAGE_2);

        Package pack3 = this.fact.createPackage();
        pack3.setDeclaredName("Package 3");

        this.addOwnedMembership(pack1, pack2);
        this.addOwnedMembership(pack2, pack3);

        MembershipImport mImport = this.fact.createMembershipImport();
        mImport.setImportedMembership((Membership) pack3.eContainer());
        pack1.getOwnedRelationship().add(mImport);

        this.assertTextualFormEquals("import 'Package 2'::'Package 3';", mImport);
    }

    @Test
    public void emptyComment() {
        this.assertTextualFormEquals("""
                comment
                    /*  */""", this.fact.createComment());
    }

    @Test
    public void commentWithBody() {
        Comment comment = this.fact.createComment();
        comment.setBody("Body");
        this.assertTextualFormEquals("""
                comment
                    /* Body */""", comment);
    }

    /**
     * Comment in Namepace can use a simple serialization format only.
     */
    @Test
    public void commentInNamespace() {
        Package pack1 = this.fact.createPackage();
        pack1.setDeclaredName(PACKAGE1);
        Comment comment = this.fact.createComment();
        comment.setBody("A body");
        this.addOwnedMembership(pack1, comment);
        this.assertTextualFormEquals("""
                package Package1 {
                    /* A body */
                }""", pack1);
    }

    /**
     * Comment in Namespace can use a simple serialization format only only if no other information are provided.
     */
    @Test
    public void commentInNamespaceWithLocal() {
        Package pack1 = this.fact.createPackage();
        pack1.setDeclaredName(PACKAGE1);
        Comment comment = this.fact.createComment();
        comment.setBody("A body");
        comment.setLocale("fr_FR");
        this.addOwnedMembership(pack1, comment);
        this.assertTextualFormEquals("""
                package Package1 {
                    comment locale "fr_FR"
                        /* A body */
                }""", pack1);
    }

    @Test
    public void fullComment() {
        Package pack1 = this.fact.createPackage();
        pack1.setDeclaredName("Pack1");

        AttributeDefinition attr = this.fact.createAttributeDefinition();
        attr.setDeclaredName("Attr1");
        this.addOwnedMembership(pack1, attr);

        Comment comment = this.fact.createComment();
        comment.setBody("A body");
        comment.setDeclaredName("XXX");
        comment.setDeclaredShortName("X");
        comment.setLocale("fr_FR");
        this.addOwnedMembership(pack1, comment);

        Annotation annotation = this.fact.createAnnotation();
        annotation.setAnnotatedElement(attr);
        comment.getOwnedRelationship().add(annotation);
        // Workaround while the subset are not implemented
        comment.getAnnotation().add(annotation);

        assertEquals(attr, comment.getAnnotatedElement().get(0));

        this.assertTextualFormEquals("""
                comment <X> XXX about Attr1 locale \"fr_FR\"
                    /* A body */""", comment);
    }

    @Test
    public void commentWithMultilineBodyInPackage() {
        Package pack1 = this.fact.createPackage();
        pack1.setDeclaredName("Pack1");

        AttributeDefinition attr = this.fact.createAttributeDefinition();
        attr.setDeclaredName("Attr1");
        this.addOwnedMembership(pack1, attr);

        Comment comment = this.fact.createComment();
        comment.setBody("A body\ntest");
        comment.setDeclaredName("XXX");
        comment.setDeclaredShortName("X");
        comment.setLocale("fr_FR");
        this.addOwnedMembership(pack1, comment);

        Annotation annotation = this.fact.createAnnotation();
        annotation.setAnnotatedElement(attr);
        comment.getOwnedRelationship().add(annotation);
        // Workaround while the subset are not implemented
        comment.getAnnotation().add(annotation);

        assertEquals(attr, comment.getAnnotatedElement().get(0));

        this.assertTextualFormEquals("""
                package Pack1 {
                    attribute def Attr1;
                    comment <X> XXX about Attr1 locale \"fr_FR\"
                        /* A body
                        test */
                }""", pack1);
    }

    /**
     * Check PartDefinition which inherit from other definitions.
     */
    @Test
    public void partDefinitionWithSuperType() {
        PartDefinition partDef = this.builder.createWithName(PartDefinition.class, "Part Def 1");
        PartDefinition partDef2 = this.builder.createWithName(PartDefinition.class, "PartDef2");
        PartDefinition partDef3 = this.builder.createWithName(PartDefinition.class, "PartDef 3");

        this.builder.addSuperType(partDef, partDef2);
        this.builder.addSuperType(partDef, partDef3);
        this.builder.addSuperType(partDef2, partDef3);

        this.assertTextualFormEquals("part def 'Part Def 1' :> PartDef2, 'PartDef 3';", partDef);
        this.assertTextualFormEquals("part def PartDef2 :> 'PartDef 3';", partDef2);
        this.assertTextualFormEquals("part def 'PartDef 3';", partDef3);

    }

    /**
     * Check PartDefinition which inherit from other definitions with name deresolution.
     */
    @Test
    public void partDefinitionWithSuperTypeAndNameDeresolution() {
        Package p1 = this.builder.createWithName(Package.class, PACKAGE1);
        PartDefinition partDef = this.builder.createInWithName(PartDefinition.class, p1, "Part Def 1");
        Package p2 = this.builder.createWithName(Package.class, "p2");
        PartDefinition partDef2 = this.builder.createInWithName(PartDefinition.class, p2, "PartDef2");
        Package p3 = this.builder.createWithName(Package.class, "p3");
        PartDefinition partDef3 = this.builder.createInWithName(PartDefinition.class, p3, "PartDef 3");

        this.builder.createIn(NamespaceImport.class, p1).setImportedNamespace(p2);

        this.builder.addSuperType(partDef, partDef2);
        this.builder.addSuperType(partDef, partDef3);
        this.builder.addSuperType(partDef2, partDef3);

        this.assertTextualFormEquals("part def 'Part Def 1' :> PartDef2, p3::'PartDef 3';", partDef);
        this.assertTextualFormEquals("part def PartDef2 :> p3::'PartDef 3';", partDef2);
        this.assertTextualFormEquals("part def 'PartDef 3';", partDef3);

    }

    @Test
    public void partDefinitionIndividual() {
        PartDefinition partDef = this.fact.createPartDefinition();
        partDef.setDeclaredName("PartDef");
        partDef.setIsIndividual(true);

        this.assertTextualFormEquals("individual part def PartDef;", partDef);
    }

    @Test
    public void partDefinitionAbstract() {
        PartDefinition partDef = this.fact.createPartDefinition();
        partDef.setDeclaredName("PartDef1");
        partDef.setIsAbstract(true);

        this.assertTextualFormEquals("abstract part def PartDef1;", partDef);
    }

    @Test
    public void partDefinitionWithVariation() {
        PartDefinition partDef = this.fact.createPartDefinition();
        partDef.setDeclaredName("PartDef1");
        partDef.setIsVariation(true);

        this.assertTextualFormEquals("variation part def PartDef1;", partDef);
    }

    @Test
    public void partDefinitionWithContent() {
        PartDefinition partDef = this.fact.createPartDefinition();
        partDef.setDeclaredName("PartDef1");

        Comment comment = this.fact.createComment();
        comment.setBody("A body");
        this.addOwnedMembership(partDef, comment);

        this.assertTextualFormEquals("""
                part def PartDef1 {
                    /* A body */
                }""", partDef);
    }

    @Test
    public void partDefinitionFull() {

        Package pack1 = this.builder.createWithName(Package.class, "Pack1");

        PartDefinition partDef = this.builder.createInWithName(PartDefinition.class, pack1, "PartDef1");
        partDef.setIsIndividual(true);
        partDef.setIsAbstract(true);

        PartDefinition partDef2 = this.builder.createWithName(PartDefinition.class, "PartDef2");

        this.builder.addSuperType(partDef, partDef2);

        this.addOwnedMembership(pack1, partDef, VisibilityKind.PRIVATE);

        this.assertTextualFormEquals("""
                package Pack1 {
                    private abstract individual part def PartDef1 :> PartDef2;
                }""", pack1);
    }

    @Test
    public void enumerationDefinitionAndEnumerationUsage() {
        EnumerationDefinition enumDef = this.builder.createWithName(EnumerationDefinition.class, "Colors");

        this.builder.createInWithName(EnumerationUsage.class, enumDef, "black");

        MetadataDefinition metaData1 = this.fact.createMetadataDefinition();
        metaData1.setDeclaredName("m1");

        MetadataUsage m1u = this.createMetadataUsage(metaData1);

        EnumerationUsage greyLiteral = this.builder.createInWithName(EnumerationUsage.class, enumDef, "grey");
        this.addOwnedMembership(greyLiteral, m1u);
        EnumerationUsage redLiteral = this.builder.createInWithName(EnumerationUsage.class, enumDef, "red");

        this.builder.createIn(Comment.class, redLiteral).setBody("A body");

        this.assertTextualFormEquals("""
                enum def Colors {
                    black;
                    #m1 grey;
                    red {
                        /* A body */
                    }
                }""", enumDef);
    }

    @Test
    public void partDefinitionWithMetadata() {

        Package pack1 = this.fact.createPackage();
        pack1.setDeclaredName("Pack1");

        MetadataDefinition metaData1 = this.fact.createMetadataDefinition();
        metaData1.setDeclaredName("m1");
        MetadataDefinition metaData2 = this.fact.createMetadataDefinition();
        metaData2.setDeclaredName("m2");

        MetadataUsage m1u = this.createMetadataUsage(metaData1);
        MetadataUsage m2u = this.createMetadataUsage(metaData2);

        PartDefinition partDef = this.fact.createPartDefinition();
        partDef.setDeclaredName("PartDef1");

        this.addOwnedMembership(partDef, m1u);
        this.addOwnedMembership(partDef, m2u);
        this.assertTextualFormEquals("#m1 #m2 part def PartDef1;", partDef);
    }

    private MetadataUsage createMetadataUsage(MetadataDefinition metaData1) {
        FeatureTyping featuringType = this.fact.createFeatureTyping();
        featuringType.setType(metaData1);

        MetadataUsage metaDataUsage = this.fact.createMetadataUsage();
        OwningMembership owningMember = this.fact.createOwningMembership();
        owningMember.getOwnedRelatedElement().add(featuringType);
        metaDataUsage.getOwnedRelationship().add(owningMember);

        return metaDataUsage;
    }

    private void addOwnedMembership(Element parent, Element ownedElement) {
        this.addOwnedMembership(parent, ownedElement, null);
    }

    private void addOwnedMembership(Element parent, Element ownedElement, VisibilityKind visibility) {
        OwningMembership ownedRelation = this.fact.createOwningMembership();
        parent.getOwnedRelationship().add(ownedRelation);
        ownedRelation.getOwnedRelatedElement().add(ownedElement);

        if (visibility != null) {

            ownedRelation.setVisibility(visibility);
        }
    }

    private String convertToText(Element source, Element context, int indent) {
        return new SysMLElementSerializer("\n", "    ", new NameDeresolver()).doSwitch(source);
    }

    private String convertToText(Element source) {
        return this.convertToText(source, (Element) source.eContainer(), 0);
    }

    private void assertTextualFormEquals(String extexted, Element elementToTest) {
        String content = this.convertToText(elementToTest);
        assertEquals(extexted, content);
    }

    @Test
    public void attributeUsage() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);

        this.assertTextualFormEquals("attribute attribute1;", attributeUsage);
    }

    @Test
    public void attributeUsageWithBinaryOperatorExpression() {
        AttributeUsageWithBinaryOperatorExpressionTestModel model = new AttributeUsageWithBinaryOperatorExpressionTestModel();
        this.assertTextualFormEquals("attribute attribute1 = bestFuel + idlingFuel * fuel;", model.getAttributeUsage());
    }

    @Test
    public void attributeUsageWithBracketOperatorExpression() {
        AttributeUsageWithBracketOperatorExpressionTestModel model = new AttributeUsageWithBracketOperatorExpressionTestModel();
        this.assertTextualFormEquals("attribute attribute1 = 80 [millimetre];", model.getAttributeUsage());
    }
    
    @Test
    public void attributeUsageWithSequenceExpression() {
        AttributeUsageWithSequenceExpressionTestModel model = new AttributeUsageWithSequenceExpressionTestModel();
        this.assertTextualFormEquals("attribute attribute1 = (fuel.mass, front.mass, rear.mass, drives.mass);", model.getAttributeUsage());
    }
    
    @Test
    public void attributeUsageWithFeatureChainExpression() {
        AttributeUsageWithFeatureChainExpressionTestModel model = new AttributeUsageWithFeatureChainExpressionTestModel();
        this.assertTextualFormEquals("attribute attribute1 = (fuel.mass, front.mass, rear.drives.mass);", model.getAttributeUsage());
    }

    @Test
    public void abstractAttributeUsage() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);
        attributeUsage.setIsAbstract(true);

        this.assertTextualFormEquals("abstract attribute attribute1;", attributeUsage);
    }

    @Test
    public void variationAttributeUsage() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);
        attributeUsage.setIsVariation(true);

        this.assertTextualFormEquals("variation attribute attribute1;", attributeUsage);
    }

    @Test
    public void readonlyAttributeUsage() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);
        attributeUsage.setIsReadOnly(true);

        this.assertTextualFormEquals("readonly attribute attribute1;", attributeUsage);
    }

    @Test
    public void derivedAttributeUsage() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);
        attributeUsage.setIsDerived(true);

        this.assertTextualFormEquals("derived attribute attribute1;", attributeUsage);
    }

    @Test
    public void endAttributeUsage() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);
        attributeUsage.setIsEnd(true);

        this.assertTextualFormEquals("end attribute attribute1;", attributeUsage);
    }

    @Test
    public void allPrefixesAttributeUsage() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);
        attributeUsage.setIsEnd(true);
        attributeUsage.setIsDerived(true);
        attributeUsage.setIsReadOnly(true);
        attributeUsage.setIsVariation(true);
        attributeUsage.setIsAbstract(true);

        this.assertTextualFormEquals("abstract variation readonly derived end attribute attribute1;", attributeUsage);
    }

    @Test
    public void attributeUsageWithSubsetting() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);

        Subsetting subsetting = this.builder.createIn(Subsetting.class, attributeUsage);

        Feature feature = this.builder.createWithName(Feature.class, SUBSETTING1);
        subsetting.setSubsettedFeature(feature);

        this.assertTextualFormEquals("attribute attribute1 :> sub1;", attributeUsage);
    }

    @Test
    public void attributeUsageWithMultipleSubsetting() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);

        Subsetting subsetting = this.builder.createIn(Subsetting.class, attributeUsage);
        Subsetting subsetting1 = this.builder.createIn(Subsetting.class, attributeUsage);

        Feature feature = this.builder.createWithName(Feature.class, SUBSETTING1);
        subsetting.setSubsettedFeature(feature);

        Feature feature1 = this.builder.createWithName(Feature.class, SUBSETTING2);
        subsetting1.setSubsettedFeature(feature1);

        this.assertTextualFormEquals("attribute attribute1 :> sub1, sub2;", attributeUsage);
    }

    @Test
    public void attributeUsageWithSubsettingAndMultiplicity() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);

        Subsetting subsetting = this.builder.createIn(Subsetting.class, attributeUsage);

        Feature feature = this.builder.createWithName(Feature.class, SUBSETTING1);
        subsetting.setSubsettedFeature(feature);

        MultiplicityRange multiplicity = this.builder.createIn(MultiplicityRange.class, attributeUsage);
        LiteralInteger literal = this.builder.createIn(LiteralInteger.class, multiplicity);
        literal.setValue(1);

        this.assertTextualFormEquals("attribute attribute1 :> sub1 [1];", attributeUsage);
    }

    @Test
    public void attributeUsageWithSubsettingAndOrderedNonUniqueMultiplicity() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);

        Subsetting subsetting = this.builder.createIn(Subsetting.class, attributeUsage);

        Feature feature = this.builder.createWithName(Feature.class, SUBSETTING1);
        subsetting.setSubsettedFeature(feature);

        MultiplicityRange multiplicity = this.builder.createIn(MultiplicityRange.class, attributeUsage);
        multiplicity.setIsUnique(false);
        multiplicity.setIsOrdered(true);
        LiteralInteger literal = this.builder.createIn(LiteralInteger.class, multiplicity);
        literal.setValue(1);

        this.assertTextualFormEquals("attribute attribute1 :> sub1 [1] ordered nonunique;", attributeUsage);
    }

    @Test
    public void attributeUsageWithSubsettingAndMultiplicityRange() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);

        Subsetting subsetting = this.builder.createIn(Subsetting.class, attributeUsage);

        Feature feature = this.builder.createWithName(Feature.class, SUBSETTING1);
        subsetting.setSubsettedFeature(feature);

        MultiplicityRange multiplicity = this.builder.createIn(MultiplicityRange.class, attributeUsage);
        LiteralInteger literal = this.builder.createIn(LiteralInteger.class, multiplicity);
        literal.setValue(1);
        this.builder.createIn(LiteralInfinity.class, multiplicity);

        this.assertTextualFormEquals("attribute attribute1 :> sub1 [1..*];", attributeUsage);
    }

    @Test
    public void attributeUsageWithRedefinition() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);

        Redefinition redefinition = this.builder.createIn(Redefinition.class, attributeUsage);

        Feature feature = this.builder.createWithName(Feature.class, "redefinition1");
        redefinition.setRedefinedFeature(feature);

        this.assertTextualFormEquals("attribute attribute1 :>> redefinition1;", attributeUsage);
    }

    @Test
    public void attributeUsageWithMultipleRedefinition() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);

        Redefinition redefinition = this.builder.createIn(Redefinition.class, attributeUsage);
        Redefinition redefinition1 = this.builder.createIn(Redefinition.class, attributeUsage);

        Feature feature = this.builder.createWithName(Feature.class, "redefinition1");
        redefinition.setRedefinedFeature(feature);

        Feature feature1 = this.builder.createWithName(Feature.class, "redefinition2");
        redefinition1.setRedefinedFeature(feature1);

        this.assertTextualFormEquals("attribute attribute1 :>> redefinition1, redefinition2;", attributeUsage);
    }

    @Test
    public void attributeUsageWithReferenceSubsetting() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);

        ReferenceSubsetting ref = this.builder.createIn(ReferenceSubsetting.class, attributeUsage);

        Feature feature = this.builder.createWithName(Feature.class, "refSubset1");
        ref.setReferencedFeature(feature);

        this.assertTextualFormEquals("attribute attribute1 ::> refSubset1;", attributeUsage);
    }

    @Test
    public void attributeUsageWithFeatureTyping() {
        AttributeUsage attributeUsage = this.builder.createWithName(AttributeUsage.class, ATTRIBUTE1);

        FeatureTyping typing = this.builder.createIn(FeatureTyping.class, attributeUsage);

        DataType dataType = this.builder.createWithName(DataType.class, "type1");
        typing.setType(dataType);
        typing.setTypedFeature(attributeUsage);
        typing.setSpecific(attributeUsage);
        typing.setGeneral(dataType);

        this.assertTextualFormEquals("attribute attribute1 : type1;", attributeUsage);
    }

    @Test
    public void literalInteger() {
        LiteralInteger literalInteger = SysmlFactory.eINSTANCE.createLiteralInteger();
        literalInteger.setValue(1);

        this.assertTextualFormEquals("1", literalInteger);
    }

    @Test
    public void literalString() {
        LiteralString literalStr = SysmlFactory.eINSTANCE.createLiteralString();
        literalStr.setValue("value");

        this.assertTextualFormEquals("value", literalStr);
    }

    @Test
    public void literalRational() {
        LiteralRational literalRat = SysmlFactory.eINSTANCE.createLiteralRational();
        literalRat.setValue(1.5);

        this.assertTextualFormEquals("1.5", literalRat);
    }

    @Test
    public void literalBoolean() {
        LiteralBoolean literalBool = SysmlFactory.eINSTANCE.createLiteralBoolean();
        literalBool.setValue(true);

        this.assertTextualFormEquals("true", literalBool);
    }

    @Test
    public void literalInfinity() {
        LiteralInfinity literalInf = SysmlFactory.eINSTANCE.createLiteralInfinity();

        this.assertTextualFormEquals("*", literalInf);
    }
}
