// ===================================================================
// PoolbackedSecurities.sysml
// Source-parity extracted from: FiBO_SEC_Debt.sysml
// Primary source module: PoolBackedSecurities.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Debt
// ===================================================================
package Pool_backed_Securities {
  doc/* This ontology defines concepts that are common to asset-backed and mortgage-backed securities, including pools, as well as structured finance instruments. (source: dct:abstract, PoolBackedSecurities.rdf) */
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

// DebtInstrument

  item def <'structured finance instrument'> StructuredFinanceInstrument specializes Structured_Instruments::StructuredProduct, PoolBackedSecurity {
  	doc /* pool-backed security wherein the risk associated with the pool has been uncoupled from the risk associated with the originating institution through a special purpose vehicle */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:StructuredFinanceInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;StructuredProduct"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;PoolBackedSecurity"/>
  }
  item def <'tranche'> Tranche specializes Structured_Instruments::StructuredFinanceInstrument {
  	doc /* segment of a pool of securities, typically debt instruments */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:Tranche
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceInstrument"/>
  }
  item def <'pool-backed security'> PoolBackedSecurity specializes Debt_Instruments::TradableDebtInstrument {
  	doc /* debt instrument that derives its cashflow from an underlying pool of mortgage loans or other receivables */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:PoolBackedSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;TradableDebtInstrument"/>
  }
  item def <'principal protected note'> PrincipalProtectedNote specializes Structured_Instruments::StructuredFinanceInstrument, Structured_Instruments::CapitalProtectedProduct {
  	doc /* structured finance that offers investors exposure to chosen underlying assets using various approaches and asymmetric pay-off profiles */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:PrincipalProtectedNote
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;CapitalProtectedProduct"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceInstrument"/>
  }
  item def <'structured finance without principal protection'> StructuredFinanceWithoutPrincipalProtection specializes Structured_Instruments::StructuredFinanceInstrument {
  	doc /* structured finance that is a short-term note linked to an underlying asset that offers a steady stream of income */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:StructuredFinanceWithoutPrincipalProtection
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceInstrument"/>
  }
  item def <'attachment point'> AttachmentPoint specializes Currency_Amount::PercentageMonetaryAmount {
  	doc /* lower tranche boundary of a tranche defined as a percentage of the value of the total pool of collateral, either at issuance or as of some point in time */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:AttachmentPoint
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;PercentageMonetaryAmount"/>
  }
  item def <'detachment point'> DetachmentPoint specializes Currency_Amount::PercentageMonetaryAmount {
  	doc /* upper tranche boundary of a tranche defined as a percentage of the value of the total pool of collateral, either at issuance or as of some point in time */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:DetachmentPoint
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;PercentageMonetaryAmount"/>
  }
  item def <'debt pool statistical measure'> DebtPoolStatisticalMeasure specializes Analytics::QualifiedMeasure {
  	doc /* qualified measure of some aspect of the behavior of one or more debt instrument(s) that may vary over time */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:DebtPoolStatisticalMeasure
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;QualifiedMeasure"/>
  }
  item def <'absolute prepayment rate'> AbsolutePrepaymentRate specializes DebtPoolStatisticalMeasure {
  	doc /* measure of the monthly rate of loan prepayments as a percentage of the original pool balance */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:AbsolutePrepaymentRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
  item def <'default rate'> DefaultRate specializes DebtPoolStatisticalMeasure, Currency_Amount::Ratio {
  	doc /* qualified measure of the rate at which holders of the debt instruments in the pool default on those instruments */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:DefaultRate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Ratio"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
  item def <'prepayment speed'> PrepaymentSpeed specializes DebtPoolStatisticalMeasure {
  	doc /* estimated rate at which a debt or part of a debt is paid off ahead of schedule */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:PrepaymentSpeed
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
  item def <'single monthly mortality'> SingleMonthlyMortality specializes DebtPoolStatisticalMeasure, FiBO_FND_ExternalStubs::Percentage {
  	doc /* estimated per-month percentage of mortgages in an MBS pool that will be paid off early */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:SingleMonthlyMortality
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Percentage"/>
  }
  item def <'weighted average coupon'> WeightedAverageCoupon specializes DebtPoolStatisticalMeasure, Analytics::ArithmeticMean {
  	doc /* weighted-average gross interest rates of the pool of mortgages that underlie a mortgage-backed security (MBS) weighed by their balances at the time the securities were issued */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:WeightedAverageCoupon
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;ArithmeticMean"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
  item def <'weighted average life'> WeightedAverageLife specializes DebtPoolStatisticalMeasure, Analytics::ArithmeticMean {
  	doc /* weighted average of the times of the principal repayments Average life is calculated using the weighted average time to the receipt of all future cash flows. */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:WeightedAverageLife
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;ArithmeticMean"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
  item def <'weighted average loan age'> WeightedAverageLoanAge specializes DebtPoolStatisticalMeasure, Analytics::ArithmeticMean {
  	doc /* dollar-weighted average measuring the age of the individual loans in a mortgage pass-through or pooled security */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:WeightedAverageLoanAge
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;ArithmeticMean"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
  item def <'weighted average maturity'> WeightedAverageMaturity specializes DebtPoolStatisticalMeasure, Analytics::ArithmeticMean {
  	doc /* weighted average amount of time until the maturities on mortgages in a mortgage-backed security (MBS) */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:WeightedAverageMaturity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;ArithmeticMean"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
  item def <'weighted average remaining term'> WeightedAverageRemainingTerm specializes DebtPoolStatisticalMeasure, Analytics::ArithmeticMean {
  	doc /* weighted average time to maturity of a portfolio of asset-backed securities (ABS) or mortgage-backed (MBS) securities */
  	// skos:definition xml:lang="en" (PoolBackedSecurities.rdf)
  	// rdfs:label xml:lang="en" (PoolBackedSecurities.rdf)
  	// RDF: fibo-sec-dbt-pbs:WeightedAverageRemainingTerm
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;ArithmeticMean"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
}

// --- SyntheticCDOs.rdf ---
