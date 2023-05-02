package org.example.gs.service;

import org.example.gs.dao.TagDao;
import org.example.gs.dto.TagRequestDto;
import org.example.gs.dto.TagResponseDto;
import org.example.gs.model.GiftCertificateParameters;
import org.example.gs.model.Parameters;
import org.example.gs.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
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
            throw new IllegalArgumentException("Tag parameters are not specified.");
        }
        String tagName = tagRequestDto.getName();
        if (tagName == null || tagName.isEmpty()) {
            throw new IllegalArgumentException("Tag parameter 'name' is not specified.");
        }
        if (tagDao.getByName(tagName) == null) {
            return tagDao.insert(TagRequestDto.fromDtoToEntity(tagRequestDto));
        } else {
            throw new IllegalArgumentException(String.format("Tag with name'%s' already exists.", tagName));
        }
    }

    @Override
    public void remove(long id) {
        if (tagDao.getById(id) == null) {
            throw new NoSuchElementException();
        }
        tagDao.delete(id);
    }

    @Override
    public TagResponseDto getById(long id) {
        Tag tag = tagDao.getById(id);
        if (tag == null) {
            throw new NoSuchElementException();
        }
        return TagResponseDto.fromEntityToDto(tag);
    }
}