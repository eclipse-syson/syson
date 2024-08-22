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
 * Service class for ShowDiagramsInheritedMembers option.
 *
 * @author arichard
 */
@Service
public class ShowDiagramsInheritedMembersService {

    @Value("${org.eclipse.syson.show.diagrams.inherited.members:false}")
    private boolean showInheritedMembers;

    public void setShowInheritedMembers(boolean showInheritedMembers) {
        this.showInheritedMembers = showInheritedMembers;
    }

    public boolean getShowInheritedMembers() {
        return this.showInheritedMembers;
    }
}
