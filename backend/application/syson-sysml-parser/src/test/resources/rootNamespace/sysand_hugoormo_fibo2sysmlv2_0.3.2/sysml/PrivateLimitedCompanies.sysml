// ===================================================================
// PrivateLimitedCompanies.sysml
// Source-parity extracted from: FiBO_BE_PrivateLimitedCompanies.sysml
// Primary source module: PrivateLimitedCompanies.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/PrivateLimitedCompanies
// Structural note: translated as a single-file package from its source umbrella.
// ===================================================================
package Private_Limited_Companies {
  doc/* This ontology defines the fundamental concepts for representing private limited companies -- i.e., companies that have characteristics of corporations and of partnerships but are neither. (source: dct:abstract, PrivateLimitedCompanies.rdf) */
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

// BusinessEntity, LegalEntity
// DeJureControllingInterestParty, EntityControllingParty
// EntityOwner
// PrincipalParty

  item def <'limited liability company'> LimitedLiabilityCompany specializes PrivateCompanyWithLimitedLiability {
    doc /* private limited company that combines the pass through taxation of a sole proprietorship or partnership with the limited liability of a corporation */
    // skos:definition xml:lang="en" (PrivateLimitedCompanies.rdf)
    // rdfs:label xml:lang="en" (PrivateLimitedCompanies.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-plc-plc;PrivateCompanyWithLimitedLiability"/>
    // RDF: fibo-be-plc-plc:LimitedLiabilityCompany
  }

  item def <'limited liability company member'> LimitedLiabilityCompanyMember specializes Control_Parties::DeJureControllingInterestParty, Control_Parties::EntityControllingParty, Ownership_Parties::EntityOwner, FiBO_FND_ExternalStubs::OrganizationMember{
    doc /* owner of an interest in a limited liability company */
    // skos:definition xml:lang="en" (PrivateLimitedCompanies.rdf)
    // rdfs:label xml:lang="en" (PrivateLimitedCompanies.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;DeJureControllingInterestParty"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;EntityControllingParty"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;EntityOwner"/>
    // RDF: fibo-be-plc-plc:LimitedLiabilityCompanyMember
    // subClassOf also: OrganizationMember - external CMNS
  }

  item def <'limited liability company taxed as a corporation'> LimitedLiabilityCompanyTaxedAsACorporation specializes LimitedLiabilityCompany {
    doc /* limited liability company that has elected to have corporate tax status */
    // skos:definition xml:lang="en" (PrivateLimitedCompanies.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-plc-plc;LimitedLiabilityCompany"/>
  	// rdfs:label xml:lang="en" (PrivateLimitedCompanies.rdf)
    // RDF: fibo-be-plc-plc:LimitedLiabilityCompanyTaxedAsACorporation
  }

  item def <'manager-managed limited liability company'> ManagerManagedLimitedLiabilityCompany specializes LimitedLiabilityCompany {
    doc /* limited liability company in which the members appoint one or more managers to handle the daily operations and administrative responsibilities of the organization */
    // skos:definition xml:lang="en" (PrivateLimitedCompanies.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-plc-plc;LimitedLiabilityCompany"/>
  	// rdfs:label xml:lang="en" (PrivateLimitedCompanies.rdf)
    // RDF: fibo-be-plc-plc:ManagerManagedLimitedLiabilityCompany
  }

  item def <'managing member'> ManagingMember specializes LimitedLiabilityCompanyMember, Executives::PrincipalParty {
    doc /* owner of an interest in a limited liability company who also runs the day-to-day business operations */
    // skos:definition xml:lang="en" (PrivateLimitedCompanies.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-plc-plc;LimitedLiabilityCompanyMember"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;PrincipalParty"/>
  	// rdfs:label xml:lang="en" (PrivateLimitedCompanies.rdf)
    // RDF: fibo-be-plc-plc:ManagingMember
  }

  item def <'private company with limited liability'> PrivateCompanyWithLimitedLiability specializes Legal_Persons::BusinessEntity, FiBO_FND_ExternalStubs::LegalEntity {
    doc /* hybrid business entity having characteristics of both a corporation and a partnership or sole proprietorship (depending on how many owners there are) */
    // skos:definition xml:lang="en" (PrivateLimitedCompanies.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-lp;BusinessEntity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalEntity"/>
  	// rdfs:label xml:lang="en" (PrivateLimitedCompanies.rdf)
    // RDF: fibo-be-plc-plc:PrivateCompanyWithLimitedLiability
  }

  item def <'private limited company'> PrivateLimitedCompany specializes PrivateCompanyWithLimitedLiability {
    doc /* private limited company whose shareholders' liability is limited to the capital they originally invested */
    // skos:definition xml:lang="en" (PrivateLimitedCompanies.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-plc-plc;PrivateCompanyWithLimitedLiability"/>
  	// rdfs:label xml:lang="en" (PrivateLimitedCompanies.rdf)
    // RDF: fibo-be-plc-plc:PrivateLimitedCompany
  }
}
