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
package org.eclipse.syson.diagram.interconnection.view.services;

import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.syson.diagram.common.view.services.ViewNodeService;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewForUsageDiagramDescriptionProvider;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.PortUsage;

/**
 * Node-related Java services used by the {@link InterconnectionViewForUsageDiagramDescriptionProvider}.
 *
 * @author gdaniel
 */
public class InterconnectionViewNodeService extends ViewNodeService {

    public InterconnectionViewNodeService(IObjectService objectService) {
        super(objectService);
    }

    public boolean isInPort(PortUsage portUsage) {
        return portUsage.getDirection().equals(FeatureDirectionKind.IN);
    }

    public boolean isOutPort(PortUsage portUsage) {
        return portUsage.getDirection().equals(FeatureDirectionKind.OUT);
    }

    public boolean isInOutPort(PortUsage portUsage) {
        return portUsage.getDirection().equals(FeatureDirectionKind.INOUT);
    }

}
