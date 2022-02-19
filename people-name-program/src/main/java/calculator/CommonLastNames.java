package calculator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CommonLastNames {
    public static Stream<Map.Entry<String, List<String>>> calculate(final Map<String, List<String>> firstNamesGroupedByLastName) {
        return firstNamesGroupedByLastName.entrySet()
                .parallelStream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(e -> e.getValue().size())))
                .limit(10);
    }
}
