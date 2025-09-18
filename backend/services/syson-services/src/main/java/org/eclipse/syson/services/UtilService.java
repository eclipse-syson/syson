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
package org.eclipse.syson.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.antlr.v4.runtime.atn.Transition;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.services.EditingContextCrossReferenceAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.ExhibitStateUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.FlowEnd;
import org.eclipse.syson.sysml.FlowUsage;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RenderingUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionFeatureKind;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.NameHelper;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.SysONEContentAdapter;

/**
 * Miscellaneous Java services used by the SysON views.
 *
 * @author arichard
 */
public class UtilService {

    private final ElementInitializerSwitch elementInitializerSwitch = new ElementInitializerSwitch();

    private final ElementUtil elementUtil = new ElementUtil();

    /**
     * Check if the given {@link Element} comes from a standard library (i.e. a {@link LibraryPackage} with its standard
     * attribute set to true) or not.
     *
     * @param element
     *            the given {@link Element}.
     * @return <code>true</code> if the given element is contained in a standard library, <code>false</code> otherwise.
     */
    public boolean isFromStandardLibrary(Element element) {
        return ElementUtil.isFromStandardLibrary(element);
    }

    /**
     * Return the {@link Package} containing the given {@link EObject}.
     *
     * @param element
     *            the {@link EObject} element.
     * @return the {@link Package} containing the given {@link EObject}.
     */
    public org.eclipse.syson.sysml.Package getContainerPackage(EObject element) {
        org.eclipse.syson.sysml.Package pack = null;
        if (element instanceof org.eclipse.syson.sysml.Package pkg) {
            pack = pkg;
        } else if (element != null) {
            return this.getContainerPackage(element.eContainer());
        }
        return pack;
    }

    /**
     * Return the {@link PartUsage} containing the given {@link PortUsage}.
     *
     * @param element
     *            the portUsage {@link PortUsage}.
     * @return the {@link PartUsage} containing the given {@link PortUsage} if found, <code>null</code> otherwise.
     */
    public PartUsage getContainerPart(PortUsage portUsage) {
        PartUsage containerPart = null;
        Usage owningUsage = portUsage.getOwningUsage();
        if (owningUsage instanceof PartUsage partUsage) {
            containerPart = partUsage;
        }
        return containerPart;
    }

    /**
     * Returns the element owning the provided {@code element}.
     * <p>
     * The owner of an element can be accessed through various methods, depending on the type of the element (e.g.
     * {@code getOwner} by default, but {@code getOwningRelatedElement} for relationships). This method provides a
     * single entry point to retrieve the owner of any element.
     * </p>
     *
     * @param element
     *            the element to get the owner from
     * @return the owning element, or {@code null} if {@code element} doesn't have an owner
     */
    public Element getOwningElement(Element element) {
        Element owner = element.getOwner();
        if (owner == null && element instanceof Relationship relationship) {
            owner = relationship.getOwningRelatedElement();
        }
        return owner;
    }

    /**
     * When an accept action usage is directly or indirectly a composite feature of a part definition or usage, then the
     * default for the receiver (via) of the accept action usage is the containing part, not the accept action itself.
     * This is known as the default accepting context.
     *
     * @param aau
     *            the acceptActionUsage {@link AcceptActionUsage}.
     * @return the first container that is a {@link Definition} or a {@link Usage} for the given
     *         {@link AcceptActionUsage} if found, <code>null</code> otherwise.
     */
    public Type getReceiverContainerDefinitionOrUsage(AcceptActionUsage aau) {
        EObject container = aau.eContainer();
        while (container != null) {
            if (container instanceof Definition || container instanceof Usage) {
                break;
            }
            container = container.eContainer();
        }
        if (container instanceof Definition || container instanceof Usage) {
            return (Type) container;
        }
        return null;
    }

    /**
     * Retrieve all exhibited {@link StateUsage} directly accessible from an element.
     *
     * @param element
     *            The {@link Element} to start from
     * @return The list of exhibited {@link StateUsage}
     */
    public List<StateUsage> getAllExhibitedStates(Element element) {
        List<StateUsage> result = new ArrayList<>();
        List<StateUsage> candidates = new ArrayList<>();
        if (element instanceof Usage usage) {
            candidates = usage.getNestedState();
        } else if (element instanceof Definition def) {
            candidates = def.getOwnedState();
        }
        candidates.stream().filter(ExhibitStateUsage.class::isInstance)
                .map(ExhibitStateUsage.class::cast)
                .filter(Objects::nonNull)
                .forEach(result::add);
        return result;
    }

    /**
     * Retrieve all {@link StateUsage} elements that are not {@link ExhibitStateUsage} directly accessible from an
     * element.
     *
     * @param element
     *            The {@link Element} to start from
     * @return The list of all {@link StateUsage} elements that are not {@link ExhibitStateUsage}
     */
    public List<StateUsage> getAllNonExhibitStates(Element element) {
        List<StateUsage> result = new ArrayList<>();
        List<StateUsage> candidates = new ArrayList<>();
        if (element instanceof Usage usage) {
            candidates = usage.getNestedState();
        } else if (element instanceof Definition def) {
            candidates = def.getOwnedState();
        }
        candidates.stream().filter(c -> !(c instanceof ExhibitStateUsage))
                .map(StateUsage.class::cast)
                .filter(Objects::nonNull)
                .forEach(result::add);
        return result;
    }

    /**
     * Get the AQL service expression getting all reachable elements based on the provided {@code domainType} type.
     *
     * @param domainType
     *            A type to be converted as an EClass name
     * @return An AQL expression calling {@code self.getAllReachable(domainType)}
     */
    public String getAllReachableExpression(String domainType) {
        return AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType);
    }

    /**
     * Get all reachable elements of a type in the {@link ResourceSet} of given {@link EObject}.
     *
     * @param eObject
     *            the {@link EObject} stored in a {@link ResourceSet}
     * @param type
     *            the search typed (either simple or qualified named of the EClass ("Package" vs "sysml::Package")
     * @return a list of reachable object
     */
    public List<EObject> getAllReachable(EObject eObject, String type) {
        EClass eClass = SysMLMetamodelHelper.toEClass(type);
        return this.getAllReachable(eObject, eClass);
    }

    /**
     * Get all reachable elements of the type given by the {@link EClass} in the {@link ResourceSet} of the given
     * {@link EObject}.
     *
     * @param eObject
     *            the {@link EObject} stored in a {@link ResourceSet}
     * @param eClass
     *            the searched {@link EClass}
     * @return a list of reachable object
     */
    public List<EObject> getAllReachable(EObject eObject, EClass eClass) {
        List<EObject> allReachable = new ArrayList<>();
        Adapter adapter = EcoreUtil.getAdapter(eObject.eAdapters(), SysONEContentAdapter.class);
        if (adapter instanceof SysONEContentAdapter cacheAdapter) {
            List<EObject> cacheResult = cacheAdapter.getCache().get(eClass);
            if (cacheResult != null) {
                allReachable.addAll(cacheResult);
            }
        }
        return allReachable;
    }

    /**
     * Get all reachable elements of the type given by the {@link EClass} in the {@link ResourceSet} of the given
     * {@link EObject}.
     *
     * @param eObject
     *            the {@link EObject} stored in a {@link ResourceSet}
     * @param eClass
     *            the searched {@link EClass}
     * @param withStandardLibs
     *            include standard libraries elements into list result
     * @return a list of reachable object
     */
    public List<EObject> getAllReachable(EObject eObject, EClass eClass, boolean withStandardLibs) {
        List<EObject> allReachable = new ArrayList<>();
        if (withStandardLibs) {
            return this.getAllReachable(eObject, eClass);
        }
        var userResources = eObject.eResource().getResourceSet().getResources().stream()
                .filter(r -> r.getURI().toString().startsWith(IEMFEditingContext.RESOURCE_SCHEME))
                .toList();
        for (Resource resource : userResources) {
            var contents = resource.getContents();
            for (EObject root : contents) {
                TreeIterator<EObject> eAllContents = root.eAllContents();
                while (eAllContents.hasNext()) {
                    EObject obj = eAllContents.next();
                    if (eClass.isInstance(obj)) {
                        allReachable.add(obj);
                    }
                }
            }
        }
        return allReachable;
    }

    /**
     * Retrieve all exhibited {@link StateUsage} directly accessible from an object which are not referential
     * {@link ExhibitStateUsage}.
     *
     * @param eObject
     *            The {@link Element} to start from
     * @return
     */
    public List<StateUsage> getAllReachableStatesWithoutReferentialExhibit(Element eObject) {
        List<StateUsage> result = new ArrayList<>();
        this.getAllReachable(eObject, SysmlPackage.eINSTANCE.getExhibitStateUsage()).stream().forEach(su -> {
            if (((ExhibitStateUsage) su).getExhibitedState() == null) {
                result.add((StateUsage) su);
            }
        });
        this.getAllReachable(eObject, SysmlPackage.eINSTANCE.getStateUsage()).stream().forEach(su -> {
            result.add((StateUsage) su);
        });
        return result;
    }


    /**
     * Collects all the sources and targets of all {@link SuccessionAsUsage} contained in the given {@link Type}.
     *
     * @param type
     *            the type for which we want to collect all the sources and targets of all {@link SuccessionAsUsage}
     * @return a list of {@link Element}s
     */
    public List<Element> collectSuccessionSourceAndTarget(Type type) {
        List<Element> result = new ArrayList<>();
        var successions = type.getOwnedFeature().stream()
                .filter(SuccessionAsUsage.class::isInstance)
                .map(SuccessionAsUsage.class::cast)
                .toList();

        for (SuccessionAsUsage succession : successions) {
            Feature source = succession.getSourceFeature();
            if (source != null) {
                result.add(source);
            }
            var target = succession.getTargetFeature();
            if (target != null && !target.isEmpty()) {
                result.addAll(target.stream().filter(Objects::nonNull).toList());
            }
        }

        type.getOwnedFeature().stream()
                .filter(TransitionUsage.class::isInstance)
                .map(TransitionUsage.class::cast)
                .forEach(transition -> {
                    ActionUsage source = transition.getSource();
                    if (source != null) {
                        result.add(source);
                    }
                    ActionUsage target = transition.getTarget();
                    if (target != null) {
                        result.add(target);
                    }
                });
        return result;
    }

    /**
     * Retrieve all elements referencing {@code eObject} through an {@link EStructuralFeature}.
     *
     * @param eObject
     *            The referenced {@link EObject}
     * @param eStructuralFeature
     *            The {@link EStructuralFeature} used for referencing
     * @return all elements referencing {@code eObject} through an {@link EStructuralFeature}
     */
    public List<EObject> getEInverseRelatedElements(EObject eObject, EStructuralFeature eStructuralFeature) {
        List<EObject> result = new ArrayList<>();
        var optAdapter = eObject.eAdapters().stream().filter(EditingContextCrossReferenceAdapter.class::isInstance).map(EditingContextCrossReferenceAdapter.class::cast).findFirst();
        if (optAdapter.isPresent()) {
            EditingContextCrossReferenceAdapter referenceAdapter = optAdapter.get();
            referenceAdapter.getInverseReferences(eObject).stream().filter(set -> set.getEStructuralFeature().equals(eStructuralFeature)).forEach(set -> result.add(set.getEObject()));
        }
        return result;
    }

    /**
     * Find an {@link Element} that match the given name in the ResourceSet of the given element.
     *
     * @param object
     *            the object for which to find a corresponding type.
     * @param elementName
     *            the element name to match.
     * @return the found element or <code>null</code>.
     */
    public <T extends Element> T findByName(EObject object, String elementName) {
        T result = null;
        Namespace namespace = null;
        if (object instanceof Element element) {
            namespace = element.getOwningNamespace();
        } else if (object instanceof Relationship relationship && relationship.getOwner() != null) {
            namespace = relationship.getOwner().getOwningNamespace();
        }
        if (namespace != null) {
            var membership = namespace.resolve(elementName);
            if (membership != null) {
                result = (T) membership.getMemberElement();
            }
        }
        return result;
    }

    /**
     * Find an {@link Element} that match the given name and type in the ResourceSet of the given element.
     *
     * @param object
     *            the object for which to find a corresponding type.
     * @param elementName
     *            the element name to match.
     * @param elementType
     *            the type to match.
     * @return the found element or <code>null</code>.
     */
    public <T extends Element> T findByNameAndType(EObject object, String elementName, Class<T> elementType) {
        return this.elementUtil.findByNameAndType(object, elementName, elementType);
    }

    /**
     * Iterate over the given {@link Collection} of root elements to find a element with the given name and type.
     *
     * @param roots
     *            the elements to inspect.
     * @param elementName
     *            the name to match.
     * @param elementType
     *            the type to match.
     * @return the found element or <code>null</code>.
     */
    public <T extends Element> T findByNameAndType(Collection<EObject> roots, String elementName, Class<T> elementType) {
        return this.elementUtil.findByNameAndType(roots, elementName, elementType);
    }

    /**
     * Returns {@code true} if the provided {@code element} is a root {@link Namespace}.
     * <p>
     * A root namespace is a strict instance of {@link Namespace}, with no owner and no name. These namespace are
     * implicit and at the root of each SysON project.
     * </p>
     *
     * @param element
     *            the element to check
     * @return {@code true} if the provided {@code element} is a root {@link Namespace}
     */
    public boolean isRootNamespace(Element element) {
        return element.eClass() == SysmlPackage.eINSTANCE.getNamespace()
                && element.getOwner() == null
                && element.getName() == null;
    }

    /**
     * Returns the libraries contained in the root namespace of the provided {@code resource}.
     *
     * @param resource
     *            the {@link Resource} to retrieve the libraries from
     * @param isStandard
     *            whether to find standard libraries or not
     * @return the libraries
     */
    public List<LibraryPackage> getLibraries(Resource resource, boolean isStandard) {
        return resource.getContents().stream()
                .filter(Namespace.class::isInstance)
                .map(Namespace.class::cast)
                .filter(this::isRootNamespace)
                .flatMap(namespace -> namespace.getOwnedElement().stream())
                .filter(LibraryPackage.class::isInstance)
                .map(LibraryPackage.class::cast)
                .filter(libraryPackage -> libraryPackage.isIsStandard() == isStandard)
                .toList();
    }

    /**
     * Create a child Action onto {@code stateDefinition}.
     *
     * @param parentState
     *            The parent {@link StateDefinition} or {@link StateUsage}
     * @param actionKind
     *            The string value representing the kind of Action. Expected values are "Entry", "Do" or "Exit".
     * @return The created {@link ActionUsage} or null if the kind value is not correct
     */
    public ActionUsage createChildAction(Element parentState, String actionKind) {
        var owningMembership = SysmlFactory.eINSTANCE.createStateSubactionMembership();
        switch (actionKind) {
            case "Entry":
                owningMembership.setKind(StateSubactionKind.ENTRY);
                break;
            case "Do":
                owningMembership.setKind(StateSubactionKind.DO);
                break;
            case "Exit":
                owningMembership.setKind(StateSubactionKind.EXIT);
                break;
            default:
                return null;
        }
        ActionUsage childAction = SysmlFactory.eINSTANCE.createActionUsage();
        owningMembership.getOwnedRelatedElement().add(childAction);
        parentState.getOwnedRelationship().add(owningMembership);
        this.elementInitializerSwitch.doSwitch(childAction);
        return childAction;
    }

    /**
     * Create a child State onto the given parent.
     *
     * @param parentState
     *            The parent {@link StateDefinition} or {@link StateUsage}
     * @param isParallel
     *            Whether the created state is parallel or not
     * @param isExhibit
     *            Whether the created state is exhibited or not
     * @return the created {@link StateUsage}.
     */
    public StateUsage createChildState(Element parentState, boolean isParallel, boolean isExhibit) {
        var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        StateUsage childState;
        if (isExhibit) {
            childState = SysmlFactory.eINSTANCE.createExhibitStateUsage();
        } else {
            childState = SysmlFactory.eINSTANCE.createStateUsage();
        }
        childState.setIsParallel(isParallel);
        featureMembership.getOwnedRelatedElement().add(childState);
        parentState.getOwnedRelationship().add(featureMembership);
        this.elementInitializerSwitch.doSwitch(childState);
        return childState;
    }

    /**
     * Create a new EndFeatureMembership to be used as {@link FlowUsage} end.
     *
     * @param targetedFeature
     *            the targeted feature (either the source or target of the flow)
     * @return the new EndFeatureMembership
     */
    public EndFeatureMembership createFlowConnectionEnd(Feature targetedFeature) {
        EndFeatureMembership featureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();

        FlowEnd flowEnd = SysmlFactory.eINSTANCE.createFlowEnd();
        featureMembership.getOwnedRelatedElement().add(flowEnd);

        Type owningType = targetedFeature.getOwningType();
        if (owningType instanceof Feature owningFeature) {
            var referenceSubSetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
            flowEnd.getOwnedRelationship().add(referenceSubSetting);
            referenceSubSetting.setReferencedFeature(owningFeature);
        }

        EndFeatureMembership target = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        flowEnd.getOwnedRelationship().add(target);

        ReferenceUsage referenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
        target.getOwnedRelatedElement().add(referenceUsage);

        Redefinition redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        redefinition.setRedefinedFeature(targetedFeature);
        redefinition.setRedefiningFeature(referenceUsage);

        referenceUsage.getOwnedRelationship().add(redefinition);

        return featureMembership;
    }

    /**
     * Checks whether {@code element} is a Parallel state. This method allows an {@link Element} as a parameter but is
     * intended to be called either with a {@link StateUsage} or a {@link StateDefinition}. Will return false in the
     * other cases.
     *
     * @param element
     *            The element to check
     */
    public boolean isParallelState(Element element) {
        return (element instanceof StateUsage su && su.isIsParallel()) || (element instanceof StateDefinition sd && sd.isIsParallel());
    }

    /**
     * Check if the given element name is a qualified name.
     *
     * @param elementName
     *            the given element name.
     * @return <code>true</code> if the given element name is a qualified name, <code>false</code> otherwise.
     */
    public boolean isQualifiedName(String elementName) {
        List<String> segments = NameHelper.parseQualifiedName(elementName);
        return segments.size() > 1;
    }

    /**
     * Remove {@link TransitionFeatureMembership} elements of specific kind from the provided {@link Transition}.
     *
     * @param transition
     *            The transition to modify
     * @param kind
     *            The {@link TransitionFeatureKind} used to identify {@link TransitionFeatureMembership} to remove
     */
    public void removeTransitionFeaturesOfSpecificKind(TransitionUsage transition, TransitionFeatureKind kind) {
        List<TransitionFeatureMembership> elementsToDelete = transition.getOwnedFeatureMembership().stream()
                .filter(TransitionFeatureMembership.class::isInstance)
                .map(TransitionFeatureMembership.class::cast)
                .filter(tfm -> tfm.getKind().equals(kind))
                .toList();
        EcoreUtil.removeAll(elementsToDelete);
    }

    /**
     * Retrieve the start action defined inside the standard library <code>Actions</code>.
     *
     * @param eObject
     *            an object to access to the library resources.
     *
     * @return the standard start ActionUsage defined in the <code>Actions</code> library.
     */
    public ActionUsage retrieveStandardStartAction(Element eObject) {
        return this.findByName(eObject, "Actions::Action::start");
    }

    /**
     * Retrieve the done action defined inside the standard library <code>Actions</code>.
     *
     * @param eObject
     *            an object to access to the library resources.
     *
     * @return the standard done ActionUsage defined in the <code>Actions</code> library.
     */
    public ActionUsage retrieveStandardDoneAction(Element eObject) {
        return this.findByName(eObject, "Actions::Action::done");
    }

    /**
     * Check if the given element is actually the standard start action defined by <code>Actions::Action::start</code>.
     *
     * @param element
     *            an element that could be the standard start action.
     * @return <code>true</code> if the given element is the standard start action and <code>false</code> otherwise.
     */
    public boolean isStandardStartAction(Element element) {
        var elt = element;
        if (element instanceof Membership membership) {
            elt = membership.getMemberElement();
        }
        if (elt instanceof ActionUsage au) {
            return "9a0d2905-0f9c-5bb4-af74-9780d6db1817".equals(au.getElementId());
        }
        return false;
    }

    /**
     * Check if the given element is actually the standard done action defined by <code>Actions::Action::done</code>.
     *
     * @param element
     *            an element that could be the standard done action.
     * @return <code>true</code> if the given element is the standard done action and <code>false</code> otherwise.
     */
    public boolean isStandardDoneAction(Element element) {
        var elt = element;
        if (element instanceof Membership membership) {
            elt = membership.getMemberElement();
        }
        if (elt instanceof ActionUsage au) {
            return "0cdc3cd3-b06c-5c32-beda-0cf4ba164a64".equals(au.getElementId());
        }
        return false;
    }

    /**
     * Configures {@code usage} to be typed by {@code type}.
     *
     * @param usage
     *            the usage that is typed by the given type
     * @param type
     *            the type
     * @return the configured {@code usage}
     */
    public Usage setFeatureTyping(Usage usage, Type type) {
        var optConjugatedPortTyping = usage.getOwnedRelationship().stream()
                .filter(ConjugatedPortTyping.class::isInstance)
                .map(ConjugatedPortTyping.class::cast)
                .findFirst();
        if (optConjugatedPortTyping.isPresent()) {
            EcoreUtil.remove(optConjugatedPortTyping.get());
        }
        var optFeatureTyping = usage.getOwnedRelationship().stream()
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .findFirst();
        if (optFeatureTyping.isPresent()) {
            FeatureTyping featureTyping = optFeatureTyping.get();
            if (!type.equals(featureTyping.getType())) {
                featureTyping.setType(type);
                featureTyping.setIsImplied(false);
            }
            featureTyping.setTypedFeature(usage);
        } else {
            var newFeatureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
            usage.getOwnedRelationship().add(newFeatureTyping);
            newFeatureTyping.setType(type);
            newFeatureTyping.setTypedFeature(usage);
            this.elementInitializerSwitch.doSwitch(newFeatureTyping);
        }
        return usage;
    }

    /**
     * Configures {@code subsettingUsage} to subset {@code feature}.
     *
     * @param subsettingUsage
     *            the usage that sub-classify the classifier
     * @param feature
     *            the subsetted feature
     * @return the configured {@code subsettingUsage}
     */
    public Usage setSubsetting(Usage subsettingUsage, Feature feature) {
        var optSubsetting = subsettingUsage.getOwnedRelationship().stream()
                .filter(elt -> elt instanceof Subsetting && !(elt instanceof Redefinition))
                .map(Subsetting.class::cast)
                .findFirst();
        if (optSubsetting.isPresent()) {
            Subsetting subsetting = optSubsetting.get();
            if (!feature.equals(subsetting.getSubsettedFeature())) {
                subsetting.setSubsettedFeature(feature);
                subsetting.setIsImplied(false);
            }
            subsetting.setSubsettingFeature(subsettingUsage);
        } else {
            var newSubsetting = SysmlFactory.eINSTANCE.createSubsetting();
            subsettingUsage.getOwnedRelationship().add(newSubsetting);
            newSubsetting.setSubsettedFeature(feature);
            newSubsetting.setSubsettingFeature(subsettingUsage);
            this.elementInitializerSwitch.caseSubsetting(newSubsetting);
        }
        return subsettingUsage;
    }

    /**
     * Configures {@code subclassifyingDefinition} to sub-classify {@code classifier}.
     *
     * @param subclassifyingDefinition
     *            the definition that sub-classify the classifier
     * @param classifier
     *            the sub-classified classifier
     * @return the configured {@code subclassifyingDefinition}
     */
    public Definition setSubclassification(Definition subclassifyingDefinition, Classifier classifier) {
        var optSubclassification = subclassifyingDefinition.getOwnedRelationship().stream()
                .filter(Subclassification.class::isInstance)
                .map(Subclassification.class::cast)
                .findFirst();
        if (optSubclassification.isPresent()) {
            Subclassification subclassification = optSubclassification.get();
            if (!classifier.equals(subclassification.getSuperclassifier())) {
                subclassification.setSuperclassifier(classifier);
                subclassification.setIsImplied(false);
            }
            subclassification.setSubclassifier(subclassifyingDefinition);
        } else {
            var newSubclassification = SysmlFactory.eINSTANCE.createSubclassification();
            subclassifyingDefinition.getOwnedRelationship().add(newSubclassification);
            newSubclassification.setSuperclassifier(classifier);
            newSubclassification.setSubclassifier(subclassifyingDefinition);
            this.elementInitializerSwitch.caseSubclassification(newSubclassification);
        }
        return subclassifyingDefinition;
    }

    /**
     * Configures {@code redefiningUsage} to redefine {@code feature}.
     *
     * @param redefiningUsage
     *            the usage that redefines the feature
     * @param feature
     *            the redefined feature
     * @return the configured {@code redefiningUsage}
     */
    public Usage setRedefinition(Usage redefiningUsage, Feature feature) {
        var optRedefinition = redefiningUsage.getOwnedRelationship().stream()
                .filter(Redefinition.class::isInstance)
                .map(Redefinition.class::cast)
                .findFirst();
        if (optRedefinition.isPresent()) {
            Redefinition redefinition = optRedefinition.get();
            if (!feature.equals(redefinition.getRedefinedFeature())) {
                redefinition.setRedefinedFeature(feature);
                redefinition.setIsImplied(false);
            }
            redefinition.setRedefiningFeature(redefiningUsage);
        } else {
            var newRedefinition = SysmlFactory.eINSTANCE.createRedefinition();
            redefiningUsage.getOwnedRelationship().add(newRedefinition);
            newRedefinition.setRedefinedFeature(feature);
            newRedefinition.setRedefiningFeature(redefiningUsage);
            this.elementInitializerSwitch.caseRedefinition(newRedefinition);
        }
        return redefiningUsage;
    }

    /**
     * Return an instance of the actual {@link PartDefinition} associated to the given {@link PartUsage}. The type of
     * the given parameter may be a subclass of {@link PartUsage}, in that case the method returns the definition
     * associated to the actual instance type.
     *
     * @param partUsage
     *            an instance of PartUsage
     * @return an actual part definition instance associated to the given part usage.
     */
    public Definition createPartDefinitionFrom(PartUsage partUsage) {
        return (Definition) SysmlFactory.eINSTANCE.create(this.getPartDefinitionEClassFrom(partUsage));
    }

    /**
     * Return the PartDefinition {@link EClass} corresponding to the given PartUsage. The type of the given parameter
     * may be a subclass of {@link PartUsage}, the method returns the {@link EClass} of the corresponding part
     * definition.
     *
     * @param partUsage
     *            a PartUsage instance
     * @return the EClass of the PartDefinition associated to the given PartUsage.
     */
    public EClass getPartDefinitionEClassFrom(PartUsage partUsage) {
        EClass result = SysmlPackage.eINSTANCE.getPartDefinition();
        if (partUsage instanceof FlowUsage) {
            result = SysmlPackage.eINSTANCE.getFlowDefinition();
        } else if (partUsage instanceof AllocationUsage) {
            result = SysmlPackage.eINSTANCE.getAllocationDefinition();
        } else if (partUsage instanceof InterfaceUsage) {
            result = SysmlPackage.eINSTANCE.getInterfaceDefinition();
        } else if (partUsage instanceof ConnectionUsage) {
            result = SysmlPackage.eINSTANCE.getConnectionDefinition();
        } else if (partUsage instanceof RenderingUsage) {
            result = SysmlPackage.eINSTANCE.getRenderingDefinition();
        } else if (partUsage instanceof ViewUsage) {
            result = SysmlPackage.eINSTANCE.getViewDefinition();
        }
        return result;
    }

    public boolean isInFeature(Feature feature) {
        return FeatureDirectionKind.IN.equals(feature.getDirection());
    }

    public boolean isOutFeature(Feature feature) {
        return FeatureDirectionKind.OUT.equals(feature.getDirection());
    }

    public boolean isInOutFeature(Feature feature) {
        return FeatureDirectionKind.INOUT.equals(feature.getDirection());
    }

    /**
     * Retrieve all {@link org.eclipse.syson.sysml.PerformActionUsage} elements that are directly accessible from an
     * element.
     *
     * @param element
     *            The {@link Element} to start from
     * @return The list of all {@link PerformActionUsage} elements
     */
    public List<PerformActionUsage> getAllPerformActions(Element element) {
        List<ActionUsage> candidates = new ArrayList<>();
        if (element instanceof Usage usage) {
            candidates = usage.getNestedAction();
        } else if (element instanceof Definition def) {
            candidates = def.getOwnedAction();
        }
        return candidates.stream()
                .filter(PerformActionUsage.class::isInstance)
                .map(PerformActionUsage.class::cast)
                .filter(performActionUsage -> !(performActionUsage instanceof ExhibitStateUsage))
                .toList();
    }

    /**
     * Gets the closest common container for both given {@link Feature}. If none is found prefer the source owning type
     * as a container. In last resort use the target owning type.
     *
     * @param f1
     *            the first feature
     * @param f2
     *            the second feature
     * @return the common type or <code>null</code>
     */
    public Type getConnectorContainer(Feature f1, Feature f2) {
        Type sourceType = f1.getOwningType();
        Type targetType = f2.getOwningType();
        Type container = EMFUtils.getLeastCommonContainer(Type.class, sourceType, targetType);

        if (container == null) {
            container = sourceType;
            if (container == null) {
                container = targetType;
            }
        }

        return container;
    }

    /**
     * Get the {@link FeatureValue} contained inside a given {@link Feature}.
     *
     * @param feature
     *            a given {@link Feature}.
     * @return a {@link FeatureValue}, or <code>null</code> if not found.
     */
    public FeatureValue getValuation(Feature feature) {
        return feature.getOwnedMembership().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * Sets the connector ends of a {@link ConnectorAsUsage}.
     *
     * @param connectorAsUsage
     *            The connector to configure
     * @param source
     *            the source of the connector
     * @param target
     *            the target of the connector
     * @param connectorContainer
     *            the container of the connector. Use to compute the path from the feature path from the connector and
     *            its ends
     */
    public void setConnectorEnds(ConnectorAsUsage connectorAsUsage, Feature source, Feature target, Type connectorContainer) {
        this.addConnectorEnd(connectorAsUsage, source, connectorContainer);
        this.addConnectorEnd(connectorAsUsage, target, connectorContainer);
    }

    /**
     * This service is called by most of the tools. It allows the get the real semantic element container of the tool
     * executed. If the given element is a ViewUsage, then it should be the owner of the ViewUsage, otherwise it should
     * be the given element itself.
     *
     * @param element
     *            the given {@link Element}.
     * @return the newly created {@link Membership}.
     */
    public Element getViewUsageOwner(Element element) {
        Element viewUsageOwner = element;
        if (element instanceof ViewUsage viewUsage) {
            viewUsageOwner = viewUsage.getOwner();
        }
        return viewUsageOwner;
    }

    /**
     * Return the {@link ViewDefinitionKind} corresponding to the given {@link Element} displayed in the given
     * {@link IDiagramContext}.
     *
     * @param element
     *            the given {@link Element}.
     * @param ancestors
     *            the list of ancestors of the element (semantic elements corresponding to graphical ancestors). It
     *            corresponds to a variable accessible from the variable manager.
     * @param editingContext
     *            the {@link IEditingContext} of the element. It corresponds to a variable accessible from the variable
     *            manager.
     * @return true if the given {@link Element} displayed in the given {@link IDiagramContext} is of the given
     *         {@link ViewDefinition}, false otherwise.
     */
    public ViewDefinitionKind getViewDefinitionKind(Element element, List<Object> ancestors, IEditingContext editingContext) {
        ViewDefinitionKind kind = ViewDefinitionKind.GENERAL_VIEW;
        if (element instanceof ViewUsage viewUsage) {
            var types = viewUsage.getType();
            if (types != null && !types.isEmpty()) {
                Type type = types.get(0);
                kind = ViewDefinitionKind.getKind(type.getQualifiedName());
            }
        } else {
            // Retrieve ViewUsage exposing the given element
            var viewUsageContainingElement = ancestors.stream().filter(ViewUsage.class::isInstance).map(ViewUsage.class::cast).findFirst();
            if (viewUsageContainingElement.isPresent()) {
                ViewUsage viewUsage = viewUsageContainingElement.get();
                kind = this.getViewDefinitionKind(viewUsage, List.of(), editingContext);
            }
        }
        return kind;
    }

    /**
     * Check whether the given element(that should be a {@link UseCaseDefinition} or a {@link UseCaseUsage}) contains an
     * objective requirement or not.
     *
     * @param self
     *            a {@link UseCaseDefinition} or a {@link UseCaseUsage} in which the objective is looked for
     * @return {@code true} if the given use case contains an objective and {@code false} otherwise.
     */
    public boolean isEmptyObjectiveRequirement(Element self) {
        return self.getOwnedRelationship().stream()
                .filter(ObjectiveMembership.class::isInstance)
                .map(ObjectiveMembership.class::cast)
                .findFirst()
                .isEmpty();
    }

    /**
     * Count the number of existing ViewUsages inside the given Namespace.
     *
     * @param namespace
     *            the given {@link Namespace}.
     * @return the number of existing ViewUsages inside the given Namespace.
     */
    public long existingViewUsagesCountForRepresentationCreation(Namespace namespace) {
        return this.elementUtil.existingViewUsagesCountForRepresentationCreation(namespace);
    }

    private ReferenceUsage addConnectorEnd(ConnectorAsUsage connectorAsUsage, Feature end, Type connectorContainer) {
        FeatureChainComputer cmp = new FeatureChainComputer();
        List<Feature> sourceFeaturePath = cmp.computeShortestPath(connectorContainer, end).orElse(List.of());
        EndFeatureMembership sourceEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        connectorAsUsage.getOwnedRelationship().add(sourceEndFeatureMembership);
        ReferenceUsage sourceFeature = SysmlFactory.eINSTANCE.createReferenceUsage();
        sourceFeature.setIsEnd(true);
        sourceEndFeatureMembership.getOwnedRelatedElement().add(sourceFeature);
        this.elementInitializerSwitch.doSwitch(sourceFeature);
        ReferenceSubsetting sourceReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        sourceFeature.getOwnedRelationship().add(sourceReferenceSubsetting);
        this.elementInitializerSwitch.doSwitch(sourceReferenceSubsetting);
        if (sourceFeaturePath.isEmpty() || sourceFeaturePath.size() == 1) {
            // If no path found create a direct reference. The model will not be valid but keep track of the desire
            // target
            sourceReferenceSubsetting.setReferencedFeature(end);
        } else {
            // We need to create a feature chain here
            Feature sourceFeatureChain = SysmlFactory.eINSTANCE.createFeature();
            for (Feature chainedFeature : sourceFeaturePath) {
                FeatureChaining featureChaining = SysmlFactory.eINSTANCE.createFeatureChaining();
                sourceFeatureChain.getOwnedRelationship().add(featureChaining);
                featureChaining.setChainingFeature(chainedFeature);
            }
            sourceReferenceSubsetting.setReferencedFeature(sourceFeatureChain);
            sourceReferenceSubsetting.getOwnedRelatedElement().add(sourceFeatureChain);
        }
        return sourceFeature;
    }
}
