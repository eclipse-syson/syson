package DETECT_Use_Cases {
    doc Acknowledgement
    /* This file contains content from DETECT model file for SysML v1.6 by Daniel Hettema, OUSD (R&E).
     * Original content is dedicated to the Public Domain.
     * Modifications Copyright (c) 2025 Sensmetry.
     */

    doc Description
    /* This view provides the use cases that this model is planning to address. It also provides
     * active links to help the user navigate to the corresponding user instructions for each use
     * case.
     *
     * DETECT is currently addressing the 'Determine DE Ecosystem Sizing' Use Case.
     */

    private import DETECT_Input;
    private import DETECT_Sizing;

    use case <UC1> 'Determine DE ecosystem Sizing' defined by DETECT_Sizing::DETECT_use_case_Def {
        subject redefines DETECT_Input::de_ecosystem; // right-click on "de_ecosystem", select "Go to Definition" to navigate to model data entry inputs
        actor engineer defined by DETECT_Sizing::DETECT_user;
        actor manager defined by DETECT_Sizing::DETECT_user;

        objective {
            doc
            /* Determine DE ecosystem Sizing and export weighted DE Ecosystem Tool Evaluation
             * Criteria and Requirements.
             */
        }
        doc
        /* The output requirements and criteria can be tailored if necessary to make the language
         * more clear to the organization.
         */

        attribute redefines uc_status = DETECT_Sizing::status_e::Green; // implemented
    }

    use case <UC2> 'Identify DE ecosystem Tool Types'
        defined by DETECT_Sizing::DETECT_use_case_Def {
        subject redefines DETECT_Input::de_ecosystem;
        actor engineer defined by DETECT_Sizing::DETECT_user;
        actor manager defined by DETECT_Sizing::DETECT_user;

        attribute redefines uc_status = DETECT_Sizing::status_e::Red; // not implemented
    }

    use case <UC3> 'Determine DE ecosystem Tools Based on Job Series'
        defined by DETECT_Sizing::DETECT_use_case_Def {
        subject redefines DETECT_Input::de_ecosystem;
        actor manager defined by DETECT_Sizing::DETECT_user;
        actor senior_leader defined by DETECT_Sizing::DETECT_user;

        attribute redefines uc_status = DETECT_Sizing::status_e::Red; // not implemented
    }
}
