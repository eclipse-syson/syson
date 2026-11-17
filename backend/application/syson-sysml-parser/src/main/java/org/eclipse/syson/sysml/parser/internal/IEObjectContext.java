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
package org.eclipse.syson.sysml.parser.internal;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.adapter.CommentAdapter;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;

/**
 * Context for the creation of an {@link EObject}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IEObjectContext {

    /**
     * Creates the {@link EObject}.
     *
     * @return the created {@link EObject}
     */
    EObject createEObject();

    /**
     * Sets the given {@link EStructuralFeature} with the operand at the top of the operand stack.
     *
     * @param feature
     *            the {@link EStructuralFeature}
     */
    void set(EStructuralFeature feature);

    /**
     * Sets the given {@link EStructuralFeature} with the given value.
     *
     * @param feature
     *            the {@link EStructuralFeature}
     * @param value
     *            the value
     */
    void set(EStructuralFeature feature, Object value);

    /**
     * Adds the operand at the top of the stack to the given {@link EStructuralFeature}.
     *
     * @param feature
     *            the {@link EStructuralFeature}
     */
    void add(EStructuralFeature feature);

    /**
     * Adds the given value to the given {@link EStructuralFeature}.
     *
     * @param feature
     *            the {@link EStructuralFeature}
     * @param value
     *            the value to add
     */
    void add(EStructuralFeature feature, Object value);

    /**
     * Sets a {@link DanglingLink} for the given {@link EReference} with the operand at the top of the stack.
     *
     * @param reference
     *            the {@link EReference}
     * @param eClass
     *            the targeted {@link EClass}
     */
    void setLink(EReference reference, EClass eClass);

    /**
     * Sets a {@link DanglingLink} for the given {@link EReference} with the given value.
     *
     * @param reference
     *            the {@link EReference}
     * @param eClass
     *            the targeted {@link EClass}
     * @param value
     *            the value
     */
    void setLink(EReference reference, EClass eClass, String value);

    /**
     * Adds a {@link DanglingLink} for the operand at the top of the stack to the given {@link EReference}.
     *
     * @param reference
     *            the {@link EReference}
     * @param eClass
     *            the targeted {@link EClass}
     */
    void addLink(EReference reference, EClass eClass);

    /**
     * Adds a {@link DanglingLink} for the given value to the given {@link EReference}.
     *
     * @param reference
     *            the {@link EReference}
     * @param eClass
     *            the targeted {@link EClass}
     * @param value
     *            the value
     */
    void addLink(EReference reference, EClass eClass, String value);

    /**
     * Attaches the given {@link ITextAdapter}.
     *
     * @param adapter
     *            the {@link ITextAdapter} to attach
     */
    void attach(ITextAdapter adapter);

    /**
     * Attaches the given {@link CommentAdapter}.
     *
     * @param adapter
     *            the {@link CommentAdapter} to attach
     */
    void attachCommentAdapter(CommentAdapter adapter);

}
