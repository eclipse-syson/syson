double checkCoverage(String module) {
  double coverage = 0.0;

  var totalStart = "<td>Total</td>";
  var resultStart = "<td class=\"ctr2\">";
  var resultEnd = "%</td>";

  var modulePath = module;
  if (!module.isBlank()) {
    modulePath += "/";
  }
  try {
    var path = Paths.get("backend/releng/syson-test-coverage/target/site/jacoco-aggregate/" + modulePath + "index.html");
    var optionalLine = Files.readAllLines(path).stream().filter(line -> line.contains(totalStart)).findFirst();
    if (optionalLine.isPresent()) {
      var line = optionalLine.get();
      var totalIndex = line.indexOf(totalStart);
      var startIndex = line.indexOf(resultStart, totalIndex);
      var endIndex = line.indexOf(resultEnd, startIndex);
      var result = line.substring(startIndex + resultStart.length(), endIndex);
      var resultToParse = result.replaceAll("\\p{Z}","").trim();

      coverage = Double.parseDouble(resultToParse);
    }
  } catch (IOException exception) {
    System.out.println(exception.getMessage());
  }
  return coverage;
}

record ModuleCoverage(String moduleName, double expectedCoverage) {}
double expectedGlobalCoverage = 59.0;
var moduleCoverageData = List.of(
  new ModuleCoverage("syson-sysml-metamodel", 67.0),
  new ModuleCoverage("syson-sysml-metamodel-edit", 9.0),
  new ModuleCoverage("syson-siriusweb-customnodes-metamodel", 49.0),
  new ModuleCoverage("syson-siriusweb-customnodes-metamodel-edit", 0.0),
  new ModuleCoverage("syson-direct-edit-grammar", 64.0),
  new ModuleCoverage("syson-services", 63.0),
  new ModuleCoverage("syson-sysml-rest-api-services", 97.0),
  new ModuleCoverage("syson-sysml-import", 77.0),
  new ModuleCoverage("syson-sysml-export", 88.0),
  new ModuleCoverage("syson-diagram-common-view", 80.0),
  new ModuleCoverage("syson-diagram-general-view", 99.0),
  new ModuleCoverage("syson-diagram-actionflow-view", 96.0),
  new ModuleCoverage("syson-diagram-statetransition-view", 94.0),
  new ModuleCoverage("syson-diagram-interconnection-view", 95.0),
  new ModuleCoverage("syson-tree-explorer-view", 81.0),
  new ModuleCoverage("syson-application-configuration", 42.0),
  new ModuleCoverage("syson-application", 37.0)
);

void display(String module, double coverage, double expected) {
  var status = "";
  if (coverage == expected) {
    status = "OK";
  } else if (coverage < expected) {
    status = "more tests are required";
  } else if (coverage > expected) {
    status = "update expected code coverage";
  }

  System.out.format("%-60s%10.2f%10.2f%35s", module, coverage, expected, status);
  System.out.println();
}

// Log the most important code coverage metrics
System.out.println("####################################################################################################################");
System.out.println("#############################################                          #############################################");
System.out.println("#############################################   Code Coverage Results  #############################################");
System.out.println("#############################################                          #############################################");
System.out.println("####################################################################################################################");
System.out.println();
System.out.format("%-60s%10s%10s%35s", "Module", "Coverage", "Expected", "Status");
System.out.println();


// Check global code coverage
double global = checkCoverage("");
boolean isValidCoverage = global >= expectedGlobalCoverage;
display("total", global, expectedGlobalCoverage);

for (var moduleCoverage: moduleCoverageData) {
  var coverage = checkCoverage(moduleCoverage.moduleName());
  display(moduleCoverage.moduleName(), coverage, moduleCoverage.expectedCoverage());
  isValidCoverage = isValidCoverage && coverage >= moduleCoverage.expectedCoverage();
}

/exit isValidCoverage ? 0 : 1