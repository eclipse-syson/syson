// ===================================================================
// ConsumerLoans.sysml
// Source-parity extracted from: FiBO_LOAN_LoanSpecific.sysml
// Primary source module: ConsumerLoans.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/LoansSpecific
// ===================================================================
package Consumer_Loans {
  doc/* The consumer loans ontology defines concepts specific to loans that are offered only to consumers rather than to organization, primarily for personal, family, or household purposes. (source: dct:abstract, ConsumerLoans.rdf) */
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

  item def <'consumer loan'> ConsumerLoan specializes Loans::Loan {
  	doc /* loan to one or more individuals for household, family, or other personal expenditures, excluding mortgages and student loans */
  	// skos:definition xml:lang="en" (ConsumerLoans.rdf)
  	// rdfs:label xml:lang="en" (ConsumerLoans.rdf)
  	// RDF: fibo-loan-ln-col:ConsumerLoan
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;Loan"/>
  }
  item def <'home equity line of credit'> HomeEquityLineOfCredit specializes ConsumerLoan, Debt::RevolvingLineOfCredit, SecuredConsumerLoan, Mortgage_Origination::LoanSecuredByRealEstate {
  	doc /* line of credit granted to a homeowner secured by the equity value in a borrower&apos;s home or other property */
  	// skos:definition xml:lang="en" (ConsumerLoans.rdf)
  	// rdfs:label xml:lang="en" (ConsumerLoans.rdf)
  	// RDF: fibo-loan-ln-col:HomeEquityLineOfCredit
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;RevolvingLineOfCredit"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-cns;SecuredConsumerLoan"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-reln-mtg;LoanSecuredByRealEstate"/>
  }
  item def <'motor vehicle loan'> MotorVehicleLoan specializes ConsumerLoan, Loans::CollateralizedLoan, SecuredConsumerLoan {
  	doc /* collateralized, simple-interest loan that is repaid in monthly installments over a period of typically three to five years, for the purpose of purchasing a vehicle */
  	// skos:definition xml:lang="en" (ConsumerLoans.rdf)
  	// rdfs:label xml:lang="en" (ConsumerLoans.rdf)
  	// RDF: fibo-loan-ln-col:MotorVehicleLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;CollateralizedLoan"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-cns;SecuredConsumerLoan"/>
  }
  item def <'secured consumer loan'> SecuredConsumerLoan specializes ConsumerLoan, Loans::SecuredLoan {
  	doc /* loan to one or more individuals for household, family, or other personal expenditures in which the borrower pledges some asset via a security agreement as collateral for the loan, or that is secured via third-party guarantee */
  	// skos:definition xml:lang="en" (ConsumerLoans.rdf)
  	// rdfs:label xml:lang="en" (ConsumerLoans.rdf)
  	// RDF: fibo-loan-ln-col:SecuredConsumerLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;SecuredLoan"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-cns;ConsumerLoan"/>
  }
  item def <'unsecured consumer loan'> UnsecuredConsumerLoan specializes ConsumerLoan, Loans::UnsecuredLoan {
  	doc /* loan to one or more individuals for household, family, or other personal expenditures granted based on the strength of the borrower&apos;s credit history or reputation in the community */
  	// skos:definition xml:lang="en" (ConsumerLoans.rdf)
  	// rdfs:label xml:lang="en" (ConsumerLoans.rdf)
  	// RDF: fibo-loan-ln-col:UnsecuredConsumerLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;UnsecuredLoan"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-cns;ConsumerLoan"/>
  }
}

// --- fibo-loan-ln-edu ---
