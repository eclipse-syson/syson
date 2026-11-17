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
package org.eclipse.syson.sysml.parser.adapter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

/**
 * Default implementation.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class AbstractTextAdapter implements ITextAdapter {

    @Override
    public void notifyChanged(Notification notification) {
        // nothing to do here
    }

    @Override
    public Notifier getTarget() {
        return null;
    }

    @Override
    public void setTarget(Notifier newTarget) {
        // nothing to do here
    }

    @Override
    public boolean isAdapterForType(Object type) {
        return type == ITextAdapter.class;
    }

}
