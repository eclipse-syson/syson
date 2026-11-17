// ===================================================================
// FIBO_BE_SoleProprietors.sysml
// CI Aggregator (public re-export of SoleProprietors)
// Paste this whole file as-is.
// ===================================================================
package FIBO_BE_SoleProprietors {
  doc/* Configuration Item (CI) package for FIBO_BE_SoleProprietors and its exported model elements. */
  public import SoleProprietors::*;
}

// ======================
// SoleProprietors.sysml
// ======================
package SoleProprietors {

  // ---- Imports (private) ----
  private import ScalarValues::*;                 // String, Integer, Real, Boolean
  private import Time::*;                         // Date, DateTime, Duration
  private import Parties::*;                      // Party (natural person proprietor)
  private import Organizations::*;                // Organization (for counterparties/registrars)
  private import FormalOrganizations::*;          // FormalOrganization (registries, tax authorities)
  private import Addresses::*;                    // PostalAddress
  private import Locations::*;                    // GeopoliticalEntity
  private import JurisdictionPackage::*;          // Jurisdiction
  private import LegalCorePackage::*;             // Law, LegalInstrument
  private import Documents::*;                    // Document, Text
  private import ProductsAndServices::*;          // (optional) for offered products/services
  private import ClientsAndAccounts::*;           // Account

  // ---- Minimal local aliases to shared model types ----
  item def BankAccount specializes ClientsAndAccounts::Account { }

  // ---- Enums / code lists ----
  enum def RegistrationStatus { enum Pending; enum Active; enum Suspended; enum Cancelled; enum Lapsed; }
  enum def LicenseStatus { enum Applied; enum Granted; enum Suspended; enum Revoked; enum Expired; }
  enum def BusinessClosureReason { enum Voluntary; enum Insolvency; enum CourtOrder; enum Transfer; enum Other; }
  enum def TaxIdType { enum VAT; enum GST; enum SalesTax; enum IncomeTax; enum EIN; enum Other; }

  // ---- Core actors (parts) ----

  part def SoleProprietor {
    doc/* natural person who owns and operates a business as an unincorporated sole proprietorship */
    attribute person: Parties::Party[1..1];
    attribute primaryResidence: Addresses::PostalAddress[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute contactEmail: String[0..1];
    attribute contactPhone: String[0..1];
  }

  part def SoleProprietorship {
    doc/* unincorporated business owned and controlled by a single natural person, not legally distinct from the owner */
    attribute proprietor: SoleProprietor[1..1];
    attribute businessName: String[1..1];                 // trading/DBA name
    attribute legalName: String[0..1];                    // often same as proprietor name
    attribute tradingAs: String[0..*];                    // additional DBAs
    attribute principalPlaceOfBusiness: Addresses::PostalAddress[0..1];
    attribute mailingAddress: Addresses::PostalAddress[0..1];
    attribute website: String[0..1];
    attribute description: String[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute dateEstablished: Date[0..1];
    attribute dateClosed: Date[0..1];
    attribute unlimitedLiability: Boolean[0..1];          // typically true
    attribute registration: BusinessRegistration[0..1];
    attribute license: BusinessLicense[0..*];
    attribute taxIdentifier: TaxIdentifier[0..*];
    attribute bankAccount: BankAccount[0..*];
    attribute offeredProduct: ProductsAndServices::Product[0..*];
    attribute offeredService: ProductsAndServices::Service[0..*];
  }

  // ---- Registration & compliance (items) ----

  item def BusinessRegistration {
    doc/* official recording of a sole proprietorship/DBA with a registry or authority */
    attribute registeredName: String[1..1];
    attribute registrationNumber: String[0..1];
    attribute registeringAuthority: FormalOrganization[0..1];
    attribute registrationDate: Date[0..1];
    attribute status: RegistrationStatus[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute certificateDocument: Document[0..1];
  }

  item def BusinessLicense {
    doc/* license or permit required to operate a business activity in a jurisdiction */
    attribute licenseType: String[1..1];
    attribute issuingAuthority: FormalOrganization[0..1];
    attribute licenseNumber: String[0..1];
    attribute issueDate: Date[0..1];
    attribute expiryDate: Date[0..1];
    attribute status: LicenseStatus[0..1];
    attribute basisLaw: LegalCorePackage::Law[0..*];
    attribute licenseDocument: Document[0..1];
  }

  item def TaxIdentifier {
    doc/* identifier assigned by a tax authority for reporting and compliance */
    attribute idType: TaxIdType[1..1];
    attribute identifierValue: String[1..1];
    attribute issuingAuthority: FormalOrganization[0..1];
    attribute issueDate: Date[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  // ---- Governance artifacts (items) ----

  item def TradingNameStatement {
    doc/* statement or filing declaring a trading-as / DBA name */
    attribute business: SoleProprietorship[1..1];
    attribute tradingName: String[1..1];
    attribute filingDate: Date[0..1];
    attribute filingAuthority: FormalOrganization[0..1];
    attribute document: Document[0..1];
  }

  item def PolicyDocument {
    doc/* internal policy adopted by the sole proprietorship (e.g., data protection) */
    attribute title: String[1..1];
    attribute text: Text[0..*];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
  }

  // ---- Events & lifecycle (items) ----

  item def BusinessStartEvent {
    doc/* event recording commencement of trading operations */
    attribute business: SoleProprietorship[1..1];
    attribute startDate: Date[1..1];
    attribute registration: BusinessRegistration[0..1];
    attribute initialLicense: BusinessLicense[0..*];
  }

  item def BusinessClosureEvent {
    doc/* event recording cessation of business operations */
    attribute business: SoleProprietorship[1..1];
    attribute closureDate: Date[1..1];
    attribute reason: BusinessClosureReason[0..1];
    attribute noticeDocument: Document[0..1];
  }

  item def ChangeOfBusinessNameEvent {
    doc/* event recording a change to the registered or trading name */
    attribute business: SoleProprietorship[1..1];
    attribute previousName: String[1..1];
    attribute newName: String[1..1];
    attribute effectiveDate: Date[1..1];
    attribute filing: TradingNameStatement[0..1];
  }

  item def AddressChangeEvent {
    doc/* event recording change of principal or mailing address */
    attribute business: SoleProprietorship[1..1];
    attribute previousAddress: Addresses::PostalAddress[0..1];
    attribute newAddress: Addresses::PostalAddress[1..1];
    attribute effectiveDate: Date[1..1];
  }

  // ---- Counterparty relationships (items) ----

  item def SupplierRelationship {
    doc/* relationship identifying a supplier to the sole proprietorship */
    attribute business: SoleProprietorship[1..1];
    attribute supplier: Organizations::Organization[1..1];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
    attribute termsDocument: Document[0..1];
  }

  item def CustomerRelationship {
    doc/* relationship identifying a customer of the sole proprietorship */
    attribute business: SoleProprietorship[1..1];
    attribute customer: Parties::Party[0..1];
    attribute customerOrganization: Organizations::Organization[0..1];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
  }

  // ---- Risk & liability (items) ----

  item def LiabilityRecord {
    doc/* record of liabilities incurred by the sole proprietorship (owner has unlimited personal liability) */
    attribute business: SoleProprietorship[1..1];
    attribute description: String[1..1];
    attribute amount: Real[0..1];
    attribute currencyCode: String[0..1];
    attribute incurredOn: Date[0..1];
    attribute dueOn: Date[0..1];
    attribute creditor: Organizations::Organization[0..1];
    attribute basisDocument: Document[0..1];
  }

  item def InsurancePolicy {
    doc/* insurance policy obtained by the business (e.g., liability, property) */
    attribute business: SoleProprietorship[1..1];
    attribute policyNumber: String[1..1];
    attribute insurer: Organizations::Organization[1..1];
    attribute coverageDescription: String[0..1];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
    attribute policyDocument: Document[0..1];
  }
}
