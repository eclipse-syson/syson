package RequirementViewAlternatives {
    metadata def Tag;
    part operator;
    constraint def Limit;
    constraint limit : Limit;
    concern def SafetyConcern;
    concern safety : SafetyConcern;

    requirement def SafetyRequirement {
        actor operator;
        require limit : Limit;
        require #Tag #Tag taggedLimit : Limit;
        frame safety : SafetyConcern;
        frame #Tag #Tag taggedSafety : SafetyConcern;
        frame #Tag concern explicitSafety : SafetyConcern;
    }
    requirement safetyRequirement : SafetyRequirement;
    assert not satisfy safetyRequirement by operator;
    assert satisfy safetyRequirement : SafetyRequirement by operator;
    satisfy requirement by operator;
    satisfy safetyRequirement = true by operator;
    assert limit : Limit;

    verification def SafetyCheck {
        objective {
            verify requirement checked : SafetyRequirement;
            verify safetyRequirement : SafetyRequirement;
            verify #Tag #Tag taggedRequirement : SafetyRequirement;
        }
    }

    use case def Subtask;
    use case def MainTask {
        include Subtask;
        include use case;
    }

    rendering def Diagram;
    rendering diagram : Diagram;
    view def SafetyView {
        render diagram : Diagram;
        render rendering focused : Diagram;
        render #Tag #Tag taggedDiagram : Diagram;
        render #Tag rendering explicitDiagram : Diagram;
    }
    view safetyView : SafetyView;
    view;
}
