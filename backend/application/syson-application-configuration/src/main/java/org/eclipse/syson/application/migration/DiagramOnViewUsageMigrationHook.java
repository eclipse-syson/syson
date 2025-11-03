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
package org.eclipse.syson.application.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventProcessorInitializationHook;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.IDAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationContent;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.repositories.IRepresentationContentRepository;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataUpdateService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.util.GetIntermediateContainerCreationSwitch;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hook that allows to update the target object of existing diagrams. This hook is coupled with the migration
 * participant about Diagram on ViewUsage. The target object of existing diagrams is moved to the ViewUsages that have
 * been created during the migration participant process.
 *
 * @author arichard
 */
@Service
public class DiagramOnViewUsageMigrationHook implements IEditingContextEventProcessorInitializationHook {

    private final ElementUtil elementUtil = new ElementUtil();

    private final IRepresentationMetadataUpdateService representationMetadataUpdateService;

    private final IRepresentationContentRepository representationContentRepository;

    private final IIdentityService identityService;

    public DiagramOnViewUsageMigrationHook(IRepresentationMetadataUpdateService representationMetadataUpdateService, IRepresentationContentRepository representationContentRepository,
            IIdentityService identityService) {
        this.representationMetadataUpdateService = Objects.requireNonNull(representationMetadataUpdateService);
        this.representationContentRepository = Objects.requireNonNull(representationContentRepository);
        this.identityService = Objects.requireNonNull(identityService);
    }

    @Transactional
    @Override
    public void preProcess(IEditingContext editingContext) {
        var resources = this.getDiagramOnViewUsageMigrationParticipantResources(editingContext);
        for (Resource resource : resources) {
            resource.getAllContents().forEachRemaining(eObject -> {
                if (eObject instanceof Element element) {
                    var adapter = this.isMigrationParticipantCandidate(element);
                    if (adapter.isPresent()) {
                        var diagramOnViewUsageMigrationAdapter = adapter.get();
                        var representationMetadata = diagramOnViewUsageMigrationAdapter.getRepresentationMetadata();
                        var representationContent = diagramOnViewUsageMigrationAdapter.getRepresentationContent();
                        var diagramName = representationMetadata.getLabel();
                        var representationDescriptionId = representationMetadata.getDescriptionId();
                        var viewUsage = this.createViewUsage(element, diagramName, representationDescriptionId);
                        var idAdapter = new IDAdapter(ElementUtil.generateUUID(viewUsage));
                        viewUsage.eAdapters().add(idAdapter);
                        this.updateDiagramTarget(this.identityService.getId(viewUsage), representationMetadata, representationContent);
                        element.eAdapters().remove(diagramOnViewUsageMigrationAdapter);
                    }
                }
            });
        }
    }

    private List<Resource> getDiagramOnViewUsageMigrationParticipantResources(IEditingContext editingContext) {
        List<Resource> resourcesWithDiagramOnViewUsageMigrationParticpant = new ArrayList<>();
        if (editingContext instanceof IEMFEditingContext emfEditingContext) {
            ResourceSet resourceSet = emfEditingContext.getDomain().getResourceSet();
            for (Resource resource : resourceSet.getResources().stream().filter(res -> !ElementUtil.isStandardLibraryResource(res)).toList()) {
                var optAdapter = resource.eAdapters().stream()
                        .filter(ResourceMetadataAdapter.class::isInstance)
                        .map(ResourceMetadataAdapter.class::cast)
                        .findFirst();
                if (optAdapter.isPresent()) {
                    var optMigrationData = optAdapter.get().getAllMigrationData().stream()
                            .filter(migData -> migData.migrationVersion().equals(DiagramOnViewUsageMigrationParticipant.PARTICIPANT_VERSION))
                            .findFirst();
                    if (optMigrationData.isPresent()) {
                        resourcesWithDiagramOnViewUsageMigrationParticpant.add(resource);
                    }
                }
            }
        }
        return resourcesWithDiagramOnViewUsageMigrationParticpant;
    }

    private Optional<DiagramOnViewUsageMigrationAdapter> isMigrationParticipantCandidate(Element element) {
        return element.eAdapters().stream()
                .filter(DiagramOnViewUsageMigrationAdapter.class::isInstance)
                .map(DiagramOnViewUsageMigrationAdapter.class::cast)
                .findFirst();
    }

    private ViewUsage createViewUsage(Element containerElement, String viewUsageName, String representationDescriptionId) {
        var getIntermediateContainerCreationSwitch = new GetIntermediateContainerCreationSwitch(containerElement);
        var intermediateContainerClass = getIntermediateContainerCreationSwitch.doSwitch(containerElement.eClass());
        if (intermediateContainerClass.isPresent()) {
            EObject intermediateContainerEObject = SysmlFactory.eINSTANCE.create(intermediateContainerClass.get());
            if (intermediateContainerEObject instanceof Relationship intermediateContainer) {
                var viewUsage = SysmlFactory.eINSTANCE.createViewUsage();
                viewUsage.setDeclaredName(viewUsageName);
                containerElement.getOwnedRelationship().add(intermediateContainer);
                intermediateContainer.getOwnedRelatedElement().add(viewUsage);
                this.setViewDefinition(containerElement, viewUsage, representationDescriptionId);
                return viewUsage;
            }
        }
        return null;
    }

    private void setViewDefinition(Element containerElement, ViewUsage viewUsage, String representationDescriptionId) {
        var viewDefinition = this.getViewDefinition(containerElement, representationDescriptionId);
        if (viewDefinition.isPresent()) {
            var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
            var idAdapter = new IDAdapter(ElementUtil.generateUUID(featureTyping));
            featureTyping.eAdapters().add(idAdapter);
            viewUsage.getOwnedRelationship().add(featureTyping);
            featureTyping.setType(viewDefinition.get());
            featureTyping.setTypedFeature(viewUsage);
        }
    }

    private Optional<ViewDefinition> getViewDefinition(Element containerElement, String representationDescriptionId) {
        Optional<ViewDefinition> optViewDef = Optional.empty();
        if (Objects.equals(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            var generalViewViewDef = this.elementUtil.findByNameAndType(containerElement, StandardDiagramsConstants.GV_QN, ViewDefinition.class);
            optViewDef = Optional.ofNullable(generalViewViewDef);
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.INTERCONNECTION_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            var generalViewViewDef = this.elementUtil.findByNameAndType(containerElement, StandardDiagramsConstants.IV_QN, ViewDefinition.class);
            optViewDef = Optional.ofNullable(generalViewViewDef);
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.ACTION_FLOW_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            var generalViewViewDef = this.elementUtil.findByNameAndType(containerElement, StandardDiagramsConstants.AFV_QN, ViewDefinition.class);
            optViewDef = Optional.ofNullable(generalViewViewDef);
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.STATE_TRANSITION_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            var generalViewViewDef = this.elementUtil.findByNameAndType(containerElement, StandardDiagramsConstants.STV_QN, ViewDefinition.class);
            optViewDef = Optional.ofNullable(generalViewViewDef);
        }
        return optViewDef;
    }

    private void updateDiagramTarget(String targetObjectId, RepresentationMetadata representationMetadata, RepresentationContent representationContent) {
        this.representationMetadataUpdateService.updateTargetObjectId(null, representationMetadata.getId(), targetObjectId);

        var newContent = this.updateRepresentationContent(representationContent, targetObjectId);
        representationContent.updateContent(null, newContent);

        this.representationContentRepository.save(representationContent);
    }

    private String updateRepresentationContent(RepresentationContent representationContent, String newTargetObjectId) {
        return representationContent.getContent().replaceFirst("\"targetObjectId\":(\s)*\"[a-z0-9-]*\"", "\"targetObjectId\":\"" + newTargetObjectId + "\"");
    }
}
