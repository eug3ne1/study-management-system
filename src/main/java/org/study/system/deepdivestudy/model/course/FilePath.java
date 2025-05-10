package org.study.system.deepdivestudy.model.course;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FilePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;

}
