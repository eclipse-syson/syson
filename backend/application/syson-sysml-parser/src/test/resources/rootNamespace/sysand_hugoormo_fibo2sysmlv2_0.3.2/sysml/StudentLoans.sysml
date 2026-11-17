// ===================================================================
// StudentLoans.sysml
// Source-parity extracted from: FiBO_LOAN_LoanSpecific.sysml
// Primary source module: StudentLoans.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/LoansSpecific
// ===================================================================
package Student_Loans {
  doc/* A loan or series of loans made for the purposes of study at some institution of learning. This ontology and much of the common supporting information on loan applications are based on extensive review and input from Sallie Mae in the US and there may be other variants of student loans that are not covered here. For example in principle a student loan may be framed as a credit facility in some arrangements and as a single loan with separate payment phases in others. (source: dct:abstract, StudentLoans.rdf) */
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

  item def <'private student loan'> PrivateStudentLoan specializes StudentLoan, RegulatedStudentLoan {
  	doc /* student loan that is not considered a government-backed / regulated loan */
  	// skos:definition xml:lang="en" (StudentLoans.rdf)
  	// rdfs:label xml:lang="en" (StudentLoans.rdf)
  	// RDF: fibo-loan-ln-edu:PrivateStudentLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-stu;RegulatedStudentLoan"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-stu;StudentLoan"/>
  }
  item def <'regulated student loan'> RegulatedStudentLoan specializes StudentLoan {
  	doc /* student loan (1) whose lender is a government agency or instrumentality, and/or (2) that is treated uniquely due to tax regulations */
  	// skos:definition xml:lang="en" (StudentLoans.rdf)
  	// rdfs:label xml:lang="en" (StudentLoans.rdf)
  	// RDF: fibo-loan-ln-edu:RegulatedStudentLoan
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-stu;StudentLoan"/>
  }
  item def <'student loan'> StudentLoan specializes Loans::Loan {
  	doc /* loan provided for the purposes of education, allowing students and parents/guardians to borrow money for college */
  	// skos:definition xml:lang="en" (StudentLoans.rdf)
  	// rdfs:label xml:lang="en" (StudentLoans.rdf)
  	// RDF: fibo-loan-ln-edu:StudentLoan
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;Loan"/>
  }
}

// --- fibo-loan-ln-grn ---
