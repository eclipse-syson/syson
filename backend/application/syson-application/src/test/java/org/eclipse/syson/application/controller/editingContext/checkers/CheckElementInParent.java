/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.application.controller.editingContext.checkers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.helper.EMFUtils;

/**
 * Checks that an element exists in the given parent.
 *
 * @author gdaniel
 */
public class CheckElementInParent implements ISemanticChecker {

    private final IObjectSearchService objectSearchService;

    private final String rootElementId;

    private String parentLabel;

    private EReference containmentReference;

    private EClass eClass;

    public CheckElementInParent(IObjectSearchService objectSearchService, String rootElementId) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.rootElementId = Objects.requireNonNull(rootElementId);
    }

    public CheckElementInParent withParentLabel(String expectedParentLabel) {
        this.parentLabel = expectedParentLabel;
        return this;
    }

    public CheckElementInParent withContainmentReference(EReference expectedContainmentReference) {
        this.containmentReference = expectedContainmentReference;
        return this;
    }

    public CheckElementInParent hasType(EClass expectedType) {
        this.eClass = expectedType;
        return this;
    }

    @Override
    public void check(IEditingContext editingContext) {
        Object semanticRootObject = this.objectSearchService.getObject(editingContext, this.rootElementId).orElse(null);
        assertThat(semanticRootObject).isInstanceOf(Element.class);
        Element semanticRootElement = (Element) semanticRootObject;
        Optional<Element> optParentElement = EMFUtils.allContainedObjectOfType(semanticRootElement, Element.class)
                .filter(element -> Objects.equals(element.getName(), this.parentLabel))
                .findFirst();
        assertThat(optParentElement).isPresent();
        Element parentElement = optParentElement.get();
        Object referenced = parentElement.eGet(this.containmentReference);
        if (referenced instanceof List<?> referencedElements) {
            assertThat(referencedElements).anySatisfy(referencedElement -> {
                assertThat(this.eClass.isInstance(referencedElement)).isTrue();
            });
        } else {
            assertThat(this.eClass.isInstance(referenced)).isTrue();
        }
    }

}
