package org.study.system.deepdivestudy.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCourseRequest {
    private String name;
    private String description;
    private List<Long> tagIds;
}
