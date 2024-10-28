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
import org.eclipse.sirius.components.emf.services.EditingContextCrossReferenceAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ExhibitStateUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FlowConnectionUsage;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RenderingUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionFeatureKind;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewUsage;
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
     * Moves the owning membership of {@code element} to {@code parent}.
     * <p>
     * This method may create a new membership in {@code parent}, potentially with a different type than
     * {@code element.getOwningMembership()}. For example, an element moved into a package will have an
     * {@link OwningMembership} instance as its parent, regardless of its original containing membership.
     * </p>
     *
     * @param element
     *            the element that has been dropped
     * @param parent
     *            the element inside which the drop has been performed
     * @return the element
     */
    public Element moveMembership(Element element, Element parent) {
        if (element.eContainer() instanceof Membership currentMembership) {
            DeleteService deleteService = new DeleteService();
            if (parent instanceof Package) {
                // the expected membership should be an OwningMembership
                if (currentMembership instanceof FeatureMembership) {
                    var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                    // Add the new membership to its container first to make sure its content stays in the same
                    // resource. Otherwise the cross-referencer will delete all the references pointing to its related
                    // element, which will have unexpected results on the model.
                    parent.getOwnedRelationship().add(owningMembership);
                    owningMembership.getOwnedRelatedElement().add(element);
                    deleteService.deleteFromModel(currentMembership);
                } else {
                    parent.getOwnedRelationship().add(currentMembership);
                }
            } else {
                // the expected membership should be a FeatureMembership
                if (currentMembership instanceof FeatureMembership) {
                    parent.getOwnedRelationship().add(currentMembership);
                } else {
                    var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
                    parent.getOwnedRelationship().add(featureMembership);
                    featureMembership.getOwnedRelatedElement().add(element);
                    deleteService.deleteFromModel(currentMembership);
                }
            }
        }
        return element;
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
     * Retrieve all exhibited {@link StateUsage} directly accessible from an object.
     *
     * @param eObject
     *            The {@link Element} to start from
     * @return The list of exhibited {@link StateUsage}
     */
    public List<StateUsage> getAllExhibitedStates(Element eObject) {
        List<StateUsage> result = new ArrayList<>();
        List<StateUsage> candidates = new ArrayList<>();
        if (eObject instanceof Usage usage) {
            candidates = usage.getNestedState();
        } else if (eObject instanceof Definition def) {
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
     * object.
     *
     * @param eObject
     *            The {@link Element} to start from
     * @return The list of all {@link StateUsage} elements that are not {@link ExhibitStateUsage}
     */
    public List<StateUsage> getAllNonExhibitStates(Element eObject) {
        List<StateUsage> result = new ArrayList<>();
        List<StateUsage> candidates = new ArrayList<>();
        if (eObject instanceof Usage usage) {
            candidates = usage.getNestedState();
        } else if (eObject instanceof Definition def) {
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

    public List<Membership> getAllStandardStartActions(Namespace self) {
        if (this.isPart(self) || this.isAction(self)) {
            return self.getOwnedRelationship().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
                    .filter(m -> {
                        return m.getMemberElement() instanceof ActionUsage au && this.isStandardStartAction(au);
                    })
                    .toList();
        }
        return List.of();
    }

    public List<Membership> getAllStandardDoneActions(Namespace self) {
        if (this.isPart(self) || this.isAction(self)) {
            return self.getOwnedRelationship().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
                    .filter(m -> {
                        return m.getMemberElement() instanceof ActionUsage au && this.isStandardDoneAction(au);
                    })
                    .toList();
        }
        return List.of();
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

    private boolean isPart(Element element) {
        return element instanceof PartUsage || element instanceof PartDefinition;
    }

    private boolean isAction(Element element) {
        return element instanceof ActionUsage || element instanceof ActionDefinition;
    }

    /**
     * Return an instance of the actual {@link PartDefinition} associated to the given {@link PartUsage}.
     * The type of the given parameter may be a subclass of {@link PartUsage}, in that case the method
     * returns the definition associated to the actual instance type.
     *
     * @param partUsage
     *         an instance of PartUsage
     * @return an actual part definition instance associated to the given part usage.
     */
    public Definition createPartDefinitionFrom(PartUsage partUsage) {
        return (Definition) SysmlFactory.eINSTANCE.create(this.getPartDefinitionEClassFrom(partUsage));
    }

    /**
     * Return the PartDefinition {@link EClass} corresponding to the given PartUsage.
     * The type of the given parameter may be a subclass of {@link PartUsage},
     * the method returns the {@link EClass} of the corresponding part definition.
     *
     * @param partUsage a PartUsage instance
     * @return the EClass of the PartDefinition associated to the given PartUsage.
     */
    public EClass getPartDefinitionEClassFrom(PartUsage partUsage) {
        EClass result = SysmlPackage.eINSTANCE.getPartDefinition();
        if (partUsage instanceof FlowConnectionUsage) {
            result = SysmlPackage.eINSTANCE.getFlowConnectionDefinition();
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
}
