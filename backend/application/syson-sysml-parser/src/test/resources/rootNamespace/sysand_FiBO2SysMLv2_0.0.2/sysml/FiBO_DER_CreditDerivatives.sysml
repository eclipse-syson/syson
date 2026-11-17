// ===================================================================
// FIBO_DER_CreditDerivatives.sysml
// CI Aggregator (public re-export of subpackages)
// ===================================================================
package FIBO_DER_CreditDerivatives {
  doc/* Configuration Item (CI) package for FIBO_DER_CreditDerivatives and its exported model elements. */
  public import CreditDefaultSwaps::*;
}

// ======================
// CreditDefaultSwaps.sysml
// ======================
package CreditDefaultSwaps {

  // ---- Imports (private) ----
  private import ScalarValues::*;             // String, Integer, Real, Boolean
  private import Time::*;                     // Date, DateTime
  private import Parties::*;                  // Party
  private import Organizations::*;            // Organization / FormalOrganization (if needed)
  private import Agreements::*;               // Agreement
  private import Contracts::*;                // Contract, ContractualElement, Warranty, etc.
  private import CurrencyAmount::*;           // MonetaryAmount, MonetaryPrice, Currency
  private import FinancialDates::*;           // ExplicitDate, Duration, ExplicitDatePeriod, RegularSchedule
  private import PaymentsAndSchedules::*;     // PaymentTerm, PaymentSchedule
  private import CashFlows::*;                // CashFlow (settlement realization)
  private import LegalCorePackage::*;                // Law (governing law)
  private import JurisdictionPackage::*;             // Jurisdiction

  // =========================================================
  // Enums & code lists
  // =========================================================

  enum def CreditEvent {
    doc/* ISDA-style credit events applicable to CDS contracts */
    enum Bankruptcy;
    enum FailureToPay;
    enum Restructuring;
    enum RepudiationMoratorium;
    enum ObligationDefault;
    enum ObligationAcceleration;
  }

  enum def SettlementMethod {
    doc/* settlement approach for protection payout */
    enum Physical;      // deliverable obligation for par
    enum Cash;          // cash payout per recovery convention
  }

  enum def RestructuringClause {
    doc/* contractually elected restructuring applicability */
    enum FullRestructuring;     // FR
    enum ModifiedRestructuring; // MR
    enum ModifiedModified;      // MM
    enum NoRestructuring;       // NR
  }

  enum def SeniorityLevel {
    doc/* seniority of the reference/obligation class */
    enum SeniorUnsecured;
    enum Subordinated;
    enum JuniorSubordinated;
    enum Secured;
    enum Other;
  }

  enum def CurrencyRollConvention {
    doc/* premium roll date convention for CDS indexes (common market rolls) */
    enum IMM;            // Mar/Jun/Sep/Dec 20th (or standard IMM)
    enum Standard;       // Standardized roll (e.g., Mar/Jun/Sep/Dec 20)
    enum Custom;
  }

  // =========================================================
  // Core CDS contract & key components (items)
  // =========================================================

  item def CreditDefaultSwap specializes Contract {
    doc/* bilateral credit derivative where a protection buyer pays a periodic premium to a protection seller in exchange for protection against specified credit events of a reference entity/obligation */

    // Parties
    attribute protectionBuyer: Parties::Party[1..1];
    attribute protectionSeller: Parties::Party[1..1];
    attribute calculationAgent: Parties::Party[0..1];

    // Reference & scope
    attribute referenceEntity: Parties::Party[0..1];             // typically a formal organization (obligor)
    attribute referenceObligation: ReferenceObligation[0..*];
    attribute deliverableObligation: DeliverableObligation[0..*];
    attribute obligationSeniority: SeniorityLevel[0..1];

    // Dates & tenor
    attribute tradeDate: Date[0..1];
    attribute maturityDate: FinancialDates::ExplicitDate[0..1];
    attribute protectionPeriod: FinancialDates::ExplicitDatePeriod[0..1];

    // Premium leg (running spread, schedule)
    attribute notional: CurrencyAmount::MonetaryAmount[1..1];
    attribute runningSpreadBps: Real[0..1];                      // e.g., 100 = 100 bps
    attribute fixedPremium: CurrencyAmount::MonetaryPrice[0..1]; // if quoted as monetary rate
    attribute premiumCurrency: CurrencyAmount::Currency[0..1];
    attribute premiumSchedule: PaymentsAndSchedules::PaymentSchedule[0..1];

    // Upfront & accrual
    attribute upfrontAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute accrualStartDate: FinancialDates::ExplicitDate[0..1];
    attribute accrualEndDate: FinancialDates::ExplicitDate[0..1];

    // Protection leg & settlement
    attribute settlementMethod: SettlementMethod[0..1];
    attribute restructuringClause: RestructuringClause[0..1];
    attribute applicableCreditEvent: CreditEvent[1..*];

    // Realization
    attribute settlementCashFlow: CashFlows::CashFlow[0..*];
    attribute terms: Agreements::Agreement[0..*];
  }

  item def SingleNameCDS specializes CreditDefaultSwap {
    doc/* CDS where the reference is a single legal entity (obligor) */
  }

  item def IndexCDS specializes CreditDefaultSwap {
    doc/* CDS referencing a standardized index of entities with rolling series */
    attribute indexName: String[1..1];
    attribute indexSeries: Integer[0..1];
    attribute indexVersion: Integer[0..1];
    attribute rollConvention: CurrencyRollConvention[0..1];
  }

  item def BasketCDS specializes CreditDefaultSwap {
    doc/* CDS referencing a basket of entities/obligations, often with a k-of-n trigger */
    attribute basketMember: Parties::Party[1..*];
    attribute triggerK: Integer[0..1];      // k-of-n default trigger
  }

  item def TrancheCDS specializes CreditDefaultSwap {
    doc/* tranched credit default swap (index or bespoke) with attachment/detachment */
    attribute attachmentPointPct: Real[1..1];   // e.g., 0.03 = 3%
    attribute detachmentPointPct: Real[1..1];   // e.g., 0.07 = 7%
  }

  // =========================================================
  // Reference obligations & deliverables
  // =========================================================

  item def ReferenceObligation {
    doc/* specific obligation used to define the scope of credit protection for the reference entity */
    attribute obligationId: String[0..1];                // identifier (e.g., ISIN/CUSIP)
    attribute seniority: SeniorityLevel[0..1];
    attribute description: String[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
    attribute notionalAmount: CurrencyAmount::MonetaryAmount[0..1];
  }

  item def DeliverableObligation {
    doc/* obligation acceptable for delivery under physical settlement */
    attribute obligationId: String[0..1];
    attribute seniority: SeniorityLevel[0..1];
    attribute description: String[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
    attribute faceAmount: CurrencyAmount::MonetaryAmount[0..1];
  }

  // =========================================================
  // Economic terms, events, and confirmations
  // =========================================================

  item def CreditEventNotice {
    doc/* formal notice that a credit event has occurred per contract definitions */
    attribute relatedCds: CreditDefaultSwap[1..1];
    attribute eventType: CreditEvent[1..1];
    attribute creditEventDate: ExplicitDate[0..1];
    attribute noticeDate: ExplicitDate[0..1];
    attribute submittedBy: Parties::Party[0..1];
    attribute description: String[0..1];
  }

  item def ProtectionSettlement {
    doc/* realized protection payout with method and cash flow(s) */
    attribute relatedCds: CreditDefaultSwap[1..1];
    attribute settlementMethod: SettlementMethod[1..1];
    attribute recoveryRatePct: Real[0..1];                 // for cash settlement
    attribute settlementAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute settlementDate: FinancialDates::ExplicitDate[0..1];
    attribute cashFlow: CashFlows::CashFlow[0..*];
  }

  item def CDSConfirmation {
    doc/* economic confirmation of a CDS trade capturing final terms as agreed between counterparties */
    attribute tradeIdentifier: String[1..1];
    attribute confirmedCds: CreditDefaultSwap[1..1];
    attribute confirmationDate: Date[0..1];
    attribute platform: String[0..1];        // e.g., clearing or confirmation platform
    attribute notes: String[0..*];
  }

  // =========================================================
  // Pricing/quotation helpers
  // =========================================================

  item def CDSQuote {
    doc/* market quotation for a CDS: running spread and/or upfront for a standard coupon */
    attribute quotedEntity: Parties::Party[0..1];         // reference entity for single-name
    attribute indexName: String[0..1];                    // for index quotes
    attribute maturityDate: FinancialDates::ExplicitDate[0..1];
    attribute standardCouponBps: Real[0..1];              // e.g., 100 or 500 bps
    attribute runningSpreadBps: Real[0..1];
    attribute upfrontAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute quoteDateTime: DateTime[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
  }

  item def RecoveryLock {
    doc/* agreement fixing a recovery rate for settlement of a CDS credit event */
    attribute referenceEntity: Parties::Party[1..1];
    attribute fixedRecoveryPct: Real[1..1];
    attribute effectiveDate: FinancialDates::ExplicitDate[0..1];
    attribute expiryDate: FinancialDates::ExplicitDate[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
  }
}
