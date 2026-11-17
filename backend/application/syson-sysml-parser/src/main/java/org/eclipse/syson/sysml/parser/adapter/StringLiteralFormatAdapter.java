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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Immutable implementation of {@link IStringLiteralFormatAdapter}.
 *
 * @author ylussaud
 */
public final class StringLiteralFormatAdapter extends AbstractTextAdapter implements IStringLiteralFormatAdapter {

    private final String literalText;

    private Notifier target;

    /**
     * Creates an adapter for the given source literal.
     *
     * @param literalText
     *            the complete string literal token
     */
    public StringLiteralFormatAdapter(String literalText) {
        this.literalText = literalText;
    }

    @Override
    public String getLiteralText() {
        return this.literalText;
    }

    /**
     * Tells whether the stored token is the canonical representation of the given semantic value.
     *
     * @param value
     *            the semantic string value
     * @return {@code true} if no lexical preservation is needed
     */
    public boolean isCanonicalFor(String value) {
        StringBuilder builder = new StringBuilder("\"");
        if (value != null) {
            for (int index = 0; index < value.length(); index++) {
                builder.append(escaped(value.charAt(index)));
            }
        }
        return this.literalText.equals(builder.append('"').toString());
    }

    private static String escaped(char character) {
        return switch (character) {
            case '\\' -> "\\\\";
            case '"' -> "\\\"";
            case '\n' -> "\\n";
            case '\r' -> "\\r";
            case '\t' -> "\\t";
            default -> Character.toString(character);
        };
    }

    @Override
    public void notifyChanged(Notification notification) {
        if (notification.getNotifier() == this.target
                && notification.getFeature() == SysMLPackage.eINSTANCE.getLiteralString_Value()
                && this.target != null) {
            this.target.eAdapters().remove(this);
        }
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
        return type == IStringLiteralFormatAdapter.class || super.isAdapterForType(type);
    }
}
