// ===================================================================
// ControlParties.sysml
// Source-parity extracted from: FiBO_BE_OwnershipAndControl.sysml
// Primary source module: ControlParties.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/OwnershipAndControl
// Structural note: split from FiBO_BE_OwnershipAndControl umbrella.
// ===================================================================
package Control_Parties {
  doc/* This ontology defines concepts relating to types of controlling parties. The concepts defined here are party in role concepts, which define the nature of some entity such as an organization or a legal person, in some role such as that of owning a controlling interest in the entity or of controlling that entity. These roles are defined in terms of the types of control enjoyed by the party, for example de facto or de jure control. An important feature of this ontology is the distinction between the holding of a controlling interest (such as voting shares), and the de facto existence of control of one body by another as asserted in company filings or as a conclusion drawn from computational analysis of controlling interests. (source: dct:abstract, ControlParties.rdf) */
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

// ConstitutionalOwner, EntityOwner, Investor

  item def <'contractual control'> ContractualControl specializes Control::DeJureControl {
      doc /* control established through the terms of some contract */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;DeJureControl"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:ContractualControl
    // subClassOf: DeJureControl - from FIBO_FND_OwnershipAndControl
  }

  item def <'controlled party'> ControlledParty specializes Control::ControlledThing {
      doc /* person or organization over which some party exercises some form of authority or influence with respect to some situation */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;ControlledThing"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:ControlledParty
    // subClassOf: ControlledThing - from FIBO_FND_OwnershipAndControl
  }

  item def <'controlling alliance'> ControllingAlliance specializes Formal_Organizations::Group {
      doc /* group that has formed some alliance to jointly exercise control over some entity */
      // skos:definition xml:lang="en" (ControlParties.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-org-fm;Group"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:ControllingAlliance
  }

  item def <'court appointed control'> CourtAppointedControl specializes Control::DeJureControl {
      doc /* control conferred by the actions of some court, for example in the context of receivership */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;DeJureControl"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:CourtAppointedControl
    // subClassOf: DeJureControl - from FIBO_FND_OwnershipAndControl
  }

  item def <'de facto controlling interest party'> DeFactoControllingInterestParty specializes Control::ControllingParty {
      doc /* party that exercises some control over an entity other than via explicit, legal means */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;ControllingParty"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:DeFactoControllingInterestParty
    // subClassOf: ControllingParty - from FIBO_FND_OwnershipAndControl
  }

  item def <'de jure controlling interest party'> DeJureControllingInterestParty specializes Control::ControllingParty {
      doc /* party that has the legal authority to exercise control */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;ControllingParty"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:DeJureControllingInterestParty
    // subClassOf: ControllingParty - from FIBO_FND_OwnershipAndControl
  }

  item def <'entity controlling party'> EntityControllingParty specializes Control::ControllingParty {
      doc /* party that has the authority to control some legal entity */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;ControllingParty"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:EntityControllingParty
    // subClassOf: ControllingParty - from FIBO_FND_OwnershipAndControl
  }

  item def <'influence-based de facto control'> InfluenceBasedDeFactoControl specializes Control::DeFactoControl {
      doc /* informal control that involves influence over some party */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;DeFactoControl"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:InfluenceBasedDeFactoControl
    // subClassOf: DeFactoControl - from FIBO_FND_OwnershipAndControl
  }

  item def <'influence-based de facto controlling party'> InfluenceBasedDeFactoControllingParty specializes DeFactoControllingInterestParty {
      doc /* party that holds influence-based control over some other party */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;DeFactoControllingInterestParty"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:InfluenceBasedDeFactoControllingParty
  }

  item def <'investment-based de facto control'> InvestmentBasedDeFactoControl specializes Control::DeFactoControl {
      doc /* control that arises through some investment in some entity, other than via the holding of constitutional equity (shares etc.) in that entity */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;DeFactoControl"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:InvestmentBasedDeFactoControl
    // subClassOf: DeFactoControl - from FIBO_FND_OwnershipAndControl
  }

  item def <'investment-based de facto controlling interest party'> InvestmentBasedDeFactoControllingInterestParty specializes DeFactoControllingInterestParty, Ownership_Parties::Investor {
      doc /* party that holds investment-based control over some other party */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;DeFactoControllingInterestParty"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;Investor"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:InvestmentBasedDeFactoControllingInterestParty
  }

  item def <'joint controlling party'> JointControllingParty specializes EntityControllingParty {
      doc /* party that collectively has the authority to control the affairs of some business organization */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;EntityControllingParty"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:JointControllingParty
  }

  item def <'majority controlling party'> MajorityControllingParty specializes EntityControllingParty {
      doc /* controlling party that possesses, either directly or indirectly, the power to direct or cause the direction of the management and policies of a legal person, whether through the ownership of a majority of voting securities, by contract, or otherwise */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;EntityControllingParty"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:MajorityControllingParty
  }

  item def <'receiver'> Receiver specializes DeJureControllingInterestParty {
      doc /* party appointed by some court for the purposes of winding up the affairs of some entity which is no longer solvent */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;DeJureControllingInterestParty"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:Receiver
  }

  item def <'sole controlling party'> SoleControllingParty specializes EntityControllingParty {
      doc /* party that has sole control over and directs the affairs of some legal entity */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;EntityControllingParty"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:SoleControllingParty
  }

  item def <'total owner'> TotalOwner specializes Ownership_Parties::ConstitutionalOwner {
      doc /* party that has 100 percent ownership some legal entity */
      // skos:definition xml:lang="en" (ControlParties.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;ConstitutionalOwner"/>
  	// rdfs:label xml:lang="en" (ControlParties.rdf)
    // RDF: fibo-be-oac-cpty:TotalOwner
  }
}

// --- CorporateControl.rdf ---
