package CaseReferenceSpecializations {
    doc /* Covers multiplicity and reference specializations in redefined case members and messages. */

    use case original {
        subject producer [1] {
            event occurrence submitted;
            event occurrence received;
        }
        actor consumer [1] {
            event occurrence accepted;
            event occurrence replied;
        }
        message request from producer.submitted to consumer.accepted;
        then message response from consumer.replied to producer.received;
    }

    use case redefines original {
        subject redefines producer [1] references realizedProducer;
        actor redefines consumer [1] references realizedConsumer;
        message redefines request references realizedExchange.request;
        then message redefines response references realizedExchange.response;
    }
}
