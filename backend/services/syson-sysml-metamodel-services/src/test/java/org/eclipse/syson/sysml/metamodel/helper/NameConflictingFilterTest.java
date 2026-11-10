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
package org.eclipse.syson.sysml.metamodel.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Checks that the services and legacy name filters preserve the same membership semantics.
 *
 * @author arichard
 */
@SuppressWarnings("deprecation")
public class NameConflictingFilterTest {

    /**
     * Supplies a fresh instance of each name-filter implementation.
     *
     * @return the filters to exercise
     */
    private static Stream<Predicate<Membership>> filters() {
        return Stream.of(new NameConflictingFilter(), new org.eclipse.syson.sysml.helper.NameConflictingFilter());
    }

    /**
     * Aliases conflict independently of the member's own name and are read only once.
     *
     * @param filter
     *            the filter to check
     */
    @ParameterizedTest
    @MethodSource("filters")
    public void aliases(Predicate<Membership> filter) {
        var feature = SysmlFactory.eINSTANCE.createFeature();
        feature.setDeclaredName("original");
        var member = mock(Membership.class);
        when(member.getMemberName()).thenReturn("alias");
        when(member.getMemberElement()).thenReturn(feature);
        assertThat(filter.test(member)).isTrue();
        verify(member).getMemberName();
        verify(member).getMemberElement();
        assertThat(filter.test(member)).isFalse();
        when(member.getMemberName()).thenReturn(feature.getName());
        assertThat(filter.test(member)).isTrue();
    }

    /**
     * Effective names and short names continue to share the same conflict set.
     *
     * @param filter
     *            the filter to check
     */
    @ParameterizedTest
    @MethodSource("filters")
    public void effectiveNames(Predicate<Membership> filter) {
        var feature = SysmlFactory.eINSTANCE.createFeature();
        feature.setDeclaredName("name");
        feature.setDeclaredShortName("short");
        var member = mock(Membership.class);
        when(member.getMemberElement()).thenReturn(feature);
        assertThat(filter.test(member)).isTrue();
        feature.setDeclaredName("other");
        assertThat(filter.test(member)).isFalse();
        feature.setDeclaredName("short");
        feature.setDeclaredShortName(null);
        assertThat(filter.test(member)).isFalse();
    }

    /**
     * Equal alias text in a distinct String retains the existing reference-comparison behavior.
     *
     * @param filter
     *            the filter to check
     */
    @ParameterizedTest
    @MethodSource("filters")
    public void equalAliasText(Predicate<Membership> filter) {
        var feature = SysmlFactory.eINSTANCE.createFeature();
        feature.setDeclaredName("name");
        feature.setDeclaredShortName("short");
        var member = mock(Membership.class);
        when(member.getMemberName()).thenReturn(new String(feature.getName()));
        when(member.getMemberElement()).thenReturn(feature);
        assertThat(filter.test(member)).isTrue();
        feature.setDeclaredName("short");
        feature.setDeclaredShortName(null);
        when(member.getMemberName()).thenReturn(null);
        assertThat(filter.test(member)).isTrue();
    }

    /**
     * Missing memberships, members and names do not introduce conflicts.
     *
     * @param filter
     *            the filter to check
     */
    @ParameterizedTest
    @MethodSource("filters")
    public void unnamedMemberships(Predicate<Membership> filter) {
        assertThat(filter.test(null)).isTrue();
        var member = mock(Membership.class);
        assertThat(filter.test(member)).isTrue();
        when(member.getMemberName()).thenReturn("unused");
        assertThat(filter.test(member)).isTrue();
        when(member.getMemberName()).thenReturn(null);
        when(member.getMemberElement()).thenReturn(SysmlFactory.eINSTANCE.createFeature());
        assertThat(filter.test(member)).isTrue();
        assertThat(filter.test(member)).isTrue();
    }
}
