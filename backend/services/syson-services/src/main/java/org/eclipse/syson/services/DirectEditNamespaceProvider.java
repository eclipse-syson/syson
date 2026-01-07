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
package org.eclipse.syson.services;

import java.util.Optional;

import org.eclipse.syson.services.api.IDirectEditNamespaceProvider;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.helper.DeresolvingNamespaceProvider;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.NamedProxy;
import org.springframework.stereotype.Service;

/**
 * Service use to resolve a proxy if the namespace provided was not resolved by default, usually if it was not imported.
 *
 * @author mcharfadi
 */
@Service
public class DirectEditNamespaceProvider implements IDirectEditNamespaceProvider {

    private static final String NAMESPACE = "SI::";

    @Override
    public Optional<Membership> getMembership(NamedProxy proxy) {
        var context = proxy.context();
        var deresolvingNamespaces = new DeresolvingNamespaceProvider().getDeresolvingNamespaces(context);
        for (Namespace namespace : deresolvingNamespaces) {
            var resolvedNamespace = namespace.resolve(NAMESPACE + proxy.nameToResolve());
            if (resolvedNamespace != null) {
                var namespaceImport = SysmlFactory.eINSTANCE.createNamespaceImport();
                namespaceImport.setImportedNamespace(resolvedNamespace.libraryNamespace());
                var sysmlPackages = EMFUtils.getAncestors(Package.class, context, null);
                if (sysmlPackages.size() == 1) {
                    sysmlPackages.get(0).getOwnedRelationship().add(namespaceImport);
                } else {
                    context.getOwner().getOwnedRelationship().add(namespaceImport);
                }
                return Optional.of(resolvedNamespace);
            }
        }
        return Optional.empty();
    }
}
