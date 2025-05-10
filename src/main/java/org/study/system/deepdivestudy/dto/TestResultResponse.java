package org.study.system.deepdivestudy.dto;

import lombok.Data;

@Data
public class TestResultResponse {
    private Long testId;
    private int totalQuestions;
    private int correctAnswers;
    private int attempt;
    private double totalScore;
    private double maxScore;
    private Double grade;
}

