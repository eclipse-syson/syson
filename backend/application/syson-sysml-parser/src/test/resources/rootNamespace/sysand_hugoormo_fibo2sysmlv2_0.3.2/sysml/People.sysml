// ===================================================================
// People.sysml
// Source-parity extracted from: FIBO_FND_AgentsAndPeople.sysml
// Primary source module: People.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/AgentsAndPeople
// ===================================================================

package People {
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
  doc/* This ontology defines concepts for people and human related terms, for use in other FIBO ontology elements. People as defined here are human persons only. (source: dct:abstract, People.rdf) */

  item def <'person'> Person {
			doc /* individual human being, with consciousness of self */
			// skos:definition xml:lang="en" (People.rdf)
			// rdfs:label xml:lang="en" (People.rdf)
			// RDF: fibo-fnd-aap-ppl:Person
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Party"/>
		}

  item def <'adult'> Adult specializes Person {
      doc /* person who has attained the age of majority as defined in some jurisdiction */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:Adult
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;Person"/>
    }

  item def <'minor'> Minor specializes Person {
      doc /* a person under a certain age, usually the age of majority in a given jurisdiction, which legally demarcates childhood from adulthood */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:Minor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;Person"/>
    }

  item def <'emancipated minor'> EmancipatedMinor specializes Minor {
      doc /* a minor who is allowed to conduct a business or any other occupation on his or her own behalf or for their own account outside the control of a parent or guardian */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:EmancipatedMinor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;Minor"/>
    }

  item def <'legally capable adult'> LegallyCapableAdult specializes Adult {
      doc /* person who has attained the age of majority as defined in some jurisdiction and who is allowed to conduct a business or any other occupation on his or her own behalf or for their own account */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:LegallyCapableAdult
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;Adult"/>
    }

  item def <'incapacitated adult'> IncapacitatedAdult specializes Adult {
      doc /* an adult who is legally identified as not having legal capacity, typically as a result of some inherent physical or mental incapacity or as a result of having contracted some illness which temporarily deprives them of such capacity */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:IncapacitatedAdult
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;Adult"/>
      // OWL-disjointWith: <owl:disjointWith rdf:resource="&fibo-fnd-aap-ppl;LegallyCapableAdult"/>
    }

  item def <'identity document'> IdentityDocument {
      doc /* any legal document which may be used to verify aspects of a person's identity */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:IdentityDocument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;LegalDocument"/>
    }

  item def <'passport'> Passport specializes IdentityDocument {
      doc /* formal identity document, issued by a national government, which certifies the identity and nationality of its holder for the purpose of international travel */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:Passport
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;IdentityDocument"/>
    }

  item def <'legal age'> LegalAge {
      doc /* age at which someone acquires the capacity to do something that they were prohibited from doing before under the law in some jurisdiction */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:LegalAge
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;Age"/>
    }

  item def <'legal working age person'> LegalWorkingAgePerson specializes Person {
			doc /* person whose age is greater than the minimum legal working age specified in a jurisdiction in which they work */
			// skos:definition xml:lang="en" (People.rdf)
			// rdfs:label xml:lang="en" (People.rdf)
			// RDF: fibo-fnd-aap-ppl:LegalWorkingAgePerson
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;Person"/>
		}

    item def <'age of majority'> AgeOfMajority specializes LegalAge {
      doc /* age at which someone acquires the rights and responsibilities of an adult in some jurisdiction */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:AgeOfMajority
      // Source: People.rdf
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;LegalAge"/>
    }
    item def <'birth certificate'> BirthCertificate {
      doc /* an original document certifying the circumstances of the birth, or a certified copy of or representation of the ensuing registration of that birth */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:BirthCertificate
      // Source: People.rdf
    }
    item def <'birth certificate identifier'> BirthCertificateIdentifier {
      doc /* identifier associated with a vital record documenting the birth of a child */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:BirthCertificateIdentifier
      // Source: People.rdf
    }
    item def <'birth certificate identification scheme'> BirthCertificateIdentificationScheme {
      doc /* system for allocating identifiers to birth certificates */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:BirthCertificateIdentificationScheme
      // Source: People.rdf
    }
    item def <'contact'> Contact {
      doc /* role or associated with a party serving as a designated point of communication, typically within a system or process */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:Contact
      // Source: People.rdf
    }
    item def <'contact record'> ContactRecord {
      doc /* record about a party in a specific communicative or liaison role */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:ContactRecord
      // Source: People.rdf
    }
    item def <'date of birth'> DateOfBirth {
      doc /* explicit date, i.e., the day, month and year, on which an individual was born */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:DateOfBirth
      // Source: People.rdf
    }
    item def <'date of death'> DateOfDeath {
      doc /* explicit date, i.e., the day, month and year, on which an individual died */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:DateOfDeath
      // Source: People.rdf
    }
    item def <'death certificate'> DeathCertificate {
      doc /* original document certifying the circumstances of the death (such as how and when it occurred), or a certified copy of or representation of the ensuing registration of that death */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:DeathCertificate
      // Source: People.rdf
    }
    item def <'death certificate identifier'> DeathCertificateIdentifier {
      doc /* identifier associated with a vital record documenting the death of an individual */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:DeathCertificateIdentifier
      // Source: People.rdf
    }
    item def <'death certificate identification scheme'> DeathCertificateIdentificationScheme {
      doc /* system for allocating identifiers to death certificates */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:DeathCertificateIdentificationScheme
      // Source: People.rdf
    }
    item def <'drivers license'> DriversLicense {
      doc /* an official document which states that a person may operate a motorized vehicle, such as a motorcycle, car, truck or a bus, on a public roadway or provides official identifying information for a non-driver */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:DriversLicense
      // Source: People.rdf
    }
    item def <'drivers license identifier'> DriversLicenseIdentifier {
      doc /* identifier associated with a drivers' or operating license for operating a motor vehicle or non-driver identification card */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:DriversLicenseIdentifier
      // Source: People.rdf
    }
    item def <'drivers license identification scheme'> DriversLicenseIdentificationScheme {
      doc /* system for allocating identifiers to driver's, operating, or non-driver identification documents */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:DriversLicenseIdentificationScheme
      // Source: People.rdf
    }
    item def <'legal working age'> LegalWorkingAge specializes LegalAge {
      doc /* age at which someone acquires the capacity to work legally in some jurisdiction */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:LegalWorkingAge
      // Source: People.rdf
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;LegalAge"/>
    }
    item def <'national identification number'> NationalIdentificationNumber {
      doc /* number or text which appears on an identity document issued by a country or jurisdiction */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:NationalIdentificationNumber
      // Source: People.rdf
    }
    item def <'national identification number scheme'> NationalIdentificationNumberScheme {
      doc /* system for allocating identifiers to national identification numbers */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:NationalIdentificationNumberScheme
      // Source: People.rdf
    }
    item def <'passport number'> PassportNumber specializes NationalIdentificationNumber {
      doc /* national identification number associated with a passport */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:PassportNumber
      // Source: People.rdf
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-ppl;NationalIdentificationNumber"/>
    }
    item def <'passport number identification scheme'> PassportNumberIdentificationScheme {
      doc /* system for allocating identifiers to passports */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:PassportNumberIdentificationScheme
      // Source: People.rdf
    }
    item def <'person name'> PersonName {
      doc /* designation by which someone is known in some context */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:PersonName
      // Source: People.rdf
    }
    item def <'place of birth'> PlaceOfBirth {
      doc /* physical location, including country, region, and municipality where an individual was born */
      // skos:definition xml:lang="en" (People.rdf)
      // rdfs:label xml:lang="en" (People.rdf)
      // RDF: fibo-fnd-aap-ppl:PlaceOfBirth
      // Source: People.rdf
    }
}
