package org.study.system.deepdivestudy.model.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.study.system.deepdivestudy.model.testing.Test;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long id;

    private String title;
    @Lob
    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FilePath> filesUrl =new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    @OneToMany(mappedBy = "lecture")
    @JsonIgnore
    private List<Test> tests = new ArrayList<>();
}
