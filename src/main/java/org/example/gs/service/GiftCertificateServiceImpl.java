package org.example.gs.service;

import org.example.gs.dao.GiftCertificateDao;
import org.example.gs.dao.TagDao;
import org.example.gs.dto.GiftCertificateRequestDto;
import org.example.gs.dto.GiftCertificateResponseDto;
import org.example.gs.model.GiftCertificate;
import org.example.gs.model.Parameters;
import org.example.gs.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String ERROR_PARAMS_NOT_SPECIFIED = "Gift certificate parameters are not specified.";
    private static final String ERROR_NAME_NOT_SPECIFIED = "Gift certificate parameter 'name' is not specified.";
    private static final String ERROR_DURATION_NOT_VALID = "Parameter 'duration' must be positive integer.";
    private static final String ERROR_PRICE_NOT_VALID = "Parameter 'price' must be positive double.";
    private static final String ERROR_NAME_ALREADY_EXISTS = "Gift certificate with name '%s' already exists.";
    private static final String ERROR_ID_NOT_FOUND = "There is no gift certificate with 'id' = '%d'.";
    @Autowired
    private GiftCertificateDao giftCertificateDao;
    @Autowired
    private TagDao tagDao;

    @Override
    public Collection<GiftCertificateResponseDto> getAll(
            Parameters giftCertificateParameters) {
        return giftCertificateDao.getAll(giftCertificateParameters)
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
    public long add(GiftCertificateRequestDto giftCertificateRequestDto) {
        checkGiftCertificateRequestDtoForInsert(giftCertificateRequestDto);
        GiftCertificate giftCertificate = GiftCertificateRequestDto.fromDtoToEntity(giftCertificateRequestDto);
        long giftCertificateId = giftCertificateDao.insert(giftCertificate);
        setTags(giftCertificateRequestDto, giftCertificateId);
        return giftCertificateId;
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
    public void update(long id, GiftCertificateRequestDto giftCertificateRequestDto) {
        giftCertificateDao.update(checkGiftCertificateRequestDtoForUpdate(id, giftCertificateRequestDto));
        if (giftCertificateRequestDto.getTags() != null) {
            giftCertificateDao.deleteTags(id);
            setTags(giftCertificateRequestDto, id);
        }
    }

    private void setTags(GiftCertificateRequestDto giftCertificateRequestDto, long giftCertificateId) {
        giftCertificateRequestDto.getTags().forEach(tagDto -> {
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

    private void checkGiftCertificateRequestDtoForInsert(GiftCertificateRequestDto giftCertificateRequestDto) {
        if (giftCertificateRequestDto == null) {
            throw new IllegalArgumentException(ERROR_PARAMS_NOT_SPECIFIED);
        }
        String name = giftCertificateRequestDto.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(ERROR_NAME_NOT_SPECIFIED);
        }
        if (giftCertificateRequestDto.getDuration() <= 0) {
            throw new IllegalArgumentException(ERROR_DURATION_NOT_VALID);
        }
        if (giftCertificateRequestDto.getPrice() <= 0.0) {
            throw new IllegalArgumentException(ERROR_PRICE_NOT_VALID);
        }
        if (giftCertificateDao.getByName(name) != null) {
            throw new IllegalArgumentException(
                    String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
    }

    private GiftCertificate checkGiftCertificateRequestDtoForUpdate(
            long id, GiftCertificateRequestDto giftCertificateRequestDto) {
        if (giftCertificateRequestDto == null) {
            throw new IllegalArgumentException(ERROR_PARAMS_NOT_SPECIFIED);
        }
        GiftCertificate giftCertificateToUpdate = giftCertificateDao.getById(id);
        if (giftCertificateToUpdate == null) {
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
        String name = giftCertificateRequestDto.getName();
        if (name != null && !name.isBlank()) {
            GiftCertificate giftCertificateToCheck = giftCertificateDao.getByName(name);
            if (giftCertificateToCheck != null &&
                    giftCertificateToCheck.getId() != giftCertificateToUpdate.getId()) {
                throw new IllegalArgumentException(
                        String.format(ERROR_NAME_ALREADY_EXISTS, name));
            }
            giftCertificateToUpdate.setName(name);
        }
        Double price = giftCertificateRequestDto.getPrice();
        if (price != null) {
            if (price <= 0.0) {
                throw new IllegalArgumentException(ERROR_PRICE_NOT_VALID);
            } else {
                giftCertificateToUpdate.setPrice(price);
            }
        }
        Integer duration = giftCertificateRequestDto.getDuration();
        if (duration != null) {
            if (duration <= 0) {
                throw new IllegalArgumentException(ERROR_DURATION_NOT_VALID);
            } else {
                giftCertificateToUpdate.setDuration(duration);
            }
        }
        String description = giftCertificateRequestDto.getDescription();
        if (description != null && !description.isBlank()) {
            giftCertificateToUpdate.setDescription(description);
        }
        return giftCertificateToUpdate;
    }
}