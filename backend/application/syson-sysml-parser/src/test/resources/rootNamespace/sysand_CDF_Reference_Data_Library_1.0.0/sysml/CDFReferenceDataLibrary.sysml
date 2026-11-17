library package CDFReferenceDataLibrary {

    doc /* The project allows to create parts/products with the relevant parameters used in early-phase studies at the European Space Agency (ESA) Concurrent Design Facility (CDF).
         * More information on the CDF at https://www.esa.int/Enabling_Support/Space_Engineering_Technology/CDF.
         * More info on the parameters at https://comet.io.esa.int/content/2-domain-expert/05-add-equipment-from-cat.html#parameters-and-the-reference-data-library-rdl and
         * https://comet.io.esa.int/content/2-domain-expert/06-provide-param-values.html.
         */

    private import Metaobjects::SemanticMetadata;
    private import ScalarValues::*;
    private import SI::*;

    part def CDFProduct {
        // cost, programmatics and risk
        attribute heritage : String;
        attribute heritageDetails : String;
        attribute longLeadItem : String;
        attribute manufacturer : String;
        attribute <MM> maturityMargin : Real;
        attribute modelType : String;
        attribute redundancyConceptScheme : RedundancyConceptSchemes default RedundancyConceptSchemes::none;
        attribute redundancyConceptType : RedundancyConceptTypes;
        attribute redundancyConceptK : Integer;
        attribute redundancyConceptN : Integer;
        attribute supplier : String;
        attribute TRL : Integer;
        // geometry
        attribute diameter : LengthValue;
        attribute height : LengthValue;
        attribute length : LengthValue;
        attribute location : VectorValues::CartesianThreeVectorValue;
        attribute shapeCDF : ShapeTypes; // items in SysML v2 already have a 'shape' attribute, so this attribute has been called 'shapeCDF'
        attribute width : LengthValue;
        // mass
        attribute mass : MassValue; // before maturityMargin
        attribute totalMass : MassValue; // derived
        // power
        attribute <P_on> powerWhileON : PowerValue; // includes maturityMargin
        attribute <P_stby> powerInStandby : PowerValue; // stand-by mode of the equipment // includes maturityMargin
        attribute <DC_LAU> powerDutyCyleLAU : Real;
        attribute <DC_SUN> powerDutyCyleSUN : Real;
        attribute <DC_SAFE> powerDutyCyleSAFE : Real;
        attribute <DC_STBY> powerDutyCycleSTBY : Real; // stand-by mode of the spacecraft
        attribute <DC_COM> powerDutyCycleCOM : Real;
        attribute <DC_EPTH> powerDutyCycleEPTH : Real;
        attribute <DC_SCI> powerDutyCycleSCI : Real;
        attribute powerInLAU : PowerValue; // derived
        attribute powerInSUN : PowerValue; // derived
        attribute powerInSAFE : PowerValue; // derived
        attribute powerInSTBY: PowerValue; // derived // stand-by mode of the spacecraft
        attribute powerInCOM: PowerValue; // derived
        attribute powerInEPTH : PowerValue; // derived
        attribute powerInSCI : PowerValue; // derived
        // thermal
        attribute <MaxNOT> maximumNonOperationalTemperature : TemperatureValue;
        attribute <MaxOT> maximumOperationalTemperature : TemperatureValue;
        attribute <MinOT> minimumOperationalTemperature : TemperatureValue;
        attribute <MinNOT> minimumNonOperationalTemperature : TemperatureValue;
    }

    enum def RedundancyConceptSchemes {
        active;
        passive;
        none;
    }

    enum def RedundancyConceptTypes {
        internal;
        external;
    }

    enum def ShapeTypes {
        box;
        cylinder;
        cone;
        sphere;
        paraboloid;
        triPrism;
        quadPrism;
        cappedCylinder;
        polyPrism;
        tetrahedron;
        wedge;
        capsule;
        triangle;
        cylinderSegment;
        sphericalSegment;
        planarQuadrilateral;
        discSegment;
        paraboloidSegment;
    }


    part cDFProducts : CDFProduct;

    metadata def cDFProduct :> SemanticMetadata {
        :>> baseType = cDFProducts meta SysML::PartUsage;
        :> annotatedElement : SysML::PartUsage;
    }
}