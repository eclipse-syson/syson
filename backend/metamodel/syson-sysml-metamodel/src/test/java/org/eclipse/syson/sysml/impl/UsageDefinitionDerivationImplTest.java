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
package org.eclipse.syson.sysml.impl;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.syson.sysml.AnalysisCaseDefinition;
import org.eclipse.syson.sysml.AnalysisCaseUsage;
import org.eclipse.syson.sysml.CalculationDefinition;
import org.eclipse.syson.sysml.CalculationUsage;
import org.eclipse.syson.sysml.ConcernDefinition;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.RenderingDefinition;
import org.eclipse.syson.sysml.RenderingUsage;
import org.eclipse.syson.sysml.VerificationCaseDefinition;
import org.eclipse.syson.sysml.VerificationCaseUsage;
import org.eclipse.syson.sysml.ViewpointDefinition;
import org.eclipse.syson.sysml.ViewpointUsage;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the derivation of specialized definition references from feature typings.
 *
 * @author arichard
 */
public class UsageDefinitionDerivationImplTest {

    private ModelBuilder builder;

    /**
     * Initializes the model builder used to create test elements.
     */
    @BeforeEach
    public void setUp() {
        this.builder = new ModelBuilder();
    }

    /**
     * Verifies that an analysis case usage derives its analysis case definition.
     */
    @Test
    public void analysisCaseUsageDerivesAnalysisCaseDefinition() {
        AnalysisCaseUsage usage = this.builder.create(AnalysisCaseUsage.class);
        AnalysisCaseDefinition definition = this.builder.create(AnalysisCaseDefinition.class);

        this.builder.setType(usage, definition);

        assertSame(definition, usage.getAnalysisCaseDefinition());
    }

    /**
     * Verifies that a calculation usage derives its calculation definition.
     */
    @Test
    public void calculationUsageDerivesCalculationDefinition() {
        CalculationUsage usage = this.builder.create(CalculationUsage.class);
        CalculationDefinition definition = this.builder.create(CalculationDefinition.class);

        this.builder.setType(usage, definition);

        assertSame(definition, usage.getCalculationDefinition());
    }

    /**
     * Verifies that a concern usage derives its concern definition.
     */
    @Test
    public void concernUsageDerivesConcernDefinition() {
        ConcernUsage usage = this.builder.create(ConcernUsage.class);
        ConcernDefinition definition = this.builder.create(ConcernDefinition.class);

        this.builder.setType(usage, definition);

        assertSame(definition, usage.getConcernDefinition());
    }

    /**
     * Verifies that a rendering usage derives its rendering definition.
     */
    @Test
    public void renderingUsageDerivesRenderingDefinition() {
        RenderingUsage usage = this.builder.create(RenderingUsage.class);
        RenderingDefinition definition = this.builder.create(RenderingDefinition.class);

        this.builder.setType(usage, definition);

        assertSame(definition, usage.getRenderingDefinition());
    }

    /**
     * Verifies that a verification case usage derives its verification case definition.
     */
    @Test
    public void verificationCaseUsageDerivesVerificationCaseDefinition() {
        VerificationCaseUsage usage = this.builder.create(VerificationCaseUsage.class);
        VerificationCaseDefinition definition = this.builder.create(VerificationCaseDefinition.class);

        this.builder.setType(usage, definition);

        assertSame(definition, usage.getVerificationCaseDefinition());
    }

    /**
     * Verifies that a viewpoint usage derives its viewpoint definition.
     */
    @Test
    public void viewpointUsageDerivesViewpointDefinition() {
        ViewpointUsage usage = this.builder.create(ViewpointUsage.class);
        ViewpointDefinition definition = this.builder.create(ViewpointDefinition.class);

        this.builder.setType(usage, definition);

        assertSame(definition, usage.getViewpointDefinition());
    }
}
