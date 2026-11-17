// ===================================================================
// OwnershipParties.sysml
// Source-parity extracted from: FiBO_BE_OwnershipAndControl.sysml
// Primary source module: OwnershipParties.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/OwnershipAndControl
// Structural note: split from FiBO_BE_OwnershipAndControl umbrella.
// ===================================================================
package Ownership_Parties {
  doc/* This ontology defines concepts relating to types of organization owning parties. The concepts defined here are party in role concepts, which define the nature of some entity such as an organization or a legal person, in some role such as that of owning equity in the entity. These roles are defined in terms of the ownership enjoyed by the party, with distinctions between constitutional ownership i.e. ownership defined in terms of stockholder equity, and investment ownership more generally. (source: dct:abstract, OwnershipParties.rdf) */
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

// Control, Ownership, ControlledThing, ControllingParty, Owner

  item def <'constitutional owner'> ConstitutionalOwner specializes EntityOwner {
      doc /* entity owner that holds an equity stake in said entity, in the form of shareholders' equity */
      // skos:definition xml:lang="en" (OwnershipParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;EntityOwner"/>
  	// rdfs:label xml:lang="en" (OwnershipParties.rdf)
    // RDF: fibo-be-oac-opty:ConstitutionalOwner
  }

  item def <'controlling equity'> ControllingEquity specializes Accounting_Equity::ShareholdersEquity {
      doc /* shareholders's equity that formally confers control in the entity, either by law or as explicitly stated in a corresponding equity instrument */
      // skos:definition xml:lang="en" (OwnershipParties.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;ShareholdersEquity"/>
  	// rdfs:label xml:lang="en" (OwnershipParties.rdf)
    // RDF: fibo-be-oac-opty:ControllingEquity
  }

  item def <'direct consolidation'> DirectConsolidation specializes EntityOwnership {
      doc /* direct ownership of an entity recorded as accounting consolidation */
      // skos:definition xml:lang="en" (OwnershipParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;EntityOwnership"/>
  	// rdfs:label xml:lang="en" (OwnershipParties.rdf)
    // RDF: fibo-be-oac-opty:DirectConsolidation
  }

  item def <'entity owner'> EntityOwner specializes Ownership::Owner {
      doc /* owner (person, group of people, or other business entity) that owns, or holds an ownership interest in, a formal business organization */
      // skos:definition xml:lang="en" (OwnershipParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Owner"/>
  	// rdfs:label xml:lang="en" (Executives.rdf)
    // RDF: fibo-be-oac-opty:EntityOwner
    // subClassOf: Owner - from FIBO_FND_OwnershipAndControl
  }

  item def <'entity ownership'> EntityOwnership specializes Ownership::Ownership {
      doc /* ownership by some party of an interest in some non-governmental formal organization */
      // skos:definition xml:lang="en" (OwnershipParties.rdf)
    // rdfs:label xml:lang="en" (OwnershipParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Ownership"/>
    // RDF: fibo-be-oac-opty:EntityOwnership
    // subClassOf: Ownership - from FIBO_FND_OwnershipAndControl
  }

  item def <'foreign branch ownership'> ForeignBranchOwnership specializes EntityOwnership {
      doc /* ownership by some party of some formal organization or organizational sub-unit that is a foreign affiliate and legally part of the owning entity */
      // skos:definition xml:lang="en" (OwnershipParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;EntityOwnership"/>
  	// rdfs:label xml:lang="en" (OwnershipParties.rdf)
    // RDF: fibo-be-oac-opty:ForeignBranchOwnership
  }

  item def <'investment equity'> InvestmentEquity specializes Accounting_Equity::OwnersEquity {
      doc /* equity that represents an ownership interest in some entity, but may or may not take the form of shareholders's equity */
      // skos:definition xml:lang="en" (OwnershipParties.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;OwnersEquity"/>
  	// rdfs:label xml:lang="en" (OwnershipParties.rdf)
    // RDF: fibo-be-oac-opty:InvestmentEquity
  }

  item def <'investor'> Investor specializes EntityOwner, Contracts::ContractParty{
      doc /* party that owns some stake in some organization by way of investment */
      // skos:definition xml:lang="en" (OwnershipParties.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;EntityOwner"/>
  	// rdfs:label xml:lang="en" (OwnershipParties.rdf)
    // RDF: fibo-be-oac-opty:Investor
    // subClassOf also: ContractParty - from FIBO_FND_Agreements
  }

  item def <'investor contract'> InvestorContract specializes Contracts::WrittenContract {
      doc /* Contract setting out the terms under which some investor invests in the entity and setting out the rights which are conferred on that investor. */
      // skos:definition xml:lang="en" (OwnershipParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
  	// rdfs:label xml:lang="en" (OwnershipParties.rdf)
    // RDF: fibo-be-oac-opty:InvestorContract
    // subClassOf: WrittenContract - from FIBO_FND_Agreements
  }

  item def <'ultimate ownership'> UltimateConsolidation specializes EntityOwnership {
      doc /* highest-level (top, end) ancestral ownership, evidenced by accounting consolidation, by some party of some other legal entity */
      // skos:definition xml:lang="en" (OwnershipParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;EntityOwnership"/>
  	// rdfs:label xml:lang="en" (OwnershipParties.rdf)
    // RDF: fibo-be-oac-opty:UltimateConsolidation
  }

}
