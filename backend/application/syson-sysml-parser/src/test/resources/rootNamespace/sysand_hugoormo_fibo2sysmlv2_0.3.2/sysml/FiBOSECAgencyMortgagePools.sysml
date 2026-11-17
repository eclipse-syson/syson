// ===================================================================
// FiBOSECAgencyMortgagePools.sysml
// Source-parity extracted from: FiBO_SEC_AgencyMortgagePools.sysml
// Primary source module(s): FiBOSECAgencyMortgagePools.rdf (for current translated content)
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/AgencyMortgagePools
// ===================================================================
package FiBO_SEC_AgencyMortgagePools {
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
	 * FiBO Securities - Agency Mortgage Pools
	 *
	 * OWL source: fibo/fbc/sec_eu/ei
	 * This module contains 6 registered classification items.
	 */
	/**
	 * 
	 */
	item def 'FHLMC-GoldPool' specializes Mortgage_backed_Securities::AgencyMortgagePool {
			doc /* agency mortgage pool created and guaranteed by the Federal Home Loan Mortgage Corporation (Freddie Mac) under the Gold program */
			// skos:definition xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// rdfs:label xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// RDF: fibo-sec-dbt-mbs:FHLMC-GoldPool
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;AgencyMortgagePool"/>
		}

	/**
	 * 
	 */
	item def 'FHLMC-Pool' specializes Mortgage_backed_Securities::AgencyMortgagePool {
			doc /* agency mortgage pool created and guaranteed by the Federal Home Loan Mortgage Corporation (Freddie Mac) */
			// skos:definition xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// rdfs:label xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// RDF: fibo-sec-dbt-mbs:FHLMC-Pool
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;AgencyMortgagePool"/>
		}

	/**
	 * 
	 */
	item def 'FNMA-Pool' specializes Mortgage_backed_Securities::AgencyMortgagePool {
			doc /* agency mortgage pool created and guaranteed by the Federal National Mortgage Association (Fannie Mae) */
			// skos:definition xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// rdfs:label xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// RDF: fibo-sec-dbt-mbs:FNMA-Pool
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;AgencyMortgagePool"/>
		}

	/**
	 * 
	 */
	item def 'GNMA-IIPool' specializes Mortgage_backed_Securities::AgencyMortgagePool {
			doc /* agency mortgage pool created and guaranteed by the Government National Mortgage Association (Ginnie Mae) under the GNMA II program */
			// skos:definition xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// rdfs:label xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// RDF: fibo-sec-dbt-mbs:GNMA-IIPool
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;AgencyMortgagePool"/>
		}

	/**
	 * 
	 */
	item def 'GNMA-PlatinumPool' specializes Mortgage_backed_Securities::AgencyMortgagePool {
			doc /* agency mortgage pool created and guaranteed by the Government National Mortgage Association (Ginnie Mae) under the Platinum program */
			// skos:definition xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// rdfs:label xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// RDF: fibo-sec-dbt-mbs:GNMA-PlatinumPool
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;AgencyMortgagePool"/>
		}

	/**
	 * 
	 */
	item def 'GNMA-iPool' specializes Mortgage_backed_Securities::AgencyMortgagePool {
			doc /* agency mortgage pool created and guaranteed by the Government National Mortgage Association (Ginnie Mae) under the GNMA I program */
			// skos:definition xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// rdfs:label xml:lang="en" (FiBOSECAgencyMortgagePools.rdf)
			// RDF: fibo-sec-dbt-mbs:GNMA-iPool
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;AgencyMortgagePool"/>
		}

}