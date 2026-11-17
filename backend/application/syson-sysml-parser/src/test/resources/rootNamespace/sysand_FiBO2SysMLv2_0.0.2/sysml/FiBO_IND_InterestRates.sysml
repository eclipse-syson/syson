// ===================================================================
// FIBO_IND_InterestRates.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_IND_InterestRates {
  doc/* Configuration Item (CI) package for FIBO_IND_InterestRates and its exported model elements. */
  public import CommonInterestRates::*;
  public import InterestRates::*;
}

// ================================================================
// CommonInterestRates.sysml
// (shared conventions, calendars, and generic rate concepts)
// ================================================================
package CommonInterestRates {

  // ---- Imports (private) ----
  private import ScalarValues::*;           // String, Integer, Real, Boolean
  private import Time::*;                   // Date, DateTime
  private import CurrencyAmount::*;         // Currency
  private import BusinessCenters::*;        // BusinessCenter, BusinessDayCalendar (from FBC FunctionalEntities)
  private import FinancialDates::*;         // Duration, ExplicitDate, ExplicitDatePeriod, RegularSchedule
  private import Organizations::*;          // Organization (administrators, agents)

  // ---- Enumerations ----

  enum def DayCountConvention {
    doc/* day count fraction method for accruing interest */
    enum ACT_360; enum ACT_365F; enum ACT_ACT_ISDA; enum ACT_ACT_ICMA; enum ACT_ACT_AFB;
    enum Thirty_360_US; enum ThirtyE_360; enum ThirtyE_360_ISDA; enum BUS_252; enum Other;
  }

  enum def BusinessDayConvention {
    doc/* adjustment rule when a date falls on a non-business day */
    enum Following; enum ModifiedFollowing; enum Preceding; enum ModifiedPreceding; enum Unadjusted; enum EndOfMonth; enum Nearest;
  }

  enum def CompoundingConvention {
    doc/* compounding basis for periodic interest accrual */
    enum Simple; enum Compounded; enum Continuous; enum SimpleThenCompounded; enum CompoundedThenSimple;
  }

  enum def RateQuoteSide { enum Mid; enum Bid; enum Ask; }
  enum def RateQuoteType { enum Fixing; enum Indicative; enum Tradable; enum Reference; }

  enum def TenorCode {
    doc/* standard short codes for interest rate tenors */
    enum ON; enum TN; enum W1; enum W2; enum W3; enum W4;
    enum M1; enum M2; enum M3; enum M6; enum M9; enum Y1; enum Y2; enum Y3; enum Y5; enum Y7; enum Y10; enum Y20; enum Y30;
    enum Custom;
  }

  // ---- Core shared items ----

  item def TenorDefinition {
    doc/* defines a maturity tenor as either a standard code or a custom period offset */
    attribute code: TenorCode[0..1];
    attribute months: Integer[0..1];
    attribute days: Integer[0..1];
    attribute years: Integer[0..1];
  }

  item def CalendarSet {
    doc/* set of business centers / calendars used to adjust dates (e.g., "UKNY", "TARGET") */
    attribute businessCenter: BusinessCenter[0..*];
    attribute businessDayCalendar: BusinessDayCalendar[0..1];
  }

  item def RateConvention {
    doc/* bundle of conventions defining how a rate is quoted and accrued */
    attribute dayCount: DayCountConvention[1..1];
    attribute businessDayConvention: BusinessDayConvention[1..1];
    attribute settlementDays: Integer[0..1];        // fixing lag/settlement lag in days
    attribute compounding: CompoundingConvention[0..1];
    attribute calendars: CalendarSet[0..1];
  }

  item def RateAdministrator {
    doc/* organization responsible for administering a benchmark rate */
    attribute organization: Organizations::Organization[1..1];
    attribute website: String[0..1];
    attribute contactEmail: String[0..1];
  }

  item def RatePublicationSchedule {
    doc/* schedule describing when and how often a rate is published */
    attribute description: String[0..1];
    attribute typicalPublicationTime: String[0..1];   // HH:MM (local time textual)
    attribute frequencyLabel: String[0..1];           // e.g., "Daily on business days"
    attribute calendars: CalendarSet[0..1];
  }
}

// ================================================================
// InterestRates.sysml
// (feature-based from InterestRates.rdf; relies on CommonInterestRates)
// ================================================================
package InterestRates {

  // ---- Imports (private) ----
  private import ScalarValues::*;                // String, Integer, Real, Boolean
  private import Time::*;                        // Date, DateTime
  private import CurrencyAmount::*;              // Currency
  private import CommonInterestRates::*;         // conventions, calendars, admin
  private import FinancialDates::*;              // ExplicitDate, ExplicitDatePeriod, RegularSchedule, Duration
  private import Analytics::*;                   // Indicator, Dataset (for series alignment)
  private import Organizations::*;               // Organization, if needed for calculation agents

  // ---- Additional enums specific to rate families ----

  enum def FloatingRateFamily {
    doc/* classification of floating rate indices */
    enum IBOR;           // legacy interbank offered rates
    enum RFR;            // nearly risk-free rates (overnight)
    enum TermRFR;        // term rates derived from RFR-linked markets
    enum OIS;            // overnight indexed swap rate (derived)
    enum Treasury;       // government bill/notes yields
    enum Swap;           // par swap rates by tenor
    enum Deposit;        // deposit rates by tenor
    enum Other;
  }

  // ---- Benchmark definitions ----

  item def FloatingRateIndex {
    doc/* named benchmark interest rate identified by currency and family */
    attribute name: String[1..1];                     // e.g., "SOFR", "â‚¬STR", "SONIA", "TONA", "SARON", "BBSW", "CDOR (legacy)"
    attribute currency: Currency[1..1];
    attribute family: FloatingRateFamily[1..1];
    attribute administrator: RateAdministrator[0..1];
    attribute publicationSchedule: RatePublicationSchedule[0..1];
    attribute conventions: RateConvention[1..1];
    attribute referenceNotes: String[0..*];           // methodology references, governance notes
  }

  item def IborIndex specializes FloatingRateIndex {
    doc/* interbank offered rate family benchmark (largely discontinued or synthetic in many jurisdictions) */
  }

  item def RiskFreeRate specializes FloatingRateIndex {
    doc/* nearly risk-free overnight rate administered by a central bank or designated administrator */
  }

  item def TermRiskFreeRate specializes FloatingRateIndex {
    doc/* forward-looking term rate constructed from RFR derivatives or transactions */
    attribute tenor: TenorDefinition[1..1];
  }

  item def OISRate specializes FloatingRateIndex {
    doc/* rate implied from overnight indexed swaps for a given tenor */
    attribute tenor: TenorDefinition[1..1];
  }

  // ---- Quotes, fixings, and time series ----

  item def RateQuote {
    doc/* one quotation or fixing value for a benchmark at a timestamp */
    attribute index: FloatingRateIndex[1..1];
    attribute observedAt: DateTime[1..1];
    attribute ratePercent: Real[1..1];               // percentage per annum, e.g., 5.375 => 5.375%
    attribute quoteSide: RateQuoteSide[0..1];
    attribute quoteType: RateQuoteType[0..1];
    attribute tenor: TenorDefinition[0..1];          // if applicable (term rates, IBOR, OIS)
  }

  item def RateSeries specializes Analytics::Indicator {
    doc/* configured published series for an interest rate index (frequency, tenor, conventions) */
    attribute index: FloatingRateIndex[1..1];
    attribute seriesCode: String[0..1];              // publisher series id
    attribute tenor: TenorDefinition[0..1];
    attribute defaultFrequency: EconomicIndicators::Frequency[0..1]; // reuse IND frequency enum if imported
    attribute conventions: RateConvention[0..1];
  }

  item def RateObservation {
    doc/* one observation for a rate series */
    attribute series: RateSeries[1..1];
    attribute observedAt: DateTime[1..1];
    attribute fixingDate: Date[0..1];
    attribute valuePct: Real[1..1];                  // rate value in percent p.a.
  }

  // ---- Yield curves (par, zero, discount) ----

  enum def CurveType { enum Par; enum Zero; enum DiscountFactor; }

  item def CurveNode {
    doc/* one node on a curve with a specific tenor or maturity date */
    attribute tenor: TenorDefinition[0..1];
    attribute maturityDate: Date[0..1];
    attribute value: Real[1..1];                     // Par/Zero rate (%) or Discount Factor (unitless)
  }

  item def YieldCurve {
    doc/* collection of nodes representing a curve for a currency and curve type, observed at a timestamp */
    attribute currency: Currency[1..1];
    attribute curveType: CurveType[1..1];
    attribute observedAt: DateTime[1..1];
    attribute node: CurveNode[1..*];
    attribute constructionNotes: String[0..*];
  }

  // ---- Compounded/Averaged RFRs (derived) ----

  enum def AveragingMethod { enum CompoundedInArrears; enum CompoundedInAdvance; enum SimpleAverage; }

  item def AveragedRFRSeries specializes RateSeries {
    doc/* series representing an average/compounded RFR over a lookback period */
    attribute averagingMethod: AveragingMethod[1..1];
    attribute lookbackDays: Integer[0..1];
    attribute observationShiftDays: Integer[0..1];
    attribute lockoutDays: Integer[0..1];
  }

  // ---- Example specializations for common benchmarks (optional typing aids) ----

  item def SOFR specializes RiskFreeRate {
    doc/* Secured Overnight Financing Rate (USD) administered by the Federal Reserve Bank of New York */
  }

  item def ESTR specializes RiskFreeRate {
    doc/* Euro Short-Term Rate (â‚¬STR) administered by the European Central Bank */
  }

  item def SONIA specializes RiskFreeRate {
    doc/* Sterling Overnight Index Average administered by the Bank of England */
  }

  item def TONAR specializes RiskFreeRate {
    doc/* Tokyo Overnight Average Rate (TONA) administered by the Bank of Japan */
  }

  item def SARON specializes RiskFreeRate {
    doc/* Swiss Average Rate Overnight administered by SIX Exchange */
  }

  // ---- Linkages to conventions and calendars ----

  item def RateIndexConventionLink {
    doc/* explicit mapping of a benchmark to its convention bundle, for audits/governance */
    attribute index: FloatingRateIndex[1..1];
    attribute conventions: RateConvention[1..1];
  }

  // ---- Publication / governance notes ----

  item def RateGovernanceRecord {
    doc/* governance information aligned to IOSCO principles for a given index */
    attribute index: FloatingRateIndex[1..1];
    attribute administrator: RateAdministrator[0..1];
    attribute methodologyUrl: String[0..1];
    attribute cessationOrChangeNotice: String[0..1];   // e.g., fallback announcements, synthetic status
    attribute notes: String[0..*];
  }
}
