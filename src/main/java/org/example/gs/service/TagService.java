package org.example.gs.service;

import org.example.gs.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getAll();
    long add(Tag tag);
    void remove(long id);
    Optional<Tag> getById(long id);
    Optional<Tag> getByName(String name);
}
