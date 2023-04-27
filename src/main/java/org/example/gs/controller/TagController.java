package org.example.gs.controller;

import org.example.gs.model.Tag;
import org.example.gs.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public List<Tag> getAllTags() {
        try {
            return tagService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/{tagId}")
    public Tag getTagById(@PathVariable("tagId") String id) {
        try {
            return tagService.getById(Long.parseLong(id)).get();
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Illegal parameter 'tagId'. Must be positive integer.", e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "There is no tag with id " + id, e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    e.getMessage(), e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addTag(@RequestBody(required = false) Tag tag) {
        if (tag == null || tag.getName() == null || tag.getName().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Parameter 'name' is not specified.");
        }
        String name = tag.getName();
        if (tagService.getByName(name).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Tag with name '%s' already exists.", name));
        }
        try {
            return tagService.add(tag);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    e.getMessage(), e);
        }
    }

    @DeleteMapping(value = "/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTag(@PathVariable("tagId") String id) {
        try {
            tagService.remove(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Illegal parameter 'tagId'. Must be positive integer.", e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "There is no tag with id " + id, e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    e.getMessage(), e);
        }
    }
}
