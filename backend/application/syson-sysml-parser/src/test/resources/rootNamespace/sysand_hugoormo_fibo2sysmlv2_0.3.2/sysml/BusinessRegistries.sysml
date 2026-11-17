// ===================================================================
// BusinessRegistries.sysml
// Source-parity extracted from: FiBO_FBC_FunctionalEntities.sysml
// Primary source module: BusinessRegistries.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/FunctionalEntities
// Structural note: translated as a split file from FiBO_FBC_FunctionalEntities umbrella.
// ===================================================================
package Business_Registries {
  doc/* This ontology extends the Registration Authorities ontology to define specific kinds of registries, such as business entity registries, registries for identifiers and codes of various sorts, and registries for financial institutions and intermediaries based on jurisdiction, who regulates them, and the services they provide. (source: dct:abstract, BusinessRegistries.rdf) */
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

    item def <'business register identifier'> BusinessRegisterIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
        doc /* identifier that uniquely identifies a business register, such as a register identified by the Global Legal Entity Identifier Foundation (GLEIF) registration authorities list */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:BusinessRegisterIdentifier
      // subClassOf: Identifier - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def <'business registration authority'> BusinessRegistrationAuthority specializes FiBO_FND_ExternalStubs::Registrar, FiBO_FND_ExternalStubs::RegistrationAuthority {
        doc /* registration authority that is responsible for maintaining a registry of business entities */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;Registrar"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistrationAuthority"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:BusinessRegistrationAuthority
      // subClassOf: Registrar, RegistrationAuthority - external CMNS
    }

    item def <'business registry'> BusinessRegistry specializes FiBO_FND_ExternalStubs::Registry {
        doc /* registry for registering and maintaining information about business entities */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;Registry"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:BusinessRegistry
      // subClassOf: Registry - external CMNS
    }

    item def <'business registry entry'> BusinessRegistryEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
        doc /* entry in a business registry */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:BusinessRegistryEntry
      // subClassOf: RegistryEntry - external CMNS
    }

    item def <'entity expiration reason'> EntityExpirationReason specializes FiBO_FND_ExternalStubs::CodeElement {
        doc /* code for the reason that a legal entity ceased to exist and/or operate */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:EntityExpirationReason
      // subClassOf: CodeElement - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def <'entity legal form registry'> EntityLegalFormRegistry specializes FiBO_FND_ExternalStubs::Registry {
        doc /* registry for registering and maintaining information about the legal forms that are valid for business entities for a particular jurisdiction following the ISO 20275 standard */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;Registry"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:EntityLegalFormRegistry
      // subClassOf: Registry - external CMNS
    }

    item def <'entity legal form registry entry'> EntityLegalFormRegistryEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
        doc /* entry in an entity legal form registry that conforms to ISO 20275 */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:EntityLegalFormRegistryEntry
      // subClassOf: RegistryEntry - external CMNS
    }

    item def <'entity status'> EntityStatus specializes Lifecycles::LifecycleStage {
        doc /* lifecycle stage indicating the operational and/or legal status of an entity */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStage"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:EntityStatus
      // subClassOf: LifecycleStage - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'entity validation level'> EntityValidationLevel specializes FiBO_FND_ExternalStubs::CodeElement {
        doc /* code for the level of validation performed by the GLEIF or LOU with respect to the reference data provided by the registrant */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:EntityValidationLevel
      // subClassOf: CodeElement - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def FormalOrganization {
      // RDF: fibo-fbc-fct-breg:FormalOrganization
    }

    item def <'legal entity identifier registry'> LegalEntityIdentifierRegistry specializes BusinessRegistry {
        doc /* registry for registering and maintaining information about business entities for a particular jurisdiction */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-breg;BusinessRegistry"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:LegalEntityIdentifierRegistry
    }

    item def <'legal entity identifier registry entry'> LegalEntityIdentifierRegistryEntry specializes BusinessRegistryEntry {
        doc /* entry in a legal entity identifier registry that conforms to ISO 17442 and the Global Legal Entity Identifier Foundation (GLEIF) Common Data Format (CDF) */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-breg;BusinessRegistryEntry"/>
      // RDF: fibo-fbc-fct-breg:LegalEntityIdentifierRegistryEntry
    }

    item def <'local operating unit'> LocalOperatingUnit specializes FiBO_FND_ExternalStubs::Registrar {
        doc /* registrar that is authorized by the Global LEI Foundation to issue legal entity identifiers */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;Registrar"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:LocalOperatingUnit
      // subClassOf: Registrar - external CMNS
    }

    item def <'North American Industry Classification System code'> NorthAmericanIndustryClassificationSystemCode specializes Classification_Schemes::IndustrySectorClassifier, FiBO_FND_ExternalStubs::CodeElement {
        doc /* the North American Industry Classification System (NAICS) code representing an industry */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
      // RDF: fibo-fbc-fct-breg:NorthAmericanIndustryClassificationSystemCode
      // subClassOf: IndustrySectorClassifier - from FIBO_FND_Arrangements (ArrangementsPackage); CodeElement - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def <'North American Industry Classification System scheme'> NorthAmericanIndustryClassificationSystemScheme specializes Classification_Schemes::IndustrySectorClassificationScheme, FiBO_FND_CodeSets_And_Schemes::CodeSet {
        doc /* the scheme defining the North American Industry Classification System (NAICS) Codes */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassificationScheme"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeSet"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:NorthAmericanIndustryClassificationSystemScheme
      // subClassOf: IndustrySectorClassificationScheme - from FIBO_FND_Arrangements; CodeSet - external CMNS
    }

    item def <'passport number'> PassportNumber specializes FiBO_FND_ExternalStubs::RegisteredIdentifier, Government_Entities::NationalIdentificationNumber{
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;NationalIdentificationNumber"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
    	doc /* national identification number associated with a passport */     	// skos:definition xml:lang="en" (People.rdf)
    	// rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fbc-fct-breg:PassportNumber
      // subClassOf: RegisteredIdentifier - external CMNS
    }

    item def <'registration authority code'> RegistrationAuthorityCode specializes FiBO_FND_ExternalStubs::CodeElement, FiBO_FND_ExternalStubs::RegisteredIdentifier {
        doc /* identifier that uniquely identifies a business registry, and is associated with a registration authority and jurisdiction, issued by the Global Legal Entity Identifier Foundation (GLEIF) */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:RegistrationAuthorityCode
      // subClassOf: CodeElement - from FIBO_FND_Accounting; RegisteredIdentifier - external CMNS
    }

    item def <'registration status'> RegistrationStatus specializes Lifecycles::LifecycleStage {
        doc /* lifecycle stage indicating the status of a given registration of something, such as a business or legal entity, as specified by the registration authority */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStage"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:RegistrationStatus
      // subClassOf: LifecycleStage - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'standard industrial classification code'> StandardIndustrialClassificationCode specializes Classification_Schemes::IndustrySectorClassifier, FiBO_FND_ExternalStubs::CodeElement {
        doc /* the SIC code representing an industry */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
      // RDF: fibo-fbc-fct-breg:StandardIndustrialClassificationCode
      // subClassOf: IndustrySectorClassifier - from FIBO_FND_Arrangements; CodeElement - from FIBO_FND_Accounting
    }

    item def <'standard industrial classification scheme'> StandardIndustrialClassificationScheme specializes Classification_Schemes::IndustrySectorClassificationScheme, FiBO_FND_CodeSets_And_Schemes::CodeSet {
        doc /* the scheme defining the Standard Industrial Classification (SIC) Code List */
        // skos:definition xml:lang="en" (BusinessRegistries.rdf)
        // rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassificationScheme"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeSet"/>
    	// rdfs:label xml:lang="en" (BusinessRegistries.rdf)
      // RDF: fibo-fbc-fct-breg:StandardIndustrialClassificationScheme
      // subClassOf: IndustrySectorClassificationScheme - from FIBO_FND_Arrangements; CodeSet - external CMNS
    }

}

// --- fibo-fbc-fct-cajfse (CARegulatory) ---
