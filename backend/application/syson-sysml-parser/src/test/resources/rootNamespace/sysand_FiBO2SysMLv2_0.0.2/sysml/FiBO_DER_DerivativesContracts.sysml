// ===================================================================
// FIBO_DER_DerivativesContracts.sysml
// ===================================================================
package FIBO_DER_DerivativesContracts {
  doc/* Configuration Item (CI) package for FIBO_DER_DerivativesContracts and its exported model elements. */
  public import DerivativesBasics::*;
  public import DerivativesMasterAgreements::*;
  public import FuturesAndForwards::*;
  public import Options::*;
  public import ExoticOptions::*;
  public import RightsAndWarrants::*;
  public import Swaps::*;
  public import CommoditiesContracts::*;
  public import CurrencyContracts::*;
  public import StructuredInstruments::*;
  public import SwapsIndividuals::*;
}

// ======================
// DerivativesBasics.sysml
// ======================
package DerivativesBasics {

  // ---- Imports ----
  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Contracts::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import PaymentsAndSchedules::*;

  // ---- Enums & shared codes ----
  enum def ExerciseStyle { enum European; enum American; enum Bermudan; }
  enum def SettlementMethod { enum Cash; enum Physical; }
  enum def OptionRight { enum Call; enum Put; }
  enum def PositionSide { enum Long; enum Short; }
  enum def QuoteType { enum Price; enum Spread; enum ImpliedVol; enum Points; }
  enum def DayCount { enum ACT360; enum ACT365F; enum Thirty360; enum ACTACT; }
  enum def BusinessDayAdj { enum Following; enum ModifiedFollowing; enum Preceding; enum None; }

  // ---- Core abstractions ----
  item def DerivativeContract specializes Contract {
    doc/* contract whose value is derived from the level or behavior of an underlying */
    attribute tradeDate: Date[0..1];
    attribute maturityDate: ExplicitDate[0..1];
    attribute settlementMethod: DerivativesBasics::SettlementMethod[0..1];
    attribute positionSide: PositionSide[0..1];
    attribute notional: MonetaryAmount[0..1];
    attribute premium: MonetaryAmount[0..1];
    attribute paymentSchedule: PaymentsAndSchedules::PaymentSchedule[0..1];
    attribute counterpartyA: Parties::Party[0..1];
    attribute counterpartyB: Parties::Party[0..1];
  }

  item def UnderlyingAsset {
    doc/* economic variable or asset on which a derivative's payoff depends */
    attribute description: String[0..1];
    attribute identifier: String[0..1];     // e.g., ISIN, ticker, index code, commodity code
    attribute currency: Currency[0..1];
  }

  item def PriceQuote {
    doc/* market quotation for a derivative instrument */
    attribute quoteType: QuoteType[1..1];
    attribute value: Real[1..1];
    attribute asOf: DateTime[0..1];
    attribute forContract: DerivativeContract[0..1];
  }

  item def PayoffFunction {
    doc/* human-readable payoff specification */
    attribute formulaText: String[1..1];
    attribute notes: String[0..*];
  }

  item def DeliveryTerms {
    doc/* parameters governing physical delivery */
    attribute deliveryLocation: String[0..1];
    attribute deliveryWindow: ExplicitDatePeriod[0..1];
    attribute businessDayAdjustment: BusinessDayAdj[0..1];
  }
}

// ======================
// DerivativesMasterAgreements.sysml
// ======================
package DerivativesMasterAgreements {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Contracts::*;
  private import Agreements::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import PaymentsAndSchedules::*;

  enum def DefaultEventKind {
    enum FailureToPay; enum Bankruptcy; enum Illegality; enum MergerWithoutAssumption;
    enum CrossDefault; enum Repudiation; enum Reorganization; enum Other;
  }

  item def ISDAMasterAgreement specializes Contract {
    doc/* framework agreement published by ISDA governing OTC derivatives between parties */
    attribute versionYear: Integer[0..1];       // e.g., 1992, 2002
    attribute governingLawText: String[0..1];   // text label for law selection
    attribute partyA: Parties::Party[1..1];
    attribute partyB: Parties::Party[1..1];
    attribute schedule: ISDASchedule[0..1];
    attribute creditSupportAnnex: CreditSupportAnnex[0..*];
  }

  item def ISDASchedule {
    doc/* negotiated elections and provisions that modify the ISDA Master */
    attribute electionsText: String[0..*];
    attribute terminationCurrency: Currency[0..1];
  }

  item def CreditSupportAnnex {
    doc/* collateral agreement defining margining and eligible collateral */
    attribute jurisdictionForm: String[0..1];     // e.g., English Law, New York Law
    attribute initialMargin: MonetaryAmount[0..1];
    attribute variationMarginFrequencyDays: Integer[0..1];
    attribute eligibleCollateralDescription: String[0..*];
    attribute haircutsNote: String[0..*];
  }

  item def EventOfDefault {
    doc/* event defined under the Master Agreement that may allow early termination */
    attribute kind: DefaultEventKind[1..1];
    attribute occurredOn: Date[0..1];
    attribute affectingParty: Parties::Party[0..1];
    attribute details: String[0..*];
  }

  item def TerminationEvent {
    doc/* Illegality/Tax/Force Majeure or other event permitting termination */
    attribute description: String[1..1];
    attribute occurredOn: Date[0..1];
  }

  item def CloseOutAmount {
    doc/* amount determined upon early termination */
    attribute amount: MonetaryAmount[1..1];
    attribute valuationDate: Date[0..1];
    attribute determinedBy: Parties::Party[0..1];
  }

  item def NettingSet {
    doc/* set of transactions aggregated for close-out netting under a master agreement */
    attribute underAgreement: ISDAMasterAgreement[1..1];
    attribute includedTradeId: String[0..*];
    attribute closeOutAmount: CloseOutAmount[0..1];
  }

  item def Confirmation {
    doc/* transaction-level confirmation capturing final agreed economic terms */
    attribute tradeIdentifier: String[1..1];
    attribute confirmsAgreement: ISDAMasterAgreement[0..1];
    attribute confirmationDate: Date[0..1];
    attribute textTerms: String[0..*];
  }
}

// ======================
// FuturesAndForwards.sysml
// ======================
package FuturesAndForwards {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Contracts::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import DerivativesBasics::*;

  enum def ContractType { enum Futures; enum Forward; }
  enum def DeliveryType { enum Physical; enum Cash; }

  item def ForwardContract specializes DerivativeContract {
    doc/* OTC agreement to buy/sell an underlying at a specified price on a future date */
    attribute underlying: UnderlyingAsset[1..1];
    attribute forwardPrice: MonetaryPrice[0..1];
    attribute deliveryType: DeliveryType[0..1];
    attribute deliveryTerms: DeliveryTerms[0..1];
  }

  item def FuturesContract specializes DerivativeContract {
    doc/* exchange-traded standardized forward with daily margining */
    attribute exchangeName: String[0..1];
    attribute contractCode: String[0..1];
    attribute underlying: UnderlyingAsset[1..1];
    attribute contractSize: Real[0..1];
    attribute tickSize: Real[0..1];
    attribute settlementType: DeliveryType[0..1];
  }

  item def MarginRequirement {
    doc/* initial or maintenance margin requirement for a futures position */
    attribute initialMargin: MonetaryAmount[0..1];
    attribute maintenanceMargin: MonetaryAmount[0..1];
    attribute currency: Currency[0..1];
  }

  item def VariationMarginFlow {
    doc/* daily mark-to-market cash flow for futures */
    attribute forContract: FuturesContract[1..1];
    attribute date: Date[1..1];
    attribute amount: MonetaryAmount[1..1];
  }
}

// ======================
// Options.sysml
// ======================
package Options {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Contracts::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import DerivativesBasics::*;

  item def OptionContract specializes DerivativeContract {
    doc/* contract granting the holder the right, but not the obligation, to buy or sell an underlying at a specified strike price */
    attribute right: DerivativesBasics::OptionRight[1..1];
    attribute style: DerivativesBasics::ExerciseStyle[1..1];
    attribute underlying: UnderlyingAsset[1..1];
    attribute strikePrice: MonetaryPrice[1..1];
    attribute holder: Parties::Party[0..1];
    attribute writer: Parties::Party[0..1];
    attribute payoff: DerivativesBasics::PayoffFunction[0..1];
  }

  item def OptionPremiumPayment {
    doc/* premium consideration for acquiring an option */
    attribute option: OptionContract[1..1];
    attribute premium: MonetaryAmount[1..1];
    attribute premiumPaymentDate: Date[0..1];
  }
}

// ======================
// ExoticOptions.sysml
// ======================
package ExoticOptions {

  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import DerivativesBasics::*;
  private import Options::*;

  enum def BarrierType {
    enum UpAndOut; enum DownAndOut; enum UpAndIn; enum DownAndIn;
  }

  item def BarrierOption specializes OptionContract {
    doc/* option whose existence or payoff depends on the underlying breaching a barrier */
    attribute barrierType: BarrierType[1..1];
    attribute barrierLevel: Real[1..1];
    attribute rebateAmount: MonetaryAmount[0..1];
    attribute observationStart: ExplicitDate[0..1];
    attribute observationEnd: ExplicitDate[0..1];
  }

  item def AsianOption specializes OptionContract {
    doc/* option whose payoff depends on an average of the underlying over a period */
    attribute averagingStart: ExplicitDate[0..1];
    attribute averagingEnd: ExplicitDate[0..1];
    attribute averagingFrequencyDays: Integer[0..1];
  }

  item def DigitalOption specializes OptionContract {
    doc/* option paying a fixed amount if a condition is met, otherwise nothing */
    attribute digitalPayout: MonetaryAmount[1..1];
  }

  item def LookbackOption specializes OptionContract {
    doc/* option with payoff based on min/max underlying price over a period */
    attribute lookbackStart: ExplicitDate[0..1];
    attribute lookbackEnd: ExplicitDate[0..1];
  }

  item def ChooserOption specializes OptionContract {
    doc/* option where the holder can choose call or put by a decision date */
    attribute chooseByDate: ExplicitDate[1..1];
  }
}

// ======================
// RightsAndWarrants.sysml
// ======================
package RightsAndWarrants {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Organizations::*;
  private import Contracts::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import DerivativesBasics::*;

  item def Warrant specializes DerivativeContract {
    doc/* long-dated option issued by an organization, typically on its own equity */
    attribute issuer: Organizations::Organization[0..1];
    attribute underlying: UnderlyingAsset[1..1];
    attribute strikePrice: MonetaryPrice[1..1];
  }

  item def SubscriptionRight specializes DerivativeContract {
    doc/* short-term right to purchase additional shares at a discount (rights issue) */
    attribute issuer: Organizations::Organization[0..1];
    attribute underlying: UnderlyingAsset[1..1];
    attribute subscriptionPrice: MonetaryPrice[1..1];
    attribute ratioNumerator: Integer[0..1];    // rights per existing shares
    attribute ratioDenominator: Integer[0..1];
  }
}

// ======================
// Swaps.sysml
// ======================
package Swaps {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Contracts::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import PaymentsAndSchedules::*;
  private import DerivativesBasics::*;

  enum def LegPayReceive { enum Pay; enum Receive; }
  enum def FloatingIndex { enum IBOR; enum SOFR; enum SONIA; enum ESTR; enum FedFunds; enum EONIA; enum Custom; }

  item def SwapContract specializes DerivativeContract {
    doc/* agreement to exchange cash flows according to specified leg terms */
    attribute leg: SwapLeg[2..*];
    attribute tradeId: String[0..1];
  }

  item def SwapLeg {
    doc/* specification of one side of cash flow exchanges in a swap */
    attribute payReceive: LegPayReceive[1..1];
    attribute notional: MonetaryAmount[1..1];
    attribute dayCount: DerivativesBasics::DayCount[0..1];
    attribute paymentSchedule: PaymentsAndSchedules::PaymentSchedule[0..1];
    attribute fixedRatePct: Real[0..1];
    attribute floatingIndex: FloatingIndex[0..1];
    attribute indexTenorMonths: Integer[0..1];      // e.g., 1, 3, 6, 12
    attribute spreadBps: Real[0..1];
    attribute resetSchedule: PaymentsAndSchedules::PaymentSchedule[0..1];
    attribute currency: Currency[0..1];
  }

  item def InterestRateSwap specializes SwapContract {
    doc/* swap exchanging fixed and floating interest cash flows in the same currency */
  }

  item def BasisSwap specializes SwapContract {
    doc/* swap exchanging two floating-rate streams (e.g., 1m vs 3m) */
  }

  item def CrossCurrencySwap specializes SwapContract {
    doc/* swap exchanging cash flows in two different currencies, may include notionals exchange */
    attribute exchangedNotional: Boolean[0..1];
    attribute currencyLegA: Currency[0..1];
    attribute currencyLegB: Currency[0..1];
  }

  item def EquitySwap specializes SwapContract {
    doc/* swap exchanging equity performance return for fixed/floating cash flows */
    attribute underlying: DerivativesBasics::UnderlyingAsset[1..1];
  }

  item def CommoditySwap specializes SwapContract {
    doc/* swap exchanging commodity price exposure for fixed/floating cash flows */
    attribute underlying: DerivativesBasics::UnderlyingAsset[1..1];
  }

  item def TotalReturnSwap specializes SwapContract {
    doc/* swap exchanging total return (price + carry) of a reference asset for funding leg */
    attribute referenceAsset: DerivativesBasics::UnderlyingAsset[1..1];
  }
}

// ======================
// CommoditiesContracts.sysml
// ======================
package CommoditiesContracts {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Contracts::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import DerivativesBasics::*;
  private import FuturesAndForwards::*;

  enum def CommodityKind { enum Energy; enum Metals; enum Agriculture; enum Livestock; enum Other; }

  item def CommodityForward specializes ForwardContract {
    doc/* forward contract on a physical commodity */
    attribute commodityKind: CommodityKind[0..1];
    attribute gradeSpec: String[0..1];
  }

  item def PhysicalDeliveryNotice {
    doc/* notice of intent and arrangements to make or take delivery under a physically-settled contract */
    attribute relatedContract: DerivativesBasics::DerivativeContract[1..1];
    attribute noticeDate: Date[1..1];
    attribute deliveryTerms: DerivativesBasics::DeliveryTerms[0..1];
  }
}

// ======================
// CurrencyContracts.sysml
// ======================
package CurrencyContracts {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Contracts::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import DerivativesBasics::*;

  item def FXSpot specializes DerivativesBasics::DerivativeContract {
    doc/* spot foreign exchange transaction */
    attribute baseCurrency: Currency[1..1];
    attribute quoteCurrency: Currency[1..1];
    attribute agreedFxRate: Real[1..1];
    attribute valueDate: Date[0..1];
  }

  item def FXForward specializes DerivativesBasics::DerivativeContract {
    doc/* forward foreign exchange contract for exchange of currencies on a future date */
    attribute baseCurrency: Currency[1..1];
    attribute quoteCurrency: Currency[1..1];
    attribute forwardRate: Real[1..1];
    attribute settlementDate: Date[0..1];
  }

  item def FXSwapContract specializes DerivativesBasics::DerivativeContract {
    doc/* simultaneous spot and forward FX transactions with opposite directions */
    attribute spotFx: FXSpot[1..1];
    attribute forwardFx: FXForward[1..1];
  }

  item def NDF specializes DerivativesBasics::DerivativeContract {
    doc/* non-deliverable forward settled in a convertible currency against a fixing */
    attribute nonDeliverableCurrency: Currency[1..1];
    attribute settlementCurrency: Currency[1..1];
    attribute notionalAmount: MonetaryAmount[1..1];
    attribute forwardRate: Real[1..1];
    attribute fixingDate: Date[0..1];
    attribute settlementDate: Date[0..1];
    attribute referenceRateText: String[0..1];
  }
}

// ======================
// StructuredInstruments.sysml
// ======================
package StructuredInstruments {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Organizations::*;
  private import Contracts::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import DerivativesBasics::*;
  private import Options::*;
  private import Swaps::*;

  item def StructuredNote specializes Contracts::Contract {
    doc/* debt instrument with embedded derivatives altering payoff profile */
    attribute issuer: Organizations::Organization[0..1];
    attribute issueDate: Date[0..1];
    attribute maturityDate: Date[0..1];
    attribute currency: Currency[0..1];
    attribute principalAmount: MonetaryAmount[0..1];
    attribute embeddedOption: Options::OptionContract[0..*];
    attribute embeddedSwap: Swaps::SwapContract[0..*];
    attribute payoff: DerivativesBasics::PayoffFunction[0..1];
  }

  item def ConvertibleBond specializes StructuredNote {
    doc/* bond convertible into a predetermined number of shares of the issuer */
    attribute conversionRatio: Real[0..1];
    attribute conversionPrice: MonetaryPrice[0..1];
    attribute conversionStart: ExplicitDate[0..1];
    attribute conversionEnd: ExplicitDate[0..1];
  }

  item def CreditLinkedNote specializes StructuredNote {
    doc/* note whose payoff is linked to credit performance of a reference entity or basket */
    attribute referenceEntity: Parties::Party[0..1];
    attribute attachmentPointPct: Real[0..1];
    attribute detachmentPointPct: Real[0..1];
  }

  item def RangeAccrualNote specializes StructuredNote {
    doc/* coupon accrues only on days when reference rate stays within a range */
    attribute lowerBound: Real[1..1];
    attribute upperBound: Real[1..1];
    attribute referenceIndexText: String[0..1];
    attribute accrualSchedule: FinancialDates::RegularSchedule[0..1];
  }
}
// ======================
// SwapsIndividuals.sysml
// (feature-based from SwapsIndividuals.rdf)
// ======================
package SwapsIndividuals {

  // ---- Imports (private) ----
  private import ScalarValues::*;       
  private import Time::*;               
  private import CurrencyAmount::*;     
  private import FinancialDates::*;     
  private import Swaps::*;              
  private import DerivativesBasics::*;  

  // =========================================================
  // Canonical swap examples (all items)
  // =========================================================

  item def VanillaInterestRateSwap specializes Swaps::InterestRateSwap {
    doc/* plain vanilla fixed-for-floating interest rate swap where one party pays fixed and receives floating on the same notional in one currency */
    attribute standardTenorYears: Integer[0..1];           // e.g., 5
    attribute fixedLegRatePct: Real[0..1];                 // e.g., 2.5
    attribute floatingLegIndex: Swaps::FloatingIndex[0..1]; // e.g., IBOR
    attribute paymentFrequencyMonths: Integer[0..1];        // e.g., 6
    attribute dayCount: DerivativesBasics::DayCount[0..1];
  }

  item def OvernightIndexSwap specializes Swaps::InterestRateSwap {
    doc/* fixed-for-floating interest rate swap where the floating leg references a daily compounded overnight rate (OIS) */
    attribute floatingRateIndex: Swaps::FloatingIndex[0..1];  // e.g., SOFR, ESTR
    attribute fixedLegRatePct: Real[0..1];
    attribute termYears: Integer[0..1];
  }

  item def BasisSwap3m6m specializes Swaps::BasisSwap {
    doc/* basis swap exchanging 3-month vs 6-month floating rate tenors in the same currency */
    attribute currency: CurrencyAmount::Currency[0..1];
    attribute tenorA: String[1..1];         // "3M"
    attribute tenorB: String[1..1];         // "6M"
    attribute spreadBps: Real[0..1];        // typical market basis
  }

  item def CrossCurrencySwapUSDJPY specializes Swaps::CrossCurrencySwap {
    doc/* cross-currency swap exchanging USD vs JPY notionals and interest flows */
    attribute tenorYears: Integer[0..1];                    // e.g., 5
  }

  item def EquityTotalReturnSwap specializes Swaps::TotalReturnSwap {
    doc/* total return swap exchanging equity total return (price + dividends) for a funding leg */
    attribute referenceIndex: String[0..1];           // e.g., S&P 500
    attribute fundingLegRate: Real[0..1];             // e.g., 1M LIBOR + spread
    attribute resetFrequencyMonths: Integer[0..1];    // e.g., 3
  }

  item def CommoditySwapWTIBrent specializes Swaps::CommoditySwap {
    doc/* commodity swap exchanging fixed payments for floating commodity price based on WTI or Brent crude */
    attribute underlyingCommodity: String[1..1];     // e.g., "WTI", "Brent"
    attribute pricingFrequencyMonths: Integer[0..1]; // e.g., monthly
    attribute fixedPricePerBarrel: Real[0..1];
  }

  item def InflationSwap specializes Swaps::SwapContract {
    doc/* swap exchanging fixed rate payments for payments linked to an inflation index */
    attribute inflationIndexName: String[1..1];      // e.g., "US CPI", "HICP"
    attribute fixedRatePct: Real[0..1];
    attribute notionalAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute maturityYears: Integer[0..1];
  }

  item def CrossCurrencyBasisSwapEURUSD specializes Swaps::BasisSwap {
    doc/* cross-currency basis swap exchanging EUR vs USD floating legs */
    attribute currencyLegA: CurrencyAmount::Currency[1..1]; // EUR
    attribute currencyLegB: CurrencyAmount::Currency[1..1]; // USD
    attribute basisSpreadBps: Real[0..1];
    attribute maturityYears: Integer[0..1];
  }

  item def TotalReturnSwapIndex specializes Swaps::TotalReturnSwap {
    doc/* TRS on an equity or credit index returning total performance versus a floating funding leg */
    attribute referenceIndex: String[1..1];          // e.g., "CDX IG", "S&P 500"
    attribute fundingRate: Real[0..1];
    attribute frequencyMonths: Integer[0..1];
  }

  item def VolatilitySwap specializes Swaps::SwapContract {
    doc/* swap exchanging realized volatility of an underlying for a fixed volatility strike */
    attribute underlyingAsset: DerivativesBasics::UnderlyingAsset[1..1];
    attribute volatilityStrikePct: Real[1..1];
    attribute settlementDate: FinancialDates::ExplicitDate[0..1];
  }
}
