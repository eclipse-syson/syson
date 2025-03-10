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

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingDomainFactory;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.configuration.SysMLEditingContextProcessor;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.upload.SysMLExternalResourceLoaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    private IEditingDomainFactory editingDomainFactory;

    @Autowired
    private SysMLEditingContextProcessor sysMLEditingContextProcessor;

    private SysMLv2SemanticImportChecker checker;

    @BeforeEach
    public void setUp() {
        this.checker = new SysMLv2SemanticImportChecker(this.sysmlResourceLoader, this.editingDomainFactory, this.sysMLEditingContextProcessor);
    }

    @Test
    @DisplayName("Given a model with duplicated names, when importing the model, then the resolution of name shoud match the closest matching element")
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
    @DisplayName("Given a model with a Redefintion, when importing the model, then the resolution of name should special Redefinition rules")
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
    @DisplayName("Given a model with a reference to an invalid type, when importing the model then the resolution of name shoud not set a reference with an incompatible target")
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
    @DisplayName("Given a model with PortDefinitions, when importing the model, then a conjugated port is create for each conjugated ports")
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
    @DisplayName("Given a SuccessionAsUsage using the syntax that create a new target action, when importing and exporting the model, then the source and target should be correctly computed.")
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
    @DisplayName("Given a SuccessionAsUsage with an implicit source feature targeting the 'start' standard library element, "
            + "when importing the model, "
            + "then a special membership should be created that reference the 'start' element and its id should belong to the aliasId of the source connector end.")
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

            Membership startMembership = startMemberships.get(0);

            // Check that alias id for the ReferenceUsage of the source feature
            Feature sourceEnd = successionUsage.getConnectorEnd().get(0);
            assertThat(sourceEnd).isInstanceOf(ReferenceUsage.class);
            assertEquals(List.of(startMembership.getElementId()), sourceEnd.getAliasIds());

        }).check(input);
    }

    @Test
    @DisplayName("Given a SuccessionAsUsage with an explicit source feature targeting the 'start' standard library element, "
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
}
