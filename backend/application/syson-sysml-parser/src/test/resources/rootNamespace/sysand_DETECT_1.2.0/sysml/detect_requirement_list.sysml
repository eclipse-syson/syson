package DETECT_Requirement_List {
    doc Acknowledgement
    /* This file contains content from DETECT model file for SysML v1.6 by Daniel Hettema, OUSD (R&E).
     * Original content is dedicated to the Public Domain.
     * Modifications Copyright (c) 2025 Sensmetry.
     */

    doc Description
    /* This package provides a listing of all the DE Ecosystem requirements with weights and the mapping to DE ecosystem size. */

    private import SequenceFunctions::*;
    private import DETECT_Input;
    private import DETECT_Sizing::*;

    requirement_list = (R1, R2, R3, R4, R5, R6);

    requirement <R1> tech_and_inno defined by DE_Ecosystem_req_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_requirement
            else null {
        doc Description
        /* Advanced Technologies and Innovation */

        attribute redefines weight = 1.0 / size(requirement_list);

        requirement_1_subrequirements = ('R1.1', 'R1.2');

        requirement <'R1.1'> inno defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Innovation */

            attribute redefines weight = 1.0 / size(requirement_1_subrequirements);

            requirement_1_1_subrequirements = ('R1.1.1', 'R1.1.2', 'R1.1.3', 'R1.1.4');

            requirement <'R1.1.1'> inno_prototyping defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall support prototyping activities for ecosystem improvements */

                attribute redefines weight = 1.0 / size(requirement_1_1_subrequirements);
            }
            requirement <'R1.1.2'> inno_prototyping_apps defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall accommodate intelligent applications that enable rigorous prototyping to facilitate digital to physical prototypes and proof of concept activities */

                attribute redefines weight = 1.0 / size(requirement_1_1_subrequirements);
            }
            requirement <'R1.1.3'> inno_development_apps defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall accommodate intelligent applications that enable rigorous development of early and often automated testing of system performance requirements */

                attribute redefines weight = 1.0 / size(requirement_1_1_subrequirements);
            }
            requirement <'R1.1.4'> inno_traceability defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall accommodate intelligent applications that enable cross-domain traceability of design thread, analytical thread and proof of concept analysis */

                attribute redefines weight = 1.0 / size(requirement_1_1_subrequirements);
            }
        }

        requirement <'R1.2'> tech defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Technologies */

            attribute redefines weight = 1.0 / size(requirement_1_subrequirements);

            requirement_1_2_subrequirements = ('R1.2.1', 'R1.2.2');

            requirement <'R1.2.1'> tech_sw defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide the software tools, software libraries, software builds, and automated testing capabilities needed for development and operations (DevOps) process implementations */

                attribute redefines weight = 1.0 / size(requirement_1_2_subrequirements);
            }
            requirement <'R1.2.2'> tech_rnd defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide advanced technologies to support leading-edge research and development capabilities such as, but not limited to, big data analytics, machine learning, artificial intelligence, ontology, virtual reality, augmented reality, and 5G technologies */

                attribute redefines weight = 1.0 / size(requirement_1_2_subrequirements);
            }
        }
    }

    requirement <R2> collab defined by DE_Ecosystem_req_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_requirement
            else null {
        doc Description
        /* Collaboration */

        attribute redefines weight = 1.0 / size(requirement_list);

        requirement_2_subrequirements = ('R2.1', 'R2.2');

        requirement <'R2.1'> resources defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Resources */

            attribute redefines weight = 1.0 / size(requirement_2_subrequirements);

            requirement_2_1_subrequirements = ('R2.1.1', 'R2.1.2', 'R2.1.3');

            requirement <'R2.1.1'> resources_notifications defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide automated notifications capability to all applicable change events */

                attribute redefines weight = 1.0 / size(requirement_2_1_subrequirements);
            }

            requirement <'R2.1.2'> resources_connections defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall be sized for growth in the number of connections and types of accesses (VM, web portal, others) to support (TBD) users */

                attribute redefines weight = 1.0 / size(requirement_2_1_subrequirements);
            }

            requirement <'R2.1.3'> resources_sharing defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall enable sharing of models, data, and simulations from authorized users and engineering disciplines */

                attribute redefines weight = 1.0 / size(requirement_2_1_subrequirements);
            }
        }

        requirement <'R2.2'> users defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Users */

            attribute redefines weight = 1.0 / size(requirement_2_subrequirements);

            requirement_2_2_subrequirements = ('R2.2.1', 'R2.2.2', 'R2.2.3', 'R2.2.4', 'R2.2.5');

            requirement <'R2.2.1'> users_teleconferencing defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall support remote teleconferencing with messaging, audio, and video */

                attribute redefines weight = 1.0 / size(requirement_2_2_subrequirements);
            }

            requirement <'R2.2.2'> users_concurrent defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall be sized and maintained for (TBD) number of users for concurrent use of program identified capabilities and functions appropriate for the program. (e.g., data managers, data users, data creators) */

                attribute redefines weight = 1.0 / size(requirement_2_2_subrequirements);
            }

            requirement <'R2.2.3'> users_identify defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall be sized and maintained for the number of users to be 100% of identified program personnel */

                attribute redefines weight = 1.0 / size(requirement_2_2_subrequirements);
            }

            requirement <'R2.2.4'> users_network_access defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide network access to authenticated users, organizations, and stakeholders */

                attribute redefines weight = 1.0 / size(requirement_2_2_subrequirements);
            }

            requirement <'R2.2.5'> users_simulation_access defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide the access for authenticated users and stakeholders to execute simulations and perform data analytics */

                attribute redefines weight = 1.0 / size(requirement_2_2_subrequirements);
            }
        }
    }

    requirement <R3> data defined by DE_Ecosystem_req_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_requirement
            else null {
        doc Description
        /* Data */

        attribute redefines weight = 1.0 / size(requirement_list);

        requirement_3_subrequirements = ('R3.1', 'R3.2', 'R3.3');

        requirement <'R3.1'> exchange defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Exchange */

            attribute redefines weight = 1.0 / size(requirement_3_subrequirements);

            requirement_3_1_subrequirements = (
                'R3.1.1', 'R3.1.2', 'R3.1.3', 'R3.1.4', 'R3.1.5', 'R3.1.6', 'R3.1.7'
            );

            requirement <'R3.1.1'> exchange_data_association defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide a capability to associate data that has been generated by different engineering domains */

                attribute redefines weight = 1.0 / size(requirement_3_1_subrequirements);
            }

            requirement <'R3.1.2'> exchange_error_monitoring defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide monitoring of error detection and quality assessment of all data translation across engineering tools */

                attribute redefines weight = 1.0 / size(requirement_3_1_subrequirements);
            }

            requirement <'R3.1.3'> exchange_automation defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide a capability to automate data exchange within workflows of users */

                attribute redefines weight = 1.0 / size(requirement_3_1_subrequirements);
            }

            requirement <'R3.1.4'> exchange_standards defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall accommodate model exchange standards, published standards and procedures for interoperability among supported modeling and analysis tools */

                attribute redefines weight = 1.0 / size(requirement_3_1_subrequirements);
            }

            requirement <'R3.1.5'> exchange_format_translation defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide a capability to translate data formats between disparate engineering tools */

                attribute redefines weight = 1.0 / size(requirement_3_1_subrequirements);
            }

            requirement <'R3.1.6'> exchange_format_compatibility defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide the compatibility to receive, store, and utilize data in specified formats, from both legacy and new tools and sources */

                attribute redefines weight = 1.0 / size(requirement_3_1_subrequirements);
            }

            requirement <'R3.1.7'> exchange_data_access defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide access to metadata and supporting data for all data and information in the ecosystem */

                attribute redefines weight = 1.0 / size(requirement_3_1_subrequirements);
            }
        }

        requirement <'R3.2'> curation defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Curation */

            attribute redefines weight = 1.0 / size(requirement_3_subrequirements);

            requirement_3_2_subrequirements = (
                'R3.2.1', 'R3.2.2', 'R3.2.3', 'R3.2.4', 'R3.2.5', 'R3.2.6', 'R3.2.7', 'R3.2.8'
            );

            requirement <'R3.2.1'> curation_config_mng defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall contain formal configuration management for all models, simulations, analysis, and technical data as the authoritative-source-of-truth for the ecosystem */

                attribute redefines weight = 1.0 / size(requirement_3_2_subrequirements);
            }

            requirement <'R3.2.2'> auto_generated defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide capabilities to maintain measures and metrics for both program-specific and ecosystem performance measuring */

                attribute redefines weight = 1.0 / size(requirement_3_2_subrequirements);
            }

            requirement <'R3.2.3'> curation_discovery defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall accommodate discovery of models and associated data from outside the local ecosystem instantiation */

                attribute redefines weight = 1.0 / size(requirement_3_2_subrequirements);
            }

            requirement <'R3.2.4'> curation_library_custom defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall enable customization of library function executable applications */

                attribute redefines weight = 1.0 / size(requirement_3_2_subrequirements);
            }

            requirement <'R3.2.5'> curation_reuse defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall include the ability to curate models for reuse */

                attribute redefines weight = 1.0 / size(requirement_3_2_subrequirements);
            }

            requirement <'R3.2.6'> curation_audit defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide auditable technical data curation over a full life-cycle of platforms/systems for (TBD) years */

                attribute redefines weight = 1.0 / size(requirement_3_2_subrequirements);
            }

            requirement <'R3.2.7'> curation_library defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide a library function of executable applications available for access and reuse */

                attribute redefines weight = 1.0 / size(requirement_3_2_subrequirements);
            }

            requirement <'R3.2.8'> curation_data_storing defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall contain an organized data-storing structure for ease of discovery and navigation */

                attribute redefines weight = 1.0 / size(requirement_3_2_subrequirements);
            }
        }

        requirement <'R3.3'> storage defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Storage */

            attribute redefines weight = 1.0 / size(requirement_3_subrequirements);

            requirement_3_3_subrequirements = ('R3.3.1', 'R3.3.2', 'R3.3.3', 'R3.3.4');

            requirement <'R3.3.1'> storage_allocation defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide for sufficient storage size allocations to all engineering technical data, for all major life cycle phases, with (TBD)% margin expansion */

                attribute redefines weight = 1.0 / size(requirement_3_3_subrequirements);
            }

            requirement <'R3.3.2'> storage_expansion_capacity defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide (TBD)% storage expansion capacity for all data that is above and beyond the theoretical storage calculations needed for the ecosystem being developed */

                attribute redefines weight = 1.0 / size(requirement_3_3_subrequirements);
            }

            requirement <'R3.3.3'> storage_availability defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall make data available to all authorized users and ensure unmodified utilization on a recurring basis */

                attribute redefines weight = 1.0 / size(requirement_3_3_subrequirements);
            }
            requirement <'R3.3.4'> storage_critical_data defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall require the need to enter and store critical data one time */

                attribute redefines weight = 1.0 / size(requirement_3_3_subrequirements);
            }
        }
    }

    requirement <R4> environrment defined by DE_Ecosystem_req_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_requirement
            else null {
        doc Description
        /* Environment */

        attribute redefines weight = 1.0 / size(requirement_list);

        requirement_4_subrequirements = ('R4.1', 'R4.2', 'R4.3', 'R4.4');

        requirement <'R4.1'> licensing defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Licensing */

            attribute redefines weight = 1.0 / size(requirement_4_subrequirements);

            requirement_4_1_subrequirements = (
                'R4.1.1', 'R4.1.2', 'R4.1.3', 'R4.1.4', 'R4.1.5', 'R4.1.6'
            );

            requirement <'R4.1.1'> licensing_metrics defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide license and tool usage metrics */

                attribute redefines weight = 1.0 / size(requirement_4_1_subrequirements);
            }

            requirement <'R4.1.2'> licensing_warning_renewal defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall have warning report capabilities for approaching license renewal dates */

                attribute redefines weight = 1.0 / size(requirement_4_1_subrequirements);
            }

            requirement <'R4.1.3'> licensing_warning_limit defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall have warning report capabilities for approaching license capacity limits */

                attribute redefines weight = 1.0 / size(requirement_4_1_subrequirements);
            }

            requirement <'R4.1.4'> licensing_flexible_mng defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall employ flexible license management strategies for all shared software that supports user availability of .999 (TBR) */

                attribute redefines weight = 1.0 / size(requirement_4_1_subrequirements);
            }

            requirement <'R4.1.5'> licensing_warning_usage defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall have warning report capabilities for low usage of tool licenses */

                attribute redefines weight = 1.0 / size(requirement_4_1_subrequirements);
            }

            requirement <'R4.1.6'> licensing_sw defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall facilitate new software tool license allocations within (TBD) days of a request */

                attribute redefines weight = 1.0 / size(requirement_4_1_subrequirements);
            }
        }

        requirement <'R4.2'> visualization defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Visualization */

            attribute redefines weight = 1.0 / size(requirement_4_subrequirements);

            requirement_4_2_subrequirements = 'R4.2.1';

            requirement <'R4.2.1'> visual_capability defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall have a fully integrated applicable visualization and reporting capability */

                attribute redefines weight = 1.0 / size(requirement_4_2_subrequirements);
            }
        }

        requirement <'R4.3'> portability defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Portability */

            attribute redefines weight = 1.0 / size(requirement_4_subrequirements);

            requirement_4_3_subrequirements = ('R4.3.1', 'R4.3.2', 'R4.3.3', 'R4.3.4');

            requirement <'R4.3.1'> port_offline defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall support offline retrieval of TBD applicable data types and TBD applicable information formats */

                attribute redefines weight = 1.0 / size(requirement_4_3_subrequirements);
            }

            requirement <'R4.3.2'> port_export_directly defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall enable the export of data directly to applicable productivity software */

                attribute redefines weight = 1.0 / size(requirement_4_3_subrequirements);
            }

            requirement <'R4.3.3'> port_VMs defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide portability of applications through Virtual Machines (VMs) and other applicable virtual means */

                attribute redefines weight = 1.0 / size(requirement_4_3_subrequirements);
            }

            requirement <'R4.3.4'> port_export defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide capability to export prototyped ecosystem improvements for reuse */

                attribute redefines weight = 1.0 / size(requirement_4_3_subrequirements);
            }
        }

        requirement <'R4.4'> support defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Support */

            attribute redefines weight = 1.0 / size(requirement_4_subrequirements);

            requirement_4_4_subrequirements = (
                'R4.4.1', 'R4.4.2', 'R4.4.3', 'R4.4.4', 'R4.4.5', 'R4.4.6', 'R4.4.7'
            );

            requirement <'R4.4.1'> support_checkout defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide for a formal, automated checkout of all applications and data */

                attribute redefines weight = 1.0 / size(requirement_4_4_subrequirements);
            }

            requirement <'R4.4.2'> support_automation defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide and support growth of automation and autonomous artifact generation */

                attribute redefines weight = 1.0 / size(requirement_4_4_subrequirements);
            }

            requirement <'R4.4.3'> 'support_non-technical' defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall incorporate mechanisms to support integrated workflows for managing non-technical programmatic work in progress, separate from released work product */

                attribute redefines weight = 1.0 / size(requirement_4_4_subrequirements);
            }

            requirement <'R4.4.4'> auto_generated defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide documentation and reference guidance describing the physical and virtual bounds of the ecosystem for ease and efficient user understanding of the ecosystem */

                attribute redefines weight = 1.0 / size(requirement_4_4_subrequirements);
            }

            requirement <'R4.4.5'> support_query defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide a query capability to search, locate, and access programmatic and engineering products by all authenticated users and stakeholders */

                attribute redefines weight = 1.0 / size(requirement_4_4_subrequirements);
            }

            requirement <'R4.4.6'> support_technical defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall incorporate mechanisms to support integrated workflows for managing technical work in progress separate from released work product */

                attribute redefines weight = 1.0 / size(requirement_4_4_subrequirements);
            }

            requirement <'R4.4.7'> support_requirements defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall incorporate model centric workflows throughout the development life-cycle, from requirements definition, through requirements selloff and support of production, deployment, operation and sustainment life-cycle phases */

                attribute redefines weight = 1.0 / size(requirement_4_4_subrequirements);
            }
        }
    }

    requirement <R5> infrastructure defined by DE_Ecosystem_req_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_requirement
            else null {
        doc Description
        /* Infrastructure */

        attribute redefines weight = 1.0 / size(requirement_list);

        requirement_5_subrequirements = ('R5.1', 'R5.2', 'R5.3');

        requirement <'R5.1'> software defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Software */

            attribute redefines weight = 1.0 / size(requirement_5_subrequirements);

            requirement_5_1_subrequirements = ('R5.1.1', 'R5.1.2', 'R5.1.3');

            requirement <'R5.1.1'> sw_tools defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall accommodate implementation of commercial off-the-shelf, custom, open source, and free-ware tools */

                attribute redefines weight = 1.0 / size(requirement_5_1_subrequirements);
            }

            requirement <'R5.1.2'> sw_access_legacy defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall enable access to legacy modeling software and tools required to support engineering life-cycle activities */

                attribute redefines weight = 1.0 / size(requirement_5_1_subrequirements);
            }

            requirement <'R5.1.3'> sw_ATO defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall establish a mechanism to enable ATO reciprocity for widely adopted and approved software tools use */

                attribute redefines weight = 1.0 / size(requirement_5_1_subrequirements);
            }
        }

        requirement <'R5.2'> networks defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Networks */

            attribute redefines weight = 1.0 / size(requirement_5_subrequirements);

            requirement_5_2_subrequirements = ('R5.2.1', 'R5.2.2', 'R5.2.3', 'R5.2.4');

            requirement <'R5.2.1'> networks_secure defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide extensibility for secure connections to Federally Funded Research Centers (FFRDCs) */

                attribute redefines weight = 1.0 / size(requirement_5_2_subrequirements);
            }

            requirement <'R5.2.2'> networks_classified defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide extensibility with interoperability to both unclassified and classified Tri-Service and other defense networks */

                attribute redefines weight = 1.0 / size(requirement_5_2_subrequirements);
            }

            requirement <'R5.2.3'> networks_remote defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall support connectivity to remote, off-line, and geographically dispersed locations, throughout the development life-cycle and going into production, operations, and sustainment phases */

                attribute redefines weight = 1.0 / size(requirement_5_2_subrequirements);
            }

            requirement <'R5.2.4'> networks_industry_academia defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide extensibility with interoperability to applicable non-secure and secure connections to industry , academia and OCONUS networks */

                attribute redefines weight = 1.0 / size(requirement_5_2_subrequirements);
            }
        }

        requirement <'R5.3'> hardwre defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Hardware/Computing */

            attribute redefines weight = 1.0 / size(requirement_5_subrequirements);

            requirement_5_3_subrequirements = ('R5.3.1', 'R5.3.2', 'R5.3.3');

            requirement <'R5.3.1'> hw_productivity defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide local basic engineering productivity computing and analysis capabilities, applicable for localized engineering scoped activities */

                attribute redefines weight = 1.0 / size(requirement_5_3_subrequirements);
            }

            requirement <'R5.3.2'> hw_IT_approved defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall be implemented and maintained with approved IT hardware and software */

                attribute redefines weight = 1.0 / size(requirement_5_3_subrequirements);
            }

            requirement <'R5.3.3'> hw_computing defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall accommodate expansion of compute capabilities to support high-performance data analysis and computing power. */

                attribute redefines weight = 1.0 / size(requirement_5_3_subrequirements);
            }
        }
    }

    requirement <R6> security defined by DE_Ecosystem_req_Def =
        if DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Small or
            DETECT_Input::de_ecosystem.system_size ==
            DETECT_enumerations::Ecosystem_size_e::Medium or
            DETECT_Input::de_ecosystem.system_size == DETECT_enumerations::Ecosystem_size_e::Large
            ?    valid_requirement
            else null {
        doc Description
        /* Security */

        attribute redefines weight = 1.0 / size(requirement_list);

        requirement_6_subrequirements = ('R6.1', 'R6.2', 'R6.3');

        requirement <'R6.1'> classification defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Classification */

            attribute redefines weight = 1.0 / size(requirement_6_subrequirements);

            requirement_6_1_subrequirements = (
                'R6.1.1', 'R6.1.2', 'R6.1.3', 'R6.1.4', 'R6.1.5', 'R6.1.6', 'R6.1.7'
            );

            requirement <'R6.1.1'> class_ops_secret defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall support operations in a Secret environment */

                attribute redefines weight = 1.0 / size(requirement_6_1_subrequirements);
            }

            requirement <'R6.1.2'> class_ops_top_secret defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall support operations in a Top Secret environment */

                attribute redefines weight = 1.0 / size(requirement_6_1_subrequirements);
            }

            requirement <'R6.1.3'> class_ops_closed defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall support operations in a closed community controlled applicable environment */

                attribute redefines weight = 1.0 / size(requirement_6_1_subrequirements);
            }

            requirement <'R6.1.4'> class_promote_review defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall have the capability to promote/demote classifications with a minimum two cleared person adjudication review */

                attribute redefines weight = 1.0 / size(requirement_6_1_subrequirements);
            }

            requirement <'R6.1.5'> class_promote_auto defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide extensibility for approved automation to promote/demote classifications */

                attribute redefines weight = 1.0 / size(requirement_6_1_subrequirements);
            }

            requirement <'R6.1.6'> class_ops_unclassified defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall support operations in an Unclassified environment */

                attribute redefines weight = 1.0 / size(requirement_6_1_subrequirements);
            }

            requirement <'R6.1.7'> class_promote defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide extensibility to accommodate bi-directional classification translation (Promote/Demote) in a multiple-level-security environment */

                attribute redefines weight = 1.0 / size(requirement_6_1_subrequirements);
            }
        }

        requirement <'R6.2'> access defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Access Control */

            attribute redefines weight = 1.0 / size(requirement_6_subrequirements);

            requirement_6_2_subrequirements = (
                'R6.2.1', 'R6.2.2', 'R6.2.3', 'R6.2.4', 'R6.2.5', 'R6.2.6', 'R6.2.7', 'R6.2.8',
                'R6.2.9'
            );

            requirement <'R6.2.1'> access_data defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide authorized users access to models and associated data */

                attribute redefines weight = 1.0 / size(requirement_6_2_subrequirements);
            }

            requirement <'R6.2.2'> access_two_factor defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide a two-factor authentication sign-on to the system */

                attribute redefines weight = 1.0 / size(requirement_6_2_subrequirements);
            }

            requirement <'R6.2.3'> access_modify_data defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide authorized users privileges to modify models */

                attribute redefines weight = 1.0 / size(requirement_6_2_subrequirements);
            }

            requirement <'R6.2.4'> access_knowledge_transfer defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall  provide accessible areas designated for knowledge transfer */

                attribute redefines weight = 1.0 / size(requirement_6_2_subrequirements);
            }

            requirement <'R6.2.5'> access_NIST_access_control defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide automated capability for managing access controls consistent with NIST access control guidance */

                attribute redefines weight = 1.0 / size(requirement_6_2_subrequirements);
            }

            requirement <'R6.2.6'> access_new_users defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall accommodate new user account approvals in less than TBD (days, hrs, minutes) */

                attribute redefines weight = 1.0 / size(requirement_6_2_subrequirements);
            }

            requirement <'R6.2.7'> 'accesss_sign-on' defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide applicable CAC enabled sign-on or single sign-on capability for all authenticated users */

                attribute redefines weight = 1.0 / size(requirement_6_2_subrequirements);
            }

            requirement <'R6.2.8'> access_NIST_authentication defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall comply with all applicable NIST Access Control guidance for identification and authentication of users and all network accesses */

                attribute redefines weight = 1.0 / size(requirement_6_2_subrequirements);
            }

            requirement <'R6.2.9'> access_authoritative_data defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide access for stakeholders and end users to all authoritative data and information at the appropriate classification level of the end user */

                attribute redefines weight = 1.0 / size(requirement_6_2_subrequirements);
            }
        }

        requirement <'R6.3'> cybersecurity defined by DE_Ecosystem_req_Def =
            if DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Small or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Medium or
                DETECT_Input::de_ecosystem.system_size ==
                DETECT_enumerations::Ecosystem_size_e::Large
                ?    valid_requirement
                else null {
            doc Description
            /* Cybersecurity */

            attribute redefines weight = 1.0 / size(requirement_6_subrequirements);

            requirement_6_3_subrequirements = ('R6.3.1', 'R6.3.2', 'R6.3.3', 'R6.3.4');

            requirement <'R6.3.1'> cyber_ATO defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem design shall support and enable authority to operate (ATO) requests within (TBD) working days */

                attribute redefines weight = 1.0 / size(requirement_6_3_subrequirements);
            }

            requirement <'R6.3.2'> cyber_IA defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall  comply with applicable Information Assurance (IA) Risk Management Framework (RMF) requirements */

                attribute redefines weight = 1.0 / size(requirement_6_3_subrequirements);
            }

            requirement <'R6.3.3'> cyber_ATO_auto defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem design shall provide extensibility to automate authority to operate (ATO) requests */

                attribute redefines weight = 1.0 / size(requirement_6_3_subrequirements);
            }

            requirement <'R6.3.4'> cyber_CUI defined by DE_Ecosystem_req_Def =
                if DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Small or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Medium or
                    DETECT_Input::de_ecosystem.system_size ==
                    DETECT_enumerations::Ecosystem_size_e::Large
                    ?    valid_requirement
                    else null {
                doc Description
                /* The DE ecosystem shall provide for Controlled Unclassified Information (CUI) and proprietary information segregation */

                attribute redefines weight = 1.0 / size(requirement_6_3_subrequirements);
            }
        }
    }
}
