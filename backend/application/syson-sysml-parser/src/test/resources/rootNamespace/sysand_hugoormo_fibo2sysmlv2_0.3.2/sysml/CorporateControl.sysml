// ===================================================================
// CorporateControl.sysml
// Source-parity extracted from: FiBO_BE_OwnershipAndControl.sysml
// Primary source module: CorporateControl.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/OwnershipAndControl
// Structural note: split from FiBO_BE_OwnershipAndControl umbrella.
// ===================================================================
package Corporate_Control {
  doc/* This ontology defines concepts relating to corporation-specific control. These concepts are based on the general types of control (both de facto control and controlling interests), as defined in the ControlParties ontology, and are the specific examples of these concepts as they apply to companies incorporated by the issuance of shares. (source: dct:abstract, CorporateControl.rdf) */
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

// ControlledParty, EntityControllingParty, MajorityControllingParty, DeJureControllingInterestParty, TotalOwner
// Shareholder

  item def <'affiliate'> Affiliate specializes Parties::PartyRole {
      doc /* party that is related to a legal entity, directly, or indirectly through one or more intermediaries, and controls, or is controlled by, or is under common control with that entity, typically determined by the degree of ownership */
      // skos:definition xml:lang="en" (CorporateControl.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  	// rdfs:label xml:lang="en" (CorporateControl.rdf)
    // RDF: fibo-be-oac-cctl:Affiliate
    // subClassOf: PartyRole - from FIBO_FND_Parties
  }

  item def <'affiliation'> Affiliation specializes Control::Control {
      doc /* situation in which a controlled party is affiliated with a controlling party for some period of time */
      // skos:definition xml:lang="en" (CorporateControl.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;Control"/>
  	// rdfs:label xml:lang="en" (CorporateControl.rdf)
    // RDF: fibo-be-oac-cctl:Affiliation
    // subClassOf: Control - from FIBO_FND_OwnershipAndControl
  }

  item def <'controlled affiliate'> ControlledAffiliate specializes Affiliate, Control_Parties::ControlledParty {
      doc /* controlled party in an affiliation situation */
      // skos:definition xml:lang="en" (CorporateControl.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cctl;Affiliate"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;ControlledParty"/>
  	// rdfs:label xml:lang="en" (CorporateControl.rdf)
    // RDF: fibo-be-oac-cctl:ControlledAffiliate
  }

  item def <'controlling affiliate'> ControllingAffiliate specializes Affiliate, Control_Parties::MajorityControllingParty {
      doc /* controlling party in an affiliation situation */
      // skos:definition xml:lang="en" (CorporateControl.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cctl;Affiliate"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;MajorityControllingParty"/>
  	// rdfs:label xml:lang="en" (CorporateControl.rdf)
    // RDF: fibo-be-oac-cctl:ControllingAffiliate
  }

  item def <'domestic ultimate parent'> DomesticUltimateParent specializes TotalControllingInterestParty {
      doc /* party that is recognized as the ultimate parent of a given organization within the country or jurisdiction of incorporation or organization */
      // skos:definition xml:lang="en" (CorporateControl.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cctl;TotalControllingInterestParty"/>
  	// rdfs:label xml:lang="en" (CorporateControl.rdf)
    // RDF: fibo-be-oac-cctl:DomesticUltimateParent
  }

  item def <'global ultimate parent'> GlobalUltimateParent specializes TotalControllingInterestParty {
      doc /* party that is recognized as the ultimate parent of a given organization world-wide */
      // skos:definition xml:lang="en" (CorporateControl.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cctl;TotalControllingInterestParty"/>
  	// rdfs:label xml:lang="en" (CorporateControl.rdf)
    // RDF: fibo-be-oac-cctl:GlobalUltimateParent
  }

  item def <'joint venture partner'> JointVenturePartner specializes Control_Parties::EntityControllingParty {
      doc /* party that shares capital, technology, human resources, risks, and benefits of an entity under shared control */
      // skos:definition xml:lang="en" (CorporateControl.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;EntityControllingParty"/>
  	// rdfs:label xml:lang="en" (CorporateControl.rdf)
    // RDF: fibo-be-oac-cctl:JointVenturePartner
  }

  item def <'significant shareholder'> SignificantShareholder specializes VotingShareholder {
      doc /* party that owns a significant voting stake in an organization that is less than 50 percent but greater than some threshold */
      // skos:definition xml:lang="en" (CorporateControl.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cctl;VotingShareholder"/>
  	// rdfs:label xml:lang="en" (CorporateControl.rdf)
    // RDF: fibo-be-oac-cctl:SignificantShareholder
  }

  item def <'subsidiary'> Subsidiary specializes ControlledAffiliate {
      doc /* legal entity that is entirely or majority owned and controlled by another legal entity */
      // skos:definition xml:lang="en" (CorporateControl.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cctl;ControlledAffiliate"/>
  	// rdfs:label xml:lang="en" (CorporateControl.rdf)
    // RDF: fibo-be-oac-cctl:Subsidiary
  }

  item def <'total controlling interest party'> TotalControllingInterestParty specializes SignificantShareholder, Control_Parties::TotalOwner {
      doc /* voting shareholder that owns 100 percent of the voting shares in some legal entity */
      // skos:definition xml:lang="en" (CorporateControl.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cctl;SignificantShareholder"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;TotalOwner"/>
  	// rdfs:label xml:lang="en" (CorporateControl.rdf)
    // RDF: fibo-be-oac-cctl:TotalControllingInterestParty
  }

  item def <'voting shareholder'> VotingShareholder specializes Control_Parties::DeJureControllingInterestParty, Corporate_Ownership::Shareholder {
      doc /* voting shareholder that owns 100 percent of the voting shares in some legal entity */
      // skos:definition xml:lang="en" (CorporateControl.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;DeJureControllingInterestParty"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cown;Shareholder"/>
  	// rdfs:label xml:lang="en" (CorporateControl.rdf)
    // RDF: fibo-be-oac-cctl:VotingShareholder (also in Executives)
  }
}

// --- CorporateOwnership.rdf ---
