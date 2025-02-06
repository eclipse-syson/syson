/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.application.export;

import java.io.IOException;

import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingDomainFactory;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.export.checker.SysmlImportExportChecker;
import org.eclipse.syson.sysml.export.SysMLv2DocumentExporter;
import org.eclipse.syson.sysml.upload.SysMLExternalResourceLoaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests that importing a SysML textual file and then exporting it gives coherent result.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImportExportTests extends AbstractIntegrationTests {

    @Autowired
    private SysMLExternalResourceLoaderService sysmlLoader;

    @Autowired
    private IEditingDomainFactory editingDomainFactory;

    @Autowired
    private SysMLv2DocumentExporter exporter;

    private SysmlImportExportChecker checker;

    @BeforeEach
    public void setUp() {
        this.checker = new SysmlImportExportChecker(this.sysmlLoader, this.editingDomainFactory, this.exporter);
    }

    /**
     * Test import/export on test file UseCaseTest.sysml. The content of UseCaseTest.sysml that have been copied below
     * is under LGPL-3.0-only license. The LGPL-3.0-only license is accessible at the root of this repository, in the
     * LICENSE-LGPL file.
     *
     * @see <a href=
     *      "https://github.com/Systems-Modeling/SysML-v2-Release/blob/master/sysml/src/examples/Simple%20Tests/UseCaseTest.sysml">UseCaseTest</a>
     */
    @Test
    public void checkUseCaseTest() throws IOException {
        /*
         * The file has been modified because a problem has been detected during the export phase.
         * Those problem force us to use some full-length qualified name. This should be investigated.
         *
         * for example:
                include UseCaseTest::uc2;
         * instead of
                include uc2;
         */
        var input = """
                package UseCaseTest {

                    part def System;
                    part def User;

                    use case def UseSystem {
                        subject system : System;
                        actor user : User;

                        objective  {
                            /* Goal */
                        }

                        include use case uc1 : UC1;
                        include use case uc2 {
                            subject = system;
                            actor user = user;
                        }
                    }

                    use case def UC1;

                    part user : User;

                    use case uc2 {
                        actor :>> user;
                    }

                    use case u : UseSystem;

                    part system : System {
                        include UseCaseTest::uc2;
                        perform UseCaseTest::u;
                    }

                }
                """;

        var expected = """
                package UseCaseTest {
                    part def System;
                    part def User;
                    use case def UseSystem {
                        subject system : System;
                        actor user : User;
                        objective {
                            /* Goal */
                        }
                        include use case uc1 : UC1;
                        include use case uc2 {
                            subject = system;
                            actor user = user;
                        }
                    }
                    use case def UC1;
                    part user : User;
                    use case uc2 {
                        actor :>> user;
                    }
                    use case u : UseSystem;
                    part system : System {
                        include uc2;
                        perform u;
                    }
                }""";

        this.checker.check(input, expected);

    }

    /**
     * Test import/export simple PortDef and PortUsage.
     *
     * @throws IOException
     */
    @DisplayName("Given a model with PortDefinition and PortUsage, when importing and exporting the model, then the exported text file should be the same as the imported one.")
    @Test
    public void checkImportPort() throws IOException {
        var input = """
                port def Port1;
                part part1 {
                    port port1 : Port1;
                }""";
        this.checker.check(input, input);
    }

    /**
     * Test import/export on test file ImportTest.sysml.
     *
     * @see <a href="https://github.com/Systems-Modeling/SysML-v2-Release/blob/master/sysml/src/examples/Simple%20Tests/ImportTest.sysml">ImportTest</a>
     */
    @Test
    public void checkImportTest() throws IOException {
        /*
         * The file has been modified because a problem has been detected during the export phase.
         * Those problem force us to use some full-length qualified name. This should be investigated.
         *
         * for example:
                private import Pkg2::Pkg21::Pkg211::*::**;
         * instead of
                private import Pkg211::*::**;
         */
        var input = """
                package ImportTest {
                    package Pkg1 {
                        private import Pkg2::Pkg21::Pkg211::P211;
                        private import Pkg2::Pkg21::*;
                        private import Pkg211::*::**;
                        part p11 : P211;
                        part def P12;
                    }
                    package Pkg2 {
                        private import Pkg1::*;
                        package Pkg21 {
                            package Pkg211 {
                                part def P211 :> P12;
                            }
                        }
                    }
                }
                """;

        var expected = """
                package ImportTest {
                    package Pkg1 {
                        private import Pkg2::Pkg21::Pkg211::P211;
                        private import Pkg2::Pkg21::*;
                        private import Pkg2::Pkg21::Pkg211::*::**;
                        part p11 : P211;
                        part def P12;
                    }
                    package Pkg2 {
                        private import Pkg1::*;
                        package Pkg21 {
                            package Pkg211 {
                                part def P211 :> P12;
                            }
                        }
                    }
                }""";

        this.checker.check(input, expected);

    }

    /**
     * Checks the import/export of model containing.
     *
     * <ul>
     * <li>ConcernDefintion</li>
     * <li>ConcerUsage</li>
     * <li>StakeholderMembership</li>
     * <li>RequirementUsage</li>
     * <li>RequirementDefinition</li>
     * <ul>
     */
    @Test
    @DisplayName("Given a model with ConcernDefinition, ConcernUsage, StakeholderMembership, RequirementUsage, and RequirementDefinition, when importing and exporting the model, then the exported text file should be the same as the imported one.")
    public void checkConcernTest() throws IOException {

        var input = """
                package root {
                    part def PartDef1;
                    concern def ConcernDef1 {
                        stakeholder s : PartDef1;
                    }
                    concern concernUsage1 : ConcernDef1 {
                        stakeholder s1;
                    }
                    requirement def R1Def {
                        stakeholder s : PartDef1;
                    }
                    requirement r1 : R1Def {
                        stakeholder s1;
                    }
                }""";

        this.checker.check(input, input);
    }

    /**
     * Test import/export on test file OccurrenceTest.sysml.
     *
     * @see <a href="https://github.com/Systems-Modeling/SysML-v2-Release/blob/master/sysml/src/examples/Simple%20Tests/OccurrenceTest.sysml">OccurrenceTest</a>
     */
    @Test
    public void checkOccurrenceTest() throws IOException {
        var input = """
                package OccurrenceTest {
                    occurrence def Occ {
                        attribute a;
                        ref occurrence occ1 : Occ;
                        occurrence occ2 : Occ;
                        item x;
                        part y;
                        individual snapshot s : Ind;
                        timeslice t;
                    }
                    occurrence occ : Occ {
                        occurrence o1 : Occ;
                        ref occurrence o2 : Occ;
                        item z;
                    }
                    individual occurrence def Ind {
                        snapshot s2;
                        timeslice t2;
                    }
                    individual occurrence ind : Ind, Occ {
                        snapshot s3;
                        individual timeslice t3;
                    }
                    individual snapshot s4 : Ind;
                    occurrence o1 {
                        occurrence o2;
                    }
                }""";

        var expected = """
                package OccurrenceTest {
                    occurrence def Occ {
                        attribute a;
                        ref occurrence occ1 : Occ;
                        occurrence occ2 : Occ;
                        item x;
                        part y;
                        individual snapshot s : Ind;
                        timeslice t;
                    }
                    occurrence occ : Occ {
                        occurrence o1 : Occ;
                        ref occurrence o2 : Occ;
                        item z;
                    }
                    individual occurrence def Ind {
                        snapshot s2;
                        timeslice t2;
                    }
                    individual occurrence ind : Ind, Occ {
                        snapshot s3;
                        individual timeslice t3;
                    }
                    individual snapshot s4 : Ind;
                    occurrence o1 {
                        occurrence o2;
                    }
                }""";

        this.checker.check(input, expected);

    }

}
