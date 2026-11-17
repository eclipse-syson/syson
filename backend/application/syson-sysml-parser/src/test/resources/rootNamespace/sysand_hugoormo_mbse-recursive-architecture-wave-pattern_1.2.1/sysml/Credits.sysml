package Credits {
    private import ScalarValues::*;

    doc
    /* Credits for the MBSE RecursiveArchitecture Weave Pattern example model. */

    package Licenses {
        metadata def AuthorLicense {
            attribute licenseId defined by ScalarValues::String;
            attribute copyrightHolder defined by ScalarValues::String;
            attribute copyrightYear defined by ScalarValues::String;
            attribute licenseUrl defined by ScalarValues::String;
            attribute appliesTo defined by ScalarValues::String;
            attribute attributionRequired defined by ScalarValues::Boolean;
            attribute exceptionsNote defined by ScalarValues::String;
        }

        metadata modelAuthorLicense : AuthorLicense {
            :>> licenseId = "CC-BY-4.0";
            :>> copyrightHolder = "Hugo Ormo";
            :>> copyrightYear = "2026";
            :>> licenseUrl = "https://creativecommons.org/licenses/by/4.0/";
            :>> appliesTo =
                "Original composition, architecture synthesis, and model integration in this repository";
            :>> attributionRequired = true;
            :>> exceptionsNote =
                "Third-party quoted or adapted materials retain their original licenses and attribution requirements";
        }

        comment authorLicensingNote about modelAuthorLicense locale "en_US"
        /* Licensing note for original authored content in this model:
         *
         * Copyright (c) 2026 hugoormo.
         * Original composition and integration work in this model is licensed under
         * Creative Commons Attribution 4.0 International (CC BY 4.0):
         * https://creativecommons.org/licenses/by/4.0/
         *
         * This licensing statement applies to original model composition, architecture
         * synthesis and integration performed in this repository. Third-party material,
         * including quoted text and externally sourced modeling patterns, remains under
         * its respective original license and attribution terms.
         */
    }

    package SourceAttributions {
        metadata def SourceCredit {
            attribute id defined by ScalarValues::String;
            attribute title defined by ScalarValues::String;
            attribute authors defined by ScalarValues::String;
            attribute sourceUrl defined by ScalarValues::String;
            attribute licenseSummary defined by ScalarValues::String;
            attribute reuseNote defined by ScalarValues::String;
            attribute dateAccessed defined by ScalarValues::String;
        }

        metadata uafCreditMeta : SourceCredit {
            :>> id = "uafml-1-3";
            :>> title = "Unified Architecture Framework Modeling Language (UAFML) Version 1.3";
            :>> authors = "IBM; KDM Analytics; Mega; OMG; No Magic; PTC; Sparx Systems";
            :>> sourceUrl = "";
            :>> licenseSummary = "Copyright and use terms retained verbatim in comment";
            :>> reuseNote = "Reference profile and terminology alignment";
            :>> dateAccessed = "2026-05-23";
        }

        metadata connectorPatternCreditMeta : SourceCredit {
            :>> id = "ports-interfaces-items-flows-lesson13";
            :>> title = "Advent of SysML v2 - Lesson 13: Ports, Interfaces, Items and Flows";
            :>> authors = "Vince Molnar; Kestutis Jankevicius";
            :>> sourceUrl =
                "https://sensmetry.com/advent-of-sysml-v2-lesson-13-ports-interfaces-items-and-flows/";
            :>> licenseSummary = "Citation / attribution in model documentation";
            :>> reuseNote = "Connector, interface, port, and flow container concepts";
            :>> dateAccessed = "2026-05-23";
        }

        metadata recursivePatternCreditMeta : SourceCredit {
            :>> id = "recursive-pattern";
            :>> title = "A SysML V2 Modeling Pattern for Recursive System Decomposition";
            :>> authors = "Sanford Friedenthal; Andrew B. Muxen";
            :>> sourceUrl = "";
            :>> licenseSummary = "Citation / attribution in model documentation";
            :>> reuseNote = "Recursive decomposition and requirement-derivation pattern baseline";
            :>> dateAccessed = "";
        }

        metadata useCasePatternCreditMeta : SourceCredit {
            :>> id = "use-case-pattern-ch33-1";
            :>> title = "The SysML v2 Book - Chapter 33.1";
            :>> authors = "Tim Weilkiens; Vince Molnar";
            :>> sourceUrl = "";
            :>> licenseSummary = "Citation / attribution in model documentation";
            :>> reuseNote = "Use-case structure and flow modeling approach";
            :>> dateAccessed = "";
        }

        metadata layeredInterfaceCreditMeta : SourceCredit {
            :>> id = "layeredInterfacePattern";
            :>> title = "A Representative Application of a Layered Interface Modeling Pattern";
            :>> authors = "Peter M. Shames; Marc A. Sarrel; Sanford Friedenthal";
            :>> sourceUrl = "docs/Sources/INCOSE_IS_2016_paper_Application-of-a-Layered-Interface-Modeling-Pattern.pdf";
            :>> licenseSummary = "Citation / attribution in model documentation";
            :>> reuseNote = "Layered Interface Modeling Pattern";
            :>> dateAccessed = "2026-06-10";
        }

        comment recursivePatternCredit about recursivePatternCreditMeta locale "en_US"
        /* The recursive pattern described in this model is based on
         * "A SysML V2 Modeling Pattern for Recursive System Decomposition"
         * by Sanford Friedenthal and Andrew B. Muxen.
         */
        comment layeredInterfaceCredit about layeredInterfaceCreditMeta locale "en_US"
        /* The layered interface pattern described in this model is based on
         * "A Representative Application of a Layered Interface Modeling Pattern"
         * Peter M. Shames, Marc A. Sarrel, and Sanford Friedenthal.
         */
        
        comment useCasePatternCredit about useCasePatternCreditMeta locale "en_US"
        /* The use-case pattern used in this model follows chapter `33.1` of
         * "The SysML v2 Book"
         * by Tim Weilkiens and Vince Molnar.
         */
        comment connectorPatternCredit about connectorPatternCreditMeta locale "en_US"
        /* The connector/interface/ports/flow container concept used in this model is
         * based on "Advent of SysML v2 - Lesson 13: Ports, Interfaces, Items and Flows"
         * by Vince Molnar and Kestutis Jankevicius:
         * https://sensmetry.com/advent-of-sysml-v2-lesson-13-ports-interfaces-items-and-flows/
         */

        comment UAFCredit about uafCreditMeta locale "en_US"
        /* Unified Architecture Framework Modeling Language (UAFML)
         * Version 1.3
         * Copyright © 2017-2021, IBM
         * Copyright © 2017-2019, KDM Analytics
         * Copyright © 2017-2025, Mega
         * Copyright © 2017-2025, Object Management Group, Inc.
         * Copyright © 2017-2025, No Magic Inc. a Dassault Systemes Company
         * Copyright © 2017-2019, PTC
         * Copyright © 2017-2025, Sparx Systems
         *
         * Subject to all of the terms and conditions below, the owners of the copyright in this specification hereby grant
         * you a fully-paid up, non-exclusive, nontransferable, perpetual, worldwide license (without the right to
         * sublicense), to use this specification to create and distribute software and special purpose specifications that are
         * based upon this specification, and to use, copy, and distribute this specification as provided under the Copyright
         * Act; provided that: (1) both the copyright notice identified above and this permission notice appear on any
         * copies of this specification; (2) the use of the specifications is for informational purposes and will not be copied
         * or posted on any network computer or broadcast in any media and will not be otherwise resold or transferred for
         * commercial purposes; and (3) no modifications are made to this specification. This limited permission
         * automatically terminates without notice if you breach any of these terms or conditions. Upon termination, you
         * will destroy immediately any copies of the specifications in your possession or control.
         */
    }

    package Acknowledgments {
        metadata def Acknowledgment {
            attribute person defined by ScalarValues::String;
            attribute contribution defined by ScalarValues::String;
        }

        metadata sanfordFriedenthalAcknowledgment : Acknowledgment {
            :>> person = "Sanford Friedenthal";
            :>> contribution = "Insights and comments on the recursive architecture pattern model";
        }

        metadata christianLalitschSchneiderAcknowledgment : Acknowledgment {
            :>> person = "Christian Lalitsch-Schneider";
            :>> contribution = "Revision and inport to Dassault Cameo";
        }

        comment sanfordFriedenthalAcknowledgmentNote
            about sanfordFriedenthalAcknowledgment
            locale "en_US"
        /* The author gratefully acknowledges Sanford Friedenthal for his insights
         * and comments on this recursive architecture pattern model.
         */

        comment christianLalitschSchneiderAcknowledgmentNote
            about christianLalitschSchneiderAcknowledgment
            locale "en_US"
        /* The author gratefully acknowledges Schristian Lalitsch-Schnieder for his revision
         * and insights on the import to Dassault's Cameo of this recursive architecture pattern model.
         */
    }
}