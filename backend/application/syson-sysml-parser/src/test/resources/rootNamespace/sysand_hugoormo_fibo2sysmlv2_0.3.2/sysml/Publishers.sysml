// ===================================================================
// Publishers.sysml
// Source-parity extracted from: FiBO_BE_FunctionalEntities.sysml
// Primary source module: Publishers.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/FunctionalEntities
// Structural note: paired split with FunctionalEntities.sysml from the same source umbrella.
// ===================================================================
package Publishers {
  doc/* The concept of a publisher is central to the notion of a data provider in financial markets. This ontology defines the fundamental concepts for publishers of information, including entities whose primary function is to publish, and those (whether or not they are publishers in that sense) that play the role of the publisher of some information. (source: dct:abstract, Publishers.rdf) */
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

  item def <'market data provider'> MarketDataProvider specializes Publishers::Publisher {
    doc /* publisher of data relevant to financial markets */
    // skos:definition xml:lang="en" (Publishers.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-pub;Publisher"/>
    	// rdfs:label xml:lang="en" (Publishers.rdf)
      // RDF: fibo-be-fct-pub:MarketDataProvider
    }

  item def <'publication'> Publication specializes FiBO_FND_ExternalStubs::Document {
    doc /* document offered for general distribution and usually produced in multiple copies */
    // skos:definition xml:lang="en" (Publishers.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
    	// rdfs:label xml:lang="en" (Publishers.rdf)
      // RDF: fibo-be-fct-pub:Publication
    }

  item def <'publisher'> Publisher specializes Parties::PartyRole {
    doc /* role of person(s) or organization(s) responsible for the printing or distribution of an item, whether in digital or print (physical) form */
    // skos:definition xml:lang="en" (Publishers.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
    	// rdfs:label xml:lang="en" (Publishers.rdf)
      // RDF: fibo-be-fct-pub:Publisher
    }

}
