package org.study.system.deepdivestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.system.deepdivestudy.entity.course.FilePath;

public interface FilePathRepository extends JpaRepository<FilePath, Long> {
}
