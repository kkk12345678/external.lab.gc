package org.example.gs.service;

import org.example.gs.dto.TagRequestDto;
import org.example.gs.dto.TagResponseDto;
import org.example.gs.model.Parameters;

import java.util.List;

public interface TagService {
    List<TagResponseDto> getAll(Parameters parameters);
    long add(TagRequestDto tagRequestDto);
    void remove(long id);
    TagResponseDto getById(long id);
}
