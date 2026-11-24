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

import java.util.Objects;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Retains the parsed comment body until the model's body changes.
 *
 * @author ylussaud
 */
public class CommentBodyAdapter extends AbstractTextAdapter implements ICommentBodyAdapter {

    private final String rawBody;

    private Notifier target;

    /**
     * Creates the adapter.
     *
     * @param rawBody
     *            the exact source block comment
     */
    public CommentBodyAdapter(String rawBody) {
        this.rawBody = rawBody;
    }

    @Override
    public String getRawBody() {
        return this.rawBody;
    }

    /**
     * Discards the captured source when the attached object's body changes.
     */
    @Override
    public void notifyChanged(Notification notification) {
        if (notification.getNotifier() == this.target && this.target != null
                && this.isBodyFeature(notification.getFeature())
                && !Objects.equals(notification.getOldValue(), notification.getNewValue())) {
            this.target.eAdapters().remove(this);
        }
    }

    /**
     * Returns whether the feature stores the body of a comment or textual representation.
     */
    private boolean isBodyFeature(Object feature) {
        return feature == SysMLPackage.eINSTANCE.getComment_Body()
                || feature == SysMLPackage.eINSTANCE.getTextualRepresentation_Body();
    }

    @Override
    public Notifier getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(Notifier newTarget) {
        this.target = newTarget;
    }

    @Override
    public boolean isAdapterForType(Object type) {
        return type == ICommentBodyAdapter.class || super.isAdapterForType(type);
    }

}
