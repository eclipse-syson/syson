// ===================================================================
// CorporateBodies.sysml
// Source-parity extracted from: FiBO_BE_Corporation.sysml
// Primary source module: CorporateBodies.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/LegalEntities
// Structural note: paired split with Corporations.sysml from the same source umbrella.
// ===================================================================
package Corporate_Bodies {
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
  item def <'benefit corporation'> BenefitCorporation specializes NotForProfitCorporation {
    doc /* not-for-profit corporation set up under specific state legislation, typically to provide some social benefit, without an obligation to maximize shareholder return */
    // skos:definition xml:lang="en" (CorporateBodies.rdf)
    // rdfs:label xml:lang="en" (CorporateBodies.rdf)
    // RDF: fibo-be-le-cb:BenefitCorporation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-cb;NotForProfitCorporation"/>
  }

  item def <'common interest development corporation'> CommonInterestDevelopmentCorporation specializes NotForProfitCorporation {
    doc /* not-for-profit corporation set up under specific state legislation as a business entity for homeowners' associations */
    // skos:definition xml:lang="en" (CorporateBodies.rdf)
    // rdfs:label xml:lang="en" (CorporateBodies.rdf)
    // RDF: fibo-be-le-cb:CommonInterestDevelopmentCorporation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-cb;NotForProfitCorporation"/>
  }

  item def <'corporation'> Corporation specializes FiBO_FND_ExternalStubs::LegalEntity {
    doc /* formal organization that is a legal entity (artificial person) distinct from its owners, created under the jurisdiction of the laws of a state or nation */
    // skos:definition xml:lang="en" (CorporateBodies.rdf)
    // rdfs:label xml:lang="en" (CorporateBodies.rdf)
    // RDF: fibo-be-le-cb:Corporation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalEntity"/>
    // subClassOf: LegalEntity - external CMNS
  }

  item def <'for profit corporation'> ForProfitCorporation specializes Corporate_Bodies::Corporation {
    doc /* corporation whose objective is to make money, i.e., to ensure realization of a financial benefit such that the amount of revenue gained from a business activity exceeds the expenses, costs and taxes needed to sustain that activity */
    // skos:definition xml:lang="en" (CorporateBodies.rdf)
    // rdfs:label xml:lang="en" (CorporateBodies.rdf)
    // RDF: fibo-be-le-cb:ForProfitCorporation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-cb;Corporation"/>
  }

  item def <'incorporation guarantee'> IncorporationGuarantee specializes Legal_Core::Constitution {
    doc /* guarantee that is part of the financial basis by which some legal entity is incorporated */
    // skos:definition xml:lang="en" (CorporateBodies.rdf)
    // rdfs:label xml:lang="en" (CorporateBodies.rdf)
    // RDF: fibo-be-le-cb:IncorporationGuarantee
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-cor;Constitution"/>
  }

  item def <'instrument of incorporation'> InstrumentOfIncorporation specializes Legal_Core::Constitution {
    doc /* memorandum and articles of association by which some legal entity is established */
    // skos:definition xml:lang="en" (CorporateBodies.rdf)
    // rdfs:label xml:lang="en" (CorporateBodies.rdf)
    // RDF: fibo-be-le-cb:InstrumentOfIncorporation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-cor;Constitution"/>
  }

  item def <'not-for-profit corporation'> NotForProfitCorporation specializes Corporate_Bodies::Corporation, Legal_Persons::NotForProfitOrganization {
    doc /* corporation approved by its jurisdictional oversight and tax authorities as operating for educational, charitable, social, religious, civic or humanitarian purposes */
    // skos:definition xml:lang="en" (CorporateBodies.rdf)
    // rdfs:label xml:lang="en" (CorporateBodies.rdf)
    // RDF: fibo-be-le-cb:NotForProfitCorporation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-cb;Corporation"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-fbo;NotForProfitOrganization"/>
  }

  item def <'stock corporation'> StockCorporation specializes Corporate_Bodies::Corporation {
    doc /* corporation that has shareholders, each of whom receives a portion of the ownership of the corporation through shares of stock */
    // skos:definition xml:lang="en" (CorporateBodies.rdf)
    // rdfs:label xml:lang="en" (CorporateBodies.rdf)
    // RDF: fibo-be-le-cb:StockCorporation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-cb;Corporation"/>
  }
}
