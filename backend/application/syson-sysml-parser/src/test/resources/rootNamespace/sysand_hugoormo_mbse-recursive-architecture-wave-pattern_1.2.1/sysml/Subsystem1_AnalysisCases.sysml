package Subsystem1_AnalysisCases {
    private import ScalarValues::*;
    private import RealFunctions::*;
    private import MyFramework::MyFrameworkAnalysisMetaPatterns::*;

    comment subsystem1AnalysisStrategyDefsModelerComment about Subsystem1_AnalysisCases locale "en_US"
    /* L2 Subsystem1 analysis strategy defs (inherit from Framework). */

    comment subsystem1AnalysisRolePolicyLinkModelerComment about Subsystem1_AnalysisCases locale "en_US"
    /* Analysis role and claim limits are defined in
     * MyFramework::MyFrameworkAnalysisMetaPatterns::analysisRolePolicy.
     */

    attribute def Subsystem1ContributionAllocationScenario specializes ContributionAllocationScenario {
        attribute payloadPrebuilderContribution redefines primaryContribution;
        attribute payloadFinisherContribution redefines secondaryContribution;
    }

    attribute def Subsystem1DegradationScenario specializes DegradationScenarioProfile;
    attribute def Subsystem1TradeAlternative specializes TradeAlternativeProfile;

    comment subsystem1AnalysisAdviceModelerComment
        about Subsystem1StrainSensitivityAnalysis
        locale "en_US"
    /* Practitioner advice: use _gb subjects for analysis when constituent
     * coupling is part of the decision.
     */
    analysis def Subsystem1StrainSensitivityAnalysis specializes ParametricSensitivityAnalysis {
        doc
        /* L2 Subsystem1 analysis strategy contract for component composition.
         * The analysis sets the basis for design decisions. For instance in the derivation of requirements.
         * The model defines what must be analyzed; external simulation tooling
         * executes the scenario matrix and returns result values to this case.
         */

        subject payloadBuilder subsets Subsystem1_Configuration::payloadBuilder_gb;

        in attribute redefines allocationScenarios defined by Subsystem1ContributionAllocationScenario [*];
        in attribute redefines baselineContribution defined by ScalarValues::Real
            default Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughPrebuilderGain
                + Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughFinisherGain;
        in attribute redefines sensitivityWindow defined by ScalarValues::Real default 0.5;

        comment subsystem1AnalysisEvidenceContractModelerComment
            about Subsystem1StrainSensitivityAnalysis
            locale "en_US"
        /* Shared evidence contract for this analysis type. */
        attribute evidenceTolerance defined by ScalarValues::Real [1] = 0.05;
        in attribute lastAnalysisRunId defined by ScalarValues::Integer [1];
        in attribute lastExecutionEpoch defined by ScalarValues::Integer [1];
        in attribute reportedAdjustedContribution defined by ScalarValues::Real [1];
        in attribute reportedSensitivityResult defined by ScalarValues::Real [1];

        objective subsystem1SensitivityObjective redefines parametricSensitivityObjective {
            doc
            /* Analyze sensitivity of quality contribution
             * with respect to component strain variations in the payload builder subsystem.
             */

            assume constraint redefines baselineAssumption;
            assume constraint {
                payloadBuilder.qualityContributionAdjusted >= 0.0
            }
            require constraint redefines nonNegativeSensitivityRequirement;
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedSensitivityResult - sensitivityResult) <= evidenceTolerance
            }
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedAdjustedContribution - payloadBuilder.qualityContributionAdjusted) <= evidenceTolerance
            }
        }

        return redefines sensitivityResult defined by ScalarValues::Real [1];
    }

    analysis def Subsystem1DegradationAnalysis specializes DegradationScenarioAnalysis {
        subject payloadBuilder subsets Subsystem1_Configuration::payloadBuilder_gb;

        in attribute redefines degradationScenarios defined by Subsystem1DegradationScenario [*];
        in attribute redefines nominalContribution defined by ScalarValues::Real
            default Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughPrebuilderGain
                + Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughFinisherGain;
        in attribute redefines degradedContribution defined by ScalarValues::Real
            default nominalContribution * degradationFactor;
        in attribute redefines degradationFactor defined by ScalarValues::Real default 0.993;

        attribute evidenceTolerance defined by ScalarValues::Real [1] = 0.05;
        in attribute lastAnalysisRunId defined by ScalarValues::Integer [1];
        in attribute lastExecutionEpoch defined by ScalarValues::Integer [1];
        in attribute reportedAdjustedContribution defined by ScalarValues::Real [1];
        in attribute reportedDegradationImpact defined by ScalarValues::Real [1];

        objective subsystem1DegradationObjective redefines degradationScenarioObjective {
            assume constraint redefines degradationInputsAssumption;
            assume constraint {
                payloadBuilder.qualityContributionAdjusted >= 0.0
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

    analysis def Subsystem1TradeStudyAnalysis specializes TradeStudyAnalysis {
        subject payloadBuilder subsets Subsystem1_Configuration::payloadBuilder_gb;

        in attribute redefines alternatives defined by Subsystem1TradeAlternative [*];
        in attribute redefines weightPerformance defined by ScalarValues::Real default 0.5;
        in attribute redefines weightCost defined by ScalarValues::Real default 0.2;
        in attribute redefines weightResilience defined by ScalarValues::Real default 0.2;
        in attribute redefines weightRisk defined by ScalarValues::Real default 0.1;

        attribute evidenceTolerance defined by ScalarValues::Real [1] = 0.05;
        in attribute lastAnalysisRunId defined by ScalarValues::Integer [1];
        in attribute lastExecutionEpoch defined by ScalarValues::Integer [1];
        in attribute reportedSelectedAlternativeScore defined by ScalarValues::Real [1];

        objective subsystem1TradeStudyObjective redefines tradeStudyObjective {
            assume constraint redefines nonNegativeWeightsAssumption;
            require constraint redefines nonNegativeSelectedScoreRequirement;
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedSelectedAlternativeScore - selectedAlternativeScore) <= evidenceTolerance
            }
        }

        return redefines selectedAlternativeScore defined by ScalarValues::Real [1];
    }

    comment subsystem1AnalysisExecutionContextModelerComment about payloadBuilderAnalysisContext locale "en_US"
    /* L2 Subsystem1 analysis execution context. */

    comment subsystem1AnalysisExecutionAdviceModelerComment
        about payloadBuilderAnalysisContext
        locale "en_US"
    /* Sequence hint: calibrate behavior and scenarios first, then
     * refine connectivity and verify.
     */
    part payloadBuilderAnalysisContext {
        part payloadBuilderUnderAnalysis subsets Subsystem1_Configuration::payloadBuilder_gb;

        analysis payloadQualityGainPayloadBuilderSensitivityAnalysis defined by Subsystem1StrainSensitivityAnalysis {
            doc
            /* Scenario contract:
             * 1) Vary L2 component contribution and strain assumptions.
             * 2) Run external simulations.
             * 3) Feed simulatedAdjustedContribution and sensitivityResult back.
             * 4) Validate objective constraints in the model.
             */
            subject redefines payloadBuilder subsets payloadBuilderUnderAnalysis;

            in attribute redefines allocationScenarios defined by Subsystem1ContributionAllocationScenario [*];

            in attribute nominalScenario subsets allocationScenarios defined by Subsystem1ContributionAllocationScenario [1] {
                attribute redefines payloadPrebuilderContribution =
                    Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughPrebuilderGain;
                attribute redefines payloadFinisherContribution =
                    Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughFinisherGain;
                attribute redefines externalContribution = 0.0;
                attribute redefines maintenanceStrain = 0.0;
                attribute redefines demandFactor = 1.0;
            }

            in attribute finisherHotspotScenario subsets allocationScenarios defined by Subsystem1ContributionAllocationScenario [1] {
                comment scenarioPerturbation /*
                    Intentional stress scenario: finisher contribution is reduced by 0.1
                    quality units from the L2 finisher allocation while the prebuilder remains
                    at its allocated budget.
                */
                attribute redefines payloadPrebuilderContribution =
                    Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughPrebuilderGain;
                attribute redefines payloadFinisherContribution =
                    Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughFinisherGain - 0.1;
                attribute redefines externalContribution = 0.0;
                attribute redefines maintenanceStrain = 1.0;
                attribute redefines demandFactor = 1.0;
            }

            comment subsystem1AnalysisUsageEvidenceModelerComment
                about payloadQualityGainPayloadBuilderSensitivityAnalysis
                locale "en_US"
            /* Usage-specific execution evidence (per analysis run instance). */
            in attribute redefines lastAnalysisRunId = 1779875123;
            in attribute redefines lastExecutionEpoch = 1779875123;
            in attribute redefines reportedAdjustedContribution = 13.97;
            in attribute redefines reportedSensitivityResult = 0.002143;
        }

        analysis payloadQualityGainPayloadBuilderSeededExemplarAnalysis defined by Subsystem1StrainSensitivityAnalysis {
            doc
            /* Seeded exemplar scenario with literal values to break circular interpretation.
             * This usage demonstrates a concrete allocation input set and keeps run-id at 0
             * so objective checks treat it as a non-validated reference scenario.
             */
            comment seededClosureTrace
            /* Closure trace: 4.0 + 10.0 = 14.0 (payloadBuilder seeded contribution). */
            subject redefines payloadBuilder subsets payloadBuilderUnderAnalysis;

            in attribute redefines allocationScenarios defined by Subsystem1ContributionAllocationScenario [*];

            in attribute recursiveClosureSeedScenario subsets allocationScenarios defined by Subsystem1ContributionAllocationScenario [1] {
                attribute redefines payloadPrebuilderContribution = 4.0;
                attribute redefines payloadFinisherContribution = 10.0;
                attribute redefines externalContribution = 0.0;
                attribute redefines maintenanceStrain = 0.0;
                attribute redefines demandFactor = 1.0;
            }

            in attribute redefines lastAnalysisRunId = 0;
            in attribute redefines lastExecutionEpoch = 0;
            in attribute redefines reportedAdjustedContribution = 0.0;
            in attribute redefines reportedSensitivityResult = 0.0;
        }

        analysis degradationAnalysis defined by Subsystem1DegradationAnalysis {
            subject redefines payloadBuilder subsets payloadBuilderUnderAnalysis;
            in attribute redefines degradationScenarios defined by Subsystem1DegradationScenario [*];
            in attribute nominalDegradationScenario subsets degradationScenarios defined by Subsystem1DegradationScenario [1] {
                attribute redefines nominalContribution =
                    Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughPrebuilderGain
                        + Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughFinisherGain;
                attribute redefines degradationFactor = 0.993;
                attribute redefines degradedContribution = nominalContribution * degradationFactor;
                attribute redefines maintenanceStrain = 0.0;
            }
            in attribute redefines lastAnalysisRunId = 0;
            in attribute redefines lastExecutionEpoch = 0;
            in attribute redefines reportedAdjustedContribution = 0.0;
            in attribute redefines reportedDegradationImpact = 0.0;
        }

        analysis tradeStudyAnalysis defined by Subsystem1TradeStudyAnalysis {
            subject redefines payloadBuilder subsets payloadBuilderUnderAnalysis;
            in attribute redefines alternatives defined by Subsystem1TradeAlternative [*];
            in attribute baselineTradeAlternative subsets alternatives defined by Subsystem1TradeAlternative [1] {
                attribute redefines expectedQualityGain =
                    Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughPrebuilderGain
                        + Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughFinisherGain;
                attribute redefines expectedCost = 4.0;
                attribute redefines expectedMaintenanceStrain = 1.0;
                attribute redefines riskPenalty = 1.0;
            }
            in attribute redefines lastAnalysisRunId = 0;
            in attribute redefines lastExecutionEpoch = 0;
            in attribute redefines reportedSelectedAlternativeScore = 0.0;
        }
    }
}
