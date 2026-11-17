// ===================================================================
// ProductsandServices.sysml
// Source-parity extracted from: FiBO_FND_ProductsAndServices.sysml
// Primary source module: ProductsAndServices.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/ProductsAndServices
// ===================================================================

package Products_and_Services {
  doc/* This ontology defines fundamental concepts for buyers, sellers, clients, customers, products, goods and services for use in other FIBO ontologies. (source: dct:abstract, ProductsAndServices.rdf) */
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

item def <'buyer'> Buyer specializes Parties::PartyRole {
	doc /* party that purchases something in exchange for money or other consideration under a contract of sale */
	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:Buyer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
item def <'client'> Client specializes Parties::PartyRole {
	doc /* party that purchases professional services from, or has a formal relationship to purchase services from another party */
	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:Client
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
item def <'client identifier'> ClientIdentifier specializes Parties::PartyRoleIdentifier {
  	doc /* sequence of characters uniquely identifying a client within the context of some organization */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:ClientIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pty-pty;PartyRoleIdentifier"/>
  }
item def <'commodity'> Commodity specializes Good {
  	doc /* material resource used in commerce that is interchangeable with other commodities of the same type */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:Commodity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Good"/>
  }
item def <'consumer'> Consumer specializes Parties::PartyRole {
  	doc /* party that utilizes economic goods or services, typically for personal, family, or household purposes */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:Consumer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
item def <'contractual product'> ContractualProduct specializes Product {
  	doc /* product that takes the form of an agreement */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:ContractualProduct
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Product"/>
  }
item def <'contractual template product'> ContractualTemplateProduct specializes ContractualProduct, OffTheShelfProduct {
  	doc /* contractual product that is finalized through specification of values for parameters defined in a template */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:ContractualTemplateProduct
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;ContractualProduct"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;OffTheShelfProduct"/>
  }
item def <'custom product'> CustomProduct specializes Product {
  	doc /* product that is made to order, commissioned based on a customer's specifications */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:CustomProduct
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Product"/>
  }
item def <'customer'> Customer specializes Buyer {
	doc /* party that receives or consumes products (goods or services) and has the ability to choose between different products and suppliers */
	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:Customer
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Buyer"/>
  }
item def <'customer identifier'> CustomerIdentifier specializes Parties::PartyRoleIdentifier {
  	doc /* sequence of characters uniquely identifying a customer within the context of some organization */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:CustomerIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pty-pty;PartyRoleIdentifier"/>
  }
item def <'good'> Good {
	doc /* physical, produced item over which ownership rights can be established, whose ownership can be passed from one party to another by engaging in transactions, and that is not money or real estate */
	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
	// RDF: fibo-fnd-pas-pas:Good
  }
item def <'negotiable commodity'> NegotiableCommodity specializes Commodity {
	doc /* commodity that can be bought or sold in some marketplace */
	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:NegotiableCommodity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Commodity"/>
  }
item def <'off-the-shelf product'> OffTheShelfProduct specializes Product {
  	doc /* product that is readily available from merchandise in stock, or can be quickly and easily configured to order, not specially designed or custom-made */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:OffTheShelfProduct
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Product"/>
  }
item def <'producer'> Producer specializes Parties::PartyRole {
  	doc /* grower, maker, fabricator, or manufacturer of some product or other good */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:Producer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
item def <'product'> Product {
	doc /* commercially distributed good that is (1) tangible property, (2) the output or result of a fabrication, manufacturing, or production process, or (3) something that passes through a distribution channel before being consumed or used. */
	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
	// RDF: fibo-fnd-pas-pas:Product
  }
item def <'product identifier'> ProductIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
  	doc /* sequence of characters uniquely identifying a product in some context */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:ProductIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
  }
item def Sale specializes REA_Transactions::TransactionEvent, FiBO_FND_ExternalStubs::Situation {
  	doc /* exchange of goods or services for money */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:Sale
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Situation"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;TransactionEvent"/>
  }
item def <'seller'> Seller specializes Parties::PartyRole {
	doc /* party that makes, offers or contracts to make a sale to an actual or potential buyer */
	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:Seller
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
item def <'service agreement'> ServiceAgreement specializes Contracts::WrittenContract {
  	doc /* written contract between a client and service provider whereby the service provider supplies some service in the form of time, effort, and/or expertise in exchange for compensation */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:ServiceAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
  }
item def <'supplier'> Supplier specializes Parties::PartyRole {
  	doc /* party that provides goods or services that some party wants or needs, especially over a long period of time */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:Supplier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
item def <'transaction confirmation'> TransactionConfirmation specializes FiBO_FND_ExternalStubs::LegalDocument {
  	doc /* written communication from a seller or service provider reciting the relevant details of a transaction */
  	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:TransactionConfirmation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;LegalDocument"/>
  }
item def <'transaction event'> TransactionEvent specializes FiBOOccurrences::OccurrenceKind {
	doc /* any sale, assignment, lease, license, loan, advance, contribution, or other transfer of any interest in or right to use any property (tangible or intangible) or money, however that transaction is effected, and regardless of whether the terms of the transaction are formally documented */
	// skos:definition xml:lang="en" (ProductsAndServices.rdf)
  	// rdfs:label xml:lang="en" (ProductsAndServices.rdf)
  	// RDF: fibo-fnd-pas-pas:TransactionEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
  }
}
