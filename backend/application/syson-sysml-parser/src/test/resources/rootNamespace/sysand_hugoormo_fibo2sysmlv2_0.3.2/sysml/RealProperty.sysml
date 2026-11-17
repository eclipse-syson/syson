// ===================================================================
// RealProperty.sysml
// Source-parity extracted from: FiBO_FND_Places.sysml
// Primary source module: RealProperty.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Places
// ===================================================================

package Real_Property {
  doc/* This ontology defines concepts including real and personal property from a legal perspective, as well as assessments of those assets, for reference for taxation, lending, and related purposes. (source: dct:abstract, RealProperty.rdf) */
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

  item def PersonalProperty specializes Ownership::Asset {
    	doc /* asset that is a movable item or possession not fixed to land */
    	// skos:definition xml:lang="en" (RealProperty.rdf)
    // rdfs:label xml:lang="en" (RealProperty.rdf)
    	// RDF: fibo-fnd-plc-rp:PersonalProperty
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Asset"/>
    }
  item def <'property inspection'> PropertyInspection specializes Assessments::AssessmentEvent {
    doc /* event that involves analyzing one or more aspects of a real property */
    	// skos:definition xml:lang="en" (RealProperty.rdf)
    	// rdfs:label xml:lang="en" (RealProperty.rdf)
    	// RDF: fibo-fnd-plc-rp:PropertyInspection
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AssessmentEvent"/>
    }
  item def <'property inspection report'> PropertyInspectionReport specializes Assessments::AssessmentReport {
    	doc /* report covering the findings of a property inspection */
    	// skos:definition xml:lang="en" (RealProperty.rdf)
    	// rdfs:label xml:lang="en" (RealProperty.rdf)
    	// RDF: fibo-fnd-plc-rp:PropertyInspectionReport
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AssessmentReport"/>
    }
  item def RealEstate specializes Ownership::Asset {
    	doc /* real property, interests in mortgages on real property (including interests in mortgages on leaseholds of land or improvements thereon), and shares in qualified real estate investment trusts */
    	// skos:definition xml:lang="en" (RealProperty.rdf)
    // rdfs:label xml:lang="en" (RealProperty.rdf)
    	// RDF: fibo-fnd-plc-rp:RealEstate
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Asset"/>
    }
  item def RealProperty specializes Real_Property::RealEstate, Accounting_Equity::PhysicalAsset {
    doc /* physical asset defined as land together with any structures that are permanently attached to that land, such as houses, trees, fences and improvements */
    // skos:definition xml:lang="en" (RealProperty.rdf)
    // rdfs:label xml:lang="en" (RealProperty.rdf)
    	// RDF: fibo-fnd-plc-rp:RealProperty
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;PhysicalAsset"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-rp;RealEstate"/>
    }
  item def <'real property appraisal'> RealPropertyAppraisal specializes Assessments::Appraisal {
    	doc /* value assessment that estimates the amount of money some real property is worth */
    	// skos:definition xml:lang="en" (RealProperty.rdf)
    	// rdfs:label xml:lang="en" (RealProperty.rdf)
    	// RDF: fibo-fnd-plc-rp:RealPropertyAppraisal
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;Appraisal"/>
    }
  item def <'real property identifier'> RealPropertyIdentifier specializes FiBO_FND_ExternalStubs::ContextualIdentifier {
    	doc /* unique identifier given to identify a specific real property in some jurisidiction */
    	// skos:definition xml:lang="en" (RealProperty.rdf)
    	// rdfs:label xml:lang="en" (RealProperty.rdf)
    	// RDF: fibo-fnd-plc-rp:RealPropertyIdentifier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtid;ContextualIdentifier"/>
    }
  item def TractIdentifier { // RDF: fibo-fnd-plc-rp:TractIdentifier
    // OWL-deprecated: true
    // OWL-equivalentClass: <owl:equivalentClass rdf:resource="&cmns-sfc;TractIdentifier"/>
    // RDF: fibo-fnd-plc-rp:TractIdentifier
  }
  item def TractOfLand { // RDF: fibo-fnd-plc-rp:TractOfLand
    // OWL-deprecated: true
    // OWL-equivalentClass: <owl:equivalentClass rdf:resource="&cmns-sfc;TractOfLand"/>
    // RDF: fibo-fnd-plc-rp:TractOfLand
  }
}
