package org.study.system.deepdivestudy.dto;

import lombok.Data;
import java.util.List;

@Data
public class TestSubmissionRequest {
    private Long testId;
    private List<SubmittedAnswer> answers;

    @Data
    public static class SubmittedAnswer {
        private Long questionId;
        private List<Long> selectedAnswerIds;
    }
}

