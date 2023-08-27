package org.example.gc.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.dto.*;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.exception.AlreadyExistsException;
import org.example.gc.parameters.GiftCertificateParameters;
import org.example.gc.entity.Tag;
import org.example.gc.repository.GiftCertificateRepository;
import org.example.gc.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class GiftCertificateServiceImpl extends AbstractService implements GiftCertificateService {
    private static final String ERROR_NAME_ALREADY_EXISTS =
            "Gift certificate with name '%s' already exists.";
    private static final String ERROR_ID_NOT_FOUND =
            "There is no gift certificate with 'id' = '%d'.";
    private static final String ERROR_NAME_NOT_FOUND =
            "There is no gift certificate with 'name' = '%s'.";
    private static final String MESSAGE_GIFT_CERTIFICATES_FOUND =
            "%d gift certificates were successfully found.";

    Random random = new Random();

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private TagRepository tagRepository;

    @Override
    public GiftCertificate getById(Long id) {
        GiftCertificate giftCertificate = check(id);
        log.info(String.format(MESSAGE_FOUND, giftCertificate));
        return giftCertificate;
    }

    @Override
    public GiftCertificate getByName(String name) {
        GiftCertificate giftCertificate = giftCertificateRepository.getByName(name);
        if (giftCertificate == null) {
            throw new NoSuchElementException(String.format(ERROR_NAME_NOT_FOUND, name));
        }
        log.info(String.format(MESSAGE_FOUND, giftCertificate));
        return giftCertificate;
    }

    @Override
    @Transactional
    public GiftCertificate add(GiftCertificateInsertDto dto) {
        validate(dto);
        String name = dto.getName();
        if (giftCertificateRepository.getByName(name) != null) {
            throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
        GiftCertificate giftCertificateToInsert = dto.toEntity();
        setTags(giftCertificateToInsert, dto);
        Instant now = Instant.now();
        giftCertificateToInsert.setCreateDate(now);
        giftCertificateToInsert.setLastUpdateDate(now);
        GiftCertificate giftCertificate = giftCertificateRepository.insertOrUpdate(giftCertificateToInsert);
        log.info(String.format(MESSAGE_INSERTED, giftCertificate));
        return giftCertificate;
    }

    @Override
    @Transactional
    public void remove(Long id) {
        GiftCertificate giftCertificate = check(id);
        giftCertificateRepository.delete(giftCertificate);
        log.info(String.format(MESSAGE_DELETED, giftCertificate));
    }

    @Override
    @Transactional
    public GiftCertificate update(Long id, GiftCertificateUpdateDto dto) {
        validate(dto);
        GiftCertificate giftCertificate = check(id);
        String description = dto.getDescription();
        if (description != null) {
            giftCertificate.setDescription(description);
        }
        String name = dto.getName();
        if (name != null) {
            GiftCertificate checkGiftCertificate = giftCertificateRepository.getByName(name);
            if (checkGiftCertificate != null && !checkGiftCertificate.getId().equals(id)) {
                throw new AlreadyExistsException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
            }
            giftCertificate.setName(name);
        }
        Double price = dto.getPrice();
        if (price != null) {
            giftCertificate.setPrice(price);
        }
        Integer duration = dto.getDuration();
        if (duration != null) {
            giftCertificate.setDuration(duration);
        }
        setTags(giftCertificate, dto);
        giftCertificate.setLastUpdateDate(Instant.now());
        GiftCertificate updatedGiftCertificate = giftCertificateRepository.insertOrUpdate(giftCertificate);
        log.info(String.format(MESSAGE_UPDATED, updatedGiftCertificate));
        return updatedGiftCertificate;
    }

    @Override
    public long count(GiftCertificateParameters giftCertificateParameters) {
        return giftCertificateRepository.count(giftCertificateParameters);
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

    private void setTags(GiftCertificate giftCertificate, GiftCertificateDto giftCertificateRequestDto) {
        Set<String> tagNames = giftCertificateRequestDto.getTags().stream()
                .map(TagDto::getName)
                .collect(Collectors.toSet());
        Set<Tag> tags = new HashSet<>(tagRepository.getByNames(tagNames));
        Set<String> existingNames = tags.stream().map(Tag::getName).collect(Collectors.toSet());
        tagNames.stream()
                .filter(name -> !existingNames.contains(name))
                .forEach(name -> tags.add(new Tag(name)));
        giftCertificate.setTags(tags);
    }

    void create() {
        if (giftCertificateRepository.getAll(new GiftCertificateParameters()).isEmpty()) {
            GiftCertificateInsertDto dto = new GiftCertificateInsertDto();
            Set<TagDto> tags = new HashSet<>();
            IntStream.range(0, 1000).forEach(i -> {
                tags.clear();
                IntStream.range(0, random.nextInt(5)).forEach(j -> tags.add(new TagDto("Tag" + j)));
                dto.setName("gc" + i);
                dto.setDescription("Some description");
                dto.setPrice(random.nextDouble(150));
                dto.setDuration(random.nextInt(30));
                dto.setTags(tags);
                add(dto);
            });
        }
    }
}