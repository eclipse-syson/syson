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
package org.eclipse.syson.sysml.helper;

public class PrettyPrinter {

    public static String prettyPrintName(String initialName) {
        String name;
        if (initialName == null) {
            name = "";
        } else {
            name = initialName;
        }
        if (name.contains("::")) {
            name = name.substring(name.lastIndexOf("::") + 2);
        }
        if (name.contains(" ")) {
            name = '\'' + name + '\'';
        }
        return name + " ";
    }
}
