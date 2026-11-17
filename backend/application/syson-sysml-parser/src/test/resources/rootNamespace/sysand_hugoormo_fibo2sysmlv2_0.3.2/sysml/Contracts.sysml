// ===================================================================
// Contracts.sysml
// Source-parity extracted from: FiBO_FND_Agreements.sysml
// Primary source module: Contracts.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Agreements
// ===================================================================

package Contracts {
  doc/* This ontology defines the concept of contract and roles that parties to contract play in the context of financial agreements. Coverage includes written contracts which are the concrete evidence of agreements between parties and verbal contracts. Contracts are further broken down into bilateral and transferable contracts, the latter being the basis for most financial instruments, and basic properties of contracts, such as terms and conditions, are also covered. (source: dct:abstract, Contracts.rdf) */
  comment license /*
  * Copyright (c) 2013-2025 EDM Council, Inc.
  * Copyright (c) 2013-2025 Object Management Group, Inc.
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the 'Software'), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  * THE SOFTWARE.
  *
  * See https://opensource.org/licenses/MIT. */

    item def <'assignable contract'> AssignableContract specializes Contracts::TransferableContract {
    	doc /* contract in which contract holder (assignor) may transfer some or all of their rights and obligations to another party (assignee) */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:AssignableContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;TransferableContract"/>
    }

    item def <'bilateral contract'> BilateralContract specializes Agreements::BilateralAgreement, Contracts::WrittenContract {
    	doc /* contract where two parties commit to perform specific actions or obligations towards each other */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:BilateralContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;BilateralAgreement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    }

    item def <'bilateral netting provision'> BilateralNettingProvision specializes NettingProvision {
    	doc /* netting provision that occurs between two parties, in which mutual obligations are offset to determine a single net payment */
    	// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:BilateralNettingProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;NettingProvision"/>
    }

    item def <'breach of contract'> BreachOfContract specializes Financial_Products_and_Services::ContractLifecycleEvent, FiBOOccurrences::OccurrenceKind {
    	doc /* classifier of events representing a violation of an express, or implied, condition of a contract to do or not to do something, without a legitimate excuse */
    	// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:BreachOfContract
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ContractLifecycleEvent"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
    }

    item def <'breach of covenant'> BreachOfCovenant specializes Financial_Products_and_Services::ContractLifecycleEvent, FiBOOccurrences::OccurrenceKind {
    	doc /* classifier of events representing breaking a promise specified in a contract to do or not to do something, without a legitimate excuse */
    	// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:BreachOfCovenant
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ContractLifecycleEvent"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
    }

    item def <'close-out netting provision'> CloseOutNettingProvision specializes NettingProvision {
    	doc /* netting provision that may be triggered when a counterparty defaults, leading to the termination of all outstanding transactions between the parties */
    	// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:CloseOutNettingProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;NettingProvision"/>
    }

    item def <'collateral agreement'> CollateralAgreement specializes Contracts::WrittenContract {
    	doc /* classifier for an event (milestone), or set of events related to the status or level of completion of a designated activity, task, or segment of work required for contract fulfillment */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:CollateralAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    }

    item def <'condition precedent'> ConditionPrecedent specializes Contracts::ContractualElement {
		doc /* stipulation that specifies conditions that must be met before some aspect of a contract takes effect */
		// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:ConditionPrecedent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualElement"/>
    }

    item def <'contract'> Contract specializes Agreements::Agreement {
		doc /* voluntary, deliberate agreement between competent parties to which the parties agree to be legally bound, and for which the parties provide valuable consideration */
		// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:Contract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Agreement"/>
    }

    item def <'contract document'> ContractDocument specializes FiBO_FND_ExternalStubs::LegalDocument {
    	doc /* legal document that records the formal terms and conditions of some contract */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:ContractDocument
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;LegalDocument"/>
    }

    item def <'contract milestone'> ContractMilestone specializes Contracts::ConditionPrecedent, FiBOOccurrences::OccurrenceKind {
    	doc /* classifier for an event (milestone), or set of events related to the status or level of completion of a designated activity, task, or segment of work required for contract fulfillment */
    	// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:ContractMilestone
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ConditionPrecedent"/>
    }

    item def <'contract party'> ContractParty specializes Parties::PartyRole {
    	doc /* netting provision that, when triggered, is facilitated by a central clearinghouse or financial institution */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:ContractParty
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
    }

    item def <'contract principal'> ContractPrincipal specializes Contracts::ContractParty {
    	doc /* party that originates a contract and is identified as the first party to that contract, in the event that the contract distinguishes any party as such */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:ContractPrincipal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
    }

    item def <'contract third party'> ContractThirdParty specializes Parties::PartyRole {
		doc /* party that is indirectly involved in, but not a counterparty to, an agreement */
		// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:ContractThirdParty
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
    }

    item def <'contractual commitment'> ContractualCommitment specializes Contracts::ContractualElement {
    	doc /* provision specifying something that the contracting parties agree to, i.e., a promise or pledge made by one of the parties to perform some action or fulfill some duty */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:ContractualCommitment
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualElement"/>
    }

    item def <'contractual definition'> ContractualDefinition specializes Contracts::ContractualElement {
    	doc /* contractual element that specifies the meaning of a term in a legal document, whose definition is substitutable for the term whenever it occurs in the body of that document */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:ContractualDefinition
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualElement"/>
    }

    item def <'contractual element'> ContractualElement specializes FiBO_FND_ExternalStubs::Constituent {
    	doc /* element, such as an arrangement, provision, requirement, rule, specification, and standard that forms an integral part of an agreement */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:ContractualElement
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Constituent"/>
    }

    item def <'counterparty'> Counterparty specializes Contracts::ContractParty {
    	doc /* party to a contract with whom one negotiates on a given agreement */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:Counterparty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
    }

    item def <'disclosure provision'> DisclosureProvision specializes Contracts::ContractualCommitment {
    	doc /* contractual provision that outlines the requirements and responsibilities of one or both parties to reveal certain information to each other */
    	// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:DisclosureProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    }

    item def <'early termination provision'> EarlyTerminationProvision specializes TerminationProvision {
    	doc /* termination of an agreement for any reason prior to its expiration date */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:EarlyTerminationProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;TerminationProvision"/>
    }

    item def <'extension provision'> ExtensionProvision specializes Contracts::ContractualCommitment {
    	doc /* contract terms that specify the conditions under which a contract can be extended */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:ExtensionProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    }

    item def <'master agreement'> MasterAgreement specializes Contracts::WrittenContract {
    	doc /* contract between named parties that outlines the terms and conditions designed to apply to a number of accounts, transactions, or other activities between the parties, and that consolidates and provides overarching terms for separate but related agreements */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:MasterAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    }

    item def <'milestone event'> MilestoneEvent specializes FiBOOccurrences::FiBOOccurrence {
    	doc /* event related to the status or level of completion of a designated activity, task, or segment of work required for contract fulfillment */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:MilestoneEvent
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
    }

    item def <'milestone schedule'> MilestoneSchedule specializes Financial_Dates::Schedule {
    	doc /* schedule of milestone events, observations, or other FiBOOccurrences and the associated dates and/or times when they will be done */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:MilestoneSchedule
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;Schedule"/>
    }

    item def <'multilateral contract'> MultilateralContract specializes Agreements::MultilateralAgreement, Contracts::WrittenContract {
    	doc /* contract where multiple parties (i.e., more than two) commit to perform specific actions or obligations towards each other */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:MultilateralContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;MultilateralAgreement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    }

    item def <'multilateral netting provision'> MultilateralNettingProvision specializes NettingProvision {
    	doc /* netting provision that, when triggered, is facilitated by a central clearinghouse or financial institution */
    	// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:MultilateralNettingProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;NettingProvision"/>
    }

    item def <'mutual contractual agreement'> MutualContractualAgreement {
		doc /* contractual element that specifies the method of reducing credit, settlement and other risks by aggregating (combining) two or more obligations to achieve a reduced net obligation */ 		// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
      // RDF: fibo-fnd-agr-ctr:MutualContractualAgreement
    }

    item def <'netting provision'> NettingProvision specializes Contracts::ContractualCommitment {
    	doc /* contractual element that specifies the method of reducing credit, settlement and other risks by aggregating (combining) two or more obligations to achieve a reduced net obligation */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:NettingProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    }

    item def <'non-binding term'> NonBindingTerm specializes Contracts::ContractualElement {
    	doc /* contractual element that specifies what should be done with respect to netting when a given contract is replaced with another */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:NonBindingTerm
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualElement"/>
    }

    item def <'non-transferable contract'> NonTransferableContract specializes Contracts::WrittenContract {
    	doc /* contract between whose individual rights and obligations are not transferable to another party without prior written permission */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:NonTransferableContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    }

    item def <'notification provision'> NotificationProvision specializes Contracts::ContractualCommitment, Financial_Instruments::RedemptionProvision {
    	doc /* provision indicating that notification is required prior to the FiBOOccurrence of a relevant event */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:NotificationProvision
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;RedemptionProvision"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    }

    item def <'novateable contract'> NovateableContract specializes Contracts::TransferableContract {
    	doc /* contract that may be replaced by another contract, and in that event, extinguishes the rights and obligations in effect under the original contract with those in the new agreement */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:NovateableContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;TransferableContract"/>
    }

    item def <'novation netting provision'> NovationNettingProvision specializes NettingProvision {
    	doc /* contractual element that specifies what should be done with respect to netting when a given contract is replaced with another */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:NovationNettingProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;NettingProvision"/>
    }

    item def <'payment netting provision'> PaymentNettingProvision specializes NettingProvision {
    	doc /* netting provision that specifies the way in which all payments in a single currency owed between parties will be aggregated for each traded currency per value date and within the larger aggregate obligation */
    	// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:PaymentNettingProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;NettingProvision"/>
    }

    item def <'representation'> Representation specializes Contracts::ContractualCommitment {
    	doc /* contractual element that is a statement made by a party to the contract, before or at the time of making the contract, in regard to some fact, circumstance, or state of affairs pertinent to the contract, which the counterparty(ies) rely on, or is influential in bringing about the contract */
    	// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:Representation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    }

    item def <'settlement netting provision'> SettlementNettingProvision specializes NettingProvision {
    	doc /* netting provision that specifies the way in which on a given payment date, each party will aggregate the amounts of a currency to be delivered by it, and only the difference in the aggregate amounts will be delivered by the party with the larger aggregate obligation */
    	// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:SettlementNettingProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;NettingProvision"/>
    }

    item def <'term sheet'> TermSheet specializes Agreements::Agreement {
    	doc /* contractual element that specifies the circumstances under which the parties can dissolve their legal relationship and discontinue the fulfillment of their obligations under the contract */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:TermSheet
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Agreement"/>
    }

    item def <'termination provision'> TerminationProvision specializes Contracts::ContractualCommitment {
    	doc /* contractual element that specifies the circumstances under which the parties can dissolve their legal relationship and discontinue the fulfillment of their obligations under the contract */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:TerminationProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    }

    item def <'transferable contract'> TransferableContract specializes Contracts::WrittenContract {
    	doc /* contractual element that is a statement of fact */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:TransferableContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    }

    item def <'unilateral contract'> UnilateralContract specializes Contracts::WrittenContract {
    	doc /* contract in which one party makes an offer that can only be accepted through performance rather than a return promise */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:UnilateralContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    }

    item def <'use of proceeds provision'> UseOfProceedsProvision specializes Contracts::ContractualCommitment {
    	doc /* contract provision specifying how funds obtained through financing, such as through a credit agreement, offering, warrant, or other instrument are intended to be used */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:UseOfProceedsProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    }

    item def <'verbal contract'> VerbalContract specializes Contract {
    	doc /* contract that exists as a result of some verbal exchange */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:VerbalContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;Contract"/>
    }

    item def <'warranty'> Warranty specializes Contracts::ContractualCommitment {
    	doc /* contractual element that is a statement of fact */
    	// skos:definition xml:lang="en" (Contracts.rdf)
		// rdfs:label xml:lang="en" (Contracts.rdf)
    	// RDF: fibo-fnd-agr-ctr:Warranty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    }

    item def <'ACTUS cashflow family'> WrittenContract specializes Contract {
    	doc /* indicates a termination date that occurs prior to an explicit expiration date */
    	// skos:definition xml:lang="en" (Contracts.rdf)
    	// rdfs:label xml:lang="en" (ACTUSTaxonomy.rdf)
    	// RDF: fibo-fnd-agr-ctr:WrittenContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;Contract"/>
    }

}
