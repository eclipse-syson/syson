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
package org.eclipse.syson.sysml.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Loads test fixtures and locates their contained elements by type and full qualified name.
 *
 * @author Arthur Daussy
 */
public class SysMLTestModel {

    private final Resource resource;

    private SysMLTestModel(Resource resource) {
        this.resource = Objects.requireNonNull(resource);
    }

    /**
     * Loads an independent XMI fixture from the classpath into a fresh resource set.
     *
     * @param path
     *            the classpath resource path
     * @return the model accessor
     * @throws IOException
     *            if the fixture cannot be loaded
     */
    public static SysMLTestModel load(String path) throws IOException {
        var resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        var resource = new XMIResourceImpl(URI.createURI("test:" + path));
        resourceSet.getResources().add(resource);
        try (var input = SysMLTestModel.class.getResourceAsStream(path)) {
            assertThat(input).as("Fixture %s", path).isNotNull();
            resource.load(input, Map.of());
        }
        assertThat(resource.getErrors()).isEmpty();
        return new SysMLTestModel(resource);
    }

    /**
     * Finds exactly one contained element matching the expected type and semantic qualified name.
     *
     * @param <T>
     *            the expected element type
     * @param type
     *            the expected metamodel type
     * @param fullQualifiedName
     *            the full qualified name including the package and separated by {@code ::}
     * @return the unique matching element
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
        assertThat(matches).as("Expected one %s named %s", type.getSimpleName(), fullQualifiedName).hasSize(1);
        return matches.getFirst();
    }
}
