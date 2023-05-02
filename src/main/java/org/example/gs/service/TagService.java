package org.example.gs.service;

import org.example.gs.dto.TagRequestDto;
import org.example.gs.dto.TagResponseDto;
import org.example.gs.model.Parameters;

import java.util.Collection;

public interface TagService {
    Collection<TagResponseDto> getAll(Parameters tagParameters);
    long add(TagRequestDto tagRequestDto);
    void remove(long id);
    TagResponseDto getById(long id);
}