// ===================================================================
// FIBO_IND_ForeignExchange.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_IND_ForeignExchange {
  doc/* Configuration Item (CI) package for FIBO_IND_ForeignExchange and its exported model elements. */
  public import ForeignExchange::*;
}

// =======================================
// ForeignExchange.sysml
// (feature-based from ForeignExchange.rdf; aligned with IND/FND CIs)
// =======================================
package ForeignExchange {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import CurrencyAmount::*;           // Currency, MonetaryAmount
  private import FinancialDates::*;           // ExplicitDate, Duration, RegularSchedule, ExplicitDatePeriod
  private import Organizations::*;            // Organization / FormalOrganization (publishers, venues)
  private import Markets::*;                  // TradingVenue, MarketOperator (if FunctionalEntities CI is loaded)
  private import BusinessCenters::*;          // BusinessCenter, BusinessDayCalendar (if available)
  private import Analytics::*;                // Metric, Indicator, Dataset (for series alignment)

  // =========================================================
  // Enumerations / code lists
  // =========================================================
  enum def QuoteBasis {
    doc/* quotation direction for FX rate */
    enum Direct;    // doc/* units of domestic currency per 1 unit of foreign currency */
    enum Indirect;  // doc/* units of foreign currency per 1 unit of domestic currency */
  }

  enum def PriceSide {
    doc/* side of quotation */
    enum Mid;
    enum Bid;
    enum Ask;
  }

  enum def QuoteConvention {
    doc/* units per base for FX quotation */
    enum UnitsPerBase;         // e.g., 1 USD = 147.25 JPY
    enum BasePerUnit;          // e.g., 1 JPY = 0.00679 USD
  }

  enum def Tenor {
    doc/* standardized forward maturity codes */
    enum ON; enum TN; enum SP;   // Overnight, Tomorrow-Next, Spot (T+2)
    enum W1; enum W2;            // 1 week, 2 weeks
    enum M1; enum M2; enum M3; enum M6; enum M9; // months
    enum Y1; enum Y2; enum Y3;  // years
    enum Custom;                 // non-standard tenor; see daysOffset
  }

  enum def SettlementConvention {
    doc/* market settlement convention relative to trade date */
    enum T0; enum T1; enum T2; enum T3;
  }

  enum def RateKind {
    doc/* category of FX rate */
    enum Spot;
    enum Forward;
    enum SwapPoints;
    enum Fixing;
    enum Reference;
  }

  // =========================================================
  // Core FX reference data (items)
  // =========================================================

  item def CurrencyPair {
    doc/* ordered pair of currencies used to quote an FX rate (base/quote) */
    attribute baseCurrency: Currency[1..1];
    attribute quoteCurrency: Currency[1..1];
    attribute isoPairCode: String[0..1];      // e.g., "EURUSD"
    attribute quoteConvention: QuoteConvention[0..1];
    attribute standardSettlement: SettlementConvention[0..1]; // typical: Spot=T+2 (except some pairs)
  }

  item def FXPriceSource {
    doc/* organization or mechanism that publishes FX rates or fixings */
    attribute publisher: Organizations::Organization[1..1];
    attribute name: String[0..1];             // e.g., "WM/Refinitiv 16:00", "ECB Reference"
    attribute methodologyRef: String[0..1];   // URL or citation text
  }

  // =========================================================
  // FX rates & quotes (items)
  // =========================================================

  item def FXRate {
    doc/* numerical quote expressing the price of one currency in terms of another at a time */
    attribute pair: CurrencyPair[1..1];
    attribute value: Real[1..1];
    attribute side: PriceSide[0..1];          // Mid/Bid/Ask
    attribute asOf: DateTime[1..1];
    attribute rateKind: RateKind[0..1];
    attribute basis: QuoteBasis[0..1];
    attribute priceSource: FXPriceSource[0..1];
  }

  item def FXSpotRate specializes FXRate {
    doc/* FX spot rate, typically settling on T+2 unless pair-specific conventions apply */
    attribute tradeDate: Date[0..1];
    attribute valueDate: Date[0..1];
    attribute settlement: SettlementConvention[0..1];   // usually T2; SP denotes spot
  }

  item def FXForwardPoints {
    doc/* forward points quoted relative to spot (bid/ask) */
    attribute pair: CurrencyPair[1..1];
    attribute tenor: Tenor[1..1];
    attribute pointsMid: Real[0..1];
    attribute pointsBid: Real[0..1];
    attribute pointsAsk: Real[0..1];
    attribute asOf: DateTime[1..1];
    attribute priceSource: FXPriceSource[0..1];
  }

  item def FXForwardRate specializes FXRate {
    doc/* outright forward FX rate for a given tenor or value date */
    attribute tenor: Tenor[0..1];
    attribute daysOffset: Integer[0..1];          // for custom tenor; number of days from spot
    attribute tradeDate: Date[0..1];
    attribute valueDate: Date[0..1];
  }

  item def FXSwapQuote {
    doc/* two-legged quote combining a near and far exchange of currencies */
    attribute pair: CurrencyPair[1..1];
    attribute nearLegRate: FXRate[1..1];          // commonly spot or near forward
    attribute farLegRate: FXRate[1..1];
    attribute tenor: Tenor[0..1];
    attribute points: FXForwardPoints[0..1];
    attribute asOf: DateTime[1..1];
    attribute priceSource: FXPriceSource[0..1];
  }

  item def FXFixing {
    doc/* reference rate (fixing) for a currency pair at a specified cut time and methodology */
    attribute pair: CurrencyPair[1..1];
    attribute fixingTimestamp: DateTime[1..1];
    attribute fixingValue: Real[1..1];
    attribute side: PriceSide[0..1];              // often Mid
    attribute priceSource: FXPriceSource[1..1];   // e.g., WM/Refinitiv, ECB
  }

  // =========================================================
  // Calendars & conventions (items)
  // =========================================================

  item def FXCalendarRule {
    doc/* rule defining business-day adjustments for FX value dates and holidays */
    attribute pair: CurrencyPair[0..1];
    attribute applicableCenters: BusinessCenter[0..*];
    attribute spotLagDays: Integer[0..1];         // default 2; exceptions e.g., USD/CAD
    attribute followsModifiedFollowing: Boolean[0..1];
    attribute calendar: BusinessDayCalendar[0..1];
  }

  // =========================================================
  // Time series alignment (optional, for analytics integration)
  // =========================================================

  item def FXRateSeries specializes Analytics::Indicator {
    doc/* indicator-like series definition for FX rates under a given configuration */
    attribute pair: CurrencyPair[1..1];
    attribute rateKind: RateKind[1..1];
    attribute side: PriceSide[0..1];
    attribute priceSource: FXPriceSource[0..1];
    attribute defaultFrequency: EconomicIndicators::Frequency[0..1]; // reuse IND freq if imported
  }

  item def FXRateObservation {
    doc/* one observation in an FX rate series (time-stamped quote or fixing) */
    attribute series: FXRateSeries[1..1];
    attribute observedAt: DateTime[1..1];
    attribute value: Real[1..1];
  }

  // =========================================================
  // Trade-related stubs (kept minimal in IND; detailed in FND/SEC CIs)
  // =========================================================

  item def FXSettlementInstruction {
    doc/* instruction details for settling an FX transaction */
    attribute payer: Organizations::Organization[0..1];
    attribute payee: Organizations::Organization[0..1];
    attribute amount: MonetaryAmount[0..1];
    attribute valueDate: Date[0..1];
    attribute correspondentBankText: String[0..1];
  }
}
