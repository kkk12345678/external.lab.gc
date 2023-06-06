package org.example.gc.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.JDBC.TagParametersHandler;
import org.example.gc.entity.Tag;
import org.example.gc.dto.TagRequestDto;
import org.example.gc.dto.TagResponseDto;
import org.example.gc.entity.TagParameters;
import org.example.gc.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TagServiceImpl implements TagService {
    private static final String JPQL_ALL = "from Tag";
    private static final String ERROR_PARAMS_VIOLATION = "Tag parameters have the following violations : [%s]";
    private static final String ERROR_NAME_ALREADY_EXISTS = "Tag with name '%s' already exists.";
    private static final String ERROR_ID_NOT_FOUND = "There is no tag with 'id' = '%d'.";

    private static final String MESSAGE_TAG_FOUND = "Tag %s was successfully found.";
    private static final String MESSAGE_TAG_INSERTED = "Tag %s was successfully inserted.";
    private static final String MESSAGE_TAG_DELETED = "Tag %s was successfully deleted.";
    private static final String MESSAGE_TAGS_FOUND = "%d tags were successfully found.";
    private static final String MESSAGE_NO_TAG_BY_ID_FOUND = "No tag with 'id' = '%d' was found.";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<TagResponseDto> getAll(TagParameters tagParameters) {
        TypedQuery<Tag> query = entityManager.createQuery(JPQL_ALL, Tag.class);
        List<Tag> tags = query.getResultList();
        log.info(String.format(MESSAGE_TAGS_FOUND, tags.size()));
        return tags.stream().map(Tag::toResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Tag add(TagRequestDto tagRequestDto) {
        validate(tagRequestDto);
        String tagName = tagRequestDto.getName();
        Tag tag = tagRequestDto.toEntity();
        if (tagRepository.getByName(tagName) == null) {
            entityManager.persist(tag);
            //return tagRepository.insert(tagRequestDto.toEntity()).getId();
            log.info(String.format(MESSAGE_TAG_INSERTED, tag));
            flushAndClear();
            return tag;
        } else {
            throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, tagName));
        }

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

    @Override
    @Transactional
    public void remove(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag == null) {
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
        //tagRepository.delete(id);
        entityManager.remove(tag);
        flushAndClear();
        log.info(String.format(MESSAGE_TAG_DELETED, tag));
    }

    @Override
    public TagResponseDto getById(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag == null) {
            log.info(String.format(MESSAGE_NO_TAG_BY_ID_FOUND, id));
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        } else {
            log.info(String.format(MESSAGE_TAG_FOUND, tag));
            return tag.toResponseDto();
        }
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}