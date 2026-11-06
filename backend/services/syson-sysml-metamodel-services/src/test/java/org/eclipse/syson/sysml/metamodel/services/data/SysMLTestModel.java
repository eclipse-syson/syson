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
package org.eclipse.syson.sysml.metamodel.services.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Objects;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.syson.sysml.Element;

/**
 * Provides typed access to the elements contained in a test resource by their full qualified names.
 *
 * @author Arthur Daussy
 */
public class SysMLTestModel {

    private final Resource resource;

    public SysMLTestModel(Resource resource) {
        this.resource = Objects.requireNonNull(resource);
    }

    /**
     * Finds a unique contained element using its semantic qualified name, including any inherited effective name.
     * Traversal follows containment, so an inherited feature is found under its declaring owner, not its usages.
     *
     * @param <T>
     *            the expected element type
     * @param type
     *            the non-null expected metamodel type
     * @param fullQualifiedName
     *            the non-null qualified name, including the root package, with segments separated by {@code ::}
     * @return the unique element matching both the type and qualified name
     * @throws AssertionError
     *            if no element or more than one element matches
     */
    public <T extends Element> T get(Class<T> type, String fullQualifiedName) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(fullQualifiedName);
        var matches = new ArrayList<T>();
        this.resource.getAllContents().forEachRemaining(candidate -> {
            if (type.isInstance(candidate)) {
                T element = type.cast(candidate);
                if (fullQualifiedName.equals(element.getQualifiedName())) {
                    matches.add(element);
                }
            }
        });
        assertThat(matches).as("Expected one %s named %s in %s", type.getSimpleName(), fullQualifiedName, this.resource.getURI()).hasSize(1);
        return matches.getFirst();
    }
}
