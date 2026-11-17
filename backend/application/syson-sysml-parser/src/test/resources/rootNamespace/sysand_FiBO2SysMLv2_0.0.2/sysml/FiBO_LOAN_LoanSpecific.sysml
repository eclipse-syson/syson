// ===================================================================
// FIBO_LOAN_LoanSpecific.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_LOAN_LoanSpecific {
  doc/* Configuration Item (CI) package for FIBO_LOAN_LoanSpecific and its exported model elements. */
  public import LoanProducts::*;
  public import ConsumerLoans::*;
  public import CardAccounts::*;
  public import CommercialLoans::*;
  public import StudentLoans::*;
  public import GreenLoans::*;
  public import MarineFinance::*;
}

// ======================
// LoanProducts.sysml
// (feature-based scaffolding for product templates & pricing)
// ======================
package LoanProducts {

  // ---- Imports (private) ----
  private import ScalarValues::*;            // String, Integer, Real, Boolean
  private import Time::*;                    // Date, DateTime
  private import CurrencyAmount::*;          // MonetaryAmount, Currency
  private import FinancialDates::*;          // Duration, ExplicitDate, RegularSchedule
  private import Loans::*;                   // Loan, InterestTerms, RepaymentTerms (from LoansGeneral)
  private import Organizations::*;           // Organization (lender)
  private import Parties::*;                 // Party (applicant/borrower)

  // ---- Enums ----
  enum def ProductCategory {
    enum Consumer; enum Mortgage; enum Auto; enum Student; enum Commercial; enum Green; enum Marine; enum Card; enum Other;
  }

  enum def EligibilityDecision { enum Eligible; enum ConditionallyEligible; enum Ineligible; }

  // ---- Core product catalog ----

  item def LoanProduct {
    doc/* reusable specification of a loan offering with parameterized pricing and eligibility rules */
    attribute productCode: String[1..1];
    attribute name: String[1..1];
    attribute category: ProductCategory[1..1];
    attribute lender: Organizations::Organization[0..1];
    attribute baseCurrency: Currency[0..1];

    // Parameter ranges
    attribute minAmount: MonetaryAmount[0..1];
    attribute maxAmount: MonetaryAmount[0..1];
    attribute minTenorMonths: Integer[0..1];
    attribute maxTenorMonths: Integer[0..1];

    // Defaults
    attribute defaultInterest: Loans::InterestTerms[0..1];
    attribute defaultRepayment: Loans::RepaymentTerms[0..1];

    // Marketing
    attribute description: String[0..1];
    attribute validFrom: Date[0..1];
    attribute validTo: Date[0..1];
  }

  item def ProductFeature {
    doc/* configurable characteristic of a loan product (e.g., payment holiday, overpayment, offset) */
    attribute name: String[1..1];
    attribute description: String[0..1];
    attribute enabled: Boolean[0..1];
  }

  item def PricingTier {
    doc/* pricing grid row determined by risk grade, LTV band, amount band, or channel */
    attribute label: String[1..1];
    attribute aprPct: Real[0..1];
    attribute marginBps: Real[0..1];
    attribute fixedRatePct: Real[0..1];
    attribute minAmount: MonetaryAmount[0..1];
    attribute maxAmount: MonetaryAmount[0..1];
    attribute minTermMonths: Integer[0..1];
    attribute maxTermMonths: Integer[0..1];
  }

  item def EligibilityCriteria {
    doc/* rules describing minimum requirements for applicant and loan characteristics */
    attribute minCreditScore: Integer[0..1];
    attribute maxDtiPct: Real[0..1];
    attribute maxLtvPct: Real[0..1];
    attribute residencyRequirementText: String[0..1];
    attribute employmentRequirementText: String[0..1];
  }

  item def ProductOffering {
    doc/* concrete offering of a product with configured features, pricing, and eligibility */
    attribute product: LoanProduct[1..1];
    attribute feature: ProductFeature[0..*];
    attribute pricingTier: PricingTier[0..*];
    attribute eligibility: EligibilityCriteria[0..1];
  }

  item def EligibilityAssessment {
    doc/* result of assessing an application against a product's eligibility criteria */
    attribute applicant: Parties::Party[1..1];
    attribute product: LoanProduct[1..1];
    attribute decision: EligibilityDecision[1..1];
    attribute reasons: String[0..*];
    attribute assessedOn: DateTime[0..1];
  }
}

// ======================
// ConsumerLoans.sysml
// (personal installment & retail finance)
// ======================
package ConsumerLoans {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import Loans::*;
  private import Parties::*;
  private import RealPropertyPackage::*;
  private import LoanProducts::*;

  // ---- Consumer loan specializations ----

  item def ConsumerInstallmentLoan specializes Loans::Loan {
    doc/* unsecured or lightly secured retail installment loan with fixed payments over a term */
    attribute purposeText: String[0..1];
    attribute retailerName: String[0..1];
  }

  item def AutoLoan specializes Loans::Loan {
    doc/* secured consumer loan for the purchase/refinance of a vehicle */
    attribute vehicleMake: String[0..1];
    attribute vehicleModel: String[0..1];
    attribute vehicleYear: Integer[0..1];
    attribute vehicleVin: String[0..1];
    attribute ltvPct: Real[0..1];
  }

  item def MortgageLoan specializes Loans::Loan {
    doc/* consumer real estate mortgage loan secured by residential property */
    attribute collateralProperty: RealProperty[0..1];
    attribute occupancyType: String[0..1];        // owner-occupied, investment, second home
    attribute lienPosition: String[0..1];         // first, second
    attribute ltvPct: Real[0..1];
  }

  item def LineOfCredit specializes Loans::Loan {
    doc/* revolving or non-amortizing consumer line of credit with draw and repayment periods */
    attribute creditLimit: MonetaryAmount[0..1];
    attribute availableCredit: MonetaryAmount[0..1];
    attribute drawPeriodEnd: Date[0..1];
    attribute renewalDate: Date[0..1];
  }
}

// ======================
// CardAccounts.sysml
// (credit/charge card revolving accounts)
// ======================
package CardAccounts {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import Loans::*;                 // reuse general loan constructs where helpful
  private import Parties::*;
  private import Organizations::*;
  private import CashFlows::*;

  // ---- Enums ----
  enum def CardNetwork { enum Visa; enum MasterCard; enum Amex; enum Discover; enum JCB; enum UnionPay; enum PrivateLabel; enum Other; }
  enum def CardType { enum Credit; enum Charge; enum DebitDeferred; enum Store; }
  enum def TransactionType { enum Purchase; enum CashAdvance; enum BalanceTransfer; enum Fee; enum Interest; enum Refund; }

  // ---- Core revolving account model ----

  item def CardAccount {
    doc/* revolving credit or charge account enabling card-based transactions */
    attribute accountNumber: String[1..1];
    attribute cardholder: Parties::Party[1..*];
    attribute issuer: Organizations::Organization[1..1];
    attribute network: CardNetwork[0..1];
    attribute cardType: CardType[1..1];

    attribute billingCycleDay: Integer[0..1];       // 1..31
    attribute statementDueDay: Integer[0..1];       // 1..31
    attribute creditLimit: CurrencyAmount::MonetaryAmount[0..1];
    attribute availableCredit: CurrencyAmount::MonetaryAmount[0..1];
    attribute aprPurchasePct: Real[0..1];
    attribute aprCashAdvancePct: Real[0..1];
    attribute penaltyAprPct: Real[0..1];
    attribute annualFeeAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute rewardsProgramText: String[0..1];
    attribute openedOn: Date[0..1];
    attribute closedOn: Date[0..1];
  }

  item def CardTransaction {
    doc/* posted transaction on a card account */
    attribute account: CardAccount[1..1];
    attribute transactionId: String[1..1];
    attribute transactionType: TransactionType[1..1];
    attribute merchantName: String[0..1];
    attribute postingDate: Date[0..1];
    attribute transactionDate: Date[0..1];
    attribute amount: CurrencyAmount::MonetaryAmount[1..1];
    attribute authorizationCode: String[0..1];
    attribute cashFlow: CashFlows::CashFlow[0..1];
  }

  item def StatementBalance {
    doc/* cycle-ending balance and minimum due for a card account */
    attribute account: CardAccount[1..1];
    attribute cycleEndDate: Date[1..1];
    attribute statementBalance: CurrencyAmount::MonetaryAmount[1..1];
    attribute minimumDue: CurrencyAmount::MonetaryAmount[1..1];
    attribute paymentDueDate: Date[1..1];
  }
}

// ======================
// CommercialLoans.sysml
// (business lending, term and revolving structures)
// ======================
package CommercialLoans {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import Loans::*;
  private import Parties::*;
  private import Organizations::*;
  private import RealPropertyPackage::*;
  private import LoanProducts::*;

  // ---- Commercial loan specializations ----

  item def CommercialTermLoan specializes Loans::Loan {
    doc/* fixed or floating rate business loan with scheduled amortization or bullet maturity */
    attribute borrowerLegalName: String[0..1];
    attribute purposeText: String[0..1];           // capex, acquisition, refinance, etc.
  }

  item def RevolvingCreditFacility specializes Loans::LoanFacility {
    doc/* committed business revolving facility allowing multiple drawdowns and repayments */
    attribute borrowingBaseText: String[0..1];
    attribute covenantPackageText: String[0..1];
  }

  item def AssetBasedLoan specializes Loans::Loan {
    doc/* loan secured by receivables, inventory, or other business assets with borrowing base */
    attribute borrowingBasePct: Real[0..1];
    attribute collateralDescription: String[0..1];
  }

  item def CommercialMortgage specializes Loans::Loan {
    doc/* mortgage loan secured by income-producing commercial real estate */
    attribute collateralProperty: RealProperty[0..1];
    attribute dscr: Real[0..1];                    // debt service coverage ratio
    attribute ltvPct: Real[0..1];
    attribute noiAmount: CurrencyAmount::MonetaryAmount[0..1]; // net operating income
  }

  item def SyndicatedLoan specializes Loans::Loan {
    doc/* multi-lender loan under a common documentation with agent bank(s) */
    attribute facilityAgent: Organizations::Organization[0..1];
    attribute securityAgent: Organizations::Organization[0..1];
    attribute lenderCount: Integer[0..1];
  }

  item def TradeFinanceLoan specializes Loans::Loan {
    doc/* short-term financing related to trade (e.g., LC-backed, export finance) */
    attribute instrumentReferenceText: String[0..1];   // LC, guarantee reference, etc.
    attribute tenorDays: Integer[0..1];
  }
}

// ======================
// StudentLoans.sysml
// (education finance variants)
// ======================
package StudentLoans {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import Loans::*;
  private import Parties::*;
  private import LoanProducts::*;

  // ---- Enums ----
  enum def StudentLoanProgram { enum Federal; enum Private; enum State; enum Institutional; enum Other; }
  enum def RepaymentPlanKind { enum Standard; enum Graduated; enum Extended; enum IncomeBased; enum PAYE; enum REPAYE; enum Other; }

  // ---- Specialization ----
  item def StudentLoan specializes Loans::Loan {
    doc/* loan extended to a student (or co-signer) to finance education costs */
    attribute program: StudentLoanProgram[0..1];
    attribute schoolName: String[0..1];
    attribute degreeProgramText: String[0..1];
    attribute enrollmentStatusText: String[0..1]; // full-time, half-time, graduated
    attribute gracePeriod: Duration[0..1];
    attribute defermentEligible: Boolean[0..1];
    attribute forbearanceEligible: Boolean[0..1];
    attribute subsidized: Boolean[0..1];
    attribute repaymentPlan: RepaymentPlanKind[0..1];
  }
}

// ======================
// GreenLoans.sysml
// (sustainability-linked & green use-of-proceeds loans)
// ======================
package GreenLoans {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import Loans::*;
  private import Parties::*;
  private import Organizations::*;
  private import Documents::*;            // Document, Text

  // ---- Enums ----
  enum def TaxonomyReference { enum EUTaxonomy; enum ICMA_GL; enum LMA_SLLP; enum CBI; enum Other; }

  // ---- Specializations ----
  item def GreenLoan specializes Loans::Loan {
    doc/* loan whose proceeds are exclusively applied to eligible green projects, aligned to a taxonomy/framework */
    attribute useOfProceedsText: String[0..*];
    attribute taxonomy: TaxonomyReference[0..*];
    attribute secondPartyOpinionProvider: Organizations::Organization[0..1];
    attribute kpiName: String[0..*];              // for SLL â€“ sustainability performance targets
    attribute kpiBaseline: String[0..*];
    attribute reportingDocument: Document[0..*];
  }

  item def SustainabilityLinkedLoan specializes Loans::Loan {
    doc/* loan with pricing or terms linked to borrower-level sustainability KPIs */
    attribute linkedKpiName: String[0..*];
    attribute stepUpBps: Real[0..1];
    attribute stepDownBps: Real[0..1];
    attribute verificationProvider: Organizations::Organization[0..1];
  }
}

// ======================
// MarineFinance.sysml
// (vessel-backed loans and maritime collateral)
// ======================
package MarineFinance {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import Loans::*;
  private import Parties::*;
  private import Organizations::*;

  // ---- Core maritime assets ----
  item def Vessel {
    doc/* maritime vessel considered as collateral or financed asset */
    attribute name: String[0..1];
    attribute imoNumber: String[0..1];            // International Maritime Organization number
    attribute callSign: String[0..1];
    attribute flagState: String[0..1];
    attribute vesselType: String[0..1];           // tanker, bulk, container, offshore
    attribute tonnage: Real[0..1];                // GT/DWT (units descriptive)
    attribute buildYear: Integer[0..1];
  }

  // ---- Specializations ----
  item def MarineLoan specializes Loans::Loan {
    doc/* loan secured by one or more maritime vessels and associated cash flows */
    attribute financedVessel: Vessel[0..*];
    attribute vesselAppraisedValue: CurrencyAmount::MonetaryAmount[0..1];
    attribute assignmentOfEarnings: Boolean[0..1];  // assignment of charter hire earnings
    attribute assignmentOfInsurances: Boolean[0..1];
    attribute preferredMortgageRegistered: Boolean[0..1];
  }

  item def VesselMortgage {
    doc/* mortgage instrument perfected over a vessel under an applicable flag state registry */
    attribute vessel: Vessel[1..1];
    attribute mortgageNumber: String[0..1];
    attribute registryPort: String[0..1];
    attribute filingDate: Date[0..1];
    attribute releaseDate: Date[0..1];
    attribute mortgageAmount: CurrencyAmount::MonetaryAmount[0..1];
  }
}
