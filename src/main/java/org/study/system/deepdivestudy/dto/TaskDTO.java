package org.study.system.deepdivestudy.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {

    private Long courseId;
    private String title;
    private String description;
    private Double maxGrade;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
