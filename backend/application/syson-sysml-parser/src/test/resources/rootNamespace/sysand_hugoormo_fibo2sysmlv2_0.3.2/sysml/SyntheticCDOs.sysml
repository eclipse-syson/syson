// ===================================================================
// SyntheticCDOs.sysml
// Source-parity extracted from: FiBO_SEC_Debt.sysml
// Primary source module: SyntheticCDOs.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Debt
// ===================================================================
package SyntheticCDOs {
  doc/* Synthetic collateralized debt obligations are instruments designed to provide the same kind of structure and returns as a CDO, but these are not backed by an actual pool of debt assets. (source: dct:abstract, SyntheticCDOs.rdf) */
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

// CollateralizedDebtObligation, CDOPortfolio
// Tranche
// DebtPool

  item def <'synthetic c d o'> SyntheticCDO specializes Collateralized_Debt_Obligations::CDODeal {
  	doc /* An instrument which is defined as a constituent of a synthetic pool of instruments. These are not holdings because they are not held, but in all other respects they are the constituents of the portfolio. */
  	// skos:definition xml:lang="en" (SyntheticCDOs.rdf)
  	// rdfs:label xml:lang="en" (SyntheticCDOs.rdf)
  	// RDF: fibo-sec-dbt-syn:SyntheticCDO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDODeal"/>
  }
  item def <'arbitrage synthetic c d o'> ArbitrageSyntheticCDO specializes SyntheticCDO, Collateralized_Debt_Obligations::ArbitrageCDO {
  	doc /* Arbitrage synthetic CDO deals are motivated by regulatory or practical considerations that might make a bank want to retain ownership of debt while achieving capital relief through CDSs. In this case, the sponsoring bank has a portfolio of obligations, called the reference portfolio. It retains that portfolio, but offloads its credit risk by transacting CDSs with the CDO. */
  	// skos:definition xml:lang="en" (SyntheticCDOs.rdf)
  	// rdfs:label xml:lang="en" (SyntheticCDOs.rdf)
  	// RDF: fibo-sec-dbt-syn:ArbitrageSyntheticCDO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;ArbitrageCDO"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-syn;SyntheticCDO"/>
  }
  item def <'synthetic balance sheet c d o'> SyntheticBalanceSheetCDO specializes SyntheticCDO, Collateralized_Debt_Obligations::BalanceSheetCDO {
  	doc /* An instrument which is defined as a constituent of a synthetic pool of instruments. These are not holdings because they are not held, but in all other respects they are the constituents of the portfolio. */
  	// skos:definition xml:lang="en" (SyntheticCDOs.rdf)
  	// rdfs:label xml:lang="en" (SyntheticCDOs.rdf)
  	// RDF: fibo-sec-dbt-syn:SyntheticBalanceSheetCDO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;BalanceSheetCDO"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-syn;SyntheticCDO"/>
  }
  item def <'synthetic amortizing security'> SyntheticAmortizingSecurity specializes SyntheticStructuredFinanceInstrument {
  	doc /* Security constructed to emulate an amortizing security. */
  	// skos:definition xml:lang="en" (SyntheticCDOs.rdf)
  	// rdfs:label xml:lang="en" (SyntheticCDOs.rdf)
  	// RDF: fibo-sec-dbt-syn:SyntheticAmortizingSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-syn;SyntheticStructuredFinanceInstrument"/>
  }
  item def <'synthetic c d o portfolio'> SyntheticCDOPortfolio{ // RDF: fibo-sec-dbt-syn:SyntheticCDOPortfolio
    // RDF: fibo-sec-dbt-syn:SyntheticCDOPortfolio
  }
  item def <'synthetic c d o portfolio constituent'> SyntheticCDOPortfolioConstituent { // RDF: fibo-sec-dbt-syn:SyntheticCDOPortfolioConstituent
    // RDF: fibo-sec-dbt-syn:SyntheticCDOPortfolioConstituent
  }
  item def <'synthetic c d o tranche'> SyntheticCDOTranche specializes Collateralized_Debt_Obligations::CollateralizedDebtObligation {
  	doc /* A cash flow structure which synthesizes the behavior of a portfolio of debt securities. For example a synthesized portfolio of CDO / Bonds / ABS using Total Returns Swaps and CDS. This does exist, it is just manufactured from different instruments. */
  	// skos:definition xml:lang="en" (SyntheticCDOs.rdf)
  	// rdfs:label xml:lang="en" (SyntheticCDOs.rdf)
  	// RDF: fibo-sec-dbt-syn:SyntheticCDOTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CollateralizedDebtObligation"/>
  }
  item def <'synthetic debt instrument pool'> SyntheticDebtInstrumentPool specializes Securities_Pools::DebtPool {
  	doc /* A cash flow structure which synthesizes the behavior of a portfolio of debt securities. For example a synthesized portfolio of CDO / Bonds / ABS using Total Returns Swaps and CDS. This does exist, it is just manufactured from different instruments. */
  	// skos:definition xml:lang="en" (SyntheticCDOs.rdf)
  	// rdfs:label xml:lang="en" (SyntheticCDOs.rdf)
  	// RDF: fibo-sec-dbt-syn:SyntheticDebtInstrumentPool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def <'synthetic debt instrument pool funding asset'> SyntheticDebtInstrumentPoolFundingAsset { // RDF: fibo-sec-dbt-syn:SyntheticDebtInstrumentPoolFundingAsset
    // RDF: fibo-sec-dbt-syn:SyntheticDebtInstrumentPoolFundingAsset
  }
  item def <'synthetic debt s p v'> SyntheticDebtSPV specializes Legal_Persons::SpecialPurposeVehicle {
  	doc /* A Special Purpose Vehicle set up for the issuance of synthetics CDOs. This entity (like all SPVs) its itself registered as some kind of legal entity, distinct from the sponsoring organization. It becomes the Issuer of Synthetic CDO issues. */
  	// skos:definition xml:lang="en" (SyntheticCDOs.rdf)
  	// rdfs:label xml:lang="en" (SyntheticCDOs.rdf)
  	// RDF: fibo-sec-dbt-syn:SyntheticDebtSPV
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-lp;SpecialPurposeVehicle"/>
  }
  item def <'synthetic pool asset'> SyntheticPoolAsset specializes Accounting_Equity::FinancialAsset {
  	doc /* The underlying CDS which is created to mechanise the cash flows in the synthetic portfolio. */
  	// skos:definition xml:lang="en" (SyntheticCDOs.rdf)
  	// rdfs:label xml:lang="en" (SyntheticCDOs.rdf)
  	// RDF: fibo-sec-dbt-syn:SyntheticPoolAsset
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;FinancialAsset"/>
  }
  item def <'synthetic structured finance instrument'> SyntheticStructuredFinanceInstrument specializes Structured_Instruments::StructuredFinanceInstrument {
  	doc /* The underlying CDS which is created to mechanise the cash flows in the synthetic portfolio. */
  	// skos:definition xml:lang="en" (SyntheticCDOs.rdf)
  	// rdfs:label xml:lang="en" (SyntheticCDOs.rdf)
  	// RDF: fibo-sec-dbt-syn:SyntheticStructuredFinanceInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceInstrument"/>
  }
}

// --- TradedShortTermDebt.rdf ---
