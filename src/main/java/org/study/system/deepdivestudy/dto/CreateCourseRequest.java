package org.study.system.deepdivestudy.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateCourseRequest {
    private Long uniId;
    private String name;
    private String description;
    private List<Long> tagIds;
}
