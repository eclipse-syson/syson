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
package org.eclipse.syson.sysml.parser.tests;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.parser.adapter.ICommentBodyAdapter;
import org.eclipse.syson.sysml.parser.adapter.INumericLiteralFormatAdapter;
import org.eclipse.syson.sysml.parser.adapter.IStringLiteralFormatAdapter;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.tests.delegate.NotSetSettingDelegateFactory;
import org.eclipse.syson.sysml.parser.tests.util.TestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.omg.sysml.delegate.invocation.OperationInvocationDelegateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract test suite for SysMLv2 parser.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@RunWith(Parameterized.class)
public abstract class AbstractSysMLRuleTestSuite {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSysMLRuleTestSuite.class);

    private static final String SYSML_DELEGATE_URI = "http://www.omg.org/spec/SysML";

    /**
     * The {@link NotSetSettingDelegateFactory} used while testing the parser.
     */
    private static final NotSetSettingDelegateFactory SETTING_FACTORY = new NotSetSettingDelegateFactory();

    static {
        // Generated implementations cache operation delegates at class initialization.
        // Install them before any fixture creates a ParameterMembership; keep the
        // parser's no-inference setting delegates independent of the logic setup.
        EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE.put(SYSML_DELEGATE_URI,
                new OperationInvocationDelegateFactory());
    }

    private static Object previousSettingDelegateFactory;

    private static int settingDelegateFactoryUsers;

    /**
     * The model test folder.
     */
    private static final String MODEL_FOLDER = "../model/";

    /**
     * Stable textual descriptions used by the adapter golden files.
     *
     * <p>
     * This deliberately describes every singleton declared by {@link ITextAdapter}. Adding a new singleton without
     * adding its golden-file representation fails during test-suite initialization instead of silently excluding it
     * from the adapter assertions.
     * </p>
     */
    private static final Map<ITextAdapter, String> TEXT_ADAPTER_DESCRIPTIONS = createTextAdapterDescriptions();

    /**
     * The mapping of {@link URI} to {@link ISysMLParseResult}.
     */
    private final Map<URI, ISysMLParseResult<?>> parseResults = new HashMap<>();

    /**
     * The test folder.
     */
    private final File testFolder;

    /**
     * Constructor.
     *
     * @param testFolder
     *            the test file path
     * @throws Exception
     *             if something went wrong
     */
    public AbstractSysMLRuleTestSuite(String testFolder) throws Exception {
        this.testFolder = new File(testFolder);
        File sysMLFolder = this.getSysMLFolder();
        for (File sysMLFile : this.getSysMLFiles(sysMLFolder)) {
            URI sysMLURI = URI.createFileURI(sysMLFile.getAbsolutePath());
            try (InputStream is = URIConverter.INSTANCE.createInputStream(sysMLURI)) {
                SysMLModelParser parser = new SysMLModelParser();
                ISysMLParseResult<?> result = this.parse(parser, is, StandardCharsets.UTF_8);
                this.parseResults.put(sysMLURI, result);
            }
        }
    }

    @BeforeClass
    public static synchronized void beforeClass() {
        if (settingDelegateFactoryUsers++ == 0) {
            previousSettingDelegateFactory = EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE
                    .put(SYSML_DELEGATE_URI, SETTING_FACTORY);
        }
    }

    @AfterClass
    public static synchronized void afterClass() {
        settingDelegateFactoryUsers--;
        if (settingDelegateFactoryUsers == 0) {
            if (previousSettingDelegateFactory == null) {
                EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE.remove(SYSML_DELEGATE_URI);
            } else {
                EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE.put(SYSML_DELEGATE_URI,
                        previousSettingDelegateFactory);
            }
            previousSettingDelegateFactory = null;
        }
    }

    private File[] getSysMLFiles(File sysMLFolder) {
        File[] res = sysMLFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.canRead() && pathname.getName().endsWith(".sysml");
            }

        });
        return res;
    }

    /**
     * Gets the <code>sysml</code> folder.
     *
     * @return the <code>sysml</code> folder
     */
    private File getSysMLFolder() {
        return new File(this.testFolder, "sysml");
    }

    /**
     * Parses the current test.
     *
     * @param parser
     *            the {@link SysMLModelParser}
     * @param is
     *            the {@link InputStream} to parse
     * @param charset
     *            the {@link Charset} to use
     *
     * @return the {@link ISysMLParseResult}
     */
    protected abstract ISysMLParseResult<?> parse(SysMLModelParser parser, InputStream is, Charset charset) throws IOException;

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

    @Test
    public void checkModel() throws IOException {
        for (Entry<URI, ISysMLParseResult<?>> entry : this.parseResults.entrySet()) {
            this.assertModel(entry.getKey(), entry.getValue());
        }
    }

    private void assertModel(URI uri, ISysMLParseResult<?> result) throws IOException {
        URI expectedModelURI = this.getExpectedModelURI(uri);
        String actualModelString = this.getModelString(result.model());
        if (URIConverter.INSTANCE.exists(expectedModelURI, null)) {
            String expectedModelString = TestUtils.getContent(URIConverter.INSTANCE, expectedModelURI, StandardCharsets.UTF_8.name());
            assertEquals(expectedModelString, actualModelString);
        } else {
            URI actualModelURI = this.getActualModelURI(uri);
            TestUtils.setContent(URIConverter.INSTANCE, actualModelURI, StandardCharsets.UTF_8.name(), actualModelString);
            fail(expectedModelURI + " doesn't exist.");
        }
    }

    private String getModelString(Object model) {
        final String res;

        if (model instanceof EObject) {
            String xmi = "";
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                Map<Object, Object> options = new HashMap<>();
                options.put(XMLResource.OPTION_ENCODING, StandardCharsets.UTF_8.name());
                Resource resource = new XMIResourceImpl();
                resource.getContents().add((EObject) model);
                ((EObject) model).eResource().save(stream, options);
                xmi = stream.toString();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                fail(e);
            }
            res = xmi;
        } else if (model instanceof Enumerator) {
            res = ((Enumerator) model).getName();
        } else if (model != null) {
            res = model.toString();
        } else {
            res = "null";
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

    private URI getExpectedSyntaxErrorsURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + "-syntax-expected.txt");
    }

    private URI getActualSyntaxErrorsURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + "-syntax-actual.txt");
    }

    private URI getExpectedDanglingLinksURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + "-links-expected.txt");
    }

    private URI getActualDanglingLinksURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + "-links-actual.txt");
    }

    private URI getExpectedTextAdaptedURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + "-text-adapters-expected.txt");
    }

    private URI getActualTextAdaptedURI(URI uri) {
        String lastSegment = uri.trimFileExtension().lastSegment();
        return URI.createURI(URI.createURI(MODEL_FOLDER).resolve(uri).toString() + lastSegment + "-text-adapters-actual.txt");
    }

    @Test
    public void checkLinks() throws IOException {
        for (Entry<URI, ISysMLParseResult<?>> entry : this.parseResults.entrySet()) {
            this.assertDanglingLinks(entry.getKey(), entry.getValue());
        }
    }

    private void assertDanglingLinks(URI uri, ISysMLParseResult<?> result) throws IOException {
        URI expectedDanglingLinksURI = this.getExpectedDanglingLinksURI(uri);
        String actualDanglingLinksString = this.getDanglingLinksString(result.danglingLinks());
        if (URIConverter.INSTANCE.exists(expectedDanglingLinksURI, null)) {
            String expectedDanglingLinksString = TestUtils.getContent(URIConverter.INSTANCE, expectedDanglingLinksURI, StandardCharsets.UTF_8.name());
            assertEquals(expectedDanglingLinksString, actualDanglingLinksString);
        } else {
            URI actualDanglingLinksURI = this.getActualDanglingLinksURI(uri);
            TestUtils.setContent(URIConverter.INSTANCE, actualDanglingLinksURI, StandardCharsets.UTF_8.name(), actualDanglingLinksString);
            fail(expectedDanglingLinksURI + " doesn't exist.");
        }
    }

    private String getDanglingLinksString(Set<DanglingLink> danglingLinks) {
        StringBuilder res = new StringBuilder();

        for (DanglingLink link : danglingLinks) {
            String uriFragment = this.getURIFragment(link.context());
            int index = 0;
            for (String target : link.targets()) {
                res.append(String.format("%s;%s;%s;%s;%s\n", uriFragment, link.reference().getName(), link.eClass().getName(), index++, target));
            }
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

    @Test
    public void checkDiagnostic() throws IOException {
        for (Entry<URI, ISysMLParseResult<?>> entry : this.parseResults.entrySet()) {
            this.assertDiagnostic(entry.getKey(), entry.getValue());
        }
    }

    private void assertDiagnostic(URI uri, ISysMLParseResult<?> result) throws IOException {
        URI expectedSyntaxErrorsURI = this.getExpectedSyntaxErrorsURI(uri);
        String actualSynthaxErrorsString = this.getSyntaxErrorsString(result.diagnostic());
        if (URIConverter.INSTANCE.exists(expectedSyntaxErrorsURI, null)) {
            String expectedSyntaxtErrorsString = TestUtils.getContent(URIConverter.INSTANCE, expectedSyntaxErrorsURI, StandardCharsets.UTF_8.name());
            assertEquals(expectedSyntaxtErrorsString, actualSynthaxErrorsString);
        } else {
            URI actualSyntaxErrorsURI = this.getActualSyntaxErrorsURI(uri);
            TestUtils.setContent(URIConverter.INSTANCE, actualSyntaxErrorsURI, StandardCharsets.UTF_8.name(), actualSynthaxErrorsString);
            fail(expectedSyntaxErrorsURI + " doesn't exist.");
        }
    }

    /**
     * Formats syntax diagnostics with source locations when available, including location-free recovery failures.
     *
     * @param diagnostic the parser diagnostic
     * @return its golden-file representation
     */
    private String getSyntaxErrorsString(Diagnostic diagnostic) {
        StringBuilder res = new StringBuilder();

        for (Diagnostic child : diagnostic.getChildren()) {
            List<?> data = child.getData();
            if (data.size() >= 2) {
                res.append(String.format("%s:%s - ", data.get(0), data.get(1)));
            }
            res.append(child.getMessage()).append('\n');
        }

        return res.toString();
    }

    @Test
    public void checkTextAdapters() throws IOException {
        for (Entry<URI, ISysMLParseResult<?>> entry : this.parseResults.entrySet()) {
            this.assertTextAdapted(entry.getKey(), entry.getValue());
        }
    }

    private void assertTextAdapted(URI uri, ISysMLParseResult<?> result) throws IOException {
        URI expectedTextAdaptedURI = this.getExpectedTextAdaptedURI(uri);
        String actualTextAdaptedString = this.getTextAdaptedString(result.textAdapted());
        if (URIConverter.INSTANCE.exists(expectedTextAdaptedURI, null)) {
            String expectedTextAdaptedString = TestUtils.getContent(URIConverter.INSTANCE, expectedTextAdaptedURI, StandardCharsets.UTF_8.name());
            assertEquals(expectedTextAdaptedString, actualTextAdaptedString);
        } else {
            URI actualTextAdaptedURI = this.getActualTextAdaptedURI(uri);
            TestUtils.setContent(URIConverter.INSTANCE, actualTextAdaptedURI, StandardCharsets.UTF_8.name(), actualTextAdaptedString);
            fail(expectedTextAdaptedURI + " doesn't exist.");
        }
    }

    private String getTextAdaptedString(Set<EObject> textAdapted) {
        StringBuilder builder = new StringBuilder();

        for (EObject eObj : textAdapted) {
            List<ITextAdapter> textAdapters = eObj.eAdapters().stream()
                    .filter(adapter -> adapter.isAdapterForType(ITextAdapter.class))
                    .map(ITextAdapter.class::cast)
                    .toList();
            if (!textAdapters.isEmpty()) {
                builder.append(this.getURIFragment(eObj) + ":\n");
            }
            for (ITextAdapter adapter : textAdapters) {
                builder.append(this.textAdapterDescription(adapter) + "\n");
            }
        }

        return builder.toString();
    }

    private String textAdapterDescription(ITextAdapter adapter) {
        String description;
        if (adapter instanceof ICommentBodyAdapter commentBodyAdapter) {
            description = "comment body " + escapeAdapterText(commentBodyAdapter.getRawBody());
        } else if (adapter instanceof INumericLiteralFormatAdapter numericLiteralFormatAdapter) {
            description = "numeric literal " + numericLiteralFormatAdapter.formattedLiteral();
        } else if (adapter instanceof IStringLiteralFormatAdapter stringLiteralFormatAdapter) {
            description = "string literal " + escapeAdapterText(stringLiteralFormatAdapter.getLiteralText());
        } else {
            description = TEXT_ADAPTER_DESCRIPTIONS.get(adapter);
            if (description == null) {
                LOGGER.error("Missing golden-file description for text adapter {}", adapter.getClass().getName());
                return "";
            }
        }
        return description;
    }

    private static String escapeAdapterText(String text) {
        return text.replace("\\", "\\\\").replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t");
    }

    private static Map<ITextAdapter, String> createTextAdapterDescriptions() {
        Map<ITextAdapter, String> descriptions = new IdentityHashMap<>();
        descriptions.put(ITextAdapter.SPECIALIZES_SYMBOL, ":>");
        descriptions.put(ITextAdapter.SPECIALIZES, "specializes");
        descriptions.put(ITextAdapter.SUBSETS_SYMBOL, ":>");
        descriptions.put(ITextAdapter.SUBSETS, "subsets");
        descriptions.put(ITextAdapter.REFERENCES_SYMBOL, "::>");
        descriptions.put(ITextAdapter.REFERENCES, "references");
        descriptions.put(ITextAdapter.CROSSES_SYMBOL, "=>");
        descriptions.put(ITextAdapter.CROSSES, "crosses");
        descriptions.put(ITextAdapter.REDEFINES_SYMBOL, ":>>");
        descriptions.put(ITextAdapter.REDEFINES, "redefines");
        descriptions.put(ITextAdapter.DEFINED_BY, "defined by");
        descriptions.put(ITextAdapter.BODY_BLOCK, "{...}");
        descriptions.put(ITextAdapter.INTERFACE_CONNECTOR_DECLARATION, "interface connector declaration");
        descriptions.put(ITextAdapter.NARY_CONNECTOR_PART, "n-ary connector part");
        descriptions.put(ITextAdapter.MESSAGE_USAGE, "message");
        descriptions.put(ITextAdapter.PARENTHESIZED_FEATURE_VALUE_EXPRESSION, "parenthesized feature value");
        descriptions.put(ITextAdapter.PARENTHESIZED_EXPRESSION, "parenthesized expression");
        descriptions.put(ITextAdapter.PARENTHESIZED_LAMBDA_ARGUMENT,
                "parenthesized lambda argument");
        descriptions.put(ITextAdapter.INTERFACE_MULTIPLICITY_BEFORE_TYPE,
                "interface multiplicity before type");
        descriptions.put(ITextAdapter.TRAILING_COMMA, "trailing comma");
        descriptions.put(ITextAdapter.FIRST_MEMBERSHIP, "first");
        descriptions.put(ITextAdapter.COMMENT_KEYWORD, "comment");
        descriptions.put(ITextAdapter.FEATURE_VALUE_DEFAULT, "default");
        descriptions.put(ITextAdapter.FEATURE_VALUE_DEFAULT_EQUALS, "default =");
        descriptions.put(ITextAdapter.FEATURE_VALUE_DEFAULT_INITIAL, "default :=");
        descriptions.put(ITextAdapter.EXPLICIT_REF_PREFIX, "ref");
        descriptions.put(ITextAdapter.END_FEATURE_KEYWORD, "explicit end feature keyword");
        descriptions.put(ITextAdapter.EXPLICIT_PUBLIC_VISIBILITY, "public");
        descriptions.put(ITextAdapter.QUOTED_ALIAS_TARGET, "quoted alias target");
        descriptions.put(ITextAdapter.SATISFY_ASSERT, "assert");
        descriptions.put(ITextAdapter.REQUIREMENT_CONSTRAINT_KEYWORD, "constraint");
        descriptions.put(ITextAdapter.FLOW_FROM, "from");
        descriptions.put(ITextAdapter.COMPACT_INDIVIDUAL_DEFINITION, "individual def");
        descriptions.put(ITextAdapter.COMPACT_INDIVIDUAL_USAGE, "individual");
        descriptions.put(ITextAdapter.PORTION_KIND_BEFORE_USAGE_KEYWORD, "portion kind before usage keyword");
        descriptions.put(ITextAdapter.METADATA_AT_SIGN, "@");
        descriptions.put(ITextAdapter.METADATA_COLON, "metadata colon");
        descriptions.put(ITextAdapter.PAYLOAD_TYPING_CLAUSE, "payload typing clause");
        descriptions.put(ITextAdapter.CONNECTION_KEYWORD, "connection");
        descriptions.put(ITextAdapter.PREFIX_METADATA, "#");
        descriptions.put(ITextAdapter.QUOTED_NAME, "quoted name");
        descriptions.put(ITextAdapter.QUOTED_SHORT_NAME, "quoted short name");
        descriptions.put(ITextAdapter.LATE_NONUNIQUE, "late nonunique");
        descriptions.put(ITextAdapter.ENUM_KEYWORD_OMITTED, "enum omitted");
        descriptions.put(ITextAdapter.DEPENDENCY_FROM_OMITTED, "dependency from omitted");
        descriptions.put(ITextAdapter.EXPLICIT_TRANSITION_KEYWORD, "transition");
        descriptions.put(ITextAdapter.TRANSITION_FIRST_KEYWORD_OMITTED,
                "transition first omitted");
        descriptions.put(ITextAdapter.EXPLICIT_ACTION_NODE_KEYWORD, "action");
        descriptions.put(ITextAdapter.DEFAULT_TARGET_SUCCESSION, "else");
        descriptions.put(ITextAdapter.SUCCESSION_KEYWORD, "succession");
        descriptions.put(ITextAdapter.RETURN_DIRECTION_OMITTED, "return direction omitted");

        addSourceSyntaxAdapterDescriptions(descriptions);

        long declaredAdapters = Arrays.stream(ITextAdapter.class.getFields())
                .filter(field -> ITextAdapter.class.isAssignableFrom(field.getType()))
                .count();
        if (descriptions.size() != declaredAdapters) {
            LOGGER.error("Expected descriptions for {} ITextAdapter singletons but found {}", declaredAdapters, descriptions.size());
            return Map.of();
        }
        return Map.copyOf(descriptions);
    }

    private static void addSourceSyntaxAdapterDescriptions(Map<ITextAdapter, String> descriptions) {
        descriptions.put(ITextAdapter.TYPING_CLAUSE_START, "typing clause start");
        descriptions.put(ITextAdapter.NONUNIQUE_AFTER_ORDERED, "nonunique after ordered");
        descriptions.put(ITextAdapter.EMPTY_PARENTHESIZED_EXPRESSION, "empty parenthesized expression");
        descriptions.put(ITextAdapter.EXPLICIT_STATE_USAGE_KEYWORD, "state");
        descriptions.put(ITextAdapter.DECLARATION_KEYWORD_OMITTED, "declaration keyword omitted");
        descriptions.put(ITextAdapter.EXPLICIT_REP_KEYWORD, "rep");
        descriptions.put(ITextAdapter.EXPLICIT_INTERFACE_CONNECT_KEYWORD, "connect");
        descriptions.put(ITextAdapter.EXPLICIT_DECLARATION_KEYWORD, "explicit declaration keyword");
    }

}
