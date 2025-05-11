package org.study.system.deepdivestudy.dto;

import lombok.Data;
import org.study.system.deepdivestudy.entity.testing.QuestionType;

import java.util.List;

@Data
public class QuestionResponse {
    private Long id;
    private String text;
    private String content;
    private Double value;
    private QuestionType type;
    private List<AnswerResponse> answers;

    @Data
    public static class AnswerResponse {
        private Long id;
        private String text;
        private Boolean isCorrect;
    }
}
