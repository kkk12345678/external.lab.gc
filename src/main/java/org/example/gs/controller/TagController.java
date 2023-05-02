package org.example.gs.controller;

import org.example.gs.dto.TagRequestDto;
import org.example.gs.model.TagParameters;
import org.example.gs.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public Object getAllTags() {
        try {
            return new ResponseEntity<>(tagService.getAll(new TagParameters()), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @GetMapping(value = "/{tagId}")
    public Object getTagById(@PathVariable("tagId") String id) {
        try {
            return new ResponseEntity<>(tagService.getById(Long.parseLong(id)), HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Illegal parameter 'tagId' = '%s'. Must be positive integer.", id));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no tag with id " + id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @PostMapping
    public Object addTag(@RequestBody(required = false) TagRequestDto tagRequestDto) {
        try {
            return new ResponseEntity<>(tagService.add(tagRequestDto), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @DeleteMapping(value = "/{tagId}")
    public Object deleteTag(@PathVariable("tagId") String id) {
        try {
            tagService.remove(Long.parseLong(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Illegal parameter 'tagId'. Must be positive integer.");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no tag with id " + id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
}