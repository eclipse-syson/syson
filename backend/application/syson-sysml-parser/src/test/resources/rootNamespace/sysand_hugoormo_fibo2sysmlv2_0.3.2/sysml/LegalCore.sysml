// ===================================================================
// LegalCore.sysml
// Source-parity extracted from: FiBO_FND_Law.sysml
// Primary source module: LegalCore.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Law
// ===================================================================

package Legal_Core {
  doc/* This ontology defines high-level legal concepts for use in other FIBO ontology elements. These concepts include law and constitution, both of which are framed at a more abstract level than national or state laws and constitutions, so that law forms the basis both for statutes and for company by-laws, and constitution forms the basis both for national or state constitutions and for instruments which are constitutive of incorporated legal entities. This ontology also defines some of the variants of these such as governmental constitutions and ordinances. Court of Law is also defined here. (source: dct:abstract, LegalCore.rdf) */
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

    item def <'court of law'> CourtOfLaw specializes Business_Registries::FormalOrganization {
    	doc /* person or body of persons having judicial authority to hear and resolve disputes on the basis of statutes or the common law */
    	// skos:definition xml:lang="en" (LegalCore.rdf)
    	// rdfs:label xml:lang="en" (LegalCore.rdf)
    	// RDF: fibo-fnd-law-cor:CourtOfLaw
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;FormalOrganization"/>
    }

    item def <'constitution'> Constitution specializes FiBO_FND_ExternalStubs::Collection {
    doc /* set of basic principles by which an organization is governed, especially in relation to the rights of the people it governs */
    // skos:definition xml:lang="en" (LegalCore.rdf)
    // rdfs:label xml:lang="en" (LegalCore.rdf)
    	// RDF: fibo-fnd-law-cor:Constitution
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Collection"/>
    }

    item def <'statute law'> StatuteLaw specializes Law {
    doc /* law enacted by a legislature */
    // skos:definition xml:lang="en" (LegalCore.rdf)
    // rdfs:label xml:lang="en" (LegalCore.rdf)
    	// RDF: fibo-fnd-law-cor:StatuteLaw
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-cor;Law"/>
    }

    item def <'law'> Law {
    doc /* rule recognized by some community as regulating the behavior of its members and that it may enforce through the imposition of penalties */
    // skos:definition xml:lang="en" (LegalCore.rdf)
    // rdfs:label xml:lang="en" (LegalCore.rdf)
      // RDF: fibo-fnd-law-cor:Law
    }
}
