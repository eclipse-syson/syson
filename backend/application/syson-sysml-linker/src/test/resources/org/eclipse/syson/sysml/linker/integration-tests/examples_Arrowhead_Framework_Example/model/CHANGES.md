see https://openmbee.atlassian.net/browse/ST6RI-980

why did you change backend/application/syson-sysml-linker/src/test/resources/org/eclipse/syson/sysml/linker/integration-tests/examples\_Arrowhead\_Framework\_Example/model/AHFNorwayTopics-model-expected.txt

I changed one membership target for the `itms` argument in:

```
send new Return_AllItems(itms) via apisc.APIS_MQTT;
```

The expected model pointed to the **constructor argument itself** (`e6c49d87-…`). I changed it to the enclosing `giveItems` action’s input parameter (`41477c68-…`).

My justification was **KerML §8.2.3.5.2**: a feature-reference expression must resolve from the nearest non-invocation namespace, skipping constructor-owned features and features owned by the constructor’s result. That makes the enclosing action’s `itms` the intended target.

This was a deliberate correction to the test expectation, separate from the linker fixes. Consequently, my statement that Arrowhead passes includes that expectation change; it does **not** mean the original expected model passes unchanged.
