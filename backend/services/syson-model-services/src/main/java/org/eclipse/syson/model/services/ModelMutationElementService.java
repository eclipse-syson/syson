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
package org.eclipse.syson.model.services;

import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.emf.utils.SiriusEMFCopier;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * Element-related services doing mutations in models.
 *
 * @author arichard
 */
@Service
public class ModelMutationElementService {

    private final ElementUtil elementUtil;

    public ModelMutationElementService() {
        this.elementUtil = new ElementUtil();
    }

    /**
     * Set a new {@link ViewDefinition} for the given {@link ViewUsage}.
     *
     * @param viewUsage
     *            the given {@link ViewUsage}.
     * @param newViewDefinition
     *            the new {@link ViewDefinition} to set, through its qualified name (for example,
     *            StandardDiagramsConstants.GV).
     * @return the given {@link ViewUsage}.
     */
    public Element setAsView(ViewUsage viewUsage, String newViewDefinition) {
        var types = viewUsage.getType();
        var generalViewViewDef = this.elementUtil.findByNameAndType(viewUsage, newViewDefinition, ViewDefinition.class);
        if (types == null || types.isEmpty()) {
            var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
            viewUsage.getOwnedRelationship().add(featureTyping);
            featureTyping.setType(generalViewViewDef);
            featureTyping.setTypedFeature(viewUsage);
        } else {
            Relationship relationship = viewUsage.getOwnedRelationship().get(0);
            if (relationship instanceof FeatureTyping featureTyping) {
                featureTyping.setType(generalViewViewDef);
            }
        }
        return viewUsage;
    }

    /**
     * Duplicates an Element.
     *
     * @param elementToDuplicate
     *         the element to duplicate
     * @param duplicateContent
     *         holds true if the content of the object to duplicate need to be duplicated
     * @param copyOutgoingReferences
     *         holds true if the outgoing references need to be copied of the object to duplicate need to be duplicated
     * @return an optional duplicated object
     */
    public Optional<Element> duplicateElement(Element elementToDuplicate, boolean duplicateContent, boolean copyOutgoingReferences) {
        Optional<EObject> duplicateObject;
        if (duplicateContent) {
            EcoreUtil.Copier copier = new EcoreUtil.Copier();
            EObject duplicatedObject = copier.copy(elementToDuplicate);
            if (copyOutgoingReferences) {
                copier.copyReferences();
            }
            duplicateObject = Optional.ofNullable(duplicatedObject);
        } else {
            SiriusEMFCopier copier = new SiriusEMFCopier();
            EObject duplicatedObject = copier.copyWithoutContent(elementToDuplicate);
            if (copyOutgoingReferences) {
                copier.copyReferences();
            }
            duplicateObject = Optional.ofNullable(duplicatedObject);
        }

        return duplicateObject.filter(Element.class::isInstance)
                .map(Element.class::cast)
                .map(duplicate -> {
                    // Change the name of element root element (not its content)
                    if (elementToDuplicate.getDeclaredName() != null && !elementToDuplicate.getDeclaredName().isBlank()) {
                        duplicate.setDeclaredName(elementToDuplicate.getDeclaredName() + "-copy");
                    } else if (elementToDuplicate.getShortName() != null && !elementToDuplicate.getShortName().isBlank()) {
                        duplicate.setDeclaredShortName(elementToDuplicate.getDeclaredShortName() + "-copy");
                    }
                    // Reset all ids
                    EMFUtils.allContainedObjectOfType(duplicate, Element.class).forEach(element -> element.setElementId(ElementUtil.generateUUID(element).toString()));

                    return duplicate;
                });
    }
}
