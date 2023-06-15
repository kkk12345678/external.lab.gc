package org.example.gc.controller;

import org.example.gc.dto.TagRequestDto;
import org.example.gc.entity.Tag;
import org.example.gc.parameters.TagParameters;
import org.example.gc.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class TagController {
    private static final String TAG_ID = "tagId";
    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<Object> getAllTags(TagParameters tagParameters) {
        return new ResponseEntity<>(tagService.getAll(tagParameters), HttpStatus.OK);
    }

    @GetMapping(value = "/{tagId}")
    public Tag getTagById(@PathVariable(TAG_ID) Long id) {
        return tagService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Object> addTag(@RequestBody TagRequestDto tagRequestDto) {
        return new ResponseEntity<>(tagService.add(tagRequestDto), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable(TAG_ID) Long id) {
        tagService.remove(id);
    }
}