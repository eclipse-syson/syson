library package LCA {
    doc
    /* defines the elements corresponding to LCA Objects
     * LCA                     | SysML 2
     * -------------------------------------              
     * unit group              | MeasurementUnit 
     * flow property           | attribute definition specializing ScalarQuantityValue
     * flow                    | attribute being typed by a flow property 
     * exchange, input, output | attribute subsetting a flow
     */
    private import ScalarValues::String;
    private import Metaobjects::SemanticMetadata;
    private import Quantities::ScalarQuantityValue;
    attribute lcaFlows : ScalarQuantityValue;
    attribute lcaExchanges :> lcaFlows;
    metadata def ExternalRef {
        doc /* this declares an element as a reference to an external object, identified by a uuid */
        attribute uuid : String;
    }
    metadata def <lcaflow> 'LCA-Flow' :> SemanticMetadata, ExternalRef {
        doc
        /* this declares an attribute as an lca-flow, to be subsetted by 
         * exchange-attributes of parts and part definitions.
         * Must be defined at the package level (i.e. as attribute of Anything) */
        :> annotatedElement : SysML::Systems::AttributeUsage;
        :> baseType = lcaFlows meta SysML::Systems::AttributeUsage;
    }
    metadata def <exchg> 'LCA-Exchange' :> SemanticMetadata {
        doc
        /* this declares an attribute as quantifing an exchange with the 
         * environment during the production of the element. 
         * A positive value is output, a negative value is input.
         * Must be owned by a part or part def.
         * Must subset an lcaflow. (I don't know how to enforce this)*/
        :> annotatedElement : SysML::Systems::AttributeUsage;
        :> baseType = lcaExchanges meta SysML::Systems::AttributeUsage;
    }
    metadata def <lca> 'LCA-Part' :> ExternalRef {
        doc
        /* this declares a part (definition) as something that will have a process in an lca-tool, 
         * whose impact can be analyzed. 
         * In a first step, the process can be generated in the lca-tool and the id will be recorded. 
         * The result of the calculation will then be added as an attribute, so that the impact can 
         * guide the system design activities. */
        :> annotatedElement : SysML::Systems::PartUsage;
        :> annotatedElement : SysML::Systems::PartDefinition;
    }
    metadata def <impact> 'LCA-Impact' {
        doc /* this marks the attributes that contain the impacts calculated by the life cycle assessement */
    }
}