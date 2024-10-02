/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.application.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link DetailsViewService}.
 *
 * @author gdaniel
 */
public class DetailsViewServiceTest {

    private DetailsViewService detailsViewService;

    @BeforeEach
    public void setUp() {
        // Use a dummy CompsedAdapterFactory, we don't test methods that require the one used by SysON for the moment.
        this.detailsViewService = new DetailsViewService(new ComposedAdapterFactory(), new IFeedbackMessageService.NoOp());
    }

    @Test
    public void getCoreFeaturesOfPartUsage() {
        List<EStructuralFeature> coreStructuralFeatures = this.detailsViewService.getCoreFeatures(SysmlFactory.eINSTANCE.createPartUsage());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getElement_DeclaredName(),
                SysmlPackage.eINSTANCE.getElement_QualifiedName(),
                SysmlPackage.eINSTANCE.getElement_DeclaredShortName(),
                SysmlPackage.eINSTANCE.getFeature_Direction(),
                SysmlPackage.eINSTANCE.getOccurrenceUsage_IsIndividual());
    }
}
