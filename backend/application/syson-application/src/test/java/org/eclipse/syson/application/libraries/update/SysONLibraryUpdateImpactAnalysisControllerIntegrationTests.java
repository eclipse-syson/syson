/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.application.libraries.update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.eclipse.sirius.components.datatree.DataTree;
import org.eclipse.sirius.components.datatree.DataTreeNode;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.tests.graphql.EditingContextUpdateLibraryImpactAnalysisReportQueryRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.ProjectWithLibraryDependencyTestProjectData;
import org.eclipse.syson.application.libraries.SysONLibraryImportTestServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests of the library controllers when computing the impact analysis of a library update.
 *
 * @author gdaniel
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SysONLibraryUpdateImpactAnalysisControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private ILibrarySearchService librarySearchService;

    @Autowired
    private EditingContextUpdateLibraryImpactAnalysisReportQueryRunner editingContextUpdateLibraryImpactAnalysisReportQueryRunner;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @SysONLibraryImportTestServer
    @DisplayName("GIVEN a project with dependencies, WHEN an impact analysis report is requested for the update of a dependency, THEN the report is returned")
    public void givenProjectWithDependenciesWhenImpactAnalysisReportIsRequestedForTheUpdateOfADependencyThenTheReportIsReturned() {
        Optional<Library> optionalLibrary = this.librarySearchService.findByNamespaceAndNameAndVersion(ProjectWithLibraryDependencyTestProjectData.LIBRARY_PROJECT_ID, "MyLibrary", "v2");
        assertThat(optionalLibrary).isPresent();

        Map<String, Object> input = Map.of("editingContextId", ProjectWithLibraryDependencyTestProjectData.EDITING_CONTEXT, "libraryId", optionalLibrary.get().getId());
        var result = this.editingContextUpdateLibraryImpactAnalysisReportQueryRunner.run(input);
        int nbElementDeleted = JsonPath.read(result.data(), "$.data.viewer.editingContext.updateLibraryImpactAnalysisReport.nbElementDeleted");
        assertThat(nbElementDeleted).isEqualTo(0);
        int nbElementModified = JsonPath.read(result.data(), "$.data.viewer.editingContext.updateLibraryImpactAnalysisReport.nbElementModified");
        assertThat(nbElementModified).isEqualTo(3);
        int nbElementCreated = JsonPath.read(result.data(), "$.data.viewer.editingContext.updateLibraryImpactAnalysisReport.nbElementCreated");
        assertThat(nbElementCreated).isEqualTo(0);

        Configuration configuration = Configuration.defaultConfiguration().mappingProvider(new JacksonMappingProvider(this.objectMapper));
        DataTree dataTree = JsonPath.parse(result, configuration).read("$.data.viewer.editingContext.updateLibraryImpactAnalysisReport.impactTree", DataTree.class);

        assertThat(dataTree.id()).isEqualTo("impact_tree");
        this.assertHasNode(dataTree, "LibraryResource1 (MyLibrary@v1)", null, ChangeKind.DELETION);
        this.assertHasNode(dataTree, "LibraryResource2 (MyLibrary@v1)", null, ChangeKind.DELETION);
        this.assertHasNode(dataTree, "LibraryResource3 (MyLibrary@v1)", null, ChangeKind.DELETION);
        this.assertHasNode(dataTree, "LibraryResource2 (MyLibrary@v2)", null, ChangeKind.ADDITION);
        this.assertHasNode(dataTree, "LibraryResource3 (MyLibrary@v2)", null, ChangeKind.ADDITION);
        this.assertHasNode(dataTree, "ProjectUsingMyLibraryV1", null, ChangeKind.NOTHING);
        this.assertHasNode(dataTree, "Package0", "ProjectUsingMyLibraryV1", ChangeKind.NOTHING);
        this.assertHasNode(dataTree, "Namespace Import", "Package0", ChangeKind.MODIFICATION);
        this.assertHasNode(dataTree, "importedNamespace: Package1", "Namespace Import", ChangeKind.DELETION);
        this.assertHasNode(dataTree, "attribute1", "Package0", ChangeKind.NOTHING);
        this.assertHasNode(dataTree, "FeatureTyping", "attribute1", ChangeKind.MODIFICATION);
        // The attribute has been moved to another resource: the proxy cannot be resolved and has been removed.
        this.assertHasNode(dataTree, "type: AttributeDefinition1", "FeatureTyping", ChangeKind.DELETION);
        this.assertHasNode(dataTree, "attribute3", "Package0", ChangeKind.NOTHING);
        this.assertHasNode(dataTree, "FeatureTyping", "attribute3", ChangeKind.MODIFICATION);
        this.assertHasNode(dataTree, "type: AttributeDefinition3", "FeatureTyping", ChangeKind.DELETION);
    }

    private void assertHasNode(DataTree dataTree, String nodeLabel, String parentNodeLabel, ChangeKind changeKind) {
        assertThat(dataTree.nodes()).anySatisfy(node -> {
            assertThat(node.label().toString()).isEqualTo(nodeLabel);
            if (parentNodeLabel != null) {
                Optional<DataTreeNode> parentNode = dataTree.nodes().stream().filter(dataTreeNode -> Objects.equals(dataTreeNode.id(), node.parentId()))
                        .findFirst();
                assertThat(parentNode).isPresent()
                        .get()
                        .returns(parentNodeLabel, dataTreeNode -> dataTreeNode.label().toString());
            } else {
                assertThat(node.parentId()).isNull();
            }
            List<List<String>> endIconsURLs = node.endIconsURLs();
            switch (changeKind) {
                case ADDITION -> assertThat(endIconsURLs)
                        .hasSize(1)
                        .first(InstanceOfAssertFactories.list(String.class))
                        .anyMatch(icon -> icon.contains("FeatureAddition.svg"));
                case DELETION -> assertThat(endIconsURLs)
                        .hasSize(1)
                        .first(InstanceOfAssertFactories.list(String.class))
                        .anyMatch(icon -> icon.contains("FeatureDeletion.svg"));
                case MODIFICATION -> assertThat(endIconsURLs)
                        .hasSize(1)
                        .first(InstanceOfAssertFactories.list(String.class))
                        .anyMatch(icon -> icon.contains("ChangeMarker.svg"));
                case NOTHING -> assertThat(endIconsURLs).isEmpty();
                default -> fail();
            }
        });
    }

    /**
     * The changes that can be represented in the impact analysis tree.
     *
     * @author gdaniel
     */
    private enum ChangeKind {
        ADDITION,
        DELETION,
        MODIFICATION,
        NOTHING
    }
}
