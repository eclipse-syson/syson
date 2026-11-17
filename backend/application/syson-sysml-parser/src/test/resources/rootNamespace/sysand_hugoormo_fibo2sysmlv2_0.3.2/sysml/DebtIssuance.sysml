// ===================================================================
// DebtIssuance.sysml
// Source-parity extracted from: FiBO_BP_SecuritiesIssuance.sysml
// Primary source module: DebtIssuance.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Processes BP/SecuritiesIssuance
// ===================================================================
package Debt_Issuance {
  doc/* General issuance process for issuance of debt instruments. Forms the basis for more detailed issuance processes such as MBS issuance and municipal bonds issue. (source: dct:abstract, DebtIssuance.rdf) */
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

  item def ABSSecuritizationProcess specializes PoolBackedSecuritySecuritizationProcess {
  	doc /* refunding in which bond issuance in which new bonds are sold at a lower rate than outstanding ones */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:ABSSecuritizationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;PoolBackedSecuritySecuritizationProcess"/>
  }
  item def AdvancedRefunding specializes RefundingPurpose {
  	doc /* refunding in which bond issuance in which new bonds are sold at a lower rate than outstanding ones */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:AdvancedRefunding
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;RefundingPurpose"/>
  }
  item def AssetPoolCreationProcess { // RDF: fibo-bp-iss-dbti:AssetPoolCreationProcess
    // RDF: fibo-bp-iss-dbti:AssetPoolCreationProcess
  }
  item def BondIssuanceProgramme specializes Issuance_Process::IssuanceProgramme, DebtIssuanceProgramme {
  	doc /* a debt issuance programe under which an entity may, from time to time, issue bonds under the terms and conditions specified in the base prospectus for that programme */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:BondIssuanceProgramme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;DebtIssuanceProgramme"/>
  }
  item def BondOffering specializes DebtOffering {
  	doc /* a debt offering of one or more bonds, as all or part of an issuance programme */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:BondOffering
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;DebtOffering"/>
  }
  item def BondOfferingWithWarrant specializes BondOffering {
  	doc /* a bond offering where the issue includes a warrant; Further notes: ISO 10962 CFI definition is A bond that is issued together with one or more warrant(s) attached as part of the offer, the warrant(s) granting the holder the right to purchase a designated security, often the common stock of the issuer of the debt, at a specified price. Review notes: This need not be any specific type of bond. The warrant is used as a sweetener to encourage people to subscribe to a new bond issue. The Bond and the Warrant trade together as a unit (called &quot;Bond Unit&quot;). */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:BondOfferingWithWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;BondOffering"/>
  }
  item def CDOSecuritizationProcess specializes DebtSecuritizationProcess {
  	doc /* information specific to the issuance of a debt security */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:CDOSecuritizationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;DebtSecuritizationProcess"/>
  }
  item def CompletedDebtsPool specializes Securities_Pools::DebtPool {
  	doc /* information specific to the issuance of a debt security */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:CompletedDebtsPool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def CreditCardPoolCreationProcess specializes RetailAssetPoolCreationProcess {
  	doc /* information specific to the issuance of a debt security */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:CreditCardPoolCreationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;RetailAssetPoolCreationProcess"/>
  }
  item def DebtInstrumentPoolCreationProcess specializes AssetPoolCreationProcess {
  	doc /* information specific to the issuance of a debt security */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:DebtInstrumentPoolCreationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;AssetPoolCreationProcess"/>
  }
  item def DebtIssuanceProcessInformation specializes Issuance_Process::TradedInstrumentIssuanceProcessInformation {
  	doc /* information specific to the issuance of a debt security */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:DebtIssuanceProcessInformation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;TradedInstrumentIssuanceProcessInformation"/>
  }
  item def DebtIssuanceProgramme specializes Issuance_Process::IssuanceProgramme {
  	doc /* a series of debt security issuances over time */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:DebtIssuanceProgramme
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;IssuanceProgramme"/>
  }
  item def DebtIssuancePurpose specializes Objectives::Objective {
  	doc /* purpose for the issuance of a debt security; also defines processes to be followed in some instances */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:DebtIssuancePurpose
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Objective"/>
  }
  item def DebtOffering specializes Issuance_Process::SecuritiesOffering {
  	doc /* an offering of one or more debt instruments for sale */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:DebtOffering
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;SecuritiesOffering"/>
  }
  item def DebtSecuritizationProcess specializes Issuance_Process::SecuritiesIssuanceProcess {
  	doc /* An activity within the process of securities issuance. */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:DebtSecuritizationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecuritiesIssuanceProcess"/>
  }
  item def HomeEquityLineOfCreditPoolCreationProcess specializes RetailAssetPoolCreationProcess {
  	doc /* An activity within the process of securities issuance. */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:HomeEquityLineOfCreditPoolCreationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;RetailAssetPoolCreationProcess"/>
  }
  item def InAssembly specializes PrivateLabelMBSIssuance::PoolLifecycleStateSelection {
  	doc /* An activity within the process of securities issuance. */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:InAssembly
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-pmbs;PoolLifecycleStateSelection"/>
  }
  item def InAssemblyDebtsPool specializes Securities_Pools::DebtPool {
  	doc /* An activity within the process of securities issuance. */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:InAssemblyDebtsPool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def InIssuance specializes PrivateLabelMBSIssuance::PoolLifecycleStateSelection {
  	doc /* An activity within the process of securities issuance. */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:InIssuance
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-pmbs;PoolLifecycleStateSelection"/>
  }
  item def IssuanceProcessActivity specializes FiBOOccurrences::OccurrenceKind {
  	doc /* A series of issuances over time. */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:IssuanceProcessActivity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
  }
  item def Issued specializes PrivateLabelMBSIssuance::PoolLifecycleStateSelection, SecurityTradingStatuses::SecurityLifecycleStatus {
  	doc /* a program of offerings of medium term notes; a set of issues where the maturity is defined after the rest of the terms have been registered with some authority; these are registered up front so that then the company wants to borrow more money they don&apos;t have to go through the registration period but have the facility up front to issue another security. */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:Issued
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-pmbs;PoolLifecycleStateSelection"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-temx-trs;SecurityLifecycleStatus"/>
  }
  item def LoanPoolCreationProcess specializes RetailAssetPoolCreationProcess {
  	doc /* a program of offerings of medium term notes; a set of issues where the maturity is defined after the rest of the terms have been registered with some authority; these are registered up front so that then the company wants to borrow more money they don't have to go through the registration period but have the facility up front to issue another security. */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:LoanPoolCreationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;RetailAssetPoolCreationProcess"/>
  }
  item def MediumTermNoteIssuanceProgramme specializes Issuance_Process::IssuanceProgramme, BondIssuanceProgramme {
  	doc /* a program of offerings of medium term notes; a set of issues where the maturity is defined after the rest of the terms have been registered with some authority; these are registered up front so that then the company wants to borrow more money they don't have to go through the registration period but have the facility up front to issue another security. */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:MediumTermNoteIssuanceProgramme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;BondIssuanceProgramme"/>
  }
  item def MediumTermNoteOffering specializes BondOffering {
  	doc /* The party which agrees to buy any certificates that are not bought by investors Term origin:DTCC */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:MediumTermNoteOffering
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;BondOffering"/>
  }
  item def MTNRegistration specializes MuniIssuance::Registration {
  	doc /* a program of offerings of medium term notes; a set of issues where the maturity is defined after the rest of the terms have been registered with some authority; these are registered up front so that then the company wants to borrow more money they don't have to go through the registration period but have the facility up front to issue another security. */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:MTNRegistration
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;Registration"/>
  }
  item def MuniIssueUnderwriter specializes Securities_Issuance::SecurityUnderwriter {
  	doc /* The party which agrees to buy any certificates that are not bought by investors Term origin:DTCC */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:MuniIssueUnderwriter
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;SecurityUnderwriter"/>
  }
  item def NewMoneyPurpose specializes DebtIssuancePurpose {
  	doc /* securities issued to raise funds for a new project or purpose */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:NewMoneyPurpose
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;DebtIssuancePurpose"/>
  }
  item def NotYetIssued specializes PrivateLabelMBSIssuance::PoolLifecycleStateSelection {
  	doc /* a procedure whereby an issuer refinances outstanding bonds by issuing new bonds */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:NotYetIssued
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-pmbs;PoolLifecycleStateSelection"/>
  }
  item def NotYetIssuedDebtsPool specializes Securities_Pools::DebtPool {
  	doc /* a procedure whereby an issuer refinances outstanding bonds by issuing new bonds */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:NotYetIssuedDebtsPool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def PoolBackedSecuritySecuritizationProcess specializes DebtSecuritizationProcess {
  	doc /* a procedure whereby an issuer refinances outstanding bonds by issuing new bonds */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:PoolBackedSecuritySecuritizationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;DebtSecuritizationProcess"/>
  }
  item def PoolBackedSecuritySecuritizationProcessActor specializes SecuritizationProcessActor {
  	doc /* a procedure whereby an issuer refinances outstanding bonds by issuing new bonds */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:PoolBackedSecuritySecuritizationProcessActor
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;SecuritizationProcessActor"/>
  }
  item def RefundingPurpose specializes DebtIssuancePurpose {
  	doc /* a procedure whereby an issuer refinances outstanding bonds by issuing new bonds */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:RefundingPurpose
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;DebtIssuancePurpose"/>
  }
  item def RemarketingPurpose specializes DebtIssuancePurpose {
  	doc /* the process of reselling securities to the public that have been tendered for purchase by the previous owners thereof */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:RemarketingPurpose
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;DebtIssuancePurpose"/>
  }
  item def RetailAssetPoolCreationProcess specializes AssetPoolCreationProcess {
  	doc /* The process by which pools of assets are created. These may then be used in the issue of securities based on those asset pools as underlying. */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:RetailAssetPoolCreationProcess
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;AssetPoolCreationProcess"/>
  }
  item def SecuritizationProcessActor specializes Issuance_Process::SecuritiesIssuanceProcessActor {
  	doc /* information on takedown quantity of the debt security handled by the underwriter (that will be brought into DTC) */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:SecuritizationProcessActor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;SecuritiesIssuanceProcessActor"/>
  }
  item def UnderwriterTakedownForDebt specializes Issuance_Process::UnderwriterTakedown {
  	doc /* information on takedown quantity of the debt security handled by the underwriter (that will be brought into DTC) */
  	// skos:definition xml:lang="en" (DebtIssuance.rdf)
  	// rdfs:label xml:lang="en" (DebtIssuance.rdf)
  	// RDF: fibo-bp-iss-dbti:UnderwriterTakedownForDebt
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-prc;UnderwriterTakedown"/>
  }
}

// --- EquitiesIPOIssuance.rdf ---
