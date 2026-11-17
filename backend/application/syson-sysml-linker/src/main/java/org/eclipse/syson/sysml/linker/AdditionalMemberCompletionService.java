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

import java.util.Objects;

import org.eclipse.syson.sysml.linker.additionalmembers.ExpressionResultParameterCompleter;
import org.eclipse.syson.sysml.linker.additionalmembers.FeatureChainSourceTargetCompleter;
import org.eclipse.syson.sysml.linker.additionalmembers.TransitionLinkCompleter;
import org.eclipse.syson.sysml.linker.additionalmembers.TransitionSourceCompleter;
import org.eclipse.syson.sysml.linker.additionalmembers.UsageMultiplicityCompleter;
import org.omg.sysml.lang.sysml.AttributeUsage;
import org.omg.sysml.lang.sysml.ConnectionUsage;
import org.omg.sysml.lang.sysml.ConstructorExpression;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureReferenceExpression;
import org.omg.sysml.lang.sysml.InvocationExpression;
import org.omg.sysml.lang.sysml.ItemUsage;
import org.omg.sysml.lang.sysml.PortUsage;
import org.omg.sysml.lang.sysml.TransitionUsage;
import org.omg.sysml.lang.sysml.Usage;
import org.omg.sysml.lang.sysml.util.SysMLSwitch;

/**
 * Adds implicit members inferred from the structure of a SysML model.
 * <p>
 * The service processes one element and deliberately does not own a traversal strategy. {@link #complete(Element)} completes every additional-member category by default and must then be called after
 * reference resolution. Callers that need to split the lifecycle select the completion type when constructing the service.
 * </p>
 * NOTE: Maybe not part the final implementation. Current discussion on this topic in https://openmbee.atlassian.net/browse/ST6RI-955
 * @author Arthur Daussy
 */
public class AdditionalMemberCompletionService extends SysMLSwitch<Boolean> {

    /**
     * Completion type.
     *
     * @author Arthur Daussy
     */
    public enum CompletionType {
        /**
         * Completes only members whose computation depends on containment and local properties.
         */
        PRE_LINK_ONLY,
        /**
         * Completes only members whose computation requires resolved references.
         */
        POST_LINK_ONLY,
        /**
         * Completes both groups in dependency order. This mode must be used after reference resolution.
         */
        ALL
    }

    private static final Boolean STOP_SUPERTYPE_PROCESSING = Boolean.TRUE;

    private final CompletionType completionType;

    /**
     * Creates a service that completes every additional-member category.
     * <p>
     * This constructor selects {@link CompletionType#ALL}; the service must therefore be used after reference resolution.
     * </p>
     */
    public AdditionalMemberCompletionService() {
        this(CompletionType.ALL);
    }

    /**
     * Creates a service restricted to the given lifecycle category. {@link CompletionType#PRE_LINK_ONLY} can be used once containment is stable. {@link CompletionType#POST_LINK_ONLY} assumes that
     * pre-link completion has already run. {@link CompletionType#ALL} runs both groups in dependency order and must be used after reference resolution.
     *
     * @param completionType
     *         the completion category used for every call to {@link #complete(Element)}
     */
    public AdditionalMemberCompletionService(CompletionType completionType) {
        this.completionType = Objects.requireNonNull(completionType);
    }

    /**
     * Completes the additional members selected by this service's {@link CompletionType}. A service created with the no-argument constructor uses {@link CompletionType#ALL} and must be called after
     * reference resolution.
     *
     * @param element
     *         the element to complete
     */
    public void complete(Element element) {
        if (element != null) {
            this.doSwitch(element);
        }
    }

    @Override
    public Boolean caseUsage(Usage object) {
        // The generic Usage case applies the end-only rule and stops supertype
        // processing because no broader additional-member rule is applicable.
        if (this.completesPreLinkMembers()) {
            UsageMultiplicityCompleter.completeEndMultiplicity(object);
        }
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseAttributeUsage(AttributeUsage object) {
        // Attribute usages use the default multiplicity rule. Stop here so the
        // broader Usage end-only rule is not applied a second time.
        this.completeDefaultMultiplicity(object);
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseItemUsage(ItemUsage object) {
        // Item usages use the default multiplicity rule. ConnectionUsage has its own
        // case and therefore does not inherit this rule.
        this.completeDefaultMultiplicity(object);
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean casePortUsage(PortUsage object) {
        // Port usages use the default multiplicity rule. Stop before the broader
        // Usage case to preserve this specialization.
        this.completeDefaultMultiplicity(object);
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseConnectionUsage(ConnectionUsage object) {
        // Connection usages deliberately use the generic end-only policy instead of
        // the default ItemUsage multiplicity rule.
        if (this.completesPreLinkMembers()) {
            UsageMultiplicityCompleter.completeEndMultiplicity(object);
        }
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseInvocationExpression(InvocationExpression object) {
        // Invocation expressions require an implicit result parameter.
        this.completeResultParameter(object);
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseConstructorExpression(ConstructorExpression object) {
        // Constructor expressions require an implicit result parameter. Its typing is now inferred by
        // ImplicitSpecializationRules (pilot) rather than materialized here.
        this.completeResultParameter(object);
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseFeatureReferenceExpression(FeatureReferenceExpression object) {
        // Feature-reference expressions require an implicit result parameter.
        this.completeResultParameter(object);
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseFeatureChainExpression(FeatureChainExpression object) {
        // Preserve result preparation for XMI/programmatic chains. Source-target preparation
        // is shared with the parser and reads only containment and stored direction.
        // Stop switch inheritance to avoid invoking result preparation twice.
        if (this.completesPreLinkMembers()) {
            this.caseInvocationExpression(object);
            FeatureChainSourceTargetCompleter.completeSourceTarget(object);
        }
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseTransitionUsage(TransitionUsage object) {
        // The source must be computed before the transition connectors. Transition
        // completion does not include generic Usage multiplicity completion, so a
        // non-null result stops supertype processing.
        if (this.completesPreLinkMembers()) {
            TransitionSourceCompleter.completeSource(object);
        }
        if (this.completesPostLinkMembers()) {
            TransitionLinkCompleter.completeTransitionLinks(object);
        }
        return STOP_SUPERTYPE_PROCESSING;
    }

    /**
     * Completes the default multiplicity during the pre-link phase for the given {@link Usage}.
     *
     * @param usage
     *            the {@link Usage} to complete
     */
    private void completeDefaultMultiplicity(Usage usage) {
        // Mirrors the pilot parser post-processors (ST6RI-947): subsetting targets are not consulted (ST6RI-774).
        if (this.completesPreLinkMembers()) {
            UsageMultiplicityCompleter.completeDefaultMultiplicity(usage);
        }
    }

    /**
     * Completes an {@link Expression} result parameter during the pre-link phase.
     *
     * @param expression
     *            the {@link Expression} to complete
     */
    private void completeResultParameter(Expression expression) {
        if (this.completesPreLinkMembers()) {
            ExpressionResultParameterCompleter.completeResultParameter(expression);
        }
    }

    /**
     *
     * Tells if the pre-link members must be completed.
     *
     * @return <code>true</code> if the pre-link members must be completed, <code>false</code> otherwise
     */
    private boolean completesPreLinkMembers() {
        return this.completionType == CompletionType.PRE_LINK_ONLY || this.completionType == CompletionType.ALL;
    }

    /**
     * Tell if the post-link members must be completed.
     *
     * @return <code>true</code> if the post-link members must be completed, <code>false</code> otherwise
     */
    private boolean completesPostLinkMembers() {
        return this.completionType == CompletionType.POST_LINK_ONLY || this.completionType == CompletionType.ALL;
    }
}
