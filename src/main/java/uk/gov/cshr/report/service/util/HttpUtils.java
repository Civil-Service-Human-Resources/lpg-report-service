package uk.gov.cshr.report.service.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class HttpUtils {

    public static List<List<String>> batchList(Integer batchSize, Collection<String> collection) {
        List<String> list = collection.stream().toList();
        return IntStream.iterate(0, i -> i + batchSize)
                .limit((int) Math.ceil((double) list.size() / batchSize))
                .mapToObj(i -> list.subList(i, Math.min(i + batchSize, list.size())))
                .toList();
    }

}
