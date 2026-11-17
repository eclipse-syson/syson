// ===================================================================
// MarketTransactions.sysml
// Source-parity extracted from: FiBO_FND_TransactionsExt.sysml
// Primary source module: MarketTransactions.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/TransactionsExt
// ===================================================================

package Market_Transactions {
  doc/* Defines the concepts for market transactions in general, on any kind of marketplace. This ontology is not used directly in FIBO content but provides the conceptual underpinnings for securities market transactions. (source: dct:abstract, MarketTransactions.rdf) */
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

  item def MarketTransaction specializes REA_Transactions::EconomicTransaction {
  	doc /* Any transaction which defines a supply of some negotiable item in return for some Consideration. The Market Transaction has a Principal and a Counterparty, i.e. it is not symmetrical. */
  	// skos:definition xml:lang="en" (MarketTransactions.rdf)
		// rdfs:label xml:lang="en" (MarketTransactions.rdf)
  	// RDF: fibo-fnd-txn-mkt:MarketTransaction
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;EconomicTransaction"/>
  }
  item def <'market transaction invoicing terms'> MarketTransactionInvoicingTerms specializes REA_Transactions::EconomicContractTermsSet {
    // rdfs:label xml:lang="en" (MarketTransactions.rdf)
    // RDF: fibo-fnd-txn-mkt:MarketTransactionInvoicingTerms
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;EconomicContractTermsSet"/>
  }
  item def <'market transaction payment terms'> MarketTransactionPaymentTerms specializes REA_Transactions::EconomicContractTermsSet {
    // rdfs:label xml:lang="en" (MarketTransactions.rdf)
    // RDF: fibo-fnd-txn-mkt:MarketTransactionPaymentTerms
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;EconomicContractTermsSet"/>
  }
  item def <'transaction counterparty'> TransactionCounterparty specializes REA_Transactions::TransactionParty, Contracts::Counterparty{
      // rdfs:label xml:lang="en" (MarketTransactions.rdf)
      // RDF: fibo-fnd-txn-mkt:TransactionCounterparty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;Counterparty"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;TransactionParty"/>
  }
  item def <'transaction principal'> TransactionPrincipal specializes REA_Transactions::TransactionParty, Contracts::ContractPrincipal{
      // rdfs:label xml:lang="en" (MarketTransactions.rdf)
      // RDF: fibo-fnd-txn-mkt:TransactionPrincipal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractPrincipal"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;TransactionParty"/>
  }
}
