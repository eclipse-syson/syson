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
package org.eclipse.syson.sysml.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.internal.StructuralModelCompletionService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureDirectionKind;
import org.omg.sysml.lang.sysml.ReturnParameterMembership;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Checks the source distinction lost when completion infers a return direction.
 *
 * @author cbrun
 */
public class ReturnParameterDirectionTests {

    private static final SysMLFactory FACTORY = SysMLFactory.eINSTANCE;

    private final StructuralModelCompletionService service = new StructuralModelCompletionService();

    @BeforeClass
    public static void setUp() {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /** Completion must retain the distinction between inferred and explicitly written OUT directions. */
    @Test
    public void marksAnOmittedReturnDirectionBeforeInferringOut() {
        ReturnParameterMembership membership = FACTORY.createReturnParameterMembership();
        Feature parameter = FACTORY.createFeature();
        membership.getOwnedRelatedElement().add(parameter);

        this.service.complete(membership);

        assertEquals(FeatureDirectionKind.OUT, parameter.getDirection());
        assertTrue(parameter.eAdapters().contains(ITextAdapter.RETURN_DIRECTION_OMITTED));
    }

    @Test
    public void doesNotMarkAnExplicitReturnDirectionAsOmitted() {
        ReturnParameterMembership membership = FACTORY.createReturnParameterMembership();
        Feature parameter = FACTORY.createFeature();
        parameter.setDirection(FeatureDirectionKind.OUT);
        membership.getOwnedRelatedElement().add(parameter);

        this.service.complete(membership);

        assertEquals(FeatureDirectionKind.OUT, parameter.getDirection());
        assertFalse(parameter.eAdapters().contains(ITextAdapter.RETURN_DIRECTION_OMITTED));
    }

}
