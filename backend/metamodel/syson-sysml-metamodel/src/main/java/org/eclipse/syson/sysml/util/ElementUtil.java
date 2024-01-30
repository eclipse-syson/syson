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
package org.eclipse.syson.sysml.util;

import com.fasterxml.uuid.Generators;

import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;

/**
 * Util class for SysML elements.
 * 
 * @author arichard
 */
public class ElementUtil {

    /**
     * The UUID for NameSpace_URL as required in SysMLv2 specification.
     */
    public static final UUID NAME_SPACE_URL_UUID = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8");

    /**
     * The prefix to prepend to all names for the construction of UUIDs as required in KerML specification.
     */
    public static final String KERML_LIBRARY_BASE_URI = "https://www.omg.org/spec/KerML/";

    /**
     * The prefix to prepend to all names for the construction of UUIDs as required in SysMLv2 specification.
     */
    public static final String SYSML_LIBRARY_BASE_URI = "https://www.omg.org/spec/SysML/";

    /**
     * Check if the given {@link Element} comes from a library (i.e. a {@link LibraryPackage}) or not.
     * 
     * @param element
     *            the given {@link Element}.
     * @param standardOnly
     *            whether the library must be standard. If set to <code>false</code>, this method will return
     *            <code>true</code> if the given element comes from a library, without regarding its standard attribute.
     * @return <code>true</code> if the given element is contained in a library, <code>false</code> otherwise.
     */
    public static boolean isFromLibrary(Element element, boolean standardOnly) {
        boolean isFromStandardLibrary = false;
        if (element instanceof LibraryPackage libraryPackage) {
            if (standardOnly) {
                isFromStandardLibrary = libraryPackage.isIsStandard();
            } else {
                isFromStandardLibrary = true;
            }
        } else {
            EObject eContainer = element.eContainer();
            if (eContainer instanceof Element eContainerElement) {
                isFromStandardLibrary = isFromStandardLibrary(eContainerElement);
            }
        }
        return isFromStandardLibrary;
    }

    /**
     * Check if the given {@link Element} comes from a standard library (i.e. a {@link LibraryPackage} with its standard
     * attribute set to true) or not.
     * 
     * @param element
     *            the given {@link Element}.
     * @return <code>true</code> if the given element is contained in a standard library, <code>false</code> otherwise.
     */
    public static boolean isFromStandardLibrary(Element element) {
        return isFromLibrary(element, true);
    }

    /**
     * Generate a UUID (a v5 for standard library elements a a random v4 random for others).
     * 
     * @return a UUID.
     */
    public static UUID generateUUID(Element element) {
        UUID uuid = UUID.randomUUID();
        if (element instanceof LibraryPackage libraryPackage && libraryPackage.isIsStandard()) {
            Resource resource = element.eResource();
            if (resource != null) {
                String uri = resource.getURI().toString().startsWith("kermllibrary:") ? ElementUtil.KERML_LIBRARY_BASE_URI : ElementUtil.SYSML_LIBRARY_BASE_URI;
                String qualifiedName = element.getQualifiedName();
                if (qualifiedName != null) {
                    uuid = generateUUIDv5(ElementUtil.NAME_SPACE_URL_UUID, uri + qualifiedName);
                }
            }
        } else if (isFromStandardLibrary(element) && element.getDeclaredName() != null && !(element instanceof Import || element instanceof Membership)) {
            String qualifiedName = element.getQualifiedName();
            Namespace libraryNamespace = element.libraryNamespace();
            if (qualifiedName != null && libraryNamespace != null) {
                UUID namespaceUUID = UUID.fromString(libraryNamespace.getElementId());
                uuid = ElementUtil.generateUUIDv5(namespaceUUID, qualifiedName);
            }
        }
        return uuid;
    }

    /**
     * Generate a UUID v5 from a given namespace and a value.
     * 
     * @param namespaceUUID
     *            the namespace to use to generate the UUID.
     * @param value
     *            the value (i.e. the qualified name of a SysML element) for which we want to generate the UUID.
     * @return a UUID in version 5.
     */
    public static UUID generateUUIDv5(UUID namespaceUUID, String value) {
        return Generators.nameBasedGenerator(namespaceUUID, null).generate(value);
    }
}
