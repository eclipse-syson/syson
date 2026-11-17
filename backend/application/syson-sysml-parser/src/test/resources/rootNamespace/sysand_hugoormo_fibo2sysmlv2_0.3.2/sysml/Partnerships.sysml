// ===================================================================
// Partnerships.sysml
// Source-parity extracted from: FiBO_BE_Partnerships.sysml
// Primary source module: Partnerships.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/Partnerships
// Structural note: translated as a single-file package from its source umbrella.
// ===================================================================
package Partnerships {
  doc/* This ontology defines partnerships and related concepts. The concepts distinguish general from limited partners, as well as the types of equity that they hold. Included are abstract definitions of partnership types based on whether they have general, limited or both kinds of partners. Both legally incorporated and non incorporated forms of partnerships are covered. (source: dct:abstract, Partnerships.rdf) */
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

// BusinessEntity, LegalEntity, OrganizationCoveringAgreement
// ConstitutionalOwner
// DeJureControllingInterestParty

  item def <'general partner'> GeneralPartner specializes Partner, Control_Parties::DeJureControllingInterestParty {
    doc /* partner and part-owner that is responsible for managing the day to day operations of the partnership and that may be jointly and severally liable for the obligations of the partnership */
    // skos:definition xml:lang="en" (Partnerships.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ptr-ptr;Partner"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;DeJureControllingInterestParty"/>
  	// rdfs:label xml:lang="en" (Partnerships.rdf)
    // RDF: fibo-be-ptr-ptr:GeneralPartner
  }

  item def <'general partnership'> GeneralPartnership specializes Partnership {
    doc /* partnership that has at least two general partners that agree to share in all assets, profits, and financial and legal liabilities of the business */
    // skos:definition xml:lang="en" (Partnerships.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ptr-ptr;Partnership"/>
    // R2/R6 trace-only: hasMember->playsRole->GeneralPartner(min 2) role-chain evidence retained from Partnerships.rdf.
  	// rdfs:label xml:lang="en" (Partnerships.rdf)
    // RDF: fibo-be-ptr-ptr:GeneralPartnership
  }

  item def <'limited liability limited partnership'> LimitedLiabilityLimitedPartnership specializes LimitedPartnership {
    doc /* limited partnership that consists of one or more general partners who are liable for the obligations of the entity as well as one or more protected limited liability partners */
    // skos:definition xml:lang="en" (Partnerships.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ptr-ptr;LimitedPartnership"/>
  	// rdfs:label xml:lang="en" (Partnerships.rdf)
    // RDF: fibo-be-ptr-ptr:LimitedLiabilityLimitedPartnership
  }

  item def <'limited liability partnership'> LimitedLiabilityPartnership specializes Partnership {
    doc /* partnership that has general partners but provides its individual partners some level of protection against personal liability for certain partnership liabilities */
    // skos:definition xml:lang="en" (Partnerships.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ptr-ptr;Partnership"/>
  	// rdfs:label xml:lang="en" (Partnerships.rdf)
    // RDF: fibo-be-ptr-ptr:LimitedLiabilityPartnership
  }

  item def <'limited partner'> LimitedPartner specializes Partner {
    doc /* partner whose liabilities are limited to the extent of their investment or guarantees and that has no involvement in the day to day operations of the partnership */
    // skos:definition xml:lang="en" (Partnerships.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ptr-ptr;Partner"/>
  	// rdfs:label xml:lang="en" (Partnerships.rdf)
    // RDF: fibo-be-ptr-ptr:LimitedPartner
  }

  item def <'limited partnership'> LimitedPartnership specializes Partnership {
    doc /* partnership that has at least one general partner and at least one limited partner */
    // skos:definition xml:lang="en" (Partnerships.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ptr-ptr;Partnership"/>
  	// rdfs:label xml:lang="en" (Partnerships.rdf)
    // RDF: fibo-be-ptr-ptr:LimitedPartnership
  }

  item def <'partner'> Partner specializes Ownership_Parties::ConstitutionalOwner, FiBO_FND_ExternalStubs::OrganizationMember{
    doc /* co-owner, member, and agent of a partnership whose participation level, including proportional liabilities and share in the profit / loss of the business is specified in a partnership agreement */
    // skos:definition xml:lang="en" (Partnerships.rdf)
    // rdfs:label xml:lang="en" (Partnerships.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;ConstitutionalOwner"/>
    // RDF: fibo-be-ptr-ptr:Partner
    // subClassOf also: OrganizationMember - external CMNS
  }

  item def <'partnership'> Partnership specializes Legal_Persons::BusinessEntity, FiBO_FND_ExternalStubs::LegalEntity {
    doc /* association of two or more legal persons to carry on as co-owners a business for profit */
    // skos:definition xml:lang="en" (Partnerships.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-lp;BusinessEntity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalEntity"/>
    // R9 addendum (approved role-chain flattening): materialized from paired
    // hasMember/isMemberOf role-chain evidence in Partnerships.rdf.
    item partner: Partner [0..*];
  	// rdfs:label xml:lang="en" (Partnerships.rdf)
    // RDF: fibo-be-ptr-ptr:Partnership
  }

  item def <'partnership agreement'> PartnershipAgreement specializes Legal_Persons::OrganizationCoveringAgreement {
    doc /* contract between partners in a partnership that establishes the terms and conditions of the relationship between the partners */
    // skos:definition xml:lang="en" (Partnerships.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-fbo;OrganizationCoveringAgreement"/>
  	// rdfs:label xml:lang="en" (Partnerships.rdf)
    // RDF: fibo-be-ptr-ptr:PartnershipAgreement
  }
}
