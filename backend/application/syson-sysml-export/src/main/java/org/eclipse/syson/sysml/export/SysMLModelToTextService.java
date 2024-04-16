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
package org.eclipse.syson.sysml.export;

import org.eclipse.syson.sysml.Element;
import org.springframework.stereotype.Service;

/**
 * Base implementation of {@link ISysMLModelToTextService}.
 * 
 * @author Arthur Daussy
 */
@Service
public class SysMLModelToTextService implements ISysMLModelToTextService {

    @Override
    public String toText(Element element) {
        return new SysMLElementSerializer().doSwitch(element);
    }

}
