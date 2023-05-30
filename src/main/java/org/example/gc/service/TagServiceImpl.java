package org.example.gc.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.example.gc.dao.TagDao;
import org.example.gc.dao.TagParametersHandler;
import org.example.gc.dto.TagRequestDto;
import org.example.gc.dto.TagResponseDto;
import org.example.gc.dao.ParametersHandler;
import org.example.gc.model.Tag;
import org.example.gc.model.TagParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private static final String ERROR_PARAMS_VIOLATION = "Tag parameters have the following violations : [%s]";
    private static final String ERROR_NAME_ALREADY_EXISTS = "Tag with name '%s' already exists.";
    private static final String ERROR_ID_NOT_FOUND = "There is no tag with 'id' = '%d'.";
    @Autowired
    private TagDao tagDao;

    @Override
    public Collection<TagResponseDto> getAll(TagParameters tagParameters) {
        return tagDao.getAll(new TagParametersHandler(tagParameters))
                .stream()
                .map(TagResponseDto::fromEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long add(TagRequestDto tagRequestDto) {
        Set<ConstraintViolation<TagRequestDto>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(tagRequestDto);
        if (violations.size() > 0) {
            throw new IllegalArgumentException(String.format(ERROR_PARAMS_VIOLATION,
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", "))));
        }
        String tagName = tagRequestDto.getName();
        if (tagDao.getByName(tagName) == null) {
            return tagDao.insert(tagRequestDto.toEntity());
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