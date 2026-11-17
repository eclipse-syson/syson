// ===================================================================
// FormalOrganizations.sysml
// Source-parity extracted from: FiBO_FND_Organizations.sysml
// Primary source module: FormalOrganizations.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Organizations
// ===================================================================

package Formal_Organizations {
  doc/* This ontology extends the Commons concept of a formal organization, which is purposefully underspecified to facilitate mapping to other organization ontologies, such as the W3C organization ontology, or others defined for specific business and financial services standards. It also defines general concepts related to employment by a formal organization. (source: dct:abstract, FormalOrganizations.rdf) */
	doc
	/* This ontology extends the Commons concept of a formal organization, which is purposefully underspecified to facilitate mapping to other organization ontologies, such as the W3C organization ontology, or others defined for specific business and financial services standards. It also defines general concepts related to employment by a formal organization.
	 * (source: dct:abstract, FormalOrganizations.rdf)
	 */
	comment license
	/* Copyright (c) 2013-2025 EDM Council, Inc.
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
	 * See https://opensource.org/licenses/MIT.
	 */
	
	item def <employee> Employee specializes FiBO_FND_ExternalStubs::OrganizationMember {
		doc /* person in the service of another under any contract of hire, express or implied, oral or written, where the employer has the right to control and direct that person in the material details of how the work is to be performed */
		// skos:definition xml:lang="en" (FormalOrganizations.rdf)
		// rdfs:label xml:lang="en" (FormalOrganizations.rdf)
		// RDF: fibo-fnd-org-fm:Employee
		// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
	}
	
	item def <employer> Employer specializes FiBO_FND_ExternalStubs::MemberBearingOrganization {
		doc /* party that provides compensation, including wages or a salary and potentially other benefits, in exchange for work performed by one or more people, and that has the right to control and direct the employee in the material details of how the work is to be performed */
		// skos:definition xml:lang="en" (FormalOrganizations.rdf)
		// rdfs:label xml:lang="en" (FormalOrganizations.rdf)
		// RDF: fibo-fnd-org-fm:Employer
		// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;MemberBearingOrganization"/>
	}
	
	item def Employment specializes FiBO_FND_ExternalStubs::Membership {
		doc /* situation representing the state of being employed, i.e., the relationship that holds between an employer and employee for some period of time */
		// skos:definition xml:lang="en" (FormalOrganizations.rdf)
		// rdfs:label xml:lang="en" (FormalOrganizations.rdf)
		// RDF: fibo-fnd-org-fm:Employment
		// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;Membership"/>
	}
	
	item def <group> Group specializes FiBO_FND_ExternalStubs::Collection {
		doc /* collection of agents (people, organizations, software agents, etc.) that are considered as a unit */
		// skos:definition xml:lang="en" (FormalOrganizations.rdf)
		// rdfs:label xml:lang="en" (FormalOrganizations.rdf)
		// RDF: fibo-fnd-org-fm:Group
		// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Collection"/>
	}
}
