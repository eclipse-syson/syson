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
package org.eclipse.syson.services.api;

import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.util.NamedProxy;

import java.util.Optional;

/**
 * Service use to resolve a proxy if the namespace provided was not resolved by default, usually if it was not imported.
 *
 * @author mcharfadi
 */
public interface IDirectEditNamespaceProvider {

    /**
     * Gets the membership from a NamedProxy if it exists.
     *
     * @param proxy
     *            the proxy to resolve
     * @return a Membership
     */
    Optional<Membership> getMembership(NamedProxy proxy);

    /**
     * Implementation which does nothing, used for mocks in unit tests.
     *
     * @author mcharfadi
     */
    class NoOp implements IDirectEditNamespaceProvider {

        @Override
        public Optional<Membership> getMembership(NamedProxy proxy) {
            return Optional.empty();
        }

    }

}
