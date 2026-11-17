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
package org.eclipse.syson.sysml.linker.tests.adapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.omg.sysml.logic.implicit.specialization.ImplicitSpecializationCacheUtil;
import org.omg.sysml.logic.implicit.specialization.ImplicitSpecializationService;
import org.omg.sysml.logic.implicit.specialization.ImplicitSpecializationServices;

/**
 * Standard library loader shared accross tests.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class LibraryLoader {

    /**
     * Path to the standard library folder with implied XML models in this module's test resources.
     */
    private static final String MODELS_SYSML_LIBRARY_XMI_IMPLIED = "src/test/resources/sysml.library.xmi.implied";

    /**
     * The {@link List} of {@link Resource} containing the standard library models.
     */
    private static List<Resource> stdLibResources;

    /**
     * Get the {@link List} of {@link Resource} containing the standard library models.
     *
     * @return the {@link List} of {@link Resource} containing the standard library models
     * @throws IOException
     *         if the models can't be loaded
     */
    public List<Resource> getStdLibResources() throws IOException {
        if (stdLibResources == null) {
            stdLibResources = new ArrayList<>();
            ResourceSetImpl stdLibRessourceSet = new ResourceSetImpl();

            ImplicitSpecializationService service = new ImplicitSpecializationService();
            ImplicitSpecializationServices.install(stdLibRessourceSet, service);

            stdLibRessourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
                    "*", new XMIResourceFactoryImpl());
            System.out.println("Loading standard lib : start");
            for (Path xmiLibFile : this.getAllStandardLibFile()) {
                URI xmiURI = URI.createFileURI(xmiLibFile.toFile().getAbsolutePath());
                final Resource resource = stdLibRessourceSet.getResource(xmiURI, true);
                stdLibResources.add(resource);
                ImplicitSpecializationCacheUtil.installCaches(resource);
            }
            System.out.println("Loading standard lib : end");
        }

        return stdLibResources;
    }

    /**
     * Gets the {@link List} of standard library {@link Path}.
     *
     * @return the {@link List} of standard library {@link Path}
     * @throws IOException
     *             if the library folder can't be read
     */
    private List<Path> getAllStandardLibFile() throws IOException {
        return Files.walk(this.getStandardLibraryPath())
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".sysmlx") || p.toString().endsWith(".kermlx"))
                .toList();
    }

    /**
     * Gets the standard library {@link Path} from this module's test resources.
     *
     * @return the standard library {@link Path}
     */
    public Path getStandardLibraryPath() {
        return Path.of(MODELS_SYSML_LIBRARY_XMI_IMPLIED).toAbsolutePath().normalize();
    }

}
