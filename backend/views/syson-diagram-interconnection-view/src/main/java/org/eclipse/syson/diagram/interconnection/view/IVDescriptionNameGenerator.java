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
package org.eclipse.syson.diagram.interconnection.view;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.util.DescriptionNameGenerator;

/**
 * Name generator for all Interconnection View description providers.
 *
 * @author arichard
 */
public class IVDescriptionNameGenerator extends DescriptionNameGenerator implements ILeveledNodeDescriptionNameGenerator {

    public IVDescriptionNameGenerator() {
        super("IV");
    }

    @Override
    public String getFirstLevelNodeName(EClass eClass) {
        return this.getName(this.getDiagramPrefix(), "FirstLevelNode", eClass.getName());
    }

    @Override
    public String getChildNodeName(EClass eClass) {
        return this.getName(this.getDiagramPrefix(), "ChildNode", eClass.getName());
    }
}
