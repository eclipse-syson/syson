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
package org.eclipse.syson.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.springframework.core.io.ClassPathResource;

/**
 * Load an XMI model from a path in the classpath.
 *
 * @author Arthur Daussy
 */
public class ClasspathXmiModelLoader {

    private final ResourceSet resourceSet;

    public ClasspathXmiModelLoader(ResourceSet resourceSet) {
        this.resourceSet = resourceSet;
    }

    /**
     * Load a resource from a file path relative to the classpath.
     *
     * @param path
     *         of the file in the classpath
     * @return a {@link Resource}
     * @throws IOException
     *         if an error occurs during loading
     */
    public Resource load(String path) throws IOException {
        ClassPathResource cr = new ClassPathResource(path);
        try (InputStream input = cr.getInputStream()) {
            XMIResource resource = new XMIResourceImpl(URI.createURI("fake://" + path));
            this.resourceSet.getResources().add(resource);
            resource.load(input, Collections.emptyMap());
            return resource;
        }
    }
}
