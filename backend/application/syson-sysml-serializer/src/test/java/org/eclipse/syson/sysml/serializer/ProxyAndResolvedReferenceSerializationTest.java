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
package org.eclipse.syson.sysml.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.ParserRuleFixture;
import org.eclipse.syson.sysml.serializer.expression.ExpressionReferenceCollector;
import org.eclipse.syson.sysml.serializer.model.DanglingReferenceIndex;
import org.eclipse.syson.sysml.serializer.model.ModelReference;
import org.eclipse.syson.sysml.serializer.model.SerializerModelQueries;
import org.eclipse.syson.sysml.serializer.names.NameRenderer;
import org.eclipse.syson.sysml.serializer.output.StringTextOutput;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceNameMatcher;
import org.eclipse.syson.sysml.serializer.rendering.SysMLTextRenderer;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;
import org.omg.sysml.logic.implicit.specialization.ImplicitSpecializationCacheUtil;
import org.omg.sysml.logic.implicit.specialization.api.IImplicitSpecializationCache;

/**
 * Verifies unresolved references in every distinct serialization context exercised by the representative serializer and
 * parser fixtures. A context combines the containing metaclass, the source metaclass, the {@link EReference}, and the
 * target metaclass. The representative fixtures reach the same contexts as the whole corpus, which only adds size; the
 * {@code syson.serializer.referenceContexts} property writes the contexts reached, to compare both. The parser entry
 * point remains attached to the selected fixture because it is required to serialize parser rule fixtures correctly.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class ProxyAndResolvedReferenceSerializationTest {

    private static final String DESCRIPTION_SEPARATOR = ": ";

    private static final String RESOLVED_NAME_PREFIX = "ResolvedReference_";

    private static final Pattern AUDIT_NAME_PATTERN = Pattern
            .compile("Audit(?:Proxy|Resolved)_\\d{6}");

    private static final SerializationOptions ROUND_TRIP_OPTIONS = SerializationOptions
            .defaults().withMode(SerializationMode.ROUND_TRIP)
            .withImplicitReferenceKeywordElision(false);

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    private static void assertProxyAndResolvedTargetNames(
            final ReferenceCase referenceCase) {
        final ReferenceFixture fixture = referenceCase.fixture();
        final SysMLSerializer serializer = new SysMLSerializer();
        final SerializationOptions options = fixture.serializationOptions();
        final String expectedUnresolvedText = serializer
                .serialize(fixture.parseResult(), options);
        try (ReferenceMutation mutation = installProxyReferences(fixture,
                referenceCase.link())) {
            assertTargetValuesAreInstalled(referenceCase.link(), mutation);
            final DanglingReferenceIndex danglingReferences = DanglingReferenceIndex
                    .from(fixture.parseResult().danglingLinks());
            final SerializationRun unresolved = serializeAndRecordReferences(
                    fixture.parseResult().model(), danglingReferences, options);
            assertEquals(expectedUnresolvedText, unresolved.text(),
                    "Serializing the proxy changed the unresolved reference text for "
                            + mutation.description());
            for (final ReferenceBinding binding : mutation.bindings()) {
                final String proxyName = new NameRenderer()
                        .referenceName(binding.proxy());
                assertReferenceWasEmitted(unresolved, proxyName,
                        binding.proxy(), binding.description());
            }

            mutation.resolveAndRenameProxiesInPlace();
            assertTargetValuesAreInstalled(referenceCase.link(), mutation);
            final SerializationRun resolved = serializeAndRecordReferences(
                    fixture.parseResult().model(), danglingReferences, options);
            for (final ReferenceBinding binding : mutation.bindings()) {
                final String resolvedName = new NameRenderer()
                        .referenceName(binding.proxy());
                assertReferenceWasEmitted(resolved, resolvedName,
                        binding.proxy(), binding.description());
            }
            assertFalse(unresolved.text().equals(resolved.text()),
                    "Resolving and renaming the reference did not change the serialized text for "
                            + mutation.description());
        }
    }

    /**
     * Visible corpus reference occurrences must be bound to the correct targets. Replacing or resolving and renaming
     * proxies must update emitted names and targets without retaining stale names. A reference whose parser spelling
     * is written while its target is an unresolved proxy must name the target once it is replaced by a resolved one.
     */
    @Test
    void everyFixtureSupportsProxyAndResolvedReferenceNames()
            throws IOException {
        final Map<ReferenceContext, ReferenceCase> casesByContext = new LinkedHashMap<>();
        try (Stream<ParserRuleFixture> fixtures = SerializerRoundTripTestSupport.representativeCorpusFixtures()) {
            fixtures.map(ProxyAndResolvedReferenceSerializationTest::parseFixture)
                    .forEach(fixture -> auditAllReferenceEmissions(fixture,
                            casesByContext));
        }
        if (casesByContext.isEmpty()) {
            throw new IllegalStateException(
                    "No dangling reference context was found in serializer or parser fixtures");
        }
        casesByContext.values().forEach(
                ProxyAndResolvedReferenceSerializationTest::assertProxyAndResolvedTargetNames);
        final String contextReport = System
                .getProperty("syson.serializer.referenceContexts");
        if (contextReport != null) {
            Files.write(Path.of(contextReport),
                    casesByContext.keySet().stream()
                            .map(ReferenceContext::toString).sorted().toList(),
                    StandardCharsets.UTF_8);
        }
    }

    /**
     * Verifies every binding and records the first fixture/link pair for each reference context visible under a proxy.
     * Under an unresolved proxy the parser spelling may win, which hides the proxy name; once every proxy is replaced
     * by a resolved target, each binding, hidden or not, must show the target's name, emitted as bound to that target.
     */
    private static void auditAllReferenceEmissions(
            final ReferenceFixture fixture,
            final Map<ReferenceContext, ReferenceCase> casesByContext) {
        try (AllProxyMutation mutation = installAuditProxyReferences(fixture)) {
            final DanglingReferenceIndex danglingReferences = DanglingReferenceIndex
                    .from(fixture.parseResult().danglingLinks());
            final SerializationRun unresolved = serializeAndRecordReferences(
                    fixture.parseResult().model(), danglingReferences,
                    fixture.serializationOptions());
            final Map<String, Long> unresolvedOccurrences = auditNameCounts(
                    unresolved.text());
            final Set<String> visibleProxyNames = auditNames(
                    unresolvedOccurrences, "AuditProxy_");
            final List<AuditBinding> visibleBindings = mutation.bindings()
                    .stream().filter(binding -> visibleProxyNames
                            .contains(binding.proxyName()))
                    .toList();
            for (final AuditBinding binding : visibleBindings) {
                casesByContext.putIfAbsent(
                        ReferenceContext.from(binding.link()),
                        new ReferenceCase(fixture, binding.link()));
                assertAtomicEmission(unresolved, binding.proxyName(),
                        binding.proxy(), binding.description(),
                        unresolvedOccurrences);
            }

            for (final AuditBinding binding : mutation.bindings()) {
                mutation.replaceProxyWithResolvedTarget(binding);
            }
            final SerializationRun resolved = serializeAndRecordReferences(
                    fixture.parseResult().model(), danglingReferences,
                    fixture.serializationOptions());
            final Map<String, Long> resolvedOccurrences = auditNameCounts(
                    resolved.text());
            final Set<String> resolvedProxyNames = auditNames(
                    resolvedOccurrences, "AuditProxy_");
            final Set<String> resolvedNames = auditNames(resolvedOccurrences,
                    "AuditResolved_");
            for (final AuditBinding binding : mutation.bindings()) {
                assertFalse(resolvedProxyNames.contains(binding.proxyName()),
                        () -> "Resolved serialization still contains proxy name "
                                + binding.proxyName() + " for "
                                + binding.description());
                assertTrue(resolvedNames.contains(binding.resolvedName()),
                        () -> "Resolved serialization does not contain "
                                + binding.resolvedName() + " for "
                                + binding.description() + ". Text: "
                                + resolved.text() + ". Unresolved occurrence: "
                                + textSnippets(unresolved.text(),
                                        binding.proxyName()));
                assertAtomicEmission(resolved, binding.resolvedName(),
                        binding.resolvedTarget(), binding.description(),
                        resolvedOccurrences);
            }
        }
    }

    private static void assertAtomicEmission(final SerializationRun run,
            final String name, final EObject target, final String description,
            final Map<String, Long> occurrencesByName) {
        final long occurrences = occurrencesByName.getOrDefault(name, 0L);
        final List<TextOutput.Reference> matchingEmissions = run.emissionIndex()
                .matching(target, name);
        final long emissions = matchingEmissions.size();
        assertEquals(occurrences, emissions,
                () -> "Expected one structured emission per atomic occurrence of "
                        + name + " for " + description + ". Text occurrences: "
                        + occurrences + ", emissions: " + emissions
                        + ", matching emissions: " + matchingEmissions
                        + ", occurrence: " + textSnippets(run.text(), name));
    }

    private static Set<String> auditNames(final Map<String, Long> occurrences,
            final String prefix) {
        return occurrences.keySet().stream()
                .filter(name -> name.startsWith(prefix))
                .collect(Collectors.toUnmodifiableSet());
    }

    private static Map<String, Long> auditNameCounts(final String text) {
        final Map<String, Long> counts = new HashMap<>();
        final Matcher matcher = AUDIT_NAME_PATTERN.matcher(text);
        while (matcher.find()) {
            counts.merge(matcher.group(), 1L, Long::sum);
        }
        return counts;
    }

    private static String textSnippets(final String text, final String name) {
        final int occurrence = text.indexOf(name);
        if (occurrence < 0) {
            return "<missing>";
        }
        final int start = Math.max(0, occurrence - 80);
        final int end = Math.min(text.length(),
                occurrence + name.length() + 80);
        return text.substring(start, end).replace('\n', ' ');
    }

    private static void assertTargetValuesAreInstalled(final DanglingLink link,
            final ReferenceMutation mutation) {
        final List<ModelReference> targets = new SerializerModelQueries().referenceTargets()
                .referenceTargets((Element) link.context(),
                        link.reference().getName(),
                        DanglingReferenceIndex.empty(),
                        new NameRenderer()::referenceName);
        for (final ReferenceBinding binding : mutation.bindings()) {
            assertTrue(
                    targets.stream().anyMatch(
                            target -> target.element() == binding.proxy()),
                    () -> "The test could not install the target for "
                            + binding.description() + DESCRIPTION_SEPARATOR
                            + targets);
        }
    }

    private static boolean isContainedInModel(final ReferenceFixture fixture,
            final DanglingLink link) {
        return link.context() == fixture.parseResult().model() || EcoreUtil
                .isAncestor(fixture.parseResult().model(), link.context());
    }

    private static ReferenceFixture parseFixture(final ParserRuleFixture fixture) {
        try {
            return new ReferenceFixture(fixture, SerializerRoundTripTestSupport.parseAccepted(fixture));
        } catch (final IOException exception) {
            throw new UncheckedIOException("Unable to read reference fixture " + fixture.sysmlPath(), exception);
        }
    }

    private static ReferenceMutation installProxyReferences(
            final ReferenceFixture fixture, final DanglingLink link) {
        final List<ReferenceBinding> bindings = new ArrayList<>();
        if (link.context() == null || link.reference() == null
                || link.eClass() == null || link.targets() == null) {
            fail("Incomplete dangling link in " + fixture.path()
                    + DESCRIPTION_SEPARATOR + link);
        }
        if (!link.reference().isMany() && link.targets().size() > 1) {
            fail("Single-valued reference has several dangling targets in "
                    + fixture.path() + DESCRIPTION_SEPARATOR + link);
        }
        Object previousValue = link.context().eGet(link.reference(), false);
        if (link.reference().isMany()) {
            previousValue = null;
        }
        final boolean previouslySet = link.context().eIsSet(link.reference());
        int ordinal = 0;
        for (final String targetName : link.targets()) {
            final Element proxy = createTarget(link, fixture.path());
            ((InternalEObject) proxy).eSetProxyURI(URI
                    .createURI("proxy:/unresolved").appendFragment(targetName));
            attachReference(link.context(), link.reference(), proxy);
            final String resolvedName = RESOLVED_NAME_PREFIX + ordinal++
                    + "_Target";
            bindings.add(new ReferenceBinding(proxy, resolvedName,
                    referenceDescription(fixture.path(), link, targetName)));
        }
        return new ReferenceMutation(link.context(), link.reference(),
                previouslySet, previousValue, bindings,
                referenceDescription(fixture.path(), link,
                        String.join(", ", link.targets())));
    }

    private static AllProxyMutation installAuditProxyReferences(
            final ReferenceFixture fixture) {
        final List<AuditSlot> slots = new ArrayList<>();
        final Map<EObject, Map<EReference, AuditSlot>> slotsByContext = new IdentityHashMap<>();
        final List<AuditBinding> bindings = new ArrayList<>();
        int ordinal = 0;
        for (final DanglingLink link : fixture.parseResult().danglingLinks()) {
            final boolean incompleteLink = !(link.context() instanceof Element)
                    || link.reference() == null || link.eClass() == null
                    || link.targets() == null;
            if (incompleteLink || !isContainedInModel(fixture, link)) {
                continue;
            }
            final AuditSlot slot = slotsByContext
                    .computeIfAbsent(link.context(),
                            ignored -> new IdentityHashMap<>())
                    .computeIfAbsent(link.reference(), ignored -> {
                        final AuditSlot created = new AuditSlot(link.context(),
                                link.reference());
                        slots.add(created);
                        return created;
                    });
            if (!link.reference().isMany() && slot.hasBinding()) {
                continue;
            }
            for (final String parsedTargetName : link.targets()) {
                final String proxyName = String.format("AuditProxy_%06d",
                        ordinal);
                final String resolvedName = String.format("AuditResolved_%06d",
                        ordinal++);
                final Element proxy = createTarget(link, fixture.path());
                ((InternalEObject) proxy).eSetProxyURI(URI
                        .createURI("proxy:/audit").appendFragment(proxyName));
                final Element resolvedTarget = createTarget(link,
                        fixture.path());
                resolvedTarget.setDeclaredName(resolvedName);
                attachReference(link.context(), link.reference(), proxy);
                final AuditBinding binding = new AuditBinding(proxy, proxyName,
                        resolvedName, resolvedTarget, link.context(),
                        link.reference(), link, referenceDescription(
                                fixture.path(), link, parsedTargetName));
                slot.add(binding);
                bindings.add(binding);
                if (!link.reference().isMany()) {
                    break;
                }
            }
        }
        slots.forEach(AuditSlot::assertBindingsInstalled);
        return new AllProxyMutation(slots, bindings);
    }

    private static Element createTarget(final DanglingLink link,
            final Path fixturePath) {
        return createTarget(concreteTargetClass(link.eClass()), fixturePath);
    }

    private static Element createTarget(final EClass concreteClass,
            final Path fixturePath) {
        try {
            final EObject target = concreteClass.getEPackage()
                    .getEFactoryInstance().create(concreteClass);
            return assertInstanceOf(Element.class, target,
                    () -> "Reference target is not a SysML Element in "
                            + fixturePath + DESCRIPTION_SEPARATOR
                            + concreteClass.getName());
        } catch (final IllegalArgumentException exception) {
            throw new AssertionError(
                    "Cannot instantiate reference target "
                            + concreteClass.getName() + " in " + fixturePath,
                    exception);
        }
    }

    private static EClass concreteTargetClass(final EClass requestedClass) {
        if (!requestedClass.isAbstract() && !requestedClass.isInterface()) {
            return requestedClass;
        }
        return SysMLPackage.eINSTANCE.getEClassifiers().stream()
                .filter(EClass.class::isInstance).map(EClass.class::cast)
                .filter(candidate -> !candidate.isAbstract()
                        && !candidate.isInterface())
                .filter(requestedClass::isSuperTypeOf)
                .min(Comparator
                        .comparingInt((final EClass candidate) -> candidate
                                .getEAllSuperTypes().size())
                        .thenComparing(EClassifier::getName))
                .orElseThrow(() -> new AssertionError(
                        "No concrete SysML subtype for "
                                + requestedClass.getName()));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void attachReference(final EObject context,
            final EReference reference, final Element target) {
        if (reference.isMany()) {
            ((List<EObject>) context.eGet(reference, false)).add(target);
        } else {
            context.eSet(reference, target);
        }
    }

    private static SerializationRun serializeAndRecordReferences(
            final Element root,
            final DanglingReferenceIndex danglingReferenceIndex,
            final SerializationOptions options) {
        final EObject modelRoot = EcoreUtil.getRootContainer(root);
        assertNull(modelRoot.eResource(),
                "Reference fixtures must be detached before serialization");
        final ResourceImpl resource = new ResourceImpl();
        resource.getContents().add(modelRoot);
        try {
            ImplicitSpecializationCacheUtil.installCaches(resource);
            final RecordingTextOutput output = new RecordingTextOutput(
                    new StringTextOutput(options.formatting()));
            final SerializationContext context = new SerializationContext(
                    options, output, new NameRenderer(),
                    new SerializerModelQueries(), danglingReferenceIndex);
            new SysMLTextRenderer(context).serialize(root);
            return new SerializationRun(output.content(), output.emissions());
        } finally {
            // Every proxy mutation must be evaluated with fresh specialization results.
            resource.getAllContents()
                    .forEachRemaining(element -> element.eAdapters().removeIf(
                            IImplicitSpecializationCache.class::isInstance));
            resource.getContents().clear();
        }
    }

    private static void assertReferenceWasEmitted(final SerializationRun run,
            final String expectedName, final EObject expectedTarget,
            final String description) {
        final String targetSegment = ExpressionReferenceCollector
                .atomicReferences(expectedName, expectedTarget).stream()
                .filter(reference -> reference.target() == expectedTarget)
                .map(TextOutput.Reference::name).findFirst()
                .orElse(expectedName);
        assertTrue(
                run.emissions().stream().anyMatch(
                        emission -> emission.target() == expectedTarget
                                && ReferenceNameMatcher.equivalent(
                                        emission.name(), targetSegment)),
                () -> "The serializer did not emit reference name '"
                        + targetSegment + "' from '" + expectedName + "' for "
                        + description + ". Reference emissions: "
                        + run.emissions());
    }

    private static String referenceDescription(final Path path,
            final DanglingLink link, final String targetName) {
        final EObject container = link.context().eContainer();
        String containingType = "<root>";
        if (container != null) {
            containingType = container.eClass().getName();
        }
        return path + " - " + containingType + " / "
                + link.context().eClass().getName() + "."
                + link.reference().getName() + " -> " + link.eClass().getName()
                + " '" + targetName + "'";
    }

    /** Holds a parsed fixture and its serialization options. */
    private record ReferenceFixture(ParserRuleFixture specification,
            ISysMLParseResult<? extends Element> parseResult) {

        private Path path() {
            return this.specification.sysmlPath();
        }

        private SerializationOptions serializationOptions() {
            return ROUND_TRIP_OPTIONS
                    .withParserEntryPoint(this.specification.parseMethod());
        }

        @Override
        public String toString() {
            return this.specification.toString();
        }
    }

    /** Identifies one dangling reference exercised by a fixture. */
    private record ReferenceCase(ReferenceFixture fixture, DanglingLink link) {

        @Override
        public String toString() {
            return ReferenceContext.from(this.link) + " - "
                    + this.fixture.path();
        }
    }

    /** Stable key describing the model context of a reference. */
    private record ReferenceContext(String containingType, String contextType,
            String referenceName, String targetType) {

        private static ReferenceContext from(final DanglingLink link) {
            final EObject container = link.context().eContainer();
            String containingType = "<root>";
            if (container != null) {
                containingType = container.eClass().getName();
            }
            return new ReferenceContext(containingType,
                    link.context().eClass().getName(),
                    link.reference().getName(), link.eClass().getName());
        }

        @Override
        public String toString() {
            return this.containingType + " / " + this.contextType + "."
                    + this.referenceName + " -> " + this.targetType;
        }
    }

    /** Captures one temporary reference mutation. */
    private record ReferenceBinding(Element proxy, String resolvedName,
            String description) {
    }

    /** Captures one proxy installed for the exhaustive emission audit. */
    private record AuditBinding(Element proxy, String proxyName,
            String resolvedName, Element resolvedTarget, EObject context,
            EReference reference, DanglingLink link, String description) {
    }

    /** Owns the temporary values installed in one EMF reference slot. */
    private static final class AuditSlot {

        private final EObject context;

        private final EReference reference;

        private final boolean previouslySet;

        private final Object previousValue;

        private final List<AuditBinding> bindings = new ArrayList<>();

        @SuppressWarnings({ "rawtypes", "unchecked" })
        private AuditSlot(final EObject context, final EReference reference) {
            this.context = context;
            this.reference = reference;
            this.previouslySet = context.eIsSet(reference);
            if (reference.isMany()) {
                this.previousValue = List
                        .copyOf((List<EObject>) context.eGet(reference, false));
            } else {
                this.previousValue = context.eGet(reference, false);
            }
        }

        private boolean hasBinding() {
            return !this.bindings.isEmpty();
        }

        private void add(final AuditBinding binding) {
            this.bindings.add(binding);
        }

        private void assertBindingsInstalled() {
            final Map<EObject, Boolean> installed = new IdentityHashMap<>();
            final Object value = this.context.eGet(this.reference, false);
            if (value instanceof final List<?> values) {
                values.stream().filter(EObject.class::isInstance)
                        .map(EObject.class::cast)
                        .forEach(element -> installed.put(element, true));
            } else if (value instanceof final EObject element) {
                installed.put(element, true);
            }
            for (final AuditBinding binding : this.bindings) {
                assertTrue(installed.containsKey(binding.proxy()),
                        () -> "The audit could not read installed proxy "
                                + binding.proxyName() + " for "
                                + binding.description());
            }
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        private void restore() {
            if (this.bindings.isEmpty()) {
                return;
            }
            if (this.reference.isMany()) {
                final List<EObject> values = (List<EObject>) this.context
                        .eGet(this.reference, false);
                values.clear();
                values.addAll((List<EObject>) this.previousValue);
            } else if (this.previouslySet) {
                this.context.eSet(this.reference, this.previousValue);
            } else {
                this.context.eUnset(this.reference);
            }
        }
    }

    /** Restores all reference slots modified by an exhaustive proxy audit. */
    private static final class AllProxyMutation implements AutoCloseable {

        private final List<AuditSlot> slots;

        private final List<AuditBinding> bindings;

        private AllProxyMutation(final List<AuditSlot> slots,
                final List<AuditBinding> bindings) {
            this.slots = List.copyOf(slots);
            this.bindings = List.copyOf(bindings);
        }

        private List<AuditBinding> bindings() {
            return this.bindings;
        }

        private void replaceProxyWithResolvedTarget(
                final AuditBinding binding) {
            if (binding.reference().isMany()) {
                @SuppressWarnings({ "rawtypes", "unchecked" })
                final List<EObject> values = (List<EObject>) binding.context()
                        .eGet(binding.reference(), false);
                for (int index = 0; index < values.size(); index++) {
                    if (values.get(index) == binding.proxy()) {
                        values.set(index, binding.resolvedTarget());
                    }
                }
            } else if (binding.context().eGet(binding.reference(),
                    false) == binding.proxy()) {
                binding.context().eSet(binding.reference(),
                        binding.resolvedTarget());
            }
        }

        @Override
        public void close() {
            for (int index = this.slots.size() - 1; index >= 0; index--) {
                this.slots.get(index).restore();
            }
        }
    }

    /** Appends proxies for one reference and removes its own additions or restores its single-valued slot on close. */
    private static final class ReferenceMutation implements AutoCloseable {

        private final EObject context;

        private final EReference reference;

        private final boolean previouslySet;

        private final Object previousValue;

        private final List<ReferenceBinding> bindings;

        private final String description;

        private ReferenceMutation(final EObject context,
                final EReference reference, final boolean previouslySet,
                final Object previousValue,
                final List<ReferenceBinding> bindings,
                final String description) {
            this.context = context;
            this.reference = reference;
            this.previouslySet = previouslySet;
            this.previousValue = previousValue;
            this.bindings = List.copyOf(bindings);
            this.description = description;
        }

        private List<ReferenceBinding> bindings() {
            return this.bindings;
        }

        private String description() {
            return this.description;
        }

        private void resolveAndRenameProxiesInPlace() {
            for (final ReferenceBinding binding : this.bindings) {
                ((InternalEObject) binding.proxy()).eSetProxyURI(null);
                binding.proxy().setDeclaredName(binding.resolvedName());
            }
        }

        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void close() {
            if (this.reference.isMany()) {
                final List<EObject> values = (List<EObject>) this.context
                        .eGet(this.reference, false);
                for (final ReferenceBinding binding : this.bindings) {
                    values.removeIf(value -> value == binding.proxy());
                }
            } else if (this.previouslySet) {
                this.context.eSet(this.reference, this.previousValue);
            } else {
                this.context.eUnset(this.reference);
            }
        }
    }

    /** Text and structured reference occurrences from one serialization. */
    private record SerializationRun(String text,
            List<TextOutput.Reference> emissions,
            ReferenceEmissionIndex emissionIndex) {

        private SerializationRun(final String text,
                final List<TextOutput.Reference> emissions) {
            this(text, emissions, ReferenceEmissionIndex.from(emissions));
        }
    }

    /** Indexes structured emissions by target identity and rendered name. */
    private static final class ReferenceEmissionIndex {

        private final Map<EObject, Map<String, List<TextOutput.Reference>>> emissionsByTarget = new IdentityHashMap<>();

        private static ReferenceEmissionIndex from(
                final List<TextOutput.Reference> emissions) {
            final ReferenceEmissionIndex index = new ReferenceEmissionIndex();
            for (final TextOutput.Reference emission : emissions) {
                if (emission.target() != null) {
                    index.add(emission);
                }
            }
            return index;
        }

        private void add(final TextOutput.Reference emission) {
            final Matcher matcher = AUDIT_NAME_PATTERN.matcher(emission.name());
            while (matcher.find()) {
                this.emissionsByTarget
                        .computeIfAbsent(emission.target(),
                                ignored -> new HashMap<>())
                        .computeIfAbsent(matcher.group(),
                                ignored -> new ArrayList<>())
                        .add(emission);
            }
        }

        private List<TextOutput.Reference> matching(final EObject target,
                final String name) {
            return this.emissionsByTarget.getOrDefault(target, Map.of())
                    .getOrDefault(name, List.of());
        }
    }

    /** Records reference emissions while delegating ordinary text output. */
    private static final class RecordingTextOutput implements TextOutput {

        private final TextOutput delegate;

        private final List<TextOutput.Reference> emissions = new ArrayList<>();

        private RecordingTextOutput(final TextOutput delegate) {
            this.delegate = delegate;
        }

        @Override
        public TextOutput beginElement(final Element element) {
            this.delegate.beginElement(element);
            return this;
        }

        @Override
        public TextOutput endElement(final Element element) {
            this.delegate.endElement(element);
            return this;
        }

        @Override
        public TextOutput token(final String token) {
            this.delegate.token(token);
            return this;
        }

        @Override
        public TextOutput referenceName(final String name,
                final EObject target) {
            this.emissions.add(new TextOutput.Reference(name, target));
            this.delegate.referenceName(name, target);
            return this;
        }

        @Override
        public TextOutput referenceText(final String text,
                final List<Reference> references) {
            this.emissions.addAll(references);
            this.delegate.referenceText(text, references);
            return this;
        }

        @Override
        public TextOutput space() {
            this.delegate.space();
            return this;
        }

        @Override
        public TextOutput newline() {
            this.delegate.newline();
            return this;
        }

        @Override
        public TextOutput beginBlock() {
            this.delegate.beginBlock();
            return this;
        }

        @Override
        public TextOutput endBlock() {
            this.delegate.endBlock();
            return this;
        }

        @Override
        public TextOutput raw(final String text) {
            this.delegate.raw(text);
            return this;
        }

        @Override
        public int mark() {
            return this.delegate.mark();
        }

        @Override
        public boolean changedSince(final int mark) {
            return this.delegate.changedSince(mark);
        }

        @Override
        public TextOutput ensureTrailingNewlineIfChanged(final int mark) {
            this.delegate.ensureTrailingNewlineIfChanged(mark);
            return this;
        }

        @Override
        public String content() {
            return this.delegate.content();
        }

        private List<TextOutput.Reference> emissions() {
            return List.copyOf(this.emissions);
        }
    }
}
