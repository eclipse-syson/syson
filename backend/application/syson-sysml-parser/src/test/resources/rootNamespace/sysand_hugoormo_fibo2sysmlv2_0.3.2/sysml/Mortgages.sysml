// ===================================================================
// Mortgages.sysml
// Source-parity extracted from: FiBO_LOAN_RealEstateLoans.sysml
// Primary source module: Mortgages.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/RealEstateLoans
// ===================================================================
package Mortgages {
  doc/* This ontology covers high-level concepts related to loans secured by real property. (source: dct:abstract, Mortgages.rdf) */
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

  item def <'closed-end mortgage loan'> ClosedEndMortgageLoan specializes Mortgage, Loans::ClosedEndCredit, Mortgage_Origination::LoanSecuredByRealEstate {
  	doc /* indicates whether or not the loan can be converted into an adjustable-rate mortgage contract (ARM) */
  	// skos:definition xml:lang="en" (Mortgages.rdf)
  	// rdfs:label xml:lang="en" (Mortgages.rdf)
  	// RDF: fibo-loan-reln-mtg:ClosedEndMortgageLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;ClosedEndCredit"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-reln-mtg;LoanSecuredByRealEstate"/>
  }
  item def <'closed-end reverse mortgage'> ClosedEndReverseMortgage specializes ClosedEndMortgageLoan, ReverseMortgageLoan {
  	doc /* reverse mortgage that provides a lump sum payment to the borrower at closing, with no ability for the borrower to receive additional funds under the mortgage at a later date */
  	// skos:definition xml:lang="en" (Mortgages.rdf)
  	// rdfs:label xml:lang="en" (Mortgages.rdf)
  	// RDF: fibo-loan-reln-mtg:ClosedEndReverseMortgage
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-reln-mtg;ClosedEndMortgageLoan"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-reln-mtg;ReverseMortgageLoan"/>
  }
  item def <'mortgage'> Mortgage specializes Mortgage_Origination::LoanSecuredByRealEstate, Debt::SecurityAgreement {
  	doc /* grant of financial interest in real property to a party that is not an owner of that real property and is recorded by a registration authority */
  	// skos:definition xml:lang="en" (Mortgages.rdf)
  	// rdfs:label xml:lang="en" (Mortgages.rdf)
  	// RDF: fibo-loan-reln-mtg:Mortgage
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;SecurityAgreement"/>
  }
  item def <'open-end mortgage loan'> OpenEndMortgageLoan specializes Mortgage, Loans::OpenEndCredit, Mortgage_Origination::LoanSecuredByRealEstate {
  	doc /* loan secured by real estate with a provision that the outstanding loan amount may be increased upon mutual agreement of the lender and the borrower */
  	// skos:definition xml:lang="en" (Mortgages.rdf)
  	// rdfs:label xml:lang="en" (Mortgages.rdf)
  	// RDF: fibo-loan-reln-mtg:OpenEndMortgageLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;OpenEndCredit"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-reln-mtg;LoanSecuredByRealEstate"/>
  }
  item def <'open-end reverse mortgage'> OpenEndReverseMortgage specializes OpenEndMortgageLoan, ReverseMortgageLoan {
  	doc /* reverse mortgage structured like a home equity line of credit in that it provides the borrower with additional funds after closing (either as fixed monthly payments, under a line of credit, or both) */
  	// skos:definition xml:lang="en" (Mortgages.rdf)
  	// rdfs:label xml:lang="en" (Mortgages.rdf)
  	// RDF: fibo-loan-reln-mtg:OpenEndReverseMortgage
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-reln-mtg;OpenEndMortgageLoan"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-reln-mtg;ReverseMortgageLoan"/>
  }
  item def <'reverse mortgage loan'> ReverseMortgageLoan specializes Mortgage, Mortgage_Origination::LoanSecuredByRealEstate {
  	doc /* loan secured by real estate that pays money to the borrower against a set principal limit based on the value of existing equity in the underlying collateral */
  	// skos:definition xml:lang="en" (Mortgages.rdf)
  	// rdfs:label xml:lang="en" (Mortgages.rdf)
  	// RDF: fibo-loan-reln-mtg:ReverseMortgageLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-reln-mtg;LoanSecuredByRealEstate"/>
  }
}
