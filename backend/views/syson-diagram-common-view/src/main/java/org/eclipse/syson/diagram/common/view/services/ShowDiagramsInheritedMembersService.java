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
 * Service class for ShowDiagramsInheritedMembers options.
 *
 * @author arichard
 */
@Service
public class ShowDiagramsInheritedMembersService {

    @Value("${org.eclipse.syson.show.diagrams.inherited.members:true}")
    private boolean showInheritedMembers;

    @Value("${org.eclipse.syson.show.diagrams.inherited.members.from.standard.libraries:false}")
    private boolean showInheritedMembersFromStandardLibraries;

    public boolean getShowInheritedMembers() {
        return this.showInheritedMembers;
    }

    public void setShowInheritedMembers(boolean showInheritedMembers) {
        this.showInheritedMembers = showInheritedMembers;
    }

    public boolean getShowInheritedMembersFromStandardLibraries() {
        return this.showInheritedMembersFromStandardLibraries;
    }

    public void setShowInheritedMembersFromStandardLibraries(boolean showInheritedMembersFromStandardLibraries) {
        this.showInheritedMembersFromStandardLibraries = showInheritedMembersFromStandardLibraries;
    }
}
