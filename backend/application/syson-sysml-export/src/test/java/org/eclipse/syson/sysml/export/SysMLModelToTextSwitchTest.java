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
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.VisibilityKind;
import org.junit.jupiter.api.Test;

/**
 * Test class for SysMLElementSerializer.
 * 
 * @author Arthur Daussy
 */
public class SysMLModelToTextSwitchTest {

    private static final String PACKAGE_2 = "Package 2";

    private static final String PACKAGE1 = "Package1";

    private final SysmlFactory fact = SysmlFactory.eINSTANCE;

    @Test
    public void emptyPackage() {
        String content = convertToText(fact.createPackage());
        assertEquals("package ;", content);
    }

    @Test
    public void emptyPackageWithName() {
        Package pack = fact.createPackage();
        pack.setDeclaredName(PACKAGE1);
        assertTextualFormEquals("package Package1;", pack);
    }

    /**
     * 
     * Check that the name of a package is quoted when containing spaces.
     */
    @Test
    public void emptyPackageWithSpaces() {
        Package pack = fact.createPackage();
        pack.setDeclaredName("Package 1");
        pack.setDeclaredShortName("T T");
        assertTextualFormEquals("package <'T T'> 'Package 1';", pack);
    }

    /**
     * A name need escaping if it contains spaces or it first letter is neither a letter or _.
     */
    @Test
    public void packageWithUnrestrictedName() {
        Package pack = fact.createPackage();
        // Need to escape since it start with a digit
        pack.setDeclaredName("4Package");
        // No need to escape _ is a valid first character
        pack.setDeclaredShortName("_T");
        assertTextualFormEquals("package <_T> '4Package';", pack);
    }

    @Test
    public void emptyPackageWithShortName() {
        Package pack = fact.createPackage();
        pack.setDeclaredName("Package1");
        pack.setDeclaredShortName("T");
        assertTextualFormEquals("package <T> Package1;", pack);
    }

    @Test
    public void packageWithContent() {
        Package pack1 = fact.createPackage();
        pack1.setDeclaredName(PACKAGE1);

        Package pack2 = fact.createPackage();
        pack2.setDeclaredName(PACKAGE_2);

        Package pack3 = fact.createPackage();
        pack3.setDeclaredName("Package 3");

        addOwnedMembership(pack1, pack2);
        addOwnedMembership(pack2, pack3);

        assertTextualFormEquals("""
                package Package1 {
                    package 'Package 2' {
                        package 'Package 3';
                    }
                }""", pack1);
    }

    @Test
    public void emptyNamespaceImport() {
        assertTextualFormEquals("import *;", fact.createNamespaceImport());
    }

    @Test
    public void privateNamespaceImport() {
        NamespaceImport nmImport = fact.createNamespaceImport();
        nmImport.setVisibility(VisibilityKind.PRIVATE);

        Package pack = fact.createPackage();
        pack.setDeclaredName(PACKAGE1);

        nmImport.setImportedNamespace(pack);

        assertTextualFormEquals("private import Package1::*;", nmImport);
    }

    @Test
    public void privateRecursiveNamespaceImport() {
        NamespaceImport nmImport = fact.createNamespaceImport();
        nmImport.setVisibility(VisibilityKind.PRIVATE);
        nmImport.setIsRecursive(true);
        Package pack = fact.createPackage();
        pack.setDeclaredName(PACKAGE1);

        nmImport.setImportedNamespace(pack);

        assertTextualFormEquals("private import Package1::*::**;", nmImport);
    }

    /**
     * Check that the full qualified name is used when the imported namespace is not located inside the containment
     * tree.
     */
    @Test
    public void externalNamespaceImport() {
        NamespaceImport nmImport = fact.createNamespaceImport();

        Package pack = fact.createPackage();
        pack.setDeclaredName(PACKAGE1);
        Package pack2 = fact.createPackage();
        pack2.setDeclaredName(PACKAGE_2);
        addOwnedMembership(pack, pack2);

        nmImport.setImportedNamespace(pack2);

        assertTextualFormEquals("import Package1::'Package 2'::*;", nmImport);
    }

    @Test
    public void importWithContent() {
        NamespaceImport nmImport = fact.createNamespaceImport();

        Package pack1 = fact.createPackage();
        pack1.setDeclaredName(PACKAGE1);

        Comment comment = fact.createComment();
        comment.setBody("A comment");
        addOwnedMembership(nmImport, comment);

        nmImport.setImportedNamespace(pack1);

        assertTextualFormEquals("""
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

        Package pack1 = fact.createPackage();
        pack1.setDeclaredName(PACKAGE1);

        Package pack2 = fact.createPackage();
        pack2.setDeclaredName(PACKAGE_2);

        Package pack3 = fact.createPackage();
        pack3.setDeclaredName("Package 3");

        addOwnedMembership(pack1, pack2);
        addOwnedMembership(pack2, pack3);

        NamespaceImport nmImport = fact.createNamespaceImport();
        nmImport.setImportedNamespace(pack3);
        pack1.getOwnedRelationship().add(nmImport);

        assertTextualFormEquals("import 'Package 2'::'Package 3'::*;", nmImport);
    }

    /**
     * Check that the full qualified name is used when the imported member is not located inside the containment tree.
     */
    @Test
    public void externalMemberImport() {
        MembershipImport mImport = fact.createMembershipImport();

        Package pack = fact.createPackage();
        pack.setDeclaredName(PACKAGE1);
        Package pack2 = fact.createPackage();
        pack2.setDeclaredName(PACKAGE_2);
        addOwnedMembership(pack, pack2);

        mImport.setImportedMembership((Membership) pack2.eContainer());

        assertTextualFormEquals("import Package1::'Package 2';", mImport);
    }

    @Test
    public void privateRecusiveMemberImport() {
        MembershipImport mImport = fact.createMembershipImport();
        mImport.setVisibility(VisibilityKind.PRIVATE);
        mImport.setIsRecursive(true);

        Package pack = fact.createPackage();
        pack.setDeclaredName(PACKAGE1);
        Package pack2 = fact.createPackage();
        pack2.setDeclaredName(PACKAGE_2);
        addOwnedMembership(pack, pack2);

        mImport.setImportedMembership((Membership) pack2.eContainer());

        assertTextualFormEquals("private import Package1::'Package 2'::**;", mImport);
    }

    /**
     * Check that the 'short' qualified name if used when the imported member is located inside the containment tree.
     */
    @Test
    public void internalMemberImport() {

        Package pack1 = fact.createPackage();
        pack1.setDeclaredName(PACKAGE1);

        Package pack2 = fact.createPackage();
        pack2.setDeclaredName(PACKAGE_2);

        Package pack3 = fact.createPackage();
        pack3.setDeclaredName("Package 3");

        addOwnedMembership(pack1, pack2);
        addOwnedMembership(pack2, pack3);

        MembershipImport mImport = fact.createMembershipImport();
        mImport.setImportedMembership((Membership) pack3.eContainer());
        pack1.getOwnedRelationship().add(mImport);

        assertTextualFormEquals("import 'Package 2'::'Package 3';", mImport);
    }

    @Test
    public void emptyComment() {
        assertTextualFormEquals("""
                comment
                    /*  */""", fact.createComment());
    }

    @Test
    public void commentWithBody() {
        Comment comment = fact.createComment();
        comment.setBody("A body");
        assertTextualFormEquals("""
                comment
                    /* A body */""", comment);
    }

    /**
     * Comment in Namepace can use a simple serialization format only.
     */
    @Test
    public void commentInNamespace() {
        Package pack1 = fact.createPackage();
        pack1.setDeclaredName(PACKAGE1);
        Comment comment = fact.createComment();
        comment.setBody("A body");
        addOwnedMembership(pack1, comment);
        assertTextualFormEquals("""
                package Package1 {
                    /* A body */
                }""", pack1);
    }

    /**
     * Comment in Namespace can use a simple serialization format only only if no other information are provided.
     */
    @Test
    public void commentInNamespaceWithLocal() {
        Package pack1 = fact.createPackage();
        pack1.setDeclaredName(PACKAGE1);
        Comment comment = fact.createComment();
        comment.setBody("A body");
        comment.setLocale("fr_FR");
        addOwnedMembership(pack1, comment);
        assertTextualFormEquals("""
                package Package1 {
                    comment locale "fr_FR"
                        /* A body */
                }""", pack1);
    }

    @Test
    public void fullComment() {
        Package pack1 = fact.createPackage();
        pack1.setDeclaredName("Pack1");
        
        AttributeDefinition attr = fact.createAttributeDefinition();
        attr.setDeclaredName("Attr1");
        addOwnedMembership(pack1, attr);
        
       
        
        Comment comment = fact.createComment();
        comment.setBody("A body");
        comment.setDeclaredName("XXX");
        comment.setDeclaredShortName("X");
        comment.setLocale("fr_FR");
        addOwnedMembership(pack1, comment);
        
        Annotation annotation = fact.createAnnotation();
        annotation.setAnnotatedElement(attr);
        comment.getOwnedRelationship().add(annotation);
        // Workaround while the subset are not implemented
        comment.getAnnotation().add(annotation);
        
        assertEquals(attr, comment.getAnnotatedElement().get(0));
        
        
        assertTextualFormEquals("""
                comment <X> XXX about Attr1 locale \"fr_FR\"
                    /* A body */""", comment);
    }

    @Test
    public void commentWithMultilineBodyInPackage() {
        Package pack1 = fact.createPackage();
        pack1.setDeclaredName("Pack1");
        
        AttributeDefinition attr = fact.createAttributeDefinition();
        attr.setDeclaredName("Attr1");
        addOwnedMembership(pack1, attr);
        
        Comment comment = fact.createComment();
        comment.setBody("A body\ntest");
        comment.setDeclaredName("XXX");
        comment.setDeclaredShortName("X");
        comment.setLocale("fr_FR");
        addOwnedMembership(pack1, comment);
        
        Annotation annotation = fact.createAnnotation();
        annotation.setAnnotatedElement(attr);
        comment.getOwnedRelationship().add(annotation);
        // Workaround while the subset are not implemented
        comment.getAnnotation().add(annotation);
        
        assertEquals(attr, comment.getAnnotatedElement().get(0));
        
        
        assertTextualFormEquals("""
                package Pack1 {
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
        PartDefinition partDef = fact.createPartDefinition();
        partDef.setDeclaredName("Part Def 1");

        PartDefinition partDef2 = fact.createPartDefinition();
        partDef2.setDeclaredName("PartDef2");
        PartDefinition partDef3 = fact.createPartDefinition();
        partDef3.setDeclaredName("PartDef 3");

        addSuperType(partDef, partDef2);
        addSuperType(partDef, partDef3);
        addSuperType(partDef2, partDef3);

        assertTextualFormEquals("part def 'Part Def 1' :> PartDef2, 'PartDef 3';", partDef);
        assertTextualFormEquals("part def PartDef2 :> 'PartDef 3';", partDef2);
        assertTextualFormEquals("part def 'PartDef 3';", partDef3);

    }

    @Test
    public void partDefinitionIndividual() {
        PartDefinition partDef = fact.createPartDefinition();
        partDef.setDeclaredName("PartDef");
        partDef.setIsIndividual(true);

        assertTextualFormEquals("individual part def PartDef;", partDef);
    }

    @Test
    public void partDefinitionAbstract() {
        PartDefinition partDef = fact.createPartDefinition();
        partDef.setDeclaredName("PartDef1");
        partDef.setIsAbstract(true);

        assertTextualFormEquals("abstract part def PartDef1;", partDef);
    }

    @Test
    public void partDefinitionWithVariation() {
        PartDefinition partDef = fact.createPartDefinition();
        partDef.setDeclaredName("PartDef1");
        partDef.setIsVariation(true);

        assertTextualFormEquals("variation part def PartDef1;", partDef);
    }

    @Test
    public void partDefinitionWithContent() {
        PartDefinition partDef = fact.createPartDefinition();
        partDef.setDeclaredName("PartDef1");

        Comment comment = fact.createComment();
        comment.setBody("A body");
        addOwnedMembership(partDef, comment);

        assertTextualFormEquals("""
                part def PartDef1 {
                    /* A body */
                }""", partDef);
    }

    @Test
    public void partDefinitionFull() {

        Package pack1 = fact.createPackage();
        pack1.setDeclaredName("Pack1");

        PartDefinition partDef = fact.createPartDefinition();
        partDef.setDeclaredName("PartDef1");
        partDef.setIsIndividual(true);
        partDef.setIsAbstract(true);

        PartDefinition partDef2 = fact.createPartDefinition();
        partDef2.setDeclaredName("PartDef2");

        addSuperType(partDef, partDef2);

        addOwnedMembership(pack1, partDef, VisibilityKind.PRIVATE);

        assertTextualFormEquals("""
                package Pack1 {
                    private abstract individual part def PartDef1 :> PartDef2;
                }""", pack1);
    }

    @Test
    public void partDefinitionWithMetadata() {

        Package pack1 = fact.createPackage();
        pack1.setDeclaredName("Pack1");

        MetadataDefinition metaData1 = fact.createMetadataDefinition();
        metaData1.setDeclaredName("m1");
        MetadataDefinition metaData2 = fact.createMetadataDefinition();
        metaData2.setDeclaredName("m2");

        MetadataUsage m1u = createMetadataUsage(metaData1);
        MetadataUsage m2u = createMetadataUsage(metaData2);

        PartDefinition partDef = fact.createPartDefinition();
        partDef.setDeclaredName("PartDef1");

        addOwnedMembership(partDef, m1u);
        addOwnedMembership(partDef, m2u);
        assertTextualFormEquals("#m1 #m2 part def PartDef1;", partDef);
    }

    private MetadataUsage createMetadataUsage(MetadataDefinition metaData1) {
        FeatureTyping featuringType = fact.createFeatureTyping();
        featuringType.setType(metaData1);

        MetadataUsage metaDataUsage = fact.createMetadataUsage();
        OwningMembership owningMember = fact.createOwningMembership();
        owningMember.getOwnedRelatedElement().add(featuringType);
        metaDataUsage.getOwnedRelationship().add(owningMember);

        return metaDataUsage;
    }

    private void addSuperType(Classifier child, Classifier parent) {
        Subclassification subClassification = fact.createSubclassification();
        subClassification.setSuperclassifier(parent);
        subClassification.setSpecific(child);

        child.getOwnedRelationship().add(subClassification);

    }

    private void addOwnedMembership(Element parent, Element ownedElement) {
        addOwnedMembership(parent, ownedElement, null);
    }

    private void addOwnedMembership(Element parent, Element ownedElement, VisibilityKind visibility) {
        OwningMembership ownedRelation = fact.createOwningMembership();
        parent.getOwnedRelationship().add(ownedRelation);
        ownedRelation.getOwnedRelatedElement().add(ownedElement);

        if (visibility != null) {

            ownedRelation.setVisibility(visibility);
        }

    }

    private String convertToText(Element source, Element context, int indent) {
        return new SysMLElementSerializer("\n", "    ").doSwitch(source);
    }

    private String convertToText(Element source) {
        return convertToText(source, (Element) source.eContainer(), 0);
    }

    private void assertTextualFormEquals(String extexted, Element elementToTest) {
        String content = convertToText(elementToTest);
        assertEquals(extexted, content);
    }

}
