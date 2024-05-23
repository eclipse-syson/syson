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
import java.util.List;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.helper.DeresolvingNamespaceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AstReferenceParser.
 *
 * @author gescande.
 */
public class ProxyResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyResolver.class);

    public void resolveAllProxy(final EObject content) {
        final List<EReference> allReferences = content.eClass().getEAllReferences().stream().filter(reference -> !reference.isContainment() && !reference.isDerived()).toList();
        for (final EReference reference : allReferences) {
            if (reference.isMany()) {
                final Object referenceList = content.eGet(reference, false);
                if (referenceList instanceof final Collection referenceCollection) {
                    final Collection<Object> resultCollection = new ArrayList<>();
                    boolean containProxy = false;
                    for (final Object target : referenceCollection) {
                        if (target instanceof final InternalEObject eTarget && eTarget.eIsProxy()) {
                            containProxy = true;
                            final Element realElement = findProxyTarget(content, eTarget);
                            resultCollection.add(realElement);
                            LOGGER.debug("Add the reference " + reference.getName() + " of object " + content.toString() + " with the resolved proxy " + eTarget.eProxyURI().fragment() + " to target " + realElement);
                        } else {
                            resultCollection.add(target);
                        }
                    }

                    if (containProxy) {
                        content.eSet(reference, resultCollection);
                    }
                }
            } else {
                final Object target = content.eGet(reference, false);

                if (target instanceof final InternalEObject eTarget) {
                    if (eTarget.eIsProxy()) {
                        Element realElement = findProxyTarget(content, eTarget);
                        // Manage specific case of conjugated port
                        if (content instanceof ConjugatedPortTyping && realElement instanceof final PortDefinition elementPortDefinition) {
                            realElement = elementPortDefinition.getConjugatedPortDefinition();
                        }
                        content.eSet(reference, realElement);
                        LOGGER.debug("Set the reference " + reference.getName() + " of object " + content.toString() + " with the resolved proxy " + eTarget.eProxyURI().fragment() + " to target " + realElement);
                    }
                }
            }
        }  
    }

    private Element findProxyTarget(final EObject owner, final InternalEObject proxyObject) {
        final URI uri = proxyObject.eProxyURI();
        final String qualifiedName = uri.fragment();

        final DeresolvingNamespaceProvider deresolvingNamespaceProvider = new DeresolvingNamespaceProvider();

        Namespace owningNamespace = null;

        if (owner instanceof final Element ownerElement) {
            owningNamespace = deresolvingNamespaceProvider.getDeresolvingNamespace(ownerElement);
        }

        if (owningNamespace == null && owner.eContainer() instanceof final Namespace containerNamespace) {
            owningNamespace = containerNamespace;
        }

        Element target = null;
        
        if (owningNamespace == null) {
            LOGGER.error("Unable to find owning Namespace of " + owner);
        } else {
            final Membership membership = owningNamespace.resolve(qualifiedName);
            if (membership == null) {
                LOGGER.error("Unable to find object with qualifiedName " + qualifiedName + " in namespace " + owningNamespace.getQualifiedName());
            } else {
                target = membership.getMemberElement();
            }
        }

        return target;
    }
}
