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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.syson.diagram.services.utils.ViewFilterSwitch;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expose;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.MembershipExpose;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.springframework.stereotype.Service;

/**
 * Query services related to exposed elements in diagrams.
 *
 * @author arichard
 */
@Service
public class DiagramQueryExposeService {

    private final MetamodelQueryElementService metamodelQueryElementService;

    private final UtilService utilService;

    public DiagramQueryExposeService() {
        this.metamodelQueryElementService = new MetamodelQueryElementService();
        this.utilService = new UtilService();
    }

    /**
     * Get the list of elements to expose for a given {@link Element}.
     */
    public List<Element> getExposedElements(Element element, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.getExposedElements(element, null, domainType, ancestors, editingContext, diagramContext);
    }

    /**
     * Get the list of elements to expose for a given {@link Element}.
     */
    public List<Element> getExposedElements(Element element, Element parent, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        List<Element> elementsToExpose = new ArrayList<>();
        if (element instanceof ViewUsage viewUsage) {
            var exposedElements = new ArrayList<Element>();
            if (parent == null) {
                exposedElements.addAll(this.getDirectExposedElements(viewUsage));
            } else {
                exposedElements.addAll(viewUsage.getExposedElement());
            }
            var filteredExposedElements = exposedElements.stream()
                    .filter(elt -> this.isTypeOf(elt, domainType) && (parent == null || (!Objects.equals(parent, elt) && EMFUtils.isAncestor(parent, elt))))
                    .toList();
            elementsToExpose.addAll(filteredExposedElements);
            var viewDefKind = this.utilService.getViewDefinitionKind(viewUsage, ancestors, editingContext);
            for (Element filteredExposedElement : filteredExposedElements) {
                boolean canBeDisplayed = new ViewFilterSwitch(viewDefKind, exposedElements, parent, this.metamodelQueryElementService).doSwitch(filteredExposedElement);
                if (!canBeDisplayed) {
                    elementsToExpose.remove(filteredExposedElement);
                }
            }
        } else if (ancestors != null) {
            var viewUsageContainingElement = ancestors.stream()
                    .filter(ViewUsage.class::isInstance)
                    .map(ViewUsage.class::cast)
                    .findFirst();
            if (viewUsageContainingElement.isPresent()) {
                elementsToExpose.addAll(this.getExposedElements(viewUsageContainingElement.get(), element, domainType, ancestors, editingContext, diagramContext));
            }
        }
        return elementsToExpose;
    }

    /**
     * Get all Actors ({@link PartUsage}) in {@link ViewUsage}'s exposed elements.
     */
    public List<PartUsage> getExposedActors(Element element, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.getExposedElements(element, domainType, ancestors, editingContext, diagramContext).stream()
                .filter(PartUsage.class::isInstance)
                .map(PartUsage.class::cast)
                .filter(this.metamodelQueryElementService::isActor)
                .toList();
    }

    /**
     * Get all Stakeholders ({@link PartUsage}) in {@link ViewUsage}'s exposed elements.
     */
    public List<PartUsage> getExposedStakeholders(Element element, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.getExposedElements(element, domainType, ancestors, editingContext, diagramContext).stream()
                .filter(PartUsage.class::isInstance)
                .map(PartUsage.class::cast)
                .filter(this.metamodelQueryElementService::isStakeholder)
                .toList();
    }

    /**
     * Get all Subjects ({@link ReferenceUsage}) in {@link ViewUsage}'s exposed elements.
     */
    public List<ReferenceUsage> getExposedSubjects(Element element, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.getExposedElements(element, domainType, ancestors, editingContext, diagramContext).stream()
                .filter(ReferenceUsage.class::isInstance)
                .map(ReferenceUsage.class::cast)
                .filter(this.metamodelQueryElementService::isSubject)
                .toList();
    }

    private Set<Element> getDirectExposedElements(ViewUsage viewUsage) {
        var directExposedElements = new HashSet<Element>();
        List<Expose> exposedElements = viewUsage.getOwnedRelationship().stream()
                .filter(Expose.class::isInstance)
                .map(Expose.class::cast)
                .toList();
        for (Expose exposedElement : exposedElements) {
            Element importedElement = exposedElement.getImportedElement();
            if (importedElement instanceof Package) {
                directExposedElements.add(importedElement);
            } else if (exposedElement instanceof MembershipExpose membershipExpose) {
                var importedMembership = membershipExpose.getImportedMembership();
                if (importedMembership != null) {
                    var memberElement = importedMembership.getMemberElement();
                    if (memberElement != null && !this.isChildOfExposedPackage(memberElement, exposedElements)) {
                        directExposedElements.add(memberElement);
                        if (exposedElement.isIsRecursive()) {
                            directExposedElements.addAll(this.getRecursiveContents(memberElement));
                        }
                    }
                }
            }
        }
        return directExposedElements;
    }

    private boolean isChildOfExposedPackage(Element element, List<Expose> exposedElements) {
        boolean isChildOfExposedElement = false;
        for (Expose exposedElement : exposedElements) {
            Element importedElement = exposedElement.getImportedElement();
            if (importedElement instanceof Package && !Objects.equals(element, importedElement) && EMFUtils.isAncestor(importedElement, element)) {
                isChildOfExposedElement = true;
                break;
            }
        }
        return isChildOfExposedElement;
    }

    private List<Element> getRecursiveContents(Element element) {
        var contents = new ArrayList<Element>();
        var ownedElements = element.getOwnedElement().stream().filter(Objects::nonNull).toList();
        contents.addAll(ownedElements);
        ownedElements.forEach(oe -> contents.addAll(this.getRecursiveContents(oe)));
        return contents;
    }

    private boolean isTypeOf(Element element, EClass domainType) {
        if (element instanceof LibraryPackage) {
            return Objects.equals(SysmlPackage.eINSTANCE.getPackage(), domainType);
        }
        return element != null && Objects.equals(element.eClass(), domainType);
    }
}
