package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {

    private static ObjectMapper om = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: run with parameter path pointing to kibana csv");
            return;
        }
        try (Reader inputReader = new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8)) {
            CsvParserSettings csvParserSettings = new CsvParserSettings();
            csvParserSettings.setHeaderExtractionEnabled(true);
            CsvParser parser = new CsvParser(csvParserSettings);
            parser.beginParsing(inputReader);
            Record record;
            List<String> result = new ArrayList<>();
            while ((record = parser.parseNextRecord()) != null) {
                String csvMessage = record.getString("message");
                Map<?, ?> map;
                try {
                    map = om.readValue(csvMessage, Map.class);
                } catch (Exception e) {
//                    result.add("2099-01-01T01:01:00.000Z" + csvMessage);
                    continue;
                }
                String timestamp = map.get("@timestamp").toString();
                String logMessage = map.get("message").toString();
                String loggerName = map.get("logger_name").toString();
                String threadName = map.get("thread_name").toString();
                String level = map.get("level").toString();

                result.add(0, StringUtils.rightPad(timestamp, 24) + " "
                    + StringUtils.rightPad(level, 5) + " "
                        + "[" + threadName + "] "
                        + StringUtils.abbreviateMiddle(loggerName, "...", 36) + " "
                        + logMessage);
            }
//            Collections.sort(result);
            result.forEach(l -> System.out.println(l));
        }
    }
}
