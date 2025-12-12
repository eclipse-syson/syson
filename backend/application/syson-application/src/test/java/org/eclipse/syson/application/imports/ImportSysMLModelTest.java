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
package org.eclipse.syson.application.imports;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.sirius.web.application.project.dto.CreateProjectInput;
import org.eclipse.sirius.web.application.project.dto.CreateProjectSuccessPayload;
import org.eclipse.sirius.web.application.project.dto.DeleteProjectInput;
import org.eclipse.sirius.web.application.project.services.BlankProjectTemplateProvider;
import org.eclipse.sirius.web.application.project.services.ProjectApplicationService;
import org.eclipse.sirius.web.application.project.services.api.IProjectApplicationService;
import org.eclipse.sirius.web.application.project.services.api.IProjectEditingContextService;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.DecisionNode;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.FlowUsage;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MetadataAccessExpression;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.TransitionFeatureKind;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.upload.SysMLExternalResourceLoaderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test that checks the semantics of imported models.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImportSysMLModelTest extends AbstractIntegrationTests {

    @Autowired
    private SysMLExternalResourceLoaderService sysmlResourceLoader;

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private IProjectApplicationService projectDeletionService;

    @Autowired
    private ProjectApplicationService projectCreationService;

    @Autowired
    private IProjectEditingContextService projectToEditingContext;

    private SysMLv2SemanticImportChecker checker;

    private String projectId;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        UUID randomUUID = UUID.randomUUID();
        IPayload project = this.projectCreationService
                .createProject(new CreateProjectInput(randomUUID, "ImportExport-" + randomUUID.toString(), BlankProjectTemplateProvider.BLANK_PROJECT_TEMPLATE_ID, List.of()));
        assertThat(project instanceof CreateProjectSuccessPayload);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        this.projectId = ((CreateProjectSuccessPayload) project).project().id().toString();

        Optional<String> editingContextId = this.projectToEditingContext.getEditingContextId(this.projectId);
        Optional<IEditingContext> optEditingContext = this.editingContextSearchService.findById(editingContextId.get());

        this.checker = new SysMLv2SemanticImportChecker(this.sysmlResourceLoader, (EditingContext) optEditingContext.get());
    }

    @AfterEach
    public void tearDown() {
        IPayload payload = this.projectDeletionService.deleteProject(new DeleteProjectInput(UUID.randomUUID(), this.projectId));
        assertThat(payload).isInstanceOf(SuccessPayload.class);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }

    @Test
    @DisplayName("Given of model with redefinition depending on inherited memberships computation, WHEN importing the model, THEN redefined feature should resolve properly using inherited memberships")
    public void checkRedefinedFeatureToInheritedFields() throws IOException {
        var input = """
                private import ISQBase::mass;
                #MP part p2 {
                    attribute :>> mass = 2.0;
                }
                private import Metaobjects::SemanticMetadata;
                part p {
                    attribute mass;
                }
                metadata def MP :> SemanticMetadata {
                    :>> baseType = p meta SysML::Systems::PartUsage;
                    :> annotatedElement : SysML::Systems::PartUsage;
                }
                part p3 :> p1::p2::p0 {
                    attribute :>> mass = 2.0;
                }
                package p1 {
                    package p2 {
                        part p0 :> p;
                    }
                }
                part p {
                    attribute mass;
                }
                """;

        this.checker.checkImportedModel(resource -> {
            Optional<AttributeUsage> pMass = EMFUtils.allContainedObjectOfType(resource, AttributeUsage.class)
                    .filter(partUsage -> "p::mass".equals(partUsage.getQualifiedName()))
                    .findFirst();

            assertThat(pMass)
                    .isPresent();

            // Checks that p2::mass redefined p::mass and not ISQBase::mass because of the use of the semantic MetadataDefinition that implicitly makes p2 subset p
            Optional<AttributeUsage> p2Mass = EMFUtils.allContainedObjectOfType(resource, AttributeUsage.class)
                    .filter(partUsage -> "p2::mass".equals(partUsage.getQualifiedName()))
                    .findFirst();
            assertThat(p2Mass)
                    .isPresent();

            assertThat(p2Mass.get()).matches(attributeUsage -> attributeUsage.redefines(pMass.get()));

            // Checks that p2::mass redefined p::mass and not ISQBase::mass because p3 subsets p1::p2::p0 and p0 subsets p
            Optional<AttributeUsage> p3Mass = EMFUtils.allContainedObjectOfType(resource, AttributeUsage.class)
                    .filter(partUsage -> "p3::mass".equals(partUsage.getQualifiedName()))
                    .findFirst();
            assertThat(p2Mass)
                    .isPresent();

            assertThat(p2Mass.get()).matches(attributeUsage -> attributeUsage.redefines(pMass.get()));
        });
    }

    @Test
    @DisplayName("GIVEN a model with rational numbers, WHEN importing the model, THEN the value should use LiteralRational")
    public void checkLiteralRationalImport() throws IOException {
        var input = """
                private import ScalarValues::*;
                part p1 {
                    attribute a : Real = 1.0;
                    attribute b = 3.14;
                    attribute c = 2.5E-10;
                    attribute d = .5;
                    attribute e = 1E+3;
                }""";

        this.checker.checkImportedModel(resource -> {
            List<FeatureValue> values = EMFUtils.allContainedObjectOfType(resource, FeatureValue.class)
                    .toList();
            assertThat(values).hasSize(5);
            assertThat(values.get(0).getValue())
                    .isInstanceOf(LiteralRational.class)
                    .matches(v -> ((LiteralRational) v).getValue() == 1.0);
            assertThat(values.get(1).getValue())
                    .isInstanceOf(LiteralRational.class)
                    .matches(v -> ((LiteralRational) v).getValue() == 3.14);
            assertThat(values.get(2).getValue())
                    .isInstanceOf(LiteralRational.class)
                    .matches(v -> ((LiteralRational) v).getValue() == 2.5E-10);
            assertThat(values.get(3).getValue())
                    .isInstanceOf(LiteralRational.class)
                    .matches(v -> ((LiteralRational) v).getValue() == 0.5);
            assertThat(values.get(4).getValue())
                    .isInstanceOf(LiteralRational.class)
                    .matches(v -> ((LiteralRational) v).getValue() == 1E+3);
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with a FlowUsage using a FeatureChain, WHEN importing the model, THEN the source and the target of the FlowUsage should be properly resolved")
    public void checkFlowUsageWithFeatureChain() throws IOException {
        var input = """
                part def P1Def {
                    port po1 : PortDef1;
                }
                port def PortDef1 {
                    out item item1: P2Def;
                }
                part def P2Def;
                part def P3Def {
                    in item item2 : P3Def;
                }
                part p1 {
                    part p2 : P1Def
                    part p3 : P3Def;
                    flow from p2.po1.item1 to p3.item2;
                }
                """;

        this.checker.checkImportedModel(resource -> {
            List<FlowUsage> flows = EMFUtils.allContainedObjectOfType(resource, FlowUsage.class)
                    .toList();
            assertThat(flows).hasSize(1);

            FlowUsage flow = flows.get(0);
            assertThat(flow.getSourceOutputFeature().getFeatureTarget().getName()).isEqualTo("item1");
            assertThat(flow.getTargetInputFeature().getFeatureTarget().getName()).isEqualTo("item2");

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with an EndFeatureMembership, WHEN importing the model, THEN the ReferenceUsage of the feature end should have isFeature = true")
    public void checkMemberFeatureOfEndFeatureMembership() throws IOException {
        var input = """
                package pa1 {
                    part pa1;
                    part pa2;

                    connect pa1 to pa2;
                }
                """;

        this.checker.checkImportedModel(resource -> {
            List<EndFeatureMembership> endFeatureMemberships = EMFUtils.allContainedObjectOfType(resource, EndFeatureMembership.class)
                    .toList();
            assertThat(endFeatureMemberships)
                    .hasSize(2)
                    .allMatch(endFeatureMembership -> endFeatureMembership.getOwnedMemberFeature() != null && endFeatureMembership.getOwnedMemberFeature().isIsEnd());
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN some basic MetadataUsage (not inheriting SemanticMetadata), WHEN importing from textual format, THEN the references 'annotatedElement', 'metadataDefinition' and owned features should be properly handled.")
    public void checkBasicMetadaUsage() throws IOException {
        var input = """
                package p1 {
                    metadata def MD1 {
                        attribute x : ScalarValues::String;
                    }
                    metadata def MD2 {
                        attribute y : ScalarValues::String;
                        :> annotatedElement : SysML::PartUsage;
                    }

                    #MD1 part p1;
                    part p2 {
                        @MD1 {
                            x = "1";
                    }
                    }
                    part p3;
                    metadata MD1 about p3;
                    part p4;
                    metadata m1 : MD1 about p4;

                    #MD2 part p5;
                }""";
        this.checker.checkImportedModel(resource -> {
            List<MetadataDefinition> definitions = EMFUtils.allContainedObjectOfType(resource, MetadataDefinition.class)
                    .toList();
            assertThat(definitions).hasSize(2);

            MetadataDefinition md1Def = definitions.get(0);

            assertThat(md1Def.getName()).isEqualTo("MD1");

            assertThat(md1Def.getOwnedFeature()).hasSize(1).first()
                    .matches(feature -> {
                        return feature.getName().equals("x");
                    });
            assertThat(md1Def.getOwnedFeature()).hasSize(1);
            Feature xFeature = md1Def.getOwnedFeature().get(0);

            MetadataDefinition md2Def = definitions.get(1);

            assertThat(md2Def.getName()).isEqualTo("MD2");

            assertThat(md2Def.getOwnedFeature()).hasSize(2);

            assertThat(md2Def.getOwnedFeature())
                    .anyMatch(feature -> {
                        return feature.getName().equals("y");
                    }, "Has a feature named y");
            assertThat(md2Def.getOwnedFeature())
                    .anyMatch(feature -> {
                        return this.isSubSettingFromStandardLib(feature, "Metaobjects::Metaobject::annotatedElement");
                    }, "Has redefined the annotatedElement feature");

            List<MetadataUsage> usages = EMFUtils.allContainedObjectOfType(resource, MetadataUsage.class)
                    .toList();

            assertThat(usages).hasSize(5);

            this.checkMetadaUsage(usages.get(0), "MD1", "p1", Map.of());
            this.checkMetadaUsage(usages.get(1), "MD1", "p2", Map.of(xFeature, "1"));
            this.checkMetadaUsage(usages.get(2), "MD1", "p3", Map.of());
            this.checkMetadaUsage(usages.get(3), "MD1", "p4", Map.of());
            this.checkMetadaUsage(usages.get(4), "MD2", "p5", Map.of());

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with a semantic MetadataDefinition, WHEN importing the model, THEN MetadataDefinition should be properly created")
    public void checkSemanticMetadataDefinition() throws IOException {
        var input = """
                private import Metaobjects::SemanticMetadata;

                part def Functions {
                    attribute x : ScalarValues::String;
                }

                part functions : Functions [*] nonunique;

                metadata def Function :> SemanticMetadata {
                    :>> baseType = functions meta SysML::ActionUsage;
                }

                #Function action a0;
                action a1;
                metadata Function about a1;
                #Function action def A0;
                """;
        this.checker.checkImportedModel(resource -> {
            List<MetadataDefinition> metadatas = EMFUtils.allContainedObjectOfType(resource, MetadataDefinition.class)
                    .toList();
            assertThat(metadatas).hasSize(1);

            MetadataDefinition metadata = metadatas.get(0);

            assertThat(metadata.specializesFromLibrary("Metaobjects::SemanticMetadata")).isTrue();
            assertThat(metadata.getOwnedFeature()).hasSize(1);

            Feature baseTypeFeature = metadata.getOwnedFeature().get(0);
            assertThat(baseTypeFeature.getName()).isEqualTo("baseType");
            assertThat(baseTypeFeature.specializesFromLibrary("Metaobjects::SemanticMetadata::baseType")).isTrue();

            FeatureValue baseTypeValuation = new UtilService().getValuation(baseTypeFeature);
            Expression baseTypeValuevalue = baseTypeValuation.getValue();
            assertThat(baseTypeValuevalue).isInstanceOf(OperatorExpression.class)
                    .matches(exp -> "meta".equals(((OperatorExpression) exp).getOperator()));

            OperatorExpression opExpression = (OperatorExpression) baseTypeValuevalue;

            assertThat(opExpression.getParameter()).hasSize(2);

            Feature seqParameter = opExpression.getParameter().get(0);
            FeatureValue seqValuation = new UtilService().getValuation(seqParameter);
            Expression seqValue = seqValuation.getValue();

            assertThat(seqValue).isInstanceOf(MetadataAccessExpression.class);

            MetadataAccessExpression metadataAccessExpression = (MetadataAccessExpression) seqValue;

            assertThat(metadataAccessExpression.getReferencedElement()).isNotNull()
                    .extracting(Element::getName).isEqualTo("functions");

            Feature result = opExpression.getResult();

            assertThat(result.getType()).hasSize(1).allMatch(t -> "SysML::Systems::ActionUsage".equals(t.getQualifiedName()));

            PartDefinition functionsDefinition = EMFUtils.allContainedObjectOfType(resource, PartDefinition.class)
                    .filter(def -> "Functions".equals(def.getName()))
                    .findFirst()
                    .get();

            PartUsage functionsUsage = EMFUtils.allContainedObjectOfType(resource, PartUsage.class)
                    .filter(def -> "functions".equals(def.getName()))
                    .findFirst()
                    .get();

            List<ActionUsage> actionUsages = EMFUtils.allContainedObjectOfType(resource, ActionUsage.class)
                    .toList();

            ActionUsage a0 = actionUsages.get(0);
            assertThat(a0.getName()).isEqualTo("a0");
            assertThat(a0.specializes(functionsUsage)).isTrue();

            EList<Element> a0Element = a0.getOwnedElement();
            assertThat(a0Element).hasSize(1).allMatch(MetadataUsage.class::isInstance);
            MetadataUsage a0MetadataUsage = (MetadataUsage) a0Element.get(0);
            this.checkMetadaUsage(a0MetadataUsage, "Function", "a0", Map.of());

            List<ActionDefinition> actionDefinitions = EMFUtils.allContainedObjectOfType(resource, ActionDefinition.class)
                    .toList();
            ActionDefinition a0Def = actionDefinitions.get(0);
            assertThat(a0Def.getName()).isEqualTo("A0");
            assertThat(a0Def.specializes(functionsDefinition)).isTrue();

            EList<Element> a0DefElement = a0Def.getOwnedElement();
            assertThat(a0DefElement).hasSize(1).allMatch(MetadataUsage.class::isInstance);
            MetadataUsage a0DefMetadataUsage = (MetadataUsage) a0DefElement.get(0);
            this.checkMetadaUsage(a0DefMetadataUsage, "Function", "A0", Map.of());

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model using a BindingConnectorAsUsage with feature chain, WHEN importing the model, THEN model is correctly imported")
    public void checkBindingConnectorWithFeatureChaine() throws IOException {
        var input = """
                package pk1 {
                    action a1 {
                        in item i1;

                        action a11 {
                            in item i11;
                        }
                        action a21 {
                            in item i21;
                        }
                        bind i1 = a21.i21;
                        binding b2 bind a11.i11 = a21.i21;
                    }
                }""";
        this.checker.checkImportedModel(resource -> {
            List<BindingConnectorAsUsage> bindings = EMFUtils.allContainedObjectOfType(resource, BindingConnectorAsUsage.class)
                    .toList();
            assertThat(bindings).hasSize(2);

            BindingConnectorAsUsage b1 = bindings.get(0);
            assertThat(b1.getSourceFeature().getFeatureTarget().getName()).isEqualTo("i1");
            assertThat(b1.getTargetFeature().get(0).getFeatureTarget().getName()).isEqualTo("i21");

            BindingConnectorAsUsage b2 = bindings.get(1);
            assertThat(b2.getName()).isEqualTo("b2");
            assertThat(b2.getSourceFeature().getFeatureTarget().getName()).isEqualTo("i11");
            assertThat(b2.getTargetFeature().get(0).getFeatureTarget().getName()).isEqualTo("i21");

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with a TextualRepresentation with a multiline body, WHEN importing the model, THEN the boly is imported without /* and */")
    public void checkTextualRepresentationFeatures() throws IOException {
        var input = """
                action def P1 {
                rep l1 language "naturalLanguage"
                    /* some comment
                    some other comment */
                rep l2 language "naturalLanguage2"
                    /* some comment 3 */
                }""";
        this.checker.checkImportedModel(resource -> {
            List<TextualRepresentation> textualRepresentations = EMFUtils.allContainedObjectOfType(resource, TextualRepresentation.class)
                    .toList();
            assertThat(textualRepresentations).hasSize(2);

            TextualRepresentation t1 = textualRepresentations.get(0);
            assertThat(t1.getLanguage()).isEqualTo("naturalLanguage");
            // Note here the extra 4 spaces due to indentation
            // The current implementation do not correctly handle indentation
            assertThat(t1.getBody()).isEqualTo("""
                    some comment
                        some other comment""");
            assertThat(t1.getDeclaredName()).isEqualTo("l1");
            TextualRepresentation t2 = textualRepresentations.get(1);
            assertThat(t2.getLanguage()).isEqualTo("naturalLanguage2");
            assertThat(t2.getBody()).isEqualTo("some comment 3");
            assertThat(t2.getDeclaredName()).isEqualTo("l2");

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with a FeatureChainExpression, WHEN importing the model, THEN target feature should be resolved")
    public void checkFeatureChainExpressionNameResolution() throws IOException {
        var input = """
                action def P1 {

                    action def A2 {
                        out pr2 : ScalarValues::Boolean;
                    }

                    action a2 : A2 {
                         out pr2;
                    }
                    if a2.pr2 then a3;
                    action a3 {}
                }""";
        this.checker.checkImportedModel(resource -> {
            FeatureChainExpression featureChaingExpression = EMFUtils.allContainedObjectOfType(resource, FeatureChainExpression.class)
                    .findFirst().get();
            assertThat(featureChaingExpression.getTargetFeature().getQualifiedName()).isEqualTo("P1::a2::pr2");

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with a SuccessionAsUsage targeting, WHEN importing the model, THEN target feature should be resolved")
    public void checkSuccessionWithImplicitSourceWithIntermediareFeatures() throws IOException {
        var input = """
                action def ActionDef1 {
                    action ax2;
                    action ax1;
                    then ax2;
                    then decide;
                }""";
        this.checker.checkImportedModel(resource -> {
            List<DecisionNode> decisionNodes = EMFUtils.allContainedObjectOfType(resource, DecisionNode.class)
                    .toList();
            assertThat(decisionNodes).hasSize(1);

            // Succession between a1 and second decision node
            List<SuccessionAsUsage> successionAsUsages = EMFUtils.allContainedObjectOfType(resource, SuccessionAsUsage.class)
                    .toList();

            SuccessionAsUsage first = successionAsUsages.get(0);
            assertThat(first.getSourceFeature().getName()).isEqualTo("ax1");
            assertThat(first.getTargetFeature()).hasSize(1).allMatch(t -> t.getName().equals("ax2"));
            SuccessionAsUsage second = successionAsUsages.get(1);
            assertThat(second.getSourceFeature().getName()).isEqualTo("ax1");
            assertThat(second.getTargetFeature()).hasSize(1).allMatch(t -> t == decisionNodes.get(0));

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with a FeatureChainExpression containing an implicitly redefined parameter, WHEN importing the model, THEN the FeatureChaining are resolved")
    public void checkFeatureChainExpressionWithImplicitParameterRedefinitionNameResolution() throws IOException {
        var input = """
                action def A1 {
                    part def P1 {
                        isValid : ScalarValues::Boolean;
                    }

                    action def A2 {
                        out prA2 : P1;
                    }

                    action a2 : A2 {
                        out pra2;
                    }
                    if a2.pra2.isValid then a3;
                    action a3 {}
                }""";
        this.checker.checkImportedModel(resource -> {
            FeatureChainExpression featureChainExpression = EMFUtils.allContainedObjectOfType(resource, FeatureChainExpression.class)
                    .findFirst()
                    .get();
            Feature targetFeature = featureChainExpression.getTargetFeature();
            assertThat(targetFeature).isNotNull();
            assertThat(targetFeature.getOwnedFeatureChaining()).hasSize(2);
            assertThat(targetFeature.getOwnedFeatureChaining().get(0).getChainingFeature().getQualifiedName()).isEqualTo("A1::a2::pra2");
            assertThat(targetFeature.getOwnedFeatureChaining().get(1).getChainingFeature().getQualifiedName()).isEqualTo("A1::P1::isValid");
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with duplicated names, WHEN importing the model, THEN the resolution of name should match the closest matching element")
    public void checkNameResolutionProcessWithDuplicatedName() throws IOException {
        var input = """
                package p1 {
                    part pa1 {
                        part pa11 :> pa1;
                        part pa1;
                    }
                }""";

        this.checker.checkImportedModel(resource -> {
            PartUsage pa11 = EMFUtils.allContainedObjectOfType(resource, PartUsage.class)
                    .filter(pa -> "pa11".equals(pa.getDeclaredName()))
                    .findFirst().get();

            assertThat(pa11.getOwnedSpecialization()).hasSize(1);
            Specialization specialization = pa11.getOwnedSpecialization().get(0);
            Type general = specialization.getGeneral();
            assertNotNull(general);
            assertThat(general.getQualifiedName()).isEqualTo("p1::pa1::pa1"); // And not "p1::pa1"
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with a Redefintion, WHEN importing the model, THEN the resolution of name should specialize Redefinition rules")
    public void checkNameResolutionProcessInRedifinition() throws IOException {
        var input = """
                package p1 {
                    part pa2 :> pa0 {
                        part pa11 :>> pa1;
                    }
                    part pa0 {
                        part pa1;
                    }
                }""";

        this.checker.checkImportedModel(resource -> {
            PartUsage pa11 = EMFUtils.allContainedObjectOfType(resource, PartUsage.class)
                    .filter(pa -> "pa11".equals(pa.getDeclaredName()))
                    .findFirst().get();

            assertThat(pa11.getOwnedRedefinition()).hasSize(1);
            Redefinition redefinition = pa11.getOwnedRedefinition().get(0);
            Feature redefinedFeature = redefinition.getRedefinedFeature();
            assertNotNull(redefinedFeature);
            assertThat(redefinedFeature.getQualifiedName()).isEqualTo("p1::pa0::pa1");
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with a reference to an invalid type, WHEN importing the model, THEN the resolution of name shoud not set a reference with an incompatible target")
    public void checkNameResolutionProcessWithInvalidTargetName() throws IOException {
        var input = """
                package p1 {
                    part pa1 {
                        part pa11 :> pa1; // <-- 'pa1' resolve to the Package "pa1" in the current namespace. This model is not valid.
                        package pa1;
                    }
                }""";

        this.checker.checkImportedModel(resource -> {
            PartUsage pa11 = EMFUtils.allContainedObjectOfType(resource, PartUsage.class)
                    .filter(pa -> "pa11".equals(pa.getDeclaredName()))
                    .findFirst().get();

            assertThat(pa11.getOwnedSpecialization()).hasSize(1);
            Specialization specialization = pa11.getOwnedSpecialization().get(0);
            Type general = specialization.getGeneral();
            assertNull(general);
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with a TransitionUsage, WHEN importing the model, THEN a TransitionUsage should be created.")
    public void checkSimpleTransitionUsage() throws IOException {
        var input = """
                action def A1 {
                    private import ScalarValues::Integer;
                    action a1 ;
                    action a2 ;
                    attribute x : Integer;
                    succession S first a1 if x == 1 then a2;
                }""";

        this.checker.checkImportedModel(resource -> {
            TransitionUsage transitionUsage = EMFUtils.allContainedObjectOfType(resource, TransitionUsage.class)
                    .filter(t -> "S".equals(t.getDeclaredName()))
                    .findFirst().get();

            ActionUsage source = transitionUsage.getSource();
            assertThat(source).isNotNull().matches(s -> "a1".equals(s.getName()));
            ActionUsage target = transitionUsage.getTarget();
            assertThat(target).isNotNull().matches(t -> "a2".equals(t.getName()));

            Succession succession = transitionUsage.getSuccession();
            assertThat(succession).isNotNull();

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with TransitionUsage with an implicit source, WHEN importing the model, THEN the implicit source should be correctly resolved.")
    public void checkTransitionWithImplicitSource() throws IOException {
        var input = """
                   action def A1 {
                    private import ScalarValues::Integer;
                    attribute x : Integer;
                    action a1;
                    action a2;
                    action a3;
                    decide d;
                        if x == 1 then a1;
                        if 2 > x then a2;
                        else a3;
                }""";

        this.checker.checkImportedModel(resource -> {
            List<TransitionUsage> transitionUsages = EMFUtils.allContainedObjectOfType(resource, TransitionUsage.class)
                    .toList();

            assertThat(transitionUsages).hasSize(3);

            TransitionUsage t1 = transitionUsages.get(0);
            ActionUsage source1 = t1.getSource();
            assertThat(source1).isNotNull().matches(s -> "d".equals(s.getName()));
            ActionUsage target1 = t1.getTarget();
            assertThat(target1).isNotNull().matches(t -> "a1".equals(t.getName()));

            this.assertOperatorExpressionGuard(t1, "==", FeatureReferenceExpression.class, LiteralInteger.class);

            TransitionUsage t2 = transitionUsages.get(1);
            ActionUsage source2 = t2.getSource();
            assertThat(source2).isNotNull().matches(s -> "d".equals(s.getName()));
            ActionUsage target2 = t2.getTarget();
            assertThat(target2).isNotNull().matches(t -> "a2".equals(t.getName()));

            this.assertOperatorExpressionGuard(t2, ">", LiteralInteger.class, FeatureReferenceExpression.class);

            TransitionUsage t3 = transitionUsages.get(2);
            ActionUsage source3 = t3.getSource();
            assertThat(source3).isNotNull().matches(s -> "d".equals(s.getName()));
            ActionUsage target3 = t3.getTarget();
            assertThat(target3).isNotNull().matches(t -> "a3".equals(t.getName()));

            EList<Expression> guardExpression3 = t3.getGuardExpression();
            assertThat(guardExpression3).isEmpty();

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a model with PortDefinitions, WHEN importing the model, THEN a conjugated port is created for each conjugated ports")
    public void checkConjugatedPortCreation() throws IOException {
        var input = """
                package Conjugated {
                    attribute def Temp;
                    port def TempPort {
                        attribute temperature : Temp;
                    }
                    part def TempPortClassic {
                        port tempPortClassic : TempPort;
                    }
                    part def TempPortConj {
                        port tempPortConj : ~TempPort;
                    }
                }""";

        this.checker.checkImportedModel(resource -> {
            PortDefinition tempPort = EMFUtils.allContainedObjectOfType(resource, PortDefinition.class)
                    .filter(pod -> "TempPort".equals(pod.getDeclaredName()))
                    .findFirst().get();

            ConjugatedPortDefinition conjugatedPortDefinition = tempPort.getConjugatedPortDefinition();
            assertThat(conjugatedPortDefinition).isNotNull();

            PortUsage tempPortConj = EMFUtils.allContainedObjectOfType(resource, PortUsage.class)
                    .filter(pou -> "tempPortConj".equals(pou.getDeclaredName()))
                    .findFirst().get();

            assertThat(tempPortConj.getType()).hasSize(1).allMatch(e -> e == conjugatedPortDefinition);

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a SuccessionAsUsage using the syntax that create a new target action, WHEN importing and exporting the model, THEN the source and target should be correctly computed.")
    public void checkSuccessionToDefinedActionSourceTest() throws IOException {
        var input = """
                action def ActionDef1 {
                    first start;
                    then action a1;
                    then action a2;
                }""";

        this.checker.checkImportedModel(resource -> {
            List<SuccessionAsUsage> successionAsUsages = EMFUtils.allContainedObjectOfType(resource, SuccessionAsUsage.class).toList();

            assertThat(successionAsUsages).hasSize(2);

            SuccessionAsUsage firstToA1 = successionAsUsages.get(0);

            assertThat(firstToA1.getSourceFeature().getName()).isEqualTo("start");
            assertThat(firstToA1.getTargetFeature()).hasSize(1).allMatch(f -> "a1".equals(f.getName()));

            SuccessionAsUsage firstToA2 = successionAsUsages.get(1);

            assertThat(firstToA2.getSourceFeature().getName()).isEqualTo("a1");
            assertThat(firstToA2.getTargetFeature()).hasSize(1).allMatch(f -> "a2".equals(f.getName()));
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a SuccessionAsUsage with an implicit source feature targeting the 'start' standard library element, "
            + "when importing the model, "
            + "then a special membership should be created that reference the 'start' element.")
    public void checkSuccessionAsUsageImplicitSourceToStartTest() throws IOException {
        var input = """
                action def ActionDef1 {
                    action a2;
                    first start;
                    then a2;
                }""";

        this.checker.checkImportedModel(resource -> {
            List<SuccessionAsUsage> successionAsUsages = EMFUtils.allContainedObjectOfType(resource, SuccessionAsUsage.class).toList();
            assertThat(successionAsUsages).hasSize(1);
            SuccessionAsUsage successionUsage = successionAsUsages.get(0);

            Feature sourceFeature = successionUsage.getSourceFeature();
            assertNotNull(sourceFeature);
            assertEquals("start", sourceFeature.getName());

            // Check that the special "membership" referencing start is present
            EList<Membership> memberships = successionUsage.getOwningType().getOwnedMembership();

            List<Membership> startMemberships = memberships.stream().filter(m -> new UtilService().isStandardStartAction(m)).toList();

            assertThat(startMemberships).hasSize(1);

            // Check that alias id for the ReferenceUsage of the source feature
            Feature sourceEnd = successionUsage.getConnectorEnd().get(0);
            assertThat(sourceEnd).isInstanceOf(ReferenceUsage.class);
            assertThat(sourceEnd.getAliasIds()).isEmpty();

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a SuccessionAsUsage with an explicit source feature targeting the 'start' standard library element, "
            + "when importing the model, "
            + "then no special membership should be created that reference the 'start'")
    public void checkSuccessionAsUsageWkithStartSourceTest() throws IOException {
        var input = """
                action def ActionDef1 {
                    action a2;
                    first start then a2;
                }""";

        this.checker.checkImportedModel(resource -> {
            List<SuccessionAsUsage> successionAsUsages = EMFUtils.allContainedObjectOfType(resource, SuccessionAsUsage.class).toList();
            assertThat(successionAsUsages).hasSize(1);
            SuccessionAsUsage successionUsage = successionAsUsages.get(0);

            Feature sourceFeature = successionUsage.getSourceFeature();
            assertNotNull(sourceFeature);
            assertEquals("start", sourceFeature.getName());

            // Check that the special "membership" referencing start is present
            EList<Membership> memberships = successionUsage.getOwningType().getOwnedMembership();

            List<Membership> startMemberships = memberships.stream().filter(m -> new UtilService().isStandardStartAction(m)).toList();
            assertThat(startMemberships).isEmpty();

        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a TransitionUsage with an AcceptActionUsage, WHEN importing the model, THEN the TransitionFeatureMembership's kind is trigger")
    public void checkTransitionUsageWithAcceptActionUsageTest() throws IOException {
        var input = """
                attribute def StartSignal;

                state myState {
                    state off;
                    accept StartSignal then on;
                    state on;
                }""";
        this.checker.checkImportedModel(resource -> {
            List<TransitionUsage> transitionUsages = EMFUtils.allContainedObjectOfType(resource, TransitionUsage.class).toList();
            assertThat(transitionUsages).hasSize(1);
            TransitionUsage transitionUsage = transitionUsages.get(0);
            assertThat(transitionUsage.getTriggerAction()).hasSize(1);
            Optional<TransitionFeatureMembership> optionalTransitionFeatureMembership = transitionUsage.getOwnedRelationship().stream()
                .filter(TransitionFeatureMembership.class::isInstance)
                .map(TransitionFeatureMembership.class::cast)
                .findFirst();
            assertThat(optionalTransitionFeatureMembership).isPresent();
            assertThat(optionalTransitionFeatureMembership.get().getKind()).isEqualTo(TransitionFeatureKind.TRIGGER);
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a TransitionUsage with an AcceptActionUsage containing a SendSignalActionUsage, WHEN importing the model, THEN the TransitionFeatureMembership holding the SendSignalAction has the effect kind")
    public void checkTransitionUsageWithAcceptActionUsageAndSendSignalActionTest() throws IOException {
        var input = """
                attribute def StartSignal;

                state myState {
                    state off;
                    accept StartSignal
                        do send StartSignal()
                        then on;
                    state on;
                }""";
        this.checker.checkImportedModel(resource -> {
            List<TransitionUsage> transitionUsages = EMFUtils.allContainedObjectOfType(resource, TransitionUsage.class).toList();
            assertThat(transitionUsages).hasSize(1);
            TransitionUsage transitionUsage = transitionUsages.get(0);
            // The AcceptActionUsage is the trigger of the transition
            assertThat(transitionUsage.getTriggerAction()).hasSize(1);
            // The SendSignalActionUsage is the effect of the transition
            assertThat(transitionUsage.getEffectAction()).hasSize(1);
            List<TransitionFeatureMembership> transitionFeatureMemberships = transitionUsage.getOwnedRelationship().stream()
                    .filter(TransitionFeatureMembership.class::isInstance)
                    .map(TransitionFeatureMembership.class::cast)
                    .toList();
            assertThat(transitionFeatureMemberships).hasSize(2);
            TransitionFeatureMembership doTransitionFeatureMembership = transitionFeatureMemberships.get(1);
            assertThat(doTransitionFeatureMembership.getKind()).isEqualTo(TransitionFeatureKind.EFFECT);
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a RequirementUsage with an assume ConstraintUsage, WHEN importing the model, THEN the RequirementConstraintMembership holding the ConstraintUsage has the assumption kind")
    public void checkRequirementUsageWithAssumeConstraintUsageTest() throws IOException {
        var input = """
                requirement myRequirement {
                    assume constraint { 1 >= 0 }
                }""";
        this.checker.checkImportedModel(resource -> {
            List<RequirementConstraintMembership> requirementConstraintMemberships = EMFUtils.allContainedObjectOfType(resource, RequirementConstraintMembership.class).toList();
            assertThat(requirementConstraintMemberships).hasSize(1);
            RequirementConstraintMembership requirementConstraintMembership = requirementConstraintMemberships.get(0);
            assertThat(requirementConstraintMembership.getKind()).isEqualTo(RequirementConstraintKind.ASSUMPTION);
            assertThat(requirementConstraintMembership.getOwnedConstraint()).isNotNull();
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a RequirementUsage with an require ConstraintUsage, WHEN importing the model, THEN the RequirementConstraintMembership holding the ConstraintUsage has the requirement kind")
    public void checkRequirementUsageWithRequireConstraintUsageTest() throws IOException {
        var input = """
                requirement myRequirement {
                    require constraint { 1 >= 0 }
                }""";
        this.checker.checkImportedModel(resource -> {
            List<RequirementConstraintMembership> requirementConstraintMemberships = EMFUtils.allContainedObjectOfType(resource, RequirementConstraintMembership.class).toList();
            assertThat(requirementConstraintMemberships).hasSize(1);
            RequirementConstraintMembership requirementConstraintMembership = requirementConstraintMemberships.get(0);
            assertThat(requirementConstraintMembership.getKind()).isEqualTo(RequirementConstraintKind.REQUIREMENT);
            assertThat(requirementConstraintMembership.getOwnedConstraint()).isNotNull();
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN an AttributeUsage with a LiteralString, WHEN importing the model, THEN the LiteralString value does not contain double quotes")
    public void checkAttributeUsageLiteralStringValueTest() throws IOException {
        var input = """
                attribute myAttribute = "value";
                """;
        this.checker.checkImportedModel(resource -> {
            List<LiteralString> literalStrings = EMFUtils.allContainedObjectOfType(resource, LiteralString.class).toList();
            assertThat(literalStrings).hasSize(1);
            LiteralString literalString = literalStrings.get(0);
            assertThat(literalString.getValue()).doesNotContain("\"");
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a PartUsage with a Multiplicity with LiteralInteger bounds, WHEN importing the model, THEN the Multiplicity value is set")
    public void checkPartUsageMultiplicityLiteralIntegerBoundsValueTest() throws IOException {
        var input = """
                part myPart[1..2];
                """;
        this.checker.checkImportedModel(resource -> {
            List<PartUsage> partUsages = EMFUtils.allContainedObjectOfType(resource, PartUsage.class).toList();
            assertThat(partUsages).hasSize(1);
            PartUsage partUsage = partUsages.get(0);
            assertThat(partUsage.getMultiplicity()).isInstanceOf(MultiplicityRange.class);
            MultiplicityRange multiplicityRange = (MultiplicityRange) partUsage.getMultiplicity();
            assertThat(multiplicityRange.getLowerBound()).isInstanceOf(LiteralInteger.class);
            LiteralInteger multiplicityRangerLowerBound = (LiteralInteger) multiplicityRange.getLowerBound();
            assertThat(multiplicityRangerLowerBound.getValue()).isEqualTo(1);
            assertThat(multiplicityRange.getUpperBound()).isInstanceOf(LiteralInteger.class);
            LiteralInteger multiplicityRangeUpperBound = (LiteralInteger) multiplicityRange.getUpperBound();
            assertThat(multiplicityRangeUpperBound.getValue()).isEqualTo(2);
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a PartUsage with a Multiplicity with FeatureReferenceExpression bounds, WHEN importing the model, THEN the Multiplicity value is set")
    public void checkPartUsageMultiplicityChainedFeatureReferenceExpressionBoundsTest() throws IOException {
        // The result of the bound expression(s) of a MultiplicityRange must be typed by ScalarValues::Integer (see
        // KerML 8.3.4.11.2).
        var input = """
                part bounds {
                    attribute lower:ScalarValues::Integer = 1;
                    part upperBounds {
                        attribute upper:ScalarValues::Integer = 2;
                    }
                }

                part myPart[bounds::lower..bounds::upperBounds::upper];
                """;
        this.checker.checkImportedModel(resource -> {
            Optional<PartUsage> optionalPartUsage = EMFUtils.allContainedObjectOfType(resource, PartUsage.class)
                    .filter(partUsage -> Objects.equals(partUsage.getName(), "myPart"))
                    .findFirst();
            assertThat(optionalPartUsage).isPresent();
            assertThat(optionalPartUsage.get().getMultiplicity()).isInstanceOf(MultiplicityRange.class);
            MultiplicityRange multiplicityRange = (MultiplicityRange) optionalPartUsage.get().getMultiplicity();
            assertThat(multiplicityRange.getLowerBound()).isInstanceOf(FeatureReferenceExpression.class);
            FeatureReferenceExpression multiplicityRangeLowerBound = (FeatureReferenceExpression) multiplicityRange.getLowerBound();
            assertThat(multiplicityRangeLowerBound.getReferent())
                    .isInstanceOf(AttributeUsage.class)
                    .returns("lower", Feature::getName);
            assertThat(multiplicityRange.getUpperBound()).isInstanceOf(FeatureReferenceExpression.class);
            FeatureReferenceExpression multiplicityRangeUpperBound = (FeatureReferenceExpression) multiplicityRange.getUpperBound();
            assertThat(multiplicityRangeUpperBound.getReferent())
                    .isInstanceOf(AttributeUsage.class)
                    .returns("upper", Feature::getName);
        }).check(input);
    }

    @Test
    @DisplayName("GIVEN a PartUsage with a Multiplicity with top-level FeatureReferenceExpression bounds, WHEN importing the model, THEN the Multiplicity value is set ")
    public void checkPartUsageMultiplicityTopLevelFeatureReferenceExpressionBoundTest() throws IOException {
        var input = """
                attribute lower:ScalarValues::Integer = 1;
                attribute upper:ScalarValues::Integer = 2;
                part myPart[lower..upper];
                """;
        this.checker.checkImportedModel(resource -> {
            List<PartUsage> partUsages = EMFUtils.allContainedObjectOfType(resource, PartUsage.class).toList();
            assertThat(partUsages).hasSize(1);
            PartUsage partUsage = partUsages.get(0);
            assertThat(partUsage.getMultiplicity()).isInstanceOf(MultiplicityRange.class);
            MultiplicityRange multiplicityRange = (MultiplicityRange) partUsage.getMultiplicity();
            assertThat(multiplicityRange.getLowerBound()).isInstanceOf(FeatureReferenceExpression.class);
            FeatureReferenceExpression multiplicityRangeLowerBound = (FeatureReferenceExpression) multiplicityRange.getLowerBound();
            assertThat(multiplicityRangeLowerBound.getReferent())
                    .isInstanceOf(AttributeUsage.class)
                    .returns("lower", Feature::getName);
            assertThat(multiplicityRange.getUpperBound()).isInstanceOf(FeatureReferenceExpression.class);
            FeatureReferenceExpression multiplicityRangeUpperBound = (FeatureReferenceExpression) multiplicityRange.getUpperBound();
            assertThat(multiplicityRangeUpperBound.getReferent())
                    .isInstanceOf(AttributeUsage.class)
                    .returns("upper", Feature::getName);
        }).check(input);
    }

    private void assertOperatorExpressionGuard(TransitionUsage t1, String expectedOperator, Class<?> expectedFirstParameterType, Class<?> expectedSecondParameterType) {
        EList<Expression> guardExpression1 = t1.getGuardExpression();
        assertThat(guardExpression1).hasSize(1);

        Expression guard = guardExpression1.get(0);
        assertThat(guard).isInstanceOf(OperatorExpression.class);

        OperatorExpression operationExpression = (OperatorExpression) guard;

        assertThat(operationExpression.getOperator()).isEqualTo(expectedOperator);

        EList<Expression> arguments = operationExpression.getArgument();

        assertThat(arguments).hasSize(2);

        assertThat(arguments.get(0)).isInstanceOf(expectedFirstParameterType);
        assertThat(arguments.get(1)).isInstanceOf(expectedSecondParameterType);
    }

    private boolean isSubSettingFromStandardLib(Feature feature, String expectedSubSettedFeature) {
        return feature.getOwnedSpecialization().stream()
                .filter(Subsetting.class::isInstance)
                .map(Subsetting.class::cast)
                .anyMatch(f -> f.getSubsettedFeature() != null && expectedSubSettedFeature.equals(f.getSubsettedFeature().getQualifiedName()));
    }

    private void checkMetadaUsage(MetadataUsage metadata, String expectedDefinitionName, String annotatedElementName, Map<Feature, String> attributeNameValue) {
        assertThat(metadata.getMetadataDefinition().getName()).isEqualTo(expectedDefinitionName);
        assertThat(metadata.getAnnotatedElement()).hasSize(1).as("Should annotate the element " + annotatedElementName).allMatch(e -> annotatedElementName.equals(e.getName()));

        EList<Feature> features = metadata.getOwnedFeature();
        assertThat(features).hasSize(attributeNameValue.size());

        for (Entry<Feature, String> entry : attributeNameValue.entrySet()) {
            Optional<ReferenceUsage> matchingFeature = features.stream()
                    .filter(ReferenceUsage.class::isInstance)
                    .map(ReferenceUsage.class::cast)
                    .filter(ref -> ref.redefines(entry.getKey()))
                    .findFirst();
            assertThat(matchingFeature).get().satisfies(f -> this.assertStringValue(f, entry.getValue()));
        }
    }

    private void assertStringValue(Feature f, String expectedValue) {
        FeatureValue valuation = new UtilService().getValuation(f);
        assertNotNull(valuation);
        assertThat(valuation.getValue())
                .isNotNull()
                .matches(v -> v instanceof LiteralString vString && expectedValue.equals(vString.getValue()), "The feature should have a Literal String with value " + expectedValue);
    }
}
