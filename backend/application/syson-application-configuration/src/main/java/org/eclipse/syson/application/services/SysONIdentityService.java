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
package org.eclipse.syson.application.services;

import java.util.Objects;

import org.eclipse.sirius.components.core.api.IDefaultIdentityService;
import org.eclipse.sirius.components.core.api.IIdentityServiceDelegate;
import org.eclipse.sirius.components.emf.services.IDAdapter;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * Dedicated {@link IIdentityServiceDelegate} for SysON. Implicit relationships are not serialized so they need an
 * IDAdapter, needed by org.eclipse.sirius.components.emf.services.DefaultIdentityService.getId(Object object) for
 * org.eclipse.sirius.web.application.views.relatedelements.services.RelatedElementsDescriptionProvider.
 *
 * @author arichard
 */
@Service
public class SysONIdentityService implements IIdentityServiceDelegate {

    private final IDefaultIdentityService defaultIdentityService;

    public SysONIdentityService(IDefaultIdentityService defaultIdentityService) {
        this.defaultIdentityService = Objects.requireNonNull(defaultIdentityService);
    }

    @Override
    public boolean canHandle(Object object) {
        return object instanceof Element;
    }

    @Override
    public String getId(Object object) {
        String id = null;
        if (object instanceof Element element && ElementUtil.isFromStandardLibrary(element)) {
            id = element.getElementId();
        } else {
            id = this.defaultIdentityService.getId(object);
        }
        if (id == null && object instanceof Relationship relationship && relationship.isIsImplied()) {
            var idAdapter = new IDAdapter(ElementUtil.generateUUID(relationship));
            relationship.eAdapters().add(idAdapter);
            id = idAdapter.getId().toString();
        }
        return id;
    }

    @Override
    public String getKind(Object object) {
        return this.defaultIdentityService.getKind(object);
    }
}
