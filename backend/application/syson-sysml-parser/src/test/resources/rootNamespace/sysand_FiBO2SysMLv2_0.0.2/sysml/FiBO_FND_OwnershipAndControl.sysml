// ===================================================================
// FIBO_FND_OwnershipAndControl.sysml
// CI Aggregator (public re-export of subpackages)
// ===================================================================
package FIBO_FND_OwnershipAndControl {
  doc/* Configuration Item (CI) package for FIBO_FND_OwnershipAndControl and its exported model elements. */
  public import Ownership::*;
  public import Control::*;
  public import OwnershipAndControl::*;
}

// ======================
// Ownership.sysml
// (feature-based from Ownership.rdf)
// ======================
package Ownership {

  // ---- Imports (private) ----
  private import ScalarValues::*;      // String, Boolean, Integer, Real
  private import Time::*;              // Date
  private import Organizations::*;     // Organization, FormalOrganization (if CI loaded)
  private import FormalOrganizations::*; // FormalOrganization (if CI loaded)
  private import Parties::*;         // Party (if CI loaded)
  private import People::*;          // Person (if CI loaded)


  // ---- Minimal local bases (safe until wired fully) ----
  item def Asset {
    attribute assetName: String[0..1];
    attribute assetDescription: String[0..1];
  }
  item def TangibleAsset specializes Asset { }
  item def IntangibleAsset specializes Asset { }


  // ===== Core concepts =====

  item def Ownership {
    doc/* legal or equitable right to possess, use, and dispose of something */
    attribute owner: Owner[1..*];
    attribute ownedThing: Asset[1..*];
    attribute acquisitionDate: Date[0..1];
    attribute terminationDate: Date[0..1];
    attribute notes: String[0..*];
  }

  item def OwnershipInterest {
    doc/* bundle of rights held by an owner in some asset or entity */
    attribute percent: Real[0..1];           // percentage if applicable (0..100)
    attribute votingPercent: Real[0..1];     // percent of voting rights
    attribute economicPercent: Real[0..1];   // percent of economic interest
    attribute effectiveDate: Date[0..1];
  }

  item def EquityOwnership specializes Ownership {
    doc/* ownership of an equity interest in an entity, such as shares in a company */
    attribute issuer: FormalOrganization[0..1];
    attribute interest: OwnershipInterest[0..*];
  }

  item def Shareholding specializes EquityOwnership {
    doc/* equity ownership represented as shares in a company */
    attribute numberOfShares: Integer[0..1];
    attribute shareClass: String[0..1];
  }

  // ===== Owners and roles =====

  item def Owner {
    doc/* party that holds ownership rights in something */
    attribute party: Party[1..1];
    attribute interest: OwnershipInterest[0..*];
  }

  item def CoOwner specializes Owner {
    doc/* owner who shares ownership with one or more other owners */
  }

  item def BeneficialOwner specializes Owner {
    doc/* natural person or party who ultimately enjoys the benefits of ownership, even if title is in another name */
  }

  item def UltimateBeneficialOwner specializes BeneficialOwner {
    doc/* natural person who ultimately owns or controls a customer and/or the person on whose behalf a transaction is being conducted */
  }

  item def NomineeOwner specializes Owner {
    doc/* party in whose name assets are registered, held on behalf of the beneficial owner */
  }

  // ===== Relationships =====

  item def JointOwnership specializes Ownership {
    doc/* ownership held simultaneously by two or more parties */
    attribute jointOwners: Owner[2..*];
  }

  item def SoleOwnership specializes Ownership {
    doc/* ownership held by a single party */
    attribute singleOwner: Owner[1..1];
  }

  item def IndirectOwnership specializes Ownership {
    doc/* ownership realized through one or more intermediary entities rather than directly in the owned thing */
    attribute throughEntity: FormalOrganization[1..*];
    attribute lookThroughPercent: Real[0..1];
  }

  item def ControllingInterest {
    doc/* ownership interest sufficient to control an entity's decisions (by law, contract, or practice) */
    attribute thresholdPercent: Real[0..1];       // e.g., >50% or jurisdiction-specific
    attribute achievedBy: OwnershipInterest[0..*];
    attribute inEntity: FormalOrganization[1..1];
  }

  item def MinorityInterestItem specializes OwnershipInterest {
    doc/* minority (non-controlling) ownership interest in an entity */
  }
}

// ======================
// Control.sysml
// (feature-based from Control.rdf)
// ======================
package Control {

  // ---- Imports (private) ----
  private import ScalarValues::*;        // String, Boolean, Real
  private import Time::*;                // Date
  private import Organizations::*;       // FormalOrganization (if CI loaded)
  private import JurisdictionPackage::*;        // Jurisdiction (from Law CI)
  private import LegalCorePackage::*;           // Law (from Law CI)
  private import Organizations::*;     // Organization, FormalOrganization (if CI loaded)
  private import FormalOrganizations::*; // FormalOrganization (if CI loaded)
  private import Parties::*;         // Party (if CI loaded)

  // ===== Enums =====

  enum def ControlMechanism {
    doc/* primary basis for control */
    enum VotingRights;
    enum BoardAppointmentRights;
    enum ContractualRights;
    enum ShareholdingThreshold;
    enum GoldenShare;
    enum DeFactoControl;
    enum Other;
  }

  // ===== Core concepts =====

  item def ControlRelationship {
    doc/* relationship in which one party has the power to direct the policies or management of another party or entity */
    attribute controller: Controller[1..1];
    attribute controlled: ControlledEntity[1..1];
    attribute mechanism: ControlMechanism[0..*];
    attribute legalBasis: LegalCorePackage::Law[0..*];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute effectiveDate: Date[0..1];
    attribute endDate: Date[0..1];
    attribute description: String[0..1];
  }

  item def Controller {
    doc/* party that has power to direct the policies or management of another party or entity */
    attribute party: Party[1..1];
  }

  item def ControlledEntity {
    doc/* party or entity whose policies or management may be directed by another */
    attribute party: Party[1..1];
  }

  item def SignificantInfluence {
    doc/* power to participate in the financial and operating policy decisions of the investee but not control or joint control of those policies */
    attribute influencer: Party[1..1];
    attribute influenced: Party[1..1];
    attribute basis: String[0..1];         // e.g., representation on board, policy-making participation
  }

  item def VotingControl specializes ControlRelationship {
    doc/* control based primarily on voting rights */
    attribute votingPercent: Real[0..1];
  }

  item def ManagementControl specializes ControlRelationship {
    doc/* control based on power to appoint or remove the majority of the board or key management personnel */
    attribute boardAppointmentRightCount: Integer[0..1];
  }

  item def ContractualControl specializes ControlRelationship {
    doc/* control achieved via contractual provisions rather than equity ownership */
    attribute contractReference: String[0..1];
  }

  item def JointControl specializes ControlRelationship {
    doc/* contractually agreed sharing of control, which exists only when decisions about the relevant activities require the unanimous consent of the parties sharing control */
    attribute jointControllers: Controller[2..*];
  }

  item def UltimateParentRelationship specializes ControlRelationship {
    doc/* relationship identifying the highest controlling entity in a chain of control */
    attribute ultimateParent: FormalOrganization[1..1];
  }

  item def ControlThreshold {
    doc/* minimum measure (e.g., percentage) used to presume control under a given legal framework */
    attribute thresholdPercent: Real[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute basisLaw: LegalCorePackage::Law[0..1];
  }
}

// ======================
// OwnershipAndControl.sysml
// (feature-based from OwnershipAndControl.rdf)
// ======================
package OwnershipAndControl {

  // ---- Imports (private) ----
  private import ScalarValues::*;          // String, Boolean, Real, Integer
  private import Time::*;                  // Date
  private import Organizations::*;         // Organizations CI (for FormalOrganization)
  private import Ownership::*;             // Ownership types
  private import Control::*;               // Control relationships
  private import JurisdictionPackage::*;          // Jurisdiction
  private import LegalCorePackage::*;             // Law
  public import FIBO_FND_Organizations::*;     // Organization, FormalOrganization (if CI loaded)

  // ===== Integrated concepts =====

  item def OwnershipControlProfile {
    doc/* consolidated view of ownership interests and control relationships for a given entity or group */
    attribute subjectEntity: FormalOrganization[1..1];
    attribute directOwners: Owner[0..*];
    attribute indirectOwners: Owner[0..*];
    attribute beneficialOwners: BeneficialOwner[0..*];
    attribute controlRelationships: ControlRelationship[0..*];
    attribute controllingInterests: ControllingInterest[0..*];
    attribute ultimateParent: FormalOrganization[0..1];
    attribute asOfDate: Date[0..1];
    attribute jurisdiction: Jurisdiction[0..1];
  }

  item def ControlChain {
    doc/* path of ownership and control from a subject entity to one or more ultimate controlling parties */
    attribute subjectEntity: FormalOrganization[1..1];
    attribute chainElement: ChainElement[1..*];
  }

  item def ChainElement {
    doc/* one link in a control chain, documenting the intermediary entity and mechanism */
    attribute entity: FormalOrganization[1..1];
    attribute viaOwnership: OwnershipInterest[0..*];
    attribute viaControl: Control::ControlRelationship[0..*];
    attribute lookThroughPercent: Real[0..1];
    attribute effectiveDate: Date[0..1];
  }

  item def UltimateBeneficialOwnershipAssessment {
    doc/* assessment of whether a natural person or persons qualify as ultimate beneficial owners according to applicable rules */
    attribute assessedEntity: FormalOrganization[1..1];
    attribute identifiedUbo: UltimateBeneficialOwner[0..*];
    attribute appliedThreshold: ControlThreshold[0..1];
    attribute legalBasis: Law[0..*];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute determinationDate: Date[0..1];
    attribute rationale: String[0..1];
  }

  item def ParentSubsidiaryRelationship {
    doc/* relationship in which a parent controls a subsidiary through majority ownership, voting rights, or other mechanisms */
    attribute parent: FormalOrganization[1..1];
    attribute subsidiary: FormalOrganization[1..*];
    attribute control: ControlRelationship[0..1];
    attribute equityLink: EquityOwnership[0..*];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
  }

  item def SignificantOwnershipChange {
    doc/* event describing a change in ownership or control that crosses a disclosure threshold or is otherwise material */
    attribute affectedEntity: FormalOrganization[1..1];
    attribute previousInterest: OwnershipInterest[0..*];
    attribute newInterest: OwnershipInterest[0..*];
    attribute previousController: Controller[0..1];
    attribute newController: Controller[0..1];
    attribute changeDate: Date[0..1];
    attribute triggerDescription: String[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute legalBasis: Law[0..*];
  }
}
