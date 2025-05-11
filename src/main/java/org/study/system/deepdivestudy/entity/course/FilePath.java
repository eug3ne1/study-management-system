package org.study.system.deepdivestudy.entity.course;

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
