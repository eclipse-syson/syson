// ===================================================================
// CreditDefaultSwaps.sysml
// Source-parity extracted from: FiBO_DER_CreditDerivatives.sysml
// Primary source module: CreditDefaultSwaps.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/CreditDerivatives
// ===================================================================
package Credit_Default_Swaps {
  doc/* Credit default swaps are financial instruments that allow the transfer of credit risk among market participants, potentially facilitating greater efficiency in the pricing and distribution/offset of credit risk. They are bilateral contracts in which one party (the protection seller) agrees to provide payment(s) to the other party (the protection buyer) should a credit event occur against the underlying. The underlier for a CDS may be a specified debt (the reference obligation), a specific debt issuer (reference entity), in which case the credit events involving the entity is what triggers the payment, a basket of reference entities and/or reference obligations, or a credit index (reference index). This ontology defines the concept of a basic credit default swap as well as more specific kinds of CDS and specifies related details. (source: dct:abstract, CreditDefaultSwaps.rdf) */
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

// CreditDerivative, DerivativeTerms
// Swap
// RegisteredAgent, WeightedBasket
// PriceDeterminationMethod
// SettlementEvent
// DatedStructuredCollection
// Asset
// ContractParty, ContractThirdParty
// Buyer, Seller
// PartyRole
// ReferenceIndex

  item def <'asset-backed credit default swap'> AssetBackedCreditDefaultSwap specializes CreditDefaultSwap {
  	doc /* credit default swap whose underlying reference obligation is an asset-backed security rather than corporate credit */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:AssetBackedCreditDefaultSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-cr-cds;CreditDefaultSwap"/>
  }
  item def <'basket credit default swap'> BasketCreditDefaultSwap specializes MultiNameCreditDefaultSwap {
  	doc /* credit default swap that references a bespoke, synthetic portfolio of underlying assets whose components have been agreed to for a specific OTC derivative by the parties to the transaction */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:BasketCreditDefaultSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-cr-cds;MultiNameCreditDefaultSwap"/>
  }
  item def <'basket of credit default swaps'> BasketOfCreditDefaultSwaps specializes Financial_Products_and_Services::WeightedBasket, Financial_Dates::DatedStructuredCollection {
  	doc /* basket of reference obligations are credit default swap instruments */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:BasketOfCreditDefaultSwaps
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;WeightedBasket"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
  }
  item def <'cash settlement method'> CashSettlementMethod specializes Instrument_Pricing::PriceDeterminationMethod {
  	doc /* strategy for calculating or otherwise establishing a reference final price for the contract */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:CashSettlementMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;PriceDeterminationMethod"/>
  }
  item def <'cash settlement terms'> CashSettlementTerms specializes Settlement::SettlementTerms {
  	doc /* commitment to determine the delivery strategy at the time of exercise */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:CashSettlementTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;SettlementTerms"/>
  }
  item def <'contingent credit default swap'> ContingentCreditDefaultSwap specializes CreditDefaultSwap {
  	doc /* credit default swap in which an additional triggering event is required */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:ContingentCreditDefaultSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-cr-cds;CreditDefaultSwap"/>
  }
  item def <'credit default swap'> CreditDefaultSwap specializes Derivatives_Basics::CreditDerivative, Swaps::Swap {
  	doc /* bilateral contract in which one party (protection seller) agrees to provide payment to the other party (protection buyer) should a credit event occur against the underlying, which could be a specified debt (the reference obligation), a specific debt issuer (reference entity), a basket of reference entities and/or reference obligations, or a credit index (reference index) */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:CreditDefaultSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;CreditDerivative"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;Swap"/>
  }
  item def <'credit default swap index'> CreditDefaultSwapIndex specializes Basket_Indices::ReferenceIndex {
  	doc /* index whose underlying reference obligations are credit default swap instruments */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:CreditDefaultSwapIndex
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-mkt-bas;ReferenceIndex"/>
  }
  item def <'credit event notice'> CreditEventNotice specializes FiBO_FND_ExternalStubs::Notice {
  	doc /* irrevocable written or verbal notice that states that a triggering event has occurred */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:CreditEventNotice
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Notice"/>
    // subClassOf: Notice - external CMNS
  }
  item def <'credit protection terms'> CreditProtectionTerms specializes Derivatives_Basics::DerivativeTerms {
  	doc /* legal terms that define triggering events and associated conditions related to settlement */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:CreditProtectionTerms
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;DerivativeTerms"/>
  }
  item def <'deliverable asset'> DeliverableObligation specializes Ownership::Asset {
  	doc /* asset that must be delivered as a part of the process of settling a credit default swap */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:DeliverableObligation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Asset"/>
  }
  item def <'deliverable obligation buyer'> DeliverableObligationBuyer specializes Contracts::ContractParty, Products_and_Services::Buyer {
  	doc /* contract party that is obliged to purchase a deliverable obligation (asset) if a triggering event occurs, depending on the event and the contract */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:DeliverableObligationBuyer
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Buyer"/>
  }
  item def <'deliverable obligation seller'> DeliverableObligationSeller specializes Contracts::ContractParty, Products_and_Services::Seller {
  	doc /* contract party that is obliged to sell a deliverable obligation (asset) if a triggering event occurs, depending on the event and the contract */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:DeliverableObligationSeller
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Seller"/>
  }
  item def <'escrow agent'> EscrowAgent specializes Financial_Products_and_Services::RegisteredAgent, Contracts::ContractThirdParty {
  	doc /* third party that holds an asset or funds before they are formally transferred from one party to another party, per the terms of a contract, within some specified time period and/or when a triggering event occurs */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:EscrowAgent
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;RegisteredAgent"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractThirdParty"/>
  }
  item def <'index credit default swap'> IndexCreditDefaultSwap specializes MultiNameCreditDefaultSwap {
  	doc /* credit default swap that references a family of standardized credit derivative indices, where the underlying reference entities are a defined basket of credit from a particular geographic region (e.g. Asia, North America, Europe), and/or credit rating level (e.g. emerging markets, high yield, investment grade) */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:IndexCreditDefaultSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-cr-cds;MultiNameCreditDefaultSwap"/>
  }
  item def <'index tranche credit default swap'> IndexTrancheCreditDefaultSwap specializes MultiNameCreditDefaultSwap {
  	doc /* credit default swap that references a synthetic collateralized debt obligation (CDO) based on a credit index where each tranche references a different segment of the loss distribution of the underlying index */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:IndexTrancheCreditDefaultSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-cr-cds;MultiNameCreditDefaultSwap"/>
  }
  item def <'loan credit default swap'> LoanCreditDefaultSwap specializes CreditDefaultSwap {
  	doc /* credit default swap whose underlying reference obligation is limited strictly to syndicated secured loans, rather than any type of corporate debt */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:LoanCreditDefaultSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-cr-cds;CreditDefaultSwap"/>
  }
  item def <'multi-name credit default swap'> MultiNameCreditDefaultSwap specializes CreditDefaultSwap {
  	doc /* credit default swap that references more than one obligation or borrower */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:MultiNameCreditDefaultSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-cr-cds;CreditDefaultSwap"/>
  }
  item def <'notifying party'> NotifyingParty specializes Parties::PartyRole {
  	doc /* party responsible for issuing one or more formal notices indicating that an event that is relevant to a given contract has taken place */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:NotifyingParty
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
    // subClassOf: PartyRole - from FIBO_FND_Parties
  }
  item def <'obligation-specific credit event'> ObligationSpecificCreditEvent specializes TriggeringEvent, Credit_Events::CreditEvent {
  	doc /* credit event that relates to an individual credit agreement or debt instrument (reference obligation) */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:ObligationSpecificCreditEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;CreditEvent"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-cr-cds;TriggeringEvent"/>
  }
  item def <'physical settlement terms'> PhysicalSettlementTerms specializes Settlement::SettlementTerms {
  	doc /* commitment to deliver the actual underlying asset on the specified delivery date, rather than cash */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:PhysicalSettlementTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;SettlementTerms"/>
  }
  item def <'settlement auction'> SettlementAuction specializes Settlement::SettlementEvent {
  	doc /* independently administered synthetic auction process on a set of defined deliverable obligations that sets the reference final price that can be used to facilitate cash settlement of all covered transactions following a credit event */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:SettlementAuction
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-stl;SettlementEvent"/>
  }
  item def <'single name credit default swap'> SingleNameCreditDefaultSwap specializes CreditDefaultSwap {
  	doc /* credit default swap whose underlying risk is a single reference obligation, or a single reference entity, such as a corporation or a sovereign borrower */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// RDF: fibo-der-cr-cds:SingleNameCreditDefaultSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-cr-cds;CreditDefaultSwap"/>
  }
  item def <'triggering event'> TriggeringEvent specializes FiBO_FND_ExternalStubs::FiOccurrence, FiBOOccurrences::FiBOOccurrence {
  	doc /* event that relates to or triggers some aspect of a credit default swap */
  	// skos:definition xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CreditDefaultSwaps.rdf)
  	// rdfs:label xml:lang="en" (CashFlows.rdf)
  	// RDF: fibo-der-cr-cds:TriggeringEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
    // subClassOf: FiBOOccurrence - external CMNS
  }
}
