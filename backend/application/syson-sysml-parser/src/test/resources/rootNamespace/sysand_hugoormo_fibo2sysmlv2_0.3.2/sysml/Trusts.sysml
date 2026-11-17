// ===================================================================
// Trusts.sysml
// Source-parity extracted from: FiBO_BE_Trusts.sysml
// Primary source module: Trusts.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/Trusts
// Structural note: translated as a single-file package from its source umbrella.
// ===================================================================
package Trusts {
  doc/* This ontology defines the fundamental common terms for trusts. Trusts are entities set up in terms of the applicable local statutes goerning trusts, and have as a minimum three specific, defined parties, known in many jurisdictions as trustor (sometimes sponsor), trustee and beneficiary. The terms in this ontology may be extended as necessary to represent specific types of trust, for example in the funds arena. (source: dct:abstract, Trusts.rdf) */
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
// LegallyDelegatedAuthority
// Beneficiary

  item def <'irrevocable trust'> IrrevocableTrust specializes Trust {
    doc /* trust that cannot be modified, amended or terminated except under certain legal circumstances and typically not without the permission of the grantor's named beneficiary or beneficiaries */
    // skos:definition xml:lang="en" (Trusts.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-tr-tr;Trust"/>
  	// rdfs:label xml:lang="en" (Trusts.rdf)
    // RDF: fibo-be-tr-tr:IrrevocableTrust
  }

  item def <'living trust'> LivingTrust specializes Trust {
    doc /* trust created during an individual's lifetime where a designated person, the trustee, is given responsibility for managing that individual's assets for the benefit of the eventual beneficiary */
    // skos:definition xml:lang="en" (Trusts.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-tr-tr;Trust"/>
  	// rdfs:label xml:lang="en" (Trusts.rdf)
    // RDF: fibo-be-tr-tr:LivingTrust
  }

  item def <'revocable trust'> RevocableTrust specializes Trust {
    doc /* trust in which legal ownership of the trust property is transferred to the trustee, but the trustor retains full power to revoke, modify or amend the trust */
    // skos:definition xml:lang="en" (Trusts.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-tr-tr;Trust"/>
  	// rdfs:label xml:lang="en" (Trusts.rdf)
    // RDF: fibo-be-tr-tr:RevocableTrust
  }

  item def <'testamentary trust'> TestamentaryTrust specializes IrrevocableTrust {
    doc /* trust established in accordance with the instructions contained in a last will and testament */
    // skos:definition xml:lang="en" (Trusts.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-tr-tr;IrrevocableTrust"/>
  	// rdfs:label xml:lang="en" (Trusts.rdf)
    // RDF: fibo-be-tr-tr:TestamentaryTrust
  }

  item def <'trust'> Trust specializes Legal_Persons::BusinessEntity, FiBO_FND_ExternalStubs::LegalEntity {
    doc /* fiduciary relationship and legal entity in which one party, known as a trustor, gives another party, the trustee, the right to hold title to and manage assets for the benefit of a third party, the beneficiary */
    // skos:definition xml:lang="en" (Trusts.rdf)
    // rdfs:label xml:lang="en" (Trusts.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-lp;BusinessEntity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalEntity"/>
    // RDF: fibo-be-tr-tr:Trust
  }

  item def <'trust agreement'> TrustAgreement specializes Legal_Persons::OrganizationCoveringAgreement {
    doc /* formal agreement that establishes a trust, whereby the trustor(s) gives the trustee(s) the responsibility to hold and manage assets for the beneficiary(ies) */
    // skos:definition xml:lang="en" (Trusts.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-fbo;OrganizationCoveringAgreement"/>
  	// rdfs:label xml:lang="en" (Trusts.rdf)
    // RDF: fibo-be-tr-tr:TrustAgreement
  }

  item def <'trust beneficiary'> TrustBeneficiary specializes Agreements::Beneficiary {
    doc /* party for whose interest (benefit) an annuity, assignment (such as a letter of credit), contract, insurance policy, judgment, promise, trust, will, etc., is made */
    // skos:definition xml:lang="en" (Trusts.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Beneficiary"/>
  	// rdfs:label xml:lang="en" (Trusts.rdf)
    // RDF: fibo-be-tr-tr:TrustBeneficiary
  }

  item def <'trustee'> Trustee specializes Executives::LegallyDelegatedAuthority, FiBO_FND_ExternalStubs::OrganizationMember{
    doc /* party that holds and manages assets for the benefit of another */
    // skos:definition xml:lang="en" (Trusts.rdf)
    // rdfs:label xml:lang="en" (Trusts.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;LegallyDelegatedAuthority"/>
    // RDF: fibo-be-tr-tr:Trustee
    // subClassOf also: OrganizationMember - external CMNS
  }

  item def <'trust fund manager'> TrustFundManager specializes Executives::LegallyDelegatedAuthority {
    doc /* party empowered to act on behalf of the trustee to manage the assets of the trust */
    // skos:definition xml:lang="en" (Trusts.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;LegallyDelegatedAuthority"/>
  	// rdfs:label xml:lang="en" (Trusts.rdf)
    // RDF: fibo-be-tr-tr:TrustFundManager
  }

  item def <'trustor'> Trustor specializes FiBO_FND_ExternalStubs::OrganizationMember {
    doc /* party that establishes a trust and places property under the protection and management of one or more trustees for the benefit of at least one beneficiary */
    // skos:definition xml:lang="en" (Trusts.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
  	// rdfs:label xml:lang="en" (Trusts.rdf)
    // RDF: fibo-be-tr-tr:Trustor
    // subClassOf: OrganizationMember - external CMNS
  }
}
