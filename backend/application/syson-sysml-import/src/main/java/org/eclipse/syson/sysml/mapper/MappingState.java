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
package org.eclipse.syson.sysml.mapper;

import java.util.Collection;

/**
 * Records the state of mapping processes, tracking elements to map, resolve, and completed mappings.
 *
 * @author gescande
 */
public record MappingState(Collection<MappingElement> toMap, Collection<MappingElement> toResolve, Collection<MappingElement> done) {

}
