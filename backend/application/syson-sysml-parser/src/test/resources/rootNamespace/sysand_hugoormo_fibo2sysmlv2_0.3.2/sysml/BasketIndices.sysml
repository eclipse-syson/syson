// ===================================================================
// BasketIndices.sysml
// Source-parity extracted from: FiBO_IND_MarketIndices.sysml
// Primary source module: BasketIndices.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Indices and Indicators IND/MarketIndices
// ===================================================================
package Basket_Indices {
  doc/* This ontology defines market indices as hypothetical portfolios of investment holdings that correspond to some segment of the financial market, whose value is determined by the prices of the underlying holdings. Coverage includes credit indices, security-based indices, economic indicator based indices, and combinations thereof. (source: dct:abstract, BasketIndices.rdf) */
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

  item def <'basket of credit risks'> BasketOfCreditRisks specializes Financial_Products_and_Services::WeightedBasket, Financial_Dates::DatedStructuredCollection {
  	doc /* basket of instruments, legal entities, or a combination thereof collected for the purpose of analyzing risk */
  	// skos:definition xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-ind-mkt-bas:BasketOfCreditRisks
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;WeightedBasket"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
  }
  item def <'basket of equities'> BasketOfEquities specializes Baskets::BasketOfSecurities {
  	doc /* basket of securities whose constituents are listed shares */
  	// skos:definition xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-ind-mkt-bas:BasketOfEquities
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-bsk;BasketOfSecurities"/>
  }
  item def <'capitalization-based weighting function'> CapitalizationBasedWeightingFunction specializes Analytics::WeightingFunction {
  	doc /* weighting function derived from the relative market capitalization (share price times the number of shares outstanding) of the companies tracked by an index */
  	// skos:definition xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (BasketIndices.rdf)
  	// RDF: fibo-ind-mkt-bas:CapitalizationBasedWeightingFunction
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;WeightingFunction"/>
  }
  item def <'credit index'> CreditIndex specializes ReferenceIndex {
  	doc /* reference index that is a function of credit events that change the value of an underlying portfolio */
  	// skos:definition xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-ind-mkt-bas:CreditIndex
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-mkt-bas;ReferenceIndex"/>
  }
  item def <'credit index constituent'> CreditIndexConstituent specializes Financial_Products_and_Services::WeightedBasketConstituent, Financial_Dates::DatedCollectionConstituent {
  	doc /* constituent of a basket of credit risks */
  	// skos:definition xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (BasketIndices.rdf)
  	// RDF: fibo-ind-mkt-bas:CreditIndexConstituent
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;WeightedBasketConstituent"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedCollectionConstituent"/>
  }
  item def <'equity index'> EquityIndex specializes ReferenceIndex {
  	doc /* benchmark whose constituents are exclusively equity instruments */
  	// skos:definition xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-ind-mkt-bas:EquityIndex
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-mkt-bas;ReferenceIndex"/>
  }
  item def <'market capitalization'> MarketCapitalization specializes Analytics::Expression {
  	doc /* expression representing the perceived value of a company as determined by the stock market at a specific point in time */
  	// skos:definition xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (BasketIndices.rdf)
  	// RDF: fibo-ind-mkt-bas:MarketCapitalization
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
  }
  item def <'reference index'> ReferenceIndex specializes Analytics::ScopedMeasure {
  	doc /* measure of change in the value of the contents of a basket over a given period of time */
  	// skos:definition xml:lang="en" (BasketIndices.rdf)
  	// rdfs:label xml:lang="en" (BasketIndices.rdf)
  	// RDF: fibo-ind-mkt-bas:ReferenceIndex
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;ScopedMeasure"/>
  }
}
