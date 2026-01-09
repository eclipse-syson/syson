/*******************************************************************************
 * Copyright (c) 2023, 2026 Obeo.
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
package org.eclipse.syson.diagram.common.view.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.IDiagramElement;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.services.ToolService;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tool-related Java services used by all diagrams.
 *
 * @author arichard
 */
public class ViewToolService extends ToolService {

    private static final String STATE_TRANSITION_COMPARTMENT_NAME = "state transition";

    protected final IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService;

    protected final Logger logger = LoggerFactory.getLogger(ViewToolService.class);

    public ViewToolService(IIdentityService identityService, IObjectSearchService objectSearchService, IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService,
            IFeedbackMessageService feedbackMessageService, ISysMLMoveElementService moveService) {
        super(identityService, objectSearchService, feedbackMessageService, moveService);
        this.viewRepresentationDescriptionSearchService = Objects.requireNonNull(viewRepresentationDescriptionSearchService);
    }

    /**
     * Check if a tool that will create an instance of the given type should be available for a diagram associated to
     * the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @param newElementType
     *            the given type.
     * @return <code>true</code> if the tool should be available, <code>false</code> otherwise.
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

    public Usage becomeNestedUsage(Usage usage, Element newContainer) {
        if (this.getOwnerHierarchy(newContainer).contains(usage) || Objects.equals(newContainer, usage)) {
            String message = MessageFormat.format("Cannot change the owner of {0}, this would create a containment cycle", String.valueOf(usage.getName()));
            this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.WARNING));
            this.logger.warn(message);
        } else {
            this.moveService.moveSemanticElement(usage, newContainer);
            usage.setIsComposite(true);
        }
        return usage;
    }

    public RequirementUsage becomeObjectiveRequirement(RequirementUsage requirement, Element newContainer) {
        if (newContainer instanceof UseCaseUsage || newContainer instanceof UseCaseDefinition) {
            if (this.utilService.isEmptyObjectiveRequirement(newContainer)) {
                var eContainer = requirement.eContainer();
                if (eContainer instanceof ObjectiveMembership objectiveMembership) {
                    // requirement is already an objective of another usecaseXXX
                    newContainer.getOwnedRelationship().add(objectiveMembership);
                } else if (eContainer instanceof OwningMembership owningMembership) {
                    var newObjectiveMembership = SysmlFactory.eINSTANCE.createObjectiveMembership();
                    newObjectiveMembership.getOwnedRelatedElement().add(requirement);
                    newContainer.getOwnedRelationship().add(newObjectiveMembership);
                    this.deleteService.deleteFromModel(owningMembership);
                }
            }
        }
        return requirement;
    }

    /**
     * Reconnects the source of a nested actor edge.
     * <p>
     * The source of this edge is either an UseCase or a Requirement, and can only be reconnected to UseCase or
     * Requirements.
     * </p>
     *
     * @param self
     *            the current UseCase or Requirement
     * @param newSource
     *            the new UseCase or Requirement
     * @param otherEnd
     *            the Actor connected to the UseCase or Requirement
     * @return the Actor
     */
    public Element reconnectSourceNestedActorEdge(Element self, Element newSource, Element otherEnd) {
        if (newSource instanceof UseCaseUsage || newSource instanceof UseCaseDefinition
                || newSource instanceof RequirementUsage || newSource instanceof RequirementDefinition) {
            if (otherEnd.getOwningMembership() instanceof ActorMembership actorMembership) {
                newSource.getOwnedRelationship().add(actorMembership);
            } else {
                // This is an error, an Actor should always be contained in an ActorMembership.
                String errorMessage = "Cannot reconnect the Actor, it is not owned by an " + ActorMembership.class.getSimpleName();
                this.logger.error(errorMessage);
                this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.ERROR));
            }
        } else {
            String errorMessage = "Cannot reconnect an Actor to a non-UseCase, non-Requirement element";
            this.logger.warn(errorMessage);
            this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
        }
        return otherEnd;
    }

    public Element reconnnectSourceCompositionEdge(Element self, Element newSource, Element otherEnd) {
        Element result = otherEnd;
        if (this.getOwnerHierarchy(newSource).contains(otherEnd) || Objects.equals(newSource, otherEnd)) {
            String message = MessageFormat.format("Cannot change the owner of {0}, this would create a containment cycle", String.valueOf(otherEnd.getName()));
            this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.WARNING));
            this.logger.warn(message);
        } else {
            this.moveService.moveSemanticElement(otherEnd, newSource);
            result = otherEnd;
        }
        return result;
    }

    public Element reconnnectTargetCompositionEdge(Element self, Element oldTarget, Element newTarget, Element otherEnd) {
        if (this.getOwnerHierarchy(otherEnd).contains(newTarget) || Objects.equals(otherEnd, newTarget)) {
            String message = MessageFormat.format("Cannot change the owner of {0}, this would create a containment cycle", String.valueOf(otherEnd.getName()));
            this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.WARNING));
            this.logger.warn(message);
        } else {
            var oldContainer = oldTarget.eContainer();
            if (newTarget instanceof Usage && oldContainer instanceof FeatureMembership featureMembership) {
                // Move the old target to the innermost package
                // We don't use moveToClosestContainingPackage here because we are moving oldTarget in the closest
                // containing package and updating newTarget at the same time.
                var pack = this.getClosestContainingPackageFrom(self);
                if (pack != null) {
                    var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                    pack.getOwnedRelationship().add(owningMembership);
                    owningMembership.getOwnedRelatedElement().add(oldTarget);
                    // reuse feature membership of the previous nested
                    var oldMembership = newTarget.eContainer();
                    featureMembership.getOwnedRelatedElement().add(newTarget);
                    if (oldMembership instanceof OwningMembership oldOwningMembership) {
                        this.deleteService.deleteFromModel(oldOwningMembership);
                    }
                }
            }
        }
        return self;
    }

    /**
     * Reconnects the annotation to the annotated element target.
     * <p>
     * The target of this edge can be all elements except a Documentation or a Comment.
     * </p>
     *
     * @param self
     *            the current annotating element
     * @param newTarget
     *            the new Annotated element
     * @return the annotating element
     */
    public Element reconnnectTargetAnnotatedEdge(Element self, Element newTarget) {
        if (!(newTarget instanceof Comment) && !(newTarget instanceof Documentation)) {
            this.moveService.moveSemanticElement(self, newTarget);
        }
        return self;
    }

    /**
     * Moves the provided {@code usage} in the closest containing package.
     *
     * @param usage
     *            the usage to move
     * @return the moved element
     */
    public Element moveToClosestContainingPackage(Usage usage) {
        var pack = this.getClosestContainingPackageFrom(usage);
        if (pack != null) {
            var oldMembership = usage.eContainer();
            var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
            pack.getOwnedRelationship().add(owningMembership);
            owningMembership.getOwnedRelatedElement().add(usage);
            if (oldMembership instanceof OwningMembership oldOwningMembership) {
                this.deleteService.deleteFromModel(oldOwningMembership);
            }
        }
        return usage;
    }



    /**
     * Return the real parent {@link Node} given the current object and the selectedNode.
     *
     * @param self
     *            the current object.
     * @param selectedNode
     *            the selectedNode (can be a {@link Node} or null (in case of a {@link Diagram}).
     * @return the real parent {@link Node} given the current object and the selectedNode.
     */
    public Object getParentViewExpression(Object self, Object selectedNode) {
        if (self instanceof StateUsage && selectedNode instanceof Node node) {
            var realParentNode = node.getChildNodes().stream().filter(childNode -> childNode.getInsideLabel().getText().contains(STATE_TRANSITION_COMPARTMENT_NAME)).findFirst();
            if (realParentNode.isPresent()) {
                return realParentNode.get();
            }
        }
        return selectedNode;
    }

    /**
     * Service to retrieve the root elements of the selection dialog of the NamespaceImport creation tool.
     *
     * @param editingContext
     *            the editing context
     * @return the list of resources that contain at least one {@link Package}
     */
    public List<Resource> getNamespaceImportSelectionDialogElements(IEditingContext editingContext) {
        var optionalResourceSet = Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(IEMFEditingContext::getDomain)
                .map(EditingDomain::getResourceSet);
        var resources = optionalResourceSet.map(resourceSet -> resourceSet.getResources().stream()
                .filter(resource -> this.containsDirectlyOrIndirectlyInstancesOf(resource, List.of(SysmlPackage.eINSTANCE.getPackage())))
                .toList())
                .orElseGet(ArrayList::new);
        return resources.stream().sorted((r1, r2) -> this.getResourceName(r1).compareTo(this.getResourceName(r2))).toList();
    }

    /**
     * Service to retrieve the children of a given element in the selection dialog of the NamespaceImport creation tool.
     *
     * @param self
     *            an element of the tree
     * @return the list of {@link Package} element found under the given root element.
     */
    public List<Package> getNamespaceImportSelectionDialogChildren(Object self) {
        List<Package> result = new ArrayList<>();
        if (self instanceof Resource resource) {
            resource.getContents().stream()
                    .filter(Element.class::isInstance)
                    .map(Element.class::cast)
                    .forEach(element -> result.addAll(this.findClosestPackageInChildren(element)));
        } else if (self instanceof Package packageElement) {
            packageElement.getOwnedRelationship().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
                    .forEach(membership -> result.addAll(this.findClosestPackageInChildren(membership)));
        }
        return result;
    }

    /**
     * Provides the root elements in the tree of the selection dialog for the StakeholderParameter creation tool.
     *
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @return the (non-{@code null}) {@link List} of all {@link Resource} that contain at least one {@link PartUsage}.
     */
    public List<Resource> getStakeholderSelectionDialogElements(IEditingContext editingContext) {
        return this.getAllResourcesWithInstancesOf(editingContext, List.of(SysmlPackage.eINSTANCE.getPartUsage()));
    }

    /**
     * Provides the children of element in the tree of the selection dialog for the StakeholderParameter creation tool.
     *
     * @param selectionDialogTreeElement
     *            a (non-{@code null}) selection dialog tree element.
     * @return the (non-{@code null}) {@link List} of all children that contain (possibly indirectly) or are
     *         {@link PartUsage}.
     */
    public List<? extends Object> getStakeholderSelectionDialogChildren(Object selectionDialogTreeElement) {
        return this.getChildrenWithInstancesOf(selectionDialogTreeElement, List.of(SysmlPackage.eINSTANCE.getPartUsage()));
    }

    /**
     * Provides the root elements in the tree of the selection dialog for the SubjectParameter creation tool.
     *
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @return the (non-{@code null}) {@link List} of all {@link Resource} that contain at least one {@link Type}.
     */
    public List<Resource> getSubjectSelectionDialogElements(IEditingContext editingContext) {
        return this.getAllResourcesWithInstancesOf(editingContext, List.of(SysmlPackage.eINSTANCE.getType()));
    }

    /**
     * Provides the children of element in the tree of the selection dialog for the SubjectParameter creation tool.
     *
     * @param selectionDialogTreeElement
     *            a (non-{@code null}) selection dialog tree element.
     * @return the (non-{@code null}) {@link List} of all children that contain (possibly indirectly) or are
     *         {@link Usage}.
     */
    public List<? extends Object> getSubjectSelectionDialogChildren(Object selectionDialogTreeElement) {
        return this.getChildrenWithInstancesOf(selectionDialogTreeElement, List.of(SysmlPackage.eINSTANCE.getType()));
    }

    /**
     * Provides the root elements in the tree of the selection dialog for the ActorParameter creation tool.
     *
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @return the (non-{@code null}) {@link List} of all {@link Resource} that contain at least one {@link PartUsage}
     *         or {@link PartDefinition}.
     */
    public List<Resource> getActorSelectionDialogElements(IEditingContext editingContext) {
        return this.getAllResourcesWithInstancesOf(editingContext, List.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getPartDefinition()));
    }

    /**
     * Provides the children of element in the tree of the selection dialog for the ActorParameter creation tool.
     *
     * @param selectionDialogTreeElement
     *            a (non-{@code null}) selection dialog tree element.
     * @return the (non-{@code null}) {@link List} of all children that contain (possibly indirectly) or are
     *         {@link PartUsage} or {@link PartDefinition}.
     */
    public List<? extends Object> getActorSelectionDialogChildren(Object selectionDialogTreeElement) {
        return this.getChildrenWithInstancesOf(selectionDialogTreeElement, List.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getPartDefinition()));
    }

    /**
     * Provides the root elements in the tree of the selection dialog for presenting all existing ActionUsage.
     *
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @return the (non-{@code null}) {@link List} of all {@link Resource} that contain at least one {@link ActionUsage}.
     */
    public List<Resource> getActionReferenceSelectionDialogElements(IEditingContext editingContext) {
        return this.getAllResourcesWithInstancesOf(editingContext, List.of(SysmlPackage.eINSTANCE.getActionUsage()));
    }

    /**
     * Provides the children of element in the tree of the selection dialog for presenting all existing ActionUsage.
     *
     * @param selectionDialogTreeElement
     *            a (non-{@code null}) selection dialog tree element.
     * @return the (non-{@code null}) {@link List} of all children that contain (possibly indirectly) an {@link ActionUsage}.
     */
    public List<? extends Object> getActionReferenceSelectionDialogChildren(Object selectionDialogTreeElement) {
        return this.getChildrenWithInstancesOf(selectionDialogTreeElement, List.of(SysmlPackage.eINSTANCE.getActionUsage()));
    }

    /**
     * Redefine an inherited port.
     *
     * @param inheritedFeature
     *            semantic inherited port
     * @param inheritedGraphicalPort
     *            graphical inherited port
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @param diagramContext
     *            the {@link DiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @return the new {@link PartUsage} that redefines the inherited one.
     */
    public PortUsage redefineInheritedPort(Feature inheritedFeature, IDiagramElement inheritedGraphicalPort, IEditingContext editingContext, DiagramContext diagramContext) {
        Object parentNode = this.getParentNode(inheritedFeature, inheritedGraphicalPort, diagramContext);
        if (parentNode instanceof Node node) {
            var type = this.objectSearchService.getObject(editingContext, node.getTargetObjectId())
                    .filter(Type.class::isInstance)
                    .map(Type.class::cast)
                    .orElse(null);
            if (type != null) {
                PortUsage portUsage = SysmlFactory.eINSTANCE.createPortUsage();
                Membership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
                membership.getOwnedRelatedElement().add(portUsage);
                type.getOwnedRelationship().add(membership);

                Redefinition redefinition = SysmlFactory.eINSTANCE.createRedefinition();
                redefinition.setRedefinedFeature(inheritedFeature);
                redefinition.setRedefiningFeature(portUsage);

                portUsage.getOwnedRelationship().add(redefinition);
                return portUsage;
            }
        }
        return null;
    }

    protected List<Resource> getAllResourcesWithInstancesOf(IEditingContext editingContext, List<EClassifier> eClassifiers) {
        Objects.requireNonNull(editingContext);

        var optResourceSet = Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(IEMFEditingContext::getDomain)
                .map(EditingDomain::getResourceSet);
        var resourcesContainingPartUsage = optResourceSet.map(resourceSet -> resourceSet.getResources().stream()
                .filter(resource -> this.containsDirectlyOrIndirectlyInstancesOf(resource, eClassifiers))
                .toList())
                .orElseGet(ArrayList::new);
        return resourcesContainingPartUsage.stream().sorted(Comparator.comparing(r -> this.getResourceName(r))).toList();
    }

    protected List<? extends Object> getChildrenWithInstancesOf(Object selectionDialogTreeElement, List<EClassifier> eClassifiers) {
        Objects.requireNonNull(selectionDialogTreeElement);

        final List<? extends Object> result;

        if (selectionDialogTreeElement instanceof Resource resource) {
            result = resource.getContents().stream()
                    .filter(content -> eClassifiers.stream().anyMatch(eClassifier -> eClassifier.isInstance(content)) || this.containsDirectlyOrIndirectlyInstancesOf(content, eClassifiers))
                    .toList();
        } else if (selectionDialogTreeElement instanceof Element sysmlElement) {
            return sysmlElement.getOwnedRelationship().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
                    .map(Membership::getOwnedRelatedElement).flatMap(List::stream)
                    .filter(content -> eClassifiers.stream().anyMatch(eClassifier -> eClassifier.isInstance(content)) || this.containsDirectlyOrIndirectlyInstancesOf(content, eClassifiers))
                    .toList();
        } else {
            result = new ArrayList<>();
        }

        return result;
    }

    protected boolean containsDirectlyOrIndirectlyInstancesOf(Resource resource, List<EClassifier> eClassifiers) {
        boolean found = false;
        final Iterator<EObject> allContents = resource.getAllContents();
        while (!found && allContents.hasNext()) {
            var eObject = allContents.next();
            for (EClassifier it : eClassifiers) {
                found = it.isInstance(eObject);
                if (found) {
                    break;
                }
            }
        }
        return found;
    }

    protected boolean containsDirectlyOrIndirectlyInstancesOf(EObject eObject, List<EClassifier> eClassifiers) {
        boolean found = false;
        final Iterator<EObject> allContents = eObject.eAllContents();
        while (!found && allContents.hasNext()) {
            var content = allContents.next();
            for (EClassifier it : eClassifiers) {
                found = it.isInstance(content);
                if (found) {
                    break;
                }
            }
        }
        return found;
    }

    protected String getResourceName(Resource resource) {
        return resource.eAdapters().stream()
                .filter(ResourceMetadataAdapter.class::isInstance)
                .map(ResourceMetadataAdapter.class::cast)
                .findFirst()
                .map(ResourceMetadataAdapter::getName)
                .orElse(resource.getURI().lastSegment());
    }

    protected List<Package> findClosestPackageInChildren(Element element) {
        var result = new ArrayList<Package>();
        if (element instanceof Package packageElement) {
            result.add(packageElement);
        } else if (element instanceof Membership membership) {
            membership.getOwnedRelatedElement()
                    .forEach(child -> result.addAll(this.findClosestPackageInChildren(child)));
        } else {
            element.getOwnedRelationship().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
                    .forEach(membership -> result.addAll(this.findClosestPackageInChildren(membership)));
        }
        return result;
    }

    protected Package getClosestContainingPackageFrom(Element element) {
        var owner = element.eContainer();
        while (!(owner instanceof Package) && owner != null) {
            owner = owner.eContainer();
        }
        return (Package) owner;
    }

    protected List<Element> getOwnerHierarchy(Element element) {
        List<Element> ownerHierarchy = new ArrayList<>();
        Element currentElement = element;
        while (currentElement.getOwner() != null) {
            ownerHierarchy.add(currentElement.getOwner());
            currentElement = currentElement.getOwner();
        }
        return ownerHierarchy;
    }
}
