// ===================================================================
// EuropeanFinancialServicesEntities.sysml
// Source-parity extracted from: FiBO_FBC_FunctionalEntities.sysml
// Primary source module: EuropeanFinancialServicesEntities.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/FunctionalEntities
// Structural note: translated as a split file from FiBO_FBC_FunctionalEntities umbrella.
// ===================================================================
package European_Financial_Services_Entities {
  doc/* RDF: fibo-fbc-fct-eufse */
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

item def <'CRD credit institution'> CRDCreditInstitution specializes CreditInstitution {
  doc /* an undertaking whose business is to receive deposits or other repayable funds from the public and to grant credits for its own account as defined by the European Banking Authority (EBA) */
  // skos:definition xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-eufse;CreditInstitution"/>
    	// rdfs:label xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-eufse:CRDCreditInstitution
    }

item def <'credit institution'> CreditInstitution specializes CreditInstitutionInvestmentFirm {
  doc /* an undertaking the business of which is to take deposits or other repayable funds from the public and to grant credits for its own account, and to which authorisation has been granted to operate within the European Union and European Economic Area countries (EEA) */
  // skos:definition xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-eufse;CreditInstitutionInvestmentFirm"/>
    	// rdfs:label xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-eufse:CreditInstitution
    }

item def <'credit institution / investment firm'> CreditInstitutionInvestmentFirm specializes Financial_Services_Entities::FinancialInstitution {
  doc /* classification specific to European financial institutions that designates them as credit institutions / investment firms as defined by the European Banking Authority (EBA) */
  // skos:definition xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
    	// rdfs:label xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-eufse:CreditInstitutionInvestmentFirm
    }

item def <'European Economic Area branch'> EuropeanEconomicAreaBranch specializes CreditInstitution {
  doc /* a branch of a credit institution authorised in another European Economic Area (EEA) country that has the right to passport its activities */
  // skos:definition xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-eufse;CreditInstitution"/>
    	// rdfs:label xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-eufse:EuropeanEconomicAreaBranch
    }

item def <'investment firm'> InvestmentFirm specializes CreditInstitutionInvestmentFirm {
  doc /* any legal person whose regular occupation or business is the provision of one or more investment services to third parties and/or the performance of one or more investment activities on a professional basis */
  // skos:definition xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-eufse;CreditInstitutionInvestmentFirm"/>
    	// rdfs:label xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-eufse:InvestmentFirm
    }

item def <'local firm'> LocalFirm specializes CreditInstitutionInvestmentFirm, Financial_Services_Entities::BrokerageFirm {
  doc /* a firm dealing for its own account on markets in financial futures or options or other derivatives and on cash markets for the sole purpose of hedging positions on derivatives markets, or dealing for the accounts of other members of those markets and being guaranteed by clearing members of the same markets, where responsibility for ensuring the performance of contracts entered into by such a firm is assumed by clearing members of the same markets */
  // skos:definition xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-eufse;CreditInstitutionInvestmentFirm"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;BrokerageFirm"/>
    	// rdfs:label xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-eufse:LocalFirm
    }

item def <'non European Economic Area branch'> NonEuropeanEconomicAreaBranch specializes CreditInstitution {
  doc /* a branch of a credit institution whose Head Office is in a third country */
  // skos:definition xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-eufse;CreditInstitution"/>
    	// rdfs:label xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-eufse:NonEuropeanEconomicAreaBranch
    }

item def <'payment institution'> PaymentInstitution specializes CreditInstitutionInvestmentFirm {
  doc /* a legal person that has been granted authorisation in accordance with Article 10 to provide and execute payment services throughout the European community */
  // skos:definition xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-eufse;CreditInstitutionInvestmentFirm"/>
    	// rdfs:label xml:lang="en" (EuropeanFinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-eufse:PaymentInstitution
    }

item def <'Credit Institution Register entry'> CreditInstitutionRegisterEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
    	doc /* entry in the Credit Institution Register, a repository of credit institutions collected by the European Banking Authority (EBA) as provided by the various national banking authorities for those institutions that qualify */     	// skos:definition xml:lang="en" (EURegulatoryAgencies.rdf)
    	// rdfs:label xml:lang="en" (EURegulatoryAgencies.rdf)
      // RDF: fibo-fbc-fct-eurga:CreditInstitutionRegisterEntry
      // subClassOf: RegistryEntry - external CMNS
    }

}


// --- fibo-fbc-fct-eurga (EUFinancialServices) ---


// --- fibo-fbc-fct-fse (FinancialServicesEntities) ---
