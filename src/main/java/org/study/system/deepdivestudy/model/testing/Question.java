package org.study.system.deepdivestudy.model.testing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private Double value;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    @JsonIgnore
    private Test test;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

}
