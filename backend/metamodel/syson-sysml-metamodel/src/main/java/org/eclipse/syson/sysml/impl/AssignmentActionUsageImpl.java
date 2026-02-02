/*******************************************************************************
 * Copyright (c) 2023, 2026 Obeo.
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
package org.eclipse.syson.sysml.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.AssignmentActionUsage;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Assignment Action Usage</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.AssignmentActionUsageImpl#getReferent <em>Referent</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.AssignmentActionUsageImpl#getTargetArgument <em>Target Argument</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.AssignmentActionUsageImpl#getValueExpression <em>Value Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssignmentActionUsageImpl extends ActionUsageImpl implements AssignmentActionUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected AssignmentActionUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getAssignmentActionUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getReferent() {
        Feature referent = this.basicGetReferent();
        return referent != null && referent.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) referent) : referent;
    }

    /**
     * <!-- begin-user-doc --> From Sysmlv2 specification: The referent of an {@link AssignmentActionUsage} is the first
     * Feature that is the memberElement of a ownedMembership that is not a FeatureMembership. <br>
     * <br>
     * <code> referent = let unownedFeatures : Sequence(Feature) = ownedMembership-><br>
     *     reject(oclIsKindOf(FeatureMembership)).memberElement-> selectByKind(Feature) in<br>
     *     if unownedFeatures->isEmpty()<br>
     *     then null else unownedFeatures->first().oclAsType(Feature) endif
     * </code> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetReferent() {
        var unownedFeatures = this.getOwnedRelationship().stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
                .filter(m -> !(m instanceof FeatureMembership))
                .map(m -> m.getMemberElement())
                .toList();
        if (unownedFeatures.isEmpty()) {
            return null;
        }
        return unownedFeatures.stream()
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Expression getTargetArgument() {
        Expression targetArgument = this.basicGetTargetArgument();
        return targetArgument != null && targetArgument.eIsProxy() ? (Expression) this.eResolveProxy((InternalEObject) targetArgument) : targetArgument;
    }

    /**
     * <!-- begin-user-doc --> From Sysmlv2 specification: The <code>targetArgument</code> of a
     * {@link AssignmentActionUsage} is its first argument <code>Expression</code>.<br>
     * <br>
     *
     * <code>
     * valueExpression = argument(1)
     * </code> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Expression basicGetTargetArgument() {
        return this.argument(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Expression getValueExpression() {
        Expression valueExpression = this.basicGetValueExpression();
        return valueExpression != null && valueExpression.eIsProxy() ? (Expression) this.eResolveProxy((InternalEObject) valueExpression) : valueExpression;
    }

    /**
     * <!-- begin-user-doc --> From Sysmlv2 specification: The <code>valueExpression</code> of a
     * {@link AssignmentActionUsage} is its second argument <code>Expression</code>.<br>
     * <br>
     *
     * <code>
     * valueExpression = argument(2)
     * </code> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Expression basicGetValueExpression() {
        return this.argument(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.ASSIGNMENT_ACTION_USAGE__REFERENT:
                if (resolve) {
                    return this.getReferent();
                }
                return this.basicGetReferent();
            case SysmlPackage.ASSIGNMENT_ACTION_USAGE__TARGET_ARGUMENT:
                if (resolve) {
                    return this.getTargetArgument();
                }
                return this.basicGetTargetArgument();
            case SysmlPackage.ASSIGNMENT_ACTION_USAGE__VALUE_EXPRESSION:
                if (resolve) {
                    return this.getValueExpression();
                }
                return this.basicGetValueExpression();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.ASSIGNMENT_ACTION_USAGE__REFERENT:
                return this.basicGetReferent() != null;
            case SysmlPackage.ASSIGNMENT_ACTION_USAGE__TARGET_ARGUMENT:
                return this.basicGetTargetArgument() != null;
            case SysmlPackage.ASSIGNMENT_ACTION_USAGE__VALUE_EXPRESSION:
                return this.basicGetValueExpression() != null;
        }
        return super.eIsSet(featureID);
    }

} // AssignmentActionUsageImpl
