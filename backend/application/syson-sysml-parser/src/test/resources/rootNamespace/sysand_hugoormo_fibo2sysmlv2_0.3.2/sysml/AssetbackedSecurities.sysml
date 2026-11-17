// ===================================================================
// AssetbackedSecurities.sysml
// Source-parity extracted from: FiBO_SEC_Debt.sysml
// Primary source module: AssetBackedSecurities.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Debt
// ===================================================================
package Asset_backed_Securities {
  doc/* Debt securities backed by a pool of assets, including loans of various kinds, credit card pools and home equity lines of credit, as well as esoteric assets. (source: dct:abstract, AssetBackedSecurities.rdf) */
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

// DebtPool

  item def <'auto asset-backed security'> AutoAssetBackedSecurity specializes Collateralized_Debt_Obligations::AssetBackedSecurity {
  	doc /* asset-backed security that is backed by an underlying pool of auto-related loans and/or leases */
  	// skos:definition xml:lang="en" (AssetBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:AutoAssetBackedSecurity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;AssetBackedSecurity"/>
  }
  item def <'auto debt pool'> AutoDebtPool specializes Securities_Pools::DebtPool {
  	doc /* debt pool of loans and/or leases that are motor vehicle-related */
  	// skos:definition xml:lang="en" (AssetBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:AutoDebtPool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def <'bond pool'> BondPool specializes Securities_Pools::DebtPool {
  	doc /* debt pool of consisting of bonds */
  	// skos:definition xml:lang="en" (AssetBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:BondPool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def <'consumer asset-backed security'> ConsumerAssetBackedSecurity specializes Collateralized_Debt_Obligations::AssetBackedSecurity, Structured_Instruments::StructuredFinanceInstrument {
  	doc /* structured finance securities collateralized by pools of auto loans and leases (auto ABS), credit card receivables (credit card ABS) or student loans (student loan ABS) */
  	// skos:definition xml:lang="en" (AssetBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:ConsumerAssetBackedSecurity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;AssetBackedSecurity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceInstrument"/>
  }
  item def <'controlled amortization asset-backed security'> ControlledAmortizationAssetBackedSecurity specializes Collateralized_Debt_Obligations::AssetBackedSecurity {
  	doc /* pool of credit card receivables associated with designated accounts */
  	// skos:definition xml:lang="en" (AssetBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:ControlledAmortizationAssetBackedSecurity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;AssetBackedSecurity"/>
  }
  item def <'credit card account pool'> CreditCardAccountPool specializes Securities_Pools::DebtPool {
  	doc /* pool of credit card receivables associated with designated accounts */
  	// skos:definition xml:lang="en" (AssetBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:CreditCardAccountPool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def <'credit card asset-backed security'> CreditCardAssetBackedSecurity specializes Collateralized_Debt_Obligations::AssetBackedSecurity {
  	doc /* asset-backed security based on credit card receivables */
  	// skos:definition xml:lang="en" (AssetBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:CreditCardAssetBackedSecurity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;AssetBackedSecurity"/>
  }
  item def <'fully amortizing asset-backed security'> FullyAmortizingAssetBackedSecurity specializes Collateralized_Debt_Obligations::AssetBackedSecurity {
  	doc /* debt pool consisting of home equity loans */
  	// skos:definition xml:lang="en" (AssetBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:FullyAmortizingAssetBackedSecurity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;AssetBackedSecurity"/>
  }
  item def <'home equity line of credit pool'> HomeEquityLineOfCreditPool specializes Securities_Pools::DebtPool {
  	doc /* An issue of securities backed by pools of mortgages held by government agencies. */
  	// skos:definition xml:lang="en" (AssetbackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetbackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:HomeEquityLineOfCreditPool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def <'home equity loan asset-backed security'> HomeEquityLoanAssetBackedSecurity specializes Collateralized_Debt_Obligations::AssetBackedSecurity {
  	doc /* asset-backed security based on home equity loan receivables */
  	// skos:definition xml:lang="en" (AssetBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:HomeEquityLoanAssetBackedSecurity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;AssetBackedSecurity"/>
  }
  item def <'student loan asset-backed security'> StudentLoanAssetBackedSecurity specializes Collateralized_Debt_Obligations::AssetBackedSecurity {
  	doc /* asset-backed security based on student loan receivables */
  	// skos:definition xml:lang="en" (AssetBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:StudentLoanAssetBackedSecurity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;AssetBackedSecurity"/>
  }
  item def <'student loan pool'> StudentLoanPool specializes Securities_Pools::DebtPool {
  	doc /* debt pool consisting of student loans */
  	// skos:definition xml:lang="en" (AssetBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (AssetBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-abs:StudentLoanPool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
}

// --- Bonds.rdf ---
