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
package org.eclipse.syson.sysml.util;

import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Namespace;

/**
 * Provider of {@link Namespace} stored in immutable {@link LibraryPackage}.
 *
 * @author Arthur Daussy
 */
public interface IImmutableLibraryNamespaceProvider {

    /**
     * Gets an element from is full qualified name and cast it with the given type if compliant.
     *
     * @param <T>
     *            the expected type
     * @param qualifiedName
     *            the full qualified name
     * @param type
     *            the expected type
     * @return an element or <code>null</code>
     */
    <T extends Namespace> T getNamespaceFromStandardLibrary(String qualifiedName, Class<T> type);

    /**
     * Gets a Namespace from is full qualified name
     *
     * @param qualifiedName
     *            the full qualified name of the target
     * @return a {@link Namespace} or <code>null</code>
     */
    Namespace getNamespaceFromStandardLibrary(String qualifiedName);

    public class NoOp implements IImmutableLibraryNamespaceProvider {

        @Override
        public <T extends Namespace> T getNamespaceFromStandardLibrary(String qualifiedName, Class<T> type) {
            return null;
        }

        @Override
        public Namespace getNamespaceFromStandardLibrary(String qualifiedName) {
            return null;
        }

    }

}
