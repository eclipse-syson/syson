// ===================================================================
// CommercialLoans.sysml
// Source-parity extracted from: FiBO_LOAN_LoanSpecific.sysml
// Primary source module: CommercialLoans.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/LoansSpecific
// ===================================================================
package Commercial_Loans {
  doc/* Commercial loans are loans where the loan purpose is some commercial purpose. Note that these are distinguished by the loan purpose not by the borrower type - borrowers may be corporate or personal, though in the majority of cases they would also be corporate loans that is loans with a corporate borrower. (source: dct:abstract, CommercialLoans.rdf) */
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

  item def <'commercial loan'> CommercialLoan specializes Loans::Loan {
  	doc /* loan extended to a corporation, commercial enterprise, joint venture, or other organization as opposed to a consumer */
  	// skos:definition xml:lang="en" (CommercialLoans.rdf)
  	// rdfs:label xml:lang="en" (ACTUSTaxonomy.rdf)
  	// RDF: fibo-loan-ln-cl:CommercialLoan
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;Loan"/>
  }
}

// --- fibo-loan-ln-col ---
