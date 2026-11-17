// ===================================================================
// FIBO_SEC_Funds.sysml
// CI Aggregator (public re-export of SEC Funds subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_SEC_Funds {
  doc/* Configuration Item (CI) package for FIBO_SEC_Funds and its exported model elements. */
  public import CollectiveInvestmentVehicles::*;
  public import Funds::*;
}

// ===================================
// CollectiveInvestmentVehicles.sysml
// (feature-based from CollectiveInvestmentVehicles.rdf)
// ===================================
package CollectiveInvestmentVehicles {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import CurrencyAmount::*;           // Currency, MonetaryAmount
  private import FinancialDates::*;           // Duration, ExplicitDate, ExplicitDatePeriod, RegularSchedule
  private import Organizations::*;            // Organization
  private import FormalOrganizations::*;      // FormalOrganization
  private import JurisdictionPackage::*;      // Jurisdiction (FND Law)
  private import LegalCorePackage::*;         // Law (FND Law)
  private import Markets::*;                  // TradingVenue (if listed)

  // ---- Enums / code lists ----
  enum def FundRegime {
    doc/* high-level regulatory regime or wrapper for a collective investment vehicle */
    enum UCITS;
    enum AIF;
    enum _40Act;              // U.S. Investment Company Act of 1940
    enum _33Act_Registered;   // U.S. Securities Act registered product
    enum PrivatePlacement;
    enum PensionFund;
    enum Other;
  }

  enum def ValuationFrequency { enum Daily; enum Weekly; enum Monthly; enum Quarterly; enum Other; }
  enum def DealingFrequency   { enum Daily; enum Weekly; enum Monthly; enum Quarterly; enum Other; }
  enum def DealingCutoffPolicy { enum Hard; enum Soft; enum Other; }

  enum def RedemptionGatePolicy {
    enum None;
    enum GatesAllowed;
    enum LockupPeriod;
    enum SidePocketAllowed;
    enum SuspensionAllowed;
    enum Other;
  }

  enum def DistributionPolicy { enum Accumulating; enum Distributing; enum Mixed; }

  // ---- Vehicle / umbrella / compartments ----

  // The legal entity that pools assets (organization exists in FND; here we capture fund-specific semantics)
  item def InvestmentFundVehicle {
    doc/* legal arrangement or entity that pools investor capital to invest in accordance with a defined policy */
    attribute legalEntity: FormalOrganization[0..1];         // fund company / trust / SICAV / LLC
    attribute managementCompany: FormalOrganization[0..1];
    attribute investmentManager: FormalOrganization[0..1];
    attribute depositaryOrCustodian: FormalOrganization[0..1];
    attribute administrator: FormalOrganization[0..1];
    attribute transferAgent: FormalOrganization[0..1];
    attribute auditor: FormalOrganization[0..1];

    attribute fundName: String[1..1];
    attribute isin: String[0..1];                             // umbrella-level where applicable
    attribute regime: FundRegime[0..1];
    attribute domicile: Jurisdiction[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];

    attribute baseCurrency: Currency[0..1];
    attribute objectiveText: String[0..1];
    attribute strategyText: String[0..1];

    attribute inceptionDate: Date[0..1];
    attribute terminationDate: Date[0..1];

    // Structure
    attribute umbrella: InvestmentFundVehicle[0..1];          // parent umbrella if this is a sub-fund
    attribute subFund: InvestmentFundVehicle[0..*];           // compartments / sub-funds
  }

  item def FeeSchedule {
    doc/* summary of fees applied at vehicle or share-class level */
    attribute managementFeePct: Real[0..1];                   // per annum
    attribute performanceFeePct: Real[0..1];                  // performance fee rate
    attribute performanceFeeBenchmarkText: String[0..1];
    attribute subscriptionFeePct: Real[0..1];
    attribute redemptionFeePct: Real[0..1];
    attribute otherFeesText: String[0..*];
  }

  item def DealingTerms {
    doc/* operational terms for subscriptions/redemptions */
    attribute dealingFrequency: DealingFrequency[0..1];
    attribute dealingCutoffPolicy: DealingCutoffPolicy[0..1];
    attribute dealingCutoffLocalTime: String[0..1];
    attribute settlementCycleBusinessDays: Integer[0..1];     // e.g., T+3
    attribute redemptionGatePolicy: RedemptionGatePolicy[0..1];
    attribute lockupPeriod: Duration[0..1];
    attribute swingPricingPolicyText: String[0..1];
  }

  item def ValuationPolicy {
    doc/* policy for NAV calculation and publication */
    attribute valuationFrequency: ValuationFrequency[0..1];
    attribute valuationPointLocalTime: String[0..1];
    attribute valuationAgent: FormalOrganization[0..1];
    attribute pricingSourcesText: String[0..1];
  }

  // Link vehicle-level policies (kept modular for reuse at share-class level when overridden)
  item def VehiclePolicies {
    attribute vehicle: InvestmentFundVehicle[1..1];
    attribute feeSchedule: FeeSchedule[0..1];
    attribute dealingTerms: DealingTerms[0..1];
    attribute valuationPolicy: ValuationPolicy[0..1];
  }
}

// ===========================
// Funds.sysml
// (feature-based from Funds.rdf)
// ===========================
package Funds {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import CurrencyAmount::*;           // Currency, MonetaryAmount, MonetaryPrice
  private import FinancialDates::*;           // ExplicitDate, ExplicitDatePeriod, RegularSchedule
  private import CollectiveInvestmentVehicles::*; // InvestmentFundVehicle, policies
  private import Organizations::*;            // Organization
  private import FormalOrganizations::*;      // FormalOrganization
  private import Markets::*;                  // TradingVenue, Listing (for ETFs/listed funds)

  // ---- Enums / code lists ----
  enum def FundInstrumentKind {
    doc/* classification of fund instruments */
    enum MutualFundShare;       // open-end
    enum UCITSShare;
    enum ETFShare;
    enum ClosedEndFundShare;
    enum HedgeFundUnit;
    enum MoneyMarketFundShare;
    enum FeederFundShare;
    enum MasterFundUnit;
    enum Other;
  }

  enum def ShareClassType { enum Accumulating; enum Distributing; enum Income; enum Growth; enum Institutional; enum Retail; enum Other; }

  enum def DealingChannel { enum TransferAgent; enum Exchange; enum MarketMaker; enum Platform; enum Other; }

  // ---- Fund instruments (items) ----

  item def FundShare {
    doc/* security or unit representing an investorâ€™s pro-rata interest in a collective investment vehicle */
    attribute instrumentId: String[0..*];                // ISIN/local code
    attribute ticker: String[0..1];                      // if listed (e.g., ETF)
    attribute kind: FundInstrumentKind[1..1];
    attribute shareClassType: ShareClassType[0..1];

    attribute fundVehicle: InvestmentFundVehicle[1..1];
    attribute currency: Currency[1..1];
    attribute baseCurrencyHedged: Boolean[0..1];         // class-level hedging?
    attribute hedgingPolicyText: String[0..1];

    attribute minimumInitialSubscription: MonetaryAmount[0..1];
    attribute minimumSubsequentSubscription: MonetaryAmount[0..1];
    attribute minimumHolding: MonetaryAmount[0..1];

    attribute dealingChannel: DealingChannel[0..*];
    attribute primaryListingVenue: Markets::TradingVenue[0..1];
    attribute otherListingVenues: Markets::TradingVenue[0..*];

    attribute feeSchedule: FeeSchedule[0..1];            // may override vehicle policies
    attribute dealingTerms: DealingTerms[0..1];          // may override vehicle policies
    attribute valuationPolicy: ValuationPolicy[0..1];

    attribute inceptionDate: Date[0..1];
    attribute closureDate: Date[0..1];
  }

  // Specializations by wrapper/structure
  item def MutualFundShare     specializes FundShare { }
  item def UCITSShare          specializes FundShare { }
  item def ETFShare            specializes FundShare { }
  item def ClosedEndFundShare  specializes FundShare { }
  item def MoneyMarketFundShare specializes FundShare { }
  item def HedgeFundUnit       specializes FundShare { }
  item def FeederFundShare     specializes FundShare { }
  item def MasterFundUnit      specializes FundShare { }

  // ---- Entitlements & events (instrument-level) ----

  item def FundDistributionEvent {
    doc/* distribution of income or capital to holders of fund shares */
    attribute fundShare: FundShare[1..1];
    attribute recordDate: Date[0..1];
    attribute exDate: Date[0..1];
    attribute payDate: Date[0..1];
    attribute distributionCurrency: Currency[0..1];
    attribute cashAmountPerUnit: MonetaryAmount[0..1];
    attribute stockRatePerUnit: Real[0..1];              // units per unit if reinvestment
    attribute distributionPolicy: DistributionPolicy[0..1];
    attribute description: String[0..1];
  }

  item def FundCorporateAction {
    doc/* action affecting fund terms or units (e.g., merge, split, class launch/closure, policy change) */
    attribute fundVehicle: InvestmentFundVehicle[0..1];
    attribute affectedShareClass: FundShare[0..*];
    attribute effectiveDate: Date[1..1];
    attribute actionText: String[1..1];
    attribute details: String[0..1];
  }

  // ---- Dealing artifacts ----

  item def SubscriptionOrder {
    doc/* order to subscribe (buy) fund units via TA or exchange */
    attribute fundShare: FundShare[1..1];
    attribute tradeDate: Date[1..1];
    attribute settlementDate: Date[0..1];
    attribute channel: DealingChannel[0..1];
    attribute grossAmount: MonetaryAmount[0..1];
    attribute netAmount: MonetaryAmount[0..1];
    attribute unitsAllotted: Real[0..1];
  }

  item def RedemptionOrder {
    doc/* order to redeem (sell) fund units via TA or exchange */
    attribute fundShare: FundShare[1..1];
    attribute tradeDate: Date[1..1];
    attribute settlementDate: Date[0..1];
    attribute channel: DealingChannel[0..1];
    attribute grossProceeds: MonetaryAmount[0..1];
    attribute netProceeds: MonetaryAmount[0..1];
    attribute unitsRedeemed: Real[0..1];
  }

  // ---- Identifier wrapper ----

  item def FundIdentifier {
    doc/* code assigned to identify a fund share class, umbrella, or vehicle */
    attribute schemeName: String[1..1];                 // ISIN, SEDOL, CINS, TICKER
    attribute value: String[1..1];
    attribute assignedToShare: FundShare[0..1];
    attribute assignedToVehicle: InvestmentFundVehicle[0..1];
  }
}
