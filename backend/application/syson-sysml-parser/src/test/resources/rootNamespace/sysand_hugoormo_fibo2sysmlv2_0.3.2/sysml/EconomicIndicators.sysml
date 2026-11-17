// ===================================================================
// EconomicIndicators.sysml
// Source-parity extracted from: FiBO_IND_EconomicIndicators.sysml
// Primary source module: EconomicIndicators.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Indices and Indicators IND/EconomicIndicators
// ===================================================================
package Economic_Indicators {
  doc/* This ontology provides the parameters which make up the various types of market economic indicators, along with basic facts about these such as the economies or countries they apply to. (source: dct:abstract, EconomicIndicators.rdf) */
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

  item def <'average daily earnings'> AverageDailyEarnings specializes AverageEarnings {
  	doc /* measure of the average daily wage an employee makes over the reporting period */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:AverageDailyEarnings
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;AverageEarnings"/>
  }
  item def <'average earnings'> AverageEarnings specializes EconomicIndicator, Analytics::Expression {
  	doc /* measure of the average wage an hourly or salaried worker makes in a given period of time */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:AverageEarnings
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EconomicIndicator"/>
  }
  item def <'average hourly earnings'> AverageHourlyEarnings specializes AverageEarnings {
  	doc /* measure of the average hourly wage an employee makes over the reporting period */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:AverageHourlyEarnings
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;AverageEarnings"/>
  }
  item def <'average monthly earnings'> AverageMonthlyEarnings specializes AverageEarnings {
  	doc /* measure of the average monthly wage an employee makes over the reporting period */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:AverageMonthlyEarnings
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;AverageEarnings"/>
  }
  item def <'average weekly earnings'> AverageWeeklyEarnings specializes AverageEarnings {
  	doc /* measure of the average weekly wage an employee makes over the reporting period */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:AverageWeeklyEarnings
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;AverageEarnings"/>
  }
  item def <'capital-labor-energy-materials multifactor productivity'> CapitalLaborEnergyMaterialsMultifactorProductivity specializes Productivity {
  	doc /* ratio of a quantity index of gross output to a quantity index of combined inputs */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:CapitalLaborEnergyMaterialsMultifactorProductivity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;Productivity"/>
  }
  item def <'capital-labor multifactor productivity (MFP), based on value added'> CapitalLaborMultifactorProductivityValueAdded specializes Productivity {
  	doc /* ratio of a quantity index of value added to a quantity index of combined labor and capital input */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:CapitalLaborMultifactorProductivityValueAdded
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;Productivity"/>
  }
  item def <'capital productivity, based on value added'> CapitalProductivityValueAdded specializes Productivity {
  	doc /* ratio of a quantity index of value added to a quantity index of capital input */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:CapitalProductivityValueAdded
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;Productivity"/>
  }
  item def <'civilian'> Civilian specializes Legal_Persons::LegallyCompetentNaturalPerson {
  	doc /* person that is not a member of the military (i.e., that is not on active duty) */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:Civilian
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-lp;LegallyCompetentNaturalPerson"/>
  }
  item def <'civilian labor force'> CivilianLaborForce specializes CivilianNonInstitutionalPopulation {
  	doc /* subset of the civilian, non-institutional population considered to be part of the labor force during a given reporting period */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:CivilianLaborForce
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;CivilianNonInstitutionalPopulation"/>
  }
  item def <'civilian labor force participation rate'> CivilianLaborForceParticipationRate specializes EconomicIndicator, Analytics::Expression {
  	doc /* economic indicator representing the rate of participation the labor force of a given economy for some specified period */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:CivilianLaborForceParticipationRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EconomicIndicator"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
  }
	item def <'civilian non-institutional person'> CivilianNonInstitutionalPerson specializes Civilian, People::LegalWorkingAgePerson {
  	doc /* legal working-age person that does not live in an institution (for example, a correctional facility, long-term care hospital, or nursing home), and is not on active military duty */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:CivilianNonInstitutionalPerson
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;LegalWorkingAgePerson"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;Civilian"/>
  }
  item def <'civilian non-institutional population'> CivilianNonInstitutionalPopulation specializes Analytics::StatisticalUniverse {
  	doc /* statistical universe consisting of people of a certain age who reside in a given region, do not live in institutions (for example, correctional facilities, long-term care hospitals, and nursing homes), and are not on active military duty */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:CivilianNonInstitutionalPopulation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalUniverse"/>
  }
  item def <'combined statistical area'> CombinedStatisticalArea specializes GovernmentSpecifiedStatisticalArea {
  	doc /* combination of adjacent metropolitan and micropolitan areas with economic ties measured by commuting patterns */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:CombinedStatisticalArea
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;GovernmentSpecifiedStatisticalArea"/>
  }
  item def <'consumer price index'> ConsumerPriceIndex specializes EconomicIndicator, Analytics::Expression {
  	doc /* economic indicator representing measure of the change over time in the prices of consumer goods and services that households consume */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:ConsumerPriceIndex
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EconomicIndicator"/>
  }
  item def <'discouraged worker population'> DiscouragedWorkerPopulation specializes MarginallyAttachedPopulation {
  	doc /* subset of the marginally attached population that have given a job-market related reason for not currently looking for work */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:DiscouragedWorkerPopulation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;MarginallyAttachedPopulation"/>
  }
  item def <'economic indicator'> EconomicIndicator specializes Analytics::ScopedMeasure {
  	doc /* derivative instrument where the holder has the right but may not have the obligation, depending on the nature of the instrument, to enter into the underlying contract, or pay or receive payment related to the underlying financial rate (or rate contract) on a specified future date based on a specified future rate and term */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:EconomicIndicator
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;ScopedMeasure"/>
  }
  item def <'employed population'> EmployedPopulation specializes CivilianLaborForce {
  	doc /* subset of the civilian labor force considered to be employed during the reporting period */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:EmployedPopulation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;CivilianLaborForce"/>
  }
  item def <'employed population part-time'> EmployedPopulationPartTime specializes EmployedPopulation {
  	doc /* subset of the employed population that includes persons that are working fewer than 30 to 35 hours per week based on usual working hours */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:EmployedPopulationPartTime
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EmployedPopulation"/>
  }
  item def <'employed population part-time for economic reasons'> EmployedPopulationPartTimeForEconomicReasons specializes EmployedPopulationPartTime, UnderemployedPopulation {
  	doc /* subset of the employed population that includes persons that are working fewer than 30 to 35 hours per week due to slack work, unfavorable business conditions, inability to find full-time work, and seasonal declines in demand */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:EmployedPopulationPartTimeForEconomicReasons
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;UnderemployedPopulation"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EmployedPopulationPartTime"/>
  }
  item def <'employed population part-time for non-economic reasons'> EmployedPopulationPartTimeForNonEconomicReasons specializes EmployedPopulationPartTime {
  	doc /* subset of the employed population that includes persons that are working fewer than 30 to 35 hours per week due to illness or other health or medical limitations, childcare problems, family or personal obligations, being in school or training, retirement or Social Security limits on earnings, and having a job where full-time work is less than 35 hours */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:EmployedPopulationPartTimeForNonEconomicReasons
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EmployedPopulationPartTime"/>
  }
  item def <'employed population temporarily not at work'> EmployedPopulationTemporarilyNotAtWork specializes EmployedPopulation {
  	doc /* subset of the employed population that includes persons that are temporarily absent from work for various reasons */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:EmployedPopulationTemporarilyNotAtWork
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EmployedPopulation"/>
  }
  item def <'employment-population ratio'> EmploymentPopulationRatio specializes EconomicIndicator, Analytics::Expression {
  	doc /* economic indicator representing the ratio of the employed population with respect to the overall civilian non-institutional population of a given economy for some specified period */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:EmploymentPopulationRatio
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EconomicIndicator"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
  }
  item def <'enterprise'> Enterprise specializes InstitutionalUnit, Functional_Entities::FunctionalBusinessEntity {
  	doc /* functional business entity that produces and/or sells goods or services */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:Enterprise
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalBusinessEntity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;InstitutionalUnit"/>
  }
  item def <'enterprise population'> EnterprisePopulation specializes Analytics::StatisticalUniverse {
  	doc /* statistical universe consisting of enterprises designed for the purposes of supporting surveys such as those used as the basis for employment and producer price indices */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:EnterprisePopulation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalUniverse"/>
  }
  item def <'establishment'> Establishment specializes InstitutionalUnit {
  	doc /* an enterprise (or part of an enterprise) that operates from a single physical location */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:Establishment
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;InstitutionalUnit"/>
  }
  item def <'establishment employment'> EstablishmentEmployment specializes EconomicIndicator, Analytics::Expression {
  	doc /* economic indicator representing the total number of persons who work in or for the establishment including working proprietors, active business partners and unpaid family workers, as well as persons working outside the establishment when paid by and under the control of the establishment, for example, sales representatives, outside service engineers and repair and maintenance personnel */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:EstablishmentEmployment
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EconomicIndicator"/>
  }
  item def <'establishment population'> EstablishmentPopulation specializes EnterprisePopulation {
  	doc /* a subset of the enterprise population focused on establishments */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:EstablishmentPopulation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EnterprisePopulation"/>
  }
  item def <'fixed basket'> FixedBasket specializes Financial_Products_and_Services::Basket, Financial_Dates::DatedStructuredCollection {
  	doc /* basket of goods and services whose quantity and quality are held fixed for some period of time */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:FixedBasket
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Basket"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
    // subClassOf: cmns-col:StructuredCollection (external CMNS)
  }
  item def <'fixed basket constituent'> FixedBasketConstituent specializes Financial_Products_and_Services::BasketConstituent, Financial_Dates::DatedCollectionConstituent {
  	doc /* component of a fixed basket */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:FixedBasketConstituent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;BasketConstituent"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedCollectionConstituent"/>
    // subClassOf: cmns-col:CollectionConstituent (external CMNS)
  }
  item def <'fixed basket population'> FixedBasketPopulation specializes Analytics::StatisticalUniverse {
  	doc /* statistical universe consisting of specific goods and/or services designed for the purposes of supporting surveys such as those used as the basis for price indices */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:FixedBasketPopulation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalUniverse"/>
  }
  item def <'government-specified statistical area'> GovernmentSpecifiedStatisticalArea specializes Analytics::StatisticalArea {
  	doc /* statistical area defined by a government agency */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:GovernmentSpecifiedStatisticalArea
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalArea"/>
  }
  item def <'gross domestic product'> GrossDomesticProduct specializes EconomicIndicator {
  	doc /* economic indicator representing the broadest measure of aggregate economic activity, measuring the total unduplicated market value of all final goods and services produced within a statistical area in a period */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:GrossDomesticProduct
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EconomicIndicator"/>
  }
  item def <'household'> Household specializes InstitutionalUnit {
  	doc /* individual or small group of persons who occupy a housing unit (such as a house or apartment) as their usual place of residence, who pool some, or all, of their income and wealth and who consume certain types of goods and services collectively, mainly housing and food */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:Household
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;InstitutionalUnit"/>
  }
  item def <'housing unit'> HousingUnit specializes FiBO_FND_ExternalStubs::PhysicalLocation {
  	doc /* house, an apartment, a mobile home or trailer, a group of rooms, or a single room occupied as separate living quarters, or if vacant, intended for occupancy as separate living quarters */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:HousingUnit
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;PhysicalLocation"/>
    // subClassOf: PhysicalLocation - external CMNS
  }
  item def <'inflation rate'> InflationRate specializes EconomicIndicator {
  	doc /* economic indicator representing a change in prices of goods and services for a specified period, for a given statistical area */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:InflationRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EconomicIndicator"/>
  }
  item def <'input producer price index'> InputProducerPriceIndex specializes ProducerPriceIndex {
  	doc /* economic indicator representing measure of the rate of change over time in the prices of inputs of goods and services purchased by the producer */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:InputProducerPriceIndex
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;ProducerPriceIndex"/>
  }
	item def <'institutional person'> InstitutionalPerson specializes People::Person {
  	doc /* person that resides in an institution for some reason, due, for example, to hospitalization, rehabilitation, or incarceration */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:InstitutionalPerson
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;Person"/>
  }
  item def <'institutional unit'> InstitutionalUnit specializes Functional_Entities::FunctionalEntity, Parties::PartyRole {
  	doc /* party that is capable, in its own right, of owning assets, incurring liabilities, and engaging in economic activities and in transactions with other parties */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:InstitutionalUnit
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalEntity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
  item def <'labor productivity, based on gross output'> LaborProductivityGrossOutput specializes Productivity {
  	doc /* ratio of a quantity index of gross output to a quantity index of labor input */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:LaborProductivityGrossOutput
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;Productivity"/>
  }
  item def <'labor productivity, based on value added'> LaborProductivityValueAdded specializes Productivity {
  	doc /* ratio of a quantity index of value added to a quantity index of labor input */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:LaborProductivityValueAdded
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;Productivity"/>
  }
  item def <'marginally attached population'> MarginallyAttachedPopulation specializes PopulationNotInLaborForce {
  	doc /* subset of the population that includes people who currently are neither working nor looking for work but indicate that they want and are available for a job and have looked for work sometime in the past 12 months */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:MarginallyAttachedPopulation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;PopulationNotInLaborForce"/>
  }
  item def <'metropolitan statistical area'> MetropolitanStatisticalArea specializes GovernmentSpecifiedStatisticalArea {
  	doc /* one or more adjacent counties or county equivalents that have at least one urban core area of at least 50,000 population, plus adjacent territory that has a high degree of social and economic integration with the core as measured by commuting ties */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:MetropolitanStatisticalArea
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;GovernmentSpecifiedStatisticalArea"/>
  }
  item def <'micropolitan statistical area'> MicropolitanStatisticalArea specializes GovernmentSpecifiedStatisticalArea {
  	doc /* one or more adjacent counties or county equivalents that have at least one urban core area of at least 10,000 population but less than 50,000, plus adjacent territory that has a high degree of social and economic integration with the core as measured by commuting ties */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:MicropolitanStatisticalArea
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;GovernmentSpecifiedStatisticalArea"/>
  }
  item def <'military person'> MilitaryPerson specializes Legal_Persons::LegallyCompetentNaturalPerson {
  	doc /* person that is a member of the active duty military */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:MilitaryPerson
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-lp;LegallyCompetentNaturalPerson"/>
  }
  item def <'output producer price index'> OutputProducerPriceIndex specializes ProducerPriceIndex {
  	doc /* economic indicator representing measure of the rate of change over time in the prices of products sold as they leave the producer */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:OutputProducerPriceIndex
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;ProducerPriceIndex"/>
  }
  item def <'personal consumption expenditures'> PersonalConsumptionExpenditures specializes EconomicIndicator {
  	doc /* economic indicator representing measure of the value of the goods and services purchased by, or on the behalf of, 'persons' */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:PersonalConsumptionExpenditures
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EconomicIndicator"/>
  }
  item def <'population not in the labor force'> PopulationNotInLaborForce specializes CivilianNonInstitutionalPopulation {
  	doc /* subset of the civilian, noninstitutional population, that is considered neither employed nor unemployed by the reporting agency during the reporting period */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:PopulationNotInLaborForce
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;CivilianNonInstitutionalPopulation"/>
  }
  item def <'producer price index'> ProducerPriceIndex specializes EconomicIndicator, Analytics::Expression {
  	doc /* economic indicator representing measure of the rate of change over time in the prices of goods and services bought and sold by producers */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:ProducerPriceIndex
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EconomicIndicator"/>
  }
  item def <'productivity'> Productivity specializes EconomicIndicator, Analytics::Expression {
  	doc /* economic indicator representing ratio of a volume measure of output to a volume measure of input use */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:Productivity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EconomicIndicator"/>
  }
  item def <'statistical information publisher'> StatisticalInformationPublisher specializes Publishers::Publisher {
  	doc /* publisher that packages and makes analytical and statistical data available */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:StatisticalInformationPublisher
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-pub;Publisher"/>
  }
  item def <'ultimate consumer'> UltimateConsumer specializes Products_and_Services::Consumer {
  	doc /* person that is the ultimate user of a good, product or service */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:UltimateConsumer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Consumer"/>
  }
  item def <'underemployed population'> UnderemployedPopulation specializes EmployedPopulation {
  	doc /* subset of the employed population that includes persons employed part-time for economic reasons, who want and are available for full-time work but have had to settle for a part-time schedule */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:UnderemployedPopulation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EmployedPopulation"/>
  }
  item def <'underemployed population with respect to occupation'> UnderemployedPopulationWithRespectToOccupation specializes UnderemployedPopulation {
  	doc /* subset of the underemployed population that includes persons employed in a role that does not reflect their training and experience, excluding those that change careers */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:UnderemployedPopulationWithRespectToOccupation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;UnderemployedPopulation"/>
  }
  item def <'underutilized population'> UnderutilizedPopulation specializes CivilianNonInstitutionalPopulation {
  	doc /* subset of the civilian non-institutional population that includes persons employed part-time for economic reasons, persons that are marginally attached to the labor force, and persons that are identified as unemployed */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:UnderutilizedPopulation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;CivilianNonInstitutionalPopulation"/>
  }
  item def <'unemployed population'> UnemployedPopulation specializes CivilianLaborForce {
  	doc /* subset of the civilian non-institutional population that includes persons employed part-time for economic reasons, persons that are marginally attached to the labor force, and persons that are identified as unemployed */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:UnemployedPopulation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;CivilianLaborForce"/>
  }
  item def <'unemployment rate'> UnemploymentRate specializes EconomicIndicator, Analytics::Expression {
  	doc /* economic indicator representing the ratio of the unemployed population with respect to the civilian labor force of a given economy for some specified period */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:UnemploymentRate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;EconomicIndicator"/>
  }
  item def <'value-added producer price index'> ValueAddedProducerPriceIndex specializes ProducerPriceIndex {
  	doc /* economic indicator representing a weighted average of the input and output producer price indices */
  	// skos:definition xml:lang="en" (EconomicIndicators.rdf)
  	// rdfs:label xml:lang="en" (EconomicIndicators.rdf)
  	// RDF: fibo-ind-ei-ei:ValueAddedProducerPriceIndex
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ei-ei;ProducerPriceIndex"/>
  }
}

// --- fibo-ind-ei-na-caei ---
