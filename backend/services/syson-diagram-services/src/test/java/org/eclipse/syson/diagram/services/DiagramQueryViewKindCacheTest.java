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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.syson.model.services.ModelQueryElementService;
import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipExpose;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests view-kind reuse without changing exposure filtering or resolution fallbacks.
 *
 * @author arichard
 */
public class DiagramQueryViewKindCacheTest {

    /**
     * All supported view kinds produce the same candidates with and without a render cache.
     *
     * @param kind
     *            the view kind under test
     */
    @ParameterizedTest
    @EnumSource(ViewDefinitionKind.class)
    public void supportedKinds(ViewDefinitionKind kind) {
        String qualifiedName = switch (kind) {
            case GENERAL_VIEW -> StandardDiagramsConstants.GV_QN;
            case INTERCONNECTION_VIEW -> StandardDiagramsConstants.IV_QN;
            case ACTION_FLOW_VIEW -> StandardDiagramsConstants.AFV_QN;
            case STATE_TRANSITION_VIEW -> StandardDiagramsConstants.STV_QN;
        };
        var view = this.view(qualifiedName);
        var cache = new DiagramQueryCache();
        var service = new DiagramQueryExposeService(mock(ModelQueryElementService.class));
        var candidates = List.of(SysmlFactory.eINSTANCE.createPackage(), SysmlFactory.eINSTANCE.createActionUsage(),
                SysmlFactory.eINSTANCE.createStateUsage(), SysmlFactory.eINSTANCE.createAttributeUsage());
        var exposes = new BasicEList<Relationship>();
        for (Element candidate : candidates) {
            var membership = mock(Membership.class);
            when(membership.getMemberElement()).thenReturn(candidate);
            var expose = mock(MembershipExpose.class);
            when(expose.getImportedMembership()).thenReturn(membership);
            exposes.add(expose);
        }
        when(view.getOwnedRelationship()).thenReturn(exposes);
        assertThat(cache.getViewDefinitionKind(view, null)).isEqualTo(kind);
        for (Element candidate : candidates) {
            assertThat(service.getExposedElements(view, candidate.eClass(), List.of(), null, null, cache))
                    .containsExactlyElementsOf(service.getExposedElements(view, candidate.eClass(), List.of(), null, null));
        }
        verify(view, times(1 + candidates.size())).getType();
    }

    /**
     * Exposure delegates and ancestor traversal share one view-kind resolution, while other views stay independent.
     */
    @Test
    public void sharedAcrossExposureQueries() {
        var view = this.view(StandardDiagramsConstants.GV_QN);
        var otherView = this.view(StandardDiagramsConstants.IV_QN);
        when(view.getExposedElement()).thenReturn(new BasicEList<>());
        var cache = new DiagramQueryCache();
        var service = new DiagramQueryExposeService(mock(ModelQueryElementService.class));
        var domainType = SysmlPackage.eINSTANCE.getPartUsage();
        assertThat(service.getExposedElements(view, domainType, List.of(), null, null, cache)).isEmpty();
        assertThat(service.getExposedActors(view, domainType, List.of(), null, null, cache)).isEmpty();
        assertThat(service.getExposedStakeholders(view, domainType, List.of(), null, null, cache)).isEmpty();
        assertThat(service.getExposedSubjects(view, domainType, List.of(), null, null, cache)).isEmpty();
        assertThat(service.getExposedElements(SysmlFactory.eINSTANCE.createPackage(), domainType, List.of(view), null, null, cache)).isEmpty();
        assertThat(cache.getViewDefinitionKind(otherView, null)).isEqualTo(ViewDefinitionKind.INTERCONNECTION_VIEW);
        verify(view).getType();
        verify(otherView).getType();
        service.getExposedElements(view, domainType, List.of(), null, null);
        service.getExposedElements(view, domainType, List.of(), null, null);
        verify(view, times(3)).getType();
    }

    /**
     * A new render sees changed view types, including the original untyped and unsupported-type fallbacks.
     */
    @Test
    public void freshKindsAndFallbacks() {
        var view = this.view(StandardDiagramsConstants.GV_QN);
        var types = new BasicEList<Type>();
        when(view.getType()).thenReturn(types);
        var cache = new DiagramQueryCache();
        assertThat(cache.getViewDefinitionKind(view, null)).isEqualTo(ViewDefinitionKind.GENERAL_VIEW);
        var type = mock(Type.class);
        when(type.getQualifiedName()).thenReturn(StandardDiagramsConstants.AFV_QN);
        types.add(type);
        assertThat(cache.getViewDefinitionKind(view, null)).isEqualTo(ViewDefinitionKind.GENERAL_VIEW);
        assertThat(new DiagramQueryCache().getViewDefinitionKind(view, null)).isEqualTo(ViewDefinitionKind.ACTION_FLOW_VIEW);
        when(type.getQualifiedName()).thenReturn("unsupported");
        var unsupportedCache = new DiagramQueryCache();
        assertThat(unsupportedCache.getViewDefinitionKind(view, null)).isNull();
        assertThat(unsupportedCache.getViewDefinitionKind(view, null)).isNull();
        verify(view, times(3)).getType();
        when(view.getType()).thenReturn(null);
        assertThat(new DiagramQueryCache().getViewDefinitionKind(view, null)).isEqualTo(ViewDefinitionKind.GENERAL_VIEW);
    }

    /**
     * Creates a view stub with a type name and no direct exposures.
     *
     * @param qualifiedName
     *            the qualified name of its first type
     * @return the view stub
     */
    private ViewUsage view(String qualifiedName) {
        var view = mock(ViewUsage.class);
        var type = mock(Type.class);
        when(type.getQualifiedName()).thenReturn(qualifiedName);
        when(view.getType()).thenReturn(new BasicEList<>(List.of(type)));
        when(view.getOwnedRelationship()).thenReturn(new BasicEList<>());
        return view;
    }
}
