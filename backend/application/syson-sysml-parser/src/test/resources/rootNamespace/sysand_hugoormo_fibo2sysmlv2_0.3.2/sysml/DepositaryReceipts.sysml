// ===================================================================
// DepositaryReceipts.sysml
// Source-parity extracted from: FiBO_SEC_Equities.sysml
// Primary source module: DepositaryReceipts.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Equities
// ===================================================================
package Depositary_Receipts {
  doc/* Depositary receipts are certificates which represent ownership of some underlying security. They are issued by a bank and give the holder the ability to participate in the returns on an instrument that they may not be able to hold directly. (source: dct:abstract, DepositaryReceipts.rdf) */
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

  item def <'American depositary receipt level'> AmericanDepositaryReceiptLevel specializes Securities_Classification::FinancialInstrumentClassifier {
    	doc /* classifier for American depositary receipts that categorizes ADRs into levels based on the extent to which the foreign company has access to the U.S. market */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:AmericanDepositaryReceiptLevel
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-cls;FinancialInstrumentClassifier"/>
    }

  item def <'American depositary receipt level scheme'> AmericanDepositaryReceiptLevelScheme specializes Securities_Classification::FinancialInstrumentClassificationScheme {
    	doc /* classifier for American depositary receipts that categorizes ADRs into levels based on the extent to which the foreign company has access to the U.S. market */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:AmericanDepositaryReceiptLevelScheme
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-cls;FinancialInstrumentClassificationScheme"/>
    }

  item def <'Chinese depositary receipt'> ChineseDepositaryReceipt specializes GlobalDepositaryReceipt {
    	doc /* global depositary receipt that represents ownership in the securities of a non-Chinese company that trades on a public exchange in China */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:ChineseDepositaryReceipt
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;GlobalDepositaryReceipt"/>
    }

  item def <'depositary receipt'> DepositaryReceipt specializes Financial_Instruments::Security {
    	doc /* negotiable financial instrument issued by a trust company, security depositary, or bank that is evidence of the deposit of publicly traded securities and that facilitates the ownership of securities traded in other jurisdictions */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:DepositaryReceipt
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Security"/>
    }

  item def <'European depositary receipt'> EuropeanDepositaryReceipt specializes GlobalDepositaryReceipt {
    	doc /* global depositary receipt that represents ownership in the securities of a non-European company that trades in European financial markets */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:EuropeanDepositaryReceipt
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;GlobalDepositaryReceipt"/>
    }

  item def <'global depositary receipt'> GlobalDepositaryReceipt specializes DepositaryReceipt {
    	doc /* depositary receipt where a certificate issued by a depositary bank, which purchases securities of foreign companies, creates a security on a local exchange backed by those securities */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:GlobalDepositaryReceipt
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;DepositaryReceipt"/>
    }

  item def <'Hong Kong depositary receipt'> HongKongDepositaryReceipt specializes GlobalDepositaryReceipt {
    	doc /* global depositary receipt that represents the purchase, or ownership, of foreign assets which are deposited in a depositary bank in Hong Kong */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:HongKongDepositaryReceipt
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;GlobalDepositaryReceipt"/>
    }

  item def <'Indian depositary receipt'> IndianDepositoryReceipt specializes GlobalDepositaryReceipt {
    	doc /* global depositary receipt that represents the purchase, or ownership, of foreign assets which are deposited in a Indian account managed by the Domestic Depository in India */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:IndianDepositoryReceipt
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;GlobalDepositaryReceipt"/>
    }

  item def <'Japanese depositary receipt'> JapaneseDepositaryReceipt specializes GlobalDepositaryReceipt {
    	doc /* global depositary receipt that represents the purchase, or ownership, of foreign assets which are deposited in a trust bank in Japan */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:JapaneseDepositaryReceipt
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;GlobalDepositaryReceipt"/>
    }

  item def <'Luxembourg depositary receipt'> LuxembourgDepositaryReceipt specializes GlobalDepositaryReceipt {
    	doc /* global depositary receipt that represents the purchase, or ownership, of foreign assets which are deposited in a Luxembourg-based account */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:LuxembourgDepositaryReceipt
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;GlobalDepositaryReceipt"/>
    }

  item def <'offshore depositary receipt'> OffshoreDepositaryReceipt specializes SponsoredDepositaryReceipt {
    	doc /* depositary receipt issued under SEC Regulation S */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:OffshoreDepositaryReceipt
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;SponsoredDepositaryReceipt"/>
    }

  item def <'participatory note'> ParticipatoryNote specializes DepositaryReceipt, Debt_Instruments::TradableDebtInstrument {
    	doc /* tradable debt instrument that facilitates the ownership of securities traded in other jurisdictions */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:ParticipatoryNote
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;TradableDebtInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;DepositaryReceipt"/>
    }

  item def <'privately placed depositary receipt'> PrivatelyPlacedDepositaryReceipt specializes SponsoredDepositaryReceipt {
    	doc /* depositary receipt that represents shares in a private placement under the SEC Rule 144-A */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:PrivatelyPlacedDepositaryReceipt
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;SponsoredDepositaryReceipt"/>
    }

  item def <'sponsored depositary receipt'> SponsoredDepositaryReceipt specializes AmericanDepositaryReceipt {
    	doc /* depositary receipt that is issued in collaboration with the foreign company enabling them to tap into international capital markets directly */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:SponsoredDepositaryReceipt
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;AmericanDepositaryReceipt"/>
    }

  item def <'unsponsored depositary receipt'> UnsponsoredDepositaryReceipt specializes AmericanDepositaryReceipt {
    	doc /* depositary receipt that is established without the company&apos;s cooperation */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:UnsponsoredDepositaryReceipt
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;AmericanDepositaryReceipt"/>
    }

  item def <'American depositary receipt'> AmericanDepositaryReceipt specializes DepositaryReceipt {
    	doc /* depositary receipt that represents securities of a foreign company and allows that company&apos;s securities to trade in the U.S. financial markets */
    	// skos:definition xml:lang="en" (DepositaryReceipts.rdf)
    	// rdfs:label xml:lang="en" (DepositaryReceipts.rdf)
    	// RDF: fibo-sec-eq-dr:AmericanDepositaryReceipt
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-dr;DepositaryReceipt"/>
    }

}

// --- fibo-sec-eq-eq ---
