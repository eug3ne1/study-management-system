package org.study.system.deepdivestudy.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TestDTO {
    private Long courseId;
    private String name;
    private String description;
    private Integer maxAttempts;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<QuestionCreateRequest> questions;
}
