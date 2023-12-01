# SysON Project

Welcome to the repository of the Eclipse SysON project.

## Background

Obeo, a prominent contributor to Eclipse's Modeling technologies, has a history of active involvement in the Model-Based Systems Engineering (MBSE) community notably through Capella. Our commitment to advancing modeling tools is evident through our work on Eclipse Sirius Web, which aims to revolutionize modeling tools. As we progress with Sirius Web, we see it becoming better suited for managing complex languages and domains.

CEA is another significant player in the Eclipse Modeling technologies world. It is the main contributor to the Papyrus modeling platform. This platform provides support for OMG standards such as UML 2.X and SysML 1.X and comes with a wide set of satellite tools providing capabilities such as simulation, code generation and document generation. CEA is widely involved in the definition of OMG standards that are provided by the Papyrus platform and its satellite tools. In particular, CEA chairs specifications such as MARTE (Model and Analysis of Real-Time and Embedded Systems), PSCS (Precise Semantics for UML Composite Structures) and PSSM (Precise Semantics for UML State Machines).

Since 2018, the Object Management Group (OMG) has initiated a major revision of SysML 1.X to increase the MBSE adoption. The intention was to develop language improvements over precision, expressiveness, consistency, interoperability, and usability. This work led to the production of SysML V2. SysML V2 introduces major changes that have an impact on both users and tool vendor’s levels. For instance, SysML V2 is not anymore based on UML but on KerML (a core modeling language with a well-grounded formal semantics). This redesign, SysMLv2, a crucial language for systems engineering, is highly important for system design and compatibility among MBSE tools. Notably Papyrus which already supports UML, SysMLv1 and Eclipse Capella, which is gaining strong traction, stands to benefit from this adoption.

To facilitate this transformative vision, the System Engineering community acknowledge the need for a robust open-source tool dedicated to SysMLv2. This realization prompted both the CEA and Obeo to initiate the development of a web-based SysMLv2 modeling tool using the Sirius Web platform. The CEA will represent the project at the OMG and will lead the effort regarding the SysMLv2 compliance and extensibility capabilities while Obeo will focus on the product and its user experience.

## Scope

Eclipse SysON project provides an open-source and interoperable tool for editing SysMLv2 models conforming to the OMG Standard, for the MBSE community.

This software will prominently showcase structured editors: graphical, form-based and tables, effectively utilizing the capabilities of the Sirius Web modeling platform. Additionally, the project will ensure seamless integration with Open-Source solutions like Papyrus and Capella, further enhancing the usability and versatility of the tool.

## Description

The Eclipse SysON project provides an open-source web-based tooling to edit SysML v2 models. It includes a set of editors (graphical, textual, form-based, etc.) enabling users to build the various parts of system models. Capitalizing on the capabilities of the Sirius Web platform, SysON offers a user-friendly interface, facilitating seamless model creation, modification, and visualization.

Furthermore, Eclipse SysON is the core of the SysMLv2 model editing feature of Papyrus and seamlessly enables co-design of SysMLv2 models alongside Eclipse Capella.

Additionally, Eclipse SysON embraces the standard API for interconnection, enhancing the interoperability of these vital modeling resources and will support the SysML v2 textual specifications as an exchange format, to ensure seamless transitions.

Through this initiative, we seek to foster growth within the MBSE community by providing a robust and accessible tool that harmonizes seamlessly with modern modeling landscapes.

## Licenses

Eclipse Public License 2.0

## Legal Issues

SysML® is a trademark owned by OMG with specific guidelines detailed here: <https://www.omg.org/legal/tm_guidelines.htm>

## Project Scheduling

* 2023
  * Q4: Initial Contribution, project and continuous integration setup, builds.
* 2024
  * Reach a level of maturity suitable for basic System modeling, which implies to:
  * Improve coverage and compliance of the standard in the metamodel implementations
  * Improve the editors, notably the  "General View"
  * Link with Capella and Capella-based products
  * Interoperability with the textual syntax and/or API

## Future Work

Realizing the implementation of such a standard via a user-friendly tool constitutes a substantial, long-term endeavor. Our aspiration is to achieve an initial level of practicality by 2024, followed by iterative enhancements.
