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

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.syson.sysml.linker.AdditionalMemberCompletionService;
import org.eclipse.syson.sysml.linker.QualifiedName;
import org.eclipse.syson.sysml.linker.SysMLLinker;
import org.eclipse.syson.sysml.linker.tests.util.IdStabilizer;
import org.eclipse.syson.sysml.linker.tests.util.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;
import org.omg.sysml.util.SysMLLibraryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolution test suite.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@RunWith(Parameterized.class)
public abstract class AbstractResolveTestSuite {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResolveTestSuite.class);

    private static final String MODEL_FOLDER = "../model/";

    private static final IdStabilizer ID_STABILIZER = new IdStabilizer();

    /**
     * The {@link LibraryLoader}.
     */
    private static final LibraryLoader LIBRARY_LOADER = new LibraryLoader();

    /**
     * The test folder.
     */
    private final File testFolder;

    /**
     * Holds ture if the standard libs are required.
     */
    private final boolean loadStandardLib;

    /**
     * The {@link ResourceSet} used to load test models.
     */
    private ResourceSet resourceSet;

    /**
     * The unresolved link {@link Map}.
     */
    private Map<EObject, Map<EReference, Set<QualifiedName>>> notResolved;

    /**
     * The linking {@link Diagnostic}.
     */
    private Diagnostic diagnostic;

    /**
     * Constructor.
     *
     * @param testFolder
     *            the test folder
     * @throws IOException
     *             if test files can't be read
     */
    public AbstractResolveTestSuite(String testFolder) throws IOException {
        this(testFolder, false);
    }

    /**
     * Constructor.
     *
     * @param testFolder
     *            the test folder
     * @param loadStandardLib
     *            holds true to load the standard librairies
     * @throws IOException
     *             if test files can't be read
     */
    public AbstractResolveTestSuite(String testFolder, boolean loadStandardLib) throws IOException {
        this.testFolder = new File(testFolder);

        this.loadStandardLib = loadStandardLib;
    }

    @BeforeClass
    public static void beforeAll() {
        SysMLLogicStandaloneSetup.doSetup();
        SysMLPackage.eINSTANCE.getName(); // make sure SysmlPackage is initialized
        SysMLLibraryUtil.setModelLibraryDirectory(LIBRARY_LOADER.getStandardLibraryPath().toString());
    }

    @Before
    public void setUp() throws IOException {
        File sysMLFolder = this.getSysMLFolder();
        this.resourceSet = new ResourceSetImpl();
        this.resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
                "*", new XMIResourceFactoryImpl());

        if (this.loadStandardLib) {
            for (Resource stdResource : LIBRARY_LOADER.getStdLibResources()) {
                this.resourceSet.getResources().add(stdResource);
            }
        }

        // Needs to install the SysMLIndexAdapter after the loading a standard lib because it causes the addition to
        // implicit super type. The current implementation relies on a qualified name
        // resolution that fails if not all standard lib resources are loaded
        Map<EObject, Map<EReference, Set<QualifiedName>>> toResolve = new LinkedHashMap<>();

        for (File xmiFile : this.getXMIFiles(sysMLFolder)) {
            URI xmiURI = URI.createFileURI(xmiFile.getAbsolutePath());
            Resource resource = this.resourceSet.getResource(xmiURI, true);

            URI linkURI = this.getLinksURI(xmiURI);
            toResolve.putAll(this.getLinkToResolve(resource.getContents().get(0), linkURI));
        }
        SysMLLinker getter = new SysMLLinker(this.resourceSet, toResolve);
        getter.resolveAll();
        AdditionalMemberCompletionService postLinkCompletion = new AdditionalMemberCompletionService();
        for (Resource resource : this.resourceSet.getResources()) {
            if (!this.isStandardLib(resource)) {
                this.completeAdditionalMembers(resource, postLinkCompletion);
            }
        }
        this.notResolved = getter.getNotResolved();
        this.diagnostic = getter.getDiagnostic();
    }

    private void completeAdditionalMembers(Resource resource, AdditionalMemberCompletionService completionService) {
        for (Element element : this.collectElements(resource)) {
            completionService.complete(element);
        }
    }

    private List<Element> collectElements(Resource resource) {
        List<Element> elements = new ArrayList<>();
        for (EObject root : resource.getContents()) {
            if (root instanceof Element element) {
                elements.add(element);
                var iterator = element.eAllContents();
                while (iterator.hasNext()) {
                    if (iterator.next() instanceof Element content) {
                        elements.add(content);
                    }
                }
            }
        }
        return elements;
    }

    /**
     * Gets the {@link Collection} of test folders from the given folder path.
     *
     * @param folderPath
     *            the folder path
     * @return the {@link Collection} of test folders from the given folder path
     */
    public static Collection<Object[]> retrieveTestFolders(String folderPath) {
        Collection<Object[]> parameters = new ArrayList<>();

        File folder = new File(folderPath);
        if (folder.exists()) {
            File[] children = folder.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    final boolean res;

                    if (pathname.isDirectory() && pathname.canRead()) {
                        File sysmlFolder = new File(pathname, "sysml");
                        res = sysmlFolder.isDirectory() && sysmlFolder.canRead();
                    } else {
                        res = false;
                    }

                    return res;
                }

            });
            Arrays.sort(children);
            for (File child : children) {
                parameters.add(new Object[] { child.getPath() });
            }
        } else {
            LOGGER.error("The test folder {} doesn't exist.", folderPath);
        }

        return parameters;
    }

    private Map<EObject, Map<EReference, Set<QualifiedName>>> getLinkToResolve(EObject root, URI linkURI) throws IOException {
        Map<EObject, Map<EReference, Set<QualifiedName>>> res = new LinkedHashMap<>();

        try (InputStream is = URIConverter.INSTANCE.createInputStream(linkURI); BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                EObject context = this.getEObject(root, fields[0]);
                EReference reference = (EReference) context.eClass().getEStructuralFeature(fields[1]);
                // TODO use index int index = Integer.valueOf(fields[2]);
                QualifiedName qualifiedName = new QualifiedName(fields[3]);
                res.computeIfAbsent(context, c -> new LinkedHashMap<>()).computeIfAbsent(reference, r -> new LinkedHashSet<>()).add(qualifiedName);
            }
        }

        return res;
    }

    /**
     * Gets the {@link EObject} form the given root {@link EObject} and given fragment.
     *
     * @param root
     *            the root {@link EObject}
     * @param fragment
     *            the uri fragment
     * @return the {@link EObject} form the given root {@link EObject} and given fragment
     */
    private EObject getEObject(EObject root, String fragment) {

        EObject current = root;
        for (String segment : fragment.substring(1).split("/")) {
            String[] refAndIndex = segment.split("\\.");
            String refName = refAndIndex[0].substring(1);
            int index = Integer.valueOf(refAndIndex[1]);
            EReference reference = (EReference) current.eClass().getEStructuralFeature(refName);
            if (reference.isMany()) {
                current = (EObject) ((List<?>) current.eGet(reference)).get(index);
            } else {
                current = (EObject) current.eGet(reference);
            }
        }

        return current;
    }

    /**
     * Gets the dangling links {@link URI} form the sysml model {@link URI}.
     *
     * @param uri
     *            the sysml model {@link URI}
     * @return the dangling links {@link URI} form the sysml model {@link URI}
     */
    private URI getLinksURI(URI uri) {
        return URI.createURI(uri.toString() + "-danglinglinks.txt");
    }

    /**
     * Gets the <code>sysml</code> folder.
     *
     * @return the <code>sysml</code> folder
     */
    private File getSysMLFolder() {
        return new File(this.testFolder, "sysml");
    }

    private File[] getXMIFiles(File sysMLFolder) {
        File[] res = sysMLFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.canRead() && pathname.getName().endsWith(".xmi");
            }

        });
        return res;
    }

    @Test
    public void checkResolvedModel() throws UnsupportedEncodingException, IOException {
        for (Resource resource : this.resourceSet.getResources()) {
            if (!this.isStandardLib(resource)) {
                ID_STABILIZER.assignStableIDs((XMIResource) resource, ID_STABILIZER.getStableSysMLPath(resource.getURI()));
                String actualModelString = this.getModelString(resource);
                URI expectedModelURI = this.getExpectedModelURI(resource.getURI());
                long startTime = System.currentTimeMillis();
                LOGGER.info("START - checkResolvedModel on {}", expectedModelURI);

                if (URIConverter.INSTANCE.exists(expectedModelURI, null)) {
                    if (Boolean.getBoolean("updateExpectedModels")) {
                        TestUtils.setContent(URIConverter.INSTANCE, expectedModelURI, StandardCharsets.UTF_8.name(), actualModelString);
                    }
                    String expectedModelString = TestUtils.getContent(URIConverter.INSTANCE, expectedModelURI, StandardCharsets.UTF_8.name());
                    assertEquals(expectedModelString, actualModelString);
                } else {
                    URI actualModelURI = this.getActualModelURI(resource.getURI());
                    TestUtils.setContent(URIConverter.INSTANCE, actualModelURI, StandardCharsets.UTF_8.name(), actualModelString);
                    Assert.fail(expectedModelURI + " doesn't exist.");
                }
                LOGGER.info("END - checkResolvedModel on {} in {} ms", expectedModelURI, System.currentTimeMillis() - startTime);

            }
        }
    }

    private boolean isStandardLib(Resource resource) {
        return resource.getURI().toString().contains(SysMLLibraryUtil.getModelLibraryPath());
    }

    @Test
    public void checkDanglindLinks() throws UnsupportedEncodingException, IOException {
        URI testURI = URI.createFileURI(this.testFolder.getAbsolutePath() + "/model/model");
        URI expectedDanglingLinksURI = this.getExpectedDanglingLinksURI(testURI);
        String actualDanglingLinksString = this.getDanglingLinksString(this.notResolved);
        LOGGER.info("START - checkDanglindLinks on {}", expectedDanglingLinksURI);

        if (URIConverter.INSTANCE.exists(expectedDanglingLinksURI, null)) {
            String expectedDanglingLinksString = TestUtils.getContent(URIConverter.INSTANCE, expectedDanglingLinksURI, StandardCharsets.UTF_8.name());
            assertEquals(expectedDanglingLinksString, actualDanglingLinksString);
        } else {
            URI actualModelURI = this.getActualDanglingLinksURI(testURI);
            TestUtils.setContent(URIConverter.INSTANCE, actualModelURI, StandardCharsets.UTF_8.name(), actualDanglingLinksString);
            Assert.fail(expectedDanglingLinksURI + " doesn't exist.");
        }
        LOGGER.info("END - checkDanglindLinks on {}", expectedDanglingLinksURI);

    }

    private String getDanglingLinksString(Map<EObject, Map<EReference, Set<QualifiedName>>> links) {
        StringBuilder res = new StringBuilder();

        for (Entry<EObject, Map<EReference, Set<QualifiedName>>> contextEntry : links.entrySet()) {
            String uriFragment = this.getURIFragment(contextEntry.getKey());
            for (Entry<EReference, Set<QualifiedName>> referenceEntry : contextEntry.getValue().entrySet()) {
                String refName = referenceEntry.getKey().getName();
                int index = 0;
                for (QualifiedName qualifiedName : referenceEntry.getValue()) {
                    res.append(String.format("%s;%s;%s;%s\n", uriFragment, refName, index++, qualifiedName.toString()));
                }
            }
        }

        return res.toString();
    }

    @Test
    public void checkDiagnostic() throws UnsupportedEncodingException, IOException {
        URI testURI = URI.createFileURI(this.testFolder.getAbsolutePath() + "/model/model");
        URI expectedDiagnosticURI = this.getExpectedDiagnosticLinksURI(testURI);
        String actualDiagnosticString = this.getDiagnosticLinksString(this.diagnostic, "");
        LOGGER.info("START - checkDiagnostic on {}", expectedDiagnosticURI);

        if (URIConverter.INSTANCE.exists(expectedDiagnosticURI, null)) {
            if (Boolean.getBoolean("updateExpectedModels")) {
                TestUtils.setContent(URIConverter.INSTANCE, expectedDiagnosticURI, StandardCharsets.UTF_8.name(), actualDiagnosticString);
            }
            String expectedDiagnosticString = TestUtils.getContent(URIConverter.INSTANCE, expectedDiagnosticURI, StandardCharsets.UTF_8.name());
            assertEquals(expectedDiagnosticString, actualDiagnosticString);
        } else {
            URI actualDiagnosticlURI = this.getActualDiagnosticLinksURI(testURI);
            TestUtils.setContent(URIConverter.INSTANCE, actualDiagnosticlURI, StandardCharsets.UTF_8.name(), actualDiagnosticString);
            Assert.fail(expectedDiagnosticURI + " doesn't exist.");
        }
        LOGGER.info("END - checkDiagnostic on {}", expectedDiagnosticURI);

    }

    private String getDiagnosticLinksString(Diagnostic currentDiagnostic, String indentation) {
        StringBuilder res = new StringBuilder();

        res.append(indentation);

        switch (currentDiagnostic.getSeverity()) {
            case Diagnostic.OK:
                res.append("OK");
                break;
            case Diagnostic.INFO:
                res.append("INFO");
                break;
            case Diagnostic.WARNING:
                res.append("WARNING");
                break;
            case Diagnostic.ERROR:
                res.append("ERROR");
                break;
            default:
                res.append("???");
                break;
        }

        res.append(" - ");
        res.append(currentDiagnostic.getMessage());
        res.append("\n");

        for (Diagnostic child : currentDiagnostic.getChildren()) {
            res.append(this.getDiagnosticLinksString(child, indentation + "  "));
        }

        return res.toString();
    }

    /**
     * Gets the {@link URI} fragment based on containing {@link EReference} name and index.
     *
     * @param eObj
     *            the {@link EObject}
     * @return the {@link URI} fragment based on containing {@link EReference} name and index
     */
    private String getURIFragment(EObject eObj) {
        final String res;

        InternalEObject internalEObject = (InternalEObject) eObj;
        if (internalEObject.eInternalContainer() == null) {
            res = "/";
        } else {
            SegmentSequence.Builder builder = SegmentSequence.newBuilder("/");
            for (InternalEObject container = internalEObject.eInternalContainer(); container != null; container = internalEObject.eInternalContainer()) {
                builder.append(container.eURIFragmentSegment(internalEObject.eContainingFeature(), internalEObject));
                internalEObject = container;
            }

            builder.append("");
            builder.reverse();

            res = builder.toSegmentSequence().toString();
        }

        return res;
    }

    private URI getExpectedModelURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + "-model-expected.txt");
    }

    private URI getActualModelURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + "-model-actual.txt");
    }

    private URI getExpectedDanglingLinksURI(URI uri) {
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + "danglinglinks-expected.txt");
    }

    private URI getActualDanglingLinksURI(URI uri) {
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + "danglinglinks-actual.txt");
    }

    private URI getExpectedDiagnosticLinksURI(URI uri) {
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + "diagnostic-expected.txt");
    }

    private URI getActualDiagnosticLinksURI(URI uri) {
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + "diagnostic-actual.txt");
    }

    private String getModelString(Resource resource) {
        final String res;

        if (resource instanceof XMIResource) {
            ID_STABILIZER.assignIDs((XMIResource) resource);
        }
        String xmi = "";
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            Map<Object, Object> options = new HashMap<>();
            options.put(XMIResource.OPTION_ENCODING, StandardCharsets.UTF_8.name());
            resource.save(stream, options);
            xmi = stream.toString();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        res = xmi;

        return res;
    }

}
