// ===================================================================
// LoansRegulatory.sysml
// Source-parity extracted from: FiBO_LOAN_LoansGeneral.sysml
// Primary source module: LoansRegulatory.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/LoansGeneral
// ===================================================================
package Loans_Regulatory {
  doc/* This ontology contains concepts relating to regulatory requirements around loans, including disclosure rights and a small number of regulation-specific concepts. These include definitions of rights conferred on borrowers under consumer protection law, rights to equal treatment and the like. (source: dct:abstract, LoansRegulatory.rdf) */
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

  item def <'borrower data protection requirement'> BorrowerDataProtectionRequirement specializes DataProtectionRequirement {
  	doc /* Applicable regulations: vary by jurisdiciton. for example, only being allowed ot divulge actual judgements against a party, but not things that are not substantiated by judgements. For example, slow payments which are not covered by some judgement against the party. There will be different regulatory requirments about: 1. What the CR Agency can hold 2. Who they can divulge it to 3. What information they must provide the borrower at his/her request Some of the third thing there is covered in the EU the Data Protection Directive and local acts that implement this. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:BorrowerDataProtectionRequirement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;DataProtectionRequirement"/>
  }
  item def <'borrower disclosure requirement'> BorrowerDisclosureRequirement specializes DisclosureRequirement { // RDF: fibo-loan-ln-reg:BorrowerDisclosureRequirement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;DisclosureRequirement"/>
      // RDF: fibo-loan-ln-reg:BorrowerDisclosureRequirement
  }
  item def <'borrower right'> BorrowerRight specializes ConsumerRight { // RDF: fibo-loan-ln-reg:BorrowerRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;ConsumerRight"/>
      // RDF: fibo-loan-ln-reg:BorrowerRight
  }
  item def <'consumer credit equal treatment requirement'> ConsumerCreditEqualTreatmentRequirement specializes ConsumerCreditRequirement {
  	doc /* Applicable regulations: vary by jurisdiciton. for example, only being allowed ot divulge actual judgements against a party, but not things that are not substantiated by judgements. For example, slow payments which are not covered by some judgement against the party. There will be different regulatory requirments about: 1. What the CR Agency can hold 2. Who they can divulge it to 3. What information they must provide the borrower at his/her request Some of the third thing there is covered in the EU the Data Protection Directive and local acts that implement this. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:ConsumerCreditEqualTreatmentRequirement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;ConsumerCreditRequirement"/>
  }
  item def <'consumer credit protection law'> ConsumerCreditProtectionLaw specializes ConsumerProtectionLaw {
  	doc /* Applicable regulations: vary by jurisdiciton. for example, only being allowed ot divulge actual judgements against a party, but not things that are not substantiated by judgements. For example, slow payments which are not covered by some judgement against the party. There will be different regulatory requirments about: 1. What the CR Agency can hold 2. Who they can divulge it to 3. What information they must provide the borrower at his/her request Some of the third thing there is covered in the EU the Data Protection Directive and local acts that implement this. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:ConsumerCreditProtectionLaw
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;ConsumerProtectionLaw"/>
  }
  item def <'consumer credit reference agency'> ConsumerCreditReferenceAgency specializes Business_Registries::FormalOrganization {
  	doc /* Applicable regulations: vary by jurisdiciton. for example, only being allowed ot divulge actual judgements against a party, but not things that are not substantiated by judgements. For example, slow payments which are not covered by some judgement against the party. There will be different regulatory requirments about: 1. What the CR Agency can hold 2. Who they can divulge it to 3. What information they must provide the borrower at his/her request Some of the third thing there is covered in the EU the Data Protection Directive and local acts that implement this. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:ConsumerCreditReferenceAgency
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;FormalOrganization"/>
  }
  item def <'consumer credit requirement'> ConsumerCreditRequirement specializes Legal_Capacity::LegalObligation {
  	doc /* Requirement set out on the lender about how they must treat the appliction to a loan */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:ConsumerCreditRequirement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalObligation"/>
  }
  item def <'consumer protection agency'> ConsumerProtectionAgency specializes Regulatory_Agencies::RegulatoryAgency {
  	doc /* Some agency tasked with regulating consumer protection in some jurisdiction. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:ConsumerProtectionAgency
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-rga;RegulatoryAgency"/>
  }
  item def <'consumer protection law'> ConsumerProtectionLaw specializes Legal_Core::StatuteLaw {
  	doc /* REquirements other than data protection, governing what a credit reference agency can or cannot do. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:ConsumerProtectionLaw
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-cor;StatuteLaw"/>
  }
  item def <'consumer right'> ConsumerRight specializes Legal_Capacity::LegalRight {
  	doc /* REquirements other than data protection, governing what a credit reference agency can or cannot do. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:ConsumerRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalRight"/>
  }
  item def <'credit reference agency requirements'> CreditReferenceAgencyRequirements { // RDF: fibo-loan-ln-reg:CreditReferenceAgencyRequirements
    // RDF: fibo-loan-ln-reg:CreditReferenceAgencyRequirements
  }
  item def <'data protection requirement'> DataProtectionRequirement specializes Legal_Capacity::LegalObligation {
  	doc /* Requirements defining how data about individuals is held. Example is the EU DA directive and laws, which make the data the property of the individual that data is about. Covers - what information i sheld - who information can be divulged to. - the individual&apos;s rights in respect of that information Privacy regulations cover most of this. EU defines &quot;Personal Data&quot; and &quot;Sensitive Personal Data&quot;. For credit reference agencies the latter would be covered. More detail about whether they can divulge facts which are not subject to formal judgements etc. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:DataProtectionRequirement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalObligation"/>
  }
  item def <'disclosure requirement'> DisclosureRequirement specializes ConsumerCreditRequirement {
  	doc /* Requirement for disclosure to borrowers or potential borrowers. There are two kinds of disclosure requirement: 1. Disclosure about the information held on the borrower 2. Disclsure about the product. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:DisclosureRequirement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;ConsumerCreditRequirement"/>
  }
  item def <'equal treatment right'> EqualTreatmentRight specializes BorrowerRight {
  	doc /* The right to equal treatment under the law. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:EqualTreatmentRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;BorrowerRight"/>
  }
  item def <'good faith estimate'> GoodFaithEstimate specializes LoanProductRepresentations {
  	doc /* GFE - representation by the lender on the costs and implications of settlement (early termination) of the loan. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:GoodFaithEstimate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;LoanProductRepresentations"/>
  }
  item def <'information right'> InformationRight specializes BorrowerRight {
  	doc /* The right to some information in some context, for example when purchasing some product. The right to full and fair disclosure of */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:InformationRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;BorrowerRight"/>
  }
  item def <'lender right'> LenderRight specializes Legal_Capacity::LegalRight {
  	doc /* Rights on the lender to protect them against loss. furthe rNtoes: Logically, considering the two parties, they both have protecxtion mechanisms. so while the lender has protecxtion mechanisms through mortgage insurance, and the consumer has protextion mechanisms such as good faith estimates. also the agencies (see Consumer Protection Agency), an instance of which is the CFPB in the US (just set up). Lender rights are: - expressed in the Contract Consumer protection develops becaues the contract is written by the potential Lender. So the rights are introcued to rectify the imbalance between the two parties. Same goes for insurance. consumer protection laws governe dby the relevant consumer protection agency. So the lender protexts itself as it writes th contract AND does the things it needs to do to protext itself, but on the approval process, and with later instruments such as insurance. Interestingly., it is the Borrower who pays for this by paying for credit reports etc. So the borrower protects itself by other mechanisms. Caveat emptor - displaced by regulation (the buyer is protected by regulation). Uberimae Fidae - in the utmost good faith. Mortgage Insurance is an additional means of mitigating the risk, that the lended may have., so if the information assessed is not accurate, or if the borrower&apos;s situation changes for the worse. then the risk rating may go down. So the Mortgage Insurance is a further strategy which mitigates any shortfall in the Lender Righrs that you may have - ie someone guarantees. In the US you can also avoid that by having paid a deposit. PIMI: Principal, Interst and Morgage Insurance. So the Borrower pays towards the MI, esxcept if they have paid a given amount as deposit. there are 2 types of MI: 1. protects the lender in the event of borrower degault 2. Insurance for &quot;Incapacity to pay the mortage&quot; (these can be bought off the shelf - can combine health, unemployment etc.). - this is the Borrower mitigating their own risk. Prevents foreclosure. Similar to general sickness etc. Where the lender charges for MI, the cost is passed onto the Borrower. e.g. if there is a % valuation (e.g. 70% in Aus, 80% in US for example) then no insurance is required. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:LenderRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalRight"/>
  }
  item def <'loan product representations'> LoanProductRepresentations specializes Contracts::Representation {
  	doc /* Representations about the loan product and the appropriateness of this for the borrower. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:LoanProductRepresentations
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;Representation"/>
  }
  item def <'loan regulatory requirement'> LoanRegulatoryRequirement specializes Legal_Capacity::LegalObligation {
  	doc /* A regulatory requirement defined in regulations by a comsumer credit act or other legislation. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:LoanRegulatoryRequirement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalObligation"/>
  }
  item def <'product disclosure requirement'> ProductDisclosureRequirement specializes DisclosureRequirement {
  	doc /* A requirement governing what representations can be made about a product, as it affects the consumer. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:ProductDisclosureRequirement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;DisclosureRequirement"/>
  }
  item def <'product disclosure right'> ProductDisclosureRight specializes InformationRight {
  	doc /* The right to information about products at the point of purchasing these. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:ProductDisclosureRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;InformationRight"/>
  }
  item def <'reg b'> RegB specializes ConsumerCreditEqualTreatmentRequirement, DisclosureRequirement {
  	doc /* US regulation concerning &quot;Equal credit opportunity act&quot; Electronic delivery of disclosures */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:RegB
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;ConsumerCreditEqualTreatmentRequirement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;DisclosureRequirement"/>
  }
  item def <'reg z'> RegZ specializes ProductDisclosureRequirement {
  	doc /* US Fed regulation &quot;Truth in Lending Act&quot; uniform standards for electronic delivery of disclosures */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:RegZ
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;ProductDisclosureRequirement"/>
  }
  item def <'right of credit record correction'> RightOfCreditRecordCorrection specializes CreditReferenceAgencyRequirements {
  	doc /* The right to have a credit record corrected. */
  	// skos:definition xml:lang="en" (LoansRegulatory.rdf)
  	// rdfs:label xml:lang="en" (LoansRegulatory.rdf)
  	// RDF: fibo-loan-ln-reg:RightOfCreditRecordCorrection
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;CreditReferenceAgencyRequirements"/>
  }

}
