/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
package org.eclipse.syson.application.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.sirius.components.collaborative.api.IRepresentationMetadataPersistenceService;
import org.eclipse.sirius.components.collaborative.api.IRepresentationPersistenceService;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramCreationService;
import org.eclipse.sirius.components.core.RepresentationMetadata;
import org.eclipse.sirius.components.core.api.ChildCreationDescription;
import org.eclipse.sirius.components.core.api.IDefaultEditService;
import org.eclipse.sirius.components.core.api.IEditServiceDelegate;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.description.DiagramDescription;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.stereotype.Service;

/**
 * Specific {@link IEditServiceDelegate} to handle edition of SysML elements.
 *
 * @author arichard
 */
@Service
public class SysMLv2EditService implements IEditServiceDelegate {

    public static final String ID_PREFIX = "SysMLv2EditService-";

    private final IDefaultEditService defaultEditService;

    private final ILabelService labelService;

    private final IObjectSearchService objectSearchService;

    private final IDiagramCreationService diagramCreationService;

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    private final IRepresentationMetadataPersistenceService representationMetadataPersistenceService;

    private final IRepresentationPersistenceService representationPersistenceService;

    private final DeleteService deleteService;

    private final UtilService utilService;

    public SysMLv2EditService(IDefaultEditService defaultEditService, ILabelService labelService, IObjectSearchService objectSearchService, IDiagramCreationService diagramCreationService,
            IRepresentationDescriptionSearchService representationDescriptionSearchService, IRepresentationMetadataPersistenceService representationMetadataPersistenceService,
            IRepresentationPersistenceService representationPersistenceService) {
        this.defaultEditService = Objects.requireNonNull(defaultEditService);
        this.labelService = Objects.requireNonNull(labelService);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.diagramCreationService = Objects.requireNonNull(diagramCreationService);
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.representationMetadataPersistenceService = Objects.requireNonNull(representationMetadataPersistenceService);
        this.representationPersistenceService = Objects.requireNonNull(representationPersistenceService);
        this.deleteService = new DeleteService();
        this.utilService = new UtilService();
    }

    @Override
    public boolean canHandle(Object object) {
        return object instanceof Element;
    }

    @Override
    public boolean canHandle(IEditingContext editingContext) {
        return true;
    }

    @Override
    public List<ChildCreationDescription> getRootCreationDescriptions(IEditingContext editingContext, String domainId, boolean suggested, String referenceKind) {
        final List<ChildCreationDescription> rootObjectCreationDescription = new ArrayList<>();
        if (SysmlPackage.eNS_URI.equals(domainId)) {
            if (suggested) {
                List<String> iconURL = this.labelService.getImagePaths(EcoreUtil.create(SysmlPackage.eINSTANCE.getPackage()));
                StyledString styledLabel = this.labelService.getStyledLabel(SysmlPackage.eINSTANCE.getPackage());
                String label = "";
                if (styledLabel != null) {
                    label = styledLabel.toString();
                }
                rootObjectCreationDescription.add(new ChildCreationDescription(ID_PREFIX + SysmlPackage.eINSTANCE.getPackage().getName(), label, iconURL));
            } else {
                List<EClass> childrenCandidates = new GetChildCreationSwitch().doSwitch(SysmlPackage.eINSTANCE.getNamespace());
                childrenCandidates.forEach(candidate -> {
                    List<String> iconURL = this.labelService.getImagePaths(EcoreUtil.create(candidate));
                    StyledString styledLabel = this.labelService.getStyledLabel(candidate);
                    String label = "";
                    if (styledLabel != null) {
                        label = styledLabel.toString();
                    }
                    ChildCreationDescription childCreationDescription = new ChildCreationDescription(ID_PREFIX + candidate.getName(), label, iconURL);
                    rootObjectCreationDescription.add(childCreationDescription);
                });
            }
        } else {
            rootObjectCreationDescription.addAll(this.defaultEditService.getRootCreationDescriptions(editingContext, domainId, suggested, referenceKind));
        }
        Collections.sort(rootObjectCreationDescription, Comparator.comparing(ChildCreationDescription::label, String.CASE_INSENSITIVE_ORDER));
        return rootObjectCreationDescription;
    }

    @Override
    public List<ChildCreationDescription> getChildCreationDescriptions(IEditingContext editingContext, String containerId, String referenceKind) {
        List<ChildCreationDescription> result = new ArrayList<>();

        var optionalContainer = this.objectSearchService.getObject(editingContext, containerId)
                .filter(EObject.class::isInstance)
                .map(EObject.class::cast);

        if (optionalContainer.isPresent()) {
            EObject container = optionalContainer.get();
            EClass eClass = container.eClass();
            EPackage ePackage = eClass.getEPackage();
            if (SysmlPackage.eNS_PREFIX.equals(ePackage.getNsPrefix())) {
                List<ChildCreationDescription> childCreationDescriptions = new ArrayList<>();
                List<EClass> childrenCandidates = new GetChildCreationSwitch().doSwitch(eClass);
                childrenCandidates.forEach(candidate -> {
                    List<String> iconURL = this.labelService.getImagePaths(EcoreUtil.create(candidate));
                    StyledString styledLabel = this.labelService.getStyledLabel(candidate);
                    String label = "";
                    if (styledLabel != null) {
                        label = styledLabel.toString();
                    }
                    ChildCreationDescription childCreationDescription = new ChildCreationDescription(ID_PREFIX + candidate.getName(), label, iconURL);
                    childCreationDescriptions.add(childCreationDescription);
                });
                result = childCreationDescriptions;
            } else {
                result = this.defaultEditService.getChildCreationDescriptions(editingContext, containerId, referenceKind);
            }
        }
        Collections.sort(result, Comparator.comparing(ChildCreationDescription::label, String.CASE_INSENSITIVE_ORDER));
        return result;
    }

    @Override
    public Optional<Object> createChild(IEditingContext editingContext, Object object, String childCreationDescriptionId) {
        if (childCreationDescriptionId.startsWith(ID_PREFIX) && object instanceof Element container) {
            EClass eClass = SysMLMetamodelHelper.toEClass(childCreationDescriptionId.substring(ID_PREFIX.length()));
            EObject eObject = SysmlFactory.eINSTANCE.create(eClass);
            Optional<EClass> intermediateContainerClass = new GetIntermediateContainerCreationSwitch(container).doSwitch(eClass);
            if (intermediateContainerClass.isPresent() && eObject instanceof Element newElement) {
                EObject intermediateContainerEObject = SysmlFactory.eINSTANCE.create(intermediateContainerClass.get());
                if (intermediateContainerEObject instanceof Relationship intermediateContainer) {
                    container.getOwnedRelationship().add(intermediateContainer);
                    intermediateContainer.getOwnedRelatedElement().add(newElement);
                }
            } else if (eObject instanceof Relationship newElement) {
                container.getOwnedRelationship().add(newElement);
            } else if (container instanceof Membership membership && eObject instanceof Element newElement) {
                membership.getOwnedRelatedElement().add(newElement);
            }
            // Updating an element in an imported model removes its imported flag. This ensures that creating a library
            // model from an imported SysML file does not make it an imported user library.
            ElementUtil.setIsImported(eObject.eResource(), false);
            new ElementInitializerSwitch().doSwitch(eObject);
            if (eObject instanceof ViewUsage viewUsage) {
                this.createDiagram(editingContext, viewUsage);
            }
            return Optional.of(eObject);
        }
        return this.defaultEditService.createChild(editingContext, object, childCreationDescriptionId);
    }

    @Override
    public Optional<Object> createRootObject(IEditingContext editingContext, UUID documentId, String domainId, String rootObjectCreationDescriptionId) {
        Optional<Object> createdObjectOptional = Optional.empty();

        var optionalEditingDomain = Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(IEMFEditingContext::getDomain);

        if (optionalEditingDomain.isPresent()) {
            AdapterFactoryEditingDomain editingDomain = optionalEditingDomain.get();

            var optionalResource = editingDomain.getResourceSet().getResources().stream()
                    .filter(resource -> documentId.toString().equals(resource.getURI().path().substring(1)))
                    .findFirst();

            if (optionalResource.isPresent()) {
                var resource = optionalResource.get();
                var rootNamespace = resource.getContents().stream()
                        .filter(Element.class::isInstance)
                        .map(Element.class::cast)
                        .filter(this.utilService::isRootNamespace)
                        .findFirst()
                        .orElseGet(() -> {
                            Namespace namespace = (Namespace) EcoreUtil.create(SysmlPackage.eINSTANCE.getNamespace());
                            resource.getContents().add(namespace);
                            return namespace;
                        });
                createdObjectOptional = this.createChild(editingContext, rootNamespace, rootObjectCreationDescriptionId);
            }
        }
        return createdObjectOptional;
    }

    @Override
    public void delete(Object object) {
        Optional<Element> optionalElement = Optional.of(object)
                .filter(Element.class::isInstance)
                .map(Element.class::cast);

        optionalElement.ifPresent(element -> this.deleteService.deleteFromModel(element));
    }

    /**
     * Create a General View diagram and associate it to the given ViewUsage.
     */
    private void createDiagram(IEditingContext editingContext, ViewUsage viewUsage) {
        this.representationDescriptionSearchService.findById(editingContext, SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID)
                .filter(DiagramDescription.class::isInstance)
                .map(DiagramDescription.class::cast)
                .ifPresent(diagramDescription -> {
                    var variableManager = new VariableManager();
                    variableManager.put(VariableManager.SELF, viewUsage);
                    variableManager.put(DiagramDescription.LABEL, viewUsage.getDeclaredName());
                    String label = diagramDescription.getLabelProvider().apply(variableManager);
                    List<String> iconURLs = diagramDescription.getIconURLsProvider().apply(variableManager);

                    Diagram diagram = this.diagramCreationService.create(viewUsage, diagramDescription, editingContext);
                    var representationMetadata = RepresentationMetadata.newRepresentationMetadata(diagram.getId())
                            .kind(diagram.getKind())
                            .label(label)
                            .descriptionId(diagram.getDescriptionId())
                            .iconURLs(iconURLs)
                            .build();
                    this.representationMetadataPersistenceService.save(null, editingContext, representationMetadata, diagram.getTargetObjectId());
                    this.representationPersistenceService.save(null, editingContext, diagram);
                });
    }
}
