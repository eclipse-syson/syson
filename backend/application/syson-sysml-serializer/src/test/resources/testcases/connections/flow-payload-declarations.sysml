package FlowPayloadDeclarations {
    flow redefined of redefines payloads from source to destination;
    flow combined of data : Data :>> payloads [0..*] ordered from source to destination;
    flow multiTyped of data : Data, Signal from source to destination;
    flow defaulted of data : Data default = initialData from source to destination;
    flow chainTyped of data : sensor.reading [2] from source to destination;
    flow lateOrdered of : Data [1] ordered from source to destination;
    flow earlyNonunique of data nonunique : Data from source to destination;
    flow shortNamed of <d> : Data from source to destination;
    flow of subsets payloads from source to destination;
    succession flow sequenced of subsets payloads from source to destination;
    flow typedWithoutEnds of Data;
    flow namedWithoutEnds : BaseFlow of data : Data;
    flow bodyWithoutEnds of subsets payloads {
        attribute delay;
    }
    succession flow sequencedWithoutEnds of Data [1];
    message notified of subsets payloads from source to destination;
    message namedPayload of data subsets payloads;
}
