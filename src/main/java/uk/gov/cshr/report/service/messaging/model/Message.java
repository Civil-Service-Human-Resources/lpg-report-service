package uk.gov.cshr.report.service.messaging.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Message<T> implements Serializable {
    private String messageId;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime messageTimestamp;
    private T metadata;
}
