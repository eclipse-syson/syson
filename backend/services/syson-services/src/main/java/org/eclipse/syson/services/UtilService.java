/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Miscellaneous Java services used by the SysON views.
 *
 * @author arichard
 */
public class UtilService {

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
     * Get all reachable elements of a type in the {@link ResourceSet} of given {@link EObject}.
     *
     * @param eObject
     *            the {@link EObject} stored in a {@link ResourceSet}
     * @param type
     *            the search typed (either simple or qualified named of the EClass ("Package" vs "sysml::Package")
     * @return a list of reachable object
     */
    public List<EObject> getAllReachable(EObject eObject, String type) {
        return this.getAllReachable(eObject, type, true);
    }

    /**
     * Get all reachable elements of a type in the {@link ResourceSet} of the given {@link EObject}.
     *
     * @param eObject
     *            the {@link EObject} stored in a {@link ResourceSet}
     * @param type
     *            the searched type (either simple or qualified named of the EClass ("Package" vs "sysml::Package")
     * @param withSubType
     *            <code>true</code> to include any element with a compatible type, <code>false</code> otherwise
     * @return a list of reachable object
     */
    public List<EObject> getAllReachable(EObject eObject, String type, boolean withSubType) {
        EClass eClass = SysMLMetamodelHelper.toEClass(type);
        return this.getAllReachable(eObject, eClass, withSubType);
    }

    /**
     * Get all reachable elements of the type given by the {@link EClass} in the {@link ResourceSet} of the given {@link EObject}.
     *
     * @param eObject
     *            the {@link EObject} stored in a {@link ResourceSet}
     * @param eClass
     *            the searched {@link EClass}
     * @return a list of reachable object
     */
    public List<EObject> getAllReachable(EObject eObject, EClass eClass) {
        return this.getAllReachable(eObject, eClass, true);
    }

    /**
     * Get all reachable elements of the type given by the {@link EClass} in the {@link ResourceSet} of the given {@link EObject}.
     *
     * @param eObject
     *            the {@link EObject} stored in a {@link ResourceSet}
     * @param eClass
     *            the searched {@link EClass}
     * @param withSubType
     *            <code>true</code> to include any element with a compatible type, <code>false</code> otherwise
     * @return a list of reachable object
     */
    public List<EObject> getAllReachable(EObject eObject, EClass eClass, boolean withSubType) {
        ResourceSet rs = eObject.eResource().getResourceSet();
        if (rs != null && eClass != null) {
            final Predicate<Notifier> predicate;
            if (withSubType) {
                predicate = e -> e instanceof EObject && eClass.isInstance(e);
            } else {
                predicate = e -> e instanceof EObject && eClass == ((EObject) e).eClass();
            }

            return this.getSysMLv2Resources(rs.getResources())
                    .flatMap(r -> this.eAllContentsStreamWithSelf(r))
                    .filter(predicate)
                    .map(EObject.class::cast)
                    .toList();
        } else {
            return List.of();
        }
    }

    /**
     * Find a {@link Definition} element that match the given name in the ResourceSet of the given element.
     *
     * @param object
     *            the object for which to find a corresponding type.
     * @param typeName
     *            the type name to match.
     * @return the found {@link Definition} element or <code>null</code>
     */
    public Definition findDefinitionByName(EObject object, String typeName) {
        final Definition result = this.findDefinitionByName(this.getAllRootsInResourceSet(object), typeName);
        return result;
    }

    /**
     * Iterate over the given {@link Collection} of root elements to find a {@link Definition} element with the given
     * name.
     *
     * @param roots
     *            the elements to inspect
     * @param typeName
     *            the name to match
     * @return the found {@link Definition} or <code>null</code>
     */
    public Definition findDefinitionByName(Collection<EObject> roots, String typeName) {
        for (final EObject root : roots) {
            final Definition result = this.findDefinitionByNameFrom(root, typeName);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Iterate over the root children to find a {@link Definition} element with the given name.
     *
     * @param root
     *            the root object to iterate.
     * @param typeName
     *            the name to match
     * @return the found {@link Definition} or <code>null</code>
     */
    private Definition findDefinitionByNameFrom(EObject root, String typeName) {
        Definition definition = null;

        if (root instanceof Definition && this.nameMatches((Definition) root, typeName)) {
            return (Definition) root;
        }

        TreeIterator<EObject> eAllContents = root.eAllContents();
        while (eAllContents.hasNext()) {
            EObject obj = eAllContents.next();
            if (obj instanceof Definition && this.nameMatches((Definition) obj, typeName)) {
                definition = (Definition) obj;
            }
        }

        return definition;
    }

    /**
     * Find a {@link Usage} element that match the given name in the ResourceSet of the given element.
     *
     * @param object
     *            the object for which to find a corresponding type.
     * @param usageName
     *            the type name to match.
     * @return the found {@link Usage} element or <code>null</code>
     */
    public Usage findUsageByName(EObject object, String usageName) {
        final Usage result = this.findUsageByName(this.getAllRootsInResourceSet(object), usageName);
        return result;
    }

    /**
     * Iterate over the given {@link Collection} of root elements to find a {@link Usage} element with the given name.
     *
     * @param roots
     *            the elements to inspect
     * @param usageName
     *            the name to match
     * @return the found {@link Usage} or <code>null</code>
     */
    public Usage findUsageByName(Collection<EObject> roots, String usageName) {
        for (final EObject root : roots) {
            final Usage result = this.findUsageByNameFrom(root, usageName);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Iterate over the root children to find a {@link Usage} element with the given name.
     *
     * @param root
     *            the root object to iterate.
     * @param usageName
     *            the name to match
     * @return the found {@link Usage} or <code>null</code>
     */
    private Usage findUsageByNameFrom(EObject root, String usageName) {
        Usage usage = null;

        if (root instanceof Usage && this.nameMatches((Definition) root, usageName)) {
            return (Usage) root;
        }

        TreeIterator<EObject> eAllContents = root.eAllContents();
        while (eAllContents.hasNext()) {
            EObject obj = eAllContents.next();
            if (obj instanceof Usage && this.nameMatches((Usage) obj, usageName)) {
                usage = (Usage) obj;
            }
        }

        return usage;
    }

    /**
     * Retrieves all the root elements of the resource in the resource set of the given context object.
     *
     * @param context
     *            the context object on which to execute this service.
     * @return a {@link Collection} of all the root element of the current resource set.
     */
    private Collection<EObject> getAllRootsInResourceSet(EObject context) {
        final Resource res = context.eResource();
        if (res != null && res.getResourceSet() != null) {
            final Collection<EObject> roots = new ArrayList<>();
            for (final Resource childRes : res.getResourceSet().getResources()) {
                roots.addAll(childRes.getContents());
            }
            return roots;
        }
        return Collections.emptyList();
    }

    /**
     * Check if the given element's name match the given String.
     *
     * @param element
     *            the {@link Element} to check.
     * @param name
     *            the name to match.
     * @return <code>true</code> if the name match, <code>false</code> otherwise.
     */
    private boolean nameMatches(Element element, String name) {
        if (element != null && element.getName() != null && name != null) {
            return element.getName().trim().equalsIgnoreCase(name.trim());
        }
        return false;
    }

    private Stream<Resource> getSysMLv2Resources(EList<Resource> resources) {
        return resources.stream().filter(r -> !r.getContents().isEmpty() && r.getContents().get(0) instanceof Element);
    }

    private Stream<Notifier> eAllContentsStreamWithSelf(Resource r) {
        if (r == null) {
            return Stream.empty();
        }
        return Stream.concat(Stream.of(r), StreamSupport.stream(Spliterators.spliteratorUnknownSize(r.getAllContents(), Spliterator.NONNULL), false));
    }
}
