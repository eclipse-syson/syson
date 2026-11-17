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

import java.util.Deque;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Context to start a new {@link EObject}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class StartContext extends AbstractContext {

    /**
     * The {@link EClass} to instantiate.
     */
    private final EClass eClass;

    /**
     * The factory used to create SysML objects with textual defaults.
     */
    private final SysMLFactory sysMLFactory;

    /**
     * Constructor.
     *
     * @param eClass
     *            the {@link EClass} to instantiate.
     * @param operandStack
     *            the operand stack
     * @param danglingLinks
     *            the {@link List} of {@link DanglingLink}
     * @param textAdapted
     *            the {@link Set} of {@link EObject} with {@link ITextAdapter}
     * @param internalDiagnostics
     *            the internal {@link Diagnostic} {@link List}
     */
    public StartContext(EClass eClass, Deque<Object> operandStack, List<DanglingLink> danglingLinks, Set<EObject> textAdapted, List<Diagnostic> internalDiagnostics,
            SysMLFactory sysMLFactory) {
        super(operandStack, danglingLinks, textAdapted, internalDiagnostics);
        this.eClass = eClass;
        this.sysMLFactory = sysMLFactory;
    }

    @Override
    protected EObject getEObject() {
        if (this.eClass.getEPackage() == SysMLPackage.eINSTANCE) {
            return this.sysMLFactory.create(this.eClass);
        }
        return EcoreUtil.create(this.eClass);
    }

}
