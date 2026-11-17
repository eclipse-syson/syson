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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Internal.SettingDelegate;
import org.eclipse.emf.ecore.EStructuralFeature.Internal.SettingDelegate.Factory;

/**
 * The factory for {@link NotSetSettingDelegate}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class NotSetSettingDelegateFactory implements Factory {

    /**
     * The {@link Map} of created {@link SettingDelegate} for the given {@link EStructuralFeature}.
     */
    private final Map<EStructuralFeature, SettingDelegate> settingDelegates = new HashMap<>();

    @Override
    public SettingDelegate createSettingDelegate(EStructuralFeature eStructuralFeature) {
        return this.settingDelegates.computeIfAbsent(eStructuralFeature, NotSetSettingDelegate::new);
    }

}
