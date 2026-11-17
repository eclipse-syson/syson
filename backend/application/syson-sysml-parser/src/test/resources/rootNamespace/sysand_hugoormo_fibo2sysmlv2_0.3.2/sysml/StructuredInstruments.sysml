// ===================================================================
// StructuredInstruments.sysml
// Source-parity extracted from: FiBO_DER_DerivativesContracts.sysml
// Primary source module: StructuredInstruments.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/DerivativesContracts
// ===================================================================
package Structured_Instruments {
  doc/* This ontology defines concepts common to pre-packaged structured finance investment strategies based on a host of underlying instruments, pools, or other assets. (source: dct:abstract, StructuredInstruments.rdf) */
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

  item def <'barrier capital protection certificate'> BarrierCapitalProtectionCertificate specializes CapitalProtectedProduct, ParticipationCertificate {
    	doc /* capital-protected product whose protection is defined as a percentage of the nominal price (e.g. 100 percent), with minimum redemption at expiry equivalent to the capital protection */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:BarrierCapitalProtectionCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;CapitalProtectedProduct"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
    }

  item def <'barrier discount certificate'> BarrierDiscountCertificate specializes ParticipationCertificate, Pool_backed_Securities::StructuredFinanceWithoutPrincipalProtection {
    	doc /* participation certificate that enables the investor to acquire the underlying asset at a lower price in return for a limited payout, and for which the maximum redemption amount (Cap) is paid out if the barrier is never breached */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:BarrierDiscountCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceWithoutPrincipalProtection"/>
    }

  item def <'barrier reverse convertible certificate'> BarrierReverseConvertibleCertificate specializes ParticipationCertificate, Pool_backed_Securities::StructuredFinanceWithoutPrincipalProtection {
    	doc /* participation certificate whose payout is conditional: should the barrier never be breached, the nominal price plus coupon is paid at redemption; if the barrier is breached the product changes into a reverse convertible */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:BarrierReverseConvertibleCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceWithoutPrincipalProtection"/>
    }

  item def <'basket certificate'> BasketCertificate specializes ParticipationCertificate {
    	doc /* certificate whose underlying asset represents a fraction of a basket of securities that corresponds to the subscription ratio */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:BasketCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
    }

  item def <'bonus certificate'> BonusCertificate specializes ParticipationCertificate {
    	doc /* certificate whose minimum redemption is equal to the nominal value provided the barrier has not been breached, with a greater risk in relation to multiple underlying asset(s) (worst-of), allowing for a higher bonus level or lower barrier, and with a reduced risk compared to a direct investment into the underlying asset(s) */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:BonusCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
    }

  item def <'capital protection certificate with coupons'> CapitalProtectionCertificateWithCoupons specializes CapitalProtectedProduct, ParticipationCertificate {
    	doc /* capital-protected product whose protection is defined as a percentage of the nominal price (e.g. 100 percent), with minimum redemption at expiry equivalent to the capital protection */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:CapitalProtectionCertificateWithCoupons
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;CapitalProtectedProduct"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
    }

  item def <'capital protection certificate with participation'> CapitalProtectionCertificateWithParticipation specializes CapitalProtectedProduct, ParticipationCertificate {
    	doc /* capital-protected product whose protection is defined as a percentage of the nominal amount (e.g. 100 percent), with minimum redemption at expiry equivalent to the capital protection */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:CapitalProtectionCertificateWithParticipation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;CapitalProtectedProduct"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
    }

  item def <'capital protection convertible certificate'> CapitalProtectionConvertibleCertificate specializes CapitalProtectedProduct, ParticipationCertificate {
    	doc /* capital-protected product whose protection is defined as a percentage of the nominal price (e.g. 100 percent), with minimum redemption at expiry equivalent to the capital protection */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:CapitalProtectionConvertibleCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;CapitalProtectedProduct"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
    }

  item def <'capital-protected product'> CapitalProtectedProduct specializes StructuredProduct {
    	doc /* structured product that participates in the performance of a financial asset, such as a stock, fund or index, while protecting the investor&apos;s capital */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:CapitalProtectedProduct
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;StructuredProduct"/>
    }

  item def <'discount certificate'> DiscountCertificate specializes ParticipationCertificate, Pool_backed_Securities::StructuredFinanceWithoutPrincipalProtection {
    	doc /* participation certificate that enables the investor to acquire the underlying asset at a lower price in return for a limited payout, and for which the underlying asset(s) and/or a cash amount is redeemed should the underlying asset close below the strike on expiry, for which, in return, the potential profit is capped */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:DiscountCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceWithoutPrincipalProtection"/>
    }

  item def <'express certificate'> ExpressCertificate specializes ParticipationCertificate, Pool_backed_Securities::StructuredFinanceWithoutPrincipalProtection {
    	doc /* participation certificate whose payout is conditional: should the underlying trade above the strike on the observation date, an early redemption consisting of nominal price plus an additional coupon amount is paid */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:ExpressCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceWithoutPrincipalProtection"/>
    }

  item def <'index certificate'> IndexCertificate specializes ParticipationCertificate {
    	doc /* certificate whose underlying asset is an index */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:IndexCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
    }

  item def <'leveraged product'> LeveragedProduct specializes StructuredProduct {
    	doc /* structured product that provides a further enhanced yield (over and above that of a yield-enhancement product), often without any limit to the upside participation, and frequently with a stop-loss in order to limit potential capital losses */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:LeveragedProduct
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;StructuredProduct"/>
    }

  item def <'outperformance bonus certificate'> OutperformanceBonusCertificate specializes BonusCertificate, OutperformanceCertificate {
    	doc /* certificate that allows disproportionate participation (outperformance) in positive performance above the strike, in which the minimum redemption is equal to the nominal value provided the barrier has not been breached, with greater risk multiple underlying asset(s) (worst-of) allow for a higher bonus level or lower barrier, and reduced risk compared to a direct investment into the underlying asset(s) */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:OutperformanceBonusCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;BonusCertificate"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;OutperformanceCertificate"/>
    }

  item def <'outperformance certificate'> OutperformanceCertificate specializes ParticipationCertificate {
    	doc /* certificate that allows disproportionate participation (outperformance) in positive performance above the strike, reflecting underlying price moves 1:1 (adjusted by the conversion ratio and any related fees), and whose risk is comparable to direct investment in the underlying asset(s) */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:OutperformanceCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
    }

  item def <'participation certificate'> ParticipationCertificate specializes ParticipationInstrument, StructuredFinanceInstrument {
    	doc /* participation product that provides the possibility to participate in the gains or losses in the price of an asset, subject to counterparty risk */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:ParticipationCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceInstrument"/>
    }

  item def <'participation instrument'> ParticipationInstrument specializes StructuredProduct, Security_Based_Derivatives::SecurityBasedDerivative {
    	doc /* structured product that provides the possibility to participate in the performance of an asset or basket of assets */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:ParticipationInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;StructuredProduct"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;SecurityBasedDerivative"/>
    }

  item def <'principal protected note'> PrincipalProtectedNote specializes CapitalProtectedProduct, StructuredFinanceInstrument {
    	doc /* structured finance that offers investors exposure to chosen underlying assets using various approaches and asymmetric pay-off profiles */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:PrincipalProtectedNote
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;CapitalProtectedProduct"/>
    }

  item def <'reverse convertible certificate'> ReverseConvertibleCertificate specializes ParticipationCertificate, Pool_backed_Securities::StructuredFinanceWithoutPrincipalProtection {
    	doc /* participation certificate whose payout is conditional: should the underlying asset close below the strike on expiry, the underlying asset(s) and/ or a cash amount is redeemed; should the underlying asset close above the strike at expiry, the nominal amount plus the coupon is paid at redemption */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:ReverseConvertibleCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceWithoutPrincipalProtection"/>
    }

  item def <'structured finance instrument'> StructuredFinanceInstrument specializes StructuredProduct, Pool_backed_Securities::PoolBackedSecurity {
    	doc /* pool-backed security wherein the risk associated with the pool has been uncoupled from the risk associated with the originating institution through a special purpose vehicle */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:StructuredFinanceInstrument
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;PoolBackedSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;StructuredProduct"/>
    }

  item def <'structured product'> StructuredProduct specializes Derivatives_Basics::DerivativeInstrument {
    	doc /* security whose cash flow characteristics depend upon one or more indices or that have embedded forwards or options or securities where an investor&apos;s investment return and the issuer&apos;s payment obligations are contingent on, or highly sensitive to, changes in the value of underlying assets, indices, interest rates or cash flows */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:StructuredProduct
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

  item def <'tracker certificate'> TrackerCertificate specializes ParticipationCertificate {
    	doc /* certificate that reflects underlying price moves 1:1 (adjusted by conversion ratio and any related fees), in which the associated risk is comparable to direct investment in the underlying asset(s) */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:TrackerCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
    }

  item def <'turbo certificate'> TurboCertificate specializes ParticipationCertificate {
    	doc /* certificate that acts as a leveraged security, whose price tracks an underlying financial asset&apos;s price one for one, and that can be used to go long or short */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:TurboCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
    }

  item def <'twin-win certificate'> TwinWinCertificate specializes ParticipationCertificate {
    	doc /* certificate that makes profits possible with rising and falling underlying asset values, in which a falling underlying asset price converts into profit up to the barrier, and whose minimum redemption is equal to the nominal value provided the barrier has not been breached */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:TwinWinCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;ParticipationCertificate"/>
    }

  item def <'yield-enhancement product'> YieldEnhancementProduct specializes StructuredProduct {
    	doc /* structured product that provides a predefined yield in return for a downside risk */
    	// skos:definition xml:lang="en" (StructuredInstruments.rdf)
    	// rdfs:label xml:lang="en" (StructuredInstruments.rdf)
    	// RDF: fibo-der-drc-si:YieldEnhancementProduct
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-str;StructuredProduct"/>
    }

}
