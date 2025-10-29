/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.diagram.services;

import java.text.MessageFormat;
import java.util.Objects;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.direct.edit.grammars.DirectEditLexer;
import org.eclipse.syson.direct.edit.grammars.DirectEditParser;
import org.eclipse.syson.services.DiagramDirectEditListener;
import org.eclipse.syson.services.LabelService;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.springframework.stereotype.Service;

/**
 * Label-related services doing mutations in diagrams.
 *
 * @author arichard
 */
@Service
public class DiagramMutationLabelService {

    private final IFeedbackMessageService feedbackMessageService;

    public DiagramMutationLabelService(IFeedbackMessageService feedbackMessageService) {
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
    }

    /**
     * Apply the direct edit result (i.e. the newLabel) to the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @param newLabel
     *            the new value to apply.
     * @return the given {@link Element}.
     */
    public Element directEdit(Element element, String newLabel) {
        return this.directEdit(element, newLabel, (String[]) null);
    }

    /**
     * Apply the direct edit result (i.e. the newLabel) to the given {@link Element}, with the provided {@code options}.
     * <p>
     * This method is typically used to enable direct edit with some restrictions (e.g. de-activate the ability to edit
     * the name of an element via direct edit). See {@link #directEdit(Element, String)} to perform a direct edit with
     * default options.
     * </p>
     *
     * @param element
     *            the given {@link Element}
     * @param newLabel
     *            the new value to apply
     * @param options
     *            the options of the direct edit
     * @return the given {@link Element}
     */
    public Element directEdit(Element element, String newLabel, String... options) {
        return this.directEdit(element, newLabel, false, options);
    }

    /**
     * Apply the direct edit result (i.e. the newLabel) to the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @param newLabel
     *            the new value to apply.
     * @return the given {@link Element}.
     */
    public Element directEdit(Element element, String newLabel, boolean isCompartmentItem, String... options) {
        DirectEditLexer lexer = new DirectEditLexer(CharStreams.fromString(newLabel));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectEditParser parser = new DirectEditParser(tokens);
        ParseTree tree;
        if (element instanceof ConstraintUsage && element.getOwningMembership() instanceof RequirementConstraintMembership && isCompartmentItem) {
            // Use the constraint expression parser only if the element is a constraint owned by a requirement, other
            // constraints (including requirements) are parsed as regular elements.
            tree = parser.constraintExpression();
        } else if (isCompartmentItem) {
            tree = parser.listItemExpression();
        } else {
            tree = parser.nodeExpression();
        }
        ParseTreeWalker walker = new ParseTreeWalker();
        DiagramDirectEditListener listener = new DiagramDirectEditListener(element, this.feedbackMessageService, options);
        walker.walk(listener, tree);
        listener.resolveProxies().forEach(proxy -> {
            this.feedbackMessageService.addFeedbackMessage(new Message(MessageFormat.format("Unable to resolve \u2035{0}\u2035", proxy.nameToResolve()), MessageLevel.WARNING));
        });
        return element;
    }

    /**
     * Apply the direct edit result (i.e. the newLabel) to the given graphical node {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @param newLabel
     *            the new value to apply.
     * @return the given {@link Element}.
     */
    public Element directEditNode(Element element, String newLabel) {
        return this.directEdit(element, newLabel, false, (String[]) null);
    }

    /**
     * Apply the direct edit result (i.e. the newLabel) to the given graphical list item {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @param newLabel
     *            the new value to apply.
     * @return the given {@link Element}.
     */
    public Element directEditListItem(Element element, String newLabel) {
        return this.directEdit(element, newLabel, true, (String[]) null);
    }

    public Element editMultiplicityRangeCenterLabel(Element element, String newLabel) {
        return this.directEdit(element, newLabel, LabelService.NAME_OFF, LabelService.REDEFINITION_OFF, LabelService.SUBSETTING_OFF, LabelService.TYPING_OFF, LabelService.VALUE_OFF);
    }

    public Element editEdgeCenterLabel(Element element, String newLabel) {
        return this.directEdit(element, newLabel, LabelService.REDEFINITION_OFF, LabelService.SUBSETTING_OFF, LabelService.VALUE_OFF);
    }
}
