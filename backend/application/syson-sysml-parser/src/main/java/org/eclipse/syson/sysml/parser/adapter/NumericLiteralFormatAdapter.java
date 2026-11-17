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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Immutable implementation of {@link INumericLiteralFormatAdapter}.
 *
 * @author ylussaud
 */
public class NumericLiteralFormatAdapter extends AbstractTextAdapter implements INumericLiteralFormatAdapter {

    private static final Pattern NUMERIC_LITERAL = Pattern.compile(
            "(?:(?<integer>[0-9]+)(?<decimal>\\.(?<fraction>[0-9]*))?|(?<decimalOnly>\\.(?<fractionOnly>[0-9]+)))"
                    + "(?:(?<marker>[eE])(?<sign>[+-]?)(?<exponent>[0-9]+))?");

    private final String integerDigits;

    private final boolean decimalPoint;

    private final String fractionalDigits;

    private final String exponentMarker;

    private final String exponentSign;

    private final String exponentDigits;

    private Notifier target;

    /**
     * Creates the adapter.
     *
     * @param integerDigits
     *            the digits before the decimal point or exponent marker
     * @param decimalPoint
     *            whether the source literal contained a decimal point
     * @param fractionalDigits
     *            the digits after the decimal point
     * @param exponentMarker
     *            the exponent marker
     * @param exponentSign
     *            the exponent sign
     * @param exponentDigits
     *            the exponent digits
     */
    public NumericLiteralFormatAdapter(String integerDigits, boolean decimalPoint, String fractionalDigits,
            String exponentMarker, String exponentSign, String exponentDigits) {
        this.integerDigits = integerDigits;
        this.decimalPoint = decimalPoint;
        this.fractionalDigits = fractionalDigits;
        this.exponentMarker = exponentMarker;
        this.exponentSign = exponentSign;
        this.exponentDigits = exponentDigits;
    }

    /**
     * Parses a numeric literal token into structured formatting parts.
     *
     * @param literalText
     *            the numeric literal token text
     * @return the adapter
     */
    public static NumericLiteralFormatAdapter parse(String literalText) {
        Matcher matcher = NUMERIC_LITERAL.matcher(literalText);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Unsupported numeric literal format: " + literalText);
        }
        return new NumericLiteralFormatAdapter(nullToEmpty(matcher.group("integer")),
                matcher.group("decimal") != null || matcher.group("decimalOnly") != null,
                firstNonNull(matcher.group("fraction"), matcher.group("fractionOnly")),
                nullToEmpty(matcher.group("marker")),
                nullToEmpty(matcher.group("sign")), nullToEmpty(matcher.group("exponent")));
    }

    private static String nullToEmpty(String value) {
        String result = value;
        if (result == null) {
            result = "";
        }
        return result;
    }

    private static String firstNonNull(String first, String second) {
        String result = first;
        if (result == null) {
            result = nullToEmpty(second);
        }
        return result;
    }

    @Override
    public String getIntegerDigits() {
        return this.integerDigits;
    }

    @Override
    public boolean hasDecimalPoint() {
        return this.decimalPoint;
    }

    @Override
    public String getFractionalDigits() {
        return this.fractionalDigits;
    }

    @Override
    public String getExponentMarker() {
        return this.exponentMarker;
    }

    @Override
    public String getExponentSign() {
        return this.exponentSign;
    }

    @Override
    public String getExponentDigits() {
        return this.exponentDigits;
    }

    @Override
    public void notifyChanged(Notification notification) {
        if (notification.getNotifier() == this.target && this.isNumericValueFeature(notification.getFeature())
                && this.target != null
                && !Objects.equals(notification.getOldValue(),
                        notification.getNewValue())) {
            this.target.eAdapters().remove(this);
        }
    }

    private boolean isNumericValueFeature(Object feature) {
        return feature == SysMLPackage.eINSTANCE.getLiteralInteger_Value()
                || feature == SysMLPackage.eINSTANCE.getLiteralRational_Value();
    }

    @Override
    public Notifier getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(Notifier newTarget) {
        this.target = newTarget;
    }

    @Override
    public boolean isAdapterForType(Object type) {
        return type == INumericLiteralFormatAdapter.class || super.isAdapterForType(type);
    }

}
