package org.example.gc.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.dto.*;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.parameters.GiftCertificateParameters;
import org.example.gc.entity.Tag;
import org.example.gc.repository.GiftCertificateRepository;
import org.example.gc.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String ERROR_PARAMS_VIOLATION =
            "Gift certificate parameters have the following violations : [%s]";
    private static final String ERROR_NAME_ALREADY_EXISTS =
            "Gift certificate with name '%s' already exists.";
    private static final String ERROR_ID_NOT_FOUND =
            "There is no gift certificate with 'id' = '%d'.";
    private static final String ERROR_NAME_NOT_FOUND =
            "There is no gift certificate with 'name' = '%s'.";

    private static final String MESSAGE_GIFT_CERTIFICATE_FOUND =
            "%s was successfully found.";
    private static final String MESSAGE_GIFT_CERTIFICATES_FOUND =
            "%d gift certificates were successfully found.";
    private static final String MESSAGE_GIFT_CERTIFICATE_INSERTED =
            "%s successfully inserted.";
    private static final String MESSAGE_GIFT_CERTIFICATE_DELETED =
            "%s successfully deleted.";
    private static final String MESSAGE_GIFT_CERTIFICATE_UPDATED =
            "%s successfully updated.";

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private TagRepository tagRepository;

    @Override
    public GiftCertificate getById(Long id) {
        GiftCertificate giftCertificate = check(id);
        log.info(String.format(MESSAGE_GIFT_CERTIFICATE_FOUND, giftCertificate));
        return giftCertificate;
    }

    @Override
    public GiftCertificate getByName(String name) {
        GiftCertificate giftCertificate = giftCertificateRepository.getByName(name);
        if (giftCertificate == null) {
            throw new NoSuchElementException(String.format(ERROR_NAME_NOT_FOUND, name));
        }
        log.info(String.format(MESSAGE_GIFT_CERTIFICATE_FOUND, giftCertificate));
        return giftCertificate;
    }

    @Override
    @Transactional
    public GiftCertificate add(GiftCertificateRequestInsertDto giftCertificateRequestInsertDto) {
        validate(giftCertificateRequestInsertDto);
        String name = giftCertificateRequestInsertDto.getName();
        if (giftCertificateRepository.getByName(name) != null) {
            throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
        GiftCertificate giftCertificateToInsert = giftCertificateRequestInsertDto.toEntity();
        setTags(giftCertificateToInsert, giftCertificateRequestInsertDto);
        Instant now = Instant.now();
        giftCertificateToInsert.setCreateDate(now);
        giftCertificateToInsert.setLastUpdateDate(now);
        GiftCertificate giftCertificate = giftCertificateRepository.insertOrUpdate(giftCertificateToInsert);
        log.info(String.format(MESSAGE_GIFT_CERTIFICATE_INSERTED, giftCertificate));
        return giftCertificate;
    }

    @Override
    @Transactional
    public void remove(Long id) {
        GiftCertificate giftCertificate = check(id);
        giftCertificateRepository.delete(giftCertificate);
        log.info(String.format(MESSAGE_GIFT_CERTIFICATE_DELETED, giftCertificate));
    }

    @Override
    @Transactional
    public GiftCertificate update(Long id, GiftCertificateRequestUpdateDto giftCertificateRequestUpdateDto) {
        validate(giftCertificateRequestUpdateDto);
        GiftCertificate giftCertificate = check(id);
        String description = giftCertificateRequestUpdateDto.getDescription();
        if (description != null) {
            giftCertificate.setDescription(description);
        }
        String name = giftCertificateRequestUpdateDto.getName();
        if (name != null) {
            GiftCertificate checkGiftCertificate = giftCertificateRepository.getByName(name);
            if (checkGiftCertificate != null && !checkGiftCertificate.getId().equals(id)) {
                throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
            }
            giftCertificate.setName(name);
        }
        Double price = giftCertificateRequestUpdateDto.getPrice();
        if (price != null) {
            giftCertificate.setPrice(price);
        }
        Integer duration = giftCertificateRequestUpdateDto.getDuration();
        if (duration != null) {
            giftCertificate.setDuration(duration);
        }
        setTags(giftCertificate, giftCertificateRequestUpdateDto);
        giftCertificate.setLastUpdateDate(Instant.now());
        GiftCertificate updatedGiftCertificate = giftCertificateRepository.insertOrUpdate(giftCertificate);
        log.info(String.format(MESSAGE_GIFT_CERTIFICATE_UPDATED, updatedGiftCertificate));
        return updatedGiftCertificate;
    }

    @Override
    public List<GiftCertificate> getAll(GiftCertificateParameters giftCertificateParameters) {
        List<GiftCertificate> giftCertificates =
                giftCertificateRepository.getAll(giftCertificateParameters);
        log.info(String.format(MESSAGE_GIFT_CERTIFICATES_FOUND, giftCertificates.size()));
        return giftCertificates;
    }

    private GiftCertificate check(Long id) {
        GiftCertificate giftCertificate = giftCertificateRepository.getById(id);
        if (giftCertificate == null) {
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
        return giftCertificate;
    }

    private void validate(GiftCertificateRequestDto dto) {
        Set<ConstraintViolation<GiftCertificateRequestDto>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
        if (violations.size() > 0) {
            throw new IllegalArgumentException(String.format(ERROR_PARAMS_VIOLATION,
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", "))));
        }
    }

    private void setTags(GiftCertificate giftCertificate, GiftCertificateRequestDto giftCertificateRequestDto) {
        Set<String> tagNames = giftCertificateRequestDto.getTags().stream()
                .map(TagRequestDto::getName)
                .collect(Collectors.toSet());
        Set<Tag> tags = new HashSet<>(tagRepository.getByNames(tagNames));
        Set<String> existingNames = tags.stream().map(Tag::getName).collect(Collectors.toSet());
        tagNames.stream()
                .filter(name -> !existingNames.contains(name))
                .forEach(name -> tags.add(new Tag(null, name)));
        giftCertificate.setTags(tags);
    }
}