// ===================================================================
// MortgagebackedSecurities.sysml
// Source-parity extracted from: FiBO_SEC_Debt.sysml
// Primary source module: MortgageBackedSecurities.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Debt
// ===================================================================
package Mortgage_backed_Securities {
  doc/* Mortgage backed securities are like asset backed securities except that the underlying loan pool is a pool of mortgage loans. (source: dct:abstract, MortgageBackedSecurities.rdf) */
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

// AssetBackedSecurity
// StructuredFinanceInstrument
// DebtPool
// PartyRole

  item def <'mortgage pool'> MortgagePool specializes Securities_Pools::DebtPool {
  	doc /* loan pool consisting of mortgages that are held in trust as collateral for the issuance of a mortgage-backed security */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:MortgagePool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def <'m b s pool'> MBSPool specializes Securities_Pools::DebtPool {
  	doc /* A pool investment consisting of a collection of MBS instruments. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:MBSPool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def <'agency m b s pool'> AgencyMBSPool specializes MBSPool {
  	doc /* A pool investment consisting of a collection of Agency MBS instruments. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:AgencyMBSPool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MBSPool"/>
  }
  item def <'agency mortgage pool'> AgencyMortgagePool specializes MortgagePool {
  	doc /* A pool of agency mortgages. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:AgencyMortgagePool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MortgagePool"/>
  }
  item def <'non agency mortgage pool'> NonAgencyMortgagePool specializes MortgagePool {
  	doc /* An issue of Mortgage Backed Security instruments in which payments on the pool are passed through to investors */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:NonAgencyMortgagePool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MortgagePool"/>
  }
  item def <'issued and securitized agency mortage pool'> IssuedAndSecuritizedAgencyMortagePool specializes AgencyMortgagePool {
  	doc /* An agency mortgage pool which has been securitized as part of an agency Mortgage Backed Security. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:IssuedAndSecuritizedAgencyMortagePool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;AgencyMortgagePool"/>
  }
  item def <'issued and securitized non agency mortgage pool'> IssuedAndSecuritizedNonAgencyMortgagePool specializes PrivateLabelMBSIssuance::NonAgencyMortgagePool {
  	doc /* A non agency mortgage pool which has been securitized as part of a tranched Mortgage Backed Security. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:IssuedAndSecuritizedNonAgencyMortgagePool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;NonAgencyMortgagePool"/>
  }
  item def FHLMCGoldPool{ // RDF: fibo-sec-dbt-mbs:FHLMC-GoldPool
    // RDF: fibo-sec-dbt-mbs:FHLMCGoldPool
  }
  item def FHLMCPool{ // RDF: fibo-sec-dbt-mbs:FHLMC-Pool
    // RDF: fibo-sec-dbt-mbs:FHLMCPool
  }
  item def FNMAPool{ // RDF: fibo-sec-dbt-mbs:FNMA-Pool
    // RDF: fibo-sec-dbt-mbs:FNMAPool
  }
  item def GNMAIIPool{ // RDF: fibo-sec-dbt-mbs:GNMA-IIPool
    // RDF: fibo-sec-dbt-mbs:GNMAIIPool
  }
  item def GNMAiPool{ // RDF: fibo-sec-dbt-mbs:GNMA-iPool
    // RDF: fibo-sec-dbt-mbs:GNMAiPool
  }
  item def GNMAPlatinumPool{ // RDF: fibo-sec-dbt-mbs:GNMA-PlatinumPool
    // RDF: fibo-sec-dbt-mbs:GNMAPlatinumPool
  }
  item def SBAPool{ // RDF: fibo-sec-dbt-mbs:SBA-Pool
    // RDF: fibo-sec-dbt-mbs:SBAPool
  }
  item def PassThroughPool{ // RDF: fibo-sec-dbt-mbs:Pass-throughPool
    // RDF: fibo-sec-dbt-mbs:PassThroughPool
  }
  item def <'mortgage-backed security'> MortgageBackedSecurity specializes Pool_backed_Securities::PoolBackedSecurity {
  	doc /* debt obligations that represent claims to the cash flows from pools of mortgage loans, most commonly on residential property */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:MortgageBackedSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;PoolBackedSecurity"/>
  }
  item def <'residential m b s'> ResidentialMBS specializes Mortgage_backed_Securities::MortgageBackedSecurity {
  	doc /* Residential Mortgage-Backed Securities, which are trust certificates (bonds) backed by a pool of residential mortgage loans. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:ResidentialMBS
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MortgageBackedSecurity"/>
  }
  item def <'commercial m b s'> CommercialMBS specializes Mortgage_backed_Securities::MortgageBackedSecurity {
  	doc /* An agency set up by a government for the purpose of issuing mortgages. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:CommercialMBS
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MortgageBackedSecurity"/>
  }
  item def <'real estate mortgage investment conduit'> RealEstateMortgageInvestmentConduit specializes Legal_Persons::SpecialPurposeVehicle {
  	doc /* special purpose vehicle that pools mortgage loans together and issues mortgage-backed securities */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:RealEstateMortgageInvestmentConduit
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-lp;SpecialPurposeVehicle"/>
  }
  item def <'mortgage-backed security offering prospectus'> MortgageBackedSecurityOfferingProspectus specializes Collective_Investment_Vehicles::Prospectus {
  	doc /* Prospectus describing the terms of the issue and each of the instruments included in the mortgage backed security issue. Term origin:SMER */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:MortgageBackedSecurityOfferingProspectus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;Prospectus"/>
  }
  item def <'agency m b s deal'> AgencyMBSDeal specializes Debt_Issuance::DebtOffering {
  	doc /* An issue of securities backed by pools of mortgages held by government agencies. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:AgencyMBSDeal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;DebtOffering"/>
  }
  item def <'private label m b s deal'> PrivateLabelMBSDeal specializes Debt_Issuance::DebtOffering {
  	doc /* special purpose vehicle that pools mortgage loans together and issues mortgage-backed securities */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:PrivateLabelMBSDeal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;DebtOffering"/>
  }
  item def PassThroughMBSDealMBS{ // RDF: fibo-sec-dbt-mbs:PassThroughMBSDeal (dedup with CDO pkg)
    // RDF: fibo-sec-dbt-mbs:PassThroughMBSDealMBS
  }
  item def PassThroughMBSDealProspectusMBS{ // RDF: fibo-sec-dbt-mbs:PassThroughMBSDealProspectus (dedup)
    // RDF: fibo-sec-dbt-mbs:PassThroughMBSDealProspectusMBS
  }
  item def <'pass through m b s instrument note'> PassThroughMBSInstrumentNote specializes Financial_Instruments::PromissoryNote {
  	doc /* An individual note of a pass through instrument. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:PassThroughMBSInstrumentNote
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;PromissoryNote"/>
  }
  item def <'m b s issuer'> MBSIssuer specializes Financial_Instruments::Issuer {
  	doc /* The issuer of a Mortgage Backed Security. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:MBSIssuer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Issuer"/>
  }
  item def <'agency m b s issuer'> AgencyMBSIssuer specializes MBSIssuer, GovernmentMortgageAgency {
  	doc /* The issuer of the pass through MBS is an Agency issuer. This is identified as being some kind of agency that is set up specifically to issue these instruments. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:AgencyMBSIssuer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;GovernmentMortgageAgency"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MBSIssuer"/>
  }
  item def <'non agency m b s issuer'> NonAgencyMBSIssuer specializes MBSIssuer {
  	doc /* A party which is a non agency issuer of MBS securities. The identity of this party may be a bank or a specialist (non agency) mortgage company. Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:NonAgencyMBSIssuer
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MBSIssuer"/>
  }
  item def <'government mortgage agency'> GovernmentMortgageAgency specializes Functional_Entities::FunctionalEntity {
  	doc /* An agency set up by a government for the purpose of issuing mortgages. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:GovernmentMortgageAgency
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalEntity"/>
  }
  item def <'specialist mortgage issuer'> SpecialistMortgageIssuer specializes Functional_Entities::FunctionalBusinessEntity {
  	doc /* The issuer of a Mortgage Backed Security. */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:SpecialistMortgageIssuer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalBusinessEntity"/>
  }
  item def <'pass through m b s issue underwriter'> PassThroughMBSIssueUnderwriter specializes Securities_Issuance::SecurityUnderwriter {
  	doc /* The party which agrees to buy any certificates that are not bought by investors Term origin:MBS PoC Reviews */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:PassThroughMBSIssueUnderwriter
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;SecurityUnderwriter"/>
  }
  item def <'paying agent'> PayingAgent specializes MuniIssuance::IssuanceAgent {
  	doc /* special purpose vehicle that pools mortgage loans together and issues mortgage-backed securities */
  	// skos:definition xml:lang="en" (MortgageBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (MortgageBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-mbs:PayingAgent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-muni;IssuanceAgent"/>
  }
}

// --- PoolBackedSecurities.rdf ---
