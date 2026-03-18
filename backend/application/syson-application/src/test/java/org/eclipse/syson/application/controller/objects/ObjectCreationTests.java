/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.application.controller.objects;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.dto.CreateChildInput;
import org.eclipse.sirius.components.collaborative.dto.CreateChildSuccessPayload;
import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.sirius.web.tests.graphql.CreateChildMutationRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.application.services.SysMLv2EditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests the object creations.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ObjectCreationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private CreateChildMutationRunner createChildMutationRunner;

    @Autowired
    private IRepresentationMetadataSearchService representationMetadataSearchService;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN an empty SysML Project, WHEN a ViewUsage is created under the root Package, THEN a General View diagram is also created on this ViewUsage")
    @GivenSysONServer({GeneralViewEmptyTestProjectData.SCRIPT_PATH})
    @Test
    public void createGeneralViewDiagramAtTheSameTimeAsViewUsage() {
        Optional<UUID> optionalSemanticData = new UUIDParser().parse(GeneralViewEmptyTestProjectData.EDITING_CONTEXT);
        assertThat(optionalSemanticData).isPresent();
        var representationMetadatas = this.representationMetadataSearchService.findAllRepresentationMetadataBySemanticData(AggregateReference.to(optionalSemanticData.get()));
        assertThat(representationMetadatas).hasSize(1);
        assertThat(representationMetadatas.get(0)).extracting("label").isEqualTo("General View");

        var input = new CreateChildInput(
                UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID,
                SysMLv2EditService.ID_PREFIX + "ViewUsage");
        var result = this.createChildMutationRunner.run(input);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        String typename = JsonPath.read(result.data(), "$.data.createChild.__typename");
        assertThat(typename).isEqualTo(CreateChildSuccessPayload.class.getSimpleName());

        String objectId = JsonPath.read(result.data(), "$.data.createChild.object.id");
        assertThat(objectId).isNotBlank();

        String objectLabel = JsonPath.read(result.data(), "$.data.createChild.object.label");
        // a ViewUsage already exists in the GeneralViewEmpty project, so the new one is the second one
        assertThat(objectLabel).isEqualTo("view2");

        String objectKind = JsonPath.read(result.data(), "$.data.createChild.object.kind");
        assertThat(objectKind).isEqualTo("siriusComponents://semantic?domain=sysml&entity=ViewUsage");

        representationMetadatas = this.representationMetadataSearchService.findAllRepresentationMetadataBySemanticData(AggregateReference.to(optionalSemanticData.get()));
        assertThat(representationMetadatas).hasSize(2)
                .anySatisfy(mdt -> assertThat(mdt.getLabel()).isEqualTo("General View"))
                .anySatisfy(mdt -> assertThat(mdt.getLabel()).isEqualTo("view2"));

    }
}
