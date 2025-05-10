package org.study.system.deepdivestudy.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EmptyTestCreateRequest {
    private String name;
    private String description;
    private Integer maxAttempts;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long courseId;
    private Long lectureId;
}

