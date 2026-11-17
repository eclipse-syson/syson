// ===================================================================
// FIBO_BE_OwnershipAndControl.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_BE_OwnershipAndControl {
  doc/* Configuration Item (CI) package for FIBO_BE_OwnershipAndControl and its exported model elements. */
  public import Executives::*;
  public import OwnershipParties::*;
  public import ControlParties::*;
  public import CorporateOwnership::*;
  public import CorporateControl::*;
}

// ======================
// Executives.sysml
// ======================
package Executives {

  // ---- Imports (private) ----
  private import ScalarValues::*;           // String, Integer, Real, Boolean
  private import Time::*;                   // Date, DateTime
  private import Parties::*;                // Party (people as actors)
  private import Organizations::*;          // Organization
  private import FormalOrganizations::*;    // FormalOrganization
  private import Documents::*;              // Document, Text
  private import JurisdictionPackage::*;    // Jurisdiction
  private import LegalCorePackage::*;       // Law (mandates)

  // ---- Core executive role and appointment ----

  part def Executive {
    doc/* person acting in an executive capacity within an organization (e.g., CEO, CFO, COO) */
    attribute person: Parties::Party[1..1];
    attribute organization: FormalOrganization[1..1];
    attribute title: String[1..1];                 // e.g., "Chief Financial Officer"
    attribute responsibilities: String[0..*];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute mandateLaw: LegalCorePackage::Law[0..*];
    attribute appointmentDocument: Document[0..1];
    attribute isDirector: Boolean[0..1];           // also serves on the board
  }

  part def InterimExecutive specializes Executive {
    doc/* executive appointed on an interim/acting basis */
  }

  part def NonExecutiveDirector {
    doc/* board member without day-to-day executive responsibilities */
    attribute person: Parties::Party[1..1];
    attribute organization: FormalOrganization[1..1];
    attribute committeeMembership: String[0..*];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
  }

  item def ExecutiveCommittee {
    doc/* governing committee of executives within an organization */
    attribute organization: FormalOrganization[1..1];
    attribute member: Executive[1..*];
    attribute charterDocument: Document[0..1];
    attribute establishedOn: Date[0..1];
  }
}

// ======================
// OwnershipParties.sysml
// ======================
package OwnershipParties {

  // ---- Imports (private) ----
  private import ScalarValues::*;           
  private import Time::*;                   
  private import Parties::*;                
  private import Organizations::*;          
  private import FormalOrganizations::*;    
  private import JurisdictionPackage::*;    
  private import LegalCorePackage::*;       
  private import Documents::*;              

  // ---- Ownership roles bound to parties/entities ----

  item def OwnerRole {
    doc/* role played by a party that holds ownership rights (legal or equitable) in an entity or asset */
    attribute holderParty: Parties::Party[0..1];
    attribute holderOrganization: FormalOrganization[0..1];
    attribute ownedEntity: FormalOrganization[0..1];
    attribute ownershipPercent: Real[0..1];
    attribute economicPercent: Real[0..1];
    attribute votingPercent: Real[0..1];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute basisDocument: Document[0..1];
  }

  item def BeneficialOwnerRole specializes OwnerRole {
    doc/* role for a natural person or party who ultimately enjoys the benefits of ownership */
  }

  item def UltimateBeneficialOwnerRole specializes BeneficialOwnerRole {
    doc/* natural person meeting the ultimate beneficial ownership (UBO) criteria per applicable rules */
  }

  item def NomineeOwnerRole specializes OwnerRole {
    doc/* role indicating title is held on behalf of the beneficial owner (nominee arrangement) */
  }
}

// ======================
// ControlParties.sysml
// ======================
package ControlParties {

  // ---- Imports (private) ----
  private import ScalarValues::*;           
  private import Time::*;                   
  private import Parties::*;                
  private import FormalOrganizations::*;    
  private import JurisdictionPackage::*;    
  private import LegalCorePackage::*;       

  // ---- Control roles ----

  enum def ControlMechanism {
    doc/* basis by which control is exercised */
    enum VotingRights;
    enum BoardAppointmentRights;
    enum ContractualRights;
    enum ShareholdingThreshold;
    enum GoldenShare;
    enum DeFactoControl;
    enum Other;
  }

  item def ControllerRole {
    doc/* role of a party that has power to direct policies or management of another entity */
    attribute controllerParty: Parties::Party[0..1];
    attribute controllerOrganization: FormalOrganization[0..1];
    attribute mechanism: ControlMechanism[0..*];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute basisLaw: LegalCorePackage::Law[0..*];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
  }

  item def ControlledPartyRole {
    doc/* role of a party whose policies or management may be directed by another */
    attribute subjectOrganization: FormalOrganization[1..1];
  }

  item def SignificantInfluenceRole {
    doc/* role reflecting participation in policy decisions without full control */
    attribute influencer: Parties::Party[0..1];
    attribute influencedOrganization: FormalOrganization[1..1];
    attribute basisText: String[0..1];           // e.g., board representation
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
  }
}

// ======================
// CorporateOwnership.sysml
// ======================
package CorporateOwnership {

  // ---- Imports (private) ----
  private import ScalarValues::*;           
  private import Time::*;                   
  private import Parties::*;                
  private import FormalOrganizations::*;    
  private import JurisdictionPackage::*;    
  private import LegalCorePackage::*;       
  private import Documents::*;              

  // ---- Ownership records at corporate level ----

  item def EquityOwnershipRecord {
    doc/* record capturing equity ownership in a corporate entity */
    attribute ownerParty: Parties::Party[0..1];
    attribute ownerOrganization: FormalOrganization[0..1];
    attribute issuer: FormalOrganization[1..1];
    attribute ownershipPercent: Real[0..1];
    attribute votingPercent: Real[0..1];
    attribute economicPercent: Real[0..1];
    attribute shareClass: String[0..1];
    attribute sharesHeld: Real[0..1];
    attribute asOfDate: Date[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute basisDocument: Document[0..1];
  }

  item def IndirectOwnershipRecord {
    doc/* ownership realized through one or more intermediary entities */
    attribute ultimateOwner: Parties::Party[0..1];
    attribute ultimateOwnerOrganization: FormalOrganization[0..1];
    attribute subjectEntity: FormalOrganization[1..1];
    attribute viaEntity: FormalOrganization[1..*];
    attribute lookThroughPercent: Real[0..1];
    attribute asOfDate: Date[0..1];
  }

  item def OwnershipThresholdAssessment {
    doc/* assessment whether relevant disclosure/control thresholds are met */
    attribute subjectEntity: FormalOrganization[1..1];
    attribute ownerParty: Parties::Party[0..1];
    attribute ownerOrganization: FormalOrganization[0..1];
    attribute computedPercent: Real[0..1];
    attribute thresholdPercent: Real[0..1];       // e.g., 10%, 25%, 50%
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute basisLaw: LegalCorePackage::Law[0..*];
    attribute asOfDate: Date[0..1];
    attribute conclusionText: String[0..1];
  }
}

// ======================
// CorporateControl.sysml
// ======================
package CorporateControl {

  // ---- Imports (private) ----
  private import ScalarValues::*;           
  private import Time::*;                   
  private import Parties::*;                
  private import FormalOrganizations::*;    
  private import JurisdictionPackage::*;    
  private import LegalCorePackage::*;       
  private import ControlParties::*;         
  private import CorporateOwnership::*;     

  // ---- Control relationships and structures ----

  item def ControlRelationshipRecord {
    doc/* record describing the existence and basis of control between entities */
    attribute controller: FormalOrganization[1..1];
    attribute controlled: FormalOrganization[1..1];
    attribute mechanism: ControlParties::ControlMechanism[0..*];
    attribute ownershipEvidence: CorporateOwnership::EquityOwnershipRecord[0..*];
    attribute contractualReference: String[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute basisLaw: LegalCorePackage::Law[0..*];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
    attribute notes: String[0..*];
  }

  item def UltimateParentLink {
    doc/* link identifying the highest controlling entity in a chain of control */
    attribute subjectEntity: FormalOrganization[1..1];
    attribute ultimateParent: FormalOrganization[1..1];
    attribute evidence: ControlRelationshipRecord[0..*];
    attribute asOfDate: Date[0..1];
  }

  item def ControlChain {
    doc/* ordered path of entities from a subject to its ultimate parent(s) */
    attribute subjectEntity: FormalOrganization[1..1];
    attribute element: ChainElement[1..*];
  }

  item def ChainElement {
    doc/* one link in a control chain */
    attribute entity: FormalOrganization[1..1];
    attribute viaOwnership: CorporateOwnership::EquityOwnershipRecord[0..*];
    attribute viaControl: ControlRelationshipRecord[0..*];
    attribute lookThroughPercent: Real[0..1];
    attribute effectiveFrom: Date[0..1];
  }

  item def SignificantOwnershipChangeEvent {
    doc/* event capturing a material change in ownership or control */
    attribute affectedEntity: FormalOrganization[1..1];
    attribute previousOwnerRecord: CorporateOwnership::EquityOwnershipRecord[0..*];
    attribute newOwnerRecord: CorporateOwnership::EquityOwnershipRecord[0..*];
    attribute previousControl: ControlRelationshipRecord[0..1];
    attribute newControl: ControlRelationshipRecord[0..1];
    attribute changeDate: Date[0..1];
    attribute triggerDescription: String[0..1];
  }
}
