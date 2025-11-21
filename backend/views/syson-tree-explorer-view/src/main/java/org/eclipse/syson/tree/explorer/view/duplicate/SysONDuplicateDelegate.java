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
package org.eclipse.syson.tree.explorer.view.duplicate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.representations.Failure;
import org.eclipse.sirius.components.representations.IStatus;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.representations.Success;
import org.eclipse.sirius.web.application.views.explorer.services.api.DuplicationSettings;
import org.eclipse.sirius.web.application.views.explorer.services.api.IObjectDuplicator;
import org.eclipse.sirius.web.application.views.explorer.services.api.IObjectDuplicatorDelegate;
import org.eclipse.syson.model.services.ModelMutationElementService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.stereotype.Service;

/**
 * SysON implementation of {@link IObjectDuplicatorDelegate} that properly handles the creation of the required
 * Membership when duplicating an Element.
 *
 * @author Arhtur Daussy
 */
@Service
public class SysONDuplicateDelegate implements IObjectDuplicatorDelegate {

    private final ModelMutationElementService modelMutationElementService;

    public SysONDuplicateDelegate(ModelMutationElementService modelMutationElementService) {
        this.modelMutationElementService = Objects.requireNonNull(modelMutationElementService);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, EObject objectToDuplicate, EObject containerEObject, String containmentFeature, DuplicationSettings settings) {
        return objectToDuplicate instanceof Element && !(objectToDuplicate instanceof Membership);
    }

    @Override
    public IStatus duplicateObject(IEditingContext editingContext, EObject objectToDuplicate, EObject containerEObject, String containmentFeature, DuplicationSettings settings) {
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
                    Optional<Element> duplicateElement = this.modelMutationElementService.duplicateElement((Element) objectToDuplicate, settings.duplicateContent(), settings.copyOutgoingReferences());
                    if (duplicateElement.isPresent()) {
                        owningMembership.getOwnedRelatedElement().add(duplicateElement.get());
                        final List<Message> messages = this.getMessages(settings);
                        return new Success(ChangeKind.SEMANTIC_CHANGE, Map.of(IObjectDuplicator.NEW_OBJECT, duplicateElement.get()), messages);
                    }
                }
            }
        }
        return new Failure("Unable to duplicate the selected Element on the targeted Element");
    }

    private List<Message> getMessages(DuplicationSettings settings) {
        final List<Message> messages;
        if (settings.updateIncomingReferences()) {
            messages = List.of(new Message("The option 'Update incoming references' is ignored. It is not yet handled in SysON.", MessageLevel.INFO));
        } else {
            messages = List.of();
        }
        return messages;
    }
}
