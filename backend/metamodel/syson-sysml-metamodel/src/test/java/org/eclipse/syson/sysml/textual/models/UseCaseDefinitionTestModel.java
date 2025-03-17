/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.sysml.textual.models;

import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Simple test model for UseCaseDefinition.
 *
 * <pre>
  package Root {
        use case def Ucd_1 {
            objective reqU_1;
            subject subject_1 : partD_1;
            actor partU_1;
        }

        part def partD_1;

        use case ucu_1 : Ucd_1 {
            actor :>> partU_1;
        }
    }
 * </pre>
 *
 * @author Arthur Daussy
 */
public class UseCaseDefinitionTestModel {

    private final ModelBuilder builder = new ModelBuilder();

    private UseCaseDefinition useCaseDefinition;

    private ObjectiveMembership objectiveMembership;

    private SubjectMembership subject;

    private ActorMembership actor;

    private PartUsage actorPartUsage;

    private UseCaseUsage useCaseUsage;

    public UseCaseDefinitionTestModel() {
        super();
        this.build();
    }

    private void build() {
        Package rootPack = this.builder.createWithName(Package.class, "Root");

        this.useCaseDefinition = this.builder.createInWithName(UseCaseDefinition.class, rootPack, "Ucd_1");

        this.objectiveMembership = this.builder.createIn(ObjectiveMembership.class, this.useCaseDefinition);
        this.subject = this.builder.createIn(SubjectMembership.class, this.useCaseDefinition);
        this.actor = this.builder.createIn(ActorMembership.class, this.useCaseDefinition);

        ReferenceUsage referenceUsage = this.builder.createWithName(ReferenceUsage.class, "subject_1");
        this.subject.getOwnedRelatedElement().add(referenceUsage);

        PartDefinition partDefinition = this.builder.createInWithName(PartDefinition.class, rootPack, "partD_1");
        this.builder.setType(referenceUsage, partDefinition);

        this.actorPartUsage = this.builder.createWithName(PartUsage.class, "partU_1");
        this.actor.getOwnedRelatedElement().add(this.actorPartUsage);

        RequirementUsage requirementUsage = this.builder.createWithName(RequirementUsage.class, "reqU_1");
        this.objectiveMembership.getOwnedRelatedElement().add(requirementUsage);

        this.useCaseUsage = this.builder.createInWithName(UseCaseUsage.class, rootPack, "ucu_1");
        this.builder.setType(this.useCaseUsage, this.useCaseDefinition);

        ActorMembership redefinedActor = this.builder.createIn(ActorMembership.class, this.useCaseUsage);

        PartUsage redefinedActionPartUsage = this.builder.create(PartUsage.class);

        redefinedActor.getOwnedRelatedElement().add(redefinedActionPartUsage);

        this.builder.addRedefinition(redefinedActionPartUsage, this.actorPartUsage);
    }

    public UseCaseDefinition getUseCaseDefinition() {
        return this.useCaseDefinition;
    }

    public ActorMembership getActor() {
        return this.actor;
    }

    public PartUsage getActorPartUsage() {
        return this.actorPartUsage;
    }

    public UseCaseUsage getUseCaseUsage() {
        return this.useCaseUsage;
    }

}
