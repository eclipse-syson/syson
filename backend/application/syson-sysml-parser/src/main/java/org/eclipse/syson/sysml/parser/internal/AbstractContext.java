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

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.adapter.CommentAdapter;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;

/**
 * Context to build an {@link EObject}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractContext implements IEObjectContext {

    /**
     * Operator interface used to implement set, add, ...
     *
     * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
     */
    private interface Operator {

        /**
         * Executes the operator.
         *
         * @param eObject
         *            the context {@link EObject}
         * @param operandStack
         *            the operand stack
         * @param manySettingsHolder
         *            the mapping of {@link EStructuralFeature#isMany() many} {@link EStructuralFeature} to the
         *            {@link List} of values
         * @param manyLinkSettingsHolder
         *            the mapping of {@link EStructuralFeature#isMany() many} not {@link EReference#isContainment()}
         *            containment {@link EReference} to the targeted {@link EClass} to the {@link List} of symbols
         * @param textAdapted
         *            the {@link Set} of {@link EObject} with {@link ITextAdapter}
         */
        void exec(EObject eObject, Deque<Object> operandStack, Map<EStructuralFeature, List<Object>> manySettingsHolder, List<DanglingLink> danglingLinks,
                Map<EReference, Map<EClass, List<String>>> manyLinkSettingsHolder, Set<EObject> textAdapted);

    }

    /**
     * The {@link Operator} stack.
     *
     */
    private final Deque<Operator> operatorStack = new ArrayDeque<>();

    /**
     * The operand stack.
     *
     */
    private final Deque<Object> operandStack;

    /**
     * The {@link List} of {@link DanglingLink}.
     *
     */
    private final List<DanglingLink> danglingLinks;

    /**
     * The {@link Set} of {@link EObject} with a {@link ITextAdapter}.
     */
    private final Set<EObject> textAdapted;

    /**
     * The internal {@link Diagnostic} {@link List}.
     */
    private final List<Diagnostic> internalDiagnostics;

    /**
     * Constructor.
     *
     * @param operandStack
     *            the operand stack
     * @param danglingLinks
     *            the {@link List} of {@link DanglingLink}
     * @param textAdapted
     *            {@link Set} of {@link EObject} with {@link ITextAdapter}
     * @param internalDiagnostics
     *            the internal {@link Diagnostic} {@link List}
     */
    public AbstractContext(Deque<Object> operandStack, List<DanglingLink> danglingLinks, Set<EObject> textAdapted, List<Diagnostic> internalDiagnostics) {
        this.operandStack = operandStack;
        this.danglingLinks = danglingLinks;
        this.textAdapted = textAdapted;
        this.internalDiagnostics = internalDiagnostics;
    }

    /**
     * Creates the {@link EObject}.
     *
     * @return the created {@link EObject}
     */
    @Override
    public EObject createEObject() {
        EObject res = this.getEObject();

        Iterator<Operator> it = this.operatorStack.descendingIterator();
        Map<EStructuralFeature, List<Object>> manySettingsHolder = new HashMap<>();
        Map<EReference, Map<EClass, List<String>>> manyLinkSettingsHolder = new LinkedHashMap<>();
        while (it.hasNext()) {
            try {
                it.next().exec(res, this.operandStack, manySettingsHolder, this.danglingLinks, manyLinkSettingsHolder, this.textAdapted);
            } catch (IllegalArgumentException | IllegalStateException | IndexOutOfBoundsException | NoSuchElementException | NullPointerException | ClassCastException
                    | UnsupportedOperationException e) {
                BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.ERROR, null, 0, e.getMessage(), new Object[] { e });
                this.internalDiagnostics.add(diagnostic);
            }
        }

        for (Entry<EStructuralFeature, List<Object>> entry : manySettingsHolder.entrySet()) {
            EStructuralFeature feature = entry.getKey();
            try {
                // Parser actions consume each newly created contained object once, so these
                // values are unique by construction. Skip EMF's redundant duplicate scan.
                @SuppressWarnings({ "rawtypes", "unchecked" })
                InternalEList<Object> values = (InternalEList<Object>) res.eGet(feature);
                values.addAllUnique(entry.getValue());
            } catch (IllegalArgumentException | IllegalStateException | IndexOutOfBoundsException | NoSuchElementException | NullPointerException | ClassCastException
                    | UnsupportedOperationException | ArrayStoreException e) {
                BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.ERROR, null, 0, e.getMessage(), new Object[] { e });
                this.internalDiagnostics.add(diagnostic);
            }
        }

        for (Entry<EReference, Map<EClass, List<String>>> referenceEntry : manyLinkSettingsHolder.entrySet()) {
            for (Entry<EClass, List<String>> eClsEntry : referenceEntry.getValue().entrySet()) {
                this.danglingLinks.add(new DanglingLink(res, referenceEntry.getKey(), eClsEntry.getKey(), eClsEntry.getValue()));
            }
        }

        return res;
    }

    /**
     * Gets the resulting {@link EObject}.
     *
     * @return the resulting {@link EObject}
     */
    protected abstract EObject getEObject();

    /**
     * Pushes the given {@link Operator}.
     *
     * @param operator
     *            the {@link Operator}
     */
    private void pushOperator(Operator operator) {
        this.operatorStack.addLast(operator);
    }

    @Override
    public void set(EStructuralFeature feature) {
        Operator operator = (eObject, stack, manySettingsHolder, pendingLinks, manyLinkSettingsHolder, adaptedElements) -> this.set(feature, stack, eObject);
        this.pushOperator(operator);
    }

    private void set(EStructuralFeature feature, Deque<Object> stack, EObject eObject) {
        Object value = stack.removeLast();
        eObject.eSet(feature, value);
    }

    @Override
    public void set(EStructuralFeature feature, Object value) {
        Operator operator = (eObject, stack, manySettingsHolder, pendingLinks, manyLinkSettingsHolder, adaptedElements) -> this.set(feature, value, eObject);
        this.pushOperator(operator);
    }

    private void set(EStructuralFeature feature, Object value, EObject eObject) {
        eObject.eSet(feature, value);
    }

    @Override
    public void add(EStructuralFeature feature) {
        Operator operator = (eObject, stack, manySettingsHolder, pendingLinks, manyLinkSettingsHolder, adaptedElements) -> this.add(feature, stack, manySettingsHolder);
        this.pushOperator(operator);
    }

    private void add(EStructuralFeature feature, Deque<Object> stack, Map<EStructuralFeature, List<Object>> manySettingsHolder) {
        Object value = stack.removeLast();
        manySettingsHolder.computeIfAbsent(feature, f -> new LinkedList<>()).add(0, value);
    }

    @Override
    public void add(EStructuralFeature feature, Object value) {
        Operator operator = (eObject, stack, manySettingsHolder, pendingLinks, manyLinkSettingsHolder, adaptedElements) -> this.add(feature, value, manySettingsHolder);
        this.pushOperator(operator);
    }

    private void add(EStructuralFeature feature, Object value, Map<EStructuralFeature, List<Object>> manySettingsHolder) {
        manySettingsHolder.computeIfAbsent(feature, f -> new LinkedList<>()).add(0, value);
    }

    @Override
    public void setLink(EReference reference, EClass eClass) {
        Operator operator = (eObject, stack, manySettingsHolder, pendingLinks, manyLinkSettingsHolder, adaptedElements) -> this.setLink(reference, eClass, stack, eObject, pendingLinks);
        this.pushOperator(operator);
    }

    private void setLink(EReference reference, EClass eClass, Deque<Object> stack, EObject eObject, List<DanglingLink> pendingLinks) {
        String value = (String) stack.removeLast();
        pendingLinks.add(new DanglingLink(eObject, reference, eClass, Collections.singletonList(value)));
    }

    @Override
    public void setLink(EReference reference, EClass eClass, String value) {
        Operator operator = (eObject, stack, manySettingsHolder, pendingLinks, manyLinkSettingsHolder, adaptedElements) -> this.setLink(reference, eClass, value, eObject, pendingLinks);
        this.pushOperator(operator);
    }

    private void setLink(EReference reference, EClass eClass, String value, EObject eObject, List<DanglingLink> pendingLinks) {
        pendingLinks.add(new DanglingLink(eObject, reference, eClass, Collections.singletonList(value)));
    }

    @Override
    public void addLink(EReference reference, EClass eClass) {
        Operator operator = (eObject, stack, manySettingsHolder, pendingLinks, manyLinkSettingsHolder, adaptedElements) -> this.addLink(reference, eClass, stack, manyLinkSettingsHolder);
        this.pushOperator(operator);
    }

    private void addLink(EReference reference, EClass eClass, Deque<Object> stack, Map<EReference, Map<EClass, List<String>>> manyLinkSettingsHolder) {
        String value = (String) stack.removeLast();
        manyLinkSettingsHolder.computeIfAbsent(reference, f -> new LinkedHashMap<>()).computeIfAbsent(eClass, eCls -> new LinkedList<>()).add(0, value);
    }

    @Override
    public void addLink(EReference reference, EClass eClass, String value) {
        Operator operator = (eObject, stack, manySettingsHolder, pendingLinks, manyLinkSettingsHolder, adaptedElements) -> this.addLink(reference, eClass, value, manyLinkSettingsHolder);
        this.pushOperator(operator);
    }

    private void addLink(EReference reference, EClass eClass, String value, Map<EReference, Map<EClass, List<String>>> manyLinkSettingsHolder) {
        manyLinkSettingsHolder.computeIfAbsent(reference, f -> new LinkedHashMap<>()).computeIfAbsent(eClass, eCls -> new LinkedList<>()).add(0, value);
    }

    @Override
    public void attach(ITextAdapter adapter) {
        Operator operator = (eObject, stack, manySettingsHolder, pendingLinks, manyLinkSettingsHolder, adaptedElements) -> this.attach(eObject, adaptedElements, adapter);
        this.pushOperator(operator);
    }

    private Object attach(EObject eObject, Set<EObject> adaptedElements, ITextAdapter adapter) {
        adaptedElements.add(eObject);
        return eObject.eAdapters().add(adapter);
    }

    @Override
    public void attachCommentAdapter(CommentAdapter adapter) {
        Operator operator = (eObject, stack, manySettingsHolder, pendingLinks, manyLinkSettingsHolder, adaptedElements) -> eObject.eAdapters().add(adapter);
        this.pushOperator(operator);
    }

}
