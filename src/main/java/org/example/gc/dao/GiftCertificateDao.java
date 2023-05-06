package org.example.gc.dao;

import org.example.gc.model.GiftCertificate;

public interface GiftCertificateDao extends EntityDao<GiftCertificate> {
    void insert(long giftCertificateId, long tagId);
    void update(GiftCertificate giftCertificate);
    void deleteTags(long giftCertificateId);
}