package RequirementsManagement_AttributesExample {
    doc /*
    Requirement management examples showing stakeholders, needs and requirements with specific metadata.
    
    Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
    */
    private import SYSMOD::System;
    private import RequirementsManagement::*;

    part theStakeholder : ExtendedStakeholder {
        doc /* Stakeholder example */
        attribute redefines risk = SYSMOD::LevelKind::high;
        attribute redefines contact = "Person Name, email, tel, department, company";
        attribute redefines categories = StakeholderCategoryKind::requirementOwner;
    }

    part projectTeamMember : ExtendedStakeholder {
        attribute :>> categories = StakeholderCategoryKind::expert;
    }

    part projectTeamMemberA : ExtendedStakeholder {
        attribute :>> categories = StakeholderCategoryKind::expert;
    }

    concern <'N-001'> theStakeholderNeed : Need {
        doc /* Need showing all available attributes with example values */

        // Traceability attributes
        
        subject system : System;
        stakeholder references theStakeholder;

        attribute redefines rationale = "Rationale for existence of the need";
        attribute :>> source = "Source of the need (e.g. stakeholder expression or regulations).";

        // Authoring attributes

        :>> owner = projectTeamMember;
        :>> status = ReqStatus::Approved;
        :>> version = "1.0";
        :>> createdBy = projectTeamMemberA;
        :>> createdDate = new Time::Iso8601DateTime(new Time::Iso8601DateTimeEncoding("2021-05-19T12:00:00+02:00"));
        :>> modifiedDate = new Time::Iso8601DateTime(new Time::Iso8601DateTimeEncoding("2024-12-05T12:00:00+02:00"));
        :>> approvalDate = new Time::Iso8601DateTime(new Time::Iso8601DateTimeEncoding("2024-12-05"));
        :>> comments = "Any additional comments.
        Can be multiline.";

        // Implementation attributes

        :>> priority = ReqPriority::MustHave;
        :>> implementationResponsible = projectTeamMember;
        :>> implementationStatus = ReqImplementationStatus::Implemented;

        // Risk attributes

        :>> stability = ReqStability::Stable;
        :>> implementationRisk {
            @RiskMetadata::Risk {
                totalRisk {
                    probability = RiskMetadata::LevelEnum::medium;
                    impact = RiskMetadata::LevelEnum::high;
                }
                technicalRisk = RiskMetadata::RiskLevelEnum::high;
                scheduleRisk = RiskMetadata::RiskLevelEnum::medium;
                costRisk = RiskMetadata::RiskLevelEnum::low;
            }
        }
        :>> riskMitigation = "Description of risk mitigation measures";

        // System verification/validation attributes

        :>> verificationMethod = "By requirement satisfaction";
        :>> verificationResponsible = projectTeamMember;
        :>> verificationStatus = VVStatus::InProgress;

        requirement references theRequirement;

    }

    requirement <'R-001'> theRequirement : Requirement {
        doc /* Requirement showing the required attributes */

        :>> rationale = "Rationale for existence of the requirement";
        :>> source = "Source of the requirement (other than the referenced need), for example for any included assumptions/constraints/numbers.";

        :>> owner = projectTeamMember;

        :>> priority = ReqPriority::ShouldHave;
        :>> implementationResponsible = projectTeamMember;
        
        :>> verificationMethod = "Run MDAO workflow";
        :>> verificationResponsible = projectTeamMember;
        :>> verificationStatus = VVStatus::NotStarted;

    }

}