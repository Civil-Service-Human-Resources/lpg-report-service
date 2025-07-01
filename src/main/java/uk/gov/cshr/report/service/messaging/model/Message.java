package uk.gov.cshr.report.service.messaging.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class Message<T> implements Serializable {
    private final String messageId;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime messageTimestamp;
    private final T metadata;
}
