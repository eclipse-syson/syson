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
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.export.utils.Appender;
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

    private void addOwnedMembership(Element parent, Element ownedElement) {
        OwningMembership ownedRelation = fact.createOwningMembership();
        parent.getOwnedRelationship().add(ownedRelation);
        ownedRelation.getOwnedRelatedElement().add(ownedElement);

    }

    private String convertToText(Element source, Element context, int indent) {
        return new SysMLElementSerializer(() -> new Appender("\n", "    ")).doSwitch(source);
    }

    private String convertToText(Element source) {
        return convertToText(source, (Element) source.eContainer(), 0);
    }

    private void assertTextualFormEquals(String extexted, Element elementToTest) {
        String content = convertToText(elementToTest);
        assertEquals(extexted, content);
    }

}
