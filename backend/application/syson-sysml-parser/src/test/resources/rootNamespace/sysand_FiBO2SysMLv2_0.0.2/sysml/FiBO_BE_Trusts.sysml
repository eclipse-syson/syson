// ===================================================================
// FIBO_BE_Trusts.sysml
// CI Aggregator (public re-export of Trusts)
// Paste this whole file as-is.
// ===================================================================
package FIBO_BE_Trusts {
  doc/* Configuration Item (CI) package for FIBO_BE_Trusts and its exported model elements. */
  public import Trusts::*;
}

// ======================
// Trusts.sysml
// ======================
package Trusts {

  // ---- Imports (private) ----
  private import ScalarValues::*;                 // String, Integer, Real, Boolean
  private import Time::*;                         // Date, DateTime, Duration
  private import Parties::*;                      // Party (people/orgs acting in roles)
  private import Organizations::*;                // Organization
  private import FormalOrganizations::*;          // FormalOrganization
  private import JurisdictionPackage::*;          // Jurisdiction
  private import LegalCorePackage::*;             // Law, LegalInstrument
  private import Documents::*;                    // Document, Text
  private import Addresses::*;                    // PostalAddress
  private import CurrencyAmount::*;               // MonetaryAmount (for distributions, valuations)
  private import Ownership::*;                    // OwnershipInterest (optional linkage to assets)
  private import ClientsAndAccounts::*;           // Account

  // ---- Minimal local aliases to shared model types ----
  item def BankAccount specializes ClientsAndAccounts::Account { }

  // ---- Enums / code lists ----

  enum def TrustType {
    doc/* jurisdiction-neutral categorization of trusts */
    enum Discretionary;
    enum FixedInterest;
    enum Bare;                 // nominee / simple trust
    enum UnitTrust;
    enum Charitable;
    enum Testamentary;
    enum InterVivos;           // living trust
    enum Revocable;
    enum Irrevocable;
    enum Grantor;              // tax characterization
    enum PurposeTrust;
    enum Other;
  }

  enum def DistributionMethod {
    doc/* method by which income/capital is distributed */
    enum TrusteeDiscretion;
    enum FixedProRata;
    enum PreferredReturnThenSplit;
    enum Accumulation;
    enum MandatoryIncome;
    enum Other;
  }

  enum def TrusteePowerType {
    doc/* common powers granted to trustee under deed or law */
    enum Invest;
    enum DistributeIncome;
    enum AdvanceCapital;
    enum AppointRemoveTrustees;
    enum VaryInvestments;
    enum AddRemoveBeneficiaries;
    enum AmendAdministrativeTerms;
    enum Delegate;
    enum Other;
  }

  enum def BeneficiaryType {
    enum Individual;
    enum ClassOfPersons;
    enum Charity;
    enum Company;
    enum OtherEntity;
  }

  enum def BeneficialInterestKind {
    enum IncomeInterest;
    enum CapitalInterest;
    enum RemainderInterest;
    enum ContingentInterest;
    enum VestedInterest;
    enum Revoked;
  }

  enum def EventKind {
    enum Settlement; enum AdditionOfProperty; enum Appointment; enum Removal;
    enum DistributionIncome; enum DistributionCapital; enum Variation; enum Termination;
  }

  // ---- Core trust actors (parts, they act/decide/process) ----

  part def Trustee {
    doc/* person or organization holding legal title to trust property and obligated to administer the trust in accordance with the deed and law */
    attribute party: Parties::Party[1..1];
    attribute appointedBy: LegalCorePackage::LegalInstrument[0..1];
    attribute appointmentDate: Date[0..1];
    attribute cessationDate: Date[0..1];
    attribute power: TrusteePowerType[0..*];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  part def Settlor {
    doc/* person or organization that creates (settles) a trust by transferring property to a trustee for the benefit of beneficiaries or a purpose */
    attribute party: Parties::Party[1..1];
    attribute settlementDate: Date[0..1];
    attribute settlementDocument: LegalCorePackage::LegalInstrument[0..1];
  }

  part def Protector {
    doc/* person who may have oversight powers (e.g., to appoint/remove trustees or consent to certain actions) */
    attribute party: Parties::Party[1..1];
    attribute powersText: String[0..*];
    attribute appointmentDate: Date[0..1];
    attribute cessationDate: Date[0..1];
  }

  part def Beneficiary {
    doc/* person or entity eligible to receive benefits under the trust (present or future, fixed or discretionary) */
    attribute beneficiaryType: BeneficiaryType[1..1];
    attribute party: Parties::Party[0..1];
    attribute organization: FormalOrganization[0..1];
    attribute classDescription: String[0..1];       // for class beneficiaries
    attribute interestKind: BeneficialInterestKind[0..*];
    attribute isPrimary: Boolean[0..1];
  }

  // ---- Core trust entity (part) ----

  part def Trust {
    doc/* legal relationship in which a trustee holds property for the benefit of beneficiaries or for a stated purpose in accordance with a governing law and trust instrument */
    attribute trustName: String[1..1];
    attribute trustType: TrustType[1..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute establishmentDate: Date[0..1];
    attribute terminationDate: Date[0..1];
    attribute registeredOffice: Addresses::PostalAddress[0..1];
    attribute deed: TrustDeed[0..1];
    attribute letterOfWishes: LetterOfWishes[0..*];
    attribute settlor: Settlor[1..*];
    attribute trustee: Trustee[1..*];
    attribute protector: Protector[0..*];
    attribute beneficiary: Beneficiary[1..*];
    attribute trustProperty: TrustProperty[0..*];
    attribute bankAccount: BankAccount[0..*];
    attribute distributionPolicy: DistributionPolicy[0..1];
    attribute investmentPolicy: InvestmentPolicy[0..1];
    attribute accountingRecord: TrustAccountingRecord[0..*];
  }

  // ---- Instruments & policies (items) ----

  item def TrustDeed {
    doc/* legal instrument establishing the trust, setting out its terms, powers, and parties */
    attribute title: String[0..1];
    attribute instrumentDocument: Documents::Document[0..1];
    attribute executionDate: Date[0..1];
    attribute effectiveDate: Date[0..1];
  }

  item def LetterOfWishes {
    doc/* non-binding guidance from the settlor to the trustees regarding the exercise of their discretion */
    attribute text: Text[0..*];
    attribute dateGiven: Date[0..1];
    attribute document: Documents::Document[0..1];
  }

  item def DistributionPolicy {
    doc/* policy describing how income and capital are to be distributed (fixed rules or trustee discretion) */
    attribute method: DistributionMethod[1..1];
    attribute notes: String[0..*];
  }

  item def InvestmentPolicy {
    doc/* policy governing investment of trust property (mandates, risk limits, benchmarks) */
    attribute statementText: String[0..*];
    attribute lastApproved: Date[0..1];
    attribute approvingTrustee: Trustee[0..1];
  }

  // ---- Trust property & economics (items) ----

  item def TrustProperty {
    doc/* property held by the trustee subject to the terms of the trust (legal title) for the benefit of beneficiaries (equitable title) */
    attribute asset: Asset[1..1];
    attribute legalTitleHolder: Trustee[1..1];
    attribute beneficialOwners: Beneficiary[0..*];
    attribute ownershipInterest: OwnershipInterest[0..*]; // if Ownership CI is present
    attribute acquisitionDate: Date[0..1];
    attribute acquisitionDocument: Documents::Document[0..1];
  }

  item def SettlementRecord {
    doc/* record of property initially or subsequently settled into the trust */
    attribute trust: Trust[1..1];
    attribute fromSettlor: Settlor[1..1];
    attribute property: TrustProperty[1..1];
    attribute settlementAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute settlementDate: Date[0..1];
  }

  item def DistributionRecord {
    doc/* record of income or capital distribution from the trust to a beneficiary */
    attribute trust: Trust[1..1];
    attribute beneficiary: Beneficiary[1..1];
    attribute amount: CurrencyAmount::MonetaryAmount[1..1];
    attribute distributionDate: Date[1..1];
    attribute isCapital: Boolean[0..1];
    attribute basisDocument: Documents::Document[0..1];
  }

  item def ValuationRecord {
    doc/* periodic valuation of trust property for reporting, tax, or distribution purposes */
    attribute trust: Trust[1..1];
    attribute property: TrustProperty[0..1];
    attribute valuationAmount: CurrencyAmount::MonetaryAmount[1..1];
    attribute valuationDate: Date[1..1];
    attribute appraiser: Organizations::Organization[0..1];
  }

  item def TrustAccountingRecord {
    doc/* record of income, expenses, gains/losses, and allocations for the trust */
    attribute trust: Trust[1..1];
    attribute periodStart: Date[1..1];
    attribute periodEnd: Date[1..1];
    attribute netIncomeAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute expensesAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute capitalGainsAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute preparedOn: Date[0..1];
    attribute preparedBy: Organizations::Organization[0..1];
  }

  // ---- Governance & changes (items) ----

  item def AppointmentRecord {
    doc/* appointment or removal record for trustees, protectors, or beneficiaries */
    attribute trust: Trust[1..1];
    attribute eventKind: EventKind[1..1];
    attribute subjectTrustee: Trustee[0..1];
    attribute subjectProtector: Protector[0..1];
    attribute subjectBeneficiary: Beneficiary[0..1];
    attribute effectiveDate: Date[1..1];
    attribute instrument: LegalCorePackage::LegalInstrument[0..1];
    attribute notes: String[0..*];
  }

  item def VariationRecord {
    doc/* record of a permitted variation or court-approved change to trust terms */
    attribute trust: Trust[1..1];
    attribute variationText: String[0..1];
    attribute effectiveDate: Date[0..1];
    attribute authorizingInstrument: LegalCorePackage::LegalInstrument[0..1];
  }

  item def TerminationRecord {
    doc/* record of the termination/winding up of a trust and final distributions */
    attribute trust: Trust[1..1];
    attribute terminationDate: Date[1..1];
    attribute finalDistribution: DistributionRecord[0..*];
    attribute instrument: LegalCorePackage::LegalInstrument[0..1];
  }

  // ---- Compliance & registry (items) ----

  item def TrustRegistration {
    doc/* registration of a trust in a registry (where applicable), often for AML/beneficial ownership */
    attribute trust: Trust[1..1];
    attribute registerName: String[0..1];
    attribute registrationNumber: String[0..1];
    attribute registrationDate: Date[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute filingDocument: Documents::Document[0..1];
  }

  item def BeneficialOwnershipDeclaration {
    doc/* declaration filed with an authority identifying beneficial owners/beneficiaries of a trust */
    attribute trust: Trust[1..1];
    attribute declaredBeneficiary: Beneficiary[1..*];
    attribute filingDate: Date[1..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute document: Documents::Document[0..1];
  }
}
