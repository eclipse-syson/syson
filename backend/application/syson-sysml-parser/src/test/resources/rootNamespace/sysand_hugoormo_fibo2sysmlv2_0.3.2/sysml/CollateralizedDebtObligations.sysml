// ===================================================================
// CollateralizedDebtObligations.sysml
// Source-parity extracted from: FiBO_SEC_Debt.sysml
// Primary source module: CollateralizedDebtObligations.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Debt
// ===================================================================
package Collateralized_Debt_Obligations {
  doc/* Collateralized debt obligations are tranched debt instruments based on pools of debt instruments, and those pools may have different management styles and objectives. Generally includes an equity tranche. This ontology also covers CDO squared. (source: dct:abstract, CollateralizedDebtObligations.rdf) */
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

// StructuredFinanceInstrument, Tranche
// DebtPool
// PartyRole

  item def <'collateralized debt obligation'> CollateralizedDebtObligation specializes Structured_Instruments::StructuredFinanceInstrument {
  	doc /* structured finance constructed from a portfolio of fixed income assets including corporate loans and mortgage backed securities. A special purpose vehicle (SPV) issues notes to investors in order to raise funds that are invested in a portfolio of those fixed income assets, held by the SPV as collateral for the notes. Further notes: Collateralized Debt Obligation, for example, ABS CDO which consists of a portfolio of different ABS bonds, and the payments to the holders of these trust certificates are derived from the cash flows of the ABS bonds. This CDO instrument is part of a CDO issue, consisting of individual CDO instruments of a given seniority. Often referred to as tranches and slices (Investopedia). Investopedia: Similar in structure to a collateralized mortgage obligation (CMO) or collateralized bond obligation (CBO), CDOs are unique in that they represent different types of debt and credit risk. In the case of CDOs, these different types of debt are often referred to as &apos;tranches&apos; or &apos;slices&apos;. Each slice has a different maturity and risk associated with it. The higher the risk, the more the CDO pays. Further details: Collateralized Debt obligations are securitized interests in pools of - generally non-mortgage - assets. Assets - called collateral - usually comprise loans or debt instruments. A CDO may be called a collateralized loan obligation (CLO) or collateralized bond obligation (CBO) if it holds only loans or bonds, respectively. Investors bear the credit risk of the collateral. Multiple tranches of securities are issued by the CDO, offering investors various maturity and credit risk characteristics. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CollateralizedDebtObligation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;StructuredFinanceInstrument"/>
  }
  item def <'asset-backed security'> AssetBackedSecurity specializes Pool_backed_Securities::PoolBackedSecurity {
  	doc /* debt instrument backed by receivables other than those arising out of real estate loans or mortgages */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:AssetBackedSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;PoolBackedSecurity"/>
  }
  item def <'a b s c d o deal'> ABSCDODeal specializes CDODeal {
  	doc /* An issue of Asset Backed Security CDO notes. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:ABSCDODeal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDODeal"/>
  }
  item def <'a b s c d o instrument'> ABSCDOInstrument specializes CashCDOTranche {
  	doc /* CDO where the underlying asset pool is ABS. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:ABSCDOInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CashCDOTranche"/>
  }
  item def <'agency c m o'> AgencyCMO specializes Debt_Issuance::DebtOffering {
  	doc /* A tranche where if there is some sort of trigger event reached then the holders of the tranche will begin to receive payments. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:AgencyCMO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;DebtOffering"/>
  }
  item def <'agency z tranche'> AgencyZTranche specializes AgencyCMO {
  	doc /* A tranche that does not receive payments while other tranches remain. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:AgencyZTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyCMO"/>
  }
  item def <'agency jump z tranche'> AgencyJumpZTranche specializes AgencyZTranche, AgencyJumpTranche {
  	doc /* A Jump Z tranche is like a Z tranche but if there is some sort of trigger event reached then the holders of the Jump Z tranche will begin to receive payments. Regular non-Sticky Jump Z tranches maintain their changed status only while the trigger event is in effect, and revert to their old payment status once the trigger event has passed. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:AgencyJumpZTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyJumpTranche"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyZTranche"/>
  }
  item def <'agency regular jump z'> AgencyRegularJumpZ specializes AgencyJumpZTranche {
  	doc /* Regular non-Sticky Jump Z tranches maintain their changed status only while the trigger event is in effect, and revert to their old payment status once the trigger event has passed. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:AgencyRegularJumpZ
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyJumpZTranche"/>
  }
  item def <'agency sticky jump z'> AgencyStickyJumpZ specializes AgencyJumpZTranche {
  	doc /* &quot;Sticky&quot; Jump Z tranches maintain the payment priority of a Jump Z tranche until they are retired. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:AgencyStickyJumpZ
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyJumpZTranche"/>
  }
  item def <'agency i o tranche'> AgencyIOTranche specializes AgencyCMO {
  	doc /* A tranche where if there is some sort of trigger event reached then the holders of the tranche will begin to receive payments. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:AgencyIOTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyCMO"/>
  }
  item def <'agency jump tranche'> AgencyJumpTranche specializes AgencyCMO {
  	doc /* A tranche where if there is some sort of trigger event reached then the holders of the tranche will begin to receive payments. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:AgencyJumpTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyCMO"/>
  }
  item def <'agency p o tranche'> AgencyPOTranche specializes AgencyCMO {
  	doc /* Principal Only tranche. This tranche will only pay principal. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:AgencyPOTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyCMO"/>
  }
  item def <'arbitrage c d o'> ArbitrageCDO specializes CDODeal {
  	doc /* Arbitrage CDO/CLO/CBO - the reference assets are bought by a firm or conduit or SPV (Special Purpose Vehicle) with a view to repackage them and sell them on as the structured product. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:ArbitrageCDO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDODeal"/>
  }
  item def <'arbitrage cdo objective'> ArbitrageCdoObjective specializes CDOOriginationObjective {
  	doc /* The objective is arbitrage, whereby the motivation is to add value by repackaging assets into tranches. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:ArbitrageCdoObjective
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDOOriginationObjective"/>
  }
  item def <'balance sheet c d o'> BalanceSheetCDO specializes CDODeal {
  	doc /* Balance Sheet CDO/CLO/CBO - the reference assets for the SDO portfolio are taken from a company/firm&apos;s balance sheet. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:BalanceSheetCDO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDODeal"/>
  }
  item def <'balance sheet c d o objective'> BalanceSheetCDOObjective specializes CDOOriginationObjective {
  	doc /* The objective is that the CDO is created to move assets off the originator balance sheet. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:BalanceSheetCDOObjective
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDOOriginationObjective"/>
  }
  item def <'cash a b s pool'> CashABSPool specializes Securities_Pools::DebtPool {
  	doc /* A pool investment consisting of a collection of cash ABS instruments. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CashABSPool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def <'cash c d o'> CashCDO specializes CDODeal {
  	doc /* A CDO which has an uderlying portfolio of assets which are held by the issuer. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CashCDO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDODeal"/>
  }
  item def <'cash c d o tranche'> CashCDOTranche specializes Pool_backed_Securities::Tranche, CollateralizedDebtObligation {
  	doc /* Arbitrage synthetic CDO deals are motivated by regulatory or practical considerations that might make a bank want to retain ownership of debt while achieving capital relief through CDSs. In this case, the sponsoring bank has a portfolio of obligations, called the reference portfolio. It retains that portfolio, but offloads its credit risk by transacting CDSs with the CDO. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CashCDOTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CollateralizedDebtObligation"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;Tranche"/>
  }
  item def <'cashflow c d o'> CashflowCDO specializes CDODeal {
  	doc /* A cash-flow CDO is analogous to a CMO. Cash flows from collateral are used to pay principal and interest to investors. If such cash flows prove inadequate, principal and interest is paid to tranches according to seniority. At any point in time, all immediate obligations to a given tranch are met before any payments are made to less senior tranches. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CashflowCDO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDODeal"/>
  }
  item def <'cashflow structure'> CashflowStructure specializes CDOCashflowTreatmentStructure {
  	doc /* The source of funds for the CDO is cashflow. this means that cash flows from collateral are used to pay principal and interest to investors. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CashflowStructure
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDOCashflowTreatmentStructure"/>
  }
  item def <'c d o cashflow treatment structure'> CDOCashflowTreatmentStructure specializes Objectives::Strategy {
  	doc /* The way in which cash flows are handled in a CDO. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CDOCashflowTreatmentStructure
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Strategy"/>
  }
  item def <'c d o deal'> CDODeal specializes Debt_Issuance::DebtOffering {
  	doc /* An Issue of a set of CDO tranches as part of an offering to the market. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CDODeal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;DebtOffering"/>
  }
  item def <'c d o management style'> CDOManagementStyle specializes Objectives::Strategy {
  	doc /* The manner in which reference assets are managed during the life of a CDO. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CDOManagementStyle
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Strategy"/>
  }
  item def <'c d o origination objective'> CDOOriginationObjective specializes Objectives::Objective {
  	doc /* The origin or motivation behind a CDO issue. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CDOOriginationObjective
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Objective"/>
  }
  item def <'c d o pool'> CDOPool specializes Securities_Pools::DebtPool {
  	doc /* A pool of CDO securities. The underlying of the CDO Squared is a pool of CDO instruments. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CDOPool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def <'c d o portfolio'> CDOPortfolio specializes Security_Assets::Portfolio {
  	doc /* A portfolio in which the reference assets of the CDO are held. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CDOPortfolio
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-ast;Portfolio"/>
  }
  item def <'c d o portfolio manager'> CDOPortfolioManager specializes Parties::PartyRole {
  	doc /* The portfolio manager for a managed CDO or arbitrage CDO (also called an asset manager). This assumes that the role is the same in both cases. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CDOPortfolioManager
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }
  item def <'c d o squared deal'> CDOSquaredDeal specializes CDODeal {
  	doc /* An issue of CDO Squared notes. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CDOSquaredDeal
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDODeal"/>
  }
  item def <'c d o squared instrument'> CDOSquaredInstrument specializes CollateralizedDebtObligation {
  	doc /* CDOs where the underlying asset pool is CDOs. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CDOSquaredInstrument
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CollateralizedDebtObligation"/>
  }
  item def <'CDOTrancheSenioritySelection'> CDOTrancheSenioritySelection { // RDF: fibo-sec-dbt-cdo:CDOTrancheSenioritySelection
    // RDF: fibo-sec-dbt-cdo:CDOTrancheSenioritySelection
  }
  item def <'c m o deal'> CMODeal specializes Debt_Issuance::DebtOffering {
  	doc /* An issue of Collateralized mortgaged Obligations. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CMODeal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;DebtOffering"/>
  }
  item def <'collateralized bond obligation'> CollateralizedBondObligation specializes CashCDOTranche {
  	doc /* structured debt security that has investment-grade bonds as its underlying assets backed by the receivables on high-yield or junk bonds */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CollateralizedBondObligation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CashCDOTranche"/>
  }
  item def <'collateralized loan obligation offering'> CollateralizedLoanObligationOffering specializes Debt_Issuance::DebtOffering {
  	doc /* A floater tranche is a tranche that is keyed to an index and a spread. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:CollateralizedLoanObligationOffering
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;DebtOffering"/>
  }
  item def <'floater tranche'> FloaterTranche specializes PrivateLabelMBSIssuance::TranchedMBSInstrument {
  	doc /* A floater tranche is a tranche that is keyed to an index and a spread. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:FloaterTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TranchedMBSInstrument"/>
  }
  item def <'regular floater tranche'> RegularFloaterTranche specializes FloaterTranche {
  	doc /* A floater tranche is a tranche that is keyed to an index and a spread. The spread is added to the index. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:RegularFloaterTranche
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;FloaterTranche"/>
  }
  item def <'super floater tranche'> SuperFloaterTranche specializes FloaterTranche {
  	doc /* Principal Only tranche. This tranche will only pay principal. Not clear how this is distinct from generic PO. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:SuperFloaterTranche
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;FloaterTranche"/>
  }
  item def <'hybrid c d o'> HybridCDO specializes CDODeal {
  	doc /* A CDO which combines features both of the cash and synthetic CDO. This is a CDO with tranches of cash and tranches of synthetic underlying assets. Further notes: So for example an issiue of $550 million may have $500 million in an asset pool and an additional $50 million created via a synthetic asset. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:HybridCDO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDODeal"/>
  }
  item def <'inverse floater tranche'> InverseFloaterTranche specializes FloaterTranche {
  	doc /* The event which triggers the Jump Z */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:InverseFloaterTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;FloaterTranche"/>
  }
  item def <'i oette'> IOette specializes AgencyIOTranche {
  	doc /* REVIEW: Is this a kind of IO Tranche. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:IOette
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyIOTranche"/>
  }
  item def <'jump z trigger event'> JumpZTriggerEvent specializes TriggerEvent {
  	doc /* The event which triggers the Jump Z */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:JumpZTriggerEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TriggerEvent"/>
  }
  item def <'jump z trigger event reversal'> JumpZTriggerEventReversal specializes TriggerEvent {
  	doc /* The reversal of the event which triggers the Jump Z */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:JumpZTriggerEventReversal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TriggerEvent"/>
  }
  item def <'loan pool'> LoanPool specializes Securities_Pools::DebtPool {
  	doc /* debt pool whose consituents are loans */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:LoanPool
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-pls;DebtPool"/>
  }
  item def <'managed c d o'> ManagedCDO specializes CDODeal {
  	doc /* A CDO where the reference assets are bought (the portfolio is ramped up) and then the CDO manager may alter the portfolio as they see fit. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:ManagedCDO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDODeal"/>
  }
  item def <'managed c d o portfolio'> ManagedCDOPortfolio specializes CDOPortfolio {
  	doc /* A portfolio where the reference assets of the CDO are bought (the portfolio is ramped up) and then the CDO manager may alter the portfolio as they see fit. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:ManagedCDOPortfolio
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDOPortfolio"/>
  }
  item def <'managed management style'> ManagedManagementStyle specializes CDOManagementStyle {
  	doc /* The tranche between senior and subordinated. Mezzanine tranches of a CDO issue are typically rated B to BBB. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:ManagedManagementStyle
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDOManagementStyle"/>
  }
  item def <'market value c d o'> MarketValueCDO specializes CDODeal {
  	doc /* The tranche between senior and subordinated. Mezzanine tranches of a CDO issue are typically rated B to BBB. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:MarketValueCDO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDODeal"/>
  }
  item def <'market value structure'> MarketValueStructure specializes CDOCashflowTreatmentStructure {
  	doc /* The tranche between senior and subordinated. Mezzanine tranches of a CDO issue are typically rated B to BBB. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:MarketValueStructure
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDOCashflowTreatmentStructure"/>
  }
  item def <'m b s instrument slice'> MBSInstrumentSlice specializes Security_Assets::Portfolio {
  	doc /* A holding of an individual slice or slices of a tranche. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:MBSInstrumentSlice
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-ast;Portfolio"/>
  }
  item def <'m b s tranche note'> MBSTrancheNote specializes Financial_Instruments::PromissoryNote {
  	doc /* An individual note of a tranche. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:MBSTrancheNote
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;PromissoryNote"/>
  }
  item def <'mezzanine c d o tranche'> MezzanineCDOTranche specializes CollateralizedDebtObligation {
  	doc /* The tranche between senior and subordinated. Mezzanine tranches of a CDO issue are typically rated B to BBB. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:MezzanineCDOTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CollateralizedDebtObligation"/>
  }
  item def <'mezzanine m b s tranche'> MezzanineMBSTranche specializes PrivateLabelMBSIssuance::TranchedMBSInstrument {
  	doc /* Specific kinds of tranche are modeled for example and investigation only and have been removed from the diagrams. These will be removed from the final model. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:MezzanineMBSTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TranchedMBSInstrument"/>
  }
  item def <'non agency i o tranche'> NonAgencyIOTranche specializes PrivateLabelMBSIssuance::TranchedMBSInstrument {
  	doc /* Interest Only tranche, meaning that this tranche will only pay interest. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:NonAgencyIOTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TranchedMBSInstrument"/>
  }
  item def <'non agency jump tranche'> NonAgencyJumpTranche specializes PrivateLabelMBSIssuance::TranchedMBSInstrument {
  	doc /* A tranche where if there is some sort of trigger event reached then the holders of the tranche will begin to receive payments. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:NonAgencyJumpTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TranchedMBSInstrument"/>
  }
  item def <'non agency jump z tranche'> NonAgencyJumpZTranche specializes NonAgencyJumpTranche, NonAgencyZTranche {
  	doc /* A Jump Z tranche is like a Z tranche but if there is some sort of trigger event reached then the holders of the Jump Z tranche will begin to receive payments. Regular non-Sticky Jump Z tranches maintain their changed status only while the trigger event is in effect, and revert to their old payment status once the trigger event has passed. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:NonAgencyJumpZTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;NonAgencyJumpTranche"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;NonAgencyZTranche"/>
  }
  item def <'non agency regular jump z tranche'> NonAgencyRegularJumpZTranche specializes NonAgencyJumpZTranche {
  	doc /* Regular non-Sticky Jump Z tranches maintain their changed status only while the trigger event is in effect, and revert to their old payment status once the trigger event has passed. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:NonAgencyRegularJumpZTranche
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;NonAgencyJumpZTranche"/>
  }
  item def <'non agency sticky jump z tranche'> NonAgencyStickyJumpZTranche specializes NonAgencyJumpZTranche {
  	doc /* &quot;Sticky&quot; Jump Z tranches maintain the payment priority of a Jump Z tranche until they are retired. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:NonAgencyStickyJumpZTranche
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;NonAgencyJumpZTranche"/>
  }
  item def <'non agency p o tranche'> NonAgencyPOTranche specializes PrivateLabelMBSIssuance::TranchedMBSInstrument {
  	doc /* Principal Only tranche. This tranche will only pay principal. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:NonAgencyPOTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TranchedMBSInstrument"/>
  }
  item def <'non agency z tranche'> NonAgencyZTranche specializes PrivateLabelMBSIssuance::TranchedMBSInstrument {
  	doc /* A tranche that does not receive payments while other tranches remain. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:NonAgencyZTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TranchedMBSInstrument"/>
  }
  item def <'p a c p o tranche'> PACPOTranche specializes AgencyPOTranche, PlannedAmortizationClassBond {
  	doc /* Planned Amortization Class principal only tranche. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:PACPOTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyPOTranche"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;PlannedAmortizationClassBond"/>
  }
  item def <'p a c tranche amortization schedule'> PACTrancheAmortizationSchedule specializes Bonds::RedemptionSchedule {
  	doc /* Planned Amortization Class tranche.This is a tranche where the principal payment must follow a certain schedule. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:PACTrancheAmortizationSchedule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;RedemptionSchedule"/>
  }
  item def <'pass through m b s deal'> PassThroughMBSDeal specializes Debt_Issuance::DebtOffering {
  	doc /* An issue of Mortgage Backed Security instruments in which payments on the pool are passed through to investors */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:PassThroughMBSDeal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;DebtOffering"/>
  }
  item def <'pass through m b s deal prospectus'> PassThroughMBSDealProspectus specializes Mortgage_backed_Securities::MortgageBackedSecurityOfferingProspectus {
  	doc /* The written prospectus for an agency, pass through issue of Mortgage Backed Securities */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:PassThroughMBSDealProspectus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MortgageBackedSecurityOfferingProspectus"/>
  }
  item def <'pass through m b s instrument'> PassThroughMBSInstrument specializes Mortgage_backed_Securities::MortgageBackedSecurity {
  	doc /* A security in which the cash flows from the underlying asset pool are passed through to the investor by way of redemption payments. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:PassThroughMBSInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MortgageBackedSecurity"/>
  }
  item def <'planned amortization class bond'> PlannedAmortizationClassBond specializes AgencyCMO {
  	doc /* Planned Amortization Class tranche.This is a tranche where the principal payment must follow a certain schedule. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:PlannedAmortizationClassBond
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyCMO"/>
  }
  item def <'residual tranche'> ResidualTranche specializes PrivateLabelMBSIssuance::TranchedMBSInstrument {
  	doc /* Unknown Further notes: Verify whether Residual Tranche and Support Tranche are meant to be in the same list of types as PAC etc. i.e. can a tranche not be PAC and Residual? this looks suspicioulsy like two semantics. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:ResidualTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TranchedMBSInstrument"/>
  }
  item def <'senior c d o tranche'> SeniorCDOTranche specializes CollateralizedDebtObligation, Pool_backed_Securities::Tranche {
  	doc /* The most senior tranche of the CDO issue. Typically rated A to AAA. If there are defaults or the CDO&apos;s collateral otherwise underperforms, scheduled payments to senior tranches take precedence over those of mezzanine tranches. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:SeniorCDOTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CollateralizedDebtObligation"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-pbs;Tranche"/>
  }
  item def <'super senior c d o tranche'> SuperSeniorCDOTranche specializes CollateralizedDebtObligation {
  	doc /* A tranche at the very top of a CDO Issue. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:SuperSeniorCDOTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CollateralizedDebtObligation"/>
  }
  item def <'senior m b s tranche'> SeniorMBSTranche specializes PrivateLabelMBSIssuance::TranchedMBSInstrument {
  	doc /* Specific kinds of tranche are modeled for example and investigation only and have been removed from the diagrams. These will be removed from the final model. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:SeniorMBSTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TranchedMBSInstrument"/>
  }
  item def <'static c d o'> StaticCDO specializes CDODeal {
  	doc /* A CDO where collateral is fixed through the life of the CDO. The reference assets are bought and then are kept untouched for the term of the product. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:StaticCDO
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDODeal"/>
  }
  item def <'static c d o portfolio'> StaticCDOPortfolio specializes CDOPortfolio {
  	doc /* A portfolio where collateral of the CDO is fixed through the life of the CDO. The reference assets are bought and then are kept untouched for the term of the product. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:StaticCDOPortfolio
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDOPortfolio"/>
  }
  item def <'static management style'> StaticManagementStyle specializes CDOManagementStyle {
  	doc /* The subordinated (also known as equity) CDO tranche is the most junior tranche in the CDO issue. If there are defaults or the CDO&apos;s collateral otherwise underperforms, scheduled payments to senior and mezzanine tranches take precedence over those to subordinated/equity tranches. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:StaticManagementStyle
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDOManagementStyle"/>
  }
  item def <'subordinated c d o equity'> SubordinatedCDOEquity specializes Financial_Instruments::Security {
  	doc /* The subordinated (also known as equity) CDO tranche is the most junior tranche in the CDO issue. If there are defaults or the CDO's collateral otherwise underperforms, scheduled payments to senior and mezzanine tranches take precedence over those to subordinated/equity tranches. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:SubordinatedCDOEquity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Security"/>
  }
  item def <'subordinated m b s tranche'> SubordinatedMBSTranche specializes PrivateLabelMBSIssuance::TranchedMBSInstrument {
  	doc /* Specific kinds of tranche are modeled for example and investigation only and have been removed from the diagrams. These will be removed from the final model. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:SubordinatedMBSTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TranchedMBSInstrument"/>
  }
  item def <'super p o tranche'> SuperPOTranche specializes AgencyPOTranche {
  	doc /* Principal Only tranche. This tranche will only pay principal. Not clear how this is distinct from generic PO. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:SuperPOTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyPOTranche"/>
  }
  item def <'support tranche'> SupportTranche{ // RDF: fibo-sec-dbt-cdo:SupportTranche
    // RDF: fibo-sec-dbt-cdo:SupportTranche
  }
  item def <'t a c tranche'> TACTranche specializes AgencyCMO, PrivateLabelMBSIssuance::TranchedMBSInstrument {
  	doc /* Targeted Amortization Class. This is related to a PAC tranche and has a payment schedule geared towards a specified prepayment speed (called the pricing speed). Agency CMO */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:TACTranche
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;AgencyCMO"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;TranchedMBSInstrument"/>
  }
  item def <'t a c tranche amortization schedule'> TACTrancheAmortizationSchedule specializes Bonds::RedemptionSchedule {
  	doc /* The rating at issue of a tranche of a security. Note this is under review */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:TACTrancheAmortizationSchedule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-bnd;RedemptionSchedule"/>
  }
  item def <'tranched m b s deal'> TranchedMBSDeal specializes Debt_Issuance::DebtOffering {
  	doc /* Any event where some value passes some threashold. Or some other type of business event. This is not restricted to &quot;trigger&quot; in the sense of a value passing a threshold. Can also be an seen such as a CDO manager going into bankruptcy. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:TranchedMBSDeal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;DebtOffering"/>
  }
  item def <'tranched m b s deal prospectus'> TranchedMBSDealProspectus specializes Mortgage_backed_Securities::MortgageBackedSecurityOfferingProspectus {
  	doc /* Any event where some value passes some threashold. Or some other type of business event. This is not restricted to "trigger" in the sense of a value passing a threshold. Can also be an seen such as a CDO manager going into bankruptcy. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:TranchedMBSDealProspectus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MortgageBackedSecurityOfferingProspectus"/>
  }
  item def <'tranched m b s instrument'> TranchedMBSInstrument specializes Mortgage_backed_Securities::MortgageBackedSecurity {
  	doc /* Any event where some value passes some threashold. Or some other type of business event. This is not restricted to "trigger" in the sense of a value passing a threshold. Can also be an seen such as a CDO manager going into bankruptcy. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:TranchedMBSInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-mbs;MortgageBackedSecurity"/>
  }
  item def <'tranche rating at issue'> TrancheRatingAtIssue { // RDF: fibo-sec-dbt-cdo:TrancheRatingAtIssue
    // RDF: fibo-sec-dbt-cdo:TrancheRatingAtIssue
  }
  item def <'tranche type'> TrancheType { // RDF: fibo-sec-dbt-cdo:TrancheType
    // RDF: fibo-sec-dbt-cdo:TrancheType
  }
  item def <'trigger event'> TriggerEvent { // RDF: fibo-sec-dbt-cdo:TriggerEvent
    // RDF: fibo-sec-dbt-cdo:TriggerEvent
  }
  item def <'true p s objective'> TruePSObjective specializes CDOOriginationObjective {
  	doc /* The currency amount in which the Note is denominated, for example $500 notes. */
  	// skos:definition xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// rdfs:label xml:lang="en" (CollateralizedDebtObligations.rdf)
  	// RDF: fibo-sec-dbt-cdo:TruePSObjective
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-cdo;CDOOriginationObjective"/>
  }
  // NOTE: PAC-1Class, PAC-2Class, PAC-3Class, PAC-ZTranche contain hyphens in their RDF names;
  // renamed here without hyphens for SysML identifier compliance
  item def PAC1Class{ // RDF: fibo-sec-dbt-cdo:PAC-1Class
    // RDF: fibo-sec-dbt-cdo:PAC1Class
  }
  item def PAC2Class{ // RDF: fibo-sec-dbt-cdo:PAC-2Class
    // RDF: fibo-sec-dbt-cdo:PAC2Class
  }
  item def PAC3Class{ // RDF: fibo-sec-dbt-cdo:PAC-3Class
    // RDF: fibo-sec-dbt-cdo:PAC3Class
  }
  item def PACZTranche{ // RDF: fibo-sec-dbt-cdo:PAC-ZTranche
  }
    // RDF: fibo-sec-dbt-cdo:PACZTranche
}

// --- DebtInstruments.rdf ---
