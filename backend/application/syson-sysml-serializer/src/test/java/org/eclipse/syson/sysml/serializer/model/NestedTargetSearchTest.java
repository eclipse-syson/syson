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
package org.eclipse.syson.sysml.serializer.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests the searches of reference targets stored on nested elements.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class NestedTargetSearchTest {

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * The nested reference target is the target of an exact reference usage: a linked part usage that references
     * another feature ({@code part p ::> q}) is skipped, even through the fallback to the installed target's name.
     */
    @Test
    void skipsLinkedFeaturesThatAreNotReferenceUsages() throws IOException {
        final ISysMLParseResult<Namespace> parsed = SerializerRoundTripTestSupport.parseTestCase("edited/nested-reference-target-of-a-part.sysml");
        final Element q = parsed.model().getOwnedRelationship().get(0).getOwnedRelatedElement().get(0);
        final Element definition = parsed.model().getOwnedRelationship().get(1).getOwnedRelatedElement().get(0);
        for (final DanglingLink link : parsed.danglingLinks()) {
            if (link.reference() == SysMLPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature()) {
                link.context().eSet(link.reference(), q);
            }
        }

        assertEquals("", new SerializerModelQueries().nestedTargets().nestedReferenceTarget(definition,
                DanglingReferenceIndex.from(parsed.danglingLinks())));
    }
}
