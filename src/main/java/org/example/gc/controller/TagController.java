package org.example.gc.controller;

import org.example.gc.dto.TagRequestDto;
import org.example.gc.dao.TagParametersHandler;
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
    public ResponseEntity<Object> getAllTags() {
        return new ResponseEntity<>(tagService.getAll(null), HttpStatus.OK);
    }

    @GetMapping(value = "/{tagId}")
    public ResponseEntity<Object> getTagById(@PathVariable(TAG_ID) Long id) {
        return new ResponseEntity<>(tagService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addTag(@RequestBody(required = false) TagRequestDto tagRequestDto) {
        return new ResponseEntity<>(tagService.add(tagRequestDto), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{tagId}")
    public ResponseEntity<Object> deleteTag(@PathVariable(TAG_ID) Long id) {
        tagService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}