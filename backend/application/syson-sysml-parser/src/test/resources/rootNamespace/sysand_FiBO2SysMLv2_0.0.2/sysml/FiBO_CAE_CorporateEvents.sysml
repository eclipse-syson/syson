// ===================================================================
// FIBO_CAE_CorporateActionsAndEvents.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_CAE_CorporateActionsAndEvents {
  doc/* Configuration Item (CI) package for FIBO_CAE_CorporateActionsAndEvents and its exported model elements. */
  public import CorporateActions::*;
  public import SecurityRelatedCorporateActions::*;
  public import GLEIF_CorporateActionIndividuals::*;
  public import ISO15022_CorporateActionIndividuals::*;
}

// ================================
// CorporateActions.sysml
// (feature-based from CorporateActions.rdf)
// ================================
package CorporateActions {

  // ---- Imports (private) ----
  private import ScalarValues::*;          
  private import Time::*;                  
  private import CurrencyAmount::*;        
  private import Parties::*;               
  private import Organizations::*;         
  private import Contracts::*;             
  private import FinancialDates::*;        
  private import SecuritiesTransactions::*;
  private import ProductsAndServices::*;   

  // =========================================================
  // Enums & classifications
  // =========================================================

  enum def CorporateActionCategory {
    doc/* high-level classification of corporate action events */
    enum Mandatory;              // e.g., stock split, merger
    enum MandatoryWithChoice;    // e.g., dividend reinvestment option
    enum Voluntary;              // e.g., tender offer
    enum Other;
  }

  enum def CorporateActionType {
    doc/* event type aligned with ISO 15022 / ISO 20022 codes */
    enum Dividend; enum StockSplit; enum RightsIssue; enum BonusIssue;
    enum Merger; enum Acquisition; enum TenderOffer; enum SpinOff;
    enum Redemption; enum ExchangeOffer; enum NameChange; enum Reorganization;
    enum Liquidation; enum Meeting; enum ProxyVote; enum Other;
  }

  enum def EventStatus {
    enum Announced; enum Pending; enum Effective; enum Completed; enum Canceled;
  }

  enum def EntitlementType {
    enum Cash; enum Stock; enum Rights; enum Options; enum Notes; enum Other;
  }

  // =========================================================
  // Core corporate action items
  // =========================================================

  item def CorporateActionEvent {
    doc/* event initiated by an issuer that affects securities holders, altering security characteristics, holdings, or entitlements */
    attribute eventId: String[1..1];
    attribute issuer: Organizations::Organization[1..1];
    attribute securityAffected: ProductsAndServices::Product[1..*];
    attribute category: CorporateActionCategory[1..1];
    attribute actionType: CorporateActionType[1..1];
    attribute recordDate: Date[0..1];
    attribute effectiveDate: Date[0..1];
    attribute paymentDate: Date[0..1];
    attribute exDate: Date[0..1];
    attribute status: EventStatus[0..1];
    attribute announcementReference: String[0..1];
    attribute sourceAnnouncement: String[0..1];
  }

  item def CorporateActionEntitlement {
    doc/* entitlement resulting from a corporate action (cash, stock, rights, etc.) */
    attribute entitlementId: String[1..1];
    attribute corpActionEvent: CorporateActionEvent[1..1];
    attribute holder: Parties::Party[0..1];
    attribute entitlementType: EntitlementType[1..1];
    attribute quantityPerShare: Real[0..1];
    attribute amountPerShare: CurrencyAmount::MonetaryAmount[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
  }

  item def AnnouncementDocument {
    doc/* formal issuer communication announcing the terms of a corporate action */
    attribute documentId: String[1..1];
    attribute issuer: Organizations::Organization[0..1];
    attribute announcementDate: Date[0..1];
    attribute description: String[0..1];
    attribute announcementLanguage: String[0..1];
  }

  item def ElectionInstruction {
    doc/* instruction submitted by a security holder to elect among corporate action choices */
    attribute instructionId: String[1..1];
    attribute holder: Parties::Party[1..1];
    attribute relatedEvent: CorporateActionEvent[1..1];
    attribute electionChoiceText: String[0..1];
    attribute quantity: Real[0..1];
    attribute submissionDate: Date[0..1];
  }

  item def CorporateActionProcessingStatus {
    doc/* status of corporate action lifecycle processing for a given account or holder */
    attribute holder: Parties::Party[0..1];
    attribute corpActionEvent: CorporateActionEvent[1..1];
    attribute currentStatusText: String[0..1];
    attribute processedOn: DateTime[0..1];
  }

  item def ReorganizationPlan {
    doc/* plan of merger, consolidation, or reorganization describing terms and treatment for existing securities */
    attribute planId: String[1..1];
    attribute relatedEvents: CorporateActionEvent[0..*];
    attribute description: String[0..1];
    attribute effectiveDate: Date[0..1];
  }

  item def CorporateMeetingEvent specializes CorporateActionEvent {
    doc/* event calling a shareholder or bondholder meeting for voting or information purposes */
    attribute meetingDate: Date[1..1];
    attribute meetingTypeText: String[0..1];
    attribute agendaText: String[0..1];
    attribute proxyDeadline: Date[0..1];
  }

  item def ProxyVotingInstruction {
    doc/* voting instruction submitted by a holder or proxy to a meeting event */
    attribute proxyId: String[1..1];
    attribute meetingEvent: CorporateMeetingEvent[1..1];
    attribute holder: Parties::Party[1..1];
    attribute voteChoiceText: String[0..1];
    attribute submissionDate: Date[0..1];
  }

  item def CorporateActionProcessing {
    doc/* process tracking the full lifecycle of a corporate action across announcement, entitlement, election, and settlement */
    attribute corpActionEvent: CorporateActionEvent[1..1];
    attribute entitlement: CorporateActionEntitlement[0..*];
    attribute elections: ElectionInstruction[0..*];
    attribute settlementTransaction: SecuritiesTransactions::SecuritiesTransaction[0..*];
    attribute lastUpdated: DateTime[0..1];
  }
}

// ================================
// SecurityRelatedCorporateActions.sysml
// (feature-based from SecurityRelatedCorporateActions.rdf)
// ================================
package SecurityRelatedCorporateActions {

  private import ScalarValues::*;
  private import Time::*;
  private import CorporateActions::*;
  private import ProductsAndServices::*;
  private import Organizations::*;
  private import CurrencyAmount::*;

  item def DividendEvent specializes CorporateActions::CorporateActionEvent {
    doc/* event distributing issuer earnings to shareholders as cash or stock */
    attribute dividendTypeText: String[0..1];
    attribute ratePerShare: Real[0..1];
    attribute dividendAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
  }

  item def StockSplitEvent specializes CorporateActions::CorporateActionEvent {
    doc/* event that increases or decreases the number of shares outstanding by dividing or consolidating shares */
    attribute oldShares: Integer[1..1];
    attribute newShares: Integer[1..1];
    attribute ratioText: String[0..1];
  }

  item def RightsIssueEvent specializes CorporateActions::CorporateActionEvent {
    doc/* event granting existing shareholders rights to purchase additional shares */
    attribute subscriptionPrice: CurrencyAmount::MonetaryAmount[0..1];
    attribute rightsRatio: Real[0..1];
    attribute expiryDate: Date[0..1];
  }

  item def BonusIssueEvent specializes CorporateActions::CorporateActionEvent {
    doc/* event issuing additional shares to shareholders free of cost */
    attribute bonusRatio: Real[0..1];
  }

  item def TenderOfferEvent specializes CorporateActions::CorporateActionEvent {
    doc/* event where an acquirer offers to buy shares from existing shareholders at a specified price */
    attribute offerPrice: CurrencyAmount::MonetaryAmount[1..1];
    attribute offerStartDate: Date[0..1];
    attribute offerEndDate: Date[0..1];
    attribute maximumShares: Integer[0..1];
  }

  item def MergerEvent specializes CorporateActions::CorporateActionEvent {
    doc/* event merging two or more companies, exchanging old shares for new shares or cash */
    attribute targetEntity: Organizations::Organization[1..1];
    attribute exchangeRatio: Real[0..1];
    attribute cashComponent: CurrencyAmount::MonetaryAmount[0..1];
  }

  item def SpinOffEvent specializes CorporateActions::CorporateActionEvent {
    doc/* event distributing shares of a subsidiary or new entity to existing shareholders */
    attribute spunEntity: Organizations::Organization[1..1];
    attribute ratio: Real[0..1];
  }

  item def RedemptionEvent specializes CorporateActions::CorporateActionEvent {
    doc/* event where an issuer redeems securities before maturity */
    attribute redemptionPrice: CurrencyAmount::MonetaryAmount[0..1];
    attribute redemptionDate: Date[0..1];
  }

  item def ExchangeOfferEvent specializes CorporateActions::CorporateActionEvent {
    doc/* event allowing holders to exchange securities for other instruments */
    attribute exchangeDescription: String[0..1];
    attribute newSecurity: ProductsAndServices::Product[0..1];
    attribute exchangeRatio: Real[0..1];
  }
}

// ================================
// GLEIF_CorporateActionIndividuals.sysml
// (feature-based from GLEIF-CorporateActionIndividuals.rdf)
// ================================
package GLEIF_CorporateActionIndividuals {

  private import ScalarValues::*;
  private import Time::*;
  private import CorporateActions::*;
  private import SecurityRelatedCorporateActions::*;

  item def LEIDividendExample specializes SecurityRelatedCorporateActions::DividendEvent {
    doc/* example dividend event published in GLEIF corporate action dataset */
    attribute exampleId: String[1..1];
    attribute lei: String[1..1];
  }

  item def LEIMergerExample specializes SecurityRelatedCorporateActions::MergerEvent {
    doc/* example merger event record from GLEIF dataset */
    attribute exampleId: String[1..1];
    attribute lei: String[1..1];
  }
}

// ================================
// ISO15022_CorporateActionIndividuals.sysml
// (feature-based from ISO15022-CorporateActionIndividuals.rdf)
// ================================
package ISO15022_CorporateActionIndividuals {

  private import ScalarValues::*;
  private import Time::*;
  private import CorporateActions::*;
  private import SecurityRelatedCorporateActions::*;

  item def ISO15022DividendEvent specializes SecurityRelatedCorporateActions::DividendEvent {
    doc/* canonical ISO 15022 code example for cash dividend event */
    attribute iso15022Code: String[1..1];   // e.g., "DVCA"
    attribute messageExampleId: String[0..1];
  }

  item def ISO15022StockSplitEvent specializes SecurityRelatedCorporateActions::StockSplitEvent {
    doc/* canonical ISO 15022 code example for stock split event */
    attribute iso15022Code: String[1..1];   // e.g., "SPLF"
    attribute messageExampleId: String[0..1];
  }

  item def ISO15022RightsIssueEvent specializes SecurityRelatedCorporateActions::RightsIssueEvent {
    doc/* canonical ISO 15022 example for rights issue */
    attribute iso15022Code: String[1..1];   // e.g., "RHDI"
    attribute messageExampleId: String[0..1];
  }

  item def ISO15022TenderOfferEvent specializes SecurityRelatedCorporateActions::TenderOfferEvent {
    doc/* canonical ISO 15022 example for tender offer event */
    attribute iso15022Code: String[1..1];   // e.g., "TEND"
    attribute messageExampleId: String[0..1];
  }

  item def ISO15022MergerEvent specializes SecurityRelatedCorporateActions::MergerEvent {
    doc/* canonical ISO 15022 example for merger or takeover */
    attribute iso15022Code: String[1..1];   // e.g., "MRGR"
    attribute messageExampleId: String[0..1];
  }
}
