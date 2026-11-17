// ===================================================================
// FIBO_BP_SecuritiesIssuance.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_BP_SecuritiesIssuance {
  doc/* Configuration Item (CI) package for FIBO_BP_SecuritiesIssuance and its exported model elements. */
  public import IssuanceProcess::*;
  public import IssuanceDocuments::*;
  public import DebtIssuance::*;
  public import EquitiesIPOIssuance::*;
  public import MBSIssuance::*;
  public import AgencyMBSIssuance::*;
  public import PrivateLabelMBSIssuance::*;
  public import MuniIssuance::*;
}

// ================================
// IssuanceProcess.sysml
// (feature-based from IssuanceProcess.rdf)
// ================================
package IssuanceProcess {

  // ---- Imports (private) ----
  private import ScalarValues::*;              // String, Integer, Real, Boolean
  private import Time::*;                      // Date, DateTime
  private import Parties::*;                   // Party (actors)
  private import Organizations::*;             // Organization / FormalOrganization
  private import Agreements::*;                // Agreement
  private import Contracts::*;                 // Contract
  private import CurrencyAmount::*;            // MonetaryAmount
  private import FinancialDates::*;            // ExplicitDate, Duration, RegularSchedule
  private import Documents::*;                 // Document, Text (from Arrangements CI if present)

  // ---- Enums ----
  enum def IssuanceStage {
    enum Mandate; enum Structuring; enum Documentation; enum Marketing;
    enum BookBuilding; enum Pricing; enum Allocation; enum Closing; enum PostIssuance;
  }

  enum def DistributionMethod {
    enum PublicOffering; enum PrivatePlacement; enum Shelf; enum BoughtDeal; enum Auction;
  }

  enum def ListingStatus {
    enum Unlisted; enum Applied; enum Listed; enum Delisted;
  }

  // ---- Core process items (items) and actors (parts) ----

  part def IssuanceParticipant {
    doc/* role played by a party or organization in an issuance (issuer, underwriter, counsel, trustee, paying agent, registrar) */
    attribute roleName: String[1..1];
    attribute party: Parties::Party[0..1];
    attribute organization: Organizations::Organization[0..1];
  }

  item def SecuritiesIssuanceProcess {
    doc/* structured process to bring a security to market, including preparation, marketing, pricing, allocation, and closing */
    attribute name: String[1..1];
    attribute description: String[0..1];
    attribute issuer: Organizations::Organization[1..1];
    attribute participants: IssuanceParticipant[1..*];
    attribute distributionMethod: DistributionMethod[0..1];
    attribute listingVenue: String[0..1];
    attribute listingStatus: ListingStatus[0..1];
    attribute timetable: RegularSchedule[0..1];
    attribute currentStage: IssuanceStage[0..1];
    attribute governingAgreement: Agreements::Agreement[0..*];
    attribute governingContract: Contracts::Contract[0..*];
    attribute keyDocument: Document[0..*];           // prospectus, OS, term sheet, etc.
    attribute expectedProceeds: MonetaryAmount[0..1];
    attribute launchDate: Date[0..1];
    attribute pricingDate: Date[0..1];
    attribute settlementDate: Date[0..1];
    attribute closingDate: Date[0..1];
  }

  item def BookBuildingRecord {
    doc/* record of investor indications, orders, and allocations for an issuance */
    attribute process: SecuritiesIssuanceProcess[1..1];
    attribute orderBookId: String[1..1];
    attribute totalDemandSharesOrUnits: Real[0..1];
    attribute priceRangeLow: Real[0..1];
    attribute priceRangeHigh: Real[0..1];
    attribute finalPrice: Real[0..1];
    attribute allocationNote: String[0..*];
    attribute updatedAt: DateTime[0..1];
  }

  item def PricingSupplement {
    doc/* document that finalizes key economic terms at pricing */
    attribute process: SecuritiesIssuanceProcess[1..1];
    attribute text: Text[0..1];
    attribute pricingDate: Date[0..1];
  }

  item def ClosingChecklist {
    doc/* list of closing deliverables (opinions, certificates, executed docs) */
    attribute process: SecuritiesIssuanceProcess[1..1];
    attribute itemText: String[0..*];
    attribute completedOn: Date[0..1];
  }
}

// ================================
// IssuanceDocuments.sysml
// (feature-based from IssuanceDocuments.rdf)
// ================================
package IssuanceDocuments {

  private import ScalarValues::*;
  private import Time::*;
  private import Organizations::*;
  private import Documents::*;                // preferred

  enum def ProspectusType { enum BaseProspectus; enum FinalProspectus; enum PreliminaryProspectus; enum OfferingCircular; enum OfficialStatement; enum TermSheet; }

  item def Prospectus {
    doc/* disclosure document describing the issuer, the securities, and risk factors for an offering */
    attribute type: ProspectusType[0..1];
    attribute issuer: Organizations::Organization[1..1];
    attribute title: String[0..1];
    attribute text: Text[0..1];
    attribute publicationDate: Date[0..1];
    attribute filedWith: String[0..1];                 // regulator or exchange
    attribute version: String[0..1];
  }

  item def OfferingCircular specializes Prospectus {
    doc/* disclosure document used for offerings where a formal prospectus is not required */
  }

  item def TermSheet {
    doc/* summary document that outlines key terms and conditions of an issuance */
    attribute issuer: Organizations::Organization[1..1];
    attribute text: Text[0..1];
    attribute asOfDate: Date[0..1];
  }

  item def Indenture {
    doc/* trust indenture or equivalent governing debt security terms and trustee appointment */
    attribute trusteeName: String[0..1];
    attribute governingLawText: String[0..1];
    attribute effectiveDate: Date[0..1];
  }

  item def PoolingAndServicingAgreement {
    doc/* PSA defining pooling of assets and servicing obligations for securitizations */
    attribute depositorName: String[0..1];
    attribute trusteeName: String[0..1];
    attribute servicerName: String[0..1];
    attribute text: Text[0..1];
    attribute effectiveDate: Date[0..1];
  }

  item def OfficialStatement specializes Prospectus {
    doc/* disclosure document used in U.S. municipal securities offerings */
  }

  item def PreliminaryOfficialStatement specializes OfficialStatement {
    doc/* preliminary OS distributed before pricing in municipal offerings */
  }
}

// ================================
// DebtIssuance.sysml
// (feature-based from DebtIssuance.rdf)
// ================================
package DebtIssuance {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Organizations::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import IssuanceProcess::*;
  private import IssuanceDocuments::*;

  enum def CouponType { enum Fixed; enum Floating; enum Zero; enum StepUp; enum StepDown; }
  enum def DayCount { enum ACT360; enum ACT365F; enum Thirty360; enum ACTACT; }
  enum def PaymentFrequency { enum Monthly; enum Quarterly; enum SemiAnnual; enum Annual; enum Other; }
  enum def DebtSecurityType { enum Bond; enum Note; enum CommercialPaper; enum CertificateOfDeposit; enum MediumTermNote; enum Other; }

  item def DebtOffering {
    doc/* offering of debt securities to investors through a defined process */
    attribute process: IssuanceProcess::SecuritiesIssuanceProcess[1..1];
    attribute issuer: Organizations::Organization[1..1];
    attribute securityType: DebtSecurityType[1..1];
    attribute currency: CurrencyAmount::Currency[1..1];
    attribute issueAmount: CurrencyAmount::MonetaryAmount[1..1];
    attribute expectedRatingText: String[0..*];
    attribute useOfProceedsText: String[0..1];
    attribute prospectus: IssuanceDocuments::Prospectus[0..1];
    attribute indenture: IssuanceDocuments::Indenture[0..1];
  }

  item def DebtSecurityTerms {
    doc/* core economic terms of a debt security */
    attribute couponType: CouponType[1..1];
    attribute couponRatePct: Real[0..1];
    attribute resetIndexText: String[0..1];                // for floaters
    attribute spreadBps: Real[0..1];
    attribute dayCount: DayCount[0..1];
    attribute paymentFrequency: PaymentFrequency[0..1];
    attribute maturityDate: FinancialDates::ExplicitDate[1..1];
    attribute firstCouponDate: FinancialDates::ExplicitDate[0..1];
    attribute callScheduleNote: String[0..*];
    attribute putScheduleNote: String[0..*];
  }

  item def DebtTranche {
    doc/* single class or tranche of a debt issuance */
    attribute seriesName: String[0..1];
    attribute terms: DebtSecurityTerms[1..1];
    attribute offeredAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute issuePricePct: Real[0..1];                   // percent of par
    attribute yieldPct: Real[0..1];
    attribute cusipOrIsin: String[0..1];
  }
}

// ================================
// EquitiesIPOIssuance.sysml
// (feature-based from EquitiesIPOIssuance.rdf)
// ================================
package EquitiesIPOIssuance {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Organizations::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import IssuanceProcess::*;
  private import IssuanceDocuments::*;

  enum def ShareClass { enum Common; enum Preferred; enum DualClassA; enum DualClassB; enum Other; }
  enum def OfferingType { enum IPO; enum FollowOn; enum Secondary; enum DirectListing; enum SPAC; }

  item def EquityOffering {
    doc/* offering of equity securities (IPO, follow-on, secondary) */
    attribute process: IssuanceProcess::SecuritiesIssuanceProcess[1..1];
    attribute issuer: Organizations::Organization[1..1];
    attribute offeringType: OfferingType[1..1];
    attribute shareClass: ShareClass[1..1];
    attribute sharesOffered: Real[0..1];
    attribute greenshoePct: Real[0..1];
    attribute priceRangeLow: Real[0..1];
    attribute priceRangeHigh: Real[0..1];
    attribute offerPrice: Real[0..1];
    attribute expectedProceeds: CurrencyAmount::MonetaryAmount[0..1];
    attribute listingExchange: String[0..1];
    attribute prospectus: IssuanceDocuments::Prospectus[0..1];
    attribute lockupPeriodDays: Integer[0..1];
  }

  item def UnderwritingSyndicate {
    doc/* group of underwriters jointly managing distribution and risk */
    attribute leadLeftName: String[0..1];
    attribute jointBookrunners: String[0..*];
    attribute coManagers: String[0..*];
  }

  item def AllocationRecord {
    doc/* record of share allocations to investors in an equity offering */
    attribute offering: EquityOffering[1..1];
    attribute totalOrdersShares: Real[0..1];
    attribute allocatedShares: Real[0..1];
    attribute notes: String[0..*];
  }
}

// ================================
// MBSIssuance.sysml
// (feature-based from MBSIssuance.rdf)
// ================================
package MBSIssuance {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Organizations::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import IssuanceProcess::*;
  private import IssuanceDocuments::*;

  enum def CollateralType { enum AgencyEligible; enum NonAgency; enum Jumbo; enum AltA; enum Subprime; enum Other; }
  enum def TrancheType { enum Senior; enum Mezzanine; enum Subordinate; enum IO; enum PO; enum Support; enum Residual; }

  item def MortgageLoan {
    doc/* mortgage loan collateral with key attributes used for pooling and securitization */
    attribute loanId: String[1..1];
    attribute principalBalance: CurrencyAmount::MonetaryAmount[1..1];
    attribute interestRatePct: Real[0..1];
    attribute termMonths: Integer[0..1];
    attribute propertyTypeText: String[0..1];
    attribute ownerOccupancyText: String[0..1];
    attribute originationDate: Date[0..1];
  }

  item def LoanPool {
    doc/* pool of mortgage loans aggregated for securitization */
    attribute poolId: String[1..1];
    attribute collateralType: CollateralType[0..1];
    attribute loans: MortgageLoan[1..*];
    attribute aggregateBalance: CurrencyAmount::MonetaryAmount[0..1];
    attribute weightedAvgCouponPct: Real[0..1];
    attribute weightedAvgMaturityMonths: Integer[0..1];
  }

  item def SecuritizationOffering {
    doc/* offering of asset-backed securities supported by a loan pool */
    attribute process: IssuanceProcess::SecuritiesIssuanceProcess[1..1];
    attribute sponsor: Organizations::Organization[1..1];
    attribute depositor: Organizations::Organization[0..1];
    attribute trustee: Organizations::Organization[0..1];
    attribute servicer: Organizations::Organization[0..1];
    attribute loanPool: LoanPool[1..1];
    attribute psa: IssuanceDocuments::PoolingAndServicingAgreement[0..1];
    attribute prospectus: IssuanceDocuments::Prospectus[0..1];
  }

  item def Tranche {
    doc/* class of notes issued by a securitization with priority rules */
    attribute className: String[1..1];
    attribute type: TrancheType[1..1];
    attribute initialBalance: CurrencyAmount::MonetaryAmount[1..1];
    attribute couponRatePct: Real[0..1];
    attribute legalFinalMaturity: FinancialDates::ExplicitDate[0..1];
    attribute cusipOrIsin: String[0..1];
  }
}

// ================================
// AgencyMBSIssuance.sysml
// (feature-based from AgencyMBSIssuance.rdf & AgencyMBSIssuance.rdf)
// ================================
package AgencyMBSIssuance {

  private import ScalarValues::*;
  private import Time::*;
  private import Organizations::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import IssuanceProcess::*;
  private import IssuanceDocuments::*;
  private import MBSIssuance::*;

  enum def Agency { enum GNMA; enum FNMA; enum FHLMC; }

  item def AgencyPool specializes MBSIssuance::LoanPool {
    doc/* mortgage loan pool eligible for issuance through an agency guarantor */
    attribute agency: Agency[1..1];
    attribute poolTypeText: String[0..1];          // e.g., TBA-eligible, custom pool
  }

  item def AgencyMBSOffering specializes MBSIssuance::SecuritizationOffering {
    doc/* agency-guaranteed MBS issuance (e.g., GNMA, FNMA, FHLMC) */
    attribute agencyPool: AgencyPool[1..1];
    attribute guaranteeFeeBps: Real[0..1];
    attribute tbaEligibility: Boolean[0..1];
    attribute programText: String[0..1];           // e.g., UMBS, Gold PCs
  }
}

// ================================
// PrivateLabelMBSIssuance.sysml
// (feature-based from PrivateLabelMBSIssuance.rdf)
// ================================
package PrivateLabelMBSIssuance {

  private import ScalarValues::*;
  private import Time::*;
  private import Organizations::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import IssuanceProcess::*;
  private import IssuanceDocuments::*;
  private import MBSIssuance::*;

  item def PrivateLabelPool specializes MBSIssuance::LoanPool {
    doc/* loan pool for non-agency securitizations (private label RMBS/CMBS) */
    attribute sponsorName: String[0..1];
    attribute originatorName: String[0..1];
  }

  item def PrivateLabelMBSOffering specializes MBSIssuance::SecuritizationOffering {
    doc/* private label securitization issuance supported by a PSA */
    attribute pool: PrivateLabelPool[1..1];
    attribute creditEnhancementNote: String[0..*]; // OC, subordination, reserve
    attribute waterfallNote: String[0..*];
  }
}

// ================================
// MuniIssuance.sysml
// (feature-based from MuniIssuance.rdf)
// ================================
package MuniIssuance {

  private import ScalarValues::*;
  private import Time::*;
  private import Parties::*;
  private import Organizations::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import IssuanceProcess::*;
  private import IssuanceDocuments::*;

  enum def MuniDebtType { enum GeneralObligation; enum Revenue; enum SpecialAssessment; enum Lease; enum TaxAnticipation; enum GrantAnticipation; enum Other; }

  item def MunicipalDebtOffering {
    doc/* offering of municipal securities by a public entity to finance projects or operations */
    attribute process: IssuanceProcess::SecuritiesIssuanceProcess[1..1];
    attribute issuer: Organizations::Organization[1..1];       // municipality or authority
    attribute debtType: MuniDebtType[1..1];
    attribute seriesName: String[0..1];
    attribute parAmount: CurrencyAmount::MonetaryAmount[1..1];
    attribute purposeText: String[0..1];
    attribute securityPledgeText: String[0..1];                // GO or revenue pledge
    attribute pos: IssuanceDocuments::PreliminaryOfficialStatement[0..1];
    attribute os: IssuanceDocuments::OfficialStatement[0..1];
    attribute continuingDisclosureText: String[0..1];
  }

  item def MuniCouponSchedule {
    doc/* coupon and maturity schedule specific to municipal bond series */
    attribute firstCouponDate: FinancialDates::ExplicitDate[0..1];
    attribute lastMaturityDate: FinancialDates::ExplicitDate[0..1];
    attribute averageLifeYears: Real[0..1];
  }
}
