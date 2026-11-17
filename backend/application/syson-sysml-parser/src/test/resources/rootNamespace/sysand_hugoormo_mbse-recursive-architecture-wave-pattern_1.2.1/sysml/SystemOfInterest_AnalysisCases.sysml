package SystemOfInterest_AnalysisCases {
    private import ScalarValues::*;
    private import RealFunctions::*;
    private import MyFramework::MyFrameworkAnalysisMetaPatterns::*;

    comment systemOfInterestAnalysisStrategyDefsModelerComment about SystemOfInterest_AnalysisCases locale "en_US"
    /* L1 analysis strategy defs (inherit from Framework meta-patterns). */

    comment systemOfInterestAnalysisRolePolicyLinkModelerComment about SystemOfInterest_AnalysisCases locale "en_US"
    /* Analysis role and claim limits are defined in
     * MyFramework::MyFrameworkAnalysisMetaPatterns::analysisRolePolicy.
     */

    attribute def SystemOfInterestContributionAllocationScenario specializes ContributionAllocationScenario {
        attribute payloadBuilderContribution redefines primaryContribution;
        attribute payloadVerifierContribution redefines secondaryContribution;
    }

    attribute def SystemOfInterestDegradationScenario specializes DegradationScenarioProfile;
    attribute def SystemOfInterestTradeAlternative specializes TradeAlternativeProfile;

    comment systemOfInterestAnalysisAdviceModelerComment
        about SystemOfInterestStrainSensitivityAnalysis
        locale "en_US"
    /* Practitioner advice: prioritize emergent behavior analysis at _gb
     * before committing interface detail.
     */
    analysis def SystemOfInterestStrainSensitivityAnalysis specializes ParametricSensitivityAnalysis {
        doc
        /* L1 analysis strategy contract for subsystem composition.
         * The analysis sets the basis for design decisions. For instance in the derivation of requirements.
         * External simulation computes scenario outcomes; model constraints define
         * acceptance and integration of returned analysis values.
         */

        subject systemOfInterest subsets SystemOfInterest_Configuration::systemOfInterest_gb;

        in attribute redefines allocationScenarios defined by SystemOfInterestContributionAllocationScenario [*];
        in attribute redefines baselineContribution defined by ScalarValues::Real
            default SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadBuilderGain
                + SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadVerifierGain;
        in attribute redefines sensitivityWindow defined by ScalarValues::Real default 0.5;

        comment systemOfInterestAnalysisEvidenceContractModelerComment
            about SystemOfInterestStrainSensitivityAnalysis
            locale "en_US"
        /* Shared evidence contract for this analysis type. */
        attribute evidenceTolerance defined by ScalarValues::Real [1] = 0.05;
        in attribute lastAnalysisRunId defined by ScalarValues::Integer [1];
        in attribute lastExecutionEpoch defined by ScalarValues::Integer [1];
        in attribute reportedAdjustedContribution defined by ScalarValues::Real [1];
        in attribute reportedSensitivityResult defined by ScalarValues::Real [1];

        objective systemOfInterestSensitivityObjective redefines parametricSensitivityObjective {
            doc
            /* Analyze sensitivity of quality contribution
             * with respect to builder/verifier strain variations at the system of interest level.
             */

            assume constraint redefines baselineAssumption;
            assume constraint {
                systemOfInterest.qualityContributionAdjusted >= 0.0
            }
            require constraint redefines nonNegativeSensitivityRequirement;
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedSensitivityResult - sensitivityResult) <= evidenceTolerance
            }
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedAdjustedContribution - systemOfInterest.qualityContributionAdjusted) <= evidenceTolerance
            }
        }

        return redefines sensitivityResult defined by ScalarValues::Real [1];
    }

    analysis def SystemOfInterestDegradationAnalysis specializes DegradationScenarioAnalysis {
        subject systemOfInterest subsets SystemOfInterest_Configuration::systemOfInterest_gb;

        in attribute redefines degradationScenarios defined by SystemOfInterestDegradationScenario [*];
        in attribute redefines nominalContribution defined by ScalarValues::Real
            default SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadBuilderGain
                + SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadVerifierGain;
        in attribute redefines degradedContribution defined by ScalarValues::Real
            default nominalContribution * degradationFactor;
        in attribute redefines degradationFactor defined by ScalarValues::Real default 0.995;

        attribute evidenceTolerance defined by ScalarValues::Real [1] = 0.05;
        in attribute lastAnalysisRunId defined by ScalarValues::Integer [1];
        in attribute lastExecutionEpoch defined by ScalarValues::Integer [1];
        in attribute reportedAdjustedContribution defined by ScalarValues::Real [1];
        in attribute reportedDegradationImpact defined by ScalarValues::Real [1];

        objective systemOfInterestDegradationObjective redefines degradationScenarioObjective {
            assume constraint redefines degradationInputsAssumption;
            assume constraint {
                systemOfInterest.qualityContributionAdjusted >= 0.0
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

    analysis def SystemOfInterestTradeStudyAnalysis specializes TradeStudyAnalysis {
        subject systemOfInterest subsets SystemOfInterest_Configuration::systemOfInterest_gb;

        in attribute redefines alternatives defined by SystemOfInterestTradeAlternative [*];
        in attribute redefines weightPerformance defined by ScalarValues::Real default 0.5;
        in attribute redefines weightCost defined by ScalarValues::Real default 0.2;
        in attribute redefines weightResilience defined by ScalarValues::Real default 0.2;
        in attribute redefines weightRisk defined by ScalarValues::Real default 0.1;

        attribute evidenceTolerance defined by ScalarValues::Real [1] = 0.05;
        in attribute lastAnalysisRunId defined by ScalarValues::Integer [1];
        in attribute lastExecutionEpoch defined by ScalarValues::Integer [1];
        in attribute reportedSelectedAlternativeScore defined by ScalarValues::Real [1];

        objective systemOfInterestTradeStudyObjective redefines tradeStudyObjective {
            assume constraint redefines nonNegativeWeightsAssumption;
            require constraint redefines nonNegativeSelectedScoreRequirement;
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedSelectedAlternativeScore - selectedAlternativeScore) <= evidenceTolerance
            }
        }

        return redefines selectedAlternativeScore defined by ScalarValues::Real [1] ;
    }

    comment systemOfInterestAnalysisExecutionContextModelerComment about systemOfInterestAnalysisContext locale "en_US"
    /* L1 analysis execution context. */

    comment systemOfInterestAnalysisExecutionAdviceModelerComment
        about systemOfInterestAnalysisContext
        locale "en_US"
    /* Keep this context aligned with _gb subject scope to preserve
     * recursive constituent semantics.
     */
    part systemOfInterestAnalysisContext {
        part systemOfInterestUnderAnalysis subsets SystemOfInterest_Configuration::systemOfInterest_gb;

        analysis payloadQualityGainSystemOfInterestSensitivityAnalysis defined by SystemOfInterestStrainSensitivityAnalysis {
            doc
            /* Scenario contract:
             * 1) Vary L1 builder/verifier contribution and maintenance strain inputs.
             * 2) Execute external simulation workflow.
             * 3) Feed simulatedAdjustedContribution and sensitivityResult back.
             * 4) Close analysis through objective constraints in-model.
             */
            subject redefines systemOfInterest subsets systemOfInterestUnderAnalysis;

            in attribute redefines allocationScenarios defined by SystemOfInterestContributionAllocationScenario [*];

            in attribute nominalScenario subsets allocationScenarios defined by SystemOfInterestContributionAllocationScenario [1] {
                attribute redefines payloadBuilderContribution =
                    SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadBuilderGain;
                attribute redefines payloadVerifierContribution =
                    SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadVerifierGain;
                attribute redefines externalContribution = 0.0;
                attribute redefines maintenanceStrain = 0.0;
                attribute redefines demandFactor = 1.0;
            }

            in attribute verifierHotspotScenario subsets allocationScenarios defined by SystemOfInterestContributionAllocationScenario [1] {
                comment scenarioPerturbation /*
                    Intentional stress scenario: verifier contribution is reduced by 0.1
                    quality units from the L1 verifier allocation while the builder remains
                    at its allocated budget.
                */
                attribute redefines payloadBuilderContribution =
                    SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadBuilderGain;
                attribute redefines payloadVerifierContribution =
                    SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadVerifierGain - 0.1;
                attribute redefines externalContribution = 0.0;
                attribute redefines maintenanceStrain = 1.0;
                attribute redefines demandFactor = 1.0;
            }

            comment systemOfInterestAnalysisUsageEvidenceModelerComment
                about payloadQualityGainSystemOfInterestSensitivityAnalysis
                locale "en_US"
            /* Usage-specific execution evidence (per analysis run instance). */
            in attribute redefines lastAnalysisRunId = 1779875123;
            in attribute redefines lastExecutionEpoch = 1779875123;
            in attribute redefines reportedAdjustedContribution = 20.97;
            in attribute redefines reportedSensitivityResult = 0.001429;
        }

        analysis payloadQualityGainSystemOfInterestSeededExemplarAnalysis defined by SystemOfInterestStrainSensitivityAnalysis {
            doc
            /* Seeded exemplar scenario with literal values to make contribution inputs explicit.
             * This usage is demonstration-only and keeps run-id at 0 to avoid claiming
             * validated simulation evidence.
             */
            comment seededClosureTrace
            /* Closure trace: 14.0 + 7.0 = 21.0 (systemOfInterest seeded contribution). */
            subject redefines systemOfInterest subsets systemOfInterestUnderAnalysis;

            in attribute redefines allocationScenarios defined by SystemOfInterestContributionAllocationScenario [*];

            in attribute recursiveClosureSeedScenario subsets allocationScenarios defined by SystemOfInterestContributionAllocationScenario [1] {
                attribute redefines payloadBuilderContribution = 14.0;
                attribute redefines payloadVerifierContribution = 7.0;
                attribute redefines externalContribution = 0.0;
                attribute redefines maintenanceStrain = 0.0;
                attribute redefines demandFactor = 1.0;
            }

            in attribute redefines lastAnalysisRunId = 0;
            in attribute redefines lastExecutionEpoch = 0;
            in attribute redefines reportedAdjustedContribution = 0.0;
            in attribute redefines reportedSensitivityResult = 0.0;
        }

        analysis degradationAnalysis defined by SystemOfInterestDegradationAnalysis {
            subject redefines systemOfInterest subsets systemOfInterestUnderAnalysis;
            in attribute redefines degradationScenarios defined by SystemOfInterestDegradationScenario [*];
            in attribute nominalDegradationScenario subsets degradationScenarios defined by SystemOfInterestDegradationScenario [1] {
                attribute redefines nominalContribution =
                    SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadBuilderGain
                        + SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadVerifierGain;
                attribute redefines degradationFactor = 0.995;
                attribute redefines degradedContribution = nominalContribution * degradationFactor;
                attribute redefines maintenanceStrain = 0.0;
            }
            in attribute redefines lastAnalysisRunId = 0;
            in attribute redefines lastExecutionEpoch = 0;
            in attribute redefines reportedAdjustedContribution = 0.0;
            in attribute redefines reportedDegradationImpact = 0.0;
        }

        analysis tradeStudyAnalysis defined by SystemOfInterestTradeStudyAnalysis {
            subject redefines systemOfInterest subsets systemOfInterestUnderAnalysis;
            in attribute redefines alternatives defined by SystemOfInterestTradeAlternative [*];
            in attribute baselineTradeAlternative subsets alternatives defined by SystemOfInterestTradeAlternative [1] {
                attribute redefines expectedQualityGain =
                    SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadBuilderGain
                        + SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadVerifierGain;
                attribute redefines expectedCost = 5.5;
                attribute redefines expectedMaintenanceStrain = 1.0;
                attribute redefines riskPenalty = 1.0;
            }
            in attribute redefines lastAnalysisRunId = 0;
            in attribute redefines lastExecutionEpoch = 0;
            in attribute redefines reportedSelectedAlternativeScore = 0.0;
        }
    }
}
