// ===================================================================
// FIBO_MD_DebtTemporal.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_MD_DebtTemporal {
  doc/* Configuration Item (CI) package for FIBO_MD_DebtTemporal and its exported model elements. */
  public import DebtTemporal::*;
}

// =======================================
// DebtTemporal.sysml
// (feature-based from DebtAnalytics.rdf; aligned with FBC/FND/IND)
// =======================================
package DebtTemporal {

  // ---- Imports (private) ----
  private import ScalarValues::*;            // String, Integer, Real, Boolean
  private import Time::*;                    // Date, DateTime
  private import FinancialDates::*;          // Duration, ExplicitDate, ExplicitDatePeriod, RegularSchedule
  private import CurrencyAmount::*;          // MonetaryAmount, Currency
  private import FinancialInstruments::*;    // DebtInstrument, SecurityIdentifier, etc. (FBC)
  private import SecuritiesIdentification::*; // SecurityIdentifier (SEC)
  private import InstrumentPricing::*;       // Pricing inputs/outputs (FBC)
  private import Settlement::*;              // Settlement conventions (FBC)
  private import Indicators::*;              // Index / Benchmark references (IND)
  private import Analytics::*;               // Dataset, Measure, Metric, Indicator (Utilities)
  private import Organizations::*;           // Organization (issuers, rating agencies)
  private import Parties::*;                 // Party (holders)
  private import LegalCorePackage::*;        // Law (for default/claims)
  private import JurisdictionPackage::*;     // Jurisdiction

  // =========================================================
  // Enumerations / code lists
  // =========================================================
  enum def CouponType { enum Fixed; enum Floating; enum Zero; enum StepUp; enum StepDown; enum InflationLinked; enum Other; }
  enum def DayCount { enum ACT_360; enum ACT_365F; enum ACT_ACT_ISDA; enum Thirty_360; enum ThirtyE_360; enum Other; }
  enum def BusinessDayConvention { enum Following; enum ModifiedFollowing; enum Preceding; enum ModifiedPreceding; enum Unadjusted; enum Other; }
  enum def Compounding { enum Simple; enum Compounded; enum Continuous; enum Other; }
  enum def ResetConvention { enum InAdvance; enum InArrears; }
  enum def PriceQuoteType { enum Clean; enum Dirty; enum Yield; enum SpreadToBenchmark; enum DiscountMargin; enum OAS; enum PricePer100; enum Other; }
  enum def PaymentStatus { enum Scheduled; enum Paid; enum Missed; enum Deferred; enum Cancelled; }
  enum def EventType {
    enum Issuance; enum CouponAccrualStart; enum CouponAccrualEnd;
    enum CouponPayment; enum RateReset; enum Call; enum Put; enum Conversion;
    enum PartialRedemption; enum FullRedemption; enum Maturity;
    enum Default; enum DistressedExchange; enum RatingChange; enum CovenantBreach;
    enum CorporateAction; enum Other;
  }
  enum def RatingScale { enum LongTerm; enum ShortTerm; enum NationalScale; enum Other; }

  // =========================================================
  // Core references & identity
  // =========================================================
  item def DebtInstrumentRef {
    doc/* reference handle to a specific debt instrument for linking temporal records */
    attribute instrument: FinancialInstruments::FinancialInstrument[0..1]; // prefer concrete if available
    attribute identifier: SecuritiesIdentification::SecurityIdentifier[0..*]; // ISIN/CUSIP/FIGI etc.
    attribute issuer: Organizations::Organization[0..1];
    attribute currency: Currency[0..1];
    attribute issueDate: Date[0..1];
    attribute maturityDate: Date[0..1];
  }

  // =========================================================
  // Static terms affecting temporal analytics
  // =========================================================
  item def CouponTerms {
    doc/* coupon calculation terms for a debt instrument */
    attribute couponType: CouponType[1..1];
    attribute fixedRatePct: Real[0..1];
    attribute referenceIndex: Indicators::Index[0..1];          // e.g., SOFR, EURIBOR, TIIE
    attribute spreadBps: Real[0..1];
    attribute dayCount: DayCount[0..1];
    attribute compounding: Compounding[0..1];
    attribute resetConvention: ResetConvention[0..1];
    attribute resetFrequencyMonths: Integer[0..1];
    attribute businessDayConvention: BusinessDayConvention[0..1];
    attribute schedule: RegularSchedule[0..1];
  }

  item def CallPutTerms {
    doc/* embedded call/put features and schedules */
    attribute isCallable: Boolean[0..1];
    attribute isPuttable: Boolean[0..1];
    attribute callSchedule: CallSchedule[0..*];
    attribute putSchedule: PutSchedule[0..*];
  }

  item def CallSchedule {
    doc/* optional redemption windows and prices for issuer calls */
    attribute callDate: Date[1..1];
    attribute callPricePer100: Real[0..1];
    attribute notes: String[0..1];
  }

  item def PutSchedule {
    doc/* optional redemption windows and prices for investor puts */
    attribute putDate: Date[1..1];
    attribute putPricePer100: Real[0..1];
    attribute notes: String[0..1];
  }

  // =========================================================
  // Temporal observations: quotes, curves, analytics
  // =========================================================
  item def PriceQuote {
    doc/* quoted price/yield/spread as of a timestamp for an instrument */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute quoteType: PriceQuoteType[1..1];
    attribute value: Real[1..1];                            // numeric value of the quote (e.g., clean price 99.5, yield 5.23)
    attribute quoteCurrency: Currency[0..1];               // for price per 100 currency units
    attribute asOf: DateTime[1..1];
    attribute source: Organizations::Organization[0..1];   // venue/vendor
  }

  item def AccruedInterestRecord {
    doc/* accrued interest amount per 100 nominal as of a date/time */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute accruedPer100: Real[1..1];
    attribute asOf: DateTime[1..1];
    attribute dayCount: DayCount[0..1];
  }

  item def BenchmarkCurveRef {
    doc/* reference to a curve used for pricing (OIS, govies, swap curve) */
    attribute name: String[1..1];
    attribute currency: Currency[0..1];
    attribute provider: Organizations::Organization[0..1];
    attribute version: String[0..1];
  }

  item def CurvePoint {
    doc/* single node on a curve term structure */
    attribute tenorMonths: Integer[0..1];
    attribute maturityDate: Date[0..1];
    attribute ratePct: Real[1..1];
    attribute asOf: DateTime[1..1];
    attribute curve: BenchmarkCurveRef[1..1];
  }

  item def OASRecord {
    doc/* option-adjusted spread computed for an instrument against a curve */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute curve: BenchmarkCurveRef[1..1];
    attribute oasBps: Real[1..1];
    attribute asOf: DateTime[1..1];
    attribute methodText: String[0..1];
  }

  item def DurationConvexityRecord {
    doc/* key rate duration, Macaulay/modified duration, convexity */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute modifiedDuration: Real[0..1];
    attribute macaulayDuration: Real[0..1];
    attribute convexity: Real[0..1];
    attribute keyRateTenorMonths: Integer[0..*];
    attribute keyRateDuration: Real[0..*];
    attribute asOf: DateTime[1..1];
  }

  item def ValuationRecord {
    doc/* model valuation snapshot (clean/dirty price, yield, PV, risk) */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute cleanPricePer100: Real[0..1];
    attribute dirtyPricePer100: Real[0..1];
    attribute yieldToMaturityPct: Real[0..1];
    attribute pvAmount: MonetaryAmount[0..1];
    attribute accruedPer100: Real[0..1];
    attribute oasBps: Real[0..1];
    attribute asOf: DateTime[1..1];
    attribute inputsDataset: Analytics::Dataset[0..1];
    attribute modelName: String[0..1];
    attribute modelVersion: String[0..1];
  }

  // =========================================================
  // Cashflows & schedules
  // =========================================================
  item def CouponPeriod {
    doc/* single coupon accrual/payment period */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute accrualStart: Date[1..1];
    attribute accrualEnd: Date[1..1];
    attribute paymentDate: Date[0..1];
    attribute couponRatePct: Real[0..1];
    attribute expectedPaymentAmount: MonetaryAmount[0..1];
    attribute status: PaymentStatus[0..1];
  }

  item def CashflowEvent {
    doc/* realized or scheduled cash event for a debt instrument */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute eventType: EventType[1..1];
    attribute eventDate: Date[1..1];
    attribute amount: MonetaryAmount[0..1];
    attribute status: PaymentStatus[0..1];
    attribute notes: String[0..1];
  }

  // =========================================================
  // Corporate actions, ratings, and credit events
  // =========================================================
  item def CorporateActionLink {
    doc/* linkage to a corporate action affecting the instrument (e.g., exchange offer, consent solicitation) */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute actionTypeText: String[1..1];
    attribute announcementDate: Date[0..1];
    attribute effectiveDate: Date[0..1];
    attribute referenceDoc: Documents::Document[0..1];
  }

  item def RatingRecord {
    doc/* issuer or instrument credit rating as of a date */
    attribute instrumentRef: DebtInstrumentRef[0..1];
    attribute issuer: Organizations::Organization[0..1];
    attribute ratingAgency: Organizations::Organization[1..1];
    attribute ratingScale: RatingScale[0..1];
    attribute ratingCode: String[1..1];             // e.g., "A-", "BBB+", "Ba1"
    attribute outlookText: String[0..1];            // Positive/Stable/Negative
    attribute watchStatusText: String[0..1];        // Watch Positive/Negative
    attribute asOf: Date[1..1];
  }

  item def CovenantRecord {
    doc/* covenant definition and current headroom measurement */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute covenantName: String[1..1];
    attribute thresholdText: String[0..1];
    attribute measuredValueText: String[0..1];
    attribute asOf: Date[1..1];
    attribute isBreached: Boolean[0..1];
  }

  item def DefaultEvent {
    doc/* event of default or distressed exchange with details */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute eventType: EventType[1..1];           // Default or DistressedExchange
    attribute eventDate: Date[1..1];
    attribute description: String[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  item def RecoveryRecord {
    doc/* recovery information post-default (price, rate, timing) */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute recoveryRatePct: Real[0..1];
    attribute referenceDate: Date[1..1];
    attribute notes: String[0..1];
  }

  // =========================================================
  // Time series structures (prices, yields, spreads)
  // =========================================================
  item def TimeSeries {
    doc/* generic time series container for debt market data */
    attribute name: String[1..1];
    attribute description: String[0..1];
    attribute instrumentRef: DebtInstrumentRef[0..1];
    attribute quoteType: PriceQuoteType[0..1];
    attribute currency: Currency[0..1];
    attribute dataset: Analytics::Dataset[0..1];
  }

  item def TimeSeriesObservation {
    doc/* single observation in a time series */
    attribute series: TimeSeries[1..1];
    attribute observationDate: Date[1..1];
    attribute value: Real[0..1];
    attribute source: Organizations::Organization[0..1];
  }

  // =========================================================
  // Linkages & collections
  // =========================================================
  item def InstrumentAnalyticsBundle {
    doc/* collected analytics objects for an instrument at or over time */
    attribute instrumentRef: DebtInstrumentRef[1..1];
    attribute couponTerms: CouponTerms[0..1];
    attribute callPutTerms: CallPutTerms[0..1];
    attribute valuation: ValuationRecord[0..1];
    attribute accrued: AccruedInterestRecord[0..1];
    attribute oas: OASRecord[0..1];
    attribute durationConvexity: DurationConvexityRecord[0..1];
    attribute lastPriceQuote: PriceQuote[0..1];
    attribute latestEvents: CashflowEvent[0..*];
  }
}
