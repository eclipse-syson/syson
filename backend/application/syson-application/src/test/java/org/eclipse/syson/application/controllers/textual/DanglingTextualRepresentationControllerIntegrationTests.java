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
package org.eclipse.syson.application.controllers.textual;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.UUID;
import java.util.function.BiFunction;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.dto.CreateRepresentationInput;
import org.eclipse.sirius.components.collaborative.dto.CreateRepresentationSuccessPayload;
import org.eclipse.sirius.components.core.api.IEditService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.graphql.tests.CreateRepresentationMutationRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.eclipse.syson.textual.view.description.TextualViewDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 * Used to test the proper deletion of dangling textual representation.
 *
 * @author gcoutable
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { "org.eclipse.syson.experimental.textual.representation.enabled=true" })
public class DanglingTextualRepresentationControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private CreateRepresentationMutationRunner createRepresentationMutationRunner;

    @Autowired
    private IRepresentationMetadataSearchService representationMetadataSearchService;

    @Autowired
    private ExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private IEditService editService;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("GIVEN a project, WHEN creating a textual representation on a semantic element and removing that same element THEN the representation is also removed")
    public void testDeleteElementWithWithChildTextualRepresentation() {
        var result = this.createRepresentationMutationRunner.run(
                new CreateRepresentationInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, TextualViewDescription.ID,
                        GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID, "textual representation"));

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        String typename = JsonPath.read(result.data(), "$.data.createRepresentation.__typename");
        assertThat(typename).isEqualTo(CreateRepresentationSuccessPayload.class.getSimpleName());

        String representationId = JsonPath.read(result.data(), "$.data.createRepresentation.representation.id");
        assertThat(representationId).isNotNull();

        var checkRepresentationMetadataCreation = this.representationMetadataSearchService.findMetadataById(AggregateReference.to(UUID.fromString(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID)), UUID.fromString(representationId));
        assertThat(checkRepresentationMetadataCreation).isPresent();

        BiFunction<IEditingContext, IInput, IPayload> function = (editingContext, input) -> {
            this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID)
                    .ifPresent(this.editService::delete);
            return new SuccessPayload(input.id());
        };

        var inputId = UUID.randomUUID();
        var changeDescription = new ChangeDescription(ChangeKind.SEMANTIC_CHANGE, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, () -> inputId);
        var input = new ExecuteEditingContextFunctionInput(inputId, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, function, changeDescription);
        var payload = this.executeEditingContextFunctionRunner.execute(input).block();
        assertThat(payload).isInstanceOf(SuccessPayload.class);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        var checkRepresentationMetadataDeletion = this.representationMetadataSearchService.findMetadataById(AggregateReference.to(UUID.fromString(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID)), UUID.fromString(representationId));
        assertThat(checkRepresentationMetadataDeletion).isEmpty();
    }
}
