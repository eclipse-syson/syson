// ===================================================================
// EquityInstruments.sysml
// Source-parity extracted from: FiBO_SEC_Equities.sysml
// Primary source module: EquityInstruments.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Equities
// ===================================================================
package Equity_Instruments {
  doc/* Core terms are those fundamental to all equity instruments. This ontology also distinguishes between privately held and publicly traded equity instruments, and defines a number of related concepts, such as voting rights. (source: dct:abstract, EquityInstruments.rdf) */
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

  item def <'adjustable rate dividend'> AdjustableRateDividend specializes PreferredDividend {
    	doc /* dividend that varies with a benchmark */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:AdjustableRateDividend
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredDividend"/>
    }

  item def <'auction rate dividend'> AuctionRateDividend specializes PreferredDividend {
    	doc /* preferred share dividend whose rate is periodically reset through an auction, typically every 7, 14, 28, or 35 days */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:AuctionRateDividend
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredDividend"/>
    }

  item def <'common share'> CommonShare specializes Share {
    	doc /* share that signifies a unit of ownership in a corporation and represents a claim on part of the corporation's assets and earnings */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:CommonShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'convertible common share'> ConvertibleCommonShare specializes CommonShare, Securities_Issuance::ConvertibleSecurity {
    	doc /* common share that is convertible into another security */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:ConvertibleCommonShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;ConvertibleSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
    }

  item def <'convertible preferred share'> ConvertiblePreferredShare specializes PreferredShare, Securities_Issuance::ConvertibleSecurity {
    	doc /* preferred share that includes an option for the holder to convert the shares into a fixed number of common shares after a predetermined date */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:ConvertiblePreferredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;ConvertibleSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'cumulative preferred share'> CumulativePreferredShare specializes PreferredShare {
    	doc /* preferred share whose dividends, if not paid on time, accumulate until paid out */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:CumulativePreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'custodian'> Custodian specializes Financial_Services_Entities::FinancialInstitution, Financial_Products_and_Services::ThirdPartyAgent {
    	doc /* financial institution that holds customers' securities for safekeeping */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:Custodian
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ThirdPartyAgent"/>
    }

  item def <'dividend'> Dividend specializes Agreements::Commitment {
    	doc /* share that confers more than one vote per share */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:Dividend
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Commitment"/>
    }

  item def <'dividend distribution method'> DividendDistributionMethod specializes Business_Dates::Convention, Objectives::Strategy {
    	doc /* convention by which dividends are provided to shareholders */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:DividendDistributionMethod
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;Convention"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Strategy"/>
    }

  item def <'dividend schedule'> DividendSchedule specializes Financial_Dates::RegularSchedule, Payments_and_Schedules::PaymentSchedule {
    	doc /* payment schedule indicating the dates on which dividends are due to be paid */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:DividendSchedule
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;RegularSchedule"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;PaymentSchedule"/>
    }

  item def <'enhanced voting share'> EnhancedVotingShare specializes Share {
    	doc /* share that confers more than one vote per share */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:EnhancedVotingShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'equity conversion terms'> EquityConversionTerms specializes Securities_Issuance::ConversionTerms {
    	doc /* position in an equity instrument */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:EquityConversionTerms
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;ConversionTerms"/>
    }

  item def <'equity position'> EquityPosition specializes Financial_Products_and_Services::Position {
    	doc /* position in an equity instrument */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:EquityPosition
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Position"/>
    }

  item def <'equity redemption provision'> EquityRedemptionProvision specializes Financial_Instruments::RedemptionProvision {
    	doc /* redemption provision that specifies the conditions under which the issuer or shareholder may redeem the shares */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:EquityRedemptionProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;RedemptionProvision"/>
    }

  item def <'equity redemption provision with extendable redemption date'> EquityRedemptionProvisionWithExtendableRedemptionDate specializes EquityRedemptionProvision {
    	doc /* equity redemption provision that allows modification of the redemption date beyond the original specified date */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:EquityRedemptionProvisionWithExtendableRedemptionDate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;EquityRedemptionProvision"/>
    }

  item def <'exchangeable preferred share'> ExchangeablePreferredShare specializes PreferredShare {
    	doc /* share whose payment status indicates that no additional money is owed to the company by shareholders on the value of the shares */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:ExchangeablePreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'extendable preferred share'> ExtendablePreferredShare specializes PreferredShare {
    	doc /* preferred share whose redemption date can be extended at the issuer or holder option */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:ExtendablePreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'fixed rate dividend'> FixedRateDividend specializes PreferredDividend {
    	doc /* dividend that provides a specified annual return on the nominal value (and any premium) paid on shares */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:FixedRateDividend
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredDividend"/>
    }

  item def <'fully paid share'> FullyPaidShare specializes Share {
    	doc /* share whose payment status indicates that no additional money is owed to the company by shareholders on the value of the shares */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:FullyPaidShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'hard retractable preferred share'> HardRetractablePreferredShare specializes RetractablePreferredShare {
    	doc /* share in a form of partnership similar to a general partnership, except that in addition to one or more general partners (GPs), there are one or more limited partners (LPs) */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:HardRetractablePreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RetractablePreferredShare"/>
    }

  item def <'limited partnership unit'> LimitedPartnershipUnit specializes Share {
    	doc /* share in a form of partnership similar to a general partnership, except that in addition to one or more general partners (GPs), there are one or more limited partners (LPs) */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:LimitedPartnershipUnit
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'listed share'> ListedShare specializes Share, Securities_Listings::ListedSecurity {
    	doc /* share that is listed on at least one platform */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:ListedShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;ListedSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'nil paid share'> NilPaidShare specializes Share {
    	doc /* share whose payment status indicates that none of the market value has been received by the company for the shares */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:NilPaidShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'non-cumulative preferred share'> NonCumulativePreferredShare specializes PreferredShare {
    	doc /* share that has no voting right */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:NonCumulativePreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'non-participating preferred share'> NonParticipatingPreferredShare specializes PreferredShare {
    	doc /* preferred share that is not a participating preferred share */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:NonParticipatingPreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'non-voting share'> NonVotingShare specializes Share {
    	doc /* share that has no voting right */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:NonVotingShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'ordinary dividend'> OrdinaryDividend specializes Dividend {
    	doc /* share whose payment status indicates that only a portion of the market value has been received by the company for the shares */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:OrdinaryDividend
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Dividend"/>
    }

  item def <'partially paid share'> PartiallyPaidShare specializes Share {
    	doc /* share whose payment status indicates that only a portion of the market value has been received by the company for the shares */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:PartiallyPaidShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'participating preferred share'> ParticipatingPreferredShare specializes PreferredShare {
    	doc /* share in a security that signifies ownership in an entity that is not publicly traded */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:ParticipatingPreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'perpetual preferred share'> PerpetualPreferredShare specializes PreferredShare {
    	doc /* preferred share that has no fixed maturity date */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:PerpetualPreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'precedence right'> PrecedenceRight specializes Legal_Capacity::ContractualRight {
    	doc /* right that give the shareholder higher priority with respect to certain pre-defined dividend payments over other shareholders and that pay out first in the case of liquidation */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:PrecedenceRight
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;ContractualRight"/>
    }

  item def <'preferred dividend'> PreferredDividend specializes Dividend {
    	doc /* commitment to distribute a portion of earnings to shareholders, similar to a dividend but often with a fixed payment amount and schedule */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:PreferredDividend
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Dividend"/>
    }

  item def <'preferred share'> PreferredShare specializes Share {
    	doc /* share that pays dividends at a specified rate and has preference over common shares in the payment of dividends and liquidation of corporate assets */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:PreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'preferred share with adjustable rate dividend'> PreferredShareWithAdjustableRateDividend specializes PreferredShare {
    	doc /* preferred share whose dividend rate varies according to some benchmark */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:PreferredShareWithAdjustableRateDividend
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'preferred share with auction rate dividend'> PreferredShareWithAuctionRateDividend specializes PreferredShare {
    	doc /* preferred share whose dividend rate is periodically reset through an auction, such as a Dutch auction */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:PreferredShareWithAuctionRateDividend
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'preferred share with fixed maturity date'> PreferredShareWithFixedMaturityDate specializes PreferredShare {
    	doc /* preferred share whose maturity date is set, typically according to the terms of the prospectus */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:PreferredShareWithFixedMaturityDate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'preferred share with fixed rate dividend'> PreferredShareWithFixedRateDividend specializes PreferredShare {
    	doc /* preferred share whose dividends are set, typically according to the terms of the prospectus */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:PreferredShareWithFixedRateDividend
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'price per share'> PricePerShare specializes Instrument_Pricing::SecurityPrice, Currency_Amount::UnitPrice {
    	doc /* price for one share of a given security at some point in time */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:PricePerShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;UnitPrice"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
    }

  item def <'privately held share'> PrivatelyHeldShare specializes Share {
    	doc /* share in a security that signifies ownership in an entity that is not publicly traded */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:PrivatelyHeldShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'qualified dividend'> QualifiedDividend specializes Dividend {
    	doc /* share whose ownership/transfer/sale is subject to special conditions including country-specific restrictions */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:QualifiedDividend
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Dividend"/>
    }

  item def <'redeemable preferred share'> RedeemablePreferredShare specializes PreferredShare {
    	doc /* preferred share that confers multiple votes per share, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:RedeemablePreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'redeemable preferred share with extendable maturity date'> RedeemablePreferredShareWithExtendableMaturityDate specializes RedeemablePreferredShare, PreferredShareWithFixedMaturityDate {
    	doc /* preferred share that confers multiple votes per share, may be exchanged for securities of another issuer, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:RedeemablePreferredShareWithExtendableMaturityDate
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedMaturityDate"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RedeemablePreferredShare"/>
    }

  item def <'redeemable preferred share with extendable redemption date'> RedeemablePreferredShareWithExtendableRedemptionDate specializes RedeemablePreferredShare {
    	doc /* preferred share that confers multiple votes per share, may be redeemed at the option of the issuer and/or of the shareholder with an extendable redemption date, provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:RedeemablePreferredShareWithExtendableRedemptionDate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RedeemablePreferredShare"/>
    }

  item def <'restricted share'> RestrictedShare specializes Share {
    	doc /* share whose ownership/transfer/sale is subject to special conditions including country-specific restrictions */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:RestrictedShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'restricted voting share'> RestrictedVotingShare specializes Share {
    	doc /* share that confers less than one vote per share */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:RestrictedVotingShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'retractable preferred share'> RetractablePreferredShare specializes PreferredShare {
    	doc /* financial instrument that signifies a unit of equity ownership in a corporation, or a unit of ownership in a mutual fund, or interest in a general or limited partnership, or a unit of ownership in a structured product, such as a real estate investment trust */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:RetractablePreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShare"/>
    }

  item def <'retractable preferred share with extendable maturity date'> RetractablePreferredShareWithExtendableMaturityDate specializes RetractablePreferredShare, PreferredShareWithFixedMaturityDate {
    	doc /* preferred share that confers multiple votes per share, may be exchanged for securities of another issuer, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:RetractablePreferredShareWithExtendableMaturityDate
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedMaturityDate"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RetractablePreferredShare"/>
    }

  item def <'retractable preferred share with extendable redemption date'> RetractablePreferredShareWithExtendableRedemptionDate specializes RetractablePreferredShare {
    	doc /* preferred share that confers multiple votes per share, may be redeemed at the option of the issuer and/or of the shareholder with an extendable redemption date, provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:RetractablePreferredShareWithExtendableRedemptionDate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RetractablePreferredShare"/>
    }

  item def <'share'> Share specializes Financial_Instruments::EquityInstrument { // RDF: fibo-sec-eq-eq:Share
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;EquityInstrument"/>
      // RDF: fibo-sec-eq-eq:Share
    }

  item def <'share issuer'> ShareIssuer specializes Financial_Instruments::Issuer {
    	doc /* ratio of the annualized dividend per share divided by the (current) price per share */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:ShareIssuer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Issuer"/>
    }

  item def <'share payment status'> SharePaymentStatus specializes Lifecycles::LifecycleStatus {
    	doc /* classifier that specifies the overall payment status for shares issued */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:SharePaymentStatus
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStatus"/>
    }

  item def <'share yield'> ShareYield specializes Instrument_Pricing::Yield {
    	doc /* ratio of the annualized dividend per share divided by the (current) price per share */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:ShareYield
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;Yield"/>
    }

  item def <'single voting share'> SingleVotingShare specializes Share {
    	doc /* share that has the right to exactly one vote */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:SingleVotingShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'soft retractable preferred share'> SoftRetractablePreferredShare specializes RetractablePreferredShare {
    	doc /* share whose ownership/transfer/sale is not subject to special conditions including country-specific restrictions */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:SoftRetractablePreferredShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RetractablePreferredShare"/>
    }

  item def <'special dividend'> SpecialDividend specializes Dividend {
    	doc /* dividend that is paid to shareholders on a one-time basis */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:SpecialDividend
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Dividend"/>
    }

  item def <'unrestricted share'> UnrestrictedShare specializes Share, Financial_Instruments::NegotiableSecurity {
    	doc /* share whose ownership/transfer/sale is not subject to special conditions including country-specific restrictions */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:UnrestrictedShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;NegotiableSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'variable interest entity share'> VariableInterestEntityShare specializes Share {
    	doc /* grants the right to vote on a per share basis to the shareholder */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:VariableInterestEntityShare
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;Share"/>
    }

  item def <'voting right'> VotingRight specializes Legal_Capacity::ContractualRight {
    	doc /* contractual right that specifies shareholder voting entitlements, such as to elect directors, elect outside auditors, and vote on matters of corporate policy */
    	// skos:definition xml:lang="en" (EquityInstruments.rdf)
    	// rdfs:label xml:lang="en" (EquityInstruments.rdf)
    	// RDF: fibo-sec-eq-eq:VotingRight
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;ContractualRight"/>
    }

  item def ShareholdingEquity{ // RDF: fibo-sec-eq-eq:Shareholding
    }

    // RDF: fibo-sec-eq-eq:ShareholdingEquity
}
