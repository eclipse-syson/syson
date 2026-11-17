// ===================================================================
// FIBO_LOAN_RealEstateLoans.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_LOAN_RealEstateLoans {
  doc/* Configuration Item (CI) package for FIBO_LOAN_RealEstateLoans and its exported model elements. */
  public import Mortgages::*;
  public import MortgageOrigination::*;
  public import HMDACoveredMortgages::*;
  public import ConstructionLoans::*;
}

// ======================
// Mortgages.sysml
// (feature-based from Mortgages.rdf; aligned with LoansGeneral)
// ======================
package Mortgages {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import CurrencyAmount::*;           // MonetaryAmount, Currency
  private import FinancialDates::*;           // Duration, ExplicitDate, RegularSchedule
  private import RealPropertyPackage::*;             // RealProperty, Parcel, BuildingProperty
  private import Parties::*;                  // Party (borrower), Guarantor, etc.
  private import Organizations::*;            // Organization (lender, servicer)
  private import Loans::*;                    // Loan, InterestTerms, RepaymentTerms
  private import CashFlows::*;                // CashFlow (escrow, payments)
  private import LegalCorePackage::*;         // Law (for foreclosure / lien law)
  private import JurisdictionPackage::*;      // Jurisdiction

  // ---- Enums / code lists ----
  enum def MortgagePurpose { enum Purchase; enum Refinance; enum CashOutRefi; enum ConstructionToPerm; enum HomeImprovement; enum Other; }
  enum def OccupancyType { enum PrimaryResidence; enum SecondHome; enum Investment; }
  enum def MortgageType { enum Conventional; enum FHA; enum VA; enum USDA; enum Jumbo; enum Other; }
  enum def LienPosition { enum First; enum Second; enum Subordinate; }
  enum def AmortizationType { enum Fixed; enum Adjustable; enum InterestOnly; enum Balloon; enum Other; }
  enum def ServicingStatus { enum Portfolio; enum ServicedForOthers; enum Securitized; enum Transferred; }
  enum def PropertyUsage { enum SingleFamily; enum Condo; enum CoOp; enum TwoToFourUnit; enum ManufacturedHome; enum MixedUse; enum Other; }

  // ---- Core mortgage artifacts ----

  item def MortgageInsurance {
    doc/* credit enhancement protecting the lender/beneficiary against borrower default (e.g., PMI, FHA MIP) */
    attribute providerName: String[0..1];
    attribute upfrontPremiumAmount: MonetaryAmount[0..1];
    attribute monthlyPremiumAmount: MonetaryAmount[0..1];
    attribute cancelationDate: Date[0..1];
  }

  item def EscrowAccount {
    doc/* account for collecting and disbursing taxes, insurance, and other assessments with periodic payments */
    attribute openingBalance: MonetaryAmount[0..1];
    attribute targetCushionAmount: MonetaryAmount[0..1];
    attribute payeeTaxAuthority: Organizations::Organization[0..1];
    attribute payeeInsuranceCompany: Organizations::Organization[0..1];
    attribute lastAnalysisDate: Date[0..1];
    attribute escrowPayment: CashFlows::CashFlow[0..*];
  }

  item def AdjustableRateTerms {
    doc/* ARMs: index, margin, caps, reset structure */
    attribute referenceIndexName: String[1..1];          // e.g., "SOFR 12M", "CMT 1Y"
    attribute marginPct: Real[1..1];                     // in percent (not bps)
    attribute initialFixedPeriodMonths: Integer[0..1];
    attribute resetFrequencyMonths: Integer[0..1];
    attribute initialRateCapPct: Real[0..1];             // absolute change caps
    attribute periodicRateCapPct: Real[0..1];
    attribute lifetimeRateCapPct: Real[0..1];
    attribute lifetimeRateFloorPct: Real[0..1];
    attribute firstResetDate: Date[0..1];
  }

  item def MortgageNote {
    doc/* promissory note evidencing the borrowerâ€™s promise to repay the mortgage debt */
    attribute noteNumber: String[0..1];
    attribute noteDate: Date[0..1];
    attribute originalPrincipal: MonetaryAmount[1..1];
    attribute interestTerms: Loans::InterestTerms[1..1];
    attribute maturityDate: Date[0..1];
  }

  item def SecurityInstrument {
    doc/* instrument creating a lien on real property to secure the note (mortgage, deed of trust, security deed) */
    attribute instrumentTypeText: String[0..1];          // jurisdictional naming
    attribute recordingJurisdiction: Jurisdiction[0..1];
    attribute recordingBookPage: String[0..1];
    attribute recordingDate: Date[0..1];
    attribute lienPosition: LienPosition[0..1];
    attribute law: LegalCorePackage::Law[0..1];
  }

  // ---- Main mortgage loan (specialization of Loan) ----

  item def Mortgage specializes Loans::Loan {
    doc/* real estateâ€“secured loan with lien on one or more properties and scheduled repayment of principal and interest */
    // Parties
    attribute lenderOrganization: Organizations::Organization[0..1];
    attribute servicerOrganization: Organizations::Organization[0..1];

    // Collateral / property
    attribute collateralProperty: RealProperty[1..*];
    attribute propertyUsage: PropertyUsage[0..1];
    attribute occupancyType: OccupancyType[0..1];
    attribute lienPosition: LienPosition[0..1];

    // Economics
    attribute mortgageType: MortgageType[0..1];
    attribute amortizationType: AmortizationType[0..1];
    attribute loanToValuePct: Real[0..1];                // LTV
    attribute combinedLtvPct: Real[0..1];                // CLTV
    attribute debtToIncomePct: Real[0..1];               // back-end ratio at origination
    attribute mortgageInsurance: MortgageInsurance[0..1];
    attribute escrow: EscrowAccount[0..1];

    // Instruments
    attribute note: MortgageNote[0..1];
    attribute securityInstrument: SecurityInstrument[0..1];
    attribute adjustableRateTerms: AdjustableRateTerms[0..1];

    // Servicing & lifecycle
    attribute servicingStatus: ServicingStatus[0..1];
    attribute nextPaymentDueDate: Date[0..1];
    attribute paidToDate: Date[0..1];
  }

  item def ReverseMortgage specializes Mortgage {
    doc/* mortgage product allowing seniors to draw equity; balance grows, typically due on maturity/trigger */
    attribute nonRecourseFlag: Boolean[0..1];
    attribute maturityTriggerText: String[0..*];
  }

  item def HELOC specializes Mortgage {
    doc/* home equity line of credit secured by residential property */
    attribute creditLimit: MonetaryAmount[0..1];
    attribute availableCredit: MonetaryAmount[0..1];
    attribute drawPeriodEnd: Date[0..1];
    attribute repaymentPeriodEnd: Date[0..1];
  }
}

// ======================
// MortgageOrigination.sysml
// (feature-based from MortgageOrigination.rdf; extends Loans/LoanApplications)
// ======================
package MortgageOrigination {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import Documents::*;                 // Document, Text
  private import Organizations::*;
  private import Parties::*;
  private import RealPropertyPackage::*;
  private import Loans::*;
  private import LoanApplications::*;          // LoanApplication, CreditScore, UnderwritingResult
  private import Mortgages::*;                 // Mortgage

  // ---- Enums ----
  enum def OriginationChannel { enum Retail; enum Wholesale; enum Correspondent; enum Broker; enum Online; }
  enum def AUSSystem { enum DU; enum LPA; enum Proprietary; enum Other; }    // Desktop Underwriter / Loan Product Advisor
  enum def AppraisalForm { enum URAR_1004; enum _2055; enum _1073_Condo; enum _1025_2to4; enum Other; }
  enum def TitleInstrument { enum Mortgage; enum DeedOfTrust; enum SecurityDeed; enum Other; }

  // ---- Mortgage-specific origination artifacts ----

  item def LoanEstimate {
    doc/* early disclosure of estimated loan terms, projections, and closing costs (e.g., TRID LE) */
    attribute document: Document[1..1];
    attribute issuedOn: Date[1..1];
    attribute estimatedInterestRatePct: Real[0..1];
    attribute estimatedMonthlyPaymentAmount: MonetaryAmount[0..1];
    attribute estimatedClosingCostsAmount: MonetaryAmount[0..1];
  }

  item def ClosingDisclosure {
    doc/* final disclosure of loan terms and closing costs (e.g., TRID CD) */
    attribute document: Document[1..1];
    attribute issuedOn: Date[1..1];
    attribute cashToCloseAmount: MonetaryAmount[0..1];
  }

  item def AppraisalReport {
    doc/* valuation of subject property prepared by a qualified appraiser */
    attribute appraisalDate: Date[0..1];
    attribute appraisedValue: MonetaryAmount[0..1];
    attribute form: AppraisalForm[0..1];
    attribute appraiserName: String[0..1];
    attribute document: Document[0..1];
    attribute subjectProperty: RealProperty[0..1];
  }

  item def TitleCommitment {
    doc/* commitment to issue title insurance subject to stated conditions and exceptions */
    attribute commitmentNumber: String[0..1];
    attribute effectiveDate: Date[0..1];
    attribute instrument: TitleInstrument[0..1];
    attribute exceptionsText: String[0..*];
    attribute issuingCompany: Organizations::Organization[0..1];
    attribute document: Document[0..1];
  }

  item def AutomatedUnderwritingFinding {
    doc/* automated underwriting findings and conditions */
    attribute system: AUSSystem[1..1];
    attribute recommendationText: String[1..1];    // Approve/Eligible, Refer, etc.
    attribute messages: String[0..*];
    attribute runDate: DateTime[0..1];
  }

  item def MortgageApplication {
    doc/* specialization of loan application for mortgage underwriting and closing */
    attribute application: LoanApplications::LoanApplication[1..1];
    attribute subjectProperty: RealProperty[1..1];
    attribute channel: OriginationChannel[0..1];
    attribute loanEstimate: LoanEstimate[0..1];
    attribute closingDisclosure: ClosingDisclosure[0..1];
    attribute appraisal: AppraisalReport[0..1];
    attribute titleCommitment: TitleCommitment[0..1];
    attribute ausFinding: AutomatedUnderwritingFinding[0..1];
    attribute intendedMortgage: Mortgages::Mortgage[0..1];
  }
}

// ======================
// HMDACoveredMortgages.sysml
// (feature-based from HomeMortgageDisclosureActCoveredMortgages.rdf)
// ======================
package HMDACoveredMortgages {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import Organizations::*;
  private import Parties::*;
  private import RealPropertyPackage::*;
  private import Mortgages::*;                 // Mortgage

  // ---- HMDA-specific enums (simplified core fields) ----
  enum def HMDAActionTaken { enum Originated; enum ApprovedButNotAccepted; enum Denied; enum WithdrawnByApplicant; enum FileClosedForIncompleteness; enum PurchasedLoan; enum PreapprovalDenied; enum PreapprovalApprovedButNotAccepted; }
  enum def HMDALoanPurpose { enum HomePurchase; enum HomeImprovement; enum Refinancing; enum CashOutRefinancing; enum OtherPurpose; }
  enum def HMDALienStatus { enum SecuredByFirstLien; enum SecuredBySubordinateLien; enum NotSecuredByALien; }
  enum def HMDAPropertyType { enum OneToFourFamily; enum ManufacturedHome; enum Multifamily; }
  enum def HMDAOccupancy { enum PrincipalResidence; enum SecondResidence; enum InvestmentProperty; }

  item def HMDARecord {
    doc/* reporting record for a mortgage under the U.S. Home Mortgage Disclosure Act (HMDA) */
    attribute reportingEntity: Organizations::Organization[1..1];
    attribute activityYear: Integer[1..1];
    attribute loan: Mortgages::Mortgage[1..1];

    // Key HMDA data points
    attribute loanPurpose: HMDALoanPurpose[1..1];
    attribute actionTaken: HMDAActionTaken[1..1];
    attribute actionTakenDate: Date[0..1];
    attribute propertyType: HMDAPropertyType[0..1];
    attribute occupancy: HMDAOccupancy[0..1];
    attribute lienStatus: HMDALienStatus[0..1];

    // Pricing / rate spread
    attribute rateSpreadPct: Real[0..1];
    attribute hoepaStatus: Boolean[0..1];

    // Geography (normally census-tract granular; simplified here)
    attribute property: RealProperty[0..1];
    attribute countyCode: String[0..1];
    attribute stateCode: String[0..1];
    attribute postalCode: String[0..1];

    // Applicant/borrower demographics (anonymized/aggregated per usage)
    attribute applicantIncomeAmount: MonetaryAmount[0..1];
  }
}

// ======================
// ConstructionLoans.sysml
// (feature-based from ConstructionLoans.rdf)
// ======================
package ConstructionLoans {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import RealPropertyPackage::*;
  private import Organizations::*;
  private import Parties::*;
  private import Loans::*;
  private import Mortgages::*;

  // ---- Enums ----
  enum def ConstructionPhase { enum PreDevelopment; enum Construction; enum Stabilization; enum Conversion; enum Completed; }
  enum def DrawStatus { enum Requested; enum Approved; enum Funded; enum Rejected; enum Cancelled; }

  // ---- Construction-specific artifacts ----

  item def DrawSchedule {
    doc/* plan of staged disbursements (draws) aligned to construction milestones */
    attribute scheduleName: String[0..1];
    attribute milestoneName: String[0..*];
    attribute plannedDrawAmount: MonetaryAmount[0..*];
    attribute plannedDrawDate: Date[0..*];
  }

  item def InspectionReport {
    doc/* report verifying completion percentage and eligibility for draw */
    attribute inspectionDate: Date[1..1];
    attribute percentComplete: Real[0..1];
    attribute inspectorName: String[0..1];
    attribute notes: String[0..*];
  }

  item def ConstructionLoan specializes Loans::Loan {
    doc/* short-term loan financing the construction of real property improvements, often interest-only with staged draws */
    attribute projectName: String[0..1];
    attribute sponsor: Parties::Party[0..*];
    attribute generalContractor: Organizations::Organization[0..1];
    attribute collateralProperty: RealProperty[1..*];
    attribute budgetAmount: MonetaryAmount[0..1];
    attribute contingencyAmount: MonetaryAmount[0..1];
    attribute interestReserveAmount: MonetaryAmount[0..1];
    attribute phase: ConstructionPhase[0..1];
    attribute drawSchedule: DrawSchedule[0..1];
  }

  item def DrawRequest {
    doc/* borrower request for a construction draw against the loan budget */
    attribute loan: ConstructionLoan[1..1];
    attribute requestId: String[1..1];
    attribute requestDate: Date[1..1];
    attribute requestedAmount: MonetaryAmount[1..1];
    attribute status: DrawStatus[0..1];
    attribute inspectionReport: InspectionReport[0..1];
    attribute fundedCashFlow: CashFlows::CashFlow[0..1];
  }

  item def ConstructionToPermanentLoan specializes Mortgages::Mortgage {
    doc/* single-close or two-close product converting a construction loan to a permanent amortizing mortgage */
    attribute conversionDate: Date[0..1];
    attribute originalConstructionLoan: ConstructionLoan[0..1];
  }
}
