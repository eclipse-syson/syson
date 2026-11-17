// ===================================================================
// USEconomicIndicators.sysml
// Source-parity extracted from: FiBO_IND_EconomicIndicators.sysml
// Primary source module: USEconomicIndicators.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Indices and Indicators IND/EconomicIndicators
// ===================================================================
package US_Economic_Indicators {
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

  item def <'American statistics publisher'> AmericanStatisticsPublisher specializes Economic_Indicators::StatisticalInformationPublisher {
  	doc /* statistical information publisher comprised of the government agencies and departments that publish statistical information on behalf of the U.S. Federal Government */
  	// skos:definition xml:lang="en" (USEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (USEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-usei:AmericanStatisticsPublisher
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;StatisticalInformationPublisher"/>
  }
  item def <'consumer expenditure survey'> ConsumerExpenditureSurvey specializes Analytics::StatisticalProgram {
  	doc /* statistical program conducted on a regular basis that provides information on the buying habits of consumers, including data on their expenditures, income, and consumer unit (families and single consumers) characteristics */
  	// skos:definition xml:lang="en" (USEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (USEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-usei:ConsumerExpenditureSurvey
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalProgram"/>
  }
  item def <'current employment statistics'> CurrentEmploymentStatistics specializes Analytics::StatisticalProgram {
  	doc /* survey conducted on a regular basis that presents analytical information related to businesses and government agencies, in order to provide detailed industry data on employment, hours, and earnings of workers */
  	// skos:definition xml:lang="en" (USEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (USEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-usei:CurrentEmploymentStatistics
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalProgram"/>
  }
  item def <'current population survey'> CurrentPopulationSurvey specializes Analytics::StatisticalProgram {
  	doc /* survey conducted on a regular basis that presents analytical information related to the general population of a given statistical area with respect to labor force, employment, unemployment, persons not in the labor force, hours of work, earnings, and other demographic and labor force characteristics */
  	// skos:definition xml:lang="en" (USEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (USEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-usei:CurrentPopulationSurvey
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalProgram"/>
  }
  item def <'employment situation establishment survey'> EmploymentSituationEstablishmentSurvey specializes EmploymentSituationSurvey {
  	doc /* survey conducted on a regular basis that presents analytical information related to the labor force of a given statistical area, surveyed with respect to businesses, and is, for the most part, seasonally adjusted */
  	// skos:definition xml:lang="en" (USEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (USEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-usei:EmploymentSituationEstablishmentSurvey
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-usei;EmploymentSituationSurvey"/>
  }
  item def <'employment situation household survey'> EmploymentSituationHouseholdSurvey specializes EmploymentSituationSurvey {
  	doc /* a survey conducted on a regular basis that presents analytical information related to the labor force of a given statistical area, surveyed with respect to households, and is, for the most part, seasonally adjusted */
  	// skos:definition xml:lang="en" (USEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (USEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-usei:EmploymentSituationHouseholdSurvey
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-usei;EmploymentSituationSurvey"/>
  }
  item def <'employment situation survey'> EmploymentSituationSurvey specializes CurrentEmploymentStatistics, CurrentPopulationSurvey {
  	doc /* a survey conducted on a regular basis that presents analytical information focused on the employment characteristics of a given statistical area */
  	// skos:definition xml:lang="en" (USEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (USEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-usei:EmploymentSituationSurvey
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-usei;CurrentEmploymentStatistics"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-usei;CurrentPopulationSurvey"/>
  }
  item def <'point of purchase survey'> PointOfPurchaseSurvey specializes Analytics::StatisticalProgram {
  	doc /* a program conducted on a regular basis that provides information on purchases of various items and services by consumers */
  	// skos:definition xml:lang="en" (USEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (USEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-usei:PointOfPurchaseSurvey
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalProgram"/>
  }
  item def <'urban consumer price index'> UrbanConsumerPriceIndex specializes Economic_Indicators::ConsumerPriceIndex {
  	doc /* an economic indicator representing a measure of the average change over time in the prices paid by urban consumers for a market basket of consumer goods and services */
  	// skos:definition xml:lang="en" (USEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (USEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-usei:UrbanConsumerPriceIndex
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;ConsumerPriceIndex"/>
  }
  item def <'urban consumers universe'> UrbanConsumersUniverse specializes Economic_Indicators::CivilianNonInstitutionalPopulation {
  	doc /* a statistical universe for consumer expenditure surveys consisting of people within a household that make joint expenditure decisions */
  	// skos:definition xml:lang="en" (USEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (USEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-usei:UrbanConsumersUniverse
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;CivilianNonInstitutionalPopulation"/>
  }
  item def <'U.S. producer price index'> USProducerPriceIndex specializes Economic_Indicators::ProducerPriceIndex {
  	doc /* an economic indicator representing a measure of the change over time in the selling prices received by domestic producers for their output */
  	// skos:definition xml:lang="en" (USEconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (USEconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-na-usei:USProducerPriceIndex
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;ProducerPriceIndex"/>
  }
}
