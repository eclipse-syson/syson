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
package org.eclipse.syson.sysml.linker.tests.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.syson.sysml.linker.AdditionalMemberCompletionService;
import org.eclipse.syson.sysml.linker.MapBasedReferenceSetChecker;
import org.eclipse.syson.sysml.linker.QualifiedName;
import org.eclipse.syson.sysml.linker.SysMLLinker;
import org.eclipse.syson.sysml.linker.tests.adapter.LibraryLoader;
import org.eclipse.syson.sysml.linker.tests.util.TestUtils;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.EMFFragmentHelper;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.parser.internal.StructuralModelCompletionService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;
import org.omg.sysml.util.SysMLLibraryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolution test suite using SysML textual files as parser input.
 *
 * @author Arthur Daussy
 */
@RunWith(Parameterized.class)
public abstract class AbstractParsedResolveTestSuite {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractParsedResolveTestSuite.class);

    private static final String MODEL_FOLDER = "../model/";

    private static final String PARSED_MODEL_ACTUAL_SUFFIX = "-parsed-model-actual.txt";

    private static final String PARSED_MODEL_EXPECTED_SUFFIX = "-parsed-model-expected.txt";

    private static final String LINKED_MODEL_EXPECTED_SUFFIX = "-model-expected.txt";

    private static final String LINKED_MODEL_ACTUAL_SUFFIX = "-model-actual.txt";

    private static final String TEST_FOLDER_PROPERTY = "parsedResolveTestFolder";

    private static final String LINKER_RESOURCE_MARKER = "org/eclipse/syson/sysml/linker/";

    private static final UUID STABLE_ID_NAMESPACE = UUID.fromString("2dd6b626-0c53-4d81-a6dd-9c5b5f7a3f4d");

    private static final String STABLE_ID_SEPARATOR = "|";

    /**
     * The {@link LibraryLoader}.
     */
    private static final LibraryLoader LIBRARY_LOADER = new LibraryLoader();

    /**
     * The test folder.
     */
    private final File testFolder;

    /**
     * Holds true if the standard libs are required.
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
    public AbstractParsedResolveTestSuite(String testFolder) throws IOException {
        this(testFolder, false);
    }

    /**
     * Constructor.
     *
     * @param testFolder
     *            the test folder
     * @param loadStandardLib
     *            holds true to load the standard libraries
     * @throws IOException
     *             if test files can't be read
     */
    public AbstractParsedResolveTestSuite(String testFolder, boolean loadStandardLib) throws IOException {
        this.testFolder = new File(testFolder);
        this.loadStandardLib = loadStandardLib;
    }

    @BeforeClass
    public static void beforeAll() {
        SysMLLogicStandaloneSetup.doSetup();
        SysMLPackage.eINSTANCE.getName();
        SysMLLibraryUtil.setModelLibraryDirectory(LIBRARY_LOADER.getStandardLibraryPath().toString());
    }

    @Before
    public void setUp() throws IOException {
        File sysMLFolder = this.getSysMLFolder();
        this.resourceSet = new ResourceSetImpl();
        this.resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

        if (this.loadStandardLib) {
            for (Resource stdResource : LIBRARY_LOADER.getStdLibResources()) {
                this.resourceSet.getResources().add(stdResource);
            }
        }

        Map<EObject, Map<EReference, Set<QualifiedName>>> toResolve = new LinkedHashMap<>();
        SysMLModelParser parser = new SysMLModelParser();
        for (File sysMLFile : this.getSysMLFiles(sysMLFolder)) {
            ISysMLParseResult<Namespace> result;
            try (InputStream inputStream = Files.newInputStream(sysMLFile.toPath())) {
                result = parser.parseRootNamespace(inputStream, StandardCharsets.UTF_8);
            }
            this.assertNoParseError(sysMLFile, result);

            URI xmiURI = URI.createFileURI(sysMLFile.getAbsolutePath()).trimFileExtension().appendFileExtension("xmi");
            Resource resource = this.resourceSet.createResource(xmiURI);
            resource.getContents().add(result.model());
            if (resource instanceof XMIResource xmiResource) {
                this.assignStableIDs(xmiResource, this.getStableSysMLPath(resource.getURI()));
            }
            this.addLinksToResolve(toResolve, result.danglingLinks());
            this.completeStructuralModel(result.model(), toResolve);
        }

        SysMLLinker linker = new SysMLLinker(this.resourceSet, toResolve);
        linker.resolveAll();
        for (Resource resource : this.resourceSet.getResources()) {
            if (!this.isStandardLib(resource)) {
                this.completeAdditionalMembers(resource);
            }
        }
        this.notResolved = linker.getNotResolved();
        this.diagnostic = linker.getDiagnostic();
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
            String requestedTestFolder = System.getProperty(TEST_FOLDER_PROPERTY);
            if (requestedTestFolder != null && !requestedTestFolder.isBlank()) {
                File[] availableTestFolders = children;
                children = Arrays.stream(children)
                        .filter(child -> requestedTestFolder.equals(child.getName()))
                        .toArray(File[]::new);
                if (children.length == 0) {
                    String availableFolderNames = Arrays.stream(availableTestFolders)
                            .map(File::getName)
                            .sorted()
                            .toList()
                            .toString();
                    LOGGER.error("No parsed resolve test folder named '{}' under {}. Available folders: {}", requestedTestFolder, folderPath, availableFolderNames);
                }
            }
            for (File child : children) {
                parameters.add(new Object[] { child.getPath() });
            }
        } else {
            LOGGER.error("The test folder {} doesn't exist.", folderPath);
        }

        return parameters;
    }

    private void assertNoParseError(File sysMLFile, ISysMLParseResult<?> result) {
        if (result.diagnostic().getSeverity() == Diagnostic.ERROR || result.model() == null) {
            fail("Parsing error on " + sysMLFile + "\n" + this.getDiagnosticLinksString(result.diagnostic(), ""));
        }
    }

    private void addLinksToResolve(Map<EObject, Map<EReference, Set<QualifiedName>>> toResolve, Set<DanglingLink> danglingLinks) {
        for (DanglingLink link : danglingLinks) {
            for (String target : link.targets()) {
                toResolve.computeIfAbsent(link.context(), context -> new LinkedHashMap<>())
                        .computeIfAbsent(link.reference(), reference -> new LinkedHashSet<>())
                        .add(new QualifiedName(target));
            }
        }
    }

    private void completeStructuralModel(Element root, Map<EObject, Map<EReference, Set<QualifiedName>>> toResolve) {
        StructuralModelCompletionService completionService = new StructuralModelCompletionService(
                new MapBasedReferenceSetChecker(toResolve));
        for (Element element : this.collectElements(root)) {
            completionService.complete(element);
        }
    }

    private void completeAdditionalMembers(Resource resource) {
        AdditionalMemberCompletionService completionService = new AdditionalMemberCompletionService();
        for (EObject root : resource.getContents()) {
            if (root instanceof Element element) {
                for (Element content : this.collectElements(element)) {
                    completionService.complete(content);
                }
            }
        }
    }

    private List<Element> collectElements(Element root) {
        List<Element> elements = new ArrayList<>();
        elements.add(root);
        var iterator = root.eAllContents();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof Element element) {
                elements.add(element);
            }
        }
        return elements;
    }

    /**
     * Gets the <code>sysml</code> folder.
     *
     * @return the <code>sysml</code> folder
     */
    private File getSysMLFolder() {
        return new File(this.testFolder, "sysml");
    }

    private File[] getSysMLFiles(File sysMLFolder) {
        File[] res = sysMLFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.canRead() && pathname.getName().endsWith(".sysml");
            }
        });
        Arrays.sort(res, Comparator.comparing(File::getName));
        return res;
    }

    @Test
    public void checkResolvedModel() throws UnsupportedEncodingException, IOException {
        for (Resource resource : this.resourceSet.getResources()) {
            if (!this.isStandardLib(resource)) {
                String actualModelString = this.getModelString(resource);
                URI expectedModelURI = this.getExpectedModelURI(resource.getURI());
                long startTime = System.currentTimeMillis();
                LOGGER.info("START - checkResolvedModel on {}", expectedModelURI);
                if (URIConverter.INSTANCE.exists(expectedModelURI, null)) {
                    if (Boolean.getBoolean("updateExpectedModels")) {
                        TestUtils.setContent(URIConverter.INSTANCE, expectedModelURI, StandardCharsets.UTF_8.name(), actualModelString);
                    }
                    String expectedModelString = TestUtils.getContent(URIConverter.INSTANCE, expectedModelURI, StandardCharsets.UTF_8.name());
                    this.assertModelEquals(resource.getURI(), expectedModelURI, expectedModelString, actualModelString);
                } else if (Boolean.getBoolean("updateExpectedModels")) {
                    TestUtils.setContent(URIConverter.INSTANCE, expectedModelURI, StandardCharsets.UTF_8.name(), actualModelString);
                } else {
                    URI actualModelURI = this.getActualModelURI(resource.getURI());
                    TestUtils.setContent(URIConverter.INSTANCE, actualModelURI, StandardCharsets.UTF_8.name(), actualModelString);
                    fail(expectedModelURI + " doesn't exist.");
                }
                LOGGER.info("END - checkResolvedModel on {} in {} ms", expectedModelURI, System.currentTimeMillis() - startTime);
            }
        }
    }

    void assertModelEquals(URI modelURI, URI expectedModelURI, String expectedModelString, String actualModelString)
            throws IOException {
        if (!expectedModelString.equals(actualModelString)
                && expectedModelURI.equals(this.getExpectedLinkedModelURI(modelURI))) {
            URI actualLinkedModelURI = this.getActualLinkedModelURI(modelURI);
            TestUtils.setContent(URIConverter.INSTANCE, actualLinkedModelURI, StandardCharsets.UTF_8.name(), actualModelString);
        }
        assertEquals(expectedModelString, actualModelString);
    }

    private boolean isStandardLib(Resource resource) {
        return resource.getURI().toString().contains(SysMLLibraryUtil.getModelLibraryPath());
    }

    @Test
    public void checkDanglindLinks() throws UnsupportedEncodingException, IOException {
        URI testURI = URI.createFileURI(this.testFolder.getAbsolutePath() + "/model/model");
        URI expectedDanglingLinksURI = this.getExpectedDanglingLinksURI(testURI);
        long startTime = System.currentTimeMillis();
        LOGGER.info("START - checkDanglindLinks on {}", expectedDanglingLinksURI);

        String actualDanglingLinksString = this.getDanglingLinksString(this.notResolved);
        if (URIConverter.INSTANCE.exists(expectedDanglingLinksURI, null)) {
            String expectedDanglingLinksString = TestUtils.getContent(URIConverter.INSTANCE, expectedDanglingLinksURI, StandardCharsets.UTF_8.name());
            assertEquals(expectedDanglingLinksString, actualDanglingLinksString);
        } else {
            URI actualModelURI = this.getActualDanglingLinksURI(testURI);
            TestUtils.setContent(URIConverter.INSTANCE, actualModelURI, StandardCharsets.UTF_8.name(), actualDanglingLinksString);
            fail(expectedDanglingLinksURI + " doesn't exist.");
        }
        LOGGER.info("END - checkDanglindLinks on {} in {} ms", expectedDanglingLinksURI, System.currentTimeMillis() - startTime);

    }

    private String getDanglingLinksString(Map<EObject, Map<EReference, Set<QualifiedName>>> links) {
        StringBuilder res = new StringBuilder();

        for (Entry<EObject, Map<EReference, Set<QualifiedName>>> contextEntry : links.entrySet()) {
            String uriFragment = EMFFragmentHelper.getURIFragment(contextEntry.getKey());
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
            String expectedDiagnosticString = TestUtils.getContent(URIConverter.INSTANCE, expectedDiagnosticURI, StandardCharsets.UTF_8.name());
            assertEquals(expectedDiagnosticString, actualDiagnosticString);
        } else {
            URI actualDiagnosticlURI = this.getActualDiagnosticLinksURI(testURI);
            TestUtils.setContent(URIConverter.INSTANCE, actualDiagnosticlURI, StandardCharsets.UTF_8.name(), actualDiagnosticString);
            fail(expectedDiagnosticURI + " doesn't exist.");
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

    URI getExpectedModelURI(URI uri) {
        URI parsedModelURI = this.getExpectedParsedModelURI(uri);
        if (URIConverter.INSTANCE.exists(parsedModelURI, null)) {
            return parsedModelURI;
        }
        return this.getExpectedLinkedModelURI(uri);
    }

    private URI getExpectedParsedModelURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + PARSED_MODEL_EXPECTED_SUFFIX);
    }

    private URI getActualModelURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + PARSED_MODEL_ACTUAL_SUFFIX);
    }

    private URI getExpectedLinkedModelURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + LINKED_MODEL_EXPECTED_SUFFIX);
    }

    private URI getActualLinkedModelURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + LINKED_MODEL_ACTUAL_SUFFIX);
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

        if (resource instanceof XMIResource xmiResource) {
            this.assignStableIDs(xmiResource, this.getStableSysMLPath(resource.getURI()));
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

    private void assignStableIDs(XMIResource resource, String stableSysMLPath) {
        for (EObject root : resource.getContents()) {
            this.assignStableIDs(resource, root, stableSysMLPath);
        }
    }

    private void assignStableIDs(XMIResource resource, EObject eObject, String stableSysMLPath) {
        if (eObject instanceof Element element) {
            String elementId = this.getStableID(stableSysMLPath, eObject);
            element.setElementId(elementId);
            resource.setID(eObject, elementId);
        }
        for (EObject child : eObject.eContents()) {
            this.assignStableIDs(resource, child, stableSysMLPath);
        }
    }

    private String getStableID(String stableSysMLPath, EObject eObject) {
        String key = STABLE_ID_NAMESPACE + STABLE_ID_SEPARATOR + stableSysMLPath + STABLE_ID_SEPARATOR + EMFFragmentHelper.getURIFragment(eObject) + STABLE_ID_SEPARATOR + eObject.eClass().getName();
        return UUID.nameUUIDFromBytes(key.getBytes(StandardCharsets.UTF_8)).toString();
    }

    private String getStableSysMLPath(URI uri) {
        String path = uri.trimFileExtension().appendFileExtension("sysml").toFileString();
        if (path == null) {
            path = uri.trimFileExtension().appendFileExtension("sysml").toString();
        }
        path = path.replace(File.separatorChar, '/');
        int markerIndex = path.indexOf(LINKER_RESOURCE_MARKER);
        if (markerIndex >= 0) {
            return path.substring(markerIndex + LINKER_RESOURCE_MARKER.length());
        }
        return Path.of(path).toAbsolutePath().normalize().toString().replace(File.separatorChar, '/');
    }

}
