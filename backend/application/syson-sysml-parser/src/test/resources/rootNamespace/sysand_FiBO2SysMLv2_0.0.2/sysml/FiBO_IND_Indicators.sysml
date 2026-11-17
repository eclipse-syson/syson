// ===================================================================
// FIBO_IND_Indicators.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_IND_Indicators {
  doc/* Configuration Item (CI) package for FIBO_IND_Indicators and its exported model elements. */
  public import Indicators::*;
}

// =======================================
// Indicators.sysml
// (feature-based from Indicators.rdf; aligned with IND/FND CIs)
// =======================================
package Indicators {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import FinancialDates::*;           // ExplicitDate, ExplicitDatePeriod, Duration
  private import Analytics::*;                // Metric, Indicator, Dataset, Measure
  private import CurrencyAmount::*;           // Currency, MonetaryAmount
  private import Organizations::*;            // Organization / FormalOrganization (admin, calc agent)
  private import Locations::*;                // GeopoliticalEntity (coverage)
  private import Documents::*;                // Document, Text (methodology/spec)

  // ---- Enums / code lists ----
  enum def WeightingMethod {
    enum PriceWeighted;
    enum MarketCapWeighted;
    enum FreeFloatMarketCapWeighted;
    enum EqualWeighted;
    enum FundamentallyWeighted;
    enum VolatilityWeighted;
    enum Custom;
  }

  enum def RebalancingFrequency { enum Daily; enum Weekly; enum Monthly; enum Quarterly; enum SemiAnnual; enum Annual; enum AdHoc; }

  enum def CalculationMethod {
    enum Arithmetic;
    enum Geometric;
    enum Laspeyres;      // fixed-base
    enum Paasche;       // current-weighted
    enum Fisher;        // geometric mean of Laspeyres & Paasche
    enum ChainLinked;
    enum Other;
  }

  enum def IndexFamilyKind {
    enum Equity;
    enum FixedIncome;
    enum MultiAsset;
    enum Commodity;
    enum Currency;
    enum RealEstate;
    enum Volatility;
    enum Economic;       // general economic/composite indicators
    enum Thematic;
    enum ESG;
    enum Custom;
  }

  enum def LevelUnit { enum IndexPoints; enum Percentage; enum BasisPoints; enum Currency; }

  // ---- Methodology & governance ----
  item def IndexMethodology {
    doc/* rules defining the purpose, eligibility, selection, weighting, calculation, review, and governance of an index */
    attribute title: String[1..1];
    attribute version: String[0..1];
    attribute text: Text[0..*];
    attribute publicationDate: Date[0..1];
    attribute methodologyDocument: Document[0..1];
  }

  item def GovernanceRecord {
    doc/* record of governance roles per IOSCO principles (administrator, calculation agent, oversight committee) */
    attribute administrator: Organizations::Organization[0..1];
    attribute calculationAgent: Organizations::Organization[0..1];
    attribute oversightCommittee: Organizations::Organization[0..1];
    attribute jurisdiction: Locations::GeopoliticalEntity[0..*];
    attribute notes: String[0..*];
  }

  // ---- Core index abstractions ----
  item def Index specializes Analytics::Indicator {
    doc/* rules-based composite measure (index/benchmark) representing the value or performance of a basket of constituents under a defined methodology */
    attribute familyKind: IndexFamilyKind[1..1];
    attribute baseDate: Date[0..1];
    attribute baseLevel: Real[0..1];                 // e.g., 100 at base date
    attribute levelUnit: LevelUnit[0..1];            // points, %, currency
    attribute currency: Currency[0..1];              // reporting currency for level/return
    attribute coverageArea: Locations::GeopoliticalEntity[0..*];
    attribute methodology: IndexMethodology[0..1];
    attribute governance: GovernanceRecord[0..1];
  }

  item def PriceIndex specializes Index {
    doc/* index whose level reflects the price of underlying constituents (not consumer price indices) */
  }

  item def ReturnIndex specializes Index {
    doc/* index reflecting total return (price change plus distributions) under specified reinvestment rules */
    attribute reinvestDividends: Boolean[0..1];
    attribute reinvestCoupons: Boolean[0..1];
    attribute withholdingTaxRatePct: Real[0..1];
  }

  item def NetReturnIndex specializes ReturnIndex {
    doc/* total return after withholding taxes at a defined rate */
  }

  item def GrossReturnIndex specializes ReturnIndex {
    doc/* total return assuming gross (pre-tax) reinvestment */
  }

  item def BenchmarkIndex specializes Index {
    doc/* index designated as a benchmark for performance measurement or regulatory reporting */
    attribute benchmarkStatement: Document[0..1];
  }

  // ---- Composition model ----
  item def IndexBasket {
    doc/* ordered set of constituents with weights on an effective date range */
    attribute index: Index[1..1];
    attribute effectiveFrom: Date[1..1];
    attribute effectiveTo: Date[0..1];
    attribute weightingMethod: WeightingMethod[1..1];
    attribute calculationMethod: CalculationMethod[0..1];
  }

  item def IndexConstituent {
    doc/* one eligible component included in an index basket with an associated weight and metadata */
    attribute basket: IndexBasket[1..1];
    attribute constituentId: String[1..1];         // identifier (e.g., ISIN, Ticker, MIC+Symbol)
    attribute constituentType: String[0..1];       // e.g., Equity, Bond, Future, FX rate
    attribute weightPct: Real[0..1];               // percentage weight (0..100)
    attribute capWeightPct: Real[0..1];            // post-cap weight (if applicable)
    attribute floatAdjFactor: Real[0..1];
    attribute country: Locations::GeopoliticalEntity[0..1];
    attribute currency: Currency[0..1];
  }

  // ---- Corporate action & maintenance hooks (lightweight) ----
  item def MaintenanceEvent {
    doc/* event affecting composition or level (e.g., add/delete, split, spin-off, special cash dividend, rebalance) */
    attribute index: Index[1..1];
    attribute eventDate: Date[1..1];
    attribute eventType: String[1..1];
    attribute description: String[0..1];
    attribute impactedBasket: IndexBasket[0..1];
  }

  item def RebalanceSchedule {
    doc/* schedule defining periodic review/reconstitution and rebalancing policies */
    attribute index: Index[1..1];
    attribute frequency: RebalancingFrequency[1..1];
    attribute reviewWindow: ExplicitDatePeriod[0..1];
    attribute nextRebalanceDate: Date[0..1];
  }

  // ---- Time series / observations ----
  item def IndexSeries specializes Analytics::Indicator {
    doc/* configuration of a published index time series (price, net TR, gross TR, currency, frequency) */
    attribute index: Index[1..1];
    attribute isTotalReturn: Boolean[0..1];
    attribute isNetOfTax: Boolean[0..1];
    attribute seriesCode: String[0..1];
    attribute defaultFrequency: EconomicIndicators::Frequency[0..1]; // reuse IND frequency enum
    attribute currency: Currency[0..1];
  }

  item def IndexObservation {
    doc/* one time-stamped observation of an index series */
    attribute series: IndexSeries[1..1];
    attribute observedAt: DateTime[1..1];
    attribute periodStart: Date[0..1];
    attribute periodEnd: Date[0..1];
    attribute level: Real[0..1];                     // index level in points
    attribute returnPct: Real[0..1];                 // optional period return (%)
    attribute sourceDataset: Analytics::Dataset[0..1];
  }

  // ---- Returns & performance analytics (optional) ----
  item def PerformanceWindow {
    doc/* pre-defined window for cumulative or annualized returns */
    attribute windowLabel: String[1..1];             // e.g., "1D","1M","YTD","1Y","3Y","5Y","Since Inception"
    attribute rollingPeriod: Duration[0..1];
  }

  item def IndexPerformance {
    doc/* performance snapshot for a given series and as-of timestamp */
    attribute series: IndexSeries[1..1];
    attribute asOf: DateTime[1..1];
    attribute window: PerformanceWindow[0..*];
    attribute cumulativeReturnPct: Real[0..1];
    attribute annualizedReturnPct: Real[0..1];
    attribute volatilityPct: Real[0..1];
    attribute trackingErrorPct: Real[0..1];
    attribute informationRatio: Real[0..1];
    attribute benchmark: IndexSeries[0..1];
  }
}
