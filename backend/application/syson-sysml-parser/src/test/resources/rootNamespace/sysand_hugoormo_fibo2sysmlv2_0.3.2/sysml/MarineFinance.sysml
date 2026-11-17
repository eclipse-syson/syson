// ===================================================================
// MarineFinance.sysml
// Source-parity extracted from: FiBO_LOAN_LoanSpecific.sysml
// Primary source module: MarineFinance.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/LoansSpecific
// ===================================================================
package Marine_Finance {
  doc/* This ontology defines concepts specific to marine finance, which involves financing vessel acquisitions for the spot market, time charters or bareboat charters, as well as the construction of work boats, and to finance the acquisition of vessels for scrapping. (source: dct:abstract, MarineFinance.rdf) */
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

  item def <'marine finance'> MarineFinancing specializes Loans::Loan {
  	doc /* A loan for the purchase of a boat or other vessel. */
  	// skos:definition xml:lang="en" (MarineFinance.rdf)
  	// rdfs:label xml:lang="en" (MarineFinance.rdf)
  	// RDF: fibo-loan-ln-mar:MarineFinancing
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;Loan"/>
  }
}
