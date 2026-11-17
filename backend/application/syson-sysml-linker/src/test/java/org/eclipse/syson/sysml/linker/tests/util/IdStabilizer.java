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
package org.eclipse.syson.sysml.linker.tests.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;

import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.omg.sysml.lang.sysml.Element;

/**
 * Assigns deterministic identifiers to linker-test resources.
 *
 * @author Arthur Daussy
 */
public class IdStabilizer {

    private static final String MODEL_EXPECTED_SUFFIX = "-model-expected.txt";

    private static final String PARSED_MODEL_EXPECTED_SUFFIX = "-parsed-model-expected.txt";

    private static final UUID STABLE_ID_NAMESPACE = UUID.fromString("2dd6b626-0c53-4d81-a6dd-9c5b5f7a3f4d");

    private static final String STABLE_ID_SEPARATOR = "|";

    private static final String LINKER_RESOURCE_MARKER = "org/eclipse/syson/sysml/linker/";

    public void assignStableIDs(XMIResource resource, String stableSysMLPath) {
        for (EObject root : resource.getContents()) {
            this.assignStableIDs(resource, root, stableSysMLPath);
        }
    }

    private void assignStableIDs(XMIResource resource, EObject eObject, String stableSysMLPath) {
        if (eObject instanceof Element) {
            String elementId = this.getStableID(stableSysMLPath, eObject);
            ((Element) eObject).setElementId(elementId);
            resource.setID(eObject, elementId);
        }
        for (EObject child : eObject.eContents()) {
            this.assignStableIDs(resource, child, stableSysMLPath);
        }
    }

    public String getStableID(String stableSysMLPath, String fragment, String type) {
        String key = STABLE_ID_NAMESPACE + STABLE_ID_SEPARATOR + stableSysMLPath + STABLE_ID_SEPARATOR + fragment + STABLE_ID_SEPARATOR + type;
        return UUID.nameUUIDFromBytes(key.getBytes(StandardCharsets.UTF_8)).toString();
    }

    public String getStableID(String stableSysMLPath, EObject eObject) {
        return this.getStableID(stableSysMLPath, this.getURIFragment(eObject), eObject.eClass().getName());
    }

    public String getStableSysMLPath(Path modelFile) {
        String path = this.toMatchingSysMLPath(modelFile).replace(File.separatorChar, '/');
        int markerIndex = path.indexOf(LINKER_RESOURCE_MARKER);
        if (markerIndex >= 0) {
            return path.substring(markerIndex + LINKER_RESOURCE_MARKER.length());
        }
        return Path.of(path).toAbsolutePath().normalize().toString().replace(File.separatorChar, '/');
    }

    private String toMatchingSysMLPath(Path modelFile) {
        String path = modelFile.toAbsolutePath().normalize().toString();
        String fileName = modelFile.getFileName().toString();
        String result;
        if (fileName.endsWith(PARSED_MODEL_EXPECTED_SUFFIX)) {
            String modelName = fileName.substring(0, fileName.length() - PARSED_MODEL_EXPECTED_SUFFIX.length());
            Path testFolder = modelFile.getParent().getParent();
            result = testFolder.resolve("sysml").resolve(modelName + ".sysml").toString();
        } else if (fileName.endsWith(MODEL_EXPECTED_SUFFIX)) {
            String modelName = fileName.substring(0, fileName.length() - MODEL_EXPECTED_SUFFIX.length());
            Path testFolder = modelFile.getParent().getParent();
            result = testFolder.resolve("sysml").resolve(modelName + ".sysml").toString();
        } else if (fileName.endsWith(".xmi")) {
            result = path.substring(0, path.length() - ".xmi".length()) + ".sysml";
        } else {
            result = path;
        }
        return result;
    }

    public String getStableSysMLPath(URI uri) {
        String path = uri.trimFileExtension().appendFileExtension("sysml").toFileString();
        if (path == null) {
            path = uri.trimFileExtension().appendFileExtension("sysml").toString();
        }
        path = path.replace(File.separatorChar, '/');
        int markerIndex = path.indexOf(LINKER_RESOURCE_MARKER);
        if (markerIndex >= 0) {
            return path.substring(markerIndex + LINKER_RESOURCE_MARKER.length());
        }
        return Path.of(path).toAbsolutePath().normalize().toString().replace(File.separatorChar, '/');
    }

    /**
     * Gets the {@link URI} fragment based on containing {@link EReference} name and index.
     *
     * @param eObj
     *            the {@link EObject}
     * @return the {@link URI} fragment based on containing {@link EReference} name and index
     */
    private String getURIFragment(EObject eObj) {
        final String res;

        InternalEObject internalEObject = (InternalEObject) eObj;
        if (internalEObject.eInternalContainer() == null) {
            res = "/";
        } else {
            SegmentSequence.Builder builder = SegmentSequence.newBuilder("/");
            for (InternalEObject container = internalEObject
                    .eInternalContainer(); container != null; container = internalEObject.eInternalContainer()) {
                builder.append(container.eURIFragmentSegment(internalEObject.eContainingFeature(), internalEObject));
                internalEObject = container;
            }

            builder.append("");
            builder.reverse();

            res = builder.toSegmentSequence().toString();
        }

        return res;
    }

    /**
     * Populates the ID map for the given {@link Resource} with the given {@link EObject} recursively.
     *
     * @param resource
     *            the {@link Resource}
     */
    public void assignIDs(XMIResource resource) {
        for (EObject root : resource.getContents()) {
            this.assignIDs(resource, root);
        }
    }

    /**
     * Populates the ID map for the given {@link Resource} with the given {@link EObject} recursively.
     *
     * @param resource
     *            the {@link Resource}
     * @param eObject
     *            the {@link EObject}
     */
    private void assignIDs(XMIResource resource, EObject eObject) {
        if (eObject instanceof org.omg.sysml.lang.sysml.Element) {
            String elementId = ((org.omg.sysml.lang.sysml.Element) eObject).getElementId();
            if (elementId != null && !elementId.trim().isEmpty()) {
                resource.setID(eObject, elementId);
            }
        }
        for (EObject child : eObject.eContents()) {
            this.assignIDs(resource, child);
        }
    }

}
