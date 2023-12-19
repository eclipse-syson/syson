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
package org.eclipse.syson.sysml;

import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Facilitates the conversion of Ecore models to SysML representations.
 *
 * @author gescande.
 */
@Component
public class EcoreToSysml {

    private final Logger logger = LoggerFactory.getLogger(SysmlToAst.class);

    public String convert(EObject rootObject) {
        String output = "";

        return output;

    }

}
