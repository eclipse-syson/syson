// ===================================================================
// PaymentsandSchedules.sysml
// Source-parity extracted from: FiBO_FND_ProductsAndServices.sysml
// Primary source module: PaymentsAndSchedules.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/ProductsAndServices
// ===================================================================

package Payments_and_Schedules {
  doc/* This ontology defines basic concepts such as payment, payee, payer, and payment schedule, extending the scheduling concepts from the Dates and Times module, among others. (source: dct:abstract, PaymentsAndSchedules.rdf) */
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

item def <'payee'> Payee specializes Agreements::Obligee {
  	doc /* a party to whom a debt should be paid, or to whose order a bill of exchange, note, or check is made payable, or who receives or will receive a payment from a payer in partial or complete fulfillment of an obligation */
  	// skos:definition xml:lang="en" (PaymentsAndSchedules.rdf)
  	// rdfs:label xml:lang="en" (PaymentsAndSchedules.rdf)
  	// RDF: fibo-fnd-pas-psch:Payee
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Obligee"/>
  }
item def <'payer'> Payer specializes Agreements::Obligor {
  	doc /* a party who pays a bill or fees, or who makes payments to a payee in partial or complete fulfillment of an obligation */
  	// skos:definition xml:lang="en" (PaymentsAndSchedules.rdf)
  	// rdfs:label xml:lang="en" (PaymentsAndSchedules.rdf)
  	// RDF: fibo-fnd-pas-psch:Payer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Obligor"/>
  }
item def <'payment'> Payment specializes FiBOOccurrences::FiBOOccurrence {
  	doc /* delivery of money in fulfillment of an obligation, such as to satisfy a claim or debt */
  	// skos:definition xml:lang="en" (PaymentsAndSchedules.rdf)
  	// rdfs:label xml:lang="en" (PaymentsAndSchedules.rdf)
  	// RDF: fibo-fnd-pas-psch:Payment
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
  }
item def <'payment event'> PaymentEvent specializes REA_Transactions::TransactionEvent {
  	doc /* an event that involves delivery of money in fulfillment of an obligation */
  	// skos:definition xml:lang="en" (PaymentsAndSchedules.rdf)
  	// rdfs:label xml:lang="en" (PaymentsAndSchedules.rdf)
  	// RDF: fibo-fnd-pas-psch:PaymentEvent
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;TransactionEvent"/>
  }
item def <'payment obligation'> PaymentObligation specializes Agreements::Commitment, Legal_Capacity::Duty {
  doc /* legally enforceable duty to pay a sum of money according to the terms stated in a contract */
  // skos:definition xml:lang="en" (PaymentsAndSchedules.rdf)
  	// rdfs:label xml:lang="en" (PaymentsAndSchedules.rdf)
  	// RDF: fibo-fnd-pas-psch:PaymentObligation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Commitment"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Duty"/>
  }
item def <'payment schedule'> PaymentSchedule specializes Financial_Dates::Schedule {
  	doc /* schedule for delivery of money in fulfillment of an obligation */
  	// skos:definition xml:lang="en" (PaymentsAndSchedules.rdf)
  	// rdfs:label xml:lang="en" (PaymentsAndSchedules.rdf)
  	// RDF: fibo-fnd-pas-psch:PaymentSchedule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;Schedule"/>
  }

}
