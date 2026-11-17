// ===================================================================
// IssuanceProcess.sysml
// Source-parity extracted from: FiBO_BP_SecuritiesIssuance.sysml
// Primary source module: IssuanceProcess.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Processes BP/SecuritiesIssuance
// ===================================================================
package Issuance_Process {
  doc/* General ontology for the process by which securities are issued. Contains the process features common to all or most securities issuance. These are extended in the process ontologies that are specific to particular types of issuance (source: dct:abstract, IssuanceProcess.rdf) */
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

  item def AllotmentInformation specializes TradedInstrumentIssuanceProcessInformation {
  	doc /* Information about the allotment of quantities of the issue to different subscribers. This relates a single instrument allotment against the subscription amounts allotted to each Subscriber. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:AllotmentInformation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;TradedInstrumentIssuanceProcessInformation"/>
  }
  item def Announcement specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* No definition */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:Announcement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;IssuanceProcessActivity"/>
  }
  item def CompetitiveSaleMethod specializes SecurityOfferingSaleMethod {
  	doc /* No definition */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:CompetitiveSaleMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecurityOfferingSaleMethod"/>
  }
  item def IssuanceProgramme { // RDF: fibo-bp-iss-prc:IssuanceProgramme
    // RDF: fibo-bp-iss-prc:IssuanceProgramme
  }
  item def IssuedSecurityIssueInformation specializes FiBO_FND_ExternalStubs::Document {
  	doc /* FIBIM: &quot;Elements relating to issue preparation/bringing to market (also known as primary market or Initial Public Offering (IPO) issuance) through to issue date.&quot; */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:IssuedSecurityIssueInformation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
  }
  item def IssueSubscriptionInformation specializes TradedInstrumentIssuanceProcessInformation {
  	doc /* Information relating to the subscription of the issue. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:IssueSubscriptionInformation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;TradedInstrumentIssuanceProcessInformation"/>
  }
  item def NegotiatedSaleMethod specializes SecurityOfferingSaleMethod {
  	doc /* No definition */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:NegotiatedSaleMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecurityOfferingSaleMethod"/>
  }
  item def OfferingProcess specializes Debt_Issuance::IssuanceProcessActivity {
  	doc /* The process step of offering a security for issue. This is the making available of a new securities issue through an underwriting. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:OfferingProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;IssuanceProcessActivity"/>
  }
  item def PotentialIssuer specializes SecuritiesIssuanceProcessActor {
  	doc /* The entity which will become the underwriter for an issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:PotentialIssuer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecuritiesIssuanceProcessActor"/>
  }
  item def PotentialUnderwriter specializes SecuritiesIssuanceProcessActor {
  	doc /* The entity which will become the underwriter for an issue. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:PotentialUnderwriter
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecuritiesIssuanceProcessActor"/>
  }
  item def PrimaryIdentifierIssuer specializes Contracts::ContractThirdParty {
  	doc /* The party which formally issues the primary security identifier to the security. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:PrimaryIdentifierIssuer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractThirdParty"/>
  }
  item def PrimarySecurityOfferingDistribution specializes SecurityOfferingDistributionMethod {
  	doc /* The original sale of a company&apos;s securities, in which the proceeds from the sale are received directly by the company. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:PrimarySecurityOfferingDistribution
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecurityOfferingDistributionMethod"/>
  }
  item def RegistrationInformation specializes TradedInstrumentIssuanceProcessInformation {
  	doc /* Information relating to the registration of a registered security. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:RegistrationInformation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;TradedInstrumentIssuanceProcessInformation"/>
  }
  item def SecondarySecurityOfferingDistribution specializes SecurityOfferingDistributionMethod {
  	doc /* An Offering of a security which has been previously issued. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:SecondarySecurityOfferingDistribution
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecurityOfferingDistributionMethod"/>
  }
  item def SecuritiesIssuanceProcess { // RDF: fibo-bp-iss-prc:SecuritiesIssuanceProcess
    // RDF: fibo-bp-iss-prc:SecuritiesIssuanceProcess
  }
    item def SecuritiesIssuanceProcessActor specializes FiBO_FND_ExternalStubs::AgentRole {
  	doc /* The distribution type of a securities Offering. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:SecuritiesIssuanceProcessActor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;AgentRole"/>
  }
  item def SecuritiesOffering specializes Financial_Products_and_Services::Offering, Agreements::Agreement { // RDF: fibo-bp-iss-prc:SecuritiesOffering
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Offering"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Agreement"/>
      // RDF: fibo-bp-iss-prc:SecuritiesOffering
  }
  item def SecurityIssuanceGuarantor specializes SecuritiesIssuanceProcessActor {
  	doc /* The distribution type of a securities Offering. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:SecurityIssuanceGuarantor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecuritiesIssuanceProcessActor"/>
  }
  item def SecurityOfferingDistributionMethod specializes Objectives::DistributionStrategy {
  	doc /* The distribution type of a securities Offering. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:SecurityOfferingDistributionMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;DistributionStrategy"/>
  }
  item def SecurityOfferingSaleMethod specializes Objectives::SalesStrategy {
  	doc /* Method for sale of a new security offering. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:SecurityOfferingSaleMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;SalesStrategy"/>
  }
    item def Subscriber specializes FiBO_FND_ExternalStubs::AgentRole {
  	doc /* Information on one Security issue, arising from the Issuance Process. Note that one Issuance Process (Offering) may relate to more than on Issue, which itself may be the issue of more than one Traded Security. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:Subscriber
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;AgentRole"/>
  }
  item def SubscriptionClosingInformation specializes TradedInstrumentIssuanceProcessInformation {
  	doc /* Information on one Security issue, arising from the Issuance Process. Note that one Issuance Process (Offering) may relate to more than on Issue, which itself may be the issue of more than one Traded Security. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:SubscriptionClosingInformation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;TradedInstrumentIssuanceProcessInformation"/>
  }
  item def TradedInstrumentIssuanceProcessInformation specializes FiBO_FND_ExternalStubs::Document {
  	doc /* Information on one Security issue, arising from the Issuance Process. Note that one Issuance Process (Offering) may relate to more than on Issue, which itself may be the issue of more than one Traded Security. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:TradedInstrumentIssuanceProcessInformation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
  }
  item def UnderwriterTakedown specializes UnderwritingProcessDetails {
  	doc /* Infomation on Takedown quantity of the security handled by the underwriter (that will be brought into DTC). */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:UnderwriterTakedown
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;UnderwritingProcessDetails"/>
  }
  item def UnderwritingProcessDetails specializes TradedInstrumentIssuanceProcessInformation {
  	doc /* Information specific to the Underwriting of the Issue. */
  	// skos:definition xml:lang="en" (IssuanceProcess.rdf)
  	// rdfs:label xml:lang="en" (IssuanceProcess.rdf)
  	// RDF: fibo-bp-iss-prc:UnderwritingProcessDetails
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;TradedInstrumentIssuanceProcessInformation"/>
  }
  // NOTE: IssuanceProcessActivity also appears in DebtIssuance.rdf
}

// --- MBSIssuance.rdf ---
