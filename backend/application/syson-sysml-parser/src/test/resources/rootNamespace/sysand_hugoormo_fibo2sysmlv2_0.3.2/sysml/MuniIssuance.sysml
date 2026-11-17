// ===================================================================
// MuniIssuance.sysml
// Source-parity extracted from: FiBO_BP_SecuritiesIssuance.sysml
// Primary source module: MuniIssuance.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Processes BP/SecuritiesIssuance
// ===================================================================
package MuniIssuance {
  doc/* Ontology for the process in which municipal bonds are issued. (source: dct:abstract, MuniIssuance.rdf) */
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

  item def AnnounceSecuritiesIssue { // RDF: fibo-bp-iss-muni:AnnounceSecuritiesIssue
    // RDF: fibo-bp-iss-muni:AnnounceSecuritiesIssue
  }
  item def BondCounsel specializes Contracts::ContractThirdParty {
  	doc /* A bank, brokerage or other financial institution that is not a direct DTC member. Correspondents rely on direct DTC Participants to perform their DTC settlement services */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:BondCounsel
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractThirdParty"/>
  }
  item def Correspondent specializes MuniIssuanceProcessParticipant, Contracts::ContractThirdParty {
  	doc /* A bank, brokerage or other financial institution that is not a direct DTC member. Correspondents rely on direct DTC Participants to perform their DTC settlement services */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:Correspondent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;MuniIssuanceProcessParticipant"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractThirdParty"/>
  }
  item def DebtAuctionProcess specializes Issuance_Process::SecuritiesIssuanceProcess {
  	doc /* Terms for Change to an Issue Amount for Debt securities */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:DebtAuctionProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecuritiesIssuanceProcess"/>
  }
  item def DebtIssueOverAllotmentTerms specializes IssueOverAllotmentTerms {
  	doc /* Terms for Change to an Issue Amount for Debt securities */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:DebtIssueOverAllotmentTerms
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;IssueOverAllotmentTerms"/>
  }
  item def DebtUnderwritingClosing specializes UnderwritingIssuanceClosing {
  	doc /* The process of crediting the DTC participant account on settlement date with the position for the new issue. The overall closing process includes the exchange of funds that happens outside of DTC. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:DebtUnderwritingClosing
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;UnderwritingIssuanceClosing"/>
  }
  item def DebtUnderwritingIssuanceProcess specializes SecuritiesUnderwritingIssuanceProcess { // RDF: fibo-bp-iss-muni:DebtUnderwritingIssuanceProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;SecuritiesUnderwritingIssuanceProcess"/>
      // RDF: fibo-bp-iss-muni:DebtUnderwritingIssuanceProcess
  }
  item def Dissemination specializes UnderwritingProcessActivity { // RDF: fibo-bp-iss-muni:Dissemination
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;UnderwritingProcessActivity"/>
      // RDF: fibo-bp-iss-muni:Dissemination
  }
  item def DisseminationUnderwriter specializes PotentialMuniUnderwriter {
  	doc /* The process of crediting the DTC participant account on settlement date with the position for the new issue. The overall closing process includes the exchange of funds that happens outside of DTC. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:DisseminationUnderwriter
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;PotentialMuniUnderwriter"/>
  }
  item def DTCCMember specializes MuniIssuanceProcessParticipant {
  	doc /* A firm which is a member of DTCC. Note: this Actor may perform any of the Roles described elsewhere in this Issuance model, i.e. the DTCC Member may also be any of the Actors defined. REVIEW: does this apply to ALL the defined Actor types? */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:DTCCMember
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;MuniIssuanceProcessParticipant"/>
  }
  item def EquityDemutualizationProcess specializes Issuance_Process::SecuritiesIssuanceProcess {
  	doc /* The process of crediting the DTC participant account on settlement date with the position for the new issue. The overall closing process includes the exchange of funds that happens outside of DTC. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:EquityDemutualizationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecuritiesIssuanceProcess"/>
  }
  item def EquityIPOProcess specializes Equities_IPO_Issuance::IPOProcess, RegisteredSecurityIssuanceProcess {
  	doc /* The process of crediting the DTC participant account on settlement date with the position for the new issue. The overall closing process includes the exchange of funds that happens outside of DTC. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:EquityIPOProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;RegisteredSecurityIssuanceProcess"/>
  }
  item def EquityUnderwritingIssuanceProcess specializes RegisteredSecurityIssuanceProcess {
  	doc /* The process of crediting the DTC participant account on settlement date with the position for the new issue. The overall closing process includes the exchange of funds that happens outside of DTC. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:EquityUnderwritingIssuanceProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;RegisteredSecurityIssuanceProcess"/>
  }
  item def IssuanceAgent { // RDF: fibo-bp-iss-muni:IssuanceAgent
    // RDF: fibo-bp-iss-muni:IssuanceAgent
  }
  item def IssuanceClosing specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* The process of crediting the DTC participant account on settlement date with the position for the new issue. The overall closing process includes the exchange of funds that happens outside of DTC. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:IssuanceClosing
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;IssuanceProcessActivity"/>
  }
  item def IssuanceCreditingParticipant { // RDF: fibo-bp-iss-muni:IssuanceCreditingParticipant
    // RDF: fibo-bp-iss-muni:IssuanceCreditingParticipant
  }
  item def IssuanceFinancialAdvisor { // RDF: fibo-bp-iss-muni:IssuanceFinancialAdvisor
    // RDF: fibo-bp-iss-muni:IssuanceFinancialAdvisor
  }
  item def IssuancePrinter { // RDF: fibo-bp-iss-muni:IssuancePrinter
    // RDF: fibo-bp-iss-muni:IssuancePrinter
  }
  item def IssuanceProcessParticipant specializes ProcessParticipant {
  	doc /* Terms for Change to an Issue Amount. A provision in an underwriting agreement, which allows members of the underwriting syndicate to purchase additional shares at the original price. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:IssuanceProcessParticipant
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;ProcessParticipant"/>
  }
  item def IssuanceSettlement specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* Terms for Change to an Issue Amount. A provision in an underwriting agreement, which allows members of the underwriting syndicate to purchase additional shares at the original price. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:IssuanceSettlement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;IssuanceProcessActivity"/>
  }
  item def IssueOverAllotmentTerms specializes IssuanceDocuments::OfferingDocumentTerms {
  	doc /* Terms for Change to an Issue Amount. A provision in an underwriting agreement, which allows members of the underwriting syndicate to purchase additional shares at the original price. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:IssueOverAllotmentTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-doc;OfferingDocumentTerms"/>
  }
  item def IssuerCounsel { // RDF: fibo-bp-iss-muni:IssuerCounsel
    // RDF: fibo-bp-iss-muni:IssuerCounsel
  }
  item def MuniDebtOffering specializes Debt_Issuance::DebtOffering {
  	doc /* The process step of offering a Municipal Debt security for issue. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:MuniDebtOffering
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;DebtOffering"/>
  }
  item def MuniIssuanceProcessParticipant specializes IssuanceProcessParticipant {
  	doc /* An intermediary between an issuer of a security and the investing public. An underwriter can either be DTC Participant or Correspondent who would clear the underwriting deal using the Crediting Participant. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:MuniIssuanceProcessParticipant
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;IssuanceProcessParticipant"/>
  }
  item def Obligor specializes Parties::PartyRole {
  	doc /* An intermediary between an issuer of a security and the investing public. An underwriter can either be DTC Participant or Correspondent who would clear the underwriting deal using the Crediting Participant. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (Agreements.rdf)
  	// RDF: fibo-bp-iss-muni:Obligor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
  item def PayingAgent specializes IssuanceAgent {
  	doc /* special purpose vehicle that pools mortgage loans together and issues mortgage-backed securities */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:PayingAgent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;IssuanceAgent"/>
  }
  item def PotentialMuniUnderwriter specializes Issuance_Process::PotentialUnderwriter {
  	doc /* An intermediary between an issuer of a security and the investing public. An underwriter can either be DTC Participant or Correspondent who would clear the underwriting deal using the Crediting Participant. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:PotentialMuniUnderwriter
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;PotentialUnderwriter"/>
  }
  item def PrivatePlacement specializes Issuance_Process::SecuritiesIssuanceProcess {
  	doc /* Underwriting, as a method of Securities Issuance. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:PrivatePlacement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecuritiesIssuanceProcess"/>
  }
  item def ProcessParticipant specializes Parties::PartyRole { // RDF: fibo-bp-iss-muni:ProcessParticipant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
      // RDF: fibo-bp-iss-muni:ProcessParticipant
  }
  item def RegisteredSecurityIssuanceProcess specializes Issuance_Process::SecuritiesIssuanceProcess { // RDF: fibo-bp-iss-muni:RegisteredSecurityIssuanceProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecuritiesIssuanceProcess"/>
      // RDF: fibo-bp-iss-muni:RegisteredSecurityIssuanceProcess
  }
  item def Registration specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* Underwriting, as a method of Securities Issuance. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:Registration
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;IssuanceProcessActivity"/>
  }
  item def RemarketingAgent { // RDF: fibo-bp-iss-muni:RemarketingAgent
    // RDF: fibo-bp-iss-muni:RemarketingAgent
  }
  item def SecuritiesIssuanceContext specializes Financial_Context_and_Process::PrimaryMarket {
  	doc /* Underwriting, as a method of Securities Issuance. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:SecuritiesIssuanceContext
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;PrimaryMarket"/>
  }
  item def SecuritiesUnderwritingIssuance specializes SecuritiesIssuanceContext {
  	doc /* Underwriting, as a method of Securities Issuance. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:SecuritiesUnderwritingIssuance
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;SecuritiesIssuanceContext"/>
  }
  item def SecuritiesUnderwritingIssuanceProcess specializes Issuance_Process::SecuritiesIssuanceProcess {
  	doc /* The process by which debt instruments are offered to the market by a syndicate of underwriters who underwrite the issue. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:SecuritiesUnderwritingIssuanceProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecuritiesIssuanceProcess"/>
  }
  item def Servicer specializes Financial_Products_and_Services::FinancialServiceProvider, Contracts::ContractThirdParty {
  	doc /* any actor within the DTCC Underwriting Process or any other Underwriting Process. Note: At present all these Actors (e.g. Underwriter) are defined simply as Process Actors and not as specializations of this type of Actor. Therefore this Actor is a Union of all of those. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-bp-iss-muni:Servicer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractThirdParty"/>
  }
  item def TransferAgent specializes IssuanceAgent { // RDF: fibo-bp-iss-muni:TransferAgent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;IssuanceAgent"/>
      // RDF: fibo-bp-iss-muni:TransferAgent
  }
  item def Trustee specializes Executives::LegallyDelegatedAuthority, FiBO_FND_ExternalStubs::OrganizationMember {
  	doc /* party that holds and manages assets for the benefit of another */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:Trustee
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;LegallyDelegatedAuthority"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
  }
  item def UnderwritingIssuanceClosing specializes IssuanceClosing {
  	doc /* any actor within the DTCC Underwriting Process or any other Underwriting Process. Note: At present all these Actors (e.g. Underwriter) are defined simply as Process Actors and not as specializations of this type of Actor. Therefore this Actor is a Union of all of those. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:UnderwritingIssuanceClosing
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;IssuanceClosing"/>
  }
  item def UnderwritingIssuanceRequestor specializes Issuance_Process::PotentialIssuer {
  	doc /* any actor within the DTCC Underwriting Process or any other Underwriting Process. Note: At present all these Actors (e.g. Underwriter) are defined simply as Process Actors and not as specializations of this type of Actor. Therefore this Actor is a Union of all of those. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:UnderwritingIssuanceRequestor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;PotentialIssuer"/>
  }
  item def UnderwritingProcessActivity specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* any actor within the DTCC Underwriting Process or any other Underwriting Process. Note: At present all these Actors (e.g. Underwriter) are defined simply as Process Actors and not as specializations of this type of Actor. Therefore this Actor is a Union of all of those. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:UnderwritingProcessActivity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;IssuanceProcessActivity"/>
  }
  item def UnderwritingProcessActor specializes Parties::PartyRole {
  	doc /* any actor within the DTCC Underwriting Process or any other Underwriting Process. Note: At present all these Actors (e.g. Underwriter) are defined simply as Process Actors and not as specializations of this type of Actor. Therefore this Actor is a Union of all of those. */
  	// skos:definition xml:lang="en" (MuniIssuance.rdf)
  	// rdfs:label xml:lang="en" (MuniIssuance.rdf)
  	// RDF: fibo-bp-iss-muni:UnderwritingProcessActor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
}

// --- PrivateLabelMBSIssuance.rdf ---
