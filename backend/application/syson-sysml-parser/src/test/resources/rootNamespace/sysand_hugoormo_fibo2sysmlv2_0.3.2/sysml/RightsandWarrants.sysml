// ===================================================================
// RightsandWarrants.sysml
// Source-parity extracted from: FiBO_DER_DerivativesContracts.sysml
// Primary source module: RightsAndWarrants.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/DerivativesContracts
// ===================================================================
package Rights_and_Warrants {
  doc/* The Rights and Warrants ontology covers a range of financial instruments providing the holder with the privilege to subscribe to or receive specific assets on terms specified. These include rights (privileges) extended to existing security holders to make new securities available to them at reduced prices or for free, and warrants whereby the holder can purchase or sell back a given quantity of the instrument, commodity or currency during a specified period at a pre-defined price. (source: dct:abstract, RightsAndWarrants.rdf) */
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

  item def <'allotment right formula'> AllotmentRightFormula specializes Analytics::Expression {
    	doc /* rule applied to calculate the number of securities for an allotment right, typically based on the number of these instruments that the holder holds */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:AllotmentRightFormula
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
      // subClassOf: Expression - external CMNS
    }

  item def <'allotment right'> AllotmentRight specializes Security_Based_Derivatives::EquityDerivative, Entitlement {
    	doc /* privileges allotted to existing security holders, entitling them to receive new securities free of charge */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:AllotmentRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;EquityDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Entitlement"/>
    }

  item def <'barrier instrument-based mini-future certificate'> BarrierInstrumentBasedMiniFutureCertificate specializes MiniFutureCertificate {
    	doc /* mini-future certificate that immediately expires if the barrier instrument trading price level is breached during product lifetime */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:BarrierInstrumentBasedMiniFutureCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;MiniFutureCertificate"/>
    }

  item def <'barrier underlying mini-future certificate'> BarrierUnderlyingMiniFutureCertificate specializes MiniFutureCertificate {
    	doc /* mini-future certificate that immediately expires if the barrier underlying level is breached during product lifetime */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:BarrierUnderlyingMiniFutureCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;MiniFutureCertificate"/>
    }

  item def <'basket warrant'> BasketWarrant specializes Warrant {
    	doc /* warrant that permits the holder to acquire a package or group of assets at a specified price during a specified period */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:BasketWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'bond purchase right'> BondPurchaseRight specializes PurchaseRight {
    	doc /* purchase right that gives a prospective acquiree&apos;s bondholders the right to buy bonds of the firm or bonds of anyone who acquires the firm at a deep discount to their fair market value */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:BondPurchaseRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;PurchaseRight"/>
    }

  item def <'bond subscription right'> BondSubscriptionRight specializes SubscriptionRight {
    	doc /* subscription right entitling existing bond holders to subscribe to new securities at a price normally lower than the prevailing market price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:BondSubscriptionRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;SubscriptionRight"/>
    }

  item def <'bond with warrant'> BondWithWarrant specializes Bonds::Bond {
    	doc /* bond that is issued together with one or more warrant(s) attached as part of the offer, the warrant(s) granting the holder the right to purchase a designated security, often the common stock of the issuer of the debt, at a specified price */
    	// skos:definition xml:lang="en" (RightsandWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsandWarrants.rdf)
    	// RDF: fibo-der-drc-raw:BondWithWarrant
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
      // Defined canonically in SEC debt; no direct local rdfs:subClassOf mapping applied here.
    }

  item def <'call put warrant'> CallPutWarrant specializes CallWarrant, PutWarrant {
    	doc /* warrant that either does not specify call or put features, or that explicitly includes both a call and put feature */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:CallPutWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;CallWarrant"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;PutWarrant"/>
    }

  item def <'call warrant'> CallWarrant specializes Warrant {
    	doc /* warrant giving the buyer (holder) the right, but not the obligation, to acquire specific underlying assets during a specified period at a specified price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:CallWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'combined instruments purchase right'> CombinedInstrumentsPurchaseRight specializes PurchaseRight {
    	doc /* purchase right that gives a prospective acquiree&apos;s holders of multiple securities the right to buy securities of the firm or securities of anyone who acquires the firm at a deep discount to their fair market value */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:CombinedInstrumentsPurchaseRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;PurchaseRight"/>
    }

  item def <'combined instruments subscription right'> CombinedInstrumentsSubscriptionRight specializes SubscriptionRight {
    	doc /* subscription right entitling existing holders of multiple securities to subscribe to new securities at a price normally lower than the prevailing market price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:CombinedInstrumentsSubscriptionRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;SubscriptionRight"/>
    }

  item def <'commodity warrant'> CommodityWarrant specializes Commodities_Contracts::CommodityDerivative, Warrant {
    	doc /* warrant that permits the holder to acquire a specified amount of a commodity during a specified period at a specified price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:CommodityWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-comm;CommodityDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'common convertible share purchase right'> CommonConvertibleSharePurchaseRight specializes PurchaseRight {
    	doc /* purchase right that gives a prospective acquiree&apos;s common, convertible shareholders the right to buy common, convertible shares of the firm or common, convertible shares of anyone who acquires the firm at a deep discount to their fair market value */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:CommonConvertibleSharePurchaseRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;PurchaseRight"/>
    }

  item def <'common convertible share subscription right'> CommonConvertibleShareSubscriptionRight specializes SubscriptionRight {
    	doc /* subscription right entitling existing common convertible shareholders to subscribe to new securities at a price normally lower than the prevailing market price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:CommonConvertibleShareSubscriptionRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;SubscriptionRight"/>
    }

  item def <'common share purchase right'> CommonSharePurchaseRight specializes PurchaseRight {
    	doc /* purchase right that gives a prospective acquiree&apos;s common shareholders the right to buy common shares of the firm or common shares of anyone who acquires the firm at a deep discount to their fair market value */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:CommonSharePurchaseRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;PurchaseRight"/>
    }

  item def <'common share subscription right'> CommonShareSubscriptionRight specializes SubscriptionRight {
    	doc /* subscription right entitling existing common shareholders to subscribe to new securities at a price normally lower than the prevailing market price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:CommonShareSubscriptionRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;SubscriptionRight"/>
    }

  item def <'constant leverage certificate'> ConstantLeverageCertificate specializes Security_Based_Derivatives::SecurityBasedDerivative, Entitlement, Options::Option {
    	doc /* entitlement that combines the structure of an open-end certificate with a leverage option with no fixed term, making leverage available without a term restriction, without a knock-out barrier dependency */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:ConstantLeverageCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;SecurityBasedDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Entitlement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Option"/>
    }

  item def <'covered warrant'> CoveredWarrant specializes Warrant {
    	doc /* warrant that gives the holder the right, but not the obligation, to buy (call warrant) or to sell (put warrant) an underlying asset at a specified price (the strike or exercise price) by a predetermined date, issued without an accompanying security by a third party that holds as many securities as would be required if all the warrants are exercised */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:CoveredWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'currency warrant'> CurrencyWarrant specializes Currency_Contracts::CurrencyDerivative, Warrant {
    	doc /* warrant that permits the holder to acquire a specified amount in a certain currency at a specified exchange rate during a specified period */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:CurrencyWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-cur;CurrencyDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'debt warrant'> DebtWarrant specializes Warrant, Security_Based_Derivatives::DebtInstrumentDerivative {
    	doc /* warrant that permits the holder to acquire a specified amount of a debt instrument or related debt observable a specified period at a specified price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:DebtWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;DebtInstrumentDerivative"/>
    }

  item def <'entitlement'> Entitlement specializes Financial_Instruments::FinancialInstrument {
    	doc /* financial instrument that provides the holder an interest in, or the privilege to subscribe to, or to receive specific assets under terms specified */
    	// skos:definition xml:lang="en" (RightsandWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsandWarrants.rdf)
    	// RDF: fibo-der-drc-raw:Entitlement
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
    }

  item def <'equity warrant'> EquityWarrant specializes CallWarrant, TraditionalWarrant, Security_Based_Derivatives::EquityDerivative {
    	doc /* warrant that permits the holder to acquire a specified amount of an equity instrument during a specified period at a specified price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:EquityWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;CallWarrant"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;TraditionalWarrant"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;EquityDerivative"/>
    }

  item def <'exchange-traded warrant'> ExchangeTradedWarrant specializes PublicWarrant, Securities_Listings::ListedSecurity {
    	doc /* warrant that is listed on a securities exchange */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:ExchangeTradedWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;PublicWarrant"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;ListedSecurity"/>
    }

  item def <'exotic warrant'> ExoticWarrant specializes Warrant {
    	doc /* warrant that gives the holder the right (but not the obligation) to buy (or sometimes sell) an underlying asset under non-standard or complex conditions, often involving additional features not found in a plain vanilla (standard) warrant */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:ExoticWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'index warrant'> IndexWarrant specializes Warrant {
    	doc /* warrant that permits the holder to acquire a specified amount based on the performance of an index during a specified period at a specified price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:IndexWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'long mini-future certificate'> LongMiniFutureCertificate specializes MiniFutureCertificate {
    	doc /* mini-future certificate that typically entitles the holder to acquire specific underlying assets during a specified period at a specified price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:LongMiniFutureCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;MiniFutureCertificate"/>
    }

  item def <'mini-future certificate'> MiniFutureCertificate specializes Exotic_Options::BarrierOption, Security_Based_Derivatives::SecurityBasedDerivative, Entitlement {
    	doc /* entitlement that combines the structure of an open-end certificate with a leverage option with no fixed term, making leverage available without a term restriction, and whose payoff depends on whether or not the underlying asset has reached or exceeded a predetermined price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:MiniFutureCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-exo;BarrierOption"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;SecurityBasedDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Entitlement"/>
    }

  item def <'naked warrant'> NakedWarrant specializes Warrant {
    	doc /* warrant that gives the holder the right, but not the obligation, to buy (call warrant) or to sell (put warrant) an underlying asset at a specified price (the strike or exercise price) by a predetermined date, issued without an accompanying security by a third party that does not hold as many securities as would be required if all the warrants are exercised */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:NakedWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'perpetual warrant'> PerpetualWarrant specializes Warrant {
    	doc /* purchase right that gives a prospective acquiree&apos;s preferred, convertible shareholders the right to buy preferred, convertible shares of the firm or preferred, convertible shares of anyone who acquires the firm at a deep discount to their fair market value */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:PerpetualWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'preferred convertible share purchase right'> PreferredConvertibleSharePurchaseRight specializes PurchaseRight {
    	doc /* purchase right that gives a prospective acquiree's preferred, convertible shareholders the right to buy preferred, convertible shares of the firm or preferred, convertible shares of anyone who acquires the firm at a deep discount to their fair market value */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:PreferredConvertibleSharePurchaseRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;PurchaseRight"/>
    }

  item def <'preferred convertible share subscription right'> PreferredConvertibleShareSubscriptionRight specializes SubscriptionRight {
    	doc /* subscription right entitling existing preferred, convertible shareholders to subscribe to new securities at a price normally lower than the prevailing market price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:PreferredConvertibleShareSubscriptionRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;SubscriptionRight"/>
    }

  item def <'preferred share purchase right'> PreferredSharePurchaseRight specializes PurchaseRight {
    	doc /* purchase right that gives a prospective acquiree&apos;s preferred shareholders the right to buy preferred shares of the firm or preferred shares of anyone who acquires the firm at a deep discount to their fair market value */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:PreferredSharePurchaseRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;PurchaseRight"/>
    }

  item def <'preferred share subscription right'> PreferredShareSubscriptionRight specializes SubscriptionRight {
    	doc /* subscription right entitling existing preferred shareholders to subscribe to new securities at a price normally lower than the prevailing market price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:PreferredShareSubscriptionRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;SubscriptionRight"/>
    }

  item def <'private warrant'> PrivateWarrant specializes Warrant, Financial_Instruments::NonNegotiableSecurity {
    	doc /* anti-takeover device that gives a prospective acquiree&apos;s shareholders the right to buy shares of the firm or shares of anyone who acquires the firm at a deep discount to their fair market value */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:PrivateWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;NonNegotiableSecurity"/>
    }

  item def <'public warrant'> PublicWarrant specializes Warrant, Financial_Instruments::NegotiableSecurity {
    	doc /* warrant that may be traded over the counter (OTC) or through an exchange */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:PublicWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;NegotiableSecurity"/>
    }

  item def <'purchase right'> PurchaseRight specializes Entitlement {
    	doc /* anti-takeover device that gives a prospective acquiree's shareholders the right to buy shares of the firm or shares of anyone who acquires the firm at a deep discount to their fair market value */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:PurchaseRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Entitlement"/>
    }

  item def <'put warrant'> PutWarrant specializes Warrant {
    	doc /* warrant giving the buyer (holder) the right, but not the obligation, to sell the assets specified (i.e., acquire cash in exchange for the underlying assets, such as stock) back to the issuer at a fixed price or formula, on or before a specified date */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:PutWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'short mini-future certificate'> ShortMiniFutureCertificate specializes MiniFutureCertificate {
    	doc /* mini-future certificate that entitles the holder to acquire cash in exchange for specific underlying assets */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:ShortMiniFutureCertificate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;MiniFutureCertificate"/>
    }

  item def <'structured warrant'> StructuredWarrant specializes Warrant {
    	doc /* warrant that is listed on an exchange, offering investors a way to participate in the price performance of an underlying asset without buying it directly */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:StructuredWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'subscription right'> SubscriptionRight specializes Entitlement {
    	doc /* privileges allotted to existing security holders, entitling them to subscribe to new securities at a price normally lower than the prevailing market price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:SubscriptionRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Entitlement"/>
    }

  item def <'traditional warrant'> TraditionalWarrant specializes Warrant {
    	doc /* warrant that gives the holder the right, but not the obligation, to buy (call warrant) or to sell (put warrant) an underlying asset at a specified price (the strike or exercise price) by a predetermined date, issued by the issuer of the underlying instrument */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:TraditionalWarrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-raw;Warrant"/>
    }

  item def <'warrant'> Warrant specializes Entitlement {
    	doc /* entitlement that permits the holder to purchase a specified amount of a financial instrument, commodity, currency or other asset during a specified period at a specified price */
    	// skos:definition xml:lang="en" (RightsAndWarrants.rdf)
    	// rdfs:label xml:lang="en" (RightsAndWarrants.rdf)
    	// RDF: fibo-der-drc-raw:Warrant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Entitlement"/>
    }

  item def CompanyWarrant { // RDF: fibo-der-drc-raw:CompanyWarrant
      // equivalentClass: EquityWarrant; no direct rdfs:subClassOf mapping applied.
    }

      // RDF: fibo-der-drc-raw:CompanyWarrant
}
