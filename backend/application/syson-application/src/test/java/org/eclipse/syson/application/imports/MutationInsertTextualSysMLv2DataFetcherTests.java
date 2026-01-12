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
package org.eclipse.syson.application.imports;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.NewObjectAsTextProjectData;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FlowUsage;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.datafetchers.MutationInsertTextualSysMLv2DataFetcher;
import org.eclipse.syson.sysml.dto.InsertTextualSysMLv2Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Integration test for {@link MutationInsertTextualSysMLv2DataFetcher}.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MutationInsertTextualSysMLv2DataFetcherTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private ExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private MutationInsertTextualSysMLv2DataRunner queryRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Sql(scripts = { NewObjectAsTextProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testCreationFromText() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        this.insertTextExpectNoMessage(NewObjectAsTextProjectData.SemanticIds.ROOT_ID, """
                package importedPackage;
                """);

        this.checkElement(Package.class,
                NewObjectAsTextProjectData.SemanticIds.ROOT_ID,
                p -> p.getOwnedElement().stream().anyMatch(e -> e instanceof org.eclipse.syson.sysml.Package pack && "importedPackage".equals(pack.getName())));
    }

    @Sql(scripts = { NewObjectAsTextProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @DisplayName("GIVEN a package importing ScalarValues namespace, WHEN importing an attribute with type Real, THEN the Real type should be correctly resolved")
    @Test
    public void testCreateRealAttribute() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        this.insertTextExpectNoMessage(NewObjectAsTextProjectData.SemanticIds.PART1_ID, " attribute x : Real");

        this.checkElement(PartUsage.class,
                NewObjectAsTextProjectData.SemanticIds.PART1_ID,
                p -> {
                    if (p.getOwnedFeature().size() == 2) {
                        Feature f1 = p.getOwnedFeature().get(1);
                        if (f1 instanceof AttributeUsage attr1) {
                            EList<Type> types = attr1.getType();
                            if (types.size() == 1) {
                                return "ScalarValues::Real".equals(types.get(0).getQualifiedName()) && "x".equals(f1.getDeclaredName());
                            }
                        }
                    }
                    return false;
                });

    }

    @Sql(scripts = { NewObjectAsTextProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @DisplayName("GIVEN a package, WHEN importing a NamespaceImport, THEN NamespaceImport should be contained in the ownedRelationships of the Package")
    @Test
    public void testCreateNamespaceImport() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        this.insertTextExpectNoMessage(NewObjectAsTextProjectData.SemanticIds.ROOT_ID, "import ScalarValues::*;");

        this.checkElement(Package.class,
                NewObjectAsTextProjectData.SemanticIds.ROOT_ID,
                p -> {
                    if (p.getOwnedImport().size() == 1) {
                        Import imp = p.getOwnedImport().get(0);
                        if (imp instanceof NamespaceImport nImp) {
                            return "ScalarValues".equals(nImp.getImportedNamespace().getQualifiedName());
                        }
                    }
                    return false;
                });

    }

    @Sql(scripts = { NewObjectAsTextProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @DisplayName("GIVEN a model with two ActionUsages, WHEN creating a succession between those actions, THEN the successsion should be created")
    @Test
    public void testCreateSuccessionAsUsage() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        this.insertTextExpectNoMessage(NewObjectAsTextProjectData.SemanticIds.ACTION_DEFINITION_1_ID, "succession s1 first action1 then action2;");

        this.checkElement(ActionDefinition.class,
                NewObjectAsTextProjectData.SemanticIds.ACTION_DEFINITION_1_ID,
                p -> {
                    if (p.getOwnedFeature().size() == 4) {
                        Feature f1 = p.getOwnedFeature().get(3);
                        if (f1 instanceof SuccessionAsUsage succession) {
                            return "Root::Definitions::ActionDefinition1::action1".equals(succession.getSourceFeature().getQualifiedName())
                                    && "Root::Definitions::ActionDefinition1::action2".equals(succession.getTargetFeature().get(0).getQualifiedName());
                        }
                    }
                    return false;
                });

    }

    @Sql(scripts = { NewObjectAsTextProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @DisplayName("GIVEN model with two ItemUsages, WHEN creating a FlowUsage between those items, THEN the flow should be created")
    @Test
    public void testCreateFlow() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        this.insertTextExpectNoMessage(NewObjectAsTextProjectData.SemanticIds.ACTION_DEFINITION_1_ID, "flow action1.item1Out to action2.item1In;");

        this.checkElement(ActionDefinition.class,
                NewObjectAsTextProjectData.SemanticIds.ACTION_DEFINITION_1_ID,
                p -> {
                    if (p.getOwnedFeature().size() == 4) {
                        Feature f1 = p.getOwnedFeature().get(3);
                        if (f1 instanceof FlowUsage flow) {
                            return "Root::Definitions::ActionDefinition1::action1".equals(flow.getSourceFeature().getQualifiedName())
                                    && "Root::Definitions::ActionDefinition1::action2".equals(flow.getTargetFeature().get(0).getQualifiedName())
                                    && "item1Out".equals(flow.getSourceOutputFeature().getName())
                                    && "item1In".equals(flow.getTargetInputFeature().getName());
                        }
                    }
                    return false;
                });

    }

    @Sql(scripts = { NewObjectAsTextProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @DisplayName("GIVEN a package that do not import ScalarValues namespace, WHEN importing an attribute with type Real, THEN attribute should be added but a message should be displayed")
    @Test
    public void testCreateRealAttributeWithResolutionProblem() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        this.insertTextExpectMessages(NewObjectAsTextProjectData.SemanticIds.PART_DEFINITION_1_ID, " attribute x : Real",
                List.of(new Message("Unable to resolve name 'Real' for reference 'type' on element '[FeatureTyping] Root::Definitions::PartDefinition1::x::<FeatureTyping>'",
                        MessageLevel.WARNING)));

        this.checkElement(PartDefinition.class,
                NewObjectAsTextProjectData.SemanticIds.PART_DEFINITION_1_ID,
                p -> {
                    if (p.getOwnedFeature().size() == 1) {
                        Feature f1 = p.getOwnedFeature().get(0);
                        if (f1 instanceof AttributeUsage xAttr) {
                            EList<Type> types = xAttr.getType();
                            // The FeatureTyping is created but the type is unresolved
                            return types.size() == 1 && types.get(0) == null && "x".equals(f1.getDeclaredName());
                        }
                    }
                    return false;
                });

    }


    private void insertTextExpectNoMessage(String parentElementId, String content) {
        var input = new InsertTextualSysMLv2Input(UUID.randomUUID(), NewObjectAsTextProjectData.EDITING_CONTEXT_ID, parentElementId, content);
        var result = this.queryRunner.run(input);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Map<String, Object> parsed = JsonPath.read(result.data(), "$.data.insertTextualSysMLv2");

        assertThat(parsed.get("__typename")).isEqualTo(SuccessPayload.class.getSimpleName());
        assertThat((List<?>) parsed.get("messages")).isEmpty();
    }

    private void insertTextExpectMessages(String parentElementId, String content, List<Message> expectedMessages) {
        var input = new InsertTextualSysMLv2Input(UUID.randomUUID(), NewObjectAsTextProjectData.EDITING_CONTEXT_ID, parentElementId, content);
        var result = this.queryRunner.run(input);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Map<String, Object> parsed = JsonPath.read(result.data(), "$.data.insertTextualSysMLv2");

        assertThat(parsed.get("__typename")).isEqualTo(SuccessPayload.class.getSimpleName());

        List<Message> actualMessages = ((List<Map<String, String>>) parsed.get("messages")).stream()
                .map(s -> new Message(s.get("body"), MessageLevel.valueOf(s.get("level").toUpperCase())))
                .toList();

        assertThat(actualMessages).isEqualTo(expectedMessages);
    }

    private <T extends Element> void checkElement(Class<T> elementType, String elementToCheck, Predicate<T> checker) {
        BiFunction<IEditingContext, IInput, IPayload> function = (editingContext, executeEditingContextFunctionInput) -> {
            return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(),
                    this.objectSearchService.getObject(editingContext, elementToCheck).get());
        };
        var mono = this.executeEditingContextFunctionRunner.execute(new ExecuteEditingContextFunctionInput(UUID.randomUUID(), NewObjectAsTextProjectData.EDITING_CONTEXT_ID, function));

        Predicate<IPayload> predicate = payload -> Optional.of(payload)
                .filter(ExecuteEditingContextFunctionSuccessPayload.class::isInstance)
                .map(ExecuteEditingContextFunctionSuccessPayload.class::cast)
                .map(ExecuteEditingContextFunctionSuccessPayload::result)
                .filter(elementType::isInstance)
                .map(elementType::cast)
                .map(checker::test)
                .orElse(false);

        StepVerifier.create(mono)
                .expectNextMatches(predicate)
                .thenCancel()
                .verify();
    }
}
