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
package org.eclipse.syson.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.syson.services.grammars.DirectEditLexer;
import org.eclipse.syson.services.grammars.DirectEditListener;
import org.eclipse.syson.services.grammars.DirectEditParser;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gdaniel
 */
public class DiagramDirectEditListenerTest {

    @DisplayName("Given a ConstraintUsage, when it is edited with '1 >= 2', then its expression is set")
    @Test
    public void testDirectEditConstraintUsageWithBooleanExpression() {

    }

    @DisplayName("Given a ConstraintUsage, when it is edited with 'feature >= 1', then its expression is set")
    @Test
    public void testDirectEditConstraintUsageWithFeatureReferenceExpression() {

    }

    @DisplayName("Given a ConstraintUsage, when it is edited with 'feature1.feature2 >= 1', then its expression is set")
    @Test
    public void testDirectEditConstraintUsageWithSingleFeatureChainingExpression() {

    }

    @DisplayName("Given a ConstraintUsage, when it is edited with 'feature1.feature2.feature3 >= 1', then its expression is set")
    @Test
    public void testDirectEditConstraintUsageWithMultipleFeatureChainingExpression() {

    }

    private void doDirectEdit(Element element, String input) {
        DirectEditLexer lexer = new DirectEditLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectEditParser parser = new DirectEditParser(tokens);
        ParseTree tree;
        if (element instanceof ConstraintUsage) {
            tree = parser.constraintExpression();
        } else {
            tree = parser.expression();
        }
        ParseTreeWalker walker = new ParseTreeWalker();
        DirectEditListener listener = new DiagramDirectEditListener(element, new TestFeedbackMessageService());
        walker.walk(listener, tree);
    }

    /**
     * A test-level implementation of {@link IFeedbackMessageService}.
     *
     * @author jmallet
     */
    public class TestFeedbackMessageService implements IFeedbackMessageService {

        private final List<Message> feedbackMessages = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void addFeedbackMessage(Message message) {
            this.feedbackMessages.add(message);
        }

        @Override
        public List<Message> getFeedbackMessages() {
            return this.feedbackMessages;
        }
    }

}
