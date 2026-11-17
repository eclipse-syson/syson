// ===================================================================
// REATransactions.sysml
// Source-parity extracted from: FiBO_FND_TransactionsExt.sysml
// Primary source module: REATransactions.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/TransactionsExt
// ===================================================================

package REA_Transactions {
  doc/* This is the core REA-derived ontology for transactions. A transaction is defined as an exchange of commitments between parties. Other aspects of REA such as claims and transaction events (commitment lifecycles) are given in separate ontologies in this module. (source: dct:abstract, REATransactions.rdf) */
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

  item def ContractualEconomicAgreement specializes EconomicAgreement, Contracts::Contract {
  	doc /* An economic agreement forming part of a transaction, which has contractual standing as evidenced by a contract between the two parties to the Agreement. */
  	// skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:ContractualEconomicAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;Contract"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;EconomicAgreement"/>
  }
  item def ContractualTransaction specializes REA_Transactions::EconomicTransaction {
  	doc /* An economic transaction which has some contractual basis. */
  	// skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:ContractualTransaction
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;EconomicTransaction"/>
  }
  item def ContractualTransactionParty specializes REA_Transactions::TransactionParty, Contracts::ContractParty {
  	doc /* That which is party to a transaction which has contractual standing. */
  	// skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:ContractualTransactionParty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;TransactionParty"/>
  }
  item def CoveredTransaction specializes REA_Transactions::ContractualTransaction {
  	doc /* A transaction covered by some Master Agreement. */
  	// skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:CoveredTransaction
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;ContractualTransaction"/>
  }
  item def DischargingEvent specializes TransactionBusinessEvent {
    doc /* Some Commitment which forms part of the subject of some Transaction, being an undertaking by one or other of the parties to the transaction, extended to the other party to that same transaction. */     // skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:DischargingEvent
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;TransactionBusinessEvent"/>
  }
  item def EconomicAgreement specializes Contracts::Contract {
    doc /* Some Commitment which forms part of the subject of some Transaction, being an undertaking by one or other of the parties to the transaction, extended to the other party to that same transaction. */     // skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:EconomicAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;Contract"/>
  }
  item def EconomicCommitment specializes Agreements::Commitment {
  	doc /* Some Commitment which forms part of the subject of some Transaction, being an undertaking by one or other of the parties to the transaction, extended to the other party to that same transaction. */
  	// skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:EconomicCommitment
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Commitment"/>
  }
  item def EconomicContract specializes Contracts::Contract {
  	doc /* A contract relating to and governing an economic transaction between two parties. */
  	// skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:EconomicContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;Contract"/>
  }
  item def EconomicContractTermsSet specializes Contracts::ContractualCommitment {
  	doc /* Terms underlying the contract for a transaction. */
  	// skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:EconomicContractTermsSet
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
  }
  item def <'economic resource'> EconomicResource {
    doc /* Anything that can bought sold or exchanged. */
    // skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
    // RDF: fibo-fnd-txn-rea:EconomicResource
  }
  item def <'economic transaction'> EconomicTransaction {
    doc /* Some exchange of some items of economic value between two parties (economic agents). */
    // skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
    // RDF: fibo-fnd-txn-rea:EconomicTransaction
  }
  item def LedgerEntry specializes Financial_Dates::DatedCollectionConstituent, FiBO_FND_ExternalStubs::RegistryEntry {
    doc /* Some imbalance, at a given point in time, between the respective rights and obligations of two parties with respect to one another. */     // skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:LedgerEntry
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedCollectionConstituent"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
  }
  item def <'r e a claim'> REAClaim {
    doc /* Some imbalance, at a given point in time, between the respective rights and obligations of two parties with respect to one another. */
    // skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
    // RDF: fibo-fnd-txn-rea:REAClaim
  }
  item def Revaluation specializes TransactionBusinessEvent {
    doc /* Occurrence in time that partners to a business transaction wish to monitor or control. */     // skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:Revaluation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;TransactionBusinessEvent"/>
  }
  item def <'transaction business event'> TransactionBusinessEvent {
    doc /* Occurrence in time that partners to a business transaction wish to monitor or control. */
    // skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
    // RDF: fibo-fnd-txn-rea:TransactionBusinessEvent
  }
  item def <'transaction event'> TransactionEvent specializes FiBOOccurrences::OccurrenceKind {
  	doc /* The event component of a transaction */
  	// skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:TransactionEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
  }
  item def <'transaction event aspect'> TransactionEventAspect {
    doc /* A transaction side as seen from the perspective of one of the parties to the transaction. */
    // skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
    // RDF: fibo-fnd-txn-rea:TransactionEventAspect
  }
  item def TransactionParty specializes Parties::PartyRole {
  	doc /* Some entity which takes part in some transaction by receiving and/or parting with some item of economic value or some payment or both. */
  	// skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:TransactionParty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
  item def TransactionUndertaking specializes UndertakingEvent {
  	doc /* A contractually defined and established commitment to deliver some goods, perform some service or make some payment in cash or in kind. */
  	// skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:TransactionUndertaking
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;UndertakingEvent"/>
  }
  item def <'undertaking'> Undertaking {
    doc /* Some undertaking to act. */
    // skos:definition xml:lang="en" (REATransactions.rdf)
    // rdfs:label xml:lang="en" (REATransactions.rdf)
    // RDF: fibo-fnd-txn-rea:Undertaking
  }
  item def <'defined in context of'> UndertakingEvent specializes TransactionBusinessEvent, Undertaking {
  	doc /* Something which occurs at a point in time, at which a party makes some commitment to some other party. */
  	// skos:definition xml:lang="en" (REATransactions.rdf)
  	// rdfs:label xml:lang="en" (REATransactions.rdf)
  	// RDF: fibo-fnd-txn-rea:UndertakingEvent
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;TransactionBusinessEvent"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;Undertaking"/>
  }
}
