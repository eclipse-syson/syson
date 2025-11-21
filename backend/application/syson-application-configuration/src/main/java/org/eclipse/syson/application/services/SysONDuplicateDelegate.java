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
package org.eclipse.syson.application.services;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.representations.Failure;
import org.eclipse.sirius.components.representations.IStatus;
import org.eclipse.sirius.web.application.views.explorer.services.api.DuplicationSettings;
import org.eclipse.sirius.web.application.views.explorer.services.api.IDefaultObjectDuplicator;
import org.eclipse.sirius.web.application.views.explorer.services.api.IObjectDuplicatorDelegate;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.stereotype.Service;

/**
 * Syson implementation of {@link IObjectDuplicatorDelegate} that properly handles the creation of the required Membership when duplicating an Element.
 *
 * @author Arhtur Daussy
 */
@Service
public class SysONDuplicateDelegate implements IObjectDuplicatorDelegate {

    private final IDefaultObjectDuplicator defaultObjectDuplicator;

    public SysONDuplicateDelegate(IDefaultObjectDuplicator defaultObjectDuplicator) {
        this.defaultObjectDuplicator = defaultObjectDuplicator;
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, EObject objectToDuplicate, EObject containerEObject,
            String containmentFeature, DuplicationSettings settings) {
        return objectToDuplicate instanceof Element && !(objectToDuplicate instanceof Membership);
    }

    @Override
    public IStatus duplicateObject(IEditingContext editingContext, EObject objectToDuplicate, EObject containerEObject,
            String containmentFeature, DuplicationSettings settings) {

        if (containmentFeature.startsWith("membership:") && containerEObject instanceof Element containerElement) {
            EClass membershipEclass = (EClass) SysmlPackage.eINSTANCE.getEClassifier(containmentFeature.replaceFirst("membership:", ""));
            if (membershipEclass != null) {
                EObject targetEClass = SysmlFactory.eINSTANCE.create(membershipEclass);
                if (targetEClass instanceof Membership owningMembership) {
                    containerElement.getOwnedRelationship().add(owningMembership);
                    // Disable the use of "update incoming reference" option since it has too many impacts on SySML metamodel (mainly handling relationships)
                    // An ongoing discussion should decide either to :
                    // * provide a Sirius Web extension to be able to disable this option in downstream projects
                    // * or provide a custom SysML duplication to handle all those case.
                    DuplicationSettings customDialogSetting = new DuplicationSettings(settings.duplicateContent(), settings.copyOutgoingReferences(), false);
                    return this.defaultObjectDuplicator.duplicateObject(editingContext, objectToDuplicate, owningMembership, SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName(),
                            customDialogSetting);
                }
            }
        }
        return new Failure("Unable to duplicate the selected Element on the targeted Element");
    }
}
