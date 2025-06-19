/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import java.text.MessageFormat;
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
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.DeresolvingNamespaceProvider;
import org.eclipse.syson.sysml.utils.LogNameProvider;
import org.eclipse.syson.sysml.utils.MessageReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AstReferenceParser.
 *
 * @author gescande.
 */
public class ProxyResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyResolver.class);

    private final LogNameProvider logNameProvider = new LogNameProvider();

    private final MessageReporter messageReporter;

    public ProxyResolver(MessageReporter messageReporter) {
        super();
        this.messageReporter = messageReporter;
    }

    public boolean resolveProxy(final ProxiedReference proxiedReference) {
        EObject realElement = this.findProxyTarget(proxiedReference.owner(), proxiedReference.targetProxy(), proxiedReference.reference());
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
                        LOGGER.debug("Add the reference {} of object {} with the resolved proxy {} to target {}", proxiedReference.reference().getName(), proxiedReference.owner().toString(),
                                proxiedReference.targetProxy().eProxyURI().fragment(), realElement);
                    } else {
                        resultCollection.add(target);
                    }
                }
                proxiedReference.owner().eSet(proxiedReference.reference(), resultCollection);
            }
        } else {
            proxiedReference.owner().eSet(proxiedReference.reference(), realElement);
            LOGGER.debug("Set the reference {} of object {} with the resolved proxy {} to target {}", proxiedReference.reference().getName(), proxiedReference.owner(),
                    proxiedReference.targetProxy().eProxyURI().fragment(), realElement);
        }
        return true;
    }

    private EObject findProxyTarget(final EObject owner, final InternalEObject proxyObject, EReference eReference) {
        final URI uri = proxyObject.eProxyURI();
        final String qualifiedName = uri.fragment();

        List<Namespace> deresolvingNamespaces = new ArrayList<>();
        if (owner instanceof Element ownerElement) {
            deresolvingNamespaces.addAll(new DeresolvingNamespaceProvider().getDeresolvingNamespaces(ownerElement));
        }

        EObject target = null;
        if (deresolvingNamespaces.isEmpty()) {
            String errorMessage = MessageFormat.format("Unable to find owning Namespace of {0}", this.logNameProvider.getName(owner));
            this.messageReporter.error(errorMessage);
        } else {
            for (Namespace deresolvingNamespace : deresolvingNamespaces) {
                final Membership membership = deresolvingNamespace.resolve(qualifiedName);
                if (membership != null) {
                    if (SysmlPackage.eINSTANCE.getMembership().isSuperTypeOf(proxyObject.eClass())) {
                        target = membership;
                    } else if (eReference.getEType().isInstance(membership.getMemberElement())) {
                        target = membership.getMemberElement();
                    } else if (this.isConjugatedPortReference(owner, membership.getMemberElement())) {
                        // Manage specific case of conjugated port
                        target = ((PortDefinition) membership.getMemberElement()).getConjugatedPortDefinition();
                    }
                }
                // Return the first matching element
                if (target != null) {
                    return target;
                }
            }
        }

        return target;
    }

    private boolean isConjugatedPortReference(final EObject owner, final Element target) {
        return owner instanceof ConjugatedPortTyping && target instanceof PortDefinition;
    }
}
