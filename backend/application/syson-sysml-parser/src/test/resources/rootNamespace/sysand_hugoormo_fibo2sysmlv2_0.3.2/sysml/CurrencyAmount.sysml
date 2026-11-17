// ===================================================================
// CurrencyAmount.sysml
// Source-parity extracted from: FiBO_FND_Accounting.sysml
// Primary source module: CurrencyAmount.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Accounting
// ===================================================================

package Currency_Amount {
  doc/* This ontology defines currency and monetary amount related concepts for use in defining other FIBO ontology elements. There are two distinct kinds of concepts that correspond to money and amounts: a concrete, actual amount of money, and the monetary measure of something denominated in some currency. These are dimensionally the same but whereas 'money amount' is defined as an amount of money, 'monetary amount' is an abstract monetary measure. The definition of currency provided herein is compliant with the definitions given in ISO 4217. ISO 4217 provides universally applicable coded representations of names of currencies and funds, used internationally for financial transaction support. The ontology has been partitioned into 2 parts: (1) the essential concept system describing the standard (this module), and (2) ISO4217-1-CurrencyCodes, which contains all of the individuals specified in ISO 4217. (source: dct:abstract, CurrencyAmount.rdf) */
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

  item def <'amount of money'> AmountOfMoney specializes FiBO_FND_ExternalStubs::ScalarQuantityValue {
  	doc /* amount of readily available cash in banknotes and coins */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:AmountOfMoney
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;ScalarQuantityValue"/>
  }

  item def <'calculated price'> CalculatedPrice specializes Currency_Amount::MonetaryPrice {
  	doc /* monetary price determined by a formula */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:CalculatedPrice
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryPrice"/>
  }

  item def Currency specializes FiBO_FND_ExternalStubs::MeasurementUnit {
  	doc /* medium of exchange value, defined by reference to the geographical location of the monetary authorities responsible for it */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
    // rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:Currency
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;MeasurementUnit"/>
  }

  item def <'currency basket'> CurrencyBasket specializes FiBO_FND_ExternalStubs::StructuredCollection {
  	doc /* selected group of currencies, in which the weighted average is used as a measure of the value or the amount of an obligation */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:CurrencyBasket
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;StructuredCollection"/>
  }

  item def <'currency identifier'> CurrencyIdentifier specializes FiBO_FND_ExternalStubs::CodeElement, FiBO_FND_ExternalStubs::Identifier {
  	doc /* sequence of characters representing some currency */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:CurrencyIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
  }

  item def ExchangeRate specializes Ratio {
  	doc /* rate at which one currency can be exchanged for another */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
    // rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:ExchangeRate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Ratio"/>
  }

  item def <'funds'> Funds specializes Currency {
  	doc /* artificial currency used as calculation basis for another currency(s) and accounting purposes */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:Funds
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;Currency"/>
  }

  item def <'funds identifier'> FundsIdentifier specializes FiBO_FND_ExternalStubs::CodeElement, FiBO_FND_ExternalStubs::Identifier {
  	doc /* sequence of characters that can be used to uniquely identify funds */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:FundsIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
  }

  item def <'interest rate'> InterestRate specializes Currency_Amount::PercentageMonetaryAmount {
  	doc /* amount charged, expressed as a percentage of principal, in exchange for the use of assets */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:InterestRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;PercentageMonetaryAmount"/>
  }

	item def <'monetary amount'> MonetaryAmount specializes FiBO_FND_ExternalStubs::ScalarQuantityValue {
    doc /* measure that is an amount of money specified in monetary units */
    // skos:definition xml:lang="en" (CurrencyAmount.rdf)
    // rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:MonetaryAmount
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;ScalarQuantityValue"/>
  }

  item def <'monetary price'> MonetaryPrice specializes Currency_Amount::MonetaryAmount, Price {
  	doc /* price that that is expressed as a monetary amount */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:MonetaryPrice
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;Price"/>
  }

  item def <'percentage monetary amount'> PercentageMonetaryAmount specializes FiBO_FND_ExternalStubs::Percentage {
    doc /* measure of some amount of money expressed as a percentage of some other amount, some notional amount or some concrete money amount */
    // skos:definition xml:lang="en" (CurrencyAmount.rdf)
    // rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:PercentageMonetaryAmount
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Percentage"/>
  }

  item def <'precious metal'> PreciousMetal specializes Commodities_Contracts::Metal, Products_and_Services::NegotiableCommodity {
  	doc /* metal that is considered to be rare and/or have a high economic value */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:PreciousMetal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;Metal"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
  }

  item def <'precious metal identifier'> PreciousMetalIdentifier specializes FiBO_FND_ExternalStubs::CodeElement, FiBO_FND_ExternalStubs::Identifier {
  	doc /* sequence of characters uniquely identifying the precious metal in some context */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:PreciousMetalIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
  }

  item def <'price'> Price specializes FiBO_FND_ExternalStubs::ScalarQuantityValue {
  	doc /* amount of money, goods, or services requested, expected, required, or given in exchange for something else */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:Price
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;ScalarQuantityValue"/>
  }

  item def Ratio { // RDF: fibo-fnd-acc-cur:Ratio
    // RDF: fibo-fnd-acc-cur:Ratio
  }

  item def <'unit of account'> UnitOfAccount specializes Currency {
  	doc /* nominal monetary unit of measure used to represent the real value (or cost) of any economic item; i.e. goods, services, assets, liabilities, income, expenses */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:UnitOfAccount
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;Currency"/>
  }

  item def <'unit of account identifier'> UnitOfAccountIdentifier specializes FiBO_FND_ExternalStubs::CodeElement, FiBO_FND_ExternalStubs::Identifier {
  	doc /* sequence of characters that can be used to uniquely identify a nominal monetary unit */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:UnitOfAccountIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
  }

  item def <'unit price'> UnitPrice specializes Currency_Amount::MonetaryPrice {
  	doc /* monetary price expressed in relation to a well-known measurable unit by which the goods or services are allocated */
  	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
  	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
  	// RDF: fibo-fnd-acc-cur:UnitPrice
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryPrice"/>
  }

}
