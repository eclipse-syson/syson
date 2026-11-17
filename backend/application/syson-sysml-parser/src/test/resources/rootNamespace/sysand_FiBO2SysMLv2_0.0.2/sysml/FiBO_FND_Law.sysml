// ===================================================================
// FIBO_FND_Law_and_AgreementsAndContracts.sysml
// Single merged CI for Law (with subpackages) + patched Agreements & Contracts
// ===================================================================

// -------------------------------------------------------------------
// CI Aggregator (Law) â€” public re-export of Law subpackages
// -------------------------------------------------------------------
package FIBO_FND_Law {
  doc/* Configuration Item (CI) package for FIBO_FND_Law and its exported model elements. */
  public import JurisdictionPackage::*;
  public import LegalCapacityPackage::*;
  public import LegalCorePackage::*;
  public import LegalAgreementsPackage::*;
  public import LegalContractsPackage::*;
}

// ======================
// Jurisdiction.sysml
// ======================
package JurisdictionPackage {

  private import ScalarValues::*;   // String, Integer, Boolean
  private import Time::*;           // Date
  private import Addresses::*;      // Place
  private import Organizations::*;  // Organization
  private import People::*;         // Person
  private import Parties::*;        // Party

  // --- Core concepts ---

  item def Jurisdiction {
    doc/* authority of a legal body to administer justice within a defined field of responsibility and geographic area */
    attribute name: String[0..1];
    attribute territory: Locations::Country[0..*];
    attribute competentCourt: Court[0..*];
    attribute governingBody: GovernmentalOrganization[0..*];
    attribute legalSystem: LegalCorePackage::LegalSystem[0..1];
  }

  item def SubjectMatterJurisdiction specializes Jurisdiction {
    doc/* jurisdiction limited to a particular subject matter or class of cases */
  }

  item def PersonalJurisdiction specializes Jurisdiction {
    doc/* jurisdiction based on a court's power over the persons involved in the litigation */
  }

  item def TerritorialJurisdiction specializes Jurisdiction {
    doc/* jurisdiction derived from geographic boundaries */
  }

  item def AppellateJurisdiction specializes Jurisdiction {
    doc/* jurisdiction to review decisions of lower courts */
  }

  // Organizations and courts

  item def GovernmentalOrganization specializes Organizations::Organization {
    doc/* organization that exercises governmental authority within a jurisdiction */
  }

  enum def CourtLevel {
    doc/* level within a court hierarchy */
    enum Supreme;
    enum Appellate;
    enum Trial;
    enum Specialized;
  }

  item def Court specializes Organizations::Organization {
    doc/* tribunal with authority to adjudicate legal disputes and administer justice */
    attribute level: CourtLevel[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  // Helpful link role
  item def CompetentAuthority {
    doc/* public authority that has the legal power to perform a designated function within a jurisdiction */
    attribute authority: Organizations::Organization[1..1];
    attribute within: Jurisdiction[0..1];
  }
}

// ======================
// LegalCapacity.sysml
// ======================
package LegalCapacityPackage {

  private import ScalarValues::*;   // String, Boolean
  private import Time::*;           // Date
  private import JurisdictionPackage::*;   // reuse Jurisdiction types

  // --- Core concepts ---

  item def LegalStatus {
    doc/* standing or classification of a person or organization recognized by law */
    attribute description: String[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  item def LegalCapacity {
    doc/* ability of a natural or legal person to hold rights and assume obligations enforceable at law */
    attribute hasFullCapacity: Boolean[0..1];
    attribute capacityNote: String[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  item def CapacityToContract specializes LegalCapacity {
    doc/* legal capacity required to create binding contractual obligations */
    attribute mayEnterCommitment: Boolean[0..1];
  }

  item def CapacityToLitigate specializes LegalCapacity {
    doc/* legal capacity to sue and be sued in a court of law */
  }

  item def LegalPerson {
    doc/* person (natural or legal) recognized as having rights and obligations under the law */
    attribute legalStatus: LegalStatus[0..*];
    attribute inJurisdiction: Jurisdiction[0..*];
  }

  item def NaturalLegalPerson specializes LegalPerson {
    doc/* natural person recognized as a subject of law */
  }

  item def ArtificialLegalPerson specializes LegalPerson {
    doc/* organization or entity recognized by law as having legal personality */
    attribute registeredName: String[0..1];
    attribute registrationId: String[0..1];
  }

  item def Competence {
    doc/* capacity, authority, or fitness to do something as required by law or regulation */
    attribute description: String[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  item def Authority {
    doc/* legal power delegated or granted to act, decide, or command */
    attribute grantedBy: Jurisdiction[0..1];
    attribute scopeNote: String[0..1];
  }
}

// ======================
// LegalCore.sysml  (extended)
// ======================
package LegalCorePackage {

  private import ScalarValues::*;   // String, Boolean
  private import Time::*;           // Date, DateTime
  private import JurisdictionPackage::*;   // Court, Jurisdiction
  private import Organizations::*;  // Organization
  private import Parties::*;        // Party
  private import Documents::*;    // Document, Text

  // --- Enums ---

  enum def LegalSystemKind {
    doc/* broad family of legal systems used to categorize jurisdictions */
    enum CommonLaw;
    enum CivilLaw;
    enum ReligiousLaw;
    enum CustomaryLaw;
    enum MixedSystem;
  }

  enum def CaseDisposition {
    doc/* outcome classification for a legal case */
    enum Pending;
    enum Dismissed;
    enum Settled;
    enum Decided;
    enum Appealed;
    enum Vacated;
    enum Remanded;
  }

  enum def RemedyKind {
    doc/* general category of legal remedy */
    enum Damages;
    enum Injunction;
    enum SpecificPerformance;
    enum Rescission;
    enum Restitution;
    enum DeclaratoryRelief;
    enum Penalty;
  }

  // --- Core legal constructs ---

  item def LegalSystem {
    doc/* system of principles and rules recognized and enforced by a jurisdiction */
    attribute kind: LegalSystemKind[0..1];
    attribute description: String[0..1];
  }

  item def Law {
    doc/* rule or set of rules recognized and enforced by a controlling authority */
    attribute title: String[0..1];
    attribute citation: String[0..1];
    attribute issuedBy: Organizations::Organization[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute text: Text[0..*];
  }

  item def Enactment specializes Law {
    doc/* law that has been formally made valid and put into effect */
    attribute enactmentDate: Date[0..1];
  }

  item def Statute specializes Enactment {
    doc/* written law passed by a legislative body */
  }

  item def Regulation specializes Law {
    doc/* rule or directive made and maintained by an authority under powers given by primary legislation */
  }

  item def RuleOfLaw {
    doc/* principle that all persons and institutions are subject to and accountable to law that is fairly applied and enforced */
  }

  item def LegalRule {
    doc/* specific normative statement that prescribes or proscribes behavior within a legal system */
    attribute text: Text[0..*];
    attribute enabledBy: Law[0..1];
  }

  item def SubjectRef {
    doc/* generic contextual reference to whatever a legal statement, commitment, or term is about */
    attribute referenceText: String[0..1];
    attribute contextNote: String[0..1];
  }

  item def LegalObligation {
    doc/* duty imposed by law or contract requiring a party to act or refrain from acting in a certain way */
    attribute sourceLaw: Law[0..1];
    attribute description: String[0..1];
  }

  item def LegalRight {
    doc/* entitlement recognized and protected by a legal system */
    attribute sourceLaw: Law[0..1];
    attribute description: String[0..1];
  }

  // --- Instruments and evidence ---

  item def LegalInstrument {
    doc/* formal legal document that records and defines rights, duties, or liabilities */
    attribute instrumentDocument: Document[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  item def Writ specializes LegalInstrument {
    doc/* written order issued by a body with administrative or judicial jurisdiction */
  }

  item def Citation specializes LegalInstrument {
    doc/* reference to a source of legal authority (e.g., statute, case, regulation) */
  }

  item def LegalOpinion specializes LegalInstrument {
    doc/* formal statement of advice by a qualified legal professional regarding a matter of law */
  }

  // --- Proceedings, cases, and decisions (extended) ---

  item def LegalProceeding {
    doc/* process in which legal rights and obligations are adjudicated */
    attribute court: Court[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute commencedOn: Date[0..1];
    attribute concludedOn: Date[0..1];
  }

  item def LegalCase specializes LegalProceeding {
    doc/* proceeding between parties in a court of law */
    attribute caseNumber: String[0..1];
    attribute plaintiff: Parties::Party[0..*];
    attribute defendant: Parties::Party[0..*];
    attribute disposition: CaseDisposition[0..1];
    attribute decision: JudicialDecision[0..1];
  }

  item def JudicialDecision {
    doc/* final ruling, judgment, or order rendered by a court in a legal matter */
    attribute decidedOn: Date[0..1];
    attribute decidedBy: Court[0..1];
    attribute cites: Law[0..*];
    attribute precedentFor: LegalCase[0..*];
    attribute text: Text[0..*];
  }

  item def Precedent {
    doc/* legal principle or rule established in a previous case that is persuasive or binding for a court */
    attribute establishedBy: JudicialDecision[1..1];
    attribute appliedIn: LegalCase[0..*];
  }

  item def CauseOfAction {
    doc/* facts that give rise to one or more legal claims */
    attribute description: String[0..1];
    attribute groundedIn: Law[0..*];
  }

  item def LegalRemedy {
    doc/* means by which a court enforces a right, imposes a penalty, or makes another court order to impose its will */
    attribute kind: RemedyKind[0..1];
    attribute amountOrTerms: String[0..1];
  }

  item def Sanction {
    doc/* penalty or coercive measure imposed to enforce obedience to a law or rule */
    attribute description: String[0..1];
    attribute imposedBy: Court[0..1];
  }

  // --- Applicability & relationships ---

  item def ApplicableLaw {
    doc/* law that applies to a specific situation, relationship, or dispute */
    attribute law: Law[1..*];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute note: String[0..1];
  }

  item def GoverningLaw specializes ApplicableLaw {
    doc/* law chosen by the parties or prescribed by conflict-of-laws rules to govern their relationship */
  }

  item def ChoiceOfLawClause specializes LegalInstrument {
    doc/* contractual clause specifying the governing law for interpretation of the agreement */
    attribute governingLaw: GoverningLaw[0..1];
  }
}

// ===================================================================
// Patched Agreements & Contracts (wired to Law/Jurisdiction)
// ===================================================================

// ======================
// Agreements.sysml  (patched wiring)
// ======================
package LegalAgreementsPackage {

  private import ScalarValues::*;   // String, Boolean
  private import Time::*;           // Date
  private import LegalCorePackage::*;      // Law, GoverningLaw, ChoiceOfLawClause
  private import JurisdictionPackage::*;   // Jurisdiction
  private import Agreements::*;   // base Agreement
  private import Parties::*;        // Party


  item def BilateralAgreement specializes Agreement {
    doc/* agreement where two parties commit to perform specific actions or obligations towards each other */
  }

  item def MultilateralAgreement specializes Agreement {
    doc/* agreement involving multiple parties */
  }

  item def MutualAgreement specializes Agreement {
    doc/* agreement reflecting mutual promises by the parties */
  }

  item def Beneficiary {
    doc/* party that receives some benefit or advantage or profits from something */
    attribute party: Party[1..1];
    attribute isBeneficiaryOf: Agreement[0..*];
  }

  item def Commitment {
    doc/* promise made by some party to act or refrain from acting in some manner */
    attribute obligor: Obligor[1..*];
    attribute obligee: Obligee[0..*];
    attribute commitmentSubject: SubjectRef[0..*];
    attribute dueDate: Date[0..1];
    attribute description: String[0..1];
  }

  item def UnilateralCommitment specializes Commitment {
    doc/* commitment made by one party without requiring a reciprocal promise from another party */
  }

  item def CommitmentAtLarge specializes Commitment {
    doc/* commitment made by some party without direct involvement from potential beneficiaries */
  }

  item def MutualCommitment specializes Commitment {
    doc/* commitment that is mutual among two or more parties */
  }

  item def IndividualUnilateralCommitment specializes Commitment {
    doc/* unilateral promise or obligation by a single party */
  }

  item def Obligor {
    doc/* party that is bound legally or by agreement to repay, make a payment, do something, or refrain from doing something */
    attribute party: Party[1..1];
    attribute hasObligation: Commitment[0..*];
  }

  item def Obligee {
    doc/* party to whom an obligation is owed, either legally or per the terms of an agreement */
    attribute party: Party[1..1];
  }
}

// ======================
// Contracts.sysml  (patched wiring)
// ======================
package LegalContractsPackage {

  private import ScalarValues::*;     // String, Boolean
  private import Time::*;             // Date, DateTime
  private import Agreements::*;       // Contract specializes Agreement
  private import LegalCorePackage::*;        // Law, GoverningLaw, ChoiceOfLawClause
  private import JurisdictionPackage::*;     // Jurisdiction
  private import FinancialDates::*;   // Duration, ExplicitDate
  private import Parties::*;          // Party
  private import Contracts::*;        // base Contract

  // ===== Core contract model =====

  item def LegalContract specializes Agreements::Agreement {
    doc/* legally binding agreement between parties that defines rights, obligations, and benefits */
    // Parties and roles
    attribute contractParty: ContractParty[0..*];
    attribute counterparty: Counterparty[0..*];
    attribute principalParty: ContractPrincipal[0..1];
    attribute thirdParty: ContractThirdParty[0..*];

    // Temporal & governance
    attribute effectiveDateTimeStamp: DateTime[0..1];
    attribute executionDate: Date[0..1];
    attribute executionDateTimeStamp: DateTime[0..1];
    attribute earlyTerminationDate: ExplicitDate[0..1];
    attribute contractDuration: Duration[0..1];
    attribute term: Duration[0..1];

    // Documentation & semantics
    attribute contractDocument: ContractDocument[0..*];
    attribute definesTermsFor: SubjectRef[0..*];
    attribute legalDescription: String[0..1];
  }

  item def WrittenContract specializes Contract {
    doc/* formal contract that is written and signed by the parties thereto */
    attribute primaryContractFor: WrittenContract[0..*];
    attribute subordinateTo: WrittenContract[0..*];
  }

  item def VerbalContract specializes Contract {
    doc/* contract that exists as a result of some verbal exchange */
  }

  item def BilateralContract specializes Contract {
    doc/* contract where two parties commit to perform specific actions or obligations towards each other */
  }

  item def UnilateralContract specializes Contract {
    doc/* contract in which one party makes an offer that can only be accepted through performance rather than a promise to perform */
  }

  item def AssignableContract specializes Contract {
    doc/* contract in which contract holder (assignor) may transfer some or all rights and obligations to another party (assignee) */
    attribute isAssignable: Boolean[0..1];
    attribute assignor: Party[0..1];
    attribute assignee: Party[0..1];
    attribute assignmentRestrictions: String[0..*];
  }

  item def TransferableContract specializes Contract {
    doc/* contract in which rights and obligations of one party may be transferred to another party */
  }

  item def NovateableContract specializes Contract {
    doc/* contract that may be replaced by another contract, extinguishing rights and obligations of the original */
  }

  item def CollateralAgreement specializes WrittenContract {
    doc/* written contract related to or accompanying a primary contract to provide clarity and additional protection for one or more parties */
  }

  // ===== Parties and party roles =====

  item def ContractParty {
    doc/* legally competent party that has entered into a binding agreement, accepting and conceding obligations and benefits thereunder */
    attribute party: Party[1..1];
  }

  item def Counterparty specializes ContractParty { }

  item def ContractPrincipal specializes ContractParty {
    doc/* party that originates a contract and is identified as the first party to that contract */
  }

  item def ContractThirdParty specializes ContractParty {
    doc/* party other than a counterparty or principal that is referenced in the contract */
  }

  // ===== Contractual content / elements =====

  item def ContractualElement {
    doc/* arrangement, requirement, rule, representation, condition, definition, or standard forming part of an agreement */
    attribute text: String[0..*];
  }

  item def NonBindingTerm specializes ContractualElement {
    doc/* term that is not intended to be legally enforceable */
  }

  item def ContractualCommitment specializes ContractualElement {
    doc/* provision specifying something that the contracting parties agree to do or refrain from doing */
    attribute obligor: Party[0..*];
    attribute obligee: Party[0..*];
    attribute commitmentSubject: SubjectRef[0..*];
    attribute dueDate: Date[0..1];
  }

  item def Representation specializes ContractualElement {
    doc/* contractual element that is a statement of fact made by a party before or at the time of making the contract */
  }

  item def Warranty specializes ContractualElement {
    doc/* contractual element that is a statement of fact or assurance regarding subject matter in the contract */
  }

  item def ConditionPrecedent specializes ContractualElement {
    doc/* stipulation that specifies the conditions that must be met before some aspect of a contract takes effect */
  }

  item def ContractualDefinition specializes ContractualElement {
    doc/* element that specifies the meaning of a term in a contract and serves as the definitive meaning for that term in the document */
    attribute definedTerm: String[1..*];
    attribute definitionText: String[0..*];
  }

  item def ExtensionProvision specializes ContractualElement {
    doc/* provision specifying conditions under which the term of a contract may be extended */
    attribute extendablePeriod: ExplicitDatePeriod[0..*];
  }

  item def NotificationProvision specializes ContractualElement {
    doc/* provision indicating that notification is required prior to the occurrence of a relevant event */
  }

  item def UseOfProceedsProvision specializes ContractualElement {
    doc/* contract provision addressing the ways in which funds obtained through financing are to be used */
  }

  // ===== Netting (and variants) =====

  item def NettingProvision specializes ContractualElement {
    doc/* provision enabling setoff or offsetting of mutual obligations to determine a net amount due */
    attribute isNettingAutomatic: Boolean[0..1];
  }

  item def BilateralNettingProvision specializes NettingProvision {
    doc/* netting provision between two parties, in which mutual obligations are offset to determine a single net payment */
  }

  item def MultilateralNettingProvision specializes NettingProvision {
    doc/* netting provision among more than two parties */
  }

  item def SettlementNettingProvision specializes NettingProvision {
    doc/* provision specifying that on a given payment date, each party will aggregate amounts owed to arrive at a net obligation */
  }

  item def PaymentNettingProvision specializes NettingProvision {
    doc/* provision specifying the way in which all payments in a single currency owed between parties are to be aggregated and netted */
  }

  item def CloseOutNettingProvision specializes NettingProvision {
    doc/* provision triggered when a counterparty defaults, leading to the termination of outstanding transactions and calculation of a net settlement amount */
  }

  item def NovationNettingProvision specializes NettingProvision {
    doc/* contractual provision addressing what should be done with respect to netting when a given contract is novated */
  }

  // ===== Lifecycle / evidence / milestones =====

  item def ContractDocument {
    doc/* legal document that records the formal terms and conditions of some contract */
    attribute evidences: Contract[0..*];
  }

  item def TermSheet {
    doc/* nonbinding agreement setting forth the basic terms and conditions under which a proposed business deal will be made */
  }

  item def BreachOfContract {
    doc/* classifier of events representing violation of an express or implied term of a contract */
    attribute breachedContract: Contract[1..1];
    attribute description: String[0..*];
    attribute breachDate: Date[0..1];
  }

  item def BreachOfCovenant specializes BreachOfContract {
    doc/* classifier of events representing breaking a promise specified in a contract to do or not to do something */
  }

  item def Amendment specializes ContractualElement {
    doc/* change made to the terms of a contract */
    attribute amends: Contract[1..1];
    attribute reason: String[0..*];
    attribute effectiveDate: Date[0..1];
  }

  item def Assignment {
    doc/* transfer of contractual rights and/or obligations from one party (assignor) to another (assignee) */
    attribute assignor: Party[1..1];
    attribute assignee: Party[1..1];
    attribute assignedContract: Contract[1..1];
    attribute assignmentDate: Date[0..1];
  }

  item def Novation {
    doc/* substitution of a new contract or party replacing an original party, with consent of all parties */
    attribute originalContract: Contract[1..1];
    attribute newContract: Contract[0..1];
    attribute outgoingParty: Party[0..1];
    attribute incomingParty: Party[0..1];
    attribute novationDate: Date[0..1];
  }

  item def ContractMilestone {
    doc/* classifier for an event or set of events related to the status or level of completion of an obligation under a contract */
    attribute linkedContract: Contract[0..*];
    attribute description: String[0..*];
    attribute date: Date[0..1];
  }
}
