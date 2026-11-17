// ===================================================================
// FIBO_BE_GovernmentEntities.sysml
// CI Aggregator (public re-export of GovernmentEntities)
// Paste this whole file as-is.
// ===================================================================
package FIBO_BE_GovernmentEntities {
  doc/* Configuration Item (CI) package for FIBO_BE_GovernmentEntities and its exported model elements. */
  public import GovernmentEntities::*;
}

// ======================
// GovernmentEntities.sysml
// ======================
package GovernmentEntities {

  // ---- Imports (private) ----
  private import ScalarValues::*;                 // String, Integer, Real, Boolean
  private import Time::*;                         // Date, DateTime, Duration
  private import Organizations::*;                // Organization
  private import FormalOrganizations::*;          // FormalOrganization
  private import Parties::*;                      // Party (people acting in roles)
  private import Addresses::*;                    // PostalAddress
  private import Locations::*;                    // GeopoliticalEntity, GeographicLocation
  private import JurisdictionPackage::*;          // Jurisdiction (FND Law)
  private import LegalCorePackage::*;             // Law, LegalInstrument (FND Law)
  private import Documents::*;                    // Document, Text

  // ---- Enums / code lists ----

  enum def GovernmentLevel {
    doc/* level of government authority within a multi-level public administration */
    enum Supranational;    // e.g., EU, UN bodies
    enum National;         // central/federal
    enum State;            // state-level (federal systems)
    enum Provincial;       // province/territory
    enum Regional;         // region/county
    enum Local;            // city/district
    enum Municipal;        // municipality/town
    enum Other;
  }

  enum def GovernmentBranch {
    doc/* functional branch of government as recognized constitutionally */
    enum Executive;
    enum Legislative;
    enum Judicial;
    enum Independent;      // independent constitutional bodies
  }

  enum def AgencyKind {
    doc/* high-level functional type for a government organization */
    enum Ministry;             // department/ministry at cabinet level
    enum Department;           // sub-ministry department
    enum Agency;               // executive agency/authority
    enum Commission;           // independent commission/board
    enum Authority;            // statutory authority
    enum SupervisoryBody;      // oversight/audit office
    enum StatisticalOffice;    // official statistics
    enum RevenueAgency;        // tax/customs
    enum SocialSecurity;       // benefits/pensions
    enum Procurement;          // public procurement
    enum Other;
  }

  enum def TenureMode {
    doc/* selection mechanism for public office holders */
    enum Elected;
    enum Appointed;
    enum CivilService;
    enum Delegated;
    enum ExOfficio;
    enum Other;
  }

  // ---- Core abstractions ----

  part def GovernmentEntity specializes FormalOrganization {
    doc/* legal public organization that exercises governmental authority, provides public services, or carries out statutory functions */
    attribute officialName: String[1..1];
    attribute shortName: String[0..*];
    attribute level: GovernmentLevel[0..1];
    attribute branch: GovernmentBranch[0..1];
    attribute agencyKind: AgencyKind[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..*];
    attribute registeredOffice: Addresses::PostalAddress[0..1];
    attribute website: String[0..*];
    attribute dateEstablished: Date[0..1];
    attribute dateTerminated: Date[0..1];
  }

  // Specialized organizational forms (jurisdiction-neutral names)
  part def Ministry specializes GovernmentEntity {
    doc/* cabinet-level department of the executive branch with a defined policy portfolio */
    attribute portfolioDescription: String[0..1];
  }

  part def Department specializes GovernmentEntity {
    doc/* organizational unit within a ministry or central administration responsible for a specific function */
    attribute parentMinistry: Ministry[0..1];
  }

  part def ExecutiveAgency specializes GovernmentEntity {
    doc/* operational agency created to deliver public services or implement policy under a statutory mandate */
    attribute sponsoringMinistry: Ministry[0..1];
  }

  part def IndependentCommission specializes GovernmentEntity {
    doc/* independent body with statutory powers and procedural autonomy */
    attribute independenceBasis: String[0..1];
  }

  part def SupervisoryAuthority specializes GovernmentEntity {
    doc/* public authority with oversight, audit, or supervisory functions over other public bodies or sectors */
    attribute scopeOfSupervision: String[0..1];
  }

  part def StatisticalOffice specializes GovernmentEntity {
    doc/* national or sub-national office responsible for official statistics and economic indicators */
    attribute standardDisseminationPolicy: Document[0..1];
  }

  part def RevenueAgency specializes GovernmentEntity {
    doc/* authority responsible for tax administration, customs, or duties */
    attribute taxAdministrationMandate: String[0..1];
  }

  // ---- Governance, mandates, and instruments ----

  item def PublicMandate {
    doc/* statement of objectives and powers granted to a government entity by law or executive order */
    attribute mandateText: String[1..1];
    attribute legalBasis: LegalCorePackage::Law[0..*];
    attribute instrument: LegalCorePackage::LegalInstrument[0..*];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
    attribute appliesTo: GovernmentEntity[1..1];
  }

  item def GovernmentProgram {
    doc/* coordinated set of projects and activities run by a government entity to achieve public policy objectives */
    attribute name: String[1..1];
    attribute description: String[0..1];
    attribute sponsoringEntity: GovernmentEntity[1..1];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..*];
  }

  // ---- Office holders & roles ----

  item def PublicOffice {
    doc/* defined position within a government entity carrying specified duties, powers, and tenure conditions */
    attribute title: String[1..1];
    attribute entity: GovernmentEntity[1..1];
    attribute branch: GovernmentBranch[0..1];
    attribute tenureMode: TenureMode[0..1];
    attribute termLength: DurationValue[0..1];
    attribute maximumTerms: Integer[0..1];
    attribute description: String[0..1];
    attribute establishedBy: LegalCorePackage::Law[0..*];
  }

  part def GovernmentOfficial {
    doc/* person occupying a public office within a government entity */
    attribute person: Parties::Party[1..1];
    attribute office: PublicOffice[1..1];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
    attribute oathOrDeclarationRef: Document[0..1];
  }

  part def HeadOfEntity specializes GovernmentOfficial {
    doc/* chief executive or administrative head of a government entity (e.g., Minister, Secretary) */
  }

  part def DeputyHead specializes GovernmentOfficial {
    doc/* deputy or vice leader acting as second-in-command */
  }

  // ---- Inter-entity relationships ----

  item def SponsorshipLink {
    doc/* relationship in which one government entity sponsors or oversees another (parent-child hierarchy) */
    attribute sponsor: GovernmentEntity[1..1];
    attribute sponsored: GovernmentEntity[1..*];
    attribute basis: PublicMandate[0..*];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
  }

  item def CooperationAgreement {
    doc/* formal arrangement for cooperation, data sharing, or joint action between government entities */
    attribute party: GovernmentEntity[2..*];
    attribute agreementTitle: String[1..1];
    attribute agreementDate: Date[0..1];
    attribute legalInstrument: LegalCorePackage::LegalInstrument[0..1];
    attribute document: Document[0..1];
  }

  item def OversightRelationship {
    doc/* relationship where one entity exercises oversight over another (audit, compliance, or performance) */
    attribute overseer: GovernmentEntity[1..1];
    attribute subjectEntity: GovernmentEntity[1..1];
    attribute scope: String[0..1];
    attribute basisLaw: LegalCorePackage::Law[0..*];
  }

  // ---- Identifiers, registries, and contact points ----

  item def GovernmentEntityIdentifier {
    doc/* identifier assigned to a government entity within a registry or standard scheme */
    attribute schemeName: String[1..1];   // e.g., national registry, LEI where applicable
    attribute value: String[1..1];
    attribute assigningAuthority: Organizations::Organization[0..1];
    attribute assignedOn: Date[0..1];
  }

  item def RegistryEntry {
    doc/* registration record for a government entity in an official registry */
    attribute entity: GovernmentEntity[1..1];
    attribute registryName: String[0..1];
    attribute registrationNumber: String[0..1];
    attribute registrationDate: Date[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  item def ContactPoint {
    doc/* public contact interface for a government entity (address, phone, email, or URL) */
    attribute entity: GovernmentEntity[1..1];
    attribute address: Addresses::PostalAddress[0..1];
    attribute email: String[0..*];
    attribute phone: String[0..*];
    attribute url: String[0..*];
    attribute hours: String[0..1];
  }

  // ---- Transparency, reporting, and policies ----

  item def TransparencyPolicy {
    doc/* policy describing disclosure, open data, and access-to-information practices of a government entity */
    attribute entity: GovernmentEntity[1..1];
    attribute policyDocument: Document[0..1];
    attribute lastUpdated: Date[0..1];
  }

  item def AnnualReport {
    doc/* annual report on activities, finances, and outcomes of a government entity */
    attribute entity: GovernmentEntity[1..1];
    attribute fiscalYear: String[1..1];
    attribute publicationDate: Date[0..1];
    attribute document: Document[0..1];
  }

  item def ProgramOutcomeReport {
    doc/* report documenting outcomes and KPIs for a government program */
    attribute program: GovernmentProgram[1..1];
    attribute reportDate: Date[0..1];
    attribute document: Document[0..1];
  }
}
