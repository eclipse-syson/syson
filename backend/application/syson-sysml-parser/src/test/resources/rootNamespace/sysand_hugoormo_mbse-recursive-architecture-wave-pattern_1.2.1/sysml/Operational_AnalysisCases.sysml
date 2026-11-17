package Operational_AnalysisCases {
    private import ScalarValues::*;
    private import RealFunctions::*;
    private import MyFramework::MyFrameworkAnalysisMetaPatterns::*;

    comment operationalAnalysisStrategyDefsModelerComment about Operational_AnalysisCases locale "en_US"
    /* L0 analysis strategy defs (inherit from Framework meta-patterns). */

    comment operationalAnalysisRolePolicyLinkModelerComment about Operational_AnalysisCases locale "en_US"
    /* Analysis role and claim limits are defined in
     * MyFramework::MyFrameworkAnalysisMetaPatterns::analysisRolePolicy.
     */

    attribute def OperationalContributionAllocationScenario specializes ContributionAllocationScenario {
        attribute systemOfInterestContribution redefines primaryContribution;
        attribute interactingSystemContribution redefines externalContribution;
    }

    attribute def OperationalDegradationScenario specializes DegradationScenarioProfile;
    attribute def OperationalTradeAlternative specializes TradeAlternativeProfile;

    comment operationalStrainSensitivityAnalysisModelerComment
        about OperationalStrainSensitivityAnalysis
        locale "en_US"
    /* Practitioner advice: keep analysis subjects at _gb so constituent
     * interactions can emerge in scenarios.
     */
    analysis def OperationalStrainSensitivityAnalysis specializes ParametricSensitivityAnalysis {
        doc
        /* L0 analysis strategy contract.
         * The analysis sets the basis for design decisions. For instance in the derivation of requirements.
         * External simulation execution is required (e.g., Python orchestration), while this model
         * definition remains the authoritative specification of inputs, objectives, and expected
         * feedback values.
         */

        subject operationalContext subsets Operational_Configuration::operationalContext_gb;
        
        comment operationalAnalysisDefaultsModelerComment
            about OperationalStrainSensitivityAnalysis
            locale "en_US"
        /* Redefinitions from the framework-level analysis parameters
         * set L0-specific defaults.
         */
        in attribute redefines allocationScenarios defined by OperationalContributionAllocationScenario [*];
        in attribute redefines baselineContribution defined by ScalarValues::Real
            default Operational_Requirements::payloadQualityGainOperationalDerived.operationalPayloadThroughSystemOfInterestGain;
        in attribute redefines sensitivityWindow defined by ScalarValues::Real default 0.5;

        comment operationalAnalysisTechnicalAttributesModelerComment
            about OperationalStrainSensitivityAnalysis
            locale "en_US"
        /* Specific analysis technical attributes. */
        attribute evidenceTolerance defined by ScalarValues::Real [1] = 0.05;


        comment operationalAnalysisFeedbackModelerComment
            about OperationalStrainSensitivityAnalysis
            locale "en_US"
        /* Parameters to be fed back from external simulation runs
         * for in-model validation.
         */
        in attribute lastAnalysisRunId defined by ScalarValues::Integer [1];
        in attribute lastExecutionEpoch defined by ScalarValues::Integer [1];
        in attribute reportedAdjustedContribution defined by ScalarValues::Real [1];
        in attribute reportedSensitivityResult defined by ScalarValues::Real [1];

        objective operationalSensitivityObjective redefines parametricSensitivityObjective {
            doc
            /* Analyze sensitivity of quality contribution
             * with respect to maintenance strain variations across the operational context.
            */

            assume constraint redefines baselineAssumption;
            comment analysisAssumptionPlacementGuidance
            /* Practitioner note: analysis-case assumptions belong inside the
             * objective that is evaluated, not directly in the analysis def
             * body. This scopes the assumption to the analysis objective and
             * matches the SysMLv2 analysis-case pattern used by RAWP.
             */
            assume constraint {
                operationalContext.qualityContributionAdjusted >= 0.0
            }
            require constraint redefines nonNegativeSensitivityRequirement;
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedSensitivityResult - sensitivityResult) <= evidenceTolerance
            }
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedAdjustedContribution - operationalContext.qualityContributionAdjusted) <= evidenceTolerance
            }
        }

        return redefines sensitivityResult defined by ScalarValues::Real [1];
    }

    analysis def OperationalDegradationAnalysis specializes DegradationScenarioAnalysis {
        subject operationalContext subsets Operational_Configuration::operationalContext_gb;

        in attribute redefines degradationScenarios defined by OperationalDegradationScenario [*];
        in attribute redefines nominalContribution defined by ScalarValues::Real
            default Operational_Requirements::payloadQualityGainOperationalDerived.operationalPayloadThroughSystemOfInterestGain;
        in attribute redefines degradedContribution defined by ScalarValues::Real
            default nominalContribution * degradationFactor;
        in attribute redefines degradationFactor defined by ScalarValues::Real default 0.995;

        attribute evidenceTolerance defined by ScalarValues::Real [1] = 0.05;
        in attribute lastAnalysisRunId defined by ScalarValues::Integer [1];
        in attribute lastExecutionEpoch defined by ScalarValues::Integer [1];
        in attribute reportedAdjustedContribution defined by ScalarValues::Real [1];
        in attribute reportedDegradationImpact defined by ScalarValues::Real [1];

        objective operationalDegradationObjective redefines degradationScenarioObjective {
            assume constraint redefines degradationInputsAssumption;
            assume constraint {
                operationalContext.qualityContributionAdjusted >= 0.0
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

    analysis def OperationalTradeStudyAnalysis specializes TradeStudyAnalysis {
        subject operationalContext subsets Operational_Configuration::operationalContext_gb;

        in attribute redefines alternatives defined by OperationalTradeAlternative [*];
        in attribute redefines weightPerformance defined by ScalarValues::Real default 0.5;
        in attribute redefines weightCost defined by ScalarValues::Real default 0.2;
        in attribute redefines weightResilience defined by ScalarValues::Real default 0.2;
        in attribute redefines weightRisk defined by ScalarValues::Real default 0.1;

        attribute evidenceTolerance defined by ScalarValues::Real [1] = 0.05;
        in attribute lastAnalysisRunId defined by ScalarValues::Integer [1];
        in attribute lastExecutionEpoch defined by ScalarValues::Integer [1];
        in attribute reportedSelectedAlternativeScore defined by ScalarValues::Real [1];

        objective operationalTradeStudyObjective redefines tradeStudyObjective {
            assume constraint redefines nonNegativeWeightsAssumption;
            require constraint redefines nonNegativeSelectedScoreRequirement;
            require constraint {
                lastAnalysisRunId <= 0 or abs(reportedSelectedAlternativeScore - selectedAlternativeScore) <= evidenceTolerance
            }
        }

        return redefines selectedAlternativeScore defined by ScalarValues::Real [1];
    }

    comment operationalAnalysisExecutionContextModelerComment about operationalAnalysisContext locale "en_US"
    /* L0 analysis execution context. */

    comment operationalAnalysisExecutionSequenceModelerComment
        about operationalAnalysisContext
        locale "en_US"
    /* Recommended sequence: behavior/analysis at _gb first, then
     * connectivity refinement, then verification.
     */
    part operationalAnalysisContext {
        part operationalContextUnderAnalysis subsets Operational_Configuration::operationalContext_gb;

        analysis payloadQualityGainOperationalSensitivityAnalysis defined by OperationalStrainSensitivityAnalysis {
            doc
            /* Scenario contract:
             * 1) Vary maintenanceStrain and contribution allocations in the L0 context.
             * 2) Execute external simulation workflow.
             * 3) Feed simulatedAdjustedContribution and sensitivityResult back into this usage.
             * 4) Evaluate objective constraints to close the analysis loop in-model.
             */
            subject redefines operationalContext subsets operationalContextUnderAnalysis;

            in attribute redefines allocationScenarios defined by OperationalContributionAllocationScenario [*];

            in attribute nominalScenario subsets allocationScenarios defined by OperationalContributionAllocationScenario [1] {
                attribute redefines systemOfInterestContribution =
                    Operational_Requirements::payloadQualityGainOperationalDerived.operationalPayloadThroughSystemOfInterestGain;
                attribute redefines interactingSystemContribution =
                    Operational_Requirements::payloadQualityGainOperationalDerived.operationalPayloadThroughInteractingSystemGain;
                attribute redefines maintenanceStrain = 0.0;
                attribute redefines demandFactor = 1.0;
            }

            in attribute operationalPressureScenario subsets allocationScenarios defined by OperationalContributionAllocationScenario [1] {
                comment scenarioPerturbation /*
                    Intentional stress scenario: systemOfInterest contribution is reduced by
                    the degradation factor while the external interacting-system allocation
                    remains at its negotiated L0 budget.
                */
                attribute redefines systemOfInterestContribution =
                    Operational_Requirements::payloadQualityGainOperationalDerived.operationalPayloadThroughSystemOfInterestGain * 0.995;
                attribute redefines interactingSystemContribution =
                    Operational_Requirements::payloadQualityGainOperationalDerived.operationalPayloadThroughInteractingSystemGain;
                attribute redefines maintenanceStrain = 1.0;
                attribute redefines demandFactor = 1.0;
            }

            in attribute redefines lastAnalysisRunId = 1779875123;
            in attribute redefines lastExecutionEpoch = 1779875123;
            in attribute redefines reportedAdjustedContribution = 19.97;
            in attribute redefines reportedSensitivityResult = 0.0015;
        }

        analysis payloadQualityGainOperationalSeededExemplarAnalysis defined by OperationalStrainSensitivityAnalysis {
            doc
            /* End-to-end seeded exemplar with literal contribution values.
             * This usage provides a concrete non-circular input set and keeps run-id at 0,
             * which marks it as a reference scenario until simulation evidence is supplied.
             */
            comment seededClosureTrace
            /* Closure trace: 21.0 + 10.0 = 31.0 (operational seeded contribution envelope). */
            subject redefines operationalContext subsets operationalContextUnderAnalysis;

            in attribute redefines allocationScenarios defined by OperationalContributionAllocationScenario [*];

            in attribute recursiveClosureSeedScenario subsets allocationScenarios defined by OperationalContributionAllocationScenario [1] {
                attribute redefines systemOfInterestContribution = 21.0;
                attribute redefines interactingSystemContribution = 10.0;
                attribute redefines maintenanceStrain = 0.0;
                attribute redefines demandFactor = 1.0;
            }

            in attribute redefines lastAnalysisRunId = 0;
            in attribute redefines lastExecutionEpoch = 0;
            in attribute redefines reportedAdjustedContribution = 0.0;
            in attribute redefines reportedSensitivityResult = 0.0;
        }

        analysis degradationAnalysis defined by OperationalDegradationAnalysis {
            subject redefines operationalContext subsets operationalContextUnderAnalysis;
            in attribute redefines degradationScenarios defined by OperationalDegradationScenario [*];
            in attribute nominalDegradationScenario subsets degradationScenarios defined by OperationalDegradationScenario [1] {
                attribute redefines nominalContribution =
                    Operational_Requirements::payloadQualityGainOperationalDerived.operationalPayloadThroughSystemOfInterestGain;
                attribute redefines degradationFactor = 0.995;
                attribute redefines degradedContribution = nominalContribution * degradationFactor;
                attribute redefines maintenanceStrain = 0.0;
            }
            in attribute redefines lastAnalysisRunId = 0;
            in attribute redefines lastExecutionEpoch = 0;
            in attribute redefines reportedAdjustedContribution = 0.0;
            in attribute redefines reportedDegradationImpact = 0.0;
        }

        analysis tradeStudyAnalysis defined by OperationalTradeStudyAnalysis {
            subject redefines operationalContext subsets operationalContextUnderAnalysis;
            in attribute redefines alternatives defined by OperationalTradeAlternative [*];
            in attribute baselineTradeAlternative subsets alternatives defined by OperationalTradeAlternative [1] {
                attribute redefines expectedQualityGain =
                    Operational_Requirements::payloadQualityGainOperationalDerived.operationalPayloadThroughSystemOfInterestGain;
                attribute redefines expectedCost = 5.0;
                attribute redefines expectedMaintenanceStrain = 1.0;
                attribute redefines riskPenalty = 1.0;
            }
            in attribute redefines lastAnalysisRunId = 0;
            in attribute redefines lastExecutionEpoch = 0;
            in attribute redefines reportedSelectedAlternativeScore = 0.0;
        }
    }
}
