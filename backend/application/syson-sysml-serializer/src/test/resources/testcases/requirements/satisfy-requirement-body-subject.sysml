// Covers a satisfy requirement usage that redefines the subject parameter in its body instead of using by
// (found with the validation case checkSatisfyRequirementUsageBindingConnector-conforming)
package SatisfyRequirementBodySubject {
    part def Component;
    requirement def Required {
        subject participant : Component;
    }
    part system : Component {
        satisfy requirement explicit : Required {
            subject participant redefines Required::participant;
        }
    }
}
