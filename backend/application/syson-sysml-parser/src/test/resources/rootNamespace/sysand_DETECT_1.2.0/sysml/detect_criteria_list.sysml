package DETECT_Criteria_List {
    doc Acknowledgement
    /* This file contains content from DETECT model file for SysML v1.6 by Daniel Hettema, OUSD (R&E).
     * Original content is dedicated to the Public Domain.
     * Modifications Copyright (c) 2025 Sensmetry.
     */

    doc Description
    /* Weighted criteria for small/medium/large DE Ecosystem according to DETECT method */

    private import SequenceFunctions::*;
    private import DETECT_Input;
    private import DETECT_Sizing::*;

    criteria_list = (C1, C2, C3, C4, C5, C6, C7, C8, C9, C10, C11, C12);

    requirement <C1> governance defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_criteria
            else null {
        doc Criteria
        /* Does the tool support a governance process? */
        doc Context
        /* The criteria category focuses on the ability of the tool being evaluated to support governance capabilities. 
         * Governance is a combination of a number of topic areas.  It deals with configuration control, configuration management, 
         * change control, change management, and data management. Supports the need to be able to curate which implies an ability 
         * to be able to determine whether the model/data is trustworthy.  Are you confident in the source information, does the source 
         * information contain the information needed to be able to make that sort of determination.
         */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_1_subcriteria = ('C1.1', 'C1.2', 'C1.3', 'C1.4', 'C1.5', 'C1.6', 'C1.7');

        requirement valid_criteria : Criteria_Def;

        requirement <'C1.1'> gov_curating defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support curation of the work product? */
            doc Context
            /* Curation makes it easier for stakeholders to find and use the right data and models.  Curation also helps 
             * in preserving and managing knowledge, making it easier to reuse and build upon existing work. Examples for consideration: work product is reused on another project, the controlled propagation of changes between work products.
             */

            attribute redefines weight = 1.0 / size(criteria_1_subcriteria);
        }

        requirement <'C1.2'> gov_auto_tracking defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support automatic tracking and managing changes to work products over time? */
            doc Context
            /* Version control is important to governance as it provides a clear history of changes made, allowing teams to track who made changes when and why.  This is important for accountability and auditing purposes.  Version control also helps when collaborating on the same project simultaneously to avoid overwriting each other's work, fostering better collaboration and coordination.  Version control also helps to ensure that everyone is working with the most up-to-date and accurate information and allows teams to revert to previous versions if something goes wrong. Examples for consideration: automatic change tracking, ability to edit recursively, branching and merging, version history logs, and conflict resolution mechanisms */

            attribute redefines weight = 1.0 / size(criteria_1_subcriteria);
        }

        requirement <'C1.3'> gov_user_change defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support a user-defined process for controlling change? */
            doc Context
            /* Considering whether a digital engineering tool supports user-defined processes for changes is crucial for maintaining control, consistency, and compliance in workflows. Unlike version control, which is used to track and manage changes, user-defined processes actively guide how changes are proposed, reviewed, approved, and implemented. This ensures consistency, reduces risk, and aligns with regulatory requirements  by enforcing structured workflows. Examples for consideration: customizable workflows, role-based permissions, and change request tracking. */

            attribute redefines weight = 1.0 / size(criteria_1_subcriteria);
        }

        requirement <'C1.4'> gov_config_mng defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support configuration management? */
            doc Context
            /* Configuration management is essential in DE tools to ensure consistency, integrity, and traceability of all system components throughout their lifecycle. Unlike version control, which is used to track change to individual files, and user-defined processes, which govern how changes are made, configuration management focuses on aligning and managing the relationships between all elements of the system. This alignment is crucial for governance as it ensures baseline integrity, compliance of systems with regulatory and organizational standards; supports change impact analysis; and prevents mismatches between components by maintaining synchronization access the system.  Examples for consideration: baseline management, dependency tracking, and automated consistency checks. */

            attribute redefines weight = 1.0 / size(criteria_1_subcriteria);
        }

        requirement <'C1.5'> gov_audit_acc defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support audit trails and accountability? */
            doc Context
            /* To support audit trails and accountability, a tool should consider the ability to log all user actions, changes, and access events comprehensively. This ensures transparency, helps in tracking the origin of issues, and facilitates compliance with regulatory requirements. Such capabilities are crucial for maintaining data integrity and trust within the system. Possible attributes for consideration include detailed logging, user authentication, and role-based access control. For example, a tool that can track changes to project tasks, providing a clear history of who made what changes and when. */

            attribute redefines weight = 1.0 / size(criteria_1_subcriteria);
        }

        requirement <'C1.6'> gov_stnd_compl defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support compliance with organizational, industry, or regulatory standards? */
            doc Context
            /* To support compliance with organizational, industry, or regulatory standards, a tool should manage regulatory requirements, facilitate policy and procedure management, and conduct risk assessments. It also must support audits, incident management, training programs, and document management and provide robust reporting and analytics. Integration with other systems and scalability are essential for comprehensive compliance coverage. Incorporating these features ensures the tool can effectively support compliance and enhance governance processes. For example, a tool might track regulatory changes, manage policy updates, and provide audit trails to demonstrate compliance. This comprehensive approach helps organizations meet compliance requirements and mitigate risks. */

            attribute redefines weight = 1.0 / size(criteria_1_subcriteria);
        }

        requirement <'C1.7'> gov_metrics_reporting defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool provide metrics and reporting? */
            doc Context
            /* Generating governance metrics and reports in digital engineering tool evaluations is essential for measuring the effectiveness of tools and processes, ensuring they meet organizational goals and regulatory requirements. These metrics help identify inefficiencies, risks, and compliance gaps, enabling informed decision-making and process optimization. For example, metrics might show that a tool improves data accuracy and reduces compliance risks, justifying its adoption. This approach ensures accountability and continuous improvement in governance practices. Possible attributes for consideration: a tool will collect and integrate data from various sources, allow customization of metrics, provide real-time monitoring with alerts, offer advanced analytics and visualization, generate detailed reports, and maintain historical data for trend analysis, track strategic objectives, monitor operational performance, and provide insights into organizational risks. */

            attribute redefines weight = 1.0 / size(criteria_1_subcriteria);
        }
    }

    requirement <C2> integration defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_criteria
            else null {
        doc Criteria
        /* Does the tool support integration of models/tools? */
        doc Context
        /* This criteria category focuses on the ability of the tool being evaluated to support integrating it into the organization's existing ecosystem or brand new ecosystem.  It is about understanding if the tool integrates and how well that integration supports industry standards as well as standards that the organization may be using.  Select tools that are generally in alignment. Select tools that are able to support integration when the need arises. It is also important to understand the way in which this is to be done.  It may not be the best idea to select a tool that requires learning about a new language that the organization doesn't have any skills in. Also it isn't recommended to choose a tool that uses antiquated and proprietary languages as this will require specialized training.  Also it could be difficult to find resources that have the type of skills needed to be able to integrate a tool. */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_2_subcriteria = (
            'C2.1', 'C2.2', 'C2.3', 'C2.4', 'C2.5', 'C2.6', 'C2.7', 'C2.8', 'C2.9', 'C2.10'
        );

        requirement <'C2.1'> intgr_env defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support integration into your organization's modeling environment? */
            doc Context
            /* Considering whether a modeling environment supports tool integration is crucial for enabling seamless data sharing, improving efficiency, and maintaining consistency in digital engineering workflows. Integrated tools reduce the risk of data silos by synchronizing changes across systems, enhancing collaboration across disciplines, and supporting better decision-making with a unified system view. Features such as open APIs, support for standards (e.g., FMI, OSLC), and centralized repositories facilitate smooth integration. This integration supports the digital thread, ensuring traceability and cohesion across the system lifecycle. */

            attribute redefines weight = 1.0 / size(criteria_2_subcriteria);
        }

        requirement <'C2.2'> intgr_import_export defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Can the tool import/export data using data exchange standard formats that are consistent with established ecosystem requirements? */
            doc Context
            /* Evaluating whether a tool supports data exchange using standardized formats consistent with established ecosystem requirements, is important for ensuring interoperability, data integrity, and compliance. Standard formats enable seamless integration across tools, facilitate collaboration with stakeholders, and align with industry regulations. Features like support for standards (e.g., STEP, XMI, FMI), customizable import/export settings, and data validation ensure smooth data sharing and compatibility. This capability also future-proofs systems by reducing risks of data obsolescence and ensuring long-term accessibility. */

            attribute redefines weight = 1.0 / size(criteria_2_subcriteria);
        }

        requirement <'C2.3'> intgr_verif_valid defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support verification / validation of models and data? */
            doc Context
            /* Considering whether a tool supports verification and validation (V&V) of models and data is critical to ensuring accuracy, reliability, and compliance in digital engineering. Verification ensures models are built correctly and meet specifications, while validation confirms they accurately represent the intended system or purpose. Features like rule-based validation, automated consistency checks, simulation tools, and traceability links enable robust V&V processes. These features help users identify issues early, ensure compliance, and build stakeholder confidence. Effective V&V reduces risks and enhances trust in the models used for decision making. */

            attribute redefines weight = 1.0 / size(criteria_2_subcriteria);
        }

        requirement <'C2.4'> intgr_auto_workflows defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support the development and use of automated workflows? */
            doc Context
            /* Considering whether a tool supports automated workflows is critical in digital engineering as it enhances efficiency, reduces errors, and ensures consistency in processes. Automated workflows streamline repetitive tasks, enforce standard procedures, and improve traceability by documenting each step. Features like drag-and-drop workflow designers, rule-based triggers, and integration with external tools enable seamless automation. These features help teams save time, maintain accuracy, and accelerate iterative processes. Automation ensures reliable execution of tasks, improving productivity and outcomes in engineering projects. */

            attribute redefines weight = 1.0 / size(criteria_2_subcriteria);
        }

        requirement <'C2.5'> intgr_interfacing defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support interfacing and integration with models? */
            doc Context
            /* Considering whether a tool supports interfacing and integration with models is crucial for enabling seamless interaction between models from different domains or tools, ensuring cohesive system-level analysis and validation. Unlike general tool integration or data exchange, interfacing focuses on connecting and using models together for real-time simulations, multi-domain collaboration, and iterative design. Features like co-simulation, standardized protocols (e.g., Functional Mockup Interface (FMI)), and real-time synchronization facilitate these interactions. For example, it could be beneficial for a tool to provide capabilities to integrate and simulate interconnected models, enabling accurate, holistic system evaluation and robust engineering workflows. */

            attribute redefines weight = 1.0 / size(criteria_2_subcriteria);
        }

        requirement <'C2.6'> intgr_language_compl defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Is the tool compliant with industry standard languages? */
            doc Context
            /* A standard language in this context can be Java, SysML, UAF, etc. Supporting industry standard languages in a digital engineering ecosystem tool ensures interoperability, scalability, and future-proofing. This compatibility allows seamless integration with other tools and systems, facilitating efficient data exchange and collaboration. It also ensures that the tool can adapt to evolving industry practices and standards, reducing the risk of obsolescence. Possible attributes for consideration include compatibility, flexibility, and compliance with industry standards. For example, a tool supporting the SysML (Systems Modeling Language) standard can easily integrate with other SysML-compliant tools, enhancing overall system design and analysis. */

            attribute redefines weight = 1.0 / size(criteria_2_subcriteria);
        }

        requirement <'C2.7'> intgr_interfacing_diff defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support interfacing and integration with different model types? */
            doc Context
            /* Model types in this context could be a data model (requirements matrices, etc.) vs. geometry model (CAD, etc.).  In a digital engineering ecosystem, it's crucial for a tool to integrate with dissimilar models to ensure seamless data exchange and interoperability, which enhances collaboration and reduces errors. This integration allows for more efficient workflows and better decision-making by enabling comprehensive analysis across different systems. Ultimately, it supports the creation of a unified digital thread, improving the overall quality and consistency of engineering outputs. Possible attributes for consideration include compatibility with various data formats, scalability, and ease of use. For example, a tool that can integrate CAD models with simulation software allows engineers to quickly validate designs and optimize performance. */

            attribute redefines weight = 1.0 / size(criteria_2_subcriteria);
        }

        requirement <'C2.8'> intgr_word_sheet_graphic defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool interface with word processing, spreadsheet, graphical illustration, and presentation software? */
            doc Context
            /* In a Digital Engineering Ecosystem, it's essential for a tool to integrate with word processing, spreadsheet, and graphical illustration and presentation software to streamline documentation, data analysis, and visual communication. This integration enhances productivity by allowing seamless transitions between different types of tasks, ensuring that data and insights are easily accessible and presentable. It also supports better collaboration among team members by providing a unified platform for various types of content.
             * Possible attributes for consideration include compatibility with common file formats, user-friendly interfaces, and robust data synchronization capabilities. For example, a tool that integrates engineering data with Excel and PowerPoint enables engineers to efficiently analyze data and create compelling presentations.
             */

            attribute redefines weight = 1.0 / size(criteria_2_subcriteria);
        }

        requirement <'C2.9'> intgr_api defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support API/ interfacing (standard plug-ins) for data exchange tool-to-tool interaction? */
            doc Context
            /* Supporting Application Protocol Interface (API)/interfacing in a digital engineering ecosystem is crucial because it enables seamless data exchange between tools, enhancing interoperability and reducing manual data entry errors. This integration fosters innovation by allowing different tools to leverage each other's functionalities, leading to more robust and efficient workflows. Additionally, APIs facilitate scalability and flexibility, making it easier to adapt to new tools and technologies as they emerge. Possible attributes for consideration include ease of customization, compatibility with standard data-exchange formats, scalability, security, and performance. For example, using Representational State Transfer (REST) API's can allow a project management tool to automatically update task statuses based on data from a time-tracking application. */

            attribute redefines weight = 1.0 / size(criteria_2_subcriteria);
        }

        requirement <'C2.10'> intgr_eng_other defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool interface with other engineering domain design and analysis tools? */
            doc Context
            /* Interfacing with engineering domain design and analysis tools is crucial because it enables cross-disciplinary collaboration, streamlining workflows between specialized domains like mechanical, electrical, and software engineering. This integration ensures consistency and accuracy in design data, reducing errors and rework. It also supports end-to-end traceability, enabling better decision-making and validation of complex system designs. Attributes to consider include compatibility with commonly used domain-specific tools and data synchronization capabilities. For example, a systems engineering tool that integrates with CAD tools like Siemens NX allows seamless data exchange between system requirements and detailed mechanical designs. */

            attribute redefines weight = 1.0 / size(criteria_2_subcriteria);
        }
    }

    requirement <C3> access defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_criteria
            else null {
        doc Criteria
        /* Does the tool support access control? */
        doc Context
        /* This criteria category focuses on the ability of the tool being evaluated to be able to provide controlled access.  Access can depend on many factors. Depending on the access rules established in an organization, a tool might not support those capabilities.  The criteria listed in this section are a number of typical items that are often considered when providing access. */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_3_subcriteria = ('C3.1', 'C3.2', 'C3.3', 'C3.4', 'C3.5', 'C3.6', 'C3.7');

        requirement <'C3.1'> access_IP_rights defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support IP and data rights automated management? */
            doc Context
            /* Supporting automated management of intellectual property (IP) and data rights in digital engineering tools is important to protect proprietary information and ensure compliance with legal agreements. Automated management reduces the risk of unauthorized access or misuse, safeguarding competitive advantages and contractual obligations. Automated management also streamlines processes for tracking data ownership, licensing, and usage rights, saving time and ensuring accountability.
             * Attributes to consider include role-based access controls and configurable licensing management settings. For example, a tool that enforces IP restrictions through automated tagging and access permissions ensures only authorized users can view or modify sensitive data.
             */

            attribute redefines weight = 1.0 / size(criteria_3_subcriteria);
        }

        requirement <'C3.2'> access_classified_mark defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support automated marking of classified information? */
            doc Context
            /* Automated classification management in digital engineering tools is vital for ensuring data is handled according to its security classification, reducing the risk of unauthorized access or breaches. It helps organizations comply with security policies by automatically tagging and restricting data based on its classification, simplifying workflows while enhancing compliance. Unlike IP and data rights management, which focuses on ownership and usage rights, classification management addresses national security and organizational confidentiality requirements.
             * Attributes to consider include integration with security frameworks (e.g., NIST, DoD classifications) and dynamic data tagging capabilities. For example, a tool that automatically labels documents as "Confidential" or "Top Secret" and enforces access restrictions based on user clearance levels ensures secure data handling.
             */

            attribute redefines weight = 1.0 / size(criteria_3_subcriteria);
        }

        requirement <'C3.3'> access_classified_multiple defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool accommodate multiple classification levels? */
            doc Context
            /* Accommodating multiple classification levels in digital engineering tools is crucial for managing projects that span diverse security requirements, enabling seamless collaboration without compromising data integrity. It ensures that data is appropriately segregated and accessible only to authorized users, reducing the risk of security breaches. This capability also supports compliance with organizational and regulatory standards, making it easier to manage complex, multi-level environments.
             * Attributes to consider include flexible data compartmentalization and user access controls based on classification. For example, a tool that segregates data into "Unclassified," "Confidential," and "Top Secret" domains, while allowing secure access for users with the appropriate clearance, ensures secure and efficient project management.
             */

            attribute redefines weight = 1.0 / size(criteria_3_subcriteria);
        }

        requirement <'C3.4'> access_edit_auth defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool allow only authorized users to edit? */
            doc Context
            /* Restricting editing capabilities to authorized users in digital engineering tools is essential to maintain data integrity and prevent unauthorized changes that could compromise system design or analysis. It ensures accountability by tracking who made changes and when, supporting auditability and traceability. This control also safeguards sensitive or critical data from accidental or malicious modifications.
             * Attributes to consider include role-based access control and detailed activity logging. For example, a tool that assigns editing permissions only to project leads while allowing others read-only access prevents unauthorized alterations and ensures a clear chain of responsibility.
             */

            attribute redefines weight = 1.0 / size(criteria_3_subcriteria);
        }

        requirement <'C3.5'> access_attribute defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool provide access controls down to the attribute level? */
            doc Context
            /* Access controls down to the attribute level in digital engineering tools are critical for ensuring fine-grained security and data governance, allowing specific data elements to be restricted based on user roles. This level of control enhances security by protecting sensitive information without limiting broader access to less critical data, enabling collaboration without compromising confidentiality. It also supports compliance with complex security policies and reduces the risk of accidental or unauthorized changes to critical attributes.
             * Attributes to consider include customizable role-based permissions and dynamic attribute tagging. For example, a tool that allows a user to view system-level parameters but restricts editing of cost-sensitive attributes ensures secure, efficient collaboration while protecting proprietary data.
             */

            attribute redefines weight = 1.0 / size(criteria_3_subcriteria);
        }

        requirement <'C3.6'> access_read_only defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support read-only or view only capabilities? */
            doc Context
            /* Supporting "read-only" or view-only capabilities in digital engineering tools is important to ensure that users can access critical information without the risk of accidental or unauthorized modifications. It enhances data integrity and security while promoting collaboration by allowing broader access to information for stakeholders who do not require editing permissions. This feature also simplifies compliance with governance and regulatory standards by controlling who can modify versus merely view data.
             * Attributes to consider include role-based access configurations that differentiate between editing and viewing permissions. For example, a tool that allows stakeholders to review system models in a read-only mode ensures transparency and collaboration without compromising data accuracy or security.
             */

            attribute redefines weight = 1.0 / size(criteria_3_subcriteria);
        }

        requirement <'C3.7'> access_group defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool have user group access control? */
            doc Context
            /* User group access control in digital engineering tools is crucial for efficiently managing permissions across teams, ensuring that users only access data relevant to their roles. This feature simplifies administration by allowing permissions to be assigned at the group level rather than individually, reducing the risk of misconfigurations. It also enhances security and collaboration by enabling teams to work within their defined boundaries while safeguarding sensitive information.
             * Attributes to consider include customizable group definitions and hierarchical access levels. For example, a tool that grants "engineering" teams edit access to design models while restricting "management" teams to read-only access ensures secure, role-appropriate collaboration.
             */

            attribute redefines weight = 1.0 / size(criteria_3_subcriteria);
        }
    }

    requirement <C4> environment defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Medium
            ?    valid_criteria
            else null {
        doc Criteria
        /* What computing environments does the tool support? */
        doc Context
        /* Tools don't exist in isolation.  This criteria category digs into how well a potential tool integrates with your organization's existing technology ecosystem.  Will it play nicely with your current operating systems, cloud platforms, or on-premise servers?  A tool that clashes with your existing infrastructure can lead to headaches, compatibility issues, and even security vulnerabilities. Evaluating supported computing environments upfront ensures a smooth transition and prevents unexpected roadblocks down the line. */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_4_subcriteria = ('C4.1', 'C4.2', 'C4.3', 'C4.4', 'C4.5', 'C4.6');

        requirement <'C4.1'> env_database defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support the organization's database solutions? */
            doc Context
            /* Ensuring a digital engineering tool supports the organization’s database solutions is vital for seamless integration, minimizing disruptions, and leveraging existing IT infrastructure. It enables efficient data exchange, storage, and retrieval, reducing the need for costly customizations or additional tools. This compatibility also enhances scalability and ensures the tool can operate within the organization’s broader ecosystem.
             * Attributes to consider include support for commonly used database types (e.g., SQL, NoSQL) and compatibility with the organization’s existing database management systems. For example, a tool that integrates with an organization’s Oracle database allows centralized access to engineering data while maintaining consistency across projects.
             */

            attribute redefines weight = 1.0 / size(criteria_4_subcriteria);
        }

        requirement <'C4.2'> env_mobile defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support mobile devices and operating systems? */
            doc Context
            /* Support for mobile devices and operating systems in digital engineering tools is important to enable flexibility and accessibility for users who need to work remotely or on the go. It enhances productivity by allowing stakeholders to review data, collaborate, and make decisions in real time, regardless of location. This capability also supports dynamic workflows, ensuring that critical updates or reviews can happen without delay.
             * Attributes to consider include compatibility with major mobile operating systems (e.g., iOS, Android) and responsive user interfaces. For example, a tool that provides a mobile app for accessing design models ensures that engineers and managers can review project updates during field operations or travel.
             */

            attribute redefines weight = 1.0 / size(criteria_4_subcriteria);
        }

        requirement <'C4.3'> env_other_products defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool require any other products to operate properly? */
            doc Context
            /* Considering whether a digital engineering tool requires additional products to operate is important to assess compatibility, cost, and implementation complexity. Tools that depend on external software may increase expenses and complicate integration, potentially creating delays or limitations in functionality. Identifying these dependencies ensures that the organization can plan for and support the required infrastructure without disrupting workflows.
             * Attributes to consider include the tool’s compatibility with existing software ecosystems and any licensing requirements for third-party products. For example, a tool that requires a specific database management system, like Microsoft SQL Server, might incur additional costs and necessitate infrastructure adjustments.
             */

            attribute redefines weight = 1.0 / size(criteria_4_subcriteria);
        }

        requirement <'C4.4'> env_network defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support the organization's networking solutions? */
            doc Context
            /* Ensuring a digital engineering tool supports the organization’s networking and desktop administration solutions is critical for seamless communication, data sharing, and operational efficiency. It enables the tool to integrate smoothly within existing IT infrastructure, reducing setup costs and compatibility issues. This alignment also ensures secure and reliable access to data across the organization, supporting collaborative workflows.
             * Attributes to consider include compatibility with VPNs, cloud services, and on-premises network configurations. For example, a tool that integrates with the organization’s secure VPN allows remote teams to access project data without compromising network security.
             */

            attribute redefines weight = 1.0 / size(criteria_4_subcriteria);
        }

        requirement <'C4.5'> env_cloud defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Is the tool available on a cloud platform? */
            doc Context
            /* Availability on a cloud platform is important for digital engineering tools as it provides scalability, enabling teams to access resources and data from anywhere with an internet connection. Cloud-based tools support real-time collaboration, reducing delays and improving efficiency in distributed teams. They also minimize infrastructure costs and maintenance efforts by leveraging the provider’s resources and updates.
             * Attributes to consider include compatibility with major cloud providers (e.g., AWS, Azure) and support for secure data storage. For example, a tool hosted on a cloud platform like AWS allows global project teams to collaborate on system designs without relying on local servers.
             */

            attribute redefines weight = 1.0 / size(criteria_4_subcriteria);
        }

        requirement <'C4.6'> env_multiple_OS defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Can the tools be used on multiple operating systems? */
            doc Context
            /* Support for multiple operating systems in digital engineering tools is crucial to accommodate diverse user preferences and existing IT infrastructures, enhancing accessibility and flexibility. It ensures that teams using different platforms, such as Windows, macOS, or Linux, can collaborate seamlessly without compatibility issues. This capability also future-proofs the tool, enabling its use across evolving organizational needs and systems.
             * Attributes to consider include compatibility with major operating systems and consistent functionality across platforms. For example, a tool that works equally well on Windows for engineers and macOS for designers ensures smooth collaboration without requiring additional software or hardware adjustments.
             */

            attribute redefines weight = 1.0 / size(criteria_4_subcriteria);
        }
    }

    requirement <C5> extensions defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_criteria
            else null {
        doc Criteria
        /* How extensible and scalable is the tool? */
        doc Context
        /* This criteria category focuses on the ability for the tool under consideration to be updated to support increasing scope and requirements. The tool needs to scale to support growth in users. Tools may allow the user to create new functions through open standards or programing languages. Some tools allow their back-end data model to be extended to include new data types. */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_5_subcriteria = 'C5.1';
        requirement <'C5.1'> ext_compatibility_frw_back defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support forward and backward compatibility? */
            doc Context
            /* Considering extensibility and scalability in digital engineering tool evaluations ensures the tool can adapt to future needs, integrate with evolving systems, and handle increasing complexity. Forward compatibility, like API support and modular architecture, allows the tool to incorporate new functionalities and technologies. Backward compatibility features, such as legacy data compatibility and configurable workflows, ensure the tool can interact with older systems or existing processes. These features reduce rework, enhance longevity, and support seamless system evolution. */

            attribute redefines weight = 1.0 / size(criteria_5_subcriteria);
        }
    }

    requirement <C6> VAULTIS defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_criteria
            else null {
        doc Criteria
        /* Does the tool provide VAULTIS Capabilities? */
        doc Context
        /* This criteria focuses on the ability of the tool being evaluated to support data that is visible, accessible, understandable, linked, trusted, inter operable and secure (VAULTIS). The intent behind this criteria is to evaluate the data management capabilities and features of a tool.  The aim of this criteria is to determine whether the tool can make data machine-readable and easily consumable, automate security classification, ensure data discoverability and visibility, provide unique identifiers and standardized metadata, support search and discovery services, facilitate data cataloging and sharing, offer an open API for controlled access, ensure interoperability of data, and utilize common data syntax and semantic metadata. Essentially, the goal is to assess the tool’s effectiveness in managing, securing, and making data accessible and usable across various platforms and contexts. */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_6_subcriteria = (
            'C6.1', 'C6.2', 'C6.3', 'C6.4', 'C6.5', 'C6.6', 'C6.7', 'C6.8', 'C6.9'
        );
        requirement <'C6.1'> VAULTIS_machine_readable defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool make data machine readable and available for consumption? */
            doc Context
            /* Considering whether a tool makes data machine-readable and available for consumption is crucial in digital engineering because it ensures interoperability, supports automation, and enables seamless integration across systems and teams. Features like standard data formats (e.g., XML, JSON), APIs for programmatic access, and compliance with open standards (like SysML or UAF) enable data to flow efficiently between tools and reduces manual effort. For example, a tool with a RESTful API can integrate real-time data into dashboards, while a tool supporting export in XML format allows models to be reused across various platforms. */

            attribute redefines weight = 1.0 / size(criteria_6_subcriteria);
        }

        requirement <'C6.2'> VAULTIS_classified_auto defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool provide auto-multilevel security classification marking of data vs manual? */
            doc Context
            /* Considering whether a tool provides automated multi-level security (MLS) classification marking versus manual classification is critical to ensuring data integrity, security, and efficiency in digital engineering workflows. Automated MLS marking reduces human error, ensures consistent adherence to security protocols, and streamlines processes in environments where data is shared across different classification levels. Examples of features that accomplish this include automated metadata tagging of files based on classification rules, integrated classification validation during data transfers, and real-time alerts or restrictions when attempting to access or share data outside the authorized classification level. */

            attribute redefines weight = 1.0 / size(criteria_6_subcriteria);
        }

        requirement <'C6.3'> VAULTIS_visibility defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool make data discoverable and visible whenever and wherever needed? */
            doc Context
            /* Considering whether a tool provides VAULTIS (Visibility, Accessibility, Understandability, Linked, Trustworthy, Inter operable, and Secure) capabilities is crucial in digital engineering tool evaluations because it ensures that data is discoverable and visible whenever and wherever needed. This enhances collaboration, reduces duplication, and improves decision-making. Features that support this include robust search functions, metadata tagging, real-time data synchronization, role-based access controls, and Application Programming Interface (API) integrations for seamless data sharing across systems. */

            attribute redefines weight = 1.0 / size(criteria_6_subcriteria);
        }

        requirement <'C6.4'> VAULTIS_uniqueID_metadata defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool provide unique identifiers and common metadata standards? */
            doc Context
            /* Unique identifiers and common metadata standards are critical in digital engineering tools to ensure traceability, interoperability, and data consistency across systems and stakeholders. These features enable efficient linking of data, reduce redundancy, and support integration with other tools. For example, features like automated ID assignment for requirements, adherence to standards like ISO 10303 (STEP) or IEEE 15288, and support for metadata tagging aligned with shared taxonomies enhance the tool’s ability to manage and share data effectively in a collaborative environment. */

            attribute redefines weight = 1.0 / size(criteria_6_subcriteria);
        }

        requirement <'C6.5'> VAULTIS_search defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support common services to search and discover data? */
            doc Context
            /* Considering whether a tool supports common services for searching and discovering data is crucial in digital engineering tool evaluations because it ensures that users can efficiently access relevant, accurate, and up-to-date information across systems. This capability enables better decision-making, reduces redundancies, and promotes collaboration in complex engineering environments. Features that support this include advanced search functions with filters, metadata tagging for improved organization, semantic search for understanding context, and integrations with federated data sources for unified access. */

            attribute redefines weight = 1.0 / size(criteria_6_subcriteria);
        }

        requirement <'C6.6'> VAULTIS_cataloging defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support data source cataloging for access and sharing? */
            doc Context
            /* Considering whether a tool supports data source cataloging for access and sharing is critical in digital engineering tool evaluations or trade studies because it ensures that stakeholders can efficiently locate, access, and share data across the digital ecosystem. This capability promotes collaboration, reduces duplication of effort, and supports decision-making by enabling traceability and consistent data usage. Examples of features include metadata tagging, advanced search functionality, data lineage tracking, integration with data repositories, and role-based access controls. */

            attribute redefines weight = 1.0 / size(criteria_6_subcriteria);
        }

        requirement <'C6.7'> VAULTIS_api defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool include an open, documented API standard for controlled access and sharing? */
            doc Context
            /* In digital engineering tool evaluations, it is critical to consider whether the tool includes an open, documented application programming interface (API) standard for controlled access and sharing to ensure interoperability, scalability, and data integration across systems. Such APIs enable seamless data exchange, automation, and collaboration while maintaining security and version control. Examples of features supporting this include RESTful APIs for data querying and updates, support for open data exchange standards like JSON, XML, or STEP, and authentication mechanisms such as OAuth 2.0 for secure access. These capabilities enhance the tool’s adaptability within diverse digital ecosystems. */

            attribute redefines weight = 1.0 / size(criteria_6_subcriteria);
        }

        requirement <'C6.8'> VAULTIS_inoperability defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Can the tool be interoperable with data that can be easily discovered, linked, retrieved, audited, trusted, understood, and referenced? */
            doc Context
            /* Considering interoperability with data that is easily discovered, linked, retrieved, audited, trusted, understood, and referenced (VAULTIS) ensures that a digital engineering tool supports seamless data exchange and traceability across systems, which is critical for collaboration and decision-making. Features that accomplish this include adherence to open standards like ISO 10303 (STEP) or OSLC, APIs for data integration, metadata tagging for discovery, audit logs for traceability, and user-friendly interfaces with clear visualizations to enhance understanding and trust. Tools with these capabilities reduce inefficiencies and support a connected, transparent digital engineering ecosystem */

            attribute redefines weight = 1.0 / size(criteria_6_subcriteria);
        }

        requirement <'C6.9'> VAULTIS_syntax defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool use common data syntax for the same data types and include semantic metadata with data assets? */
            doc Context
            /* Considering whether a tool uses common data syntax and includes semantic metadata is crucial in digital engineering tool evaluations because they help ensure interoperability, data consistency, and reusability across systems and teams. Common data syntax reduces errors during data exchanges, while semantic metadata enhances context and meaning, enabling better integration and automation. Features to look for include support for standard data schemas (e.g., XML, JSON), compliance with frameworks like ISO 10303 (STEP), and the ability to tag data with ontology-based metadata for semantic clarity. */

            attribute redefines weight = 1.0 / size(criteria_6_subcriteria);
        }
    }

    requirement <C7> collaboration defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_criteria
            else null {
        doc Criteria
        /* Does the tool facilitate collaboration? */
        doc Context
        /* This criteria focuses on evaluating the collaboration capabilities of an engineering tool.  This aim is to determine whether the tool can maintain data integrity during multiple-user access, support various privileges for data sharing, notify users of data changes, and facilitate brainstorming, agile workflows, and diagramming. The evaluation also considers the ability to support data sharing across integrated development environments (IDEs), simultaneous multi-user access, controlled notifications, activity and task planning, and day-to-day collaboration activities such as event registration and automated communication. The goal is to ensure the tool effectively supports collaborative efforts and enhances productivity through automated workflows and comprehensive collaboration features. */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_7_subcriteria = ('C7.1', 'C7.2', 'C7.3', 'C7.4', 'C7.5', 'C7.6', 'C7.7', 'C7.8');
        requirement <'C7.1'> collab_integrity defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool maintain data integrity and data management during multiple user access? */
            doc Context
            /* Data Integrity and Management criteria ensures the accuracy, consistency, and reliability of data while allowing concurrent access by multiple users. This capability performs managing, updating, and analyzing data in real time, supporting collaborative workflows across teams or organizations. Key features include rules for data validation, ensuring that all entries adhere to predefined formats and standards. Automatically detects data inconsistencies, duplicates, or errors, maintains comprehensive audit trails to track changes and identify unauthorized modifications. Supports locking sections for multiple users to make changes at the same time. */

            attribute redefines weight = 1.0 / size(criteria_7_subcriteria);
        }

        requirement <'C7.2'> collab_privileges defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support a variety of privileges for sharing data? */
            doc Context
            /* Privileged access in a tool is designed to enable secure and efficient data sharing while accommodating different levels of user privileges. This type of capability ensures that sensitive data is accessible only to authorized individuals based on predefined roles, enhancing both security and operational flexibility. */

            attribute redefines weight = 1.0 / size(criteria_7_subcriteria);
        }

        requirement <'C7.3'> collab_notify defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool notify you when data is changed? */
            doc Context
            /* Data change notification is a functionality within a software tool that actively monitors data for updates or modifications and alerts users when changes occur. This feature ensures that stakeholders remain informed about critical updates in real time, enhancing collaboration, decision-making, and responsiveness. */

            attribute redefines weight = 1.0 / size(criteria_7_subcriteria);
        }

        requirement <'C7.4'> collab_ideas defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool facilitate brainstorming and ideation? */
            doc Context
            /* A brainstorming and idea creation feature is a functionality within a software tool designed to foster collaboration and creativity, enabling teams or individuals to generate, organize, and refine ideas effectively. This feature serves as a virtual workspace for innovation, providing the tools and structure necessary to support creative thinking and problem-solving. */

            attribute redefines weight = 1.0 / size(criteria_7_subcriteria);
        }

        requirement <'C7.5'> collab_sharing_IDE defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support sharing of data from within and across IDEs? */
            doc Context
            /* Data sharing for Integrated Development Environments (IDEs) is a specialized software feature or plugin designed to enable seamless sharing of data, code snippets, or project assets both within and across IDEs. It facilitates collaboration among developers, allowing them to work more efficiently by breaking down silos and streamlining communication. */

            attribute redefines weight = 1.0 / size(criteria_7_subcriteria);
        }

        requirement <'C7.6'> collab_multiple_users defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support simultaneous use with multiple users in multiple locations? */
            doc Context
            /* Evaluating a tool's ability to support simultaneous use by multiple users in multiple locations is crucial for enabling real-time collaboration, enhancing productivity, and ensuring seamless communication across distributed teams. This capability allows team members to work together efficiently, regardless of their geographical locations, which is essential in today's globalized work environment. It also helps in maintaining consistency and coherence in project execution, as all users can access and update information simultaneously. Possible attributes to consider include real-time synchronization, user access controls, and compatibility with various devices and platforms. For example, a tool that supports live editing and instant updates can significantly improve the efficiency and effectiveness of remote teamwork */

            attribute redefines weight = 1.0 / size(criteria_7_subcriteria);
        }

        requirement <'C7.7'> collab_task_planning defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support activity and task planning? */
            doc Context
            /* Evaluating a tool's ability to support activity and task planning is essential for ensuring efficient project management, resource allocation, and timely completion of tasks. This capability helps teams to organize their work, set priorities, and track progress, which is crucial for meeting deadlines and achieving project goals. It also facilitates coordination among team members, reducing the risk of overlapping efforts and missed tasks. Possible attributes to consider include user-friendliness, integration with calendars, and the ability to assign tasks and set deadlines. For example, a tool that allows users to create detailed task lists, assign responsibilities, and monitor progress can significantly enhance team productivity and project outcomes. */

            attribute redefines weight = 1.0 / size(criteria_7_subcriteria);
        }

        requirement <'C7.8'> collab_commenting defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support commenting? */
            doc Context
            /* Evaluating a tool's ability to support user comments is crucial for fostering collaboration, capturing feedback, and ensuring traceability of decisions. This capability enhances communication among team members, facilitates knowledge sharing, and helps in tracking the evolution of ideas and requirements. It also aids in maintaining a clear record of discussions, which is essential for future reference and accountability. Possible attributes to consider include ease of use, integration with other tools, and the ability to tag or categorize comments. For example, a tool that allows users to easily add, search, and filter comments can significantly improve project management and team collaboration. */

            attribute redefines weight = 1.0 / size(criteria_7_subcriteria);
        }
    }

    requirement <C8> audit_version_control defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_criteria
            else null {
        doc Criteria
        /* Does the tool support automated version control and auditing? */
        doc Context
        /* This criteria category focuses on evaluating a tool’s capabilities in managing and controlling versions. The goal is to assess the tool’s effectiveness in ensuring data integrity, compliance, and streamlined work flow management through automation and robust version control features. Version control also helps when collaborating on the same project simultaneously without overwriting each other's work, fostering better collaboration and coordination. */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_8_subcriteria = ('C8.1', 'C8.2', 'C8.3');
        requirement <'C8.1'> audit_auto defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool automatically audit against a defined standard? */
            doc Context
            /* When evaluating a tool's ability to automatically audit against a defined standard, it is crucial to assess its accuracy, compliance, and integration capabilities. Such capabilities ensure that all changes are tracked and verified, reducing errors and maintaining consistency. This is important because it enhances traceability, accountability, and regulatory compliance, ultimately improving the overall quality and reliability of the engineering process. Possible attributes for consideration include audit trail completeness, real-time monitoring, and compatibility with existing standards. For example, a tool that can automatically generate audit reports in compliance with ISO 9001 standards would be highly beneficial. Also important are the tools ability to allow the user to enter and tailor their own standards and establish custom rules for automated checking. */

            attribute redefines weight = 1.0 / size(criteria_8_subcriteria);
        }

        requirement <'C8.2'> audit_branching_merging defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support branching and merging? */
            doc Context
            /* In a digital engineering ecosystem tool evaluation, it is crucial to assess a tool's ability to support branching and merging to ensure efficient version control and auditing. These capabilities allow teams to work on multiple versions of a project simultaneously, integrate changes seamlessly, and maintain a comprehensive history of modifications, which is essential for traceability and accountability. This ensures that any changes can be tracked, reviewed, and reverted if necessary, enhancing collaboration and reducing errors. Possible attributes for consideration include the tool's ability to handle complex branching strategies, merge conflict resolution, and integration with other version control systems. For example, a tool like Git provides robust branching and merging capabilities, allowing teams to manage large-scale projects with ease. */

            attribute redefines weight = 1.0 / size(criteria_8_subcriteria);
        }

        requirement <'C8.3'> audit_auto_checking defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support auto checking? */
            doc Context
            /* In a digital engineering ecosystem tool evaluation, it's crucial to assess a tool's ability to create rules for auto-checking to ensure automated version control and auditing. This capability enhances consistency, reduces human error, and ensures compliance with standards. It also streamlines the development process, saving time and resources.  Possible attributes for consideration include flexibility, ease of rule creation, integration with existing systems, and scalability. For example, a tool that allows engineers to set up automated checks for compliance with industry standards can significantly reduce the time spent on manual audits. */

            attribute redefines weight = 1.0 / size(criteria_8_subcriteria);
        }
    }

    requirement <C9> reuse defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_criteria
            else null {
        doc Criteria
        /* Does the ecosystem facilitate product reuse? */
        doc Context
        /* This criteria category focuses on evaluating a tool’s capabilities in managing and facilitating the reuse of products within an ecosystem. Specifically, the questions aim to determine whether the tool supports product reuse, captures the pedigree of reused work products, facilitates control over the point of origin of products, provides data tagging, and ensures automated traceability to the original product. The goal for this criteria category is to assess the tool’s effectiveness in promoting efficient reuse, maintaining product integrity, and ensuring comprehensive traceability and control throughout the product lifecycle. */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_9_subcriteria = ('C9.1', 'C9.2', 'C9.3', 'C9.4');
        requirement <'C9.1'> reuse_pedigree defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool capture pedigree for reused work product? */
            doc Context
            /* Evaluating a tool's ability to capture model and data pedigree information is crucial for ensuring traceability, accountability, and quality control. This capability supports the curation process by maintaining a clear history of data and model changes, which is essential for effective reuse and collaboration. It also helps in verifying the authenticity and reliability of the data, thereby enhancing decision-making processes. Possible attributes for consideration include version control, metadata management, and audit trails. For example, a tool that tracks the origin and modifications of a model can help identify the most reliable version for future projects. */

            attribute redefines weight = 1.0 / size(criteria_9_subcriteria);
        }

        requirement <'C9.2'> reuse_origin defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool facilitate point of origin product control? */
            doc Context
            /* Evaluating a tool's ability to facilitate point of origin product control is crucial for ensuring the integrity and traceability of data throughout the product lifecycle. This capability supports efficient reuse of components by maintaining accurate version control and ensuring that all modifications are documented and traceable. It also enhances collaboration across teams by providing a single source of truth, reducing errors and inconsistencies. Possible attributes for consideration include version control, audit trails, and access management. For example, a tool with robust version control can track changes made to a digital model, ensuring that all team members are working with the most up-to-date information. */

            attribute redefines weight = 1.0 / size(criteria_9_subcriteria);
        }

        requirement <'C9.3'> reuse_tagging defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool provide data tagging? */
            doc Context
            /* Evaluating a tool's ability to facilitate data tagging is crucial for ensuring efficient data reuse, as it enhances data discoverability, consistency, and interoperability. Effective data tagging allows for easier retrieval and integration of data across different projects, saving time and reducing redundancy. This capability is important because it supports better decision-making and collaboration within the digital engineering ecosystem. Possible attributes for consideration include the tool's flexibility in defining custom tags, its support for metadata standards, and its integration with other systems. For example, a tool that allows engineers to tag simulation data with specific project identifiers and parameters can streamline the process of finding relevant datasets for future simulations. */

            attribute redefines weight = 1.0 / size(criteria_9_subcriteria);
        }

        requirement <'C9.4'> reuse_auto_traceability defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool provide automated traceability to the original product? */
            doc Context
            /* Evaluating a tool's ability to provide automated traceability to the origin of a product is crucial for ensuring accurate documentation, enhancing accountability, and facilitating efficient reuse of components. This capability helps in maintaining a clear lineage of product development, which is essential for quality control and compliance with industry standards. Additionally, it supports the identification and reuse of proven components, reducing development time and costs. Possible attributes for consideration include the tool's ability to track changes, maintain version history, and integrate with other systems. For example, a tool that automatically logs all modifications and links them to specific requirements can significantly streamline the verification and validation process. */

            attribute redefines weight = 1.0 / size(criteria_9_subcriteria);
        }
    }

    requirement <C10> cybersecurity defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_criteria
            else null {
        doc Criteria
        /* Does the tool support cybersecurity requirements? */
        doc Context
        /* This criteria category focuses on evaluating a tool’s ability to comply with cybersecurity requirements as required in the NIST cybersecurity framework. */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_10_subcriteria = 'C10.1';
        requirement <'C10.1'> cyber_NIST defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small
                ?    valid_criteria
                else null {
            doc Criteria
            /* Is the tool compliant with the NIST Cybersecurity Framework? */
            doc Context
            /* Supportive tools enhance productivity by enabling users to perform tasks efficiently and with ease. They improve user satisfaction and engagement, leading to higher adoption rates and better outcomes. Additionally, they reduce the need for extensive training and support, saving time and resources.  Possible attributes to consider include functionality, usability, accessibility, flexibility, and support. For example, a project management tool with an intuitive interface and robust support resources can significantly boost team productivity and collaboration. */

            attribute redefines weight = 1.0 / size(criteria_10_subcriteria);
        }
    }

    requirement <C11> user_interface defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_criteria
            else null {
        doc Criteria
        /* Does the tool provide a user-friendly experience? */
        doc Context
        /* This criteria category focuses on evaluating the usability and flexibility of a tool. Specifically, these criteria aim to determine whether the tool offers a user-friendly experience, supports automated scripting, allows for the customization and configuration of templates and automated reports, and can handle multiple simultaneous sessions. Essentially, the goal is to assess the tool’s effectiveness in providing an intuitive interface, facilitating automation, offering customization options, and supporting concurrent usage to enhance overall user productivity and satisfaction. */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_11_subcriteria = ('C11.1', 'C11.2', 'C11.3');
        requirement <'C11.1'> ui_auto_scripting defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool allow automated scripting? */
            doc Context
            /* Automated scripting capabilities in a Digital Engineering Ecosystem tool are crucial for enhancing efficiency, consistency, and scalability of engineering processes. They allow for repetitive tasks to be automated, reducing human error and freeing up engineers to focus on more complex tasks. Additionally, these capabilities enable seamless integration with other tools and systems, fostering a more cohesive workflow. Possible attributes to consider include scripting language support, ease of use, flexibility, and integration capabilities. For example, a tool that supports Python scripting can easily automate data analysis tasks, significantly speeding up the engineering workflow. */

            attribute redefines weight = 1.0 / size(criteria_11_subcriteria);
        }

        requirement <'C11.2'> ui_templates_reports defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool allow for templates and automated reports to be customized/configured? */
            doc Context
            /* In a Digital Engineering Ecosystem tool evaluation, it is important to assess a tool's ability to be customized and configured to ensure it can meet specific project requirements and integrate seamlessly with existing workflows. Customization and configuration capabilities allow for greater flexibility, improved efficiency, and tailored user experiences, which can significantly enhance productivity and user satisfaction. Additionally, these features enable the tool to adapt to evolving needs and technological advancements, ensuring long-term usability and relevance. Possible attributes for consideration include user interface customization, API integration, and configuration options for workflows and data management. For example, a tool that allows for custom dashboard creation and API integration can provide a more personalized and efficient user experience. */

            attribute redefines weight = 1.0 / size(criteria_11_subcriteria);
        }

        requirement <'C11.3'> ui_multi_sessions defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool provide a single user multiple session capability? */
            doc Context
            /* Evaluating a tool's ability to support a single user multiple session capability is crucial for ensuring seamless workflow continuity, enhancing productivity, and enabling efficient task management. This capability allows users to switch between different tasks or projects without losing progress, which is essential for complex engineering tasks. It also supports collaboration by allowing users to maintain multiple active sessions for different purposes. Possible attributes for consideration include session persistence, user authentication, and session management features. For example, an engineer working on multiple design projects can benefit from a tool that allows them to switch between sessions without losing their work. */

            attribute redefines weight = 1.0 / size(criteria_11_subcriteria);
        }
    }

    requirement <C12> support defined by Criteria_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_criteria
            else null {
        doc Criteria
        /* Does the tool provide product support? */
        doc Context
        /* This criteria category focuses on evaluating the overall support features of a tool. Specifically, the questions aim to determine whether the tool offers product support, supports automated installation, provides flexible licensing options, and has online support available. Additionally, they assess whether the tool can generate automated reports (such as metrics, usage, health, status, and reminders) and includes user templates and examples for learning purposes. Essentially, the goal is to assess the tool’s effectiveness in providing comprehensive support, ease of installation, flexible usage terms, and helpful resources to enhance user experience and productivity. */

        attribute redefines weight = 1.0 / size(criteria_list);

        criteria_12_subcriteria = ('C12.1', 'C12.2', 'C12.3', 'C12.4', 'C12.5', 'C12.6', 'C12.7');

        requirement <'C12.1'> supp_auto_install defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support automated installation? */
            doc Context
            /* Product support capabilities for automated installation in a Digital Engineering Ecosystem ensure smooth deployment and maintenance, reducing downtime and operational costs. This support enhances user experience by providing timely updates and troubleshooting, which is crucial for system reliability. It also facilitates scalability, allowing the system to grow and adapt to changing needs. Important attributes to consider include ease of use, compatibility, scalability, error handling, integration, and support. For example, a tool with automated installation might offer a one-click setup process that integrates seamlessly with continuous integration/continuous delivery pipelines. */

            attribute redefines weight = 1.0 / size(criteria_12_subcriteria);
        }

        requirement <'C12.2'> supp_licensing defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support flexible licensing? */
            doc Context
            /* Flexibility in a product's licensing approach is important because it allows organizations to scale usage according to their needs, adapt to changing project requirements, and manage costs effectively. This flexibility ensures that the tool can be used by different teams and departments without unnecessary restrictions, fostering innovation and collaboration. Additionally, it helps in accommodating various business models and operational workflows. Attributes for consideration include license duration, user count, feature access, and pricing models. For example, a company might choose a subscription-based license that allows for monthly adjustments in user count and feature access to align with project demands. */

            attribute redefines weight = 1.0 / size(criteria_12_subcriteria);
        }

        requirement <'C12.3'> supp_online defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool (and/or its developer) provide online support? */
            doc Context
            /* Online support is important to consider in a Digital tool evaluation because it ensures users have access to immediate assistance, reducing downtime and enhancing productivity. It also provides a platform for continuous learning and troubleshooting, which can improve user proficiency and satisfaction. Additionally, online support can offer updates and resources that keep the tool relevant and efficient. Possible attributes for consideration include response time, availability, and the quality of resources provided. For example, a tool with 24/7 live chat support can significantly benefit global teams working across different time zones. */

            attribute redefines weight = 1.0 / size(criteria_12_subcriteria);
        }

        requirement <'C12.4'> supp_auto_reporting defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool support automated reporting (e.g., metrics, use, health, status, reminders to users, etc.)? */
            doc Context
            /* Typical capabilities of tools that support automated reporting in a digital engineering ecosystem include generating metrics, tracking usage, monitoring health and status, and sending reminders to users. These capabilities are important because they provide real-time insights, enhance decision-making, and ensure the system's optimal performance. Automated reporting also helps in identifying issues early, improving efficiency, and maintaining compliance. Possible attributes for consideration include customization options, integration with other systems, and the frequency of updates. For example, a tool that automatically generates weekly status reports can help project managers stay informed and make timely adjustments. */

            attribute redefines weight = 1.0 / size(criteria_12_subcriteria);
        }

        requirement <'C12.5'> supp_examples defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool include user templates and examples to learn from? */
            doc Context
            /* Effective product support in a digital engineering ecosystem tool evaluation includes user templates, examples, and comprehensive help resources. These capabilities are crucial as they provide users with practical guidance, accelerate the learning curve, and enhance overall productivity. By offering clear, context-specific instructions and real-world examples, users can better understand and use the tool's features, leading to more efficient and accurate outcomes.  Attributes to consider include the quality of documentation, availability of tutorials, and responsiveness of customer support. For example, a tool that offers interactive tutorials and a robust knowledge base can significantly improve user proficiency and satisfaction. */

            attribute redefines weight = 1.0 / size(criteria_12_subcriteria);
        }

        requirement <'C12.6'> supp_longterm defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Is the tool compatible with long term product support? */
            doc Context
            /* Long-term product support ensures that tools remain functional, secure, and up-to-date, which is crucial for maintaining productivity and minimizing disruptions. It also provides users with confidence that their investment will be protected and supported over time. Additionally, consistent support can lead to better user satisfaction and loyalty. Attributes to consider include the availability of updates, customer service quality, and the frequency of security patches. For example, a tool that offers regular updates and responsive customer service can significantly enhance user experience and trust */
            attribute redefines weight = 1.0 / size(criteria_12_subcriteria);
        }

        requirement <'C12.7'> supp_LOTAR defined by Criteria_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_criteria
                else null {
            doc Criteria
            /* Does the tool provide LOTAR capabilities? */
            doc Context
            /* In a DE ecosystem, supporting long-term archival and retrieval (LOTAR) is crucial for preserving data integrity, ensuring regulatory compliance, and facilitating future access to historical data. This capability helps organizations maintain comprehensive records of essential for audits, legal requirements, and knowledge transfer. Additionally, it supports the reuse of data in future projects, enhancing efficiency and innovation. Key attributes to consider include data format standardization, metadata management, and robust security measures. For example, implementing a system that uses standardized file formats and metadata tagging can significantly streamline the retrieval process and ensure data remains accessible and usable over time. */

            attribute redefines weight = 1.0 / size(criteria_12_subcriteria);
        }
    }
}
