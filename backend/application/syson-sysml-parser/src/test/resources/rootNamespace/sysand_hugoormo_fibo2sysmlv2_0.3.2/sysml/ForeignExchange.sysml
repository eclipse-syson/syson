// ===================================================================
// ForeignExchange.sysml
// Source-parity extracted from: FiBO_IND_ForeignExchange.sysml
// Primary source module: ForeignExchange.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Indices and Indicators IND/ForeignExchange
// ===================================================================
package Foreign_Exchange {
  doc/* This ontology provides the parameters for foreign exchange rates, covering spot and forward rates, as well as foreign exchange spot rate volatilities. (source: dct:abstract, ForeignExchange.rdf) */
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

  item def <'currency conversion service'> CurrencyConversionService specializes ForeignExchangeService {
  	doc /* indicates the origin of a quoted exchange rate */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:CurrencyConversionService
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-fx-fx;ForeignExchangeService"/>
  }
  item def <'currency forward rate'> CurrencyForwardRate specializes QuotedExchangeRate {
  	doc /* rate of exchange between two currencies for settlement at some future point in time, expressed as a premium on the spot rate */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:CurrencyForwardRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-fx-fx;QuotedExchangeRate"/>
  }
  item def <'currency forward rate volatility'> CurrencyForwardRateVolatility specializes ExchangeRateVolatility, Indicators::ImpliedVolatility {
  	doc /* measure of exchange rate fluctuation based on a range of projected values for exchange rates */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:CurrencyForwardRateVolatility
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-fx-fx;ExchangeRateVolatility"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;ImpliedVolatility"/>
  }
  item def <'currency spot buy rate'> CurrencySpotBuyRate specializes CurrencySpotRate {
  	doc /* indicative spot buying market rate as observed by the reporting source */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:CurrencySpotBuyRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-fx-fx;CurrencySpotRate"/>
  }
  item def <'currency spot mid rate'> CurrencySpotMidRate specializes CurrencySpotRate {
  	doc /* indicative middle market (mean of spot buying and selling) rate as observed by the reporting source */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:CurrencySpotMidRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-fx-fx;CurrencySpotRate"/>
  }
  item def <'currency spot rate'> CurrencySpotRate specializes QuotedExchangeRate {
  	doc /* rate at which one currency may be exchanged for another for immediate delivery */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:CurrencySpotRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-fx-fx;QuotedExchangeRate"/>
  }
  item def <'currency spot sell rate'> CurrencySpotSellRate specializes CurrencySpotRate {
  	doc /* indicative spot selling market rate as observed by the reporting source */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:CurrencySpotSellRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-fx-fx;CurrencySpotRate"/>
  }
  item def <'currency spot volatility'> CurrencySpotVolatility specializes ExchangeRateVolatility, Indicators::HistoricalVolatility {
  	doc /* measure of exchange rate fluctuation based on a range of past actual values for exchange rates */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:CurrencySpotVolatility
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-fx-fx;ExchangeRateVolatility"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;HistoricalVolatility"/>
  }
  item def <'exchange rate structure'> ExchangeRateStructure specializes Financial_Dates::DatedStructuredCollection {
  	doc /* structured collection of quoted or projected exchange rates, such that volatility may be constructed for the structure */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:ExchangeRateStructure
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
  }
  item def <'exchange rate volatility'> ExchangeRateVolatility specializes Indicators::Volatility {
  	doc /* statistical measure of the rate of change in the rate at which one currency can be exchanged for another */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:ExchangeRateVolatility
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;Volatility"/>
  }
  item def <'foreign exchange service'> ForeignExchangeService specializes Financial_Products_and_Services::FinancialService {
  	doc /* financial service involving the exchange of one currency for another, conversion of one currency for another, and transfer of money from one country to another whereby currency conversion is required */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:ForeignExchangeService
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
  }
  item def <'international electronic funds transfer service'> InternationalElectronicFundsTransferService specializes Financial_Services_Entities::ElectronicFundsTransferService, ForeignExchangeService {
  	doc /* electronic funds transfer (EFT) service involving the transfer of funds across national borders, that may also involve currency conversion */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:InternationalElectronicFundsTransferService
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;ElectronicFundsTransferService"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-fx-fx;ForeignExchangeService"/>
  }
  item def <'quoted exchange rate'> QuotedExchangeRate specializes Currency_Amount::ExchangeRate, Indicators::MarketRate {
  	doc /* derivative instrument where the holder has the right but may not have the obligation, depending on the nature of the instrument, to enter into the underlying contract, or pay or receive payment related to the underlying financial rate (or rate contract) on a specified future date based on a specified future rate and term */
  	// skos:definition xml:lang="en" (ForeignExchange.rdf)
  	// rdfs:label xml:lang="en" (ForeignExchange.rdf)
  	// RDF: fibo-ind-fx-fx:QuotedExchangeRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;ExchangeRate"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;MarketRate"/>
  }
}
