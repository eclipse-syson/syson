// ===================================================================
// FIBO_FBC_FinancialInstruments.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_FBC_FinancialInstruments {
  doc/* Configuration Item (CI) package for FIBO_FBC_FinancialInstruments and its exported model elements. */
  public import FinancialInstruments::*;
  public import InstrumentPricing::*;
  public import Settlement::*;
}

// ================================
// FinancialInstruments.sysml
// (feature-based from FinancialInstruments.rdf)
// ================================
package FinancialInstruments {

  // ---- Imports (private) ----
  private import ScalarValues::*;              // String, Integer, Real, Boolean
  private import Time::*;                      // Date, DateTime
  private import CurrencyAmount::*;            // MonetaryAmount, Currency, MonetaryPrice
  private import FinancialDates::*;            // ExplicitDate, Duration, RegularSchedule
  private import ProductsAndServices::*;       // Product (supertype)
  private import Organizations::*;             // Organization / FormalOrganization
  private import Parties::*;                   // Party (issuer contacts, lead managers)
  private import Debt::*;                      // Debt::DebtInstrument (cross-link)
  private import DerivativesBasics::*;      // optional link to derivative contracts
  private import SecuritiesTransactions::*;    // for trading/settlement references

  // ---- Enums / code lists ----
  enum def InstrumentKind {
    doc/* high-level classification of financial instruments */
    enum Debt; enum Equity; enum Fund; enum Derivative; enum MoneyMarket; enum StructuredNote; enum Deposit; enum Other;
  }

  enum def ListingStatus { enum Unlisted; enum Listed; enum Suspended; enum Delisted; }
  enum def Transferability { enum Registered; enum Bearer; enum BookEntryOnly; }
  enum def InstrumentLifecycleStatus { enum Proposed; enum Issued; enum Active; enum Matured; enum Redeemed; enum Cancelled; enum Defaulted; }

  // ---- Identifiers & references ----
  item def InstrumentIdentifier {
    doc/* code or string uniquely identifying a financial instrument in some scheme */
    attribute value: String[1..1];                 // e.g., ISIN/CUSIP/FIGI
    attribute scheme: String[0..1];                // "ISIN","CUSIP","FIGI","SEDOL"
    attribute assigningAuthority: String[0..1];
  }

  item def TickerSymbol {
    doc/* exchange listing symbol for a security */
    attribute value: String[1..1];
    attribute exchangeMic: String[0..1];           // ISO 10383 MIC
  }

  item def TradingVenue {
    doc/* place of execution such as exchange, MTF, or OTF */
    attribute name: String[1..1];
    attribute mic: String[0..1];
    attribute countryCode: String[0..1];
  }

  // ---- Core instrument model ----
  item def FinancialInstrument specializes ProductsAndServices::Product {
    doc/* contractually defined, tradable financial product that embodies monetary value and rights/obligations */
    attribute instrumentId: String[0..1];
    attribute kind: InstrumentKind[1..1];
    attribute issuer: Organizations::Organization[0..1];
    attribute listingStatus: ListingStatus[0..1];
    attribute primaryListing: TradingVenue[0..1];
    attribute listingDate: Date[0..1];
    attribute transferability: Transferability[0..1];
    attribute status: InstrumentLifecycleStatus[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
    attribute issuePrice: CurrencyAmount::MonetaryPrice[0..1];
    attribute issueAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute maturityDate: FinancialDates::ExplicitDate[0..1];
    attribute identifiers: InstrumentIdentifier[0..*];
    attribute ticker: TickerSymbol[0..*];
  }

  item def EquityInstrument specializes FinancialInstrument {
    doc/* ownership interest in an entity with residual claim on assets and earnings */
    attribute shareClassText: String[0..1];
    attribute votingRightsText: String[0..1];
    attribute parValue: CurrencyAmount::MonetaryAmount[0..1];
  }

  item def FundInstrument specializes FinancialInstrument {
    doc/* collective investment vehicle share/unit */
    attribute fundTypeText: String[0..1];           // UCITS, ETF, MutualFund, AIF
    attribute navFrequencyText: String[0..1];       // Daily, Weekly, Monthly
  }

  item def DebtInstrumentRef specializes FinancialInstrument {
    doc/* link wrapper when a financial instrument is a debt security (see FBC Debt) */
    attribute debt: Debt::DebtInstrument[1..1];
  }

  item def DerivativeInstrumentRef specializes FinancialInstrument {
    doc/* link wrapper when a financial instrument is a derivative contract */
    attribute contract: DerivativesBasics::DerivativeContract[0..1];
    attribute swapContract: Swaps::SwapContract[0..1];
  }

  // ---- Events and relationships ----
  item def CorporateActionLink {
    doc/* link to corporate actions affecting this instrument over time */
    attribute instrument: FinancialInstrument[1..1];
    attribute actionEventId: String[1..1];         // reference to CAE event
    attribute actionTypeText: String[0..1];
    attribute effectiveDate: Date[0..1];
  }

  item def TradingRecord {
    doc/* record of a trade or execution for an instrument */
    attribute instrument: FinancialInstrument[1..1];
    attribute transaction: SecuritiesTransactions::SecuritiesTransaction[1..1];
    attribute venue: TradingVenue[0..1];
    attribute tradeDate: Date[0..1];
  }
}

// ================================
// InstrumentPricing.sysml
// (feature-based from InstrumentPricing.rdf)
// ================================
package InstrumentPricing {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import FinancialInstruments::*;

  // ---- Enums ----
  enum def QuoteType { enum Bid; enum Ask; enum Mid; enum Close; enum Open; enum Last; enum Indicative; enum NAV; }
  enum def PriceType { enum Clean; enum Dirty; enum Yield; enum Spread; enum PricePerUnit; enum IndexLevel; }
  enum def MarketDataSource { enum Exchange; enum OTC; enum Vendor; enum Internal; }
  enum def ValuationMethod { enum MarkToMarket; enum MarkToModel; enum AmortizedCost; enum IndependentPrice; }

  // ---- Market data & quotes ----
  item def MarketDataPoint {
    doc/* atomic market datum observed at a time for an instrument/venue */
    attribute instrument: FinancialInstruments::FinancialInstrument[0..1];
    attribute observedAt: DateTime[1..1];
    attribute source: MarketDataSource[0..1];
    attribute fieldName: String[1..1];              // e.g., BID, ASK, YIELD
    attribute fieldValue: Real[1..1];
    attribute currency: CurrencyAmount::Currency[0..1];
  }

  item def PriceQuote {
    doc/* quoted price or yield for an instrument with quote context */
    attribute instrument: FinancialInstruments::FinancialInstrument[1..1];
    attribute quoteType: QuoteType[1..1];
    attribute priceType: PriceType[1..1];
    attribute quoteValue: Real[1..1];
    attribute asOf: DateTime[1..1];
    attribute source: MarketDataSource[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
  }

  item def SpreadQuote {
    doc/* quotation expressed as a spread to a benchmark (e.g., OAS, Z-spread) */
    attribute instrument: FinancialInstruments::FinancialInstrument[1..1];
    attribute benchmarkText: String[1..1];
    attribute spreadBps: Real[1..1];
    attribute asOf: DateTime[1..1];
  }

  // ---- Valuation & analytics ----
  item def ValuationReport {
    doc/* result of valuing one or more instruments as of a specific time under a stated methodology */
    attribute asOf: DateTime[1..1];
    attribute method: ValuationMethod[1..1];
    attribute currency: CurrencyAmount::Currency[0..1];
    attribute lineItem: ValuationLineItem[1..*];
  }

  item def ValuationLineItem {
    doc/* per-instrument valuation outcome with key measures */
    attribute instrument: FinancialInstruments::FinancialInstrument[1..1];
    attribute priceClean: Real[0..1];
    attribute priceDirty: Real[0..1];
    attribute yieldToMaturityPct: Real[0..1];
    attribute accruedInterestAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute pvAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute durationMod: Real[0..1];
    attribute convexity: Real[0..1];
  }

  item def PricingModel {
    doc/* parametric model used to derive price or risk measures */
    attribute name: String[1..1];             // e.g., DCF, BlackScholes, SABR
    attribute version: String[0..1];
    attribute calibrationNote: String[0..1];
  }

  item def ModelValuation specializes ValuationReport {
    doc/* valuation produced by a specific pricing model and assumptions */
    attribute model: PricingModel[1..1];
    attribute assumptionsText: String[0..1];
  }
}

// ================================
// Settlement.sysml
// (feature-based from Settlement.rdf)
// ================================
package Settlement {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import Parties::*;
  private import Organizations::*;
  private import FinancialInstruments::*;
  private import SecuritiesTransactions::*;      // for executed trades

  // ---- Enums ----
  enum def SettlementMethod { enum DvP; enum FoP; enum PvP; enum CashOnly; }
  enum def SettlementStatus { enum Unmatched; enum Matched; enum Alleged; enum Pending; enum Settled; enum Failed; enum Cancelled; }
  enum def DeliveryType { enum Physical; enum BookEntry; enum Cash; }
  enum def FailureReason { enum InsufficientSecurities; enum InsufficientCash; enum CounterpartyNoShow; enum MarketClosure; enum Other; }

  // ---- Actors (parts) ----
  part def CentralCounterparty specializes Organizations::Organization {
    doc/* clearing house that interposes itself as buyer to every seller and seller to every buyer */
    attribute lei: String[0..1];
  }

  part def Custodian specializes Organizations::Organization {
    doc/* institution that safekeeps financial instruments and administers transactions */
    attribute bic: String[0..1];
  }

  part def PayingAgent specializes Organizations::Organization {
    doc/* agent responsible for making cash payments to holders on behalf of issuer */
  }

  // ---- Accounts ----
  item def AccountIdentifier {
    doc/* identifier for a cash or securities account in a given scheme (e.g., IBAN, BIC+Account, DTC/Euroclear/CREST participant) */
    attribute value: String[1..1];
    attribute scheme: String[0..1];
  }

  item def CashAccount {
    doc/* account used for settlement of cash legs */
    attribute owner: Parties::Party[1..1];
    attribute bank: Organizations::Organization[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
    attribute identifier: AccountIdentifier[0..*];
  }

  item def SecuritiesAccount {
    doc/* safekeeping account used to hold and deliver securities */
    attribute owner: Parties::Party[1..1];
    attribute custodian: Custodian[0..1];
    attribute depositoryText: String[0..1];          // e.g., DTC, Euroclear
    attribute identifier: AccountIdentifier[0..*];
  }

  // ---- Instructions & obligations ----
  item def SettlementInstruction {
    doc/* instruction specifying assets, amounts, accounts, and timing for settlement of a transaction */
    attribute transaction: SecuritiesTransactions::SecuritiesTransaction[1..1];
    attribute settlementDate: Date[1..1];
    attribute method: SettlementMethod[1..1];
    attribute deliveryType: DeliveryType[0..1];
    attribute deliveringSecuritiesAccount: SecuritiesAccount[0..1];
    attribute receivingSecuritiesAccount: SecuritiesAccount[0..1];
    attribute payerCashAccount: CashAccount[0..1];
    attribute payeeCashAccount: CashAccount[0..1];
    attribute settlementAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
    attribute placeOfSettlementText: String[0..1];
  }

  item def SettlementObligation {
    doc/* obligation arising from a transaction to deliver securities and/or cash by a settlement date */
    attribute instruction: SettlementInstruction[1..1];
    attribute quantity: Real[0..1];
    attribute instrument: FinancialInstruments::FinancialInstrument[0..1];
    attribute cashAmount: CurrencyAmount::MonetaryAmount[0..1];
  }

  // ---- Matching & status ----
  item def MatchingStatusRecord {
    doc/* status of pre-settlement matching between counterparties */
    attribute instruction: SettlementInstruction[1..1];
    attribute status: SettlementStatus[1..1];
    attribute matchedOn: DateTime[0..1];
    attribute mismatchNote: String[0..*];
  }

  item def SettlementEvent {
    doc/* lifecycle event during settlement (matched, settled, failed) with timestamps and references */
    attribute instruction: SettlementInstruction[1..1];
    attribute status: SettlementStatus[1..1];
    attribute eventTime: DateTime[1..1];
    attribute note: String[0..1];
  }

  item def SettlementFailure {
    doc/* failure of a settlement to complete as scheduled */
    attribute instruction: SettlementInstruction[1..1];
    attribute reason: FailureReason[0..1];
    attribute failDate: Date[1..1];
    attribute resolvedOn: Date[0..1];
    attribute penaltyAmount: CurrencyAmount::MonetaryAmount[0..1];
  }

  // ---- Netting and cycles ----
  item def SettlementCycle {
    doc/* market convention for time between trade date (T) and settlement (e.g., T+2) */
    attribute name: String[1..1];
    attribute daysAfterTrade: Integer[1..1];
    attribute appliesToVenueText: String[0..1];
  }

  item def NettingSet {
    doc/* set of transactions eligible for netting by CCP or bilateral agreement */
    attribute ccp: CentralCounterparty[0..1];
    attribute participant: Parties::Party[1..*];
    attribute instruction: SettlementInstruction[1..*];
  }
}
