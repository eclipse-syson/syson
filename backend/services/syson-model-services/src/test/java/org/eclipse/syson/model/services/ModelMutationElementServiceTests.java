/*******************************************************************************
 * Copyright (c) 2025, 2026  Obeo.
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
import java.util.Optional;

import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
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

    private final MetamodelQueryElementService metamodelElementQueryService = new MetamodelQueryElementService();

    @Test
    @DisplayName("GIVEN a element with a name, WHEN duplicating this element, THEN the name and id of the duplicated element should be modified")
    public void checkElementWithNameDuplication() {
        Package package1 = this.modelBuilder.createWithName(Package.class, "package1");
        String package1Id = package1.getElementId();
        Optional<Element> result = this.modelMutationElementService.duplicateElement(package1, false, false);

        assertThat(result)
                .isPresent()
                .as("The copied package should have a modified name")
                .matches(element -> element.get().getName().equals("package1-copy"))
                .as("The copied package should have a modified id")
                .matches(element -> !element.get().getElementId().equals(package1Id));
    }

    @Test
    @DisplayName("GIVEN a element with a shortname only, WHEN duplicating this element, THEN the shortname and id of the duplicated element should be modified")
    public void checkElementWithShortnameDuplication() {
        Package package1 = this.modelBuilder.create(Package.class);
        package1.setDeclaredShortName("pk1");
        String package1Id = package1.getElementId();
        Optional<Element> result = this.modelMutationElementService.duplicateElement(package1, false, false);

        assertThat(result)
                .isPresent()
                .matches(element -> element.get().getShortName().equals("pk1-copy") && !element.get().getElementId().equals(package1Id));
    }

    @Test
    @DisplayName("GIVEN a element with content and outgoing references, WHEN duplicating this element with its content, THEN the content is also duplicated and the outgoing references are kept")
    public void checkElementWithContentDuplication() {
        Package package0 = this.modelBuilder.createWithName(Package.class, "packageO");
        Package package1 = this.modelBuilder.createWithName(Package.class, "package1");

        NamespaceImport importNamespace = SysmlFactory.eINSTANCE.createNamespaceImport();
        importNamespace.setImportedNamespace(package0);
        package1.getOwnedRelationship().add(importNamespace);

        PartDefinition pd1 = this.modelBuilder.createInWithName(PartDefinition.class, package1, "PD1");
        PartUsage p1 = this.modelBuilder.createInWithName(PartUsage.class, package1, "p1");

        this.modelBuilder.setType(p1, pd1);
        Optional<Element> result = this.modelMutationElementService.duplicateElement(package1, true, true);

        assertThat(result)
                .isPresent()
                .matches(element -> element.get() instanceof Package);

        Package copiedPackage = (Package) result.get();
        assertThat(copiedPackage.getOwnedRelationship()).hasSize(3);

        assertThat(copiedPackage.getOwnedRelationship())
                .first()
                .isInstanceOf(NamespaceImport.class)
                .as("The copied import still point the Package0")
                .matches(namespaceImport -> ((NamespaceImport) namespaceImport).getImportedNamespace() == package0);

        assertThat(copiedPackage.getOwnedElement()).hasSize(2);

        Element copiedPartDef = copiedPackage.getOwnedElement().get(0);
        assertThat(copiedPartDef)
                .isInstanceOf(PartDefinition.class)
                .as("The copied PartDefinition has the same name")
                .matches(partDef -> partDef.getName().equals("PD1"))
                .as("The copied PartDefinition has not the same ID")
                .matches(partDef -> !partDef.getElementId().equals(pd1.getElementId()));

        Element copiedPart = copiedPackage.getOwnedElement().get(1);
        assertThat(copiedPart)
                .isInstanceOf(PartUsage.class)
                // Note that the name of the children does not change but their id does
                .as("The copied PartUsage still have the same name")
                .matches(partUsage -> partUsage.getName().equals("p1"))
                .as("The copied part usage does not have the same id")
                .matches(partUsage -> !partUsage.getElementId().equals(p1.getElementId()))
                .as("The copied part usage still point to PD1")
                .matches(partUsage -> ((PartUsage) partUsage).getType().contains(copiedPartDef));
    }

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
