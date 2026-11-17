// ===================================================================
// Executives.sysml
// Source-parity extracted from: FiBO_BE_OwnershipAndControl.sysml
// Primary source module: Executives.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/OwnershipAndControl
// Structural note: split from FiBO_BE_OwnershipAndControl umbrella.
// ===================================================================
package Executives {
  doc/* This ontology defines concepts relating to executives and their formal capacities. The concepts defined in this ontology cover types of corporate officers, board members and the like, along with the capacities in terms of which those party roles are defined, and the kinds of entity (principally natural persons) that are able to perform in those roles. (source: dct:abstract, Executives.rdf) */
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

// EntityControllingParty, ControllingParty
// InstrumentOfIncorporation

  item def <'articles of incorporation'> ArticlesOfIncorporation specializes Corporate_Bodies::InstrumentOfIncorporation {
    doc /* contract that establishes a new corporation or, when amended, adjusts the legal basis for the corporation, and outlines basic information about the corporation, including the type of business, and a description of the business' operational characteristics */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-cb;InstrumentOfIncorporation"/>
    // RDF: fibo-be-oac-exec:ArticlesOfIncorporation
  }

  item def <'auditor'> Auditor specializes AuthorizedParty {
    doc /* party qualified and authorized to review and verify the accuracy of financial records and ensure that companies comply with tax laws */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;AuthorizedParty"/>
    // RDF: fibo-be-oac-exec:Auditor
  }

  item def Authorization {
    // RDF: fibo-be-oac-exec:Authorization
  }

  item def <'authorized individual'> AuthorizedIndividual specializes ResponsibleParty {
    doc /* role of an individual that has been given the responsibility to act on behalf of another party under some set of guidelines */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;ResponsibleParty"/>
    // RDF: fibo-be-oac-exec:AuthorizedIndividual
  }

  item def AuthorizedParty {
    // RDF: fibo-be-oac-exec:AuthorizedParty
  }

  item def AuthorizingParty {
    // RDF: fibo-be-oac-exec:AuthorizingParty
  }

  item def <'board capacity'> BoardCapacity specializes Legal_Capacity::DelegatedLegalAuthority {
    doc /* authority to act in a fiduciary capacity with respect to the organization, including but not limited to determining and executing corporate policy */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;DelegatedLegalAuthority"/>
    // RDF: fibo-be-oac-exec:BoardCapacity
    // subClassOf: DelegatedLegalAuthority - from FIBO_FND_Law
  }

  item def <'board composition control'> BoardCompositionControl specializes Control::Control {
    doc /* situation in which a voting shareholder, entity owner, or some other party in the case of a not-for-profit organization, appoints and/or nominates someone to the board of directors of an organization for some period of time */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;Control"/>
    // RDF: fibo-be-oac-exec:BoardCompositionControl
  }

  item def <'board member'> BoardMember specializes LegallyDelegatedAuthority, ResponsibleParty, FiBO_FND_ExternalStubs::OrganizationMember{
    doc /* party that has fiduciary responsibility with respect to the organization, including but not limited to determining and executing corporate policy */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;LegallyDelegatedAuthority"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;ResponsibleParty"/>
    // RDF: fibo-be-oac-exec:BoardMember
    // subClassOf also: OrganizationMember - external CMNS
  }

  item def <'board membership'> BoardMembership specializes Control::Control {
    doc /* situation relating an individual member of the board of directors to the organization */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;Control"/>
    // RDF: fibo-be-oac-exec:BoardMembership
  }

  item def <'board of directors'> BoardOfDirectors specializes Control_Parties::EntityControllingParty {
    doc /* group of people comprising the governing body of an organization that has the authority to set organizational strategy and policies as well as to select and, to some degree manage, leadership */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;EntityControllingParty"/>
    // R9 addendum (approved role-chain flattening): materialized from paired
    // hasMember/isMemberOf role-chain evidence in Executives.rdf.
    item boardMember: BoardMember [0..*];
    // RDF: fibo-be-oac-exec:BoardOfDirectors
  }

  item def <'bylaws'> Bylaws specializes Legal_Core::Law {
    doc /* written rules for conduct of a corporation, association, partnership or any organization */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-cor;Law"/>
    // RDF: fibo-be-oac-exec:Bylaws
    // subClassOf: Law - from FIBO_FND_Law
  }

  item def <'chief executive officer'> ChiefExecutiveOfficer specializes CorporateOfficer, ExecutiveBoardMember, PrincipalParty {
    doc /* top corporate officer responsible for an organization's overall operations and performance */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;CorporateOfficer"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;ExecutiveBoardMember"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;PrincipalParty"/>
    // RDF: fibo-be-oac-exec:ChiefExecutiveOfficer
  }

  item def <'chief financial officer'> ChiefFinancialOfficer specializes CorporateOfficer {
    doc /* senior-most corporate officer responsible for financial control and planning for an organization or project */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;CorporateOfficer"/>
    // RDF: fibo-be-oac-exec:ChiefFinancialOfficer
  }

  item def <'company law'> CompanyLaw specializes Legal_Core::StatuteLaw {
    doc /* legislation under which the formation, registration or incorporation, governance, and dissolution of a firm is administered and controlled */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-cor;StatuteLaw"/>
    // RDF: fibo-be-oac-exec:CompanyLaw
    // subClassOf: StatuteLaw - from FIBO_FND_Law
  }

  item def <'company secretary'> CompanySecretary specializes CorporateOfficer {
    doc /* corporate officer appointed by the directors of an organization, responsible for ensuring compliance with legal obligations related to corporate governance */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;CorporateOfficer"/>
    // RDF: fibo-be-oac-exec:CompanySecretary
  }

  item def <'controlling nominee'> ControllingNominee specializes LegallyDelegatedAuthority {
    doc /* party that holds legal title to an asset (e.g., stocks, bonds, or property) on behalf of the beneficial owner */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;LegallyDelegatedAuthority"/>
    // RDF: fibo-be-oac-exec:ControllingNominee
  }

  item def <'corporate bylaws'> CorporateBylaws specializes Bylaws {
    doc /* written rules for conduct of a corporation, adopted by the board of directors */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;Bylaws"/>
    // RDF: fibo-be-oac-exec:CorporateBylaws
  }

  item def <'corporate officer'> CorporateOfficer specializes Executive, Signatory, FiBO_FND_ExternalStubs::OrganizationMember{
    doc /* high-level management executive of a corporation or other organization, hired by the board of directors or the business owner(s), charged with certain operational responsibilities, and who has the authority to act on behalf of the organization, including the authority to enter into contracts on behalf of the organization */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;Executive"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;Signatory"/>
    // RDF: fibo-be-oac-exec:CorporateOfficer
    // subClassOf also: OrganizationMember - external CMNS
  }

  item def <'executive'> Executive specializes LegallyDelegatedAuthority {
    doc /* person appointed and given the responsibility to manage the affairs of an organization and the authority to make decisions within specified role-specific boundaries */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;LegallyDelegatedAuthority"/>
    // RDF: fibo-be-oac-exec:Executive
  }

  item def <'executive board member'> ExecutiveBoardMember specializes BoardMember, Formal_Organizations::Employee{
    doc /* member of a board of directors that is also an employee of the organization */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-org-fm;Employee"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;BoardMember"/>
    // RDF: fibo-be-oac-exec:ExecutiveBoardMember
    // subClassOf also: Employee - external CMNS
  }

  item def <'external auditor'> ExternalAuditor specializes Auditor {
    doc /* independent party qualified and authorized to examine and report on the accuracy of financial records and ensure that companies comply with tax laws */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;Auditor"/>
    // RDF: fibo-be-oac-exec:ExternalAuditor
  }

  item def <'internal auditor'> InternalAuditor specializes Auditor, Formal_Organizations::Employee{
    doc /* employee who is qualified and authorized to review and verify the accuracy of financial records and evaluate internal controls and compliance */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-org-fm;Employee"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;Auditor"/>
    // RDF: fibo-be-oac-exec:InternalAuditor
    // subClassOf also: Employee - external CMNS
  }

  item def LegallyDelegatedAuthority specializes Control::ControllingParty {
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;ControllingParty"/>
  	doc /* member of the board of directors of an organization that has no executive responsibilities towards the running of that organization */   	// skos:definition xml:lang="en" (Executives.rdf)
  	// rdfs:label xml:lang="en" (Executives.rdf)
    // RDF: fibo-be-oac-exec:LegallyDelegatedAuthority
  }

  item def <'non-executive board member'> NonExecutiveBoardMember specializes BoardMember {
    doc /* member of the board of directors of an organization that has no executive responsibilities towards the running of that organization */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;BoardMember"/>
    // RDF: fibo-be-oac-exec:NonExecutiveBoardMember
  }

  item def <'principal party'> PrincipalParty specializes Signatory, FiBO_FND_ExternalStubs::OrganizationMember {
    doc /* controlling party that is responsible for the management of daily business operations of an organization */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;Signatory"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
    // RDF: fibo-be-oac-exec:PrincipalParty
  }

  item def ResponsibleParty {
    // RDF: fibo-be-oac-exec:ResponsibleParty
  }

  item def <'signatory'> Signatory specializes AuthorizedIndividual, LegallyDelegatedAuthority {
    doc /* responsible party authorized to sign agreements on behalf of themselves, another person, or an organization */
    // skos:definition xml:lang="en" (Executives.rdf)
    // rdfs:label xml:lang="en" (Executives.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;AuthorizedIndividual"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;LegallyDelegatedAuthority"/>
    // RDF: fibo-be-oac-exec:Signatory
  }
}

// --- OwnershipParties.rdf ---
