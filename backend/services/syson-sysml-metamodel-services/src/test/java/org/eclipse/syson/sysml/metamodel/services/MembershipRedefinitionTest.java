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
package org.eclipse.syson.sysml.metamodel.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.helper.MembershipComputer;
import org.eclipse.syson.sysml.util.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests semantic replacement through the membership helper used by model services and serialization.
 *
 * @author Arthur Daussy
 */
public class MembershipRedefinitionTest {

    /**
     * The services helper exposes the same surviving feature identities regardless of names or diamond branch order.
     */
    @ParameterizedTest
    @CsvSource({
        "Renamed, Renamed::renamed",
        "Transitive, Transitive::finalValue",
        "SharedSource, SharedSource::selected",
        "Anonymous, Anonymous::x",
        "SameName, SameName::x",
        "Diamond, Right::rightValue",
        "ReverseDiamond, Right::rightValue",
        "DiamondSameName, RightSameName::x",
        "ReverseDiamondSameName, RightSameName::x",
        "typedUsage, Renamed::renamed",
        "configuredUsage, configuredUsage::configured"
    })
    @DisplayName("GIVEN a redefining context, WHEN computing visible memberships through services, THEN only effective features remain")
    public void visibleMemberships(String contextName, String replacementName) throws IOException {
        // See /redefinitions/inherited-redefinitions.xmi for the textual SysML model in an XML comment.
        var model = SysMLTestModel.load("/redefinitions/inherited-redefinitions.xmi");
        var context = model.get(Type.class, "Inheritance::" + contextName);
        var replacement = model.get(Feature.class, "Inheritance::" + replacementName);
        var preserved = model.get(Feature.class, "Inheritance::Base::preserved");
        var memberships = new MembershipComputer<>(context, new BasicEList<>()).visibleMemberships(false, true, true);
        assertThat(memberships.stream()
                .filter(FeatureMembership.class::isInstance)
                .map(membership -> membership.getMemberElement())
                .toList()).containsExactlyInAnyOrder(replacement, preserved);
    }
}
