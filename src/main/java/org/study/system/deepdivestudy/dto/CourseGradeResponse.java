package org.study.system.deepdivestudy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CourseGradeResponse {
    private String courseName;
    private String testName;
    private Double score;
    private Double maxScore;
    private LocalDateTime testDate;
}
