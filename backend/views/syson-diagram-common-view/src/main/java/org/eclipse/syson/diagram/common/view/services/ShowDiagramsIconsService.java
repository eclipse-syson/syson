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
package org.eclipse.syson.diagram.common.view.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class for ShowDiagramsIcons option.
 *
 * @author arichard
 */
@Service
public class ShowDiagramsIconsService {

    @Value("${org.eclipse.syson.show.diagrams.icons:true}")
    private boolean showIcons;

    public void setShowIcons(boolean showIcons) {
        this.showIcons = showIcons;
    }

    public boolean getShowIcons() {
        return this.showIcons;
    }
}
