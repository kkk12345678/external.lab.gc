package org.example.gs.service;

import org.example.gs.dao.TagDao;
import org.example.gs.dto.TagRequestDto;
import org.example.gs.dto.TagResponseDto;
import org.example.gs.model.Parameters;
import org.example.gs.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private static final String ERROR_PARAMS_NOT_SPECIFIED = "Tag parameters are not specified.";
    private static final String ERROR_NAME_NOT_SPECIFIED = "Tag parameter 'name' is not specified.";
    private static final String ERROR_NAME_ALREADY_EXISTS = "Tag with name '%s' already exists.";
    private static final String ERROR_ID_NOT_FOUND = "There is no tag with 'id' = '%d'.";
    @Autowired
    private TagDao tagDao;

    @Override
    public Collection<TagResponseDto> getAll(Parameters tagParameters) {
        return tagDao.getAll(tagParameters)
                .stream()
                .map(TagResponseDto::fromEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long add(TagRequestDto tagRequestDto) {
        if (tagRequestDto == null) {
            throw new IllegalArgumentException(ERROR_PARAMS_NOT_SPECIFIED);
        }
        String tagName = tagRequestDto.getName();
        if (tagName == null || tagName.isEmpty()) {
            throw new IllegalArgumentException(ERROR_NAME_NOT_SPECIFIED);
        }
        if (tagDao.getByName(tagName) == null) {
            return tagDao.insert(TagRequestDto.fromDtoToEntity(tagRequestDto));
        } else {
            throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, tagName));
        }
    }

    @Override
    public void remove(long id) {
        if (tagDao.getById(id) == null) {
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
        tagDao.delete(id);
    }

    @Override
    public TagResponseDto getById(long id) {
        Tag tag = tagDao.getById(id);
        if (tag == null) {
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
        return TagResponseDto.fromEntityToDto(tag);
    }
}