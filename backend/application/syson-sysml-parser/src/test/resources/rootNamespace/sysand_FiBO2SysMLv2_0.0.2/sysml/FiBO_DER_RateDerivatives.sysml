// ===================================================================
// FIBO_DER_RateDerivatives.sysml
// CI Aggregator (public re-export of subpackages)
// ===================================================================
package FIBO_DER_RateDerivatives {
  doc/* Configuration Item (CI) package for FIBO_DER_RateDerivatives and its exported model elements. */
  public import RateDerivatives::*;
  public import IRSwaps::*;
  public import IRSwapExampleIndividuals::*;
}

// ======================
// RateDerivatives.sysml
// (feature-based from RateDerivatives.rdf)
// ======================
package RateDerivatives {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import CurrencyAmount::*;           // MonetaryAmount, MonetaryPrice, Currency
  private import FinancialDates::*;           // ExplicitDate, Duration, RegularSchedule, ExplicitDatePeriod
  private import Parties::*;                  // Party (holder/writer)
  private import DerivativesBasics::*;        // DayCount, ExerciseStyle, SettlementMethod, PayoffFunction
  private import Swaps::*;                    // SwapContract, InterestRateSwap, FloatingIndex

  // ---- Enums ----
  enum def CompoundingMethod {
    enum Simple;
    enum Periodic;
    enum Continuous;
    enum DailyCompounded;
  }

  enum def ResetConvention {
    enum InAdvance;
    enum InArrears;
  }

  enum def SwaptionType {
    enum Payer;   // right to pay fixed (receive float)
    enum Receiver;// right to receive fixed (pay float)
  }

  // ---- Common rate reference ----
  item def InterestRateReference {
    doc/* identifiable interest rate setting used as a floating reference (e.g., SOFR 1M, 3M EURIBOR) */
    attribute index: Swaps::FloatingIndex[1..1];
    attribute tenorMonths: Integer[0..1];      // 1, 3, 6, 12 (if applicable)
    attribute currency: CurrencyAmount::Currency[0..1];
    attribute resetConvention: ResetConvention[0..1];
  }

  // ---- Forward Rate Agreement (FRA) ----
  item def ForwardRateAgreement {
    doc/* OTC contract to borrow/lend at a fixed rate for a future accrual period, cash-settled at period start */
    attribute tradeDate: Date[0..1];
    attribute effectiveDate: ExplicitDate[0..1];      // period start
    attribute maturityDate: ExplicitDate[0..1];       // period end
    attribute notional: MonetaryAmount[1..1];
    attribute fixedRatePct: Real[1..1];
    attribute referenceRate: InterestRateReference[1..1];
    attribute dayCount: DerivativesBasics::DayCount[0..1];
    attribute settlementCurrency: CurrencyAmount::Currency[0..1];
    attribute settlementAmount: MonetaryAmount[0..1]; // realized cash settlement
    attribute buyer: Parties::Party[0..1];
    attribute seller: Parties::Party[0..1];
  }

  // ---- Caps, Floors, Collars ----
  item def InterestRateOption {
    doc/* option whose payoff depends on an interest rate level relative to a strike */
    attribute holder: Parties::Party[0..1];
    attribute writer: Parties::Party[0..1];
    attribute settlementMethod: DerivativesBasics::SettlementMethod[0..1];
    attribute payoff: DerivativesBasics::PayoffFunction[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
  }

  item def Caplet specializes InterestRateOption {
    doc/* single-period call on a floating rate fixing, with strike (cap rate) */
    attribute fixingDate: ExplicitDate[0..1];
    attribute paymentDate: ExplicitDate[0..1];
    attribute notional: MonetaryAmount[1..1];
    attribute strikeRatePct: Real[1..1];
    attribute referenceRate: InterestRateReference[1..1];
    attribute dayCount: DerivativesBasics::DayCount[0..1];
  }

  item def Floorlet specializes InterestRateOption {
    doc/* single-period put on a floating rate fixing, with strike (floor rate) */
    attribute fixingDate: ExplicitDate[0..1];
    attribute paymentDate: ExplicitDate[0..1];
    attribute notional: MonetaryAmount[1..1];
    attribute strikeRatePct: Real[1..1];
    attribute referenceRate: InterestRateReference[1..1];
    attribute dayCount: DerivativesBasics::DayCount[0..1];
  }

  item def InterestRateCap specializes InterestRateOption {
    doc/* portfolio of caplets across a schedule; pays when reference > cap strike */
    attribute schedule: RegularSchedule[1..1];
    attribute notional: MonetaryAmount[1..1];
    attribute capStrikeRatePct: Real[1..1];
    attribute referenceRate: InterestRateReference[1..1];
    attribute dayCount: DerivativesBasics::DayCount[0..1];
  }

  item def InterestRateFloor specializes InterestRateOption {
    doc/* portfolio of floorlets across a schedule; pays when reference < floor strike */
    attribute schedule: RegularSchedule[1..1];
    attribute notional: MonetaryAmount[1..1];
    attribute floorStrikeRatePct: Real[1..1];
    attribute referenceRate: InterestRateReference[1..1];
    attribute dayCount: DerivativesBasics::DayCount[0..1];
  }

  item def InterestRateCollar {
    doc/* combination of a cap and a floor, typically purchased cap and sold floor (or vice versa) */
    attribute longCap: InterestRateCap[0..1];
    attribute shortFloor: InterestRateFloor[0..1];
    attribute shortCap: InterestRateCap[0..1];
    attribute longFloor: InterestRateFloor[0..1];
  }

  // ---- Swaption (option on a swap) ----
  item def Swaption {
    doc/* option granting right (not obligation) to enter into an interest rate swap at a future date */
    attribute type: SwaptionType[1..1];                           // payer / receiver
    attribute style: DerivativesBasics::ExerciseStyle[1..1];      // European/Bermudan
    attribute optionExpiry: ExplicitDate[1..1];
    attribute underlyingSwap: Swaps::InterestRateSwap[0..1];      // full contract if specified
    attribute notional: MonetaryAmount[0..1];                      // if underlying terms are parameterized
    attribute fixedRatePct: Real[0..1];
    attribute floatReference: InterestRateReference[0..1];
    attribute swapMaturityDate: ExplicitDate[0..1];
    attribute premiumAmount: MonetaryAmount[0..1];
    attribute buyer: Parties::Party[0..1];
    attribute seller: Parties::Party[0..1];
  }

  // ---- Rate Futures (e.g., IBOR / OIS futures) ----
  item def InterestRateFuture {
    doc/* exchange-traded futures contract whose price reflects an interest rate expectation */
    attribute contractCode: String[0..1];
    attribute referenceRate: InterestRateReference[1..1];
    attribute contractSize: Real[0..1];                // e.g., 1,000,000 notional proxy
    attribute priceQuote: Real[0..1];                  // e.g., 100 - rate
    attribute maturityDate: ExplicitDate[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
  }
}

// ======================
// IRSwaps.sysml
// (feature-based from IRSwaps.rdf)
// ======================
package IRSwaps {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import DerivativesBasics::*;
  private import Swaps::*;                   // SwapContract, InterestRateSwap, BasisSwap, CrossCurrencySwap

  // ---- Additional enums / conventions for IRS ----
  enum def FixedLegRole { enum Payer; enum Receiver; }
  enum def CompoundingConvention { enum None; enum Flat; enum Straight; enum SpreadExclusive; }

  // ---- Convention bundle ----
  item def IRSConventions {
    doc/* standardized set of conventions commonly referenced in IRS term sheets */
    attribute dayCountFixed: DerivativesBasics::DayCount[0..1];
    attribute dayCountFloat: DerivativesBasics::DayCount[0..1];
    attribute payFreqMonthsFixed: Integer[0..1];
    attribute payFreqMonthsFloat: Integer[0..1];
    attribute resetFreqMonths: Integer[0..1];
    attribute businessDayAdjustment: DerivativesBasics::BusinessDayAdj[0..1];
    attribute compounding: CompoundingConvention[0..1];
  }

  // ---- OIS specialization ----
  item def OvernightIndexSwap specializes Swaps::InterestRateSwap {
    doc/* fixed-for-compounded-overnight floating rate swap */
    attribute floatingIndex: Swaps::FloatingIndex[1..1];       // e.g., SOFR/SONIA/â‚¬STR
    attribute compoundingMethod: RateDerivatives::CompoundingMethod[0..1];
    attribute dayCountFloat: DerivativesBasics::DayCount[0..1];
    attribute dayCountFixed: DerivativesBasics::DayCount[0..1];
  }

  // ---- Zero-coupon OIS (single payment) ----
  item def ZeroCouponOIS specializes OvernightIndexSwap {
    doc/* OIS variant with one payment at maturity for each leg */
  }

  // ---- Plain-vanilla IRS convenience wrapper ----
  item def PlainVanillaIRS specializes Swaps::InterestRateSwap {
    doc/* market-standard fixed-for-floating IRS with equal notionals and regular schedules */
    attribute fixedLegRole: FixedLegRole[0..1];
    attribute conventions: IRSConventions[0..1];
  }
}

// ======================
// IRSwapExampleIndividuals.sysml
// (feature-based from IRSwapExampleIndividuals.rdf)
// ======================
package IRSwapExampleIndividuals {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import Swaps::*;
  private import IRSwaps::*;
  private import DerivativesBasics::*;

  // ---- Canonical examples (all items) ----

  item def IRS_5Y_USD_Vanilla specializes IRSwaps::PlainVanillaIRS {
    doc/* example: 5-year USD fixed vs 3M floating IRS */
    attribute fixedLegRatePct: Real[0..1];
  }

  item def OIS_1Y_USD_SOFR specializes IRSwaps::OvernightIndexSwap {
    doc/* example: 1-year USD OIS vs SOFR */
  }

  item def ZC_OIS_2Y_EUR_ESTR specializes IRSwaps::ZeroCouponOIS {
    doc/* example: 2-year EUR zero-coupon OIS vs â‚¬STR */
  }

  item def IRS_10Y_EUR_Vanilla specializes IRSwaps::PlainVanillaIRS {
    doc/* example: 10-year EUR fixed vs 6M floating IRS */
    attribute fixedLegRatePct: Real[0..1];
  }
}
