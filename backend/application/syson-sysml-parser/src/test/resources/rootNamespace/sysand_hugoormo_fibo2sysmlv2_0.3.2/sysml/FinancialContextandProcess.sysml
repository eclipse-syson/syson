// ===================================================================
// FinancialContextandProcess.sysml
// Source-parity extracted from: FiBO_BP_Process.sysml
// Primary source module: FinancialContextAndProcess.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Processes BP/Process
// ===================================================================
package Financial_Context_and_Process {
  doc/* This ontology defines process concepts in general for refinement and re-use elsewhere in the FIBO business process domain. (source: dct:abstract, FinancialContextAndProcess.rdf) */
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

  item def Clearing specializes ClearingAndSettlement {
    	doc /* The process by which securities trades are cleared. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:Clearing
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;ClearingAndSettlement"/>
    }

  item def ClearingAndSettlement specializes Clearing, FinancialIndustryContext {
    	doc /* The business process or service area of securities clearing and settlement. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:ClearingAndSettlement
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;FinancialIndustryContext"/>
    }

  item def FinancialIndustryContext specializes FiBO_FND_ExternalStubs::Context {
    	doc /* The context in which business activites take place within the financial industry, i.e. investment management, wholesale financial markets trading and so on. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:FinancialIndustryContext
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtdsg;Context"/>
    }

  item def FinancialMarketsRegulation specializes FinancialIndustryContext {
    	doc /* Issuance and primary market trading of new Traded Financial Instruments */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:FinancialMarketsRegulation
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;FinancialIndustryContext"/>
    }

  item def InvestmentManagement specializes FinancialIndustryContext {
    	doc /* Issuance and primary market trading of new Traded Financial Instruments */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:InvestmentManagement
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;FinancialIndustryContext"/>
    }

  item def MarketDataProvision specializes PreTrade {
    	doc /* Issuance and primary market trading of new Traded Financial Instruments */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:MarketDataProvision
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;PreTrade"/>
    }

  item def PortfolioManagement specializes InvestmentManagement {
    	doc /* Issuance and primary market trading of new Traded Financial Instruments */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:PortfolioManagement
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;InvestmentManagement"/>
    }

  item def PreTrade specializes InvestmentManagement {
    	doc /* Issuance and primary market trading of new Traded Financial Instruments */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:PreTrade
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;InvestmentManagement"/>
    }

  item def PretradeQuotes specializes PreTrade {
    	doc /* Issuance and primary market trading of new Traded Financial Instruments */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:PretradeQuotes
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;PreTrade"/>
    }

  item def PretradeReferenceDataProvision specializes PreTrade {
    	doc /* Issuance and primary market trading of new Traded Financial Instruments */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:PretradeReferenceDataProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;PreTrade"/>
    }

  item def PrimaryMarket specializes FinancialIndustryContext {
    	doc /* Issuance and primary market trading of new Traded Financial Instruments */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:PrimaryMarket
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;FinancialIndustryContext"/>
    }

  item def PrimaryMarketClosing specializes PrimaryMarket {
    	doc /* Matching of trade allegations to identify confirmed trades, on an Over the Counter market in Traded Securities. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:PrimaryMarketClosing
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;PrimaryMarket"/>
    }

  item def PrimaryMarketIndicationsOfInterest specializes PrimaryMarket, PreTrade{ // RDF: fibo-bp-prc-bpfcp:PrimaryMarketIndicationsOfInterest
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;PreTrade"/>
        // RDF: fibo-bp-prc-bpfcp:PrimaryMarketIndicationsOfInterest
    }

  item def SecondaryMarketTradingContext specializes FinancialIndustryContext { // RDF: fibo-bp-prc-bpfcp:SecondaryMarketTradingContext
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;FinancialIndustryContext"/>
        // RDF: fibo-bp-prc-bpfcp:SecondaryMarketTradingContext
    }

  item def SecuritiesCustody specializes FinancialIndustryContext { // RDF: fibo-bp-prc-bpfcp:SecuritiesCustody
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;FinancialIndustryContext"/>
        // RDF: fibo-bp-prc-bpfcp:SecuritiesCustody
    }

  item def SecuritiesPostTrade specializes FinancialIndustryContext { // RDF: fibo-bp-prc-bpfcp:SecuritiesPostTrade
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;FinancialIndustryContext"/>
        // RDF: fibo-bp-prc-bpfcp:SecuritiesPostTrade
    }

  item def SecuritiesPostTradePositionManagement specializes SecuritiesPostTrade { // RDF: fibo-bp-prc-bpfcp:SecuritiesPostTradePositionManagement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;SecuritiesPostTrade"/>
      // RDF: fibo-bp-prc-bpfcp:SecuritiesPostTradePositionManagement
    }

  item def SecuritiesTrade specializes FinancialIndustryContext {
    	doc /* Matching of trade allegations to identify confirmed trades, on an Over the Counter market in Traded Securities. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:SecuritiesTrade
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;FinancialIndustryContext"/>
    }

  item def SecuritiesTradeAllocation specializes SecuritiesTrade, SecuritiesPostTrade {
    	doc /* Matching of trade allegations to identify confirmed trades, on an Over the Counter market in Traded Securities. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:SecuritiesTradeAllocation
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;SecuritiesPostTrade"/>
    }

  item def SecuritiesTradeCaptureAndValidation specializes SecuritiesTrade, SecuritiesPostTrade {
    	doc /* Matching of trade allegations to identify confirmed trades, on an Over the Counter market in Traded Securities. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:SecuritiesTradeCaptureAndValidation
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;SecuritiesPostTrade"/>
    }

  item def SecuritiesTradeConfirmationAffirmation specializes SecuritiesTrade, SecuritiesPostTrade {
    	doc /* Matching of trade allegations to identify confirmed trades, on an Over the Counter market in Traded Securities. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:SecuritiesTradeConfirmationAffirmation
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;SecuritiesPostTrade"/>
    }

  item def SecuritiesTradeDatePositionReporting specializes SecuritiesTrade {
    	doc /* Matching of trade allegations to identify confirmed trades, on an Over the Counter market in Traded Securities. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:SecuritiesTradeDatePositionReporting
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;SecuritiesTrade"/>
    }

  item def SecuritiesTradeExecution specializes SecuritiesTrade {
    	doc /* Matching of trade allegations to identify confirmed trades, on an Over the Counter market in Traded Securities. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:SecuritiesTradeExecution
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;SecuritiesTrade"/>
    }

  item def SecuritiesTradeOrderRouting specializes SecuritiesTrade {
    	doc /* Matching of trade allegations to identify confirmed trades, on an Over the Counter market in Traded Securities. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:SecuritiesTradeOrderRouting
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;SecuritiesTrade"/>
    }

  item def SecuritiesTradesMatching specializes SecuritiesTrade, SecuritiesPostTrade {
    	doc /* Matching of trade allegations to identify confirmed trades, on an Over the Counter market in Traded Securities. */
    	// skos:definition xml:lang="en" (FinancialContextAndProcess.rdf)
    	// rdfs:label xml:lang="en" (FinancialContextAndProcess.rdf)
    	// RDF: fibo-bp-prc-bpfcp:SecuritiesTradesMatching
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;SecuritiesPostTrade"/>
    }

  item def SecuritiesTradesReporting specializes SecuritiesTrade, SecuritiesPostTrade{ // RDF: fibo-bp-prc-bpfcp:SecuritiesTradesReporting
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;SecuritiesPostTrade"/>
        // RDF: fibo-bp-prc-bpfcp:SecuritiesTradesReporting
    }

  item def SecurityRetirement specializes TradedSecurityLifecycle { // RDF: fibo-bp-prc-bpfcp:SecurityRetirement
    // RDF: fibo-bp-prc-bpfcp:SecurityRetirement
    }

  item def ShortSaleLocate specializes PreTrade { // RDF: fibo-bp-prc-bpfcp:ShortSaleLocate
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;PreTrade"/>
        // RDF: fibo-bp-prc-bpfcp:ShortSaleLocate
    }

  item def TradeAdvertisements specializes PreTrade { // RDF: fibo-bp-prc-bpfcp:TradeAdvertisements
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;PreTrade"/>
        // RDF: fibo-bp-prc-bpfcp:TradeAdvertisements
    }

  item def TradedSecurityLifecycle { // RDF: fibo-bp-prc-bpfcp:TradedSecurityLifecycle
    // RDF: fibo-bp-prc-bpfcp:TradedSecurityLifecycle
    }

  item def TradesReferenceDataProvision specializes SecuritiesTrade { // RDF: fibo-bp-prc-bpfcp:TradesReferenceDataProvision
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;SecuritiesTrade"/>
        // RDF: fibo-bp-prc-bpfcp:TradesReferenceDataProvision
    }

  item def WhenIssuedTrading specializes SecuritiesTrade, PrimaryMarket{ // RDF: fibo-bp-prc-bpfcp:WhenIssuedTrading
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-prc-fcp;PrimaryMarket"/>
    }

        // RDF: fibo-bp-prc-bpfcp:WhenIssuedTrading
}
