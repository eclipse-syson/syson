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
package org.eclipse.syson.form.services.api;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.sysml.Element;

/**
 * Provides custom Help text for the Details view widgets.
 *
 * @author mcharfadi
 */
public interface IDetailsViewHelpTextProvider {

    boolean canHandle(Element element, EStructuralFeature eStructuralFeature);

    String getHelpText(Element element, EStructuralFeature eStructuralFeature);

}
