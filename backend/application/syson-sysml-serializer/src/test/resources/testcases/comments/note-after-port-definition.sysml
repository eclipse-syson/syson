// Covers a note ending the line of a port definition, which owns an implicit conjugated port definition
// (found with the validation case checkPortDefinitionSpecialization-trigger-in-case-of-port-definition)
package NoteAfterPortDefinition {
    port def Plug; // note after a port definition
    part def Other;
}
