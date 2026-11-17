// ===================================================================
// Corporations.sysml
// Source-parity extracted from: FiBO_BE_Corporation.sysml
// Primary source modules: Corporations.rdf; Corporations.rdf (for current translated content)
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/Corporations
// Structural note: paired split with CorporateBodies.sysml from the same source umbrella.
// ===================================================================
package Corporations {
  doc/* This ontology originally defined the fundamental concepts for companies incorporated by the issuance of shares. Terms defined in this ontology are those which are applicable to all such entities. Many of these concepts form the basis of the relationships of ownership and control which obtain between entities of this type. (source: dct:abstract, Corporations.rdf) */
  // DEPRECATION_SCOPE: source_namespace
  // DEPRECATED_SOURCE: Corporations.rdf
  // DEPRECATED: true
  // DEPRECATION_POLICY: compatibility_shim
  // MIGRATION_GUIDE: prefer canonical ownership in Corporate_Bodies and related active packages for new modeling.
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
  item def <'board agreement'> BoardAgreement specializes Legal_Persons::OrganizationCoveringAgreement {
    doc /* formal, legally binding agreement between members of the Board of Directors of the organization */
    // skos:definition xml:lang="en" (Corporations.rdf)
    // rdfs:label xml:lang="en" (Corporations.rdf)
    // RDF: fibo-be-le-cb:BoardAgreement
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-fbo;OrganizationCoveringAgreement"/>
  }

  item def <'joint stock company'> JointStockCompany specializes Legal_Persons::BusinessEntity {
    doc /* for-profit, unincorporated business that has some characteristics of a corporation and some features of a partnership, with ownership interests represented by shares of stock */
    // skos:definition xml:lang="en" (Corporations.rdf)
    // rdfs:label xml:lang="en" (Corporations.rdf)
    // RDF: fibo-be-le-cb:JointStockCompany
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-lp;BusinessEntity"/>
  }

  item def <'privately held company'> PrivatelyHeldCompany specializes Corporate_Bodies::StockCorporation {
    doc /* corporation whose issued shares are all held by a family or a small group of investors and, therefore, cannot be bought by the public */
    // DEPRECATED: true
    // REPLACED_BY: Corporate_Bodies::StockCorporation
    // skos:definition xml:lang="en" (Corporations.rdf)
    // rdfs:label xml:lang="en" (Corporations.rdf)
    // RDF: fibo-be-le-cb:PrivatelyHeldCompany
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-cb;StockCorporation"/>
  }

  item def <'publicly held company'> PubliclyHeldCompany specializes Corporate_Bodies::StockCorporation {
    doc /* corporation whose shares are traded and held publicly */
    // DEPRECATED: true
    // REPLACED_BY: Corporate_Bodies::StockCorporation
    // skos:definition xml:lang="en" (Corporations.rdf)
    // rdfs:label xml:lang="en" (Corporations.rdf)
    // RDF: fibo-be-le-cb:PubliclyHeldCompany
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-cb;StockCorporation"/>
  }

  item def <'registration identifier'> RegistrationIdentifier specializes Functional_Entities::OrganizationIdentifier {
    doc /* identifier that is officially allocated to an organization at the time of registration, typically in a jurisdiction in which said organization is organized or registered and used in that jurisdiction to identify the organization */
    // skos:definition xml:lang="en" (Corporations.rdf)
    // rdfs:label xml:lang="en" (Corporations.rdf)
    // RDF: fibo-be-le-cb:RegistrationIdentifier
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationIdentifier"/>
  }

  item def <'registration identifier scheme'> RegistrationIdentifierScheme specializes FiBO_FND_ExternalStubs::OrganizationIdentificationScheme {
    doc /* scheme that defines the registration identifier per the issuing registration authority */
    // skos:definition xml:lang="en" (Corporations.rdf)
    // rdfs:label xml:lang="en" (Corporations.rdf)
    // RDF: fibo-be-le-cb:RegistrationIdentifierScheme
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationIdentificationScheme"/>
    // subClassOf: OrganizationIdentificationScheme - external CMNS
  }

  item def <'religious corporation'> ReligiousCorporation specializes Corporate_Bodies::NotForProfitCorporation {
    doc /* not-for-profit corporation whose objective is specific to some fundamental set of beliefs and practices generally agreed upon by a number of people, and that is incorporated under the law */
    // skos:definition xml:lang="en" (Corporations.rdf)
    // rdfs:label xml:lang="en" (Corporations.rdf)
    // RDF: fibo-be-le-cb:ReligiousCorporation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-cb;NotForProfitCorporation"/>
  }

}
