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
package org.eclipse.syson.sysml.serializer.output;

import java.util.List;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EObject;
import org.omg.sysml.lang.sysml.Element;

/**
 * Text emission API used by serializers.
 *
 * <p>
 * Implementations own whitespace and indentation. Domain serializers should use this API instead of writing directly to
 * a {@link StringBuilder}.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public interface TextOutput {

    /**
     * Starts the text range emitted for the given model element.
     *
     * <p>
     * Implementations that do not collect traceability information may ignore this call. Serializer code should open
     * these scopes at central dispatch points instead of passing elements through every token emission call.
     * </p>
     *
     * @param element
     *            the element whose text starts at the current output offset
     * @return this output
     */
    TextOutput beginElement(Element element);

    /**
     * Ends the text range emitted for the given model element. Scopes close in reverse opening order using the same
     * element instance.
     *
     * @param element
     *            the element whose text ends at the current output offset
     * @return this output
     */
    TextOutput endElement(Element element);

    /**
     * Emits an inline token, preceded by any pending indentation and token separator. Null or empty tokens are ignored
     * so callers can pass optional fragments without guarding them.
     *
     * @param token
     *            the token text, or {@code null}
     * @return this output
     */
    TextOutput token(String token);

    /**
     * Emits the textual name of a model reference.
     *
     * <p>
     * The target is optional because unresolved parser references are not always represented by an EMF proxy.
     * </p>
     *
     * @param name
     *            the already rendered reference name
     * @param target
     *            the resolved or proxy target, or {@code null} for a dangling reference
     * @return this output
     */
    TextOutput referenceName(String name, EObject target);

    /**
     * Emits text containing reference occurrences. Plain text outputs may treat this as one token; structured outputs
     * can use the occurrences to preserve traceability inside otherwise composite text such as an expression.
     *
     * @param text
     *            the composite text to emit
     * @param references
     *            the reference occurrences of {@code text}, in textual order
     * @return this output
     */
    default TextOutput referenceText(final String text,
            final List<Reference> references) {
        return this.token(text);
    }

    /**
     * Emits text whose reference descriptions can be computed on demand. This default evaluates the supplier
     * immediately; implementations that do not expose traceability can override it to avoid that computation.
     *
     * @param text
     *            the text to emit
     * @param references
     *            the reference descriptions required by structured outputs
     * @return this output
     */
    default TextOutput referenceText(final String text,
            final Supplier<List<Reference>> references) {
        return this.referenceText(text, references.get());
    }

    /**
     * Requests a token separator before the next inline token. Requests do not accumulate and are dropped at the start
     * of a line or by a following {@link #newline()}, so callers can ask for a space without tracking the context.
     *
     * @return this output
     */
    TextOutput space();

    /**
     * Ends the current line; the next token is indented at the current block depth.
     *
     * @return this output
     */
    TextOutput newline();

    /**
     * Opens a {@code {}} body: emits the opening brace, ends the line and increases indentation.
     *
     * @return this output
     */
    TextOutput beginBlock();

    /**
     * Closes the body opened by {@link #beginBlock()}, putting the closing brace on its own line at the outer
     * indentation. The line is not terminated, so callers decide what follows the brace.
     *
     * @return this output
     */
    TextOutput endBlock();

    /**
     * Emits text verbatim, bypassing token spacing within it. Used for source fragments that already carry their own
     * layout, such as comment bodies; text ending with the line separator leaves the output at a line start.
     *
     * @param text
     *            the text to copy, or {@code null}
     * @return this output
     */
    TextOutput raw(String text);

    /** Returns the current UTF-16 offset in plain SysML text, without flushing pending whitespace. */
    int mark();

    /**
     * Tells whether text was written after the given {@link #mark()}. Pending separators that were not yet flushed do
     * not count.
     *
     * @param mark
     *            an offset previously returned by {@link #mark()}
     * @return {@code true} if the buffer grew since {@code mark}
     */
    boolean changedSince(int mark);

    /**
     * Terminates the current line only when something was written since {@code mark} and the output does not already
     * end with a line separator, so optional sections can be closed without producing blank lines.
     *
     * @param mark
     *            an offset previously returned by {@link #mark()}
     * @return this output
     */
    TextOutput ensureTrailingNewlineIfChanged(int mark);

    /**
     * Returns the final document: the SysML text itself, or its rendered form for structured outputs such as HTML.
     *
     * @return the emitted content
     */
    String content();

    /** A rendered reference name and its resolved, proxy, or absent target. */
    record Reference(String name, EObject target) {
    }
}
