package org.study.system.deepdivestudy.service;

import org.springframework.stereotype.Service;

import org.study.system.deepdivestudy.dto.UniversityDTO;
import org.study.system.deepdivestudy.entity.University;
import org.study.system.deepdivestudy.repository.UniversityRepository;


import java.util.Optional;

@Service
public class UniversityService {

    UniversityRepository universityRepository;

    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public Optional<University> getById(Long id){
        return universityRepository.findById(id);
    }

    public Iterable<University> getAll(){
        return universityRepository.findAll();
    }

    public void deleteById(Long id) {
        universityRepository.deleteById(id);
    }

    public void createUni(UniversityDTO universityDTO){
        University university = new University();
        university.setName(universityDTO.getName());
        System.out.println(universityDTO.getCountry());
        university.setCountryName(universityDTO.getCountry());
        universityRepository.save(university);
    }
}
