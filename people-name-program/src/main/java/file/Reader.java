package file;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Reader {
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z]+,\\s[a-zA-Z]+\\s");

    public static Map<String, List<String>> read(final String fileName) {
        final var filePath = Path.of("src", fileName);
        try (final var content = Files.lines(filePath)) {
            return content
                    .parallel()
                    .filter(PATTERN.asPredicate())
                    .map(e -> e.replaceAll("\\s\\W.+", ""))
                    .map(e -> Map.entry(e.substring(0, e.indexOf(",")), e.substring(e.indexOf(" ") + 1)))
                    .collect(
                            Collectors.groupingBy(
                                    Map.Entry::getKey,
                                    LinkedHashMap::new,
                                    Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                            )
                    );
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
