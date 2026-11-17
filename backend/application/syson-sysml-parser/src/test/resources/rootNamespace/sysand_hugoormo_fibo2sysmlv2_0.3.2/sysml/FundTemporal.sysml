// ===================================================================
// FundTemporal.sysml
// Source-parity extracted from: FiBO_MD_CIVTemporal.sysml
// Primary source module: FundTemporal.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Market Data MD/CIVTemporal
// ===================================================================
package Fund_Temporal {
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

  item def <'accrued fees'> AccruedFees { // RDF: fibo-md-civt-fund:AccruedFees
    // RDF: fibo-md-civt-fund:AccruedFees
  }
  item def <'accrued income'> AccruedIncome { // RDF: fibo-md-civt-fund:AccruedIncome
    // RDF: fibo-md-civt-fund:AccruedIncome
  }
  item def <'accrued taxes'> AccruedTaxes { // RDF: fibo-md-civt-fund:AccruedTaxes
    // RDF: fibo-md-civt-fund:AccruedTaxes
  }
  item def <'annualized performance determination method'> AnnualizedPerformanceDeterminationMethod specializes PerformanceDeterminationMethod {
  	doc /* Annualized performance determination. */
  	// skos:definition xml:lang="en" (FundTemporal.rdf)
  	// rdfs:label xml:lang="en" (FundTemporal.rdf)
  	// RDF: fibo-md-civt-fund:AnnualizedPerformanceDeterminationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-civx-fun;PerformanceDeterminationMethod"/>
  }
  item def <'fee payable'> FeePayable { // RDF: fibo-md-civt-fund:FeePayable
    // RDF: fibo-md-civt-fund:FeePayable
  }
  item def <'fund price'> FundPrice specializes Instrument_Pricing::SecurityPrice {
  	doc /* Performance determined gross of fee. Review: Is this mutually exclusive with the other listed method? It sounds like it is not. */
  	// skos:definition xml:lang="en" (FundTemporal.rdf)
  	// rdfs:label xml:lang="en" (FundTemporal.rdf)
  	// RDF: fibo-md-civt-fund:FundPrice
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
  }
  item def <'funds tax'> FundsTax { // RDF: fibo-md-civt-fund:FundsTax
    // RDF: fibo-md-civt-fund:FundsTax
  }
  item def <'fund unit performance'> FundUnitPerformance { // RDF: fibo-md-civt-fund:FundUnitPerformance
    // RDF: fibo-md-civt-fund:FundUnitPerformance
  }
  item def <'gross of fee performance determination method'> GrossOfFeePerformanceDeterminationMethod specializes PerformanceDeterminationMethod {
  	doc /* Performance determined gross of fee. Review: Is this mutually exclusive with the other listed method? It sounds like it is not. */
  	// skos:definition xml:lang="en" (FundTemporal.rdf)
  	// rdfs:label xml:lang="en" (FundTemporal.rdf)
  	// RDF: fibo-md-civt-fund:GrossOfFeePerformanceDeterminationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-civx-fun;PerformanceDeterminationMethod"/>
  }
  item def <'money weighted rate of return performance determination method'> MoneyWeightedRateOfReturnPerformanceDeterminationMethod specializes PerformanceDeterminationMethod {
  	doc /* Money weighted rate of return. */
  	// skos:definition xml:lang="en" (FundTemporal.rdf)
  	// rdfs:label xml:lang="en" (FundTemporal.rdf)
  	// RDF: fibo-md-civt-fund:MoneyWeightedRateOfReturnPerformanceDeterminationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-civx-fun;PerformanceDeterminationMethod"/>
  }
  item def <'performance determination method'> PerformanceDeterminationMethod specializes Objectives::Method {
  	doc /* A method for performance determination (e.g. Time weighted and money weighted, annualized, gross of fee) along with the time frame in which this is determined. PLUS Performances NamePeriod */
  	// skos:definition xml:lang="en" (FundTemporal.rdf)
  	// rdfs:label xml:lang="en" (FundTemporal.rdf)
  	// RDF: fibo-md-civt-fund:PerformanceDeterminationMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Method"/>
  }
  item def <'sigma value of holdings'> SigmaValueOfHoldings { // RDF: fibo-md-civt-fund:SigmaValueOfHoldings
    // RDF: fibo-md-civt-fund:SigmaValueOfHoldings
  }
  item def <'swing'> Swing { // RDF: fibo-md-civt-fund:Swing
    // RDF: fibo-md-civt-fund:Swing
  }
  item def <'swing price'> SwingPrice specializes FundPrice {
  	doc /* Time weighted rate of return. */
  	// skos:definition xml:lang="en" (FundTemporal.rdf)
  	// rdfs:label xml:lang="en" (FundTemporal.rdf)
  	// RDF: fibo-md-civt-fund:SwingPrice
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-civx-fun;FundPrice"/>
  }
  item def <'time weighted rate of return performance determination method'> TimeWeightedRateOfReturnPerformanceDeterminationMethod specializes PerformanceDeterminationMethod {
  	doc /* Time weighted rate of return. */
  	// skos:definition xml:lang="en" (FundTemporal.rdf)
  	// rdfs:label xml:lang="en" (FundTemporal.rdf)
  	// RDF: fibo-md-civt-fund:TimeWeightedRateOfReturnPerformanceDeterminationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-civx-fun;PerformanceDeterminationMethod"/>
  }
}
