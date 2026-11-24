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
package org.eclipse.syson.sysml.serializer;

import java.util.Objects;

import org.eclipse.syson.sysml.serializer.model.DanglingReferenceIndex;
import org.eclipse.syson.sysml.serializer.model.SerializerModelQueries;
import org.eclipse.syson.sysml.serializer.names.NameRenderer;
import org.eclipse.syson.sysml.serializer.output.TextOutput;

/**
 * Dependencies shared by the renderers during one serialization run.
 * <p>
 * The context is created once per call by {@link SysMLSerializer} and handed to every renderer, which keeps only the
 * collaborators it needs. The references are fixed, but the output buffer and name renderer hold mutable per-run
 * state, so a context must never be reused across calls or threads. Renderer caches and reference-collection caches
 * also belong to this run, never to the public serializer.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SerializationContext {

    private final SerializationOptions options;

    private final TextOutput output;

    private final NameRenderer nameRenderer;

    private final SerializerModelQueries modelQueries;

    private final DanglingReferenceIndex danglingReferenceIndex;

    private final ReferencePathResolver referencePathResolver;

    private final boolean hasResolvedDanglingTargets;

    /**
     * Creates a context without linker-confirmed reference paths: qualified references are then attributed to their
     * terminal target only. Mainly used by tests.
     *
     * @param options
     *            the run's options
     * @param output
     *            the buffer receiving the serialized syntax
     * @param nameRenderer
     *            the run's contextual name renderer
     * @param modelQueries
     *            the run's model queries
     * @param danglingReferenceIndex
     *            the parser spellings of references unresolved at load time
     */
    public SerializationContext(final SerializationOptions options, final TextOutput output,
            final NameRenderer nameRenderer, final SerializerModelQueries modelQueries,
            final DanglingReferenceIndex danglingReferenceIndex) {
        this(options, output, nameRenderer, modelQueries, danglingReferenceIndex, ReferencePathResolver.NONE);
    }

    /**
     * Creates the context for one run. The dangling-reference index is queried once here: whether it holds resolved
     * targets is fixed for the whole run.
     *
     * @param options
     *            the run's options
     * @param output
     *            the buffer receiving the serialized syntax
     * @param nameRenderer
     *            the run's contextual name renderer
     * @param modelQueries
     *            the run's model queries
     * @param danglingReferenceIndex
     *            the parser spellings of references unresolved at load time
     * @param referencePathResolver
     *            the linker-confirmed targets of qualified-reference segments
     */
    public SerializationContext(final SerializationOptions options, final TextOutput output,
            final NameRenderer nameRenderer, final SerializerModelQueries modelQueries,
            final DanglingReferenceIndex danglingReferenceIndex, final ReferencePathResolver referencePathResolver) {
        this.options = Objects.requireNonNull(options);
        this.output = Objects.requireNonNull(output);
        this.nameRenderer = Objects.requireNonNull(nameRenderer);
        this.modelQueries = Objects.requireNonNull(modelQueries);
        this.danglingReferenceIndex = Objects.requireNonNull(danglingReferenceIndex);
        this.referencePathResolver = Objects.requireNonNull(referencePathResolver);
        this.hasResolvedDanglingTargets = danglingReferenceIndex.hasResolvedTargets();
    }

    /**
     * Returns the serialization mode, formatting and parser-entry options supplied for this run.
     *
     * @return the run's serialization options
     */
    public SerializationOptions options() {
        return this.options;
    }

    /**
     * Returns the shared output receiving syntax and reference metadata from all renderers in this run.
     *
     * @return the run's mutable text or HTML output
     */
    public TextOutput output() {
        return this.output;
    }

    /**
     * Returns the shared name renderer whose active context follows recursive element rendering.
     *
     * @return the run's contextual name renderer
     */
    public NameRenderer nameRenderer() {
        return this.nameRenderer;
    }

    /**
     * Returns the model-query services shared by this run's renderers, including containment, syntax and reference queries.
     *
     * @return the run's model-query collaborators
     */
    public SerializerModelQueries modelQueries() {
        return this.modelQueries;
    }

    /**
     * Returns the retained parser spellings used to preserve unresolved references and detect renamed targets.
     *
     * @return the parser-reference index supplied for this run
     */
    public DanglingReferenceIndex danglingReferenceIndex() {
        return this.danglingReferenceIndex;
    }

    /**
     * Returns the caller's resolver for qualified-name segments, used to confirm alias paths and link their segments.
     *
     * @return the path resolver supplied for this run, defaulting to {@link ReferencePathResolver#NONE}
     */
    public ReferencePathResolver referencePathResolver() {
        return this.referencePathResolver;
    }

    /**
     * Reports whether any retained parser link had a resolved target when this run's context was created. When none
     * did, no reference can have been renamed since parsing, so callers skip the comparatively costly rename detection
     * on serialized reference text.
     *
     * @return {@code true} when at least one retained parser link is resolved
     */
    public boolean hasResolvedDanglingTargets() {
        return this.hasResolvedDanglingTargets;
    }
}
