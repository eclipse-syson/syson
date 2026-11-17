// ===================================================================
// FIBO_MD_CIVTemporal.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_MD_CIVTemporal {
  doc/* Configuration Item (CI) package for FIBO_MD_CIVTemporal and its exported model elements. */
  public import FundsTemporal::*;
}

// =======================================
// FundsTemporal.sysml
// (feature-based from FundsTemporal.rdf; aligned with FBC and IND domains)
// =======================================
package FundsTemporal {

  // ---- Imports (private) ----
  private import ScalarValues::*;            // String, Integer, Real, Boolean
  private import Time::*;                    // Date, DateTime
  private import FinancialDates::*;          // ExplicitDate, ExplicitDatePeriod, Duration
  private import CurrencyAmount::*;          // MonetaryAmount, Currency
  private import Organizations::*;           // Organization / FormalOrganization (fund manager, custodian)
  private import Parties::*;                 // Party (investors, distributors)
  private import FinancialInstruments::*;    // FundInstrument, FinancialInstrument (from FBC)
  private import Indicators::*;              // Index, Indicator (for benchmark references)
  private import Analytics::*;               // Measure, Dataset, Metric (for analytics)

  // =========================================================
  // Enumerations
  // =========================================================
  enum def FundValuationMethod {
    doc/* defines the basis on which a fundâ€™s net asset value (NAV) is calculated */
    enum MarkToMarket;
    enum MarkToModel;
    enum AmortizedCost;
    enum Hybrid;
  }

  enum def PricingFrequency { enum Daily; enum Weekly; enum BiWeekly; enum Monthly; enum Quarterly; enum AdHoc; }

  enum def SubscriptionStatus { enum Open; enum Closed; enum Restricted; }

  enum def FundEventType {
    enum Launch;
    enum NAVPublication;
    enum DividendDistribution;
    enum SubscriptionWindow;
    enum RedemptionWindow;
    enum Merger;
    enum Termination;
    enum Other;
  }

  // =========================================================
  // Core fund temporal data
  // =========================================================

  item def CollectiveInvestmentVehicle {
    doc/* collective investment vehicle (fund, SICAV, trust, partnership, etc.) that pools investor money under defined terms */
    attribute fundId: String[1..1];
    attribute name: String[1..1];
    attribute legalFormText: String[0..1];
    attribute domicile: Locations::GeopoliticalEntity[0..1];
    attribute currency: Currency[0..1];
    attribute launchDate: Date[0..1];
    attribute terminationDate: Date[0..1];
    attribute managementCompany: Organizations::Organization[0..1];
    attribute custodian: Organizations::Organization[0..1];
    attribute administrator: Organizations::Organization[0..1];
    attribute fundInstrument: FinancialInstruments::FundInstrument[0..1];
    attribute benchmarkIndex: Indicators::Index[0..1];
  }

  item def NAVRecord {
    doc/* net asset value (NAV) record capturing valuation and units outstanding as of a valuation date */
    attribute fund: CollectiveInvestmentVehicle[1..1];
    attribute navDate: Date[1..1];
    attribute navPerShare: MonetaryAmount[1..1];
    attribute totalAssetsAmount: MonetaryAmount[0..1];
    attribute totalLiabilitiesAmount: MonetaryAmount[0..1];
    attribute unitsOutstanding: Real[0..1];
    attribute valuationMethod: FundValuationMethod[0..1];
  }

  item def NAVHistory {
    doc/* collection of NAV records representing the historical valuation of a fund */
    attribute fund: CollectiveInvestmentVehicle[1..1];
    attribute record: NAVRecord[1..*];
  }

  item def DividendDistributionRecord {
    doc/* distribution of income or realized gains to investors as dividends */
    attribute fund: CollectiveInvestmentVehicle[1..1];
    attribute recordDate: Date[1..1];
    attribute paymentDate: Date[0..1];
    attribute distributionPerShare: MonetaryAmount[1..1];
    attribute totalDistributionAmount: MonetaryAmount[0..1];
  }

  item def SubscriptionWindow {
    doc/* time period during which subscriptions or redemptions are allowed */
    attribute fund: CollectiveInvestmentVehicle[1..1];
    attribute windowTypeText: String[0..1];  // e.g., "Subscription", "Redemption"
    attribute openDate: Date[1..1];
    attribute closeDate: Date[0..1];
    attribute status: SubscriptionStatus[0..1];
  }

  item def FundEvent {
    doc/* lifecycle event affecting a fund (launch, NAV publication, merger, termination, etc.) */
    attribute eventType: FundEventType[1..1];
    attribute fund: CollectiveInvestmentVehicle[1..1];
    attribute eventDate: Date[1..1];
    attribute description: String[0..1];
  }

  // =========================================================
  // Fund-level metrics and analytics
  // =========================================================

  item def FundPerformance specializes Analytics::Indicator {
    doc/* indicator describing cumulative and periodic performance of a fund over time */
    attribute fund: CollectiveInvestmentVehicle[1..1];
    attribute measurementWindow: FinancialDates::ExplicitDatePeriod[0..1];
    attribute returnPct: Real[0..1];
    attribute benchmarkRelativeReturnPct: Real[0..1];
    attribute volatilityPct: Real[0..1];
    attribute sharpeRatio: Real[0..1];
    attribute trackingErrorPct: Real[0..1];
    attribute benchmark: Indicators::Index[0..1];
  }

  item def FundAUMRecord {
    doc/* record of total assets under management (AUM) as of a date */
    attribute fund: CollectiveInvestmentVehicle[1..1];
    attribute asOfDate: Date[1..1];
    attribute aumAmount: MonetaryAmount[1..1];
    attribute inflowsAmount: MonetaryAmount[0..1];
    attribute outflowsAmount: MonetaryAmount[0..1];
    attribute netFlowAmount: MonetaryAmount[0..1];
  }

  item def FundHoldingsSnapshot {
    doc/* composition of holdings as of a date, with weightings per asset or sector */
    attribute fund: CollectiveInvestmentVehicle[1..1];
    attribute asOfDate: Date[1..1];
    attribute holdingCount: Integer[0..1];
    attribute topHoldingsText: String[0..*];     // simplified textual placeholder
    attribute sectorAllocationText: String[0..*];
  }

  // =========================================================
  // Fund share classes and temporal relationships
  // =========================================================

  item def ShareClass {
    doc/* class of fund shares with distinct fee, currency, or distribution characteristics */
    attribute shareClassCode: String[1..1];
    attribute fund: CollectiveInvestmentVehicle[1..1];
    attribute currency: Currency[0..1];
    attribute isin: String[0..1];
    attribute classLaunchDate: Date[0..1];
    attribute managementFeePct: Real[0..1];
    attribute performanceFeePct: Real[0..1];
    attribute minimumInvestmentAmount: MonetaryAmount[0..1];
  }

  item def ShareClassNAVRecord specializes NAVRecord {
    doc/* NAV record specific to a share class */
    attribute shareClass: ShareClass[1..1];
  }

  item def ShareClassDividendRecord specializes DividendDistributionRecord {
    doc/* dividend distribution record specific to a share class */
    attribute shareClass: ShareClass[1..1];
  }

  // =========================================================
  // Relationships between funds and temporal datasets
  // =========================================================

  item def FundSeriesLink {
    doc/* mapping between a fund and associated analytical or historical datasets */
    attribute fund: CollectiveInvestmentVehicle[1..1];
    attribute dataset: Analytics::Dataset[1..1];
    attribute description: String[0..1];
  }

  item def FundFamily {
    doc/* grouping of related funds under a common sponsor or umbrella */
    attribute familyName: String[1..1];
    attribute sponsor: Organizations::Organization[0..1];
    attribute fund: CollectiveInvestmentVehicle[0..*];
  }

  item def FundMergerEvent specializes FundEvent {
    doc/* event capturing the merger or consolidation of funds */
    attribute acquiringFund: CollectiveInvestmentVehicle[1..1];
    attribute acquiredFund: CollectiveInvestmentVehicle[1..1];
    attribute exchangeRatio: Real[0..1];
  }

  item def FundTerminationEvent specializes FundEvent {
    doc/* event marking the closure or liquidation of a fund */
    attribute terminationReasonText: String[0..1];
    attribute liquidationDate: Date[0..1];
  }
}
