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


    public static void resolveAllProxy(EObject content) {
        List<EReference> allReferences = content.eClass().getEAllReferences().stream().filter(reference -> !reference.isContainment() && !reference.isDerived()).toList();
        for (EReference reference : allReferences) {
            if (reference.isMany()) {
                Object referenceList = content.eGet(reference, false);
                if (referenceList instanceof Collection referenceCollection) {
                    Collection<Object> resultCollection = new ArrayList<>();
                    boolean containProxy = false;
                    for (Object target : referenceCollection) {
                        if (target instanceof InternalEObject eTarget && eTarget.eIsProxy()) {
                            containProxy = true;
                            Element realElement = findProxyTarget(content, eTarget);
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
                Object target = content.eGet(reference, false);

                if (target instanceof InternalEObject eTarget) {
                    if (eTarget.eIsProxy()) {
                        Element realElement = findProxyTarget(content, eTarget);
                        // Manage specific case of conjugated port
                        if (content instanceof ConjugatedPortTyping && realElement instanceof PortDefinition elementPortDefinition) {
                            realElement = elementPortDefinition.getConjugatedPortDefinition();
                        }
                        content.eSet(reference, realElement);
                        LOGGER.debug("Set the reference " + reference.getName() + " of object " + content.toString() + " with the resolved proxy " + eTarget.eProxyURI().fragment() + " to target " + realElement);
                    }
                }
            }
        }  
    }

    private static Element findProxyTarget(EObject owner, InternalEObject proxyObject) {
        URI uri = proxyObject.eProxyURI();
        String qualifiedName = uri.fragment();

        DeresolvingNamespaceProvider deresolvingNamespaceProvider = new DeresolvingNamespaceProvider();

        Namespace owningNamespace = null;

        if (owner instanceof Element ownerElement) {
            owningNamespace = deresolvingNamespaceProvider.getDeresolvingNamespace(ownerElement);
        }

        if (owningNamespace == null && owner.eContainer() instanceof Namespace containerNamespace) {
            owningNamespace = containerNamespace;
        }


        Element target = null;
        
        if (owningNamespace == null) {
            LOGGER.error("Unable to find owning Namespace of " + owner);
        } else {
            Membership membership = owningNamespace.resolve(qualifiedName);
            if (membership == null) {
                LOGGER.error("Unable to find object with qualifiedName " + qualifiedName + " in namespace " + owningNamespace.getQualifiedName());
            } else {
                target = membership.getMemberElement();
            }
        }

        return target;
    }
}
