// ===================================================================
// SecuritiesPools.sysml
// Source-parity extracted from: FiBO_SEC_Securities.sysml
// Primary source module(s): SecuritiesPools.rdf (for current translated content)
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Securities
// ===================================================================
package Securities_Pools {
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

  item def <'pool'> Pool specializes FiBO_FND_ExternalStubs::Collection {
    	doc /* combination of resources for a common purpose or benefit */
    // R3 direct exact pair: materialized from Pool hasMember PoolConstituent
    // and PoolConstituent isMemberOf Pool.
    item poolConstituent: PoolConstituent [0..*];
    	// skos:definition xml:lang="en" (SecuritiesPools.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesPools.rdf)
    	// RDF: fibo-sec-sec-pls:Pool
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Collection"/>
    }

  item def <'instrument pool'> InstrumentPool specializes Pool {
    	doc /* pool consisting of financial instruments that may be included in the same investment vehicle */
    	// skos:definition xml:lang="en" (SecuritiesPools.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesPools.rdf)
    	// RDF: fibo-sec-sec-pls:InstrumentPool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;Pool"/>
    }

  item def <'debt pool'> DebtPool specializes InstrumentPool {
    	doc /* pool consisting of debt instruments, such as bonds, loans or mortgages */
    	// skos:definition xml:lang="en" (SecuritiesPools.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesPools.rdf)
    	// RDF: fibo-sec-sec-pls:DebtPool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;InstrumentPool"/>
    }

  item def <'fund family'> FundFamily specializes FiBO_FND_ExternalStubs::Collection {
    	doc /* collection of managed investments that are all managed by a single investment institution */
    	// skos:definition xml:lang="en" (SecuritiesPools.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesPools.rdf)
    	// RDF: fibo-sec-sec-pls:FundFamily
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Collection"/>
    }

  item def <'instrument pool as asset'> InstrumentPoolAsAsset specializes Accounting_Equity::FinancialAsset {
    	doc /* financial asset in the form of an instrument pool */
    	// skos:definition xml:lang="en" (SecuritiesPools.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesPools.rdf)
    	// RDF: fibo-sec-sec-pls:InstrumentPoolAsAsset
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;FinancialAsset"/>
    }

  item def <'pool constituent'> PoolConstituent specializes FiBO_FND_ExternalStubs::Constituent {
    	doc /* component of a pool */
    	// skos:definition xml:lang="en" (SecuritiesPools.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesPools.rdf)
    	// RDF: fibo-sec-sec-pls:PoolConstituent
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Constituent"/>
    }

  item def <'securities pool'> SecuritiesPool specializes InstrumentPool {
    	doc /* pool of securities organized for the purpose of issuing notes against those securities */
    	// skos:definition xml:lang="en" (SecuritiesPools.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesPools.rdf)
    	// RDF: fibo-sec-sec-pls:SecuritiesPool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;InstrumentPool"/>
    }

  item def SecuritiesPoolConstituent{ // RDF: fibo-sec-sec-pls:SecuritiesPoolConstituent
    }

    // RDF: fibo-sec-sec-pls:SecuritiesPoolConstituent
}

// --- fibo-sec-sec-rst ---
