package org.study.system.deepdivestudy.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class TestResponse {
    private Long id;
    private String name;
    private String description;
    private Integer maxAttempts;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long lectureId;

    private List<QuestionResponse> questions;
}
