package SystemOfInterest_VerificationCases {
    private import VerificationCases::*;
    private import ScalarValues::*;
    private import MyFramework::MyFrameworkVerificationMetaPatterns::*;

    comment systemOfInterestVerificationStrategyDefsModelerComment about SystemOfInterest_VerificationCases locale "en_US"
    /* L1 verification strategy defs (inherit from Framework meta-patterns). */

    comment systemOfInterestVerificationRolePolicyLinkModelerComment about SystemOfInterest_VerificationCases locale "en_US"
    /* Verification evidence intent and claim limits are defined in
     * MyFramework::MyFrameworkVerificationMetaPatterns.
     * See also MyFramework::MyFrameworkAnalysisMetaPatterns::analysisRolePolicy
     * for the analysis-vs-empirical claim boundary.
     */

    verification def SystemOfInterestEvidenceVerification specializes IntegratedChainEvidenceVerification {
        doc
        /* L1 SYSTEMOFINTEREST EVIDENCE VERIFICATION STRATEGY
         * 
         * Specializes the 8-stage evidence pipeline for subsystem context.
         * Subject: systemOfInterest (L1 whitebox integration)
         * Objective: Verify payload quality gain through subsystem composition
         * Stimulus: Execute full interacting chain in wb context (allocated behavior)
         * Method: analyze for this executable model reference. Implemented systems should
         * repeat this intent through test or demonstration evidence where applicable.
         * HINT: Scope is verified through configuration wiring: connector/interface bindings
         * in SystemOfInterest_Configuration route the payload and feedback path, so a passing
         * end-to-end run confirms integrated interaction across builder and verifier.
         * 
         * This def captures the L1-specific verification strategy once.
         * Scenario usages inherit from this def for different subsystem configurations.
         */

        subject systemOfInterest subsets SystemOfInterest_Configuration::systemOfInterest_wb;

        objective systemOfInterestObjective {
            doc
            /* Verify payloadQualityGainSystemOfInterestDerived is satisfied.
             * - Requirement: payload quality gain through L1 subsystem composition, as specified
             *   by SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.
             * - Scenario: L1 whitebox context with payloadBuilder and payloadVerifier.
             * - Evidence strategy: Execute subsystem actions, collect quality contributions.
             * - Verdict note: inherited verdict computation is a scaffold; concrete projects
             *   shall specialize evidence collection and acceptance logic.
             */
            verify SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived;
            metadata VerificationMethod { kind = VerificationMethodKind::analyze; }
        }
        
        action redefines applyStimulus {
            doc
            /* L1-specific: execute the connected constituent actions that own executable
             * behavior. The whitebox action remains a traceability shell when behavior is
             * delegated to the builder and verifier endpoints.
             */
            in item preparedContext;
            out item stimulusExecution;
            perform SystemOfInterest_Configuration::systemOfInterest_wb.payloadBuilder.buildPayload_gb;
            then perform SystemOfInterest_Configuration::systemOfInterest_wb.payloadVerifier.verifyPayload_bb;
        }

        comment systemOfInterestVerificationInheritanceModelerComment
            about SystemOfInterestEvidenceVerification
            locale "en_US"
        /* All other 8 actions inherited from meta-pattern. */
    }

    comment systemOfInterestVerificationScenarioUsagesModelerComment
        about payloadQualityGainSystemOfInterestVerification
        locale "en_US"
    /* L1 verification scenario usages (inherit from L1 defs). */

    verification payloadQualityGainSystemOfInterestVerification defined by SystemOfInterestEvidenceVerification {
        doc
        /* SCENARIO 1: Nominal SystemOfInterest subsystem composition.
         * 
         * Inherits L1 evidence strategy; subject binding and prepareContext
         * can be specialized for this scenario.
         */
    }

    verification systemOfInterestCompliancetoMyStandardVerification defined by ManualComplianceInspectionVerification {
        doc
        /* SCENARIO: SystemOfInterest subsystem compliance.
         * 
         * One-off manual inspection verification at L1.
         * Verify that subsystem architecture complies with MyStandard.
         */

        subject systemOfInterest subsets SystemOfInterest_Configuration::systemOfInterest_bb;

        objective systemOfInterestInspectionObjective {
            doc
            /* INSPECTION CRITERIA FOR L1 SUBSYSTEMS:
             * 1) Verify subsystem types inherit from MyStandard roots.
             * 2) Confirm subsystem ports/connections are standard-compliant.
             * 3) Validate satisfy relations link subsystem architecture to L1 requirements.
             * 4) Check subsystem allocations are traceable to L2 components.
             * 5) Ensure boundary specifications conform to standard ontology.
             */
        }
    }
    
    comment systemOfInterestVerificationScenarioExtensionModelerComment
        about payloadQualityGainSystemOfInterestVerification
        locale "en_US"
    /* Additional scenario usages can be added,
     * for example degraded operational mode.
     */
}
