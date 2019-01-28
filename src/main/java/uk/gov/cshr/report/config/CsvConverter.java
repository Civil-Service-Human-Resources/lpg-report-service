package uk.gov.cshr.report.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import uk.gov.cshr.report.exception.CsvUnmarshallingException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CsvConverter<T> extends AbstractHttpMessageConverter<T> {

        private final ObjectMapper objectMapper;

        CsvConverter(ObjectMapper objectMapper) {
            super(new MediaType("text", "csv", Charset.forName("UTF-8")));
            this.objectMapper = objectMapper;
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return true;
        }

        @Override
        protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) {
            return null;
        }

        @Override
        protected void writeInternal(T object, HttpOutputMessage outputMessage) {
            try {
                ObjectWriter objectWriter = getCsvWriter(object);
                try (PrintWriter outputWriter = new PrintWriter(outputMessage.getBody())) {
                        outputWriter.write(objectWriter.writeValueAsString(object));
                }
            } catch (IOException e) {
                throw new CsvUnmarshallingException("Unable to write CSV output", e);
            }

        }

        private ObjectWriter getCsvWriter(T object) {
            Set<String> fields = getUniqueFieldNames(object);
            CsvSchema.Builder schemaBuilder = CsvSchema.builder().setUseHeader(true);

            for (String field : fields) {
                schemaBuilder.addColumn(field);
            }
            return new CsvMapper().writerFor(List.class).with(schemaBuilder.build());
        }

        private Set<String> getUniqueFieldNames(T object) {
            try {
                JsonNode root = objectMapper.readTree(objectMapper.writeValueAsString(object));
                Set<String> uniqueFieldNames = new LinkedHashSet<>();

                root.forEach(element -> {
                    Iterator<String> it = element.fieldNames();
                    while (it.hasNext()) {
                        String field = it.next();
                        uniqueFieldNames.add(field);
                    }
                });
                return uniqueFieldNames;
            } catch (IOException e) {
                throw new CsvUnmarshallingException("Error reading object fields", e);
            }
        }
    }