// ===================================================================
// Markets.sysml
// Source-parity extracted from: FiBO_FBC_FunctionalEntities.sysml
// Primary source module: Markets.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/FunctionalEntities
// Structural note: translated as a split file from FiBO_FBC_FunctionalEntities umbrella.
// ===================================================================
package Markets {
  doc/* This ontology defines the fundamental concepts for markets, exchanges, regulated markets, and multilateral trading facilities. (source: dct:abstract, Markets.rdf) */
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

    item def <'alternative trading system'> AlternativeTradingSystem specializes Exchange {
      doc /* trading venue that is more loosely regulated than a regulated exchange */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:AlternativeTradingSystem
    }

    item def <'approved publication arrangement'> ApprovedPublicationArrangement specializes DataReportingServicesProvider {
      doc /* data reporting services provider that is authorized to provide the service of publishing certain trade reports on behalf of banks, investment firms, or asset management companies */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;DataReportingServicesProvider"/>
      // RDF: fibo-fbc-fct-mkt:ApprovedPublicationArrangement
    }

    item def <'approved reporting mechanism'> ApprovedReportingMechanism specializes DataReportingServicesProvider {
      doc /* data reporting services provider that is authorized to provide the service of reporting details of transactions to competent authorities or ESMA (the European Securities and Markets Authority) on behalf of investment firms */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;DataReportingServicesProvider"/>
      // RDF: fibo-fbc-fct-mkt:ApprovedReportingMechanism
    }

    item def <'auction market'> AuctionMarket specializes Exchange {
      doc /* exchange in which buyers and sellers submit competitive bids that do not involve direct negotiations between parties, as part of an auction scheduled either on demand or on a periodic basis */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:AuctionMarket
    }

    item def <'consolidated tape provider'> ConsolidatedTapeProvider specializes DataReportingServicesProvider {
      doc /* data reporting services provider that is authorized to provide the service of collecting trade reports for financial instruments from regulated markets, MTFs, OTFs and APAs and consolidating them into a continuous electronic live data stream providing price and volume data per financial instrument */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;DataReportingServicesProvider"/>
      // RDF: fibo-fbc-fct-mkt:ConsolidatedTapeProvider
    }

    item def <'crypto asset services provider'> CryptoAssetServicesProvider specializes Financial_Products_and_Services::FinancialServiceProvider {
      doc /* financial services provider that provides services for crypto assets that enable the control of crypto assets, and participate in, or provide, financial services for issuers' offers, or sale, of crypto assets */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
      // RDF: fibo-fbc-fct-mkt:CryptoAssetServicesProvider
    }

    item def <'dark pool'> DarkPool specializes AlternativeTradingSystem {
      doc /* privately organized alternative trading system, that allows institutional investors the ability to trade without exposure until after the trade has been executed and reported */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;AlternativeTradingSystem"/>
      // RDF: fibo-fbc-fct-mkt:DarkPool
    }

    item def <'data reporting services provider'> DataReportingServicesProvider specializes Publishers::MarketDataProvider, Reporting::ReportingParty {
      doc /* market data provider and reporting party that reports and/or publishes data on securities transactions, including required regulatory reporting for such transactions, and as such is subject to regulatory supervision */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-pub;MarketDataProvider"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rep;ReportingParty"/>
      // RDF: fibo-fbc-fct-mkt:DataReportingServicesProvider
      // subClassOf: ReportingParty - external CMNS
    }

    item def <'designated contract market'> DesignatedContractMarket specializes Exchange {
      doc /* exchange, trading system, or platform that enables listing for trading futures or option contracts based on any underlying commodity, index or instrument */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:DesignatedContractMarket
    }

    item def <'electronic communication network'> ElectronicCommunicationNetwork specializes AlternativeTradingSystem {
      doc /* alternative trading system that automatically matches buy and sell orders for securities in the market */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;AlternativeTradingSystem"/>
      // RDF: fibo-fbc-fct-mkt:ElectronicCommunicationNetwork
    }

    item def <'exchange'> Exchange specializes Facilities::Facility {
      doc /* any organization, association, or group of persons, whether incorporated or unincorporated, which constitutes, maintains, or provides a facility for bringing together purchasers and sellers of financial instruments, commodities, or other products, services, or goods, and includes the market place and facilities maintained by such exchange */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-sfc;Facility"/>
      // RDF: fibo-fbc-fct-mkt:Exchange
      // subClassOf: Facility - external CMNS
    }

    item def <'exchange participant'> ExchangeParticipant specializes Financial_Products_and_Services::RegisteredAgent, FiBO_FND_ExternalStubs::OrganizationMember{
      doc /* registered agent who, in accordance with the rules of an exchange, may trade on or through the exchange and whose name is entered in a list, register or roll kept by the exchange as an agent who may trade on or through the exchange */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;RegisteredAgent"/>
      // RDF: fibo-fbc-fct-mkt:ExchangeParticipant
      // subClassOf also: OrganizationMember - external CMNS
    }

    item def <'interdealer quotation system'> InterdealerQuotationSystem specializes Exchange {
      doc /* automated system for organizing and disseminating price quotes by brokers and dealer firms that facilitates electronic trading in securities */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:InterdealerQuotationSystem
    }

    item def <'market category classifier'> MarketCategoryClassifier specializes FiBO_FND_ExternalStubs::Classifier {
      doc /* classifier representing the controlled vocabulary that delineates the nature of the exchange or data reporting services provider where possible */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
      // RDF: fibo-fbc-fct-mkt:MarketCategoryClassifier
      // subClassOf: Classifier - external CMNS
    }

    item def <'market identifier'> MarketIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
      doc /* identifier that specifies a universal method of identifying exchanges, trading platforms, regulated or non-regulated markets, and data reporting services providers as sources of prices and related information in order to facilitate automated processing */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
      // RDF: fibo-fbc-fct-mkt:MarketIdentifier
      // subClassOf: Identifier - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def <'market indicator code status'> MarketIdentifierCodeStatus specializes Lifecycles::LifecycleStage {
      doc /* lifecycle stage indicating the status of the MIC code, as specified by the registration authority */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStage"/>
      // RDF: fibo-fbc-fct-mkt:MarketIdentifierCodeStatus
      // subClassOf: LifecycleStage - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'market level classifier'> MarketLevelClassifier specializes FiBO_FND_ExternalStubs::Classifier {
      doc /* classifier that indicates whether the exchange or data reporting services provider is an operating level or market segment level facility */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
      // RDF: fibo-fbc-fct-mkt:MarketLevelClassifier
      // subClassOf: Classifier - external CMNS
    }

    item def <'market segment-level market'> MarketSegmentLevelMarket specializes Exchange {
      doc /* section of an exchange/market/trade reporting facility that specialises in one or more specific instruments or that is regulated differently */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:MarketSegmentLevelMarket
    }

    item def <'market segment-level market identifier'> MarketSegmentLevelMarketIdentifier specializes MarketIdentifier {
      doc /* market identifier that identifies a section of an exchange/market/trade reporting facility that specialises in one or more specific instruments or that is regulated differently */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;MarketIdentifier"/>
      // RDF: fibo-fbc-fct-mkt:MarketSegmentLevelMarketIdentifier
    }

    item def <'multilateral trading facility'> MultilateralTradingFacility specializes AlternativeTradingSystem {
      doc /* trading system that facilitates the exchange of financial instruments between multiple parties */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;AlternativeTradingSystem"/>
      // RDF: fibo-fbc-fct-mkt:MultilateralTradingFacility
    }

    item def <'off-market facility'> OffMarketFacility specializes Exchange {
      doc /* facility used for reporting over-the-counter (OTC) and other direct trades that are not executed by the exchange but are reported through the exchange */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:OffMarketFacility
    }

    item def <'operating-level market'> OperatingLevelMarket specializes Exchange {
      doc /* exchange/market/trade reporting facility in a specific market/country */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:OperatingLevelMarket
    }

    item def <'operating-level market identifier'> OperatingLevelMarketIdentifier specializes MarketIdentifier {
      doc /* market identifier that identifies an exchange/market/trade reporting facility in a specific market/country */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;MarketIdentifier"/>
      // RDF: fibo-fbc-fct-mkt:OperatingLevelMarketIdentifier
    }

    item def <'organised trading facility'> OrganizedTradingFacility specializes AlternativeTradingSystem {
      doc /* multi-lateral system which is not an RM or an MTF and in which multiple third-party buying and selling interests in bonds, structured finance products, emission allowances or derivatives are able to interact in the system in a way that results in a contract in accordance with the provisions of Title II of MiFID II */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;AlternativeTradingSystem"/>
      // RDF: fibo-fbc-fct-mkt:OrganizedTradingFacility
    }

    item def <'quote-driven market'> QuoteDrivenMarket specializes Exchange {
      doc /* exchange in which prices are determined from bid and ask quotations made by market makers, dealers, or specialists */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:QuoteDrivenMarket
    }

    item def <'recognised market operator'> RecognizedMarketOperator specializes Exchange {
      doc /* exchange that is operated or maintained by an operator registered under certain securities regulations that brings together purchasers and sellers of capital market products */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:RecognizedMarketOperator
    }

    item def <'registered multilateral trading facility'> RegisteredMultilateralTradingFacility specializes MultilateralTradingFacility {
      doc /* multilateral system operated by an investment firm or market operator, which brings together multiple third-party buying and selling interests in financial instruments in the system, in accordance with non-discretionary rules, in a way that results in a contract in accordance with the provisions of Title II of the MiFID II */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;MultilateralTradingFacility"/>
      // RDF: fibo-fbc-fct-mkt:RegisteredMultilateralTradingFacility
    }

    item def <'regulated exchange'> RegulatedExchange specializes Exchange {
      doc /* regulated market that is operated by and/or managed by a market operator that brings together or facilitates the bringing together of multiple third-party buying and selling interests in financial instruments */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:RegulatedExchange
    }

    item def <'swap execution facility'> SwapExecutionFacility specializes Exchange {
      doc /* exchange that enables participants to execute and trade swaps */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:SwapExecutionFacility
    }

    item def <'systematic internaliser'> SystematicInternaliser specializes Exchange {
      doc /* investment firm that, on an organised, frequent, systematic and substantial basis, deals on its own account by executing client orders outside a regulated exchange, MTF or OTF without operating a multilateral system */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-mkt;Exchange"/>
      // RDF: fibo-fbc-fct-mkt:SystematicInternaliser
    }

    item def <'trade reporting facility'> TradeReportingFacility specializes Facilities::Facility {
      doc /* facility that provides a mechanism for the reporting of transactions effected otherwise than on an exchange */
      // skos:definition xml:lang="en" (Markets.rdf)
      // rdfs:label xml:lang="en" (Markets.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-sfc;Facility"/>
      // RDF: fibo-fbc-fct-mkt:TradeReportingFacility
      // subClassOf: Facility - external CMNS
    }

}

// --- fibo-fbc-fct-nic (USFinancialServices) ---
