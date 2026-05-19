/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.syson.services.api.IDefaultSysMLMoveElementService;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.services.api.ISysMLMoveElementServiceDelegate;
import org.eclipse.syson.services.api.MoveStatus;
import org.eclipse.syson.sysml.Element;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ISysMLMoveElementService} which delegates to {@link ISysMLMoveElementServiceDelegate}.
 * <p>
 * {@link IDefaultSysMLMoveElementService} is used as fallback if there is no
 * {@link ISysMLMoveElementServiceDelegate} to delegate to.
 * </p>
 *
 * @author Arthur Daussy
 */
@Service
public class SysMLMoveElementService implements ISysMLMoveElementService {

    private final List<ISysMLMoveElementServiceDelegate> moveElementServiceDelegates;

    private final IDefaultSysMLMoveElementService defaultMoveElementService;

    public SysMLMoveElementService(List<ISysMLMoveElementServiceDelegate> moveElementServiceDelegates, IDefaultSysMLMoveElementService defaultMoveElementService) {
        this.moveElementServiceDelegates = Objects.requireNonNull(moveElementServiceDelegates);
        this.defaultMoveElementService = Objects.requireNonNull(defaultMoveElementService);
    }

    /**
     * Moves an element into a new parent.
     *
     * @param element
     *            the element to move
     * @param newParent
     *            the new parent
     * @return <code>true</code> if the element has been move, <code>false</code> otherwise
     */
    @Override
    public MoveStatus moveSemanticElement(Element element, Element newParent) {
        return this.getDelegate(element, newParent)
                .map(delegate -> delegate.moveSemanticElement(element, newParent))
                .orElseGet(() -> this.defaultMoveElementService.moveSemanticElement(element, newParent));
    }

    private Optional<ISysMLMoveElementServiceDelegate> getDelegate(Element element, Element newParent) {
        return this.moveElementServiceDelegates.stream()
                .filter(delegate -> delegate.canHandle(element, newParent))
                .findFirst();
    }
}
