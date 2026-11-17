// ===================================================================
// CollectiveInvestmentVehicles.sysml
// Source-parity extracted from: FiBO_SEC_Funds.sysml
// Primary source module: CollectiveInvestmentVehicles.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Funds
// ===================================================================
package Collective_Investment_Vehicles {
  doc/* Reference data terms and non time dependent facts about funds and CIVs. (source: dct:abstract, CollectiveInvestmentVehicles.rdf) */
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

// ClassificationScheme
// PartyRole

  item def <'accumulating share class'> AccumulatingShareClass specializes FundShareClassUnit {
  	doc /* A share class in which there is no option to reinvest. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:AccumulatingShareClass
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundShareClassUnit"/>
  }
  item def <'asset class strategy'> AssetClassStrategy specializes FundPortfolioInvestmentLimitations {
  	doc /* Strategy which is asset class based. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:AssetClassStrategy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundPortfolioInvestmentLimitations"/>
  }
  item def <'collective investment vehicle'> CollectiveInvestmentVehicle specializes Funds::PooledFund {
  	doc /* assets pooled by investors whose share capital remains separate from the assets of the vehicle */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:CollectiveInvestmentVehicle
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;PooledFund"/>
  }
  item def <'common share in fund'> CommonShareInFund specializes FundShareClassUnit {
  	doc /* A share unit in a fund, which is classified as a Common Share class. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:CommonShareInFund
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundShareClassUnit"/>
  }
  item def <'currency strategy'> CurrencyStrategy specializes FundPortfolioInvestmentLimitations {
  	doc /* Strategy which is currency based. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:CurrencyStrategy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundPortfolioInvestmentLimitations"/>
  }
  item def <'distributing share class'> DistributingShareClass specializes FundShareClassUnit {
  	doc /* A fund which invests in at least 85% shares. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:DistributingShareClass
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundShareClassUnit"/>
  }
  item def <'equity fund'> EquityFund specializes CollectiveInvestmentVehicle {
  	doc /* A fund which invests in at least 85% shares. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:EquityFund
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;CollectiveInvestmentVehicle"/>
  }
  item def <'equity portfolio'> EquityPortfolio specializes FundPortfolio {
  	doc /* A portfolio which has at least 85% exposure to shares. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:EquityPortfolio
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundPortfolio"/>
  }
  item def <'fonds commun de placement'> FondsCommunDePlacement specializes CollectiveInvestmentVehicle {
  	doc /* open-ended collective investment fund, which is a contractual form set up between the fund manager and investors and not a separate legal entity */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FondsCommunDePlacement
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;CollectiveInvestmentVehicle"/>
  }
  item def <'fund accountant'> FundAccountant specializes FundsProcessingParty {
  	doc /* The party that keeps accounting records of the available assets and liabilities of the Fund. It calculates dealing prices, the Net Asset Value (NAV) of the Fund, and may provide fund performance and tax data. Can be subcontracted by the FundAdministrator. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundAccountant
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundsProcessingParty"/>
  }
  item def <'fund bond class unit'> FundBondClassUnit specializes FundUnit {
  	doc /* A fund unit which takes the form of debt in that fund. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundBondClassUnit
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;FundUnit"/>
  }
  item def <'fund bond unit coupon'> FundBondUnitCoupon specializes Currency_Amount::InterestRate {
  	doc /* A fixed coupon paid out to holders of the Fund Bond Unit. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundBondUnitCoupon
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;InterestRate"/>
  }
  item def <'fund cash distribution policy'> FundCashDistributionPolicy specializes FundUnitDistributionPolicy {
  	doc /* Policy for distribution of cash dividends or notes in the event that the fund units have a distribution policy of &quot;Cash&quot; rather than &quot;Reinvestment&quot;. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundCashDistributionPolicy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundUnitDistributionPolicy"/>
  }
  item def <'fund classification'> FundClassification specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* The category of the fund according to the asset class. This is a published code by which the Fund is classified. Further notes: Could be EFCF codes. This is the way in which &quot;Type of fund&quot; is articulated. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundClassification
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'fund classification scheme'> FundClassificationScheme specializes Assessments::ClassificationScheme {
  	doc /* A published scheme for the category of a fund according to the asset class. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundClassificationScheme
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;ClassificationScheme"/>
  }
  item def <'fund contract'> FundContract specializes Legal_Persons::OrganizationCoveringAgreement {
  	doc /* contract that embodies and defines the fund legal form in cases where there is no independent organization */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-fbo;OrganizationCoveringAgreement"/>
  }
  item def <'fund coupon policy'> FundCouponPolicy specializes FundCashDistributionPolicy {
  	doc /* Terms for the expected distribution of bond unit coupons. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundCouponPolicy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundCashDistributionPolicy"/>
  }
  item def <'fund depositary'> FundDepositary specializes FundsProcessingParty {
  	doc /* The party that holds and safeguards holdings owned by a fund. It is also responsible for compliance of the portfolio with legal ratios etc. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundDepositary
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundsProcessingParty"/>
  }
  item def <'fund distribution policy'> FundDistributionPolicy specializes Legal_Capacity::Policy {
  	doc /* policy indicating the overall strategy or limitations on distribution for the fund */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundDistributionPolicy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Policy"/>
  }
  item def <'fund distributor'> FundDistributor specializes FundsProcessingParty {
  	doc /* an entity through which a fund may be bought by or for an investor. This may be on a discretionary or advisory basis, or as an execution-only service. Definition origin:EFAMA DD */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundDistributor
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundsProcessingParty"/>
  }
  item def <'fund dividend policy'> FundDividendPolicy specializes FundCashDistributionPolicy {
  	doc /* Terms for the expected distributions of dividends. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundDividendPolicy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundCashDistributionPolicy"/>
  }
  item def <'fund investment objective'> FundInvestmentObjective specializes Objectives::InvestmentObjective {
  	doc /* The aim of a Fund e.g outperfom a given benchmark. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundInvestmentObjective
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;InvestmentObjective"/>
  }
  item def <'fund investment policy'> FundInvestmentPolicy specializes Legal_Capacity::Policy {
  	doc /* policy that the fund implements in order to achieve the stated fund objectives */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundInvestmentPolicy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Policy"/>
  }
  item def <'fund legal form documentation'> FundLegalFormDocumentation specializes Legal_Core::Constitution {
  	doc /* For a fund which is constituted under the law of contract, the constitution or articles that define the fund. These are embodied in a Contract. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundLegalFormDocumentation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-cor;Constitution"/>
  }
  item def <'fund order desk'> FundOrderDesk specializes Clients_and_Accounts::AccountProvider {
  	doc /* The Fund Order Desk is a party to the Fund Order Desk Account. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundOrderDesk
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;AccountProvider"/>
  }
  item def <'fund portfolio'> FundPortfolio specializes Security_Assets::Portfolio {
  	doc /* An account containing a number of financial instruments along with cash positions in one or more currencies and belonging to a Fund */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundPortfolio
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-ast;Portfolio"/>
  }
  item def <'fund portfolio investment limitations'> FundPortfolioInvestmentLimitations specializes InvestmentRestriction {
  	doc /* Detailed Policy on approximately how the portfolio is to be allocated to achieve the stated investment goals. This underpins the detailed strategy of how to achieve this. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundPortfolioInvestmentLimitations
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;InvestmentRestriction"/>
  }
  item def <'fund portfolio investment policy'> FundPortfolioInvestmentPolicy specializes Legal_Capacity::Policy {
  	doc /* policy with respect to allocation of the portfolio that is designed to meet the stated investment strategy and goals */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundPortfolioInvestmentPolicy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Policy"/>
  }
  item def <'fund processing general terms'> FundProcessingGeneralTerms specializes FundProcessingTerms {
  	doc /* Formal terms for general processing of the fund. These set out what the investor and the fund may or may not do. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundProcessingGeneralTerms
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundProcessingTerms"/>
  }
  item def <'fund processing terms'> FundProcessingTerms specializes Contracts::ContractualCommitment {
  	doc /* Formal terms for processing of the fund. These set out what the investor and the fund may or may not do. These include terms for redemption and subscription processing as well as general processing terms. ISO FIBIM definition: Processing characteristics linked to the instrument, ie, not to the market. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundProcessingTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
  }
  item def <'fund redemption restriction'> FundRedemptionRestriction specializes Securities_Restrictions::SecuritiesRestriction {
  	doc /* Formal terms for redemption of units in the fund. These set out what the investor and the fund may or may not do. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundRedemptionRestriction
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-rst;SecuritiesRestriction"/>
  }
  item def <'fund redemption terms'> FundRedemptionTerms specializes FundProcessingTerms {
  	doc /* Formal terms for redemption of units in the fund. These set out what the investor and the fund may or may not do. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundRedemptionTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundProcessingTerms"/>
  }
  item def <'fund reinvestment policy'> FundReinvestmentPolicy specializes FundUnitDistributionPolicy {
  	doc /* Terms for the expected reinvestment of profits on fund units. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundReinvestmentPolicy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundUnitDistributionPolicy"/>
  }
  item def <'funds cash distribution'> FundsCashDistribution specializes FundUnitDistributionMethod {
  	doc /* Accrued income is distributed periodically to the investor. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundsCashDistribution
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundUnitDistributionMethod"/>
  }
  item def <'fund share class unit'> FundShareClassUnit specializes FundUnit {
  	doc /* The legal structure in which you can purchase part of an investment pool, defined by a variety of characteristics like investor type, minimum size of investment, distribution type, fee and currency. A fund unit which gives the holder an equity stake in the fund. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundShareClassUnit
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;FundUnit"/>
  }
  item def <'funds processing'> FundsProcessing specializes Lifecycles::LifecycleStage {
  	doc /* The context of managing and processing funds, issuing funds units etc. This is the context in which the different parties involved in a fund are defined as parties. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundsProcessing
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStage"/>
  }
  item def <'funds processing account'> FundsProcessingAccount specializes Clients_and_Accounts::Account {
  	doc /* An account used specifically in the processing of funds. Like all accounts this is (per FIBIM definition) a business relationship between two entities; one entity is the account owner, the other entity is the account servicer. Please refer to Financial global model for treatment of accounts relationships in this model. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundsProcessingAccount
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;Account"/>
  }
  item def <'funds processing party'> FundsProcessingParty specializes Parties::PartyRole {
  	doc /* party involved in the processing of funds */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundsProcessingParty
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
  item def <'fund subscription terms'> FundSubscriptionTerms specializes FundProcessingTerms {
  	doc /* Subscription terms for the fund. Further notes: ISO FIBIM, EFAMA DD and FPP combine terms for subscription, redemption and general holding requirements. These have been separated here as they are different kinds of term, but this can be reviewed. Subscription Terms are identified as terms of the fund and not the fund unit, since terms for how you might subscribe can&apos;t be binding one someone who has not yet subscribed. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundSubscriptionTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundProcessingTerms"/>
  }
  item def <'fund supervisory authority'> FundSupervisoryAuthority specializes Parties::PartyRole {
  	doc /* party that supervises the fund or fund industry */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundSupervisoryAuthority
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
  item def <'fund transfer agent'> FundTransferAgent specializes FundsProcessingParty {
  	doc /* An entity that undertakes the execution of subscription, redemption and switch orders on behalf of a fund. Definition origin:EFAMA DD */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundTransferAgent
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundsProcessingParty"/>
  }
  item def <'fund unit'> FundUnit specializes Legal_Capacity::ContractualInterest {
  	doc /* investment fund that pursues a total return and is usually open to qualified investors only */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundUnit
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;ContractualInterest"/>
  }
  item def <'fund unit distribution method'> FundUnitDistributionMethod specializes Objectives::DistributionStrategy {
  	doc /* The normal distribution policy for funds. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundUnitDistributionMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;DistributionStrategy"/>
  }
  item def <'fund unit distribution policy'> FundUnitDistributionPolicy specializes Legal_Capacity::Policy {
  	doc /* policy relating to the unit, e.g. if income is paid out or retained in the fund and how this is treated, including distribution policy details for dividends and coupons. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:FundUnitDistributionPolicy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Policy"/>
  }
  item def <'income accumulation'> IncomeAccumulation specializes FundUnitDistributionMethod {
  	doc /* Accrued income is not distributed and instead remains reflected within the unit/share price. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:IncomeAccumulation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundUnitDistributionMethod"/>
  }
  item def <'fund investment restrictions set'> InvestmentRestriction specializes Securities_Restrictions::SecuritiesRestriction {
  	doc /* Limitations that apply to the fund as a whole, such as risk factors. these are used to determine whether the fund is appropriate for a given type of investor to invest in. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:InvestmentRestriction
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-rst;SecuritiesRestriction"/>
  }
  item def <'investment strategy'> InvestmentStrategy specializes Objectives::BusinessStrategy {
  	doc /* plan, method, or series of maneuvers or stratagems for obtaining a specific investment goal */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:InvestmentStrategy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;BusinessStrategy"/>
  }
  item def <'jurisdiction strategy'> JurisdictionStrategy specializes FundPortfolioInvestmentLimitations {
  	doc /* Strategy which is jurisdiction based. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:JurisdictionStrategy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundPortfolioInvestmentLimitations"/>
  }
  item def <'key investor information document'> KeyInvestorInformationDocument specializes IssuanceDocuments::OfferingDocument {
  	doc /* Parameters for the calculation of the net asset value for an investment fund/fund class. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:KeyInvestorInformationDocument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;OfferingDocument"/>
  }
  item def <'net asset value calculation method'> NetAssetValueCalculationMethod specializes Analytics::Expression {
  	doc /* Parameters for the calculation of the net asset value for an investment fund/fund class. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:NetAssetValueCalculationMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
  }
  item def <'note fund'> NoteFund specializes Funds::ManagedInvestment {
  	doc /* Strategy which is organization based. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:NoteFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;ManagedInvestment"/>
  }
  item def <'note fund share'> NoteFundShare specializes FundUnit {
  	doc /* investment interest in a fund that issues notes rather than traditional equity, in cases where the fund is structured as a company */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:NoteFundShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;FundUnit"/>
  }
  item def <'note fund unit'> NoteFundUnit specializes FundUnit {
  	doc /* investment interest in a fund that issues notes rather than traditional equity, in cases where the fund is structured as a trust, partnership, or unitized fund */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:NoteFundUnit
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-fund;FundUnit"/>
  }
  item def <'organization strategy'> OrganizationStrategy specializes FundPortfolioInvestmentLimitations {
  	doc /* Strategy which is organization based. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:OrganizationStrategy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundPortfolioInvestmentLimitations"/>
  }
  item def <'other investment fund information'> OtherInvestmentFundInformation specializes FiBO_FND_ExternalStubs::Document {
  	doc /* Things which are not part of the prospectus but are important if you want to understand the fund. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:OtherInvestmentFundInformation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
  }
  item def <'portfolio benchmark'> PortfolioBenchmark specializes FiBO_FND_ExternalStubs::Measure {
  	doc /* Security or other price against which the performance of the portfolio is evaluated. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:PortfolioBenchmark
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Measure"/>
  }
  item def <'portfolio investment strategy'> PortfolioInvestmentStrategy specializes InvestmentStrategy {
  	doc /* The manner in which the portfolio manager tries to reach the funds objectives. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:PortfolioInvestmentStrategy
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;InvestmentStrategy"/>
  }
  item def <'preferred share in fund'> PreferredShareInFund specializes FundShareClassUnit {
  	doc /* A holding of private equity. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:PreferredShareInFund
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundShareClassUnit"/>
  }
  item def <'private equity holding'> PrivateEquityHolding specializes Security_Assets::Portfolio {
  	doc /* A holding of private equity. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:PrivateEquityHolding
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-ast;Portfolio"/>
  }
  item def <'prospectus'> Prospectus specializes IssuanceDocuments::OfferingDocument {
  	doc /* formal, written offering document to sell securities that provides the facts an investor needs to make an informed investment decision */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:Prospectus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;OfferingDocument"/>
  }
  item def <'real estate holding'> RealEstateHolding specializes Security_Assets::Portfolio {
  	doc /* tangible asset consisting of real estate that is owned or controlled by an individual, organization, or entity for investment, operational, or strategic purposes */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:RealEstateHolding
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-ast;Portfolio"/>
  }
  item def <'refer to fund order desk'> ReferToFundOrderDesk specializes FundRedemptionRestriction {
  	doc /* Restriction requiring an investor to refer to the fund order desk prior to redeeming a fund. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:ReferToFundOrderDesk
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundRedemptionRestriction"/>
  }
  item def <'risk level'> RiskLevel specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* A French fund vehicle type. Designation and definition needed. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:RiskLevel
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'sector strategy'> SectorStrategy specializes FundPortfolioInvestmentLimitations {
  	doc /* Strategy which is sector based. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:SectorStrategy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundPortfolioInvestmentLimitations"/>
  }
  item def <'s i c a f'> SICAF specializes CollectiveInvestmentVehicle {
  	doc /* A French fund vehicle type. Designation and definition needed. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:SICAF
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;CollectiveInvestmentVehicle"/>
  }
  item def <'s i c a v'> SICAV specializes CollectiveInvestmentVehicle {
  	doc /* Societe Collective a Capital Variable */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:SICAV
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;CollectiveInvestmentVehicle"/>
  }
  item def <'unit issuer'> UnitIssuer specializes FundsProcessingParty {
  	doc /* The party which issues the Fund Unit. */
  	// skos:definition xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// rdfs:label xml:lang="en" (CollectiveInvestmentVehicles.rdf)
  	// RDF: fibo-sec-fund-civ:UnitIssuer
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-fund-civ;FundsProcessingParty"/>
  }
}

// --- CollectiveInvestmentVehicles.rdf ---
