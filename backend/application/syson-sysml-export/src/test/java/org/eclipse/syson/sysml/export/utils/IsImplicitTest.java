/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.sysml.export.utils;

import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Testing the isImpicit API which is looking if an element is implicit, if so then the element should not have a
 * textual representation. An element is implicit if its marked with "isImplied" or if its children are implicit,
 * recursively.
 *
 * <pre>
 * objective {
 *      subject;
 *      return out;
 * }
 * </pre>
 *
 * @author wbilem
 */
public class IsImplicitTest {

    private final ModelBuilder builder = new ModelBuilder();

    private final ObjectiveMembership objective;

    public IsImplicitTest() {
        this.objective = this.builder.create(ObjectiveMembership.class);
        this.isImplicitTest();
    }

    public ObjectiveMembership getObjectiveMembership() {
        return this.objective;
    }

    private void isImplicitTest() {
        RequirementUsage requirementUsage = this.builder.create(RequirementUsage.class);
        requirementUsage.setIsImpliedIncluded(true);
        this.objective.getOwnedRelatedElement().add(requirementUsage);

        SubjectMembership subjectMembership = this.builder.create(SubjectMembership.class);
        requirementUsage.getOwnedRelationship().add(subjectMembership);

        ReferenceUsage refUsage1 = this.builder.create(ReferenceUsage.class);
        refUsage1.setIsImpliedIncluded(true);
        subjectMembership.getOwnedRelatedElement().add(refUsage1);

        Redefinition redefinition1 = this.builder.create(Redefinition.class);
        redefinition1.setIsImplied(true);
        redefinition1.setRedefiningFeature(refUsage1);
        refUsage1.getOwnedRelationship().add(redefinition1);

        ReturnParameterMembership returnParamMembership = this.builder.create(ReturnParameterMembership.class);
        requirementUsage.getOwnedRelationship().add(returnParamMembership);

        ReferenceUsage refUsage2 = this.builder.create(ReferenceUsage.class);
        refUsage2.setIsImpliedIncluded(true);
        returnParamMembership.getOwnedRelatedElement().add(refUsage2);

        Redefinition redefinition2 = this.builder.create(Redefinition.class);
        redefinition2.setIsImplied(true);
        redefinition2.setRedefiningFeature(refUsage2);
        refUsage2.getOwnedRelationship().add(redefinition2);

        Redefinition redefinition3 = this.builder.create(Redefinition.class);
        redefinition3.setIsImplied(true);
        redefinition3.setRedefiningFeature(requirementUsage);
        requirementUsage.getOwnedRelationship().add(redefinition3);
    }
}
