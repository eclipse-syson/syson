// ===================================================================
// BusinessCenters.sysml
// Source-parity extracted from: FiBO_FBC_FunctionalEntities.sysml
// Primary source module: BusinessCenters.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/FunctionalEntities
// Structural note: translated as a split file from FiBO_FBC_FunctionalEntities umbrella.
// ===================================================================
package Business_Centers {
  doc/* This ontology refines the notion of a business center for reference in defining markets and exchanges, clearing houses, and other functional entities as appropriate. The ontology covers the concept of an FpML business center (excluding those that are business day adjustments), with a focus on a physical place where business is transacted, where relevant. (source: dct:abstract, BusinessCenters.rdf) */
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

    item def BusinessCenter {
      // RDF: fibo-fbc-fct-bc:BusinessCenter
    }

    item def <'business center code'> BusinessCenterCode specializes FiBO_FND_ExternalStubs::GeographicRegionIdentifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;GeographicRegionIdentifier"/>
    	doc /* code used to denote a metropolitan area where business is conducted */     	// skos:definition xml:lang="en" (BusinessCenters.rdf)
    	// rdfs:label xml:lang="en" (BusinessCenters.rdf)
      // RDF: fibo-fbc-fct-bc:BusinessCenterCode
      // subClassOf: GeographicRegionIdentifier - external CMNS
    }

    item def <'business center code set'> BusinessCenterCodeScheme specializes FiBO_FND_CodeSets_And_Schemes::CodeSet {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeSet"/>
    	doc /* coding scheme used to define a set of codes for municipalities or business centers */     	// skos:definition xml:lang="en" (BusinessCenters.rdf)
    	// rdfs:label xml:lang="en" (BusinessCenters.rdf)
      // RDF: fibo-fbc-fct-bc:BusinessCenterCodeScheme
      // subClassOf: CodeSet - external CMNS
    }

    item def <'business day adjustment code'> BusinessDayAdjustmentCode specializes FiBO_FND_ExternalStubs::CodeElement {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    	doc /* code used to denote a convention for specifying what happens when a date falls on a day that is weekend or holiday in some municipality or business center */     	// skos:definition xml:lang="en" (BusinessCenters.rdf)
    	// rdfs:label xml:lang="en" (BusinessCenters.rdf)
      // RDF: fibo-fbc-fct-bc:BusinessDayAdjustmentCode
      // subClassOf: CodeElement - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def Municipality {
      // RDF: fibo-fbc-fct-bc:Municipality
    }

}

// --- fibo-fbc-fct-breg (BusinessRegistries) ---
