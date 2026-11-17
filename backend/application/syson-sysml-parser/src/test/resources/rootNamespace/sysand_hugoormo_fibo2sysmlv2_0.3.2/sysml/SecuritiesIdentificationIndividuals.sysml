// ===================================================================
// SecuritiesIdentificationIndividuals.sysml
// Source-parity extracted from: FiBO_SEC_Securities.sysml
// Primary source module: SecuritiesIdentificationIndividuals.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Securities
// ===================================================================
package Securities_Identification_Individuals {
  doc/* This ontology defines concepts and primarily individuals required to identify securities, including the individuals that represent a number of well-known securities identifiers and related schemes, registries, and registration authorities. (source: dct:abstract, SecuritiesIdentificationIndividuals.rdf) */
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

  item def <'Committee on Uniform Securities Identification Procedures (CUSIP) number'> CommitteeOnUniformSecuritiesIdentificationProceduresNumber specializes Securities_Identification::NationalSecuritiesIdentifyingNumber, Securities_Identification::ProprietarySecurityIdentifier, FiBO_FND_ExternalStubs::RegisteredIdentifier { // RDF: fibo-sec-sec-idind:CommitteeOnUniformSecuritiesIdentificationProceduresNumber
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;NationalSecuritiesIdentifyingNumber"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;ProprietarySecurityIdentifier"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
        // RDF: fibo-sec-sec-idind:CommitteeOnUniformSecuritiesIdentificationProceduresNumber
    }

  item def <'common code registry entry'> CommonCodeRegistryEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
    	doc /* entry in a common code registry */
    	// skos:definition xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// RDF: fibo-sec-sec-idind:CommonCodeRegistryEntry
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
    }

  item def <'CUSIP International Numbering System (CINS) number'> CUSIPInternationalNumberingSystemNumber specializes Securities_Identification::ProprietarySecurityIdentifier, FiBO_FND_ExternalStubs::RegisteredIdentifier {
    	doc /* 9-character alphanumeric identifier that employs the same 9 characters as CUSIP, but also contains a letter of the alphabet in the first position signifying the issuer's country or geographic region, issued by CUSIP Global Services */
    	// skos:definition xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// RDF: fibo-sec-sec-idind:CUSIPInternationalNumberingSystemNumber
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;ProprietarySecurityIdentifier"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
    }

  item def <'Euroclear Clearstream common code'> EuroclearClearstreamCommonCode specializes Securities_Identification::ProprietarySecurityIdentifier, FiBO_FND_ExternalStubs::RegisteredIdentifier { // RDF: fibo-sec-sec-idind:EuroclearClearstreamCommonCode
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;ProprietarySecurityIdentifier"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
        // RDF: fibo-sec-sec-idind:EuroclearClearstreamCommonCode
    }

  item def <'financial instrument global identifier'> FinancialInstrumentGlobalIdentifier specializes FiBO_FND_ExternalStubs::RegisteredIdentifier {
    	doc /* financial instrument identifier that is defined as specified in the Object Management Group (OMG) Financial Instrument Global Identifier (FIGI) Specification */
    	// skos:definition xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// RDF: fibo-sec-sec-idind:FinancialInstrumentGlobalIdentifier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
    }

  item def <'Financial Instrument Global Identifier (FIGI) registry entry'> FinancialInstrumentGlobalIdentifierRegistryEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
    	doc /* entry in a Financial Instrument Global Identifier (FIGI) registry */
    	// skos:definition xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// RDF: fibo-sec-sec-idind:FinancialInstrumentGlobalIdentifierRegistryEntry
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
    }

  item def <'Refinitiv instrument code'> RefinitivInstrumentCode specializes Securities_Identification::ProprietarySecurityIdentifier {
    	doc /* proprietary code for financial instruments and indices owned, managed, and distributed by the London Stock Exchange Group's LSEG Financial Solutions (branded as Refinitiv) */
    	// skos:definition xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// RDF: fibo-sec-sec-idind:RefinitivInstrumentCode
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;ProprietarySecurityIdentifier"/>
    }

  item def <'Stock Exchange Daily Official List (SEDOL) code'> StockExchangeDailyOfficialListCode specializes Securities_Identification::ListedSecurityIdentifier, Securities_Identification::NationalSecuritiesIdentifyingNumber, Securities_Identification::ProprietarySecurityIdentifier {
    	doc /* seven-character security identifier, issued by the London Stock Exchange, that is the National Securities Identifying Number (NSIN) for securities issued in the United Kingdom, which is also part of the ISIN for the security it identifies */
    	// skos:definition xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// RDF: fibo-sec-sec-idind:StockExchangeDailyOfficialListCode
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;ListedSecurityIdentifier"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;NationalSecuritiesIdentifyingNumber"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;ProprietarySecurityIdentifier"/>
    }

  item def <'Telekurs Id'> TelekursId specializes Securities_Identification::ProprietarySecurityIdentifier {
    	doc /* identifier used to identify financial instruments owned, managed, and distributed by SIX Financial Information (formerly Telekurs AG and subsequently SIX Telekurs Ltd.) */
    	// skos:definition xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// RDF: fibo-sec-sec-idind:TelekursId
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;ProprietarySecurityIdentifier"/>
    }

  item def <'Valoren'> Valoren specializes Securities_Identification::ListedSecurityIdentifier, Securities_Identification::NationalSecuritiesIdentifyingNumber, Securities_Identification::ProprietarySecurityIdentifier {
    	doc /* identification number assigned to financial instruments in Switzerland, Liechtenstein and Belgium, issued by SIX Financial Information, that is the National Securities Identifying Number (NSIN) for securities issued in those countries and is also part of the ISIN for the security it identifies */
    	// skos:definition xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIdentificationIndividuals.rdf)
    	// RDF: fibo-sec-sec-idind:Valoren
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;ListedSecurityIdentifier"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;NationalSecuritiesIdentifyingNumber"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-id;ProprietarySecurityIdentifier"/>
    }

}

// --- fibo-sec-sec-iss ---
