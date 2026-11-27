// Covers the body members of a transition usage
// (found with the validation cases checkTransitionUsageSourceBindingConnector-conforming and
// checkTransitionUsageSuccessionBindingConnector-trigger-in-case-of-binding-to-body-attribute)
package TransitionUsageBody {
    state def Controller {
        state waiting;
        state running;
        transition change first waiting then running {
            attribute trace : ScalarValues::Boolean;
        }
    }
}
