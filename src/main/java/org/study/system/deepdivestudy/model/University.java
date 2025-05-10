package org.study.system.deepdivestudy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.study.system.deepdivestudy.model.users.Student;
import org.study.system.deepdivestudy.model.users.Teacher;

import java.util.List;

@Entity
@Data
@Table(name = "universities")
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_id")
    private Long id;

    private String name;

    @Column(name = "country_name")
    private String countryName;

    @ManyToMany
    @JoinTable(
            name = "university_teacher",
            joinColumns = @JoinColumn(name = "university_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    @JsonIgnore
    private List<Teacher> teachers;

    @ManyToMany
    @JoinTable(
            name = "university_students",
            joinColumns = @JoinColumn(name = "university_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonIgnore
    private List<Student> students;


}
