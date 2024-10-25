/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
     * Test import/export on test file UseCaseTest.sysml.
     *
     * @see https://github.com/Systems-Modeling/SysML-v2-Release/blob/master/sysml/src/examples/Simple%20Tests/UseCaseTest.sysml
     */
    @Test
    public void checkUseCaseTest() throws IOException {
        /*
         * The file has been modified because a problem has been detected during the export phase see
         * https://github.com/eclipse-syson/syson/issues/795. Once this problem is fixed, the used of qualified names should
         * not be necessary anymore.
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
                        actor :>> UseCaseTest::user;
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

}
