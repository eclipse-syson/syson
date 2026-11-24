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

import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.serializer.model.SerializerModelQueries;
import org.eclipse.syson.sysml.serializer.names.NameRenderer;
import org.eclipse.syson.sysml.serializer.output.HtmlTextOutput;
import org.eclipse.syson.sysml.serializer.output.StringTextOutput;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.rendering.SysMLTextRenderer;
import org.omg.sysml.lang.sysml.Element;

/**
 * Serializes OMG SysMLv2 model elements to textual SysML or to a standalone highlighted HTML document.
 *
 * <p>
 * The convenience methods cover the common cases. Anything else (unresolved parser references, contextual naming,
 * confirmed reference paths, HTML links) is described by a {@link SerializationRequest}. Each call creates its own
 * rendering state, so one instance can be shared. A {@code null} root or parse result produces empty output.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SysMLSerializer {

    /**
     * Serializes an element with the default options; use {@link #serialize(ISysMLParseResult)} when the parse result
     * is available, since only it carries the spellings of unresolved references.
     *
     * @param root
     *            the element to serialize, or {@code null}
     * @return the SysML text
     */
    public String serialize(final Element root) {
        return this.serialize(SerializationRequest.forElement(root));
    }

    /**
     * Serializes an element with the given options.
     *
     * @param root
     *            the element to serialize, or {@code null}
     * @param options
     *            the options, or {@code null} for the defaults
     * @return the SysML text
     */
    public String serialize(final Element root, final SerializationOptions options) {
        return this.serialize(SerializationRequest.forElement(root).withOptions(options));
    }

    /**
     * Serializes a parsed model, keeping the source spelling of references the linker could not resolve.
     *
     * @param parseResult
     *            the parser output, or {@code null}
     * @return the SysML text
     */
    public String serialize(final ISysMLParseResult<? extends Element> parseResult) {
        return this.serialize(SerializationRequest.forParseResult(parseResult));
    }

    /**
     * Serializes a parsed model with the given options, keeping the source spelling of unresolved references.
     *
     * @param parseResult
     *            the parser output, or {@code null}
     * @param options
     *            the options, or {@code null} for the defaults
     * @return the SysML text
     */
    public String serialize(final ISysMLParseResult<? extends Element> parseResult, final SerializationOptions options) {
        return this.serialize(SerializationRequest.forParseResult(parseResult).withOptions(options));
    }

    /**
     * Serializes the request to plain text.
     *
     * @param request
     *            what to serialize and how
     * @return the SysML text, empty when the request has no root
     */
    public String serialize(final SerializationRequest request) {
        if (request.root() == null) {
            return "";
        }
        return this.render(request, new StringTextOutput(request.options().formatting()));
    }

    /**
     * Renders an element as a standalone highlighted HTML document with the default options.
     *
     * @param root
     *            the element to serialize, or {@code null}
     * @return the HTML document
     */
    public String serializeToHtml(final Element root) {
        return this.serializeToHtml(SerializationRequest.forElement(root));
    }

    /**
     * Renders a parsed model as a standalone highlighted HTML document, keeping the source spelling of unresolved
     * references.
     *
     * @param parseResult
     *            the parser output, or {@code null}
     * @return the HTML document
     */
    public String serializeToHtml(final ISysMLParseResult<? extends Element> parseResult) {
        return this.serializeToHtml(SerializationRequest.forParseResult(parseResult));
    }

    /**
     * Renders the request as a standalone highlighted HTML document. HTML keeps the configured formatting: captured
     * source whitespace is never replayed.
     *
     * @param request
     *            what to serialize and how
     * @return the HTML document, empty when the request has no root
     */
    public String serializeToHtml(final SerializationRequest request) {
        if (request.root() == null) {
            return "";
        }
        final HtmlReferenceLinkResolver linkResolver = request.htmlLinkResolver().orElse(null);
        return this.render(request, new HtmlTextOutput(request.options().formatting(), linkResolver));
    }

    /**
     * Builds the per-call rendering state and writes the request root into the given output.
     */
    private String render(final SerializationRequest request, final TextOutput output) {
        final SerializationOptions options = request.options();
        final NameRenderer nameRenderer = request.contextualNameResolver()
                .map(NameRenderer::new)
                .orElseGet(NameRenderer::new);
        // Without contextual naming, the model queries still need a resolver; an empty name means "no opinion".
        final ReferenceNameResolver modelQueryNameResolver = request.contextualNameResolver()
                .orElse((target, context) -> "");
        final SerializerModelQueries modelQueries = new SerializerModelQueries(options.mode() == SerializationMode.ROUND_TRIP,
                modelQueryNameResolver, request.referencePathResolver());
        final SerializationContext context = new SerializationContext(options, output, nameRenderer, modelQueries,
                request.danglingReferences(), request.referencePathResolver());
        new SysMLTextRenderer(context).serialize(request.root());
        return output.content();
    }
}
