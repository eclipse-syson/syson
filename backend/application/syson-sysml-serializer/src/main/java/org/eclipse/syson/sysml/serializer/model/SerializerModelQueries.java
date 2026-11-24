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

import org.eclipse.syson.sysml.serializer.ReferenceNameResolver;
import org.eclipse.syson.sysml.serializer.ReferencePathResolver;

/**
 * The model queries of one serialization run, wired once from the serialization mode and the caller's resolvers.
 * Renderers keep only the collaborators they use:
 * <ul>
 * <li>{@link ModelNavigation}: owned relationships, body members, ends, values, operands and containers;</li>
 * <li>{@link ModelFlags}: attributes written as keywords or prefixes (visibility, {@code abstract}, direction,
 * ...);</li>
 * <li>{@link ParserAdapterQueries}: syntax choices recorded by the parser (markers, comments, literal spellings);</li>
 * <li>{@link MultiplicityText}: bracketed multiplicities;</li>
 * <li>{@link ReferenceTargets}: text and target of one cross-reference, imports and aliases, proxy naming;</li>
 * <li>{@link SpecializationTargetNames}: targets of typing, subsetting, redefinition and similar clauses;</li>
 * <li>{@link NestedTargetSearch}: reference text found in owned subtrees (dangling targets, feature chains,
 * assignment targets).</li>
 * </ul>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SerializerModelQueries {

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    private final MultiplicityText multiplicities;

    private final ReferenceTargets referenceTargets;

    private final SpecializationTargetNames specializationTargets;

    private final NestedTargetSearch nestedTargets;

    /** Creates normalized-mode queries without contextual names or reference paths, as used by focused tests. */
    public SerializerModelQueries() {
        this(false);
    }

    /** Creates queries without contextual names or reference paths. */
    public SerializerModelQueries(final boolean preserveParsedReferenceNames) {
        this(preserveParsedReferenceNames, (target, context) -> null);
    }

    /** Creates queries without reference paths. */
    public SerializerModelQueries(final boolean preserveParsedReferenceNames, final ReferenceNameResolver referenceNameResolver) {
        this(preserveParsedReferenceNames, referenceNameResolver, ReferencePathResolver.NONE);
    }

    /**
     * Wires the queries of one run.
     *
     * @param preserveParsedReferenceNames
     *            {@code true} in round-trip mode, where parser spellings and omissions are preserved
     * @param referenceNameResolver
     *            supplies contextual names for fallback reference targets
     * @param referencePathResolver
     *            confirms parser paths that the namespace lookup cannot resolve
     */
    public SerializerModelQueries(final boolean preserveParsedReferenceNames, final ReferenceNameResolver referenceNameResolver,
            final ReferencePathResolver referencePathResolver) {
        this.navigation = new ModelNavigation();
        this.adapters = new ParserAdapterQueries();
        this.flags = new ModelFlags(preserveParsedReferenceNames, this.adapters);
        this.multiplicities = new MultiplicityText(this.navigation);
        this.referenceTargets = new ReferenceTargets(preserveParsedReferenceNames, referenceNameResolver, referencePathResolver,
                this.navigation);
        this.nestedTargets = new NestedTargetSearch(this.navigation, this.referenceTargets);
        this.specializationTargets = new SpecializationTargetNames(this.navigation, this.referenceTargets, this.nestedTargets);
    }

    /**
     * Returns the shared containment queries for owned members, relationship ends, values and operands.
     *
     * @return the run's model navigation
     */
    public ModelNavigation navigation() {
        return this.navigation;
    }

    /**
     * Returns the queries that turn model attributes and parser markers into declaration keywords and modifiers.
     *
     * @return the flag queries configured for this run's serialization mode
     */
    public ModelFlags flags() {
        return this.flags;
    }

    /**
     * Returns access to the parser's syntax markers, comments and captured literal spellings.
     *
     * @return the shared parser-adapter queries
     */
    public ParserAdapterQueries adapters() {
        return this.adapters;
    }

    /**
     * Returns the queries that assemble bracketed multiplicity text from owned and derived bound expressions.
     *
     * @return the run's multiplicity text service
     */
    public MultiplicityText multiplicities() {
        return this.multiplicities;
    }

    /**
     * Returns reference naming that combines stored targets, retained parser spellings and the caller's resolvers.
     * Proxies are named without resolving them in the model.
     *
     * @return the shared reference-target queries
     */
    public ReferenceTargets referenceTargets() {
        return this.referenceTargets;
    }

    /**
     * Returns the target names of typing, subsetting, redefinition and related specialization clauses.
     *
     * @return the run's specialization-target queries
     */
    public SpecializationTargetNames specializationTargets() {
        return this.specializationTargets;
    }

    /**
     * Returns searches for reference spellings and targets held in owned subtrees, such as chains and assignment targets.
     *
     * @return the run's nested-reference queries
     */
    public NestedTargetSearch nestedTargets() {
        return this.nestedTargets;
    }
}
