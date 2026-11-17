// ===================================================================
// SecurityAssets.sysml
// Source-parity extracted from: FiBO_SEC_Securities.sysml
// Primary source module: SecurityAssets.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Securities
// ===================================================================
package Security_Assets {
  doc/* This ontology defines basic concepts such as portfolio, security holding and holder, and extends the notion of a financial asset to include an acquisition price. (source: dct:abstract, SecurityAssets.rdf) */
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

  item def <'financial asset'> FinancialAsset specializes Ownership::TangibleAsset {
    	doc /* non-physical, tangible asset whose value is derived from a contractual claim, such as bank deposits, bonds, stocks, rights, certificates, and bank balances */
    	// skos:definition xml:lang="en" (SecurityAssets.rdf)
    	// rdfs:label xml:lang="en" (SecurityAssets.rdf)
    	// RDF: fibo-sec-sec-ast:FinancialAsset
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;TangibleAsset"/>
    }

  item def <'portfolio'> Portfolio specializes Accounting_Equity::FinancialAsset, FiBO_FND_ExternalStubs::Collection {
    	doc /* collection of investments (financial assets) such as stocks, bonds and cash equivalents, as well as mutual funds, real estate, and so forth */
    	// skos:definition xml:lang="en" (SecurityAssets.rdf)
    	// rdfs:label xml:lang="en" (SecurityAssets.rdf)
    	// RDF: fibo-sec-sec-ast:Portfolio
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;FinancialAsset"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Collection"/>
    }

  item def <'has acquisition price'> PortfolioHolding { // RDF: fibo-sec-sec-ast:PortfolioHolding
    }

    // RDF: fibo-sec-sec-ast:PortfolioHolding
}

// --- fibo-sec-sec-bsk ---
