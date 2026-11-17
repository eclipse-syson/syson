// ===================================================================
// SecuritiesTransactions.sysml
// Source-parity extracted from: FiBO_FND_TransactionsExt.sysml
// Primary source module: SecuritiesTransactions.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/TransactionsExt
// ===================================================================

package Securities_Transactions {
  doc/* Describes the basic concepts for securities transactions, as an extension of market transactions more generally. Incudes types of securities transactions, parties to the transaction, settlement and the covering contract for the transaction. This ontology would form the basis for more detailed securities transaction concepts that would ideally be derived from the FIX standard. (source: dct:abstract, SecuritiesTransactions.rdf) */
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

  item def FinancialPrimaryMarketTransaction specializes Market_Transactions::MarketTransaction, REA_Transactions::ContractualTransaction {
		doc /* A Transaction in which some negotiable security is provided in exchange for some Consideration. */ 		// skos:definition xml:lang="en" (SecuritiesTransactions.rdf)
		// rdfs:label xml:lang="en" (SecuritiesTransactions.rdf)
  	// RDF: fibo-fnd-txn-sec:FinancialPrimaryMarketTransaction
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-mkt;MarketTransaction"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;ContractualTransaction"/>
  }
  item def FinancialSecuritiesSecondaryMarketTransaction specializes Market_Transactions::MarketTransaction, REA_Transactions::ContractualTransaction {
  	doc /* A Transaction in which some negotiable security is provided in exchange for some Consideration. */
  	// skos:definition xml:lang="en" (SecuritiesTransactions.rdf)
		// rdfs:label xml:lang="en" (SecuritiesTransactions.rdf)
  	// RDF: fibo-fnd-txn-sec:FinancialSecuritiesSecondaryMarketTransaction
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-mkt;MarketTransaction"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;ContractualTransaction"/>
  }
  item def SecuritiesTransactionContract specializes REA_Transactions::EconomicContract {
  	doc /* The contract (written or implied) which governs the transaction of securities in the secondary Market. */
  	// skos:definition xml:lang="en" (SecuritiesTransactions.rdf)
		// rdfs:label xml:lang="en" (SecuritiesTransactions.rdf)
  	// RDF: fibo-fnd-txn-sec:SecuritiesTransactionContract
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-rea;EconomicContract"/>
  }
  item def SecuritiesTransactionCounterparty specializes Market_Transactions::TransactionCounterparty {
		doc /* Trading in securities ahead of them being traded. */ 		// skos:definition xml:lang="en" (SecuritiesTransactions.rdf)
		// rdfs:label xml:lang="en" (SecuritiesTransactions.rdf)
  	// RDF: fibo-fnd-txn-sec:SecuritiesTransactionCounterparty
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-mkt;TransactionCounterparty"/>
  }
  item def SecuritiesTransactionPrincipal specializes Market_Transactions::TransactionPrincipal {
		doc /* Trading in securities ahead of them being traded. */ 		// skos:definition xml:lang="en" (SecuritiesTransactions.rdf)
		// rdfs:label xml:lang="en" (SecuritiesTransactions.rdf)
  	// RDF: fibo-fnd-txn-sec:SecuritiesTransactionPrincipal
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-mkt;TransactionPrincipal"/>
  }
  item def <'settlement process'> SettlementProcess {
    // rdfs:label xml:lang="en" (SecuritiesTransactions.rdf)
    // RDF: fibo-fnd-txn-sec:SettlementProcess
  }
  item def WhenIssuedTransaction specializes FinancialSecuritiesSecondaryMarketTransaction {
  	doc /* Trading in securities ahead of them being traded. */
  	// skos:definition xml:lang="en" (SecuritiesTransactions.rdf)
		// rdfs:label xml:lang="en" (SecuritiesTransactions.rdf)
  	// RDF: fibo-fnd-txn-sec:WhenIssuedTransaction
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-sec;FinancialSecuritiesSecondaryMarketTransaction"/>
  }

}
