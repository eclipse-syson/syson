package SystemOfInterest_Requirements {
    
    /*
    PATTERN RULES FOR SYSTEMOFINTEREST_REQUIREMENTS:
    
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
        This package implements the MBSE RecursiveArchitecture Weave Pattern at L1 (System of Interest level).
        All requirements MUST follow the rules above. Violations detected by:
        - Syside syntax/semantic validation
        - Constraint evaluation on live behavioral outputs
        - Cross-level consistency verification
    */
    
    comment payloadQualityGainSystemOfInterestModelerComment
        about payloadQualityGainSystemOfInterest
        locale "en_US"
    /* L1 original requirement: transforms the L0 derived need into an
     * L1 blackbox requirement.
     */
    requirement payloadQualityGainSystemOfInterest
        subsets
            Operational_Requirements::payloadQualityGainOperationalDerived
                .payloadQualityGainSystemOfInterestNeed {
        comment transformedRequirementClosureSemantics /*
            NEED VALIDATION: By subsetting the L0 child need, this L1 requirement validates
            the transformation of the operational need into the system-of-interest context.

            REQUIREMENT VERIFICATION: This transformed L1 requirement is verified by
            SystemOfInterest_Configuration::systemOfInterest_bb and the associated evidence
            defined at the L1 level.
        */
        subject mySystemOfInterest redefines operationalContext
            subsets SystemOfInterest_Configuration::systemOfInterest_bb;

        doc
        /* The payload shall achieve a gain of quality through the systemOfInterest
         * of at least 20 units. (Here we decontextualize the requirement from L0)
         * This requirement is derived from the subsetted operational derived requirement.
         * With this we ensure that the system of interest will perform as needed in the 
         * operational context by inheriting the constraint from the operational requirements 
         * and redefining the subject. However, the System of Interest could impose 
         * stringent requirements that could make it suitable for different operational contexts.
         */
        comment payloadQualityGainSystemOfInterestConstraintModelerComment
            about payloadQualityGainSystemOfInterest
            locale "en_US"
        /* Constraint inherited from parent requirement through subsetting. */
    } 

    comment payloadQualityGainSystemOfInterestDerivedModelerComment
        about payloadQualityGainSystemOfInterestDerived
        locale "en_US"
    /* L1 derived requirement: allocates the L1 requirement to L2 constituents. */
    requirement payloadQualityGainSystemOfInterestDerived
        subsets Operational_Requirements::payloadQualityGainOperationalDerived.payloadQualityGainSystemOfInterestNeed {
        subject  mySystemOfInterest redefines operationalContext
            subsets SystemOfInterest_Configuration::systemOfInterest_gb;

        doc
        /* The payload shall achieve a gain of quality through the systemOfInterest
         * of at least 20 units.
         * The systemOfInterest shall operate at an optimum effort by ensuring that
         * the payloadBuilder outputs twice as much gain as the payloadVerifier.
         * This requirement is decomposed into needs for the constituent parts
         * (payloadBuilder and payloadVerifier).
         */
         
        comment systemOfInterestRequirementDecompositionModelerComment
            about payloadQualityGainSystemOfInterestDerived
            locale "en_US"
        /* Requirement decomposition predicates. */
        require constraint qualityGainThroughput {
            Operational_Requirements::payloadQualityGainOperationalDerived.operationalPayloadThroughSystemOfInterestGain 
            <= systemPayloadThroughPayloadBuilderGain + systemPayloadThroughPayloadVerifierGain
        }
        require constraint qualityGainOptimumEffortBalance {
            systemPayloadThroughPayloadBuilderGain >=
                SystemOfInterest_StakeholdersAndConcerns::systemOptimalQualityGainEffort.payloadBuilderToVerifierMinRatio
                * systemPayloadThroughPayloadVerifierGain
        }

        attribute systemPayloadThroughPayloadVerifierGain defined by ScalarValues::Real [1]
            = SystemOfInterest_StakeholdersAndConcerns::systemOptimalQualityGainEffort.payloadVerifierTechnologyEnvelopeMax;

        attribute systemPayloadThroughPayloadBuilderGain defined by ScalarValues::Real [1]
            = SystemOfInterest_StakeholdersAndConcerns::systemOptimalQualityGainEffort.payloadBuilderToVerifierMinRatio
                * systemPayloadThroughPayloadVerifierGain;
        
        comment builderAllocationRationale /*
            DESIGN DECISION: PayloadBuilder allocated 14.0 units (70% of system of interest capacity).
            RATIONALE: Primary processing subsystem; carries main payload transformation responsibility.
        */
        
        comment verifierAllocationRationale /*
            DESIGN DECISION: PayloadVerifier allocated 7.0 units (30% of system of interest capacity).
            RATIONALE: Quality assurance subsystem; secondary but mandatory; 2:1 builder:verifier ratio ensures payload integrity.
        */

        comment systemOfInterestFramedConcernModelerComment
            about payloadQualityGainSystemOfInterestDerived
            locale "en_US"
        /* Framed concern is a required subrequirement once assumptions hold. */
        frame SystemOfInterest_StakeholdersAndConcerns::systemOptimalQualityGainEffort {
            redefines builtPayload = mySystemOfInterest.producePayload_gb.buildPayload.builtPayload;
            redefines verifiedPayload = mySystemOfInterest.producePayload_gb.verifyPayload.verifiedPayload;
        }

        comment systemOfInterestConstraintInheritanceModelerComment
            about payloadQualityGainSystemOfInterestDerived
            locale "en_US"
        /* Constraint inherited from parent requirement;
         * validated through attribute binding.
         */

        comment payloadQualityGainPayloadBuilderNeedModelerComment
            about payloadQualityGainPayloadBuilderNeed
            locale "en_US"
        /* Child need handed over to L2 Subsystem1. */
        requirement payloadQualityGainPayloadBuilderNeed {
            comment recursiveClosureSemantics /*
                NEED VALIDATION: This L1 child need is validated by
                Subsystem1_Requirements::payloadQualityGainPayloadBuilder, which subsets this
                requirement and redefines the subject in the L2 payloadBuilder context.

                REQUIREMENT VERIFICATION: The transformed L2 requirement is verified by the
                Subsystem1 configuration and its verification cases.
            */
            doc
            /* The payload shall achieve a gain of quality through the payloadBuilder
             * in the context of the system of interest of at least 14 units.
             * This is a derived need that shall be satisfied by the payloadBuilder
             * (Subsystem1) at level L2.
             */

            subject redefines mySystemOfInterest;

            require constraint {
                mySystemOfInterest.payloadBuilder.qualityContribution >= systemPayloadThroughPayloadBuilderGain
            }
        }

        comment payloadQualityGainPayloadVerifierNeedModelerComment
            about payloadQualityGainPayloadVerifierNeed
            locale "en_US"
        /* Child need handed over to L2 Subsystem2. */
        requirement payloadQualityGainPayloadVerifierNeed {
            comment recursiveClosureSemantics /*
                NEED VALIDATION: This L1 child need is validated by
                Subsystem2_Requirements::payloadQualityGainPayloadVerifier, which subsets this
                requirement and redefines the subject in the L2 payloadVerifier context.

                REQUIREMENT VERIFICATION: The transformed L2 requirement is verified by the
                Subsystem2 blackbox configuration and its terminal verification cases.
            */
            doc
            /* The payload shall achieve a gain of quality through the payloadVerifier
             * in the context of the system of interest of at least 7 units.
             * This is a derived need that shall be satisfied by the payloadVerifier
             * (Subsystem2) at level L2.
             */

            subject redefines mySystemOfInterest;

            require constraint {
                mySystemOfInterest.payloadVerifier.qualityContribution >= systemPayloadThroughPayloadVerifierGain
            }
        }
    }
}
