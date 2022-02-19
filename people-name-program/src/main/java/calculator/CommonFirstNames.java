package calculator;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonFirstNames {
    public static Stream<Map.Entry<String, Long>> calculate(final Map<String, List<String>> firstNamesGroupedByLastName) {
        return firstNamesGroupedByLastName.values()
                .parallelStream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10);
    }
}
