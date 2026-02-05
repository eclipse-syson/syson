/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.model.services.aql;

import java.util.Objects;

import org.eclipse.syson.model.services.ModelMutationElementService;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;

/**
 * Entry point for all model-related services doing mutations in models and called by AQL expressions in representation
 * descriptions.
 *
 * @author arichard
 */
public class ModelMutationAQLService {

    private final ModelMutationElementService modelMutationElementService;

    private final MetamodelMutationElementService metamodelElementMutationService;

    public ModelMutationAQLService(ModelMutationElementService modelMutationElementService) {
        this.modelMutationElementService = Objects.requireNonNull(modelMutationElementService);
        this.metamodelElementMutationService = new MetamodelMutationElementService();
    }

    /**
     * {@link MetamodelMutationElementService#createObjectiveDocumentation(Element, String)}.
     */
    public Documentation createObjectiveDocumentation(Element element, String referenceName) {
        return this.metamodelElementMutationService.createObjectiveDocumentation(element, referenceName);
    }

    /**
     * {@link MetamodelMutationElementService#createMembership(Element)}.
     */
    public Membership createMembership(Element element) {
        return this.metamodelElementMutationService.createMembership(element);
    }

    /**
     * {@link ModelMutationElementService#createPartUsageAndInterface(PartUsage)}.
     */
    public Element createPartUsageAndInterface(PartUsage self) {
        return this.modelMutationElementService.createPartUsageAndInterface(self);
    }

    /**
     * {@link ModelMutationElementService#createPartUsageAndBindingConnectorAsUsage(PartUsage)}.
     */
    public Element createPartUsageAndBindingConnectorAsUsage(PartUsage self) {
        return this.modelMutationElementService.createPartUsageAndBindingConnectorAsUsage(self);
    }

    /**
     * {@link ModelMutationElementService#createPartUsageAndFlowConnection(PartUsage)}.
     */
    public Element createPartUsageAndFlowConnection(PartUsage self) {
        return this.modelMutationElementService.createPartUsageAndFlowConnection(self);
    }

    /**
     * {@link ModelMutationElementService#createPartUsageAndFlowConnection(PartUsage)}.
     */
    public Element createSatisfy(Element element, RequirementUsage existingRequirement) {
        return this.modelMutationElementService.createSatisfy(element, existingRequirement);
    }

    /**
     * {@link ModelMutationElementService#setAsView(ViewUsage, String)}.
     */
    public Element setAsView(ViewUsage viewUsage, String newViewDefinition) {
        return this.modelMutationElementService.setAsView(viewUsage, newViewDefinition);
    }
}
