// ===================================================================
// SecuritiesClassification.sysml
// Source-parity extracted from: FiBO_SEC_Securities.sysml
// Primary source module: SecuritiesClassification.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Securities
// ===================================================================
package Securities_Classification {
  doc/* This ontology defines the fundamental concepts for classifying financial instruments, particularly securities, including, but not limited to classification schemes developed by government, regulatory agencies, and industry to classify the issuers of such securities as well as the securities themselves. (source: dct:abstract, SecuritiesClassification.rdf) */
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

  item def <'asset class'> AssetClass specializes FinancialInstrumentClassifier {
    	doc /* financial instrument classifier for a group of securities that exhibit similar characteristics, behave similarly in the marketplace and are subject to the same laws and regulations */
    	// skos:definition xml:lang="en" (SecuritiesClassification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesClassification.rdf)
    	// RDF: fibo-sec-sec-cls:AssetClass
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-cls;FinancialInstrumentClassifier"/>
    }

  item def <'classification of financial instruments code scheme'> ClassificationOfFinancialInstrumentsCodeScheme specializes FinancialInstrumentClassificationScheme {
    	doc /* classification scheme for set of codes for financial instruments that can be used globally for straight-through processing by all involved participants in an electronic data processing environment */
    	// skos:definition xml:lang="en" (SecuritiesClassification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesClassification.rdf)
    	// RDF: fibo-sec-sec-cls:ClassificationOfFinancialInstrumentsCodeScheme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-cls;FinancialInstrumentClassificationScheme"/>
    }

  item def <'financial instrument classification code'> FinancialInstrumentClassificationCode specializes FinancialInstrumentClassifier {
    	doc /* classifier and code for a financial instrument defined in the ISO 10962 Classification of Financial Instruments (CFI) Code Scheme */
    	// skos:definition xml:lang="en" (SecuritiesClassification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesClassification.rdf)
    	// RDF: fibo-sec-sec-cls:FinancialInstrumentClassificationCode
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-cls;FinancialInstrumentClassifier"/>
    }

  item def <'financial instrument classification scheme'> FinancialInstrumentClassificationScheme specializes Assessments::ClassificationScheme {
    	doc /* classification scheme defining a set of classifiers for financial instruments */
    	// skos:definition xml:lang="en" (SecuritiesClassification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesClassification.rdf)
    	// RDF: fibo-sec-sec-cls:FinancialInstrumentClassificationScheme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;ClassificationScheme"/>
    }

  item def <'financial instrument classifier'> FinancialInstrumentClassifier specializes FiBO_FND_ExternalStubs::Classifier {
    	doc /* classifier for a financial instrument based on its type and features */
    	// skos:definition xml:lang="en" (SecuritiesClassification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesClassification.rdf)
    	// RDF: fibo-sec-sec-cls:FinancialInstrumentClassifier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    }

  item def <'Global Industry Classification Standards classifier'> GlobalIndustryClassificationStandardsClassifier specializes Classification_Schemes::IndustrySectorClassifier, FiBO_FND_ExternalStubs::CodeElement {
    	doc /* four-tiered standardized classification or delineation for an organization based on the principal business activity of the organization */
    	// skos:definition xml:lang="en" (SecuritiesClassification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesClassification.rdf)
    	// RDF: fibo-sec-sec-cls:GlobalIndustryClassificationStandardsClassifier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassifier"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    }

  item def <'industry classification benchmark classifier'> IndustryClassificationBenchmarkClassifier specializes Classification_Schemes::IndustrySectorClassifier, FiBO_FND_ExternalStubs::CodeElement {
    	doc /* standardized classification or delineation for an organization based on their main source of revenue */
    	// skos:definition xml:lang="en" (SecuritiesClassification.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesClassification.rdf)
    	// RDF: fibo-sec-sec-cls:IndustryClassificationBenchmarkClassifier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassifier"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    }

}

// --- fibo-sec-sec-eur ---
