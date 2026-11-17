// ===================================================================
// InterestRates.sysml
// Source-parity extracted from: FiBO_IND_InterestRates.sysml
// Primary source module: InterestRates.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Indices and Indicators IND/InterestRates
// ===================================================================
package Interest_Rates {
  doc/* This ontology provides the basic types of interest rate which are recognized in the financial markets, and the relationships between these where applicable. These include bank base rates, inter-bank offer rates, overnight rates of interest and the US Federal Funds rate which is widely used as a rate of reference. It also includes the concept of a market rate spread between two interest rates. (source: dct:abstract, InterestRates.rdf) */
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

  item def <'base rate'> BaseRate specializes ReferenceInterestRate {
  	doc /* basic rate of interest on which the actual rate a bank charges on loans to its customers is calculated */
  	// skos:definition xml:lang="en" (InterestRates.rdf)
  	// rdfs:label xml:lang="en" (InterestRates.rdf)
  	// RDF: fibo-ind-ir-ir:BaseRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ir-ir;ReferenceInterestRate"/>
  }
  item def <'floating interest rate'> FloatingInterestRate specializes Debt::VariableInterestRate {
  	doc /* variable interest rate that is based on a specific index or benchmark rate */
  	// skos:definition xml:lang="en" (InterestRates.rdf)
  	// rdfs:label xml:lang="en" (InterestRates.rdf)
  	// RDF: fibo-ind-ir-ir:FloatingInterestRate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;VariableInterestRate"/>
    // No direct rdfs:subClassOf mapping in InterestRates.rdf; same-named FBC class exists in DebtPackage.
  }
  item def <'interbank bid rate'> InterbankBidRate specializes InterbankRate {
  	doc /* interbank rate that is the interest rate at which participating banks are willing to borrow deposits from other banks */
  	// skos:definition xml:lang="en" (InterestRates.rdf)
  	// rdfs:label xml:lang="en" (InterestRates.rdf)
  	// RDF: fibo-ind-ir-ir:InterbankBidRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ir-ir;InterbankRate"/>
  }
  item def <'interbank mid rate'> InterbankMidRate specializes InterbankRate {
  	doc /* interbank rate that represents the mid-point between bid and offer rates */
  	// skos:definition xml:lang="en" (InterestRates.rdf)
  	// rdfs:label xml:lang="en" (InterestRates.rdf)
  	// RDF: fibo-ind-ir-ir:InterbankMidRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ir-ir;InterbankRate"/>
  }
  item def <'interbank offered rate'> InterbankOfferedRate specializes InterbankRate {
  	doc /* interbank rate that is the interest rate at which participating banks lend money */
  	// skos:definition xml:lang="en" (InterestRates.rdf)
  	// rdfs:label xml:lang="en" (InterestRates.rdf)
  	// RDF: fibo-ind-ir-ir:InterbankOfferedRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ir-ir;InterbankRate"/>
  }
  item def <'interbank rate'> InterbankRate specializes ReferenceInterestRate {
  	doc /* reference rate that is the rate of interest charged on short-term loans between banks */
  	// skos:definition xml:lang="en" (InterestRates.rdf)
  	// rdfs:label xml:lang="en" (InterestRates.rdf)
  	// RDF: fibo-ind-ir-ir:InterbankRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ir-ir;ReferenceInterestRate"/>
  }
  item def <'interest rate authority'> InterestRateAuthority specializes Publishers::Publisher, Financial_Products_and_Services::FinancialServiceProvider {
  	doc /* financial service provider/publisher responsible for specifying some benchmark interest rate */
  	// skos:definition xml:lang="en" (InterestRates.rdf)
  	// rdfs:label xml:lang="en" (InterestRates.rdf)
  	// RDF: fibo-ind-ir-ir:InterestRateAuthority
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-pub;Publisher"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
  }
  item def <'interest rate benchmark'> InterestRateBenchmark specializes FiBO_FND_ExternalStubs::QuantityKind { // RDF: fibo-ind-ir-ir:InterestRateBenchmark
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;QuantityKind"/>
    // subClassOf: QuantityKind - external CMNS
    // RDF: fibo-ind-ir-ir:InterestRateBenchmark
  }
  item def <'interest rate benchmark classification scheme'> InterestRateBenchmarkClassificationScheme specializes Assessments::ClassificationScheme {
  	doc /* scheme for classifying interest rate benchmarks, such as the FpML classification scheme */
  	// skos:definition xml:lang="en" (InterestRates.rdf)
  	// rdfs:label xml:lang="en" (InterestRates.rdf)
  	// RDF: fibo-ind-ir-ir:InterestRateBenchmarkClassificationScheme
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;ClassificationScheme"/>
  }
  item def <'overnight rate'> OvernightRate specializes ReferenceInterestRate {
  	doc /* reference rate that is an interest rate at which a depository institution lends funds to another depository institution (short-term), or the interest rate the central bank charges a financial institution to borrow money overnight */
  	// skos:definition xml:lang="en" (InterestRates.rdf)
  	// rdfs:label xml:lang="en" (InterestRates.rdf)
  	// RDF: fibo-ind-ir-ir:OvernightRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ir-ir;ReferenceInterestRate"/>
  }
  item def <'reference interest rate'> ReferenceInterestRate specializes Currency_Amount::InterestRate, Indicators::MarketRate {
  	doc /* market rate that is a rate of interest paid by or agreed among some bank or set of banks */
  	// skos:definition xml:lang="en" (InterestRates.rdf)
  	// rdfs:label xml:lang="en" (InterestRates.rdf)
  	// RDF: fibo-ind-ir-ir:ReferenceInterestRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;InterestRate"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;MarketRate"/>
  }
  item def <'specific-provider interest rate benchmark'> SpecificProviderInterestRateBenchmark specializes InterestRateBenchmark {
  	doc /* interest rate benchmark that is made available by a specific market data provider for reference purposes */
  	// skos:definition xml:lang="en" (InterestRates.rdf)
  	// rdfs:label xml:lang="en" (InterestRates.rdf)
  	// RDF: fibo-ind-ir-ir:SpecificProviderInterestRateBenchmark
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ir-ir;InterestRateBenchmark"/>
  }
}
