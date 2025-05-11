package org.study.system.deepdivestudy.dto;

import lombok.Data;
import org.study.system.deepdivestudy.entity.task.Task;
import org.study.system.deepdivestudy.entity.users.Student;

@Data
public class TaskGradeDTO {

    private Double grade;
    private Student student;
    private Task task;

}