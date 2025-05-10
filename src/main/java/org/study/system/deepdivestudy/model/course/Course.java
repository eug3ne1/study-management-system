package org.study.system.deepdivestudy.model.course;


import jakarta.persistence.*;
import lombok.Data;
import org.study.system.deepdivestudy.model.Tag;
import org.study.system.deepdivestudy.model.task.Task;
import org.study.system.deepdivestudy.model.testing.Test;
import org.study.system.deepdivestudy.model.users.Student;
import org.study.system.deepdivestudy.model.users.Teacher;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany
    @JoinTable(
            name = "course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lecture> lectures = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Test> tests = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany
    private List<Tag> tags = new ArrayList<>();

    public void setTags(List<Tag> tags) {
        this.tags = new ArrayList<>(tags);
    }

}
