package org.example.gs.dao;

import org.example.gs.model.GiftCertificate;
import org.example.gs.model.Tag;

import javax.sql.DataSource;

public interface GiftCertificateTagDao {
    void setDataSource(DataSource dataSource);
    void insert(Tag tag, GiftCertificate giftCertificate);
}
