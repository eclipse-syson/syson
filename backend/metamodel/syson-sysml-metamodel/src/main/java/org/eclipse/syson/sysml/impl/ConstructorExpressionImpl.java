/**
 * Copyright (c) 2023, 2025 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.syson.sysml.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.ConstructorExpression;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Constructor Expression</b></em>'. <!--
 * end-user-doc -->
 *
 * @generated
 */
public class ConstructorExpressionImpl extends InstantiationExpressionImpl implements ConstructorExpression {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ConstructorExpressionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getConstructorExpression();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Expression> getArgument() {
        List<Expression> arguments = new ArrayList<>();
        Type instantiatedType = this.getInstantiatedType();
        if (instantiatedType != null) {
            EList<Feature> feature = instantiatedType.getFeature();
            arguments = feature.stream().map(f -> {
                List<Expression> expressions = new ArrayList<>();
                Feature result = this.getResult();
                if (result != null) {
                    expressions = result.getOwnedFeature().stream()
                            .filter(current -> current.redefines(f))
                            .map(current -> this.getValuation(current))
                            .filter(Objects::nonNull)
                            .map(v -> v.getValue())
                            .filter(Objects::nonNull)
                            .toList();
                }
                return expressions;
            }).flatMap(Collection::stream).toList();
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getInstantiationExpression_Argument(), arguments.size(), arguments.toArray());
    }

    /**
     * @generated NOT
     */
    private FeatureValue getValuation(Feature feature) {
        return feature.getOwnedMembership().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst()
                .orElse(null);
    }
} // ConstructorExpressionImpl
