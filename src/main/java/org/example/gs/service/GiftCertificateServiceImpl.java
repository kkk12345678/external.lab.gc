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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    @Autowired
    private GiftCertificateDao giftCertificateDao;
    @Autowired
    private TagDao tagDao;
    /*
    @Autowired
    private Logger logger;
     */

    @Override
    public List<GiftCertificateResponseDto> getAll(Parameters parameters) {
        return giftCertificateDao.getAll(parameters)
                .stream()
                .map(GiftCertificateResponseDto::fromEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateResponseDto getById(long id) {
        GiftCertificate giftCertificate = giftCertificateDao.getById(id);
        if (giftCertificate == null) {
            throw new NoSuchElementException();
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
            throw new NoSuchElementException();
        }
    }

    @Override
    @Transactional
    public void update(long id, GiftCertificateRequestDto giftCertificateRequestDto) {
        //logger.info("Processing " + giftCertificateRequestDto);
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
            throw new IllegalArgumentException("No data specified to insert.");
        }
        String name = giftCertificateRequestDto.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Parameter 'name' is not specified.");
        }
        if (giftCertificateRequestDto.getDuration() <= 0) {
            throw new IllegalArgumentException("Parameter 'duration' must be positive integer.");
        }
        if (giftCertificateRequestDto.getPrice() <= 0.0) {
            throw new IllegalArgumentException("Parameter 'price' must be positive double.");
        }
        if (giftCertificateDao.getByName(name) != null) {
            throw new IllegalArgumentException(
                    String.format("Gift certificate with name '%s' already exists.", name));
        }
    }

    private GiftCertificate checkGiftCertificateRequestDtoForUpdate(
            long id, GiftCertificateRequestDto giftCertificateRequestDto) {
        if (giftCertificateRequestDto == null) {
            throw new IllegalArgumentException("No data specified to update.");
        }
        GiftCertificate giftCertificateToUpdate = giftCertificateDao.getById(id);
        if (giftCertificateToUpdate == null) {
            throw new NoSuchElementException();
        }
        String name = giftCertificateRequestDto.getName();
        if (name != null && !name.isBlank()) {
            GiftCertificate giftCertificateToCheck = giftCertificateDao.getByName(name);
            if (giftCertificateToCheck != null &&
                    giftCertificateToCheck.getId() != giftCertificateToUpdate.getId()) {
                throw new IllegalArgumentException(
                        String.format("Gift certificate with name '%s' already exists.", name));
            }
            giftCertificateToUpdate.setName(name);
        }
        Double price = giftCertificateRequestDto.getPrice();
        if (price != null) {
            if (price <= 0.0) {
                throw new IllegalArgumentException("Parameter 'price' must be positive double.");
            } else {
                giftCertificateToUpdate.setPrice(price);
            }
        }
        Integer duration = giftCertificateRequestDto.getDuration();
        if (duration != null) {
            if (duration <= 0) {
                throw new IllegalArgumentException("Parameter 'duration' must be positive integer.");
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