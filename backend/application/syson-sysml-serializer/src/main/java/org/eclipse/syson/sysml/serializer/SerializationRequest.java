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
import java.util.Optional;

import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.serializer.model.DanglingReferenceIndex;
import org.omg.sysml.lang.sysml.Element;

/**
 * Everything one serialization call needs besides the serializer itself.
 *
 * <p>
 * The request gathers the optional collaborators in one place so that {@link SysMLSerializer} can expose a few
 * intent-revealing entry points instead of one overload per combination. Each {@code with...} method returns a copy,
 * which lets a caller describe only what differs from the defaults.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SerializationRequest {

    private final Element root;

    private final SerializationOptions options;

    private final DanglingReferenceIndex danglingReferences;

    private final ReferenceNameResolver contextualNameResolver;

    private final ReferencePathResolver referencePathResolver;

    private final HtmlReferenceLinkResolver htmlLinkResolver;

    private SerializationRequest(final Element root, final SerializationOptions options,
            final DanglingReferenceIndex danglingReferences, final ReferenceNameResolver contextualNameResolver,
            final ReferencePathResolver referencePathResolver, final HtmlReferenceLinkResolver htmlLinkResolver) {
        this.root = root;
        this.options = Objects.requireNonNullElseGet(options, SerializationOptions::defaults);
        this.danglingReferences = Objects.requireNonNullElseGet(danglingReferences, DanglingReferenceIndex::empty);
        this.contextualNameResolver = contextualNameResolver;
        this.referencePathResolver = Objects.requireNonNullElse(referencePathResolver, ReferencePathResolver.NONE);
        this.htmlLinkResolver = htmlLinkResolver;
    }

    /**
     * Starts a request for a model element that has no parser context, such as an element edited in SysON.
     *
     * @param root
     *            the element to serialize, or {@code null} to produce empty output
     * @return a request using the default options and no unresolved parser references
     */
    public static SerializationRequest forElement(final Element root) {
        return new SerializationRequest(root, null, null, null, null, null);
    }

    /**
     * Starts a request for a freshly parsed model. Prefer this over {@link #forElement(Element)} when the parse result
     * is available: its dangling links are the only record of references the linker could not resolve.
     *
     * @param parseResult
     *            the parser output, or {@code null} to produce empty output
     * @return a request carrying the parsed root and its unresolved references
     */
    public static SerializationRequest forParseResult(final ISysMLParseResult<? extends Element> parseResult) {
        if (parseResult == null) {
            return forElement(null);
        }
        return forElement(parseResult.model()).withDanglingReferences(DanglingReferenceIndex.from(parseResult.danglingLinks()));
    }

    /**
     * Selects the syntax-preservation mode and formatting.
     *
     * @param newOptions
     *            the options, or {@code null} for {@link SerializationOptions#defaults()}
     * @return a copy of this request using these options
     */
    public SerializationRequest withOptions(final SerializationOptions newOptions) {
        return new SerializationRequest(this.root, newOptions, this.danglingReferences, this.contextualNameResolver,
                this.referencePathResolver, this.htmlLinkResolver);
    }

    /**
     * Supplies the parser spellings of references that were unresolved at load time, so that they are not lost from
     * the output.
     *
     * @param newDanglingReferences
     *            the retained parser links, or {@code null} for none
     * @return a copy of this request using these references
     */
    public SerializationRequest withDanglingReferences(final DanglingReferenceIndex newDanglingReferences) {
        return new SerializationRequest(this.root, this.options, newDanglingReferences, this.contextualNameResolver,
                this.referencePathResolver, this.htmlLinkResolver);
    }

    /**
     * Lets the caller decide how a referenced element is named from the place it is referenced, typically to produce
     * the shortest name that the importing namespaces make visible. Without it, references use the serializer's own
     * naming rules.
     *
     * @param resolver
     *            the contextual name resolver
     * @return a copy of this request using this resolver
     */
    public SerializationRequest withContextualNames(final ReferenceNameResolver resolver) {
        return new SerializationRequest(this.root, this.options, this.danglingReferences, Objects.requireNonNull(resolver),
                this.referencePathResolver, this.htmlLinkResolver);
    }

    /**
     * Lets the caller confirm the element behind each segment of a qualified reference, so that round-trip output can
     * keep a linker-validated spelling such as an alias path.
     *
     * @param resolver
     *            the reference path resolver
     * @return a copy of this request using this resolver
     */
    public SerializationRequest withReferencePaths(final ReferencePathResolver resolver) {
        return new SerializationRequest(this.root, this.options, this.danglingReferences, this.contextualNameResolver,
                Objects.requireNonNull(resolver), this.htmlLinkResolver);
    }

    /**
     * Makes references navigable in HTML output. Plain-text serialization ignores it.
     *
     * @param resolver
     *            the link resolver
     * @return a copy of this request using this resolver
     */
    public SerializationRequest withHtmlLinks(final HtmlReferenceLinkResolver resolver) {
        return new SerializationRequest(this.root, this.options, this.danglingReferences, this.contextualNameResolver,
                this.referencePathResolver, Objects.requireNonNull(resolver));
    }

    /**
     * Returns the element to serialize; {@code null} means the serializer returns empty output.
     *
     * @return the root element, possibly {@code null}
     */
    public Element root() {
        return this.root;
    }

    /**
     * Returns the effective options, never {@code null}.
     *
     * @return the options
     */
    public SerializationOptions options() {
        return this.options;
    }

    /**
     * Returns the retained parser references, never {@code null}.
     *
     * @return the dangling reference index
     */
    public DanglingReferenceIndex danglingReferences() {
        return this.danglingReferences;
    }

    /**
     * Returns the caller's contextual naming, when it replaces the serializer's own naming rules.
     *
     * @return the contextual name resolver, if any
     */
    public Optional<ReferenceNameResolver> contextualNameResolver() {
        return Optional.ofNullable(this.contextualNameResolver);
    }

    /**
     * Returns the reference path resolver; by default no path is confirmed.
     *
     * @return the reference path resolver, never {@code null}
     */
    public ReferencePathResolver referencePathResolver() {
        return this.referencePathResolver;
    }

    /**
     * Returns the HTML link resolver, when references should be navigable.
     *
     * @return the HTML link resolver, if any
     */
    public Optional<HtmlReferenceLinkResolver> htmlLinkResolver() {
        return Optional.ofNullable(this.htmlLinkResolver);
    }
}
