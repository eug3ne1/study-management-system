package org.study.system.deepdivestudy.model.task;

import jakarta.persistence.*;
import lombok.Data;

import org.study.system.deepdivestudy.model.testing.Test;
import org.study.system.deepdivestudy.model.users.Student;

@Entity
@Data
@Table(name = "task_grades")
public class TaskGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}
