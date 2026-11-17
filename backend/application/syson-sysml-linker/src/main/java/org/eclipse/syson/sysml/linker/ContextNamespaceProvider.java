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
package org.eclipse.syson.sysml.linker;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.omg.sysml.lang.sysml.AllocationUsage;
import org.omg.sysml.lang.sysml.AssignmentActionUsage;
import org.omg.sysml.lang.sysml.Conjugation;
import org.omg.sysml.lang.sysml.Connector;
import org.omg.sysml.lang.sysml.ConstructorExpression;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.EndFeatureMembership;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureChaining;
import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.FeatureReferenceExpression;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.FeatureValue;
import org.omg.sysml.lang.sysml.FlowEnd;
import org.omg.sysml.lang.sysml.Function;
import org.omg.sysml.lang.sysml.IndexExpression;
import org.omg.sysml.lang.sysml.InstantiationExpression;
import org.omg.sysml.lang.sysml.InvocationExpression;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.PerformActionUsage;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.RequirementUsage;
import org.omg.sysml.lang.sysml.ReturnParameterMembership;
import org.omg.sysml.lang.sysml.SendActionUsage;
import org.omg.sysml.lang.sysml.Specialization;
import org.omg.sysml.lang.sysml.Subclassification;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.Succession;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TransitionUsage;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.lang.sysml.VerificationCaseUsage;
import org.omg.sysml.util.SysMLLibraryUtil;

/**
 * Provides the {@link Namespace} from which the linker starts resolving a reference.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ContextNamespaceProvider {

    /**
     * The {@link SysMLLinker}.
     */
    private final SysMLLinker linker;

    private final SysMLProxySafeNavigator sysMLEMFNavigator = new SysMLProxySafeNavigator();

    private final SysMLMetamodelHelper sysMLMetamodelHelper = new SysMLMetamodelHelper();

    /**
     * Constructor.
     *
     * @param linker
     *            the {@link SysMLLinker}
     */
    public ContextNamespaceProvider(SysMLLinker linker) {
        this.linker = linker;
    }

    /**
     * Gets the context {@link Namespace} for the given {@link EObject}.
     *
     * @param eObj
     *            the {@link EObject}
     * @param reference
     *            The resolved {@link EReference}
     * @return the context {@link Namespace} for the given {@link Element} if any, <code>null</code> otherwise
     */
    public Namespace getContextNamespace(EObject eObj, EReference reference) {
        Namespace res;

        if (reference == SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature()) {
            res = this.getFeatureChainingContextNamespace((FeatureChaining) eObj);
        } else if (reference == SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature()) {
            res = this.getRedefinitionContextNamespace((Redefinition) eObj);
        } else if (reference == SysMLPackage.eINSTANCE.getSubsetting_SubsettedFeature()
                || reference == SysMLPackage.eINSTANCE.getSubclassification_Superclassifier()) {
            res = this.getSpecializationContextNamespace((Specialization) eObj);
        } else if (reference == SysMLPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature()) {
            res = this.getReferenceSubsettingContext((ReferenceSubsetting) eObj);
        } else if (reference == SysMLPackage.eINSTANCE.getMembership_MemberElement()) {
            res = this.getContextNamespaceForMembership((Membership) eObj);
        } else {
            // Fallback to containing Namespace
            res = this.sysMLEMFNavigator.getContaining(Namespace.class, eObj);
        }

        return res;
    }

    /**
     * Gets the context used to resolve the general type referenced by a {@link Specialization}.
     * <p>
     * KerML 8.2.3.5.2 specifies that, for a {@link Specialization} with a non-null owning type, the local namespace is
     * the owning namespace of that type. Therefore, name resolution must start from the namespace that owns the
     * specializing type, not from the type itself. This method currently applies that rule to the {@link Subsetting}
     * and {@link Subclassification} references handled above. Resolving a Subclassification from this context is also
     * required for the special Redefinition lookup in KerML 8.2.3.5.1, which searches the general Types of the
     * redefining Feature's owning Type.
     * </p>
     *
     * @param specialization
     *            the {@link Specialization}
     * @return the local {@link Namespace} specified by KerML 8.2.3.5.2
     */
    private Namespace getSpecializationContextNamespace(Specialization specialization) {
        return this.getOwningTypeContext(specialization);
    }

    /**
     * Gets the scope of an end reference. KerML 8.2.3.5.2 specifies the connector's owning namespace when a
     * ReferenceSubsetting belongs to an end feature of a Connector. In particular, a typed connector's inherited end
     * names must not shadow the connected features in that outer namespace.
     *
     * @param subsetting
     *            the reference subsetting being linked
     * @return the connector's owning namespace, or the existing feature context for other reference subsettings
     */
    private Namespace getReferenceSubsettingContext(ReferenceSubsetting subsetting) {
        Namespace feature = this.sysMLEMFNavigator.getContaining(Namespace.class, subsetting.eContainer());
        Namespace result = this.getConnectorEndContext(feature);
        if (result == null) {
            result = feature;
        }
        return result;
    }

    /**
     * Gets the outer scope specific to a connector end, including a generated transition succession.
     *
     * @param namespace
     *            the possible connector end
     * @return its reference context, or {@code null} if the namespace is not a connector end
     */
    private Namespace getConnectorEndContext(Namespace namespace) {
        Namespace result = null;
        if (namespace instanceof Feature feature && feature.isEnd()) {
            Namespace owner = this.sysMLEMFNavigator.getContaining(Namespace.class, feature.eContainer());
            if (owner instanceof Connector) {
                result = this.sysMLEMFNavigator.getContaining(Namespace.class, owner.eContainer());
                if (result instanceof TransitionUsage && this.isTransitionTarget(owner, feature)) {
                    result = this.sysMLEMFNavigator.getContaining(Namespace.class, result.eContainer());
                }
            }
        }
        return result;
    }

    /**
     * Identifies the target end of the succession owned by a transition. SysML 8.2.2.18.3 represents a transition's
     * target with the second end of a generated TransitionSuccession. Its reference is scoped outside that generated
     * succession and the transition, as in the pilot's SysMLScopeProvider, so StateAction::done remains visible.
     *
     * @param connector
     *            the possible transition succession
     * @param feature
     *            the end being linked
     * @return whether the end is the generated transition target
     */
    private boolean isTransitionTarget(Namespace connector, Feature feature) {
        return connector instanceof Succession && !(connector.eContainer() instanceof FeatureMembership)
                && this.getOwnedEnds(connector).indexOf(feature.eContainer()) == 1;
    }

    /**
     * Gets the lexical scope of a membership target, including a transition source's owning state or action.
     *
     * @param membership the membership being resolved
     * @return its resolution context
     */
    private Namespace getContextNamespaceForMembership(Membership membership) {
        Namespace res = this.sysMLEMFNavigator.getContaining(Namespace.class, membership.eContainer());

        if (res instanceof TransitionUsage transition && !(membership instanceof FeatureMembership)) {
            res = this.sysMLEMFNavigator.getContaining(Namespace.class, transition.eContainer());
        } else if (res instanceof FeatureChainExpression featureChainExpression) {
            // KerML 8.2.3.5.2: use the argument expression's result, not a reference inside its arguments.
            res = this.getArgumentResult(featureChainExpression);
        } else if (res instanceof FeatureReferenceExpression
                || (res instanceof InstantiationExpression && !(membership instanceof FeatureMembership))) {
            res = this.getNonInvocationNamespace(res);
            if (res instanceof PerformActionUsage) {
                res = res.getOwningNamespace();
            }
        } else if (res instanceof AssignmentActionUsage) {
            ParameterMembership targetParameter = this.getPreviousParameterMembership(membership);
            Namespace expressionContext = this.getContainedFeatureReferenceExpressionContext(targetParameter);
            if (expressionContext != null) {
                res = expressionContext;
            }
        }

        return res;
    }

    /**
     * Gets the result of the first argument expression (KerML 8.2.3.5.2), following its semantic context when the
     * parsed expression has no materialized result parameter.
     *
     * @param expression
     *            the expression whose source argument is inspected
     * @return its argument's result parameter or semantic context, or {@code null} if unavailable
     */
    private Namespace getArgumentResult(Expression expression) {
        for (var relationship : expression.getOwnedRelationship()) {
            if (relationship instanceof ParameterMembership parameter && !(parameter instanceof ReturnParameterMembership)) {
                Expression value = this.getValueExpression(parameter.getOwnedMemberParameter());
                if (value != null) {
                    // Link the result's semantic prerequisites before entering its namespace. An untyped
                    // result could otherwise fall back to the containing chain and match that chain itself.
                    Namespace context = this.getExpressionValueContext(value);
                    Feature result = this.getOwnedResult(value);
                    if (result != null && (context != null || result.getOwnedRelationship().stream().anyMatch(Specialization.class::isInstance))) {
                        context = result;
                    }
                    return context;
                }
            }
        }
        // Preserve the direct typing context of incomplete models that have no argument expression.
        return expression.getOwnedRelationship().stream()
                .filter(FeatureTyping.class::isInstance)
                .flatMap(typing -> this.linker.get(typing, SysMLPackage.eINSTANCE.getFeatureTyping_Type()).stream())
                .filter(Namespace.class::isInstance).map(Namespace.class::cast).findFirst().orElse(null);
    }

    /**
     * Gets a contained result parameter without requesting derived expression types.
     *
     * @param namespace
     *            the expression or function owning the result
     * @return the owned result parameter, or {@code null}
     */
    private Feature getOwnedResult(Namespace namespace) {
        return namespace.getOwnedRelationship().stream()
                .filter(ReturnParameterMembership.class::isInstance)
                .map(ReturnParameterMembership.class::cast)
                .map(ReturnParameterMembership::getOwnedMemberParameter)
                .findFirst().orElse(null);
    }

    /**
     * Gets the namespace supplying members through an expression result or a feature value. Expression-result
     * specialization (KerML 8.3.4.8.3 through 8.3.4.8.5 and 8.4.4.9.5) is followed first. If it supplies no context,
     * the implied valuation specialization is followed (KerML 8.3.3.3.4, {@code checkFeatureValuationSpecialization}).
     *
     * @param feature
     *            the feature whose result context is sought
     * @return the result context, or {@code null} when neither rule supplies one
     */
    public Namespace getResultContext(Feature feature) {
        Namespace resultContext = this.getExpressionResultContext(feature);
        if (resultContext == null) {
            resultContext = this.getFeatureValueContext(feature);
        }
        return resultContext;
    }

    /**
     * Follows the specified specialization of an expression result without materializing implied relationships. KerML
     * 8.3.4.8.3, 8.3.4.8.4 and 8.3.4.8.5 make constructor, chain and reference results specialize their instantiated
     * type, terminal feature and referent, respectively. For function invocations, KerML 8.4.4.9.5 makes the invocation
     * result redefine the function result.
     *
     * @param feature
     *            a possible expression result parameter
     * @return the namespace supplying its members, or {@code null} when no supported result context is available
     */
    private Namespace getExpressionResultContext(Feature feature) {
        Namespace result = null;
        if (feature.eContainer() instanceof ReturnParameterMembership membership && membership.eContainer() instanceof Expression expression) {
            result = this.getExpressionValueContext(expression);
        }
        return result;
    }

    /**
     * Gets the namespace supplying an expression result's members, including when the result is not materialized.
     *
     * @param expression
     *            the expression whose result is followed
     * @return its result's semantic context, or {@code null}
     */
    private Namespace getExpressionValueContext(Expression expression) {
        Namespace result = null;
        if (expression instanceof FeatureChainExpression) {
            for (var relationship : expression.getOwnedRelationship()) {
                if (relationship instanceof OwningMembership owning && !(owning instanceof FeatureMembership)
                        && owning.getOwnedMemberElement() instanceof Feature chain) {
                    result = this.getReferencedFeatureContext(chain);
                    break;
                }
            }
            if (result == null) {
                result = this.getReferencedMember(expression);
            }
        } else if (expression instanceof FeatureReferenceExpression || expression instanceof ConstructorExpression) {
            result = this.getReferencedMember(expression);
        } else if (expression instanceof IndexExpression indexExpression) {
            result = this.getIndexResultContext(indexExpression);
        } else if (expression instanceof InvocationExpression) {
            Namespace instantiated = this.getReferencedMember(expression);
            if (instantiated instanceof Function || instantiated instanceof Expression) {
                result = this.getOwnedResult(instantiated);
            } else {
                result = instantiated;
            }
        }
        return result;
    }

    /**
     * Follows KerML 8.3.4.8.6, {@code checkIndexExpressionResultSpecialization}: an index result specializes the
     * first argument's result, except when that result specializes {@code Collections::Array}. Resolve the argument
     * through the linker so parsed result parameters need not already have their implied specializations.
     *
     * @param expression
     *            the index expression
     * @return its sequence argument's result context, or {@code null} for arrays or an unresolved argument
     */
    private Namespace getIndexResultContext(IndexExpression expression) {
        Namespace result = this.getArgumentResult(expression);
        Type arrayType = SysMLLibraryUtil.getLibraryType(expression, "Collections::Array");
        if (arrayType != null && this.specializesArray(result, arrayType, new HashSet<>())) {
            result = null;
        }
        return result;
    }

    /**
     * Checks the array exception using linked specialization targets and semantic result contexts. Reading derived
     * supertypes here could use a cache populated before the argument's typing was linked.
     *
     * @param context
     *            the argument result or one of its general types
     * @param arrayType
     *            the library Array type
     * @param visited
     *            contexts already checked, to break cycles
     * @return whether the argument result specializes Array
     */
    private boolean specializesArray(Namespace context, Type arrayType, Set<Namespace> visited) {
        if (context == null || !visited.add(context)) {
            return false;
        }
        boolean result = context == arrayType;
        if (!result && context instanceof Feature feature) {
            result = this.specializesArray(this.getResultContext(feature), arrayType, visited);
        }
        if (!result) {
            for (var relationship : List.copyOf(context.getOwnedRelationship())) {
                if (relationship instanceof Specialization specialization) {
                    EReference reference = this.sysMLMetamodelHelper.getSpecializationFeature(specialization);
                    if (reference != null) {
                        for (Element general : this.linker.get(specialization, reference)) {
                            if (general instanceof Namespace namespace) {
                                result |= this.specializesArray(namespace, arrayType, visited);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Follows the implied valuation specialization. KerML 8.3.3.3.4, {@code checkFeatureValuationSpecialization},
     * requires an undirected feature with no non-implied owned specialization to specialize its value expression's
     * result.
     *
     * @param feature
     *            the feature whose value is inspected
     * @return the value result's context, or {@code null} when no valuation specialization applies
     */
    private Namespace getFeatureValueContext(Feature feature) {
        Namespace result = null;
        boolean hasExplicitSpecialization = feature.getOwnedRelationship().stream()
                .anyMatch(relationship -> relationship instanceof Specialization specialization && !specialization.isImplied());
        if (feature.getDirection() == null && !hasExplicitSpecialization) {
            Expression value = this.getValueExpression(feature);
            if (value != null) {
                result = this.getExpressionValueContext(value);
            }
        }
        return result;
    }

    /**
     * Gets the first non-null expression supplied by an owned feature value.
     *
     * @param feature
     *            the feature to inspect, possibly {@code null} in incomplete models
     * @return the value expression, or {@code null}
     */
    private Expression getValueExpression(Feature feature) {
        Expression result = null;
        if (feature != null) {
            result = feature.getOwnedRelationship().stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .map(FeatureValue::getValue)
                    .filter(Objects::nonNull)
                    .findFirst().orElse(null);
        }
        return result;
    }

    /**
     * Resolves the parser's non-owning membership for an expression referent or instantiated type.
     *
     * @param expression
     *            the expression containing the reference
     * @return the referenced namespace, or {@code null} while its link remains unresolved
     */
    private Namespace getReferencedMember(Expression expression) {
        // Resolving a membership can append implied relationships to the expression.
        return List.copyOf(expression.getOwnedRelationship()).stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
                .filter(membership -> !(membership instanceof OwningMembership))
                .map(this.linker::getMemberElement)
                .filter(Namespace.class::isInstance)
                .map(Namespace.class::cast)
                .findFirst().orElse(null);
    }

    /**
     * Gets the expression-reference scope. Ordinary invocation scopes follow KerML 8.2.3.5.2.
     * For a linked constructor, preserve the pilot implementation's result-relative lookup: its argument
     * features implicitly redefine public features of the instantiated type (KerML 8.3.4.8.3 and 8.4.4.9.4).
     * This constructor exception preserves pilot interchange targets; incomplete constructors retain the
     * outer non-invocation scope. Direct send arguments also retain their enclosing evaluation scope.
     * <p>
     * The enclosing-scope walk uses EMF containment; a constructor's instantiated type is resolved through the linker.
     * Derived owning namespaces and memberships are avoided because they can depend on unresolved references.
     * </p>
     *
     * @param invocationNamespace
     *            the invocation namespace to leave
     * @return the nearest containing namespace that is not part of the invocation, or <code>null</code>
     */
    private Namespace getNonInvocationNamespace(Namespace invocationNamespace) {
        EObject current = invocationNamespace.eContainer();
        if (current instanceof FeatureValue && current.eContainer() instanceof Namespace parameter && this.isSendArgument(parameter)) {
            current = parameter.eContainer();
        }
        Namespace result = null;
        while (current != null && result == null) {
            if (current instanceof Feature constructorResult
                    && constructorResult.eContainer() instanceof ReturnParameterMembership resultMembership
                    && resultMembership.eContainer() instanceof ConstructorExpression constructor
                    && this.hasConstructorResultScope(constructor)) {
                result = constructorResult;
            } else if (current instanceof Namespace namespace && !this.isInvocationNamespace(namespace)) {
                result = namespace;
            } else {
                current = current.eContainer();
            }
        }
        return result;
    }

    /**
     * Tests whether a linked constructor provides the pilot-compatible result scope. Constructors used as direct
     * send arguments keep the send-parameter scope: SysML 8.2.2.17.4 represents these as parameter FeatureValues,
     * and KerML 8.2.3.5.2 skips their nested constructor features. Otherwise an input such as
     * {@code send new Payload(content)} could bind to its own generated argument instead of the enclosing content.
     *
     * @param constructor
     *            the constructor whose argument is being linked
     * @return whether to retain the constructor result as the lookup scope
     */
    private boolean hasConstructorResultScope(ConstructorExpression constructor) {
        boolean sendArgument = constructor.eContainer() instanceof FeatureValue value
                && value.eContainer() instanceof Namespace parameter && this.isSendArgument(parameter);
        return !sendArgument && this.getReferencedMember(constructor) != null;
    }

    private boolean isInvocationNamespace(Namespace namespace) {
        return namespace instanceof FeatureReferenceExpression
                || namespace instanceof InstantiationExpression
                || this.isFeatureOwnedByInstantiationExpression(namespace)
                || this.isFeatureOwnedByConstructorResult(namespace);
    }

    /**
     * Identifies the generated parameter scope of a direct send-action argument. SysML 8.2.2.17.4 maps the payload,
     * sender and receiver expressions to parameter FeatureValues. As in the pilot's non-expression namespace traversal,
     * lookup leaves these generated scopes and starts in the send action. KerML 8.3.4.7.3,
     * {@code checkExpressionTypeFeaturing}, gives a value expression the featuring types of its featureWithValue; for
     * these parameters the featuring type is the send action.
     *
     * @param namespace
     *            the possible generated argument parameter
     * @return whether lookup should leave this parameter scope
     */
    private boolean isSendArgument(Namespace namespace) {
        return namespace instanceof Feature && namespace.eContainer() instanceof ParameterMembership membership
                && membership.eContainer() instanceof SendActionUsage;
    }

    /**
     * Tells if the given {@link Namespace} is a feature owned by an instantiation expression.
     *
     * @return <code>true</code> if the given {@link Namespace} is a feature owned by an instantiation expression,
     *         <code>false</code> otherwise
     */
    private boolean isFeatureOwnedByInstantiationExpression(Namespace namespace) {
        return namespace instanceof Feature feature
                && feature.eContainer() instanceof FeatureMembership featureMembership
                && featureMembership.eContainer() instanceof InstantiationExpression;
    }

    /**
     * Tells if the given {@link Namespace} is a constructor result feature.
     *
     * @param namespace
     *            the {@link Namespace}
     * @return <code>true</code> if the given {@link Namespace} is a constructor result feature, <code>false</code>
     *         otherwise
     */
    private boolean isFeatureOwnedByConstructorResult(Namespace namespace) {
        boolean result = false;
        if (namespace instanceof Feature feature
                && feature.eContainer() instanceof FeatureMembership featureMembership) {
            EObject featureOwner = featureMembership.eContainer();
            if (featureOwner instanceof Feature constructorResult
                    && constructorResult.eContainer() instanceof ReturnParameterMembership returnMembership
                    && returnMembership.eContainer() instanceof ConstructorExpression) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Gets the context {@link Namespace} for the given {@link Redefinition}.
     *
     * @param redefinition
     *            the {@link Redefinition}
     * @return the context namespace, or null when the redefinition has no containing type
     */
    private Namespace getRedefinitionContextNamespace(Redefinition redefinition) {
        Namespace result;

        Type owningType = this.sysMLEMFNavigator.getRedefinitionOwningType(redefinition);
        if (owningType == null) {
            result = null;
        } else if (owningType instanceof FlowEnd flowEnd) {
            result = this.getFlowEndRedefinitionContext(flowEnd);
        } else if (owningType instanceof RequirementUsage && owningType.getOwningNamespace() instanceof VerificationCaseUsage verificationCaseUsage) {
            result = verificationCaseUsage;
        } else if (owningType.getOwningMembership() != null
                && owningType.getOwningMembership().getOwningRelatedElement() instanceof ConstructorExpression constructorExpression) {
            result = this.getConstructorExpressionRedefinitionContext(constructorExpression);
        } else {
            result = owningType;
        }

        return result;
    }

    /**
     * Gets the context {@link Namespace} for a {@link Redefinition} inside a {@link ConstructorExpression}.
     *
     * @param constructorExpression
     *            the {@link ConstructorExpression}
     * @return the context {@link Namespace} for a {@link Redefinition} inside a {@link ConstructorExpression} if any,
     *         <code>null</code> otherwise
     */
    private Namespace getConstructorExpressionRedefinitionContext(ConstructorExpression constructorExpression) {
        Namespace result;
        if (!constructorExpression.getOwnedRelationship().isEmpty() && constructorExpression.getOwnedRelationship().get(0) instanceof Membership membership) {
            result = this.getMemberNamespace(membership);
        } else {
            result = null;
        }
        return result;
    }

    /**
     * Gets the {@link FlowEnd} redefinition context {@link Type}.
     *
     * @param flowEnd
     *            the {@link Float}
     * @return the {@link FlowEnd} redefinition context {@link Type}
     */
    private Type getFlowEndRedefinitionContext(FlowEnd flowEnd) {
        Type result = flowEnd;
        if (!flowEnd.getOwnedSpecialization().isEmpty()) {
            Specialization specialization = flowEnd.getOwnedSpecialization().get(0);
            EReference specializationFeature = this.sysMLMetamodelHelper.getSpecializationFeature(specialization);
            Set<Element> types = this.linker.get(specialization, specializationFeature);
            if (!types.isEmpty()) {
                Type referencedType = (Type) types.iterator().next();
                if (referencedType instanceof Feature feature) {
                    result = this.getReferencedFeatureContext(feature);
                } else {
                    result = referencedType;
                }
            }
        }
        return result;
    }

    /**
     * Gets the referenced {@link Feature} from the given {@link Feature}.
     *
     * @param feature
     *            the current {@link Feature}
     * @return the referenced {@link Feature} from the given {@link Feature}
     */
    public Feature getReferencedFeatureContext(Feature feature) {
        Feature result = feature;
        if (!feature.getOwnedFeatureChaining().isEmpty()) {
            List<FeatureChaining> featureChainings = feature.getOwnedFeatureChaining();
            FeatureChaining lastChainingFeature = featureChainings.get(featureChainings.size() - 1);
            Set<Element> features = this.linker.get(lastChainingFeature, SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature());
            result = features.stream()
                    .filter(Feature.class::isInstance)
                    .map(Feature.class::cast)
                    .findFirst()
                    .orElse(null);
        }
        return result;
    }

    /**
     * Gets the context {@link Namespace} for the given {@link FeatureChaining}.
     *
     * @param featureChaining
     *            the {@link FeatureChaining}
     * @return the context {@link Namespace} for the given {@link FeatureChaining} if any, <code>null</code> otherwise
     */
    private Namespace getFeatureChainingContextNamespace(FeatureChaining featureChaining) {
        Namespace res;
        FeatureChaining previousChaining = this.getPreviousFeatureChaining(featureChaining);
        if (previousChaining != null) {
            Set<Element> types = this.linker.get(previousChaining, SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature());
            res = types.stream()
                    .filter(Namespace.class::isInstance)
                    .map(Namespace.class::cast)
                    .findFirst().orElse(null);
        } else {
            res = this.getFirstFeatureChainingContext(featureChaining);
        }
        return res;
    }

    /**
     * Gets the context of the first {@link FeatureChaining} by applying the context rule of the relationship that owns
     * the chained feature.
     * <p>
     * KerML 8.2.3.5.2 requires the owning relationship to be treated as the context relationship. The relationship is
     * obtained from containment only because {@link Feature#getOwningRelationship()} is derived and may depend on model
     * completion. The three normative relationship kinds are handled explicitly. A containment fallback is kept for
     * incomplete parser models whose owner is not one of those kinds.
     * </p>
     *
     * @param featureChaining
     *            the first feature chaining
     * @return the context namespace, or <code>null</code> when a required context is not linked yet
     */
    private Namespace getFirstFeatureChainingContext(FeatureChaining featureChaining) {
        Namespace result;
        Feature featureChained = this.sysMLEMFNavigator.getContaining(Feature.class, featureChaining.eContainer());
        EObject owningRelationship = null;
        if (featureChained != null) {
            owningRelationship = featureChained.eContainer();
        }
        if (owningRelationship instanceof OwningMembership owningMembership && owningMembership.eContainer() instanceof AssignmentActionUsage) {
            ParameterMembership targetParameter = this.getPreviousParameterMembership(owningMembership);
            result = this.getContainedFeatureReferenceExpressionContext(targetParameter);
        } else if (owningRelationship instanceof Membership membership) {
            result = this.getContextNamespaceForMembership(membership);
        } else if (owningRelationship instanceof Subsetting subsetting) {
            result = this.getFirstFeatureChainingSpecializationContext(subsetting);
        } else if (owningRelationship instanceof Conjugation conjugation) {
            result = this.getConjugationContextNamespace(conjugation);
        } else {
            result = this.sysMLEMFNavigator.getContaining(Namespace.class, featureChaining);
        }
        return result;
    }

    /**
     * Gets the context of the first feature chaining owned by a {@link Subsetting}.
     * <p>
     * The parser represents each qualified allocation end with a {@link ReferenceSubsetting} whose target has already
     * been linked, and whose owned feature contains the chain still being linked. For this form, the namespace owning
     * the corresponding target provides the context of the first chain segment. This is deliberately selected by
     * semantic types rather than by a fixed containment depth, and the target is always read through
     * {@link SysMLLinker#get(EObject, EReference)}.
     * </p>
     *
     * @param subsetting
     *            the subsetting that owns the chained feature
     * @return the context namespace
     */
    private Namespace getFirstFeatureChainingSpecializationContext(Subsetting subsetting) {
        Namespace result = null;
        if (subsetting instanceof ReferenceSubsetting referenceSubsetting) {
            result = this.getAllocationEndFeatureChainingContext(referenceSubsetting);
            if (result == null) {
                Namespace feature = this.sysMLEMFNavigator.getContaining(Namespace.class, subsetting.eContainer());
                result = this.getConnectorEndContext(feature);
            }
        }
        if (result == null) {
            result = this.getSpecializationContextNamespace(subsetting);
        }
        return result;
    }

    /**
     * Gets the parser-specific first-chain context of an allocation end.
     *
     * @param referenceSubsetting
     *            the reference subsetting that owns the chained feature
     * @return the namespace owning the resolved target of the corresponding allocation end, or <code>null</code> when
     *         the parser-specific form is not present or its prerequisite is not resolved yet
     */
    private Namespace getAllocationEndFeatureChainingContext(ReferenceSubsetting referenceSubsetting) {
        Namespace result = null;
        Feature referencingFeature = this.sysMLEMFNavigator.getContaining(Feature.class, referenceSubsetting.eContainer());
        if (referencingFeature != null && referencingFeature.eContainer() instanceof EndFeatureMembership endMembership
                && endMembership.eContainer() instanceof AllocationUsage allocation) {
            List<EndFeatureMembership> allocationEnds = this.getOwnedEnds(allocation);
            int endIndex = allocationEnds.indexOf(endMembership);
            AllocationUsage containingAllocation = this.sysMLEMFNavigator.getContaining(AllocationUsage.class, allocation.eContainer());
            List<EndFeatureMembership> correspondingEnds = allocationEnds;
            if (containingAllocation != null) {
                correspondingEnds = this.getOwnedEnds(containingAllocation);
            }
            if (endIndex >= 0 && endIndex < correspondingEnds.size()) {
                Specialization correspondingSpecialization = this.sysMLEMFNavigator.getContained(Specialization.class, correspondingEnds.get(endIndex));
                if (correspondingSpecialization != null) {
                    EReference specializationFeature = this.sysMLMetamodelHelper.getSpecializationFeature(correspondingSpecialization);
                    result = this.linker.get(correspondingSpecialization, specializationFeature).stream()
                            .filter(Namespace.class::isInstance)
                            .map(Namespace.class::cast)
                            .map(namespace -> this.sysMLEMFNavigator.getContaining(Namespace.class, namespace.eContainer()))
                            .findFirst()
                            .orElse(null);
                }
            }
        }
        return result;
    }

    /**
     * Gets a namespace's owned end memberships in declaration order.
     *
     * @param namespace
     *            the namespace to inspect
     * @return the owned end memberships
     */
    private List<EndFeatureMembership> getOwnedEnds(Namespace namespace) {
        return namespace.getOwnedRelationship().stream()
                .filter(EndFeatureMembership.class::isInstance)
                .map(EndFeatureMembership.class::cast)
                .toList();
    }

    /**
     * Gets the context namespace of a {@link Conjugation} without navigating its derived owning type.
     *
     * @param conjugation
     *            the conjugation
     * @return the owning namespace of its owning type, or the owning namespace of the conjugation
     */
    private Namespace getConjugationContextNamespace(Conjugation conjugation) {
        return this.getOwningTypeContext(conjugation);
    }

    /**
     * Gets the namespace outside a relationship's containing type, using containment only.
     *
     * @param relationship
     *            the relationship whose context is sought
     * @return the owning type's containing namespace, or the relationship's containing namespace if no type exists
     */
    private Namespace getOwningTypeContext(EObject relationship) {
        EObject container = relationship.eContainer();
        Type owningType = this.sysMLEMFNavigator.getContaining(Type.class, container);
        if (owningType != null) {
            container = owningType.eContainer();
        }
        return this.sysMLEMFNavigator.getContaining(Namespace.class, container);
    }

    /**
     * Gets the previous {@link ParameterMembership} from the given {@link Membership}.
     *
     * @param membership
     *            the {@link Membership}
     * @return the previous {@link ParameterMembership} from the given {@link Membership}
     */
    private ParameterMembership getPreviousParameterMembership(Membership membership) {
        ParameterMembership res = null;

        if (membership.eContainer() instanceof Element owningRelatedElement) {
            int index = owningRelatedElement.getOwnedRelationship().indexOf(membership);
            for (int i = index - 1; i > -1; i--) {
                if (owningRelatedElement.getOwnedRelationship().get(i) instanceof ParameterMembership) {
                    res = (ParameterMembership) owningRelatedElement.getOwnedRelationship().get(i);
                    break;
                }
            }
        }

        return res;
    }

    /**
     * Gets the context {@link Namespace} of the given {@link EObject} that contains a
     * {@link FeatureReferenceExpression}.
     *
     * @param eObj
     *            the {@link EObject}
     * @return the context {@link Namespace} of the given {@link EObject} that contains a
     *         {@link FeatureReferenceExpression} if any, <code>null</code> otherwise
     */
    private Namespace getContainedFeatureReferenceExpressionContext(EObject eObj) {
        Namespace result = null;
        if (eObj != null) {
            FeatureReferenceExpression expression = this.sysMLEMFNavigator.getContained(FeatureReferenceExpression.class, eObj);
            if (expression != null) {
                result = expression.getOwnedRelationship().stream()
                        .filter(Membership.class::isInstance)
                        .map(Membership.class::cast)
                        .findFirst()
                        .map(this::getMemberNamespace)
                        .orElse(null);
            }
        }
        return result;
    }

    /**
     * Resolves a membership directly to its first namespace target, without following nested memberships.
     *
     * @param membership
     *            the selected membership
     * @return the referenced namespace, or {@code null}
     */
    private Namespace getMemberNamespace(Membership membership) {
        return this.linker.get(membership, SysMLPackage.eINSTANCE.getMembership_MemberElement()).stream()
                .filter(Namespace.class::isInstance)
                .map(Namespace.class::cast)
                .findFirst().orElse(null);
    }

    /**
     * Gets the {@link FeatureChaining} before the given {@link FeatureChaining}.
     *
     * @param featureChaining
     *            the {@link FeatureChaining}
     * @return the {@link FeatureChaining} before the given {@link FeatureChaining} if any, <code>null</code> otherwise
     */
    private FeatureChaining getPreviousFeatureChaining(FeatureChaining featureChaining) {
        FeatureChaining res;

        EObject container = featureChaining.eContainer();
        if (container != null) {
            // Both possible containment references, Element::ownedRelationship and
            // Relationship::ownedRelatedElement, are multi-valued in the SysML metamodel.
            Collection<?> values = (Collection<?>) container.eGet(featureChaining.eContainingFeature());
            List<FeatureChaining> featureChainings = values.stream()
                    .filter(FeatureChaining.class::isInstance)
                    .map(FeatureChaining.class::cast).collect(Collectors.toList());
            int index = featureChainings.indexOf(featureChaining);
            if (index > 0) {
                res = featureChainings.get(index - 1);
            } else {
                res = null;
            }
        } else {
            res = null;
        }

        return res;
    }

}
