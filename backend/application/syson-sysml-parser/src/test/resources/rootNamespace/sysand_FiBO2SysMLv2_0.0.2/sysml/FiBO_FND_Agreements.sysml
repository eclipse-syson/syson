// ======================================================
// FIBO_FND_AgreementsAndContracts.sysml
// Configuration Item (CI) Aggregator  â€” public re-export
// ======================================================
package FIBO_FND_AgreementsAndContracts {
  doc/* Configuration Item (CI) package for FIBO_FND_AgreementsAndContracts and its exported model elements. */
  public import Agreements::*;
  public import Contracts::*;
}

// ======================
// Agreements.sysml  (feature-based from Agreements.rdf)
// ======================
package Agreements {

  // ---- Imports (private) ----
  private import ScalarValues::*;   // String, Boolean
  private import Time::*;           // Date
  private import Parties::*;     // Party

  // ===== Classes =====

  item def SubjectRef {
    doc/* generic contextual reference to whatever an agreement or contract element is about */
    attribute referenceText: String[0..1];
    attribute contextNote: String[0..1];
  }

  item def Agreement {
    doc/* negotiated understanding between two or more parties reflecting offer and acceptance of commitments */
    attribute party: Party[2..*];                      // participating parties
    attribute beneficiary: Beneficiary[0..*];
    attribute commitment: Commitment[1..*];
    attribute effectiveDate: Date[0..1];
    attribute expirationDate: Date[0..1];
    attribute governingLaw: String[0..1];
    attribute contractualElement: Contracts::ContractualElement[0..*];  // cross-ref
    attribute milestoneProvision: Contracts::ContractMilestone[0..*];   // hasMilestoneProvision
    attribute nonBindingTerm: Contracts::NonBindingTerm[0..*];          // hasNonBindingTerm
  }

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

  // Roles (as in the ontology theyâ€™re PartyRole-like classifiers)
  item def Obligor {
    doc/* party that is bound legally or by agreement to repay, make a payment, do something, or refrain from doing something */
    attribute party: Party[1..1];
    attribute hasObligation: Commitment[0..*];   // hasObligation / isObligationOf
  }

  item def Obligee {
    doc/* party to whom an obligation is owed, either legally or per the terms of an agreement */
    attribute party: Party[1..1];
  }
}

// ======================
// Contracts.sysml  (feature-based from Contracts.rdf)
// ======================
package Contracts {

  // ---- Imports (private) ----
  private import ScalarValues::*;   // String, Boolean
  private import Time::*;           // Date, DateTime
  private import FinancialDates::*; // Duration, ExplicitDate, ExplicitDatePeriod
  private import Agreements::*;   // to resolve Agreement if not already
  private import Documents::*;    // Text
  private import People::*;       // reuse IdentityDocument types
  private import Parties::*;     // Party
  private import LegalCorePackage::*;     // Law
  private import JurisdictionPackage::*; // Jurisdiction

  // ===== Core contract model =====

  item def Contract specializes Agreement {
    doc/* legally binding agreement between parties that defines rights, obligations, and benefits */
    // Parties and roles
    attribute contractParty: ContractParty[0..*];     // hasContractParty
    attribute counterparty: Counterparty[0..*];       // hasCounterparty
    attribute principalParty: ContractPrincipal[0..1];// hasPrincipalParty
    attribute thirdParty: ContractThirdParty[0..*];   // hasThirdParty
    // Temporal & governance
    attribute effectiveDateTimeStamp: DateTime[0..1]; // hasEffectiveDateTimeStamp
    attribute executionDate: Date[0..1];              // hasExecutionDate
    attribute executionDateTimeStamp: DateTime[0..1]; // hasExecutionDateTimeStamp
    attribute earlyTerminationDate: ExplicitDate[0..1]; // hasEarlyTerminationDate
    attribute contractDuration: Duration[0..1];       // hasContractDuration
    attribute term: Duration[0..1];                   // hasTerm
    attribute governingJurisdiction: Jurisdiction[0..1]; // hasGoverningJurisdiction
    // Documentation & semantics
    attribute contractDocument: ContractDocument[0..*];  // isEvidencedBy / isEvidenceFor
    attribute definesTermsFor: SubjectRef[0..*];           // definesTermsFor
    attribute legalDescription: String[0..1];             // hasLegalDescription
  }

  item def WrittenContract specializes Contract {
    doc/* formal contract that is written and signed by the parties thereto */
    attribute primaryContractFor: WrittenContract[0..*];  // isPrimaryContractFor
    attribute subordinateTo: WrittenContract[0..*];       // isSubordinateTo
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
    attribute isAssignable: Boolean[0..1];      // isAssignable
    attribute assignor: Party[0..1];
    attribute assignee: Party[0..1];
    attribute assignmentRestrictions: Text[0..*];
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
    attribute text: Text[0..*];
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
    attribute definitionText: Text[0..*];
  }

  item def ExtensionProvision specializes ContractualElement {
    doc/* provision specifying conditions under which the term of a contract may be extended */
    attribute extendablePeriod: ExplicitDatePeriod[0..*]; // hasExtendablePeriod
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
    attribute isNettingAutomatic: Boolean[0..1];      // isNettingAutomatic
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
    attribute evidences: Contract[0..*];     // isEvidenceFor
  }

  item def TermSheet {
    doc/* nonbinding agreement setting forth the basic terms and conditions under which a proposed business deal will be made */
  }

  item def BreachOfContract {
    doc/* classifier of events representing violation of an express or implied term of a contract */
    attribute breachedContract: Contract[1..1];
    attribute description: Text[0..*];
    attribute breachDate: Date[0..1];
  }

  item def BreachOfCovenant specializes BreachOfContract {
    doc/* classifier of events representing breaking a promise specified in a contract to do or not to do something */
  }

  item def Amendment specializes ContractualElement {
    doc/* change made to the terms of a contract */
    attribute amends: Contract[1..1];
    attribute reason: Text[0..*];
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
    attribute description: Text[0..*];
    attribute date: Date[0..1];
  }
}

