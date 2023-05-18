package org.example.gc.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.example.gc.dao.GiftCertificateDao;
import org.example.gc.dao.GiftCertificateParametersHandler;
import org.example.gc.dao.TagDao;
import org.example.gc.dto.*;
import org.example.gc.model.GiftCertificate;
import org.example.gc.model.GiftCertificateParameters;
import org.example.gc.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String ERROR_PARAMS_VIOLATION = "Gift certificate parameters have the following violations : [%s]";
    private static final String ERROR_NAME_ALREADY_EXISTS = "Gift certificate with name '%s' already exists.";
    private static final String ERROR_ID_NOT_FOUND = "There is no gift certificate with 'id' = '%d'.";
    @Autowired
    private GiftCertificateDao giftCertificateDao;
    @Autowired
    private TagDao tagDao;

    @Override
    public Collection<GiftCertificateResponseDto> getAll(GiftCertificateParameters giftCertificateParameters) {
        return giftCertificateDao.getAll(new GiftCertificateParametersHandler(giftCertificateParameters))
                .stream()
                .map(GiftCertificateResponseDto::fromEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateResponseDto getById(long id) {
        GiftCertificate giftCertificate = giftCertificateDao.getById(id);
        if (giftCertificate == null) {
            throw new NoSuchElementException(
                    String.format(ERROR_ID_NOT_FOUND, id));
        }
        return GiftCertificateResponseDto.fromEntityToDto(giftCertificate);
    }

    @Override
    public GiftCertificateResponseDto getByName(String name) {
        return GiftCertificateResponseDto.fromEntityToDto(giftCertificateDao.getByName(name));
    }

    @Override
    @Transactional
    public long add(GiftCertificateRequestInsertDto giftCertificateRequestInsertDto) {
        validate(giftCertificateRequestInsertDto);
        String name = giftCertificateRequestInsertDto.getName();
        if (giftCertificateDao.getByName(name) != null) {
            throw new IllegalArgumentException(
                    String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
        long id = giftCertificateDao.insert(giftCertificateRequestInsertDto.toEntity());
        Collection<TagRequestDto> tags = giftCertificateRequestInsertDto.getTags();
        if (tags != null) {
            setTags(tags, id);
        }
        return id;
    }

    @Override
    public void remove(long id) {
        if (giftCertificateDao.getById(id) != null) {
            giftCertificateDao.delete(id);
        } else {
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
    }

    @Override
    @Transactional
    public void update(long id, GiftCertificateRequestUpdateDto giftCertificateRequestUpdateDto) {
        validate(giftCertificateRequestUpdateDto);
        GiftCertificate giftCertificateToUpdate = giftCertificateDao.getById(id);
        if (giftCertificateToUpdate == null) {
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
        String name = giftCertificateRequestUpdateDto.getName();
        GiftCertificate giftCertificateToCheck = giftCertificateDao.getByName(name);
        if (giftCertificateToCheck != null && giftCertificateToCheck.getId() != id) {
            throw new IllegalArgumentException(
                    String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
        giftCertificateToUpdate.setName(name);
        Double price = giftCertificateRequestUpdateDto.getPrice();
        if (price != null) {
            giftCertificateToUpdate.setPrice(price);
        }
        Integer duration = giftCertificateRequestUpdateDto.getDuration();
        if (duration != null) {
            giftCertificateToUpdate.setDuration(duration);
        }
        giftCertificateDao.update(giftCertificateToUpdate);
        Collection<TagRequestDto> tags = giftCertificateRequestUpdateDto.getTags();
        if (tags != null) {
            setTags(tags, id);
        }
    }

    private void validate(GiftCertificateRequestUpdateDto giftCertificateRequestUpdateDto) {
        Set<ConstraintViolation<GiftCertificateRequestUpdateDto>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(giftCertificateRequestUpdateDto);
        if (violations.size() > 0) {
            throw new IllegalArgumentException(String.format(ERROR_PARAMS_VIOLATION,
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", "))));
        }
    }

    private void validate(GiftCertificateRequestInsertDto giftCertificateRequestInsertDto) {
        Set<ConstraintViolation<GiftCertificateRequestInsertDto>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(giftCertificateRequestInsertDto);
        if (violations.size() > 0) {
            throw new IllegalArgumentException(String.format(ERROR_PARAMS_VIOLATION,
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", "))));
        }
    }

    private void setTags(Collection<TagRequestDto> tags, long giftCertificateId) {
        tags.forEach(tagDto -> {
            String tagName = tagDto.getName();
            Tag tag = tagDao.getByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tag.setId(tagDao.insert(tag));
            }
            giftCertificateDao.insert(giftCertificateId, tag.getId());
        });
    }
}