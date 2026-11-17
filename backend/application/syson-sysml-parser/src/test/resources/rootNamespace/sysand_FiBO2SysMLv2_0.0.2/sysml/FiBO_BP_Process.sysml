// ===================================================================
// FIBO_BP_Processes.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_BP_Processes {
  doc/* Configuration Item (CI) package for FIBO_BP_Processes and its exported model elements. */
  public import FinancialContextAndProcess::*;
}

// ================================
// FinancialContextAndProcess.sysml
// (feature-based from FinancialContextAndProcess.rdf)
// ================================
package FinancialContextAndProcess {

  // ---- Imports (private) ----
  private import ScalarValues::*;              // String, Integer, Real, Boolean
  private import Time::*;                      // Date, DateTime
  private import Parties::*;                   // Party (actors)
  private import Organizations::*;             // Organization / FormalOrganization
  private import Agreements::*;                // Agreement (governance hooks)
  private import Contracts::*;                 // Contract / terms used by processes
  private import CurrencyAmount::*;            // MonetaryAmount (costs, budgets)
  private import ProductsAndServices::*;       // Product / Service (process outputs)
  private import Documents::*;                 // Document / Text (procedures, SOPs)
  private import FinancialDates::*;            // ExplicitDate, Duration, Schedules
  private import MarketTransactions::*;        // Market/Securities/REA transactions (references)
  private import Analytics::*;                 // Analytics/Annotation (optional linkage)
  private import AnnotationVocabulary::*;      // Tag / Annotation (optional linkage)

  // =========================================================
  // Enums & code lists
  // =========================================================
  enum def ProcessKind {
    doc/* coarse-grained classification of financial processes */
    enum FrontOffice;
    enum MiddleOffice;
    enum BackOffice;
    enum FinanceAndAccounting;
    enum RiskAndCompliance;
    enum LegalAndDocumentation;
    enum ReferenceData;
    enum CorporateActions;
    enum OnboardingKYC;
    enum Reporting;
    enum Other;
  }

  enum def ProcessStatus {
    enum Proposed;
    enum Approved;
    enum Active;
    enum Suspended;
    enum Retired;
  }

  enum def ControlType {
    doc/* type of control embedded in a process */
    enum Preventive;
    enum Detective;
    enum Corrective;
  }

  enum def EvidenceKind {
    enum Document;
    enum Record;
    enum AuditTrail;
    enum SystemLog;
    enum Attestation;
  }

  // =========================================================
  // Core context objects (items) and actors (parts)
  // =========================================================

  part def ProcessOwner {
    doc/* accountable role for process performance, control effectiveness, and approvals */
    attribute party: Parties::Party[1..1];
    attribute organization: Organizations::Organization[0..1];
    attribute title: String[0..1];
  }

  part def ProcessPerformer {
    doc/* role that performs one or more activities within a process */
    attribute party: Parties::Party[1..1];
    attribute organization: Organizations::Organization[0..1];
    attribute qualifications: String[0..*];
  }

  item def BusinessCapability {
    doc/* high-level ability of an organization that a process enables or realizes */
    attribute name: String[1..1];
    attribute description: String[0..1];
  }

  item def SystemApplication {
    doc/* information system or application used by a process activity */
    attribute systemName: String[1..1];
    attribute version: String[0..1];
    attribute vendor: String[0..1];
  }

  // =========================================================
  // Process definitions (items)
  // =========================================================

  item def BusinessProcess {
    doc/* structured set of related activities that collectively produce a business outcome under defined governance and controls */
    attribute name: String[1..1];
    attribute description: String[0..1];
    attribute kind: ProcessKind[0..1];
    attribute status: ProcessStatus[0..1];
    attribute businessObjective: String[0..*];
    attribute capability: BusinessCapability[0..*];
    attribute owner: ProcessOwner[1..*];
    attribute businessStakeholder: Parties::Party[0..*];
    attribute governingAgreement: Agreements::Agreement[0..*];
    attribute governingContract: Contracts::Contract[0..*];
    attribute inputDocument: Document[0..*];
    attribute inputRecord: Document[0..*];
    attribute outputDocument: Document[0..*];
    attribute outputRecord: Document[0..*];
    attribute producesProduct: ProductsAndServices::Product[0..*];
    attribute producesService: ProductsAndServices::Service[0..*];
    attribute supportingSystem: SystemApplication[0..*];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
  }

  item def Procedure {
    doc/* prescriptive set of steps, rules, and responsibilities to perform a process or activity consistently */
    attribute title: String[1..1];
    attribute version: String[0..1];
    attribute text: Text[0..1];
    attribute effectiveDate: Date[0..1];
    attribute supersedes: Procedure[0..1];
    attribute procedureReferences: Document[0..*];
  }

  item def WorkInstruction {
    doc/* detailed task-level instruction to carry out a specific step in a procedure */
    attribute title: String[1..1];
    attribute text: Text[0..1];
    attribute effectiveDate: Date[0..1];
    attribute relatedProcedure: Procedure[0..1];
  }

  item def Activity {
    doc/* atomic unit of work within a process, transforming inputs into outputs according to rules and controls */
    attribute name: String[1..1];
    attribute description: String[0..1];
    attribute performer: ProcessPerformer[0..*];
    attribute inputDocument: Document[0..*];
    attribute outputDocument: Document[0..*];
    attribute inputRecord: Document[0..*];
    attribute outputRecord: Document[0..*];
    attribute systemUsed: SystemApplication[0..*];
    attribute expectedDuration: Duration[0..1];
    attribute actualDuration: Duration[0..1];
    attribute startTime: DateTime[0..1];
    attribute endTime: DateTime[0..1];
    attribute control: Control[0..*];
  }

  item def Control {
    doc/* measure to manage risk within a process/activity, with defined frequency, owner, and evidence */
    attribute name: String[1..1];
    attribute type: ControlType[0..1];
    attribute description: String[0..1];
    attribute frequencyText: String[0..1];     // e.g., daily, T+1, per-transaction
    attribute owner: ProcessOwner[0..1];
    attribute evidence: Evidence[0..*];
    attribute effectiveDate: Date[0..1];
  }

  item def Evidence {
    doc/* artifact demonstrating performance of a control or completion of an activity */
    attribute kind: EvidenceKind[0..1];
    attribute description: String[0..1];
    attribute document: Document[0..1];
    attribute capturedOn: DateTime[0..1];
    attribute retainedUntil: Date[0..1];
  }

  // =========================================================
  // Orchestration: flows, dependencies, schedules
  // =========================================================

  item def ActivityDependency {
    doc/* precedence or dependency relationship between activities (finish-to-start, etc.) */
    attribute predecessor: Activity[1..1];
    attribute successor: Activity[1..1];
    attribute relationText: String[0..1];     // FS, SS, FF, SF (informal label)
    attribute lagDays: Integer[0..1];
  }

  item def ProcessFlow {
    doc/* end-to-end orchestration of activities with dependencies and checkpoints */
    attribute process: BusinessProcess[1..1];
    attribute activity: Activity[1..*];
    attribute processDependency: ActivityDependency[0..*];
    attribute schedule: FinancialDates::RegularSchedule[0..1];
    attribute milestone: ProcessMilestone[0..*];
  }

  item def ProcessMilestone {
    doc/* significant event or checkpoint used to track process progress or compliance */
    attribute name: String[1..1];
    attribute description: String[0..1];
    attribute targetDate: Date[0..1];
    attribute achievedDate: Date[0..1];
  }

  // =========================================================
  // Specializations for common financial processes
  // =========================================================

  item def ClientOnboardingProcess specializes BusinessProcess {
    doc/* process for collecting, validating, and approving client information, including KYC and AML checks */
    attribute kycDocument: Document[0..*];
    attribute sanctionScreeningEvidence: Evidence[0..*];
  }

  item def TradeLifecycleProcess specializes BusinessProcess {
    doc/* front-to-back process for trade capture, enrichment, confirmation, allocation, settlement, and reporting */
    attribute relatedTransaction: MarketTransactions::MarketTransaction[0..*];
    attribute confirmationDocument: Document[0..*];
    attribute settlementInstruction: MarketTransactions::SettlementInstruction[0..*];
  }

  item def CorporateActionsProcessing specializes BusinessProcess {
    doc/* process for ingesting, validating, and applying corporate action events to positions and holdings */
    attribute eventNotice: Document[0..*];
    attribute eventEffectiveDate: Date[0..*];
  }

  item def FinancialReportingProcess specializes BusinessProcess {
    doc/* process that produces periodic financial statements and regulatory reports */
    attribute reportingStandard: String[0..*];   // e.g., IFRS, US GAAP
    attribute reportDocument: Document[0..*];
  }

  item def CollateralManagementProcess specializes BusinessProcess {
    doc/* process to manage margin calls, collateral movements, and disputes under master agreements */
    attribute creditSupportAnnex: DerivativesMasterAgreements::CreditSupportAnnex[0..*];
    attribute marginCallRecord: Document[0..*];
  }

  item def KYCReviewActivity specializes Activity {
    doc/* activity to review and approve KYC artifacts for onboarding or periodic refresh */
    attribute riskRating: String[0..1];
    attribute reviewOutcome: String[0..1];
  }

  item def TradeConfirmationActivity specializes Activity {
    doc/* activity to match, confirm, and issue confirmations for executed trades */
    attribute confirmationSentOn: DateTime[0..1];
    attribute counterpartyAcknowledgedOn: DateTime[0..1];
  }

  item def SettlementActivity specializes Activity {
    doc/* activity executing cash and/or securities settlement */
    attribute settlementDate: Date[0..1];
    attribute settlementResultText: String[0..1];
  }

  // =========================================================
  // Performance & monitoring
  // =========================================================

  item def KPI {
    doc/* key performance indicator associated with a process or activity */
    attribute name: String[1..1];
    attribute definitionText: String[0..1];
    attribute targetValue: Real[0..1];
    attribute unitText: String[0..1];
    attribute measurementWindow: FinancialDates::ExplicitDatePeriod[0..1];
  }

  item def KRI {
    doc/* key risk indicator associated with a process or activity */
    attribute name: String[1..1];
    attribute definitionText: String[0..1];
    attribute thresholdValue: Real[0..1];
    attribute unitText: String[0..1];
    attribute monitoringWindow: FinancialDates::ExplicitDatePeriod[0..1];
  }

  item def ProcessMonitoringProfile {
    doc/* set of KPIs and KRIs tracked for a given process with thresholds and governance */
    attribute process: BusinessProcess[1..1];
    attribute kpi: KPI[0..*];
    attribute kri: KRI[0..*];
    attribute reviewFrequencyText: String[0..1];
    attribute lastReviewedOn: Date[0..1];
  }
}
