package uk.gov.cshr.report.service.util;

import java.util.List;
import java.util.stream.IntStream;

public class HttpUtils {

    public static List<List<String>> batchList(Integer batchSize, List<String> list) {
        return IntStream.iterate(0, i -> i + batchSize)
                .limit((int) Math.ceil((double) list.size() / batchSize))
                .mapToObj(i -> list.subList(i, Math.min(i + batchSize, list.size())))
                .toList();
    }

}
