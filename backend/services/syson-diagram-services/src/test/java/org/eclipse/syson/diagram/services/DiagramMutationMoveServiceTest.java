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
package org.eclipse.syson.diagram.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.services.api.MoveStatus;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DiagramMutationMoveService}.
 *
 * @author Arthur
 */
public class DiagramMutationMoveServiceTest {

    private static final String DROPPED_LABEL = "Dropped part";

    private static final String TARGET_LABEL = "Target part";

    @DisplayName("GIVEN a failed semantic move, WHEN moving an element in a diagram, THEN a warning feedback message uses labels")
    @Test
    void testMoveElementFailureCreatesFeedbackMessageWithLabels() {
        PartUsage droppedElement = SysmlFactory.eINSTANCE.createPartUsage();
        PartUsage targetElement = SysmlFactory.eINSTANCE.createPartUsage();
        ISysMLMoveElementService moveService = mock(ISysMLMoveElementService.class);
        DiagramMutationExposeService diagramMutationExposeService = mock(DiagramMutationExposeService.class);
        DiagramQueryElementService diagramQueryElementService = mock(DiagramQueryElementService.class);
        CapturingFeedbackMessageService feedbackMessageService = new CapturingFeedbackMessageService();
        TestLabelService labelService = new TestLabelService()
                .withLabel(droppedElement, DROPPED_LABEL)
                .withLabel(targetElement, TARGET_LABEL);
        DiagramMutationMoveService service = new DiagramMutationMoveService(feedbackMessageService, labelService, moveService, diagramMutationExposeService, diagramQueryElementService);

        when(moveService.moveSemanticElement(droppedElement, targetElement)).thenReturn(MoveStatus.buildFailure("move refused"));

        service.moveElement(droppedElement, null, targetElement, null, null, null, null);

        assertThat(feedbackMessageService.getFeedbackMessages()).containsExactly(new Message("Unable to move Dropped part in Target part: move refused", MessageLevel.WARNING));
        verify(moveService).moveSemanticElement(droppedElement, targetElement);
        verifyNoInteractions(diagramMutationExposeService, diagramQueryElementService);
    }

    /**
     * Test feedback service collecting messages in memory.
     */
    private static final class CapturingFeedbackMessageService implements IFeedbackMessageService {

        private final List<Message> feedbackMessages = new ArrayList<>();

        @Override
        public void addFeedbackMessage(Message message) {
            this.feedbackMessages.add(message);
        }

        @Override
        public List<Message> getFeedbackMessages() {
            return this.feedbackMessages;
        }
    }

    /**
     * Test label service returning labels by object identity.
     */
    private static final class TestLabelService extends ILabelService.NoOp {

        private final IdentityHashMap<Object, StyledString> labels = new IdentityHashMap<>();

        TestLabelService withLabel(Object object, String label) {
            this.labels.put(object, StyledString.of(label));
            return this;
        }

        @Override
        public StyledString getStyledLabel(Object object) {
            return this.labels.getOrDefault(object, StyledString.of(""));
        }
    }
}
