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
package org.eclipse.syson.sysml.metamodel.services.textual;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.parser.DanglingLink;

/**
 * Keeps imported reference text on its SysON owner for the lifetime of the loaded element.
 * The OMG metamodel descriptors are shared, but the temporary parsed model is never retained.
 *
 * @author arichard
 */
public final class ImportedReferenceText extends AdapterImpl {

    private final EReference sourceFeature;
    private final EReference omgFeature;
    private final EClass expectedType;
    private final List<String> names;

    /** Captures the text and the corresponding feature on the converted owner. */
    public ImportedReferenceText(EObject owner, DanglingLink link) {
        this.sourceFeature = (EReference) owner.eClass().getEStructuralFeature(link.reference().getName());
        this.omgFeature = link.reference();
        this.expectedType = link.eClass();
        this.names = List.copyOf(link.targets());
    }

    /** Rebinds the captured spelling to the current serialization's OMG copy. */
    DanglingLink forCopy(EObject copy) {
        return new DanglingLink(copy, this.omgFeature, this.expectedType, this.names);
    }

    /** A changed or cleared reference supersedes the spelling originally parsed for it. */
    @Override
    public void notifyChanged(Notification notification) {
        if (notification.getFeature() == this.sourceFeature && !notification.isTouch() && notification.getEventType() != Notification.RESOLVE) {
            this.getTarget().eAdapters().remove(this);
        }
    }
}
