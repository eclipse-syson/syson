// ===================================================================
// FIBO_FND_Parties.sysml
// CI Aggregator (public re-export)
// ===================================================================
package FIBO_FND_Parties {
  doc/* Configuration Item (CI) package for FIBO_FND_Parties and its exported model elements. */
  public import Parties::*;
}

// ======================
// Parties.sysml
// (updated: Party and specializations are parts)
// ======================
package Parties {

  // ---- Imports (private) ----
  private import ScalarValues::*;     // String, Boolean
  private import Time::*;             // Date
  private import Addresses::*;        // Address
  private import Locations::*;        // Location

  // ---- Minimal local CMNS-aligned bases (to be wired later to shared FND/Commons pkgs) ----
  item def ContextualIdentifier {     // cmns-cxtid:ContextualIdentifier
    attribute identifierValue: String[0..1];
    attribute contextNote: String[0..1];
  }
  item def ContextualName {           // cmns-cxtdsg:ContextualName
    attribute nameValue: String[0..1];
    attribute contextNote: String[0..1];
  }
  item def ContactPoint {
    doc/* electronic or physical point of contact for a party */
    attribute contactType: String[0..1];   // email, phone, URI, postal, etc.
    attribute value: String[0..1];
    attribute preferred: Boolean[0..1];
  }

  // =========================================================
  // Core concepts (as parts)
  // =========================================================

  part def Party {
    doc/* actor (person, organization, or other legal/contractual participant) that can enter into relationships, assume obligations, and exercise rights */
    attribute partyName: PartyName[0..*];
    attribute partyIdentifier: PartyIdentifier[0..*];
    attribute description: String[0..1];
    attribute domicile: Locations::Location[0..1];
    attribute principalAddress: Addresses::Address[0..1];
    attribute contactPoint: ContactPoint[0..*];
    attribute partyRole: PartyRole[0..*];              // roles this party plays
    attribute relationship: PartyRelationship[0..*];   // relationships involving this party
  }

  part def IndependentParty specializes Party {
    doc/* party that acts on its own behalf and is not dependent on another party for authority */
  }

  part def DependentParty specializes Party {
    doc/* party whose decisions or actions are subject to the will or authority of another party */
    attribute dependsOn: Party[0..*];
  }

  // =========================================================
  // Names and identifiers (contextual, value-like)
  // =========================================================

  item def PartyName specializes ContextualName {
    doc/* contextual designation by which a party is known */
  }

  item def PartyIdentifier specializes ContextualIdentifier {
    doc/* sequence of characters that uniquely identifies a party within a given context */
    attribute identifierValue: String[0..1];
    attribute issuingAuthority: String[0..1];
  }

  // =========================================================
  // Roles and assignments (remain items)
  // =========================================================

  item def PartyRole {
    doc/* function, capacity, or position that a party occupies in some context */
    attribute roleName: String[0..1];
    attribute roleDescription: String[0..1];
  }

  item def PartyRoleAssignment {
    doc/* assignment recording that a party plays a role in some context for a period of time */
    attribute party: Party[1..1];
    attribute role: PartyRole[1..1];
    attribute context: String[0..1];         // agreement, contract, organization unit, etc.
    attribute effectiveDate: Date[0..1];
    attribute endDate: Date[0..1];
    attribute note: String[0..1];
  }

  // =========================================================
  // Party-to-party relationships (remain items)
  // =========================================================

  enum def PartyRelationshipKind {
    doc/* classification of relationships that may exist between parties */
    enum ParentSubsidiary;
    enum Affiliate;
    enum Counterparty;
    enum GuarantorGuaranteed;
    enum AgentPrincipal;
    enum EmployerEmployee;
    enum CustomerSupplier;
    enum RelatedParty;
    enum Other;
  }

  item def PartyRelationship {
    doc/* association between two parties with an optional classification and time-bounds */
    attribute sourceParty: Party[1..1];
    attribute targetParty: Party[1..1];
    attribute relationshipKind: PartyRelationshipKind[0..1];
    attribute effectiveDate: Date[0..1];
    attribute endDate: Date[0..1];
    attribute description: String[0..1];
  }
}
