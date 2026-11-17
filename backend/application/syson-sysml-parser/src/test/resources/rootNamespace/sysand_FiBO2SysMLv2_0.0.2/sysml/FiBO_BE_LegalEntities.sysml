// ===================================================================
// FIBO_BE_LegalEntities.sysml
// CI Aggregator (public re-export of LegalEntities subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_BE_LegalEntities {
  doc/* Configuration Item (CI) package for FIBO_BE_LegalEntities and its exported model elements. */
  public import LegalPersons::*;
  public import LEIEntities::*;
  public import CorporateBodies::*;
  public import FormalBusinessOrganizations::*;
}

// ======================
// LegalPersons.sysml
// ======================
package LegalPersons {

  // ---- Imports (private) ----
  private import ScalarValues::*;                // String, Integer, Real, Boolean
  private import Time::*;                        // Date, DateTime
  private import Organizations::*;               // Organization
  private import FormalOrganizations::*;         // FormalOrganization
  private import JurisdictionPackage::*;         // Jurisdiction
  private import LegalCorePackage::*;            // Law, LegalInstrument
  private import Documents::*;                   // Document, Text
  private import Parties::*;                     // Party
  private import Addresses::*;                   // PostalAddress
  private import Locations::*;                   // GeopoliticalEntity

  // ---- Enums ----
  enum def LegalPersonCategory {
    doc/* broad categories of legal persons under law */
    enum Natural;            // human individuals
    enum Juridical;          // corporate or incorporated entities
    enum Unincorporated;     // partnerships, associations
    enum Governmental;       // public entities
    enum Trust;              // fiduciary structures
    enum Other;
  }

  enum def LegalStatusKind { enum Active; enum Dissolved; enum Liquidation; enum Suspended; enum Merged; }

  // ---- Core abstraction ----

  part def LegalPerson specializes FormalOrganization {
    doc/* entity recognized as having legal rights and obligations under the law of a jurisdiction */
    attribute alternateName: String[0..*];
    attribute category: LegalPersonCategory[1..1];
    attribute dateEstablished: Date[0..1];
    attribute dateTerminated: Date[0..1];
    attribute status: LegalStatusKind[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute registrationNumber: String[0..1];
    attribute registrationAuthority: Organizations::Organization[0..1];
    attribute registrationDocument: Documents::Document[0..1];
  }

  // Subtypes
  part def JuridicalPerson specializes LegalPerson {
    doc/* legal person created by legal act or registration, distinct from natural persons composing it */
  }

  part def NaturalPersonEntity specializes LegalPerson {
    doc/* individual person with capacity to hold rights and obligations; modeled as LegalPerson when acting in law */
    attribute personRef: Parties::Party[1..1];
  }

  part def GovernmentalLegalPerson specializes LegalPerson {
    doc/* government or administrative body with legal personality */
  }

  part def TrustEntity specializes LegalPerson {
    doc/* fiduciary arrangement treated as a separate legal entity for certain purposes */
    attribute trustee: Parties::Party[0..*];
    attribute governingInstrument: LegalCorePackage::LegalInstrument[0..1];
  }

  part def PartnershipEntity specializes LegalPerson {
    doc/* unincorporated association or partnership of two or more persons carrying on a business with profit-sharing intent */
    attribute partner: Parties::Party[1..*];
    attribute partnershipAgreement: Documents::Document[0..1];
  }
}

// ======================
// LEIEntities.sysml
// ======================
package LEIEntities {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import LegalPersons::*;                // LegalPerson
  private import Organizations::*;               // Organization
  private import JurisdictionPackage::*;         // Jurisdiction
  private import Documents::*;                   // Document, Text
  private import LegalCorePackage::*;            // Law (for GLEIF governance)
  private import Locations::*;                   // GeopoliticalEntity

  // ---- Enums ----
  enum def LEIEntityCategory { enum LegalEntity; enum InternationalBranch; enum Fund; enum Umbrella; enum Other; }
  enum def LEIRegistrationStatus { enum Issued; enum Lapsed; enum Retired; enum Merged; enum Annulled; enum Duplicate; enum PendingTransfer; enum PendingArchival; enum Other; }

  // ---- Core LEI data ----
  item def LegalEntityIdentifierRecord {
    doc/* record of a registered Legal Entity Identifier (LEI) as defined by ISO 17442 and maintained by the GLEIF system */
    attribute leiCode: String[1..1];
    attribute legalName: String[1..1];
    attribute entityCategory: LEIEntityCategory[0..1];
    attribute registrationAuthorityEntityId: String[0..1];
    attribute registrationAuthorityName: String[0..1];
    attribute status: LEIRegistrationStatus[0..1];
    attribute managingLOU: Organizations::Organization[0..1];
    attribute registrationDate: Date[0..1];
    attribute lastUpdateDate: Date[0..1];
    attribute nextRenewalDate: Date[0..1];
    attribute legalFormText: String[0..1];
    attribute headquartersAddress: Addresses::PostalAddress[0..1];
    attribute legalAddress: Addresses::PostalAddress[0..1];
    attribute jurisdiction: Jurisdiction[0..1];
  }

  // ---- Parent relationships per GLEIF standard ----
  item def LEIParentRelationship {
    doc/* relationship describing ownership or consolidation hierarchy among LEI entities */
    attribute parentLeiCode: String[1..1];
    attribute childLeiCode: String[1..1];
    attribute relationshipTypeText: String[0..1];       // e.g., DIRECT_CONSOLIDATION_PARENT
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
    attribute exemptionReasonText: String[0..1];
  }

  // ---- Link between LegalPerson and LEI ----
  item def LEILink {
    doc/* assignment of an LEI record to a LegalPerson */
    attribute leiRecord: LegalEntityIdentifierRecord[1..1];
    attribute legalPerson: LegalPersons::LegalPerson[1..1];
    attribute assignedOn: Date[0..1];
  }
}

// ======================
// CorporateBodies.sysml
// ======================
package CorporateBodies {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import LegalPersons::*;                // LegalPerson
  private import JurisdictionPackage::*;         // Jurisdiction
  private import LegalCorePackage::*;            // Law, LegalInstrument
  private import Organizations::*;               // Organization
  private import Parties::*;                     // Party
  private import Documents::*;                   // Document, Text

  // ---- Enums ----
  enum def CorporateBodyType {
    doc/* generic legal types of corporate bodies across jurisdictions */
    enum Company;
    enum Corporation;
    enum Cooperative;
    enum Partnership;
    enum Foundation;
    enum Association;
    enum Society;
    enum Mutual;
    enum Trust;
    enum Other;
  }

  enum def IncorporationMode { enum ByRegistration; enum ByCharter; enum ByStatute; enum ByDecree; enum ByContract; enum Custom; }

  // ---- Corporate body abstraction ----
  part def CorporateBody specializes LegalPersons::LegalPerson {
    doc/* organization with distinct legal personality formed under corporate or other enabling law */
    attribute bodyType: CorporateBodyType[1..1];
    attribute incorporationMode: IncorporationMode[0..1];
    attribute incorporationInstrument: LegalCorePackage::LegalInstrument[0..1];
    attribute memorandumOrCharter: Documents::Document[0..1];
    attribute capacityText: String[0..1];               // capacity to contract/own property
    attribute perpetualSuccession: Boolean[0..1];
    attribute liabilityLimited: Boolean[0..1];
  }

  // ---- Typical specialized bodies ----
  part def Company specializes CorporateBody { }
  part def Cooperative specializes CorporateBody { }
  part def Foundation specializes CorporateBody { }
  part def Association specializes CorporateBody { }
  part def MutualOrganization specializes CorporateBody { }
  part def Partnership specializes CorporateBody { }
  part def CharitableOrganization specializes CorporateBody { }

  // ---- Incorporation details ----
  item def IncorporationRecord {
    doc/* record of incorporation or registration for a corporate body */
    attribute corporateBody: CorporateBody[1..1];
    attribute registryName: String[0..1];
    attribute registrationNumber: String[0..1];
    attribute incorporationDate: Date[0..1];
    attribute certificateDocument: Documents::Document[0..1];
    attribute jurisdiction: Jurisdiction[0..1];
  }

  // ---- Governance ----
  part def CorporateGovernor {
    doc/* person or entity charged with governing a corporate body (director, trustee, officer) */
    attribute person: Parties::Party[1..1];
    attribute title: String[0..1];
    attribute appointmentDate: Date[0..1];
    attribute cessationDate: Date[0..1];
    attribute legalAuthority: LegalCorePackage::Law[0..1];
  }

  item def CorporateGovernanceStructure {
    doc/* structural description of how governance is organized in a corporate body */
    attribute corporateBody: CorporateBody[1..1];
    attribute governingBodyText: String[0..1];          // e.g., Board of Directors, Council, Trustees
    attribute governor: CorporateGovernor[0..*];
  }
}

// ======================
// FormalBusinessOrganizations.sysml
// ======================
package FormalBusinessOrganizations {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CorporateBodies::*;             // CorporateBody
  private import LegalPersons::*;                // LegalPerson
  private import JurisdictionPackage::*;         // Jurisdiction
  private import LegalCorePackage::*;            // Law
  private import Documents::*;                   // Document, Text
  private import Addresses::*;                   // PostalAddress
  private import Parties::*;                     // Party (owners)
  private import Organizations::*;               // Organization
  private import Locations::*;                   // GeopoliticalEntity

  // ---- Enums ----
  enum def BusinessOrganizationType {
    doc/* common business organization forms */
    enum SoleProprietorship;
    enum Partnership;
    enum LimitedLiabilityPartnership;
    enum LimitedLiabilityCompany;
    enum Corporation;
    enum Cooperative;
    enum JointVenture;
    enum HoldingCompany;
    enum Branch;
    enum Other;
  }

  // ---- Core abstraction ----
  part def BusinessOrganization specializes CorporateBodies::CorporateBody {
    doc/* formal business entity established for commercial activity, typically registered and having distinct legal personality */
    attribute businessType: BusinessOrganizationType[1..1];
    attribute tradeName: String[0..*];
    attribute formationDate: Date[0..1];
    attribute primaryActivityText: String[0..1];
    attribute naicsCode: String[0..1];
    attribute sicCode: String[0..1];
    attribute numberOfEmployees: Integer[0..1];
    attribute totalAssetsAmount: Real[0..1];
  }

  // ---- Ownership and structure ----
  item def ShareholderRecord {
    doc/* record of ownership or shareholding in a business organization */
    attribute organization: BusinessOrganization[1..1];
    attribute shareholder: Parties::Party[1..1];
    attribute ownershipPercentage: Real[0..1];
    attribute sharesHeld: Real[0..1];
    attribute classOfShares: String[0..1];
    attribute votingRights: Boolean[0..1];
    attribute asOfDate: Date[0..1];
  }

  item def SubsidiaryRelationship {
    doc/* parent-subsidiary or control relationship between organizations */
    attribute parent: BusinessOrganization[1..1];
    attribute subsidiary: BusinessOrganization[1..1];
    attribute ownershipPercent: Real[0..1];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
  }

  // ---- Registration ----
  item def BusinessRegistration {
    doc/* record of registration of a business organization in a registry */
    attribute organization: BusinessOrganization[1..1];
    attribute registryName: String[0..1];
    attribute registrationNumber: String[0..1];
    attribute registrationDate: Date[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  // ---- Governance ----
  part def BusinessExecutive {
    doc/* person appointed to a senior management role within a business organization */
    attribute person: Parties::Party[1..1];
    attribute title: String[0..1];
    attribute appointmentDate: Date[0..1];
    attribute cessationDate: Date[0..1];
  }

  item def BusinessGovernanceStructure {
    doc/* management structure of a business organization */
    attribute organization: BusinessOrganization[1..1];
    attribute executive: BusinessExecutive[0..*];
    attribute governanceNotes: String[0..1];
  }
}
