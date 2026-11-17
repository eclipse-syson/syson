// ===================================================================
// LegalPersons.sysml
// Source-parity extracted from: FiBO_BE_LegalEntities.sysml
// Primary source module: LegalPersons.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/LegalEntities
// Structural note: this file intentionally consolidates lp/fbo/lei fragments from the same source umbrella.
// ===================================================================
package Legal_Persons {
  doc/* This ontology defines legal personhood concepts. A legal person as defined here is any natural person or organization which is capable of accruing liability on its own part. (source: dct:abstract, LegalPersons.rdf) */
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

// OrganizationIdentifier

  // --- fibo-be-le-lp (LegalPersons) ---

  item def <'business entity'> BusinessEntity specializes Business_Registries::FormalOrganization {
    doc /* entity that is formed and administered as per commercial law in order to engage in business activities */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;FormalOrganization"/>
    // RDF: fibo-be-le-lp:BusinessEntity
  }

  item def <'business license'> BusinessLicense specializes Legal_Capacity::License {
    doc /* license that allows the holder to conduct business or carry out a specific profession within some jurisdiction for some period of time */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;License"/>
    // RDF: fibo-be-le-lp:BusinessLicense
  }

  item def <'chartered legal person'> CharteredLegalPerson specializes FiBO_FND_ExternalStubs::LegalEntity {
    doc /* a legal person created by a royal charter or decree */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalEntity"/>
    // RDF: fibo-be-le-lp:CharteredLegalPerson
  }

  item def <'legally competent natural person'> LegallyCompetentNaturalPerson specializes LegalPerson, People::Person{
    doc /* person who is considered competent, under the circumstances, to enter into a contract, conduct business, or participate in other activities that generally require the mental ability to understand problems and make decisions on his or her own behalf */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;Person"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalPerson"/>
    // RDF: fibo-be-le-lp:LegallyCompetentNaturalPerson
  }

  item def LegalPerson {
    // RDF: fibo-be-le-lp:LegalPerson
  }

  item def <'not for profit objective'> NotForProfitObjective specializes Objectives::Objective {
    doc /* objective that reflects the charitable, educational, religious, humanitarian, public services, or other not for profit goals of an organization */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Objective"/>
    // RDF: fibo-be-le-lp:NotForProfitObjective
  }

  item def <'power of attorney'> PowerOfAttorney specializes Legal_Capacity::LegalCapacity {
    doc /* legal authorization given by one party (the principal) to another (the agent or attorney-in-fact) to perform certain acts on the principal's behalf */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalCapacity"/>
    // RDF: fibo-be-le-lp:PowerOfAttorney
  }

  item def <'profit objective'> ProfitObjective specializes Objectives::BusinessObjective {
    doc /* objective that reflects pursuit of a financial benefit that may be realized when the amount of revenue gained from a business activity exceeds the expenses, costs and taxes needed to sustain that activity */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;BusinessObjective"/>
    // RDF: fibo-be-le-lp:ProfitObjective
  }

  item def <'public purpose'> PublicPurpose specializes Objectives::Objective {
    doc /* objective that reflects values generally thought to be shared by and that is intended to benefit the populace as a whole */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Objective"/>
    // RDF: fibo-be-le-lp:PublicPurpose
  }

  item def <'religious objective'> ReligiousObjective specializes NotForProfitObjective {
    doc /* not-for-profit objective that reflects the religious goals of an organization */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-lp;NotForProfitObjective"/>
    // RDF: fibo-be-le-lp:ReligiousObjective
  }

  item def <'fonds commun de placement'> SpecialPurposeVehicle specializes FiBO_FND_ExternalStubs::LegalEntity {
    doc /* legal entity created to fulfill narrow, specific, and frequently temporary objectives */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalEntity"/>
    // RDF: fibo-be-le-lp:SpecialPurposeVehicle
  }

  item def <'statutory body'> StatutoryBody specializes FiBO_FND_ExternalStubs::LegalEntity {
    doc /* legal entity established by a government to consider evidence and make judgements in some field of activity */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalEntity"/>
    // RDF: fibo-be-le-lp:StatutoryBody
  }

  item def <'variable interest entity'> VariableInterestEntity specializes FiBO_FND_ExternalStubs::LegalEntity {
    doc /* legal entity whose shareholders are entitled to a percentage of a named company's profits via a private contract */
    // skos:definition xml:lang="en" (LegalPersons.rdf)
    // rdfs:label xml:lang="en" (LegalPersons.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalEntity"/>
    // RDF: fibo-be-le-lp:VariableInterestEntity
  }

  // --- fibo-be-le-fbo (FormalBusinessOrganizations) ---

  item def <'branch'> Branch specializes FiBO_FND_ExternalStubs::OrganizationalSubUnit {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationalSubUnit"/>
  	doc /* part of a larger organization that might not be co-located with it */   	// skos:definition xml:lang="en" (FormalBusinessOrganizations.rdf)
  	// rdfs:label xml:lang="en" (FormalBusinessOrganizations.rdf)
    // RDF: fibo-be-le-fbo:Branch
  }

  item def <'division'> Division specializes FiBO_FND_ExternalStubs::OrganizationalSubUnit {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationalSubUnit"/>
  	doc /* part of an organization, such as a line of business, that may have separate accounting and reporting requirements */   	// skos:definition xml:lang="en" (FormalBusinessOrganizations.rdf)
  	// rdfs:label xml:lang="en" (FormalBusinessOrganizations.rdf)
    // RDF: fibo-be-le-fbo:Division
  }

  item def <'joint venture'> JointVenture specializes FiBO_FND_ExternalStubs::LegalEntity {
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalEntity"/>
  	doc /* legal entity that is formed between parties that pool their resources for the purpose of accomplishing a specific task but otherwise retain their distinct identities */   	// skos:definition xml:lang="en" (FormalBusinessOrganizations.rdf)
  	// rdfs:label xml:lang="en" (FormalBusinessOrganizations.rdf)
    // RDF: fibo-be-le-fbo:JointVenture
  }

  item def <'non-governmental organization'> NonGovernmentalOrganization specializes NotForProfitOrganization {
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-fbo;NotForProfitOrganization"/>
  	doc /* not-for-profit organization that functions independently of government */   	// skos:definition xml:lang="en" (FormalBusinessOrganizations.rdf)
  	// rdfs:label xml:lang="en" (FormalBusinessOrganizations.rdf)
    // RDF: fibo-be-le-fbo:NonGovernmentalOrganization
  }

  item def <'not for profit organization'> NotForProfitOrganization specializes Business_Registries::FormalOrganization {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;FormalOrganization"/>
  	doc /* organization that uses its surplus revenues to further achieve its purpose rather than distributing its surplus income to the organization's owners (directors, investors, and equivalents) as profit / dividends */   	// skos:definition xml:lang="en" (FormalBusinessOrganizations.rdf)
  	// rdfs:label xml:lang="en" (FormalBusinessOrganizations.rdf)
    // RDF: fibo-be-le-fbo:NotForProfitOrganization
  }

  item def <'organization covering agreement'> OrganizationCoveringAgreement specializes Contracts::WrittenContract {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
  	doc /* contract between the principals in a formal organization that specifies the relationship between the principals, and between the principals and the entity */   	// skos:definition xml:lang="en" (FormalBusinessOrganizations.rdf)
  	// rdfs:label xml:lang="en" (FormalBusinessOrganizations.rdf)
    // RDF: fibo-be-le-fbo:OrganizationCoveringAgreement
  }

  item def <'value-added tax identification number'> ValueAddedTaxIdentificationNumber specializes Parties::TaxIdentifier, Functional_Entities::OrganizationIdentifier, FiBO_FND_ExternalStubs::RegisteredIdentifier{
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationIdentifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pty-pty;TaxIdentifier"/>
  	doc /* tax identifier that identifies a taxable person (business) or non-taxable legal entity for a consumption tax that is assessed incrementally, levied on the price of a product or service at each stage of production, distribution, and sale to the end consumer */   	// skos:definition xml:lang="en" (FormalBusinessOrganizations.rdf)
  	// rdfs:label xml:lang="en" (FormalBusinessOrganizations.rdf)
    // RDF: fibo-be-le-fbo:ValueAddedTaxIdentificationNumber
    attribute numberValue[0..1];
  }

  // --- fibo-be-le-lei (LEIEntities) ---

  item def <'accounting framework'> AccountingFramework specializes RelationshipQualifier {
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-lei;RelationshipQualifier"/>
  	doc /* framework, including policies, methods, rules, and processes, used to measure, recognize, present, and disclose the information appearing in an entity's financial statements, and, from a legal ownership perspective, that is applied for accounting consolidation determination */   	// skos:definition xml:lang="en" (LEIEntities.rdf)
  	// rdfs:label xml:lang="en" (LEIEntities.rdf)
    // RDF: fibo-be-le-lei:AccountingFramework
  }

  item def <'contractually capable entity'> ContractuallyCapableEntity specializes FiBO_FND_ExternalStubs::Role {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;AgentRole"/>
  	doc /* a unique entity that is legally or financially responsible for the performance of financial transactions, or has the legal right in its jurisdiction to enter independently into legal contracts, regardless of whether it is incorporated or constituted in some other way (e.g. trust, partnership, contractual). This excludes natural persons, but includes governmental organizations and supranationals. */   	// skos:definition xml:lang="en" (LEIEntities.rdf)
  	// rdfs:label xml:lang="en" (LEIEntities.rdf)
    // RDF: fibo-be-le-lei:ContractuallyCapableEntity
  }

  item def <'entity legal form'> EntityLegalForm specializes FiBO_FND_ExternalStubs::Classifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    // RDF: fibo-be-le-lei:EntityLegalForm
  }

  item def <'entity legal form identifier'> EntityLegalFormIdentifier specializes FiBO_FND_ExternalStubs::CodeElement {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
  	doc /* code that denotes an entity legal form as defined in ISO 20275 */   	// skos:definition xml:lang="en" (LEIEntities.rdf)
  	// rdfs:label xml:lang="en" (LEIEntities.rdf)
    // RDF: fibo-be-le-lei:EntityLegalFormIdentifier
    attribute identifierValue[0..1];
  }

  item def <'entity legal form scheme'> EntityLegalFormScheme specializes Assessments::ClassificationScheme, FiBO_FND_CodeSets_And_Schemes::CodeSet {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;ClassificationScheme"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeSet"/>
  	doc /* scheme that specifies the elements of the codes for entity legal forms, such as those that are sanctioned in a given jurisdiction as defined in ISO 20725 */   	// skos:definition xml:lang="en" (LEIEntities.rdf)
  	// rdfs:label xml:lang="en" (LEIEntities.rdf)
    // RDF: fibo-be-le-lei:EntityLegalFormScheme
  }

  item def <'legal entity identifier'> LegalEntityIdentifier specializes Functional_Entities::OrganizationIdentifier, FiBO_FND_ExternalStubs::RegisteredIdentifier{
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationIdentifier"/>
  	doc /* an organization identifier that uniquely identifies a legal person as defined in ISO 17442 */   	// skos:definition xml:lang="en" (LEIEntities.rdf)
  	// rdfs:label xml:lang="en" (LEIEntities.rdf)
    // RDF: fibo-be-le-lei:LegalEntityIdentifier
  }

  item def <'legal entity identifier scheme'> LegalEntityIdentifierScheme specializes FiBO_FND_ExternalStubs::OrganizationIdentificationScheme {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationIdentificationScheme"/>
  	doc /* a scheme that specifies the elements of an unambiguous legal entity identifier (LEI) scheme to identify the legal entities relevant to any financial transaction */   	// skos:definition xml:lang="en" (LEIEntities.rdf)
  	// rdfs:label xml:lang="en" (LEIEntities.rdf)
    // RDF: fibo-be-le-lei:LegalEntityIdentifierScheme
  }

  item def <'LEI registered entity'> LEIRegisteredEntity specializes LegalPerson {
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalPerson"/>
  	doc /* a legal person that has registered for and is identified by a legal entity identifier */   	// skos:definition xml:lang="en" (LEIEntities.rdf)
  	// rdfs:label xml:lang="en" (LEIEntities.rdf)
    // RDF: fibo-be-le-lei:LEIRegisteredEntity
  }

  item def OrganizationName {
    // RDF: fibo-be-le-lei:OrganizationName
    attribute nameValue[0..1];
  }

  item def <'relationship period qualifier'> RelationshipPeriodQualifier specializes FiBO_FND_ExternalStubs::Classifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  	doc /* a classifier that qualifies something about the reporting period specified, such as that the date period reflects an accounting or document filing period */   	// skos:definition xml:lang="en" (LEIEntities.rdf)
  	// rdfs:label xml:lang="en" (LEIEntities.rdf)
    // RDF: fibo-be-le-lei:RelationshipPeriodQualifier
  }

  item def <'relationship qualifier'> RelationshipQualifier specializes FiBO_FND_ExternalStubs::Classifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  	doc /* a classifier that qualifies something about the relationship between consolidated entities during the reporting period, such as the accounting framework used */   	// skos:definition xml:lang="en" (LEIEntities.rdf)
  	// rdfs:label xml:lang="en" (LEIEntities.rdf)
    // RDF: fibo-be-le-lei:RelationshipQualifier
  }

  item def <'relationship record'> RelationshipRecord specializes FiBO_FND_ExternalStubs::Constituent {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Constituent"/>
  	doc /* a record describing relationships between legal entities */   	// skos:definition xml:lang="en" (LEIEntities.rdf)
  	// rdfs:label xml:lang="en" (LEIEntities.rdf)
    // RDF: fibo-be-le-lei:RelationshipRecord
  }

  item def <'relationship status'> RelationshipStatus specializes FiBO_FND_ExternalStubs::Classifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  	doc /* a classifier that specifies the status of the relationship between consolidated entities during the reporting period (active or inactive) */   	// skos:definition xml:lang="en" (LEIEntities.rdf)
  	// rdfs:label xml:lang="en" (LEIEntities.rdf)
    // RDF: fibo-be-le-lei:RelationshipStatus
  }

}
