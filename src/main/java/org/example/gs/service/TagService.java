package org.example.gs.service;

import org.example.gs.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getFromIds(List<Long> ids);
    long add(Tag tag);
    void remove(Tag tag);
    List<Tag> getAll();

    Optional<Tag> getById(long id);
}
