package org.example.gc.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.entity.Tag;
import org.example.gc.dto.TagRequestDto;
import org.example.gc.parameters.TagParameters;
import org.example.gc.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    private static final String ERROR_PARAMS_VIOLATION =
            "Tag parameters have the following violations : [%s]";
    private static final String ERROR_NAME_ALREADY_EXISTS =
            "Tag with name '%s' already exists.";
    private static final String ERROR_ID_NOT_FOUND =
            "There is no tag with 'id' = '%d'.";

    private static final String MESSAGE_TAG_FOUND =
            "Tag %s was successfully found.";
    private static final String MESSAGE_TAG_INSERTED =
            "%s was successfully inserted.";
    private static final String MESSAGE_TAG_DELETED =
            "%s was successfully deleted.";
    private static final String MESSAGE_TAGS_FOUND =
            "%d tags were successfully found.";
    private static final String MESSAGE_NO_TAG_BY_ID_FOUND =
            "No tag with 'id' = '%d' was found.";

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> getAll(TagParameters tagParameters) {
        List<Tag> tags = tagRepository.getAll(tagParameters);
        log.info(String.format(MESSAGE_TAGS_FOUND, tags.size()));
        return tags;
    }

    @Override
    @Transactional
    public Tag add(TagRequestDto tagRequestDto) {
        validate(tagRequestDto);
        String tagName = tagRequestDto.getName();
        if (tagRepository.getByName(tagName) == null) {
            Tag tag = tagRepository.insertOrUpdate(tagRequestDto.toEntity());
            log.info(String.format(MESSAGE_TAG_INSERTED, tag));
            return tag;
        } else {
            throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, tagName));
        }
    }

    @Override
    @Transactional
    public void remove(Long id) {
        Tag tag = check(id);
        tagRepository.delete(tag);
        log.info(String.format(MESSAGE_TAG_DELETED, tag));
    }

    @Override
    public Tag getById(Long id) {
        Tag tag = check(id);
        log.info(String.format(MESSAGE_TAG_FOUND, tag));
        return tag;
    }

    private void validate(TagRequestDto tagRequestDto) {
        Set<ConstraintViolation<TagRequestDto>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(tagRequestDto);
        if (violations.size() > 0) {
            throw new IllegalArgumentException(String.format(ERROR_PARAMS_VIOLATION,
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", "))));
        }
    }

    private Tag check(Long id) {
        Tag tag = tagRepository.getById(id);
        if (tag == null) {
            log.info(String.format(MESSAGE_NO_TAG_BY_ID_FOUND, id));
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
        return tag;
    }
}