// ===================================================================
// FIBO_FBC_DebtAndEquities.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_FBC_DebtAndEquities {
  doc/* Configuration Item (CI) package for FIBO_FBC_DebtAndEquities and its exported model elements. */
  public import Debt::*;
  public import CreditEvents::*;
  public import CreditRatings::*;
  public import Guaranty::*;
}

// ======================
// Debt.sysml
// (feature-based from Debt.rdf)
// ======================
package Debt {

  // ---- Imports (private) ----
  private import ScalarValues::*;          // String, Integer, Real, Boolean
  private import Time::*;                  // Date, DateTime
  private import CurrencyAmount::*;        // MonetaryAmount, Currency, MonetaryPrice
  private import FinancialDates::*;        // ExplicitDate, ExplicitDatePeriod, Duration, RegularSchedule
  private import Parties::*;               // Party (borrower, lender)
  private import Organizations::*;         // Organization / FormalOrganization (issuer)
  private import Contracts::*;             // Contract, ContractualCommitment
  private import Agreements::*;            // Agreement

  // ---- Enums ----
  enum def DebtInstrumentType {
    doc/* broad classification of debt instruments */
    enum Bond; enum Note; enum Loan; enum CommercialPaper; enum Debenture; enum ConvertibleBond; enum MediumTermNote; enum Other;
  }

  enum def InterestBasis {
    enum Fixed; enum Floating; enum ZeroCoupon; enum StepUp; enum StepDown; enum InflationLinked;
  }

  enum def PaymentFrequency {
    enum Monthly; enum Quarterly; enum SemiAnnual; enum Annual; enum Other;
  }

  enum def DayCount {
    enum ACT360; enum ACT365F; enum Thirty360; enum ACTACT;
  }

  enum def SeniorityLevel {
    enum SeniorSecured; enum SeniorUnsecured; enum Subordinated; enum JuniorSubordinated; enum Preferred;
  }

  enum def SecurityType {
    enum Secured; enum Unsecured;
  }

  // ---- Core debt items ----

  item def DebtInstrument specializes Contracts::Contract {
    doc/* financial instrument representing a borrower's obligation to pay interest and/or repay principal to a lender or investor */
    attribute instrumentId: String[0..1];
    attribute type: DebtInstrumentType[1..1];
    attribute issuer: Organizations::Organization[0..1];
    attribute borrower: Parties::Party[0..1];
    attribute lender: Parties::Party[0..*];
    attribute currency: Currency[0..1];
    attribute issueDate: Date[0..1];
    attribute maturityDate: ExplicitDate[0..1];
    attribute initialPrincipal: MonetaryAmount[0..1];
    attribute outstandingPrincipal: MonetaryAmount[0..1];
    attribute seniority: SeniorityLevel[0..1];
    attribute securityKind: SecurityType[0..1];
    attribute governingAgreement: Agreements::Agreement[0..*];
  }

  item def InterestTerms {
    doc/* parameters defining how interest accrues and is paid */
    attribute basis: InterestBasis[1..1];
    attribute fixedRatePct: Real[0..1];
    attribute floatingIndexText: String[0..1];    // e.g., SOFR 3M, EURIBOR 6M
    attribute spreadBps: Real[0..1];
    attribute dayCount: Debt::DayCount[0..1];
    attribute paymentFrequency: Debt::PaymentFrequency[0..1];
    attribute firstCouponDate: ExplicitDate[0..1];
    attribute lastCouponDate: ExplicitDate[0..1];
    attribute paymentSchedule: RegularSchedule[0..1];
    attribute compoundingText: String[0..1];
  }

  item def RedemptionTerms {
    doc/* conditions under which principal is repaid or redeemed (at maturity or earlier) */
    attribute redemptionPricePct: Real[0..1];
    attribute callProvisionText: String[0..*];
    attribute putProvisionText: String[0..*];
    attribute sinkingFundText: String[0..*];
    attribute earlyRedemptionDates: ExplicitDate[0..*];
  }

  item def Covenant {
    doc/* promise or restriction in a debt instrument (affirmative, negative, financial) */
    attribute covenantText: String[1..1];
    attribute breachConsequenceText: String[0..*];
  }

  item def DebtSecurity specializes DebtInstrument {
    doc/* tradable debt instrument (e.g., bond, note, commercial paper) issued by an organization to investors */
    attribute terms: InterestTerms[0..1];
    attribute redemption: RedemptionTerms[0..1];
    attribute covenant: Covenant[0..*];
    attribute isinOrCusip: String[0..1];
    attribute prospectusRef: String[0..1];
  }

  item def Bond specializes DebtSecurity {
    doc/* long- or medium-term debt security with periodic coupon and redemption at maturity */
  }

  item def Note specializes DebtSecurity {
    doc/* debt security with shorter maturity or different offering documentation conventions */
  }

  item def CommercialPaper specializes DebtSecurity {
    doc/* short-term unsecured promissory note typically issued at a discount */
    attribute discountRatePct: Real[0..1];
    attribute maturityDays: Integer[0..1];
  }

  item def LoanAgreement specializes DebtInstrument {
    doc/* bilateral or syndicated loan agreement defining drawdown, interest, amortization, and covenants */
    attribute facilityLimit: MonetaryAmount[0..1];
    attribute drawdownSchedule: RegularSchedule[0..1];
    attribute amortizationSchedule: RegularSchedule[0..1];
    attribute interestTerms: InterestTerms[0..1];
    attribute collateralDescription: String[0..*];
  }

  item def ConvertibleBond specializes DebtSecurity {
    doc/* debt security convertible into equity of the issuer under specified terms */
    attribute conversionRatio: Real[0..1];
    attribute conversionPrice: MonetaryPrice[0..1];
    attribute conversionStart: ExplicitDate[0..1];
    attribute conversionEnd: ExplicitDate[0..1];
  }

  item def PaymentRecord {
    doc/* record of an actual payment made under a debt instrument (coupon, principal) */
    attribute instrument: DebtInstrument[1..1];
    attribute paymentDate: Date[1..1];
    attribute amount: MonetaryAmount[1..1];
    attribute paymentTypeText: String[0..1];  // coupon, principal, fee
  }
}

// ======================
// CreditEvents.sysml
// (feature-based from CreditEvents.rdf)
// ======================
package CreditEvents {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Organizations::*;
  private import FinancialDates::*;
  private import CurrencyAmount::*;
  private import Debt::*;                    // link to instruments

  // ---- Enums ----
  enum def CreditEventType {
    doc/* standard credit event taxonomy (aligned with market usage) */
    enum FailureToPay; enum Bankruptcy; enum Moratorium; enum Repudiation; enum Restructuring; enum ObligationAcceleration; enum ObligationDefault; enum Other;
  }

  enum def EventStatus { enum Observed; enum Verified; enum Disputed; enum Settled; }

  // ---- Core items ----

  item def CreditEvent {
    doc/* event affecting a reference entityâ€™s obligations, potentially triggering contractual protections */
    attribute eventId: String[1..1];
    attribute referenceEntity: Organizations::Organization[1..1];
    attribute eventType: CreditEventType[1..1];
    attribute eventDate: Date[1..1];
    attribute status: EventStatus[0..1];
    attribute description: String[0..1];
    attribute affectedObligation: Debt::DebtInstrument[0..*];
    attribute publicSourceRef: String[0..*];       // press, regulator, court records
  }

  item def RestructuringTerms {
    doc/* details when the event type is Restructuring */
    attribute changeInCouponPct: Real[0..1];
    attribute changeInMaturity: Duration[0..1];
    attribute changeInPrincipalPct: Real[0..1];
    attribute mandatoryExchangeText: String[0..1];
  }

  item def BankruptcyDetail {
    doc/* details for bankruptcy/insolvency events */
    attribute courtName: String[0..1];
    attribute filingTypeText: String[0..1];    // e.g., Chapter 11
    attribute caseNumber: String[0..1];
  }
}

// ======================
// CreditRatings.sysml
// (feature-based from CreditRatings.rdf)
// ======================
package CreditRatings {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import Organizations::*;
  private import CurrencyAmount::*;
  private import Debt::*;
  private import FinancialDates::*;

  // ---- Enums & code lists ----
  enum def RatingScaleFamily { enum LongTerm; enum ShortTerm; enum BankFinancialStrength; enum NationalScale; enum Other; }
  enum def OutlookType { enum Positive; enum Stable; enum Negative; enum Developing; enum UnderReview; }

  // ---- Actors (parts) ----
  part def RatingAgency specializes Organizations::Organization {
    doc/* organization that assigns credit ratings and opinions on credit risk */
    attribute agencyCode: String[0..1];      // e.g., S&P, Moodyâ€™s, Fitch
    attribute country: String[0..1];
  }

  // ---- Core items ----
  item def RatingScale {
    doc/* ordered list of symbols defining a rating agencyâ€™s scale for a specific family (e.g., long-term) */
    attribute family: RatingScaleFamily[1..1];
    attribute agency: RatingAgency[1..1];
    attribute symbolsHighToLow: String[1..1];   // e.g., "AAA,AA+,AA,AA-,A+,...,D"
  }

  item def CreditRating {
    doc/* opinion on the creditworthiness of an obligor or instrument at a point in time */
    attribute ratedEntity: Organizations::Organization[0..1];
    attribute ratedInstrument: Debt::DebtInstrument[0..1];
    attribute agency: RatingAgency[1..1];
    attribute ratingSymbol: String[1..1];        // e.g., "A-", "BBB+", "Ba1"
    attribute scale: RatingScale[0..1];
    attribute outlook: OutlookType[0..1];
    attribute watchStatusText: String[0..1];
    attribute ratingDate: Date[1..1];
    attribute ratingActionText: String[0..1];    // Upgrade, Downgrade, Affirmed, Placed on Watch
  }

  item def RatingHistory {
    doc/* time series of ratings and outlooks for an entity or instrument */
    attribute subjectEntity: Organizations::Organization[0..1];
    attribute subjectInstrument: Debt::DebtInstrument[0..1];
    attribute agency: RatingAgency[1..1];
    attribute history: CreditRating[1..*];
  }
}

// ======================
// Guaranty.sysml
// (feature-based from Guaranty.rdf)
// ======================
package Guaranty {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Organizations::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import Contracts::*;
  private import Debt::*;

  // ---- Enums ----
  enum def GuarantyType { enum Payment; enum Performance; enum Limited; enum Unlimited; enum Several; enum JointAndSeveral; enum Other; }
  enum def ReleaseCondition { enum AutomaticUponPayment; enum Novation; enum ConsentOfBeneficiary; enum ExpiryDate; enum Other; }

  // ---- Core items ----
  item def GuarantyAgreement specializes Contracts::Contract {
    doc/* agreement under which a guarantor undertakes to satisfy obligations of a principal obligor if that obligor fails to perform */
    attribute guarantyId: String[0..1];
    attribute guarantyType: GuarantyType[1..1];
    attribute guarantor: Parties::Party[1..1];
    attribute principalObligor: Parties::Party[1..1];
    attribute guaranteedObligation: Debt::DebtInstrument[0..*];
    attribute maximumLiability: CurrencyAmount::MonetaryAmount[0..1];
    attribute releaseCondition: ReleaseCondition[0..1];
    attribute expiryDate: Date[0..1];
    attribute governingLawText: String[0..1];
  }

  item def LetterOfCredit {
    doc/* documentary credit issued by a bank guaranteeing payment to a beneficiary upon presentation of complying documents */
    attribute issuingBank: Organizations::Organization[1..1];
    attribute applicant: Parties::Party[1..1];
    attribute beneficiary: Parties::Party[1..1];
    attribute amount: CurrencyAmount::MonetaryAmount[1..1];
    attribute issueDate: Date[0..1];
    attribute expiryDate: Date[0..1];
    attribute standby: Boolean[0..1];
    attribute governingRulesText: String[0..1]; // e.g., UCP 600, ISP98
  }

  item def CreditEnhancement {
    doc/* arrangement that improves credit profile of an obligation (e.g., guaranty, LOC, cash collateral) */
    attribute description: String[0..1];
    attribute enhancementProvider: Parties::Party[0..1];
    attribute linkedObligation: Debt::DebtInstrument[0..*];
  }
}
