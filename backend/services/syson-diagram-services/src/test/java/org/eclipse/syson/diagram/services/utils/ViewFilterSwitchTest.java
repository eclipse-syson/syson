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
package org.eclipse.syson.diagram.services.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests the containment rules used to display exposed elements.
 *
 * @author arichard
 */
public class ViewFilterSwitchTest {

    /**
     * An attribute in a nested package is displayed only in its own package in a General View.
     *
     * @param kind
     *            the kind of standard view
     */
    @ParameterizedTest
    @EnumSource(ViewDefinitionKind.class)
    public void attributeInNestedPackage(ViewDefinitionKind kind) {
        var builder = new ModelBuilder();
        var packageA = builder.create(Package.class);
        var packageB = builder.createIn(Package.class, packageA);
        var attribute = builder.createIn(AttributeUsage.class, packageB);
        List<Element> exposedElements = List.of(packageA, packageB, attribute);
        var queryService = new MetamodelQueryElementService();

        assertThat(new ViewFilterSwitch(kind, exposedElements, packageA, queryService).doSwitch(attribute)).isFalse();
        assertThat(new ViewFilterSwitch(kind, exposedElements, packageB, queryService).doSwitch(attribute))
                .isEqualTo(ViewDefinitionKind.isGeneralView(kind));
    }

    /**
     * Check attribute eligibility independently at the diagram root, inside its owning part, and inside the part's
     * package. The package permits this tree representation because no nested package separates it from the attribute.
     * These checks do not imply that all three representations are displayed simultaneously.
     */
    @Test
    public void attributeInPartRemainsEligibleInSupportedGraphicalContexts() {
        var builder = new ModelBuilder();
        var pkg = builder.create(Package.class);
        var part = builder.createIn(PartUsage.class, pkg);
        var attribute = builder.createIn(AttributeUsage.class, part);
        List<Element> exposedElements = List.of(pkg, part, attribute);
        var queryService = new MetamodelQueryElementService();

        assertThat(new ViewFilterSwitch(ViewDefinitionKind.GENERAL_VIEW, exposedElements, null, queryService).doSwitch(attribute)).isTrue();
        assertThat(new ViewFilterSwitch(ViewDefinitionKind.GENERAL_VIEW, exposedElements, pkg, queryService).doSwitch(attribute)).isTrue();
        assertThat(new ViewFilterSwitch(ViewDefinitionKind.GENERAL_VIEW, exposedElements, part, queryService).doSwitch(attribute)).isTrue();
    }
}
