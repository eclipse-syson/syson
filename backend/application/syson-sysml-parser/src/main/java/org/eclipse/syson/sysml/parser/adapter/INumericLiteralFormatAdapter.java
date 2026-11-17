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
package org.eclipse.syson.sysml.parser.adapter;

import org.eclipse.emf.common.notify.Adapter;

/**
 * Preserves the lexical numeric format used for a parsed numeric literal.
 *
 * <p>
 * The SysML metamodel stores numeric literal values as Java numbers. That is enough for semantic serialization, but it
 * loses formatting decisions that are visible in source text. This adapter stores those decisions as structured lexical
 * parts instead of keeping an opaque raw token.
 * </p>
 *
 * <p>
 * Examples:
 * </p>
 *
 * <pre>
 * 1E20                    // integer digits "1", exponent marker "E", exponent digits "20"
 * 271828182845904523536.0 // integer digits, decimal point, fractional digits "0"
 * 7.2973525693E-3         // integer digits "7", fractional digits, exponent sign "-"
 * </pre>
 *
 * @author ylussaud
 */
public interface INumericLiteralFormatAdapter extends Adapter {

    /**
     * Gets the digits before the decimal point or exponent marker.
     *
     * @return the integer digits
     */
    String getIntegerDigits();

    /**
     * Tells whether the literal explicitly contained a decimal point.
     *
     * @return <code>true</code> if the literal had a decimal point
     */
    boolean hasDecimalPoint();

    /**
     * Gets the digits after the decimal point.
     *
     * @return the fractional digits, or an empty string
     */
    String getFractionalDigits();

    /**
     * Gets the exponent marker as it appeared in source.
     *
     * @return <code>"E"</code>, <code>"e"</code>, or an empty string
     */
    String getExponentMarker();

    /**
     * Gets the exponent sign as it appeared in source.
     *
     * @return <code>"+"</code>, <code>"-"</code>, or an empty string
     */
    String getExponentSign();

    /**
     * Gets the exponent digits.
     *
     * @return the exponent digits, or an empty string
     */
    String getExponentDigits();

    /**
     * Reconstructs the formatted literal from the stored lexical parts.
     *
     * @return the formatted literal text
     */
    default String formattedLiteral() {
        StringBuilder builder = new StringBuilder(this.getIntegerDigits());
        if (this.hasDecimalPoint()) {
            builder.append('.').append(this.getFractionalDigits());
        }
        if (!this.getExponentMarker().isBlank()) {
            builder.append(this.getExponentMarker()).append(this.getExponentSign()).append(this.getExponentDigits());
        }
        return builder.toString();
    }

}
