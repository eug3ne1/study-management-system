package org.study.system.deepdivestudy.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.study.system.deepdivestudy.model.University;
import org.study.system.deepdivestudy.model.course.Course;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private String firstName;

    private String middleName;

    private String lastName;

    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    private List<Course> enrollments = new ArrayList<>();

    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    private List<University> universities = new ArrayList<>();

}
