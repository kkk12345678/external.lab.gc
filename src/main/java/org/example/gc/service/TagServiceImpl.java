package org.example.gc.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.dto.TagDto;
import org.example.gc.entity.Tag;
import org.example.gc.parameters.TagParameters;
import org.example.gc.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class TagServiceImpl extends AbstractService implements TagService {
    private static final String ERROR_NAME_ALREADY_EXISTS =
            "Tag with name '%s' already exists.";
    private static final String ERROR_ID_NOT_FOUND =
            "There is no tag with 'id' = '%d'.";
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
    public Tag add(TagDto dto) {
        validate(dto);
        String tagName = dto.getName();
        if (tagRepository.getByName(tagName) == null) {
            Tag tag = tagRepository.insertOrUpdate(dto.toEntity());
            log.info(String.format(MESSAGE_INSERTED, tag));
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
        log.info(String.format(MESSAGE_DELETED, tag));
    }

    @Override
    public Tag getById(Long id) {
        Tag tag = check(id);
        log.info(String.format(MESSAGE_FOUND, tag));
        return tag;
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