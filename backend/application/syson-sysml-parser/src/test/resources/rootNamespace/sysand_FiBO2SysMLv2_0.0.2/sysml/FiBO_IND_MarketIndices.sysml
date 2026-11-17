// ===================================================================
// FIBO_IND_MarketIndices.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_IND_MarketIndices {
  doc/* Configuration Item (CI) package for FIBO_IND_MarketIndices and its exported model elements. */
  public import BasketIndices::*;
  public import EquityIndexExampleIndividuals::*;
}

// =======================================
// BasketIndices.sysml
// (feature-based from BasketIndices.rdf; aligned with Indicators CI)
// =======================================
package BasketIndices {

  // ---- Imports (private) ----
  private import ScalarValues::*;            // String, Integer, Real, Boolean
  private import Time::*;                    // Date, DateTime
  private import FinancialDates::*;          // ExplicitDate, Duration, ExplicitDatePeriod
  private import Indicators::*;              // Index, IndexBasket, IndexConstituent, IndexObservation
  private import CurrencyAmount::*;          // Currency, MonetaryAmount
  private import Organizations::*;           // Organization (index sponsors, calc agents)
  private import Locations::*;               // GeopoliticalEntity (region, market scope)
  private import Documents::*;               // Document, Text (methodology)

  // ---- Enumerations ----
  enum def BasketType { enum Equity; enum FixedIncome; enum Commodity; enum Currency; enum MultiAsset; enum ESG; enum Thematic; enum Other; }
  enum def BasketPurpose { enum Benchmark; enum Strategy; enum Custom; enum Hedge; enum Research; enum Derived; }

  // ---- Core basket definition ----
  item def BasketIndex specializes Indicators::Index {
    doc/* index that tracks a defined basket of instruments or assets under a weighting methodology */
    attribute basketType: BasketType[1..1];
    attribute purpose: BasketPurpose[0..1];
    attribute sponsor: Organizations::Organization[0..1];
    attribute region: Locations::GeopoliticalEntity[0..1];
  }

  item def BasketConstituent specializes Indicators::IndexConstituent {
    doc/* constituent in a basket index; may include non-listed or custom assets */
    attribute identifierText: String[0..1];
    attribute sourceMarket: String[0..1];
  }

  item def BasketMethodology specializes Indicators::IndexMethodology {
    doc/* methodology specific to a basket index (selection, weighting, rebalancing) */
    attribute inclusionCriteriaText: String[0..*];
    attribute exclusionCriteriaText: String[0..*];
    attribute weightingRuleText: String[0..*];
    attribute reviewFrequencyText: String[0..1];
  }

  item def BasketIndexBasket specializes Indicators::IndexBasket {
    doc/* basket composition of a basket index as of an effective date */
    attribute basketIndex: BasketIndex[1..1];
    attribute methodology: BasketMethodology[0..1];
  }

  // ---- Maintenance and updates ----
  item def BasketRebalance specializes Indicators::MaintenanceEvent {
    doc/* event representing the reconstitution or rebalancing of a basket index */
    attribute basketIndex: BasketIndex[1..1];
    attribute notes: String[0..*];
  }

  item def BasketIndexSeries specializes Indicators::IndexSeries {
    doc/* published time series of basket index levels */
    attribute basketIndex: BasketIndex[1..1];
  }

  item def BasketIndexObservation specializes Indicators::IndexObservation {
    doc/* observation of a basket index level */
    attribute basketIndex: BasketIndex[1..1];
  }

  item def BasketFamily {
    doc/* group of related basket indices under a common methodology */
    attribute familyName: String[1..1];
    attribute parentOrganization: Organizations::Organization[0..1];
    attribute baseMethodology: BasketMethodology[0..1];
    attribute basketIndices: BasketIndex[0..*];
  }
}

// =======================================
// EquityIndexExampleIndividuals.sysml
// (feature-based from EquityIndexExampleIndividuals.rdf)
// =======================================
package EquityIndexExampleIndividuals {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import Indicators::*;
  private import BasketIndices::*;
  private import CurrencyAmount::*;
  private import Locations::*;
  private import Organizations::*;

  // ---- Example equity index families (canonical market benchmarks) ----

  item def SAndP500 specializes BasketIndices::BasketIndex {
    doc/* S&P 500 Index â€“ U.S. large-cap equity market benchmark */
  }

  item def NASDAQComposite specializes BasketIndices::BasketIndex {
    doc/* NASDAQ Composite Index â€“ U.S. tech-weighted equity benchmark */
  }

  item def DowJonesIndustrialAverage specializes BasketIndices::BasketIndex {
    doc/* Dow Jones Industrial Average â€“ U.S. blue-chip price-weighted index */
  }

  item def MSCIWorld specializes BasketIndices::BasketIndex {
    doc/* MSCI World Index â€“ global developed markets equity benchmark */
  }

  item def MSCIEM specializes BasketIndices::BasketIndex {
    doc/* MSCI Emerging Markets Index â€“ global emerging markets equity benchmark */
  }

  item def FTSE100 specializes BasketIndices::BasketIndex {
    doc/* FTSE 100 Index â€“ U.K. large-cap equity benchmark */
  }

  item def Nikkei225 specializes BasketIndices::BasketIndex {
    doc/* Nikkei 225 Index â€“ Japan equity benchmark */
  }

  item def EuroStoxx50 specializes BasketIndices::BasketIndex {
    doc/* Euro Stoxx 50 Index â€“ Eurozone large-cap equity benchmark */
  }

  // ---- Example series bindings ----

  item def Series_SP500_Price specializes BasketIndices::BasketIndexSeries {
    doc/* canonical price index series for S&P 500 */
  }

  item def Series_SP500_TotalReturn specializes BasketIndices::BasketIndexSeries {
    doc/* canonical total return series for S&P 500 */
  }

  item def Series_MSciWorld_Price specializes BasketIndices::BasketIndexSeries {
    doc/* canonical price index series for MSCI World */
  }
}
