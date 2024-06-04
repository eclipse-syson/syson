package org.eclipse.syson.sysml.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link PerformActionUsageImpl}.
 *
 * @author Arthur Daussy
 */
public class PerformActionUsageImplTest {

    private final ModelBuilder builder = new ModelBuilder();

    @Test
    public void getNames() {

        PerformActionUsage performAction = this.builder.create(PerformActionUsage.class);

        ActionUsage actionUsage = this.builder.createInWithFullName(ActionUsage.class, null, "ActionUsage1", "AU1");

        this.builder.addReferenceSubsetting(performAction, actionUsage);

        assertEquals("ActionUsage1", performAction.getName());
        assertEquals("ActionUsage1", performAction.effectiveName());
        assertEquals("AU1", performAction.getShortName());
        assertEquals("AU1", performAction.effectiveShortName());
        assertEquals(actionUsage, performAction.namingFeature());

    }

}
