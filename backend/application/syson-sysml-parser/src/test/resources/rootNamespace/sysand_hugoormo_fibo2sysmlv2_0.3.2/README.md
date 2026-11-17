# FiBO2SysMLv2 Project

This library provides a SysML v2 packaging of the EDM Council Financial Industry Business Ontology (FiBO) for model-driven engineering, analysis, and reuse in Sysand-compatible environments.

## Purpose

The goal is to make FiBO available as an executable, inspectable SysML v2 model set while preserving semantic intent from the source ontologies.

## Content

The project contains:

- Domain-organized SysML v2 modules across BE, BP, CAE, DER, FBC, FND, IND, LOAN, MD, SEC, ACTUS, and bridge packages.
- Translation outputs aligned to FiBO source modules and package structure where practical.
- Release packaging artifacts for Sysand (.project metadata, lockfile, and kpar output).

## Maintainers

- Hugo Ormo <hugo.ormo@icloud.com>
- Max Cramer <maxcramer@gmx.de>

## Assumptions

The library has been curated under two explicit assumptions:

- Fidelity assumption: model content should preserve source semantics and structural intent from EDM Council FiBO, with only minimal transformation needed for SysML v2 representation and package split strategy.
- Provenance assumption: traceable source alignment is required, with source references and gate-based checks used to manage missmatches, source-limited gaps, and controlled exceptions.

