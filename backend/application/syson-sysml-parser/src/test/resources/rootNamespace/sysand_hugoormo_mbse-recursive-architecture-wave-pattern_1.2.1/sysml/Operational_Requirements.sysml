package Operational_Requirements {
    
    /*
    PATTERN RULES FOR OPERATIONAL_REQUIREMENTS:
    
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
        This package implements the MBSE RecursiveArchitecture Weave Pattern at L0 (Operational level).
        All requirements MUST follow the rules above. Violations detected by:
        - Syside syntax/semantic validation
        - Constraint evaluation on live behavioral outputs
        - Cross-level consistency verification
    */

    comment payloadQualityGainOperationalModelerComment about payloadQualityGainOperational locale "en_US"
    /* L0 original requirement: defines the capability expected from
     * the L0 blackbox context.
     */
    requirement payloadQualityGainOperational {
        subject operationalContext subsets Operational_Configuration::operationalContext_bb ;
        doc
        /* The payload shall achieve a gain of quality through its generation at the 
         * systemOfInterest and through its final consumption at the interactingSystem 
         * as per the stakeholder concern "operationalQualityGain".
         */
        
        frame Operational_StakeholdersAndConcerns::operationalQualityGain;
    }

    comment payloadQualityGainOperationalDerivedModelerComment
        about payloadQualityGainOperationalDerived
        locale "en_US"
    /* L0 derived requirement: decomposes the original requirement over L0
     * constituents and creates L1 needs.
     */
    requirement payloadQualityGainOperationalDerived subsets payloadQualityGainOperational {
        doc
        /* The payload shall achieve a gain of quality through all the required 
         * parts for the full completion of its life cycle of at least 30 units 
         * assuming that the interactingSystem contributes with at most 10 quality units.
         * This requirement is decomposed into needs for the constituent systems
         * (systemOfInterest and interactingSystem) at the graybox level.
         */

        subject redefines operationalContext
            subsets Operational_Configuration::operationalContext_gb;
        
        comment operationalRequirementDecompositionModelerComment
            about payloadQualityGainOperationalDerived
            locale "en_US"
        /* Requirement decomposition predicates. */
        require constraint qualityGainThroughput { 
            Operational_StakeholdersAndConcerns::operationalQualityGain.operationalQualityGain
            <= operationalPayloadThroughInteractingSystemGain + operationalPayloadThroughSystemOfInterestGain
        }
        comment operationalRequirementAssumptionGateModelerComment
            about payloadQualityGainOperationalDerived
            locale "en_US"
        /* Assumption gate: if false, verification is non-conclusive for this requirement. */
        assume Operational_StakeholdersAndConcerns::operationalMaintenanceStrain;

        attribute operationalPayloadThroughInteractingSystemGain defined by ScalarValues::Real [1]
            = Operational_StakeholdersAndConcerns::operationalMaintenanceStrain.operationalPayloadThroughInteractingSystemGain;
        
        comment externalSystemAllocationRationale /*
            DESIGN DECISION: Interacting system contribution capped at 10.0 units.
            RATIONALE: External system has limited capability; negotiated SLA limits external contribution.
        */
        
        attribute operationalPayloadThroughSystemOfInterestGain defined by ScalarValues::Real [1] = 20.0;
        
        comment internalSystemAllocationRationale /*
            DESIGN DECISION: System of interest contribution set to 20.0 units (2x external system).
            RATIONALE: Internal system expected to bear majority of processing load; operational requirements mandate this ratio.
        */

        comment payloadQualityGainSystemOfInterestNeedModelerComment
            about payloadQualityGainSystemOfInterestNeed
            locale "en_US"
        /* This child requirement is the L1 need transformed into the L1 bb requirement. */
        requirement payloadQualityGainSystemOfInterestNeed {
            comment recursiveClosureSemantics /*
                NEED VALIDATION: This L0 child need is validated by
                SystemOfInterest_Requirements::payloadQualityGainSystemOfInterest, which subsets
                this requirement and redefines the subject in the L1 system context.

                REQUIREMENT VERIFICATION: The transformed L1 requirement is verified by the
                SystemOfInterest configuration and its verification cases.
            */
            doc
            /* The payload shall achieve a gain of quality through the systemOfInterest
             * in the operational context of at least 20 units.
             * This is a derived need that shall be satisfied by the systemOfInterest
             * at level L1.
             */

            subject redefines operationalContext;
                

            require constraint {
               operationalContext.mySystemOfInterest.qualityContribution >= payloadQualityGainOperationalDerived.operationalPayloadThroughSystemOfInterestGain
            }
        }

        comment payloadQualityGainInteractingSystemNeedModelerComment
            about payloadQualityGainInteractingSystemNeed
            locale "en_US"
        /* Terminal branch at L0 for interactingSystem
         * (not recursively decomposed in this pattern).
         */
        requirement payloadQualityGainInteractingSystemNeed {
            comment recursiveClosureSemantics /*
                TERMINAL VALIDATION AND VERIFICATION: This L0 child need is terminal in this
                reference pattern. It remains in the operational context and is verified by the
                interacting system blackbox rather than transformed to a deeper decomposition
                level.
            */
            doc
            /* The payload shall achieve a gain of quality through the interactingSystem
             * in the operational context of at most 10 units.
             * This is a derived need that shall be satisfied by the interactingSystem
             * at the operational level (since it is not further decomposed).
             */

             subject redefines operationalContext;

            require constraint {
                operationalContext.myInteractingSystem.qualityContribution <= payloadQualityGainOperationalDerived.operationalPayloadThroughInteractingSystemGain
            }
        }
    }
}
