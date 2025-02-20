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
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingDomainFactory;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.configuration.SysMLEditingContextProcessor;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SuccessionAsUsage;
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
