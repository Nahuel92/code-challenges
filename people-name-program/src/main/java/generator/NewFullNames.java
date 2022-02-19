package generator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NewFullNames {
    public static List<Map.Entry<String, String>> generate(final Map<String, List<String>> firstNamesGroupedByLastName, final Integer n) {
        final Map<String, String> uniqueFullNames = getUniqueFullNames(firstNamesGroupedByLastName, n);
        final var firstNames = uniqueFullNames.values();

        var newFullNames = getNewFullNames(uniqueFullNames, firstNames);
        while (newFullNames.stream().anyMatch(e -> e.getValue().isBlank() || uniqueFullNames.entrySet().contains(e))) {
            newFullNames = getNewFullNames(uniqueFullNames, firstNames);
        }
        return newFullNames;
    }

    private static Map<String, String> getUniqueFullNames(final Map<String, List<String>> firstNamesGroupedByLastName, final Integer n) {
        final Set<String> firstNames = ConcurrentHashMap.newKeySet();
        return firstNamesGroupedByLastName.entrySet()
                .parallelStream()
                .map(e -> Map.entry(
                                e.getKey(),
                                e.getValue()
                                        .parallelStream()
                                        .filter(firstNames::add)
                                        .findFirst()
                                        .orElse("")
                        )
                )
                .filter(e -> !e.getValue().isBlank())
                .limit(n)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static List<Map.Entry<String, String>> getNewFullNames(final Map<String, String> uniqueFullNames, final Collection<String> firstNames) {
        final var mutableFirstNames = new ArrayList<>(firstNames);
        Collections.shuffle(mutableFirstNames);
        return uniqueFullNames.keySet()
                .stream()
                .map(ln -> {
                    final var randomName = mutableFirstNames.stream()
                            .filter(e -> !e.equals(uniqueFullNames.get(ln)))
                            .findFirst()
                            .orElse("");
                    mutableFirstNames.remove(randomName);
                    return Map.entry(ln, randomName);
                })
                .toList();
    }
}
