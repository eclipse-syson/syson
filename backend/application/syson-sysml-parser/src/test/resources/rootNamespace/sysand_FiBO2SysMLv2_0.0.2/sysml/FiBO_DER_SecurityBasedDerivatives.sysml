// ===================================================================
// FIBO_DER_SecurityBasedDerivatives.sysml
// CI Aggregator (public re-export of subpackages)
// ===================================================================
package FIBO_DER_SecurityBasedDerivatives {
  doc/* Configuration Item (CI) package for FIBO_DER_SecurityBasedDerivatives and its exported model elements. */
  public import SecurityBasedDerivatives::*;
  public import EquitySwaps::*;
}

// ======================
// SecurityBasedDerivatives.sysml
// (feature-based from SecurityBasedDerivatives.rdf)
// ======================
package SecurityBasedDerivatives {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import Parties::*;                  // Party
  private import Organizations::*;            // Organization / FormalOrganization
  private import CurrencyAmount::*;           // MonetaryAmount, Currency
  private import FinancialDates::*;           // ExplicitDate, Duration, RegularSchedule
  private import DerivativesBasics::*;        // ExerciseStyle, SettlementMethod, PayoffFunction
  private import Contracts::*;                // Contract, ContractualElement
  private import Agreements::*;               // Agreement
  private import LegalCorePackage::*;                // Law
  private import JurisdictionPackage::*;             // Jurisdiction

  // =========================================================
  // Core definitions
  // =========================================================

  item def SecurityBasedDerivativeContract specializes Contracts::Contract {
    doc/* derivative whose value depends on the price or yield of one or more securities (e.g., equity, bond, index, or basket) */
    attribute tradeId: String[0..1];
    attribute underlyingSecurity: UnderlyingSecurity[0..*];
    attribute tradeDate: Date[0..1];
    attribute maturityDate: FinancialDates::ExplicitDate[0..1];
    attribute notional: CurrencyAmount::MonetaryAmount[0..1];
    attribute settlementMethod: DerivativesBasics::SettlementMethod[0..1];
    attribute jurisdiction: Jurisdiction[0..1];
  }

  item def UnderlyingSecurity {
    doc/* security or basket of securities serving as the underlying of a derivative */
    attribute identifier: String[1..1];                // e.g., ISIN, ticker
    attribute issuer: Organizations::Organization[0..1];
    attribute description: String[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
    attribute quantity: Real[0..1];
  }

  item def EquityOption specializes SecurityBasedDerivativeContract {
    doc/* option granting right to buy or sell a specified number of shares at a set price before or on a given date */
    attribute optionType: DerivativesBasics::OptionRight[1..1];
    attribute style: DerivativesBasics::ExerciseStyle[1..1];
    attribute strikePrice: CurrencyAmount::MonetaryPrice[1..1];
    attribute underlying: UnderlyingSecurity[1..1];
  }

  item def IndexOption specializes SecurityBasedDerivativeContract {
    doc/* option whose underlying is a market index such as S&P 500 or FTSE 100 */
    attribute indexName: String[1..1];
    attribute optionType: DerivativesBasics::OptionRight[1..1];
    attribute style: DerivativesBasics::ExerciseStyle[1..1];
    attribute strikeLevel: Real[1..1];
  }

  item def ConvertibleBondOption specializes SecurityBasedDerivativeContract {
    doc/* embedded option within a convertible bond allowing exchange of bond into equity */
    attribute conversionRatio: Real[0..1];
    attribute conversionPrice: CurrencyAmount::MonetaryPrice[0..1];
    attribute underlying: UnderlyingSecurity[0..1];
  }

  item def EquityForward specializes SecurityBasedDerivativeContract {
    doc/* forward contract to buy or sell an equity security at a future date and price */
    attribute forwardPrice: CurrencyAmount::MonetaryPrice[1..1];
    attribute settlementDate: FinancialDates::ExplicitDate[1..1];
    attribute underlying: UnderlyingSecurity[1..1];
  }

  item def EquityFuture specializes SecurityBasedDerivativeContract {
    doc/* standardized exchange-traded forward on an equity or index */
    attribute contractCode: String[1..1];
    attribute contractSize: Real[0..1];
    attribute exchange: String[0..1];
    attribute underlying: UnderlyingSecurity[1..1];
  }

  item def EquitySwap specializes SecurityBasedDerivativeContract {
    doc/* swap exchanging total or partial return on an equity or index for fixed/floating payments */
    attribute underlying: UnderlyingSecurity[1..*];
    attribute totalReturnLeg: TotalReturnLeg[1..1];
    attribute fundingLeg: FundingLeg[1..1];
  }

  item def TotalReturnLeg {
    doc/* leg paying total return of the underlying (price + dividends) */
    attribute reference: UnderlyingSecurity[1..*];
    attribute calculationFrequencyMonths: Integer[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
  }

  item def FundingLeg {
    doc/* leg paying interest or financing cost for receiving total return */
    attribute notional: CurrencyAmount::MonetaryAmount[0..1];
    attribute floatingRateIndex: String[0..1];        // textual identifier (e.g., SOFR)
    attribute spreadBps: Real[0..1];
    attribute paymentFrequencyMonths: Integer[0..1];
  }

  item def EquityBasketSwap specializes EquitySwap {
    doc/* equity swap with multiple underlying securities (basket) and aggregated return */
    attribute basketName: String[0..1];
    attribute basketWeightingMethod: String[0..1];
  }

  item def EquityIndexSwap specializes EquitySwap {
    doc/* swap referencing performance of a published equity index */
    attribute indexName: String[1..1];
  }

  item def EquityTotalReturnSwap specializes EquitySwap {
    doc/* total return swap on a single equity security */
    attribute referenceEquity: UnderlyingSecurity[1..1];
  }

  item def DividendSwap specializes SecurityBasedDerivativeContract {
    doc/* swap exchanging fixed payments for actual dividends paid on a reference equity or index */
    attribute reference: UnderlyingSecurity[1..1];
    attribute dividendPeriod: FinancialDates::ExplicitDatePeriod[0..1];
    attribute fixedRatePct: Real[0..1];
  }
}

// ======================
// EquitySwaps.sysml
// (feature-based from EquitySwaps.rdf)
// ======================
package EquitySwaps {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import SecurityBasedDerivatives::*;
  private import DerivativesBasics::*;
  private import Swaps::*;
  private import Parties::*;
  private import Organizations::*;

  // =========================================================
  // Equity swap variations and canonical instances (items)
  // =========================================================

  item def VanillaEquitySwap specializes SecurityBasedDerivatives::EquitySwap {
    doc/* standard equity swap exchanging total return on a reference equity for floating-rate payments */
    attribute referenceEquity: SecurityBasedDerivatives::UnderlyingSecurity[1..1];
    attribute floatingRateIndex: Swaps::FloatingIndex[0..1];
    attribute spreadBps: Real[0..1];
  }

  item def EquityBasketTotalReturnSwap specializes SecurityBasedDerivatives::EquityBasketSwap {
    doc/* total return swap on an equity basket */
    attribute componentCount: Integer[0..1];
    attribute weighting: String[0..1];
  }

  item def EquityIndexTotalReturnSwap specializes SecurityBasedDerivatives::EquityIndexSwap {
    doc/* total return swap on an equity index, e.g., S&P 500 TRS */
    attribute currency: CurrencyAmount::Currency[0..1];
    attribute maturityYears: Integer[0..1];
  }

  item def DividendFuture specializes SecurityBasedDerivatives::DividendSwap {
    doc/* standardized future or forward contract on cumulative dividends of an equity or index over a period */
    attribute contractSize: Real[0..1];
    attribute exchange: String[0..1];
    attribute quotationStyle: String[0..1];
  }

  item def EquityVarianceSwap specializes SecurityBasedDerivatives::EquitySwap {
    doc/* swap exchanging realized variance of an equity price for a fixed strike variance */
    attribute varianceNotional: Real[1..1];
    attribute varianceStrike: Real[1..1];
    attribute observationStart: FinancialDates::ExplicitDate[0..1];
    attribute observationEnd: FinancialDates::ExplicitDate[0..1];
    attribute settlementDate: FinancialDates::ExplicitDate[0..1];
  }

  item def EquityVolatilitySwap specializes SecurityBasedDerivatives::EquitySwap {
    doc/* swap exchanging realized volatility of an equity or index for a fixed strike volatility */
    attribute volatilityNotional: Real[1..1];
    attribute volatilityStrike: Real[1..1];
    attribute reference: SecurityBasedDerivatives::UnderlyingSecurity[1..1];
  }
}
