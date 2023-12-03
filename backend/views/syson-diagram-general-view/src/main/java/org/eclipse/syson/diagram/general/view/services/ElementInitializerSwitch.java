/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.diagram.general.view.services;

import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch called when a new element is created in the General View. Allows to set various attributes/references.
 *
 * @author arichard
 */
public class ElementInitializerSwitch extends SysmlSwitch<Element> {

    @Override
    public Element caseElement(Element object) {
        return object;
    }

    @Override
    public Element caseEnumerationDefinition(EnumerationDefinition object) {
        object.setIsVariation(true);
        return object;
    }
}
