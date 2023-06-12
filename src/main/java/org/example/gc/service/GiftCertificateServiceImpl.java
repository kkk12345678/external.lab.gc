package org.example.gc.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.dto.*;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.entity.GiftCertificateParameters;
import org.example.gc.entity.Tag;
import org.example.gc.repository.GiftCertificateRepository;
import org.example.gc.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    public GiftCertificateResponseDto getById(Long id) {
        GiftCertificate giftCertificate = check(id);
        log.info(String.format(MESSAGE_GIFT_CERTIFICATE_FOUND, giftCertificate));
        return giftCertificate.toResponseDto();
    }

    @Override
    public GiftCertificateResponseDto getByName(String name) {
        GiftCertificate giftCertificate = giftCertificateRepository.getByName(name);
        if (giftCertificate == null) {
            throw new NoSuchElementException(String.format(ERROR_NAME_NOT_FOUND, name));
        }
        log.info(String.format(MESSAGE_GIFT_CERTIFICATE_FOUND, giftCertificate));
        return giftCertificate.toResponseDto();
    }

    @Override
    @Transactional
    public Long add(GiftCertificateRequestInsertDto giftCertificateRequestInsertDto) {
        validate(giftCertificateRequestInsertDto);
        String name = giftCertificateRequestInsertDto.getName();
        if (giftCertificateRepository.getByName(name) != null) {
            throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
        GiftCertificate giftCertificateToInsert = giftCertificateRequestInsertDto.toEntity();
        setTags(giftCertificateToInsert, giftCertificateRequestInsertDto);
        giftCertificateToInsert.setCreateDate(getDate());
        giftCertificateToInsert.setLastUpdateDate(getDate());
        GiftCertificate giftCertificate = giftCertificateRepository.insertOrUpdate(giftCertificateToInsert);
        log.info(String.format(MESSAGE_GIFT_CERTIFICATE_INSERTED, giftCertificate));
        return giftCertificate.getId();
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
    public void update(Long id, GiftCertificateRequestUpdateDto giftCertificateRequestUpdateDto) {
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
        giftCertificate.setLastUpdateDate(getDate());
        giftCertificateRepository.insertOrUpdate(giftCertificate);
        log.info(String.format(MESSAGE_GIFT_CERTIFICATE_UPDATED, giftCertificate));
    }

    @Override
    public List<GiftCertificateResponseDto> getAll(GiftCertificateParameters giftCertificateParameters) {
        List<GiftCertificate> giftCertificates =
                giftCertificateRepository.getAll(giftCertificateParameters);
        log.info(String.format(MESSAGE_GIFT_CERTIFICATES_FOUND, giftCertificates.size()));
        return giftCertificates.stream().
                map(GiftCertificate::toResponseDto).
                collect(Collectors.toList());
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
        Set<Tag> tags = new HashSet<>();
        giftCertificateRequestDto.getTags().forEach(tagRequestDto -> {
            String tagName = tagRequestDto.getName();
            Tag tag = tagRepository.getByName(tagName);
            tags.add(tag == null ? new Tag(null, tagName) : tag);
        });
        giftCertificate.setTags(tags);
    }

    private static String getDate() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }
}