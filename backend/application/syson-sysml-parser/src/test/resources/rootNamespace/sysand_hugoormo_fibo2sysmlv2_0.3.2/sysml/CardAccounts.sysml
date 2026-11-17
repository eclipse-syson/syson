// ===================================================================
// CardAccounts.sysml
// Source-parity extracted from: FiBO_LOAN_LoanSpecific.sysml
// Primary source module: CardAccounts.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/LoansSpecific
// ===================================================================
package Card_Accounts {
  doc/* This ontology defines revolving credit account-related concepts that are specific to credit and debit cards. Note that it does not differentiate between consumer and commercial/corporate cards and is capable of representing either. (source: dct:abstract, CardAccounts.rdf) */
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

  item def <'card account'> CardAccount specializes Clients_and_Accounts::CustomerAccount {
  	doc /* account whose terms and conditions are defined in a card agreement that is represented by a payment card */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardAccount
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;CustomerAccount"/>
  }
  item def <'card authentication value'> CardAuthenticationValue specializes MagneticStripeVerificationCodeValue {
  	doc /* card verification value specifically for JCB payment cards */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardAuthenticationValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;MagneticStripeVerificationCodeValue"/>
  }
  item def <'card authentication value 2'> CardAuthenticationValue2 specializes CardAuthenticationValue, ThreeDigitVerificationCodeValue {
  	doc /* card verification value specifically for JCB payment cards */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardAuthenticationValue2
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;ThreeDigitVerificationCodeValue"/>
  }
  item def <'card expiration date'> CardExpirationDate specializes FiBO_FND_ExternalStubs::Date {
  	doc /* date on which a given payment card expires */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardExpirationDate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDate"/>
  }
  item def <'cardholder'> Cardholder specializes Clients_and_Accounts::CustomerAccountHolder {
  	doc /* account holder to whom a payment card is issued */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:Cardholder
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;CustomerAccountHolder"/>
  }
  item def <'card identification number'> CardIdentificationNumber specializes ThreeDigitVerificationCodeValue {
  	doc /* card verification value specifically for American Express and Discover payment cards */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardIdentificationNumber
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;ThreeDigitVerificationCodeValue"/>
  }
  item def <'card product'> CardProduct specializes Financial_Products_and_Services::FinancialProduct {
  	doc /* financial product involving the issuance of credit, debit, or other payment cards */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardProduct
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialProduct"/>
  }
  item def <'card security code'> CardSecurityCode specializes MagneticStripeVerificationCodeValue {
  	doc /* card verification value specifically for American Express payment cards */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardSecurityCode
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;MagneticStripeVerificationCodeValue"/>
  }
  item def <'card validation code'> CardValidationCode specializes MagneticStripeVerificationCodeValue {
  	doc /* card verification code specifically for Mastercard payment cards */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardValidationCode
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;MagneticStripeVerificationCodeValue"/>
  }
  item def <'card validation code 2'> CardValidationCode2 specializes CardValidationCode, ThreeDigitVerificationCodeValue {
  	doc /* card verification value specifically for Mastercard payment cards */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardValidationCode2
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;ThreeDigitVerificationCodeValue"/>
  }
  item def <'card verification code or value'> CardVerificationCodeValue specializes FiBO_FND_ExternalStubs::CodeElement {
  	doc /* code that specifies either (1) magnetic-stripe data, or (2) printed security features that are used to protect data integrity and limit alteration, counterfeiting and fraud generally */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardVerificationCodeValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
  }
  item def <'card verification value'> CardVerificationValue specializes MagneticStripeVerificationCodeValue {
  	doc /* card verification value specifically for Visa and Discover payment cards */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardVerificationValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;MagneticStripeVerificationCodeValue"/>
  }
  item def <'card verification value 2'> CardVerificationValue2 specializes CardVerificationValue, ThreeDigitVerificationCodeValue {
  	doc /* card verification value specifically for Visa payment cards */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CardVerificationValue2
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;ThreeDigitVerificationCodeValue"/>
  }
  item def <'commercial credit card agreement'> CommercialCreditCardAgreement specializes CreditCardAgreement {
  	doc /* credit card agreement for a card issued to, or in conjunction with, a formal organization, such as a small business, middle market business, local, state, or national government, or large corporation */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CommercialCreditCardAgreement
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;CreditCardAgreement"/>
  }
  item def <'consumer credit card agreement'> ConsumerCreditCardAgreement specializes CreditCardAgreement, Consumer_Loans::UnsecuredConsumerLoan {
  	doc /* credit card agreement for a card issued for household, family, or other personal expenditures that is accessed by a borrower's use of a credit card */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:ConsumerCreditCardAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-cns;UnsecuredConsumerLoan"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;CreditCardAgreement"/>
  }
  item def <'credit card'> CreditCard specializes PaymentCard {
  	doc /* card issued by a financial service provider that enables the cardholder to borrow funds */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CreditCard
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;PaymentCard"/>
  }
  item def <'credit card account'> CreditCardAccount specializes CardAccount, Clients_and_Accounts::LoanOrCreditAccount {
  	doc /* card account whose terms and conditions are defined in a credit card agreement that is represented by a credit card */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CreditCardAccount
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;LoanOrCreditAccount"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;CardAccount"/>
  }
  item def <'credit card agreement'> CreditCardAgreement specializes PaymentCardAgreement, Debt::CommittedCreditFacility {
  	doc /* account-specific credit facility that specifies the terms and conditions under which the credit card is offered to the cardholder by the issuer */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CreditCardAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CommittedCreditFacility"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;PaymentCardAgreement"/>
  }
  item def <'credit card network'> CreditCardNetwork specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* classifier for the network that authorizes, processes, and sets the terms of credit card transactions, as well as transfers payments between shoppers, merchants, and their banks */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CreditCardNetwork
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'credit card product'> CreditCardProduct specializes CardProduct {
  	doc /* card product allowing the holder to purchase goods or services on credit */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:CreditCardProduct
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;CardProduct"/>
  }
  item def <'debit card'> DebitCard specializes PaymentCard {
  	doc /* payment card issued by a financial service provider that enables the cardholder to access funds in a demand deposit account */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:DebitCard
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;PaymentCard"/>
  }
  item def <'debit card account'> DebitCardAccount specializes CardAccount {
  	doc /* card account that is represented by a one or more debit cards */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:DebitCardAccount
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;CardAccount"/>
  }
  item def <'debit card product'> DebitCardProduct specializes CardProduct {
  	doc /* card product card typically provided by a depository institution allowing the holder to transfer money electronically to another account when making a purchase */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:DebitCardProduct
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;CardProduct"/>
  }
  item def <'issuing financial institution'> IssuingFinancialInstitution specializes Financial_Instruments::Issuer, Financial_Products_and_Services::FinancialServiceProvider {
  	doc /* issuer and financial services provider that issues payment cards or performs, facilitates, or supports issuing services including but not limited to issuing banks and issuing processors */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:IssuingFinancialInstitution
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Issuer"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
  }
  item def <'magnetic stripe verification code or value'> MagneticStripeVerificationCodeValue specializes CardVerificationCodeValue {
  	doc /* card verification code on a card's magnetic stripe that uses secure cryptographic processes to protect data integrity on the stripe, and reveals any alteration or counterfeiting */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:MagneticStripeVerificationCodeValue
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;CardVerificationCodeValue"/>
  }
  item def <'payment card'> PaymentCard specializes FiBO_FND_ExternalStubs::LegalDocument {
  	doc /* legal document issued by a financial services provider that enables the cardholder to access the funds in the customer's designated bank accounts, or through a credit account and make payments by electronic funds transfer and access automated teller machines (ATMs) */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:PaymentCard
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;LegalDocument"/>
  }
  item def <'payment card agreement'> PaymentCardAgreement specializes Debt::CreditAgreement, Clients_and_Accounts::AccountSpecificServiceAgreement {
  	doc /* account-specific credit agreement that specifies the terms and conditions under which the payment card is offered to the cardholder by the issuer */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:PaymentCardAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;AccountSpecificServiceAgreement"/>
  }
  item def <'primary card account number'> PrimaryCardAccountNumber specializes Clients_and_Accounts::AccountIdentifier {
  	doc /* composite identifier of 14 or 16 digits embossed on a bank or payment card and encoded in the card's magnetic strip */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:PrimaryCardAccountNumber
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;AccountIdentifier"/>
  }
  item def <'smart card'> SmartCard specializes PaymentCard {
  	doc /* payment card that has integrated circuits embedded within it */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:SmartCard
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;PaymentCard"/>
  }
  item def <'three-digit verification code or value'> ThreeDigitVerificationCodeValue specializes CardVerificationCodeValue {
  	doc /* card verification code that is the rightmost three-digit value printed in the signature panel area on the back of the card */
  	// skos:definition xml:lang="en" (CardAccounts.rdf)
  	// rdfs:label xml:lang="en" (CardAccounts.rdf)
  	// RDF: fibo-loan-ln-card:ThreeDigitVerificationCodeValue
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-crd;CardVerificationCodeValue"/>
  }
}

// --- fibo-loan-ln-cl ---
