// ===================================================================
// AgencyMBSIssuance.sysml
// Source-parity extracted from: FiBO_BP_SecuritiesIssuance.sysml
// Primary source module: AgencyMBSIssuance.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Processes BP/SecuritiesIssuance
// ===================================================================
package AgencyMBSIssuance {
  doc/* Process ontology for the process of issuance (securitization) of mortgage backed securities by government agencies. Based on US government agency MBS issuance process. (source: dct:abstract, AgencyMBSIssuance.rdf) */
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

  item def AcquireMortgage specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* Draft of set of information defining the pass thorugh security terms. These will eventually become the contractual terms of the instrument. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:AcquireMortgage
  }
  item def AddMortgageToPool specializes Debt_Issuance::IssuanceProcessActivity { // RDF: fibo-bp-iss-ambs:AddMortgageToPool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
      // RDF: fibo-bp-iss-ambs:AddMortgageToPool
  }
  item def AgencyMortgagePoolCreationProcess specializes Debt_Issuance::RetailAssetPoolCreationProcess { // RDF: fibo-bp-iss-ambs:AgencyMortgagePoolCreationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;RetailAssetPoolCreationProcess"/>
      // RDF: fibo-bp-iss-ambs:AgencyMortgagePoolCreationProcess
  }
  item def AllocatePrimaryIdentifier specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* An entity which may become a primary investor in the issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:AllocatePrimaryIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def AssessPoolSuitablilityForIssuance specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* Draft of set of information defining the pass thorugh security terms. These will eventually become the contractual terms of the instrument. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:AssessPoolSuitablilityForIssuance
  }
  item def ClassifyMortgage specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* Draft of set of information defining the pass thorugh security terms. These will eventually become the contractual terms of the instrument. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:ClassifyMortgage
  }
  item def DefineMortgagePool specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* Draft of set of information defining the pass thorugh security terms. These will eventually become the contractual terms of the instrument. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:DefineMortgagePool
  }
  item def DraftPassThroughTermsheet specializes Contracts::TermSheet {
  	doc /* Draft of set of information defining the pass thorugh security terms. These will eventually become the contractual terms of the instrument. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:DraftPassThroughTermsheet
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;TermSheet"/>
  }
  item def DrawUpOfferingMemorandum specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* In reality there is one termsheet that has sets of information for the terms for each Tranche. This class of information identifies the terms of one tranche, but it does not exist as a separate document in its own right. Further Notes ? We may need to firm up the relationship between the individual tranche termsheet and the information about the relationships among these (some of which are quite complex) and the terms that are common to more than one tranche. In practice these may be separate sections of one document. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:DrawUpOfferingMemorandum
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def DrawUpTermsheet specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* Identify mortgage conforming to overall requirements for this issuer. */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:DrawUpTermsheet
  }
  item def FinalizePoolContent specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* Identify mortgage conforming to overall requirements for this issuer. */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:FinalizePoolContent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def FinalizeProspectus specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* Identify mortgage conforming to overall requirements for this issuer. */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:FinalizeProspectus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def IdentifyConformingMortgage specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* Identify mortgage conforming to overall requirements for this issuer. */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:IdentifyConformingMortgage
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def IdentifyUnderwriter specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* A part or section of a prospectus for a pass through MBS issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:IdentifyUnderwriter
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def InAssemblyAgencyMortgagePool specializes Mortgage_backed_Securities::AgencyMortgagePool { // RDF: fibo-bp-iss-ambs:InAssemblyAgencyMortgagePool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;AgencyMortgagePool"/>
      // RDF: fibo-bp-iss-ambs:InAssemblyAgencyMortgagePool
  }
  item def InIssuanceAgencyMortgagePool specializes Mortgage_backed_Securities::AgencyMortgagePool {
  	doc /* A part or section of a prospectus for a pass through MBS issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:InIssuanceAgencyMortgagePool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;AgencyMortgagePool"/>
  }
  item def MakeSecuritiesAvailabeInMarket specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* A part or section of a prospectus for a pass through MBS issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:MakeSecuritiesAvailabeInMarket
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def MarketIssue specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* A part or section of a prospectus for a pass through MBS issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:MarketIssue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def NotYetIssuedAgencyMortgagePool specializes Mortgage_backed_Securities::AgencyMortgagePool {
  	doc /* A part or section of a prospectus for a pass through MBS issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:NotYetIssuedAgencyMortgagePool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;AgencyMortgagePool"/>
  }
  item def PassThroughIssueProspectusPart specializes MBS_Issuance::ProspectusPart {
  	doc /* A part or section of a prospectus for a pass through MBS issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:PassThroughIssueProspectusPart
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-mbs;ProspectusPart"/>
  }
  item def PassThroughMBSDraftProspectus specializes IssuanceDocuments::PreliminaryProspectus {
  	doc /* The draft prospectus for a pass through Mortgage Backed Securities issue, as determined by the issuing agency prior to marketing the issue. Certain terms in the draft prospectus will be finalized later in the issuance process to become the actual Prospectus. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:PassThroughMBSDraftProspectus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-doc;PreliminaryProspectus"/>
  }
  item def PassThroughMBSFinalProspectus specializes IssuanceDocuments::FinalProspectus, Mortgage_backed_Securities::MortgageBackedSecurityOfferingProspectus {
  	doc /* Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:PassThroughMBSFinalProspectus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MortgageBackedSecurityOfferingProspectus"/>
  }
  item def PassThroughMBSFinalTermsheet specializes PassThroughIssueProspectusPart, Contracts::TermSheet {
  	doc /* The final termsheet for the pass through MBS issue. This defines the terms for the MBS contract itself. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:PassThroughMBSFinalTermsheet
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ambs;PassThroughIssueProspectusPart"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;TermSheet"/>
  }
  item def PassThroughMBSSecuritizationProcess specializes MBS_Issuance::MBSSecuritizationProcess {
  	doc /* The offering memorandum for a pass through MBS issue, setting out basic information about a future issue, for the information of prospective investors and their agents. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:PassThroughMBSSecuritizationProcess
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-mbs;MBSSecuritizationProcess"/>
  }
  item def PassThroughOfferingMemorandum specializes IssuanceDocuments::OfferingDocument {
  	doc /* The offering memorandum for a pass through MBS issue, setting out basic information about a future issue, for the information of prospective investors and their agents. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:PassThroughOfferingMemorandum
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;OfferingDocument"/>
  }
  item def PoolConformanceCriteria { // RDF: fibo-bp-iss-ambs:PoolConformanceCriteria
    // RDF: fibo-bp-iss-ambs:PoolConformanceCriteria
  }
  item def PotentialAgencyMBSIssuer specializes Debt_Issuance::PoolBackedSecuritySecuritizationProcessActor { // RDF: fibo-bp-iss-ambs:PotentialAgencyMBSIssuer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;PoolBackedSecuritySecuritizationProcessActor"/>
      // RDF: fibo-bp-iss-ambs:PotentialAgencyMBSIssuer
  }
  item def PotentialPassThroughIssuanceUnderwriter specializes Issuance_Process::PotentialUnderwriter {
  	doc /* The entity which will become the underwriter for the pass through MBS issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:PotentialPassThroughIssuanceUnderwriter
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;PotentialUnderwriter"/>
  }
  item def RegisterSecurity specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* The mortgage is automatically validated for conformance to the requirements of the pool in which it is to be included. */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:RegisterSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def SuitableForIssue { // RDF: fibo-bp-iss-ambs:SuitableForIssue
    // RDF: fibo-bp-iss-ambs:SuitableForIssue
  }
  item def ValidateConformance specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* The mortgage is automatically validated for conformance to the requirements of the pool in which it is to be included. */
  	// skos:definition xml:lang="en" (AgencyMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (AgencyMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-ambs:ValidateConformance
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
}

// --- DebtIssuance.rdf ---
