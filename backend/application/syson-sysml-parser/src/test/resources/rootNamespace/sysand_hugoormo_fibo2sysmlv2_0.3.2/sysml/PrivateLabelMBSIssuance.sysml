// ===================================================================
// PrivateLabelMBSIssuance.sysml
// Source-parity extracted from: FiBO_BP_SecuritiesIssuance.sysml
// Primary source module: PrivateLabelMBSIssuance.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Processes BP/SecuritiesIssuance
// ===================================================================
package PrivateLabelMBSIssuance {
  doc/* Process ontology for the process of issuance (securitization) of mortgage backed securities by commercial institutions rather than by government agencies. (source: dct:abstract, PrivateLabelMBSIssuance.rdf) */
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

  item def AllocateRatings specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* An entity which may become a primary investor in the issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:AllocateRatings
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def AssessPoolSuitabilityForIssuance specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* An entity which may become a primary investor in the issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:AssessPoolSuitabilityForIssuance
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def AssessRatings specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* An entity which may become a primary investor in the issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:AssessRatings
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def BrokerDealer specializes Functional_Entities::FunctionalEntity {
  	doc /* An entity which may become a primary investor in the issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
  	// RDF: fibo-bp-iss-pmbs:BrokerDealer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalEntity"/>
  }
  item def CloseDeal specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* the denom of the notes */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:CloseDeal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def DefineNotesParameters specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* the denom of the notes */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:DefineNotesParameters
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def DefinePoolCharacteristics specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* Draft of set of information defining the notes breakdown of one tranche. covers denominations and amounts that you can byu of the instrument in this tranche. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:DefinePoolCharacteristics
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def DraftTrancheNotesParameters specializes TrancheNotesParameters, FiBO_FND_ExternalStubs::Document {
  	doc /* Draft of set of information defining the notes breakdown of one tranche. covers denominations and amounts that you can byu of the instrument in this tranche. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:DraftTrancheNotesParameters
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
  }
  item def DraftTrancheStructure specializes FiBO_FND_ExternalStubs::Document {
  	doc /* Draft of set of information defining the tranches in the tranched issue and how these relate to one another. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:DraftTrancheStructure
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
  }
  item def DraftTrancheTermsheet specializes Contracts::TermSheet {
  	doc /* Draft of set of information defining one tranche of a tranched issue. This will become the termsheet of an individual tranche within that issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:DraftTrancheTermsheet
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;TermSheet"/>
  }
  item def DrawUpTrancheTermsheets specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* After the deal is closed the security is formally registered with some registraton authority This is the "official" bit - there is not a separate "Issue" activity which is official ????? */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:DrawUpTrancheTermsheets
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def GetCommitmentFromInvestors specializes Debt_Issuance::IssuanceProcessActivity { // RDF: fibo-bp-iss-pmbs:GetCommitmentFromInvestors
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
      // RDF: fibo-bp-iss-pmbs:GetCommitmentFromInvestors
  }
  item def InAssemblyMortgagePool specializes NonAgencyMortgagePool { // RDF: fibo-bp-iss-pmbs:InAssemblyMortgagePool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;NonAgencyMortgagePool"/>
      // RDF: fibo-bp-iss-pmbs:InAssemblyMortgagePool
  }
  item def IndividualTrancheDefinitions specializes TranchedMBSIssueProspectusPart, Contracts::TermSheet {
  	doc /* In reality there is one termsheet that has sets of information for the terms for each Tranche. This class of information identifies the terms of one tranche, but it does not exist as a separate document in its own right. Further Notes ? We may need to firm up the relationship between the individual tranche termsheet and the information about the relationships among these (some of which are quite complex) and the terms that are common to more than one tranche. In practice these may be separate sections of one document. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:IndividualTrancheDefinitions
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-pmbs;TranchedMBSIssueProspectusPart"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;TermSheet"/>
  }
  item def InIssuanceNonAgencyMortgagePool specializes NonAgencyMortgagePool {
  	doc /* In reality there is one termsheet that has sets of information for the terms for each Tranche. This class of information identifies the terms of one tranche, but it does not exist as a separate document in its own right. Further Notes ? We may need to firm up the relationship between the individual tranche termsheet and the information about the relationships among these (some of which are quite complex) and the terms that are common to more than one tranche. In practice these may be separate sections of one document. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:InIssuanceNonAgencyMortgagePool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;NonAgencyMortgagePool"/>
  }
  item def Investor specializes Ownership_Parties::EntityOwner, Contracts::ContractParty {
  	doc /* What happens here? e.g. notices / marketing (phone calls) Structured Finance: There&apos;s not really notices in the newspaper, it&apos;s a very small market and it&apos;s all based on relationships so there&apos;s no public notice. So you would get an email from the sales person at the bank who has just closed the deal and is now selling these (this bank is the broker/dealer who bought it?) There&apos;s not really much of a secondary market - the initial investors would often hold on to these. There is something around Bloomberg - you can go there and see what&apos;s available, if someone has a number of notes from a iven tranche, that they are willing to sell. So there&apos;s no transpoarency (!!) Sales would be OTC but less transparent e.g. if you look up a normal OTC stock, you would be able to see more of this information, than in these (non Agency) MBS issues and other SF. DOES THIS APPLY IN ALL MBS??. */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (OwnershipParties.rdf)
  	// RDF: fibo-bp-iss-pmbs:Investor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;EntityOwner"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
  }
  item def MakeSecuritiesAvailableInMarket specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* What happens here? e.g. notices / marketing (phone calls) Structured Finance: There's not really notices in the newspaper, it's a very small market and it's all based on relationships so there's no public notice. So you would get an email from the sales person at the bank who has just closed the deal and is now selling these (this bank is the broker/dealer who bought it?) There's not really much of a secondary market - the initial investors would often hold on to these. There is something around Bloomberg - you can go there and see what's available, if someone has a number of notes from a iven tranche, that they are willing to sell. So there's no transpoarency (!!) Sales would be OTC but less transparent e.g. if you look up a normal OTC stock, you would be able to see more of this information, than in these (non Agency) MBS issues and other SF. DOES THIS APPLY IN ALL MBS??. */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:MakeSecuritiesAvailableInMarket
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def MarketIssueToPrimaryInvestors specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* The agency which will provide the primary securitiy identifier for the security. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:MarketIssueToPrimaryInvestors
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def MBSSecuritizationEnd { // RDF: fibo-bp-iss-pmbs:MBSSecuritizationEnd
    // RDF: fibo-bp-iss-pmbs:MBSSecuritizationEnd
  }
  item def MBSSecuritizationStart { // RDF: fibo-bp-iss-pmbs:MBSSecuritizationStart
    // RDF: fibo-bp-iss-pmbs:MBSSecuritizationStart
  }
  item def NonAgencyMortgagePool specializes Mortgage_backed_Securities::MortgagePool {
  	doc /* An issue of Mortgage Backed Security instruments in which payments on the pool are passed through to investors */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:NonAgencyMortgagePool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MortgagePool"/>
  }
  item def NonAgencyPoolCreationEnd { // RDF: fibo-bp-iss-pmbs:NonAgencyPoolCreationEnd
    // RDF: fibo-bp-iss-pmbs:NonAgencyPoolCreationEnd
  }
  item def NonAgencyPoolCreationProcess specializes Debt_Issuance::RetailAssetPoolCreationProcess {
  	doc /* The agency which will provide the primary securitiy identifier for the security. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:NonAgencyPoolCreationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;RetailAssetPoolCreationProcess"/>
  }
  item def NonAgencyPoolCreationStart { // RDF: fibo-bp-iss-pmbs:NonAgencyPoolCreationStart
    // RDF: fibo-bp-iss-pmbs:NonAgencyPoolCreationStart
  }
  item def NotYetIssuedNonAgencyMortgagePool specializes NonAgencyMortgagePool {
  	doc /* The agency which will provide the primary securitiy identifier for the security. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:NotYetIssuedNonAgencyMortgagePool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;NonAgencyMortgagePool"/>
  }
  item def NumberingAgency specializes Issuance_Process::SecuritiesIssuanceProcessActor {
  	doc /* The agency which will provide the primary securitiy identifier for the security. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:NumberingAgency
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecuritiesIssuanceProcessActor"/>
  }
  item def PoolLifecycleStateSelection specializes Lifecycles::LifecycleStage {
  	doc /* The entity which will become the issuing party for the Tranched MBS Issue. This entity is the principal actor in most of the activities involved in the issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:PoolLifecycleStateSelection
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStage"/>
  }
  item def PoolReadyForIssue { // RDF: fibo-bp-iss-pmbs:PoolReadyForIssue
    // RDF: fibo-bp-iss-pmbs:PoolReadyForIssue
  }
  item def PoolTrustee specializes Trusts::Trustee, Parties::PartyRole {
  	doc /* The entity which will become the issuing party for the Tranched MBS Issue. This entity is the principal actor in most of the activities involved in the issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:PoolTrustee
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
  item def PotentialMBSInvestor specializes Ownership_Parties::Investor {
  	doc /* The entity which will become the issuing party for the Tranched MBS Issue. This entity is the principal actor in most of the activities involved in the issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:PotentialMBSInvestor
  }
  item def PotentialNonAgencyMBSIssuer specializes Debt_Issuance::PoolBackedSecuritySecuritizationProcessActor {
  	doc /* The entity which will become the issuing party for the Tranched MBS Issue. This entity is the principal actor in most of the activities involved in the issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:PotentialNonAgencyMBSIssuer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;PoolBackedSecuritySecuritizationProcessActor"/>
  }
  item def PotentialTranchedIssueUnderwriter specializes Issuance_Process::PotentialUnderwriter {
  	doc /* The entity which will become the underwriter for the tranched MBS issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:PotentialTranchedIssueUnderwriter
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;PotentialUnderwriter"/>
  }
  item def PrimaryInvestor specializes Ownership_Parties::Investor {
  	doc /* A party which becomes the primary investor in the issue, by purchasing some of the tranches of the issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:PrimaryInvestor
  }
  item def PurchaseMortgageIntoPool specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* After the deal is closed the security is formally registered with some registraton authority This is the &quot;official&quot; bit - there is not a separate &quot;Issue&quot; activity which is official ????? */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:PurchaseMortgageIntoPool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProcessActivity"/>
  }
  item def RatingsAgency { // RDF: fibo-bp-iss-pmbs:RatingsAgency
    // RDF: fibo-bp-iss-pmbs:RatingsAgency
  }
  item def RatingsSuitableForIssue specializes AgencyMBSIssuance::SuitableForIssue {
  	doc /* After the deal is closed the security is formally registered with some registraton authority This is the "official" bit - there is not a separate "Issue" activity which is official ????? */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:RatingsSuitableForIssue
  }
  item def RemittanceReport specializes Reporting::Report {
  	doc /* Report containing a specific and limited set of information about the Deal. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:RemittanceReport
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rep;Report"/>
  }
  item def TranchedDraftProspectus specializes IssuanceDocuments::PreliminaryProspectus {
  	doc /* The draft prospectus for a tranched Mortgage Backed Securities issue, as determined by the issuing entity prior to marketing the issue. Certain terms in the draft prospectus will be finalized later in the issuance process to become the actual Prospectus. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:TranchedDraftProspectus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-doc;PreliminaryProspectus"/>
  }
  item def TranchedMBSDealSettlement { // RDF: fibo-bp-iss-pmbs:TranchedMBSDealSettlement
    // RDF: fibo-bp-iss-pmbs:TranchedMBSDealSettlement
  }
    item def TranchedMBSDealTransaction specializes Securities_Transactions::FinancialPrimaryMarketTransaction {
  	doc /* The deal transaction by which the MBS Issue is issued to primary investors. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:TranchedMBSDealTransaction
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-sec;FinancialPrimaryMarketTransaction"/>
  }
  item def TranchedMBSInstrument specializes Mortgage_backed_Securities::MortgageBackedSecurity {
  	doc /* Any event where some value passes some threashold. Or some other type of business event. This is not restricted to "trigger" in the sense of a value passing a threshold. Can also be an seen such as a CDO manager going into bankruptcy. */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:TranchedMBSInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MortgageBackedSecurity"/>
  }
  item def TranchedMBSIssueProspectusPart specializes MBS_Issuance::ProspectusPart {
  	doc /* A part or section of a prospectus for a tranched mortgage backed securities issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:TranchedMBSIssueProspectusPart
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-mbs;ProspectusPart"/>
  }
    item def TranchedMBSPrimaryDealTransactionSettlementProcess specializes Securities_Transactions::SettlementProcess {
  	doc /* The process by which the primary deal transaction is settled. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:TranchedMBSPrimaryDealTransactionSettlementProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-txn-sec;SettlementProcess"/>
  }
  item def TranchedMBSProspectusOutline specializes IssuanceDocuments::PreliminaryProspectus {
  	doc /* An outline of the tranched prospectus, provind an intial representation of the possible tranches and their features. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:TranchedMBSProspectusOutline
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-doc;PreliminaryProspectus"/>
  }
  item def TranchedMBSSecuritizationProcess specializes MBS_Issuance::MBSSecuritizationProcess {
  	doc /* The Offering Memorandum will include or attach the terms for two or more individual tranches that will make up the issue, and the structure of the tranches, including how they will relate to one another (priorities and so on). Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:TranchedMBSSecuritizationProcess
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-mbs;MBSSecuritizationProcess"/>
  }
  item def TranchedOfferingMemorandum specializes IssuanceDocuments::OfferingDocument {
  	doc /* The Offering Memorandum will include or attach the terms for two or more individual tranches that will make up the issue, and the structure of the tranches, including how they will relate to one another (priorities and so on). Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:TranchedOfferingMemorandum
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;OfferingDocument"/>
  }
  item def TrancheNotesParameters specializes TranchedMBSIssueProspectusPart {
  	doc /* One set of information defining the notes breakdown of one tranche. Covers denominations and amounts that you can byu of the instrument in this tranche. Q: Is this really defined in the prospectus? A: yes The prospectus lists the characteristics including e.g. &quot;The notes will be sold in denominations of X AND Increuemtns of Y e.g. $250 000 incremented by $1000. Parameters include: Denominations Minimum amounts what else? Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:TrancheNotesParameters
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-pmbs;TranchedMBSIssueProspectusPart"/>
  }
  item def TrancheStructureAndTermsheet specializes TranchedMBSIssueProspectusPart {
  	doc /* One sheet defining the MBS structure. */
  	// skos:definition xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (PrivateLabelMBSIssuance.rdf)
  	// RDF: fibo-bp-iss-pmbs:TrancheStructureAndTermsheet
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-pmbs;TranchedMBSIssueProspectusPart"/>
  }
  // NOTE: AllocatePrimaryIdentifier, DrawUpOfferingMemorandum, FinalizePoolContent,
  //       FinalizeProspectus, IdentifyConformingMortgage, IdentifyUnderwriter,
  //       RegisterSecurity also appear in AgencyMBSIssuance.rdf

}
