package org.eclipse.syson.sysml.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;

public class ImplicitSpecializationAccumulator {

    private boolean hasSubSetting;

    private boolean hasRedefinition;

    private boolean hasSubclassification;

    private boolean hasFeatureTyping;

    private final List<Specialization> accumulator = new ArrayList<>();

    public ImplicitSpecializationAccumulator(boolean hasSubSetting, boolean hasRedefinition, boolean hasSubclassification, boolean hasFeatureTyping) {
        super();
        this.hasSubSetting = hasSubSetting;
        this.hasRedefinition = hasRedefinition;
        this.hasSubclassification = hasSubclassification;
        this.hasFeatureTyping = hasFeatureTyping;
    }

    public static ImplicitSpecializationAccumulator fromExistingSpecialisation(List<Specialization> specialization) {
        boolean hasSubSetting = false;
        boolean hasRedefinition = false;
        boolean hasSubclassification = false;
        boolean hasFeatureTyping = false;

        for (Specialization s : specialization) {
            if (!hasSubSetting && s instanceof Subsetting) {
                hasSubSetting = true;
            }
            if (!hasRedefinition && s instanceof Redefinition) {
                hasRedefinition = true;
            }
            if (!hasSubclassification && s instanceof Subclassification) {
                hasSubclassification = true;
            }
            if (!hasFeatureTyping && s instanceof FeatureTyping) {
                hasFeatureTyping = true;
            }
        }
        return new ImplicitSpecializationAccumulator(hasSubSetting, hasRedefinition, hasSubclassification, hasFeatureTyping);
    }

    public void addAll(List<? extends Specialization> specializations) {
        for (Specialization s : specializations) {
            this.add(s);
        }
    }

    public boolean hasSubSetting() {
        return this.hasSubSetting;
    }

    public boolean hasRedefinition() {
        return this.hasRedefinition;
    }

    public boolean hasSubclassification() {
        return this.hasSubclassification;
    }

    public boolean hasFeatureTyping() {
        return this.hasFeatureTyping;
    }

    public List<Specialization> getSpecializations() {
        return this.accumulator;
    }

    public void add(Specialization s) {
        if (!this.hasSubSetting && s instanceof Subsetting) {
            this.hasSubSetting = true;
        }
        if (!this.hasRedefinition && s instanceof Redefinition) {
            this.hasRedefinition = true;
        }
        if (!this.hasSubclassification && s instanceof Subclassification) {
            this.hasSubclassification = true;
        }
        if (!this.hasFeatureTyping && s instanceof FeatureTyping) {
            this.hasFeatureTyping = true;
        }
        this.accumulator.add(s);
    }

}
