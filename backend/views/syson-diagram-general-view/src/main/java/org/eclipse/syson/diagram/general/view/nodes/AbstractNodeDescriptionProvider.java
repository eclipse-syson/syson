/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Common pieces of node descriptions shared by {@link INodeDescriptionProvider} in General View.
 *
 * @author arichard
 */
public abstract class AbstractNodeDescriptionProvider implements INodeDescriptionProvider {

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected final IColorProvider colorProvider;

    public AbstractNodeDescriptionProvider(IColorProvider colorProvider) {
        this.colorProvider = Objects.requireNonNull(colorProvider);
    }

    protected String getNewElementDefaultName(EClassifier eClassifier) {
        String defaultName = "";
        if (eClassifier instanceof EClass eClass && SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eClass)) {
            char[] charArray = eClass.getName().toCharArray();
            charArray[0] = Character.toLowerCase(charArray[0]);
            defaultName = new String(charArray);
            if (defaultName.endsWith("Usage")) {
                defaultName = defaultName.substring(0, defaultName.length() - 5);
            }
        } else {
            defaultName = eClassifier.getName();
        }
        return defaultName;
    }
}
