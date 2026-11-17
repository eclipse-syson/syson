package Operational_VerificationCases {
    private import VerificationCases::*;
    private import ScalarValues::*;
    private import MyFramework::MyFrameworkVerificationMetaPatterns::*;

    comment operationalVerificationStrategyDefsModelerComment about Operational_VerificationCases locale "en_US"
    /* L0 verification strategy defs (inherit from Framework meta-patterns). */

    comment operationalVerificationRolePolicyLinkModelerComment about Operational_VerificationCases locale "en_US"
    /* Verification evidence intent and claim limits are defined in
     * MyFramework::MyFrameworkVerificationMetaPatterns.
     * See also MyFramework::MyFrameworkAnalysisMetaPatterns::analysisRolePolicy
     * for the analysis-vs-empirical claim boundary.
     */
    
    verification def OperationalEvidenceVerification specializes IntegratedChainEvidenceVerification {
        doc
        /* L0 OPERATIONAL EVIDENCE VERIFICATION STRATEGY
         * 
         * Specializes the 8-stage evidence pipeline for operational context.
         * Subject: operationalContext (L0 whitebox integration)
         * Objective: Verify payload quality gain through system interaction
         * Stimulus: Execute full interacting chain in wb context (allocated behavior)
         * Method: analyze, because this reference pattern verifies executable model behavior.
         * A realized operational system would require corresponding test or demonstration
         * evidence in the real operational context.
         * HINT: Scope is verified through configuration wiring: action inputs/outputs are
         * bound via connectors/interfaces/ports in Operational_Configuration, so end-to-end
         * success validates the full interaction path rather than isolated action fragments.
         * 
         * This def captures the L0-specific verification strategy once.
         * Scenario usages inherit from this def and bind prepareContext parameters
         * for different operational conditions (nominal, high-load, contingency, etc.).
         */

        subject operationalContext subsets Operational_Configuration::operationalContext_wb;

        objective operationalObjective {
            doc
            /* Verify payloadQualityGainOperationalDerived is satisfied.
             * - Requirement: payload quality gain through the L0 decomposition, as specified
             *   by Operational_Requirements::payloadQualityGainOperationalDerived.
             * - Scenario: L0 operational whitebox context (bound by prepareContext).
             * - Evidence strategy: Execute exchangePayload action, collect quality values.
             * - Verdict note: inherited verdict computation is a scaffold; concrete projects
             *   shall specialize evidence collection and acceptance logic.
             */
            verify Operational_Requirements::payloadQualityGainOperationalDerived;
            metadata VerificationMethod { kind = VerificationMethodKind::analyze; }
        }
        
        action redefines applyStimulus {
            doc
            /* L0-specific: execute the lowest integrated starter and endpoint actions that own
             * executable behavior. The whitebox context action is retained for traceability;
             * executable behavior is delegated to connected constituent actions.
             */
            in item preparedContext;
            out item stimulusExecution;
            perform Operational_Configuration::operationalContext_wb.mySystemOfInterest.payloadBuilder.buildPayload_gb;
            perform Operational_Configuration::operationalContext_wb.mySystemOfInterest.payloadVerifier.verifyPayload_bb;
            then perform Operational_Configuration::operationalContext_wb.myInteractingSystem.consumePayload_bb;
        }

        comment operationalEvidenceVerificationInheritanceModelerComment
            about OperationalEvidenceVerification
            locale "en_US"
        /* All other 8 actions inherited from meta-pattern;
         * can be specialized if needed.
         */
    }

    comment operationalVerificationScenarioUsagesModelerComment
        about payloadQualityGainOperationalVerification
        locale "en_US"
    /* L0 verification scenario usages (inherit from L0 defs). */
    
    verification payloadQualityGainOperationalVerification defined by OperationalEvidenceVerification {
        doc
        /* SCENARIO 1: Nominal Operational Context
         * 
         * Standard payload exchange under nominal system conditions.
         * Inherits L0 evidence strategy; subject binding and prepareContext
         * can be specialized for this scenario without duplicating the pipeline.
         */
    }

    verification operationalSystemsCompliancetoMyStandardVerification defined by ManualComplianceInspectionVerification {
        doc
        /* SCENARIO: Operational Systems Compliance
         * 
         * One-off manual inspection verification at L0.
         * Verify that systems in operational context comply with MyStandard.
         */

        subject operationalContext subsets Operational_Configuration::operationalContext_bb;

        objective operationalInspectionObjective {
            doc
            /* INSPECTION CRITERIA (5-point checklist):
             * 1) Verify subsetting chain traces to MyStandard roots
             * 2) Confirm relevant parts subset MyStandard::myStandardComplyingPart where compliance is claimed
             * 3) Validate satisfy relations link bb/gb to requirements
             * 4) Check boundary parts reference standard-compliant ontology
             * 5) Ensure compliance annotations present and traceable
             */
        }
    }
    
    comment operationalVerificationScenarioExtensionModelerComment
        about payloadQualityGainOperationalVerification
        locale "en_US"
    /* Additional scenario usages can be added by inheriting from the defs,
     * for example high-load and contingency operational contexts.
     */
}

