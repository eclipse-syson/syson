// ===================================================================
// FiBO_BE_FunctionalEntities.sysml
// CI Aggregator (public re-export of FunctionalEntities + Publishers)
// Paste this whole file as-is.
// ===================================================================
package FIBO_BE_FunctionalEntities {
  doc/* Configuration Item (CI) package for FIBO_BE_FunctionalEntities and its exported model elements. */
  public import FunctionalEntities::*;
  public import Publishers::*;
}

// ======================
// FunctionalEntities.sysml
// ======================
package FunctionalEntities {

  // ---- Imports (private) ----
  private import ScalarValues::*;                 // String, Integer, Real, Boolean
  private import Time::*;                         // Date, DateTime
  private import Organizations::*;                // Organization, BusinessUnit
  private import FormalOrganizations::*;          // FormalOrganization
  private import Parties::*;                      // Party (for people in roles if referenced)
  private import JurisdictionPackage::*;          // Jurisdiction (FND Law)
  private import LegalCorePackage::*;             // Law (if constraints/mandates referenced)
  private import Addresses::*;                    // PostalAddress
  private import Locations::*;                    // GeopoliticalEntity
  private import Documents::*;                    // Document, Text

  // ---- Core abstractions ----

  part def FunctionalEntity specializes FormalOrganization {
    doc/* organizational entity defined by its function (purpose, mandate, or operating role) rather than solely by legal form */
    attribute registeredOffice: Addresses::PostalAddress[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..*];
    attribute website: String[0..*];
  }

  part def Enterprise specializes FunctionalEntity {
    doc/* functional organizational entity engaged in economic activity, possibly comprising multiple operating units */
    attribute lineOfBusiness: String[0..*];
  }

  part def OperatingUnit specializes FunctionalEntity {
    doc/* operational subdivision of an enterprise that performs a distinct function (e.g., treasury, HR, research) */
    attribute parentEnterprise: Enterprise[0..1];
    attribute functionDescription: String[0..1];
  }

  part def Division specializes OperatingUnit {
    doc/* operating unit grouping related business capabilities under common management */
  }

  part def BusinessLine specializes OperatingUnit {
    doc/* unit organized around a product/service family or customer segment */
  }

  part def NonProfitEntity specializes FunctionalEntity {
    doc/* organization established for public or mutual benefit, without profit distribution to members or leaders */
    attribute charitablePurposeText: String[0..1];
  }

  part def GovernmentBody specializes FunctionalEntity {
    doc/* governmental organization that exercises public authority within a jurisdiction */
    attribute governmentLevel: String[0..1];           // e.g., federal/state/local
    attribute competentJurisdiction: Jurisdiction[0..1];
  }

  part def SelfRegulatoryOrganization specializes FunctionalEntity {
    doc/* membership-based organization with delegated authority to create and enforce industry standards */
    attribute mandateDocument: Document[0..1];
  }

  // ---- Roles frequently assumed by functional entities ----

  item def FunctionalRole {
    doc/* named capability or responsibility performed by a functional entity */
    attribute roleName: String[1..1];
    attribute description: String[0..1];
    attribute mandatedBy: LegalCorePackage::Law[0..*];
  }

  part def RoleAssignment {
    doc/* assignment of a role to a functional entity (and optionally to a person acting on its behalf) */
    attribute role: FunctionalRole[1..1];
    attribute assigneeEntity: FunctionalEntity[1..1];
    attribute actingPerson: Parties::Party[0..1];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
    attribute referenceDocument: Document[0..1];
  }
}

// ======================
// Publishers.sysml
// ======================
package Publishers {

  // ---- Imports (private) ----
  private import ScalarValues::*;                 // String, Integer, Real, Boolean
  private import Time::*;                         // Date, DateTime
  private import Documents::*;                    // Document, Text
  private import Locations::*;                    // GeopoliticalEntity
  private import Addresses::*;                    // PostalAddress
  private import FunctionalEntities::*;           // FunctionalEntity (base)
  private import JurisdictionPackage::*;          // Jurisdiction
  private import LegalCorePackage::*;             // Law (copyright, legal basis)

  // ---- Core publishing entities ----

  part def Publisher specializes FunctionalEntities::FunctionalEntity {
    doc/* organization whose primary function is to prepare, produce, and disseminate content (e.g., print, digital, data) */
    attribute imprintName: String[0..*];
    attribute contentDomain: String[0..*];       // e.g., finance, legal, news, academic
  }

  part def DataPublisher specializes Publisher {
    doc/* publisher whose primary outputs are structured datasets and/or feeds */
    attribute deliveryChannel: String[0..*];     // API, file, feed, print
    attribute updateFrequency: String[0..*];     // e.g., real-time, daily, monthly
  }

  part def PeriodicalPublisher specializes Publisher {
    doc/* publisher focused on serial issues (journals, magazines, bulletins) */
    attribute seriesTitle: String[0..1];
    attribute issn: String[0..1];
  }

  // ---- Publications and datasets ----

  enum def PublicationKind {
    enum Report;
    enum PeriodicalIssue;
    enum Book;
    enum Dataset;
    enum WebContent;
    enum Documentation;
    enum Other;
  }

  item def PublicationIdentifier {
    doc/* identifier that uniquely references a publication within some scheme */
    attribute scheme: String[1..1];              // e.g., DOI, ISSN, ISBN, internal id
    attribute value: String[1..1];
    attribute assigningAuthority: String[0..1];
    attribute assignedOn: Date[0..1];
  }

  item def Publication {
    doc/* released unit of published content, which may be textual, tabular, audiovisual, or data */
    attribute title: String[1..1];
    attribute kind: PublicationKind[1..1];
    attribute publisher: Publisher[1..1];
    attribute publicationDate: Date[0..1];
    attribute edition: String[0..1];
    attribute publicationLanguage: String[0..1];
    attribute geography: Locations::GeopoliticalEntity[0..*];
    attribute identifier: PublicationIdentifier[0..*];
    attribute document: Document[0..1];
    attribute abstractText: String[0..1];
    attribute copyrightNotice: String[0..1];
  }

  item def PeriodicalIssue specializes Publication {
    doc/* one issue within a serial/periodical series */
    attribute volume: String[0..1];
    attribute issueNumber: String[0..1];
    attribute issueDate: Date[0..1];
  }

  item def DataFeed specializes Publication {
    doc/* recurrently updated data publication delivered in discrete refreshes */
    attribute refreshInterval: String[0..1];     // e.g., T+0 intraday, daily EOD
    attribute schemaReference: Document[0..1];
  }

  item def DatasetRelease specializes Publication {
    doc/* snapshot or vintage of a dataset at a particular publication time */
    attribute asOfDateTime: DateTime[0..1];
    attribute replacesIdentifier: PublicationIdentifier[0..1];
  }

  // ---- Editorial & rights management ----

  item def EditorialPolicy {
    doc/* statement of editorial standards, review procedures, and corrections policy */
    attribute policyDocument: Document[0..1];
    attribute lastUpdated: Date[0..1];
  }

  item def RightsAndLicensing {
    doc/* licensing and usage rights attached to a publication */
    attribute licenseName: String[0..1];         // e.g., CC BY, proprietary
    attribute licenseText: Text[0..*];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
  }

  item def PublishingAgreement {
    doc/* agreement setting terms between a publisher and a content owner or author */
    attribute publisher: Publisher[1..1];
    attribute counterpartyName: String[1..1];
    attribute agreementDate: Date[0..1];
    attribute jurisdiction: Jurisdiction[0..1];
    attribute rights: RightsAndLicensing[0..*];
    attribute document: Document[0..1];
  }

  // ---- Provenance & corrections ----

  item def PublicationProvenance {
    doc/* lineage information describing the origin, transformations, and validations applied to content */
    attribute publication: Publication[1..1];
    attribute sourceDescription: String[0..*];
    attribute processingNotes: String[0..*];
    attribute validationNotes: String[0..*];
  }

  item def Erratum {
    doc/* published correction that amends previously released content */
    attribute relatesTo: Publication[1..1];
    attribute erratumDate: Date[1..1];
    attribute description: String[1..1];
    attribute replacement: Publication[0..1];
  }

  // ---- Operational metadata ----

  item def DistributionChannel {
    doc/* mechanism and endpoint by which a publication is delivered to consumers */
    attribute name: String[1..1];               // e.g., â€œWebsiteâ€, â€œAPI v2â€, â€œEmailâ€
    attribute endpoint: String[0..1];
    attribute contactEmail: String[0..1];
    attribute availabilityWindow: String[0..1];
  }

  item def PublicationLifecycle {
    doc/* stages of publication from drafting to archival */
    attribute publication: Publication[1..1];
    attribute draftDate: Date[0..1];
    attribute reviewDate: Date[0..1];
    attribute releaseDate: Date[0..1];
    attribute revisedOn: Date[0..*];
    attribute retiredOn: Date[0..1];
  }
}
