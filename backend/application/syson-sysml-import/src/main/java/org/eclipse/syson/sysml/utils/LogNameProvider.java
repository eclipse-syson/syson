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
package org.eclipse.syson.sysml.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.Element;

/**
 * Helper used to compute a qualified name of an element to be used inside a log message.
 *
 * @author Arthur Daussy
 */
public class LogNameProvider {

    public String getName(EObject eObject) {
        return "[" + eObject.eClass().getName() + "] " + this.getQualifiedName(eObject);
    }

    private String getQualifiedName(EObject eObject) {
        if (eObject instanceof Element element) {
            String qualifiedName = element.getQualifiedName();
            if (qualifiedName == null && element.eContainer() != null) {
                qualifiedName = this.getQualifiedName(element.eContainer()) + "::<" + eObject.eClass().getName() + ">";
            }
            return qualifiedName;
        }
        return "";
    }
}
