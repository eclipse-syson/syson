// ===================================================================
// FIBO_IND_EconomicIndicators.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_IND_EconomicIndicators {
  doc/* Configuration Item (CI) package for FIBO_IND_EconomicIndicators and its exported model elements. */
  public import EconomicIndicators::*;
}

// ================================
// EconomicIndicators.sysml
// (feature-based from EconomicIndicators.rdf; aligned with Utilities::Analytics)
// ================================
package EconomicIndicators {

  // ---- Imports (private) ----
  private import ScalarValues::*;            // String, Integer, Real, Boolean
  private import Time::*;                    // Date, DateTime
  private import FinancialDates::*;          // ExplicitDate, ExplicitDatePeriod, Duration
  private import Analytics::*;               // Metric, Indicator, Measure, Dataset (Utilities CI)
  private import Organizations::*;           // Organization / FormalOrganization (publisher)
  private import Locations::*;               // GeopoliticalEntity (country/region)
  private import Documents::*;               // Document, Text (methodology)
  private import CurrencyAmount::*;          // Currency, MonetaryAmount (for currency-valued indicators)

  // ---- Enums / code lists ----
  enum def Frequency {
    enum Annual; enum Quarterly; enum Monthly; enum Weekly; enum Daily; enum IntraDay;
  }

  enum def SeasonalAdjustment {
    enum NotSeasonallyAdjusted;  // NSA
    enum SeasonallyAdjusted;     // SA
    enum SAAR;                   // Seasonally Adjusted Annual Rate
  }

  enum def UnitKind {
    doc/* broad unit category for indicator values */
    enum Index;          // e.g., index level (base=100)
    enum Percentage;     // e.g., unemployment rate (%)
    enum Currency;       // e.g., GDP in currency units
    enum Count;          // e.g., housing starts count
    enum Ratio;          // e.g., debt-to-GDP
    enum RatePerAnnum;   // e.g., growth rate (annualized)
    enum Other;
  }

  enum def BaseType {
    enum FixedBaseYear;     // e.g., BaseYear = 2015 = 100
    enum ChainLinked;       // chained volume / chain-weighted
    enum None;
  }

  enum def ReleaseStatus { enum Scheduled; enum Released; enum Revised; enum Final; enum Cancelled; }
  enum def RevisionKind  { enum First; enum Second; enum Third; enum Benchmark; enum SeasonalRecalc; enum Other; }
  enum def AggregationKind { enum Level; enum Change; enum ChangeYoY; enum ChangeQoQ; enum RateAnnualized; }

  // ---- Core indicator abstractions ----

  item def EconomicIndicator specializes Analytics::Indicator {
    doc/* statistical measure summarizing an aspect of economic activity (e.g., GDP, CPI, unemployment) */
    attribute unitKind: UnitKind[1..1];
    attribute defaultFrequency: Frequency[1..1];
    attribute defaultSeasonalAdjustment: SeasonalAdjustment[0..1];
    attribute baseType: BaseType[0..1];
    attribute baseYear: Integer[0..1];                  // if fixed-base index (e.g., 2015=100)
    attribute publisher: Organizations::Organization[0..1];
    attribute coverageArea: Locations::GeopoliticalEntity[0..*]; // countries/regions
    attribute methodology: MethodologyDocument[0..1];
  }

  item def MethodologyDocument {
    doc/* document describing sources, concepts, coverage, classification, and compilation methods */
    attribute title: String[1..1];
    attribute reference: Documents::Document[0..1];
    attribute text: Text[0..*];
    attribute version: String[0..1];
    attribute publicationDate: Date[0..1];
  }

  item def SeasonalAdjustmentMethod {
    doc/* algorithm or approach for seasonal adjustment (e.g., X-13ARIMA-SEATS, TRAMO/SEATS) */
    attribute name: String[1..1];
    attribute description: String[0..1];
    attribute provider: Organizations::Organization[0..1];
  }

  // A concrete series = indicator + geo + adjustment + frequency + unit specialization
  item def IndicatorSeries {
    doc/* specific published time series for an economic indicator under a defined configuration */
    attribute indicator: EconomicIndicator[1..1];
    attribute seriesCode: String[0..1];                 // publisher series id
    attribute geography: Locations::GeopoliticalEntity[0..1];
    attribute frequency: Frequency[1..1];
    attribute seasonalAdjustment: SeasonalAdjustment[0..1];
    attribute aggregation: AggregationKind[0..1];       // level, % change, etc.
    attribute unitKind: UnitKind[1..1];
    attribute unitLabel: String[0..1];                  // e.g., "Index 2015=100", "%", "USD bn"
    attribute currency: Currency[0..1];                 // if currency-valued (e.g., GDP)
    attribute baseType: BaseType[0..1];
    attribute baseYear: Integer[0..1];
    attribute seasonalMethod: SeasonalAdjustmentMethod[0..1];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
    attribute sourceDataset: Analytics::Dataset[0..1];
  }

  // Time-stamped observation
  item def IndicatorObservation {
    doc/* one observation for a series, referring to a period and a value */
    attribute series: IndicatorSeries[1..1];
    attribute periodStart: Date[1..1];
    attribute periodEnd: Date[0..1];
    attribute periodLabel: String[0..1];                // e.g., "2025-Q3", "2025-09"
    attribute value: Real[0..1];
    attribute monetaryValue: MonetaryAmount[0..1];      // alternative for currency-valued level
    attribute release: DataRelease[0..1];
    attribute revision: RevisionRecord[0..1];
  }

  // Release calendar & events
  item def DataRelease {
    doc/* official publication event for an indicator or group of indicators */
    attribute title: String[1..1];
    attribute releasedBy: Organizations::Organization[1..1];
    attribute referencePeriod: ExplicitDatePeriod[0..1];
    attribute scheduledAt: DateTime[0..1];
    attribute releasedAt: DateTime[0..1];
    attribute status: ReleaseStatus[0..1];
    attribute notes: String[0..*];
  }

  item def EconomicCalendar {
    doc/* schedule of upcoming and past releases relevant to a jurisdiction or market */
    attribute jurisdiction: Locations::GeopoliticalEntity[0..1];
    attribute release: DataRelease[0..*];
  }

  // Revisions / vintages
  item def RevisionRecord {
    doc/* description of a change to previously released figures (vintage tracking) */
    attribute kind: RevisionKind[1..1];
    attribute revisedAt: DateTime[1..1];
    attribute reason: String[0..1];
    attribute supersedesRelease: DataRelease[0..1];
  }

  item def VintageDataset {
    doc/* snapshot of all series values as of a particular release date/time */
    attribute asOfRelease: DataRelease[1..1];
    attribute dataset: Analytics::Dataset[0..1];
  }

  // ---- Common indicator specializations (for typing convenience) ----

  item def GrossDomesticProduct specializes EconomicIndicator {
    doc/* market value of all final goods and services produced within a country in a given period */
  }

  item def RealGrossDomesticProduct specializes GrossDomesticProduct {
    doc/* inflation-adjusted GDP (chained or fixed-base) */
  }

  item def ConsumerPriceIndex specializes EconomicIndicator {
    doc/* measure of the average change over time in prices paid by consumers for a market basket of goods and services */
  }

  item def CoreConsumerPriceIndex specializes ConsumerPriceIndex {
    doc/* CPI excluding volatile components (such as food and energy) depending on publisher definition */
  }

  item def ProducerPriceIndex specializes EconomicIndicator {
    doc/* measure of average change over time in selling prices received by domestic producers for their output */
  }

  item def UnemploymentRate specializes EconomicIndicator {
    doc/* percentage of the labor force that is jobless and actively seeking employment */
    attribute workingAgeDefinition: String[0..1];
  }

  item def IndustrialProductionIndex specializes EconomicIndicator {
    doc/* real output of manufacturing, mining, and utilities sectors */
  }

  item def RetailSales specializes EconomicIndicator {
    doc/* estimate of total receipts of retail stores, used to gauge consumer demand */
  }

  item def PurchasingManagersIndex specializes EconomicIndicator {
    doc/* diffusion index summarizing economic trends in the manufacturing/services sectors based on surveys */
  }

  item def HousingStarts specializes EconomicIndicator {
    doc/* number of new privately-owned housing units on which construction has started */
  }

  item def ConsumerConfidenceIndex specializes EconomicIndicator {
    doc/* measure of consumer optimism about the state of the economy */
  }

  item def TradeBalance specializes EconomicIndicator {
    doc/* difference between a country's exports and imports of goods and services */
  }

  // ---- Convenience links between indicator and its canonical series ----

  item def IndicatorSeriesLink {
    doc/* mapping from an indicator specialization to its canonical published series for a geography */
    attribute indicator: EconomicIndicator[1..1];
    attribute canonicalSeries: IndicatorSeries[1..1];
    attribute geography: Locations::GeopoliticalEntity[0..1];
  }
}
// ===================================================================
// FIBO_IND_NorthAmericaEconomicIndicators.sysml
// CI Aggregator (public re-export of localized indicator subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_IND_NorthAmericaEconomicIndicators {
  doc/* Configuration Item (CI) package for FIBO_IND_NorthAmericaEconomicIndicators and its exported model elements. */
  public import USEconomicIndicators::*;
  public import CAEconomicIndicators::*;
}

// =======================================
// USEconomicIndicators.sysml
// (feature-based from USEconomicIndicators.rdf)
// =======================================
package USEconomicIndicators {

  // ---- Imports (private) ----
  private import ScalarValues::*;               // String, Integer, Real, Boolean
  private import Time::*;                       // Date, DateTime
  private import FinancialDates::*;             // ExplicitDate, ExplicitDatePeriod
  private import Analytics::*;                  // Metric, Indicator, Dataset
  private import EconomicIndicators::*;         // base EconomicIndicator, IndicatorSeries, etc.
  private import Organizations::*;              // Organization for publishers
  private import Locations::*;                  // GeopoliticalEntity / Country
  private import Documents::*;                  // Document, Text
  private import CurrencyAmount::*;             // MonetaryAmount, Currency

  // ---- Convenience: canonical geography for U.S. ----
  item def UnitedStatesCountry specializes Locations::GeopoliticalEntity {
    doc/* United States of America â€“ canonical geography for U.S. indicators */
  }

  // ---- Core U.S. publishers (as references to Organizations package) ----
  // (Use your Organizations CI to define concrete instances if desired)
  item def USDatasource {
    doc/* reference holder for common U.S. publisher organizations */
    attribute bureauOfLaborStatistics: Organizations::Organization[0..1];   // BLS
    attribute bureauOfEconomicAnalysis: Organizations::Organization[0..1];  // BEA
    attribute censusBureau: Organizations::Organization[0..1];              // U.S. Census
    attribute federalReserve: Organizations::Organization[0..1];            // Board of Governors
    attribute ismInstitute: Organizations::Organization[0..1];              // ISM
  }

  // ---- U.S. indicator specializations ----

  item def USCPI specializes EconomicIndicators::ConsumerPriceIndex {
    doc/* U.S. Consumer Price Index for All Urban Consumers (CPI-U) published by BLS */
  }

  item def USCoreCPI specializes EconomicIndicators::CoreConsumerPriceIndex {
    doc/* U.S. Core CPI (ex. food & energy) published by BLS */
  }

  item def USPCEPriceIndex specializes EconomicIndicators::EconomicIndicator {
    doc/* U.S. Personal Consumption Expenditures (PCE) Price Index published by BEA */
  }

  item def USCorePCEPriceIndex specializes USPCEPriceIndex {
    doc/* U.S. Core PCE Price Index (ex. food & energy) published by BEA */
  }

  item def USGDP specializes EconomicIndicators::GrossDomesticProduct {
    doc/* U.S. nominal GDP (current dollars) published by BEA */
  }

  item def USRealGDP specializes EconomicIndicators::RealGrossDomesticProduct {
    doc/* U.S. real GDP (chained dollars) published by BEA */
  }

  item def USUnemploymentRate specializes EconomicIndicators::UnemploymentRate {
    doc/* U.S. unemployment rate (U-3) published by BLS */
  }

  item def USNonfarmPayrolls specializes EconomicIndicators::EconomicIndicator {
    doc/* U.S. total nonfarm payroll employment (level / change) published by BLS */
  }

  item def USRetailSales specializes EconomicIndicators::RetailSales {
    doc/* U.S. advance monthly retail sales published by Census Bureau */
  }

  item def USIndustrialProduction specializes EconomicIndicators::IndustrialProductionIndex {
    doc/* U.S. industrial production index published by Federal Reserve (G.17) */
  }

  item def USHousingStarts specializes EconomicIndicators::HousingStarts {
    doc/* U.S. new privately-owned housing units started (SAAR) published by Census Bureau */
  }

  item def USISMManufacturingPMI specializes EconomicIndicators::PurchasingManagersIndex {
    doc/* U.S. ISM Manufacturing PMI (diffusion index) published by ISM */
  }

  item def USISMServicesPMI specializes EconomicIndicators::PurchasingManagersIndex {
    doc/* U.S. ISM Services PMI (diffusion index) published by ISM */
  }

  item def USTradeBalance specializes EconomicIndicators::TradeBalance {
    doc/* U.S. international trade in goods and services balance published by BEA/Census */
  }

  // ---- Example canonical series bindings (optional templates) ----

  item def Series_US_CPI_AllItems_SA {
    doc/* canonical CPI-U All Items SA monthly series */
    attribute series: EconomicIndicators::IndicatorSeries[1..1];
    attribute indicator: USCPI[1..1];
  }

  item def Series_US_UnemploymentRate_SA {
    doc/* canonical unemployment rate SA monthly series */
    attribute series: EconomicIndicators::IndicatorSeries[1..1];
    attribute indicator: USUnemploymentRate[1..1];
  }
}

// =======================================
// CAEconomicIndicators.sysml
// (feature-based from CAEconomicIndicators.rdf)
// =======================================
package CAEconomicIndicators {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import FinancialDates::*;
  private import Analytics::*;
  private import EconomicIndicators::*;         // base EconomicIndicator, IndicatorSeries, etc.
  private import Organizations::*;              // Organization for publishers (Statistics Canada, BoC)
  private import Locations::*;                  // GeopoliticalEntity / Country
  private import Documents::*;
  private import CurrencyAmount::*;

  // ---- Convenience: canonical geography for Canada ----
  item def CanadaCountry specializes Locations::GeopoliticalEntity {
    doc/* Canada â€“ canonical geography for Canadian indicators */
  }

  // ---- Core Canadian publishers (references) ----
  item def CADatasource {
    doc/* reference holder for Canadian publisher organizations */
    attribute statisticsCanada: Organizations::Organization[0..1];   // StatCan
    attribute bankOfCanada: Organizations::Organization[0..1];       // BoC
    attribute cmhc: Organizations::Organization[0..1];               // CMHC (housing)
    attribute iveyPMIOrg: Organizations::Organization[0..1];         // Ivey PMI publishers
  }

  // ---- Canada indicator specializations ----

  item def CACPI specializes EconomicIndicators::ConsumerPriceIndex {
    doc/* Canada CPI (All-items) published by Statistics Canada */
  }

  item def CACoreCPI specializes EconomicIndicators::CoreConsumerPriceIndex {
    doc/* Canada CPI â€“ preferred core measures (BoC trim/median/common) â€“ modeled as a family */
  }

  item def CACoreCPITrim specializes CACoreCPI {
    doc/* Bank of Canada CPI-trim core measure */
  }

  item def CACoreCPIMedian specializes CACoreCPI {
    doc/* Bank of Canada CPI-median core measure */
  }

  item def CACoreCPICommon specializes CACoreCPI {
    doc/* Bank of Canada CPI-common core measure */
  }

  item def CAGDP specializes EconomicIndicators::GrossDomesticProduct {
    doc/* Canada nominal GDP published by Statistics Canada */
  }

  item def CARealGDP specializes EconomicIndicators::RealGrossDomesticProduct {
    doc/* Canada real GDP (chained dollars) published by Statistics Canada */
  }

  item def CAUnemploymentRate specializes EconomicIndicators::UnemploymentRate {
    doc/* Canada unemployment rate (LFS) published by Statistics Canada */
  }

  item def CARetailSales specializes EconomicIndicators::RetailSales {
    doc/* Canada retail trade sales published by Statistics Canada */
  }

  item def CAHousingStarts specializes EconomicIndicators::HousingStarts {
    doc/* Canada housing starts published by CMHC */
  }

  item def CAIveyPMI specializes EconomicIndicators::PurchasingManagersIndex {
    doc/* Canada Ivey PMI (diffusion index) */
  }

  item def CATradeBalance specializes EconomicIndicators::TradeBalance {
    doc/* Canada international merchandise trade balance (goods/services) */
  }

  // ---- Example canonical series bindings (optional templates) ----

  item def Series_CA_CPI_AllItems_SA {
    doc/* canonical Canada CPI All-items SA monthly series */
    attribute series: EconomicIndicators::IndicatorSeries[1..1];
    attribute indicator: CACPI[1..1];
  }

  item def Series_CA_UnemploymentRate_SA {
    doc/* canonical Canada unemployment rate SA monthly series */
    attribute series: EconomicIndicators::IndicatorSeries[1..1];
    attribute indicator: CAUnemploymentRate[1..1];
  }
}
