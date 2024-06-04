package org.eclipse.syson.sysml.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.Test;

/**
 *
 * Test class for {@link ConstraintUsageImpl}.
 *
 * @author Arthur Daussy
 */
public class ConstraintUsageImplTest {

    private final ModelBuilder builder = new ModelBuilder();

    @Test
    public void getNames() {
        ConstraintUsage superConstraint = this.builder.createWithName(ConstraintUsage.class, "SuperConstraint");
        superConstraint.setDeclaredShortName("SC");

        ConstraintUsage constraint = this.builder.create(ConstraintUsage.class);


        RequirementConstraintMembership reqMembership = this.builder.create(RequirementConstraintMembership.class);

        reqMembership.getOwnedRelatedElement().add(constraint);
        this.builder.addReferenceSubsetting(constraint, superConstraint);

        assertEquals("SuperConstraint", constraint.getName());
        assertEquals("SuperConstraint", constraint.effectiveName());
        assertEquals("SC", constraint.getShortName());
        assertEquals("SC", constraint.effectiveShortName());

        // Set declaredName and declaredShortName

        constraint.setDeclaredName("Co1");
        constraint.setDeclaredShortName("C1");

        assertEquals("Co1", constraint.getName());
        assertEquals("Co1", constraint.effectiveName());
        assertEquals("C1", constraint.getShortName());
        assertEquals("C1", constraint.effectiveShortName());
    }

}
