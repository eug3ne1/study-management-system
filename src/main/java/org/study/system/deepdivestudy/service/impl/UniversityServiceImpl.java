package org.study.system.deepdivestudy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.study.system.deepdivestudy.dto.UniversityDTO;
import org.study.system.deepdivestudy.entity.University;
import org.study.system.deepdivestudy.repository.UniversityRepository;
import org.study.system.deepdivestudy.service.UniversityService;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    @Override
    public Optional<University> getById(Long id){
        return universityRepository.findById(id);
    }

    @Override
    public Iterable<University> getAll(){
        return universityRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        universityRepository.deleteById(id);
    }

    @Override
    public void createUni(UniversityDTO universityDTO){
        University university = new University();
        university.setName(universityDTO.getName());
        System.out.println(universityDTO.getCountry());
        university.setCountryName(universityDTO.getCountry());
        universityRepository.save(university);
    }
}
