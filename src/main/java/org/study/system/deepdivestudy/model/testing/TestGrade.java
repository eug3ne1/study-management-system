package org.study.system.deepdivestudy.model.testing;

import jakarta.persistence.*;
import lombok.Data;
import org.study.system.deepdivestudy.model.users.Student;

@Entity
@Data
@Table(name = "test_grades")
public class TestGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
}
