package org.example.gc.service;

import org.example.gc.dto.TagDto;
import org.example.gc.entity.Tag;
import org.example.gc.parameters.TagParameters;

import java.util.List;

public interface TagService {
    List<Tag> getAll(TagParameters tagParameters);
    Tag add(TagDto tagRequestDto);
    void remove(Long id);
    Tag getById(Long id);
    Tag getMostValuable();
}