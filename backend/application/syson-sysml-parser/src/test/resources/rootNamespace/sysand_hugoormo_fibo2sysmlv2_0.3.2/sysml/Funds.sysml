// ===================================================================
// Funds.sysml
// Source-parity extracted from: FiBO_SEC_Funds.sysml
// Primary source module: Funds.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Funds
// ===================================================================
package Funds {
  doc/* This ontology defines fundamental concepts about funds and collective investment vehicles (CIVs). (source: dct:abstract, Funds.rdf) */
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

// PartyRole
// CollectiveInvestmentVehicle, FundUnit

  item def <'aligned community investment fund'> AlignedCommunityInvestmentFund specializes Funds::CommunityInvestmentFund {
  	doc /* community investment fund that raises capital publicly from community members and deploys some of that capital via investments into its client businesses or other businesses in the community */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:AlignedCommunityInvestmentFund
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;CommunityInvestmentFund"/>
  }
  item def <'closed-end investment'> ClosedEndInvestment specializes Funds::ManagedInvestment {
  	doc /* investment fund that has a fixed number of shares offered by an investment company through an initial public offering */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:ClosedEndInvestment
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;ManagedInvestment"/>
  }
  item def <'community investment fund'> CommunityInvestmentFund specializes Funds::PrivateFund {
  	doc /* professionally-managed investment fund with three essential characteristics: capital is sourced from people in the community (ideally from retail/non-accredited investors); capital is invested into local people, projects, and businesses; and capital is deployed by individuals in the community, typically but not necessarily a nonprofit fund */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:CommunityInvestmentFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;PrivateFund"/>
  }
  item def <'diversified community investment fund'> DiversifiedCommunityInvestmentFund specializes Funds::CommunityInvestmentFund {
  	doc /* community investment fund that invests primarily in real estate but can also invest in any local business or project, and can do so with investment capital raised publicly from within the fund&apos;s own community, including community investors */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:DiversifiedCommunityInvestmentFund
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;CommunityInvestmentFund"/>
  }
  item def <'exchange-traded fund'> ExchangeTradedFund specializes Funds::OpenEndInvestment {
  	doc /* investment fund whose fund units are traded on an exchange, much like stocks */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:ExchangeTradedFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;OpenEndInvestment"/>
  }
  item def <'fund administrator'> FundAdministrator specializes Functional_Entities::ServiceProvider {
  	doc /* role of the party responsible for managing the operational, accounting, and compliance functions of an investment fund */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:FundAdministrator
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;ServiceProvider"/>
  }
  item def <'fund holder'> FundHolder specializes Corporate_Ownership::BeneficialOwner, Contracts::Counterparty {
  	doc /* role of the party responsible for making investment decisions and managing the portfolio of an investment fund */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:FundHolder
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cown;BeneficialOwner"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;Counterparty"/>
  }
  item def <'fund holding'> FundHolding specializes Accounting_Equity::FinancialAsset {
  	doc /* ownership interest in a fund, which may represented by fund units that confer financial rights and governance privileges */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:FundHolding
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;FinancialAsset"/>
  }
  item def <'fund manager'> FundManager specializes Functional_Entities::ServiceProvider {
  	doc /* role of the party responsible for making investment decisions and managing the portfolio of an investment fund */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:FundManager
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;ServiceProvider"/>
  }
  item def <'fund of funds'> FundOfFunds specializes Funds::ManagedInvestment {
  	doc /* investment fund that invests directly in other investment funds rather than investing in stocks, bonds, and other securities */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:FundOfFunds
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;ManagedInvestment"/>
  }
  item def <'fund position'> FundPosition specializes Financial_Products_and_Services::Position {
  	doc /* position in some fund, which may be defined in terms of fund units */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:FundPosition
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Position"/>
  }
  item def <'hedge fund'> HedgeFund specializes Funds::OpenEndInvestment {
  	doc /* investment fund that pursues a total return and is usually open to qualified investors only */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:HedgeFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;OpenEndInvestment"/>
  }
  item def <'legal fund structure'> LegalFundStructure specializes Legal_Capacity::LegalConstruct {
  	doc /* structure of a fund with respect to its legal formation in some jurisdiction */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:LegalFundStructure
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalConstruct"/>
  }
  item def <'managed investment'> ManagedInvestment specializes Collective_Investment_Vehicles::CollectiveInvestmentVehicle {
  	doc /* investment pool that is controlled by a professional investment manager who invests the pool in various financial instruments and assets that align with their investment objectives and is overseen by a board of directors */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:ManagedInvestment
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;CollectiveInvestmentVehicle"/>
  }
  item def <'mutual fund'> MutualFund specializes Funds::OpenEndInvestment {
  	doc /* open-end professionally managed investment fund established for the purpose of investing in securities such as stocks, bonds, money market instruments and similar assets */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:MutualFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;OpenEndInvestment"/>
  }
  item def <'nonprofit fund'> NonprofitFund specializes Funds::PrivateFund {
  	doc /* investment vehicle designed to support a nonprofit mission, whose objectives include environmental stewardship and/or social responsibility in addition to financial performance */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:NonprofitFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;PrivateFund"/>
  }
  item def <'non-tradable fund unit'> NonTradableFundUnit specializes Collective_Investment_Vehicles::FundUnit, Financial_Instruments::NonNegotiableSecurity {
  	doc /* security representing an interest in a fund that cannot be traded ontside of the fund itself */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:NonTradableFundUnit
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;NonNegotiableSecurity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;FundUnit"/>
  }
  item def <'open-end investment'> OpenEndInvestment specializes Funds::ManagedInvestment {
  	doc /* investment fund that offered through a fund company that sells shares directly to investors */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:OpenEndInvestment
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;ManagedInvestment"/>
  }
  item def <'pension fund'> PensionFund specializes Funds::ManagedInvestment {
  	doc /* investment fund run by a financial intermediary on behalf of an organization and its employees/members */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:PensionFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;ManagedInvestment"/>
  }
  item def <'pooled fund'> PooledFund specializes Accounting_Equity::FinancialAsset, Securities_Pools::Pool {
  	doc /* pool of funds that a group of investors combines for common benefit */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:PooledFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;FinancialAsset"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;Pool"/>
  }
  item def <'private credit fund'> PrivateCreditFund specializes Funds::PrivateFund {
  	doc /* investment vehicle that provides loans or other forms of credit to companies, typically outside of the traditional banking system */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:PrivateCreditFund
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;PrivateFund"/>
  }
  item def <'private equity fund'> PrivateEquityFund specializes Funds::PrivateFund {
  	doc /* investment fund used for making investments in various equity (and to a lesser extent debt) securities according to an investment strategy associated with private equity */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:PrivateEquityFund
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;PrivateFund"/>
  }
  item def <'private fund'> PrivateFund specializes Funds::ManagedInvestment {
  	doc /* managed investment that cannot offer securities to the public */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:PrivateFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;ManagedInvestment"/>
  }
  item def <'real estate investment trust'> RealEstateInvestmentTrust specializes Funds::ManagedInvestment {
  	doc /* investment fund that offers shares/units to the public and invests in real estate directly */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:RealEstateInvestmentTrust
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;ManagedInvestment"/>
  }
  item def <'sovereign wealth fund'> SovereignWealthFund specializes Collective_Investment_Vehicles::CollectiveInvestmentVehicle {
  	doc /* state-owned investment fund that consists of pools of money derived from a country&apos;s reserves */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:SovereignWealthFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;CollectiveInvestmentVehicle"/>
  }
  item def <'tradable fund unit'> TradableFundUnit specializes Collective_Investment_Vehicles::FundUnit, Financial_Instruments::EquityInstrument, Financial_Instruments::NegotiableSecurity {
  	doc /* security representing a tradable interest in a fund */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:TradableFundUnit
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;EquityInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;NegotiableSecurity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;FundUnit"/>
  }
  item def <'unitized fund'> UnitizedFund specializes Collective_Investment_Vehicles::CollectiveInvestmentVehicle {
  	doc /* pooled investment vehicle in which investors hold units that represent a proportional share of the fund&apos;s underlying assets, typically used in pensions or insurance-based products */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:UnitizedFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;CollectiveInvestmentVehicle"/>
  }
  item def <'unit trust'> UnitTrust specializes Funds::UnitizedFund {
  	doc /* pooled investment vehicle in which investors hold units that represent beneficial ownership in a trust-managed portfolio of assets */
  	// skos:definition xml:lang="en" (Funds.rdf)
  	// rdfs:label xml:lang="en" (Funds.rdf)
  	// RDF: fibo-sec-fund-fund:UnitTrust
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;UnitizedFund"/>
  }
}
