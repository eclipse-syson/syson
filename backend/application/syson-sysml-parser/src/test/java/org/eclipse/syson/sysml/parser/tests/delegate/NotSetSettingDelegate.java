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
package org.eclipse.syson.sysml.parser.tests.delegate;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Internal.SettingDelegate;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicSettingDelegate.Stateless;

/**
 * A never set {@link SettingDelegate}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class NotSetSettingDelegate extends Stateless {

    public NotSetSettingDelegate(EStructuralFeature eStructuralFeature) {
        super(eStructuralFeature);
    }

    @Override
    protected Object get(InternalEObject owner, boolean resolve, boolean coreType) {
        return this.eStructuralFeature.getDefaultValue();
    }

    @Override
    protected boolean isSet(InternalEObject owner) {
        return false;
    }

}
