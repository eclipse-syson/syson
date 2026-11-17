package Subsystem2_AnalysisCases {
    private import ScalarValues::*;
    private import RealFunctions::*;
    private import MyFramework::MyFrameworkAnalysisMetaPatterns::*;

    comment subsystem2AnalysisStrategyDefsModelerComment about Subsystem2_AnalysisCases locale "en_US"
    /* L2 Subsystem2 analysis strategy defs (terminal leaf). */

    comment subsystem2AnalysisRolePolicyLinkModelerComment about Subsystem2_AnalysisCases locale "en_US"
    /* Analysis role and claim limits are defined in
     * MyFramework::MyFrameworkAnalysisMetaPatterns::analysisRolePolicy.
     */

    attribute def Subsystem2DegradationScenario specializes DegradationScenarioProfile;
    attribute def Subsystem2TradeAlternative specializes TradeAlternativeProfile;

    comment subsystem2AnalysisTerminalBranchModelerComment
        about Subsystem2DegradationAnalysis
        locale "en_US"
    /* Terminal branch note: _bb analysis here is an explicit exception
     * because no _gb decomposition exists.
     */
    analysis def Subsystem2DegradationAnalysis specializes DegradationScenarioAnalysis {
        doc
        /* L2 Subsystem2 terminal analysis strategy contract.
         *
         * External simulation evaluates degradation scenarios and reports impact
         * values that are validated by this model-level objective.
         */

        subject payloadVerifier subsets Subsystem2_Configuration::payloadVerifier_bb;

        in attribute redefines degradationScenarios defined by Subsystem2DegradationScenario [*];
        in attribute verifierCapabilityMargin defined by ScalarValues::Real [1] default 3.0;
        in attribute redefines nominalContribution defined by ScalarValues::Real
            default SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadVerifierGain
                + verifierCapabilityMargin;
        in attribute redefines degradedContribution defined by ScalarValues::Real
            default nominalContribution * degradationFactor;
        in attribute redefines degradationFactor defined by ScalarValues::Real default 0.80;

        comment subsystem2AnalysisEvidenceContractModelerComment
            about Subsystem2DegradationAnalysis
            locale "en_US"
        /* Shared evidence contract for this analysis type. */
        attribute evidenceTolerance defined by ScalarValues::Real [1] = 0.05;
        in attribute lastAnalysisRunId defined by ScalarValues::Integer [1];
        in attribute lastExecutionEpoch defined by ScalarValues::Integer [1];
        in attribute reportedAdjustedContribution defined by ScalarValues::Real [1];
        in attribute reportedDegradationImpact defined by ScalarValues::Real [1];

        objective subsystem2DegradationObjective redefines degradationScenarioObjective {
            doc
            /* Analyze degradation scenarios for the payload verifier
             * terminal subsystem under nominal and adverse conditions.
             */

            assume constraint redefines degradationInputsAssumption;
            assume constraint {
                payloadVerifier.qualityContribution >= 0.0
            }
            require constraint redefines nonNegativeDegradationRequirement;
            require constraint redefines degradationConsistencyRequirement;
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedDegradationImpact - degradationImpact) <= evidenceTolerance
            }
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedAdjustedContribution - degradedContribution) <= evidenceTolerance
            }
        }

        return redefines degradationImpact defined by ScalarValues::Real [1];
    }

    analysis def Subsystem2TradeStudyAnalysis specializes TradeStudyAnalysis {
        subject payloadVerifier subsets Subsystem2_Configuration::payloadVerifier_bb;

        in attribute redefines alternatives defined by Subsystem2TradeAlternative [*];
        in attribute redefines weightPerformance defined by ScalarValues::Real default 0.5;
        in attribute redefines weightCost defined by ScalarValues::Real default 0.2;
        in attribute redefines weightResilience defined by ScalarValues::Real default 0.2;
        in attribute redefines weightRisk defined by ScalarValues::Real default 0.1;

        attribute evidenceTolerance defined by ScalarValues::Real [1] = 0.05;
        in attribute lastAnalysisRunId defined by ScalarValues::Integer [1];
        in attribute lastExecutionEpoch defined by ScalarValues::Integer [1];
        in attribute reportedSelectedAlternativeScore defined by ScalarValues::Real [1];

        objective subsystem2TradeStudyObjective redefines tradeStudyObjective {
            assume constraint redefines nonNegativeWeightsAssumption;
            require constraint redefines nonNegativeSelectedScoreRequirement;
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedSelectedAlternativeScore - selectedAlternativeScore) <= evidenceTolerance
            }
        }

        return redefines selectedAlternativeScore defined by ScalarValues::Real [1];
    }

    comment subsystem2AnalysisScenarioUsagesModelerComment
        about payloadQualityGainPayloadVerifierDegradationAnalysis
        locale "en_US"
    /* L2 Subsystem2 analysis scenario usages. */

    comment subsystem2AnalysisScenarioAdviceModelerComment
        about payloadQualityGainPayloadVerifierDegradationAnalysis
        locale "en_US"
    /* Practitioner advice: if a future _gb is introduced, migrate
     * analysis subjects/usages to that _gb.
     */
    analysis payloadQualityGainPayloadVerifierDegradationAnalysis defined by Subsystem2DegradationAnalysis {
        doc
        /* Scenario contract:
         * 1) Define nominal/degraded verifier scenarios.
         * 2) Execute external simulations.
         * 3) Feed simulatedAdjustedContribution and degradationImpact back.
         * 4) Validate objective constraints in-model.
         */

        subject redefines payloadVerifier;

        in attribute redefines degradationScenarios defined by Subsystem2DegradationScenario [*];
        in attribute nominalDegradationScenario subsets degradationScenarios defined by Subsystem2DegradationScenario [1] {
            comment scenarioAssumption /*
                Intentional terminal capability scenario: nominal verifier capability is
                modeled as the L1 verifier allocation plus a 3.0-unit local capability
                margin. This is not a second source for the requirement budget.
            */
            attribute redefines nominalContribution =
                SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadVerifierGain
                    + 3.0;
            attribute redefines degradationFactor = 0.80;
            attribute redefines degradedContribution = nominalContribution * degradationFactor;
            attribute redefines maintenanceStrain = 0.0;
        }
        
        comment subsystem2AnalysisUsageEvidenceModelerComment
            about payloadQualityGainPayloadVerifierDegradationAnalysis
            locale "en_US"
        /* Usage-specific execution evidence (per analysis run instance). */
        in attribute redefines lastAnalysisRunId = 0;
        in attribute redefines lastExecutionEpoch = 0;
        in attribute redefines reportedAdjustedContribution = 0.0;
        in attribute redefines reportedDegradationImpact = 0.0;

    }

    analysis payloadQualityGainPayloadVerifierTradeStudyAnalysis defined by Subsystem2TradeStudyAnalysis {
        doc
        /* Scenario contract:
         * 1) Define candidate verifier alternatives.
         * 2) Execute trade-off scoring workflow.
         * 3) Feed selectedAlternativeScore back.
         * 4) Validate objective constraints in-model.
         */

        subject redefines payloadVerifier;

        in attribute redefines alternatives defined by Subsystem2TradeAlternative [*];
        in attribute baselineTradeAlternative subsets alternatives defined by Subsystem2TradeAlternative [1] {
            comment scenarioAssumption /*
                Baseline trade alternative uses the same terminal capability margin as the
                degradation scenario, so the requirement allocation remains the source of
                truth and the extra 3.0 units stay visible as a scenario assumption.
            */
            attribute redefines expectedQualityGain =
                SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadVerifierGain
                    + 3.0;
            attribute redefines expectedCost = 3.0;
            attribute redefines expectedMaintenanceStrain = 1.0;
            attribute redefines riskPenalty = 1.0;
        }

        in attribute redefines lastAnalysisRunId = 0;
        in attribute redefines lastExecutionEpoch = 0;
        in attribute redefines reportedSelectedAlternativeScore = 0.0;

    }
}
