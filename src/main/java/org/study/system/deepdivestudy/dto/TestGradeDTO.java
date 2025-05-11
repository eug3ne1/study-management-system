package org.study.system.deepdivestudy.dto;

import lombok.Data;
import org.study.system.deepdivestudy.entity.testing.Test;
import org.study.system.deepdivestudy.entity.users.Student;

@Data
public class TestGradeDTO {

    private Double grade;
    private Student student;
    private Test test;

}
