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
package org.eclipse.syson.standard.diagrams.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.sirius.components.diagrams.description.DiagramDescription;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.syson.diagram.services.DiagramQueryCache;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.Test;

/**
 * Tests cache registration and lifetime specifically for standard SysON diagram renders.
 *
 * @author arichard
 */
public class SDVDiagramQueryCacheCustomizerTest {

    private static final String EXISTING_VARIABLE = "existing";

    /**
     * Each standard-diagram render receives a fresh cache, even when reusing its variable manager.
     */
    @Test
    public void renderLifetime() {
        var customizer = new SDVDiagramQueryCacheCustomizer();
        var description = mock(DiagramDescription.class);
        when(description.getId()).thenReturn(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var variables = new VariableManager();
        var existingValue = new Object();
        variables.put(EXISTING_VARIABLE, existingValue);

        assertThat(customizer.customize(description, variables)).isSameAs(variables);
        var first = variables.get(DiagramQueryCache.VARIABLE_NAME, DiagramQueryCache.class).orElseThrow();
        customizer.customize(description, variables);
        assertThat(variables.get(DiagramQueryCache.VARIABLE_NAME, DiagramQueryCache.class).orElseThrow()).isNotSameAs(first);
        assertThat(variables.get(EXISTING_VARIABLE, Object.class)).containsSame(existingValue);

        var otherVariables = new VariableManager();
        customizer.customize(description, otherVariables);
        var otherCache = otherVariables.get(DiagramQueryCache.VARIABLE_NAME, DiagramQueryCache.class).orElseThrow();
        assertThat(otherCache).isNotSameAs(first).isNotSameAs(variables.get(DiagramQueryCache.VARIABLE_NAME, DiagramQueryCache.class).orElseThrow());
    }

    /**
     * Other representations receive no cache and retain their existing variables.
     */
    @Test
    public void otherRepresentationsRemainUnchanged() {
        var customizer = new SDVDiagramQueryCacheCustomizer();
        var description = mock(DiagramDescription.class);
        when(description.getId()).thenReturn("another-diagram");
        var variables = new VariableManager();
        var existingValue = new Object();
        variables.put(EXISTING_VARIABLE, existingValue);

        assertThat(customizer.customize(description, variables)).isSameAs(variables);
        assertThat(variables.get(DiagramQueryCache.VARIABLE_NAME, DiagramQueryCache.class)).isEmpty();
        assertThat(variables.get(EXISTING_VARIABLE, Object.class)).containsSame(existingValue);
    }
}
