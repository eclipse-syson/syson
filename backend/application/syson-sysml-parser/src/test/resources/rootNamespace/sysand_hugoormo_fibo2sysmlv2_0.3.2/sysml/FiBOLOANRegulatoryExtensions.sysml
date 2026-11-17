// ===================================================================
// FiBOLOANRegulatoryExtensions.sysml
// Source-parity extracted from: FiBO_LOAN_Regulatory_Extensions.sysml
// Primary source module(s): HomeMortgageDisclosureActCoveredMortgages.rdf (for current translated content)
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/RegulatoryExtensions
// ===================================================================
package FiBO_LOAN_Regulatory_Extensions {
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

 doc /*
	 * FiBO Loans - Regulatory Classifications and Extensions
	 * This module contains 3 registered classification items.
	 */
	/**
	 * 
	 */
	item def 'HMDA-CoveredLoanContract' specializes Mortgage_Origination::LoanSecuredByRealEstate {
			doc /* loan secured by real estate that is subject to reporting under the Home Mortgage Disclosure Act (HMDA) */
			// skos:definition xml:lang="en" (FiBOLOANRegulatoryExtensions.rdf)
			// rdfs:label xml:lang="en" (FiBOLOANRegulatoryExtensions.rdf)
			// RDF: fibo-loan-ln-hmda:HMDA-CoveredLoanContract
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-reln-re;LoanSecuredByRealEstate"/>
		}

	/**
	 * 
	 */
	item def 'HMDA-PreApprovalRequest' specializes Loan_Applications::PreApprovalRequest {
			doc /* pre-approval request that is subject to the requirements of the Home Mortgage Disclosure Act (HMDA) */
			// skos:definition xml:lang="en" (FiBOLOANRegulatoryExtensions.rdf)
			// rdfs:label xml:lang="en" (FiBOLOANRegulatoryExtensions.rdf)
			// RDF: fibo-loan-ln-hmda:HMDA-PreApprovalRequest
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-app;PreApprovalRequest"/>
		}

	/**
	 */
	item def 'HMDA-Report' specializes Reporting::Report {
			doc /* report required to be submitted under the Home Mortgage Disclosure Act (HMDA) disclosing data on mortgage loans */
			// skos:definition xml:lang="en" (FiBOLOANRegulatoryExtensions.rdf)
			// rdfs:label xml:lang="en" (FiBOLOANRegulatoryExtensions.rdf)
			// RDF: fibo-loan-ln-hmda:HMDA-Report
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rep;Report"/>
		}

}