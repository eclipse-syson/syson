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

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;

/**
 * Context to restart the last {@link EObject} operand.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class RestartContext extends AbstractContext {

    /**
     * The {@link EObject} to continue.
     *
     */
    private final EObject eObject;

    /**
     * Constructor.
     *
     * @param ruleContext
     *            the current {@link ParserRuleContext}.
     * @param operandStack
     *            the operand stack
     * @param danglingLinks
     *            the {@link List} of {@link DanglingLink}
     * @param textAdapted
     *            the {@link Set} of {@link EObject} with {@link ITextAdapter}
     * @param internalDiagnostics
     *            the internal {@link Diagnostic} {@link List}
     */
    public RestartContext(ParserRuleContext ruleContext, Deque<Object> operandStack, List<DanglingLink> danglingLinks, Set<EObject> textAdapted, List<Diagnostic> internalDiagnostics) {
        super(ruleContext, operandStack, danglingLinks, textAdapted, internalDiagnostics);
        this.eObject = (EObject) operandStack.removeLast();
    }

    @Override
    protected EObject getEObject() {
        return this.eObject;
    }

}
