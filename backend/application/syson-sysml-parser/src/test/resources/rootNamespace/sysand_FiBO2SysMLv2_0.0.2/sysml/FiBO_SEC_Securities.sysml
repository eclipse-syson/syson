// ===================================================================
// FIBO_SEC_Securities.sysml
// CI Aggregator (public re-export of SEC Securities subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_SEC_Securities {
  doc/* Configuration Item (CI) package for FIBO_SEC_Securities and its exported model elements. */
  public import SecuritiesCore::*;
  public import SecuritiesIdentification::*;
  public import SecuritiesIdentificationIndividuals::*;
  public import SecuritiesClassification::*;
  public import SecuritiesIssuance::*;
  public import SecuritiesListings::*;
  public import SecuritiesRestrictions::*;
  public import SecurityAssets::*;
  public import Pools::*;
  public import Baskets::*;
  public import ParametricSchedules::*;
}

// ======================
// SecuritiesCore.sysml
// ======================
package SecuritiesCore {

  // ---- Imports (private) ----
  private import ScalarValues::*;            // String, Integer, Real, Boolean
  private import Time::*;                    // Date, DateTime
  private import CurrencyAmount::*;          // Currency, MonetaryAmount, MonetaryPrice
  private import FinancialDates::*;          // Duration, ExplicitDate, ExplicitDatePeriod, RegularSchedule
  private import Organizations::*;           // Organization
  private import FormalOrganizations::*;     // FormalOrganization
  private import Markets::*;                 // TradingVenue, Market

  enum def SecurityLifecycleStatus { enum Proposed; enum Active; enum Suspended; enum Matured; enum Redeemed; enum Cancelled; enum Defaulted; enum Delisted; }

  // ---- Core security abstraction ----
  item def Security {
    doc/* fungible financial instrument representing ownership, credit, entitlement, or reference exposure */
    attribute securityId: String[0..*];                // generic holder (ISIN/CUSIP/etc.) â€” see Identification pkg
    attribute title: String[0..1];
    attribute issuer: FormalOrganization[0..1];
    attribute currency: Currency[0..1];
    attribute issueDate: Date[0..1];
    attribute firstTradeDate: Date[0..1];
    attribute status: SecurityLifecycleStatus[0..1];
    attribute nominalAmount: MonetaryAmount[0..1];
    attribute description: String[0..1];

    // Cross-domain hooks
    attribute classification: SecuritiesClassification::SecurityClassification[0..*];
    attribute listing: SecuritiesListings::Listing[0..*];
    attribute restriction: SecuritiesRestrictions::SecurityRestriction[0..*];
  }

  // Convenience specializations (non-exhaustive, for typing)
  item def EquitySecurity   specializes Security { }
  item def DebtSecurityCore specializes Security { }       // aligns with SEC_Debt::DebtSecurity via import when combined
  item def FundSecurity     specializes Security { }       // e.g., ETF share / fund unit
  item def DerivativeSecurity specializes Security { }
}

// =================================
// SecuritiesIdentification.sysml
// =================================
package SecuritiesIdentification {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import SecuritiesCore::*;
  private import Organizations::*;
  private import FormalOrganizations::*;

  // Generic identifier wrapper
  item def SecurityIdentifier {
    doc/* identifier value for a security under a named scheme */
    attribute schemeName: String[1..1];          // ISIN, CUSIP, SEDOL, FIGI, TICKER, WKN, VALOR
    attribute value: String[1..1];
    attribute assignmentAuthority: Organizations::Organization[0..1];
    attribute assignedSecurity: Security[0..1];
    attribute assignedIssuer: FormalOrganization[0..1];     // for LEI or issuer codes
    attribute assignedOn: Date[0..1];
  }

  // Convenience typed aliases (optional specializations)
  item def ISIN  specializes SecurityIdentifier { }
  item def CUSIP specializes SecurityIdentifier { }
  item def SEDOL specializes SecurityIdentifier { }
  item def FIGI  specializes SecurityIdentifier { }
  item def TickerIdentifier specializes SecurityIdentifier { }
  item def LEIIdentifier   specializes SecurityIdentifier { } // issuer LEI when used at issuer level
}

// ===============================================
// SecuritiesIdentificationIndividuals.sysml
// ===============================================
package SecuritiesIdentificationIndividuals {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import SecuritiesIdentification::*;
  private import SecuritiesCore::*;
  private import FormalOrganizations::*;

  // Example individuals (illustrative placeholders)
  item def ExampleIssuer specializes FormalOrganization { }

  item def ExampleSecurity specializes SecuritiesCore::Security {
    attribute :>> title: String[0..1] = "Example Security";
  }

  item def ExampleISIN specializes ISIN {
    attribute :>> schemeName: String[1..1] = "ISIN";
    attribute :>> value: String[1..1] = "US0000000001";
    attribute :>> assignedSecurity: SecuritiesCore::Security[0..1];
  }

  item def ExampleFIGI specializes FIGI {
    attribute :>> schemeName: String[1..1] = "FIGI";
    attribute :>> value: String[1..1] = "BBG000BLNNH6";
    attribute :>> assignedSecurity: SecuritiesCore::Security[0..1];
  }
}

// =================================
// SecuritiesClassification.sysml
// =================================
package SecuritiesClassification {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import SecuritiesCore::*;

  enum def AssetClass { enum Equity; enum Debt; enum Fund; enum Derivative; enum FX; enum Commodity; enum Other; }
  enum def ExposureKind { enum Ownership; enum Credit; enum Reference; enum Basket; enum Index; enum Commodity; enum Currency; enum Rate; enum Volatility; enum Other; }

  item def CFI {
    doc/* ISO 10962 CFI code describing category/attributes of a security */
    attribute code: String[1..1];               // 6-char code
    attribute shortDescription: String[0..1];
  }

  item def SecurityClassification {
    doc/* classification record for a security */
    attribute assetClass: AssetClass[0..1];
    attribute exposureKind: ExposureKind[0..1];
    attribute cfi: CFI[0..1];
    attribute notes: String[0..1];
    attribute forSecurity: SecuritiesCore::Security[0..1];
  }
}

// ===========================
// SecuritiesIssuance.sysml
// ===========================
package SecuritiesIssuance {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import SecuritiesCore::*;
  private import Organizations::*;
  private import FormalOrganizations::*;

  enum def OfferingType { enum Public; enum PrivatePlacement; enum Shelf; enum Programmatic; enum Rights; enum Other; }
  enum def AllotmentMethod { enum ProRata; enum BookBuild; enum Auction; enum FixedPrice; enum Other; }

  item def OfferingDocument {
    doc/* legal/marketing document for a securities offering (e.g., prospectus, offering memorandum) */
    attribute title: String[1..1];
    attribute publicationDate: Date[0..1];
    attribute issuer: FormalOrganization[0..1];
    attribute summaryText: String[0..1];
  }

  item def SecuritiesOffering {
    doc/* process/event by which new securities are created and sold */
    attribute issuer: FormalOrganization[1..1];
    attribute offeringType: OfferingType[0..1];
    attribute offeredSecurityTemplate: SecuritiesCore::Security[0..1]; // template/series to be issued
    attribute currency: Currency[0..1];
    attribute plannedSize: MonetaryAmount[0..1];
    attribute issueDate: Date[0..1];
    attribute bookBuildWindow: ExplicitDatePeriod[0..1];
    attribute allotmentMethod: AllotmentMethod[0..1];
    attribute document: OfferingDocument[0..*];
    attribute underwriter: FormalOrganization[0..*];
  }

  item def Series {
    doc/* set of securities issued under common terms */
    attribute seriesName: String[1..1];
    attribute baseSecurity: SecuritiesCore::Security[0..1];
    attribute firstIssueDate: Date[0..1];
    attribute finalIssueDate: Date[0..1];
  }

  item def TrancheRecord {
    doc/* issuance slice with its own size/price/time */
    attribute series: Series[0..1];
    attribute trancheName: String[0..1];
    attribute issueDate: Date[0..1];
    attribute size: MonetaryAmount[0..1];
    attribute price: MonetaryPrice[0..1];
  }
}

// ========================
// SecuritiesListings.sysml
// ========================
package SecuritiesListings {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import SecuritiesCore::*;
  private import Markets::*;                 // TradingVenue, MIC code in your Markets CI

  enum def ListingStatus { enum Applied; enum Listed; enum Suspended; enum Delisted; enum NotListed; }
  enum def QuoteCurrencyPolicy { enum SecurityCurrency; enum VenueCurrency; enum Other; }

  item def Listing {
    doc/* the registration and maintenance of a securityâ€™s presence on a trading venue */
    attribute security: SecuritiesCore::Security[1..1];
    attribute venue: TradingVenue[1..1];
    attribute ticker: String[0..1];
    attribute micCode: String[0..1];
    attribute status: ListingStatus[0..1];
    attribute listedOn: Date[0..1];
    attribute delistedOn: Date[0..1];
    attribute quoteCurrencyPolicy: QuoteCurrencyPolicy[0..1];
    attribute notes: String[0..1];
  }
}

// ============================
// SecuritiesRestrictions.sysml
// ============================
package SecuritiesRestrictions {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import SecuritiesCore::*;
  private import JurisdictionPackage::*;    // Jurisdiction (FND Law)
  private import LegalCorePackage::*;       // Law (FND Law)
  private import Parties::*;                // Party (for eligible investors)

  enum def RestrictionKind {
    enum TransferRestriction;     // e.g., private placement transfer limits
    enum HoldingRestriction;      // e.g., QIB-only
    enum Lockup;                  // e.g., 90/180-day lockup
    enum Legend;                  // restrictive legend requirements
    enum CountryBlock;            // geo restrictions
    enum SanctionsBlock;
    enum Other;
  }

  item def SecurityRestriction {
    doc/* constraint on sale, transfer, holding, or offering of a security */
    attribute appliesTo: SecuritiesCore::Security[1..1];
    attribute kind: RestrictionKind[1..1];
    attribute jurisdiction: Jurisdiction[0..*];
    attribute legalBasis: LegalCorePackage::Law[0..*];
    attribute eligibleInvestorDescription: String[0..1];    // e.g., QIB, accredited, professional client
    attribute lockupEndDate: Date[0..1];
    attribute legendText: String[0..1];
    attribute notes: String[0..1];
  }
}

// ===================
// SecurityAssets.sysml
// ===================
package SecurityAssets {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import SecuritiesCore::*;
  private import Pools::*;
  private import Baskets::*;

  item def UnderlyingAssetLink {
    doc/* linkage from a security to an underlying single asset, basket, or index */
    attribute security: SecuritiesCore::Security[1..1];
    attribute underlyingSecurity: SecuritiesCore::Security[0..1];
    attribute underlyingBasket: Baskets::Basket[0..1];
    attribute underlyingPool: Pools::SecurityPool[0..1];
    attribute weightPercent: Real[0..1];
    attribute notes: String[0..1];
  }

  item def CollateralAssetLink {
    doc/* identifies collateral posted or pledged in relation to a security */
    attribute security: SecuritiesCore::Security[1..1];
    attribute collateralSecurity: SecuritiesCore::Security[0..1];
    attribute collateralDescription: String[0..1];
    attribute haircutPercent: Real[0..1];
  }
}

// ==============
// Pools.sysml
// ==============
package Pools {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import SecuritiesCore::*;

  item def SecurityPool {
    doc/* collection of assets that back one or more securities */
    attribute poolId: String[0..1];
    attribute member: SecuritiesCore::Security[0..*];
    attribute currentBalance: MonetaryAmount[0..1];
    attribute cutOffDate: Date[0..1];
    attribute notes: String[0..1];
  }
}

// ===============
// Baskets.sysml
// ===============
package Baskets {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import SecuritiesCore::*;

  item def Basket {
    doc/* defined set of constituents with associated weights or quantities */
    attribute basketId: String[0..1];
    attribute label: String[0..1];
    attribute creationDate: Date[0..1];
    attribute notes: String[0..1];
    attribute constituent: BasketConstituent[0..*];
  }

  item def BasketConstituent {
    attribute security: SecuritiesCore::Security[1..1];
    attribute weightPercent: Real[0..1];
    attribute quantityUnits: Real[0..1];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
  }
}

// ============================
// ParametricSchedules.sysml
// ============================
package ParametricSchedules {

  // ---- Imports (private) ----
  private import ScalarValues::*;
  private import Time::*;
  private import FinancialDates::*;   // RegularSchedule

  item def ParametricSchedule {
    doc/* schedule defined by a rule or series of dated parameters (e.g., step-up, call dates) */
    attribute name: String[0..1];
    attribute ruleText: String[0..1];
    attribute schedulePoint: SchedulePoint[0..*];
  }

  item def SchedulePoint {
    attribute effectiveDate: Date[1..1];
    attribute valueText: String[0..1];     // human-readable term (e.g., "coupon 5.25%", "call @102")
  }

  // Specialized patterns
  item def StepUpSchedule specializes ParametricSchedule { }
  item def StepDownSchedule specializes ParametricSchedule { }
  item def CallDateSchedule specializes ParametricSchedule { }
  item def PutDateSchedule  specializes ParametricSchedule { }
}

// ==================================================
// EUSecuritiesRestrictions.sysml  (NEW subpackage)
// ==================================================
package EUSecuritiesRestrictions {

  // ---- Imports (private) ----
  private import ScalarValues::*;                  // String, Integer, Real, Boolean
  private import Time::*;                          // Date
  private import SecuritiesRestrictions::*;        // SecurityRestriction base
  private import SecuritiesCore::*;                // Security
  private import JurisdictionPackage::*;           // Jurisdiction (FND Law)
  private import LegalCorePackage::*;              // Law (FND Law)
  private import Parties::*;                       // Party

  // ---- EU frameworks / code lists ----
  enum def EURestrictionBasis {
    doc/* principal EU frameworks that commonly drive sell/hold/offer restrictions */
    enum ProspectusRegulation;     // (EU) 2017/1129
    enum MiFIDII_TargetMarket;     // MiFID II product governance (target market)
    enum PRIIPs_KID;               // Packaged Retail & Insurance-based Investment Products KID
    enum UCITS_Rules;              // UCITS distribution/eligibility
    enum AIFMD;                    // Alternative Investment Fund Managers Directive
    enum MarketAbuse;              // MAR inside info / stabilisation disclosures
    enum Sanctions;                // EU sanctions regimes
    enum Other;
  }

  enum def SFDRCategory {
    doc/* high-level SFDR product classification */
    enum Article6;
    enum Article8;
    enum Article9;
    enum NotApplicable;
  }

  enum def EUSellingRestrictionFlag {
    enum RetailInvestorQualifiedOnly;   // retail allowed only if KID etc.
    enum ProfessionalClientsOnly;       // MiFID professional clients
    enum EligibleCounterpartiesOnly;    // ECPs
    enum QualifiedInvestorsOnly;        // Prospectus Reg â€œqualified investorsâ€
  }

  // ---- EU restriction record ----
  item def EUSecurityRestriction specializes SecuritiesRestrictions::SecurityRestriction {
    doc/* EU-specific restriction record capturing regime basis, investor categorisation, and documentation conditions */
    attribute basis: EURestrictionBasis[1..*];
    attribute sellingFlags: EUSellingRestrictionFlag[0..*];
    attribute qualifiedInvestorDefinitionText: String[0..1];
    attribute kidRequired: Boolean[0..1];
    attribute kidLanguageNote: String[0..1];
    attribute sfdrCategory: SFDRCategory[0..1];
    attribute targetMarketText: String[0..1];
    attribute negativeTargetMarketText: String[0..1];
    attribute passportingNote: String[0..1];
    attribute inJurisdiction: Jurisdiction[0..*];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
    attribute sellingLegend: String[0..1];
  }
}

// ==================================================
// USSecuritiesRestrictions.sysml  (NEW subpackage)
// ==================================================
package USSecuritiesRestrictions {

  // ---- Imports (private) ----
  private import ScalarValues::*;                  // String, Integer, Real, Boolean
  private import Time::*;                          // Date
  private import SecuritiesRestrictions::*;        // SecurityRestriction base
  private import SecuritiesCore::*;                // Security
  private import JurisdictionPackage::*;           // Jurisdiction (FND Law)
  private import LegalCorePackage::*;              // Law (FND Law)
  private import Parties::*;                       // Party

  // ---- U.S. frameworks / code lists ----
  enum def USRestrictionBasis {
    doc/* principal U.S. offering/resale frameworks used for restrictions */
    enum SecuritiesAct_Registration;   // Registered under â€™33 Act
    enum RegD_506b;                    // Private placement to accredited investors (no general solicitation)
    enum RegD_506c;                    // Private placement with general solicitation (verification required)
    enum RegS;                         // Offshore offers/sales
    enum Rule144;                      // Resale of restricted/controlled securities (holding periods)
    enum Rule144A;                     // Resales to QIBs
    enum RegA_Tier1;
    enum RegA_Tier2;
    enum RegCF;                        // Crowdfunding
    enum Section4a2;                   // Private offering exemption (statutory)
    enum BlueSky;                      // State-level requirements
    enum Sanctions;                    // OFAC
    enum Other;
  }

  enum def InvestorEligibilityFlag {
    enum AccreditedInvestorOnly;
    enum QIBOnly;                        // Qualified Institutional Buyer
    enum InstitutionalOnly;              // Institutional accounts
    enum RetailPermitted;                // Registered, or as allowed
  }

  // ---- U.S. restriction record ----
  item def USSecurityRestriction specializes SecuritiesRestrictions::SecurityRestriction {
    doc/* U.S.-specific restriction record capturing basis (â€™33 Act exemptions/conditions), eligibility, and resale limits */
    attribute basis: USRestrictionBasis[1..*];
    attribute investorEligibility: InvestorEligibilityFlag[0..*];
    attribute holdingPeriodMonths: Integer[0..1];        // e.g., Rule 144 6/12 months
    attribute legendRequired: Boolean[0..1];
    attribute cedeEligibleOnly: Boolean[0..1];           // if settlement/process constrained
    attribute restrictedCUSIP: Boolean[0..1];
    attribute generalSolicitationAllowed: Boolean[0..1]; // Reg D 506(c)
    attribute verificationRequired: Boolean[0..1];       // e.g., accredited verification
    attribute inJurisdiction: Jurisdiction[0..*];        // typically United States + states as needed
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
    attribute sellingLegend: String[0..1];
    attribute resaleConditionsText: String[0..1];
  }
}
