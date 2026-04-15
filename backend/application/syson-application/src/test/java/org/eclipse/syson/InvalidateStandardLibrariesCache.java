/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Invalidates the standard libraries cache for the annotated method or class.
 * <p>
 * Using this annotation will ensure the editing contexts of the annotated method/class will be loaded from scratch, without any cached version of the standard libraries. Using this annotation
 * invalidates the standard libraries cache for all the editing contexts. Using this annotation without the {@code org.eclipse.syson.test.cacheStandardLibraries=true} property doesn't have any
 * effect, since standard libraries won't be cached at all.
 * </p>
 *
 * @author gdaniel
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(InvalidateStandardLibrariesCacheExtension.class)
public @interface InvalidateStandardLibrariesCache {
}
