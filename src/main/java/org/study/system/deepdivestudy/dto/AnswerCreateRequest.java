package org.study.system.deepdivestudy.dto;

import lombok.Data;

@Data
public class AnswerCreateRequest {
    private String text;
    private Boolean isCorrect;
}
