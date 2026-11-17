# Requirements Management

This library provides concepts related to requirements management, in particular a requirement def type that includes
various attributes to be modeled according to the INCOSE Needs & Requirements Manual (2024).

This library has originally been developed as part of the [ODE4HERA project](https://www.ode4hera.eu/),
however we encourage contributions by anyone!
To contribute, go to:

**https://github.com/ode4hera/SysML-v2-Requirements-Management**

## Modeling Concepts

The `examples` folder contains examples of models using the concepts described below.

### Stakeholders

For stakeholder modeling we reuse the concepts defined by [SYSMOD](https://sysand.com/projects/mbse4u/sysmod/).

### Needs and Requirements

Needs are informal expressions from stakeholders about what they want from the system.
Needs are often expressed in fuzzy and/or ambiguous terms, and are elicited by questioning the stakeholders.

Requirements define what the system should do in order to meet stakeholder needs.
Requirements should be verifiable, meaning that by the end of the design process, it can be checked whether the design
indeed satisfies the defined requirements. Therefore, as opposed to needs, requirements should be formulated using
precise, unambiguous, verifiable semantics.

In SysML v2, both needs and requirements can be modeled using `requirement` elements, since these can also simply
consist of a statement without including any semantics for automated checking the satisfaction of the requirement.
However, to separate needs from requirements, the `concern` type is used to model needs.

This library provides two base definitions for need and requirements:
- `concern def Need`
- `requiremend def Requirement`

### Needs and Requirements Attributes

This library provides a set of attributes for needs and requirements, which are defined based on chapter 15 of the
INCOSE Needs and Requirements Manual (2024). These attributes are defined by `NeedsRequirementsAttributes` and
inherited by the `Need` and `Requirement` defs.

The library defines in details which attributes are included, which attributes are not included because they are
possible to be represented semantically in SysML v2, and which attributes are not included for now (because they were
out of scope for the project).
