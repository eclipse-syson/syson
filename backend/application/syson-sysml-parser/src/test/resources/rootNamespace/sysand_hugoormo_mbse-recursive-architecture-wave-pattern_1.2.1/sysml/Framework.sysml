package MyFramework {
    doc
    /* this package represents an exemplar framework for demonstrating the integration of
     * a framework in the MBSE RecursiveArchitecture Weave Pattern.
     * A framework shall provide a basis for communication between stakeholders and will
     * define the viewpoints that shall frame predefined concerns of a set of generic stakeholders.
     * UAF is an example of a framework that provides viewpoints for the operational level.
     * A framework may or may not also provide an ontology of the scope of the framework.
     */

    package MyFrameworkRecursiveClosureSemantics {
        doc
        /* This package documents the recursive validation and verification closure semantics
         * used by the reference architecture pattern.
         *
         * A derived requirement may own nested child needs that allocate part of the parent
         * obligation to a lower architectural level. Those child needs are not intended to be
         * directly satisfied by the parent-level configuration. They are validated when a
         * lower-level requirement subsets the child need and redefines its subject in the
         * lower-level context.
         *
         * The transformed lower-level requirement is then verified by the lower-level
         * architecture through satisfy relations, behavioral bindings, analysis cases, and
         * verification cases as applicable.
         *
         * In this pattern:
         * - need validation = parent child need is accepted and transformed by subsetting
         * - requirement verification = transformed requirement is satisfied by architecture
         *
         * This separation avoids duplicate satisfy links while preserving both evidence
         * questions: whether the decomposition is valid in the parent context, and whether the
         * lower-level architecture fulfills the transformed requirement.
         */

        comment recursiveClosureRule /*
            Recursive closure rule:
            A parent-level child need is considered validated when a lower-level requirement
            subsets it. A transformed lower-level requirement is considered verified when a
            lower-level configuration subject satisfies it and the applicable verification
            objective verifies it.
        */
    }

    package MyFrameworkConfigurationBehaviorSemantics {
        doc
        /* This package documents the recursive configuration and behavior semantics used
         * by the reference architecture pattern.
         *
         * The pattern separates architectural realization from behavioral value production:
         * configuration elements own parts, connections, satisfy relations, and action
         * performances; behavior elements define how payload quality values are produced
         * and propagated through action outputs.
         */

        comment blackboxBehavioralRealizationRule /*
            BLACKBOX BEHAVIORAL REALIZATION RULE:
            A blackbox configuration part shall separate capability input from realized
            effect. Capability is provided as an independent seed attribute, while
            realized contribution is computed by behavior and then bound back to the
            architecture element.

            Pattern:
            part X_bb {
                attribute qualityCapabilitySetting = <seeded capability envelope>;
                attribute realizedQualityContribution = action_bb.outputPayload.quality;
                attribute redefines qualityContribution = realizedQualityContribution;
                perform action action_bb references X_Behavior::action_bb { ... }
                satisfy X_Requirements::originalRequirement;
            }

            Action inputs should use qualityCapabilitySetting (or equivalent name) to
            avoid circular-looking semantics where qualityContribution appears to feed
            itself. qualityContribution remains a compatibility alias for realized effect
            so existing constraints and requirement relations remain stable.
        */

        comment capabilityToRealizationCausalityRule /*
            CAPABILITY-TO-REALIZATION CAUSALITY RULE:
            Recursive value flow shall be one-way at each decomposition level:
            capability seed -> behavioral transformation -> realized quality effect.

            Requirements constrain realized effect and allocate capability envelopes.
            Analysis/verification scenarios may inject capability seeds, but they shall
            not directly overwrite realized effect attributes in configuration parts.

            SysMLv2-specification-limitation note:
            In current SysMLv2 language maturity, execution semantics for some typed
            value-resolution/arithmetic combinations are not fully standardized.
            Models may therefore encode realized-quality delta intent explicitly while
            treating full numeric execution as future-specification behavior.
        */

        comment grayboxDecompositionRule /*
            GRAYBOX DECOMPOSITION RULE:
            A graybox configuration part exposes constituent parts, actions, and direct
            handoff flows. It shall not override the blackbox qualityContribution binding.
            Recursive allocation sums are defined in the derived requirement, not by
            rebinding the graybox configuration.

            This preserves SysML v2 binding-feature consistency and keeps the derived
            requirement as the source of truth for allocated constituent budgets.
        */

        comment whiteboxMountingTunnelingRule /*
            WHITEBOX MOUNTING AND TUNNELING RULE:
            A whitebox configuration may redefine graybox constituents to deeper integrated
            forms and connect to the executable terminal endpoint inside a decomposed branch.
            This tunneling pattern avoids redundant ports and bindings on every intermediate
            part while still exposing the integrated interaction path at the selected level.
        */

        comment delegatedBehaviorExecutionRule /*
            DELEGATED BEHAVIOR EXECUTION RULE:
            Graybox and whitebox actions may act as structural or traceability shells when
            executable behavior is delegated to constituent actions. Verification stimuli
            and executable scenarios shall run the lowest integrated constituent actions that
            own behavior, while configuration connections define the intended handoff path.
        */

        comment rootOperationalBehaviorExceptionRule /*
            ROOT OPERATIONAL BEHAVIOR EXCEPTION RULE:
            The L0 operational blackbox may assign root value-stream quality directly because
            it represents the whole operational context as an observed or stipulated value
            stream. Designed lower levels should compute quality incrementally from
            qualityContribution inputs and payload handoffs.
        */

        comment useCaseIntentTraceabilityRule /*
            USE CASE INTENT TRACEABILITY RULE:
            Use cases may be used as preliminary interaction contracts that capture the
            early behavioral story: initiating participant, responding participant, exchanged
            messages, and intended ordering. They are not the authoritative executable
            behavior and shall not replace behavior actions, configuration bindings,
            analysis, verification, or requirement satisfaction evidence.

            In the recursive pattern, a blackbox use case may draft the interaction intent.
            A graybox use case may then redefine that use case by referencing participants
            to realized configuration parts and messages to connector flows. This preserves
            traceability from early behavior intent into the architectural realization.

            Stronger projects may extend the trace chain from:
            use case message -> connector flow -> action input/output -> behavior result
            -> analysis or verification evidence.

            Use cases are optional at levels where they do not add explanatory value, such
            as simple terminal parts with a single behavior action.
        */

        comment COTSConnectorBoundaryRule /*
            COTS CONNECTOR BOUNDARY RULE:
            Commercial off-the-shelf items may impose fixed ports, interfaces, protocols,
            data shapes, timing, certification boundaries, or supplier-owned behavior that
            the architecture cannot freely redesign.

            In the recursive pattern, a connector to a COTS item may therefore specialize a
            COTS integration connector and incorporate the externally imposed interface
            contract already at graybox level. The connector does not own or implement the
            COTS port. It records the compatibility assertion between the designed endpoint
            and the fixed COTS boundary.

            This preserves the rich-connector pattern: ordinary rich connectors carry
            interaction intent and may introduce interface structure when useful; COTS
            integration connectors carry interaction intent plus externally imposed boundary
            constraints.
        */

        abstract connection def COTSIntegrationConnector {
            doc
            /* Marker connector category for COTS integration connectors.
             *
             * This framework definition intentionally owns no generic interface features.
             * Concrete product libraries specialize this definition and expose the actual
             * supplier-imposed interface contract that the designed endpoint must satisfy.
             * The marker enables queries, inspections, and future verification rules over
             * all COTS integration connectors without duplicating product-specific
             * interface semantics in the framework.
             */

        }
    }

    package MyFrameworkFoundationSemantics {
        doc
        /* This package documents how the framework, ontology, standard, and taxonomy
         * layers work together in the MBSE RecursiveArchitecture Weave Pattern.
         */

        comment foundationLayeringRule /*
            FOUNDATION LAYERING RULE:
            The framework defines method semantics, stakeholder/viewpoint abstractions, and
            reusable analysis/verification patterns. The ontology defines abstract domain
            concepts. The standard defines reusable compliance obligations. Each taxonomy
            contextualizes ontology concepts for a specific architectural owner, level, or
            product scope.
        */

        comment localTaxonomyOwnershipRule /*
            LOCAL TAXONOMY OWNERSHIP RULE:
            Repeated taxonomies at each level or part are intentional. In real projects,
            different organizations, suppliers, or organizational units may use different
            product taxonomies while still mapping back to a shared ontology and standard.
            A local taxonomy may therefore introduce local item, part, connection, port, and
            interface names while preserving traceability through subsetting or
            specialization.
        */

        comment qualityVocabularyRule /*
            QUALITY VOCABULARY RULE:
            quality is the value carried by exchanged payload or service items.
            qualityContribution is the value attributed to an architecture element as the
            contribution it realizes through behavior.
            qualityGain is the standard compliance measure used by MyStandard for elements
            in the scope of the standard.
            These values may be bound or compared, but they are not interchangeable by name.
        */

        comment valueProvenanceGlossaryRule /*
            VALUE PROVENANCE GLOSSARY:
            normativeTarget:
            requirement-level obligation derived from stakeholder concerns and decomposition logic.

            executableSeed:
            explicit scenario/envelope input used to stimulate behavior and explore feasibility.

            computedResult:
            behavior-produced value computed from seeds, constraints, and architecture interactions.

            observedEvidence:
            measured result from a realized system under V&V context; not claimable from model-only execution.
        */

        comment standardComplianceOverlayRule /*
            STANDARD COMPLIANCE OVERLAY RULE:
            The standard is a compliance overlay, not the source of the recursive mission
            requirements. Architecture elements may subset MyStandard::myStandardComplyingPart
            to inherit standard compliance obligations while still satisfying their recursive
            domain requirements separately.
        */
    }

    package MyFrameworkStakeholdersAndConcerns {
        part frameworkOperationalUser {
            doc
            /* The operationalUser is a stakeholder with operational level value stream concerns */
        }
        part frameworkOperationalUpkeeper {
            doc
            /* The operationalMaintenance is a stakeholder with operational maintenance concerns */
        }

        concern frameworkOperationalValueStream {
            doc
            /* The value stream of a product or system in the operational context shall provide the needed
             * value to ensure operational success.
             */
            subject operationalContext;
            stakeholder frameworkUser subsets frameworkOperationalUser;
        }

        concern frameworkOperationalMaintenanceStrain {
            doc
            /* The maintenance strain of a product or system in the operational context shall be 
             * kept within limits to ensure operational availability and manageable maintenance effort.
             */
            subject operationalContext;
            stakeholder frameworkUpkeeper subsets frameworkOperationalUpkeeper;
        }
    }

    package MyFrameworkViewpoints {
        viewpoint def ValueStream {
            doc
            /* The value stream viewpoint focuses on the value stream of a 
             * system or product as it flows through its life cycle, including its 
             * internal structure and in case of a system its behavior.
             */
            comment valueStreamModelerComment about ValueStream locale "en_US"
            /* Redefinition of frame <concern> is not supported by SysMLv2.
             * This viewpoint should remain associated with
             * FrameworkStakeholdersAndConcerns::frameworkOperationalValueStream
             * to preserve traceability and alignment with framework concerns.
             */
        }
        viewpoint def OperationalResilience {
            doc
            /* The operational resilience viewpoint focuses on the resilience of a 
             * system or product in the operational context, including its ability to 
             * maintain functionality under stress and recover from disruptions.
             */
            comment operationalResilienceModelerComment about OperationalResilience locale "en_US"
            /* Redefinition of frame <concern> is not supported by SysMLv2.
             * This viewpoint should remain associated with
             * FrameworkStakeholdersAndConcerns::frameworkOperationalMaintenanceStrain
             * to preserve traceability and alignment with framework concerns.
             */
        }
    }

    package MyFrameworkViews {
        view def ProductValueStream {
            doc
            /* The product value stream focuses on the value stream of a product as it 
             * flows through its life cycle, including its internal structure and in case of a system its behavior.
             */
            viewpoint valueStream defined by MyFrameworkViewpoints::ValueStream;
            comment productValueStreamModelerComment about ProductValueStream locale "en_US"
            /* Framework-specific view directives:
             * filter @SysML::PartUsage
             * render asSequenceView
             */
        }

        view def SystemValueStream {
            doc
            /* The system value stream focuses on the value stream of a system as it 
             * flows through its life cycle, including its internal structure and behavior.
             */
            viewpoint valueStream defined by MyFrameworkViewpoints::ValueStream;
            comment systemValueStreamModelerComment about SystemValueStream locale "en_US"
            /* Framework-specific view directives:
             * filter @SysML::PartUsage
             * render asSequenceView
             */
        }

        view def SystemMaintenanceValueStream specializes SystemValueStream {
            doc
            /* The system maintenance value stream focuses on the maintenance value stream of a system as it 
             * flows through its life cycle, including its internal structure and behavior.
             */
            viewpoint operationalResilience defined by MyFrameworkViewpoints::OperationalResilience;
            comment systemMaintenanceValueStreamModelerComment
                about SystemMaintenanceValueStream
                locale "en_US"
            /* Framework-specific view directives, adding to SystemValueStream:
             * filter @SysML::PartUsage
             * render asSequenceView
             */
        }
    }

    package MyFrameworkVerificationMetaPatterns {
        private import ScalarValues::*;

        doc
        /* This package defines reusable verification meta-patterns for the MBSE RecursiveArchitecture Weave Pattern.
         * The intent is to separate normative requirement truth (in requirements) from evidential strategy
         * (in verification). Practitioners should specialize these defs at each level (L0-L3).
         *
         * Important publication note:
         * These meta-patterns define verification intent, scope, evidence pipeline, and method
         * classification. The default verdict expressions are placeholders that keep the
         * reference model executable and structurally valid. They are not a substitute for
         * project-specific acceptance logic, measured evidence, or an implemented test bench.
         */

        comment verificationExecutionRule /*
            VERIFICATION EXECUTION RULE:
            Evidence verification executes the lowest integrated starter actions that own
            executable behavior for the selected subject. Whitebox actions may remain
            traceability shells when their executable behavior is delegated to constituent
            parts. In that case, the stimulus shall execute the constituent endpoint actions
            connected by the selected configuration subject.
        */

        comment verificationMethodRule /*
            VERIFICATION METHOD RULE:
            Use analyze when evidence comes from executable model behavior, calculated traces,
            or numeric constraint evaluation. Use inspect for manual, ad-hoc, review-based
            checks such as structural compliance and modeling-rule conformance. Use test or
            demonstration only when a realized product, prototype, test bench, or real
            operational context exists. The same analysis intent modeled here should be
            repeated as test or demonstration evidence when implemented parts or deployed
            systems become available.
        */

        comment placeholderVerdictRule /*
            PLACEHOLDER VERDICT RULE:
            PassIf(true) in the reusable meta-patterns is a scaffold default. Concrete
            projects shall replace or specialize verdict computation with acceptance logic
            that combines requirement evaluation, evidence adequacy, method suitability,
            and traceable observations.
        */

        verification def IntegratedChainEvidenceVerification specializes EvidenceBasedVerificationMetaPattern {
            doc
            /* INTENT CATEGORY: integrated-chain acceptance verification.
             *
             * Use this category when a level is decomposed and verification must validate
             * the full interaction chain across constituent parts connected by configuration
             * connectors/interfaces/ports.
             *
             * The integrated-chain subject should be the highest integrated subject available
             * at that level. The stimulus may execute owned constituent actions directly when
             * the whitebox action is retained only as a traceability shell.
             *
             * LIGHTWEIGHT COVERAGE PLACEHOLDERS:
             * - Declarative guidance only (not enforced in verdict logic).
             * - Keep strictCoverageEnabled=false in reference models.
             */

            attribute expectedScope defined by ScalarValues::String [1] = "integrated-chain";
            attribute minimumScenarioCount defined by ScalarValues::Integer [1] = 1;
            attribute requiresBoundaryScenario defined by ScalarValues::Boolean [1] = true;
            attribute strictCoverageEnabled defined by ScalarValues::Boolean [1] = false;
        }

        verification def TerminalCapabilityEvidenceVerification specializes EvidenceBasedVerificationMetaPattern {
            doc
            /* INTENT CATEGORY: terminal capability verification.
             *
             * Use this category when a branch is terminal (no further decomposition at this
             * level), and verification focuses on the single-part capability evidence.
             *
             * In a model-only reference pattern this is typically analyze evidence from the
             * terminal behavior. In an implemented product this same intent should be
             * repeated as test or demonstration evidence where appropriate.
             *
             * LIGHTWEIGHT COVERAGE PLACEHOLDERS:
             * - Declarative guidance only (not enforced in verdict logic).
             * - Keep strictCoverageEnabled=false in reference models.
             */

            attribute expectedScope defined by ScalarValues::String [1] = "terminal-capability";
            attribute minimumScenarioCount defined by ScalarValues::Integer [1] = 1;
            attribute repeatabilityRunCount defined by ScalarValues::Integer [1] = 1;
            attribute strictCoverageEnabled defined by ScalarValues::Boolean [1] = false;
        }

        verification def ManualComplianceInspectionVerification specializes InspectVerificationMetaPattern {
            doc
            /* INTENT CATEGORY: one-off manual compliance inspection.
             *
             * Use this category for case-specific inspections that are primarily review-based
             * and not intended for reusable automation templates.
             *
             * Inspection is intentionally different from analyze/test/demo evidence: it records
             * human judgment against structural, compliance, and modeling-rule criteria.
             *
             * LIGHTWEIGHT COVERAGE PLACEHOLDERS:
             * - Declarative guidance only (not enforced in verdict logic).
             * - Keep strictCoverageEnabled=false in reference models.
             */

            attribute expectedScope defined by ScalarValues::String [1] = "manual-compliance-inspection";
            attribute checklistCompletenessTargetPercent defined by ScalarValues::Integer [1] = 100;
            attribute independentReviewerRecommended defined by ScalarValues::Boolean [1] = true;
            attribute strictCoverageEnabled defined by ScalarValues::Boolean [1] = false;
        }

        verification def InspectVerificationMetaPattern {
            private import VerificationCases::*;

            doc
            /* HEURISTICS FOR INSPECT VERIFICATION:
             * 1) Use inspect for structural and compliance checks that are primarily manual or review-based.
             * 2) Keep objective text explicit: state what is inspected, against which rule, and expected evidence.
             * 3) Do not restate or redefine requirement semantics here; inspect verifies conformance to them.
             * 4) Record findings in natural language with references to model elements and rationale.
             * 5) Prefer concise, checklist-like objective criteria to improve repeatability between reviewers.
             */

            subject inspectedSubject;

            objective inspectionObjective {
                doc
                /* Natural-language inspection objective to be specialized per verification case.
                 * Example: inspect subsetting chain integrity, naming conventions, standard compliance,
                 * and placement of satisfy relations for the selected subject.
                 */
            }

            metadata VerificationMethod {
                kind = VerificationMethodKind::inspect;
            }

            action collectInspectionEvidence {
                doc
                /* Pipeline step: collect inspection evidence.
                 * Gather reviewer observations, checklist findings, and references
                 * to the inspected model elements.
                 */
                out item findings;
            }
            then action evaluateInspectionFindings {
                doc
                /* Pipeline step: evaluate inspection findings.
                 * Determine verdict from the collected findings against the
                 * inspection objective. In specialized cases, replace the
                 * placeholder expression with explicit checklist criteria.
                 */
                in item findings;
                comment evaluateInspectionFindingsModelerComment
                    about evaluateInspectionFindings
                    locale "en_US"
                /* Placeholder verdict: specialize for real inspection checklist closure. */
                out verdict : VerdictKind = PassIf(true);
            }

            flow collectInspectionEvidence.findings to evaluateInspectionFindings.findings;
            return verdict : VerdictKind = evaluateInspectionFindings.verdict;
        }

        verification def EvidenceBasedVerificationMetaPattern {
            private import VerificationCases::*;

            doc
            /* HEURISTICS FOR ANALYZE/TEST VERIFICATION:
             * 1) Use this pattern for evidence-based verification where verdict depends on observed behavior.
             * 2) Keep requirement constraints normative; verification constraints must be evidential/complementary.
             * 3) Explicitly define context, input strategy, observation plan, and acceptance mapping.
             * 4) Use tri-state semantics through verdict rationale: pass, fail, or inconclusive when evidence is weak.
             * 5) Analyze: model-computed traces and scenario coverage. Test: measured traces, calibration, repeatability.
             * 6) The reference model specializes stimulus only; concrete programs should also specialize observation,
             *    requirement evaluation, evidence adequacy, and verdict computation when real evidence is available.
             */

            doc
            /* ANALYZE VS TEST SPECIALIZATION GUIDANCE:
             * - Analyze specialization:
             *   * VerificationMethod.kind = VerificationMethodKind::analyze.
             *   * Stimulus is model-level (parameter sets, boundary values, scenario sweeps).
             *   * Observations are computed traces from model execution.
             *   * Evidence adequacy emphasizes scenario/path/constraint coverage.
             *
             * - Test specialization:
             *   * VerificationMethod.kind = VerificationMethodKind::test.
             *   * Stimulus is procedure-level over a realized product (test bench, instruments, scripts).
             *   * Observations are measured values with timestamps and uncertainty.
             *   * Evidence adequacy emphasizes calibration, repeatability, and environmental control.
             *
             * - Shared rule for both:
             *   * Requirement constraints remain normative.
             *   * Verification logic provides evidential adequacy and verdict confidence.
             */

            subject verificationSubject;

            objective verificationObjective {
                doc
                /* Specialize objective with:
                 * - requirement(s) under verification,
                 * - scenario boundaries,
                 * - evidence quality criteria,
                 * - acceptance mapping from observed data to verdict.
                 */
            }

            metadata VerificationMethod {
                comment verificationMethodModelerComment about VerificationMethod locale "en_US"
                /* Specialize in concrete cases as analyze or test depending on evidence source. */
                kind = VerificationMethodKind::analyze;
            }

            action prepareContext {
                doc
                /* Pipeline step: prepare context.
                 * Bind assumptions, operational mode, and scenario boundaries used for this run.
                 */
                out item preparedContext;
            }
            then action applyStimulus {
                doc
                /* Pipeline step: apply stimulus.
                 * Execute model inputs (analyze) or test procedures (test) for the prepared context.
                 */
                in item preparedContext;
                out item stimulusExecution;
            }
            then action collectObservations {
                doc
                /* Pipeline step: collect observations.
                 * Gather raw outputs, traces, and measurement records produced during stimulus execution.
                 * In this reference pattern this step remains intentionally generic; concrete
                 * projects should specialize it with the exact values, traces, or measurements
                 * collected from model execution, tests, or demonstrations.
                 */
                in item stimulusExecution;
                out item rawObservations;
            }
            then action normalizeEvidence {
                doc
                /* Pipeline step: normalize evidence.
                 * Process raw observations into comparable evidence (units, timestamps,
                 * tolerances, uncertainty handling, and formatting).
                 */
                in item rawObservations;
                out item processedEvidence;
            }
            then action evaluateRequirements {
                doc
                /* Pipeline step: evaluate requirements.
                 * Evaluate requirement truth conditions against processed evidence.
                 * This step should not alter requirement semantics.
                 * Requirement constraints remain the normative source of truth.
                 */
                in item processedEvidence;
                out item requirementEvaluation;
            }
            then action evaluateEvidenceAdequacy {
                doc
                /* Pipeline step: evaluate evidence adequacy.
                 * Determine whether the evidence quality is sufficient (coverage,
                 * validity, repeatability, calibration as applicable).
                 * This is the proper place to decide whether analyze evidence is sufficient
                 * or whether later test/demonstration evidence is still required.
                 */
                in item processedEvidence;
                out item evidenceAdequacy;
            }
            then action computeVerdict {
                doc
                /* Pipeline step: compute verdict.
                 * Combine requirement evaluation and evidence adequacy into final verdict.
                 * Specializations should implement pass/fail/inconclusive semantics explicitly.
                 * The default PassIf(true) below is a scaffold placeholder, not acceptance
                 * evidence for a real program.
                 */
                in item requirementEvaluation;
                in item evidenceAdequacy;
                comment computeVerdictModelerComment about computeVerdict locale "en_US"
                /* Placeholder verdict: specialize with concrete acceptance logic. */
                out verdict : VerdictKind = PassIf(true);
            }
            then action emitVerificationRecord {
                doc
                /* Pipeline step: emit verification record.
                 * Package objective, method, context, processed evidence, and final verdict
                 * into a traceable verification artifact.
                 */
                in item preparedContext;
                in item processedEvidence;
                in item requirementEvaluation;
                in item evidenceAdequacy;
                in verdict;
                out item verificationRecord;
            }

            flow prepareContext.preparedContext to applyStimulus.preparedContext;
            flow applyStimulus.stimulusExecution to collectObservations.stimulusExecution;
            flow collectObservations.rawObservations to normalizeEvidence.rawObservations;
            flow normalizeEvidence.processedEvidence to evaluateRequirements.processedEvidence;
            flow normalizeEvidence.processedEvidence to evaluateEvidenceAdequacy.processedEvidence;
            flow evaluateRequirements.requirementEvaluation to computeVerdict.requirementEvaluation;
            flow evaluateEvidenceAdequacy.evidenceAdequacy to computeVerdict.evidenceAdequacy;
            flow prepareContext.preparedContext to emitVerificationRecord.preparedContext;
            flow normalizeEvidence.processedEvidence to emitVerificationRecord.processedEvidence;
            flow evaluateRequirements.requirementEvaluation to emitVerificationRecord.requirementEvaluation;
            flow evaluateEvidenceAdequacy.evidenceAdequacy to emitVerificationRecord.evidenceAdequacy;
            flow computeVerdict.verdict to emitVerificationRecord.verdict;

            return verdict : VerdictKind = computeVerdict.verdict;
        }
    }

    package MyFrameworkAnalysisMetaPatterns {
        private import ScalarValues::*;
        private import RealFunctions::*;

        doc
        /* Reusable analysis meta-patterns for what-if studies and option comparison.
         * 
         * These analysis definitions specify a contract that practitioners must implement 
         * through external evaluation logic (e.g., Python scripts, simulation tools, etc.):
         * 
         * 1. Each analysis def declares inputs (assumptions), computation objectives, and outputs.
         * 2. Practitioners design and execute evaluation logic that:
         *    - Takes the declared inputs
         *    - Evaluates them against the objective assume/require constraints
         *    - Produces the required returned output values
         * 3. Results feed back into the model as bindings to architecture constraints and attributes.
         * 4. The model evaluates objective constraints against those returned values.
         * 
         * This approach keeps the system model as the authoritative specification while 
         * allowing flexible, tool-agnostic implementation of analysis execution.
         * 
         * These analysis defs provide analysis evidence by exploring behavior under scenarios.
         */

        comment analysisRolePolicy /*
            ANALYSIS ROLE POLICY (REFERENCE PATTERN CONTEXT):
            Analysis in this pattern is decision-support evidence, not empirical proof of
            real-world performance. Exogenous technology envelopes and assumptions may be
            fictitious by design to teach method and traceability.

            Therefore analysis shall be used to:
            - evaluate robustness of allocations under scenario variation,
            - compare alternatives and justify design decisions,
            - calibrate tolerances and margins in derived requirements,
            - expose coupling and assumption sensitivity across decomposition levels.

            Analysis shall not be used to claim validated operational capability.
            Empirical claims require realization plus verification/validation with measured
            evidence on the implemented system.
        */

        attribute def ContributionAllocationScenario {
            attribute primaryContribution defined by ScalarValues::Real [1];
            attribute secondaryContribution defined by ScalarValues::Real [1];
            attribute externalContribution defined by ScalarValues::Real [1];
            attribute maintenanceStrain defined by ScalarValues::Real [1];
            attribute demandFactor defined by ScalarValues::Real [1];
        }

        attribute def DegradationScenarioProfile {
            attribute nominalContribution defined by ScalarValues::Real [1];
            attribute degradedContribution defined by ScalarValues::Real [1];
            attribute degradationFactor defined by ScalarValues::Real [1];
            attribute maintenanceStrain defined by ScalarValues::Real [1];
        }

        attribute def TradeAlternativeProfile {
            attribute expectedQualityGain defined by ScalarValues::Real [1];
            attribute expectedCost defined by ScalarValues::Real [1];
            attribute expectedMaintenanceStrain defined by ScalarValues::Real [1];
            attribute riskPenalty defined by ScalarValues::Real [1];
        }

        analysis def ParametricSensitivityAnalysis {
            doc
            /* Analyze sensitivity of outcome versus budget and parameter variations.
             */

            subject analysisSubject;

            objective parametricSensitivityObjective {
                doc
                /* Specialize assumptions and required acceptance constraints for the analyzed subject.
                 */
                assume constraint baselineAssumption {
                    baselineContribution >= 0.0 and sensitivityWindow >= 0.0
                }
                require constraint nonNegativeSensitivityRequirement {
                    sensitivityResult >= 0.0
                }
            }

            in attribute allocationScenarios defined by ContributionAllocationScenario [*];
            in attribute baselineContribution defined by ScalarValues::Real [1];
            in attribute variedContributionSamples defined by ScalarValues::Real [*];
            in attribute sensitivityWindow defined by ScalarValues::Real [1];

            action evaluateScenario {
                out item rawResponse;
            }
            then action computeSensitivity {
                in item rawResponse;
                out sensitivityIndex defined by ScalarValues::Real [1] = 0.0;
            }

            flow evaluateScenario.rawResponse to computeSensitivity.rawResponse;

            return sensitivityResult defined by ScalarValues::Real [1] = computeSensitivity.sensitivityIndex;
        }

        analysis def DegradationScenarioAnalysis {
            doc
            /* Analyze behavior degradation between nominal and degraded conditions.
             */

            subject analysisSubject;

            objective degradationScenarioObjective {
                doc
                /* Specialize degradation assumptions and required acceptance constraints for the analyzed subject.
                 */
                assume constraint degradationInputsAssumption {
                    nominalContribution >= 0.0 and degradedContribution >= 0.0 and degradationFactor >= 0.0
                }
                require constraint nonNegativeDegradationRequirement {
                    degradationImpact >= 0.0
                }
                require constraint degradationConsistencyRequirement {
                    abs(degradedContribution - (nominalContribution * degradationFactor)) <= degradationConsistencyTolerance
                }
            }

            in attribute degradationScenarios defined by DegradationScenarioProfile [*];
            in attribute nominalContribution defined by ScalarValues::Real [1];
            in attribute degradedContribution defined by ScalarValues::Real [1];
            in attribute degradationFactor defined by ScalarValues::Real [1];
            in attribute degradationConsistencyTolerance defined by ScalarValues::Real [1] default 0.001;

            action computeImpact {
                out impact defined by ScalarValues::Real [1] = nominalContribution - degradedContribution;
            }

            return degradationImpact defined by ScalarValues::Real [1] = computeImpact.impact;
        }

        analysis def TradeStudyAnalysis {
            doc
            /* Analyze alternatives with a weighted scalar evaluation function.
             */

            subject analysisSubject;

            objective tradeStudyObjective {
                doc
                /* Specialize scoring assumptions and required acceptance constraints for alternatives.
                 */
                assume constraint nonNegativeWeightsAssumption {
                    weightPerformance >= 0.0 and weightCost >= 0.0 and weightResilience >= 0.0 and weightRisk >= 0.0
                }
                require constraint nonNegativeSelectedScoreRequirement {
                    selectedAlternativeScore >= 0.0
                }
            }

            in attribute alternatives defined by TradeAlternativeProfile [*];
            in attribute weightPerformance defined by ScalarValues::Real [1];
            in attribute weightCost defined by ScalarValues::Real [1];
            in attribute weightResilience defined by ScalarValues::Real [1];
            in attribute weightRisk defined by ScalarValues::Real [1];

            action evaluateAlternatives {
                out selectedScore defined by ScalarValues::Real [1] = 0.0;
                out selectedIndex defined by ScalarValues::Integer [1] = 1;
            }

            attribute selectedAlternativeIndex defined by ScalarValues::Integer [1] = evaluateAlternatives.selectedIndex;
            return selectedAlternativeScore defined by ScalarValues::Real [1] = evaluateAlternatives.selectedScore;
        }
    }

    package MyFrameworkStackInterface {

        comment S_Friedenthal
        /* Refer to: P. Shames, M. Sarrel, S. Friedenthal, A Representative Application of a Layered 
        * Interface Modeling Pattern
        */

        abstract item def DataPacket {
            attribute data defined by ScalarValues::String [0..*];
            attribute header defined by ScalarValues::String [1..*];
        }

        abstract item dataPacket defined by DataPacket [0..*] nonunique;

        abstract port def MyStackPort {
            comment stackLayerSelectionRule /*
                PRACTITIONER RULE:
                This stack port supports progressive detail modeling. For any single connector usage,
                choose one connector-facing layer as authoritative (e.g., dataPacketPort OR
                applicationProtocolPort OR communicationProtocolPort).

                Do not mix flow declarations across multiple nested layers in the same connector
                unless an explicit cross-layer trace constraint is intended.

                If more detail is needed later, refine by redefinition at a lower layer rather than
                combining multiple boundary layers in one declaration.
            */
            
            port dataPacketPort {
                out dataPacketOut subsets dataPacket ;
                in dataPacketIn subsets dataPacket ;
            }

            bind dataPacketPort.dataPacketOut = applicationProtocolPort.dataPacketIn;
            bind dataPacketPort.dataPacketIn = applicationProtocolPort.dataPacketOut;
            
            port applicationProtocolPort {
                in dataPacketIn subsets dataPacket ;
                out dataPacketOut subsets dataPacket ;
                out item applicationPackageOut subsets dataPacket;
                in item applicationPackageIn subsets dataPacket;

                perform action packApplicationProtocol {
                    in  unpackagedData  = dataPacketIn;
                    out item packagedApplicationProtocol = applicationPackageOut;
                }
                perform action unpackApplicationProtocol {
                    in item packagedApplicationProtocol = applicationPackageIn;
                    out unpackagedData  = dataPacketOut;
                }
            }

            bind applicationProtocolPort.applicationPackageOut = communicationProtocolPort.applicationPackageIn;
            bind applicationProtocolPort.applicationPackageIn = communicationProtocolPort.applicationPackageOut;


            port communicationProtocolPort {
                out item applicationPackageOut subsets dataPacket;
                in item applicationPackageIn subsets dataPacket;
                in item communicationPackageIn subsets dataPacket;
                out item communicationPackageOut subsets dataPacket;
            
                perform action packCommunicationProtocol {
                    in item packagedApplicationProtocol = applicationPackageIn;
                    out item packagedCommunicationProtocol= communicationPackageOut;
                }
                perform action unpackCommunicationProtocol {
                    in item packagedCommunicationProtocol = communicationPackageIn;
                    out item packagedApplicationProtocol = applicationPackageOut;
                }
            }

            bind communicationProtocolPort.communicationPackageOut = transportProtocolPort.communicationPackageIn;
            bind communicationProtocolPort.communicationPackageIn = transportProtocolPort.communicationPackageOut;

            port transportProtocolPort {
                out item communicationPackageOut subsets dataPacket;
                in item communicationPackageIn subsets dataPacket;
                in item transportPackageIn subsets dataPacket;
                out item transportPackageOut subsets dataPacket;
            
                perform action packTransportProtocol {
                    in item packagedCommunicationProtocol = communicationPackageIn;
                    out item packagedTransportProtocol= transportPackageOut;
                }
                perform action unpackTransportProtocol {
                    in item packagedTransportProtocol = transportPackageOut;
                    out item packagedCommunicationProtocol = communicationPackageOut;
                }
            }

            bind transportProtocolPort.transportPackageIn = dataLinkPort.transportPackageOut;
            bind transportProtocolPort.transportPackageOut = dataLinkPort.transportPackageIn;

            port dataLinkPort {
                in item transportPackageIn subsets dataPacket;
                out item transportPackageOut subsets dataPacket;
                out item dataLinkPackageOut subsets dataPacket;
                in item dataLinkPackageIn subsets dataPacket;
            
                perform action sendDataLinkPackage {
                    in item packagedTransportProtocol = transportPackageIn;
                    out item dataLinkPackage= dataLinkPackageOut;
                }
                perform action receiveDataLinkPackage {
                    in item dataLinkPackage = dataLinkPackageIn;
                    out item packagedTransportProtocol = transportPackageOut;
                }
            }

            bind dataLinkPort.dataLinkPackageOut = physicalPort.dataLinkPackageIn;
            bind dataLinkPort.transportPackageIn = physicalPort.dataLinkPackageOut;

            port physicalPort {
                in item dataLinkPackageIn subsets dataPacket;
                out item dataLinkPackageOut subsets dataPacket;
                in  twistedPairSignalIn defined by ScalarValues::String;
                out twistedPairSignalOut defined by ScalarValues::String; 
                
                perform action sendSignal {
                    in item dataLinkPackage = dataLinkPackageIn;
                    out  twistedPairSignal= twistedPairSignalOut;
                }
                perform action receiveSignal {
                    in  twistedPairSignal = twistedPairSignalIn;
                    out item dataLinkPackage = dataLinkPackageOut;
                }
            }
        }
    }
}
