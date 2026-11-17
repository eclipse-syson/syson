// ===================================================================
// ConstructionLoans.sysml
// Source-parity extracted from: FiBO_LOAN_RealEstateLoans.sysml
// Primary source module: ConstructionLoans.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/RealEstateLoans
// ===================================================================
package Construction_Loans {
  doc/* Construction loans are credit facilities with separate draw-downs (which may be specified through sub-facilities for large projects) allowed given evidence of completion, or substantial completion, of agreed milestones of the construction project. (source: dct:abstract, ConstructionLoans.rdf) */
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

  item def <'construction loan'> ConstructionLoan specializes Mortgage_Origination::LoanSecuredByRealEstate, Debt::CreditFacility, Contracts::MasterAgreement, Loans::CollateralizedLoan {
  	doc /* loan covering construction and development costs, secured by a mortgage on the property financed */
  	// skos:definition xml:lang="en" (ConstructionLoans.rdf)
  	// rdfs:label xml:lang="en" (ConstructionLoans.rdf)
  	// RDF: fibo-loan-reln-cnst:ConstructionLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditFacility"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;MasterAgreement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;CollateralizedLoan"/>
  }
}

// --- fibo-loan-reln-hmda ---
