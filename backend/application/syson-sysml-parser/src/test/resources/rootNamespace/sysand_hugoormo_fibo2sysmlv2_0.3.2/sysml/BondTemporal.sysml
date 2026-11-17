// ===================================================================
// BondTemporal.sysml
// Source-parity extracted from: FiBO_MD_DebtTemporal.sysml
// Primary source modules: DebtAnalytics.rdf; Pools.rdf; MortgageBackedSecurities.rdf; PoolBackedSecurities.rdf; SecurityBasedDerivatives.rdf (for current translated content)
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Market Data MD/DebtTemporal
// ===================================================================
package Bond_Temporal {
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

  item def <'bond equivalent yield'> BondEquivalentYield specializes DebtInstrumentYield, RelativelyDefinedDebtInstrumentYield {
  	doc /* Yield determined on an equivalent basis to the yield of another bond. This is used to be able to realistically compare prices between debt instruments across different markets. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:BondEquivalentYield
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;RelativelyDefinedDebtInstrumentYield"/>
  }
  item def <'credit spread'> CreditSpread specializes Instrument_Pricing::YieldSpread {
  	doc /* yield spread that reflects the additional net yield an investor can earn from a security with more credit risk relative to one with less credit risk */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:CreditSpread
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;YieldSpread"/>
  }
  item def <'current yield calculation method'> CurrentYieldCalculationMethod specializes YieldCalculationMethod {
  	doc /* The ratio of the interest payment amount to the clean price. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:CurrentYieldCalculationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationMethod"/>
  }
  item def <'debt convexity analytic'> DebtConvexityAnalytic specializes Financial_Dates::DatedCollectionConstituent {
  	doc /* The second derivative of a security&apos;s price with respect to its yield, divided by the security&apos;s price. A security exhibits positive convexity when its price rises more for a downward move in its yield than its price declines for an equal upward move in its yield. Further notes: A measure of the change in price for a given change in Modified Duration. This always (necessarily) refers to Modified Duration. This is used as another risk measurement. Numerator is always (a) duration - either MacCaulays or Modified. Always rate of change of (one of the) Duration against some other parameter. The other paramater can be characterised as a Yield (it may be the Price, but that has a relationship to the Yield in any case). REVIEW: Inconsistency in the above - is it always necessarily Modified Duration that is referred to, or &quot;any&quot; Duration measure (Macaulays and.or Modified)? notes 9 Dec A measure of the sensitivity of the price with reference to interest rates. This is normally determined with reference to maturity, but since there are different maturity dates, this figure gives an estimate of the equitvalent if you had a homogenous portfolio, i.e. this is an estimate based on a pure equivalent, homogenous portfolio. Convexity of instrument versus portfolio. Sees instrument in terms of the set of cashflows. The term Convexity can be applied either to a bond or to a portfolio. More notes: When you get Convexit in MD, it will tell you what Duration it is refrfering to, along with Redemption Date (logically). Also if there is Option Adjusted Yield, there is a third set of analytics. What are they? i.e. OA Convexity, Duration Yield and the rest. Conclusions: Agreed to revisit this in OTC. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DebtConvexityAnalytic
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedCollectionConstituent"/>
  }
  item def <'debt instrument yield'> DebtInstrumentYield specializes Instrument_Pricing::Yield {
  	doc /* The return on the debt instrument at the stated price. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DebtInstrumentYield
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;Yield"/>
  }
  item def <'debt pool'> DebtPool specializes Securities_Pools::InstrumentPool {
  	doc /* pool consisting of debt instruments, such as bonds, loans or mortgages */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DebtPool
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;InstrumentPool"/>
  }
  item def <'debt price spread'> DebtPriceSpread specializes Indicators::MarketSpread {
  	doc /* The difference between the [what?] of a security and the fair price value of a different security which is used as a point of reference. The spread is used to determine the price of the instrument. (draft definition) */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DebtPriceSpread
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;MarketSpread"/>
  }
  item def <'debt securities market maker'> DebtSecuritiesMarketMaker specializes Functional_Entities::ServiceProvider {
  	doc /* An actor which has the role of Market Maker in a given market. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DebtSecuritiesMarketMaker
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;ServiceProvider"/>
  }
  item def <'debt yield to average life'> DebtYieldToAverageLife specializes DebtInstrumentYield {
  	doc /* The yield achieved by substituting a bond&apos;s average life for the issue&apos;s final maturity date. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DebtYieldToAverageLife
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DebtInstrumentYield"/>
  }
  item def <'debt yield to equivalent life'> DebtYieldToEquivalentLife specializes DebtInstrumentYield {
  	doc /* The yield achieved by substituting a bond&apos;s equivalent life for the issue&apos;s final maturity date. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DebtYieldToEquivalentLife
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DebtInstrumentYield"/>
  }
  item def <'debt yield to maturity'> DebtYieldToMaturity specializes DebtInstrumentYield {
  	doc /* The internal rate of return an investor would achieve if he or she purchased that bond at its current dirty price, and held it to maturity, assuming all coupon and principal payments are received as scheduled. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DebtYieldToMaturity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DebtInstrumentYield"/>
  }
  item def <'debt yield to next call'> DebtYieldToNextCall specializes DebtInstrumentYield {
  	doc /* The yield of a bond to the next possible call date. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DebtYieldToNextCall
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DebtInstrumentYield"/>
  }
  item def <'debt yield to worst'> DebtYieldToWorst specializes DebtInstrumentYield {
  	doc /* Yield to the worst case of when the instrument might be called. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DebtYieldToWorst
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DebtInstrumentYield"/>
  }
  item def <'discounted instrument yield'> DiscountedInstrumentYield specializes DebtInstrumentYield {
  	doc /* Yield quoted for a discount instrument. This is the ratio of the discount to the face value, divided by the period to maturity as a fraction of a year. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DiscountedInstrumentYield
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DebtInstrumentYield"/>
  }
  item def <'duration analytic'> DurationAnalytic specializes Pool_backed_Securities::DebtPoolStatisticalMeasure {
  	doc /* Weighted average time to receipt of all the payments. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:DurationAnalytic
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
  item def <'effective duration'> EffectiveDuration specializes DurationAnalytic {
  	doc /* Public Securities Association (PSA) speed used for the underlying collateral for cash-flow calculations in the &quot;down 300&quot; scenario. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:EffectiveDuration
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DurationAnalytic"/>
  }
  item def <'effective yield'> EffectiveYield specializes DebtInstrumentYield { // RDF: fibo-md-dbtx-bnd:EffectiveYield
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DebtInstrumentYield"/>
    // RDF: fibo-md-dbtx-bnd:EffectiveYield
  }
  item def <'equivalent life analytic'> EquivalentLifeAnalytic specializes LifeAnalytic {
  	doc /* Public Securities Association (PSA) speed used for the underlying collateral for cash-flow calculations in the "down 300" scenario. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:EquivalentLifeAnalytic
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;LifeAnalytic"/>
  }
  item def <'equivalent yield calculation method'> EquivalentYieldCalculationMethod specializes YieldCalculationMethod, RelativeYieldCalculationMethod {
  	doc /* Public Securities Association (PSA) speed used for the underlying collateral for cash-flow calculations in the "down 300" scenario. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:EquivalentYieldCalculationMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;RelativeYieldCalculationMethod"/>
  }
  item def <'exchange traded bond price'> ExchangeTradedBondPrice specializes Instrument_Pricing::SecurityPrice {
  	doc /* Public Securities Association (PSA) speed used for the underlying collateral for cash-flow calculations in the "down 300" scenario. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:ExchangeTradedBondPrice
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
  }
  item def <'f f i e c down 300 prepay speed'> FFIECDown300PrepaySpeed specializes PrepaymentSpeed {
  	doc /* Public Securities Association (PSA) speed used for the underlying collateral for cash-flow calculations in the "down 300" scenario. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:FFIECDown300PrepaySpeed
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;PrepaymentSpeed"/>
  }
  item def <'f f i e c up 300 prepay speed'> FFIECUp300PrepaySpeed specializes PrepaymentSpeed {
  	doc /* Public Securities Association (PSA) speed used for the underlying collateral for cash-flow calculations in the &quot;up 300&quot; scenario. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:FFIECUp300PrepaySpeed
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;PrepaymentSpeed"/>
  }
  item def <'i c m a yield formula'> ICMAYieldFormula specializes YieldCalculationFormula {
  	doc /* The calculation method specified by ICMA (formerly ISMA) for determination of yield for fixed-rate bonds. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:ICMAYieldFormula
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationFormula"/>
  }
  item def <'implied forward rate'> ImpliedForwardRate specializes Currency_Amount::InterestRate {
  	doc /* The spread determined internally within the organisation from information available at their own trading desks. Further Notes Internal prices within a bank would be determined by surveying their own traders. So e.g. corporate desk trades these 30 bonds, get the daily spreads on those at the end of the day and calculate the price. The traders determine the pricing during the based on market movements. (this is all for OTC traded bonds, not exchange traded bonds). */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:ImpliedForwardRate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;InterestRate"/>
  }
  item def <'internally determined price spread'> InternallyDeterminedPriceSpread specializes DebtPriceSpread {
  	doc /* The spread determined internally within the organisation from information available at their own trading desks. Further Notes Internal prices within a bank would be determined by surveying their own traders. So e.g. corporate desk trades these 30 bonds, get the daily spreads on those at the end of the day and calculate the price. The traders determine the pricing during the based on market movements. (this is all for OTC traded bonds, not exchange traded bonds). */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:InternallyDeterminedPriceSpread
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DebtPriceSpread"/>
  }
  item def <'japanese compound yield calculation method'> JapaneseCompoundYieldCalculationMethod specializes YieldCalculationMethod {
  	doc /* No definition in selection list. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:JapaneseCompoundYieldCalculationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationMethod"/>
  }
  item def <'japanese simple yield calculation method'> JapaneseSimpleYieldCalculationMethod specializes YieldCalculationMethod {
  	doc /* No definition.Term put here from memory. 02 Dec changed from Japanese Yield to Japanese Simple Yield. note hat Japanese Compound yield also here (from FIBIM or anothe rlist, added 25 nov with the rest). */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:JapaneseSimpleYieldCalculationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationMethod"/>
  }
  item def <'key rate duration'> KeyRateDuration specializes DurationAnalytic {
  	doc /* Some measure of the life of a security, other than the actual time to maturity itself. This is a derived figure, based on certain parameters as appropriate to that type of instrument, to give a figure that is equivalent to and similar to the basic maturity of the instrument, for the purposes of analysing that security. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:KeyRateDuration
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DurationAnalytic"/>
  }
  item def <'life analytic'> LifeAnalytic specializes Pool_backed_Securities::DebtPoolStatisticalMeasure {
  	doc /* The return on the debt instrument at the stated price. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:LifeAnalytic
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
  item def <'loan pool prepayment model'> LoanPoolPrepaymentModel specializes Analytics::Formula {
  	doc /* Model of the prepayments of loans in a pool of individual loans, such as a mortgage pool or loan pool. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:LoanPoolPrepaymentModel
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Formula"/>
  }
  item def <'loan prepayment formula'> LoanPrepaymentFormula specializes Analytics::Formula {
  	doc /* The formula which embodies the model for loan pool prepayment speed. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:LoanPrepaymentFormula
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Formula"/>
  }
  item def <'MacCaulays Duration Analytic'> MacCaulaysDurationAnalytic specializes DurationAnalytic {
  	doc /* Prepayment speed that results in the same average life as that computed for the Collateralized Mortgage Obligation (CMO), Asset Backed Securities (ABS) or Mortgage Backed Securities (MBS) using the Maturity Prepay Model. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:MacCaulaysDurationAnalytic
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DurationAnalytic"/>
  }
  item def <'maturity equivalent p s a'> MaturityEquivalentPSA specializes PrepaymentSpeed {
  	doc /* Prepayment speed that results in the same average life as that computed for the Collateralized Mortgage Obligation (CMO), Asset Backed Securities (ABS) or Mortgage Backed Securities (MBS) using the Maturity Prepay Model. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:MaturityEquivalentPSA
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;PrepaymentSpeed"/>
  }
  item def <'modified duration analytic'> ModifiedDurationAnalytic specializes DurationAnalytic {
  	doc /* The percentage price change of a security for a given change in yield. The higher the modified duration of a security, the higher its risk. Ad/ModDuration = [duration / {1 + (IRR/M)}]; where IRR is the internal rate of return and M is the number of compounding periods per year. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:ModifiedDurationAnalytic
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DurationAnalytic"/>
  }
  item def <'mortgage-backed security'> MortgageBackedSecurity specializes Pool_backed_Securities::PoolBackedSecurity {
  	doc /* debt obligations that represent claims to the cash flows from pools of mortgage loans, most commonly on residential property */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:MortgageBackedSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;PoolBackedSecurity"/>
  }
  item def <'native yield'> NativeYield specializes DebtInstrumentYield, RelativelyDefinedDebtInstrumentYield {
  	doc /* The yield of the security as determined using the Yield Calculation Method that is the default for the market that the security is traded in. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:NativeYield
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;RelativelyDefinedDebtInstrumentYield"/>
  }
  item def <'native yield calculation method'> NativeYieldCalculationMethod specializes YieldCalculationMethod, RelativeYieldCalculationMethod {
  	doc /* The convention used in the marketplace for that security. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:NativeYieldCalculationMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;RelativeYieldCalculationMethod"/>
  }
  item def <'next call'> NextCall specializes Debt_Instruments::CallEvent {
  	doc /* The next call of the issue, as at the current time. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:NextCall
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;CallEvent"/>
  }
  item def <'next call date'> NextCallDate specializes NextCall, FiBO_FND_ExternalStubs::Date {
  	doc /* The next date on which the issue can be called, from the present date. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:NextCallDate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;Date"/>
  }
  item def <'next put'> NextPut specializes Debt_Instruments::PutEvent {
  	doc /* The next available put date for the issue, as at the current time. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:NextPut
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;PutEvent"/>
  }
  item def <'option adjusted yield'> OptionAdjustedYield specializes DebtInstrumentYield {
  	doc /* NB specified as a spread. synonym: OAS Based on different Int Rate paths. There are different OAS models just like there are different Yield methods. Also would make reference to the Yield Curve - but these are parameters that go into that model. Limit this model at the point where it distinguishes the difference between things - we are not in a position to mathematically model the things themselves, just capture the basic facts. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:OptionAdjustedYield
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DebtInstrumentYield"/>
  }
  item def <'o t c bond market price'> OTCBondMarketPrice specializes Instrument_Pricing::SecurityPrice {
  	doc /* The price determined for the marketplace for a bond which is traded over the counter. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:OTCBondMarketPrice
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
  }
  item def <'partial calls estimation model'> PartialCallsEstimationModel specializes Analytics::Formula {
  	doc /* A model of how the early partial calls are estimated. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:PartialCallsEstimationModel
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Formula"/>
  }
  item def <'pool factor'> PoolFactor specializes FiBO_FND_ExternalStubs::Percentage {
  	doc /* How much of the original pool is still outstanding. This is a number below one. Expressed as percentage. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:PoolFactor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Percentage"/>
  }
  item def <'pool paydown rate'> PoolPaydownRate specializes Currency_Amount::Ratio {
  	doc /* The rate at which the pool is paying down. This is based on observed factor. CPR, SMM, etc. etc. Measured differently for different kinds of security. CBO might have a prepayment rate for example if the underlying bond is callable. with a non agency mortgge dela, defualts will effect this. so for instance there is principal is no lnger inthe pool because the mortgagee defaults. With agency these are not taken out in the case of default but for non agency these mortgages are removed from the pool if and when a mortgagee defualts. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:PoolPaydownRate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Ratio"/>
  }
  item def <'prepayment speed'> PrepaymentSpeed specializes Pool_backed_Securities::DebtPoolStatisticalMeasure {
  	doc /* estimated rate at which a debt or part of a debt is paid off ahead of schedule */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:PrepaymentSpeed
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
  item def <'price value of basis point'> PriceValueOfBasisPoint specializes Pool_backed_Securities::DebtPoolStatisticalMeasure {
  	doc /* Sensitivity of the price for one basis point change in yield, defined as the difference in price given 1 bp change in yield. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:PriceValueOfBasisPoint
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;DebtPoolStatisticalMeasure"/>
  }
  item def <'relatively defined debt instrument yield'> RelativelyDefinedDebtInstrumentYield specializes DebtInstrumentYield {
  	doc /* The method used in determining Yield in the Russian markets. This is based on an effective yield with fundamentally different math. To give an example of the use of a different "yield type", we have Russia, which trades based on an effective yield. The price-yield math is fundamentally different. Notes Origin:Fidessa Uses a trade space and effective yield formula. MAy have same day types but different math. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:RelativelyDefinedDebtInstrumentYield
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DebtInstrumentYield"/>
  }
  item def <'relative yield calculation method'> RelativeYieldCalculationMethod specializes YieldCalculationMethod {
  	doc /* The method used in determining Yield in the Russian markets. This is based on an effective yield with fundamentally different math. To give an example of the use of a different &quot;yield type&quot;, we have Russia, which trades based on an effective yield. The price-yield math is fundamentally different. Notes Origin:Fidessa Uses a trade space and effective yield formula. MAy have same day types but different math. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:RelativeYieldCalculationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationMethod"/>
  }
  item def <'russian yield calculation method'> RussianYieldCalculationMethod specializes YieldCalculationMethod {
  	doc /* The method used in determining Yield in the Russian markets. This is based on an effective yield with fundamentally different math. To give an example of the use of a different "yield type", we have Russia, which trades based on an effective yield. The price-yield math is fundamentally different. Notes Origin:Fidessa Uses a trade space and effective yield formula. MAy have same day types but different math. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:RussianYieldCalculationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationMethod"/>
  }
  item def <'russian yield formula'> RussianYieldFormula specializes YieldCalculationFormula {
  	doc /* This is based on a different Effective Yield than on another market. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:RussianYieldFormula
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationFormula"/>
  }
  item def <'simple yield calculation method'> SimpleYieldCalculationMethod specializes YieldCalculationMethod {
  	doc /* The annual rate of return expressed as a percentage. This is the return divided by the outlay and multiplied by 100 to express the figure as a percentage. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:SimpleYieldCalculationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationMethod"/>
  }
  item def <'spanish yield calculation method'> SpanishYieldCalculationMethod specializes YieldCalculationMethod {
  	doc /* The method used in determining annual yield in Spanish corporate bonds. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:SpanishYieldCalculationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationMethod"/>
  }
  item def <'tradable debt instrument'> TradableDebtInstrument specializes Financial_Instruments::DebtInstrument, Financial_Instruments::Security {
  	doc /* The Average Life analytic at the time the security was issued. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:TradableDebtInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DebtInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Security"/>
  }
  item def <'u s corporate bond yield calculation method'> USCorporateBondYieldCalculationMethod specializes YieldCalculationMethod {
  	doc /* This has 30/360 and semi-annual compounding. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:USCorporateBondYieldCalculationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationMethod"/>
  }
  item def <'u s treasury yield calculation method'> USTreasuryYieldCalculationMethod specializes YieldCalculationMethod {
  	doc /* No definition.Term put here from memory. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:USTreasuryYieldCalculationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationMethod"/>
  }
  item def <'wall street yield calculation method'> WallStreetYieldCalculationMethod specializes YieldCalculationMethod {
  	doc /* No definition.Term put here from memory. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:WallStreetYieldCalculationMethod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;YieldCalculationMethod"/>
  }
  item def <'average life at issue'> WeightedAverageLifeAtIssue specializes LifeAnalytic, Pool_backed_Securities::WeightedAverageLife {
  	doc /* The Average Life analytic at the time the security was issued. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:WeightedAverageLifeAtIssue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;WeightedAverageLife"/>
  }
  item def <'weighted average time to receipt of cashflows'> WeightedAverageTimeToReceiptOfCashflows specializes LifeAnalytic, Analytics::Formula {
  	doc /* The weighted average time to the receipt of cashflows for an instrument. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:WeightedAverageTimeToReceiptOfCashflows
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Formula"/>
  }
  item def <'worst call'> WorstCall specializes NextCall, Debt_Instruments::CallEvent {
  	doc /* call event representing the worst case with respect to when the instrument might be called */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:WorstCall
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;CallEvent"/>
  }
  item def <'yield calculation formula'> YieldCalculationFormula specializes Analytics::Formula {
  	doc /* The formula used in determining the Yield. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:YieldCalculationFormula
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Formula"/>
  }
  item def <'yield calculation method'> YieldCalculationMethod specializes Analytics::Formula {
  	doc /* The method by which the yield is calculated. This includes a formula for calculation and a specific day count convention and compounding. You would apply this calculation method on top of the underlying terms and conditions, do for example the holiday calenders and so on, are used in these formulae. For final cash flow: Japanese yield will round down accrued interest. Add: The actual underlying math. Wall Street uses the same ICMA formula. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:YieldCalculationMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Formula"/>
  }
  item def <'yield to next put'> YieldToNextPut specializes DebtInstrumentYield {
  	doc /* The number of decimal places used in the publication of the factor value. */
  	// skos:definition xml:lang="en" (BondTemporal.rdf)
  	// rdfs:label xml:lang="en" (BondTemporal.rdf)
  	// RDF: fibo-md-dbtx-bnd:YieldToNextPut
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-dbtx-aly;DebtInstrumentYield"/>
  }
}
