// ===================================================================
// SecurityBasedDerivatives.sysml
// Source-parity extracted from: FiBO_DER_SecurityBasedDerivatives.sysml
// Primary source module: SecurityBasedDerivatives.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/SecurityBasedDerivatives
// ===================================================================
package Security_Based_Derivatives {
  doc/* This ontology defines common concepts for derivatives based on securities as their underliers, including those based on indices or baskets of these assets. (source: dct:abstract, SecurityBasedDerivatives.rdf) */
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

// BasketOfSecurities
// DerivativeInstrument

  item def <'basket of debt instruments'> BasketOfDebtInstruments specializes Baskets::BasketOfSecurities {
  	doc /* basket of securities whose constituents are debt instruments */
  	// skos:definition xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-der-sbd-sbd:BasketOfDebtInstruments
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-bsk;BasketOfSecurities"/>
  }
  item def <'capitalized lease obligation debt basket'> CapitalizedLeaseObligationDebtBasket specializes BasketOfDebtInstruments {
  	doc /* basket of debt instruments whose constituents are contracts entitling a renter the temporary use of an asset and, in accounting terms, has asset ownership characteristics */
  	// skos:definition xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-der-sbd-sbd:CapitalizedLeaseObligationDebtBasket
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;BasketOfDebtInstruments"/>
  }
  item def <'contribution debt basket'> ContributionDebtBasket specializes BasketOfDebtInstruments {
  	doc /* basket of debt instruments that allows a company to incur an amount of indebtedness that is equal to (or in top tier sponsor transactions in the U.S., up to two times) the amount of equity contributed to the group */
  	// skos:definition xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-der-sbd-sbd:ContributionDebtBasket
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;BasketOfDebtInstruments"/>
  }
  item def <'credit facility debt basket'> CreditFacilityDebtBasket specializes BasketOfDebtInstruments {
  	doc /* basket of securities whose constituents are credit agreements that allow the borrower to periodically take out money over an extended period of time rather than reapplying for a loan every time they need funds */
  	// skos:definition xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-der-sbd-sbd:CreditFacilityDebtBasket
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;BasketOfDebtInstruments"/>
  }
  item def <'debt instrument derivative'> DebtInstrumentDerivative specializes SecurityBasedDerivative {
  	doc /* basket of debt instruments that provides additional capacity for potential funding and does not require the proceeds to be used for any particular purpose */
  	// skos:definition xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-der-sbd-sbd:DebtInstrumentDerivative
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;SecurityBasedDerivative"/>
  }
  item def <'equity derivative'> EquityDerivative specializes SecurityBasedDerivative {
  	doc /* security-based derivative whose underlier is based on equities (e.g. shares, basket of equities or index) or their cashflow(s) */
  	// skos:definition xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-der-sbd-sbd:EquityDerivative
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;SecurityBasedDerivative"/>
  }
  item def <'general debt basket'> GeneralDebtBasket specializes BasketOfDebtInstruments {
  	doc /* basket of debt instruments that provides additional capacity for potential funding and does not require the proceeds to be used for any particular purpose */
  	// skos:definition xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-der-sbd-sbd:GeneralDebtBasket
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;BasketOfDebtInstruments"/>
  }
  item def <'incremental facility debt basket'> IncrementalFacilityDebtBasket specializes CreditFacilityDebtBasket {
  	doc /* basket of credit facilities whose constituents are extensible, allowing companies to borrow an additional term loan or revolving credit facility (or increase the commitments applicable thereto) under the same credit agreement subject to certain parameters */
  	// skos:definition xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-der-sbd-sbd:IncrementalFacilityDebtBasket
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;CreditFacilityDebtBasket"/>
  }
  item def <'local lines of credit debt basket'> LocalLinesOfCreditDebtBasket specializes BasketOfDebtInstruments {
  	doc /* basket of debt instruments that may be relevant for companies with international operations, often permitting debt to be incurred by a non-guarantor restricted subsidiary */
  	// skos:definition xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-der-sbd-sbd:LocalLinesOfCreditDebtBasket
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;BasketOfDebtInstruments"/>
  }
  item def NonGuarantorDebtBasket specializes BasketOfDebtInstruments { // RDF: fibo-der-sbd-sbd:Non-GuarantorDebtBasket
    // RDF: fibo-der-sbd-sbd:NonGuarantorDebtBasket
  }
  item def <'ratio debt basket'> RatioDebtBasket specializes BasketOfDebtInstruments {
  	doc /* basket of debt instruments whose constituents are specified based on a leverage ratio based on total debt rather than only secured debt */
  	// skos:definition xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-der-sbd-sbd:RatioDebtBasket
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;BasketOfDebtInstruments"/>
  }
  item def <'security-based derivative'> SecurityBasedDerivative specializes Derivatives_Basics::DerivativeInstrument {
  	doc /* derivative instrument whose underlier is based on a security, including collections of securities and indices based on securities */
  	// skos:definition xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-der-sbd-sbd:SecurityBasedDerivative
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
  }
}
