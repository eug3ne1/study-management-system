package org.study.system.deepdivestudy.service;

import org.study.system.deepdivestudy.dto.UniversityDTO;
import org.study.system.deepdivestudy.entity.University;

import java.util.Optional;

public interface UniversityService {
    Optional<University> getById(Long id);

    Iterable<University> getAll();

    void deleteById(Long id);

    void createUni(UniversityDTO universityDTO);
}
