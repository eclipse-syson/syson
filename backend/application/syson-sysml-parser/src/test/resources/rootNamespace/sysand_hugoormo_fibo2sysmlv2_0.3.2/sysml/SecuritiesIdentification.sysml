// ===================================================================
// SecuritiesIdentification.sysml
// Source-parity extracted from: FiBO_SEC_Securities.sysml
// Primary source module: SecuritiesIdentification.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Securities
// ===================================================================
package Securities_Identification {
  doc/* This ontology defines concepts required to identify securities, including a number of well-known securities identifiers and related schemes, registries, and registration authorities. (source: dct:abstract, SecuritiesIdentification.rdf) */
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

  item def <'security identification scheme'> SecurityIdentificationScheme specializes FinancialInstrumentIdentificationScheme, FiBO_FND_ExternalStubs::RegistrationScheme {
    	doc /* formal definition of the structure and application of a particular set of securities identifiers */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:SecurityIdentificationScheme
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistrationScheme"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;FinancialInstrumentIdentificationScheme"/>
    }

  item def <'security identifier'> SecurityIdentifier specializes Financial_Instruments::FinancialInstrumentIdentifier {
    	doc /* any publicly available identifier that is used to identify a security */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:SecurityIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrumentIdentifier"/>
    }

  item def <'financial instrument identification scheme'> FinancialInstrumentIdentificationScheme specializes FiBO_FND_CodeSets_And_Schemes::IdentificationScheme {
    	doc /* formal definition of the structure and application of a particular set of financial instrument identifiers */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:FinancialInstrumentIdentificationScheme
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;IdentificationScheme"/>
    }

  item def <'international securities identification number'> InternationalSecuritiesIdentificationNumber specializes SecurityIdentifier, FiBO_FND_CodeSets_And_Schemes::StructuredIdentifier {
    	doc /* security identifier that is defined as specified in ISO 6166, Securities and related financial instruments -- International securities identification numbering system (ISIN) */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:InternationalSecuritiesIdentificationNumber
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtid;StructuredIdentifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;SecurityIdentifier"/>
    }

  item def <'international securities identification numbering scheme'> InternationalSecuritiesIdentificationNumberingScheme specializes SecurityIdentificationScheme {
    	doc /* formal definition of the structure and application of a ISINs as defined in ISO 6166 */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:InternationalSecuritiesIdentificationNumberingScheme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;SecurityIdentificationScheme"/>
    }

  item def <'listed security identifier'> ListedSecurityIdentifier specializes SecurityIdentifier {
    	doc /* security identifier issued in the public domain and referred to in listings and other relevant publications */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:ListedSecurityIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;SecurityIdentifier"/>
    }

  item def <'listing'> Listing specializes SecurityRegistryEntry {
    	doc /* indicates whether the security conforms to the Employee Retirement Income Security Act (ERISA) of 1974, a federal outline for regulating employee benefit plans, including healthcare plans sponsored and/or insured by an employer */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:Listing
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;SecurityRegistryEntry"/>
    }

  item def <'national numbering agency'> NationalNumberingAgency specializes FiBO_FND_ExternalStubs::RegistrationAuthority {
    	doc /* registration authority responsible for issuing and managing National Securities Identifying Numbers for securities in accordance with the ISO 6166 standard in some jurisdiction (typically that of a country) */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:NationalNumberingAgency
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistrationAuthority"/>
    }

  item def <'National Securities Identifying Number'> NationalSecuritiesIdentifyingNumber specializes SecurityIdentifier {
    	doc /* generic, nine-digit alpha numeric code which identifies a fungible security, assigned by a national numbering agency under the ISO 6166 standard */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:NationalSecuritiesIdentifyingNumber
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;SecurityIdentifier"/>
    }

  item def <'National Securities Identifying Number registry'> NationalSecuritiesIdentifyingNumberRegistry specializes SecurityRegistry {
    	doc /* registry used by a national numbering agency to manage the financial instrument identifiers and related information that it registers */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:NationalSecuritiesIdentifyingNumberRegistry
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;SecurityRegistry"/>
    }

  item def <'national numbering agency registry entry'> NationalSecuritiesIdentifyingNumberRegistryEntry specializes SecurityRegistryEntry {
    	doc /* entry in a National Securities Identifying Number registry */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:NationalSecuritiesIdentifyingNumberRegistryEntry
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;SecurityRegistryEntry"/>
    }

  item def <'national security identification scheme'> NationalSecurityIdentificationScheme specializes SecurityIdentificationScheme {
    	doc /* security identification scheme, defining the format and structure of a National Securities Identifying Number (NSIN), published nationally on behalf of a country */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:NationalSecurityIdentificationScheme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;SecurityIdentificationScheme"/>
    }

  item def <'proprietary security identification scheme'> ProprietarySecurityIdentificationScheme specializes SecurityIdentificationScheme {
    	doc /* security identification scheme published by a commercial entity */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:ProprietarySecurityIdentificationScheme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;SecurityIdentificationScheme"/>
    }

  item def <'proprietary security identifier'> ProprietarySecurityIdentifier specializes SecurityIdentifier {
    	doc /* identifier supplied by a commercial entity */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:ProprietarySecurityIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;SecurityIdentifier"/>
    }

  item def <'security registry'> SecurityRegistry specializes FiBO_FND_ExternalStubs::Registry {
    	doc /* registry used to manage security identifiers and related information */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:SecurityRegistry
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;Registry"/>
    }

  item def <'security registry entry'> SecurityRegistryEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
    	doc /* record for a security in a securities registry */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:SecurityRegistryEntry
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
    }

  item def <'ticker symbol'> TickerSymbol specializes Identifiers_and_Indices::ReassignableIdentifier {
    	doc /* reassignable identifier of relatively short character string length that is unique within an exchange for a particular financial instrument or listing for that instrument */
    	// skos:definition xml:lang="en" (SecuritiesIdentification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentification.rdf)
    	// RDF: fibo-sec-sec-id:TickerSymbol
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-id;ReassignableIdentifier"/>
    }

}

// --- fibo-sec-sec-idind ---
