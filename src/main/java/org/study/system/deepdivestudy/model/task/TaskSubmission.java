package org.study.system.deepdivestudy.model.task;

import jakarta.persistence.*;
import lombok.Data;
import org.study.system.deepdivestudy.model.course.FilePath;
import org.study.system.deepdivestudy.model.users.Student;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "task_submission")
public class TaskSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private Double grade;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FilePath> filesUrl =new ArrayList<>();

}
