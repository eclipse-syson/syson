// ===================================================================
// LegalCapacity.sysml
// Source-parity extracted from: FiBO_FND_Law.sysml
// Primary source module: LegalCapacity.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Law
// ===================================================================

package Legal_Capacity {
  doc/* This ontology defines high-level legal concepts related to legal responsibilities. The ontology defines things which are conferred upon some entity by some legal instrument, and elaborates this into a number of specific capacities, responsibilities and powers, each of which forms the basis for many of the concepts used elsewhere in FIBO in defining legal personhood, executive powers and the like. (source: dct:abstract, LegalCapacity.rdf) */
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

    item def <'contractual interest'> ContractualInterest specializes Legal_Capacity::ContractualRight {
    	doc /* legally enforceable benefit or entitlement arising from a contract, agreement, or instrument, in which an entity holds specified rights or obligations related to the performance, use, or benefit of something, without necessarily holding ownership */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:ContractualInterest
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;ContractualRight"/>
    }

    item def <'contractual obligation'> ContractualObligation specializes Legal_Capacity::LegalObligation {
    	doc /* demand or assertion made by one party on another, based on facts that, taken together, give rise to a legally enforceable right or judicial action */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:ContractualObligation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalObligation"/>
    }

    item def <'contractual option'> ContractualOption specializes Legal_Capacity::ContractualRight {
    	doc /* contractual right that may be exercised at some point in the future, such as an option to extend a contract, or other available but not obligatory rights as defined in the contract */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:ContractualOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;ContractualRight"/>
    }

    item def <'contractual right'> ContractualRight specializes Right {
    	doc /* legally enforceable benefit or entitlement granted to a party within a binding agreement */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:ContractualRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Right"/>
    }

    item def <'delegated legal authority'> DelegatedLegalAuthority specializes Legal_Capacity::LegalCapacity {
    	doc /* institutionalized and legal power inherent in a particular job, function, or position that is meant to enable its holder to successfully carry out his or her responsibilities, where such power has been delegated through some formal means */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:DelegatedLegalAuthority
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalCapacity"/>
    }

    item def <'delivery obligation'> DeliveryObligation specializes Legal_Capacity::ContingentObligation, Legal_Capacity::LegalObligation {
    	doc /* obligation to deliver something in order to satisfy a claim or debt */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:DeliveryObligation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;ContingentObligation"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalObligation"/>
    }

    item def <'duty'> Duty specializes LegalConstruct {
    	doc /* requirement for one party to formally inform another party (or parties) about specific events, actions, or changes as outlined in the agreement */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:Duty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalConstruct"/>
    }

    item def <'legal capacity'> LegalCapacity specializes LegalConstruct {
    	doc /* the capability to carry out certain actions or to have certain rights together with the resources to do so */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:LegalCapacity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalConstruct"/>
    }

    item def <'legal right'> LegalRight specializes Right {
    	doc /* personal right, privilege, or benefit that a government, contract, or law provides or protects, making an individual or entity eligible to receive something */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:LegalRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Right"/>
    }

    item def <'license identifier'> LicenseIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
    	doc /* an identifier associated with a license */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:LicenseIdentifier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
    }

    item def <'licensee'> Licensee specializes Parties::PartyRole {
    	doc /* a party to whom a license has been granted */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:Licensee
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
    }

    item def <'licensor'> Licensor specializes Parties::PartyRole {
    	doc /* a party who grants a license */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:Licensor
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
    }

    item def <'litigation capacity'> LitigationCapacity specializes Legal_Capacity::LegalCapacity {
    	doc /* the legal capacity to pursue a litigation action in law */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:LitigationCapacity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalCapacity"/>
    }

    item def <'regulation'> Regulation specializes LegalConstruct {
    	doc /* an obligation or duty that is enforceable by a court */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:Regulation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalConstruct"/>
    }

    item def <'reporting policy'> ReportingPolicy specializes Policy {
    	doc /* policy specifying principles, rules and/or guidelines regarding some aspect of reporting */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:ReportingPolicy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Policy"/>
    }

    item def <'right'> Right specializes LegalConstruct {
    	doc /* entitlement to perform certain actions (or not), or to be in certain states (or not); or entitlement that requires others to perform certain actions or be in certain states (or not) */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:Right
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalConstruct"/>
    }

    item def <'signatory capacity'> SignatoryCapacity specializes Legal_Capacity::LegalCapacity {
    	doc /* the capacity of some natural person to sign agreements on the part of some organization or legal person */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    	// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:SignatoryCapacity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalCapacity"/>
    }

    item def Claim specializes LegalConstruct {
    	doc /* demand or assertion made by one party on another, based on facts that, taken together, give rise to a legally enforceable right or judicial action */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    // rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:Claim
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalConstruct"/>
    }

    item def ContingentObligation specializes Legal_Capacity::Duty {
    	doc /* obligation that depends on a future event or the performance of an action */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    // rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:ContingentObligation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Duty"/>
    }

    item def ContingentRight specializes Right {
    	doc /* right that depends on a future event or the performance of an action */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    // rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:ContingentRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Right"/>
    }

    item def ContractualCapability specializes Legal_Capacity::LegalCapacity {
    	doc /* the capacity to enter into legally binding contracts */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    // rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:ContractualCapability
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalCapacity"/>
    }

    item def <'legal construct'> LegalConstruct {
		doc /* something which is conferred by way of law or contract, such as a right */
		// skos:definition xml:lang="en" (LegalCapacity.rdf)
		// rdfs:label xml:lang="en" (LegalCapacity.rdf)
      // RDF: fibo-fnd-law-lcap:LegalConstruct
    }

    item def LegalObligation specializes Legal_Capacity::Duty {
    	doc /* an obligation or duty that is enforceable by a court */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    // rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:LegalObligation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Duty"/>
    }

    item def LiabilityCapacity specializes Legal_Capacity::LegalCapacity {
    	doc /* the ability to be sued at law */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    // rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:LiabilityCapacity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalCapacity"/>
    }

    item def License specializes FiBO_FND_ExternalStubs::LegalDocument {
    	doc /* grant of permission needed to do something */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
    // rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:License
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;LegalDocument"/>
    }

    item def NotificationObligation specializes Legal_Capacity::ContingentObligation, Legal_Capacity::LegalObligation {
    	doc /* requirement for one party to formally inform another party (or parties) about specific events, actions, or changes as outlined in the agreement */
    	// skos:definition xml:lang="en" (LegalCapacity.rdf)
		// rdfs:label xml:lang="en" (LegalCapacity.rdf)
    	// RDF: fibo-fnd-law-lcap:NotificationObligation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;ContingentObligation"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalObligation"/>
    }

    item def <'policy'> Policy {
		doc /* system of principles, rules and guidelines, adopted by an organization to guide decision making with respect to particular situations and implemented via procedures or protocols to achieve stated goals */
		// skos:definition xml:lang="en" (LegalCapacity.rdf)
		// rdfs:label xml:lang="en" (LegalCapacity.rdf)
		// RDF: fibo-fnd-law-lcap:Policy
    }
}
