package org.example.gs.service;

import org.example.gs.dao.GiftCertificateJdbcTemplate;
import org.example.gs.dao.GiftCertificateTagsJdbcTemplate;
import org.example.gs.dao.TagJdbcTemplate;
import org.example.gs.model.GiftCertificate;
import org.example.gs.model.Tag;
import org.example.gs.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class GiftCertificateServiceImpl implements GiftCertificateService {
    @Autowired
    private GiftCertificateJdbcTemplate giftCertificateJdbcTemplate;
    @Autowired
    private TagJdbcTemplate tagJdbcTemplate;
    @Autowired
    private GiftCertificateTagsJdbcTemplate giftCertificateTagsJdbcTemplate;

    @Override
    @Transactional
    public long add(GiftCertificate giftCertificate, String[] tags) {
        String giftCertificateName = giftCertificate.getName();
        Optional<GiftCertificate> optional = giftCertificateJdbcTemplate.getByName(giftCertificateName);
        if (optional.isEmpty()) {
            giftCertificateJdbcTemplate.insert(giftCertificate);
            long giftCertificateId = giftCertificateJdbcTemplate.getByName(giftCertificateName).orElseThrow().getId();
            for (String tagName : tags) {
                Optional<Tag> optionalTag = tagJdbcTemplate.getByName(tagName);
                Tag tag = new Tag();
                if (optionalTag.isEmpty()) {
                    tag.setName(tagName);
                    tagJdbcTemplate.insert(tag);
                    tag.setId(tagJdbcTemplate.getByName(tagName).orElseThrow().getId());
                } else {
                    tag = optionalTag.get();
                }
                giftCertificateTagsJdbcTemplate.insert(giftCertificateId, tag.getId());
            }
        } else {
            throw new IllegalArgumentException();
        }
        return giftCertificateJdbcTemplate.getByName(giftCertificateName).orElseThrow().getId();
    }

    @Override
    public void remove(GiftCertificate giftCertificate) {

    }

    @Override
    public void update(GiftCertificate giftCertificate) {

    }

    @Override
    public List<GiftCertificate> getAll(SortType sortType, SortOrder sortOrder) {
        return giftCertificateJdbcTemplate.getAll();
    }

    @Override
    public List<GiftCertificate> findByTagName(String tagName, SortType sortType, SortOrder sortOrder) {
        return null;
    }

    @Override
    public List<GiftCertificate> findByName(String name, SortType sortType, SortOrder sortOrder) {
        return null;
    }

    @Override
    public List<GiftCertificate> findByDescription(String description, SortType sortType, SortOrder sortOrder) {
        return null;
    }


}
