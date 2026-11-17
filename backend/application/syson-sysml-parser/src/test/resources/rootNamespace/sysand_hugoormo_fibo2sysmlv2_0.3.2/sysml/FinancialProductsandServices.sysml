// ===================================================================
// FinancialProductsandServices.sysml
// Source-parity extracted from: FiBO_FBC_ProductsAndServices.sysml
// Primary source module: FinancialProductsAndServices.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/ProductsAndServices
// Structural note: translated as a split file from FiBO_FBC_ProductsAndServices umbrella.
// ===================================================================
package Financial_Products_and_Services {
  doc/* This ontology defines concepts that extend the Foundations (FND) Products and Services concepts specifically for the financial industry, including financial product, financial service, and financial service provider. (source: dct:abstract, FinancialProductsAndServices.rdf) */
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
// FunctionalEntity, FunctionalBusinessEntity, ServiceProvider, Publication
// Lifecycle, LifecycleEvent, LifecycleEventOccurrence, LifecycleOccurrence, LifecycleStage, LifecycleStageOccurrence

  item def <'agency agreement'> AgencyAgreement specializes Contracts::WrittenContract {
    doc /* agreement that designates a party as a registered agent to represent and act on behalf of another party in some, typically legal, financial, or medical capacity */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:AgencyAgreement
    // subClassOf: WrittenContract - from FIBO_FND_Agreements (AgreementsPackage)
  }
  item def <'agent for service of process'> AgentForServiceOfProcess specializes RegisteredAgent {
    doc /* registered agent (person or organization) designated by a business entity, such as a corporation, to receive legal correspondence on behalf of the business entity in the jurisdiction in which the agent's address is located */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;RegisteredAgent"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:AgentForServiceOfProcess
  }
  item def <'basket'> Basket specializes FiBO_FND_ExternalStubs::Collection {
    doc /* collection of goods, services, or other things (e.g., financial contracts) that can be purchased and sold in some marketplace */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Collection"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:Basket
    // subClassOf: Collection - external CMNS
  }
  item def <'basket constituent'> BasketConstituent specializes FiBO_FND_ExternalStubs::Constituent {
    doc /* component of a basket */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Constituent"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:BasketConstituent
    // subClassOf: Constituent - external CMNS
  }
  item def <'breach of contract'> BreachOfContract specializes ContractLifecycleEvent, FiBOOccurrences::OccurrenceKind{
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ContractLifecycleEvent"/>
    // RDF: fibo-fbc-pas-fpas:BreachOfContract
  }
  item def <'breach of covenant'> BreachOfCovenant specializes ContractLifecycleEvent, FiBOOccurrences::OccurrenceKind{
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ContractLifecycleEvent"/>
    // RDF: fibo-fbc-pas-fpas:BreachOfCovenant
  }
  item def <'broker'> Broker specializes Contracts::ContractThirdParty {
      doc /* any party that acts as an intermediary between a buyer and a seller, usually charging a commission */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractThirdParty"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:Broker
    // subClassOf: ContractThirdParty - from FIBO_FND_Agreements (AgreementsPackage)
  }
  item def <'broker dealer'> BrokerDealer specializes Functional_Entities::FunctionalEntity {
      doc /* any party in the business of buying and selling securities, operating as both a broker and a dealer, depending on the transaction */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalEntity"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:BrokerDealer
    // subClassOf: FunctionalEntity - from FunctionalEntities
  }
  item def <'catalog'> Catalog specializes Publishers::Publication {
    doc /* publication including a list of products available for sale with their descriptions and possibly prices */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-pub;Publication"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:Catalog
    // subClassOf: Publication - from FunctionalEntities
  }
  item def <'contract lifecycle'> ContractLifecycle specializes Lifecycles::Lifecycle {
    doc /* lifecycle of an agreement, including, but not limited to a credit agreement, financial instrument, or other formal contract, from initial stages through retirement */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;Lifecycle"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:ContractLifecycle
    // subClassOf: Lifecycle - from FIBO_FND_Arrangements (ArrangementsPackage)
  }
  item def <'contract lifecycle event'> ContractLifecycleEvent specializes Lifecycles::LifecycleEvent {
    doc /* kind of event that occurs during one or more stages of the lifecycle of an agreement */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleEvent"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:ContractLifecycleEvent
    // subClassOf: LifecycleEvent - from FIBO_FND_Arrangements (ArrangementsPackage)
  }
  item def <'contract lifecycle event FiBOOccurrence'> ContractLifecycleEventOccurrence specializes Lifecycles::LifecycleEventOccurrence {
    doc /* actual occurrence of an event during a specific stage of a specific contract lifecycle */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleEventOccurrence"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:ContractLifecycleEventOccurrence
    // subClassOf: LifecycleEventOccurrence - from FIBO_FND_Arrangements (ArrangementsPackage)
  }
  item def <'contract lifecycle FiBOOccurrence'> ContractLifecycleOccurrence specializes Lifecycles::LifecycleOccurrence {
    doc /* realization of the lifecycle of a specific contract */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleOccurrence"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:ContractLifecycleOccurrence
    // subClassOf: LifecycleOccurrence - from FIBO_FND_Arrangements (ArrangementsPackage)
  }
  item def <'contract lifecycle stage'> ContractLifecycleStage specializes Lifecycles::LifecycleStage {
    doc /* phase in the lifecycle of an agreement */
      // skos:definition xml:lang="en" (FinancialProductsandServices.rdf)
      // rdfs:label xml:lang="en" (FinancialProductsandServices.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStage"/>
    // RDF: fibo-fbc-pas-fpas:ContractLifecycleStage
    // subClassOf: LifecycleStage - from FIBO_FND_Arrangements (ArrangementsPackage)
  }
  item def <'contract lifecycle stage FiBOOccurrence'> ContractLifecycleStageOccurrence specializes Lifecycles::LifecycleStageOccurrence {
    doc /* realization, from start to finish of a phase in an occurrence of a specific contract lifecycle */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStageOccurrence"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:ContractLifecycleStageOccurrence
    // subClassOf: LifecycleStageOccurrence - from FIBO_FND_Arrangements (ArrangementsPackage)
  }
  item def <'dealer'> Dealer specializes Functional_Entities::ServiceProvider {
      doc /* any party that purchases goods or services for resale and acts on their own behalf in a transaction */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;ServiceProvider"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:Dealer
    // subClassOf: ServiceProvider - from FunctionalEntities
  }
  item def <'exposure'> Exposure {
    doc /* the extent to which an individual or organization is unprotected and open to damage, danger, risk of suffering a loss, or uncertainty */
      // skos:definition xml:lang="en" (FinancialProductsandServices.rdf)
      // rdfs:label xml:lang="en" (FinancialProductsandServices.rdf)
    // RDF: fibo-fbc-pas-fpas:Exposure
  }
  item def <'financial exposure'> FinancialExposure specializes Exposure, Analytics::Expression{
    doc /* the extent to which an individual or organization is open to risk of suffering a loss in a transaction, or with respect to some investment or set of investments, e.g., some holding; the amount one stands to lose in that transaction or investment */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Exposure"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:FinancialExposure
    // subClassOf also: Expression - external CMNS
  }
  item def <'financial intermediation service'> FinancialIntermediationService specializes FinancialService {
    doc /* any financial service in which a third party (the intermediary) matches lenders and investors with entrepreneurs and other borrowers in need of capital */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:FinancialIntermediationService
  }
  item def <'financial product'> FinancialProduct specializes Products_and_Services::Product {
    doc /* product provided to consumers and businesses by financial institutions such as banks, insurance companies, brokerage firms, consumer finance companies, and investment companies */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Product"/>
  	// skos:definition xml:lang="en" (FinancialProductsandServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsandServices.rdf)
    // RDF: fibo-fbc-pas-fpas:FinancialProduct
    // subClassOf: Product - from FIBO_FND_ProductsAndServices (ProductsServicesPackage)
  }
  item def <'financial product catalog'> FinancialProductCatalog specializes Catalog {
    doc /* catalog of financial products and/or services available for sale with their description and other product details */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Catalog"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:FinancialProductCatalog
  }
  item def <'financial service'> FinancialService specializes FiBO_FND_ExternalStubs::Service {
    doc /* service provided to consumers and businesses by financial institutions such as banks, insurance companies, brokerage firms, consumer finance companies, and investment companies */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;Service"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:FinancialService
    // subClassOf: Service - external CMNS
  }
  item def <'financial service provider'> FinancialServiceProvider specializes Functional_Entities::FunctionalBusinessEntity, Functional_Entities::ServiceProvider {
    doc /* functional entity either licensed to provide financial services to consumers and/or businesses or established by law to provide financial services, such as a central bank */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalBusinessEntity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;ServiceProvider"/>
  	// skos:definition xml:lang="en" (FinancialProductsandServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsandServices.rdf)
    // RDF: fibo-fbc-pas-fpas:FinancialServiceProvider
    // subClassOf: FunctionalBusinessEntity, ServiceProvider - from FunctionalEntities
  }
  item def <'holding'> Holding specializes Ownership::Ownership {
    doc /* real or personal property (assets), including but not limited to financial assets, to which one holds title and of which one has possession */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Ownership"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:Holding
    // subClassOf: Ownership - from FIBO_FND_OwnershipAndControl (OwnershipControlPackage)
  }
  item def <'legal agent'> LegalAgent specializes FiBO_FND_ExternalStubs::AgentRole {
    doc /* any party that has been legally empowered to act on behalf of another party */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;AgentRole"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:LegalAgent
    // subClassOf: AgentRole - external CMNS
  }
  item def <'licensed agent'> LicensedAgent specializes LegalAgent, Executives::ResponsibleParty, Legal_Capacity::Licensee{
    doc /* any individual who is licensed to perform a legally binding function, and who has been legally empowered to act on behalf of another party */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Licensee"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;LegalAgent"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;ResponsibleParty"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:LicensedAgent
    // subClassOf also: Licensee - external CMNS
  }
  item def <'offeree'> Offeree specializes Parties::PartyRole {
    doc /* party that receives an offer from something from someone (i.e., an offerer) based on the terms of the offering */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:Offeree
    // subClassOf: PartyRole - from FIBO_FND_Parties
  }
  item def <'offering'> Offering {
    doc /* expression of interest in providing something to someone that is contingent upon acceptance, forbearance, or some other consideration, as might be desired by an offeree(s) */
      // skos:definition xml:lang="en" (FinancialProductsandServices.rdf)
      // rdfs:label xml:lang="en" (FinancialProductsandServices.rdf)
    // RDF: fibo-fbc-pas-fpas:Offering
  }
  item def <'offeror'> Offeror specializes Parties::PartyRole {
    doc /* party that proposes to make something available to someone (i.e., an offeree) based on the terms of the offering */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:Offeror
    // subClassOf: PartyRole - from FIBO_FND_Parties
  }
  item def <'position'> Position specializes Holding {
    doc /* investor's stake, i.e., a holding, in a particular asset (such as an individual security) */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Holding"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:Position
  }
  item def <'product lifecycle'> ProductLifecycle specializes Lifecycles::Lifecycle {
    doc /* lifecycle specific to a product or product family */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;Lifecycle"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:ProductLifecycle
    // subClassOf: Lifecycle - from FIBO_FND_Arrangements (ArrangementsPackage)
  }
  item def <'product lifecycle event'> ProductLifecycleEvent specializes Lifecycles::LifecycleEvent {
    doc /* kind of event that occurs during one or more stages of a product lifecycle */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleEvent"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:ProductLifecycleEvent
  }
  item def <'product lifecycle event FiBOOccurrence'> ProductLifecycleEventOccurrence specializes Lifecycles::LifecycleEventOccurrence {
    doc /* actual occurrence of an event that happens during a specific stage of a specific product lifecycle */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleEventOccurrence"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:ProductLifecycleEventOccurrence
  }
  item def <'product lifecycle FiBOOccurrence'> ProductLifecycleOccurrence specializes Lifecycles::LifecycleOccurrence {
    doc /* realization of the lifecycle of a specific product */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleOccurrence"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:ProductLifecycleOccurrence
  }
  item def <'product lifecycle stage'> ProductLifecycleStage specializes Lifecycles::LifecycleStage {
    doc /* phase in a product lifecycle */
      // skos:definition xml:lang="en" (FinancialProductsandServices.rdf)
      // rdfs:label xml:lang="en" (FinancialProductsandServices.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStage"/>
    // RDF: fibo-fbc-pas-fpas:ProductLifecycleStage
  }
  item def <'product lifecycle stage FiBOOccurrence'> ProductLifecycleStageOccurrence specializes Lifecycles::LifecycleStageOccurrence {
    doc /* realization of a specific stage in the lifecycle of a given product */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStageOccurrence"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:ProductLifecycleStageOccurrence
  }
  item def <'registered agent'> RegisteredAgent specializes Contracts::ContractParty, LegalAgent {
      doc /* legal agent designated by some party to represent them and act on their behalf under a formal agency agreement */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;LegalAgent"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:RegisteredAgent
    // subClassOf: ContractParty - from FIBO_FND_Agreements (AgreementsPackage)
  }
  item def <'regulated commodity'> RegulatedCommodity specializes Products_and_Services::Commodity {
    doc /* commodity under the jurisdiction of the regulatory agency, such as the Commodities Futures Trading Commission (CFTF) in the United States, which includes any commodity traded in an organized contracts market */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Commodity"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:RegulatedCommodity
    // subClassOf: Commodity - from FIBO_FND_ProductsAndServices (ProductsServicesPackage)
  }
  item def <'third-party agent'> ThirdPartyAgent specializes LegalAgent, Functional_Entities::ServiceProvider, Legal_Capacity::Licensee{
    doc /* any service provider that is licensed to perform a legally binding function and has been legally empowered to act on behalf of another party */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Licensee"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;LegalAgent"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;ServiceProvider"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:ThirdPartyAgent
    // subClassOf also: Licensee - external CMNS
  }
  item def <'trade'> Trade specializes FiBO_FND_ExternalStubs::Situation {
      doc /* situation that realizes an agreement between parties participating in a voluntary action of buying and selling goods and services */
    // skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
    // rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Situation"/>
    // RDF: fibo-fbc-pas-fpas:Trade
    // subClassOf: Situation - external CMNS
  }
  item def <'trade identifier'> TradeIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
    doc /* sequence of characters identifying a trade within some context */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:TradeIdentifier
    // subClassOf: Identifier - from FIBO_FND_Accounting
  }
  item def <'trade lifecycle'> TradeLifecycle specializes Lifecycles::Lifecycle {
    doc /* lifecycle that defines the evolution of a trade, from initiation through settlement */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;Lifecycle"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:TradeLifecycle
  }
  item def <'trade lifecycle event'> TradeLifecycleEvent specializes Lifecycles::LifecycleEvent {
    doc /* kind of event that occurs during one or more stages of the lifecycle of a trade */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleEvent"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:TradeLifecycleEvent
  }
  item def <'trade lifecycle event FiBOOccurrence'> TradeLifecycleEventOccurrence specializes Lifecycles::LifecycleEventOccurrence {
    doc /* realization of an event that happens during a specific stage of a specific trade lifecycle */
      // skos:definition xml:lang="en" (FinancialProductsandServices.rdf)
      // rdfs:label xml:lang="en" (FinancialProductsandServices.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleEventOccurrence"/>
    // RDF: fibo-fbc-pas-fpas:TradeLifecycleEventOccurrence
  }
  item def <'trade lifecycle FiBOOccurrence'> TradeLifecycleOccurrence specializes Lifecycles::LifecycleOccurrence {
    doc /* realization of the lifecycle for a specific trade */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleOccurrence"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:TradeLifecycleOccurrence
  }
  item def <'trade lifecycle stage'> TradeLifecycleStage specializes Lifecycles::LifecycleStage {
    doc /* phase in the lifecycle of a trade */
      // skos:definition xml:lang="en" (FinancialProductsandServices.rdf)
      // rdfs:label xml:lang="en" (FinancialProductsandServices.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStage"/>
    // RDF: fibo-fbc-pas-fpas:TradeLifecycleStage
  }
  item def <'trade lifecycle stage FiBOOccurrence'> TradeLifecycleStageOccurrence specializes Lifecycles::LifecycleStageOccurrence {
    doc /* realization of a phase in the lifecycle of a specific trade */
      // skos:definition xml:lang="en" (FinancialProductsandServices.rdf)
      // rdfs:label xml:lang="en" (FinancialProductsandServices.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStageOccurrence"/>
    // RDF: fibo-fbc-pas-fpas:TradeLifecycleStageOccurrence
  }
  item def <'trader'> Trader specializes Functional_Entities::ServiceProvider {
    doc /* party that engages in the transfer of financial assets in any financial market on behalf of a client or the financial services provider */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;ServiceProvider"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:Trader
    // subClassOf: ServiceProvider - from FunctionalEntities
  }
  item def <'trading strategy'> TradingStrategy specializes Objectives::Strategy {
    doc /* approach used for buying and selling in the securities markets */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Strategy"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:TradingStrategy
    // subClassOf: Strategy - external CMNS
  }
  item def <'weighted basket'> WeightedBasket specializes Basket, FiBO_FND_ExternalStubs::StructuredCollection {
    doc /* basket whose constituents have some relative importance with respect to one another */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Basket"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;StructuredCollection"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:WeightedBasket
    // subClassOf also: StructuredCollection - external CMNS
  }
  item def <'weighted basket constituent'> WeightedBasketConstituent specializes BasketConstituent {
    doc /* component of a basket whose relative importance with respect to other basket constituents is known */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;BasketConstituent"/>
  	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
    // RDF: fibo-fbc-pas-fpas:WeightedBasketConstituent
  }

}
