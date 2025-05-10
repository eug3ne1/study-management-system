package org.study.system.deepdivestudy.model.testing;

import jakarta.persistence.*;
import lombok.Data;
import org.study.system.deepdivestudy.model.users.Student;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "test_attempts")
public class TestAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Long id;

    private LocalDateTime submittedAt;

    private Double score;

    private int correctAnswers;
    private int totalQuestions;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
}
