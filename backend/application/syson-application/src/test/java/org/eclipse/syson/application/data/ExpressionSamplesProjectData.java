/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.application.data;

import java.util.List;

/**
 * Ids for project "Expression-Samples".
 *
 * @author pcdavid
 */
public class ExpressionSamplesProjectData {
    public static final String EDITING_CONTEXT_ID = "aac82b89-8d53-4b16-91b4-deccc180ba62";

    public static final String SCRIPT_PATH = "/scripts/database-content/ExpressionSamples.sql";

    /**
     * Ids of the semantic elements.
     */
    public static final class SemanticIds {
        public static final String EXPRESSIONS_DOCUMENT_ID = "ef3e6929-0415-4295-b42f-64bed8bd4f55";

        public static final String EXPRESSIONS_PACKAGE_ID = "baf5ea6a-7861-4b92-8be2-3fe7a2ebc415";

        public static final String TANK_ID = "2bde22f7-a834-4afa-84bf-1bae1bb434d8";

        public static final String TANK_PRESSURE_ATTRIBUTE_ID = "7479570e-234f-4603-a764-d45ba1228aad";

        public static final String TANK_MAX_VOLUME_ATTRIBUTE_ID = "d9926727-7378-4177-a940-2fb6c1c89dce";

        public static final String TANK_MAX_VOLUME_ATTRIBUTE_VALUE_ID = "a9216e54-f44c-4b1f-b262-13650324d325";

        public static final String TANK_PRESSURE_LIMIT_CONSTRAINT_ID = "7d42ee06-3c27-4eaa-9438-344fc789906a";

        public static final String TANK_PRESSURE_LIMIT_CONSTRAINT_VALUE_ID = "faa3b115-5b07-4ecf-9147-0d58ceffaf9c";

        public static final String PERFORMANCE_CONCERN_ID = "da2296a4-25ed-4f04-abc8-d47f93cd223c";

        public static final String PERFORMANCE_CONCERN_ASSUME_ID = "4fd2c402-15b7-4546-ad9c-7aa9419a1528";

        public static final String PERFORMANCE_CONCERN_REQUIRE_ID = "6891b4f5-6f02-4c73-ae23-db88ad10b253";

        public static final String PERFORMANCE_CONCERN_REQUIRE_EXPRESSION_ID = "6f2e1644-b09d-46a2-b155-fc123e0f2953";

        public static final String THERMAL_CONTROL_STATE_DEFINITION_ID = "2efcf5d4-4948-4b7d-b779-f3f694eb8165";

        public static final String THERMAL_CONTROL_TO_HEATING_TRANSITION_ID = "0b517687-ef3f-4057-9ce5-27e27f627a22";

        public static final String THERMAL_CONTROL_TO_COOLING_TRANSITION_ID = "e1c27d0b-476e-42d4-9b81-b5f064d9cbbe";

        public static final String THERMAL_CONTROL_TO_COOLING_TRANSITION_GUARD_EXPRESSION_ID = "ec67ead5-7739-4dd3-a82e-f1bb5ef34268";

        public static final String SENSOR_OPERABILITY_REQUIREMENT_ID = "96735823-f93e-4edd-948e-2db5df124d11";

        public static final String SENSOR_OPERABILITY_ENVIRONMENTAL_PRECONDITION_ASSUMED_CONSTRAINT_ID = "baa1ff9e-5735-4393-ba94-324731366733";

        public static final String SENSOR_OPERABILITY_ENVIRONMENTAL_PRECONDITION_ASSUMED_CONSTRAINT_EXPRESSION_ID = "97f03244-a4a5-47cd-988d-15119a3b2210";

        public static final List<String> ALL_IDS = List.of(
                EXPRESSIONS_DOCUMENT_ID,
                EXPRESSIONS_PACKAGE_ID,
                TANK_ID,
                TANK_PRESSURE_ATTRIBUTE_ID,
                TANK_MAX_VOLUME_ATTRIBUTE_ID,
                TANK_MAX_VOLUME_ATTRIBUTE_VALUE_ID,
                TANK_PRESSURE_LIMIT_CONSTRAINT_ID,
                TANK_PRESSURE_LIMIT_CONSTRAINT_VALUE_ID,
                PERFORMANCE_CONCERN_ID,
                PERFORMANCE_CONCERN_ASSUME_ID,
                PERFORMANCE_CONCERN_REQUIRE_ID,
                PERFORMANCE_CONCERN_REQUIRE_EXPRESSION_ID,
                THERMAL_CONTROL_STATE_DEFINITION_ID,
                THERMAL_CONTROL_TO_HEATING_TRANSITION_ID,
                THERMAL_CONTROL_TO_COOLING_TRANSITION_ID,
                THERMAL_CONTROL_TO_COOLING_TRANSITION_GUARD_EXPRESSION_ID);
    }
}
