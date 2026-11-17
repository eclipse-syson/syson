// ===================================================================
// FIBO_MD_TemporalCore.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_MD_TemporalCore {
  doc/* Configuration Item (CI) package for FIBO_MD_TemporalCore and its exported model elements. */
  public import SecurityTradingStatuses::*;
  public import SecurityCreditStatuses::*;
}

// =======================================
// SecurityTradingStatuses.sysml
// (feature-based from SecurityTradingStatuses.rdf; aligned with FBC/SEC)
// =======================================
package SecurityTradingStatuses {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import Organizations::*;            // Exchange, Regulator, MarketOperator
  private import Markets::*;                  // TradingVenue (from FBC FunctionalEntities)
  private import FinancialInstruments::*;     // FinancialInstrument, SecurityIdentifier
  private import SecuritiesIdentification::*; // SecurityIdentifier (SEC)
  private import Analytics::*;                // Dataset (optionally for history collections)
  private import JurisdictionPackage::*;      // Jurisdiction (from FND Law)
  private import LegalCorePackage::*;         // Law (basis for halts/suspensions)

  // =========================================================
  // Enumerations / code lists
  // =========================================================
  enum def TradingStatusKind {
    doc/* high-level trading state for a listed security */
    enum Active;            // trading permitted
    enum Halted;            // temporary halt
    enum Suspended;         // suspended by venue/regulator
    enum Delisted;          // removed from listing
    enum WhenIssued;        // conditional trading prior to issuance
    enum WhenDistributed;   // conditional trading prior to distribution
    enum PreOpen;           // auction/pre-open phase
    enum PostClose;         // after-hours/closed phase
    enum Closed;            // venue closed
    enum Other;
  }

  enum def HaltReasonCode {
    doc/* common halt reason categories */
    enum NewsPending;
    enum NewsReleased;
    enum OrderImbalance;
    enum CircuitBreakerLevel1;
    enum CircuitBreakerLevel2;
    enum CircuitBreakerLevel3;
    enum RegulatoryConcern;
    enum ExtraordinaryEvent;
    enum TechnicalIssue;
    enum CorporateAction;
    enum Other;
  }

  enum def ListingEventType {
    enum InitialListing;
    enum TransferOfListing;
    enum TickerChange;
    enum NameChange;
    enum Delisting;
    enum Relisting;
    enum ListingTierChange;
    enum Other;
  }

  enum def ListingTier {
    enum Primary;
    enum Secondary;
    enum OverTheCounter;
    enum Pink;          // OTC Pink tiers
    enum Professional;
    enum Growth;
    enum Venture;
    enum Other;
  }

  // =========================================================
  // References & identity
  // =========================================================
  item def SecurityRef {
    doc/* reference handle to a specific security for linking temporal records */
    attribute instrument: FinancialInstruments::FinancialInstrument[0..1];
    attribute identifier: SecuritiesIdentification::SecurityIdentifier[0..*];
    attribute primaryTicker: String[0..1];
    attribute issuer: Organizations::Organization[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
  }

  // =========================================================
  // Trading status records & events
  // =========================================================
  item def TradingStatusRecord {
    doc/* time-stamped trading status for a security on a venue */
    attribute security: SecurityRef[1..1];
    attribute venue: Markets::TradingVenue[0..1];
    attribute status: TradingStatusKind[1..1];
    attribute effectiveFrom: DateTime[1..1];
    attribute effectiveTo: DateTime[0..1];
    attribute notes: String[0..1];
  }

  item def TradingHaltEvent {
    doc/* event describing a trading halt/suspension */
    attribute security: SecurityRef[1..1];
    attribute venue: Markets::TradingVenue[0..1];
    attribute reason: HaltReasonCode[0..1];
    attribute announcedAt: DateTime[0..1];
    attribute effectiveFrom: DateTime[1..1];
    attribute effectiveTo: DateTime[0..1];
    attribute orderedBy: Organizations::Organization[0..1];     // exchange/regulator
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute description: String[0..1];
  }

  // =========================================================
  // Listing lifecycle
  // =========================================================
  item def ListingRecord {
    doc/* listing attributes of a security on a given venue/tier */
    attribute security: SecurityRef[1..1];
    attribute venue: Markets::TradingVenue[1..1];
    attribute listingTier: ListingTier[0..1];
    attribute listedFrom: Date[0..1];
    attribute listedTo: Date[0..1];
    attribute isPrimaryListing: Boolean[0..1];
  }

  item def ListingEvent {
    doc/* lifecycle event related to listing/relocation/delisting */
    attribute security: SecurityRef[1..1];
    attribute venue: Markets::TradingVenue[0..1];
    attribute eventType: ListingEventType[1..1];
    attribute eventDate: Date[1..1];
    attribute description: String[0..1];
    attribute referenceDoc: Documents::Document[0..1];
  }

  // =========================================================
  // Collections
  // =========================================================
  item def TradingStatusHistory {
    doc/* collection of status records for audit and analytics */
    attribute security: SecurityRef[1..1];
    attribute record: TradingStatusRecord[1..*];
    attribute dataset: Analytics::Dataset[0..1];
  }
}

// =======================================
// SecurityCreditStatuses.sysml
// (feature-based from SecurityCreditStatuses.rdf; aligned with FBC/FND/DER)
// =======================================
package SecurityCreditStatuses {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import CurrencyAmount::*;           // MonetaryAmount (for arrears, past-due)
  private import Organizations::*;            // Organization (issuer, rating agency)
  private import FinancialInstruments::*;     // FinancialInstrument, DebtInstrument
  private import CreditRatings::*;            // Rating scales/codes (from FBC DebtAndEquities)
  private import Analytics::*;                // Dataset (for vintage histories)
  private import JurisdictionPackage::*;      // Jurisdiction
  private import LegalCorePackage::*;         // Law (default basis)

  // =========================================================
  // Enumerations / code lists
  // =========================================================
  enum def CreditPerformanceStatus {
    doc/* current credit performance state for a security/issuer */
    enum Current;                 // no arrears
    enum PastDue_30;              // 30+ dpd
    enum PastDue_60;
    enum PastDue_90;
    enum NonAccrual;
    enum Default;                 // payment default or bankruptcy filing
    enum DistressedExchange;      // coercive restructuring
    enum InWorkout;               // restructuring/forbearance ongoing
    enum Cured;                   // returned to current
    enum Other;
  }

  enum def WatchFlag { enum None; enum Positive; enum Negative; enum Evolving; }

  enum def OutlookFlag { enum Positive; enum Stable; enum Negative; enum Developing; }

  // =========================================================
  // Rating & watch records
  // =========================================================
  item def CreditRatingRecord {
    doc/* instrument- or issuer-level credit rating as of a date */
    attribute instrument: FinancialInstruments::FinancialInstrument[0..1];
    attribute issuer: Organizations::Organization[0..1];
    attribute ratingAgency: Organizations::Organization[1..1];
    attribute ratingCode: String[1..1];          // e.g., 'A-', 'Ba2'
    attribute scaleName: String[0..1];           // LT/Issuer/Issue/National
    attribute outlook: OutlookFlag[0..1];
    attribute watch: WatchFlag[0..1];
    attribute ratingDate: Date[1..1];
    attribute rationaleText: String[0..1];
  }

  item def RatingActionEvent {
    doc/* action on a rating (upgrade, downgrade, affirm, watch add/remove) */
    attribute priorRating: CreditRatingRecord[0..1];
    attribute newRating: CreditRatingRecord[1..1];
    attribute actionDate: Date[1..1];
    attribute actionText: String[0..1];          // "Upgrade", "Downgrade", "Affirm", "Watch Negative", etc.
    attribute referenceDoc: Documents::Document[0..1];
  }

  // =========================================================
  // Credit performance & default tracking
  // =========================================================
  item def CreditStatusRecord {
    doc/* current credit performance status for a security (often debt) */
    attribute instrument: FinancialInstruments::FinancialInstrument[1..1];
    attribute status: CreditPerformanceStatus[1..1];
    attribute statusAsOf: Date[1..1];
    attribute daysPastDue: Integer[0..1];
    attribute pastDueAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute notes: String[0..1];
  }

  item def DefaultEventRecord {
    doc/* detailed record of a payment default, bankruptcy, or distressed exchange */
    attribute instrument: FinancialInstruments::FinancialInstrument[1..1];
    attribute eventDate: Date[1..1];
    attribute eventKindText: String[1..1];       // "Payment Default", "Bankruptcy", "Distressed Exchange"
    attribute missedAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute description: String[0..1];
  }

  item def RecoveryOutcomeRecord {
    doc/* outcome after default (recovery rate/price) */
    attribute instrument: FinancialInstruments::FinancialInstrument[1..1];
    attribute referenceDate: Date[1..1];
    attribute recoveryRatePct: Real[0..1];
    attribute recoveryPricePer100: Real[0..1];
    attribute notes: String[0..1];
  }

  // =========================================================
  // Collections / histories
  // =========================================================
  item def CreditStatusHistory {
    doc/* time-ordered collection of credit status for audit/analytics */
    attribute instrument: FinancialInstruments::FinancialInstrument[1..1];
    attribute record: CreditStatusRecord[1..*];
    attribute dataset: Analytics::Dataset[0..1];
  }

  item def RatingHistory {
    doc/* time-ordered collection of credit ratings */
    attribute subjectIssuer: Organizations::Organization[0..1];
    attribute subjectInstrument: FinancialInstruments::FinancialInstrument[0..1];
    attribute record: CreditRatingRecord[1..*];
    attribute dataset: Analytics::Dataset[0..1];
  }
}
