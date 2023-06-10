package org.example.gc.service;

import org.example.gc.dto.TagRequestDto;
import org.example.gc.dto.TagResponseDto;
import org.example.gc.entity.TagParameters;

import java.util.List;

public interface TagService {
    List<TagResponseDto> getAll(TagParameters tagParameters);
    Long add(TagRequestDto tagRequestDto);
    void remove(Long id);
    TagResponseDto getById(Long id);
}