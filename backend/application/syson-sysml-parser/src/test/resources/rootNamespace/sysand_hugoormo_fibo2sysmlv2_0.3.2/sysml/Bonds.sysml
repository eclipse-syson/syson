// ===================================================================
// Bonds.sysml
// Source-parity extracted from: FiBO_SEC_Debt.sysml
// Primary source module: Bonds.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Debt
// ===================================================================
package Bonds {
  doc/* This ontology defines the basic concept of a bond and a number of bond variants including convertible and callable bonds. Medium term notes (MTNs) and debentures are also defined. (source: dct:abstract, Bonds.rdf) */
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

// TradableDebtInstrument
// MortgageBackedSecurity
// PartyRole

  item def <'bond'> Bond specializes Debt_Instruments::TradableDebtInstrument, Debt::CreditAgreementRepaidAtMaturity {
  	doc /* a bond offering where the issue includes a warrant; Further notes: ISO 10962 CFI definition is A bond that is issued together with one or more warrant(s) attached as part of the offer, the warrant(s) granting the holder the right to purchase a designated security, often the common stock of the issuer of the debt, at a specified price. Review notes: This need not be any specific type of bond. The warrant is used as a sweetener to encourage people to subscribe to a new bond issue. The Bond and the Warrant trade together as a unit (called "Bond Unit"). */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:Bond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreementRepaidAtMaturity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;TradableDebtInstrument"/>
  }
  item def <'amortizing bond'> AmortizingBond specializes Bond {
  	doc /* bond that regularly pays down the principal (face value) on the debt along with its interest expense over the life of the bond */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:AmortizingBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'bond amortization payment terms'> BondAmortizationPaymentTerms specializes BondPrincipalRepaymentTerms {
  	doc /* terms that include a schedule for repayment of the principal over the lifetime of the bond, typically in equal payments at regular intervals */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BondAmortizationPaymentTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;BondPrincipalRepaymentTerms"/>
  }
  item def <'bond conversion terms'> BondConversionTerms specializes Securities_Issuance::ConversionTerms {
  	doc /* terms indicating when a convertible bond can be converted to another security (usually a publicly traded share issued by of the same issuer) */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BondConversionTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;ConversionTerms"/>
  }
  item def <'bond coupon'> BondCoupon specializes Currency_Amount::InterestRate {
  	doc /* interest rate on a debt security that the issuer promises to pay to the holder until maturity, expressed as an annual percentage of the face value */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BondCoupon
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;InterestRate"/>
  }
  item def <'bond insurance'> BondInsurance specializes Guaranty::InsurancePolicy {
  	doc /* insurance policy that a bond issuer purchases that guarantees the repayment of the principal and all associated interest payments to the bondholders in the event of default */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BondInsurance
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-gty;InsurancePolicy"/>
  }
  item def <'bond principal repayment terms'> BondPrincipalRepaymentTerms specializes Debt::PrincipalRepaymentTerms {
  	doc /* terms for the repayment of the principal on a bond */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BondPrincipalRepaymentTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;PrincipalRepaymentTerms"/>
  }
	item def <'bond registrar'> BondRegistrar specializes Financial_Products_and_Services::LegalAgent, FiBO_FND_ExternalStubs::Registrar {
  	doc /* party responsible for maintaining records on behalf of the issuer that identify the owners of a registered bond issue */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BondRegistrar
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;LegalAgent"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;Registrar"/>
  }
  item def <'bond variable coupon'> BondVariableCoupon specializes Debt::VariableInterestRate, BondCoupon {
  	doc /* bond coupon that has a variable interest rate */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BondVariableCoupon
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;VariableInterestRate"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;BondCoupon"/>
  }
  item def <'bond with partial call'> BondWithPartialCall specializes CallableBond {
  	doc /* bond with a feature whereby the issue can be partially called for amounts that are at the discretion of the issuer */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BondWithPartialCall
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;CallableBond"/>
  }
  item def <'bond with published sinking fund'> BondWithPublishedSinkingFund specializes AmortizingBond {
  	doc /* bond whereby amortization is via a known and published schedule */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BondWithPublishedSinkingFund
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;AmortizingBond"/>
  }
  item def <'bond with warrant'> BondWithWarrant specializes Bond {
  	doc /* bond that is issued together with one or more warrant(s) attached as part of the offer, the warrant(s) granting the holder the right to purchase a designated security, often the common stock of the issuer of the debt, at a specified price */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BondWithWarrant
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'Build America Bond'> BuildAmericaBond specializes MunicipalBond {
  	doc /* taxable municipal bond issued through December 31, 2010 under the American Recovery and Reinvestment Act of 2009 (ARRA) */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BuildAmericaBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;MunicipalBond"/>
  }
  item def <'bullet bond'> BulletBond specializes Bond {
  	doc /* bond whose entire principal value is paid on the maturity date, rather than amortized over its lifetime */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BulletBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'bond bullet principal repayment terms'> BulletPrincipalRepaymentTerms specializes BondPrincipalRepaymentTerms {
  	doc /* terms for repayment of the principal for a bond as a single payment covering the whole principal amount */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:BulletPrincipalRepaymentTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;BondPrincipalRepaymentTerms"/>
  }
  item def <'callable bond'> CallableBond specializes Bond {
  	doc /* bond that includes a stipulation allowing the issuer the right to repurchase and retire the bond at the call price after the call protection period */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:CallableBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'callable convertible bond'> CallableConvertibleBond specializes CallableBond, ConvertibleBond {
  	doc /* convertible bond that is also callable */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:CallableConvertibleBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;CallableBond"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;ConvertibleBond"/>
  }
  item def <'certificate of obligation'> CertificateOfObligation specializes MunicipalSecurity {
  	doc /* municipal security available to governing councils in case of emergency, such as a natural disaster, that needs immediate action without time for voter referendum */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:CertificateOfObligation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;MunicipalSecurity"/>
  }
  item def <'certificate of participation'> CertificateOfParticipation specializes Financial_Instruments::DebtInstrument {
  	doc /* debt instrument evidencing a pro rata share in a specific pledged revenue stream, usually lease payments by the issuer that are typically subject to annual appropriation */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:CertificateOfParticipation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DebtInstrument"/>
  }
  item def <'conventional gilt'> ConventionalGilt specializes UKGovernmentSecurity, FixedCouponBond {
  	doc /* fixed coupon bond issued by HM Treasury that guarantees to pay the holder of the gilt a fixed cash payment (coupon) every six months until the maturity date, at which point the holder receives the final coupon payment and the return of the principal */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:ConventionalGilt
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;FixedCouponBond"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;UKGovernmentSecurity"/>
  }
  item def <'convertible bond'> ConvertibleBond specializes Bond, Securities_Issuance::ConvertibleSecurity {
  	doc /* bond that gives the holder the right to convert the bond into a fixed number of shares (conversion ratio) if the equity price rises above a specified level (strike price) */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:ConvertibleBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;ConvertibleSecurity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'corporate bond'> CorporateBond specializes Bond {
  	doc /* bond issued by a company in order to raise financing */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:CorporateBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'coupon payment terms'> CouponPaymentTerms specializes Debt::InterestPaymentTerms {
  	doc /* terms for payment of interest on a bond */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:CouponPaymentTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;InterestPaymentTerms"/>
  }
  item def <'equity linked bond'> EquityLinkedBond specializes VariableCouponBond {
  	doc /* bond based on the return on an equity over time, i.e. the price and dividend payments or the total return (similar to total return swaps) */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:EquityLinkedBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;VariableCouponBond"/>
  }
  item def <'extraordinary redemption provision'> ExtraordinaryRedemptionProvision specializes Debt_Instruments::CallFeature {
  	doc /* provision that gives a bond issuer the right to call its bonds due to an unusual one-time FiBOOccurrence, as specified in the offering statement */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:ExtraordinaryRedemptionProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;CallFeature"/>
  }
	item def <'first regular coupon date'> FirstRegularCouponDate specializes FiBO_FND_ExternalStubs::Date {
  	doc /* scheduled date for the first regular coupon payment, in a regular repeating series of coupon periods, after any non standard (long or short) first coupon period in the schedule */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:FirstRegularCouponDate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDate"/>
  }
  item def <'fixed coupon bond'> FixedCouponBond specializes Bond, Debt_Instruments::FixedIncomeSecurity {
  	doc /* bond whose coupon rate and principal amount are fixed at the time of origination or sale and remain constant while the security is outstanding */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:FixedCouponBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;FixedIncomeSecurity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'fixed coupon terms'> FixedCouponTerms specializes CouponPaymentTerms {
  	doc /* terms for payment of interest on a bond with a fixed interest rate */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:FixedCouponTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;CouponPaymentTerms"/>
  }
  item def <'floating rate note'> FloatingRateNote specializes VariableCouponBond {
  	doc /* bond with a variable interest rate based on a published reference interest rate */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:FloatingRateNote
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;VariableCouponBond"/>
  }
  item def <'full faith credit bond'> FullFaithCreditBond specializes UnsecuredBond {
  	doc /* bond secured by an unconditional promise to pay by another entity */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:FullFaithCreditBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;UnsecuredBond"/>
  }
  item def <'general obligation municipal bond'> GeneralObligationMunicipalBond specializes MunicipalBond, FullFaithCreditBond {
  	doc /* municipal bond that is backed by the full faith and credit and general resources of the issuing municipality, including its general taxing authority */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:GeneralObligationMunicipalBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;FullFaithCreditBond"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;MunicipalBond"/>
  }
  item def <'government bond'> GovernmentBond specializes GovernmentIssuedDebtSecurity, Bond {
  	doc /* debt security issued by a government to fund government spending */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:GovernmentBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;GovernmentIssuedDebtSecurity"/>
  }
  item def <'government issued debt security'> GovernmentIssuedDebtSecurity specializes Financial_Instruments::DebtInstrument {
  	doc /* debt security issued by some government on behalf of some polity, including sovereign and municipal debt */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:GovernmentIssuedDebtSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DebtInstrument"/>
  }
  item def <'green bond'> GreenBond specializes Bond {
  	doc /* bond issued specifically to fund climate or environmental projects */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:GreenBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'implicit full faith credit bond'> ImplicitFullFaithCreditBond specializes UnsecuredBond {
  	doc /* bond issued by a government sponsored agency or corporation rather than by the government directly */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:ImplicitFullFaithCreditBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;UnsecuredBond"/>
  }
  item def <'index-linked bond'> IndexLinkedBond specializes VariableIncomeBond {
  	doc /* bond whose income may vary over time, because either the coupon rate or principal amount is related to a specific index, such as the Consumer Price Index */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:IndexLinkedBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;VariableIncomeBond"/>
  }
  item def <'inflation-linked bond'> InflationLinkedBond specializes IndexLinkedBond {
  	doc /* bond indexed to inflation so that the principal or interest payments rise and fall with the rate of inflation */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:InflationLinkedBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;IndexLinkedBond"/>
  }
	item def <'last regular coupon date'> LastRegularCouponDate specializes FiBO_FND_ExternalStubs::Date {
  	doc /* scheduled date of the last regular coupon payment, that is the last of the payments that are made according to a regular repeating series of coupon periods, before any non-standard (long or short) final coupon period in the schedule */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:LastRegularCouponDate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDate"/>
  }
  item def <'listed bond'> ListedBond specializes Bond, Securities_Listings::ListedSecurity {
  	doc /* bond that may be traded on an exchange */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:ListedBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;ListedSecurity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'make whole call'> MakeWholeCall specializes Debt_Instruments::CallEvent {
  	doc /* call allowing the issuer to pay off remaining debt early */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:MakeWholeCall
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;CallEvent"/>
  }
  item def <'mandatory convertible bond'> MandatoryConvertibleBond specializes ConvertibleBond {
  	doc /* convertible bond that converts into shares at maturity regardless of the equity price */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:MandatoryConvertibleBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;ConvertibleBond"/>
  }
  item def <'medium term note'> MediumTermNote specializes Bond {
  	doc /* bond issued over time under a shelf registration program, where each issue may have a different coupon and maturity typically ranging from one to ten years */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:MediumTermNote
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'municipal bond'> MunicipalBond specializes GovernmentBond, MunicipalSecurity {
  	doc /* government bond that may be issued by a regional, rather than national, authority */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:MunicipalBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;GovernmentBond"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;MunicipalSecurity"/>
  }
  item def <'municipal debt funds usage'> MunicipalDebtFundsUsage specializes FiBO_FND_ExternalStubs::LegalDocument {
  	doc /* official statement specifying how the funds raised via a new issue of municipal securities are to be applied */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:MunicipalDebtFundsUsage
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;LegalDocument"/>
  }
  item def <'municipal debt remarketing agent'> MunicipalDebtRemarketingAgent specializes Financial_Products_and_Services::Dealer, Contracts::ContractThirdParty {
  	doc /* municipal securities dealer responsible for reselling to investors securities (such as variable rate demand obligations and other tender option bonds) that have been tendered for purchase by their owner */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:MunicipalDebtRemarketingAgent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Dealer"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractThirdParty"/>
  }
  item def <'municipal debt source of funds'> MunicipalDebtSourceOfFunds specializes FiBO_FND_ExternalStubs::LegalDocument {
  	doc /* official statement identifying the source of funds for a new issue of municipal securities */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:MunicipalDebtSourceOfFunds
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;LegalDocument"/>
  }
  item def <'municipal note'> MunicipalNote specializes MunicipalSecurity {
  	doc /* short-term obligation to repay a specified principal amount on a certain date, together with interest at a stated rate, usually payable from a defined source of anticipated revenues */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:MunicipalNote
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;MunicipalSecurity"/>
  }
  item def <'municipal security'> MunicipalSecurity specializes GovernmentIssuedDebtSecurity {
  	doc /* debt obligation issued by a regional governmental entity */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:MunicipalSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;GovernmentIssuedDebtSecurity"/>
  }
  item def <'municipal trustee'> MunicipalTrustee specializes Trusts::Trustee, Financial_Services_Entities::FinancialInstitution {
  	doc /* financial institution with trust powers, designated by the issuer, that acts, pursuant to a bond contract, in a fiduciary capacity for the benefit of the bondholders in enforcing the terms of the bond contract */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:MunicipalTrustee
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-tr-tr;Trustee"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
  }
  item def <'original issue discount bond'> OriginalIssueDiscountBond specializes Bond {
  	doc /* interest-bearing bond issued at a deep discount to face value */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:OriginalIssueDiscountBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'partial call'> PartialCall specializes Debt_Instruments::CallEvent {
  	doc /* call of part of an issue */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:PartialCall
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;CallEvent"/>
  }
  item def <'partial call feature'> PartialCallFeature specializes Debt_Instruments::CallFeature {
  	doc /* call feature whereby the issuer can recall part of the issue on scheduled dates, where bonds are selected to be called according to some rule, or by selecting various bonds at random */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:PartialCallFeature
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;CallFeature"/>
  }
  item def <'partial redemption allocation convention'> PartialRedemptionAllocationConvention specializes Business_Dates::Convention {
  	doc /* convention used to determine how the partial call will be actioned with respect to bond selection */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:PartialRedemptionAllocationConvention
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;Convention"/>
  }
  item def <'perpetual bond'> PerpetualBond specializes Bond {
  	doc /* bond that has no maturity date, i.e., one that pays interest in perpetuity */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:PerpetualBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'put feature'> PutFeature specializes Debt::DebtTerms, Financial_Instruments::RedemptionProvision {
  	doc /* redemption provision giving the holder the right, but not the obligation, to sell a specified amount of the debt instrument (i.e., redeem it), prior to maturity */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:PutFeature
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;DebtTerms"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;RedemptionProvision"/>
  }
  item def <'rate basis convention'> RateBasisConvention specializes Business_Dates::Convention {
  	doc /* basis for a percentage quoted rate as used in specifying bond call percentage amounts */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:RateBasisConvention
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;Convention"/>
  }
  item def <'redemption payment'> RedemptionPayment specializes Payments_and_Schedules::PaymentEvent, Debt_Instruments::RedemptionEvent {
  	doc /* repayment event involving payment of a bond's principal amount at maturity or when it is called */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:RedemptionPayment
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;PaymentEvent"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;RedemptionEvent"/>
  }
  item def <'redemption schedule'> RedemptionSchedule specializes Payments_and_Schedules::PaymentSchedule {
  	doc /* repayment schedule whereby a given percentage of a bond issue is redeemed on predefined dates */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:RedemptionSchedule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;PaymentSchedule"/>
  }
  item def <'regulatory call'> RegulatoryCall specializes Debt_Instruments::CallEvent {
  	doc /* call triggered by some regulation-specific rule */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:RegulatoryCall
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;CallEvent"/>
  }
  item def <'remarketable bond'> RemarketableBond specializes CorporateBond, FloatingRateNote {
  	doc /* corporate bond program where the coupon rate on outstanding bonds is periodically reset through an auction process */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:RemarketableBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;CorporateBond"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;FloatingRateNote"/>
  }
  item def <'revenue bond'> RevenueBond specializes MunicipalBond, SecuredBond {
  	doc /* municipal bond supported by the revenue from a specific project */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:RevenueBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;SecuredBond"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;MunicipalBond"/>
  }
  item def <'secured bond'> SecuredBond specializes Bond {
  	doc /* bond that is backed by collateral, such as a tangible asset or income stream, in addition to a general promise to pay */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:SecuredBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'sinking fund amortization terms'> SinkingFundAmortizationTerms specializes BondAmortizationPaymentTerms {
  	doc /* terms for the paydown of principal in a sinking fund type of amortizing security */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:SinkingFundAmortizationTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;BondAmortizationPaymentTerms"/>
  }
  item def <'sovereign bond'> SovereignBond specializes GovernmentBond, SovereignDebtInstrument {
  	doc /* bond issued by the government of a country */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:SovereignBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;SovereignDebtInstrument"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;GovernmentBond"/>
  }
  item def <'sovereign debt instrument'> SovereignDebtInstrument specializes GovernmentIssuedDebtSecurity {
  	doc /* debt security issued by the government of a country */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:SovereignDebtInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;GovernmentIssuedDebtSecurity"/>
  }
  item def <'special assessment bond'> SpecialAssessmentBond specializes MunicipalBond, SecuredBond {
  	doc /* municipal bond used to fund a development project that is payable from the revenues of an assessment (tax) on the community that is intended to benefit from the project */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:SpecialAssessmentBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;SecuredBond"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;MunicipalBond"/>
  }
  item def <'special obligation bond'> SpecialObligationBond specializes MunicipalBond {
  	doc /* bond secured by a limited revenue source, such as receipts derived from a designated project */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:SpecialObligationBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;MunicipalBond"/>
  }
  item def <'special tax bond'> SpecialTaxBond specializes MunicipalBond, SecuredBond {
  	doc /* bond secured by revenues derived from designated taxes other than ad valorem taxes */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:SpecialTaxBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;SecuredBond"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;MunicipalBond"/>
  }
  item def <'stepped coupon terms'> SteppedCouponTerms specializes CouponPaymentTerms {
  	doc /* coupon payment terms for securities with a coupon that increases (steps up) while the bond is outstanding */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:SteppedCouponTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;CouponPaymentTerms"/>
  }
  item def <'step up bond'> StepUpBond specializes Bond, Debt_Instruments::FixedIncomeSecurity {
  	doc /* bond with a coupon that increases (steps up) while the bond is outstanding */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:StepUpBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;FixedIncomeSecurity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'strip bond'> StripBond specializes ZeroCouponBond, Debt_Instruments::Strip {
  	doc /* bond that is part of a series of bonds formed by selling each interest payment and the principal amount of a bond as separate zero coupon bonds. */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:StripBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;ZeroCouponBond"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;Strip"/>
  }
  item def <'tax allocation bond'> TaxAllocationBond specializes MunicipalBond, SecuredBond {
  	doc /* bond payable from the incremental increase in tax revenues realized from any increase in property value and other economic activity, often designed to capture the economic benefit resulting from a bond financing */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:TaxAllocationBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;SecuredBond"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;MunicipalBond"/>
  }
  item def <'treasury bill'> TreasuryBill specializes USTreasurySecurity, Traded_Short_Term_Debt::MoneyMarketInstrument {
  	doc /* short-term zero coupon treasury obligation with a maturity ranging from one to twelve months */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:TreasuryBill
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-tstd;MoneyMarketInstrument"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;USTreasurySecurity"/>
  }
  item def <'treasury bond'> TreasuryBond specializes USTreasurySecurity, SovereignBond {
  	doc /* long term term coupon bearing treasury obligation issued in terms of 20 years or 30 years that pays interest every six months until they mature */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:TreasuryBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;SovereignBond"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;USTreasurySecurity"/>
  }
  item def <'treasury inflation-protected security'> TreasuryInflationProtectedSecurity specializes USTreasurySecurity, InflationLinkedBond, VariablePrincipalBond {
  	doc /* variable income bond whose principal is indexed to inflation or deflation and thus changes over the life of the security */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:TreasuryInflationProtectedSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;InflationLinkedBond"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;VariablePrincipalBond"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;USTreasurySecurity"/>
  }
  item def <'treasury note'> TreasuryNote specializes USTreasurySecurity, MediumTermNote {
  	doc /* medium term coupon bearing treasury obligation with original maturity ranging from two to ten years */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:TreasuryNote
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;MediumTermNote"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;USTreasurySecurity"/>
  }
  item def <'U.K. Government security'> UKGovernmentSecurity specializes SovereignDebtInstrument {
  	doc /* debt instrument issued by HM Treasury and listed on the London Stock Exchange */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:UKGovernmentSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;SovereignDebtInstrument"/>
  }
  item def <'unlisted bond'> UnlistedBond specializes Bond {
  	doc /* bond that is traded over the counter rather than via an exchange or other listing facility */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:UnlistedBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'unsecured bond'> UnsecuredBond specializes Bond {
  	doc /* bond that is only secured by the bond issuer's good credit standing */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:UnsecuredBond
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'U.S. Treasury security'> USTreasurySecurity specializes SovereignDebtInstrument {
  	doc /* debt instrument issued by the United States Department of the Treasury that carries a full faith and credit guarantee */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:USTreasurySecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;SovereignDebtInstrument"/>
  }
  item def <'variable coupon bond'> VariableCouponBond specializes VariableIncomeBond {
  	doc /* bond that has a floating interest rate */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:VariableCouponBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;VariableIncomeBond"/>
  }
  item def <'variable coupon terms'> VariableCouponTerms specializes CouponPaymentTerms {
  	doc /* contractual terms specifying the calculation of the interest rate for a variable coupon bond */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:VariableCouponTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;CouponPaymentTerms"/>
  }
  item def <'variable debt principal'> VariableDebtPrincipal specializes Debt_Instruments::PubliclyIssuedDebt {
  	doc /* principal that is defined in relation to some variable and so varies over time, as principal */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:VariableDebtPrincipal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;PubliclyIssuedDebt"/>
  }
  item def <'variable income bond'> VariableIncomeBond specializes Bond, Debt_Instruments::VariableIncomeSecurity {
  	doc /* bond whose income may vary over time, because either the coupon rate or principal amount changes in line with an index or schedule over the life of the security */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:VariableIncomeBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;VariableIncomeSecurity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'variable interest calculation formula'> VariableInterestCalculationFormula specializes Debt_Instruments::FullyIndexedInterestRate {
  	doc /* formula for the calculation of variable interest */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:VariableInterestCalculationFormula
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;FullyIndexedInterestRate"/>
  }
  item def <'variable interest expression'> VariableInterestExpression specializes Analytics::Expression {
  	doc /* an expression used to determine a variable interest payment amount */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:VariableInterestExpression
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
  }
  item def <'variable principal bond'> VariablePrincipalBond specializes VariableIncomeBond {
  	doc /* bond whose principal adjusts over time with changes in an index */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:VariablePrincipalBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;VariableIncomeBond"/>
  }
  item def <'zero coupon bond'> ZeroCouponBond specializes Bond, Debt_Instruments::FixedIncomeSecurity {
  	doc /* bond issued with a coupon rate of zero and that trades at a deep discount to face value */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:ZeroCouponBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;FixedIncomeSecurity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;Bond"/>
  }
  item def <'zero coupon terms'> ZeroCouponTerms specializes BondAmortizationPaymentTerms, FixedCouponTerms {
  	doc /* terms for payment of interest on a zero coupon bond */
  	// skos:definition xml:lang="en" (Bonds.rdf)
  	// rdfs:label xml:lang="en" (Bonds.rdf)
  	// RDF: fibo-sec-dbt-bnd:ZeroCouponTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;BondAmortizationPaymentTerms"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;FixedCouponTerms"/>
  }
}

// --- CollateralizedDebtObligations.rdf ---
