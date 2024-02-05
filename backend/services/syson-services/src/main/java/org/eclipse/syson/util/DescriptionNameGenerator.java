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
package org.eclipse.syson.util;

/**
 * Name generator for all SysON description providers.
 * 
 * @author arichard
 */
public class DescriptionNameGenerator {

    protected static String getName(String prefix, String descType, String type) {
        StringBuilder name = new StringBuilder();
        name.append(prefix)
            .append(" ")
            .append(descType)
            .append(" ")
            .append(type);
        return name.toString();
    }

    protected static String getNodeName(String prefix, String type) {
        return getName(prefix, "Node", type);
    }

    protected static String getCompartmentName(String prefix, String type) {
        return getName(prefix, "Compartment", type);
    }

    protected static String getCompartmentItemName(String prefix, String type) {
        return getName(prefix, "CompartmentItem", type);
    }

    protected static String getEdgeName(String prefix, String type) {
        return getName(prefix, "Edge", type);
    }
}
