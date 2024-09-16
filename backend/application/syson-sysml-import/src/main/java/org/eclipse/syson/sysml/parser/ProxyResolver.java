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
package org.eclipse.syson.sysml.parser;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AstReferenceParser.
 *
 * @author gescande.
 */
public class ProxyResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyResolver.class);

    public boolean resolveProxy(final ProxiedReference proxiedReference) {
        EObject realElement = this.findProxyTarget(proxiedReference.owner(), proxiedReference.targetProxy());
        if (realElement == null) {
            return false;
        }
        if (proxiedReference.reference().isMany()) {
            final Object referenceList = proxiedReference.owner().eGet(proxiedReference.reference(), false);
            if (referenceList instanceof Collection referenceCollection) {
                final Collection<Object> resultCollection = new ArrayList<>();
                for (final Object target : referenceCollection) {
                    if (target.equals(proxiedReference.targetProxy())) {
                        resultCollection.add(realElement);
                        LOGGER.debug("Add the reference {} of object {} with the resolved proxy {} to target {}", proxiedReference.reference().getName(), proxiedReference.owner().toString(), proxiedReference.targetProxy().eProxyURI().fragment(), realElement);
                    } else {
                        resultCollection.add(target);
                    }
                }
                proxiedReference.owner().eSet(proxiedReference.reference(), resultCollection);
            }
        } else {
            // Manage specific case of conjugated port
            if (proxiedReference.owner() instanceof ConjugatedPortTyping && realElement instanceof PortDefinition elementPortDefinition) {
                realElement = elementPortDefinition.getConjugatedPortDefinition();
            }
            proxiedReference.owner().eSet(proxiedReference.reference(), realElement);
            if (realElement != null) {
                LOGGER.debug("Set the reference {} of object {} with the resolved proxy {} to target {}", proxiedReference.reference().getName(), proxiedReference.owner(), proxiedReference.targetProxy().eProxyURI().fragment(), realElement);
            } else {
                LOGGER.debug("Unable to set the reference {} of object {} because of the unresolved proxy {}.", proxiedReference.reference().getName(), proxiedReference.owner(), proxiedReference.targetProxy().eProxyURI().fragment());
            }
        }
        return true;
    }

    private EObject findProxyTarget(final EObject owner, final InternalEObject proxyObject) {
        final URI uri = proxyObject.eProxyURI();
        final String qualifiedName = uri.fragment();

        Namespace owningNamespace = null;
        if (owner instanceof Element ownerElement) {
            owningNamespace = ownerElement.getOwningNamespace();
        }

        if (owningNamespace == null && owner.eContainer() instanceof Namespace containerNamespace) {
            owningNamespace = containerNamespace;
        }

        EObject target = null;
        if (owningNamespace == null) {
            LOGGER.error("Unable to find owning Namespace of {}", owner);
        } else {
            final Membership membership = owningNamespace.resolve(qualifiedName);
            if (membership != null) {
                if (SysmlPackage.eINSTANCE.getMembership().isSuperTypeOf(proxyObject.eClass())) {
                    target = membership;
                } else {
                    target = membership.getMemberElement();
                }
            }
        }

        return target;
    }
}
