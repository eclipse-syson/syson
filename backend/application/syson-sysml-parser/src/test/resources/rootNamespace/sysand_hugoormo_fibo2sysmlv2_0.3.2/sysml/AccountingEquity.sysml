// ===================================================================
// AccountingEquity.sysml
// Source-parity extracted from: FiBO_FND_Accounting.sysml
// Primary source module: AccountingEquity.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Accounting
// ===================================================================

package Accounting_Equity {
  doc/* This ontology defines equity-related concepts for use in defining other FIBO ontology elements. These are based on basic accounting principles as they relate to equity, debt, assets and liabilities of a firm. Equity forms the basis for ownership of certain forms of corporate body. (source: dct:abstract, AccountingEquity.rdf) */
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

  item def <'capital surplus'> CapitalSurplus specializes PaidInCapital {
  	doc /* equity that is manifested in the form of shares in an entity, fund or structured product */
  	// skos:definition xml:lang="en" (AccountingEquity.rdf)
  	// rdfs:label xml:lang="en" (AccountingEquity.rdf)
  	// RDF: fibo-fnd-acc-aeq:CapitalSurplus
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;PaidInCapital"/>
  }

  item def <'earnings before interest, taxes, depreciation and amortization'> EarningsBeforeInterestTaxesDepreciationAmortization specializes Analytics::Expression {
  	doc /* measure of potential cash flow that excludes the effects of capital structure, taxes and non-cash expenses */
  	// skos:definition xml:lang="en" (AccountingEquity.rdf)
  	// rdfs:label xml:lang="en" (AccountingEquity.rdf)
  	// RDF: fibo-fnd-acc-aeq:EarningsBeforeInterestTaxesDepreciationAmortization
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
  }

  item def <'financial asset'> FinancialAsset specializes Ownership::TangibleAsset {
  	doc /* non-physical, tangible asset whose value is derived from a contractual claim, such as bank deposits, bonds, stocks, rights, certificates, and bank balances */
  	// skos:definition xml:lang="en" (AccountingEquity.rdf)
  	// rdfs:label xml:lang="en" (AccountingEquity.rdf)
  	// RDF: fibo-fnd-acc-aeq:FinancialAsset
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;TangibleAsset"/>
  }

  item def Income specializes Currency_Amount::MonetaryAmount {
  	doc /* revenue received during a period of time */
  	// skos:definition xml:lang="en" (AccountingEquity.rdf)
    // rdfs:label xml:lang="en" (AccountingEquity.rdf)
  	// RDF: fibo-fnd-acc-aeq:Income
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
  }

  item def OwnersEquity specializes Analytics::Expression {
    doc /* owners' share in a business plus operating profit */
    // skos:definition xml:lang="en" (AccountingEquity.rdf)
    // rdfs:label xml:lang="en" (AccountingEquity.rdf)
  	// RDF: fibo-fnd-acc-aeq:OwnersEquity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
  }

  item def <'paid-in capital'> PaidInCapital specializes OwnersEquity {
  	doc /* assets received from investors in exchange for an ownership interest */
  	// skos:definition xml:lang="en" (AccountingEquity.rdf)
  	// rdfs:label xml:lang="en" (AccountingEquity.rdf)
  	// RDF: fibo-fnd-acc-aeq:PaidInCapital
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;OwnersEquity"/>
  }

  item def <'physical asset'> PhysicalAsset specializes Ownership::TangibleAsset {
  	doc /* tangible asset that has a material form, such as property, equipment, and inventory */
  	// skos:definition xml:lang="en" (AccountingEquity.rdf)
  	// rdfs:label xml:lang="en" (AccountingEquity.rdf)
  	// RDF: fibo-fnd-acc-aeq:PhysicalAsset
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;TangibleAsset"/>
  }

  item def <'retained earnings'> RetainedEarnings specializes OwnersEquity {
  	doc /* net profits kept to accumulate in a business after dividends are paid */
  	// skos:definition xml:lang="en" (AccountingEquity.rdf)
  	// rdfs:label xml:lang="en" (AccountingEquity.rdf)
  	// RDF: fibo-fnd-acc-aeq:RetainedEarnings
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;OwnersEquity"/>
  }

  item def <'shareholders equity'> ShareholdersEquity specializes OwnersEquity {
  	doc /* equity that is manifested in the form of shares in an entity, fund or structured product */
  	// skos:definition xml:lang="en" (AccountingEquity.rdf)
    // rdfs:label xml:lang="en" (AccountingEquity.rdf)
  	// RDF: fibo-fnd-acc-aeq:ShareholdersEquity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;OwnersEquity"/>
  }

}
