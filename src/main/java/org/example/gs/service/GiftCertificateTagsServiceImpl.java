package org.example.gs.service;

import org.example.gs.dao.GiftCertificateTagsJdbcTemplate;
import org.example.gs.dao.TagJdbcTemplate;
import org.example.gs.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


public class GiftCertificateTagsServiceImpl implements GiftCertificateTagsService {
    @Autowired
    private TagJdbcTemplate tagJdbcTemplate;
    @Autowired
    private GiftCertificateTagsJdbcTemplate giftCertificateTagsJdbcTemplate;

    @Override
    public List<String> getTagNamesByGiftCertificate(GiftCertificate giftCertificate) {
        return giftCertificateTagsJdbcTemplate.getTagIdByCertificateId(giftCertificate.getId()).stream()
                .map((id) -> tagJdbcTemplate.getById(id).orElseThrow().getName())
                .collect(Collectors.toList());
    }
}
