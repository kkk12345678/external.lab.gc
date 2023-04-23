package org.example.gs.util;

import org.example.gs.model.GiftCertificate;

import org.example.gs.service.GiftCertificateTagsService;
import org.example.gs.service.GiftCertificateTagsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {
    @Autowired
    GiftCertificateTagsService giftCertificateTagsService;
/*
    public GiftCertificateMapper() {
    }
    public GiftCertificateMapper(GiftCertificateTagsService giftCertificateTagsService) {
        this.giftCertificateTagsService = giftCertificateTagsService;
    }
*/
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        long id = rs.getLong("id");
        giftCertificate.setId(id);
        giftCertificate.setName(rs.getString("name"));
        giftCertificate.setDescription(rs.getString("description"));
        giftCertificate.setPrice(rs.getDouble("price"));
        giftCertificate.setDuration(rs.getInt("duration"));
        giftCertificate.setCreateDate(rs.getString("create_date"));
        giftCertificate.setLastUpdateDate(rs.getString("last_update_date"));
        giftCertificate.setTags(new GiftCertificateTagsServiceImpl().getTagNamesByGiftCertificate(giftCertificate).toArray(new String[0]));
        return giftCertificate;
    }
}