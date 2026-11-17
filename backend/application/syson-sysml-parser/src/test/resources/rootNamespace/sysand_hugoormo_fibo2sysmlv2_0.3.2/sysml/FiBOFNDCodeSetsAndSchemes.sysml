// ===================================================================
// FiBOFNDCodeSetsAndSchemes.sysml
// Source-parity extracted from: FiBO_FND_CodeSets_And_Schemes.sysml
// Primary source module(s): Arrangements.rdf; IdentifiersAndIndices.rdf; ClassificationSchemes.rdf; Designators.rdf; InternationalRegistriesAndAuthorities.rdf; HomeMortgageDisclosureActCoveredMortgages.rdf (for current translated content)
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/CodeSets_And_Schemes
// ===================================================================

package FiBO_FND_CodeSets_And_Schemes {
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
	 * FiBO Foundations - Code Sets and Identification Schemes
	 * This module contains 6 registered classification items.
	 */
	/**
	 * 
	 */
	item def 'HMDA-Disposition' specializes FiBO_FND_ExternalStubs::Classifier {
			doc /* a type of action taken regarding an application for a HMDA covered loan */
			// skos:definition xml:lang="en" (HomeMortgageDisclosureActCoveredMortgages.rdf)
			// rdfs:label xml:lang="en" (HomeMortgageDisclosureActCoveredMortgages.rdf)
			// RDF: fibo-loan-reln-hmda:HMDA-Disposition
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
		}

	/**
	 * 
	 */
	item def StructuredIdentifier {
			doc /* identifier that is structured according to a specific scheme or pattern */
			// skos:definition xml:lang="en" (IdentifiersAndIndices.rdf)
			// rdfs:label xml:lang="en" (IdentifiersAndIndices.rdf)
			// RDF: fibo-fnd-arr-id:StructuredIdentifier
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
		}

	/**
	 * 
	 */
	item def ContextualName specializes FiBO_FND_ExternalStubs::Name {
			doc /* name that is used in a particular context to identify or refer to something */
			// skos:definition xml:lang="en" (Designators.rdf)
			// rdfs:label xml:lang="en" (Designators.rdf)
			// RDF: cmns-cxtdsg:ContextualName
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dsg;Name"/>
		}

	/**
	 * 
	 */
	item def MarketIdentifierCodeRegistryEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
			doc /* entry in a market identifier code registry that conforms to ISO 10383 */
			// skos:definition xml:lang="en" (InternationalRegistriesAndAuthorities.rdf)
			// rdfs:label xml:lang="en" (InternationalRegistriesAndAuthorities.rdf)
			// RDF: fibo-fbc-fct-ireg:MarketIdentifierCodeRegistryEntry
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-breg;BusinessRegistryEntry"/>
		}

	/**
	 * 
	 */
	item def CodeSet specializes Arrangements::Scheme {
			doc /* scheme for allocating codes to a set of concepts or items */
			// skos:definition xml:lang="en" (ClassificationSchemes.rdf)
			// rdfs:label xml:lang="en" (ClassificationSchemes.rdf)
			// RDF: fibo-fnd-arr-cls:CodeSet
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;ClassificationScheme"/>
		}

	/**
	 * 
	 */
	item def IdentificationScheme specializes Arrangements::Scheme {
			doc /* scheme for allocating identifiers to a set of things */
			// skos:definition xml:lang="en" (IdentifiersAndIndices.rdf)
			// rdfs:label xml:lang="en" (IdentifiersAndIndices.rdf)
			// RDF: fibo-fnd-arr-id:IdentificationScheme
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;IdentificationScheme"/>
		}

}