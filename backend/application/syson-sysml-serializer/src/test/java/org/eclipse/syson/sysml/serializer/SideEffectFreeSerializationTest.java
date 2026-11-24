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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.ParserRuleFixture;
import org.eclipse.syson.sysml.serializer.model.SafeEmfAccess;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Specifies that serialization is strictly free of side effects on the model: it never resolves a proxy, never asks
 * the {@link org.eclipse.emf.ecore.resource.ResourceSet} for an object or a resource, never triggers a notification
 * and leaves every feature value and adapter list as it found them.
 * <p>
 * Each representative corpus fixture is parsed, and every reference the parser recorded gets a target installed, as
 * linking would: once an unresolvable proxy, once a resolved element held by another resource of the same resource
 * set. The model is then serialized in both modes, as text and HTML, with and without the parser spellings and a
 * contextual name resolver.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class SideEffectFreeSerializationTest {

    private static final URI UNLOADED_RESOURCE = URI.createURI("unloaded:/library.sysml");

    private static final URI TARGET_RESOURCE = URI.createURI("memory:/targets.sysml");

    /** Marks a feature whose value the SysML logic computes or holds, such as the element id. */
    private static final String SYSML_LOGIC = "http://www.omg.org/spec/SysML";

    /** Restricts the corpus to fixtures whose description contains this text, to investigate one report. */
    private static final String FIXTURE_FILTER_PROPERTY = "syson.serializer.sideEffects.fixtures";

    private static final String SERIALIZER_PACKAGE = "org.eclipse.syson.sysml.serializer.";

    private static final int REPORTED_ORIGINS = 40;

    /** The features redefined in each metaclass; a metaclass never changes. */
    private static final Map<EClass, Set<EObject>> REDEFINED_FEATURES = new HashMap<>();

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * Serializing a model whose references hold resolved targets, in any mode and output format, must not notify or
     * modify anything, nor attach the adapters and caches of the SysML logic.
     */
    @Test
    void serializingResolvedReferencesHasNoSideEffect() throws IOException {
        this.assertNoSideEffectOnCorpus(false);
    }

    /**
     * Serializing a model whose references hold unresolved proxies must not try to resolve them, nor notify or modify
     * anything.
     */
    @Test
    void serializingProxyReferencesHasNoSideEffect() throws IOException {
        this.assertNoSideEffectOnCorpus(true);
    }

    private void assertNoSideEffectOnCorpus(final boolean proxies) throws IOException {
        final SideEffects sideEffects = new SideEffects();
        int fixtureCount = 0;
        final String only = System.getProperty(FIXTURE_FILTER_PROPERTY, "");
        try (Stream<ParserRuleFixture> fixtures = SerializerRoundTripTestSupport.representativeCorpusFixtures()) {
            for (final ParserRuleFixture fixture : fixtures
                    .filter(candidate -> candidate.toString().contains(only)).toList()) {
                this.serializeFixture(fixture, proxies, sideEffects);
                fixtureCount++;
            }
        }
        assertTrue(fixtureCount > 0, "No corpus fixture was found");
        if (!sideEffects.isEmpty()) {
            fail(sideEffects.report());
        }
    }

    private void serializeFixture(final ParserRuleFixture fixture, final boolean proxies, final SideEffects sideEffects) throws IOException {
        final ISysMLParseResult<? extends Element> parseResult = SerializerRoundTripTestSupport.parseAccepted(fixture);
        final Element root = parseResult.model();
        final EObject modelRoot = EcoreUtil.getRootContainer(root);
        final RecordingResourceSet resourceSet = new RecordingResourceSet(sideEffects, fixture);
        final Resource resource = new ResourceImpl(URI.createURI("memory:/" + fixture.sysmlPath().getFileName()));
        resourceSet.getResources().add(resource);
        resource.getContents().add(modelRoot);
        final Resource targets = new ResourceImpl(TARGET_RESOURCE);
        resourceSet.getResources().add(targets);
        installTargets(parseResult, proxies, targets);

        final NotificationRecorder recorder = new NotificationRecorder(sideEffects, fixture);
        final List<EObject> objects = new ArrayList<>();
        objects.add(modelRoot);
        modelRoot.eAllContents().forEachRemaining(objects::add);
        targets.getAllContents().forEachRemaining(objects::add);
        objects.forEach(object -> object.eAdapters().add(recorder));
        // Values first: reading a stored feature that the metamodel redefines as a computed one may attach logic adapters.
        final Map<EObject, Map<String, Object>> before = values(objects);
        final Map<EObject, List<Adapter>> adaptersBefore = adapters(objects);
        final List<Object> resourceAdaptersBefore = List.copyOf(resource.eAdapters());

        resourceSet.recording = true;
        serialize(parseResult, fixture);
        resourceSet.recording = false;

        final Map<EObject, List<Adapter>> adaptersAfter = adapters(objects);
        final Map<EObject, Map<String, Object>> after = values(objects);
        for (final EObject object : objects) {
            if (!adaptersAfter.get(object).equals(adaptersBefore.get(object))) {
                sideEffects.add("adapters changed on " + object.eClass().getName() + ": " + adaptersAfter.get(object).stream()
                        .filter(adapter -> !adaptersBefore.get(object).contains(adapter))
                        .map(adapter -> adapter.getClass().getSimpleName())
                        .toList(), fixture);
            }
            final Map<String, Object> previous = before.get(object);
            final List<String> changed = after.get(object).entrySet().stream()
                    .filter(entry -> !Objects.equals(entry.getValue(), previous.get(entry.getKey())))
                    .map(Map.Entry::getKey)
                    .toList();
            if (!changed.isEmpty()) {
                sideEffects.add("values changed on " + object.eClass().getName() + ": " + changed, fixture);
            }
        }
        if (!List.copyOf(resource.eAdapters()).equals(resourceAdaptersBefore)) {
            sideEffects.add("resource adapters changed", fixture);
        }
    }

    /**
     * Serializes once per variation: normalized text, round-trip HTML with links, contextual naming, and a request
     * without the parser spellings, which names every installed target from the model.
     */
    private static void serialize(final ISysMLParseResult<? extends Element> parseResult, final ParserRuleFixture fixture) {
        final SerializationOptions normalized = SerializationOptions.defaults().withParserEntryPoint(fixture.parseMethod());
        final SerializationRequest request = SerializationRequest.forParseResult(parseResult);
        final SysMLSerializer serializer = new SysMLSerializer();
        serializer.serialize(request.withOptions(normalized));
        serializer.serializeToHtml(request.withOptions(normalized.withMode(SerializationMode.ROUND_TRIP)).withHtmlLinks(target -> Optional.of("#")));
        serializer.serialize(request.withOptions(normalized).withContextualNames((target, context) -> ""));
        serializer.serialize(SerializationRequest.forElement(parseResult.model()).withOptions(normalized));
    }

    /**
     * Installs a target for every spelling of every reference the parser recorded, as linking would: an unresolvable
     * proxy named after the spelling, or a resolved element of that name held by another resource.
     */
    private static void installTargets(final ISysMLParseResult<? extends Element> parseResult, final boolean proxies, final Resource targets) {
        for (final DanglingLink link : parseResult.danglingLinks()) {
            if (isInstallable(link)) {
                final List<String> spellings = link.targets();
                int count = 1;
                if (link.reference().isMany()) {
                    count = spellings.size();
                }
                for (int index = 0; index < Math.min(count, spellings.size()); index++) {
                    attach(link.context(), link.reference(), target(link.eClass(), spellings.get(index), proxies, targets));
                }
            }
        }
    }

    private static boolean isInstallable(final DanglingLink link) {
        final boolean complete = link.context() != null && link.reference() != null && link.eClass() != null;
        return complete && link.targets() != null && link.reference().isChangeable() && !link.reference().isDerived();
    }

    private static Element target(final EClass requestedClass, final String spelling, final boolean proxy, final Resource targets) {
        final Element target = (Element) EcoreUtil.create(concreteClass(requestedClass));
        if (proxy) {
            ((InternalEObject) target).eSetProxyURI(UNLOADED_RESOURCE.appendFragment(spelling));
        } else {
            target.setDeclaredName(spelling.substring(spelling.lastIndexOf(':') + 1));
            targets.getContents().add(target);
        }
        return target;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void attach(final EObject context, final EReference reference, final Element target) {
        if (reference.isMany()) {
            ((List) context.eGet(reference, false)).add(target);
        } else {
            context.eSet(reference, target);
        }
    }

    /** Returns the class itself when concrete, else its concrete SysML subclass with the fewest supertypes. */
    private static EClass concreteClass(final EClass requestedClass) {
        if (!requestedClass.isAbstract() && !requestedClass.isInterface()) {
            return requestedClass;
        }
        return SysMLPackage.eINSTANCE.getEClassifiers().stream()
                .filter(EClass.class::isInstance)
                .map(EClass.class::cast)
                .filter(candidate -> !candidate.isAbstract() && !candidate.isInterface() && requestedClass.isSuperTypeOf(candidate))
                .min(Comparator.comparingInt((final EClass candidate) -> candidate.getEAllSuperTypes().size()).thenComparing(EClass::getName))
                .orElse(SysMLPackage.eINSTANCE.getNamespace());
    }

    /** Tells whether a feature holds its own value, as opposed to computing it from other features. */
    private static boolean isStored(final EStructuralFeature feature) {
        return !feature.isDerived() && !feature.isVolatile() && !feature.isTransient() && feature.getEAnnotation(SYSML_LOGIC) == null;
    }

    /** Tells whether another feature of the metaclass redefines the attribute, whose getter then computes it. */
    private static boolean isRedefined(final EClass eClass, final EStructuralFeature feature) {
        return REDEFINED_FEATURES.computeIfAbsent(eClass, SideEffectFreeSerializationTest::redefinedFeatures).contains(feature);
    }

    private static Set<EObject> redefinedFeatures(final EClass eClass) {
        return eClass.getEAllStructuralFeatures().stream()
                .flatMap(candidate -> candidate.getEAnnotations().stream())
                .filter(annotation -> "redefines".equals(annotation.getSource()))
                .flatMap(annotation -> annotation.getReferences().stream())
                .collect(Collectors.toSet());
    }

    private static Map<EObject, List<Adapter>> adapters(final List<EObject> objects) {
        final Map<EObject, List<Adapter>> adapters = new IdentityHashMap<>();
        objects.forEach(object -> adapters.put(object, List.copyOf(object.eAdapters())));
        return adapters;
    }

    /**
     * Captures, per object, every reference value (without resolving), every stored attribute value and its proxy URI,
     * reading the model the way the serializer does. Attributes held, computed or redefined by the SysML logic, such as
     * the member name of an owning membership, are skipped: reading them would itself change the model.
     */
    private static Map<EObject, Map<String, Object>> values(final List<EObject> objects) {
        final Map<EObject, Map<String, Object>> snapshot = new IdentityHashMap<>();
        for (final EObject object : objects) {
            final Map<String, Object> values = new TreeMap<>();
            for (final EStructuralFeature feature : object.eClass().getEAllStructuralFeatures()) {
                if (feature instanceof final EReference reference) {
                    values.put(feature.getName(), List.copyOf(SafeEmfAccess.unresolvedValues(object, reference)));
                } else if (isStored(feature) && !isRedefined(object.eClass(), feature)) {
                    values.put(feature.getName(), value(object, feature));
                }
            }
            values.put("<proxyURI>", ((InternalEObject) object).eProxyURI());
            snapshot.put(object, values);
        }
        return snapshot;
    }

    @SuppressWarnings("checkstyle:IllegalCatch")
    private static Object value(final EObject object, final EStructuralFeature feature) {
        final Object value;
        try {
            value = object.eGet(feature, false);
        } catch (final RuntimeException exception) {
            // Some stored features are redefined by computed ones, which may fail on incomplete models.
            return exception.getClass();
        }
        Object copy = value;
        if (value instanceof final InternalEList<?> list) {
            copy = new ArrayList<>(list.basicList());
        } else if (value instanceof final List<?> list) {
            copy = new ArrayList<>(list);
        }
        return copy;
    }

    /**
     * Groups side effects by where they happened, so that one report lists every culprit: the first metamodel frames
     * below the serializer, then the serializer frames.
     */
    private static final class SideEffects {

        private final Map<String, List<String>> fixturesByOrigin = new TreeMap<>();

        void add(final String kind, final ParserRuleFixture fixture) {
            this.fixturesByOrigin.computeIfAbsent(kind + " at " + frames(), key -> new ArrayList<>()).add(fixture.toString());
        }

        boolean isEmpty() {
            return this.fixturesByOrigin.isEmpty();
        }

        String report() {
            final Map<String, List<String>> reported = new LinkedHashMap<>();
            // Causes, which name a serializer frame, come before their consequences on the model.
            this.fixturesByOrigin.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey(Comparator.comparing((String origin) -> origin.endsWith("[]")).thenComparing(origin -> origin)))
                    .limit(REPORTED_ORIGINS)
                    .forEach(entry -> reported.put(entry.getKey(), entry.getValue()));
            final StringBuilder text = new StringBuilder(this.fixturesByOrigin.size() + " side-effect origins:\n");
            reported.forEach((origin, fixtures) -> text.append(fixtures.size()).append(" x ").append(origin).append("\n    e.g. ").append(fixtures.get(0)).append('\n'));
            return text.toString();
        }

        private static String frames() {
            final List<StackTraceElement> frames = List.of(Thread.currentThread().getStackTrace());
            final String metamodelFrames = frames.stream()
                    .dropWhile(frame -> !frame.getClassName().contains("SideEffectFree"))
                    .dropWhile(frame -> frame.getClassName().contains("SideEffectFree"))
                    .takeWhile(frame -> !frame.getClassName().startsWith(SERIALIZER_PACKAGE))
                    .filter(frame -> frame.getClassName().startsWith("org.omg"))
                    .limit(4)
                    .map(frame -> frame.getClassName().substring(frame.getClassName().lastIndexOf('.') + 1) + "." + frame.getMethodName())
                    .toList()
                    .toString();
            return metamodelFrames + frames.stream()
                    .filter(frame -> frame.getClassName().startsWith(SERIALIZER_PACKAGE) && !frame.getClassName().contains("SideEffectFree"))
                    .limit(6)
                    .map(frame -> frame.getClassName().substring(SERIALIZER_PACKAGE.length()) + "." + frame.getMethodName() + ":" + frame.getLineNumber())
                    .toList()
                    .toString();
        }
    }

    /** Records every object and resource lookup, which is how EMF resolves a proxy. */
    private static final class RecordingResourceSet extends ResourceSetImpl {

        private final SideEffects sideEffects;

        private final ParserRuleFixture fixture;

        private boolean recording;

        RecordingResourceSet(final SideEffects sideEffects, final ParserRuleFixture fixture) {
            this.sideEffects = sideEffects;
            this.fixture = fixture;
        }

        @Override
        public EObject getEObject(final URI uri, final boolean loadOnDemand) {
            if (this.recording) {
                this.sideEffects.add("proxy resolution", this.fixture);
            }
            return null;
        }

        @Override
        public Resource getResource(final URI uri, final boolean loadOnDemand) {
            if (this.recording) {
                this.sideEffects.add("resource lookup", this.fixture);
            }
            return super.getResource(uri, false);
        }
    }

    /** Records every notification sent by a model object. */
    private static final class NotificationRecorder extends AdapterImpl {

        private final SideEffects sideEffects;

        private final ParserRuleFixture fixture;

        NotificationRecorder(final SideEffects sideEffects, final ParserRuleFixture fixture) {
            this.sideEffects = sideEffects;
            this.fixture = fixture;
        }

        @Override
        public void notifyChanged(final Notification notification) {
            if (!notification.isTouch() || notification.getEventType() == Notification.RESOLVE) {
                this.sideEffects.add("notification " + notification.getEventType(), this.fixture);
            }
        }
    }
}
