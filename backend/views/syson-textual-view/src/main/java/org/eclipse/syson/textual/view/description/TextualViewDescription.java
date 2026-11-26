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
package org.eclipse.syson.textual.view.description;

import java.util.function.Predicate;

import org.eclipse.sirius.components.representations.IRepresentationDescription;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ViewUsage;

/**
 * Description of the SysON textual representation.
 *
 * @author gcoutable
 */
public class TextualViewDescription implements IRepresentationDescription {

    public static final String ID = "syson-textual-view";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getLabel() {
        return "SysML v2 Text (Experimental)";
    }

    @Override
    public Predicate<VariableManager> getCanCreatePredicate() {
        return variableManager -> variableManager.get(VariableManager.SELF, Element.class)
                .filter(element -> !(element instanceof ViewUsage))
                .isPresent();
    }
}
