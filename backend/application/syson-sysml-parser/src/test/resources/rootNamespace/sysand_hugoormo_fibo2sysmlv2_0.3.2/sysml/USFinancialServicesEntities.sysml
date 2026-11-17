// ===================================================================
// FiBOBECorporationTypesUS.sysml
// Source-parity extracted from: FiBO_BE_CorporationTypes_US.sysml
// Primary source module: USFinancialServicesEntities.rdf (for current translated content)
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/CorporationTypes
// ===================================================================
package FiBO_BE_CorporationTypes_US {
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

 doc /*
	 * FiBO Business Entities - US Corporation Types
	 *
	 * OWL source: fibo/fbc/functionalentities/usfinancialservicesentities
	 * Structural note: retained in BE for translation compatibility during phased domain reconciliation.
	 * This module contains 11 registered classification items.
	 */
	/**
	 * 
	 */
	item def <'international bank of US depositary, edge, trust company'> InternationalBankOfUSDepositoryEdgeTrustCompany specializes Financial_Services_Entities::Bank {
			doc /* bank in the United States that is engaged in international banking and financial operations */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:InternationalBankOfUSDepositoryEdgeTrustCompany
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;Bank"/>
		}

	/**
	 * 
	 */
	item def <'foreign banking organization as a bank holding company'> ForeignBankingOrganizationAsABankHoldingCompany specializes Financial_Services_Entities::BankHoldingCompany {
			doc /* foreign banking organization that is a bank holding company in the United States */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:ForeignBankingOrganizationAsABankHoldingCompany
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;BankHoldingCompany"/>
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;ForeignBankingOrganization"/>
		}

	/**
	 * 
	 */
	item def 'EdgeCorporation-DomesticBranch' specializes Legal_Persons::Branch {
			doc /* branch of an edge corporation that is located in the United States */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:EdgeCorporation-DomesticBranch
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-fbo;Branch"/>
		}

	/**
	 * 
	 */
	item def 'AgreementCorporation-Banking' specializes Financial_Services_Entities::DepositoryInstitution {
			doc /* agreement corporation whose charter allows the corporation to engage in international banking */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:AgreementCorporation-Banking
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;DepositoryInstitution"/>
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;AgreementCorporation"/>
		}

	/**
	 * 
	 */
	item def 'EdgeCorporation-Banking' specializes Financial_Services_Entities::DepositoryInstitution {
			doc /* edge corporation whose charter allows the corporation to engage in international banking */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:EdgeCorporation-Banking
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;DepositoryInstitution"/>
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;EdgeCorporation"/>
		}

	/**
	 * 
	 */
	item def <'foreign banking organization of a bank holding company'> ForeignBankingOrganizationOfABankHoldingCompany specializes US_National_Bank_Institutions::ForeignBankingOrganization {
			doc /* foreign banking organization that is owned or controlled by a bank holding company */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:ForeignBankingOrganizationOfABankHoldingCompany
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;ForeignBankingOrganization"/>
		}

	/**
	 * 
	 */
	item def 'AgreementCorporation-Investment' specializes Financial_Services_Entities::InvestmentBank {
			doc /* agreement corporation whose charter allows the corporation to engage in international investment banking */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:AgreementCorporation-Investment
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;InvestmentBank"/>
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;AgreementCorporation"/>
		}

	/**
	 * 
	 */
	item def 'EdgeCorporation-Investment' specializes Financial_Services_Entities::InvestmentBank {
			doc /* edge corporation whose charter allows the corporation to engage in international investment banking */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:EdgeCorporation-Investment
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;InvestmentBank"/>
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;EdgeCorporation"/>
		}

	/**
	 * 
	 */
	item def <'commercial lending company'> CommercialLendingCompany specializes Financial_Services_Entities::NonDepositoryInstitution {
			doc /* any institution, other than a bank or an organization operating under section 25 of the Federal Reserve Act, engaged in the business of making commercial loans */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:CommercialLendingCompany
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
		}

	/**
	 * 
	 */
	item def <'New York Article XII investment company'> NewYorkArticleXIIInvestmentCompany specializes Financial_Services_Entities::NonDepositoryInstitution {
			doc /* specialized non-depository lending institution that has broad borrowing and lending powers and may invest in stocks and other securities */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:NewYorkArticleXIIInvestmentCompany
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
		}

	/**
	 * 
	 */
	item def <'non-depository trust company'> NonDepositoryTrustCompany specializes Financial_Services_Entities::NonDepositoryInstitution {
			doc /* trust company that accepts and executes trusts, but does not issue currency; non-depository trust companies can either be members or non-members of the Federal Reserve System */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:NonDepositoryTrustCompany
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;TrustCompany"/>
		}
	
	item def <'domestic branch of domestic bank'> DomesticBranchOfDomesticBank specializes US_National_Bank_Institutions::BranchOfADepositoryInstitution {
			doc /* branch that resides in the United States, and whose parent is also located in the United States */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:DomesticBranchOfDomesticBank
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;BranchOfADepositoryInstitution"/>
		}

	/**
	 * 
	 */
	item def <'foreign branch of foreign bank'> ForeignBranchOfForeignBank specializes US_National_Bank_Institutions::BranchOfADepositoryInstitution {
			doc /* branch that resides outside of the United States whose parent is located outside of the United States */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:ForeignBranchOfForeignBank
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;BranchOfADepositoryInstitution"/>
		}

	/**
	 * 
	 */
	item def <'foreign branch of US bank'> ForeignBranchOfUSBank specializes US_National_Bank_Institutions::BranchOfADepositoryInstitution {
			doc /* branch that resides outside of the United States, but has a parent that is located in the United States */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:ForeignBranchOfUSBank
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;BranchOfADepositoryInstitution"/>
		}

	/**
	 * 
	 */
	item def <'federal branch of foreign banking organization'> FederalBranchOfForeignBankingOrganization specializes US_National_Bank_Institutions::DomesticBranchOfAForeignBank {
			doc /* branch of a foreign bank established and operating under section 3102 of CFR Title 12, Chapter 32 and section 4 of the International Banking Act of 1978 */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:FederalBranchOfForeignBankingOrganization
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;DomesticBranchOfAForeignBank"/>
		}

	/**
	 * 
	 */
	item def <'limited branch of a foreign bank'> LimitedBranchOfAForeignBank specializes US_National_Bank_Institutions::DomesticBranchOfAForeignBank {
			doc /* branch of a foreign bank that receives only such deposits as would be permitted for a corporation organized under section 25A of the Federal Reserve Act (12 U.S.C. 611-631) */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:LimitedBranchOfAForeignBank
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;DomesticBranchOfAForeignBank"/>
		}

	/**
	 * 
	 */
	item def <'state branch of foreign banking organization'> StateBranchOfForeignBankingOrganization specializes US_National_Bank_Institutions::DomesticBranchOfAForeignBank {
			doc /* branch of a foreign bank located in any State of the United States at which deposits are received, established and operating under the laws of that State, where state means any State of the United States or the District of Columbia */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:StateBranchOfForeignBankingOrganization
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;DomesticBranchOfAForeignBank"/>
		}

	/**
	 * 
	 */
	item def <'office of a foreign bank'> OfficeOfAForeignBank specializes Facilities::Facility {
			doc /* facility that is any branch, agency, representative office, or commercial lending company subsidiary of a foreign bank in the United States */
			// skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
			// rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
			// RDF: fibo-fbc-fct-usfse:OfficeOfAForeignBank
			// OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-sfc;Facility"/>
		}


	// RDF: fibo-be-agents:BankersBankNotSubjectToReserveRequirements
	item def <
			'bankers&apos; bank that is not subject to reserve requirements'
		> BankersBankNotSubjectToReserveRequirements
		specializes US_National_Bank_Institutions::BankersBank {
	    doc /* bankers' bank whose activities are not limited by specific requirements of the Federal Reserve */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}
	
	/* * */
	// RDF: fibo-be-agents:BankersBankSubjectToReserveRequirements
	item def <
			'bankers&apos; bank subject to reserve requirements'
		> BankersBankSubjectToReserveRequirements
		specializes US_National_Bank_Institutions::BankersBank {
	    doc /* bankers' bank whose activities are limited by specific requirements of the Federal Reserve */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}
	
	
	// RDF: fibo-be-agents:DomesticNonBankSubsidiaryOfADomesticEntity
	item def <
			'domestic non-bank subsidiary of a domestic entity'
		> DomesticNonBankSubsidiaryOfADomesticEntity
		specializes US_National_Bank_Institutions::DomesticEntity {
	    doc /* non-bank financial service provider based in the United States of which 25 percent or more of whose voting shares are owned or controlled by an entity that is also based in the United States, or of which a majority of its directors are controlled by such domestic entity, or of which 25 percent or more of whose voting shares are held by trustees for the benefit of the shareholders or members of such domestic entity */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}
	
	
	// RDF: fibo-be-agents:InsuredFederalBranchOfForeignBankingOrganization
	item def <
			'insured federal branch of foreign banking organization'
		> InsuredFederalBranchOfForeignBankingOrganization
		specializes FederalBranchOfForeignBankingOrganization {
	    doc /* any office or any place of business of a foreign bank located in any State of the United States at which deposits are received established and operating under section 4 of the International Banking Act of 1978 that is insured and regulated by the Federal Deposit Insurance Corporation (FDIC) */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}
	
	/* * */
	// RDF: fibo-be-agents:UninsuredFederalBranchOfForeignBankingOrganization
	item def <
			'uninsured federal branch of foreign banking organization'
		> UninsuredFederalBranchOfForeignBankingOrganization
		specializes FederalBranchOfForeignBankingOrganization {
	    doc /* any office or any place of business of a foreign bank located in any State of the United States at which deposits are received established and operating under section 4 of the International Banking Act of 1978 that is not insured or regulated by the Federal Deposit Insurance Corporation (FDIC) */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}
	

	// RDF: fibo-be-agents:ForeignBranchOfForeignBankManagedByUSOffice
	item def <
			'foreign branch of foreign bank managed by US office'
		> ForeignBranchOfForeignBankManagedByUSOffice
		specializes ForeignBranchOfForeignBank {
	    doc /* branch that resides outside of the United States whose parent is located outside of the United States that is managed by a US branch or agency of the foreign bank */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}
	
	/* * */
	// RDF: fibo-be-agents:InternationalNonBankSubsidiaryOfADomesticEntity
	item def <
			'international non-bank subsidiary of a domestic entity'
		> InternationalNonBankSubsidiaryOfADomesticEntity
		specializes NonBankSubsidiaryOfADomesticEntity {
	    doc /* non-bank financial service provider based outside of the United States of which 25 percent or more of whose voting shares are owned or controlled by an entity that is based in the United States, or of which a majority of its directors are controlled by such domestic entity, or of which 25 percent or more of whose voting shares are held by trustees for the benefit of the shareholders or members of such domestic entity */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}
	
	/* * */
	// RDF: fibo-be-agents:RepresentativeOfficeOfAForeignBank
	item def <'representative office of a foreign bank'> RepresentativeOfficeOfAForeignBank
		specializes OfficeOfAForeignBank {
	    doc /* office of a foreign bank which is located in any state and is not a federal branch, federal agency, state branch, state agency, or commercial lending company subsidiary */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}
	
	/* * */
	// RDF: fibo-be-agents:RegionalAdministrativeOfficeOfAForeignBank
	item def <
			'regional administrative office of a foreign bank'
		> RegionalAdministrativeOfficeOfAForeignBank
		specializes RepresentativeOfficeOfAForeignBank {
	    doc /* representative office that (1) Is established by a foreign bank that operates two or more branches, agencies, commercial lending companies, or banks in the United States; (2) Is located in the same city as one or more of the foreign bank's branches, agencies, commercial lending companies, or banks in the United States; (3) Manages, supervises, or coordinates the operations of the foreign bank or its affiliates, if any, in a particular geographic area that includes the United States or a region thereof, including by exercising credit approval authority in that area pursuant to written standards, credit policies, and procedures established by the foreign bank; and (4) Does not solicit business from actual or potential customers of the foreign bank or its affiliates. */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}
	
	// RDF: fibo-be-agents:InsuredStateBranchOfForeignBankingOrganization
	item def <
			'insured state branch of foreign banking organization'
		> InsuredStateBranchOfForeignBankingOrganization
		specializes StateBranchOfForeignBankingOrganization {
	    doc /* any office or any place of business of a foreign bank located in any State of the United States at which deposits are received, established and operating under the laws of that State that is insured and regulated by the Federal Deposit Insurance Corporation (FDIC) */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}
	
	/* * */
	// RDF: fibo-be-agents:UninsuredStateBranchOfForeignBankingOrganization
	item def <
			'uninsured state branch of foreign banking organization'
		> UninsuredStateBranchOfForeignBankingOrganization
		specializes StateBranchOfForeignBankingOrganization {
	    doc /* any office or any place of business of a foreign bank located in any State of the United States at which deposits are received, established and operating under the laws of that State that is not insured or regulated by the Federal Deposit Insurance Corporation (FDIC) */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}
	
	/* * */
	// RDF: fibo-be-agents:NonBankSubsidiaryOfADomesticEntity
	item def <'non-bank subsidiary of a domestic entity'> NonBankSubsidiaryOfADomesticEntity
		specializes FiBO_FND_ExternalStubs::OrganizationalSubUnit {
	    doc /* non-bank entity of which 25 percent or more of whose voting shares are owned or controlled by an entity that is based in the United States, or of which a majority of its directors are controlled by such domestic entity, or of which 25 percent or more of whose voting shares are held by trustees for the benefit of the shareholders or members of such domestic entity */
	    // skos:definition xml:lang="en" (USFinancialServicesEntities.rdf)
	    // rdfs:label xml:lang="en" (USFinancialServicesEntities.rdf)
	}

}