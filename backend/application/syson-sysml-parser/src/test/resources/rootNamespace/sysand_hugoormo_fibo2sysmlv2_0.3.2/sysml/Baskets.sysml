// ===================================================================
// Baskets.sysml
// Source-parity extracted from: FiBO_SEC_Securities.sysml
// Primary source module: Baskets.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Securities
// ===================================================================
package Baskets {
  doc/* This ontology defines the concept of a tradable container of securities, indices, and/or market rates, and identifies the elements that can be constituents of a such a basket. (source: dct:abstract, Baskets.rdf) */
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

  item def <'market basket'> BasketOfIndices specializes Financial_Products_and_Services::WeightedBasket, Financial_Dates::DatedStructuredCollection {
    	doc /* derivative instrument where the holder has the right but may not have the obligation, depending on the nature of the instrument, to enter into the underlying contract, or pay or receive payment related to the underlying financial rate (or rate contract) on a specified future date based on a specified future rate and term */
    	// skos:definition xml:lang="en" (Baskets.rdf)
    	// rdfs:label xml:lang="en" (Baskets.rdf)
    	// RDF: fibo-sec-sec-bsk:BasketOfIndices
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;WeightedBasket"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
    }

  item def <'basket Of indices constituent'> BasketOfIndicesConstituent specializes Financial_Products_and_Services::WeightedBasketConstituent, Financial_Dates::DatedCollectionConstituent {
    	doc /* single constituent of a basket of indices */
    	// skos:definition xml:lang="en" (Baskets.rdf)
    	// rdfs:label xml:lang="en" (Baskets.rdf)
    	// RDF: fibo-sec-sec-bsk:BasketOfIndicesConstituent
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;WeightedBasketConstituent"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedCollectionConstituent"/>
    }

  item def <'basket of securities'> BasketOfSecurities specializes Financial_Products_and_Services::WeightedBasket, Financial_Dates::DatedStructuredCollection {
    	doc /* derivative instrument whose underlier is based on a security, including collections of securities and indices based on securities */
    	// skos:definition xml:lang="en" (Baskets.rdf)
    	// rdfs:label xml:lang="en" (Baskets.rdf)
    	// RDF: fibo-sec-sec-bsk:BasketOfSecurities
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;WeightedBasket"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
    }

  item def <'mixed basket'> MixedBasket specializes Financial_Products_and_Services::WeightedBasket, Financial_Dates::DatedStructuredCollection {
    	doc /* basket that has a mix of constituents, including securities and indices */
    	// skos:definition xml:lang="en" (Baskets.rdf)
    	// rdfs:label xml:lang="en" (Baskets.rdf)
    	// RDF: fibo-sec-sec-bsk:MixedBasket
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;WeightedBasket"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
    }

  item def <'securities basket constituent'> SecuritiesBasketConstituent specializes Financial_Products_and_Services::WeightedBasketConstituent, Financial_Dates::DatedCollectionConstituent {
    	doc /* basket of securities whose constituents are listed shares */
    	// skos:definition xml:lang="en" (Baskets.rdf)
    	// rdfs:label xml:lang="en" (Baskets.rdf)
    	// RDF: fibo-sec-sec-bsk:SecuritiesBasketConstituent
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;WeightedBasketConstituent"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedCollectionConstituent"/>
    }

}

// --- fibo-sec-sec-cls ---
