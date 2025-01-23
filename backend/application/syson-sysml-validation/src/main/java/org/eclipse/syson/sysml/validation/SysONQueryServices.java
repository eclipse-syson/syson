/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.sysml.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.syson.sysml.Element;

/**
 * Services used by the validation rules in SysON.
 *
 * @author arichard
 */
public class SysONQueryServices {

    /**
     * Service allowing to replace the interval '..' operator of OCL, which does not exist in AQL.
     *
     * @param self
     *            an {@link Element} on which the service will be called.
     * @param lowerBound
     *            the lower bound of the interval.
     * @param upperBound
     *            the upper bound of the interval.
     * @return the interval from the lower to the upper bound as a list of integer.
     */
    public List<Integer> interval(Element self, Integer lowerBound, Integer upperBound) {
        List<Integer> interval = new ArrayList<>();
        if (upperBound >= lowerBound) {
            for (int i = lowerBound; i <= upperBound; i++) {
                interval.add(i);
            }
        }
        return interval;
    }
}
