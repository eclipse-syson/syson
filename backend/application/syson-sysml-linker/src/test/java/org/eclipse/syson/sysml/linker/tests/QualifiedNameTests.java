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
package org.eclipse.syson.sysml.linker.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.syson.sysml.linker.QualifiedName;
import org.junit.Test;

/**
 * Checks quoted dollar names, global qualification, and conjugated global qualification.
 *
 * @author cbrun
 */
public class QualifiedNameTests {

    @Test
    public void keepsDollarAsALocalElementName() {
        QualifiedName name = new QualifiedName("'$'");
        assertFalse(name.isGlobal());
        assertTrue(name.isLocal());
        assertEquals("$", name.getFirstSegment());
    }

    @Test
    public void keepsQuotedDollarAsALocalNamespaceName() {
        QualifiedName name = new QualifiedName("'$'::Name");
        assertFalse(name.isGlobal());
        assertEquals("$", name.getFirstSegment());
        assertEquals("Name", name.removeFirstSegment().getFirstSegment());
    }

    @Test
    public void recognizesTheGlobalPrefix() {
        QualifiedName name = new QualifiedName("$::Name");
        assertTrue(name.isGlobal());
        assertEquals("Name", name.getFirstSegment());
        assertEquals("$::Name", name.toString());
    }

    @Test
    public void keepsConjugatedGlobalQualification() {
        QualifiedName name = new QualifiedName("~$::Name");
        assertTrue(name.isGlobal());
        assertTrue(name.isConjugated());
        assertEquals("$::Name::~Name", name.getLongNotation().toString());
    }
}
