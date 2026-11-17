// ===================================================================
// FIBO_LOAN_LoansGeneral.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_LOAN_LoansGeneral {
  doc/* Configuration Item (CI) package for FIBO_LOAN_LoansGeneral and its exported model elements. */
  public import Loans::*;
  public import LoanApplications::*;
  public import LoanEvents::*;
  public import LoansRegulatory::*;
}

// ======================
// Loans.sysml
// (feature-based from Loans.rdf)
// ======================
package Loans {

  // ---- Imports (private) ----
  private import ScalarValues::*;            // String, Integer, Real, Boolean
  private import Time::*;                    // Date, DateTime
  private import CurrencyAmount::*;          // MonetaryAmount, Currency
  private import FinancialDates::*;          // Duration, ExplicitDate, ExplicitDatePeriod, RegularSchedule
  private import Parties::*;                 // Party (borrowers, lenders, guarantors)
  private import Organizations::*;           // Organization, FormalOrganization
  private import Contracts::*;               // Contract (LoanContract specialization)
  private import CashFlows::*;               // CashFlow (for payments and disbursements)
  private import RealPropertyPackage::*;            // RealProperty (optional collateral)
  private import OwnershipAndControl::*;     // Owner, OwnershipInterest (optional collateral ownership)

  // ---- Enumerations ----
  enum def LoanPurposeKind {
    enum Consumer; enum Mortgage; enum Auto; enum Student;
    enum Commercial; enum Construction; enum WorkingCapital;
    enum ProjectFinance; enum TradeFinance; enum Other;
  }

  enum def RateType { enum Fixed; enum Floating; enum StepUp; enum Teaser; enum Other; }

  enum def AmortizationMethod {
    enum InterestOnly; enum LevelPayment; enum Bullet; enum Balloon; enum NegativeAmortization; enum Custom;
  }

  enum def PaymentFrequency { enum Weekly; enum BiWeekly; enum Monthly; enum Quarterly; enum SemiAnnual; enum Annual; enum AdHoc; }

  enum def LoanStatus { enum Proposed; enum Approved; enum Active; enum Delinquent; enum Defaulted; enum Restructured; enum Matured; enum Closed; }

  // ---- Core loan model ----

  item def InterestTerms {
    doc/* terms governing interest accrual and calculation for a loan */
    attribute rateType: RateType[1..1];
    attribute fixedRatePct: Real[0..1];                    // if fixed
    attribute referenceIndex: String[0..1];                // e.g., "SOFR 1M" (link to IND::InterestRates if desired)
    attribute spreadBps: Real[0..1];                       // spread over index (basis points)
    attribute compounding: CommonInterestRates::CompoundingConvention[0..1];
    attribute dayCount: CommonInterestRates::DayCountConvention[0..1];
    attribute resetFrequency: PaymentFrequency[0..1];
    attribute floorPct: Real[0..1];
    attribute capPct: Real[0..1];
  }

  item def RepaymentTerms {
    doc/* principal repayment structure and schedule configuration */
    attribute amortizationMethod: AmortizationMethod[1..1];
    attribute paymentFrequency: PaymentFrequency[1..1];
    attribute gracePeriod: Duration[0..1];
    attribute firstPaymentDate: Date[0..1];
    attribute lastPaymentDate: Date[0..1];
    attribute schedule: RegularSchedule[0..1];
  }

  item def CollateralItem {
    doc/* asset pledged to secure performance of a loan */
    attribute description: String[0..1];
    attribute appraisedValue: MonetaryAmount[0..1];
    attribute lienPosition: String[0..1];                  // e.g., "First", "Second"
    attribute property: RealProperty[0..1];  // optional tie to property CI
    attribute owner: Ownership::Owner[0..1];
  }

  item def Covenant {
    doc/* promise in a loan agreement requiring or restricting actions of the borrower */
    attribute covenantText: String[1..1];
    attribute isAffirmative: Boolean[0..1];
    attribute isFinancial: Boolean[0..1];
    attribute thresholdValue: Real[0..1];
    attribute measurementFrequency: PaymentFrequency[0..1];
  }

  item def LoanContract specializes Contracts::Contract {
    doc/* legally binding agreement defining rights and obligations for a loan */
    attribute contractId: String[0..1];
  }

  item def Loan {
    doc/* extension of credit to a borrower, with obligation to repay principal plus interest under agreed terms */
    attribute loanIdentifier: String[1..1];
    attribute contract: LoanContract[0..1];
    attribute borrower: Parties::Party[1..*];
    attribute lender: Parties::Party[1..*];
    attribute guarantor: Parties::Party[0..*];
    attribute purpose: LoanPurposeKind[0..1];

    // Economics
    attribute currency: Currency[1..1];
    attribute originalPrincipal: MonetaryAmount[1..1];
    attribute currentPrincipalOutstanding: MonetaryAmount[0..1];
    attribute interest: InterestTerms[1..1];
    attribute repayment: RepaymentTerms[1..1];
    attribute feesAmount: MonetaryAmount[0..1];

    // Dates
    attribute originationDate: Date[0..1];
    attribute effectiveDate: Date[0..1];
    attribute maturityDate: Date[0..1];

    // Collateral & covenants
    attribute collateral: CollateralItem[0..*];
    attribute covenant: Covenant[0..*];

    // Lifecycle
    attribute status: LoanStatus[0..1];
    attribute lastStatusChange: DateTime[0..1];

    // Cash movements
    attribute disbursement: CashFlows::CashFlow[0..*];
    attribute scheduledPayment: CashFlows::CashFlow[0..*];
  }

  item def SecuredLoan specializes Loan {
    doc/* loan backed by specific collateral securing the obligation */
  }

  item def UnsecuredLoan specializes Loan {
    doc/* loan not secured by specific collateral */
  }

  item def LoanFacility {
    doc/* umbrella commitment under which one or more loan drawdowns (tranches) may be made */
    attribute facilityId: String[1..1];
    attribute borrower: Parties::Party[1..*];
    attribute lender: Parties::Party[1..*];
    attribute committedAmount: MonetaryAmount[1..1];
    attribute availableAmount: MonetaryAmount[0..1];
    attribute currency: Currency[1..1];
    attribute openingDate: Date[0..1];
    attribute expiryDate: Date[0..1];
    attribute baseInterest: InterestTerms[0..1];
    attribute covenant: Covenant[0..*];
  }

  item def LoanTranche specializes Loan {
    doc/* drawdown under a facility with potentially distinct pricing or maturity terms */
    attribute facility: LoanFacility[0..1];
    attribute trancheNumber: String[0..1];
  }
}

// ======================
// LoanApplications.sysml
// (feature-based from LoanApplications.rdf)
// ======================
package LoanApplications {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import Parties::*;
  private import Organizations::*;
  private import Documents::*;              // Document, Text
  private import Loans::*;                  // Loan, LoanFacility

  // ---- Enumerations ----
  enum def ApplicationStatus { enum Draft; enum Submitted; enum InReview; enum Approved; enum Declined; enum Withdrawn; enum Expired; }

  enum def DecisionReasonKind { enum CreditRisk; enum InsufficientDocumentation; enum Policy; enum Pricing; enum Capacity; enum Collateral; enum Other; }

  // ---- Core application artifacts ----
  item def Applicant {
    doc/* party applying for credit */
    attribute party: Parties::Party[1..1];
  }

  item def ApplicationDocument {
    doc/* document provided to support a loan application (e.g., statements, IDs) */
    attribute document: Document[1..1];
    attribute description: String[0..1];
  }

  item def CreditScore {
    doc/* score representing applicantâ€™s creditworthiness under a specific model */
    attribute modelName: String[1..1];
    attribute scoreValue: Integer[1..1];
    attribute scoreDate: Date[0..1];
    attribute provider: Organizations::Organization[0..1];
  }

  item def UnderwritingResult {
    doc/* assessment outcome from underwriting */
    attribute dtiRatioPct: Real[0..1];                 // debt-to-income
    attribute ltvRatioPct: Real[0..1];                 // loan-to-value
    attribute affordabilityNote: String[0..1];
    attribute creditScore: CreditScore[0..1];
    attribute recommendedDecision: ApplicationStatus[0..1]; // Approved/Declined
  }

  item def LoanApplication {
    doc/* request by one or more applicants for a new loan or facility */
    attribute applicationId: String[1..1];
    attribute applicant: Applicant[1..*];
    attribute lender: Organizations::Organization[0..1];
    attribute requestedAmount: MonetaryAmount[1..1];
    attribute requestedCurrency: Currency[1..1];
    attribute requestedPurposeText: String[0..1];
    attribute collateralDescription: String[0..1];
    attribute createdOn: DateTime[0..1];
    attribute status: ApplicationStatus[0..1];
    attribute supportingDocument: ApplicationDocument[0..*];
    attribute underwriting: UnderwritingResult[0..1];
    attribute decision: Decision[0..1];
    attribute resultingLoan: Loans::Loan[0..*];
    attribute resultingFacility: Loans::LoanFacility[0..1];
  }

  item def Decision {
    doc/* decision taken on a loan application */
    attribute decidedOn: DateTime[0..1];
    attribute decidedBy: Organizations::Organization[0..1];
    attribute status: ApplicationStatus[1..1];         // Approved or Declined
    attribute reasonKind: DecisionReasonKind[0..1];
    attribute reasonText: String[0..1];
    attribute conditionsPrecedent: String[0..*];       // conditions to satisfy before closing
  }
}

// ======================
// LoanEvents.sysml
// (feature-based from LoanEvents.rdf)
// ======================
package LoanEvents {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import CashFlows::*;
  private import FinancialDates::*;
  private import Loans::*;                  // Loan
  private import Agreements::*;             // Agreement (if needed for amendments)

  // ---- Core lifecycle events ----
  item def LoanEvent {
    doc/* event affecting the state, economics, or obligations of a loan */
    attribute loan: Loans::Loan[1..1];
    attribute eventTime: DateTime[1..1];
    attribute description: String[0..1];
  }

  item def DisbursementEvent specializes LoanEvent {
    doc/* initial or subsequent funded amount paid out to borrower */
    attribute cashFlow: CashFlow[0..1];
    attribute amount: MonetaryAmount[0..1];
    attribute valueDate: Date[0..1];
  }

  item def ScheduledPaymentEvent specializes LoanEvent {
    doc/* scheduled installment per repayment plan */
    attribute dueDate: Date[1..1];
    attribute paidDate: Date[0..1];
    attribute principalComponent: MonetaryAmount[0..1];
    attribute interestComponent: MonetaryAmount[0..1];
    attribute feesComponent: MonetaryAmount[0..1];
    attribute cashFlow: CashFlow[0..1];
  }

  item def PrepaymentEvent specializes LoanEvent {
    doc/* borrower-initiated early repayment of principal */
    attribute amount: MonetaryAmount[1..1];
    attribute penaltyAmount: MonetaryAmount[0..1];
  }

  item def RateResetEvent specializes LoanEvent {
    doc/* interest rate reset based on index observation and spread */
    attribute previousRatePct: Real[0..1];
    attribute newRatePct: Real[1..1];
    attribute effectiveFrom: Date[1..1];
  }

  item def CovenantBreachEvent specializes LoanEvent {
    doc/* occurrence of a breach of covenant terms */
    attribute covenantText: String[0..1];
    attribute measuredValue: Real[0..1];
  }

  item def ModificationEvent specializes LoanEvent {
    doc/* amendment to terms (e.g., maturity, spread, amortization) */
    attribute amendedField: String[1..1];
    attribute previousValueText: String[0..1];
    attribute newValueText: String[0..1];
    attribute effectiveDate: Date[0..1];
  }

  item def DefaultEvent specializes LoanEvent {
    doc/* event of default such as non-payment or insolvency */
    attribute defaultReasonText: String[0..1];
    attribute defaultDate: Date[0..1];
  }

  item def CureEvent specializes LoanEvent {
    doc/* remediation of a prior default or breach */
    attribute curedDate: Date[0..1];
    attribute cureDescription: String[0..1];
  }

  item def MaturityEvent specializes LoanEvent {
    doc/* loan reaches contractual maturity */
    attribute maturityDate: Date[1..1];
    attribute finalPaymentAmount: MonetaryAmount[0..1];
  }
}

// ======================
// LoansRegulatory.sysml
// (feature-based from LoansRegulatory.rdf)
// ======================
package LoansRegulatory {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import Organizations::*;
  private import JurisdictionPackage::*;          // Jurisdiction (from FND Law CI)
  private import LegalCorePackage::*;            // Law (if needed)
  private import Loans::*;

  // ---- Enumerations ----
  enum def IFRS9Stage { enum Stage1; enum Stage2; enum Stage3; }
  enum def BaselAssetClass { enum Sovereign; enum Bank; enum Corporate; enum Retail; enum SecuredByMortgages; enum Equity; enum Other; }
  enum def NonPerformingStatus { enum Performing; enum PastDue; enum NonPerforming; enum Forborne; enum WrittenOff; }

  // ---- Regulatory & risk attributes ----

  item def RegulatoryClassification {
    doc/* classification of a loan under regulatory frameworks (Basel, IFRS9, local rules) */
    attribute loan: Loans::Loan[1..1];
    attribute jurisdiction: Jurisdiction[0..1];
    attribute ifrs9Stage: IFRS9Stage[0..1];
    attribute baselAssetClass: BaselAssetClass[0..1];
    attribute riskWeightPct: Real[0..1];
    attribute pdPct: Real[0..1];                   // probability of default
    attribute lgdPct: Real[0..1];                  // loss given default
    attribute eadAmount: MonetaryAmount[0..1];     // exposure at default
    attribute reportingDate: Date[0..1];
  }

  item def NonPerformingLoanRecord {
    doc/* tracking record for non-performing and forborne loans */
    attribute loan: Loans::Loan[1..1];
    attribute status: NonPerformingStatus[1..1];
    attribute pastDueDays: Integer[0..1];
    attribute forbearanceFlag: Boolean[0..1];
    attribute classificationDate: Date[0..1];
    attribute notes: String[0..*];
  }

  item def SupervisoryReporting {
    doc/* package of data prepared for submission to a supervisory authority */
    attribute reportingEntity: Organizations::Organization[1..1];
    attribute jurisdiction: Jurisdiction[0..1];
    attribute asOfDate: Date[1..1];
    attribute includesLoan: Loans::Loan[0..*];
    attribute includesRegClassification: RegulatoryClassification[0..*];
    attribute nplRecord: NonPerformingLoanRecord[0..*];
    attribute referenceLaw: LegalCorePackage::Law[0..*];
    attribute reportId: String[0..1];
  }
}
