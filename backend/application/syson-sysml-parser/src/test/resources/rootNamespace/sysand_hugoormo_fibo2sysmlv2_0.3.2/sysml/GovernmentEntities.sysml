// ===================================================================
// GovernmentEntities.sysml
// Source-parity extracted from: FiBO_BE_GovernmentEntities.sysml
// Primary source module: GovernmentEntities.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/GovernmentEntities
// Structural note: translated as a single-file package from its source umbrella.
// ===================================================================
package Government_Entities {
  doc/* This ontology defines the fundamental concepts for representing polities and government entities and their relations. (source: dct:abstract, GovernmentEntities.rdf) */
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

// FunctionalEntity
// LegalEntity, LegalPerson

  // --- fibo-be-ge-ge ---

  item def <'branch of government'> BranchOfGovernment specializes GovernmentBody {
    doc /* division of the government of a state, with separate and independent powers and areas of responsibility so that the powers of one branch are not in conflict with the powers associated with the other branches */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;GovernmentBody"/>
    // RDF: fibo-be-ge-ge:BranchOfGovernment
  }

  item def <'devolved government'> DevolvedGovernment specializes Government {
    doc /* government and the politicians that that run a subnational territory with powers that may be temporary and reversible, ultimately residing with the central government */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;Government"/>
    // RDF: fibo-be-ge-ge:DevolvedGovernment
  }

  item def <'executive branch'> ExecutiveBranch specializes BranchOfGovernment {
    doc /* the branch of government that is authorized and responsible for the daily administration of the government */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;BranchOfGovernment"/>
    // RDF: fibo-be-ge-ge:ExecutiveBranch
  }

  item def <'federal government'> FederalGovernment specializes NationalGovernment {
    doc /* union of states under a central government distinct from the individual governments of the separate states */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;NationalGovernment"/>
    // RDF: fibo-be-ge-ge:FederalGovernment
  }

  item def <'federated sovereignty'> FederatedSovereignty specializes SovereignState {
    doc /* polity characterized by a union of partially self-governing states or regions under a central (federal) government */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;SovereignState"/>
    // RDF: fibo-be-ge-ge:FederatedSovereignty
  }

  item def <'government'> Government specializes Functional_Entities::FunctionalEntity {
    doc /* system by which a state or community is controlled */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalEntity"/>
    // R2/R6 trace-only: hasMember union(BranchOfGovernment|GovernmentAgency|GovernmentDepartment), min 0 evidence retained from GovernmentEntities.rdf.
    // RDF: fibo-be-ge-ge:Government
  }

  item def <'government agency'> GovernmentAgency specializes GovernmentBody {
    doc /* permanent or semi-permanent organization, often an appointed commission, in the machinery of government that is responsible for the oversight and administration of specific functions */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;GovernmentBody"/>
    // RDF: fibo-be-ge-ge:GovernmentAgency
  }

  item def <'government appointee'> GovernmentAppointee specializes GovernmentOfficial, Executives::Executive {
    doc /* individual designated by government decree to lead, or participate in some capacity in a government body */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;GovernmentOfficial"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;Executive"/>
    // RDF: fibo-be-ge-ge:GovernmentAppointee
  }

  item def <'government body'> GovernmentBody specializes Business_Registries::FormalOrganization {
    doc /* formal organization that is an agency, instrumentality, or other body of a supranational, national, federal, state, or local government, including certain multijurisdictional agencies and departments that carry out the business of government */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;FormalOrganization"/>
    // RDF: fibo-be-ge-ge:GovernmentBody
  }

  item def <'government department'> GovernmentDepartment specializes GovernmentBody {
    doc /* specialized organization responsible for a sector of government public administration */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;GovernmentBody"/>
    // RDF: fibo-be-ge-ge:GovernmentDepartment
  }

  item def <'government minister'> GovernmentMinister specializes GovernmentOfficial, Executives::Executive {
    doc /* government official that is an executive, who is either appointed or elected to a high office in the government */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;GovernmentOfficial"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;Executive"/>
    // RDF: fibo-be-ge-ge:GovernmentMinister
  }

  item def <'government official'> GovernmentOfficial specializes Executives::ResponsibleParty {
    doc /* person elected or appointed to administer some aspect of a government */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;ResponsibleParty"/>
    // RDF: fibo-be-ge-ge:GovernmentOfficial
  }

  item def <'identity document'> IdentityDocument specializes FiBO_FND_ExternalStubs::LegalDocument {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;LegalDocument"/>
    // RDF: fibo-be-ge-ge:IdentityDocument
  }

  item def <'instrumentality'> Instrumentality specializes GovernmentBody, FiBO_FND_ExternalStubs::LegalEntity {
    doc /* organization that serves a public purpose and is closely tied to a government, but is not a government agency */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;GovernmentBody"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalEntity"/>
    // RDF: fibo-be-ge-ge:Instrumentality
  }

  item def <'judiciary'> Judiciary specializes BranchOfGovernment {
    doc /* branch of government that comprises the system of courts that interprets and applies the law in the name of the supranational, national, federal, or regional government, depending on its jurisdiction */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;BranchOfGovernment"/>
    // RDF: fibo-be-ge-ge:Judiciary
  }

  item def <'legislature'> Legislature specializes BranchOfGovernment {
    doc /* the law-making body of a political unit, usually a national government, that has power to enact, amend, and repeal public policy */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;BranchOfGovernment"/>
    // RDF: fibo-be-ge-ge:Legislature
  }

  item def <'municipal entity'> MunicipalEntity specializes Polity {
    doc /* polity that typically represents a city, township, or other administrative subdivision having corporate status and powers of self-government or jurisdiction */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;Polity"/>
    // RDF: fibo-be-ge-ge:MunicipalEntity
  }

  item def <'municipal government'> MunicipalGovernment specializes Government {
    doc /* regional government of a city, township, or other administrative subdivision */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;Government"/>
    // RDF: fibo-be-ge-ge:MunicipalGovernment
  }

  item def <'national government'> NationalGovernment specializes Government {
    doc /* government and the politicians that that run a country as a whole (as opposed to local government) */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;Government"/>
    // RDF: fibo-be-ge-ge:NationalGovernment
  }

  item def <'national identification number'> NationalIdentificationNumber specializes FiBO_FND_ExternalStubs::Identifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
  	doc /* number or text which appears on an identity document issued by a country or jurisdiction */   	// skos:definition xml:lang="en" (People.rdf)
  	// rdfs:label xml:lang="en" (People.rdf)
    // RDF: fibo-be-ge-ge:NationalIdentificationNumber
    attribute numberValue[0..1];
  }

  item def <'polity'> Polity specializes GovernmentBody, Legal_Persons::LegalPerson {
    doc /* legal person that is a supranational entity, crown, state, or subordinate civil authority, such as a province, prefecture, county, municipality, city, or district representing the people of that entity */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;GovernmentBody"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalPerson"/>
    // RDF: fibo-be-ge-ge:Polity
  }

  item def <'regional government'> RegionalGovernment specializes Government {
    doc /* administrative body for a geographic area, such as a county, smaller town, or other similar community */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;Government"/>
    // RDF: fibo-be-ge-ge:RegionalGovernment
  }

  item def <'regional sovereignty'> RegionalSovereignty specializes Polity {
    doc /* legal person that corresponds to an administrative division, administrative unit, administrative entity or country subdivision (or, sometimes, geopolitical division or subnational entity), that has the capacity to incur debt, issue contracts, and enter into relations with other similar entities */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;Polity"/>
    // RDF: fibo-be-ge-ge:RegionalSovereignty
  }

  item def <'sovereign state'> SovereignState specializes Polity {
    doc /* non-physical juridical entity that is represented by one centralized government that has sovereignty over a geographic area */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;Polity"/>
    // RDF: fibo-be-ge-ge:SovereignState
  }

  item def <'supranational entity'> SupranationalEntity specializes Polity {
    doc /* governmental or non-governmental entity that is established by international law or treaty or incorporated at an international level */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;Polity"/>
    // RDF: fibo-be-ge-ge:SupranationalEntity
  }

  item def <'tribal area'> TribalArea specializes FiBO_FND_ExternalStubs::GeopoliticalEntity {
    doc /* designation for geographic region administered by a group of indigenous people rather than by a sovereign state or regional governmental entity */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;GeopoliticalEntity"/>
    // RDF: fibo-be-ge-ge:TribalArea
  }

  item def <'tribal entity'> TribalEntity specializes Polity {
    doc /* legal entity that represents fundamental unit of sovereign tribal (indigenous) government */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;Polity"/>
    // RDF: fibo-be-ge-ge:TribalEntity
  }

  item def <'tribal government'> TribalGovernment specializes Government {
    doc /* government representing a group of indigenous people that has legal authority to govern those people, including authority to legislate the existence of tribal entities */
    // skos:definition xml:lang="en" (GovernmentEntities.rdf)
    // rdfs:label xml:lang="en" (GovernmentEntities.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;Government"/>
    // RDF: fibo-be-ge-ge:TribalGovernment
  }
}
