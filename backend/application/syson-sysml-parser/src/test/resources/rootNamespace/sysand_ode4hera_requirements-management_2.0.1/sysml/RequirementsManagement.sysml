//
// Requirements Management library
//
// The library provides concepts related to requirements management, in particular a
// requirement def type that includes various attributes to be modeled according to
// the INCOSE Needs & Requirements Manual.
//
// This library has been developed as part of the ODE4HERA project: https://www.ode4hera.eu/
//
// Version: v2.0.1
// License: MIT
// Maintainer: DLR Institute of System Architectures in Aeronautics
// Contact: digital.engineering@dlr.de
// Topics: Requirements Management, MBSE
//
// MIT License
//
// Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

library package RequirementsManagement {
	doc
    /*
	 * Library that provides requirements management attributes according to the
     * INCOSE Needs & Requirements Manual (2024), chapter 15.
     *
     * Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
	 */

    // Stakeholders
    public import SYSMOD::ExtendedStakeholder;
    public import SYSMOD::LevelKind;
    public import SYSMOD::StakeholderCategoryKind;

    alias Stakeholder for ExtendedStakeholder;

    // Needs and requirements

    concern def Need specializes NeedsRequirementsAttributes {
        doc /* Stakeholder need: something the stakeholder needs from the system, not expressed as a formal requirement. */
    }

    requirement def Requirement specializes NeedsRequirementsAttributes {
        doc /* Requirement: a formalization of one or more needs that can be objectively verified. */
    }

    requirement def NeedsRequirementsAttributes :> ProvenanceAttributes {
        doc /* Requirement and need attributes.
        Source: INCOSE Needs and Requirements Manual (NRM; 2024), Chapter 15.
        
        Attributes not included in the following list, because they can be covered semantically:
        - A2 Trace to Parent: semantically covered by requirements groups and requirements derivation
        - A4 States and Modes: semantically covered by assumptions
        - A5 Allocation/Budgeting: semantically covered by requirement breakdowns and allocation to parts
        - A6 System V&V Success Criteria: semantically covered by allocation to parts and requirement constraints
        - A7 System V&V Strategy: similar to V&V method (A8)
        - A10 System V&V Level: semantically covered by linking to an analysis case
        - A12 Condition of Use: semantically covered by assumptions
        - A15 Unique Identifier: semantically covered by the shorthand notation
        - A16 Unique Name: semantically covered by the requirement object name
        - A20 Stakeholders: semantically covered by linking to stakeholders
        - A28/A29 Need/Req V&V Status: covered by need/req status (A30)
        - A32 Trace to Interface Definition: semantically covered by the requirement subject
        - A33 Trace to Dependent Peer Requirements: semantically covered by requirement grouping and allocation
        - A35 Criticality/Essentiality: covered by priority (A34)
        - A40 Type/Category: semantically covered by class specialization and subject/constraint types

        Attributes not covered:
        - A11 System V&V Phase: not covered
        - A21 Change Management Authority: not covered
        - A22 Change Proposed: not covered
        - A38 Key Driving Need/Req: not covered
        - A41 to A46 Applicability/Reuse: not covered
        - A47 to A49 Product Lines: not covered
        */

        // Attributes related to traceability

        attribute rationale : ScalarValues::String[0..1] {
            doc /* A1 Rationale:
            States the reason for the need/requirement's existence.
            Defines why it should be included, references other information relevant for understanding,
            and may include assumptions relevant to the requirement. */
        }

        attribute source : ScalarValues::String[0..1] {
            doc /* A3 Trace to Source:
            Defines the source for the need/requirement, showing compliance to
            for example higher-level organizational requirements, regulations, and standards.
            Other examples of sources include: stakeholder interview, mission, goals,
            user story, organization department (marketing, quality, safety, compliance, etc.), analysis. */
        }

        // Attributes related to need/requirement authoring and verification/validation

        part :>> owner {
            doc /* A19 Owner:
            The person or organization that maintains the need/requirement (and all attributes),
            has the right to say something about it, approves changes to it, and reports the status.
            The owner preferably should be someone (or a position / job title) actively working on the project. */
        }

        attribute status : ReqStatus default ReqStatus::Draft {
            doc /* A30 Status of the Need or Requirement:
            Maturity of the need/requirement.
            Can be used to determine when to baseline a set of needs/requirements:
            this makes sense once all items reach a high level of maturity. */
        }

        attribute version : ScalarValues::String[0..1] {
            doc /* A23 Version Number:
            Indication of the version of the need/requirement, to ensure tool synchronization
            and that the correct version of the need/requirement is implemented. */
        }

        part :>> createdBy {
            doc /* A17 Originator/Author:
            The person defining and entering a need/requirement into the model. */
        }

        attribute :>> createdDate {
            doc /* A18 Date Requirement Entered:
            The date/time that the need/requirement was entered into the model. */
        }

        attribute :>> modifiedDate {
            doc /* A24 Date of Last Change:
            Date/time that the need/requirement was last changed. */
        }

        attribute approvalDate : Time::DateTime[0..1] {
            doc /* A24 Approval Date:
            Date that the current version of the need/requirement was approved. */
        }

        attribute comments : ScalarValues::String[0..1] {
            doc /* A39 Additional Coments:
            Generic comments field, for example to document status/actions/notes. */
        }

        // Attributes related to implementation of the need/requirement

        attribute priority : ReqPriority[0..1] {
            doc /* A34 Priority:
            Indicating how important it is to the success of the system that the need/requirement is satisfied. */
        }

        part implementationResponsible : ExtendedStakeholder[0..1] {
            doc /* A27 Responsible Person:
            Person (name, position/title, or role) responsible for ensuring
            that the need/requirement is implemented in the design
            and that it is verified/validated against the system. */
        }

        attribute implementationStatus : ReqImplementationStatus default ReqImplementationStatus::UnderReview {
            doc /* A31 Status of Implementation:
            Records the status of implementation of the need or requirement.
            For a need, this refers to the transformation into one or more requirements.
            For requirements, this refers to the implementation/realization in the system design. */
        }

        // Attributes related to risk

        attribute stability : ReqStability[0..1] {
            doc /* A26 Stability/Volatility:
            Indication of the likelihood that the need/requirement will change.
            For example, numerical values might be best guesses and subject to change later,
            or the number is not agreed upon by all relevant stakeholders. */
        }

        item implementationRisk : RiskMetadata::Risk[0..1] {
            doc /* A36 Risk of Implementation:
            An indication of the risk of the need/requirement not being satisfied. */
        }

        attribute riskMitigation : ScalarValues::String[0..1] {
            doc /* A37 Risk Mitigation:
            Description of how the implementation risk is or will be mitigated. */
        }

        // Attributes related to verification/validation of the system against the need/requirement

        attribute verificationMethod : ScalarValues::String[0..1] {
            doc /* A8 System Verification or Validation Method:
            The method to be used to provide objective evidence,
            for example testing, demonstration, or analysis. */
        }

        part verificationResponsible : ExtendedStakeholder[0..1] {
            doc /* A9 System Verification or Validation Responsible Organization:
            The organization responsible for performing the verification and validation of the need/requirement. */
        }

        attribute verificationStatus : VVStatus default VVStatus::NotStarted {
            doc /* A13+A14 System Verification or Validation Results/Status:
            Status and results of the V&V activity (e.g. not started, started, completed, signed-off, unsuccessful, failed, passed). */
        }

    }

    occurrence def ProvenanceAttributes {
        
        part owner : ExtendedStakeholder[0..1] {
            doc /* 
            Generic person or organization definition, who maintains the element,
            has the right to say something about it, approves changes to it, and reports the status.
            The owner preferably should be someone (or a position / job title) actively working on the project.
            */
        }

        part createdBy : ExtendedStakeholder[0..1] {
            doc /*
            The person defining and entering an element into the model.
            */
        }

        attribute createdDate : Time::DateTime[0..1] {
            doc /* 
            The date/time that the element was entered into the model.
            */
        }

        attribute modifiedDate : Time::DateTime[0..1] {
            doc /*
            Date/time that the element was last changed.
            */
        }

    }

    enum def VVStatus {
        doc /* Verification and validation status */
        NotStarted {
            doc /* V&V not started yet */
        }
        InProgress {
            doc /* V&V was started, however is not completed yet */
        }
        Completed {
            doc /* V&V was completed and results were approved (if necessary) */
        }
        CompletedUnsuccessful {
            doc /* V&V was started, however could not be completed */
        }
        CompletedFailed {
            doc /* V&V was completed, however was not successful  */
        }
        CompletedPassed {
            doc /* V&V was completed and was successful  */
        }
    }

    enum def ReqStability {
        doc /* Requirement stability/volatiliy status */
        Stable {
            doc /* Not likely to change */
        }
        LikelyToChange {
            doc /* Likely to change */
        }
        Incomplete {
            doc /* Not completely defined yet and therefore likely to change */
        }
        ToBeResolved {
            doc /* Some issue should be resolved before the need/requirement can be applied */
        }
    }

    enum def ReqStatus {
        doc /* Need/requirement maturity status */
        Draft {
            doc /* Initial version of the requirement */
        }
        InDevelopment {
            doc /* Development of statement/values/attributes is in progress */
        }
        ReadyForReview {
            doc /* Development has been completed */
        }
        InReview {
            doc /* Need/requirement is in the process of being verified and validated
            (note: requirement V&V means checking against desired characteristics, not V&V of the system!) */
        }
        Approved {
            doc /* Need/requirement has been verified and validated */
        }
        Disapproved {
            doc /* Review process has completed, however the need/requirement has not been verified and/or validated */
        }
        Deleted {
            doc /* Need/requirement has been deleted from the database */
        }
    }

    enum def ReqImplementationStatus {
        doc /* Need/status implementation status */
        UnderReview {
            doc /* Decision whether to implement is under review */
        }
        Waived {
            doc /* No plans to implement */
        }
        Pending {
            doc /* Implementation planned, not started yet */
        }
        InProgress {
            doc /* Implementation has started, not finished yet */
        }
        Implemented {
            doc /* Implementation has finished successfully */
        }
        PartiallyImplemented {
            doc /* Implementation has finished, however was only partially successful */
        }
        NotImplemented {
            doc /* Implementation has finished successfully */
        }
    }

    enum def ReqPriority {
        doc /* Priority levels for needs/requirements, based on MoSCoW categories. */
        MustHave {
            doc /* The system is perceived as a FAILURE if this need IS NOT satisfied */
        }
        ShouldHave {
            doc /* The system is percieved (significantly) WORSE than expected if this need IS NOT satisfied */
        }
        CouldHave {
            doc /* The system is perceived (significantly) BETTER than expected if this need IS satisfied */
        }
    }

}