// ===================================================================
// Agreements.sysml
// Source-parity extracted from: FiBO_FND_Agreements.sysml
// Primary source module: Agreements.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Agreements
// ===================================================================

package Agreements {
  doc/* This ontology defines the concept of an agreement and roles that parties to an agreement play in the context of financial agreements. Agreements represent an understanding between parties, whereas contracts typically formalize such agreements. (source: dct:abstract, Agreements.rdf) */
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

    item def <'agreement'> Agreement specializes FiBO_FND_ExternalStubs::Situation {
    	doc /* negotiated understanding between two or more parties, reflecting the offer and acceptance of commitments on the part of either party */
    	// skos:definition xml:lang="en" (Agreements.rdf)
		// rdfs:label xml:lang="en" (Agreements.rdf)
    	// RDF: fibo-fnd-agr-agr:Agreement
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Situation"/>
    }

    item def <'beneficiary'> Beneficiary specializes Parties::PartyRole {
    	doc /* party that receives some benefit or advantage or profits from something */
    	// skos:definition xml:lang="en" (Agreements.rdf)
    	// rdfs:label xml:lang="en" (Agreements.rdf)
    	// RDF: fibo-fnd-agr-agr:Beneficiary
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
    }

    item def <'bilateral agreement'> BilateralAgreement specializes Agreements::Agreement {
    	doc /* agreement where two parties commit to perform specific actions or obligations towards each other */
    	// skos:definition xml:lang="en" (Agreements.rdf)
		// rdfs:label xml:lang="en" (Agreements.rdf)
    	// RDF: fibo-fnd-agr-agr:BilateralAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Agreement"/>
    }

    item def <'commitment'> Commitment specializes FiBO_FND_ExternalStubs::Situation {
    	doc /* promise made by some party to act or refrain from acting in some manner */
    	// skos:definition xml:lang="en" (Agreements.rdf)
    	// rdfs:label xml:lang="en" (Agreements.rdf)
    	// RDF: fibo-fnd-agr-agr:Commitment
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Situation"/>
    }

    item def <'commitment at large'> CommitmentAtLarge specializes UnilateralCommitment {
    	doc /* commitment made by some party without direct involvement from the potential beneficiaries of that commitment */
    	// skos:definition xml:lang="en" (Agreements.rdf)
    	// rdfs:label xml:lang="en" (Agreements.rdf)
    	// RDF: fibo-fnd-agr-agr:CommitmentAtLarge
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;UnilateralCommitment"/>
    }

    item def IndividualUnilateralCommitment { // RDF: fibo-fnd-agr-agr:IndividualUnilateralCommitment
      // OWL-deprecated: true
      // OWL-equivalentClass: <owl:equivalentClass rdf:resource="&fibo-fnd-agr-agr;UnilateralCommitment"/>
      // RDF: fibo-fnd-agr-agr:IndividualUnilateralCommitment
    }

    item def <'multilateral agreement'> MultilateralAgreement specializes Agreements::Agreement {
    	doc /* agreements that involve or include multiple parties */
    	// skos:definition xml:lang="en" (Agreements.rdf)
		// rdfs:label xml:lang="en" (Agreements.rdf)
    	// RDF: fibo-fnd-agr-agr:MultilateralAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Agreement"/>
    }

    item def MutualAgreement { // RDF: fibo-fnd-agr-agr:MutualAgreement
      // OWL-deprecated: true
      // OWL-equivalentClass: <owl:equivalentClass rdf:resource="&fibo-fnd-agr-agr;Agreement"/>
      // RDF: fibo-fnd-agr-agr:MutualAgreement
    }

    item def MutualCommitment { // RDF: fibo-fnd-agr-agr:MutualCommitment
      // OWL-deprecated: true
      // OWL-equivalentClass: <owl:equivalentClass rdf:resource="&fibo-fnd-agr-agr;Commitment"/>
      // RDF: fibo-fnd-agr-agr:MutualCommitment
    }

    item def <'obligee'> Obligee specializes Parties::PartyRole {
    	doc /* party to whom some commitment or obligation is owed, either legally or per the terms of an agreement */
    	// skos:definition xml:lang="en" (Agreements.rdf)
    	// rdfs:label xml:lang="en" (Agreements.rdf)
    	// RDF: fibo-fnd-agr-agr:Obligee
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
    }

    item def <'obligor'> Obligor specializes Parties::PartyRole {
    doc /* party that is bound legally or by agreement to repay a debt, make a payment, do something, or refrain from doing something */
    // skos:definition xml:lang="en" (Agreements.rdf)
    	// rdfs:label xml:lang="en" (Agreements.rdf)
    	// RDF: fibo-fnd-agr-agr:Obligor
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
    }

    item def <'unilateral commitment'> UnilateralCommitment specializes Commitment {
    	doc /* commitment (promise or obligation) made by one party without requiring a reciprocal promise from another party */
    	// skos:definition xml:lang="en" (Agreements.rdf)
    	// rdfs:label xml:lang="en" (Agreements.rdf)
    	// RDF: fibo-fnd-agr-agr:UnilateralCommitment
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Commitment"/>
    }

}
