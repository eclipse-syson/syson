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
package org.eclipse.syson.diagram.common.view.services;

import java.util.Optional;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.variables.DiagramVariables;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.representations.IOperationValidator;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.components.view.emf.diagram.tools.DiagramElementPaletteVariableManagerProvider;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Provides the missing edge target variable when evaluating edge tools in an ordinary diagram element palette.
 * This service should be deleted when https://github.com/eclipse-sirius/sirius-web/issues/6997 will be fixed.
 *
 * @author arichard
 */
@Primary
@Service
public class SysONDiagramElementPaletteVariableManagerProvider extends DiagramElementPaletteVariableManagerProvider {

    /**
     * Creates the palette variable provider.
     *
     * @param operationValidator
     *            validates the variables required by palette operations
     */
    public SysONDiagramElementPaletteVariableManagerProvider(IOperationValidator operationValidator) {
        super(operationValidator);
    }

    /**
     * Supplies the source semantic element as an edge target placeholder until a connector target is selected.
     *
     * @param editingContext
     *            the editing context
     * @param diagramContext
     *            the diagram context
     * @param diagramElement
     *            the selected diagram element
     * @param semanticElement
     *            its semantic element
     * @return the palette variables
     */
    @Override
    public Optional<VariableManager> getVariableManager(IEditingContext editingContext, DiagramContext diagramContext, Object diagramElement, Object semanticElement) {
        var variableManager = super.getVariableManager(editingContext, diagramContext, diagramElement, semanticElement);
        variableManager.ifPresent(variables -> variables.put(DiagramVariables.EDGE_TARGET.name(), semanticElement));
        return variableManager;
    }
}
