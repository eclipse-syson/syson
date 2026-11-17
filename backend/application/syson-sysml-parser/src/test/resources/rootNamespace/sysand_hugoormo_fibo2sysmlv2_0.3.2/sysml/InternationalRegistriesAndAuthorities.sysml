// ===================================================================
// FiBOBEOtherEntities.sysml
// Source-parity extracted from: FiBO_BE_OtherEntities.sysml
// Primary source modules: InternationalRegistriesAndAuthorities.rdf; InternationalRegistriesAndAuthorities.rdf (for current translated content)
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/OtherEntities
// ===================================================================
package International_Registries_and_Authorities{
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
	 * FiBO Business Entities - Other Financial Entities
	 *
	 * OWL source: fibo/fbc/functionalentities/internationalregistriesandauthorities
	 * Structural note: retained in BE for translation compatibility during phased domain reconciliation.
	 * This module contains 2 registered classification items.
	 */
	/**
	 * 
	 */
	item def <'business identifier code data record'> BusinessIdentifierCodeDataRecord specializes Business_Registries::BusinessRegistryEntry {
			doc /* entry in a registry that conforms to ISO 9362 for the management of BIC codes and related registration information */
			// skos:definition xml:lang="en" (InternationalRegistriesAndAuthorities.rdf)
			// rdfs:label xml:lang="en" (InternationalRegistriesAndAuthorities.rdf)
			// RDF: fibo-fbc-fct-ireg:BusinessIdentifierCodeDataRecord
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-breg;BusinessRegistryEntry"/>
		}

	/**
	 * 
	 */
	item def <'domestic entity other'> DomesticEntityOther specializes US_National_Bank_Institutions::DomesticEntity {
			doc /* domestic institution that engages in banking activities usually in connection with the business of banking in the United States */
			// skos:definition xml:lang="en" (InternationalRegistriesAndAuthorities.rdf)
			// rdfs:label xml:lang="en" (InternationalRegistriesAndAuthorities.rdf)
			// RDF: fibo-fbc-fct-usfse:DomesticEntityOther
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;DomesticEntity"/>
		}

}
