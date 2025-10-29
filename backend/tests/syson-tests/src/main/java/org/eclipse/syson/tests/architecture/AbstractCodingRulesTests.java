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
package org.eclipse.syson.tests.architecture;

import static com.tngtech.archunit.core.domain.JavaModifier.ABSTRACT;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.GeneralCodingRules;

import java.lang.annotation.Target;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Superclass of all the coding rules test cases.
 *
 * @author sbegaudeau
 */
public abstract class AbstractCodingRulesTests {

    private static final String GRAPHQL_GUAVA = "graphql.com.google..";

    private static final String GUAVA_ANNOTATIONS = "com.google.common.annotations..";

    private static final String GUAVA_BASE = "com.google.common.base..";

    private static final String GUAVA_COLLECT = "com.google.common.collect..";

    private static final String GUAVA_ESCAPE = "com.google.common.escape..";

    private static final String GUAVA_EVENTBUS = "com.google.common.eventbus..";

    private static final String GUAVA_HASH = "com.google.common.hash..";

    private static final String GUAVA_HTML = "com.google.common.html..";

    private static final String GUAVA_IO = "com.google.common.io..";

    private static final String GUAVA_MATH = "com.google.common.math..";

    private static final String GUAVA_NET = "com.google.common.net..";

    private static final String GUAVA_PRIMITIVES = "com.google.common.primitives..";

    private static final String GUAVA_REFLECT = "com.google.common.reflect..";

    private static final String GUAVA_UTIL = "com.google.common.util..";

    private static final String GUAVA_XML = "com.google.common.xml..";

    private static final String GUAVA_THIRDPARTY = "com.google.thirdparty..";

    private static final String SPRING_STRINGUTILS = "org.springframework.util.StringUtils";

    private static final String TESTCASE_SUFFIX = "TestCases";

    private static final String EMF = "org.eclipse.emf..";

    private static final String EMFJSON = "org.eclipse.sirius.emfjson..";

    private static final String APACHE_COMMONS = "org.apache.commons..";

    private static final String JACKSON_ANNOTATION = "com.fasterxml.jackson.annotation..";

    private static final String JETBRAINS = "org.jetbrains..";

    private static final String IS = "is";

    private static final String GET = "get";

    protected abstract String getProjectRootPackage();

    protected abstract JavaClasses getClasses();

    @Test
    public void noClassesShouldUseGraphQLGuava() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(GRAPHQL_GUAVA);

        rule.check(this.getClasses());
    }

    @Test
    public void noClassesShouldUseGuava() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        GUAVA_ANNOTATIONS,
                        GUAVA_BASE,
                        GUAVA_COLLECT,
                        GUAVA_ESCAPE,
                        GUAVA_EVENTBUS,
                        GUAVA_HASH,
                        GUAVA_HTML,
                        GUAVA_IO,
                        GUAVA_MATH,
                        GUAVA_NET,
                        GUAVA_PRIMITIVES,
                        GUAVA_REFLECT,
                        GUAVA_UTIL,
                        GUAVA_XML,
                        GUAVA_THIRDPARTY
                );

        rule.check(this.getClasses());
    }

    @Test
    public void noClassesShouldUseJacksonAnnotations() {
        var rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should()
                .dependOnClassesThat()
                .resideInAPackage(JACKSON_ANNOTATION);

        rule.check(this.getClasses());
    }

    @Test
    public void noClassesShouldUseJetbrainsImport() {
        var rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should()
                .dependOnClassesThat()
                .resideInAPackage(JETBRAINS);

        rule.check(this.getClasses());
    }

    public void noClassesShouldUseEMF() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should()
                .dependOnClassesThat()
                .resideInAPackage(EMF)
                .orShould()
                .dependOnClassesThat()
                .resideInAPackage(EMFJSON);

        rule.check(this.getClasses());
    }

    @Test
    public void noClassesShouldUseEcoreUtilDelete() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should()
                .callMethod("org.eclipse.emf.ecore.util.EcoreUtil", "delete", "org.eclipse.emf.ecore.EObject")
                .orShould()
                .callMethod("org.eclipse.emf.ecore.util.EcoreUtil", "delete", "org.eclipse.emf.ecore.EObject", "boolean")
                .because("EcoreUtil.delete doesn't work well with Sysml Standard Libraries in the ResourceSet, use DeleteService instead");
        rule.check(this.getClasses());
    }

    public void noClassesShouldUseApacheCommons() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should()
                .dependOnClassesThat()
                .resideInAPackage(APACHE_COMMONS);

        rule.check(this.getClasses());
    }

    @Test
    public void noClassesShouldUseSpringStringUtils() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should()
                .dependOnClassesThat()
                .areAssignableTo(SPRING_STRINGUTILS);

        rule.check(this.getClasses());
    }

    @Test
    public void noClassesShouldUseStandardStreams() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should(GeneralCodingRules.ACCESS_STANDARD_STREAMS);

        rule.check(this.getClasses());
    }

    @Test
    public void noClassesShouldThrowGenericExceptions() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should(GeneralCodingRules.THROW_GENERIC_EXCEPTIONS);

        rule.check(this.getClasses());
    }

    @Test
    public void noClassesShouldUseJavaLogging() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should(GeneralCodingRules.USE_JAVA_UTIL_LOGGING);

        rule.check(this.getClasses());
    }

    @Test
    public void classesShouldUsePublicVisibility() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should().bePackagePrivate()
                .andShould().bePrivate()
                .andShould().beProtected();
        rule.check(this.getClasses());
    }

    @Test
    public void classesShouldHavePublicOrPrivateConstructors() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .should(this.haveProtectedOrPackageConstructor());
        rule.check(this.getClasses());
    }

    private ArchCondition<JavaClass> haveProtectedOrPackageConstructor() {
        return new ArchCondition<>("have a package private constructor") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                boolean hasProtectedOrPackageConstructor = javaClass.getAllConstructors().stream()
                        .filter(javaConstructor -> {
                            return javaConstructor.reflect().getDeclaringClass().equals(javaClass.reflect());
                        })
                        .anyMatch(javaConstructor -> {
                            boolean isProtected  = javaConstructor.getModifiers().contains(JavaModifier.PROTECTED);
                            boolean isPackage = !javaConstructor.getModifiers().contains(JavaModifier.PRIVATE) && !javaConstructor.getModifiers().contains(JavaModifier.PROTECTED) && !javaConstructor.getModifiers().contains(JavaModifier.PUBLIC);
                            return isProtected || isPackage;
                        });

                boolean isAnonymousClass = javaClass.isAnonymousClass();
                boolean isEnum = javaClass.isEnum();

                boolean conditionSatisfied = hasProtectedOrPackageConstructor && !isAnonymousClass && !isEnum;

                String pattern = "The class {0} has a constructor with a protected or package visibility";
                String message = MessageFormat.format(pattern, javaClass.getSimpleName());
                events.add(new SimpleConditionEvent(javaClass, conditionSatisfied, message));
            }
        };
    }

    @Test
    public void interfacesShouldRespectNamingConventions() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .and()
                .areInterfaces()
                .and()
                .areNotAnnotatedWith(Target.class)
                .should()
                .haveSimpleNameStartingWith("I");

        rule.check(this.getClasses());
    }

    @Test
    public void abstractClassesShouldRespectNamingConventions() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .and()
                .areNotInterfaces()
                .and()
                .haveModifier(ABSTRACT)
                .should()
                .haveSimpleNameStartingWith("Abstract");

        rule.check(this.getClasses());
    }

    /**
     * Composition should be used instead of inheritance to share business code since it is way easier to test and it is
     * easier to determine and thus maintain the dependencies of a piece of business code.
     */
    @Test
    public void abstractClassesShouldNotContainBusinessCode() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that()
                .resideInAPackage(this.getProjectRootPackage())
                .and()
                .areNotInterfaces()
                .and()
                .haveModifier(ABSTRACT)
                .should(this.notContainBusinessCode());

        rule.check(this.getClasses());
    }

    /**
     * This predicate will be used to identify business code in a class.
     *
     * <p>
     * For that it will look for the following patterns in a Java class:
     * <p>
     * <ul>
     * <li>methods which are not getters with a field matching the name of the method</li>
     * </ul>
     *
     * @return A predicate which can be used to identify business code in a class
     */
    private ArchCondition<JavaClass> notContainBusinessCode() {
        return new ArchCondition<>("not contain business code") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                boolean isConditionSatisfied = true;

                Set<JavaMethod> methods = javaClass.getMethods();
                Iterator<JavaMethod> iterator = methods.iterator();
                while (isConditionSatisfied && iterator.hasNext()) {
                    JavaMethod javaMethod = iterator.next();
                    String name = javaMethod.getName();
                    if (name.startsWith(IS) && (javaMethod.getRawReturnType().isAssignableTo(Boolean.class) || javaMethod.getRawReturnType().isAssignableTo(boolean.class))) {
                        name = name.substring(IS.length());
                    } else if (name.startsWith(GET)) {
                        name = name.substring(GET.length());
                    }

                    if (!name.isBlank()) {
                        name = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
                        isConditionSatisfied = javaClass.tryGetField(name).isPresent();
                    } else {
                        isConditionSatisfied = false;
                    }
                }

                String message = "The abstract class does not have any business code";
                if (!isConditionSatisfied) {
                    String pattern = "The abstract class {0} does contain business code, please favor composition over inheritance to share business code";
                    message = MessageFormat.format(pattern, javaClass.getSimpleName());
                }
                events.add(new SimpleConditionEvent(javaClass, isConditionSatisfied, message));
            }
        };
    }

    /**
     * Static methods make the lifecycle of the code complex. They make the code harder to reason about and they can
     * very easily become either:
     *
     * <ul>
     * <li>A problem to unit tests a class since they can't be mocked</li>
     * <li>God classes with a collection of unrelated utility methods</li>
     * </ul>
     *
     * With the introduction of this test, it appears that apart from some utility constructors like in the @Immutable
     * classes, we do not have a single static method with a real behavior. Thus, nothing of value will be lost.
     *
     * In this test, we will ensure that static methods are either used to return a builder or they act as a builder
     * directly and are thus used to return a new instance. We will prevent the introduction of any static method in
     * the code apart from the following use cases:
     *
     * <ul>
     * <li>Enum since Java enum are considered as extending java.lang.Enum which comes with static methods</li>
     * <li>Java 8+ lambdas which are compiled to hidden static methods (to make it short)</li>
     * <li>@Parameters methods used by JUnit test cases</li>
     * <li>@Immutable classes which are using a static method to create the builder</li>
     * <li>Static methods defined on records returning the record on which they are defined</li>
     * <li>Static methods returning a class annotated with @Builder</li>
     * </ul>
     */
    @Test
    public void noMethodsShouldBeStatic() {
        ArchRule rule = ArchRuleDefinition.noMethods()
                .that()
                .areDeclaredInClassesThat()
                .resideInAPackage(this.getProjectRootPackage())
                .and()
                .areDeclaredInClassesThat()
                .areNotAssignableTo(Enum.class)
                .and()
                .areDeclaredInClassesThat()
                .areNotAssignableTo(Record.class)
                .and()
                .areDeclaredInClassesThat(this.isNotTestCase())
                .and(this.isNotLambda())
                .and(this.isNotSwitchTable())
                .and(this.isNotRecordStaticBuilder())
                .should()
                .beStatic();

        rule.check(this.getClasses());
    }

    private DescribedPredicate<JavaClass> isNotTestCase() {
        return new DescribedPredicate<>("is not a test case") {
            @Override
            public boolean apply(JavaClass javaClass) {
                return !javaClass.getName().endsWith(TESTCASE_SUFFIX);
            }
        };
    }

    /**
     * Lambda are compiled as hidden static methods named lambda$XXX. This method will help detect them and ignore them.
     *
     * @return A predicate which will help us ignore lambda methods
     */
    private DescribedPredicate<JavaMethod> isNotLambda() {
        return new DescribedPredicate<>("is not a lambda") {
            @Override
            public boolean apply(JavaMethod javaMethod) {
                return !javaMethod.getName().startsWith("lambda$") && !javaMethod.getName().startsWith("$deserializeLambda$");
            }
        };
    }

    /**
     * Some switch can be compiled as hidden static methods named $SWITCH_TABLE$. This predicate will help detect them
     * and ignore them.
     *
     * @return A predicate which help us ignore switch expressions
     */
    private DescribedPredicate<JavaMethod> isNotSwitchTable() {
        return new DescribedPredicate<>("is not a switch table (whatever that is...)") {
            @Override
            public boolean apply(JavaMethod javaMethod) {
                return !javaMethod.getFullName().contains("$SWITCH_TABLE$");
            }
        };
    }

    /**
     * Used to detect that the static method is not defined in a record and does not return the same record.
     *
     * @return A predicate used to ignore some static methods
     */
    private DescribedPredicate<JavaMethod> isNotRecordStaticBuilder() {
        return new DescribedPredicate<>("is not an alternate builder in a record") {
            @Override
            public boolean apply(JavaMethod javaMethod) {
                return !(javaMethod.getOwner().isRecord() && javaMethod.getRawReturnType().equals(javaMethod.getOwner()));
            }
        };
    }
}
