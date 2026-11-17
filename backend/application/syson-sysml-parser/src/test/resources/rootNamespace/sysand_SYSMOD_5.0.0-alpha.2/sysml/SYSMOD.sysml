//
// Library SYSMOD - The Systems Modeling Toolbox
//
// The library provides concepts of the common and general purpose MBSE methodology SYSMOD.
// 
// Version: 5.0.0-alpha.2 (v4.x are SYSMOD for SysML v1 versions); work in progress
// License: Apache-2.0
// Maintainer: MBSE4U, Tim Weilkiens, Contact: tim@mbse4u.com
// Website: https://mbse4u.com/sysmod
// Topic: MBSE, Methodology
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// Copyright 2025 MBSE4U
//
library package SYSMOD {
    doc 
    /*
     * Definition of SYSMOD Concepts for SysML v2
     *
     * Version v5.1alpha 
     *
     * Copyright 2025, MBSE4U
     *
     */
    abstract occurrence def Project {
        doc /* The Project provides the context for the main engineering artifacts and builds the cross relationships. */

        private import RequirementDerivation::*;

        //
        // Level -1
        //

        // Problem Space - Brownfield & Problem Statement
        part brownfieldSystemContext : SystemContext;       
        requirement problemStatement : ProblemStatement {
            subject system :> brownfieldSystemContext.systemOfInterest;
        }
        part problemStakeholders [1..*] : Stakeholder;
        
        // Solution Space - System Idea
        part systemIdeaContext :> brownfieldSystemContext;
        satisfy problemStatement by systemIdeaContext.systemOfInterest;

        //
        // Level 0
        //

        // Problem Space
        part requirementSystemContext :> systemIdeaContext;
        part systemStakeholders [1..*] : Stakeholder;
        requirement systemRequirementSpecification {
            subject system :> requirementSystemContext.systemOfInterest;
        }
        #derivation connection {
            end #original ::> problemStatement;
            end #derive ::> systemRequirementSpecification;
        }

        // Solution Space
        part systemContext :> requirementSystemContext;
        part functionalContext :> systemContext;
        part logicalContext :> systemContext;
        part productContext :> systemContext;
        satisfy systemRequirementSpecification by systemContext.systemOfInterest;

        // Execute the process
        action mbseProcess[1] {
            out requirement problemStatement = Project::problemStatement;
            out part problemStakeholders[*] = Project::problemStakeholders;
            out part brownfieldContext = Project::brownfieldSystemContext;

            first start then analyzeProblem;
            perform action analyzeProblem : SYSMOD_Process::'Analyze the Problem' {
                out requirement problemStatement = mbseProcess.problemStatement;
                out part problemStakeholders[*] = mbseProcess.problemStakeholders;
                out part brownfieldContext = mbseProcess.brownfieldContext;
            }
            flow of Requirements::RequirementCheck from analyzeProblem.problemStatement to identifySystemIdea.problemStatement;
            first analyzeProblem then identifySystemIdea;
            perform action identifySystemIdea : SYSMOD_Process::'Specify the System Idea and System Objectives' {
                in requirement problemStatement;
                out part systemIdeaContext = Project::systemIdeaContext;
                out requirement systemObjectives [*];
            }
        }
    }
    occurrence projects [*] nonunique : Project;

    abstract requirement def ProblemStatement {
        doc /* The problem that is to be solved by the system. */
        assert constraint {
            SequenceFunctions::size(stakeholders) > 0 
        }
    }
    requirement problemStatements[0..*] nonunique : ProblemStatement;

    abstract part def SystemContext {
        doc /*
             * The System Context includes all system actors, the system of interest, 
             * and the relationships between the actors and the system.
             */
        part actors [*];
        part theSystem : System;
        alias <soi> systemOfInterest for theSystem;
    }
    part systemContexts [*] nonunique : SystemContext;

    abstract part def System :> SystemPart;
    part systems [*] nonunique : System;

    abstract part def SystemPart {
        attribute maturity : MaturityKind default MaturityKind::initial;
        attribute totalMaturity = (RealFunctions::sum(systemParts.maturity) + maturity) 
                                / (SequenceFunctions::size(systemParts) + 1);
        part systemParts [*] :SystemPart :> subparts;
    }
    part systemParts [*] nonunique : SystemPart;

    enum def MaturityKind :> ScalarValues::Real {
        final = 1.0;
        complete = 0.75;
        inProgress = 0.5;
        started = 0.25;
        initial = 0.0;
    }

    abstract part def Stakeholder {
        doc /* SYSMOD-specific definition of the stakeholder concept */
        attribute risk : PriorityKind;
        attribute effort : PriorityKind;
        attribute contact : ScalarValues::String;
        attribute categories [*] : StakeholderCategoryKind;
    }
    part stakeholders [*] nonunique : Stakeholder;
    enum def PriorityKind :> ScalarValues::Real {
        none = 0.0;
        low = 0.25;
        medium = 0.5;
        high = 0.75;
        critial = 1.0;
    }
    enum def StakeholderCategoryKind {
        Expert;
        RequirementOwner;
        User;
        Other;
    }

    abstract requirement def SystemObjective {
        doc /*
             * A goal that the system is aiming for. It does not have to be satisfied, 
             * but an evaluation must show that the system has brought the objective closer. 
             * Of course, it may also be satisfied.
             */
    }
    requirement systemObjectives [*] : SystemObjective;

    abstract requirement def ExtendedRequirement {
        // tbd
    }
    requirement extendedRequirements [*] nonunique : ExtendedRequirement;

    //
    // Semantic Metadata
    //
    metadata def <project> ProjectContextMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = projects meta SysML::OccurrenceUsage;
    }   
    metadata def <systemContext> SystemContextMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = systemContexts meta SysML::PartUsage;
    }   
    metadata def <system> SystemMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = systems meta SysML::PartUsage;
    }
    metadata def <systemPart> SystemPartMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = systemParts meta SysML::PartUsage;
    }
    metadata def <exStakeholder> StakeholderMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = stakeholders meta SysML::PartUsage;
    }   
    metadata def <systemObjective> SystemObjectiveMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = systemObjectives meta SysML::RequirementUsage;
    }   
    metadata def <extendedRequirement> ExtendedRequirementMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = extendedRequirements meta SysML::RequirementUsage;
    }
}

package SYSMOD_Process {
    action def 'Analyze the Problem' {
        out requirement problemStatement;
        out part stakeholders[*];
        out part brownfieldContext;
    }
    action def 'Specify the System Idea and System Objectives' {
        in requirement problemStatement;
        out part systemIdea;
        out requirement systemObjectives [*];
    }
}
