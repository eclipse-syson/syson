// ===================================================================
// FIBO_MD_DerivativesTemporal.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_MD_DerivativesTemporal {
  doc/* Configuration Item (CI) package for FIBO_MD_DerivativesTemporal and its exported model elements. */
  public import FuturesTemporal::*;
  public import ETOptionsTemporal::*;
}

// =======================================
// FuturesTemporal.sysml
// (feature-based from FuturesTemporal.rdf; aligned with FBC/DER domains)
// =======================================
package FuturesTemporal {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import FinancialDates::*;           // ExplicitDate, Duration, RegularSchedule
  private import CurrencyAmount::*;           // MonetaryAmount, Currency
  private import Markets::*;                  // TradingVenue, MarketOperator (from FBC)
  private import DerivativesBasics::*;     // FuturesContract, ForwardContract, SwapContract, etc.
  private import InstrumentPricing::*;        // PriceQuote, ValuationReport (from FBC)
  private import Analytics::*;                // Dataset, Metric, Indicator
  private import Organizations::*;            // Organization (exchange, clearing house)
  private import LegalCorePackage::*;         // Law (for governing agreements)
  private import JurisdictionPackage::*;      // Jurisdiction

  // =========================================================
  // Enumerations
  // =========================================================
  enum def FuturesCategory { enum Financial; enum Commodity; enum Index; enum InterestRate; enum Currency; enum Volatility; enum Weather; enum Other; }
  enum def ContractMonthCode { enum F; enum G; enum H; enum J; enum K; enum M; enum N; enum Q; enum U; enum V; enum X; enum Z; }
  enum def ExpirationCycle { enum Monthly; enum Quarterly; enum BiMonthly; enum Custom; }
  enum def FuturesStatus { enum Listed; enum Expired; enum Delisted; enum Suspended; enum CashSettled; }
  enum def SettlementType { enum Physical; enum Cash; }
  enum def PriceLimitType { enum Daily; enum Static; enum Dynamic; }

  // =========================================================
  // Core contract temporal model
  // =========================================================

  item def FuturesContractRef {
    doc/* reference to a futures contract (generic and maturity-specific) */
    attribute contract: FuturesAndForwards::FuturesContract[0..1];
    attribute exchangeSymbol: String[0..1];          // e.g., "ESZ5"
    attribute contractCode: String[0..1];
    attribute underlyingAssetText: String[0..1];
    attribute category: FuturesCategory[0..1];
    attribute exchange: Markets::TradingVenue[0..1];
    attribute clearingHouse: Organizations::Organization[0..1];
    attribute expirationDate: Date[0..1];
    attribute settlementType: SettlementType[0..1];
    attribute status: FuturesStatus[0..1];
  }

  item def FuturesSeries {
    doc/* continuous futures series representing a sequence of contract months */
    attribute name: String[1..1];
    attribute rootSymbol: String[0..1];             // e.g., "ES" for S&P 500 E-mini
    attribute contractMonthCode: ContractMonthCode[0..1];
    attribute expirationCycle: ExpirationCycle[0..1];
    attribute exchange: Markets::TradingVenue[0..1];
    attribute firstListedDate: Date[0..1];
    attribute lastTradeDate: Date[0..1];
    attribute category: FuturesCategory[0..1];
  }

  // =========================================================
  // Price and volume time series
  // =========================================================

  item def FuturesQuote {
    doc/* daily or intraday quote for a futures contract */
    attribute contractRef: FuturesContractRef[1..1];
    attribute asOf: DateTime[1..1];
    attribute openPrice: Real[0..1];
    attribute highPrice: Real[0..1];
    attribute lowPrice: Real[0..1];
    attribute closePrice: Real[0..1];
    attribute settlementPrice: Real[0..1];
    attribute volumeContracts: Integer[0..1];
    attribute openInterestContracts: Integer[0..1];
    attribute priceLimitType: PriceLimitType[0..1];
  }

  item def FuturesSeriesDataset specializes Analytics::Dataset {
    doc/* dataset capturing continuous or composite futures series (roll-adjusted) */
    attribute futuresSeries: FuturesSeries[1..1];
    attribute rollConventionText: String[0..1];
    attribute adjustmentMethodText: String[0..1];    // e.g., "Panama", "Back-adjusted"
  }

  // =========================================================
  // Lifecycle events and adjustments
  // =========================================================

  item def FuturesEvent {
    doc/* lifecycle event for a futures contract (listing, roll, expiry, etc.) */
    attribute contractRef: FuturesContractRef[1..1];
    attribute eventType: String[1..1];
    attribute eventDate: Date[1..1];
    attribute description: String[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  item def RollEvent specializes FuturesEvent {
    doc/* event representing the roll from one contract to another in a series */
    attribute fromContract: FuturesContractRef[0..1];
    attribute toContract: FuturesContractRef[0..1];
    attribute rollDate: Date[0..1];
  }

  // =========================================================
  // Analytics and valuation
  // =========================================================

  item def FuturesValuation specializes InstrumentPricing::ValuationReport {
    doc/* daily mark-to-market valuation for a futures contract or portfolio */
    attribute contractRef: FuturesContractRef[1..1];
    attribute markToMarketPrice: Real[0..1];
    attribute variationMarginAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute notionalValue: CurrencyAmount::MonetaryAmount[0..1];
  }

  item def FuturesPosition {
    doc/* open position record tracking long/short exposure */
    attribute holder: Parties::Party[1..1];
    attribute contractRef: FuturesContractRef[1..1];
    attribute positionDate: Date[1..1];
    attribute quantityContracts: Integer[1..1];
    attribute averagePrice: Real[0..1];
    attribute unrealizedPnLAmount: CurrencyAmount::MonetaryAmount[0..1];
  }

  item def FuturesPnLRecord {
    doc/* realized or unrealized profit and loss for a position */
    attribute position: FuturesPosition[1..1];
    attribute asOf: Date[1..1];
    attribute realizedPnLAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute unrealizedPnLAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute cumulativePnLAmount: CurrencyAmount::MonetaryAmount[0..1];
  }
}

// =======================================
// ETOptionsTemporal.sysml
// (feature-based from ETOptionsTemporal.rdf; equity & ETF options temporal model)
// =======================================
package ETOptionsTemporal {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import FinancialDates::*;           // ExplicitDate, Duration, RegularSchedule
  private import CurrencyAmount::*;           // MonetaryAmount, Currency
  private import Markets::*;                  // TradingVenue, MarketOperator
  private import DerivativesBasics::*;     // OptionContract, DerivativeInstrumentRef
  private import InstrumentPricing::*;        // PriceQuote, ValuationReport
  private import Analytics::*;                // Dataset, Metric
  private import Organizations::*;            // Organization (exchange, clearinghouse)
  private import Parties::*;                  // Party (traders, holders)

  // =========================================================
  // Enumerations
  // =========================================================
  enum def OptionType { enum Call; enum Put; }
  enum def OptionStyle { enum American; enum European; enum Bermudan; enum Asian; enum Barrier; enum Digital; enum Other; }
  enum def OptionSettlement { enum Physical; enum Cash; }
  enum def OptionLifecycleStatus { enum Listed; enum Exercised; enum Expired; enum Assigned; enum Cancelled; }
  enum def OptionSeriesType { enum SingleStock; enum Index; enum ETF; enum FuturesOption; enum Custom; }

  // =========================================================
  // Core option & series model
  // =========================================================

  item def OptionContractRef {
    doc/* reference to a specific listed or OTC option contract */
    attribute underlyingInstrumentText: String[0..1];  // underlying ticker or index
    attribute optionType: OptionType[1..1];
    attribute optionStyle: OptionStyle[1..1];
    attribute strikePrice: CurrencyAmount::MonetaryAmount[1..1];
    attribute expirationDate: Date[1..1];
    attribute settlement: OptionSettlement[0..1];
    attribute seriesType: OptionSeriesType[0..1];
    attribute exchange: Markets::TradingVenue[0..1];
    attribute clearingHouse: Organizations::Organization[0..1];
    attribute status: OptionLifecycleStatus[0..1];
  }

  item def OptionSeries {
    doc/* defined set of options with shared characteristics (underlying, expiration, strike grid) */
    attribute seriesCode: String[1..1];
    attribute underlyingSymbol: String[0..1];
    attribute expirationDate: Date[1..1];
    attribute exchange: Markets::TradingVenue[0..1];
    attribute listingDate: Date[0..1];
    attribute delistingDate: Date[0..1];
    attribute seriesType: OptionSeriesType[0..1];
  }

  // =========================================================
  // Market data (quotes and greeks)
  // =========================================================

  item def OptionQuote {
    doc/* end-of-day or intraday option quote with bid/ask, greeks, and volume */
    attribute contractRef: OptionContractRef[1..1];
    attribute asOf: DateTime[1..1];
    attribute bidPrice: Real[0..1];
    attribute askPrice: Real[0..1];
    attribute midPrice: Real[0..1];
    attribute lastPrice: Real[0..1];
    attribute impliedVolatilityPct: Real[0..1];
    attribute delta: Real[0..1];
    attribute gamma: Real[0..1];
    attribute vega: Real[0..1];
    attribute theta: Real[0..1];
    attribute rho: Real[0..1];
    attribute volumeContracts: Integer[0..1];
    attribute openInterestContracts: Integer[0..1];
  }

  item def OptionSeriesDataset specializes Analytics::Dataset {
    doc/* dataset containing time series of option quotes or derived implied vol surfaces */
    attribute optionSeries: OptionSeries[1..1];
    attribute surfaceConstructionMethodText: String[0..1];
  }

  // =========================================================
  // Lifecycle events and exercises
  // =========================================================

  item def OptionEvent {
    doc/* event in an option's lifecycle (listing, exercise, assignment, expiration, etc.) */
    attribute contractRef: OptionContractRef[1..1];
    attribute eventType: String[1..1];
    attribute eventDate: Date[1..1];
    attribute description: String[0..1];
  }

  item def ExerciseEvent specializes OptionEvent {
    doc/* exercise of an option contract */
    attribute holder: Parties::Party[1..1];
    attribute quantity: Integer[1..1];
    attribute settlementAmount: CurrencyAmount::MonetaryAmount[0..1];
  }

  item def ExpirationEvent specializes OptionEvent {
    doc/* expiration event when option ceases to exist */
    attribute expirationDate: Date[1..1];
    attribute settlement: OptionSettlement[0..1];
  }

  // =========================================================
  // Valuation and analytics
  // =========================================================

  item def OptionValuation specializes InstrumentPricing::ValuationReport {
    doc/* valuation report including implied volatility and risk measures */
    attribute contractRef: OptionContractRef[1..1];
    attribute modelName: String[0..1];
    attribute modelVersion: String[0..1];
    attribute impliedVolatilityPct: Real[0..1];
    attribute delta: Real[0..1];
    attribute gamma: Real[0..1];
    attribute vega: Real[0..1];
    attribute theta: Real[0..1];
    attribute rho: Real[0..1];
  }

  item def VolatilitySurface {
    doc/* surface of implied volatilities across strikes and maturities */
    attribute underlyingSymbol: String[1..1];
    attribute asOf: Date[1..1];
    attribute surfaceDataset: Analytics::Dataset[1..1];
    attribute modelMethodText: String[0..1];
  }
}
