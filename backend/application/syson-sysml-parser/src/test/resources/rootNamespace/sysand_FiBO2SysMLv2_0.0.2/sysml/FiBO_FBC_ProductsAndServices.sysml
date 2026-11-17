// ===================================================================
// FIBO_FBC_ProductsAndServices.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_FBC_ProductsAndServices {
  doc/* Configuration Item (CI) package for FIBO_FBC_ProductsAndServices and its exported model elements. */
  public import FinancialProductsAndServices::*;
  public import ClientsAndAccounts::*;
}

// ==========================================
// FinancialProductsAndServices.sysml
// (feature-based from FinancialProductsAndServices.rdf)
// ==========================================
package FinancialProductsAndServices {

  // ---- Imports (private) ----
  private import ScalarValues::*;              // String, Integer, Real, Boolean
  private import Time::*;                      // Date, DateTime
  private import CurrencyAmount::*;            // Currency, MonetaryAmount, MonetaryPrice
  private import FinancialDates::*;            // ExplicitDate, Duration, RegularSchedule
  private import Parties::*;                   // Party
  private import Organizations::*;             // Organization / FormalOrganization
  private import FinancialInstruments::*;      // FinancialInstrument (cross-link)
  private import AnnotationVocabulary::*;      // Tag / Annotation (optional)

  // ---- Enums / code lists ----
  enum def ProductCategory {
    doc/* high-level category for financial products */
    enum Account; enum Payment; enum Lending; enum Deposit; enum Investment; enum Insurance; enum Advisory; enum Other;
  }

  enum def ServiceType {
    doc/* category of financial service performed for/with a client */
    enum AccountServicing; enum Payments; enum Custody; enum Brokerage; enum PortfolioManagement;
    enum Advisory; enum Underwriting; enum MarketMaking; enum Clearing; enum Settlement; enum Reporting; enum Other;
  }

  enum def FeeType {
    enum Flat; enum PercentageOfAmount; enum BasisPoints; enum Performance; enum Subscription; enum Redemption; enum Other;
  }

  enum def EligibilityCriterionKind {
    enum Residency; enum KYCLevel; enum Accreditation; enum MinimumDeposit; enum RiskProfile; enum Age; enum Other;
  }

  // ---- Core product & service abstractions ----

  item def Product {
    doc/* packaged financial offering with defined terms, charges, and eligibility */
    attribute productId: String[1..1];
    attribute name: String[0..1];
    attribute category: ProductCategory[1..1];
    attribute description: String[0..1];
    attribute offeredBy: Organizations::Organization[0..1];
    attribute currency: Currency[0..*];
    attribute launchDate: Date[0..1];
    attribute discontinuedDate: Date[0..1];
    attribute linkedInstrument: FinancialInstruments::FinancialInstrument[0..*];
    attribute tag: AnnotationVocabulary::Tag[0..*];
  }

  item def Service {
    doc/* activity performed by a provider for a client, governed by service terms */
    attribute serviceId: String[1..1];
    attribute serviceType: ServiceType[1..1];
    attribute description: String[0..1];
    attribute providedBy: Organizations::Organization[0..1];
    attribute supportedProduct: Product[0..*];
  }

  item def FeeSchedule {
    doc/* schedule describing how fees are calculated and charged */
    attribute feeType: FeeType[1..1];
    attribute amount: MonetaryAmount[0..1];
    attribute ratePct: Real[0..1];                  // 0..1 for percentage representation
    attribute accrualFrequencyText: String[0..1];   // e.g., Monthly, Quarterly
    attribute chargeTimingText: String[0..1];       // InAdvance, InArrears, EventDriven
    attribute validFrom: Date[0..1];
    attribute validTo: Date[0..1];
  }

  item def EligibilityCriterion {
    doc/* condition that must be satisfied to access a product or service */
    attribute kind: EligibilityCriterionKind[1..1];
    attribute criterionText: String[1..1];
  }

  item def TermsAndConditions {
    doc/* narrative and structured parameters governing a product/service */
    attribute summaryText: String[0..1];
    attribute currency: Currency[0..1];
    attribute interestRatePct: Real[0..1];
    attribute minimumBalance: MonetaryAmount[0..1];
    attribute maximumLimit: MonetaryAmount[0..1];
    attribute noticePeriod: Duration[0..1];
    attribute schedule: RegularSchedule[0..1];
  }

  // ---- Specializations ----

  item def PaymentService specializes Service {
    doc/* service that moves money between accounts or parties */
    attribute supportedSchemeText: String[0..*];    // e.g., SEPA, SWIFT, ACH, FPS
    attribute perTransactionLimit: MonetaryAmount[0..1];
  }

  item def CustodyService specializes Service {
    doc/* safekeeping of assets and administration of corporate actions */
    attribute safekeptInstrument: FinancialInstruments::FinancialInstrument[0..*];
  }

  item def BrokerageService specializes Service {
    doc/* order handling and execution in markets on behalf of clients */
    attribute supportedVenueText: String[0..*];     // exchange names / MICs (free text)
  }

  item def PortfolioManagementService specializes Service {
    doc/* discretionary or advisory portfolio management */
    attribute discretionary: Boolean[0..1];
    attribute benchmarkText: String[0..1];
  }

  // ---- Packaging / commercialization ----

  item def ProductOffering {
    doc/* commercial packaging (marketing name, fees, eligibility) of an underlying product/service */
    attribute offeringId: String[1..1];
    attribute product: Product[1..1];
    attribute serviceBundle: Service[0..*];
    attribute terms: TermsAndConditions[0..1];
    attribute feeSchedule: FeeSchedule[0..*];
    attribute eligibility: EligibilityCriterion[0..*];
    attribute availableFrom: Date[0..1];
    attribute availableTo: Date[0..1];
  }
}

// ==========================================
// ClientsAndAccounts.sysml
// (feature-based from ClientsAndAccounts.rdf)
// ==========================================
package ClientsAndAccounts {

  // ---- Imports (private) ----
  private import ScalarValues::*;              // String, Integer, Real, Boolean
  private import Time::*;                      // Date, DateTime
  private import CurrencyAmount::*;            // Currency, MonetaryAmount
  private import FinancialDates::*;            // Duration, RegularSchedule
  private import Parties::*;                   // Party
  private import Organizations::*;             // Organization / FormalOrganization
  private import FinancialProductsAndServices::*; // Product, Service, ProductOffering

  // ---- Enums ----
  enum def ClientCategory { enum Retail; enum Professional; enum EligibleCounterparty; enum Institutional; enum PrivateBanking; enum Other; }
  enum def AccountType { enum Current; enum Savings; enum TimeDeposit; enum Custody; enum Brokerage; enum Margin; enum Loan; enum CreditCard; enum Other; }
  enum def AccountStatus { enum Proposed; enum Active; enum Suspended; enum Closed; }
  enum def OwnershipForm { enum Individual; enum Joint; enum Corporate; enum Trust; enum Nominee; enum Other; }
  enum def KYCStatus { enum NotStarted; enum InProgress; enum Verified; enum Expired; enum Failed; }
  enum def TaxResidencyStatus { enum Declared; enum Undeclared; enum Pending; }

  // ---- Client (processor = part) ----

  part def Client specializes Parties::Party {
    doc/* party that consumes financial products and services and maintains one or more accounts */
    attribute clientId: String[1..1];
    attribute category: ClientCategory[0..1];
    attribute primaryRelationshipOrg: Organizations::Organization[0..1];
    attribute relationshipStart: Date[0..1];
    attribute kycStatus: KYCStatus[0..1];
    attribute riskProfileText: String[0..1];
    attribute taxResidencyStatus: TaxResidencyStatus[0..1];
  }

  // ---- Accounts (items = processed artifacts) ----

  item def Account {
    doc/* record representing a contractual relationship and ledger between a client and a provider for a given purpose */
    attribute accountId: String[1..1];
    attribute type: AccountType[1..1];
    attribute status: AccountStatus[0..1];
    attribute owner: Client[1..*];
    attribute beneficialOwner: Parties::Party[0..*];
    attribute servicingOrg: Organizations::Organization[0..1];
    attribute currency: Currency[0..1];
    attribute openedOn: Date[0..1];
    attribute closedOn: Date[0..1];
    attribute ownershipForm: OwnershipForm[0..1];
    attribute relatedOffering: FinancialProductsAndServices::ProductOffering[0..1];
  }

  item def AccountIdentifier {
    doc/* code uniquely identifying an account in some scheme (e.g., IBAN, BBAN, client internal ID) */
    attribute value: String[1..1];
    attribute scheme: String[0..1];
    attribute issuingAuthority: String[0..1];
    attribute account: Account[1..1];
  }

  item def AccountBalance {
    doc/* balance snapshot of an account at a point in time */
    attribute account: Account[1..1];
    attribute asOf: DateTime[1..1];
    attribute amount: MonetaryAmount[1..1];
    attribute availableAmount: MonetaryAmount[0..1];
    attribute creditLimit: MonetaryAmount[0..1];
  }

  item def AccountFeature {
    doc/* optional feature enabled on an account */
    attribute account: Account[1..1];
    attribute name: String[1..1];          // e.g., Overdraft, Sweep, Checkbook, OnlineAccess
    attribute enabled: Boolean[0..1];
    attribute limitAmount: MonetaryAmount[0..1];
  }

  // ---- Mandates & preferences ----

  item def AccountMandate {
    doc/* instruction granting authority to one or more parties to operate an account */
    attribute account: Account[1..1];
    attribute authorizedParty: Parties::Party[1..*];
    attribute scopeText: String[0..1];     // e.g., ViewOnly, Payments, Trading
    attribute validFrom: Date[0..1];
    attribute validTo: Date[0..1];
  }

  item def CommunicationPreference {
    doc/* preference for statements, notifications, and alerts */
    attribute client: Client[1..1];
    attribute channelText: String[1..1];   // Email, Postal, App, SMS
    attribute communicationLanguage: String[0..1];
    attribute statementFrequencyText: String[0..1];
  }

  // ---- Servicing & fees ----

  item def AccountServicingArrangement {
    doc/* service relationship linking an account to a providerâ€™s servicing terms and fees */
    attribute account: Account[1..1];
    attribute service: FinancialProductsAndServices::Service[1..1];
    attribute feeSchedule: FinancialProductsAndServices::FeeSchedule[0..*];
    attribute terms: FinancialProductsAndServices::TermsAndConditions[0..1];
    attribute startedOn: Date[0..1];
    attribute endedOn: Date[0..1];
  }

  item def StatementRecord {
    doc/* periodic or on-demand statement summarizing balances and activity */
    attribute account: Account[1..1];
    attribute periodStart: Date[1..1];
    attribute periodEnd: Date[1..1];
    attribute statementDate: Date[1..1];
    attribute closingBalance: MonetaryAmount[1..1];
    attribute minimumPaymentDue: MonetaryAmount[0..1];
  }

  // ---- Onboarding & suitability ----

  item def KYCRecord {
    doc/* record of due diligence checks and evidence used to establish client identity and risk */
    attribute client: Client[1..1];
    attribute status: KYCStatus[1..1];
    attribute checklistText: String[0..*];
    attribute lastReviewedOn: Date[0..1];
  }

  item def SuitabilityProfile {
    doc/* assessment capturing objectives, horizon, constraints, and risk tolerance */
    attribute client: Client[1..1];
    attribute objectiveText: String[0..*];
    attribute horizonText: String[0..1];
    attribute riskToleranceText: String[0..1];
    attribute updatedOn: Date[0..1];
  }
}
