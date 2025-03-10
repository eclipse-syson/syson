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
package org.eclipse.syson.application.omnibox.api;

import java.util.function.Predicate;

/**
 * {@link Predicate} that returns whether an {@link String editingContextId} supports publishing SysML Projects.
 *
 * @author flatombe
 */
public interface IPredicateCanEditingContextPublishSysMLProject extends Predicate<String> {

}