// ===================================================================
// FinancialInstruments.sysml
// Source-parity extracted from: FiBO_FBC_FinancialInstruments.sysml
// Primary source module: FinancialInstruments.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/FinancialInstruments
// Structural note: translated as a split file from FiBO_FBC_FinancialInstruments umbrella.
// ===================================================================
package Financial_Instruments {
  doc/* This ontology defines the fundamental concepts for financial instruments in general, providing the high-level hooks for build-out in more detail in the relevant domain areas. These include, but are not limited to, equities, options, debt instruments, and so forth, some of which may be negotiable. (source: dct:abstract, FinancialInstruments.rdf) */
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

    item def <'calculation agent'> CalculationAgent specializes Financial_Products_and_Services::ThirdPartyAgent {
    doc /* party that is responsible for determining the value of an instrument and in some cases, determines how much the parties owe one another */
    // skos:definition xml:lang="en" (FinancialInstruments.rdf)
    // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ThirdPartyAgent"/>
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
      // RDF: fibo-fbc-fi-fi:CalculationAgent
    }

    item def <'cash instrument'> CashInstrument specializes FinancialInstrument {
        doc /* financial instrument whose value is determined by the market and that is readily transferable (highly liquid) */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
    	// rdfs:label xml:lang="en" (ACTUSTaxonomy.rdf)
      // RDF: fibo-fbc-fi-fi:CashInstrument
    }

    item def <'commodity instrument'> CommodityInstrument specializes FinancialInstrument {
        doc /* financial instrument representing an ownership interest in bulk goods, such as raw materials and primary agricultural products */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
      // RDF: fibo-fbc-fi-fi:CommodityInstrument
    }

    item def <'credit facility'> CreditFacility specializes Debt::CreditAgreementRepaidPeriodically {
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreementRepaidPeriodically"/>
      // RDF: fibo-fbc-fi-fi:CreditFacility
    }

    item def <'currency instrument'> CurrencyInstrument specializes FinancialInstrument {
        doc /* financial instrument used for the purposes of currency trading */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:CurrencyInstrument
    }

    item def <'debt instrument'> DebtInstrument specializes Debt::CreditAgreement, FinancialInstrument {
        doc /* financial instrument and credit agreement evidencing monies owed by the issuer to the holder on terms as specified */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:DebtInstrument
    }

    item def <'derivative instrument'> DerivativeInstrument specializes FinancialInstrument {
        doc /* financial instrument that confers on its holders certain rights or obligations, whose value is derived from one or more underlying assets */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:DerivativeInstrument
    }

    item def <'entitlement'> Entitlement specializes FinancialInstrument {
        doc /* financial instrument that provides the holder an interest in, or the privilege to subscribe to, or to receive specific assets under terms specified */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:Entitlement
    }

    item def <'equity instrument'> EquityInstrument specializes Security {
        doc /* financial instrument representing an ownership interest in an entity or pool of assets */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Security"/>
    	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
      // RDF: fibo-fbc-fi-fi:EquityInstrument
    }

    item def <'exempt security'> ExemptSecurity specializes Security {
        doc /* security that is exempt from certain regulatory rules */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Security"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:ExemptSecurity
    }

    item def <'financial instrument'> FinancialInstrument specializes Contracts::WrittenContract {
        doc /* written contract that gives rise to both a financial asset of one entity and a financial liability of another entity */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:FinancialInstrument
      // subClassOf: WrittenContract - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'financial instrument identifier'> FinancialInstrumentIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
        doc /* sequence of characters uniquely identifying a financial instrument for some purpose and within a specified context */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:FinancialInstrumentIdentifier
      // subClassOf: Identifier - from FIBO_FND_Accounting
    }

    item def <'future'> Future specializes Derivatives_Basics::DerivativeInstrument {
        doc /* derivative instrument that obligates the buyer to receive and the seller to deliver the assets specified at an agreed price, at some later point in time */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:Future
    }

    item def <'issuer'> Issuer specializes Contracts::ContractPrincipal {
        doc /* role of a party that issues (or proposes to issue in a formal filing) one or more financial instruments */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractPrincipal"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:Issuer
      // subClassOf: ContractPrincipal - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'negotiable security'> NegotiableSecurity specializes Security, Contracts::TransferableContract {
        doc /* security that can be transferred to another party */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Security"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;TransferableContract"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:NegotiableSecurity
      // subClassOf also: TransferableContract - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'non-negotiable security'> NonNegotiableSecurity specializes Security {
        doc /* security that is not transferable to another party */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Security"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:NonNegotiableSecurity
    }

    item def <'option'> Option specializes Derivatives_Basics::DerivativeInstrument {
        doc /* derivative instrument that grants to the holder either the privilege to purchase or the privilege to sell the assets specified at a predetermined price or formula at or within a time period in the future */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:Option
    }

    item def <'packaged financial product'> PackagedFinancialProduct specializes Products_and_Services::ContractualProduct, Financial_Products_and_Services::FinancialProduct {
        doc /* financial product that acts as a container for at least one financial instrument, including other financial products, and whose value is derived from, or based on a reference asset, market measure, or investment strategy */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;ContractualProduct"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialProduct"/>
      // RDF: fibo-fbc-fi-fi:PackagedFinancialProduct
      // subClassOf: ContractualProduct - from FIBO_FND_ProductsAndServices; FinancialProduct - from FinancialProductsAndServices
    }

    item def <'promissory note'> PromissoryNote specializes DebtInstrument {
        doc /* debt instrument that is a written promise by one party to another that commits that party to pay a specified sum on demand or within a specified time frame under specified terms */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DebtInstrument"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:PromissoryNote
    }

    item def <'redemption provision'> RedemptionProvision specializes Contracts::ContractualCommitment {
        doc /* contract provision enabling the issuer (writer) to regain possession through repayment of some stipulated price */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:RedemptionProvision
      // subClassOf: ContractualCommitment - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'securities transaction'> SecuritiesTransaction specializes Financial_Products_and_Services::Trade {
        doc /* transaction between two or more parties involving the exchange of commonly defined financial products */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Trade"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:SecuritiesTransaction
    }

    item def <'security'> Security specializes FinancialInstrument {
        doc /* financial instrument that can be bought or sold */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:Security
    }

    item def <'spot contract'> SpotContract specializes FinancialInstrument {
    doc /* financial instrument that settles for immediate delivery on a specified date */
    // skos:definition xml:lang="en" (FinancialInstruments.rdf)
    // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:SpotContract
    }

    item def <'standardized terms'> StandardizedTerms specializes Contracts::ContractualCommitment {
        doc /* contract terms that, grouped together, that are generally and consistently reused across many contracts, published by some organization, and capable of being incorporated into a contract by reference */
        // skos:definition xml:lang="en" (FinancialInstruments.rdf)
        // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:StandardizedTerms
      // subClassOf: ContractualCommitment - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'underlier'> Underlier specializes FiBO_FND_ExternalStubs::Undergoer {
    doc /* indicates the currency purchased with respect to a currency or related instrument */
    // skos:definition xml:lang="en" (FinancialInstruments.rdf)
    // rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Undergoer"/>
    	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
      // RDF: fibo-fbc-fi-fi:Underlier
      // subClassOf: Undergoer - external CMNS
    }

    item def <'unique transaction identifier'> UniqueTransactionIdentifier specializes Financial_Products_and_Services::TradeIdentifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;TradeIdentifier"/>
    	doc /* sequence of characters identifying a financial transaction uniquely whenever useful and agreed by the parties or community involved in the transaction */     	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
    	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
      // RDF: fibo-fbc-fi-fi:UniqueTransactionIdentifier
    }

}

// --- fibo-fbc-fi-ip (InstrumentPricing) ---
