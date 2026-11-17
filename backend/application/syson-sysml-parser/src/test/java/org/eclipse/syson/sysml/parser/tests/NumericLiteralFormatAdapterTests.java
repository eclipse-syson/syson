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
package org.eclipse.syson.sysml.parser.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.syson.sysml.parser.adapter.NumericLiteralFormatAdapter;
import org.junit.Test;
import org.omg.sysml.lang.sysml.LiteralInteger;
import org.omg.sysml.lang.sysml.LiteralRational;
import org.omg.sysml.lang.sysml.SysMLFactory;

/**
 * Tests the lifecycle of {@link NumericLiteralFormatAdapter}.
 *
 * @author ylussaud
 */
public class NumericLiteralFormatAdapterTests {

    /** Equal value notifications must not erase the spelling captured by the parser. */
    @Test
    public void retainsIntegerFormattingWhenValueIsUnchanged() {
        LiteralInteger literal = SysMLFactory.eINSTANCE.createLiteralInteger();
        literal.setValue(1);
        NumericLiteralFormatAdapter adapter = NumericLiteralFormatAdapter.parse("001");
        literal.eAdapters().add(adapter);

        literal.setValue(1);

        assertThat(literal.eAdapters()).contains(adapter);
        assertThat(adapter.getTarget()).isSameAs(literal);
        assertThat(adapter.formattedLiteral()).isEqualTo("001");
    }

    /** Rational literals keep their decimal and exponent spelling until their value changes. */
    @Test
    public void retainsRationalFormattingWhenValueIsUnchanged() {
        LiteralRational literal = SysMLFactory.eINSTANCE.createLiteralRational();
        literal.setValue(1.0);
        NumericLiteralFormatAdapter adapter = NumericLiteralFormatAdapter.parse("1.00e0");
        literal.eAdapters().add(adapter);

        literal.setValue(1.0);

        assertThat(literal.eAdapters()).contains(adapter);
        assertThat(adapter.getTarget()).isSameAs(literal);
        assertThat(adapter.formattedLiteral()).isEqualTo("1.00e0");
    }

    @Test
    public void removesAdapterWhenIntegerValueChanges() {
        LiteralInteger literal = SysMLFactory.eINSTANCE.createLiteralInteger();
        NumericLiteralFormatAdapter adapter = NumericLiteralFormatAdapter.parse("1");
        literal.eAdapters().add(adapter);

        assertThat(adapter.getTarget()).isSameAs(literal);

        literal.setValue(2);

        assertThat(literal.eAdapters()).doesNotContain(adapter);
        assertThat(adapter.getTarget()).isNull();
    }

    @Test
    public void removesAdapterWhenRationalValueChanges() {
        LiteralRational literal = SysMLFactory.eINSTANCE.createLiteralRational();
        NumericLiteralFormatAdapter adapter = NumericLiteralFormatAdapter.parse("1.0");
        literal.eAdapters().add(adapter);

        assertThat(adapter.getTarget()).isSameAs(literal);

        literal.setValue(2.0);

        assertThat(literal.eAdapters()).doesNotContain(adapter);
        assertThat(adapter.getTarget()).isNull();
    }

}
