package org.study.system.deepdivestudy.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.system.deepdivestudy.dto.TeacherDTO;
import org.study.system.deepdivestudy.dto.UniversityDTO;
import org.study.system.deepdivestudy.entity.University;
import org.study.system.deepdivestudy.service.TeacherService;
import org.study.system.deepdivestudy.service.UniversityService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/university")
@AllArgsConstructor
public class UniversityController {

    private final UniversityService universityService;
    private final TeacherService teacherService;



    @GetMapping("/{id}")
    public ResponseEntity<University> getByID (@PathVariable Long id){
        Optional<University> universityServiceByIdyId = universityService.getById(id);
        return universityServiceByIdyId.map(university -> new ResponseEntity<>(university, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }


    @GetMapping("/{id}/teachers")
    public ResponseEntity<List<TeacherDTO>> getTeachersByUni(@PathVariable Long id){
        return new ResponseEntity<>(teacherService.getTeachersByUni(id),HttpStatus.OK);
    }

    @GetMapping
    public Iterable<University> getAll(){
        return universityService.getAll();
    }

    @PostMapping
    public ResponseEntity<String> save (@RequestBody UniversityDTO dto) {
        System.out.println("Received DTO: " + dto);
        universityService.createUni(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        universityService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
