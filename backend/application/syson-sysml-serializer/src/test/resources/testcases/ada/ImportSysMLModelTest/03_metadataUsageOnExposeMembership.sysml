// Extracted from ImportSysMLModelTest#metadataUsageOnExposeMembership
package Test {
     metadata def MyAnnotation;

     item def MyItem;

     view MyView {
         expose MyItem { @MyAnnotation; }
     }

    item def MyItem2;
 }
