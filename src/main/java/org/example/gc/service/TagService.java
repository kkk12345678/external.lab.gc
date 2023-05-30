package org.example.gc.service;

import org.example.gc.dto.TagRequestDto;
import org.example.gc.dto.TagResponseDto;
import org.example.gc.model.TagParameters;

import java.util.Collection;

public interface TagService {
    Collection<TagResponseDto> getAll(TagParameters tagParameters);
    long add(TagRequestDto tagRequestDto);
    void remove(long id);
    TagResponseDto getById(long id);
}