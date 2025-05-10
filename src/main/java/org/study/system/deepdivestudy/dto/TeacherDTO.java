package org.study.system.deepdivestudy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherDTO {
    Long id;
    String firstName;
    String lastName;
    String middleName;
}
