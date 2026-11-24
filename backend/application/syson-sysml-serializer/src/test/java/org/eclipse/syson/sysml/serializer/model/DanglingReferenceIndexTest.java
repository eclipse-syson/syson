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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.Dependency;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests the index of parser spellings kept for unresolved references.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class DanglingReferenceIndexTest {

    static {
        // Install delegates before initializing the metamodel through the reference constants below.
        SysMLLogicStandaloneSetup.doSetup();
    }

    private static final EReference TYPE_REFERENCE = SysMLPackage.eINSTANCE.getFeatureTyping_Type();

    private static final EClass TYPE_CLASS = SysMLPackage.eINSTANCE.getType();

    private static final String TARGET_NAME = "T";

    /**
     * Links missing their context, reference or target list must not make any query throw: they must not be indexed
     * by name, must not report a target of any type, and must not count as resolved, while complete links next to them
     * are still answered. Queries with a missing context or reference must return empty results too, and must not
     * match the incomplete links that lack one.
     */
    @Test
    void toleratesIncompleteLinks() {
        final FeatureTyping typing = SysMLFactory.eINSTANCE.createFeatureTyping();
        final FeatureTyping incomplete = SysMLFactory.eINSTANCE.createFeatureTyping();
        final DanglingReferenceIndex index = DanglingReferenceIndex.from(Set.of(
                new DanglingLink(typing, TYPE_REFERENCE, TYPE_CLASS, List.of(TARGET_NAME)),
                new DanglingLink(incomplete, TYPE_REFERENCE, TYPE_CLASS, null),
                new DanglingLink(null, TYPE_REFERENCE, TYPE_CLASS, List.of("U")),
                new DanglingLink(incomplete, null, TYPE_CLASS, List.of("V"))));

        assertTrue(index.hasTargetOfType(typing, TYPE_REFERENCE, TYPE_CLASS));
        assertFalse(index.hasTargetOfType(incomplete, TYPE_REFERENCE, TYPE_CLASS));
        assertFalse(index.hasTargetOfType(null, TYPE_REFERENCE, TYPE_CLASS));
        assertFalse(index.hasTargetOfType(incomplete, null, TYPE_CLASS));
        assertEquals(List.of(TARGET_NAME), index.targets(typing, TYPE_REFERENCE.getName()));
        assertEquals(List.of(), index.targets(incomplete));
        assertEquals(Set.of(typing), index.contexts());
        assertFalse(index.hasResolvedTargets());
        for (final DanglingReferenceIndex queried : List.of(index, DanglingReferenceIndex.empty())) {
            assertEquals(List.of(), queried.targets(null, TYPE_REFERENCE.getName()));
            assertEquals(List.of(), queried.targets(typing, null));
            assertEquals(List.of(), queried.targets(null));
            assertEquals(Set.of(), queried.referenceNames(null));
        }
    }

    /** A set containing a {@code null} link must still give an index, without the null link. */
    @Test
    void ignoresNullLinks() {
        final FeatureTyping typing = SysMLFactory.eINSTANCE.createFeatureTyping();
        final Set<DanglingLink> links = new HashSet<>();
        links.add(null);

        final DanglingReferenceIndex onlyNull = DanglingReferenceIndex.from(Collections.singleton(null));
        links.add(new DanglingLink(typing, TYPE_REFERENCE, TYPE_CLASS, List.of(TARGET_NAME)));
        final DanglingReferenceIndex withNull = DanglingReferenceIndex.from(links);

        assertEquals(Set.of(), onlyNull.contexts());
        assertFalse(onlyNull.hasResolvedTargets());
        assertEquals(List.of(TARGET_NAME), withNull.targets(typing));
        assertTrue(withNull.hasTargetOfType(typing, TYPE_REFERENCE, TYPE_CLASS));
    }

    /** Clearing a target list after building the index must change neither name nor type queries. */
    @Test
    void snapshotsTargetLists() {
        final FeatureTyping typing = SysMLFactory.eINSTANCE.createFeatureTyping();
        final List<String> names = new ArrayList<>(List.of(TARGET_NAME));
        final DanglingReferenceIndex index = DanglingReferenceIndex.from(Set.of(new DanglingLink(typing, TYPE_REFERENCE, TYPE_CLASS, names)));

        names.clear();

        assertEquals(List.of(TARGET_NAME), index.targets(typing, TYPE_REFERENCE.getName()));
        assertTrue(index.hasTargetOfType(typing, TYPE_REFERENCE, TYPE_CLASS));
    }

    /** Spellings of different references of one object must keep the order in which the parser reported the links. */
    @Test
    void keepsLinkOrderAcrossReferences() {
        final Dependency dependency = SysMLFactory.eINSTANCE.createDependency();
        final EReference client = SysMLPackage.eINSTANCE.getDependency_Client();
        final EReference supplier = SysMLPackage.eINSTANCE.getDependency_Supplier();
        final Set<DanglingLink> links = new LinkedHashSet<>();
        links.add(new DanglingLink(dependency, client, TYPE_CLASS, List.of("a1")));
        links.add(new DanglingLink(dependency, supplier, TYPE_CLASS, List.of("b1")));
        links.add(new DanglingLink(dependency, client, TYPE_CLASS, List.of("a2")));

        final DanglingReferenceIndex index = DanglingReferenceIndex.from(links);

        assertEquals(List.of("a1", "b1", "a2"), index.targets(dependency));
        assertEquals(List.of("a1", "a2"), index.targets(dependency, client.getName()));
    }
}
