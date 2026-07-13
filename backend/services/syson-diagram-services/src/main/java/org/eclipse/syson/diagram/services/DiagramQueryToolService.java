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
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.metamodel.util.ElementUtil;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.springframework.stereotype.Service;

/**
 * Tool-related services doing queries for diagrams.
 *
 * @author arichard
 */
@Service
public class DiagramQueryToolService {

    private final IObjectSearchService objectSearchService;

    private final ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService;

    private final UtilService utilService;

    public DiagramQueryToolService(IObjectSearchService objectSearchService, ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.showDiagramsInheritedMembersService = Objects.requireNonNull(showDiagramsInheritedMembersService);
        this.utilService = new UtilService();
    }

    /**
     * Tool precondition for control node actions (Start/Done/Decision/Fork/Join/Merge) invoked on diagram background.
     *
     * @param element
     *            the given {@link Element}.
     * @return <code>true</code> if the tool should be available, <code>false</code> otherwise.
     */
    public boolean isControlNodeActionCreationToolInsideActionOnAFV(Element element, IEditingContext editingContext, DiagramContext diagramContext) {
        if (this.isAFVDiagram(editingContext, diagramContext)) {
            var owner = this.utilService.getViewUsageOwner(element);
            return owner instanceof ActionUsage || owner instanceof ActionDefinition;
        }
        return false;
    }

    /**
     * Tool precondition for control node actions (Start/Done/Decision/Fork/Join/Merge) invoked on a selected node.
     *
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @param selectedNode
     *            the selected node. It corresponds to a variable accessible from the variable manager.
     * @return <code>true</code> if the tool should be available, <code>false</code> otherwise.
     */
    public boolean isControlNodeActionCreationToolInAction(IEditingContext editingContext, Node selectedNode) {
        return this.objectSearchService.getObject(editingContext, selectedNode.getTargetObjectId())
                .map(object -> {
                    return object instanceof ActionUsage || object instanceof ActionDefinition;
                }).orElse(false);
    }

    /**
     * Service to check whether the given element has a subject defined or not.
     *
     * @param self
     *            a {@link org.eclipse.syson.sysml.RequirementUsage} or a {@link org.eclipse.syson.sysml.RequirementDefinition} or a {@link org.eclipse.syson.sysml.CaseUsage} or a
     *            {@link org.eclipse.syson.sysml.CaseDefinition}
     * @return {@code true} if {@code self} contains a subject and {@code false} otherwise.
     */
    public boolean isEmptySubjectCompartment(Element self) {
        if (self instanceof org.eclipse.syson.sysml.RequirementUsage
                || self instanceof org.eclipse.syson.sysml.RequirementDefinition
                || self instanceof org.eclipse.syson.sysml.CaseUsage
                || self instanceof org.eclipse.syson.sysml.CaseDefinition) {
            return self.getOwnedRelationship().stream()
                    .filter(SubjectMembership.class::isInstance)
                    .map(SubjectMembership.class::cast)
                    .findFirst()
                    .isEmpty();
        }
        return true;
    }

    /**
     * Service to check whether the given UseCaseUsage or UseCaseDefinition has an objective requirement defined or not.
     *
     * @param self
     *            a {@link org.eclipse.syson.sysml.UseCaseUsage} or a {@link org.eclipse.syson.sysml.UseCaseDefinition}
     * @return {@code true} if {@code self} contains a subject and {@code false} otherwise.
     */
    public boolean isEmptyObjectiveRequirementCompartment(Element self) {
        return this.utilService.isEmptyObjectiveRequirement(self);
    }

    /**
     * Service to check whether the given element has a subaction of Kind {@code kind} subject defined or not.
     *
     * @param self
     *            a {@link StateUsage} or a {@link StateDefinition}
     * @return {@code true} if {@code self} contains a subaction of the specified kind and {@code false} otherwise.
     */
    public boolean isEmptyOfActionKindCompartment(Element self, String kind) {
        if (self instanceof StateUsage
                || self instanceof StateDefinition) {
            return self.getOwnedRelationship().stream()
                    .filter(StateSubactionMembership.class::isInstance)
                    .map(StateSubactionMembership.class::cast)
                    .noneMatch(mem -> mem.getKind().getLiteral().equalsIgnoreCase(kind));
        }
        return true;
    }

    /**
     * Returns the inherited features matching the given compartment reference according to the inherited-members flags.
     *
     * @param type
     *            the type owning the compartment
     * @param eReferenceName
     *            the compartment reference name
     * @return the inherited features to display
     */
    public List<Feature> getInheritedCompartmentItems(Type type, String eReferenceName) {
        boolean showInheritedMembers = this.showDiagramsInheritedMembersService.getShowInheritedMembers();
        boolean showInheritedMembersFromStandardLibraries = this.showDiagramsInheritedMembersService.getShowInheritedMembersFromStandardLibraries();

        if (!showInheritedMembers && !showInheritedMembersFromStandardLibraries) {
            return List.of();
        }

        List<Feature> inheritedElements = new ArrayList<>();
        var eStructuralFeature = type.eClass().getEStructuralFeature(eReferenceName);
        if (eStructuralFeature instanceof EReference eReference) {
            type.getInheritedFeature().stream()
                    .filter(feature -> new InheritedCompartmentItemFilterSwitch(eReference).doSwitch(feature))
                    .forEach(inheritedElements::add);
        }

        var alreadySpecializedFeatures = new ArrayList<Feature>();
        type.getOwnedFeature().stream()
                .flatMap(feature -> this.getSpecializationTypeHierarchy(feature).stream())
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .filter(inheritedElements::contains)
                .forEach(alreadySpecializedFeatures::add);
        inheritedElements.stream()
                .flatMap(feature -> this.getSpecializationTypeHierarchy(feature).stream())
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .filter(inheritedElements::contains)
                .forEach(alreadySpecializedFeatures::add);
        inheritedElements.removeAll(alreadySpecializedFeatures);
        inheritedElements.remove(type);

        if (showInheritedMembers && !showInheritedMembersFromStandardLibraries) {
            inheritedElements.removeIf(ElementUtil::isFromStandardLibrary);
        } else if (showInheritedMembersFromStandardLibraries && !showInheritedMembers) {
            inheritedElements.removeIf(element -> !ElementUtil.isFromStandardLibrary(element));
        }
        return inheritedElements;
    }

    /**
     * Service to check whether the given accept action usage has a receiver defined or not.
     *
     * @param element
     *            the accept action usage to inspect
     * @return {@code true} if the receiver is empty
     */
    public boolean isEmptyAcceptActionUsageReceiver(Element element) {
        if (element instanceof org.eclipse.syson.sysml.AcceptActionUsage acceptActionUsage) {
            var receiverExp = acceptActionUsage.getReceiverArgument();
            if (receiverExp != null) {
                var receiverMembership = receiverExp.getOwnedRelationship().stream()
                        .filter(org.eclipse.syson.sysml.Membership.class::isInstance)
                        .map(org.eclipse.syson.sysml.Membership.class::cast)
                        .findFirst()
                        .orElse(null);
                if (receiverMembership != null) {
                    return receiverMembership.getMemberElement() == null;
                }
            }
        }
        return true;
    }

    /**
     * Service to check whether the given accept action usage has a payload defined or not.
     *
     * @param element
     *            the accept action usage to inspect
     * @return {@code true} if the payload is empty
     */
    public boolean isEmptyAcceptActionUsagePayload(Element element) {
        boolean result = true;
        if (element instanceof org.eclipse.syson.sysml.AcceptActionUsage acceptActionUsage) {
            var payloadParameter = acceptActionUsage.getPayloadParameter();
            if (payloadParameter != null && !payloadParameter.getOwnedRelationship().isEmpty()) {
                var type = payloadParameter.getOwnedRelationship().stream()
                        .filter(org.eclipse.syson.sysml.FeatureTyping.class::isInstance)
                        .map(org.eclipse.syson.sysml.FeatureTyping.class::cast)
                        .map(org.eclipse.syson.sysml.FeatureTyping::getType)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null);
                return type == null;
            }
        }
        return result;
    }

    /**
     * Checks if a tool creating the given type should be available for the provided diagram context.
     *
     * @param element
     *            the semantic context element
     * @param editingContext
     *            the editing context
     * @param diagramContext
     *            the diagram context
     * @param newElementType
     *            the type created by the tool
     * @return {@code true} if the tool should be available, {@code false} otherwise
     */
    public boolean toolShouldBeAvailable(Element element, IEditingContext editingContext, DiagramContext diagramContext, EClass newElementType) {
        ViewDefinitionKind viewDefinitionKind = this.utilService.getViewDefinitionKind(element, List.of(), editingContext);
        var elt = this.utilService.getViewUsageOwner(element);

        return switch (viewDefinitionKind) {
            case INTERCONNECTION_VIEW -> this.toolShouldBeAvailableOnInterconnectionView(elt, newElementType);
            case ACTION_FLOW_VIEW -> this.toolShouldBeAvailableOnActionFlowView(elt, newElementType);
            case STATE_TRANSITION_VIEW -> this.toolShouldBeAvailableOnStateTransitionView(elt, newElementType);
            default -> this.toolShouldBeAvailableOnGeneralView(elt, newElementType);
        };
    }

    private boolean toolShouldBeAvailableOnGeneralView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof Package) {
            toolShouldBeAvailable = true;
        } else if (element instanceof Usage && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        } else if (element instanceof Definition && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        }
        return toolShouldBeAvailable;
    }

    private boolean toolShouldBeAvailableOnInterconnectionView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof Package) {
            toolShouldBeAvailable = true;
            if (SysmlPackage.eINSTANCE.getAttributeUsage().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = false;
            } else if (SysmlPackage.eINSTANCE.getPortUsage().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = false;
            }
        } else if (element instanceof Usage && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        } else if (element instanceof Definition && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        }
        return toolShouldBeAvailable;
    }

    private boolean toolShouldBeAvailableOnActionFlowView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof ActionUsage) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else if (element instanceof ActionDefinition) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else {
            if (SysmlPackage.eINSTANCE.getActionUsage().equals(domainClass) || SysmlPackage.eINSTANCE.getActionUsage().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            } else if (SysmlPackage.eINSTANCE.getActionDefinition().equals(domainClass) || SysmlPackage.eINSTANCE.getActionDefinition().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            }
        }
        return toolShouldBeAvailable;
    }

    private boolean toolShouldBeAvailableOnStateTransitionView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof StateUsage) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else if (element instanceof StateDefinition) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else {
            if (SysmlPackage.eINSTANCE.getStateUsage().equals(domainClass) || SysmlPackage.eINSTANCE.getStateUsage().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            } else if (SysmlPackage.eINSTANCE.getStateDefinition().equals(domainClass) || SysmlPackage.eINSTANCE.getStateDefinition().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            }
        }
        return toolShouldBeAvailable;
    }

    private List<Type> getSpecializationTypeHierarchy(Type type) {
        List<Type> specializationTypeHierarchy = new ArrayList<>();
        this.getSpecializationTypeHierarchy(type, specializationTypeHierarchy);
        return specializationTypeHierarchy;
    }

    private void getSpecializationTypeHierarchy(Type type, List<Type> specializationTypeHierarchy) {
        if (type != null) {
            List<Type> specializationTypes = new ArrayList<>();
            type.getOwnedSpecialization().stream()
                    .map(Specialization::getGeneral)
                    .forEach(general -> {
                        if (general != null && !specializationTypeHierarchy.contains(general) && !specializationTypes.contains(general)) {
                            specializationTypes.add(general);
                            specializationTypeHierarchy.add(general);
                        }
                    });
            specializationTypes.forEach(specializationType -> this.getSpecializationTypeHierarchy(specializationType, specializationTypeHierarchy));
        }
    }

    private boolean isAFVDiagram(IEditingContext editingContext, DiagramContext diagramContext) {
        var objectId = diagramContext.diagram().getTargetObjectId();
        return this.objectSearchService.getObject(editingContext, objectId)
                .map(object -> {
                    if (object instanceof ViewUsage viewUsage) {
                        if (!viewUsage.getType().isEmpty()) {
                            Type type = viewUsage.getType().getFirst();
                            return type instanceof ViewDefinition && StandardDiagramsConstants.AFV_QN.equals(type.getQualifiedName());
                        }
                    }
                    return false;
                }).orElse(false);
    }
}
