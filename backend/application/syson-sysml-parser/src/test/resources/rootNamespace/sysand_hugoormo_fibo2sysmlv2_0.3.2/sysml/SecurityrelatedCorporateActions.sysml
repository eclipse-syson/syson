// ===================================================================
// SecurityrelatedCorporateActions.sysml
// Source-parity extracted from: FiBO_CAE_CorporateEvents.sysml
// Primary source module: SecurityRelatedCorporateActions.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Corporate Action and Events CAE/CorporateEvents
// ===================================================================
package Security_related_Corporate_Actions {
  doc/* This ontology defines the kinds of income and corporate action events covered by ISO 15022 and other standards, including recent extensions to those standards. Scope has been limited to security-related events and actions, and excludes most notification and meetings related events. (source: dct:abstract, SecurityRelatedCorporateActions.rdf) */
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

  item def <'security related corporate action'> SecurityRelatedCorporateAction specializes Corporate_Actions::CorporateAction {
    doc /* base class for all security-related corporate actions */
    // skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    // rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    // RDF: fibo-cae-ce-srca:SecurityRelatedCorporateAction
  }

  item def <'bond default event'> BondDefaultAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryCorporateAction, Credit_Events::ObligationDefault {
    	doc /* corporate action which indicates a failure by the issuer to perform obligations defined as default events under the bond agreement and that have not been remedied */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:BondDefaultAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;ObligationDefault"/>
    }

  item def <'bonus issue'> BonusIssue specializes Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action in which security holders are awarded additional assets free of payment from the issuer in proportion to their holding */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:BonusIssue
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'bonus rights issue'> BonusRightsIssue specializes IntermediateSecuritiesDistribution {
    	doc /* A Rights Issue in which the rights are given to the holders of the referenced shares for free. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:BonusRightsIssue
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-srca;IntermediateSecuritiesDistribution"/>
    }

  item def <'bonus share plan distribution'> BonusSharePlanDistribution specializes CapitalDistribution {
    	doc /* Event in which shares are issued from the Share Premium Reserve of the company and are considered as a capital distribution rather than a disbursement of income, with different tax implications. SWIFT Definition (adapted above) &quot;Typically found in Australia, shares are issued from the Share Premium Reserve of the company and are considered as a capital distribution rather than a disbursement of income, with different tax implications.&quot; To be researches further. There are certainly Share Plans in Australia, where you can take a dividend as shares. Thewse would be in the nature of Bonus Shares. They are a tradeoff from cash dividends. Other terms to model: A &quot;Scheme of Arrangment&quot; - does this change the capital reserves or is it just another word for restructure. An arrangement with the company&apos;s debtors, and coule potentially change the capital base by converting the debt to shares (see other diagram note with these in). */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:BonusSharePlanDistribution
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-srca;CapitalDistribution"/>
    }

  item def <'call on intermediate securities'> CallOnIntermediateSecurities specializes Corporate_Actions::MandatoryWithChoiceCorporateAction {
    	doc /* corporate action that involves a call or exercise on nil paid securities or intermediate securities resulting from an intermediate securities distribution (RHDI) */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:CallOnIntermediateSecurities
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryWithChoiceCorporateAction"/>
    }

  item def <'cancellation of shares'> CancellationOfShares specializes Corporate_Actions::CorporateAction {
    	doc /* The cancellation of shares. Further Notes Only possible with shares not publicly issued i.e. treasury shares */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:CancellationOfShares
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

  item def <'capital distribution'> CapitalDistribution specializes Corporate_Actions::VoluntaryCorporateAction {
    	doc /* corporate action that pays shareholders an amount in cash issued from the issuer&apos;s capital account */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:CapitalDistribution
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'capital gains distribution'> CapitalGainsDistribution specializes Corporate_Actions::VoluntaryCorporateAction {
    	doc /* corporate action that distributes profits resulting from the sale of company assets to shareholders */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:CapitalGainsDistribution
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'cash dividend action'> CashDividendAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action that distributes cash to shareholders in proportion to their equity holding */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:CashDividendAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'change of security trading status event'> ChangeOfSecurityTradingStatusEvent specializes CorporateChangeOfStatusEvent {
    	doc /* An event in which the trading status of a tradable security changes. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:ChangeOfSecurityTradingStatusEvent
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-srca;CorporateChangeOfStatusEvent"/>
    }

  item def <'change to smallest negotiable unit'> ChangeToSmallestNegotiableUnit specializes Corporate_Actions::CorporateAction {
    	doc /* Modification of the smallest negotiable unit of shares in order to obtain a new negotiable unit. SWIFT:SMAL */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:ChangeToSmallestNegotiableUnit
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

  item def <'conversion action'> ConversionAction specializes SecurityRelatedCorporateAction, Corporate_Actions::VoluntaryCorporateAction {
    	doc /* corporate action involving conversion of securities (generally convertible bonds or preferred shares) into another form of securities (usually common shares) at a pre-stated price/ratio */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:ConversionAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'conversion suspension action'> ConversionSuspensionAction specializes SecurityRelatedCorporateAction, Corporate_Actions::CorporateAction {
    	doc /* Suspension of conversion of securities, generally a couple of weeks before a meeting takes place. SWIFT = SCOP */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:ConversionSuspensionAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

  item def <'corporate action obligation'> CorporateActionObligation specializes Corporate_Actions::CorporateAction, Legal_Capacity::Duty {
    	doc /* An obligation related to the holding of a Security. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:CorporateActionObligation
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;Duty"/>
    }

  item def <'corporate action payment obligation'> CorporateActionPaymentObligation specializes CorporateActionObligation {
    	doc /* Some obligation to deliver some security in the thr context of corporate actions. All cash proceeds and security proceeds can be represented a s acontractual obligation. Where does that obligation arise? Usually in the Contract itself - but there may be other answers to this. Defines what has to be delivered. or paid. Entitlement meanwhile is a calculation based on the Contract. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:CorporateActionPaymentObligation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-srca;CorporateActionObligation"/>
    }

  item def <'corporate action securities delivery obligation'> CorporateActionSecuritiesDeliveryObligation specializes CorporateActionObligation {
    	doc /* Some obligation to deliver some security in the thr context of corporate actions. All cash proceeds and security proceeds can be represented a s acontractual obligation. Where does that obligation arise? Usually in the Contract itself - but there may be other answers to this. Defines what has to be delivered. or paid. Entitlement meanwhile is a calculation based on the Contract. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:CorporateActionSecuritiesDeliveryObligation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-srca;CorporateActionObligation"/>
    }

  item def <'corporate change of status event'> CorporateChangeOfStatusEvent specializes Corporate_Actions::CorporateAction, Lifecycles::LifecycleEvent {
    	doc /* Some change to the status of some security. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:CorporateChangeOfStatusEvent
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleEvent"/>
    }

  item def <'coupon strip'> CouponStrip specializes Corporate_Actions::CorporateAction {
    	doc /* Coupon stripping is the process whereby interest coupons for future payment dates are separated from the security corpus that entitles the holder to the principal repayment. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:CouponStrip
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

  item def <'decrease in value action'> DecreaseInValueAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action resulting in a reduction of face value of a share or the value of fund assets */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:DecreaseInValueAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'dividend option action'> DividendOptionAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryWithChoiceCorporateAction {
    	doc /* corporate action that involves distribution of a dividend to shareholders with a choice of benefit to receive */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:DividendOptionAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryWithChoiceCorporateAction"/>
    }

  item def <'dividend reinvestment action'> DividendReinvestmentAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryWithChoiceCorporateAction {
    	doc /* corporate action that involves a dividend payment whereby holders can keep cash or have the cash reinvested into additional shares in the issuing company */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:DividendReinvestmentAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryWithChoiceCorporateAction"/>
    }

  item def <'drawing'> Drawing specializes Corporate_Actions::MandatoryCorporateAction {
    	doc /* Redemption in part before the scheduled final maturity date of a security. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:Drawing
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'dutch auction'> DutchAuction specializes Corporate_Actions::VoluntaryCorporateAction {
    	doc /* corporate action by a party wishing to acquire a security */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:DutchAuction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'exchange action'> ExchangeAction specializes SecurityRelatedCorporateAction, Corporate_Actions::CorporateAction {
    	doc /* corporate action that reflects an exchange of holdings for other securities and/or cash */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:ExchangeAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

  item def <'formal offer'> FormalOffer specializes Corporate_Actions::VoluntaryCorporateAction {
    	doc /* corporate action involving an offer to a holder, potential holder, or other intersted party, to enter into a trade which is or would be legally binding on the part of the party making the offer (the offeror). */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:FormalOffer
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'full call early redemption action'> FullCallEarlyRedemptionAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action that involves redemption of an entire issue outstanding of securities prior to maturity */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:FullCallEarlyRedemptionAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'interest payment action'> InterestPaymentAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action that involves distribution of a regular interest payment to holders of an interest-bearing asset */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:InterestPaymentAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'interest payment in kind'> InterestPaymentInKind specializes InterestPaymentAction {
    	doc /* Interest payment, in any kind except cash, distributed to holders of an interest bearing asset. SWIFT = PINK */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:InterestPaymentInKind
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-srca;InterestPaymentAction"/>
    }

  item def <'interest payment with principal'> InterestPaymentWithPrincipal specializes InterestPaymentAction {
    	doc /* A payment of a portion of the principal of an interest bearing asset, in addition to the interest payment. SWIFT = PRII */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:InterestPaymentWithPrincipal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-srca;InterestPaymentAction"/>
    }

  item def <'interest rate adjustment'> InterestRateAdjustment specializes Corporate_Actions::MandatoryCorporateAction {
    	doc /* Scheduled change to the coupon rate for a floating or adjustable rate security. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:InterestRateAdjustment
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'intermediate securities distribution'> IntermediateSecuritiesDistribution specializes Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action involving the distribution of intermediate securities or privilege that gives the holder the right to take part in a future event */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:IntermediateSecuritiesDistribution
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'listing status delisting message'> ListingStatusDelistingMessage specializes FiBO_FND_ExternalStubs::Notice {
    	doc /* Security is no longer able to comply with the listing requirements of a stock exchange and is removed from official board quotation. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:ListingStatusDelistingMessage
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Notice"/>
    }

  item def <'maturity extension'> MaturityExtension specializes Corporate_Actions::CorporateAction, Credit_Default_Swaps::ObligationSpecificCreditEvent {
    	doc /* credit event involving extension of payments beyond the original maturity date of the obligation */
    	// skos:definition xml:lang="en" (SecurityrelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityrelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:MaturityExtension
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;ObligationSpecificCreditEvent"/>
    }

  item def <'odd lot offer'> OddLotOffer specializes Corporate_Actions::VoluntaryCorporateAction {
    	doc /* Offer by issuer to allow holders of an odd lot of a security to order a commission-free transaction at market price, to sell the odd lot, or to buy an amount of shares which will bring the position to a round lot (board lot). SWIFT = ODLT */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:OddLotOffer
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'pari-passu action'> PariPassuAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action that occurs when securities with different characteristics become identical in all respects */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:PariPassuAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'partial redemption with reduction of nominal value action'> PartialRedemptionWithReductionOfNominalValueAction specializes SecurityRelatedCorporateAction, Corporate_Actions::CorporateAction {
    	doc /* corporate action that involves redemption of securities in part before their scheduled final maturity date with reduction of the nominal value of the securities */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:PartialRedemptionWithReductionOfNominalValueAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

  item def <'partial redemption without reduction of nominal value action'> PartialRedemptionWithoutReductionOfNominalValueAction specializes SecurityRelatedCorporateAction, Corporate_Actions::CorporateAction {
    	doc /* corporate action that involves redemption of securities in part before their scheduled final maturity date with no reduction in nominal value */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:PartialRedemptionWithoutReductionOfNominalValueAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

  item def <'post-merger securities exchange'> PostMergerSecuritiesExchange specializes Corporate_Actions::CorporateAction {
    	doc /* corporate action that involves the exchange of outstanding securities, initiated by the issuer which may include options, as the result of two or more companies combining assets, that is, an external, third party company */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:PostMergerSecuritiesExchange
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

  item def <'put redemption action'> PutRedemptionAction specializes SecurityRelatedCorporateAction, Corporate_Actions::VoluntaryCorporateAction {
    	doc /* corporate action involving early redemption of a security at the election of the holder subject to the terms and condition of the issue with no reduction in nominal value */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:PutRedemptionAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'redemption at maturity action'> RedemptionAtMaturityAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action that involves redemption of an entire issue outstanding of securities at final maturity */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:RedemptionAtMaturityAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'redenomination action'> RedenominationAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action by which the unit (currency and/or nominal) of a security is restated */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:RedenominationAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'repurchase offer'> RepurchaseOffer specializes Corporate_Actions::VoluntaryCorporateAction {
    	doc /* corporate action in which an offer is made to existing shareholders by the issuing company to repurchase equity or other securities convertible into equity */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:RepurchaseOffer
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'reverse stock split'> ReverseStockSplit specializes Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action involving a decrease in a company&apos;s number of outstanding equities without any change in the shareholder&apos;s equity or the aggregate market value at the time of the split */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:ReverseStockSplit
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'rights exercise event'> RightsExerciseEvent specializes Corporate_Actions::CorporateAction {
    	doc /* Exercising the right to purchase the shares. Furhter Notes: This is an action on the part of the holder. SWIFT: Call/exercise on nil-paid securities/rights resulting from a rights distribution (RHDI) (To be used for the second event in case rights issue is dealt with in 2 events, first event being the RHDI). */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:RightsExerciseEvent
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

  item def <'shares premium dividend action'> SharesPremiumDividendAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryWithChoiceCorporateAction {
    	doc /* corporate action that pays shareholders an amount in cash issued from the shares premium reserve */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:SharesPremiumDividendAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryWithChoiceCorporateAction"/>
    }

  item def <'stock dividend action'> StockDividendAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action that involves distribution of a dividend to shareholders in the form of equities of the issuing corporation */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:StockDividendAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'stock split'> StockSplit specializes Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action involving an increase in a corporation&apos;s number of outstanding equities without any change in the shareholder&apos;s equity or the aggregate market value at the time of the split */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:StockSplit
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'tender offer'> TenderOffer specializes Corporate_Actions::VoluntaryCorporateAction {
    	doc /* corporate action involving an offer made to shareholders, normally by a third party, requesting them to sell (tender) or exchange their equities */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:TenderOffer
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'trading status active message'> TradingStatusActiveMessage specializes TradingStatusMessage {
    	doc /* Trading in security has commenced or security has been re-activated after a supsension in trading. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:TradingStatusActiveMessage
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-srca;TradingStatusMessage"/>
    }

  item def <'trading status message'> TradingStatusMessage specializes FiBO_FND_ExternalStubs::Notice {
    	doc /* A message about trading status. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:TradingStatusMessage
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Notice"/>
    }

  item def <'trading status suspended message'> TradingStatusSuspendedMessage specializes TradingStatusMessage {
    	doc /* Trading in the security has been suspended. */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:TradingStatusSuspendedMessage
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-srca;TradingStatusMessage"/>
    }

  item def <'warrant exercise action'> WarrantExerciseAction specializes SecurityRelatedCorporateAction, Corporate_Actions::CorporateAction {
    	doc /* corporate action that offers holders the option to buy (call warrant) or to sell (put warrant) a specific amount of stock, cash, or commodity, at a predetermined price, during a predetermined period of time (which usually corresponds to the life of the issue) */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:WarrantExerciseAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

  item def <'worthless security action'> WorthlessSecurityAction specializes SecurityRelatedCorporateAction, Corporate_Actions::MandatoryCorporateAction {
    	doc /* corporate action that involves booking out of valueless securities */
    	// skos:definition xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// rdfs:label xml:lang="en" (SecurityRelatedCorporateActions.rdf)
    	// RDF: fibo-cae-ce-srca:WorthlessSecurityAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

}
