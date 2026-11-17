package CometInterceptor {
    
    doc /* This model is based on the European Space Agency (ESA) Concurrent Design Facility (CDF) public report of the Comet Interceptor study (https://esamultimedia.esa.int/docs/cdf/CometInterceptor_CDF_Study_Report.pdf)
         * All contents come directly or are inferred from the information contained in this report, and do not take into account subsequent iterations of the mission design.
         * More information about the mission and the study and the list of acronyms can be found in the report.
         */

    private import ScalarValues::*;
    private import RequirementDerivation::*;
    private import SI::*;
    private import CDFReferenceDataLibrary::*;


    package 'Mission objectives' {
        use case 'Assess the bulk shape of the nucleus and the morphology of its surface at a scale comparable to the best images returned by previous comet flyby missions, and those returned by New Horizons of Kuiper Belt Objects' {
            subject ::> 'Comet Interceptor mission context'.'Comet Interceptor space system';
            actor ::> 'Comet Interceptor mission context'.LPC;
            actor ::> 'Comet Interceptor mission context'.'Science community';
        }
        use case 'Determine the bulk composition of the nucleus’ surface' {
            subject ::> 'Comet Interceptor mission context'.'Comet Interceptor space system';
            actor ::> 'Comet Interceptor mission context'.LPC;
            actor ::> 'Comet Interceptor mission context'.'Science community';
        }
        use case 'Investigate activity in a fresh comet' {
            subject ::> 'Comet Interceptor mission context'.'Comet Interceptor space system';
            actor ::> 'Comet Interceptor mission context'.LPC;
            actor ::> 'Comet Interceptor mission context'.'Science community';
        }
        use case 'Assess the molecular composition of the coma simultaneously at multiple locations and the isotopic composition along one path' {
            subject ::> 'Comet Interceptor mission context'.'Comet Interceptor space system';
            actor ::> 'Comet Interceptor mission context'.LPC;
            actor ::> 'Comet Interceptor mission context'.'Science community';
        }

        use case 'Identify parent and daughter neutral and ion species and assess their relationship in the coma' {
            subject ::> 'Comet Interceptor mission context'.'Comet Interceptor space system';
            actor ::> 'Comet Interceptor mission context'.LPC;
            actor ::> 'Comet Interceptor mission context'.'Science community';
        }
        use case 'Characterise the dust in the coma' {
            subject ::> 'Comet Interceptor mission context'.'Comet Interceptor space system';
            actor ::> 'Comet Interceptor mission context'.LPC;
            actor ::> 'Comet Interceptor mission context'.'Science community';
        }
        use case 'Assess the structure of boundaries and regions in the plasma environment of a comet' {
            subject ::> 'Comet Interceptor mission context'.'Comet Interceptor space system';
            actor ::> 'Comet Interceptor mission context'.LPC;
            actor ::> 'Comet Interceptor mission context'.'Science community';
        }
        use case 'Assess the energy, mass and momentum transfer in the cometary environment, through the coma and across boundaries' {
            subject ::> 'Comet Interceptor mission context'.'Comet Interceptor space system';
            actor ::> 'Comet Interceptor mission context'.LPC;
            actor ::> 'Comet Interceptor mission context'.'Science community';
        }
        use case 'Assess how plasma and dust interact in various regions of the coma' {
            subject ::> 'Comet Interceptor mission context'.'Comet Interceptor space system';
            actor ::> 'Comet Interceptor mission context'.LPC;
            actor ::> 'Comet Interceptor mission context'.'Science community';
        }
    }


    package 'Mission requirements' {
        requirement <'MIS-010'> {
            doc /* The mission shall intercept a Long Period Comet (LPC) or an interstellar body in a fly-by scenario. */
            comment /* See science requirements for more details */
        }
        requirement <'MIS-020'> {
            doc /* The mission shall include one main S/C (ESA) (hereafter S/C A) and two probes (one JAXA and one ESA) (namely probes B1 and B2, respectively) 
                * in order to gather multi-point observations of the comet and its coma. */
            comment /* See science requirements for more details */
        }
        requirement <'MIS-030'> {
            doc /* The mission shall embark the instruments as specified in the relevant Instrument Interface Description Documents. */
            comment /* See science requirements for more details */
        }
        requirement <'MIS-040'> {
            doc /* The mission shall allow parking the spacecraft in a Halo orbit around the SEL2 point after launch, wait for a target of opportunity, and then intercept it. */
            comment /* Ability to find a suitable target comet */
        }
        requirement <'MIS-050'> {
            doc /* The mission shall allow a fly-by to backup target comets 73P/Schwassmann-Wachmann (TBC) and 26P/Grigg-Skjellerup (TBC) within the nominal mission lifetime. */
            comment /* Ability to find a suitable backup comet in case no other suitable target is found */
        }
        requirement <'MIS-060'> {
            doc /* The mission shall be compatible with a launch together with the ARIEL mission/spacecraft. */
            comment /* Mission constraint for a shared launch with ARIEL */
        }
        requirement <'MIS-070'> {
            doc /* The nominal mission lifetime shall be maximum 5 years (TBC). */
            comment /* F-class mission constraint */
        }
        requirement <'MIS-080'> {
            doc /* The mission shall be compatible with ESA's F-mission budget Cost to Completion (CaC) not exceeding 150 M€. */
            comment /* F-class mission constraint */
        }
    }


    package 'Mission requirement satisfaction' {
        private import 'Mission requirements'::*;

        satisfy 'MIS-010' by 'Comet Interceptor mission context'.'Comet Interceptor space system';
        satisfy 'MIS-020' by 'Comet Interceptor mission context'.'Comet Interceptor space system';
        satisfy 'MIS-030' by 'Comet Interceptor mission context'.'Comet Interceptor space system';
        satisfy 'MIS-040' by 'Comet Interceptor mission context'.'Comet Interceptor space system';
        satisfy 'MIS-050' by 'Comet Interceptor mission context'.'Comet Interceptor space system';
        satisfy 'MIS-060' by 'Comet Interceptor mission context'.'Comet Interceptor space system';
        satisfy 'MIS-070' by 'Comet Interceptor mission context'.'Comet Interceptor space system';
        satisfy 'MIS-080' by 'Comet Interceptor mission context'.'Comet Interceptor space system';
    }


    package 'System requirements' {
        requirement <'SYS-010'> {
            doc /* The Comet Interceptor S/C shall contain one main S/C (S/C A - ESA) and two probes (B2 - ESA and B1 - JAXA). */
        }
        requirement <'SYS-020'> {
            doc /* The Comet Interceptor S/C shall be launched by the Ariane 62 launcher from Kourou (Guiana Space Centre). */
        }
        requirement <'SYS-030'> {
            doc /* The Comet Interceptor S/C shall be launched in 2028. */
        }
        requirement <'SYS-040'> {
            doc /* The Comet Interceptor S/C nominal lifetime shall be maximum 5 years (TBC). */
        }
        requirement <'SYS-050'> {
            doc /* The Comet Interceptor S/C shall embark the instruments as specified in the relevant Instrument Interface Description Documents. */
        }
        requirement <'SYS-060'> {
            doc /* The Comet Interceptor S/C A post flyby phase duration shall be maximum 6 months. */
        }
        requirement <'SYS-070'> {
            doc /* All Comet Interceptor S/C equipment shall be TRL 6 by Mission Selection (Q1 2020) and TRL 7 by Mission Adoption (Q4 2022). */
        }
        requirement <'SYS-080'> {
            doc /* The Comet Interceptor S/C A shall provide the mechanical, thermal and electrical interfaces for both probes B1 and B2. */
        }
        requirement <'SYS-090'> {
            doc /* The S/C A, B1 and B2 configurations shall ensure unobstructed fields of view for the instruments for observation purposes and for the communication antennas. */
        }
        requirement <'SYS-100'> {
            doc /* The S/C A shall carry the probes (B1 and B2) from integration up to their separation before the flyby. */
        }
        requirement <'SYS-101'> {
            doc /* The mission shall allow a release of the probe B2 at least 24 hours (TBC) before the closest approach. */
        }
        requirement <'SYS-102'> {
            doc /* The mission shall allow a flyby altitude for the probe B2 of between 100 m to 400 m (TBC). */
        }
        requirement <'SYS-103'> {
            doc /* The mission shall allow a release of the probe B1 at least 48 hours (TBC) before the closest approach. */
        }
        requirement <'SYS-104'> {
            doc /* The mission shall allow a flyby altitude for the probe B1 of between 100 m to 400 m (TBC). */
        }
        requirement <'SYS-110'> {
            doc /* The instruments on B1 and B2 shall be used only after separation of the spacecraft from S/C A. */
        }
        requirement <'SYS-120'> {
            doc /* Check-out operation activities of the scientific instruments and the rest of equipment on-board the probes B1 and B2 shall be possible before their separation from S/C A. */
        }
        requirement <'SYS-130'> {
            doc /* The probe B2 shall be designed considering a design-to-cost approach. */
        }
        requirement <'SYS-140'> {
            doc /* The probe B2 shall be designed for a 24h (TBC) lifetime. */
        }
        requirement <'SYS-150'> {
            doc /* The probe B2 shall be spin-stabilised. */
        }
    }


    package 'System requirement satisfaction' {
        private import 'System requirements'::*;

        satisfy 'SYS-010' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-020' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-030' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-040' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-050' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-060' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-070' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-080' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-090' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-100' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-101' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-102' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-103' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-104' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-110' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-120' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-130' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-140' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
        satisfy 'SYS-150' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
    }


    package 'Mission to System requirement derivation' {
        private import 'Mission requirements'::*;
        private import 'System requirements'::*;

        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-010';
        }
        #derivation connection {
            end #original ::> 'MIS-060';
            end #derive ::> 'SYS-020';
        }
        #derivation connection {
            end #original ::> 'MIS-080';
            end #derive ::> 'SYS-030';
        }
        #derivation connection {
            end #original ::> 'MIS-070';
            end #derive ::> 'SYS-040';
        }
        #derivation connection {
            end #original ::> 'MIS-030';
            end #derive ::> 'SYS-050';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-060';
        }
        #derivation connection {
            end #original ::> 'MIS-070';
            end #derive ::> 'SYS-060';
        }
        #derivation connection {
            end #original ::> 'MIS-060';
            end #derive ::> 'SYS-070';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-080';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-090';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-100';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-101';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-102';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-103';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-104';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-110';
        }
        #derivation connection {
            end #original ::> 'MIS-030';
            end #derive ::> 'SYS-110';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-120';
        }
        #derivation connection {
            end #original ::> 'MIS-030';
            end #derive ::> 'SYS-120';
        }
        #derivation connection {
            end #original ::> 'MIS-080';
            end #derive ::> 'SYS-130';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-140';
        }
        #derivation connection {
            end #original ::> 'MIS-030';
            end #derive ::> 'SYS-140';
        }
        #derivation connection {
            end #original ::> 'MIS-080';
            end #derive ::> 'SYS-140';
        }
        #derivation connection {
            end #original ::> 'MIS-020';
            end #derive ::> 'SYS-150';
        }
        #derivation connection {
            end #original ::> 'MIS-030';
            end #derive ::> 'SYS-150';
        }
    }


    // Mission Architecture
    part 'Comet Interceptor mission context' {
        part 'Comet Interceptor space system' {
            part 'Ariane 62 launcher'{
                comment /* Dual launch with ARIEL */
            }
            part 'Comet Interceptor ground segment' {
                part <MOC> 'Mission Operations Centre';
                part <'JAXA MOC'> 'JAXA Mission Operations Centre';
                part 'ESTRACK ground station network';
            }
            part 'Comet Interceptor space segment'; // System of Interest

            exhibit 'Mission phases';
        }
        part <LPC> 'Long Period Comet';
        part 'Science community';
        part Earth;
        part Sun;
        part 'Space environment';
    }


    state 'Mission phases' {
        entry;
        then state 'Ariane 62 launch from Kourou';
        then state 'L2 transfer';
        then state 'Waiting at L2';
        then state 'Target transfer';
        then state 'Comet fly-by';
        then done;
    }


    state 'System modes'{
        state <LAU> 'Launch mode' {
            doc /* From launch to spacecraft separation.
                * Most equipment OFF (OBC and receiver potentially ON)
                */
        }
        state <SUN> 'Sun Acquisition Mode' {
            doc /* From spacecraft separation up to Sun pointing attitude
                * Minimum units are ON (Sun sensors, TT&C up- and down-link via LGA)
                * Instruments are OFF
                */
        }
        state <SAFE> 'Safe Mode' {
            doc /* Minimum units ON to ensure prolonged safe state and communications to facilitate coming back to a nominal mode
                * Instruments are OFF
                */
        }
        state <STBY> 'Standby Mode' {
            doc /* Applicable when not thrusting, communicating with Earth, nor acquiring science data
                * Instruments are OFF
                * Most platform units are ON
                */
        }
        state <COM> 'Communication Mode' {
            doc /* Applicable for communication with Earth
                * Most platform units are ON and transmitter is ON
                */
        }
        state <EPTH> 'EP Thrusting Mode' {
            doc /* Applicable for reaching the target object
                * Instruments are OFF
                * Most platform units are ON and EP thruster is ON
                */
        }
        state <SCI> 'Science Mode' {
            doc /* Applicable for science acquisition during the fly-by at encounter
                * Instruments are ON
                * Most platform units are ON
                */
        }

        entry;
        then LAU;
        transition first LAU then SUN;
        transition first SUN then STBY;
        transition first STBY then SAFE;
        transition first SAFE then STBY;
        transition first STBY then SCI;
        transition first SCI then STBY;
        transition first STBY then COM;
        transition first COM then STBY;
        transition first STBY then EPTH;
        transition first EPTH then STBY;
        transition first SCI then SAFE;
        transition first COM then SAFE;
        transition first EPTH then SAFE;
    }


    state 'Equipment power modes' {
        entry;
        then state OFF;
        then state <STBY> 'Stand-by';
        then state ON;
        transition first OFF then ON;
        transition first STBY then OFF;
        transition first ON then OFF;
        transition first ON then STBY;
    }

    allocate 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment' to 'Comet Interceptor space segment (physical)';


    // Product tree
    #cDFProduct part 'Comet Interceptor space segment (physical)' {
        #cDFProduct part 'Probe B1' {
            attribute :>> totalMass = 30.00 [kg];

            exhibit 'System modes';
        }
        #cDFProduct part 'Probe B2' {
            attribute :>> totalMass = 40.47 [kg];
            attribute :>> powerInLAU = 0.00 [W];
            attribute :>> powerInSUN = 0.00 [W];
            attribute :>> powerInSAFE = 0.00 [W];
            attribute :>> powerInSTBY = 0.00 [W];
            attribute :>> powerInCOM = 0.00 [W];
            attribute :>> powerInEPTH = 0.00 [W];
            attribute :>> powerInSCI = 35.59 [W];

            exhibit 'System modes'.SCI;
        }
        #cDFProduct part <'S/C A'> 'Main spacecraft' {
            #cDFProduct part <INS> Payload {
                #cDFProduct part <CoCa> 'Comet Camera' {
                    attribute :>> totalMass = 12.81 [kg];
                    attribute :>> powerInLAU = 0.00 [W];
                    attribute :>> powerInSUN = 0.00 [W];
                    attribute :>> powerInSAFE = 0.00 [W];
                    attribute :>> powerInSTBY = 0.00 [W];
                    attribute :>> powerInCOM = 0.00 [W];
                    attribute :>> powerInEPTH = 0.00 [W];
                    attribute :>> powerInSCI = 14.40 [W];
                    attribute :>> MinNOT = -20 ['°C_abs'];
                    attribute :>> MinOT = -20 ['°C_abs'];
                    attribute :>> MaxOT = 25 ['°C_abs'];
                    attribute :>> MaxNOT = 35 ['°C_abs'];
                }
                #cDFProduct part <DFP> 'Dust Field and Plasma package' {
                    attribute :>> totalMass = 10.788 [kg];
                    attribute :>> powerInLAU = 0.00 [W];
                    attribute :>> powerInSUN = 0.00 [W];
                    attribute :>> powerInSAFE = 0.00 [W];
                    attribute :>> powerInSTBY = 0.00 [W];
                    attribute :>> powerInCOM = 0.00 [W];
                    attribute :>> powerInEPTH = 0.00 [W];
                    attribute :>> powerInSCI = 20.16 [W];
                    attribute :>> MinNOT = -25 ['°C_abs'];
                    attribute :>> MinOT = -20 ['°C_abs'];
                    attribute :>> MaxOT = 40 ['°C_abs'];
                    attribute :>> MaxNOT = 50 ['°C_abs'];
                }
                #cDFProduct part <MANiac> 'Mass Analyzer for Neutrals in a Coma' {
                    attribute :>> totalMass = 6.54 [kg];
                    attribute :>> powerInLAU = 0.00 [W];
                    attribute :>> powerInSUN = 0.00 [W];
                    attribute :>> powerInSAFE = 0.00 [W];
                    attribute :>> powerInSTBY = 0.00 [W];
                    attribute :>> powerInCOM = 0.00 [W];
                    attribute :>> powerInEPTH = 0.00 [W];
                    attribute :>> powerInSCI = 27.60 [W];
                    attribute :>> MinNOT = -30 ['°C_abs'];
                    attribute :>> MinOT = -20 ['°C_abs'];
                    attribute :>> MaxOT = 40 ['°C_abs'];
                    attribute :>> MaxNOT = 50 ['°C_abs'];
                }
                #cDFProduct part <MIRMIS> 'Modular Infrared Molecules and Ice Sensor' {
                    attribute :>> totalMass = 6.93 [kg];
                    attribute :>> powerInLAU = 0.00 [W];
                    attribute :>> powerInSUN = 0.00 [W];
                    attribute :>> powerInSAFE = 0.00 [W];
                    attribute :>> powerInSTBY = 0.00 [W];
                    attribute :>> powerInCOM = 0.00 [W];
                    attribute :>> powerInEPTH = 0.00 [W];
                    attribute :>> powerInSCI = 9.00 [W];
                    attribute :>> MinNOT = -40 ['°C_abs'];
                    attribute :>> MinOT = -30 ['°C_abs'];
                    attribute :>> MaxOT = 60 ['°C_abs'];
                    attribute :>> MaxNOT = 70 ['°C_abs'];
                }
            }
            #cDFProduct part Platform {
                #cDFProduct part <AOGNC> 'Attitude, orbit, guidance, navigation control' {
                    #cDFProduct part <A_DPU> 'A DTU Data Processing Unit' {
                        attribute :>> mass = 0.560 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 3.6 [W];
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = -1;
                        attribute :>> DC_SAFE = -1;
                        attribute :>> DC_STBY = -1;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = 1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -65 ['°C_abs'];
                        attribute :>> MinOT = -60 ['°C_abs'];
                        attribute :>> MaxOT = 35 ['°C_abs'];
                        attribute :>> MaxNOT = 40 ['°C_abs'];
                    }
                    #cDFProduct part <A_IMU_LN200> 'AIMU Northrop Grumman LN200 Core' [2] {
                        attribute :>> mass = 0.750 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 12 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                        attribute :>> redundancyConceptK = 1;
                        attribute :>> redundancyConceptN = 2;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = 1;
                        attribute :>> DC_SAFE = 1;
                        attribute :>> DC_STBY = 1;
                        attribute :>> DC_COM = -1;
                        attribute :>> DC_EPTH = 1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -59 ['°C_abs'];
                        attribute :>> MinOT = -54 ['°C_abs'];
                        attribute :>> MaxOT = 71 ['°C_abs'];
                        attribute :>> MaxNOT = 76 ['°C_abs'];
                    }
                    #cDFProduct part <A_NAVCAM_OH> 'A DTU NAVCAM Optical Head' [2] {
                        attribute :>> mass = 0.376 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 0.7 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                        attribute :>> redundancyConceptK = 1;
                        attribute :>> redundancyConceptN = 2;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = -1;
                        attribute :>> DC_SAFE = -1;
                        attribute :>> DC_STBY = -1;
                        attribute :>> DC_COM = -1;
                        attribute :>> DC_EPTH = -1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -65 ['°C_abs'];
                        attribute :>> MinOT = -60 ['°C_abs'];
                        attribute :>> MaxOT = 35 ['°C_abs'];
                        attribute :>> MaxNOT = 40 ['°C_abs'];
                    }
                    #cDFProduct part <A_STR_OH> 'A DTU STR Optical Head' [2] {
                        attribute :>> mass = 0.350 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 0.7 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                        attribute :>> redundancyConceptK = 1;
                        attribute :>> redundancyConceptN = 2;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = -1;
                        attribute :>> DC_SAFE = -1;
                        attribute :>> DC_STBY = -1;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = 1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -60 ['°C_abs']; // was -40 in the report (incorrect)
                        attribute :>> MinOT = -60 ['°C_abs'];
                        attribute :>> MaxOT = 35 ['°C_abs'];
                        attribute :>> MaxNOT = 40 ['°C_abs'];
                    }
                    #cDFProduct part <A_SUN_LENS_Bison64> 'A SUN LENS Bison 64' [6] {
                        attribute :>> mass = 0.025 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = -45 ['°C_abs'];
                        attribute :>> MinOT = -40 ['°C_abs'];
                        attribute :>> MaxOT = 80 ['°C_abs'];
                        attribute :>> MaxNOT = 85 ['°C_abs'];
                    }
                    #cDFProduct part <RW_RSI_4_215> 'A RW Rockwell Collins RSI 4-215' [4] {
                        attribute :>> mass = 5.400 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 20 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::active;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                        attribute :>> redundancyConceptK = 3;
                        attribute :>> redundancyConceptN = 4;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = -1;
                        attribute :>> DC_SAFE = -1;
                        attribute :>> DC_STBY = -1;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = -1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -40 ['°C_abs'];
                        attribute :>> MinOT = -20 ['°C_abs'];
                        attribute :>> MaxOT = 70 ['°C_abs'];
                        attribute :>> MaxNOT = 75 ['°C_abs'];
                    }
                }
                #cDFProduct part <COM> Communications {
                    #cDFProduct part <XDST> 'X-Band DSTRASP' { // product artificially decomposed into receiver and transmitter, for a better calculation of power budget
                        #cDFProduct part <XPND_RX> 'Transponder Receiver' [2] {
                            attribute :>> mass = 3.600 [kg]; // full XDST mass is shown in RX element (artificial)
                            attribute :>> MM = 0.05;
                            attribute :>> P_stby = 16 [W];
                            attribute :>> P_on = 16 [W];
                            attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::active;
                            attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                            attribute :>> redundancyConceptK = 1;
                            attribute :>> redundancyConceptN = 2;
                            attribute :>> DC_LAU = -1;
                            attribute :>> DC_SUN = 1;
                            attribute :>> DC_SAFE = 1;
                            attribute :>> DC_STBY = 1;
                            attribute :>> DC_COM = 1;
                            attribute :>> DC_EPTH = 1;
                            attribute :>> DC_SCI = 1;
                            attribute :>> MinNOT = -40 ['°C_abs'];
                            attribute :>> MinOT = -20 ['°C_abs'];
                            attribute :>> MaxOT = 65 ['°C_abs'];
                            attribute :>> MaxNOT = 70 ['°C_abs'];
                        }
                        #cDFProduct part <XPND_TX> 'Transponder Transmitter' [2] {
                            attribute :>> mass = 0 [kg]; // full XDST mass is shown in RX element (artificial)
                            attribute :>> MM = 0.05;
                            attribute :>> P_stby = 0 [W];
                            attribute :>> P_on = 16 [W];
                            attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                            attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                            attribute :>> redundancyConceptK = 1;
                            attribute :>> redundancyConceptN = 2;
                            attribute :>> DC_LAU = -1;
                            attribute :>> DC_SUN = 1;
                            attribute :>> DC_SAFE = 1;
                            attribute :>> DC_STBY = -1;
                            attribute :>> DC_COM = 1;
                            attribute :>> DC_EPTH = -1;
                            attribute :>> DC_SCI = -1;
                            attribute :>> MinNOT = -40 ['°C_abs'];
                            attribute :>> MinOT = -20 ['°C_abs'];
                            attribute :>> MaxOT = 65 ['°C_abs'];
                            attribute :>> MaxNOT = 70 ['°C_abs'];
                        }
                    }
                    #cDFProduct part <XHGA> 'X-Band HGA' {
                        attribute :>> mass = 6.050 [kg];
                        attribute :>> MM = 0.1;
                        attribute :>> MinNOT = -155 ['°C_abs'];
                        attribute :>> MinOT = -150 ['°C_abs'];
                        attribute :>> MaxOT = 150 ['°C_abs'];
                        attribute :>> MaxNOT = 150 ['°C_abs'];
                    }
                    #cDFProduct part <XLGA> 'X-Band LGA' [2] {
                        attribute :>> mass = 0.300 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = -155 ['°C_abs'];
                        attribute :>> MinOT = -150 ['°C_abs'];
                        attribute :>> MaxOT = 150 ['°C_abs'];
                        attribute :>> MaxNOT = 150 ['°C_abs'];
                    }
                    #cDFProduct part <XRFDN> 'X-Band RFDN' {
                        attribute :>> mass = 10.000 [kg];
                        attribute :>> MM = 0.2;
                        attribute :>> MinNOT = -40 ['°C_abs'];
                        attribute :>> MinOT = -30 ['°C_abs'];
                        attribute :>> MaxOT = 60 ['°C_abs'];
                        attribute :>> MaxNOT = 70 ['°C_abs'];
                    }
                    #cDFProduct part <XTWT> 'X-Band TWT' [2] {
                        attribute :>> mass = 0.800 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 103.07 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                        attribute :>> redundancyConceptK = 1;
                        attribute :>> redundancyConceptN = 2;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = 0.55;
                        attribute :>> DC_SAFE = 0.3;
                        attribute :>> DC_STBY = -1;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = 0;
                        attribute :>> DC_SCI = -1;
                        attribute :>> MinNOT = -40 ['°C_abs'];
                        attribute :>> MinOT = -20 ['°C_abs'];
                        attribute :>> MaxOT = 60 ['°C_abs'];
                        attribute :>> MaxNOT = 70 ['°C_abs'];
                    }
                    #cDFProduct part <XTWTA_EPC> 'X-Band TWTA EPC' [2] {
                        attribute :>> mass = 1.400 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 9 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                        attribute :>> redundancyConceptK = 1;
                        attribute :>> redundancyConceptN = 2;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = 1;
                        attribute :>> DC_SAFE = 1;
                        attribute :>> DC_STBY = -1;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = 0;
                        attribute :>> DC_SCI = -1;
                        attribute :>> MinNOT = -30 ['°C_abs'];
                        attribute :>> MinOT = -20 ['°C_abs'];
                        attribute :>> MaxOT = 60 ['°C_abs'];
                        attribute :>> MaxNOT = 70 ['°C_abs'];
                    }
                    #cDFProduct part <A_ISL_GOMx_board> 'A GOMx Electronics' [2] {
                        attribute :>> mass = 0.503 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 6.72 [W];
                        attribute :>> P_on = 8.16 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                        attribute :>> redundancyConceptK = 1;
                        attribute :>> redundancyConceptN = 2;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = -1;
                        attribute :>> DC_SAFE = -1;
                        attribute :>> DC_STBY = -1;
                        attribute :>> DC_COM = -1;
                        attribute :>> DC_EPTH = -1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -40 ['°C_abs'];
                        attribute :>> MinOT = -30 ['°C_abs'];
                        attribute :>> MaxOT = 85 ['°C_abs'];
                        attribute :>> MaxNOT = 95 ['°C_abs'];
                    }
                    #cDFProduct part <A_ISL_GOMx_Patch> 'A GOMx Antenna Patch' [6] {
                        attribute :>> mass = 0.150 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 1 [W];
                        attribute :>> P_on = 7.2 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                        attribute :>> redundancyConceptK = 3; // was 1 in the report (incorrect)
                        attribute :>> redundancyConceptN = 6; // was 2 in the report (incorrect)
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = -1;
                        attribute :>> DC_SAFE = -1;
                        attribute :>> DC_STBY = -1;
                        attribute :>> DC_COM = -1;
                        attribute :>> DC_EPTH = -1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -40 ['°C_abs'];
                        attribute :>> MinOT = -30 ['°C_abs'];
                        attribute :>> MaxOT = 85 ['°C_abs'];
                        attribute :>> MaxNOT = 95 ['°C_abs'];
                    }
                }
                #cDFProduct part <DH> 'Data handling' {
                    #cDFProduct part <A_OBC> 'A Onboard Computer' {
                        attribute :>> mass = 6.500 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 23 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::internal;
                        attribute :>> redundancyConceptK = 1;
                        attribute :>> redundancyConceptN = 2;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = 1;
                        attribute :>> DC_SAFE = 1;
                        attribute :>> DC_STBY = 1;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = 1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -40 ['°C_abs'];
                        attribute :>> MinOT = -30 ['°C_abs'];
                        attribute :>> MaxOT = 60 ['°C_abs'];
                        attribute :>> MaxNOT = 75 ['°C_abs'];
                    }
                    #cDFProduct part <A_RIU> 'A Remote Interface Unit' {
                        attribute :>> mass = 12.000 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 30 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::internal;
                        attribute :>> redundancyConceptK = 1;
                        attribute :>> redundancyConceptN = 2;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = 1;
                        attribute :>> DC_SAFE = 1;
                        attribute :>> DC_STBY = 1;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = 1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -40 ['°C_abs'];
                        attribute :>> MinOT = -30 ['°C_abs'];
                        attribute :>> MaxOT = 60 ['°C_abs'];
                        attribute :>> MaxNOT = 75 ['°C_abs'];
                    }
                }
                #cDFProduct part <PWR> Power {
                    #cDFProduct part <A_SA> 'A SolarArray' [2] {
                        attribute :>> mass = 14.630 [kg];
                        attribute :>> MM = 0.1;
                        attribute :>> MinNOT = -150 ['°C_abs'];
                        attribute :>> MinOT = -150 ['°C_abs'];
                        attribute :>> MaxOT = 150 ['°C_abs'];
                        attribute :>> MaxNOT = 150 ['°C_abs'];
                    }
                    #cDFProduct part <A_Bat> 'A Battery' {
                        attribute :>> mass = 4.900 [kg];
                        attribute :>> MM = 0.1;
                        attribute :>> MinNOT = -10 ['°C_abs'];
                        attribute :>> MinOT = 10 ['°C_abs'];
                        attribute :>> MaxOT = 30 ['°C_abs'];
                        attribute :>> MaxNOT = 45 ['°C_abs'];
                    }
                    #cDFProduct part <A_PCDU> 'A PCDU' {
                        attribute :>> mass = 18.500 [kg];
                        attribute :>> MM = 0.1;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 30 [W];
                        attribute :>> DC_LAU = 0;
                        attribute :>> DC_SUN = 1;
                        attribute :>> DC_SAFE = 1;
                        attribute :>> DC_STBY = 1;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = 1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -40 ['°C_abs'];
                        attribute :>> MinOT = -20 ['°C_abs'];
                        attribute :>> MaxOT = 60 ['°C_abs'];
                        attribute :>> MaxNOT = 60 ['°C_abs'];
                    }
                }
                #cDFProduct part <CPROP> 'Chemical propulsion'{
                    #cDFProduct part <A_Lat_Val> 'A Latch Valves' [2] {
                        attribute :>> mass = 0.550 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 30 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                        attribute :>> redundancyConceptK = 1; // was 2 in the report (incorrect)
                        attribute :>> redundancyConceptN = 2; // was 4 in the report (incorrect)
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = 0.475;
                        attribute :>> DC_SAFE = 0.1;
                        attribute :>> DC_STBY = 0.01;
                        attribute :>> DC_COM = 0.01;
                        attribute :>> DC_EPTH = 0.0002;
                        attribute :>> DC_SCI = 0;
                        attribute :>> MinNOT = 0 ['°C_abs'];
                        attribute :>> MinOT = 0 ['°C_abs'];
                        attribute :>> MaxOT = 50 ['°C_abs'];
                        attribute :>> MaxNOT = 50 ['°C_abs'];
                    }
                    #cDFProduct part <A_Pass_Valve> 'A Passivation_Valve' {
                        attribute :>> mass = 0.070 [kg];
                        attribute :>> MM = 0.05;
                    }
                    #cDFProduct part <A_Pipes> 'A Pipes' {
                        attribute :>> mass = 3.000 [kg];
                        attribute :>> MM = 0.2;
                        attribute :>> MinNOT = 0 ['°C_abs'];
                        attribute :>> MinOT = 0 ['°C_abs'];
                        attribute :>> MaxOT = 50 ['°C_abs'];
                        attribute :>> MaxNOT = 50 ['°C_abs'];
                    }
                    #cDFProduct part <A_Press_Trans> 'A Pressure Transducer' [3] {
                        attribute :>> mass = 0.220 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0.216 [W];
                        attribute :>> P_on = 0.216 [W];
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = 1;
                        attribute :>> DC_SAFE = 1;
                        attribute :>> DC_STBY = 1;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = 1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = 0 ['°C_abs'];
                        attribute :>> MinOT = 0 ['°C_abs'];
                        attribute :>> MaxOT = 50 ['°C_abs'];
                        attribute :>> MaxNOT = 50 ['°C_abs'];
                    }
                    #cDFProduct part <A_Prop_Filt> 'A Propellant Filter' {
                        attribute :>> mass = 0.110 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = 0 ['°C_abs'];
                        attribute :>> MinOT = 0 ['°C_abs'];
                        attribute :>> MaxOT = 50 ['°C_abs'];
                        attribute :>> MaxNOT = 50 ['°C_abs'];
                    }
                    #cDFProduct part <A_Fil_Dr_Val> 'A Fill Drain Valve' [2] {
                        attribute :>> mass = 0.070 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = 0 ['°C_abs'];
                        attribute :>> MinOT = 0 ['°C_abs'];
                        attribute :>> MaxOT = 50 ['°C_abs'];
                        attribute :>> MaxNOT = 50 ['°C_abs'];
                    }
                    #cDFProduct part <A_Test_Port> 'A Test_Ports' [2] {
                        attribute :>> mass = 0.070 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = 0 ['°C_abs'];
                        attribute :>> MinOT = 0 ['°C_abs'];
                        attribute :>> MaxOT = 50 ['°C_abs'];
                        attribute :>> MaxNOT = 50 ['°C_abs'];
                    }
                    #cDFProduct part <A_Tk_CPROP> 'A Tank CPROP' [2] {
                        attribute :>> mass = 5.900 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = -48 ['°C_abs'];
                        attribute :>> MinOT = -48 ['°C_abs'];
                        attribute :>> MaxOT = 200 ['°C_abs'];
                        attribute :>> MaxNOT = 200 ['°C_abs'];
                    }
                    #cDFProduct part <A_Thr_5N> 'A Thruster_MONARC 5N' [8] {
                        attribute :>> mass = 0.490 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 4.1 [W];
                        attribute :>> P_on = 18 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::external;
                        attribute :>> redundancyConceptK = 4;
                        attribute :>> redundancyConceptN = 8;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = 0.475;
                        attribute :>> DC_SAFE = 0.1;
                        attribute :>> DC_STBY = 0.01;
                        attribute :>> DC_COM = 0.01;
                        attribute :>> DC_EPTH = 0.0002;
                        attribute :>> DC_SCI = -1;
                        attribute :>> MinNOT = -48 ['°C_abs'];
                        attribute :>> MinOT = -48 ['°C_abs'];
                        attribute :>> MaxOT = 200 ['°C_abs'];
                        attribute :>> MaxNOT = 200 ['°C_abs'];
                    }
                }
                #cDFProduct part <EPROP> 'Electrical propulsion' {
                    #cDFProduct part <A_PPU> 'A Power Processing Unit' {
                        attribute :>> mass = 10.660 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 1252 [W];
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = -1;
                        attribute :>> DC_SAFE = -1;
                        attribute :>> DC_STBY = -1;
                        attribute :>> DC_COM = -1;
                        attribute :>> DC_EPTH = 0.64;
                        attribute :>> DC_SCI = -1;
                        attribute :>> MinNOT = -45 ['°C_abs'];
                        attribute :>> MinOT = -25 ['°C_abs'];
                        attribute :>> MaxOT = 70 ['°C_abs'];
                        attribute :>> MaxNOT = 70 ['°C_abs'];
                    }
                    #cDFProduct part <A_Thruster_PPS1350> 'A Thruster PPS1350' {
                        attribute :>> mass = 4.350 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = -48 ['°C_abs'];
                        attribute :>> MinOT = -48 ['°C_abs'];
                        attribute :>> MaxOT = 200 ['°C_abs'];
                        attribute :>> MaxNOT = 200 ['°C_abs'];
                    }
                    #cDFProduct part <A_BPRU> 'A BPRU' {
                        attribute :>> mass = 2.750 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = -30 ['°C_abs'];
                        attribute :>> MinOT = 20 ['°C_abs'];
                        attribute :>> MaxOT = 50 ['°C_abs'];
                        attribute :>> MaxNOT = 50 ['°C_abs'];
                    }
                    #cDFProduct part <A_FU> 'A FU' {
                        attribute :>> mass = 0.675 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = -45 ['°C_abs'];
                        attribute :>> MinOT = -40 ['°C_abs'];
                        attribute :>> MaxOT = 70 ['°C_abs'];
                        attribute :>> MaxNOT = 70 ['°C_abs'];
                    }
                    #cDFProduct part <A_Miscellaneous> 'A Miscellaneous' {
                        attribute :>> mass = 3.500 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = -30 ['°C_abs'];
                        attribute :>> MinOT = -30 ['°C_abs'];
                        attribute :>> MaxOT = 50 ['°C_abs'];
                        attribute :>> MaxNOT = 50 ['°C_abs'];
                    }
                    #cDFProduct part <A_PRE_Card> 'A PRE Card' {
                        attribute :>> mass = 1.270 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = -40 ['°C_abs'];
                        attribute :>> MinOT = -20 ['°C_abs'];
                        attribute :>> MaxOT = 70 ['°C_abs'];
                        attribute :>> MaxNOT = 70 ['°C_abs'];
                    }
                    #cDFProduct part <A_XFC> 'A XFC' {
                        attribute :>> mass = 0.820 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = -15 ['°C_abs'];
                        attribute :>> MinOT = -15 ['°C_abs'];
                        attribute :>> MaxOT = 95 ['°C_abs'];
                        attribute :>> MaxNOT = 95 ['°C_abs'];
                    }
                    #cDFProduct part <A_Prop_Tank> 'A Propellant Tank' [2] {
                        attribute :>> mass = 6.350 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = -20 ['°C_abs'];
                        attribute :>> MinOT = -20 ['°C_abs'];
                        attribute :>> MaxOT = 50 ['°C_abs'];
                        attribute :>> MaxNOT = 50 ['°C_abs'];
                    }
                }
                #cDFProduct part <TC> 'Thermal control' {
                    #cDFProduct part <A_TC_FILLER> 'A TC Thermal Filler' {
                        attribute :>> mass = 0.250 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_TC_HEATER> 'A TC Heater' {
                        attribute :>> mass = 0.500 [kg];
                        attribute :>> MM = 0.2;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 115 [W];
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = -1;
                        attribute :>> DC_SAFE = 0.835;
                        attribute :>> DC_STBY = 1;
                        attribute :>> DC_COM = 0.748;
                        attribute :>> DC_EPTH = 0.774;
                        attribute :>> DC_SCI = 0.5305;
                    }
                    #cDFProduct part <A_TC_MLI> 'A TC Multi Layer Insulation' {
                        attribute :>> mass = 5.000 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_TC_PAINT> 'A TC Paint' {
                        attribute :>> mass = 2.000 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_TC_RAD> 'A TC Radiator Panel' {
                        attribute :>> mass = 1.000 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_TC_SO> 'A TC Stand Offs' {
                        attribute :>> mass = 0.050 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_TC_STRAP> 'A TC Thermal Strap' {
                        attribute :>> mass = 0.500 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_TC_T_SENS> 'A TC Temperature Sensor' {
                        attribute :>> mass = 0.500 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_TC_HP> 'A TC Heat Pipes' {
                        attribute :>> mass = 1.000 [kg];
                        attribute :>> MM = 0.2;
                    }
                }
                #cDFProduct part <STR> Structures {
                    #cDFProduct part <A_Misc_STR> 'A Miscellaneous STR' {
                        attribute :>> mass = 5.000 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_RCS_Structure> 'A RCS Suport' {
                        attribute :>> mass = 0.770 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_SecondarySTR> 'A Secondary Structure' {
                        attribute :>> mass = 10.000 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_Inserts> 'A Inserts' {
                        attribute :>> mass = 4.000 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_ShearW> 'A ShearWebs' {
                        attribute :>> mass = 3.810 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_SA_yoke_1> 'A Solar Array Yoke #1' [2] {
                        attribute :>> mass = 1.000 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_Baseplate> 'Baseplate' {
                        attribute :>> mass = 28.880 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_ExtPanels_1> 'A Closure Panels #1' {
                        attribute :>> mass = 19.606 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_ShieldingPanels_1> 'A ShieldingPanels #1' {
                        attribute :>> mass = 59.610 [kg];
                        attribute :>> MM = 0.2;
                    }
                    #cDFProduct part <A_PL_Panel> 'Payload Support Panel' {
                        attribute :>> mass = 3.000 [kg];
                        attribute :>> MM = 0.2;
                    }
                }
                #cDFProduct part <MEC> Mechanisms {
                    #cDFProduct part <A_APM_HDRM_APME> 'A Antenna Pointing Mechanisms Subsystem with Driver and HDRM' {
                        attribute :>> mass = 13.100 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> P_stby = 0 [W];
                        attribute :>> P_on = 18 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::internal;
                        attribute :>> redundancyConceptK = 1;
                        attribute :>> redundancyConceptN = 2;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = -1;
                        attribute :>> DC_SAFE = -1;
                        attribute :>> DC_STBY = -1;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = 0;
                        attribute :>> DC_SCI = -1;
                        attribute :>> MinNOT = -45 ['°C_abs'];
                        attribute :>> MinOT = -45 ['°C_abs'];
                        attribute :>> MaxOT = 145 ['°C_abs'];
                        attribute :>> MaxNOT = 145 ['°C_abs'];
                    }
                    #cDFProduct part <A_SA_HDRM> 'A SA HDRM' [8] {
                        attribute :>> mass = 0.500 [kg];
                        attribute :>> MM = 0.1;
                        attribute :>> MinNOT = -120 ['°C_abs'];
                        attribute :>> MinOT = -120 ['°C_abs'];
                        attribute :>> MaxOT = 120 ['°C_abs'];
                        attribute :>> MaxNOT = 120 ['°C_abs'];
                    }
                    #cDFProduct part <A_SADM> 'A SADM' [2] {
                        attribute :>> mass = 4.000 [kg];
                        attribute :>> MM = 0.1;
                        attribute :>> P_stby = 1 [W];
                        attribute :>> P_on = 15 [W];
                        attribute :>> redundancyConceptScheme = RedundancyConceptSchemes::passive;
                        attribute :>> redundancyConceptType = RedundancyConceptTypes::internal;
                        attribute :>> redundancyConceptK = 1;
                        attribute :>> redundancyConceptN = 2;
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = 0.07;
                        attribute :>> DC_SAFE = 0.1;
                        attribute :>> DC_STBY = 0;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = 0.1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -120 ['°C_abs'];
                        attribute :>> MinOT = -120 ['°C_abs'];
                        attribute :>> MaxOT = 120 ['°C_abs'];
                        attribute :>> MaxNOT = 120 ['°C_abs'];
                    }
                    #cDFProduct part <A_SADE> 'A SA drive electronics' {
                        attribute :>> mass = 3.000 [kg];
                        attribute :>> MM = 0.1;
                        attribute :>> P_stby = 1 [W];
                        attribute :>> P_on = 5 [W];
                        attribute :>> DC_LAU = -1;
                        attribute :>> DC_SUN = 0.5;
                        attribute :>> DC_SAFE = 1;
                        attribute :>> DC_STBY = 0;
                        attribute :>> DC_COM = 1;
                        attribute :>> DC_EPTH = 1;
                        attribute :>> DC_SCI = 1;
                        attribute :>> MinNOT = -50 ['°C_abs'];
                        attribute :>> MinOT = -50 ['°C_abs'];
                        attribute :>> MaxOT = 40 ['°C_abs'];
                        attribute :>> MaxNOT = 40 ['°C_abs'];
                    }
                    #cDFProduct part <A_Clamp_Band> 'A Clamp Band Ejection System' {
                        attribute :>> mass = 3.000 [kg];
                        attribute :>> MM = 0.05;
                        attribute :>> MinNOT = -40 ['°C_abs'];
                        attribute :>> MinOT = -40 ['°C_abs'];
                        attribute :>> MaxOT = 145 ['°C_abs'];
                        attribute :>> MaxNOT = 145 ['°C_abs'];
                    }
                }
                #cDFProduct part Harness {
                    attribute :>> totalMass = 26.00 [kg];
                }
                #cDFProduct part Propellant {
                    #cDFProduct part <CPROP_Fuel> 'CPROP Fuel' {
                        attribute :>> mass = 47.47 [kg];
                        attribute margin redefines MM = 0.02;
                    }
                    #cDFProduct part <CPROP_Pressurant> 'CPROP Pressurant' {
                        attribute :>> mass = 0.80 [kg];
                        attribute margin redefines MM = 0.02;
                    }
                    #cDFProduct part <EPROP_Fuel> 'EPROP Fuel' {
                        attribute :>> mass = 90.06 [kg];
                        attribute margin redefines MM = 0.02;
                    }
                }
                #cDFProduct part <A_tank_shields> 'A_tank_shields' [4] {
                        attribute :>> mass = 0.800 [kg];
                        attribute :>> MM = 0.2;
                }            
            }
        }
        attribute wetMass redefines totalMass = 796.42 [kg]; // /!\ wetMass=dryMass*(1+systemMargin)+massPropellant // value is presented here but can be calculated
        attribute systemMargin = 0.2; // both for mass and power (except for EPTH mode, see report)
        attribute dryMass : MassValue;

        exhibit 'System modes';
    }
}