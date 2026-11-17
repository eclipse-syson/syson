package RequirementsManagement_AttributesExample {
    doc /*
    Requirement management examples showing stakeholders, needs and requirements with specific metadata.
    
    Copyright 2025 Deutsches Zentrum für Luft- und Raumfahrt eV (DLR), Siemens Industry Software NV, Siemens AG, Siemens Industry Software SAS, IRT Saint Exupéry, EnginSoft SpA
    */
    private import SYSMOD::*;
    private import RequirementsManagement::*;

    part theStakeholder : Stakeholder {
        doc /* Stakeholder example */
        attribute redefines risk = RiskMetadata::LevelEnum::high;
        attribute redefines contact = "Person Name, email, tel, department, company";
        attribute redefines categories = StakeholderCategoryKind::RequirementOwner;
    }

    part projectTeamMember : Stakeholder {
        attribute :>> categories = StakeholderCategoryKind::Expert;
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
        :>> createdBy = "Name Surname";
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