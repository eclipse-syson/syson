// ===================================================================
// CanadianEconomicIndicators.sysml
// Source-parity extracted from: FiBO_IND_EconomicIndicators.sysml
// Primary source module: CanadianEconomicIndicators.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Indices and Indicators IND/EconomicIndicators
// ===================================================================
package Canadian_Economic_Indicators {
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

  item def <'Canadian consumer price index'> CanadianConsumerPriceIndex specializes Economic_Indicators::ConsumerPriceIndex {
  	doc /* economic indicator representing a measure of changes over time in the prices of a fixed basket of consumer goods and services that Canadian private households consume */
  	// skos:definition xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-caei:CanadianConsumerPriceIndex
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;ConsumerPriceIndex"/>
  }
  item def <'Canadian households consumers universe'> CanadianHouseholdsConsumersUniverse specializes Economic_Indicators::CivilianNonInstitutionalPopulation {
  	doc /* a statistical universe consisting of all private households in Canada, with the exception of soldiers on military bases, people living on First Nations reserves, institutionalized persons, and households living in the rural areas of the three northern territories */
  	// skos:definition xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-caei:CanadianHouseholdsConsumersUniverse
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;CivilianNonInstitutionalPopulation"/>
  }
  item def <'Canadian producer price index'> CanadianProducerPriceIndex specializes Economic_Indicators::ProducerPriceIndex {
  	doc /* an economic indicator representing a measure of the change over time in the prices of a fixed-basket of domestic producer goods and services */
  	// skos:definition xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-caei:CanadianProducerPriceIndex
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;ProducerPriceIndex"/>
  }
  item def <'industrial products sector'> IndustrialProductsSector specializes Classification_Schemes::IndustrySectorClassifier {
  	doc /* an economic indicator representing a measure of the change over time in the prices of a fixed-basket of domestic producer goods and services */
  	// skos:definition xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-caei:IndustrialProductsSector
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassifier"/>
  }
  item def <'new housing sector'> NewHousingSector specializes Classification_Schemes::IndustrySectorClassifier {
  	doc /* an economic indicator representing a measure of the change over time in the prices of a fixed-basket of domestic producer goods and services */
  	// skos:definition xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-caei:NewHousingSector
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassifier"/>
  }
  item def <'raw materials sector'> RawMaterialsSector specializes Classification_Schemes::IndustrySectorClassifier {
  	doc /* an economic indicator representing a measure of the change over time in the prices of a fixed-basket of domestic producer goods and services */
  	// skos:definition xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (CanadianEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-caei:RawMaterialsSector
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassifier"/>
  }
}

// --- fibo-ind-ei-na-usei ---
