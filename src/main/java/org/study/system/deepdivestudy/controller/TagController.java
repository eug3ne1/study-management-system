package org.study.system.deepdivestudy.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.system.deepdivestudy.entity.Tag;
import org.study.system.deepdivestudy.repository.TagRepository;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@AllArgsConstructor
public class TagController {

    private final TagRepository tagRepository;

    @GetMapping
    public List<Tag> findAll(){
        return tagRepository.findAll();
    }

}
