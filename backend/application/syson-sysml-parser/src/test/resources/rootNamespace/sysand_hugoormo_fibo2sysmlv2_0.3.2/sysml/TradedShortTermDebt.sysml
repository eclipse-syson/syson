// ===================================================================
// TradedShortTermDebt.sysml
// Source-parity extracted from: FiBO_SEC_Debt.sysml
// Primary source module: TradedShortTermDebt.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Debt
// ===================================================================
package Traded_Short_Term_Debt {
  doc/* This ontology defines a number of basic, traded short-term debt instruments, many of which are considered money market instruments that may be freely traded. (source: dct:abstract, TradedShortTermDebt.rdf) */
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

// TradableDebtInstrument
// PartyRole

  item def <'money market instrument'> MoneyMarketInstrument specializes Debt_Instruments::FixedIncomeSecurity {
  	doc /* a short-term debt security that gives the owner the unconditional right to receive a stated, fixed sum of money on a specified date */
  	// skos:definition xml:lang="en" (TradedShortTermDebt.rdf)
  	// rdfs:label xml:lang="en" (TradedShortTermDebt.rdf)
  	// RDF: fibo-sec-dbt-tstd:MoneyMarketInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;FixedIncomeSecurity"/>
  }
  item def <'bankers&apos; acceptance'> BankersAcceptance specializes BillOfExchange {
  	doc /* short-term debt instrument that is guaranteed and paid by a bank and used as a relatively safe form of payment for large transactions */
  	// skos:definition xml:lang="en" (TradedShortTermDebt.rdf)
  	// rdfs:label xml:lang="en" (TradedShortTermDebt.rdf)
  	// RDF: fibo-sec-dbt-tstd:BankersAcceptance
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-tstd;BillOfExchange"/>
  }
  item def <'bill of exchange'> BillOfExchange specializes MoneyMarketInstrument {
  	doc /* short-term negotiable financial instrument consisting of an order in writing addressed by one person (the seller of goods) to another (the buyer), requiring the latter to pay a fixed amount of money on demand (a sight draft) or on a predetermined date (a time draft) */
  	// skos:definition xml:lang="en" (TradedShortTermDebt.rdf)
  	// rdfs:label xml:lang="en" (TradedShortTermDebt.rdf)
  	// RDF: fibo-sec-dbt-tstd:BillOfExchange
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-tstd;MoneyMarketInstrument"/>
  }
  item def <'commercial paper'> CommercialPaper specializes MoneyMarketInstrument {
  	doc /* an unsecured short-term debt instrument typically issued by a bank, corporation, or foreign government to obtain funds to meet short-term debt obligations, such as accounts receivable, inventories, or payroll, backed only by an issuing bank or company promise to pay the face amount on the maturity date specified on the note */
  	// skos:definition xml:lang="en" (TradedShortTermDebt.rdf)
  	// rdfs:label xml:lang="en" (TradedShortTermDebt.rdf)
  	// RDF: fibo-sec-dbt-tstd:CommercialPaper
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-tstd;MoneyMarketInstrument"/>
  }
  item def <'eurodollar deposit'> EurodollarDeposit specializes Clients_and_Accounts::CertificateOfDeposit {
  	doc /* a certificate of deposit with a fixed interest rate issued in U.S. dollars outside the jurisdiction of the Federal Reserve, held at banks outside of the United States, including branches of U.S. banks located outside of the U.S. */
  	// skos:definition xml:lang="en" (TradedShortTermDebt.rdf)
  	// rdfs:label xml:lang="en" (TradedShortTermDebt.rdf)
  	// RDF: fibo-sec-dbt-tstd:EurodollarDeposit
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;CertificateOfDeposit"/>
  }
  item def <'repurchase agreement'> RepurchaseAgreement specializes MoneyMarketInstrument {
  	doc /* agreement between two parties whereby one party lends the other a security at a specified price with a commitment to take the security back at a later date for another specified price */
  	// skos:definition xml:lang="en" (TradedShortTermDebt.rdf)
  	// rdfs:label xml:lang="en" (TradedShortTermDebt.rdf)
  	// RDF: fibo-sec-dbt-tstd:RepurchaseAgreement
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-tstd;MoneyMarketInstrument"/>
  }
  item def <'drawee'> Drawee specializes Payments_and_Schedules::Payer {
  	doc /* a certificate of deposit with a fixed interest rate issued in U.S. dollars outside the jurisdiction of the Federal Reserve, held at banks outside of the United States, including branches of U.S. banks located outside of the U.S. */
  	// skos:definition xml:lang="en" (TradedShortTermDebt.rdf)
  	// rdfs:label xml:lang="en" (TradedShortTermDebt.rdf)
  	// RDF: fibo-sec-dbt-tstd:Drawee
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;Payer"/>
  }
  item def <'drawer'> Drawer specializes Agreements::Obligee {
  	doc /* party that requires a drawee to pay either a third party or themselves with respect to a bill of exchange */
  	// skos:definition xml:lang="en" (TradedShortTermDebt.rdf)
  	// rdfs:label xml:lang="en" (TradedShortTermDebt.rdf)
  	// RDF: fibo-sec-dbt-tstd:Drawer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Obligee"/>
  }

}
