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
package org.eclipse.syson.diagram.general.view;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.util.DescriptionNameGenerator;

/**
 * Name generator for all General View description providers.
 * 
 * @author arichard
 */
public class GVDescriptionNameGenerator extends DescriptionNameGenerator {

    private static final String PREFIX = "GV";

    public static String getName(String descType, String type) {
        return getName(PREFIX, descType, type);
    }

    public static String getNodeName(String type) {
        return getNodeName(PREFIX, type);
    }

    public static String getNodeName(EClass eClass) {
        return getNodeName(PREFIX, eClass.getName());
    }

    public static String getCompartmentName(EClass eClass, EReference eReference) {
        return getCompartmentName(PREFIX, eClass.getName() + " " + eReference.getName());
    }

    public static String getCompartmentItemName(EClass eClass, EReference eReference) {
        return getCompartmentItemName(PREFIX, eClass.getName() + " " + eReference.getName());
    }

    public static String getEdgeName(String type) {
        return getEdgeName(PREFIX, type);
    }
}
