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
package org.eclipse.syson.application.migration;

import java.util.Objects;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationContent;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;

/**
 * Adapter to add on every Element that is candidate for the {@link DiagramOnViewUsageMigrationParticipant}/
 * {@link DiagramOnViewUsageMigrationHook}. In {@link DiagramOnViewUsageMigrationParticipant}, the adapter should be
 * added on Element having diagrams and on which the new ViewUsage will be created. Then Then in
 * {@link DiagramOnViewUsageMigrationHook}, the adapter is used to move the semantic Element associated to existing
 * diagrams, then the adapter is deleted.
 *
 * @author arichard
 */
public class DiagramOnViewUsageMigrationAdapter implements Adapter {

    private Notifier notifier;

    private final RepresentationMetadata representationMetadata;

    private final RepresentationContent representationContent;

    public DiagramOnViewUsageMigrationAdapter(RepresentationMetadata representationMetadata, RepresentationContent representationContent) {
        this.representationMetadata = Objects.requireNonNull(representationMetadata);
        this.representationContent = Objects.requireNonNull(representationContent);
    }

    @Override
    public void notifyChanged(Notification notification) {
        // do nothing
    }

    @Override
    public Notifier getTarget() {
        return this.notifier;
    }

    @Override
    public void setTarget(Notifier newTarget) {
        this.notifier = newTarget;
    }

    @Override
    public boolean isAdapterForType(Object type) {
        return false;
    }

    public RepresentationContent getRepresentationContent() {
        return this.representationContent;
    }

    public RepresentationMetadata getRepresentationMetadata() {
        return this.representationMetadata;
    }
}
