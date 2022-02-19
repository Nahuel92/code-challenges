import calculator.CommonLastNames;
import calculator.CommonFirstNames;
import file.Reader;
import generator.NewFullNames;
import generator.Summary;

import java.util.List;
import java.util.Map;

// Implementation comments:
// 1. Class left in the default package on purpose to help
//      you to quickly identify the entrypoint class and method for this project.
// 2. static methods were used because you didn't allow me to use
//      a framework like Spring to inject dependencies, and I am lazy
//      to manually create and manage objects.
public class Main {
    public static void main(final String[] args) {
        // TODO: Put your file under src/main/resources and then modify this
        // variable to hold your file's name.
        final var fileName = "main/resources/coding-test-data.txt";

        // TODO: Modify this variable to hold the amount of names to be generated.
        final var n = 25;
        processFile(fileName, n);
    }

    private static void processFile(final String fileName, final Integer n) {
        final var firstNamesGroupedByLastName = Reader.read(fileName);
        printSummary(firstNamesGroupedByLastName);
        printMostCommonLastNames(firstNamesGroupedByLastName);
        printMostCommonFirstNames(firstNamesGroupedByLastName);
        printListOfModifiedNames(firstNamesGroupedByLastName, n);
    }

    private static void printSummary(final Map<String, List<String>> firstNamesGroupedByLastName) {
        final var summary = Summary.generate(firstNamesGroupedByLastName);
        System.out.println("1. The names cardinality for full, last, and first names:");
        System.out.println("Full names: " + summary.get("fullNames"));
        System.out.println("Last names: " + summary.get("lastNames"));
        System.out.println("First names: " + summary.get("firstNames"));
        System.out.println();
    }

    private static void printMostCommonLastNames(final Map<String, List<String>> firstNamesGroupedByLastName) {
        System.out.println("2. The most common last names are:");
        CommonLastNames.calculate(firstNamesGroupedByLastName)
                .forEachOrdered(e -> System.out.println(e.getKey() + ": " + e.getValue().size()));
        System.out.println();
    }

    private static void printMostCommonFirstNames(final Map<String, List<String>> firstNamesGroupedByLastName) {
        System.out.println("3. The most common first names are:");
        CommonFirstNames.calculate(firstNamesGroupedByLastName)
                .forEachOrdered(e -> System.out.println(e.getKey() + ": " + e.getValue()));
        System.out.println();
    }

    private static void printListOfModifiedNames(final Map<String, List<String>> firstNamesGroupedByLastName, final Integer n) {
        System.out.println("4. List of " + n + " Modified Names:");
        NewFullNames.generate(firstNamesGroupedByLastName, n).forEach(e -> System.out.println(e.getKey() + ", " + e.getValue()));
        System.out.println();
    }
}
