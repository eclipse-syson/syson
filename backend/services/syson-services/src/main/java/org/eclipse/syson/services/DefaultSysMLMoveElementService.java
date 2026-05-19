/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.services;

import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.services.api.IDefaultSysMLMoveElementService;
import org.eclipse.syson.services.api.ISysMLMoveElementCheckerService;
import org.eclipse.syson.services.api.MoveStatus;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipExpose;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.stereotype.Service;

/**
 * Default {@link IDefaultSysMLMoveElementService} for SysML elements.
 *
 * @author Arthur Daussy
 */
@Service
public class DefaultSysMLMoveElementService implements IDefaultSysMLMoveElementService {

    private final DeleteService deleteService;

    private final UtilService utilService;

    private final ISysMLMoveElementCheckerService moveElementCheckerService;

    public DefaultSysMLMoveElementService(ISysMLMoveElementCheckerService moveElementCheckerService) {
        this.moveElementCheckerService = Objects.requireNonNull(moveElementCheckerService);
        this.deleteService = new DeleteService();
        this.utilService = new UtilService();
    }

    @Override
    public MoveStatus moveSemanticElement(Element element, Element newParent) {
        final MoveStatus moveStatus;
        MoveStatus canMoveStatus = this.moveElementCheckerService.canMove(element, newParent);
        if (!canMoveStatus.isSuccess()) {
            moveStatus = canMoveStatus;
        } else if (element == newParent) {
            moveStatus = MoveStatus.buildSuccess();
        } else if (newParent instanceof Membership) {
            moveStatus = MoveStatus.buildFailure("Membership can't be used as a target of a move");
        } else if (element instanceof Import imprt) {
            newParent.getOwnedRelationship().add(0, imprt);
            moveStatus = MoveStatus.buildSuccess();
        } else {
            moveStatus = this.moveWithMembership(element, newParent);
        }
        return moveStatus;
    }

    /**
     * Moves the owning membership of {@code element} to {@code parent}.
     * <p>
     * This method may create a new membership in {@code parent}, potentially with a different type than
     * {@code element.getOwningMembership()}. For example, an element moved into a package will have an
     * {@link OwningMembership} instance as its parent, regardless of its original containing membership.
     * </p>
     *
     * @param element
     *            the element that has been dropped
     * @param parent
     *            the element inside which the drop has been performed
     * @return true if the given element has been moved
     */
    private MoveStatus moveWithMembership(Element element, Element parent) {
        final MoveStatus moveStatus;

        if (element != null && element.eContainer() instanceof Membership currentMembership) {
            if (parent instanceof Package) {
                // the expected membership should be an OwningMembership
                if (currentMembership instanceof FeatureMembership) {
                    var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                    // Add the new membership to its container first to make sure its content stays in the same
                    // resource. Otherwise the cross-referencer will delete all the references pointing to its
                    // related element, which will have unexpected results on the model.
                    parent.getOwnedRelationship().add(owningMembership);
                    moveStatus = MoveStatus.buildSuccess();
                    owningMembership.getOwnedRelatedElement().add(element);
                    // If the currentMembership is exposed, we need to add new links between the MembershipExposes and
                    // the new owningMembership
                    var eInverseRelatedElements = this.utilService.getEInverseRelatedElements(currentMembership, SysmlPackage.eINSTANCE.getMembershipImport_ImportedMembership());
                    for (EObject eObject : eInverseRelatedElements) {
                        if (eObject instanceof MembershipExpose membershipExpose) {
                            membershipExpose.setImportedMembership(owningMembership);
                        }
                    }
                    this.deleteService.deleteFromModel(currentMembership);
                } else {
                    parent.getOwnedRelationship().add(currentMembership);
                    moveStatus = MoveStatus.buildSuccess();
                }
            } else {
                // the expected membership should be a FeatureMembership
                if (currentMembership instanceof FeatureMembership) {
                    parent.getOwnedRelationship().add(currentMembership);
                    moveStatus = MoveStatus.buildSuccess();
                } else {
                    var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
                    parent.getOwnedRelationship().add(featureMembership);
                    moveStatus = MoveStatus.buildSuccess();
                    featureMembership.getOwnedRelatedElement().add(element);
                    // If the currentMembership is exposed, we need to add new links between the MembershipExposes and
                    // the new featureMembership
                    var eInverseRelatedElements = this.utilService.getEInverseRelatedElements(currentMembership, SysmlPackage.eINSTANCE.getMembershipImport_ImportedMembership());
                    for (EObject eObject : eInverseRelatedElements) {
                        if (eObject instanceof MembershipExpose membershipExpose) {
                            membershipExpose.setImportedMembership(featureMembership);
                        }
                    }
                    this.deleteService.deleteFromModel(currentMembership);
                }
            }
        } else {
            moveStatus = MoveStatus.buildFailure("Element is not contained in a membership");
        }
        return moveStatus;
    }
}
