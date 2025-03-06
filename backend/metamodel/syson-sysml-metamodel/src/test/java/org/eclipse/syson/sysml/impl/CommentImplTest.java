/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.sysml.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.emf.common.util.EList;
import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link CommentImpl}.
 *
 * @author Arthur Daussy
 */
public class CommentImplTest {

    private final SysmlFactory fact = SysmlFactory.eINSTANCE;

    @Test
    public void annotatedElementsEmpty() {
        Comment comment = this.fact.createComment();
        assertTrue(comment.getAnnotatedElement().isEmpty());
    }

    /**
     * If there is no annotation, returns the owning namespace.
     */
    @Test
    public void annotatedElementsOwningNameSpace() {
        Comment comment = this.fact.createComment();

        Package pack1 = this.fact.createPackage();
        pack1.setDeclaredName("Pack1");

        OwningMembership ownedRelation = this.fact.createOwningMembership();
        pack1.getOwnedRelationship().add(ownedRelation);
        ownedRelation.getOwnedRelatedElement().add(comment);

        EList<Element> annotatedElements = comment.getAnnotatedElement();
        assertEquals(1, annotatedElements.size(), "Annotated element should return the owning namespace");
        assertEquals(pack1, annotatedElements.get(0), "Annotated element should return the owning namespace");

    }

    /**
     * If any annotation, return the annotated elements.
     */
    @Test
    public void annotatedElementsAnnotations() {

        Package pack1 = this.fact.createPackage();
        pack1.setDeclaredName("Pack1");

        Package pack2 = this.fact.createPackage();
        pack2.setDeclaredName("Pack2");

        Comment comment = this.fact.createComment();

        OwningMembership ownedRelation = this.fact.createOwningMembership();
        pack1.getOwnedRelationship().add(ownedRelation);
        ownedRelation.getOwnedRelatedElement().add(comment);

        Annotation annotation1 = this.fact.createAnnotation();

        comment.getOwnedRelationship().add(annotation1);
        assertTrue(comment.getAnnotation().contains(annotation1));

        annotation1.setAnnotatedElement(pack2);

        EList<Element> annotatedElements = comment.getAnnotatedElement();
        assertEquals(1, annotatedElements.size(), "Annotated element should return pack2");
        assertEquals(pack2, annotatedElements.get(0), "Annotated element should return the pack2");

    }

}
