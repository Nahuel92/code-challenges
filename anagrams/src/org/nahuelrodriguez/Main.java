package org.nahuelrodriguez;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;

public class Main {
    public static void main(final String... args) {
        final var input = Set.of("cats", "redraw", "tap", "dog", "pat",
                "acts", "drawer", "remote", "reward", "god");

        final var output = input.stream()
                .map(e -> Map.entry(sortWordChars(e), e))
                .collect(
                        groupingBy(Map.Entry::getKey,
                                mapping(Map.Entry::getValue, toSet())
                        )
                );
        System.out.println(output.values());
    }

    private static String sortWordChars(final String word) {
        final var wordCharArray = word.toCharArray();
        Arrays.sort(wordCharArray);
        return String.valueOf(wordCharArray);
    }
}
