// ===================================================================
// FIBO_BE_Partnerships.sysml
// CI Aggregator (public re-export of Partnerships)
// Paste this whole file as-is.
// ===================================================================
package FIBO_BE_Partnerships {
  doc/* Configuration Item (CI) package for FIBO_BE_Partnerships and its exported model elements. */
  public import Partnerships::*;
}

// ======================
// Partnerships.sysml
// ======================
package Partnerships {

  // ---- Imports (private) ----
  private import ScalarValues::*;                 // String, Integer, Real, Boolean
  private import Time::*;                         // Date, DateTime, Duration
  private import Parties::*;                      // Party (people/orgs acting in roles)
  private import Organizations::*;                // Organization
  private import FormalOrganizations::*;          // FormalOrganization
  private import JurisdictionPackage::*;          // Jurisdiction
  private import LegalCorePackage::*;             // Law, LegalInstrument
  private import Documents::*;                    // Document, Text
  private import Addresses::*;                    // PostalAddress
  private import Ownership::*;                    // OwnershipInterest (if FND CI loaded)

  // ---- Enums / code lists ----

  enum def PartnershipForm {
    doc/* legal form of partnership; jurisdiction-specific subtypes can map here */
    enum GeneralPartnership;
    enum LimitedPartnership;
    enum LimitedLiabilityPartnership;         // LLP
    enum LimitedLiabilityLimitedPartnership;  // LLLP
    enum ProfessionalPartnership;
    enum JointVenturePartnership;
    enum Other;
  }

  enum def PartnerRoleType {
    doc/* role classification of a partner */
    enum GeneralPartner;
    enum LimitedPartner;
    enum ManagingPartner;
    enum SeniorPartner;
    enum JuniorPartner;
    enum SilentPartner;
    enum NomineePartner;
    enum CorporatePartner;
    enum Other;
  }

  enum def ProfitAllocationMethod {
    doc/* method by which profits/losses are allocated among partners */
    enum ProRataCapital;
    enum FixedRatio;
    enum PriorityReturnThenSplit;
    enum PreferredReturnWaterfall;
    enum EqualShare;
    enum Other;
  }

  enum def CapitalContributionKind {
    enum Cash; enum Property; enum Services; enum IP; enum Other;
  }

  enum def DissolutionReason {
    enum Expiry; enum UnanimousDecision; enum CourtOrder; enum Insolvency; enum ChangeInLaw; enum Other;
  }

  // ---- Core partnership entity (parts, since it acts/processes) ----

  part def Partnership specializes FormalOrganization {
    doc/* unincorporated business association of two or more persons carrying on a business in common with a view of profit (or other lawful purpose, jurisdictionally defined) */
    attribute partnershipForm: PartnershipForm[1..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute dateEstablished: Date[0..1];
    attribute dateDissolved: Date[0..1];
    attribute dissolutionReason: DissolutionReason[0..1];
    attribute partnershipAgreement: PartnershipAgreement[0..1];
    attribute partner: Partner[1..*];
    attribute capitalAccount: PartnershipCapitalAccount[0..*];
    attribute profitAllocationPolicy: ProfitAllocationPolicy[0..1];
    attribute manager: Partner[0..*];                       // managing partner(s)
  }

  // ---- Partners & roles ----

  part def Partner {
    doc/* party admitted to a partnership pursuant to the partnership agreement with rights and obligations thereunder */
    attribute party: Parties::Party[1..1];
    attribute roleType: PartnerRoleType[1..1];
    attribute admissionDate: Date[0..1];
    attribute withdrawalDate: Date[0..1];
    attribute capitalCommitment: CapitalCommitment[0..1];
    attribute capitalAccount: PartnershipCapitalAccount[0..1];
    attribute profitShareRatio: Real[0..1];                 // 0..1 where applicable
    attribute votingPercent: Real[0..1];
    attribute isDesignatedMember: Boolean[0..1];            // applicable to some LLP regimes
    attribute isGeneralPartner: Boolean[0..1];
    attribute isLimitedPartner: Boolean[0..1];
  }

  // ---- Agreement & key terms (items) ----

  item def PartnershipAgreement {
    doc/* contract among partners governing management, contributions, allocations, distributions, admission/withdrawal, and dissolution */
    attribute title: String[0..1];
    attribute agreementDocument: Document[0..1];
    attribute executionDate: Date[0..1];
    attribute effectiveDate: Date[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute term: DurationValue[0..1];
    attribute profitAllocationMethod: ProfitAllocationMethod[0..1];
    attribute containsWaterfall: DistributionWaterfall[0..1];
    attribute amendment: AgreementAmendment[0..*];
  }

  item def AgreementAmendment {
    doc/* change made to the terms of a partnership agreement */
    attribute amendmentDocument: Document[0..1];
    attribute effectiveDate: Date[0..1];
    attribute summary: String[0..1];
  }

  // ---- Capital & economics ----

  item def CapitalCommitment {
    doc/* total committed capital a partner agrees to contribute, including form and schedule */
    attribute committedAmount: Real[0..1];
    attribute currencyCode: String[0..1];
    attribute contributionKind: CapitalContributionKind[0..*];
    attribute commitmentDate: Date[0..1];
    attribute dueByDate: Date[0..1];
  }

  item def CapitalContribution {
    doc/* actual contribution made by a partner to the partnership */
    attribute partner: Partner[1..1];
    attribute amount: Real[0..1];
    attribute currencyCode: String[0..1];
    attribute contributionKind: CapitalContributionKind[0..1];
    attribute contributionDate: Date[0..1];
    attribute description: String[0..1];
  }

  item def Distribution {
    doc/* payment or in-kind distribution from partnership to a partner */
    attribute partner: Partner[1..1];
    attribute amount: Real[0..1];
    attribute currencyCode: String[0..1];
    attribute distributionDate: Date[0..1];
    attribute description: String[0..1];
  }

  item def ProfitAllocationPolicy {
    doc/* policy defining how profits and losses are allocated among partners (ratios, hurdles, waterfalls) */
    attribute method: ProfitAllocationMethod[1..1];
    attribute preferredReturnRatePct: Real[0..1];
    attribute hurdleRatePct: Real[0..1];
    attribute catchUpPct: Real[0..1];
    attribute carriedInterestPct: Real[0..1];
    attribute notes: String[0..*];
  }

  item def DistributionWaterfall {
    doc/* ordered steps determining how cash flows are distributed among partners */
    attribute stepOrderDescription: String[0..*];
    attribute preferredReturnAccrual: Boolean[0..1];
    attribute carriedInterestPct: Real[0..1];
    attribute gpCatchUpPct: Real[0..1];
  }

  item def PartnershipCapitalAccount {
    doc/* running record of a partnerâ€™s equity in the partnership (contributions + allocations â€“ distributions) */
    attribute partnership: Partnership[1..1];
    attribute partner: Partner[1..1];
    attribute openingBalance: Real[0..1];
    attribute currentBalance: Real[0..1];
    attribute currencyCode: String[0..1];
    attribute lastUpdated: Date[0..1];
  }

  // ---- Interests & ownership (items, references Ownership CI when present) ----

  item def PartnershipInterest {
    doc/* transferable or non-transferable interest evidencing a partnerâ€™s right to profits, losses, and distributions (and possibly management rights) */
    attribute partnership: Partnership[1..1];
    attribute holder: Partner[1..1];
    attribute economicPercent: Real[0..1];
    attribute votingPercent: Real[0..1];
    attribute classLabel: String[0..1];       // e.g., GP Interest, LP Class A
    attribute transferable: Boolean[0..1];
    attribute restrictionsText: String[0..1];
  }

  item def InterestTransfer {
    doc/* transfer of a partnership interest (subject to agreement restrictions and approvals) */
    attribute fromHolder: Partner[1..1];
    attribute toHolder: Partner[1..1];
    attribute interest: PartnershipInterest[1..1];
    attribute transferDate: Date[0..1];
    attribute approvalDocument: Document[0..1];
    attribute reason: String[0..1];
  }

  // ---- Admission, withdrawal, dissolution events ----

  item def PartnerAdmission {
    doc/* event recording the admission of a new partner */
    attribute partnership: Partnership[1..1];
    attribute newPartner: Partner[1..1];
    attribute admissionDate: Date[1..1];
    attribute basisDocument: Document[0..1];
  }

  item def PartnerWithdrawal {
    doc/* event recording withdrawal/retirement of a partner */
    attribute partnership: Partnership[1..1];
    attribute withdrawingPartner: Partner[1..1];
    attribute withdrawalDate: Date[1..1];
    attribute settlementAmount: Real[0..1];
    attribute settlementCurrency: String[0..1];
  }

  item def PartnershipDissolution {
    doc/* event capturing dissolution of the partnership and initiation of winding-up */
    attribute partnership: Partnership[1..1];
    attribute dissolutionDate: Date[1..1];
    attribute reason: DissolutionReason[0..1];
    attribute liquidator: Parties::Party[0..1];
    attribute finalDistributionDate: Date[0..1];
    attribute dissolutionInstrument: LegalCorePackage::LegalInstrument[0..1];
  }

  // ---- Governance & management ----

  item def PartnershipPolicy {
    doc/* internal policy adopted by partners (e.g., conflicts of interest, valuation, expense policy) */
    attribute partnership: Partnership[1..1];
    attribute title: String[1..1];
    attribute document: Document[0..1];
    attribute effectiveFrom: Date[0..1];
    attribute effectiveTo: Date[0..1];
  }

  item def MeetingRecord {
    doc/* minutes/resolution record of a partnersâ€™ meeting or management committee */
    attribute partnership: Partnership[1..1];
    attribute meetingDate: Date[1..1];
    attribute document: Document[0..1];
    attribute resolutionText: String[0..*];
  }

  // ---- Compliance & registry ----

  item def PartnershipRegistration {
    doc/* registration of a partnership with an authority or registry (name registration, LLP filing, etc.) */
    attribute partnership: Partnership[1..1];
    attribute registryName: String[0..1];
    attribute registrationNumber: String[0..1];
    attribute registrationDate: Date[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute statusText: String[0..1];
  }

  item def AnnualFiling {
    doc/* annual report/confirmation statement required by the registry */
    attribute partnership: Partnership[1..1];
    attribute filingDate: Date[1..1];
    attribute periodCovered: DurationValue[0..1];
    attribute document: Document[0..1];
    attribute acceptedByRegistry: Boolean[0..1];
  }

  // ---- Liability & indemnities ----

  item def IndemnityProvision {
    doc/* provision describing indemnity protection for partners/managers */
    attribute agreement: PartnershipAgreement[1..1];
    attribute scopeText: String[0..1];
    attribute exclusionsText: String[0..1];
  }

  item def LiabilityLimitation {
    doc/* statement of liability limitations applicable to partners (e.g., LLP statutory shield) */
    attribute partnership: Partnership[1..1];
    attribute description: String[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
  }
}
