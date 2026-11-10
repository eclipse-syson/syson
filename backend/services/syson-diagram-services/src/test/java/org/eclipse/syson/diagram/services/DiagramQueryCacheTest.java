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
package org.eclipse.syson.diagram.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests immutable inheritance snapshots and their reuse within a diagram render.
 *
 * @author arichard
 */
public class DiagramQueryCacheTest {

    /**
     * The cache snapshots each type once, and another render sees subsequent model changes.
     */
    @Test
    public void immutableSnapshots() {
        var type = mock(Type.class);
        var first = SysmlFactory.eINSTANCE.createAttributeUsage();
        var second = SysmlFactory.eINSTANCE.createAttributeUsage();
        var inherited = new BasicEList<Feature>(List.of(first));
        when(type.getInheritedFeature()).thenReturn(inherited);
        var cache = new DiagramQueryCache();
        var snapshot = cache.getInheritedFeatures(type);
        inherited.add(second);
        assertThat(cache.getInheritedFeatures(type)).containsExactly(first);
        assertThatThrownBy(() -> snapshot.add(second)).isInstanceOf(UnsupportedOperationException.class);
        verify(type).getInheritedFeature();
        assertThat(new DiagramQueryCache().getInheritedFeatures(type)).containsExactly(first, second);
        verify(type, times(2)).getInheritedFeature();
    }

    /**
     * Compartment filtering neither recomputes inheritance nor mutates the shared feature list.
     */
    @Test
    public void independentCompartments() {
        var options = new ShowDiagramsInheritedMembersService();
        options.setShowInheritedMembers(true);
        var service = new DiagramQueryToolService(mock(IObjectSearchService.class), options);
        var type = mock(Type.class);
        var attribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        var part = SysmlFactory.eINSTANCE.createPartUsage();
        when(type.eClass()).thenReturn(SysmlPackage.eINSTANCE.getPartDefinition());
        when(type.getOwnedFeature()).thenReturn(new BasicEList<>());
        when(type.getInheritedFeature()).thenReturn(new BasicEList<>(List.of(attribute, part)));
        var cache = new DiagramQueryCache();

        var parts = service.getInheritedCompartmentItems(type, "ownedPart", cache);
        assertThat(parts).containsExactly(part);
        parts.clear();
        assertThat(service.getInheritedCompartmentItems(type, "ownedAttribute", cache)).containsExactly(attribute);
        assertThat(service.getInheritedCompartmentItems(type, "ownedPart", cache)).containsExactly(part);
        assertThat(cache.getInheritedFeatures(type)).containsExactly(attribute, part);
        verify(type).getInheritedFeature();
    }

    /**
     * All display-option combinations retain their filtering and the disabled fast path avoids computing inheritance.
     *
     * @param ordinary whether ordinary inherited features are displayed
     * @param standard whether standard-library inherited features are displayed
     * @param expectedCount the expected number of candidates
     */
    @ParameterizedTest
    @CsvSource({"false, false, 0", "true, false, 1", "false, true, 1", "true, true, 2"})
    public void displayOptions(boolean ordinary, boolean standard, int expectedCount) {
        var builder = new ModelBuilder();
        var library = SysmlFactory.eINSTANCE.createLibraryPackage();
        library.setIsStandard(true);
        var libraryType = builder.createInWithName(PartDefinition.class, library, "LibraryType");
        var standardAttribute = builder.createInWithName(AttributeUsage.class, libraryType, "standard");
        var ordinaryAttribute = builder.createWithName(AttributeUsage.class, "ordinary");
        var type = mock(Type.class);
        when(type.eClass()).thenReturn(SysmlPackage.eINSTANCE.getPartDefinition());
        when(type.getOwnedFeature()).thenReturn(new BasicEList<>());
        when(type.getInheritedFeature()).thenReturn(new BasicEList<>(List.of(ordinaryAttribute, standardAttribute)));
        var options = new ShowDiagramsInheritedMembersService();
        options.setShowInheritedMembers(ordinary);
        options.setShowInheritedMembersFromStandardLibraries(standard);
        var service = new DiagramQueryToolService(mock(IObjectSearchService.class), options);

        var candidates = service.getInheritedCompartmentItems(type, "ownedAttribute", new DiagramQueryCache());
        assertThat(candidates).hasSize(expectedCount);
        assertThat(candidates.contains(ordinaryAttribute)).isEqualTo(ordinary);
        assertThat(candidates.contains(standardAttribute)).isEqualTo(standard);
        if (!ordinary && !standard) {
            verify(type, never()).getInheritedFeature();
        }
    }

    /**
     * Redefinitions made between renders invalidate results by creating a new cache, without changing old snapshots.
     */
    @Test
    public void redefinitionBetweenRenders() {
        var builder = new ModelBuilder();
        var base = builder.createWithName(PartDefinition.class, "Base");
        var original = builder.createInWithName(AttributeUsage.class, base, "x");
        var derived = builder.createWithName(PartDefinition.class, "Derived");
        builder.addSubclassification(derived, base);
        var cache = new DiagramQueryCache();
        assertThat(cache.getInheritedFeatures(derived)).containsExactly(original);
        var replacement = builder.createInWithName(AttributeUsage.class, derived, "replacement");
        builder.addRedefinition(replacement, original);
        assertThat(cache.getInheritedFeatures(derived)).containsExactly(original);
        assertThat(new DiagramQueryCache().getInheritedFeatures(derived)).isEmpty();
    }
}
