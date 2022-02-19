package generator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Summary {
    public static Map<String, Long> generate(final Map<String, List<String>> firstNamesGroupedByLastName) {
        final var fullNames = firstNamesGroupedByLastName.values()
                .parallelStream()
                .mapToLong(Collection::size)
                .sum();
        final long lastNames = firstNamesGroupedByLastName.size();
        final var firstNames = firstNamesGroupedByLastName.values()
                .parallelStream()
                .flatMap(Collection::parallelStream)
                .distinct()
                .count();

        return Map.ofEntries(
                Map.entry("fullNames", fullNames),
                Map.entry("lastNames", lastNames),
                Map.entry("firstNames", firstNames)
        );
    }
}
