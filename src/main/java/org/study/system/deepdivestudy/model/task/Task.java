package org.study.system.deepdivestudy.model.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.study.system.deepdivestudy.model.course.Course;
import org.study.system.deepdivestudy.model.course.FilePath;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FilePath> filesUrl =new ArrayList<>();

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
