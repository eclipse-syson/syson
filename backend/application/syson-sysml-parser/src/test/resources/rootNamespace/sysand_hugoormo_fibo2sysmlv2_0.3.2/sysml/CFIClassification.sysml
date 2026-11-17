// ===================================================================
// CFIClassification.sysml
// Source-parity extracted from: FiBO_SEC_Equities.sysml
// Primary source module(s): CFIClassification.rdf (for current translated content)
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Equities
// ===================================================================
package CFI_Classification {
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

  item def <'common, voting, unrestricted, fully-paid, registered share'> CommonVotingUnrestrictedFullyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::SingleVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::FullyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly one vote per share, is unrestricted from a sales perspective, is fully paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonVotingUnrestrictedFullyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;FullyPaidShare"/>
    }

  item def <'common, voting, unrestricted, nil paid, registered share'> CommonVotingUnrestrictedNilPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::SingleVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::NilPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly one vote per share, is unrestricted from a sales perspective, is nil paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonVotingUnrestrictedNilPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NilPaidShare"/>
    }

  item def <'common, voting, unrestricted, partly paid, registered share'> CommonVotingUnrestrictedPartlyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::SingleVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::PartiallyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly one vote per share, is unrestricted from a sales perspective, is partially paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonVotingUnrestrictedPartlyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PartiallyPaidShare"/>
    }

  item def <'common, voting, restricted, fully-paid, registered share'> CommonVotingRestrictedFullyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::SingleVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::FullyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly one vote per share, is restricted from a sales / transfer perspective, is fully paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonVotingRestrictedFullyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;FullyPaidShare"/>
    }

  item def <'common, voting, restricted, nil paid, registered share'> CommonVotingRestrictedNilPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::SingleVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::NilPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly one vote per share, is restricted from a sales / transfer perspective, is nil paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonVotingRestrictedNilPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NilPaidShare"/>
    }

  item def <'common, voting, restricted, partly paid, registered share'> CommonVotingRestrictedPartlyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::SingleVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::PartiallyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly one vote per share, is restricted from a sales / transfer perspective, is partially paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonVotingRestrictedPartlyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PartiallyPaidShare"/>
    }

  item def <'common, non-voting, unrestricted, fully-paid, registered share'> CommonNonVotingUnrestrictedFullyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::NonVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::FullyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly 0 votes per share, is unrestricted from a sales perspective, is fully paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonNonVotingUnrestrictedFullyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;FullyPaidShare"/>
    }

  item def <'common, non-voting, unrestricted, nil paid, registered share'> CommonNonVotingUnrestrictedNilPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::NonVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::NilPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly 0 votes per share, is unrestricted from a sales perspective, is nil paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonNonVotingUnrestrictedNilPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NilPaidShare"/>
    }

  item def <'common, non-voting, unrestricted, partly paid, registered share'> CommonNonVotingUnrestrictedPartlyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::NonVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::PartiallyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly 0 votes per share, is unrestricted from a sales perspective, is partially paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonNonVotingUnrestrictedPartlyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PartiallyPaidShare"/>
    }

  item def <'common, non-voting, restricted, fully-paid, registered share'> CommonNonVotingRestrictedFullyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::NonVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::FullyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly 0 votes per share, is restricted from a sales / transfer perspective, is fully paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonNonVotingRestrictedFullyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;FullyPaidShare"/>
    }

  item def <'common, non-voting, restricted, nil paid, registered share'> CommonNonVotingRestrictedNilPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::NonVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::NilPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly 0 votes per share, is restricted from a sales / transfer perspective, is nil paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonNonVotingRestrictedNilPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NilPaidShare"/>
    }

  item def <'common, non-voting, restricted, partly paid, registered share'> CommonNonVotingRestrictedPartlyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::NonVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::PartiallyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers exactly 0 votes per share, is restricted from a sales / transfer perspective, is partially paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonNonVotingRestrictedPartlyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PartiallyPaidShare"/>
    }

  item def <'common, restricted voting, unrestricted, fully-paid, registered share'> CommonRestrictedVotingUnrestrictedFullyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::RestrictedVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::FullyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers less than one vote per share, is unrestricted from a sales / transfer perspective, is fully paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonRestrictedVotingUnrestrictedFullyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;FullyPaidShare"/>
    }

  item def <'common, restricted voting, unrestricted, nil paid, registered share'> CommonRestrictedVotingUnrestrictedNilPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::RestrictedVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::NilPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers less than one vote per share, is unrestricted from a sales / transfer perspective, is nil paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonRestrictedVotingUnrestrictedNilPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NilPaidShare"/>
    }

  item def <'common, restricted voting, unrestricted, partly paid, registered share'> CommonRestrictedVotingUnrestrictedPartlyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::RestrictedVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::PartiallyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers less than one vote per share, is unrestricted from a sales / transfer perspective, is partially paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonRestrictedVotingUnrestrictedPartlyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PartiallyPaidShare"/>
    }

  item def <'common, restricted voting, restricted, fully-paid, registered share'> CommonRestrictedVotingRestrictedFullyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::RestrictedVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::FullyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers less than one vote per share, is restricted from a sales / transfer perspective, is fully paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonRestrictedVotingRestrictedFullyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;FullyPaidShare"/>
    }

  item def <'common, restricted voting, restricted, nil paid, registered share'> CommonRestrictedVotingRestrictedNilPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::RestrictedVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::NilPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers less than one vote per share, is restricted from a sales / transfer perspective, is nil paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonRestrictedVotingRestrictedNilPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NilPaidShare"/>
    }

  item def <'common, restricted voting, restricted, partly paid, registered share'> CommonRestrictedVotingRestrictedPartlyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::RestrictedVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::PartiallyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers less than one vote per share, is restricted from a sales / transfer perspective, is partially paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonRestrictedVotingRestrictedPartlyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PartiallyPaidShare"/>
    }

  item def <'common, enhanced voting, unrestricted, fully-paid, registered share'> CommonEnhancedVotingUnrestrictedFullyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::EnhancedVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::FullyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers multiple votes per share, is not restricted from a sales / transfer perspective, is fully paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonEnhancedVotingUnrestrictedFullyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;EnhancedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;FullyPaidShare"/>
    }

  item def <'common, enhanced voting, unrestricted, nil paid, registered share'> CommonEnhancedVotingUnrestrictedNilPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::EnhancedVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::NilPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers multiple votes per share, is not restricted from a sales / transfer perspective, is nil paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonEnhancedVotingUnrestrictedNilPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;EnhancedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NilPaidShare"/>
    }

  item def <'common, enhanced voting, unrestricted, partly paid, registered share'> CommonEnhancedVotingUnrestrictedPartlyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::EnhancedVotingShare, Equity_Instruments::UnrestrictedShare, Equity_Instruments::PartiallyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers multiple votes per share, is not restricted from a sales / transfer perspective, is partially paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonEnhancedVotingUnrestrictedPartlyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;EnhancedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;UnrestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PartiallyPaidShare"/>
    }

  item def <'common, enhanced voting, restricted, fully-paid, registered share'> CommonEnhancedVotingRestrictedFullyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::EnhancedVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::FullyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers multiple votes per share, is restricted from a sales / transfer perspective, is fully paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonEnhancedVotingRestrictedFullyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;EnhancedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;FullyPaidShare"/>
    }

  item def <'common, enhanced voting, restricted, nil paid, registered share'> CommonEnhancedVotingRestrictedNilPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::EnhancedVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::NilPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers multiple votes per share, is restricted from a sales / transfer perspective, is nil paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonEnhancedVotingRestrictedNilPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;EnhancedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NilPaidShare"/>
    }

  item def <'common, enhanced voting, restricted, partly paid, registered share'> CommonEnhancedVotingRestrictedPartlyPaidRegisteredShare specializes Equity_Instruments::CommonShare, Equity_Instruments::EnhancedVotingShare, Equity_Instruments::RestrictedShare, Equity_Instruments::PartiallyPaidShare, Securities_Listings::RegisteredSecurity {
    	doc /* common share that confers multiple votes per share, is restricted from a sales / transfer perspective, is partially paid and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:CommonEnhancedVotingRestrictedPartlyPaidRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CommonShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;EnhancedVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;RestrictedShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PartiallyPaidShare"/>
    }

  item def <'preferred, voting, perpetual, fixed income, registered share'> PreferredVotingPerpetualFixedIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::PerpetualPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, has no fixed maturity date, provides a periodic stated income, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingPerpetualFixedIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PerpetualPreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
    }

  item def <'preferred, voting, perpetual, adjustable income, registered share'> PreferredVotingPerpetualAdjustableIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::PerpetualPreferredShare, Equity_Instruments::PreferredShareWithAdjustableRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, has no fixed maturity date, provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingPerpetualAdjustableIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PerpetualPreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAdjustableRateDividend"/>
    }

  item def <'preferred, voting, perpetual, auction rate income, registered share'> PreferredVotingPerpetualAuctionRateIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::PerpetualPreferredShare, Equity_Instruments::PreferredShareWithAuctionRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, has no fixed maturity date, provides a periodic income whose dividend rate is adjusted through an auction, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingPerpetualAuctionRateIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PerpetualPreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAuctionRateDividend"/>
    }

  item def <'preferred, voting, perpetual, normal income, registered share'> PreferredVotingPerpetualNormalIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::PerpetualPreferredShare, Equity_Instruments::NonCumulativePreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, has no fixed maturity date, provides the same dividends as common/ordinary shareholders, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingPerpetualNormalIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PerpetualPreferredShare"/>
    }

  item def <'preferred, voting, perpetual, cumulative, registered share'> PreferredVotingPerpetualCumulativeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::PerpetualPreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, has no fixed maturity date, provides a periodic stated income and dividends not paid in any year accumulate, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingPerpetualCumulativeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PerpetualPreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
    }

  item def <'preferred, voting, perpetual, cumulative participating, registered share'> PreferredVotingPerpetualCumulativeParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::PerpetualPreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, has no fixed maturity date, provides a periodic stated income, dividends not paid in any year accumulate, and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingPerpetualCumulativeParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PerpetualPreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, perpetual, participating, registered share'> PreferredVotingPerpetualParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::PerpetualPreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, has no fixed maturity date, provides a periodic stated income and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingPerpetualParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PerpetualPreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, redeemable, fixed income, registered share'> PreferredVotingRedeemableFixedIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic stated income, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableFixedIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
    }

  item def <'preferred, voting, redeemable, adjustable income, registered share'> PreferredVotingRedeemableAdjustableIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::PreferredShareWithAdjustableRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableAdjustableIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAdjustableRateDividend"/>
    }

  item def <'preferred, voting, redeemable, auction rate income, registered share'> PreferredVotingRedeemableAuctionRateIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::PreferredShareWithAuctionRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic income whose dividend rate is adjusted through an auction, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableAuctionRateIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAuctionRateDividend"/>
    }

  item def <'preferred, voting, redeemable, normal income, registered share'> PreferredVotingRedeemableNormalIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder, provides the same dividends as common/ordinary shareholders, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableNormalIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
    }

  item def <'preferred, voting, redeemable, cumulative, registered share'> PreferredVotingRedeemableCumulativeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic stated income and dividends not paid in any year accumulate, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableCumulativeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
    }

  item def <'preferred, voting, redeemable, cumulative participating, registered share'> PreferredVotingRedeemableCumulativeParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic stated income, dividends not paid in any year accumulate, and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableCumulativeParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, redeemable, participating, registered share'> PreferredVotingRedeemableParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic stated income and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, redeemable exchangeable, fixed income, registered share'> PreferredVotingRedeemableExchangeableFixedIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic stated income, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableFixedIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
    }

  item def <'preferred, voting, redeemable exchangeable, adjustable income, registered share'> PreferredVotingRedeemableExchangeableAdjustableIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::PreferredShareWithAdjustableRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableAdjustableIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAdjustableRateDividend"/>
    }

  item def <'preferred, voting, redeemable exchangeable, auction rate income, registered share'> PreferredVotingRedeemableExchangeableAuctionRateIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::PreferredShareWithAuctionRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic income whose dividend rate is adjusted through an auction, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableAuctionRateIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAuctionRateDividend"/>
    }

  item def <'preferred, voting, redeemable exchangeable, normal income, registered share'> PreferredVotingRedeemableExchangeableNormalIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, may be redeemed at the option of the issuer and/or of the shareholder, provides the same dividends as common/ordinary shareholders, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableNormalIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
    }

  item def <'preferred, voting, redeemable exchangeable, cumulative, registered share'> PreferredVotingRedeemableExchangeableCumulativeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic stated income and dividends not paid in any year accumulate, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableCumulativeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
    }

  item def <'preferred, voting, redeemable exchangeable, cumulative participating, registered share'> PreferredVotingRedeemableExchangeableCumulativeParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic stated income, dividends not paid in any year accumulate, and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableCumulativeParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, redeemable exchangeable, participating, registered share'> PreferredVotingRedeemableExchangeableParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one per share, may be exchanged for securities of another issuer, may be redeemed at the option of the issuer and/or of the shareholder, provides a periodic stated income and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, redeemable/exchangeable/extendable, fixed income, registered share'> PreferredVotingRedeemableExchangeableExtendableFixedIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic stated income, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableExtendableFixedIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
    }

  item def <'preferred, voting, redeemable/exchangeable/extendable, adjustable income, registered share'> PreferredVotingRedeemableExchangeableExtendableAdjustableIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::PreferredShareWithAdjustableRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableExtendableAdjustableIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAdjustableRateDividend"/>
    }

  item def <'preferred, voting, redeemable/exchangeable/extendable, auction rate income, registered share'> PreferredVotingRedeemableExchangeableExtendableAuctionRateIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::PreferredShareWithAuctionRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic income whose dividend rate is adjusted through an auction, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableExtendableAuctionRateIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAuctionRateDividend"/>
    }

  item def <'preferred, voting, redeemable/exchangeable/extendable, normal income, registered share'> PreferredVotingRedeemableExchangeableExtendableNormalIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::NonCumulativePreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, whose issuer and/or shareholders have the option to extend the maturity date, that provides the same dividends as common/ordinary shareholders, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableExtendableNormalIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
    }

  item def <'preferred, voting, redeemable/exchangeable/extendable, cumulative, registered share'> PreferredVotingRedeemableExchangeableExtendableCumulativeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic stated income and dividends not paid in any year accumulate, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableExtendableCumulativeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
    }

  item def <'preferred, voting, redeemable/exchangeable/extendable, cumulative participating, registered share'> PreferredVotingRedeemableExchangeableExtendableCumulativeParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic stated income, dividends not paid in any year accumulate, and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableExtendableCumulativeParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, redeemable/exchangeable/extendable, participating, registered share'> PreferredVotingRedeemableExchangeableExtendableParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic stated income and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExchangeableExtendableParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, redeemable extendable, fixed income, registered share'> PreferredVotingRedeemableExtendableFixedIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic stated income, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExtendableFixedIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
    }

  item def <'preferred, voting, redeemable extendable, adjustable income, registered share'> PreferredVotingRedeemableExtendableAdjustableIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::PreferredShareWithAdjustableRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExtendableAdjustableIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAdjustableRateDividend"/>
    }

  item def <'preferred, voting, redeemable extendable, auction rate income, registered share'> PreferredVotingRedeemableExtendableAuctionRateIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::PreferredShareWithAuctionRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic income whose dividend rate is adjusted through an auction, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExtendableAuctionRateIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAuctionRateDividend"/>
    }

  item def <'preferred, voting, redeemable extendable, normal income, registered share'> PreferredVotingRedeemableExtendableNormalIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, whose issuer and/or shareholders have the option to extend the maturity date, that provides the same dividends as common/ordinary shareholders, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExtendableNormalIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
    }

  item def <'preferred, voting, redeemable extendable, cumulative, registered share'> PreferredVotingRedeemableExtendableCumulativeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic stated income and dividends not paid in any year accumulate, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExtendableCumulativeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
    }

  item def <'preferred, voting, redeemable extendable, cumulative participating, registered share'> PreferredVotingRedeemableExtendableCumulativeParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic stated income, dividends not paid in any year accumulate, and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExtendableCumulativeParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, redeemable extendable, participating, registered share'> PreferredVotingRedeemableExtendableParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, whose issuer and/or shareholders have the option to extend the maturity date, that provides a periodic stated income and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingRedeemableExtendableParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, exchangeable, fixed income, registered share'> PreferredVotingExchangeableFixedIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, provides a periodic stated income, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExchangeableFixedIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
    }

  item def <'preferred, voting, exchangeable, adjustable income, registered share'> PreferredVotingExchangeableAdjustableIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::PreferredShareWithAdjustableRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExchangeableAdjustableIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAdjustableRateDividend"/>
    }

  item def <'preferred, voting, exchangeable, auction rate income, registered share'> PreferredVotingExchangeableAuctionRateIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::PreferredShareWithAuctionRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, provides a periodic income whose dividend rate is adjusted through an auction, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExchangeableAuctionRateIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAuctionRateDividend"/>
    }

  item def <'preferred, voting, exchangeable, normal income, registered share'> PreferredVotingExchangeableNormalIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::NonCumulativePreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, provides the same dividends as common/ordinary shareholders, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExchangeableNormalIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
    }

  item def <'preferred, voting, exchangeable, cumulative, registered share'> PreferredVotingExchangeableCumulativeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, provides a periodic stated income and dividends not paid in any year accumulate, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExchangeableCumulativeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
    }

  item def <'preferred, voting, exchangeable, cumulative participating, registered share'> PreferredVotingExchangeableCumulativeParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, provides a periodic stated income, dividends not paid in any year accumulate, and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExchangeableCumulativeParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, exchangeable, participating, registered share'> PreferredVotingExchangeableParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExchangeablePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be exchanged for securities of another issuer, provides a periodic stated income and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExchangeableParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExchangeablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, extendable, fixed income, registered share'> PreferredVotingExtendableFixedIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder with an extendable redemption date, provides a periodic stated income, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExtendableFixedIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
    }

  item def <'preferred, voting, extendable, adjustable income, registered share'> PreferredVotingExtendableAdjustableIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::PreferredShareWithAdjustableRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder with an extendable redemption date, provides a periodic income whose dividend rate is set periodically, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExtendableAdjustableIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAdjustableRateDividend"/>
    }

  item def <'preferred, voting, extendable, auction rate income, registered share'> PreferredVotingExtendableAuctionRateIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::PreferredShareWithAuctionRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder with an extendable redemption date, provides a periodic income whose dividend rate is adjusted through an auction, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExtendableAuctionRateIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithAuctionRateDividend"/>
    }

  item def <'preferred, voting, extendable, normal income, registered share'> PreferredVotingExtendableNormalIncomeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::NonCumulativePreferredShare, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder with an extendable redemption date, provides the same dividends as common/ordinary shareholders, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExtendableNormalIncomeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
    }

  item def <'preferred, voting, extendable, cumulative, registered share'> PreferredVotingExtendableCumulativeRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::NonParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder with an extendable redemption date, provides a periodic stated income and dividends not paid in any year accumulate, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExtendableCumulativeRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonParticipatingPreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
    }

  item def <'preferred, voting, extendable, cumulative participating, registered share'> PreferredVotingExtendableCumulativeParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::CumulativePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder with an extendable redemption date, provides a periodic stated income, dividends not paid in any year accumulate, and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExtendableCumulativeParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;CumulativePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

  item def <'preferred, voting, extendable, participating, registered share'> PreferredVotingExtendableParticipatingRegisteredShare specializes Equity_Instruments::SingleVotingShare, Equity_Instruments::ExtendablePreferredShare, Equity_Instruments::ParticipatingPreferredShare, Equity_Instruments::NonCumulativePreferredShare, Equity_Instruments::PreferredShareWithFixedRateDividend, Securities_Listings::RegisteredSecurity {
    	doc /* preferred share that confers exactly one vote per share, may be redeemed at the option of the issuer and/or of the shareholder with an extendable redemption date, provides a periodic stated income and participates with common shareholders in further dividend and capital distributions, and is registered */
    	// skos:definition xml:lang="en" (CFIClassification.rdf)
    	// rdfs:label xml:lang="en" (CFIClassification.rdf)
    	// RDF: fibo-sec-eq-cfi:PreferredVotingExtendableParticipatingRegisteredShare
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;NonCumulativePreferredShare"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;PreferredShareWithFixedRateDividend"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-lst;RegisteredSecurity"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;SingleVotingShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ExtendablePreferredShare"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-eq-eq;ParticipatingPreferredShare"/>
    }

}

// --- fibo-sec-eq-dr ---
