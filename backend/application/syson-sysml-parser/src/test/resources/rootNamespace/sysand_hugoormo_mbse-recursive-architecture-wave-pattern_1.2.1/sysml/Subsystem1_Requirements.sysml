package Subsystem1_Requirements {
    
    /*
    PATTERN RULES FOR SUBSYSTEM1_REQUIREMENTS:
    
    1. ORIGINAL REQUIREMENTS (no binding):
       - Decontextualize parent need at this level's blackbox context
       - Inherit constraint from parent via subsetting (no hardcoding)
       - Subject MUST reference _bb (blackbox) configuration
       - Frame/assume gates capture stakeholder concerns and assumptions
       - No attribute binding at requirement level (binding lives in configuration)
    
    2. DERIVED REQUIREMENTS (with binding and allocation):
       - Decompose original requirement into child needs via budget allocation
       - Subject MUST reference _gb (graybox) configuration
       - Attribute binding MUST define sum: qualityContribution = budget1 + budget2
       - Budget attributes MUST have explicit values with design rationale comments
       - Nested child needs MUST reference budget attributes (not hardcoded values)
       - Constraint MUST reference budget attributes (validates allocation closure)
    
    3. CONSTRAINT INHERITANCE:
       - Original requirement constraints are inherited via subsetting (automatic)
       - DO NOT hardcode inherited constraints in child requirements (DRY violation)
       - Child requirement constraints MUST reference budget attributes (not literal values)
    
    4. CONFIGURATION BINDING STRATEGY:
       - Blackbox configuration (Config_bb): attribute binding to action output
       - Graybox configuration (Config_gb): MUST remain abstract (no binding override)
       - Whitebox configuration (Config_wb): integration level for verification
       - Binding is set once at blackbox level; inherited, never overridden
    
    5. SUBJECT REDEFINITION:
       - Original requirement subject: redefines parent context (narrows focus)
       - Derived requirement subject: redefines parent context (same narrowing)
       - Nested child needs subject: redefine derived requirement's subject
       - Redefinition chain implements context decomposition L0→L1→L2→L3

    6. RECURSIVE CLOSURE SEMANTICS:
       - A nested child need is validated when a lower-level requirement subsets it.
       - The transformed lower-level requirement is verified when architecture satisfies it.
       - See MyFramework::MyFrameworkRecursiveClosureSemantics for the reusable rule.
    */
    
    comment patternRules /*
        This package implements the MBSE RecursiveArchitecture Weave Pattern at L2 (Subsystem level).
        All requirements MUST follow the rules above. Violations detected by:
        - Syside syntax/semantic validation
        - Constraint evaluation on live behavioral outputs
        - Cross-level consistency verification
    */
    
    comment payloadQualityGainPayloadBuilderModelerComment
        about payloadQualityGainPayloadBuilder
        locale "en_US"
    /* L2 original requirement: transforms the L1 derived need into an
     * L2 blackbox requirement.
     */
    requirement payloadQualityGainPayloadBuilder
        subsets
            SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived
                .payloadQualityGainPayloadBuilderNeed {
        comment transformedRequirementClosureSemantics /*
            NEED VALIDATION: By subsetting the L1 child need, this L2 requirement validates
            the transformation of the system-level payloadBuilder need into the Subsystem1
            context.

            REQUIREMENT VERIFICATION: This transformed L2 requirement is verified by
            Subsystem1_Configuration::payloadBuilder_bb and, for decomposition evidence, by
            Subsystem1_Configuration::payloadBuilder_gb.
        */
        subject payloadBuilder redefines mySystemOfInterest subsets Subsystem1_Configuration::payloadBuilder_bb;
        doc
        /* The payload shall achieve a gain of quality through Subsystem1 
         * of at least 14 units. (Here we decontextualize the requirement from L1)
         * This requirement is derived from the subsetted system of interest derived requirement.
         * With this we ensure that Subsystem1 will perform as needed in the
         * context of the system of interest by inheriting the constraint from the 
         * system of interest requirements and redefining the subject.
         */
        comment payloadQualityGainPayloadBuilderConstraintModelerComment
            about payloadQualityGainPayloadBuilder
            locale "en_US"
        /* Constraint inherited from parent requirement through subsetting. */
    }

    comment payloadQualityGainPayloadBuilderDerivedModelerComment
        about payloadQualityGainPayloadBuilderDerived
        locale "en_US"
    /* L2 derived requirement: allocates the L2 requirement to L3 components. */
    requirement payloadQualityGainPayloadBuilderDerived
        subsets SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.payloadQualityGainPayloadBuilderNeed {
        subject payloadBuilder redefines mySystemOfInterest subsets Subsystem1_Configuration::payloadBuilder_gb;
        
        attribute redefines mySystemOfInterest.payloadBuilder.qualityContribution = subsystemPayloadThroughPrebuilderGain + subsystemPayloadThroughFinisherGain;
        
        doc
        /* The payload shall achieve a gain of quality through the payloadBuilder
         * of at least 14 units such that the cost of the subsystem is optimal.
         * This requirement is decomposed into needs for the constituent parts
         * (payloadPrebuilder and payloadFinisher).
         */

        comment subsystem1RequirementDecompositionModelerComment
            about payloadQualityGainPayloadBuilderDerived
            locale "en_US"
        /* Requirement decomposition predicates. */
        require constraint qualityGainThroughput {
            SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadBuilderGain
            <= subsystemPayloadThroughPrebuilderGain + subsystemPayloadThroughFinisherGain
        }
        
        attribute subsystemPayloadThroughPrebuilderGain defined by ScalarValues::Real [1]
            = Subsystem1_StakeholdersAndConcerns::subsystemOptimalCost.payloadPrebuilderTechnologyEnvelopeMax;
        
        comment prebuilderAllocationRationale /*
            DESIGN DECISION: PayloadPrebuilder allocated 4.0 units (28.6% of builder capacity).
            RATIONALE: Initial preparation phase; smaller allocation; validates input and prepares payload structure. A trade-off analysis would be preformed that surfaces this cost vs. allocation.
        */
        
        attribute subsystemPayloadThroughFinisherGain defined by ScalarValues::Real [1]
            = Subsystem1_StakeholdersAndConcerns::subsystemOptimalCost.payloadFinisherTechnologyEnvelopeMin;
        
        comment finisherAllocationRationale /*
            DESIGN DECISION: PayloadFinisher allocated 10.0 units (71.4% of builder capacity).
            RATIONALE: Main assembly and integration phase; larger allocation; combines and integrates subcomponents into final payload. A trade-off analysis would be preformed that surfaces this cost vs. allocation.
        */

        frame Subsystem1_StakeholdersAndConcerns::subsystemOptimalCost {
            redefines prebuiltPayload = payloadBuilder.payloadPrebuilder.prebuildPayload_bb.prebuiltPayload;
            redefines finishedPayload = payloadBuilder.payloadFinisher.finishPayload_bb.finishedPayload;
        }

        comment subsystem1ConstraintInheritanceModelerComment
            about payloadQualityGainPayloadBuilderDerived
            locale "en_US"
        /* Constraint inherited from parent requirement;
         * validated through attribute binding.
         */

        comment payloadQualityGainPayloadPrebuilderNeedModelerComment
            about payloadQualityGainPayloadPrebuilderNeed
            locale "en_US"
        /* Child need handed over to L3 Component1_1. */
        requirement payloadQualityGainPayloadPrebuilderNeed {
            comment recursiveClosureSemantics /*
                NEED VALIDATION: This L2 child need is validated by
                Component1_1_Requirements::payloadQualityGainComponent1_1, which subsets this
                requirement and redefines the subject in the L3 payloadPrebuilder context.

                REQUIREMENT VERIFICATION: The transformed L3 requirement is verified by the
                Component1_1 blackbox configuration and its terminal verification cases.
            */
            doc
            /* The payload shall achieve a gain of quality through the payloadPrebuilder
             * in the context of the payloadBuilder of at least 4 units.
             * This is a derived need that shall be satisfied by the payloadPrebuilder
             * (Component1_1) at level L3.
             */

            subject redefines payloadBuilder;

            require constraint {
                payloadBuilder.payloadPrebuilder.qualityContribution >= subsystemPayloadThroughPrebuilderGain
            }
        }

        comment payloadQualityGainPayloadFinisherNeedModelerComment
            about payloadQualityGainPayloadFinisherNeed
            locale "en_US"
        /* Child need handed over to L3 Component1_2. */
        requirement payloadQualityGainPayloadFinisherNeed {
            comment recursiveClosureSemantics /*
                NEED VALIDATION: This L2 child need is validated by
                Component1_2_Requirements::payloadQualityGainComponent1_2, which subsets this
                requirement and redefines the subject in the L3 payloadFinisher context.

                REQUIREMENT VERIFICATION: The transformed L3 requirement is verified by the
                Component1_2 blackbox configuration and its terminal verification cases.
            */
            doc
            /* The payload shall achieve a gain of quality through the payloadFinisher
             * in the context of the payloadBuilder of at least 10 units.
             * This is a derived need that shall be satisfied by the payloadFinisher
             * (Component1_2) at level L3.
             */

            subject redefines payloadBuilder;

            require constraint {
                payloadBuilder.payloadFinisher.qualityContribution >= subsystemPayloadThroughFinisherGain
            }
        }
    }
}
