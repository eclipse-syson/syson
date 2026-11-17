// ===================================================================
// FiBOACTUSContractTerms.sysml
// Source-parity extracted from: FiBO_ACTUS_ContractTerms.sysml
// Primary source module: FiBOACTUSContractTerms.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO ACTUS ACTUS/ContractTerms
// ===================================================================
package FiBO_ACTUS_ContractTerms {
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
	 * FiBO ACTUS Contract Terms - OWL source: fibo/fnd/arrangements/documents/Contracts
	 * 
	 * This module defines ACTUS (Algorithmic Contract Types Unified Standards) contract
	 * term concepts. ACTUS provides a standardized specification for financial contracts
	 * and their data model for structured contract data and analytics.
	 * 
	 * The classes in this package represent enumerated types and code elements used in
	 * ACTUS contract definitions, including business day conventions, calendar codes,
	 * and contract term specifications.
	 */

	/**
	 * Code element representing ACTUS business day convention
	 * 
	 * OWL source documentation:
	 * Specifies how dates are calculated in the ACTUS contract framework when dates
	 * fall on non-business days (weekends or holidays).
	 */
	item def ACTUSBusinessDayConvention specializes FiBO_FND_ExternalStubs::CodeElement {
			doc /* code element for one of a set of conventions that ACTUS supports for handling dates that fall on non-business days */
			// skos:definition xml:lang="en" (FiBOACTUSContractTerms.rdf)
			// rdfs:label xml:lang="en" (FiBOACTUSContractTerms.rdf)
			// RDF: fibo-actus-act-ct:ACTUSBusinessDayConvention
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
		}

	/**
	 * Code element representing ACTUS calendar code
	 * 
	 * OWL source documentation:
	 * Enumeration of calendar codes used in ACTUS contracts to define holidays
	 * and business day adjustments for specific jurisdictions or markets.
	 */
	item def ACTUSCalendarCode specializes FiBO_FND_ExternalStubs::CodeElement {
			doc /* code for a calendar that applies to a particular contract, defining holidays and business day adjustments for specific jurisdictions or markets */
			// skos:definition xml:lang="en" (FiBOACTUSContractTerms.rdf)
			// rdfs:label xml:lang="en" (FiBOACTUSContractTerms.rdf)
			// RDF: fibo-actus-act-ct:ACTUSCalendarCode
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
		}

	/**
	 * Code element representing ACTUS contract term specification
	 * 
	 * OWL source documentation:
	 * Encodes standardized contract terms according to the ACTUS specification,
	 * enabling systematic representation and processing of contract attributes.
	 */
	item def ACTUSContractTerm specializes FiBO_FND_ExternalStubs::CodeElement {
			doc /* code denoting a term describing an aspect of one or more ACTUS contract type(s), enabling systematic representation and processing of contract attributes */
			// skos:definition xml:lang="en" (FiBOACTUSContractTerms.rdf)
			// rdfs:label xml:lang="en" (FiBOACTUSContractTerms.rdf)
			// RDF: fibo-actus-act-ct:ACTUSContractTerm
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
		}

}