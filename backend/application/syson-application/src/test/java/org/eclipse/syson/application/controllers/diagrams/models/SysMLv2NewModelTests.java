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
package org.eclipse.syson.application.controllers.diagrams.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.UUID;
import java.util.stream.Stream;

import org.eclipse.sirius.web.application.document.dto.CreateDocumentInput;
import org.eclipse.sirius.web.application.document.dto.CreateDocumentSuccessPayload;
import org.eclipse.sirius.web.tests.graphql.CreateDocumentMutationRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.application.sysmlv2.SysMLv2StereotypeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests for SysMLv2StereotypeHandler.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SysMLv2NewModelTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private CreateDocumentMutationRunner createDocumentMutationRunner;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    private static Stream<Arguments> createDocumentParameters() {
        return Stream.of(
                Arguments.of(SysMLv2StereotypeProvider.EMPTY_SYSML_ID, "modelNameWithoutSysMLExtension"),
                Arguments.of(SysMLv2StereotypeProvider.EMPTY_SYSML_ID, "modelName.sysml"),
                Arguments.of(SysMLv2StereotypeProvider.EMPTY_SYSML_ID, "modelName.SYSML"),
                Arguments.of(SysMLv2StereotypeProvider.EMPTY_SYSML_LIBRARY_ID, "libraryNameWithoutSysMLExtension"),
                Arguments.of(SysMLv2StereotypeProvider.EMPTY_SYSML_LIBRARY_ID, "libraryName.sysml"),
                Arguments.of(SysMLv2StereotypeProvider.EMPTY_SYSML_LIBRARY_ID, "libraryName.SYSML"));
    }

    @DisplayName("GIVEN a sysml project, WHEN creating a SysML model, THEN it is suffixed by .sysml automatically")
    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("createDocumentParameters")
    public void createDocument(String stereotypeId, String documentName) {
        var input = new CreateDocumentInput(
                UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                SysMLv2StereotypeProvider.EMPTY_SYSML_ID,
                documentName);
        var result = this.createDocumentMutationRunner.run(input);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        String typename = JsonPath.read(result.data(), "$.data.createDocument.__typename");
        assertThat(typename).isEqualTo(CreateDocumentSuccessPayload.class.getSimpleName());

        String createdDocumentName = JsonPath.read(result.data(), "$.data.createDocument.document.name");
        assertThat(createdDocumentName).startsWith(documentName);
        assertThat(createdDocumentName.toLowerCase()).endsWith(".sysml");
    }
}
