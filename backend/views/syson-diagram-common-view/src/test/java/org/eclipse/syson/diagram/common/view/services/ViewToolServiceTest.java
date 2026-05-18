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
package org.eclipse.syson.diagram.common.view.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.services.api.MoveStatus;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ViewToolService}.
 *
 * @author Arthur Daussy
 */
public class ViewToolServiceTest {

    private static final String FAILURE_MESSAGE = "move refused";

    @DisplayName("GIVEN a failed semantic move, WHEN becoming a nested usage, THEN a warning feedback message uses labels")
    @Test
    void testBecomeNestedUsageFailureCreatesFeedbackMessageWithLabels() {
        Usage usage = SysmlFactory.eINSTANCE.createPartUsage();
        PartUsage newContainer = SysmlFactory.eINSTANCE.createPartUsage();
        TestServices testServices = this.createService(usage, "Nested usage", newContainer, "New container");

        Usage result = testServices.service.becomeNestedUsage(usage, newContainer);

        assertThat(result).isSameAs(usage);
        assertThat(usage.isIsComposite()).isFalse();
        assertThat(testServices.feedbackMessageService.getFeedbackMessages()).containsExactly(new Message("Unable to move Nested usage in New container: move refused", MessageLevel.WARNING));
        assertThat(testServices.moveService.movedElement).isSameAs(usage);
        assertThat(testServices.moveService.newParent).isSameAs(newContainer);
        assertThat(testServices.moveService.callCount).isEqualTo(1);
    }

    @DisplayName("GIVEN a failed semantic move, WHEN reconnecting a composition source, THEN a warning feedback message uses labels")
    @Test
    void testReconnectSourceCompositionEdgeFailureCreatesFeedbackMessageWithLabels() {
        PartUsage self = SysmlFactory.eINSTANCE.createPartUsage();
        PartUsage newSource = SysmlFactory.eINSTANCE.createPartUsage();
        PartUsage otherEnd = SysmlFactory.eINSTANCE.createPartUsage();
        TestServices testServices = this.createService(self, "Composition owner", newSource, "New source", otherEnd, "Nested part");

        Element result = testServices.service.reconnnectSourceCompositionEdge(self, newSource, otherEnd);

        assertThat(result).isSameAs(otherEnd);
        assertThat(testServices.feedbackMessageService.getFeedbackMessages()).containsExactly(new Message("Unable to move Composition owner in New source: move refused", MessageLevel.WARNING));
        assertThat(testServices.moveService.movedElement).isSameAs(otherEnd);
        assertThat(testServices.moveService.newParent).isSameAs(newSource);
        assertThat(testServices.moveService.callCount).isEqualTo(1);
    }

    @DisplayName("GIVEN a failed semantic move, WHEN reconnecting an annotation target, THEN a warning feedback message uses labels")
    @Test
    void testReconnectTargetAnnotatedEdgeFailureCreatesFeedbackMessageWithLabels() {
        PartUsage self = SysmlFactory.eINSTANCE.createPartUsage();
        PartUsage newTarget = SysmlFactory.eINSTANCE.createPartUsage();
        TestServices testServices = this.createService(self, "Annotation", newTarget, "Annotated element");

        Element result = testServices.service.reconnnectTargetAnnotatedEdge(self, newTarget);

        assertThat(result).isSameAs(self);
        assertThat(testServices.feedbackMessageService.getFeedbackMessages()).containsExactly(new Message("Unable to move Annotation in Annotated element: move refused", MessageLevel.WARNING));
        assertThat(testServices.moveService.movedElement).isSameAs(self);
        assertThat(testServices.moveService.newParent).isSameAs(newTarget);
        assertThat(testServices.moveService.callCount).isEqualTo(1);
    }

    private TestServices createService(Object firstObject, String firstLabel, Object secondObject, String secondLabel) {
        return this.createService(firstObject, firstLabel, secondObject, secondLabel, null, null);
    }

    private TestServices createService(Object firstObject, String firstLabel, Object secondObject, String secondLabel, Object thirdObject, String thirdLabel) {
        TestMoveElementService moveService = new TestMoveElementService(MoveStatus.buildFailure(FAILURE_MESSAGE));
        CapturingFeedbackMessageService feedbackMessageService = new CapturingFeedbackMessageService();
        TestLabelService labelService = new TestLabelService()
                .withLabel(firstObject, firstLabel)
                .withLabel(secondObject, secondLabel);
        if (thirdObject != null) {
            labelService.withLabel(thirdObject, thirdLabel);
        }

        ViewToolService service = new ViewToolService(
                new IIdentityService.NoOp(),
                new IObjectSearchService.NoOp(),
                new IViewRepresentationDescriptionSearchService.NoOp(),
                feedbackMessageService,
                moveService,
                new ISysONExplorerService.NoOp(),
                labelService);
        return new TestServices(service, moveService, feedbackMessageService);
    }

    /**
     * Services used by a test case.
     *
     * @param service
     *            the tested service
     * @param moveService
     *            the mocked move service
     * @param feedbackMessageService
     *            the feedback message service collecting messages
     */
    private record TestServices(ViewToolService service, TestMoveElementService moveService, CapturingFeedbackMessageService feedbackMessageService) {
    }

    /**
     * Test move service returning a fixed status and recording calls.
     */
    private static final class TestMoveElementService implements ISysMLMoveElementService {

        private final MoveStatus moveStatus;

        private Element movedElement;

        private Element newParent;

        private int callCount;

        TestMoveElementService(MoveStatus moveStatus) {
            this.moveStatus = moveStatus;
        }

        @Override
        public MoveStatus moveSemanticElement(Element element, Element targetParent) {
            this.movedElement = element;
            this.newParent = targetParent;
            this.callCount++;
            return this.moveStatus;
        }
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
