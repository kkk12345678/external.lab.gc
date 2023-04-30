package org.example.gs.controller;

import org.example.gs.dto.TagRequestDto;
import org.example.gs.model.Parameters;
import org.example.gs.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public Object getAllTags() {
        try {
            return new ResponseEntity<>(tagService.getAll(new Parameters()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping(value = "/{tagId}")
    public Object getTagById(@PathVariable("tagId") String id) {
        try {
            return new ResponseEntity<>(tagService.getById(Long.parseLong(id)), HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(
                    String.format("Illegal parameter tagId = '%s'. Must be positive integer.", id),
                    HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    "There is no tag with id " + id,
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping
    public Object addTag(@RequestBody(required = false) TagRequestDto tagRequestDto) {
        try {
            return new ResponseEntity<>(tagService.add(tagRequestDto), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @DeleteMapping(value = "/{tagId}")
    public Object deleteTag(@PathVariable("tagId") String id) {
        try {
            tagService.remove(Long.parseLong(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Illegal parameter 'tagId'. Must be positive integer.", HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("There is no tag with id " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
