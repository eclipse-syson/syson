// ===================================================================
// EquitiesIPOIssuance.sysml
// Source-parity extracted from: FiBO_BP_SecuritiesIssuance.sysml
// Primary source module: EquitiesIPOIssuance.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Processes BP/SecuritiesIssuance
// ===================================================================
package Equities_IPO_Issuance {
  doc/* Issuance process for equity instruments that are issued via an Initial Public Offering (IPO). (source: dct:abstract, EquitiesIPOIssuance.rdf) */
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

  item def AgreeBasisForAllocation specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:AgreeBasisForAllocation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:AgreeBasisForAllocation
  }
  item def Allocation specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:Allocation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:Allocation
  }
  item def AllocationBasisDetails specializes FiBO_FND_ExternalStubs::Document { // RDF: fibo-bp-iss-eipo:AllocationBasisDetails
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
      // RDF: fibo-bp-iss-eipo:AllocationBasisDetails
  }
  item def AllocationDetails specializes FiBO_FND_ExternalStubs::Document { // RDF: fibo-bp-iss-eipo:AllocationDetails
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
      // RDF: fibo-bp-iss-eipo:AllocationDetails
  }
  item def AnnounceEquityIssue specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:AnnounceEquityIssue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:AnnounceEquityIssue
  }
  item def ApplicationForShares specializes FiBO_FND_ExternalStubs::Document { // RDF: fibo-bp-iss-eipo:ApplicationForShares
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
      // RDF: fibo-bp-iss-eipo:ApplicationForShares
  }
  item def AppointAdvisors specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:AppointAdvisors
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:AppointAdvisors
  }
  item def ApproveForFlotation specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:ApproveForFlotation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:ApproveForFlotation
  }
  item def CorporateBroker specializes Financial_Products_and_Services::Broker { // RDF: fibo-bp-iss-eipo:CorporateBroker
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Broker"/>
      // RDF: fibo-bp-iss-eipo:CorporateBroker
  }
  item def EquityAnnouncement specializes Issuance_Process::Announcement, FiBO_FND_ExternalStubs::Notice{ // RDF: fibo-bp-iss-eipo:EquityAnnouncement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Notice"/>
      // RDF: fibo-bp-iss-eipo:EquityAnnouncement
  }
  item def FilingDetails specializes FiBO_FND_ExternalStubs::Document { // RDF: fibo-bp-iss-eipo:FilingDetails
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
      // RDF: fibo-bp-iss-eipo:FilingDetails
  }
  item def FormalApprovalForListingAndTrading specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:FormalApprovalForListingAndTrading
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:FormalApprovalForListingAndTrading
  }
  item def IndicationOfInterest specializes FiBO_FND_ExternalStubs::Document { // RDF: fibo-bp-iss-eipo:IndicationOfInterest
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
      // RDF: fibo-bp-iss-eipo:IndicationOfInterest
  }
  item def InitialPublicOfferingProcessStep specializes FiBOOccurrences::OccurrenceKind { // RDF: fibo-bp-iss-eipo:InitialPublicOfferingProcessStep
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
      // RDF: fibo-bp-iss-eipo:InitialPublicOfferingProcessStep
  }
  item def IPOFullProspectus specializes IssuanceDocuments::FinalProspectus { // RDF: fibo-bp-iss-eipo:IPOFullProspectus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-doc;FinalProspectus"/>
      // RDF: fibo-bp-iss-eipo:IPOFullProspectus
  }
  item def IPOPreliminaryProspectus specializes IssuanceDocuments::PreliminaryProspectus { // RDF: fibo-bp-iss-eipo:IPOPreliminaryProspectus
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-doc;PreliminaryProspectus"/>
    // RDF: fibo-bp-iss-eipo:IPOPreliminaryProspectus
  }
  item def IPOProcess specializes Issuance_Process::SecuritiesIssuanceProcess { // RDF: fibo-bp-iss-eipo:IPOProcess
    // RDF: fibo-bp-iss-eipo:IPOProcess
  }
  item def IPOSettlementDetails specializes FiBO_FND_ExternalStubs::Document { // RDF: fibo-bp-iss-eipo:IPOSettlementDetails
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
      // RDF: fibo-bp-iss-eipo:IPOSettlementDetails
  }
  item def MarketingOfShareIssue specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:MarketingOfShareIssue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:MarketingOfShareIssue
  }
  item def NoticeOfAllocation specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:NoticeOfAllocation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:NoticeOfAllocation
  }
  item def PotentialShareUnderwriter specializes Issuance_Process::PotentialUnderwriter { // RDF: fibo-bp-iss-eipo:PotentialShareUnderwriter
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;PotentialUnderwriter"/>
      // RDF: fibo-bp-iss-eipo:PotentialShareUnderwriter
  }
  item def PublishInitialProspectus specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:PublishInitialProspectus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:PublishInitialProspectus
  }
  item def PurchasePrice specializes Currency_Amount::MonetaryPrice { // RDF: fibo-bp-iss-eipo:PurchasePrice
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryPrice"/>
      // RDF: fibo-bp-iss-eipo:PurchasePrice
  }
  item def PurchaseUnallocatedStock specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:PurchaseUnallocatedStock
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:PurchaseUnallocatedStock
  }
  item def ReceiveApplications specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:ReceiveApplications
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:ReceiveApplications
  }
  item def RegisterWithRegulatoryAuthority specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:RegisterWithRegulatoryAuthority
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:RegisterWithRegulatoryAuthority
  }
  item def RegistrationStatementDocument specializes FiBO_FND_ExternalStubs::Document { // RDF: fibo-bp-iss-eipo:RegistrationStatementDocument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
      // RDF: fibo-bp-iss-eipo:RegistrationStatementDocument
  }
  item def ReportingAccountant specializes Parties::PartyRole { // RDF: fibo-bp-iss-eipo:ReportingAccountant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
      // RDF: fibo-bp-iss-eipo:ReportingAccountant
  }
  item def SetPrice specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:SetPrice
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:SetPrice
  }
  item def SettlementOfAllocatedShares specializes InitialPublicOfferingProcessStep { // RDF: fibo-bp-iss-eipo:SettlementOfAllocatedShares
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-ipo;InitialPublicOfferingProcessStep"/>
      // RDF: fibo-bp-iss-eipo:SettlementOfAllocatedShares
  }
  item def ShareRegister specializes FiBO_FND_ExternalStubs::Registry { // RDF: fibo-bp-iss-eipo:ShareRegister
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;Registry"/>
      // RDF: fibo-bp-iss-eipo:ShareRegister
  }
  item def Sponsor specializes Parties::PartyRole { // RDF: fibo-bp-iss-eipo:Sponsor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
      // RDF: fibo-bp-iss-eipo:Sponsor
  }
  item def SyndicateMember specializes FiBO_FND_ExternalStubs::OrganizationMember, Parties::PartyRole {
  	doc /* role of an individual or organization that is a member of an alliance set up to pool resources and share risk with respect to a large transaction */
  	// skos:definition xml:lang="en" (EquitiesIPOIssuance.rdf)
  	// rdfs:label xml:lang="en" (EquitiesIPOIssuance.rdf)
  	// RDF: fibo-bp-iss-eipo:SyndicateMember
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
  item def Underwriting { // RDF: fibo-bp-iss-eipo:Underwriting
  }
    // RDF: fibo-bp-iss-eipo:Underwriting
}

// --- IssuanceDocuments.rdf ---
