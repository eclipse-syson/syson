/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.sysml.parser.translation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compute an EReference from an AST reference.
 *
 * @author Arthur Daussy
 */
public class EReferenceComputer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EReferenceComputer.class);

    private static final List<EReference> REL_CONTAINEMTS_REF = List.of(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(), SysmlPackage.eINSTANCE.getElement_OwnedRelationship());

    public Optional<EReference> getNonContainmentReference(EClass ownerEClass, EClass targetEClass, String astRefName) {

        List<EReference> references = this.getAllMacthingOrdererAllEReference(ownerEClass, targetEClass);
        final Optional<EReference> ref;
        if (references.isEmpty()) {
            ref = Optional.empty();
        } else {
            String translatedRefName = this.toMetamodelRefName(ownerEClass, astRefName);
            ref = this.getNonContainmentReference(references, translatedRefName);
        }

        return ref;
    }

    public Optional<EReference> getContainmentReference(EObject owner, EClass targetEClass, String astRefName) {

        EClass ownerEClass = owner.eClass();
        List<EReference> references = this.getAllMacthingOrdererAllEReference(ownerEClass, targetEClass).stream().filter(r -> r.isContainment()).toList();
        final Optional<EReference> ref;
        if (references.isEmpty()) {
            ref = Optional.empty();
        } else {
            /**
             * <p>
             * In case both ownedRelatedElement and ownedRelationship are candidates use the owner containing feature to
             * find which containment feature to use. Most of the time the containment pattern alternate one
             * OwnedRelatedElement and one OwnedRelationship. It should be possible to find from the astRefName value
             * but at the moment we haven't found the correct pattern Here are the identified use case:
             * <ul>
             * <li>"children,heritage" -> ownedRelationship</li>
             * <li>"target" -> ownedRelationship</li>
             * <p>
             */

            if (references.equals(REL_CONTAINEMTS_REF)) {
                if (owner.eContainingFeature() == SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement()) {
                    ref = Optional.of(SysmlPackage.eINSTANCE.getElement_OwnedRelationship());
                } else {
                    ref = Optional.of(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement());
                }
            } else {
                if (references.size() > 1) {
                    LOGGER.warn("More than feature found to hold a " + targetEClass.getName() + " in " + ownerEClass.getName());
                }
                ref = Optional.of(references.get(0));
            }
        }

        return ref;
    }

    private String toMetamodelRefName(EClass ownerType, String refName) {
        String result = refName;

        if (SysmlPackage.eINSTANCE.getRelationship().isSuperTypeOf(ownerType) && "targetRef".equals(refName)) {
            result = SysmlPackage.eINSTANCE.getRelationship_Target().getName();
        }

        return result;
    }

    private Optional<EReference> getNonContainmentReference(List<EReference> references, String name) {
        return references.stream().filter(ref -> !ref.isContainment() && this.match(ref, name)).findFirst();
    }

    private boolean match(EReference ref, String name) {
        return ref.getName().equalsIgnoreCase(name) || this.redefine(ref, name);
    }

    private boolean redefine(EReference ref, String name) {
        List<EReference> references = new ArrayList<>();
        this.collectAllRedefinitions(ref, references);
        return references.stream().anyMatch(r -> r.getName().equalsIgnoreCase(name));
    }

    private void collectAllRedefinitions(EReference ref, Collection<EReference> accumulator) {
        EAnnotation eAnnotation = ref.getEAnnotation("redefines");
        if (eAnnotation != null) {
            List<EReference> referencedFeature = eAnnotation.getReferences().stream()
                    .filter(EReference.class::isInstance)
                    .map(EReference.class::cast)
                    .toList();

            for (EReference redRef : referencedFeature) {
                if (!accumulator.contains(redRef)) {
                    accumulator.add(redRef);
                    this.collectAllRedefinitions(redRef, accumulator);
                }
            }
        }
    }

    private List<EReference> getAllMacthingOrdererAllEReference(EClass eClass, EClass targetType) {
        Stream<EReference> superReferences = eClass.getEAllSuperTypes().stream()
                .sorted(Comparator.comparing(sup -> this.distance(eClass, sup)))
                .flatMap(sup -> sup.getEReferences().stream());

        return Stream.concat(eClass.getEReferences().stream(), superReferences)
                .filter(ref -> ref.getEReferenceType().isSuperTypeOf(targetType) && ref.isChangeable())
                .toList();
    }

    private int distance(EClass child, EClass parent) {
        final int distance;
        if (child == parent) {
            distance = 0;
        } else if (child.getESuperTypes().contains(parent)) {
            distance = 1;
        } else {
            distance = child.getESuperTypes().stream().mapToInt(sup -> 1 + this.distance(sup, parent)).min().orElse(Integer.MAX_VALUE);
        }
        return distance;
    }

}
