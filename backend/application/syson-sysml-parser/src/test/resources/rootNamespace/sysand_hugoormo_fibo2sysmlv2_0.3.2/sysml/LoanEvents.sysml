// ===================================================================
// LoanEvents.sysml
// Source-parity extracted from: FiBO_LOAN_LoansGeneral.sysml
// Primary source module: LoanEvents.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/LoansGeneral
// ===================================================================
package Loan_Events {
  doc/* This ontology defines a wide range of events relating to loans. These include legal proceedings, prepayments, and so forth. (source: dct:abstract, LoanEvents.rdf) */
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

  item def <'collateral valuation'> CollateralValuation specializes Assessments::AssessmentActivity {
  	doc /* assessment activity resulting in the valuation of real property as collateral */
  	// skos:definition xml:lang="en" (LoanEvents.rdf)
  	// rdfs:label xml:lang="en" (LoanEvents.rdf)
  	// RDF: fibo-loan-ln-ev:CollateralValuation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AssessmentActivity"/>
  }
  item def <'court judgment'> CourtJudgment specializes LegalProceeding {
  	doc /* decision by a court or other tribunal that resolves a controversy and determines the rights and obligations of the parties */
  	// skos:definition xml:lang="en" (LoanEvents.rdf)
  	// rdfs:label xml:lang="en" (LoanEvents.rdf)
  	// RDF: fibo-loan-ln-ev:CourtJudgment
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ev;LegalProceeding"/>
  }
  item def <'legal proceeding'> LegalProceeding specializes FiBOOccurrences::FiBOOccurrence {
  	doc /* legal step or action taken at the direction of, or by the authority of, a court or agency */
  	// skos:definition xml:lang="en" (LoanEvents.rdf)
  	// rdfs:label xml:lang="en" (LoanEvents.rdf)
  	// RDF: fibo-loan-ln-ev:LegalProceeding
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
  }
  item def <'loan default proceeding'> LoanDefaultProceeding specializes LegalProceeding {
  	doc /* [no definition] Further Review This is typically part of mortgagte servicing. THere would typically be a whole department dealing with this. Dealing with default, helping borrowers make payment Collections Default admin Foreclosure Reselling All dealt with by several sub departments. This requires subject matter experts in this area. 1. scoping Identify default as a possible state. This hands off to other business processes. Once you get into the default scenario we are talking about a proces that is going to fall into place over a period of time. The bank works out what to do with the default scenario, e.g. whether it restructures, forecloses, seeks restitution from the security (collateral). It does nto help us to understand the structure of the loan, rather tha consequences of the loan. If we were to further explore the default detail we would bring in other SMEs. And we would have to model a process flow. 1.1 impact on the pool of an MBS Loan Default Proceeding (special ase of legal thing) is an aspect of Default Management / Administratoin. there is also the State of the Loan. Sale / something / fulfilment / fiunduing / approved = servicing mode. Something happens (non payment) =&amp;gt; Default Grace Period followed by negotiation. Some threshold whereby after a given amount of delinquency it needs to go into some other process moving towards foreclosure. Do State Diagram. Stages of loan. */
  	// skos:definition xml:lang="en" (LoanEvents.rdf)
  	// rdfs:label xml:lang="en" (LoanEvents.rdf)
  	// RDF: fibo-loan-ln-ev:LoanDefaultProceeding
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ev;LegalProceeding"/>
  }
  item def <'loan paid in full'> LoanPaidInFull specializes LoanPhase {
  	doc /* amount before the application of sale proceeds and recoveries */
  	// skos:definition xml:lang="en" (LoanEvents.rdf)
  	// rdfs:label xml:lang="en" (LoanEvents.rdf)
  	// RDF: fibo-loan-ln-ev:LoanPaidInFull
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ev;LoanPhase"/>
  }
  item def <'loan phase'> LoanPhase specializes Lifecycles::LifecycleStage {
  	doc /* amount before the application of sale proceeds and recoveries */
  	// skos:definition xml:lang="en" (LoanEvents.rdf)
  	// rdfs:label xml:lang="en" (LoanEvents.rdf)
  	// RDF: fibo-loan-ln-ev:LoanPhase
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStage"/>
  }
  item def <'prepayment'> Prepayment specializes Payments_and_Schedules::Payment {
  	doc /* amount before the application of sale proceeds and recoveries */
  	// skos:definition xml:lang="en" (LoanEvents.rdf)
  	// rdfs:label xml:lang="en" (LoanEvents.rdf)
  	// RDF: fibo-loan-ln-ev:Prepayment
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;Payment"/>
  }
  item def <'repayment phase'> RepaymentPhase specializes LoanPhase {
  	doc /* amount before the application of sale proceeds and recoveries */
  	// skos:definition xml:lang="en" (LoanEvents.rdf)
  	// rdfs:label xml:lang="en" (LoanEvents.rdf)
  	// RDF: fibo-loan-ln-ev:RepaymentPhase
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ev;LoanPhase"/>
  }
}

// --- fibo-loan-ln-ln ---
