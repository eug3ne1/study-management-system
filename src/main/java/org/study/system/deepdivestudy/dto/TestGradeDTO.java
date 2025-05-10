package org.study.system.deepdivestudy.dto;

import lombok.Data;
import org.study.system.deepdivestudy.model.testing.Test;
import org.study.system.deepdivestudy.model.users.Student;

@Data
public class TestGradeDTO {

    private Double grade;
    private Student student;
    private Test test;

}
