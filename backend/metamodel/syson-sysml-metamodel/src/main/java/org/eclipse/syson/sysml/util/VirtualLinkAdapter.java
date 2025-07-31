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

import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.Element;

/**
 * Adapter that virtually link an element to another.
 *
 * @author Arthur Daussy
 */
public class VirtualLinkAdapter extends AdapterImpl {

    private final Element virtualOwningRelatedElement;

    private final String virtualReferenceName;

    /**
     * Gets the target of a virtual link.
     *
     * @param source
     *            the source {@link EObject}
     * @param virtualReferenceName
     *            the reference name associated to the virtual link
     * @return an optional target
     */
    public static Optional<Element> getVirtualLink(EObject source, String virtualReferenceName) {
        return source.eAdapters().stream()
                .filter(VirtualLinkAdapter.class::isInstance)
                .map(VirtualLinkAdapter.class::cast)
                .filter(vl -> vl.getVirtualReferenceName() == virtualReferenceName)
                .map(VirtualLinkAdapter::getVirtualOwningRelatedElement)
                .findFirst();
    }

    /**
     * Check if the given source has a VirtaulLinkAdapter.
     *
     * @param source
     *            the source {@link EObject}
     * @return <code>true</code> if the given source has a VirtaulLinkAdapter, <code>false</code> otherwise.
     */
    public static boolean hasVirtualLink(EObject source) {
        return source.eAdapters().stream().anyMatch(VirtualLinkAdapter.class::isInstance);
    }

    public VirtualLinkAdapter(Element virtualOwningRelatedElement, String virtualReferenceName) {
        super();
        this.virtualOwningRelatedElement = Objects.requireNonNull(virtualOwningRelatedElement);
        this.virtualReferenceName = Objects.requireNonNull(virtualReferenceName);
    }

    public Element getVirtualOwningRelatedElement() {
        return this.virtualOwningRelatedElement;
    }

    public String getVirtualReferenceName() {
        return this.virtualReferenceName;
    }
}
