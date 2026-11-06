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
package org.eclipse.syson.sysml.metamodel.services.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests typed element lookup in {@link SysMLTestModel}.
 *
 * @author Arthur Daussy
 */
public class SysMLTestModelTest {

    private static final String ATTRIBUTE_NAME = "DirectFeatures::Context::a";

    /**
     * Qualified lookup preserves element identity across compatible types and reports missing or mistyped targets.
     */
    @Test
    @DisplayName("GIVEN a model accessor, WHEN looking up a qualified name, THEN the expected type is checked and lookup failures identify the target")
    public void qualifiedModelLookup() throws IOException {
        // See /feature-chains/direct-features.xmi for the textual SysML model in an XML comment.
        var resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        var resource = new XMIResourceImpl(URI.createURI("test:/direct-features.xmi"));
        resourceSet.getResources().add(resource);
        try (var input = this.getClass().getResourceAsStream("/feature-chains/direct-features.xmi")) {
            assertThat(input).isNotNull();
            resource.load(input, Map.of());
        }
        assertThat(resource.getErrors()).isEmpty();
        var model = new SysMLTestModel(resource);

        // Compatible requested types must return the same contained object.
        assertThat(model.get(AttributeUsage.class, ATTRIBUTE_NAME)).isSameAs(model.get(Feature.class, ATTRIBUTE_NAME));
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> model.get(PartDefinition.class, ATTRIBUTE_NAME))
                .withMessageContaining("PartDefinition")
                .withMessageContaining(ATTRIBUTE_NAME);
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> model.get(Feature.class, "DirectFeatures::Context::missing"))
                .withMessageContaining("DirectFeatures::Context::missing");
    }
}
