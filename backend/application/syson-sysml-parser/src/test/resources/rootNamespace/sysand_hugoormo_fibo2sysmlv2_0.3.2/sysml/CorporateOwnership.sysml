// ===================================================================
// CorporateOwnership.sysml
// Source-parity extracted from: FiBO_BE_OwnershipAndControl.sysml
// Primary source module: CorporateOwnership.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/OwnershipAndControl
// Structural note: split from FiBO_BE_OwnershipAndControl umbrella.
// ===================================================================
package Corporate_Ownership {
  doc/* This ontology defines concepts relating to corporation-specific ownership. Roles are defined in terms of the ownership enjoyed by the party, and are the specific examples of these concepts as they apply to companies incorporated by the issuance of shares. (source: dct:abstract, CorporateOwnership.rdf) */
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

// ConstitutionalOwner
// Control, Ownership, ControlledThing, ControllingParty, Owner

  item def <'beneficial owner'> BeneficialOwner specializes Ownership::Owner {
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Owner"/>
  	doc /* indicates the number of units of */   	// skos:definition xml:lang="en" (CorporateOwnership.rdf)
  	// rdfs:label xml:lang="en" (CorporateOwnership.rdf)
    // RDF: fibo-be-oac-cown:BeneficialOwner
    // subClassOf: Owner - from FIBO_FND_OwnershipAndControl
  }

  item def <'beneficial ownership'> BeneficialOwnership specializes Ownership::Ownership {
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Ownership"/>
    // RDF: fibo-be-oac-cown:BeneficialOwnership
    // subClassOf: Ownership - from FIBO_FND_OwnershipAndControl
  }

  item def <'purchase lot'> PurchaseLot specializes Accounting_Equity::FinancialAsset {
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;FinancialAsset"/>
  	doc /* financial asset that is a block of securities or other financial assets bought in one transaction on a given date at a specific price */   	// skos:definition xml:lang="en" (CorporateOwnership.rdf)
  	// rdfs:label xml:lang="en" (CorporateOwnership.rdf)
    // RDF: fibo-be-oac-cown:PurchaseLot
    // subClassOf: FinancialAsset - from FIBO_FND_Accounting (AccountingEquity)
  }

  item def <'shareholder'> Shareholder specializes Ownership_Parties::ConstitutionalOwner, Contracts::Counterparty{
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;Counterparty"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;ConstitutionalOwner"/>
  	doc /* party that owns shares in and has rights and responsibilities with respect to some asset, provided in exchange for investment */   	// skos:definition xml:lang="en" (CorporateOwnership.rdf)
  	// rdfs:label xml:lang="en" (CorporateOwnership.rdf)
    // RDF: fibo-be-oac-cown:Shareholder
    // subClassOf also: Counterparty - from FIBO_FND_Agreements
  }

  item def <'shareholding'> Shareholding specializes Accounting_Equity::FinancialAsset {
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;FinancialAsset"/>
  	doc /* financial institution that holds customers&apos; securities for safekeeping */   	// skos:definition xml:lang="en" (EquityInstruments.rdf)
  	// rdfs:label xml:lang="en" (CorporateOwnership.rdf)
    // RDF: fibo-be-oac-cown:Shareholding
    // subClassOf: FinancialAsset - from FIBO_FND_Accounting (AccountingEquity)
  }

  item def <'tax lot'> TaxLot specializes Accounting_Equity::FinancialAsset {
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;FinancialAsset"/>
  	doc /* financial asset that is a block of securities or other financial assets with a distinct cost basis for tax reporting purposes */   	// skos:definition xml:lang="en" (CorporateOwnership.rdf)
  	// rdfs:label xml:lang="en" (CorporateOwnership.rdf)
    // RDF: fibo-be-oac-cown:TaxLot
    // subClassOf: FinancialAsset - from FIBO_FND_Accounting (AccountingEquity)
  }
}

// --- Executives.rdf ---
