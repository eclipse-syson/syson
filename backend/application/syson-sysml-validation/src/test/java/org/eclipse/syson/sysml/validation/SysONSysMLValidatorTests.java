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
package org.eclipse.syson.sysml.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EValidatorRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.interpreter.AQLInterpreter;
import org.eclipse.sirius.components.interpreter.Result;
import org.eclipse.sirius.components.interpreter.Status;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.validation.rules.SysONValidationRule;
import org.eclipse.syson.sysml.validation.rules.ValidationRule;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRule;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRulesProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.LoggerFactory;

/**
 * Checks that only successfully evaluated, false constraints produce model diagnostics.
 *
 * @author adaussy
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class SysONSysMLValidatorTests {

    private final Logger logger = (Logger) LoggerFactory.getLogger(SysONSysMLValidator.class);

    private final ListAppender<ILoggingEvent> appender = new ListAppender<>();

    @AfterEach
    public void detachLogAppender() {
        this.logger.detachAppender(this.appender);
        this.appender.stop();
    }

    @BeforeEach
    public void initializeLogAppender() {
        this.appender.start();
        this.logger.addAppender(this.appender);
    }

    @Test
    @DisplayName("GIVEN a broken rule and a violated rule, WHEN validating a feature, THEN validation continues and reports only the violation")
    public void validateContinuesAfterEvaluationFailure() {
        // GIVEN
        AttributeUsage feature = this.createFeature();
        IValidationRule brokenRule = this.createRule("brokenRule", "aql:self.missingOperation()");
        IValidationRule violatedRule = this.createRule("violatedRule", "aql:false");
        SysONSysMLValidator validator = this.createValidator(brokenRule, violatedRule);
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN
        boolean valid = validator.validate(feature, diagnostics, Map.of());

        // THEN
        assertThat(valid).isFalse();
        assertThat(diagnostics.getChildren()).singleElement().satisfies(diagnostic -> {
            assertThat(diagnostic.getSource()).isEqualTo("violatedRule");
            assertThat(diagnostic.getData()).isEqualTo(List.of(feature));
        });
        assertThat(this.appender.list).singleElement().satisfies(event -> {
            assertThat(event.getLevel()).isEqualTo(Level.WARN);
            assertThat(event.getFormattedMessage()).contains("brokenRule", "local-feature");
        });
    }

    @Test
    @DisplayName("GIVEN a feature without a resource, WHEN a constraint is violated, THEN its diagnostic is produced without an exception")
    public void validateDetachedFeatureViolation() {
        // GIVEN
        AttributeUsage feature = this.createFeature();
        feature.eResource().getContents().remove(feature);
        SysONSysMLValidator validator = this.createValidator(this.createRule("violatedRule", "aql:false"));
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN
        boolean valid = validator.validate(feature, diagnostics, Map.of());

        // THEN
        assertThat(valid).isFalse();
        assertThat(diagnostics.getChildren()).singleElement().satisfies(diagnostic -> {
            assertThat(diagnostic.getSource()).isEqualTo("violatedRule");
            assertThat(diagnostic.getData()).isEqualTo(List.of(feature));
        });
    }

    @Test
    @DisplayName("GIVEN a broken rule and a violated rule, WHEN validating an editing context, THEN only the model diagnostic is returned")
    public void validateEditingContextKeepsOnlyModelDiagnostics() {
        // GIVEN
        AttributeUsage feature = this.createFeature();
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getResources().add(feature.eResource());
        IEMFEditingContext editingContext = mock(IEMFEditingContext.class);
        when(editingContext.getDomain()).thenReturn(new AdapterFactoryEditingDomain(new ComposedAdapterFactory(), new BasicCommandStack(), resourceSet));
        SysONSysMLValidator validator = this.createValidator(
                this.createRule("brokenRule", "aql:self.missingOperation()"), this.createRule("violatedRule", "aql:false"));
        EValidator.Registry registry = new EValidatorRegistryImpl();
        registry.put(SysmlPackage.eINSTANCE, validator);
        SysONSysMLValidationService service = new SysONSysMLValidationService(registry, mock(ISysONResourceService.class));

        // WHEN
        List<Object> diagnostics = service.validate((IEditingContext) editingContext);

        // THEN
        assertThat(diagnostics).singleElement().isInstanceOfSatisfying(Diagnostic.class, diagnostic -> {
            assertThat(diagnostic.getSource()).isEqualTo("violatedRule");
            assertThat(diagnostic.getSeverity()).isEqualTo(Diagnostic.WARNING);
            assertThat(diagnostic.getData()).isEqualTo(List.of(feature));
        });
        assertThat(this.appender.list).singleElement().satisfies(event ->
                assertThat(event.getFormattedMessage()).contains("brokenRule", "local-feature"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"aql:self.", "aql:self.missingOperation()", "aql:42", "aql:null", "aql:'true'", "aql:'false'",
        "aql:Sequence{self.missingOperation(), true}->last()", "aql:Sequence{self.missingOperation(), false}->last()"})
    @DisplayName("GIVEN an unsuccessful or non-Boolean AQL result, WHEN validating a feature, THEN only a server warning is produced")
    public void validateEvaluationFailureIsOnlyLogged(String expression) {
        // GIVEN
        AttributeUsage feature = this.createFeature();
        SysONSysMLValidator validator = this.createValidator(this.createRule("brokenRule", expression));
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN
        var valid = validator.validate(feature, diagnostics, Map.of());
        assertThat(valid).isTrue();

        // THEN
        assertThat(diagnostics.getChildren()).isEmpty();
        assertThat(this.appender.list).singleElement().satisfies(event -> {
            assertThat(event.getLevel()).isEqualTo(Level.WARN);
            assertThat(event.getFormattedMessage()).contains("brokenRule", "local-feature", expression);
        });
    }

    @Test
    @DisplayName("GIVEN an AQL expression with an unresolved service call, WHEN evaluating it, THEN true can accompany a non-OK status")
    public void validateFailedEvaluationCanStillReturnTrue() {
        // GIVEN
        AttributeUsage feature = this.createFeature();
        String expression = "aql:Sequence{self.missingOperation(), true}->last()";
        AQLInterpreter interpreter = new AQLInterpreter(List.of(), List.of(SysmlPackage.eINSTANCE));

        // WHEN
        Result result = interpreter.evaluateExpression(Map.of("self", feature), expression);

        // THEN
        assertThat(result.getStatus()).isNotEqualTo(Status.OK);
        assertThat(result.asObject()).contains(true);
    }

    @Test
    @DisplayName("GIVEN a satisfied constraint, WHEN validating a feature, THEN no diagnostic or server warning is produced")
    public void validateSatisfiedConstraint() {
        // GIVEN
        AttributeUsage feature = this.createFeature();
        SysONSysMLValidator validator = this.createValidator(this.createRule("satisfiedRule", "aql:true"));
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN
        boolean valid = validator.validate(feature, diagnostics, Map.of());

        // THEN
        assertThat(valid).isTrue();
        assertThat(diagnostics.getChildren()).isEmpty();
        assertThat(this.appender.list).isEmpty();
    }

    @Test
    @DisplayName("GIVEN a violated constraint, WHEN validating a feature, THEN the expected model diagnostic is produced")
    public void validateViolatedConstraint() {
        // GIVEN
        AttributeUsage feature = this.createFeature();
        SysONSysMLValidator validator = this.createValidator(this.createRule("violatedRule", "aql:false"));
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN
        boolean valid = validator.validate(feature, diagnostics, Map.of());

        // THEN
        assertThat(valid).isFalse();
        assertThat(diagnostics.getChildren()).singleElement().satisfies(diagnostic -> {
            assertThat(diagnostic.getSource()).isEqualTo("violatedRule");
            assertThat(diagnostic.getSeverity()).isEqualTo(Diagnostic.WARNING);
            assertThat(diagnostic.getData()).isEqualTo(List.of(feature));
            assertThat(diagnostic.getMessage()).isEqualTo(
                    "#violatedRule·····[memory:/validation.sysml/speed]‎·····Constraint on type 'sysml:AttributeUsage' is not respected: Test constraint.");
        });
        assertThat(this.appender.list).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({"true, false", "false, true", "false, false"})
    @DisplayName("GIVEN a redefinition with missing endpoints, WHEN validating it, THEN one actionable integrity error replaces dependent semantic checks")
    public void validateMissingRedefinitionFeatures(boolean hasLocal, boolean hasSource) {
        // GIVEN the same Base/Derived example, with explicitly unset endpoints as an incomplete relationship can store.
        RedefinitionModel model = this.createRedefinitionModel();
        if (!hasLocal) {
            model.redefinition().setRedefiningFeature(null);
        }
        if (!hasSource) {
            model.redefinition().setRedefinedFeature(null);
        }
        IValidationRule dependentRule = new ValidationRule(SysmlPackage.eINSTANCE.getRedefinition(), "validateSubsettingFeaturingTypes",
                "aql:self.missingOperation()", "This check must not run on an incomplete redefinition.", Diagnostic.WARNING);
        SysONSysMLValidator validator = this.createRedefinitionValidator(dependentRule);
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN checking the incomplete relationship, before attempting semantic navigation through its endpoints.
        boolean valid = validator.validate(model.redefinition(), diagnostics, Map.of());

        // THEN only the missing endpoints are reported, with the relation selected and no secondary AQL warning.
        assertThat(valid)
                .isFalse();
        this.assertRedefinitionIntegrityDiagnostic(diagnostics, model.redefinition());
        assertThat(this.appender.list)
                .isEmpty();
        assertThat(model.redefinition().getRedefiningFeature() != null)
                .isEqualTo(hasLocal);
        assertThat(model.redefinition().getRedefinedFeature() != null)
                .isEqualTo(hasSource);
    }


    @Test
    @DisplayName("GIVEN an incomplete redefinition, WHEN restoring its target, THEN the integrity error disappears and semantic checks resume")
    public void validateRepairedRedefinition() {
        // GIVEN attribute speed :>> Base::speed with a missing target and an endpoint-dependent semantic rule returning false.
        RedefinitionModel model = this.createRedefinitionModel();
        model.redefinition().setRedefinedFeature(null);
        IValidationRule semanticRule = new ValidationRule(SysmlPackage.eINSTANCE.getRedefinition(), "validateSubsettingFeaturingTypes", "aql:false", "Semantic violation.", Diagnostic.WARNING);
        SysONSysMLValidator validator = this.createRedefinitionValidator(semanticRule);
        BasicDiagnostic incomplete = new BasicDiagnostic();
        validator.validate(model.redefinition(), incomplete, Map.of());
        this.assertRedefinitionIntegrityDiagnostic(incomplete, model.redefinition());

        // WHEN restoring the exact original target and validating again with the same validator.
        model.redefinition().setRedefinedFeature(model.source());
        BasicDiagnostic repaired = new BasicDiagnostic();
        boolean valid = validator.validate(model.redefinition(), repaired, Map.of());

        // THEN the dependent semantic check resumes now that both endpoints are present.
        assertThat(valid)
                .isFalse();
        assertThat(repaired.getChildren())
                .extracting(Diagnostic::getSource)
                .containsExactly("validateSubsettingFeaturingTypes");
        assertThat(model.redefinition().getRedefinedFeature())
                .isSameAs(model.source());
        assertThat(this.appender.list)
                .isEmpty();
    }

    @Test
    @DisplayName("GIVEN a complete redefinition owned by a type, WHEN validating it, THEN integrity checks accept the separate relationship")
    public void validateSeparateRedefinition() {
        // GIVEN Base::speed and Derived::speed linked by a complete redefinition owned by Derived itself.
        RedefinitionModel model = this.createRedefinitionModel();
        model.local().getOwningType().getOwnedRelationship().add(model.redefinition());
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN validating this separate declaration instead of requiring containment in the local feature.
        boolean valid = this.createRedefinitionValidator().validate(model.redefinition(), diagnostics, Map.of());

        // THEN endpoint presence is sufficient for this structural check; ownership is not silently rewritten.
        assertThat(valid)
                .isTrue();
        assertThat(diagnostics.getChildren())
                .isEmpty();
        assertThat(model.redefinition().getOwningRelatedElement())
                .isSameAs(model.local().getOwningType());
        assertThat(model.redefinition().getRedefiningFeature())
                .isSameAs(model.local());
    }

    @Test
    @DisplayName("GIVEN an anonymous local feature with an incomplete redefinition, WHEN validating it, THEN the relationship ID identifies the incomplete relation")
    public void validateAnonymousRedefinitionOwner() {
        // GIVEN part def Derived { attribute :>> Base::speed; }, with the target cleared after creation.
        RedefinitionModel model = this.createRedefinitionModel();
        model.local().setDeclaredName(null);
        model.redefinition().setRedefinedFeature(null);
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN the local feature can no longer inherit a name from the missing target.
        boolean valid = this.createRedefinitionValidator().validate(model.redefinition(), diagnostics, Map.of());

        // THEN the diagnostic still selects the relation and identifies it by its stable ID.
        assertThat(valid)
                .isFalse();
        this.assertRedefinitionIntegrityDiagnostic(diagnostics, model.redefinition());
    }

    @Test
    @DisplayName("GIVEN a detached incomplete redefinition, WHEN validating it, THEN a diagnostic uses its ID without requiring a document or owner")
    public void validateDetachedIncompleteRedefinition() {
        // GIVEN a previously owned redefinition removed from containment, still missing its target.
        RedefinitionModel model = this.createRedefinitionModel();
        model.local().getOwnedRelationship().remove(model.redefinition());
        model.redefinition().setRedefinedFeature(null);
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN validating an isolated relationship, as can occur in service-level callers.
        boolean valid = this.createRedefinitionValidator().validate(model.redefinition(), diagnostics, Map.of());

        // THEN neither the absent resource nor absent owner prevents reporting the missing endpoint.
        assertThat(valid)
                .isFalse();
        assertThat(diagnostics.getChildren())
                .singleElement()
                .satisfies((Diagnostic diagnostic) -> {
                    assertThat(diagnostic.getSource())
                        .isEqualTo("validateRedefinitionRequiredFeatures");
                    assertThat(diagnostic.getData())
                        .isEqualTo(List.of(model.redefinition()));
                    assertThat(diagnostic.getMessage())
                        .isEqualTo(
                            "#validateRedefinitionRequiredFeatures·····[<no document>/<ID: local-redefinition>]‎·····Constraint on type 'sysml:Redefinition' is not respected: "
                            + SysONValidationRule.validateRedefinitionRequiredFeatures.message());
                });
    }

    private void assertRedefinitionIntegrityDiagnostic(BasicDiagnostic diagnostics, Redefinition relation) {
        assertThat(diagnostics.getChildren())
                .singleElement()
                .satisfies((Diagnostic diagnostic) -> {
                    assertThat(diagnostic.getSource())
                        .isEqualTo("validateRedefinitionRequiredFeatures");
                    assertThat(diagnostic.getSeverity())
                        .isEqualTo(Diagnostic.ERROR);
                    assertThat(diagnostic.getData())
                        .isEqualTo(List.of(relation));
                    assertThat(diagnostic.getMessage())
                        .isEqualTo(
                            "#validateRedefinitionRequiredFeatures·····[memory:/redefinitions.sysml/<ID: local-redefinition>]‎·····Constraint on type 'sysml:Redefinition' is not respected: " + SysONValidationRule.validateRedefinitionRequiredFeatures.message());
                });
    }

    private RedefinitionModel createRedefinitionModel() {
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        Resource resource = new JSONResourceFactory().createResource(URI.createURI("memory:/redefinitions.sysml"));
        resourceSet.getResources().add(resource);
        PartDefinition base = SysmlFactory.eINSTANCE.createPartDefinition();
        base.setDeclaredName("Base");
        PartDefinition derived = SysmlFactory.eINSTANCE.createPartDefinition();
        derived.setDeclaredName("Derived");
        resource.getContents().addAll(List.of(base, derived));
        Subclassification specialization = SysmlFactory.eINSTANCE.createSubclassification();
        specialization.setSubclassifier(derived);
        specialization.setSuperclassifier(base);
        derived.getOwnedRelationship().add(specialization);
        AttributeUsage source = SysmlFactory.eINSTANCE.createAttributeUsage();
        source.setDeclaredName("speed");
        source.setElementId("source-feature");
        FeatureMembership sourceMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        base.getOwnedRelationship().add(sourceMembership);
        sourceMembership.getOwnedRelatedElement().add(source);
        AttributeUsage local = SysmlFactory.eINSTANCE.createAttributeUsage();
        local.setDeclaredName("speed");
        local.setElementId("local-feature");
        FeatureMembership localMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        derived.getOwnedRelationship().add(localMembership);
        localMembership.getOwnedRelatedElement().add(local);
        Redefinition relation = SysmlFactory.eINSTANCE.createRedefinition();
        relation.setElementId("local-redefinition");
        relation.setRedefiningFeature(local);
        relation.setRedefinedFeature(source);
        local.getOwnedRelationship().add(relation);
        return new RedefinitionModel(resource, local, source, relation);
    }

    private SysONSysMLValidator createRedefinitionValidator(IValidationRule... rules) {
        IValidationRulesProvider provider = mock(IValidationRulesProvider.class);
        when(provider.getAQLServiceClasses()).thenReturn(List.of());
        when(provider.getAQLServiceInstances()).thenReturn(List.of());
        when(provider.getEPackages()).thenReturn(List.of());
        doReturn(Stream.concat(Stream.of(SysONValidationRule.validateRedefinitionRequiredFeatures), Stream.of(rules))
                .toList()).when(provider).getValidationRules(SysmlPackage.eINSTANCE.getRedefinition());
        return new SysONSysMLValidator(provider);
    }

    /**
     * The owned Base/Derived features and their stored redefinition.
     */
    private record RedefinitionModel(Resource resource, AttributeUsage local, AttributeUsage source, Redefinition redefinition) {
    }

    private AttributeUsage createFeature() {
        AttributeUsage feature = SysmlFactory.eINSTANCE.createAttributeUsage();
        feature.setDeclaredName("speed");
        feature.setElementId("local-feature");
        Resource resource = new ResourceImpl(URI.createURI("memory:/validation.sysml"));
        resource.getContents().add(feature);
        return feature;
    }

    private IValidationRule createRule(String name, String expression) {
        return new ValidationRule(SysmlPackage.eINSTANCE.getAttributeUsage(), name, expression, "Test constraint.", Diagnostic.WARNING);
    }

    private SysONSysMLValidator createValidator(IValidationRule... rules) {
        IValidationRulesProvider provider = mock(IValidationRulesProvider.class);
        when(provider.getAQLServiceClasses()).thenReturn(List.of());
        when(provider.getAQLServiceInstances()).thenReturn(List.of());
        when(provider.getEPackages()).thenReturn(List.of());
        doReturn(List.of(rules)).when(provider).getValidationRules(SysmlPackage.eINSTANCE.getAttributeUsage());
        return new SysONSysMLValidator(provider);
    }
}
