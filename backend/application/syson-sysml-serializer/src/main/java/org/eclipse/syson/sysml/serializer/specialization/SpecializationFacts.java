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
package org.eclipse.syson.sysml.serializer.specialization;

import java.util.List;

import org.omg.sysml.lang.sysml.Relationship;

/**
 * Specialization values of one element, collected once by {@link SpecializationFactsCollector}.
 * <p>
 * {@code items} lists the explicit specializations and the multiplicity in source order, which is the order of the
 * element's non-implied owned relationships: writing them in that order reproduces the source clause interleaving
 * and lets the reparsed model keep the same relationship order. {@code multiplicity} is the text to emit, blank when
 * another construct already renders it, while {@code rawMultiplicity} is always the model's multiplicity, kept to strip
 * it from captured redefinition names and to decide where a late {@code nonunique} goes.
 * </p>
 *
 * @param items
 *            the specialization and multiplicity items, in source order
 * @param rawMultiplicity
 *            the model multiplicity text
 * @param multiplicity
 *            the multiplicity text to emit in the specialization part, possibly blank
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
record SpecializationFacts(List<SourceItem> items, String rawMultiplicity, String multiplicity) {

    /** Kind of a specialization item, one per {@code featureSpecialization} alternative plus the multiplicity. */
    enum ClauseKind {
        /** {@code :} / {@code defined by}. */
        TYPING,
        /** {@code :>} / {@code specializes}. */
        SUBCLASSIFICATION,
        /** {@code :>} / {@code subsets}. */
        SUBSETTING,
        /** {@code :>>} / {@code redefines}. */
        REDEFINITION,
        /** {@code ::>} / {@code references}. */
        REFERENCE_SUBSETTING,
        /** {@code =>} / {@code crosses}. */
        CROSS_SUBSETTING,
        /** The owned multiplicity ({@code [1]}). */
        MULTIPLICITY;

        /** Tells whether the grammar rule of this kind takes exactly one target per clause. */
        boolean singleTarget() {
            return this == REFERENCE_SUBSETTING || this == CROSS_SUBSETTING;
        }
    }

    /**
     * One explicit relationship of the declaration.
     *
     * @param kind
     *            the clause kind
     * @param relationship
     *            the source relationship, or {@code null} for a multiplicity not owned by a direct relationship
     * @param names
     *            the rendered target names, empty for a multiplicity
     */
    record SourceItem(ClauseKind kind, Relationship relationship, List<String> names) {
    }
}
