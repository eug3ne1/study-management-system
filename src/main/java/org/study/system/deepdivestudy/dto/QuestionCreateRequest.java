package org.study.system.deepdivestudy.dto;

import lombok.Data;
import org.study.system.deepdivestudy.entity.testing.QuestionType;

import java.util.List;

@Data
public class QuestionCreateRequest {
    private String text;
    private String content;
    private Double value;
    private QuestionType type;
    private List<AnswerCreateRequest> answers;
}

