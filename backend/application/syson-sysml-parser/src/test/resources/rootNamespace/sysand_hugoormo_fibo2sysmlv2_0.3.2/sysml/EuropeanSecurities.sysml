// ===================================================================
// EuropeanSecurities.sysml
// Source-parity extracted from: FiBO_SEC_Securities.sysml
// Primary source module(s): EuropeanSecurities.rdf (for current translated content)
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Securities
// ===================================================================
package European_Securities {
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

  item def <'Basel III Designation'> BaselIIIDesignation specializes Securities_Classification::FinancialInstrumentClassifier {
    	doc /* security for which MiFID reporting is required */
    	// skos:definition xml:lang="en" (EuropeanSecurities.rdf)
    	// rdfs:label xml:lang="en" (EuropeanSecurities.rdf)
    	// RDF: fibo-sec-sec-eur:BaselIIIDesignation
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-cls;FinancialInstrumentClassifier"/>
    }

  item def <'MiFID regulated security'> MiFIDRegulatedSecurity specializes Financial_Instruments::Security {
    	doc /* security for which MiFID reporting is required */
    	// skos:definition xml:lang="en" (EuropeanSecurities.rdf)
    	// rdfs:label xml:lang="en" (EuropeanSecurities.rdf)
    	// RDF: fibo-sec-sec-eur:MiFIDRegulatedSecurity
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Security"/>
    }

}

// --- fibo-sec-sec-id ---
