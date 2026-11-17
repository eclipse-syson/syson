// ===================================================================
// FIBO_SEC_Equities.sysml
// CI Aggregator (public re-export of SEC Equities subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_SEC_Equities {
  doc/* Configuration Item (CI) package for FIBO_SEC_Equities and its exported model elements. */
  public import EquityInstruments::*;
  public import DepositaryReceipts::*;
  public import EquityCFIClassificationIndividuals::*;
}

// ===========================
// EquityInstruments.sysml
// ===========================
package EquityInstruments {

  // ---- Imports (private) ----
  private import ScalarValues::*;            // String, Integer, Real, Boolean
  private import Time::*;                    // Date, DateTime
  private import CurrencyAmount::*;          // MonetaryAmount, Currency
  private import Organizations::*;           // Organization
  private import FormalOrganizations::*;     // FormalOrganization
  private import Parties::*;                 // Party
  private import Markets::*;                 // TradingVenue, Listing
  private import FinancialDates::*;          // ExplicitDatePeriod, RegularSchedule

  // ---- Core equity abstractions ----

  item def EquityInstrument {
    doc/* security that represents an ownership interest in an issuing entity and entitles holders to residual claims on assets and earnings */
    attribute instrumentId: String[0..*];             // ISIN/CUSIP/local code
    attribute ticker: String[0..1];
    attribute issuer: FormalOrganization[0..1];
    attribute currency: Currency[0..1];
    attribute parValue: MonetaryAmount[0..1];
    attribute authorizedShares: Integer[0..1];
    attribute issuedShares: Integer[0..1];
    attribute outstandingShares: Integer[0..1];
    attribute votingRightsText: String[0..1];         // summary (1 share = 1 vote, multiple class, etc.)
    attribute dividendPolicyText: String[0..1];
    attribute listingVenue: Markets::TradingVenue[0..*];
    attribute primaryListing: Markets::TradingVenue[0..1];
    attribute issueDate: Date[0..1];
  }

  item def CommonShare specializes EquityInstrument {
    doc/* equity security that represents residual ownership in the issuing entity and typically conveys voting rights */
  }

  item def PreferredShare specializes EquityInstrument {
    doc/* equity security with priority over common shares regarding dividends and liquidation and may have limited or no voting rights */
    attribute dividendRatePct: Real[0..1];
    attribute isCumulative: Boolean[0..1];
    attribute isParticipating: Boolean[0..1];
    attribute isConvertible: Boolean[0..1];
    attribute conversionTermsText: String[0..1];
    attribute redemptionTermsText: String[0..1];
  }

  // ---- Share classes, actions, and entitlements ----

  item def ShareClass {
    doc/* set of rights/terms defining a category of shares issued by the same issuer */
    attribute className: String[1..1];               // e.g., "Class A"
    attribute votingRightsText: String[0..1];
    attribute dividendPreferenceText: String[0..1];
    attribute convertibilityText: String[0..1];
    attribute numberIssued: Integer[0..1];
  }

  item def EquityAction {
    doc/* action affecting the terms, number, or distribution of equity instruments (corporate action perspective) */
    attribute effectiveDate: Date[0..1];
    attribute recordDate: Date[0..1];
    attribute paymentDate: Date[0..1];
    attribute description: String[0..1];
  }

  item def StockSplit specializes EquityAction {
    doc/* corporate action that increases the number of shares by dividing existing shares */
    attribute splitRatioNumerator: Integer[1..1];
    attribute splitRatioDenominator: Integer[1..1];
  }

  item def ReverseStockSplit specializes EquityAction {
    doc/* corporate action that reduces the number of shares by combining existing shares */
    attribute reverseRatioNumerator: Integer[1..1];
    attribute reverseRatioDenominator: Integer[1..1];
  }

  item def DividendEvent specializes EquityAction {
    doc/* distribution of value to shareholders in cash, shares, or other consideration */
    attribute dividendAmount: MonetaryAmount[0..1];   // for cash dividend
    attribute dividendCurrency: Currency[0..1];
    attribute isStockDividend: Boolean[0..1];
    attribute stockDividendRate: Real[0..1];          // shares per share
  }

  item def RightsIssue specializes EquityAction {
    doc/* offer to existing shareholders to purchase additional shares, usually at a discount, in proportion to existing holdings */
    attribute subscriptionPrice: MonetaryAmount[0..1];
    attribute rightsRatioNumerator: Integer[0..1];
    attribute rightsRatioDenominator: Integer[0..1];
    attribute tradingPeriod: ExplicitDatePeriod[0..1];
  }

  item def Warrant specializes EquityInstrument {
    doc/* security granting the holder the right to purchase the issuer's equity at a specified price before expiry */
    attribute exercisePrice: MonetaryAmount[0..1];
    attribute expirationDate: Date[0..1];
    attribute exerciseStyleText: String[0..1];        // American, European, etc.
    attribute ratioUnderlyingPerWarrant: Real[0..1];
    attribute underlyingIssuer: FormalOrganization[0..1];
  }

  // ---- Identifiers / metadata ----

  item def EquityIdentifier {
    doc/* sequence of characters identifying an equity instrument under a scheme */
    attribute schemeName: String[1..1];               // e.g., ISIN, CUSIP, SEDOL, TICKER
    attribute value: String[1..1];
    attribute assignmentAuthority: Organizations::Organization[0..1];
  }

  item def EquityMetadata {
    doc/* descriptive metadata and analytics hooks for an equity instrument */
    attribute instrument: EquityInstrument[1..1];
    attribute sectorText: String[0..1];
    attribute industryText: String[0..1];
    attribute freeFloatShares: Integer[0..1];
    attribute indexMembershipText: String[0..*];
    attribute referenceDataset: Analytics::Dataset[0..1];
  }
}

// ===========================
// DepositaryReceipts.sysml
// ===========================
package DepositaryReceipts {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import EquityInstruments::*;       // EquityInstrument
  private import FormalOrganizations::*;     // FormalOrganization
  private import Organizations::*;           // Organization
  private import Markets::*;                 // TradingVenue
  private import FinancialDates::*;          // ExplicitDatePeriod

  enum def DRKind {
    doc/* classification of depositary receipts by issuance/scope */
    enum ADR;            // American Depositary Receipt
    enum GDR;            // Global Depositary Receipt
    enum EDR;            // European Depositary Receipt
    enum IDR;            // International Depositary Receipt
    enum BDR;            // Brazilian Depositary Receipt (example)
    enum Other;
  }

  enum def ADRLevel {
    doc/* SEC/NYSE/Nasdaq-recognized ADR program levels */
    enum LevelI;
    enum LevelII;
    enum LevelIII;
    enum Rule144A;
  }

  item def DepositaryReceipt specializes EquityInstrument {
    doc/* negotiable certificate representing shares of a foreign company, held by a depositary bank and traded in another market */
    attribute kind: DRKind[1..1];
    attribute depositaryBank: FormalOrganization[0..1];
    attribute underlyingIssuer: FormalOrganization[0..1];
    attribute underlyingShare: EquityInstruments::EquityInstrument[0..1];
    attribute ratioDRToUnderlying: Real[0..1];          // e.g., 1 DR = 2 underlying shares
    attribute programLevel: ADRLevel[0..1];             // only applicable for ADRs
    attribute programEffective: ExplicitDatePeriod[0..1];
  }

  item def SponsoredDR specializes DepositaryReceipt {
    doc/* depositary receipt established with participation of the underlying issuer */
    attribute sponsor: FormalOrganization[0..1];
  }

  item def UnsponsoredDR specializes DepositaryReceipt {
    doc/* depositary receipt established by a depositary bank without a formal agreement with the underlying issuer */
  }
}

// ===========================================
// EquityCFIClassificationIndividuals.sysml
// ===========================================
package EquityCFIClassificationIndividuals {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import EquityInstruments::*;     // instrument specializations
  private import Organizations::*;         // Organization if needed for notes

  item def CFIEquityCode {
    doc/* ISO 10962 (CFI) equity classification code with description and mapping hints */
    attribute code: String[1..1];           // e.g., "ESVUFR" (example pattern)
    attribute shortDescription: String[1..1];
    attribute notes: String[0..1];
  }

  // Representative CFI â€œindividualsâ€ for common equity types (illustrative)
  item def CFI_Common_SingleVoting {
    attribute cfi: CFIEquityCode[1..1];
    attribute appliesTo: CommonShare[0..*];
  }

  item def CFI_Preferred_Cumulative {
    attribute cfi: CFIEquityCode[1..1];
    attribute appliesTo: PreferredShare[0..*];
  }

  item def CFI_DR_ADR_LevelII {
    attribute cfi: CFIEquityCode[1..1];
    attribute appliesTo: DepositaryReceipts::DepositaryReceipt[0..*];
  }
}
