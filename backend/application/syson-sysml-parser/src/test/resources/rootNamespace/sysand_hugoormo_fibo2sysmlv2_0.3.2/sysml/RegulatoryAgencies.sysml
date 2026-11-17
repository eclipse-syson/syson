// ===================================================================
// RegulatoryAgencies.sysml
// Source-parity extracted from: FiBO_FBC_FunctionalEntities.sysml
// Primary source module: RegulatoryAgencies.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/FunctionalEntities
// Structural note: translated as a split file from FiBO_FBC_FunctionalEntities umbrella.
// ===================================================================
package Regulatory_Agencies {
  doc/* This ontology defines general purpose concepts for representation of regulatory agencies, also known as regulatory authorities or regulators. Examples of financial industry regulatory agencies in the US include the Securities Exchange Commission, FINRA, and the FDIC, among others. The SEC and FINRA are both registration authorities and regulatory agencies. The FDIC is a regulatory agency and an insurer, and may be a registration authority for certain state-chartered banks in the US without bank holding companies. (source: dct:abstract, RegulatoryAgencies.rdf) */
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

    item def <'examiner'> Examiner specializes Executives::ResponsibleParty {
      doc /* party empowered as an official representative by a regulatory agency to investigate and review specified documents for accuracy and truthfulness */
      // skos:definition xml:lang="en" (RegulatoryAgencies.rdf)
      // rdfs:label xml:lang="en" (RegulatoryAgencies.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;ResponsibleParty"/>
      // RDF: fibo-fbc-fct-ra:Examiner
      // subClassOf: ResponsibleParty - external CMNS
    }

    item def <'government-issued license'> GovernmentIssuedLicense specializes Legal_Capacity::License {
      doc /* grant of permission needed to legally perform some task, provide some service, exercise a certain privilege, or pursue some business or occupation */
      // skos:definition xml:lang="en" (RegulatoryAgencies.rdf)
      // rdfs:label xml:lang="en" (RegulatoryAgencies.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;License"/>
    	// rdfs:label xml:lang="en" (RegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-ra:GovernmentIssuedLicense
      // subClassOf: License - from FIBO_FND_Law (LegalCoreConceptsPackage)
    }

    item def <'regulation identification scheme'> RegulationIdentificationScheme specializes FiBO_FND_CodeSets_And_Schemes::IdentificationScheme {
      doc /* a scheme for organizing information and allocating identifiers to regulations */
      // skos:definition xml:lang="en" (RegulatoryAgencies.rdf)
      // rdfs:label xml:lang="en" (RegulatoryAgencies.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;IdentificationScheme"/>
    	// rdfs:label xml:lang="en" (RegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-ra:RegulationIdentificationScheme
      // subClassOf: IdentificationScheme - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'regulation identifier'> RegulationIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
      doc /* an identifier associated with a regulation */
      // skos:definition xml:lang="en" (RegulatoryAgencies.rdf)
      // rdfs:label xml:lang="en" (RegulatoryAgencies.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
    	// rdfs:label xml:lang="en" (RegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-ra:RegulationIdentifier
      // subClassOf: Identifier - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def RegulatoryAgency {
      // RDF: fibo-fbc-fct-ra:RegulatoryAgency
    }

    item def <'regulatory capacity'> RegulatoryCapacity specializes Legal_Capacity::LegalCapacity {
      doc /* the capacity of some natural person to regulate some industry, organization, or product by virtue of some certification program on behalf of some regulatory agency */
      // skos:definition xml:lang="en" (RegulatoryAgencies.rdf)
      // rdfs:label xml:lang="en" (RegulatoryAgencies.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalCapacity"/>
    	// rdfs:label xml:lang="en" (RegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-ra:RegulatoryCapacity
      // subClassOf: LegalCapacity - from FIBO_FND_Law (LegalCoreConceptsPackage)
    }

    item def RegulatoryReport specializes Reporting::Report {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rep;Report"/>
      // RDF: fibo-fbc-fct-ra:RegulatoryReport
      // subClassOf: Report - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'tax authority'> TaxAuthority specializes RegulatoryAgency {
      doc /* regulatory agency that has jurisdiction over the assessment, determination, collection, imposition and other aspects of any tax */
      // skos:definition xml:lang="en" (RegulatoryAgencies.rdf)
      // rdfs:label xml:lang="en" (RegulatoryAgencies.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-rga;RegulatoryAgency"/>
    	// rdfs:label xml:lang="en" (RegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-ra:TaxAuthority
    }

}

// --- fibo-fbc-fct-usjfse (USFinancialServices) ---


// --- fibo-fbc-fct-usjrga (USFinancialServices) ---


// --- fibo-fbc-fct-usrga (USFinancialServices) ---
