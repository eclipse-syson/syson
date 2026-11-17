// ===================================================================
// FIBO_BE_PrivateLimitedCompanies.sysml
// CI Aggregator (public re-export of PrivateLimitedCompanies)
// Paste this whole file as-is.
// ===================================================================
package FIBO_BE_PrivateLimitedCompanies {
  doc/* Configuration Item (CI) package for FIBO_BE_PrivateLimitedCompanies and its exported model elements. */
  public import PrivateLimitedCompanies::*;
}

// ======================
// PrivateLimitedCompanies.sysml
// ======================
package PrivateLimitedCompanies {

  // ---- Imports (private) ----
  private import ScalarValues::*;                 // String, Integer, Real, Boolean
  private import Time::*;                         // Date, DateTime, Duration
  private import Organizations::*;                // Organization
  private import FormalOrganizations::*;          // FormalOrganization
  private import Parties::*;                      // Party
  private import Addresses::*;                    // PostalAddress
  private import Locations::*;                    // GeopoliticalEntity
  private import JurisdictionPackage::*;          // Jurisdiction
  private import LegalCorePackage::*;             // Law, LegalInstrument
  private import Documents::*;                    // Document, Text
  private import Ownership::*;                    // OwnershipInterest
  private import Control::*;                      // ControlRelationship
  private import Corporations::*;                 // Corporation
  private import LegalPersons::*;                 // LegalPerson

  // ---- Enums / code lists ----

  enum def PrivateCompanyType {
    doc/* jurisdiction-neutral categorization of private companies */
    enum LimitedByShares;
    enum LimitedByGuarantee;
    enum Unlimited;
    enum SingleMemberCompany;
    enum CloseCorporation;
    enum LimitedLiabilityCompany; // for LLC/PLLC analogs
    enum Other;
  }

  enum def ShareTransferRestrictionType {
    doc/* modes of restricting transfer of shares */
    enum PreEmption;
    enum BoardApproval;
    enum TagAlongDragAlong;
    enum FirstRefusal;
    enum ConsentOfMembers;
    enum LockUp;
    enum Prohibition;
    enum Other;
  }

  enum def CompanySecretaryRequirement { enum Mandatory; enum Optional; enum NotRequired; }

  enum def AuditRequirement { enum Mandatory; enum Optional; enum Exempt; }

  // ---- Core abstraction ----

  part def PrivateLimitedCompany specializes Corporations::Corporation {
    doc/* privately held company whose liability of members is limited by shares or guarantee and whose shares are not freely transferable */
    attribute companyType: PrivateCompanyType[1..1];
    attribute registrationNumber: String[0..1];
    attribute memorandum: Document[0..1];
    attribute director: Corporations::Director[1..*];
    attribute shareholder: Shareholder[1..*];
    attribute secretary: CompanySecretary[0..1];
    attribute auditor: Auditor[0..1];
    attribute shareClass: Corporations::ShareClass[0..*];
    attribute transferRestriction: ShareTransferRestriction[0..*];
  }

  // ---- Officers ----

  part def CompanySecretary {
    doc/* officer responsible for maintaining statutory registers and ensuring compliance with corporate filing obligations */
    attribute person: Parties::Party[1..1];
    attribute appointmentDate: Date[0..1];
    attribute cessationDate: Date[0..1];
    attribute compSecretaryRequirement: CompanySecretaryRequirement[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  part def Auditor {
    doc/* appointed external auditor responsible for examining and verifying financial statements */
    attribute firm: FormalOrganization[1..1];
    attribute appointmentDate: Date[0..1];
    attribute cessationDate: Date[0..1];
    attribute auditorRequirement: AuditRequirement[0..1];
  }

  // ---- Members & shares ----

  part def Shareholder {
    doc/* person or organization holding shares in the private company */
    attribute holderParty: Parties::Party[0..1];
    attribute holderOrganization: FormalOrganization[0..1];
    attribute shareClass: Corporations::ShareClass[1..1];
    attribute sharesHeld: Real[1..1];
    attribute ownershipPercent: Real[0..1];
    attribute votingPercent: Real[0..1];
    attribute shareCertificate: ShareCertificate[0..1];
  }

  item def ShareCertificate {
    doc/* document evidencing ownership of shares in the company */
    attribute certificateNumber: String[1..1];
    attribute issueDate: Date[0..1];
    attribute shareClass: Corporations::ShareClass[0..1];
    attribute numberOfShares: Real[0..1];
    attribute registeredHolder: Shareholder[0..1];
    attribute signedByDirector: Parties::Party[0..1];
  }

  // ---- Restrictions & registers ----

  item def ShareTransferRestriction {
    doc/* restriction on transfer of shares imposed by law, articles, or agreement */
    attribute restrictionType: ShareTransferRestrictionType[1..1];
    attribute description: String[0..1];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
    attribute basisDocument: Document[0..1];
  }

  item def RegisterOfMembers {
    doc/* statutory register of shareholders */
    attribute company: PrivateLimitedCompany[1..1];
    attribute registerEntry: MemberRegisterEntry[1..*];
    attribute lastUpdated: Date[0..1];
  }

  item def MemberRegisterEntry {
    doc/* entry for each shareholder showing holdings and contact details */
    attribute member: Shareholder[1..1];
    attribute address: Addresses::PostalAddress[0..1];
    attribute sharesHeld: Real[1..1];
    attribute entryDate: Date[0..1];
  }

  item def RegisterOfDirectors {
    doc/* statutory register of directors and officers */
    attribute company: PrivateLimitedCompany[1..1];
    attribute director: Corporations::Director[1..*];
    attribute secretary: CompanySecretary[0..1];
    attribute lastUpdated: Date[0..1];
  }

  // ---- Governance & meetings ----

  item def BoardResolution {
    doc/* formal resolution adopted by the board of directors */
    attribute company: PrivateLimitedCompany[1..1];
    attribute resolutionText: String[1..1];
    attribute meetingDate: Date[1..1];
    attribute document: Document[0..1];
    attribute signedBy: Parties::Party[0..*];
  }

  item def ShareholderResolution {
    doc/* resolution adopted by shareholders at general meeting */
    attribute company: PrivateLimitedCompany[1..1];
    attribute resolutionText: String[1..1];
    attribute meetingDate: Date[1..1];
    attribute document: Document[0..1];
    attribute votesFor: Real[0..1];
    attribute votesAgainst: Real[0..1];
  }

  item def AnnualGeneralMeeting {
    doc/* yearly meeting of shareholders to receive financials, elect directors, and appoint auditors */
    attribute company: PrivateLimitedCompany[1..1];
    attribute meetingDate: Date[1..1];
    attribute location: Addresses::PostalAddress[0..1];
    attribute minutesDocument: Document[0..1];
  }

  // ---- Compliance & filings ----

  item def AnnualReturn {
    doc/* statutory filing summarizing company particulars for a given year */
    attribute company: PrivateLimitedCompany[1..1];
    attribute periodCovered: DurationValue[0..1];
    attribute filingDate: Date[1..1];
    attribute registry: Organizations::Organization[0..1];
    attribute document: Document[0..1];
  }

  item def FinancialStatement {
    doc/* financial report prepared by the company (audited or unaudited) */
    attribute company: PrivateLimitedCompany[1..1];
    attribute fiscalYear: String[1..1];
    attribute issueDate: Date[0..1];
    attribute audited: Boolean[0..1];
    attribute auditor: Auditor[0..1];
    attribute document: Document[0..1];
  }

  item def CompanyFilingRecord {
    doc/* record of filing made to a corporate registry */
    attribute company: PrivateLimitedCompany[1..1];
    attribute title: String[1..1];
    attribute filingDate: Date[0..1];
    attribute registryName: String[0..1];
    attribute document: Document[0..1];
  }

  // ---- Corporate events ----

  item def ChangeOfDirectorEvent {
    doc/* event recording appointment or resignation of a director */
    attribute company: PrivateLimitedCompany[1..1];
    attribute person: Parties::Party[1..1];
    attribute changeTypeText: String[1..1];     // e.g., "Appointed", "Resigned"
    attribute effectiveDate: Date[1..1];
    attribute filingReference: CompanyFilingRecord[0..1];
  }

  item def ChangeOfShareholdingEvent {
    doc/* event capturing changes in the ownership of shares */
    attribute company: PrivateLimitedCompany[1..1];
    attribute transferFrom: Shareholder[0..1];
    attribute transferTo: Shareholder[0..1];
    attribute numberOfShares: Real[0..1];
    attribute effectiveDate: Date[1..1];
    attribute approvalDocument: Document[0..1];
  }

  item def ChangeOfRegisteredOfficeEvent {
    doc/* event where company changes its registered address */
    attribute company: PrivateLimitedCompany[1..1];
    attribute previousAddress: Addresses::PostalAddress[0..1];
    attribute newAddress: Addresses::PostalAddress[1..1];
    attribute effectiveDate: Date[1..1];
  }

  item def ChangeOfNameEvent {
    doc/* event recording a company name change */
    attribute company: PrivateLimitedCompany[1..1];
    attribute oldName: String[1..1];
    attribute newName: String[1..1];
    attribute effectiveDate: Date[1..1];
  }

  item def ChangeOfAuditorEvent {
    doc/* event recording appointment or resignation of auditor */
    attribute company: PrivateLimitedCompany[1..1];
    attribute previousAuditor: Auditor[0..1];
    attribute newAuditor: Auditor[0..1];
    attribute effectiveDate: Date[1..1];
  }

  item def CompanyDissolutionEvent {
    doc/* event marking formal dissolution of the company */
    attribute company: PrivateLimitedCompany[1..1];
    attribute dissolutionDate: Date[1..1];
    attribute basisDocument: Document[0..1];
    attribute liquidationCompleted: Boolean[0..1];
  }

  // ---- Compliance relationships ----

  item def RegistrarFilingObligation {
    doc/* statutory requirement to file specific documents with registrar */
    attribute jurisdiction: Jurisdiction[0..1];
    attribute obligationText: String[1..1];
    attribute frequencyText: String[0..1];
    attribute referenceLaw: LegalCorePackage::Law[0..1];
  }

  item def PenaltyForNonCompliance {
    doc/* penalty applicable for failure to comply with filing or governance requirements */
    attribute description: String[1..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute monetaryFineAmount: Real[0..1];
    attribute imprisonmentTermMonths: Integer[0..1];
  }
}
