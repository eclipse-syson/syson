/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.sirius.components.core.api.ChildCreationDescription;
import org.eclipse.sirius.components.core.api.IDefaultEditService;
import org.eclipse.sirius.components.core.api.IEditServiceDelegate;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.emf.services.api.IEMFKindService;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.springframework.stereotype.Service;

/**
 * Specific {@link IEditServiceDelegate} to handle edition of SysML elements.
 *
 * @author arichard
 */
@Service
public class SysMLv2EditService implements IEditServiceDelegate {

    private static final String ID_PREFIX = "SysMLv2EditService-";

    private final IDefaultEditService defaultEditService;

    private final IObjectService objectService;

    private final IEMFKindService emfKindService;

    private final DeleteService deleteService;

    private final UtilService utilService;

    public SysMLv2EditService(IDefaultEditService defaultEditService, IObjectService objectService, IEMFKindService emfKindService) {
        this.defaultEditService = Objects.requireNonNull(defaultEditService);
        this.objectService = Objects.requireNonNull(objectService);
        this.emfKindService = Objects.requireNonNull(emfKindService);
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
                List<String> iconURL = this.objectService.getImagePath(EcoreUtil.create(SysmlPackage.eINSTANCE.getPackage()));
                String label = this.objectService.getLabel(SysmlPackage.eINSTANCE.getPackage());
                rootObjectCreationDescription.add(new ChildCreationDescription(ID_PREFIX + SysmlPackage.eINSTANCE.getPackage().getName(), label, iconURL));
            } else {
                List<EClass> childrenCandidates = new GetChildCreationSwitch().doSwitch(SysmlPackage.eINSTANCE.getNamespace());
                childrenCandidates.forEach(candidate -> {
                    List<String> iconURL = this.objectService.getImagePath(EcoreUtil.create(candidate));
                    String label = this.objectService.getLabel(candidate);
                    ChildCreationDescription childCreationDescription = new ChildCreationDescription(ID_PREFIX + candidate.getName(), label, iconURL);
                    rootObjectCreationDescription.add(childCreationDescription);
                });
            }
        } else {
            rootObjectCreationDescription.addAll(this.defaultEditService.getRootCreationDescriptions(editingContext, domainId, suggested, referenceKind));
        }
        Collections.sort(rootObjectCreationDescription, Comparator.comparing(ChildCreationDescription::getLabel, String.CASE_INSENSITIVE_ORDER));
        return rootObjectCreationDescription;
    }

    @Override
    public List<ChildCreationDescription> getChildCreationDescriptions(IEditingContext editingContext, String kind, String referenceKind) {
        List<ChildCreationDescription> result;
        String ePackageName = this.emfKindService.getEPackageName(kind);
        if (SysmlPackage.eNS_PREFIX.equals(ePackageName)) {
            List<ChildCreationDescription> childCreationDescriptions = new ArrayList<>();
            String eClassName = this.emfKindService.getEClassName(kind);
            Optional<EClass> eClass = this.getEClass(eClassName);
            if (eClass.isPresent()) {
                List<EClass> childrenCandidates = new GetChildCreationSwitch().doSwitch(eClass.get());
                childrenCandidates.forEach(candidate -> {
                    List<String> iconURL = this.objectService.getImagePath(EcoreUtil.create(candidate));
                    String label = this.objectService.getLabel(candidate);
                    ChildCreationDescription childCreationDescription = new ChildCreationDescription(ID_PREFIX + candidate.getName(), label, iconURL);
                    childCreationDescriptions.add(childCreationDescription);
                });
            }
            result = childCreationDescriptions;
        } else {
            result = this.defaultEditService.getChildCreationDescriptions(editingContext, kind, referenceKind);
        }
        Collections.sort(result, Comparator.comparing(ChildCreationDescription::getLabel, String.CASE_INSENSITIVE_ORDER));
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
            new ElementInitializerSwitch().doSwitch(eObject);
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

    @Override
    public void editLabel(Object object, String labelField, String newValue) {
        this.defaultEditService.editLabel(object, labelField, newValue);
    }

    private Optional<EClass> getEClass(String eClassName) {
        return Optional.ofNullable(SysmlPackage.eINSTANCE.getEClassifier(eClassName))
                .filter(EClass.class::isInstance)
                .map(EClass.class::cast);
    }
}
