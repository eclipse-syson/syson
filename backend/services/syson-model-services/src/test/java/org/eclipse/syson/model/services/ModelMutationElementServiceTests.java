/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.model.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelElementQueryService;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link ModelMutationElementService}.
 *
 * @author Arthur Daussy
 */
public class ModelMutationElementServiceTests {

    private final ModelBuilder modelBuilder = new ModelBuilder();

    private final ModelMutationElementService modelMutationElementService = new ModelMutationElementService();

    private final MetamodelElementQueryService metamodelElementQueryService = new MetamodelElementQueryService();


    @DisplayName("GIVEN a PartUsage, WHEN using createPartUsageAndBindingConnectorAsUsage method, THEN all required elements should be created")
    @Test
    public void checkCreatePartUsageAndFlowConnection() {
        Package root = this.modelBuilder.createWithName(Package.class, "RootPackage");

        PartUsage sourcePart = this.modelBuilder.createInWithName(PartUsage.class, root, "part2");

        this.modelMutationElementService.createPartUsageAndBindingConnectorAsUsage(sourcePart);

        assertThat(sourcePart.getOwnedFeature())
                .hasSize(1)
                .allMatch(PortUsage.class::isInstance);

        PortUsage newPort = (PortUsage) sourcePart.getOwnedFeature().get(0);

        List<PartUsage> otherParts = root.getOwnedElement().stream()
                .filter(PartUsage.class::isInstance)
                .map(PartUsage.class::cast)
                .filter(partUsage -> partUsage != sourcePart)
                .toList();

        assertThat(otherParts)
                .hasSize(1)
                .allMatch(partUsage -> !partUsage.getOwnedFeature().isEmpty() && partUsage.getOwnedFeature().get(0) instanceof PortUsage);

        PortUsage targetPort = (PortUsage) otherParts.get(0).getOwnedFeature().get(0);


        List<BindingConnectorAsUsage> bindings = root.getOwnedElement().stream()
                .filter(BindingConnectorAsUsage.class::isInstance)
                .map(BindingConnectorAsUsage.class::cast)
                .toList();

        assertThat(bindings)
                .hasSize(1)
                .allMatch(bindingConnectorAsUsage -> this.metamodelElementQueryService.getSource(bindingConnectorAsUsage) == newPort)
                .allMatch(bindingConnectorAsUsage -> this.metamodelElementQueryService.getTarget(bindingConnectorAsUsage).get(0) == targetPort);


    }
}
