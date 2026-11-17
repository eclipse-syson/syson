package DETECT_Sizing {
    doc Acknowledgement
    /* This file contains content from DETECT model file for SysML v1.6 by Daniel Hettema, OUSD (R&E).
     * Original content is dedicated to the Public Domain.
     * Modifications Copyright (c) 2025 Sensmetry.
     */

    doc Description
    /* Determine DE Ecosystem Sizing */

    requirement def Criteria_Def {
        subject de_tool_ecosystem = DETECT_Input::de_ecosystem;
        attribute weight defined by ScalarValues::Rational [0..1];
    }

    requirement def DE_Ecosystem_req_Def {
        subject de_tool_ecosystem = DETECT_Input::de_ecosystem;
        attribute weight defined by ScalarValues::Rational [0..1];
    }

    requirement valid_criteria : Criteria_Def;
    requirement valid_requirement : DE_Ecosystem_req_Def;

    package DETECT_enumerations {
        doc
        /* The Enumerated Values are value attributes that are assigned to each of the size
         * parameter enumerations.  The value assigned to parameters that indicate a large DE
         * ecosystem development effort is a 3, the value assigned to a medium ecosystem size
         * parameter is a two and a value of one is assigned to a small ecosystem effort.
         *
         * The value enumerations can be changed as desired.  If for example the value of 3 is
         * changed to a 10 then every size parameter that was assigned the value of 3 will change
         * to a 10.
         *
         * This scoring scale can be adjusted as needed.
         */

        attribute def Attr_with_value {
            doc
            /* Generic attribute definition for all enumerations containing nested "value"
             * attribute
             */
            attribute value : ScalarValues::Natural default 0;
        }

        enum def Ecosystem_size_e {
            Small;
            Medium;
            Large;
            TBD; // selection placeholder;
        }
        enum def Automation_e specializes Attr_with_value {
            'Manual, Low Automation' { redefines value = 1; }
            'Moderate, Adoption of autonomous platforms, Ongoing integration efforts' {
                redefines value = 2;
            }
            'Autonomous, Enterprise-Level, Full Automation' {
                redefines value = 3;
            }
            TBD { redefines value = 0; } // selection placeholder;
        }
        enum def CollaborationPartners_e specializes Attr_with_value {
            '0 (regional)' { redefines value = 1; }
            '1-5' { redefines value = 2; }
            '>5' { redefines value = 3; }
            TBD { redefines value = 0; } // selection placeholder;
        }
        enum def Enclaves_e specializes Attr_with_value {
            '1-10 enclaves' { redefines value = 1; }
            '11-50 enclaves' { redefines value = 2; }
            '50 + enclaves' { redefines value = 3; }
            TBD { redefines value = 0; } // selection placeholder;
        }
        enum def EngineeringDomains_e specializes Attr_with_value {
            '<=2' { redefines value = 1; }
            '>2' { redefines value = 2; }
            '>5' { redefines value = 3; }
            TBD { redefines value = 0; } // selection placeholder;
        }
        enum def GeographicLocations_e specializes Attr_with_value {
            '1' { redefines value = 1; }
            '2-5' { redefines value = 2; }
            '>5' { redefines value = 3; }
            TBD { redefines value = 0; } // selection placeholder;
        }
        enum def LifecyclePhases_e specializes Attr_with_value {
            '1-2' { redefines value = 1; } // AMBIGUITY!!! (when the number of lifecycle phases ecosystem supports is '2')
            '2 to 3' { redefines value = 2; } // AMBIGUITY!!! (when the number of lifecycle phases ecosystem supports is '2')
            All { redefines value = 3; }
            TBD { redefines value = 0; } // selection placeholder;
        }
        enum def ProjectDeliverables_e specializes Attr_with_value {
            '10s' { redefines value = 1; }
            '100s' { redefines value = 2; }
            '1000s' { redefines value = 3; }
            TBD { redefines value = 0; } // selection placeholder;
        }
        enum def Storage_e specializes Attr_with_value {
            Gigabytes { redefines value = 1; }
            Terabytes { redefines value = 2; }
            Petabytes { redefines value = 3; }
            TBD { redefines value = 0; } // selection placeholder;
        }
        enum def Tenants_e specializes Attr_with_value {
            'Single application, single DB' { redefines value = 1; }
            'Multi application, single DB' { redefines value = 2; }
            'Multi application, multi DB' { redefines value = 3; }
            TBD { redefines value = 0; } // selection placeholder;
        }
        enum def Users_e specializes Attr_with_value {
            '1-100' { redefines value = 1; } // AMBIGUITY!!! (when the number of supported users is '100')
            '100-1000' { redefines value = 2; } // AMBIGUITY!!! (when the number of supported users is '100')
            '>1000' { redefines value = 3; }
            TBD { redefines value = 0; } // selection placeholder;
        }
    }

    // DETECT USER DEFINITION
    part def DETECT_user {
        doc
        /* A person using DETECT method to evaluate DE tool. */
    }

    use case def DETECT_use_case_Def {
        attribute uc_status defined by status_e default status_e::Red;
    }

    // DIGITAL ENGINEERING ECOSYSTEM SIZE DEFINITION
    part def DE_Ecosystem {
        item inputs {
            attribute <Q1> number_of_users : DETECT_enumerations::Users_e {
                doc Question
                /* How many people do you expect will be using the DE ecosystem being developed? */

                doc Description
                /* This parameter describes the **number of users** that one might think of when
                 * sizing their DE ecosystem effort.
                 *
                 * **Small DE Ecosystem**
                 *
                 * - Typically does not require additional services or software tools for license
                 *   and user account administration
                 * - Manual administration is feasible
                 *
                 * **As User Base Grows**
                 *
                 * - Even within a single domain, managing licensing and user accounts manually
                 *   becomes challenging
                 * - Increased tooling in the DE ecosystem becomes necessary for effective
                 *   administration
                 */
            }
            attribute <Q2> number_of_partners : DETECT_enumerations::CollaborationPartners_e {
                doc Question
                /* How many partners will need to use the DE ecosystem being developed? */
                doc Description
                /* This parameter describes the **number of partners** that are anticipated to be
                 * using and interacting with the DE ecosystem.
                 *
                 * As this number grows, so will the size of the DE ecosystem. More collaboration
                 * partners will suggest an increase in the **infrastructure**, **people**, and
                 * **organization** that will have to be involved. Adding just **1 to 5
                 * collaboration partners** is estimated to be a medium-sized scope. It also implies
                 * that there would need to be a medium number of **enclaves** and **tenants**.
                 */
            }
            attribute <Q3> engineering_domains : DETECT_enumerations::EngineeringDomains_e {
                doc Question
                /* How many engineering domains need to use the DE ecosystem being developed? */
                doc Description
                /* This parameter describes the **number of engineering domains** that are expected
                 * to be required to perform the necessary engineering for a given product.
                 *
                 * **Examples of Engineering Domains**
                 *
                 * - Systems engineering
                 * - Mechanical engineering
                 * - Software engineering
                 * - Analysis domain
                 *
                 * **Impact on Ecosystem**
                 *
                 * - **Small number of domains** (e.g., a purely mechanical product) → Fewer tools
                 *   required → Less tool-to-tool integration needed
                 * - **Multiple domains** → More tools and capabilities required → Increased
                 *   integration complexity
                 */
            }
            attribute <Q4> geographic_locations : DETECT_enumerations::GeographicLocations_e {
                doc Question
                /* How many geographic locations will be connected by the DE ecosystem being
                 * developed?
                 */
                doc Description
                /* This parameter represents the number of different geographic locations that
                 * the users will be in when using the DE ecosystem that is being developed.
                 */
            }
            attribute <Q5> lifecycle_phases : DETECT_enumerations::LifecyclePhases_e {
                doc Question
                /* How many of your Lifecycle Phases need to be supported by the DE ecosystem being
                 * developed?
                 */
                doc Description
                /* This parameter refers to the **number of life-cycle phases** the DE ecosystem is
                 * intended to support, using the **Adaptive Acquisition Framework, Major Capability
                 * Pathway** as a reference.
                 *
                 * **Life-Cycle Phases**
                 *
                 * - **MSA** - Material Solutions Analysis
                 * - **TMRR** - Technology Maturation & Risk Reduction
                 * - **EMD** - Engineering & Manufacturing Development
                 * - **PD** - Production & Deployment
                 * - **O&S** - Operation & Support
                 *
                 * **Scope Considerations**
                 *
                 * - Working in **only one phase** → May not need tools that support all life-cycle
                 *   phases
                 * - **Example:** Capturing the full bill of materials (BOM) for an operational
                 *   system throughout its lifetime via a full-scale PLM ecosystem is orders of
                 *   magnitude more scope than managing pre-Milestone-A trade study data
                 */
            }
            attribute <Q6> automation : DETECT_enumerations::Automation_e {
                doc Question
                /* What degree of automation is desired for the DE ecosystem being developed? */
                doc Description
                /* Levels of automation in digital engineering can be categorized into several
                 * stages, each representing a different degree of automation and integration.
                 *
                 * **Automation Levels**
                 *
                 * 1. **Manual**
                 *
                 *    - Tasks performed entirely by humans without automated assistance
                 *    - **Integration:** Low
                 *    - **Level of effort:** High
                 *
                 * 2. **Assisted**
                 *
                 *    - Humans perform tasks with tools/software providing guidance
                 *    - **Integration:** Moderate
                 *    - **Level of effort:** Moderate
                 *
                 * 3. **Semi-Automated**
                 *
                 *    - Some tasks automated, human intervention still required
                 *    - **Integration:** High
                 *    - **Level of effort:** Moderate to Low
                 *
                 * 4. **Fully Automated**
                 *
                 *    - Tasks performed entirely by automated systems with minimal human oversight
                 *    - **Integration:** Very High
                 *    - **Level of effort:** Low
                 *
                 * 5. **Autonomous**
                 *
                 *    - Systems operate independently, making decisions and adapting to changes
                 *    - **Integration:** Fully Integrated ecosystem required
                 *    - **Level of effort:** Very Low
                 *
                 * **Summary**
                 *
                 * As automation increases, integration becomes more seamless and human effort
                 * decreases. This progression enables more efficient and reliable operations,
                 * reducing human error and increasing productivity. For example, a fully automated
                 * manufacturing line can operate continuously with minimal human intervention,
                 * leading to higher output and consistency.
                 */
            }
            attribute <Q7> job_series {
                doc Question
                /* TBD */
                doc Description
                /* TBD */
            }
            attribute <Q8> enclaves : DETECT_enumerations::Enclaves_e {
                doc Question
                /* How many enclaves are needed to support the number of people, geographic
                 * locations, engineering domains, and collaboration partners for the DE
                 * ecosystem being developed?
                 */
                doc Description
                /* This parameter is **derived** based on collaboration partners, domains, and
                 * organizations that are to work within the DE ecosystem.
                 *
                 * **What Enclaves Represent**
                 *
                 * - Number of **organizational boundaries** that may be part of the DE ecosystem
                 * - **Distinct networks**, often segregated by constraints such as:
                 *
                 *   - Classification level
                 *   - Intellectual Property (IP) constraints
                 *   - Business constraints
                 */
            }
            attribute <Q9> tenants : DETECT_enumerations::Tenants_e {
                doc Question
                /* Which set of situations most closely matches the DE ecosystem planned to be
                 * developed?
                 */
                doc Description
                /* This parameter would likely be **derived** based on the following factors that
                 * are to work in the DE ecosystem being developed:
                 *
                 * - **Collaboration partners**
                 * - **Engineering domains**
                 * - **Organizations**
                 */
            }
            attribute <Q10> project_deliverables : DETECT_enumerations::ProjectDeliverables_e {
                doc Question
                /* What is the order of magnitude of deliverables anticipated? */
                doc Description
                /* This parameter is about the **order of magnitude of deliverables/decisions**
                 * that will need to be supported by the DE ecosystem.
                 *
                 * **Impact on Ecosystem**
                 *
                 * - **Smaller number** → Fewer customizations, reports, queries, data flows, etc.
                 *   needed
                 * - **Larger number** → More extensive tooling and data management capabilities
                 *   required
                 *
                 * **Note** In Digital Engineering, deliverables are tied to decisions where many
                 * data products are required.
                 */
            }
            attribute <Q11> storage : DETECT_enumerations::Storage_e {
                doc Question
                /* What is the order of magnitude of the amount of data that is anticipated to be
                 * stored?
                 */
                doc Description
                /* This parameter helps determine **how much storage** needs to be included in your
                 * DE ecosystem solution.
                 *
                 * **Considerations**
                 *
                 * - The more storage required, the "larger" the storage solution will need to be
                 * - Consider both current needs and anticipated growth over the project lifecycle
                 * - Factor in data retention requirements, backups, and redundancy needs
                 */
            }
        }
        derived attribute system_size_number : ScalarValues::Natural {
            /* A parameter aggregating the values of each user input, used to determine size of the
             * anticipated Digital Engineering Ecosystem
             */
        }

        derived attribute system_size : DETECT_enumerations::Ecosystem_size_e {
            /* A parameter defining size of the anticipated Digital Engineering Ecosystem, which
             * value (enum) is derived by calculation
             */
        }
    }

    enum def status_e {
        Green {
            doc
            /* The feature is implemented */
        }
        Red {
            doc
            /* The feature is NOT implemented */
        }
    }
}
