// ===================================================================
// CanadianFinancialServicesEntities.sysml
// Source-parity extracted from: FiBO_FBC_FunctionalEntities.sysml
// Primary source module: CanadianFinancialServicesEntities.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/FunctionalEntities
// Structural note: translated as a split file from FiBO_FBC_FunctionalEntities umbrella.
// ===================================================================
package Canadian_Financial_Services_Entities {
  doc/* RDF: fibo-fbc-fct-cajfse */
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

item def <'Canadian credit union'> CanadianCreditUnion specializes Financial_Services_Entities::CreditUnion {
  doc /* not-for-profit financial institution, typically formed by the employees of a company, labor union, or religious group, operated as a cooperative association organized for the purpose of promoting thrift among its members and creating a source of credit for provident or productive purposes */
  // skos:definition xml:lang="en" (CanadianFinancialServicesEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;CreditUnion"/>
    	// rdfs:label xml:lang="en" (CanadianFinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-cajfse:CanadianCreditUnion
    }

item def <'business number'> BusinessNumber specializes Parties::TaxIdentifier, Functional_Entities::OrganizationIdentifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pty-pty;TaxIdentifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationIdentifier"/>
    	doc /* unique, 9-digit number that is the standard identifier for legal entities in Canada which are typically a business */     	// skos:definition xml:lang="en" (CARegulatoryAgencies.rdf)
    	// rdfs:label xml:lang="en" (CARegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-cajrga:BusinessNumber
      // subClassOf: TaxIdentifier - external CMNS; OrganizationIdentifier - from FiBO_BE_FunctionalEntities
    }

item def <'corporation income tax number'> CorporationIncomeTaxNumber specializes BusinessNumber {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-cajrga;BusinessNumber"/>
    	doc /* concatenation of an entity's business number, the 'RC' abbreviation and a 4-digit subaccount number used for reporting corporate income tax */     	// skos:definition xml:lang="en" (CARegulatoryAgencies.rdf)
    	// rdfs:label xml:lang="en" (CARegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-cajrga:CorporationIncomeTaxNumber
    }

item def <'Goods and Services Tax / Harmonized Sales Tax registration number'> GoodsServicesTaxHarmonizedSalesTaxRegistrationNumber specializes BusinessNumber, Legal_Persons::ValueAddedTaxIdentificationNumber{
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-fbo;ValueAddedTaxIdentificationNumber"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-cajrga;BusinessNumber"/>
      // RDF: fibo-fbc-fct-cajrga:GoodsServicesTaxHarmonizedSalesTaxRegistrationNumber
      // subClassOf also: ValueAddedTaxIdentificationNumber - external
    }

item def <'import export program number'> ImportExportProgramNumber specializes BusinessNumber {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-cajrga;BusinessNumber"/>
    	doc /* concatenation of an entity's business number, the 'RM' abbreviation and a 4-digit subaccount number used for customs and import/export reporting purposes */     	// skos:definition xml:lang="en" (CARegulatoryAgencies.rdf)
    	// rdfs:label xml:lang="en" (CARegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-cajrga:ImportExportProgramNumber
    }

item def <'information return program number'> InformationReturnsProgramNumber specializes BusinessNumber {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-cajrga;BusinessNumber"/>
    	doc /* concatenation of an entity's business number, the 'RZ' abbreviation and a 4-digit subaccount number used for information returns */     	// skos:definition xml:lang="en" (CARegulatoryAgencies.rdf)
    	// rdfs:label xml:lang="en" (CARegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-cajrga:InformationReturnsProgramNumber
    }

item def <'payroll deductions program number'> PayrollDeductionsProgramNumber specializes BusinessNumber {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-cajrga;BusinessNumber"/>
    	doc /* concatenation of an entity's business number, the 'RP' abbreviation and a 4-digit subaccount number used for reporting payroll deductions */     	// skos:definition xml:lang="en" (CARegulatoryAgencies.rdf)
    	// rdfs:label xml:lang="en" (CARegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-cajrga:PayrollDeductionsProgramNumber
    }

item def <'registered charity program number'> RegisteredCharityProgramNumber specializes BusinessNumber {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-cajrga;BusinessNumber"/>
    	doc /* concatenation of an entity's business number, the 'RR' abbreviation and a 4-digit subaccount number used for registered charity contribution */     	// skos:definition xml:lang="en" (CARegulatoryAgencies.rdf)
    	// rdfs:label xml:lang="en" (CARegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-cajrga:RegisteredCharityProgramNumber
    }

}


// --- fibo-fbc-fct-cajrga (CARegulatory) ---


// --- fibo-fbc-fct-eufse (EUFinancialServices) ---
