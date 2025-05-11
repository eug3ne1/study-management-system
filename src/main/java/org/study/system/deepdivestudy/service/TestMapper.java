package org.study.system.deepdivestudy.service;

import org.springframework.stereotype.Component;
import org.study.system.deepdivestudy.dto.TestResponse;
import org.study.system.deepdivestudy.entity.testing.Test;
import java.util.stream.Collectors;

@Component
public class TestMapper {

    public static TestResponse toDto(Test test) {
        TestResponse dto = new TestResponse();
        dto.setId(test.getId());
        dto.setName(test.getName());
        dto.setDescription(test.getDescription());
        dto.setStartTime(test.getStartTime());
        dto.setEndTime(test.getEndTime());
        dto.setMaxAttempts(test.getMaxAttempts());

        if (test.getLecture() != null) {
            dto.setLectureId(test.getLecture().getId());
        }

        if (test.getQuestions() != null) {
            dto.setQuestions(test.getQuestions().stream()
                    .map(TestService::mapToQuestionResponse)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

}

