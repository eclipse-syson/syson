// ===================================================================
// SecuritiesRestrictions.sysml
// Source-parity extracted from: FiBO_SEC_Securities.sysml
// Primary source module: SecuritiesRestrictions.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Securities
// ===================================================================
package Securities_Restrictions {
  doc/* This ontology defines the concepts related to restrictions on finanicial instruments, securities and listings. (source: dct:abstract, SecuritiesRestrictions.rdf) */
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

  item def <'securities restriction'> SecuritiesRestriction specializes Legal_Capacity::LegalObligation {
    	doc /* legal obligation that is applicable to a financial instrument or listing as mandated in a law or by contract */
    	// skos:definition xml:lang="en" (SecuritiesRestrictions.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesRestrictions.rdf)
    	// RDF: fibo-sec-sec-rst:SecuritiesRestriction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalObligation"/>
    }

  item def <'contractual restriction'> ContractualRestriction specializes SecuritiesRestriction, Contracts::ContractualCommitment {
    	doc /* contract terms setting out restrictions on either the holder or the issuer of the security, as specified in the terms of the instrument itself */
    	// skos:definition xml:lang="en" (SecuritiesRestrictions.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesRestrictions.rdf)
    	// RDF: fibo-sec-sec-rst:ContractualRestriction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-rst;SecuritiesRestriction"/>
    }

  item def <'individual investor'> IndividualInvestor specializes Ownership_Parties::Investor {
    	doc /* investor that is a person, rather than an organization, who may or may not be considered a qualified investor from a legal perspective */
    	// skos:definition xml:lang="en" (SecuritiesRestrictions.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesRestrictions.rdf)
    	// RDF: fibo-sec-sec-rst:IndividualInvestor
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;Investor"/>
    }

  item def <'institutional investor'> InstitutionalInvestor specializes Ownership_Parties::Investor {
    	doc /* investor that pools money to purchase securities, real property, and other investment assets or originates loans */
    	// skos:definition xml:lang="en" (SecuritiesRestrictions.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesRestrictions.rdf)
    	// RDF: fibo-sec-sec-rst:InstitutionalInvestor
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;Investor"/>
    }

  item def <'investors&apos; domicile restriction'> InvestorsDomicileRestriction specializes LegalHoldingRestriction {
    	doc /* legal holding restriction that specifies where holders of a security may be domiciled */
    	// skos:definition xml:lang="en" (SecuritiesRestrictions.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesRestrictions.rdf)
    	// RDF: fibo-sec-sec-rst:InvestorsDomicileRestriction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-rst;LegalHoldingRestriction"/>
    }

  item def <'legal holding restriction'> LegalHoldingRestriction specializes SecuritiesRestriction {
    	doc /* security restriction applicable to holding the security */
    	// skos:definition xml:lang="en" (SecuritiesRestrictions.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesRestrictions.rdf)
    	// RDF: fibo-sec-sec-rst:LegalHoldingRestriction
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-rst;SecuritiesRestriction"/>
    }

  item def <'qualified investor restriction'> QualifiedInvestorRestriction specializes LegalHoldingRestriction {
    	doc /* legal holding restriction that defines the concept of a qualified investor for a given purpose and specifies that only such qualified investors may hold the security */
    	// skos:definition xml:lang="en" (SecuritiesRestrictions.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesRestrictions.rdf)
    	// RDF: fibo-sec-sec-rst:QualifiedInvestorRestriction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-rst;LegalHoldingRestriction"/>
    }

  item def <'securities regulation'> SecuritiesRegulation specializes Legal_Capacity::Regulation {
    	doc /* regulation codified in law specific to securities and investments */
    	// skos:definition xml:lang="en" (SecuritiesRestrictions.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesRestrictions.rdf)
    	// RDF: fibo-sec-sec-rst:SecuritiesRegulation
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Regulation"/>
    }

  item def <'trading restriction'> TradingRestriction specializes SecuritiesRestriction {
    	doc /* restriction that limits trading in some manner, typically summarized on term sheets or in other documentation, including but not limited to restrictions on day trading */
    	// skos:definition xml:lang="en" (SecuritiesRestrictions.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesRestrictions.rdf)
    	// RDF: fibo-sec-sec-rst:TradingRestriction
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-rst;SecuritiesRestriction"/>
    }

}

// --- fibo-sec-sec-sch ---
