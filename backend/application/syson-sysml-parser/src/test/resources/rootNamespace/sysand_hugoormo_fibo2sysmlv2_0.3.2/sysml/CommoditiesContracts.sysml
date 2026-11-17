// ===================================================================
// CommoditiesContracts.sysml
// Source-parity extracted from: FiBO_DER_DerivativesContracts.sysml
// Primary source module: CommoditiesContracts.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/DerivativesContracts
// ===================================================================
package Commodities_Contracts {
  doc/* This ontology specifies core concepts for commodities-based derivatives and spot contracts, including the definitions of the most common categories of underlying negotiable commodities, corresponding to those outlined in the ISO 10962 CFI standard. Note that the ontology does not include any specific units of measure for these commodities. The intent is that FIBO users would select one of the many available units ontologies to use in specifying the details of individual contracts. (source: dct:abstract, CommoditiesContracts.rdf) */
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

  item def <'agricultural resource'> AgriculturalResource specializes Products_and_Services::NegotiableCommodity {
    	doc /* negotiable commodity that is obtained via some agricultural process, including forestry, fishing, livestock, grain, dairy, corn, cocoa, soybeans, sugar, coffee */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:AgriculturalResource
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
    }

  item def <'base metal'> BaseMetal specializes Metal {
    	doc /* common metal that tarnishes, oxidizes, or corrodes relatively quickly when exposed to air or moisture, that is widely used in commercial and industrial applications, such as construction and manufacturing */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:BaseMetal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;Metal"/>
    }

  item def <'basket of commodities'> BasketOfCommodities specializes Financial_Products_and_Services::WeightedBasket {
    	doc /* physical precious metal that is officially recognized as being at least 99.5 percent pure */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:BasketOfCommodities
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;WeightedBasket"/>
    }

  item def <'bullion'> Bullion specializes PreciousMetal {
    	doc /* physical precious metal that is officially recognized as being at least 99.5 percent pure */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:Bullion
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;PreciousMetal"/>
    }

  item def <'commodity basket constituent'> CommodityBasketConstituent specializes Financial_Products_and_Services::WeightedBasketConstituent {
    	doc /* derivative instrument whose primary underlying item is a physical commodity, or the price, or related index, or any other aspect related to a physical commodity */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:CommodityBasketConstituent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;WeightedBasketConstituent"/>
    }

  item def <'commodity derivative underlier'> CommodityDerivativeUnderlier specializes Derivatives_Basics::Underlier {
    	doc /* underlier of a commodity derivative, including, but not limited to, the negotiable commodity itself */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:CommodityDerivativeUnderlier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Underlier"/>
    }

  item def <'commodity derivative'> CommodityDerivative specializes Financial_Instruments::CommodityInstrument, Derivatives_Basics::DerivativeInstrument {
    	doc /* derivative instrument whose primary underlying item is a physical commodity, or the price, or related index, or any other aspect related to a physical commodity */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:CommodityDerivative
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;CommodityInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

  item def <'commodity forward'> CommodityForward specializes CommodityDerivative, Futures_and_Forwards::Forward {
    	doc /* forward contract in which a buyer and seller agree upon delivery of a specified quality and quantity of goods at a specified future date */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:CommodityForward
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;CommodityDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;Forward"/>
    }

  item def <'commodity future'> CommodityFuture specializes CommodityDerivative, Futures_and_Forwards::Future {
    	doc /* futures contract to buy or sell a predetermined amount of a commodity at a specific price on a specific date in the future */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:CommodityFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;CommodityDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Future"/>
    }

  item def <'commodity index'> CommodityIndex specializes Basket_Indices::ReferenceIndex {
    	doc /* investment vehicle that tracks a basket of commodities to measure their price and investment return performance */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:CommodityIndex
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-mkt-bas;ReferenceIndex"/>
    }

  item def <'commodity option'> CommodityOption specializes CommodityDerivative, Options::VanillaOption {
    	doc /* option where the option buyer has the right to buy or sell specified commodities or commodity related index at a fixed price or formula, on or before a specified date */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:CommodityOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;CommodityDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;VanillaOption"/>
    }

  item def <'commodity return leg'> CommodityReturnLeg specializes Swaps::ReturnLeg {
    	doc /* leg of a commodity return swap in which the return is based on a physical commodity, or the price, or behavior of the price, or any other aspect related to a physical commodity */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:CommodityReturnLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;ReturnLeg"/>
    }

  item def <'commodity spot contract'> CommoditySpotContract specializes Financial_Instruments::CommodityInstrument, Financial_Instruments::SpotContract {
    	doc /* contract that involves physical delivery of the commodity asset at settlement */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:CommoditySpotContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;CommodityInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;SpotContract"/>
    }

  item def <'commodity swap'> CommoditySwap specializes CommodityDerivative, Swaps::ReturnSwap {
    	doc /* commodity derivative that includes, without limitation, any swap for which the primary underlying notional item is a physical commodity, or the price, or behavior of the price, or the level of a commodity index, or other aspect related to a physical commodity */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:CommoditySwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;CommodityDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;ReturnSwap"/>
    }

  item def <'energy resource'> EnergyResource specializes Products_and_Services::NegotiableCommodity {
    	doc /* negotiable commodity that is an energy-related product, or a derivative of an energy-related product, including electricity, renewable energy, or any power/energy delivered through a utility network of provider; diesel fuel, fuel oil, gas oil, gasoline, heating oil, jet fuel, kerosene, natural gas, oil (Brent, Tapis, Dubai, WTI) */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:EnergyResource
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
    }

  item def <'energy transmission rights'> EnergyTransmissionRights specializes EnergyResource {
    	doc /* rights to the transmission of power across an electricity distribution network */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:EnergyTransmissionRights
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;EnergyResource"/>
    }

  item def <'environmental resource'> EnvironmentalResource specializes Products_and_Services::NegotiableCommodity {
    	doc /* negotiable commodity including offset credits */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:EnvironmentalResource
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
    }

  item def <'extraction resource'> ExtractionResource specializes Products_and_Services::NegotiableCommodity {
    	doc /* negotiable commodity that is a mineral resource obtained via withdrawal from the natural environment */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:ExtractionResource
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
    }

  item def <'fertilizer resource'> FertilizerResource specializes Products_and_Services::NegotiableCommodity {
    	doc /* negotiable commodity that includes ammonia, diammonium phosphate (DAP), potash, sulphur, urea, urea and ammonium nitrate (UAN) */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:FertilizerResource
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
    }

  item def <'freight resource'> FreightResource specializes Products_and_Services::NegotiableCommodity {
    	doc /* negotiable commodity that is a freight index route */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:FreightResource
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
    }

  item def <'gas commodity'> GasCommodity specializes EnergyResource, ExtractionResource {
    	doc /* extraction resource that is natural gas */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:GasCommodity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;EnergyResource"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;ExtractionResource"/>
    }

  item def <'generated resource'> GeneratedResource specializes Products_and_Services::NegotiableCommodity {
    	doc /* negotiable commodity obtained via some generation process, including electricity, renewable energy, or any power/energy delivered through a utility network or provider */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:GeneratedResource
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
    }

  item def <'grain commodity'> GrainCommodity specializes AgriculturalResource {
    	doc /* agricultural resource that is a small, hard seed, of a food plant, especially of a cereal plant, such as wheat, corn, rye, oats, rice, and millet */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:GrainCommodity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;AgriculturalResource"/>
    }

  item def <'industrial resource'> IndustrialResource specializes Products_and_Services::NegotiableCommodity {
    	doc /* negotiable commodity that is obtained via some industrial process, including construction and manufacturing */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:IndustrialResource
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
    }

  item def <'meat commodity'> MeatCommodity specializes AgriculturalResource {
    	doc /* agricultural resource derived from the flesh of an animal (especially a mammal), such as pork bellies */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:MeatCommodity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;AgriculturalResource"/>
    }

  item def <'metal'> Metal specializes ExtractionResource {
    	doc /* material that, when freshly prepared, polished, or fractured, shows a lustrous appearance, and conducts electricity and heat relatively well */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:Metal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;ExtractionResource"/>
    }

  item def <'oil commodity'> OilCommodity specializes GeneratedResource {
    	doc /* generated resource that is a viscous liquid derived from petroleum, including for use as fuel, or as a lubricant, and the manufacture of many types of paints, plastics, and other materials */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:OilCommodity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;GeneratedResource"/>
    }

  item def <'oil grade'> OilGrade specializes FiBO_FND_ExternalStubs::Classifier {
    	doc /* measure of the viscosity of oil during operation */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:OilGrade
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
      // subClassOf: Classifier - external CMNS
    }

  item def <'paper resource'> PaperResource specializes Products_and_Services::NegotiableCommodity {
    	doc /* negotiable commodity that is a paper product, including containerboard, newsprint, pulp, recovered paper */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:PaperResource
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
    }

  item def <'polypropylene resource'> PolypropyleneResource specializes Products_and_Services::NegotiableCommodity {
    	doc /* negotiable commodity including plastics */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:PolypropyleneResource
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
    }

  item def <'precious metal'> PreciousMetal specializes Metal, Products_and_Services::NegotiableCommodity {
    	doc /* metal that is considered to be rare and/or have a high economic value */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:PreciousMetal
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;Metal"/>
    }

  item def <'service resource'> ServiceResource specializes Products_and_Services::NegotiableCommodity {
    	doc /* negotiable commodity involving services such as transportation, communications, and trade */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:ServiceResource
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;NegotiableCommodity"/>
    }

  item def <'weather derivative'> WeatherDerivative specializes CommodityDerivative, Derivatives_Basics::DerivativeInstrument {
    	doc /* derivative instrument whose primary underlying notional item is based on something related to the weather, for example, the average temperature in Chicago in January */
    	// skos:definition xml:lang="en" (CommoditiesContracts.rdf)
    	// rdfs:label xml:lang="en" (CommoditiesContracts.rdf)
    	// RDF: fibo-der-drc-comm:WeatherDerivative
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;CommodityDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

}
