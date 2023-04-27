package org.example.gs.dao;

import org.example.gs.model.GiftCertificate;

public interface GiftCertificateDao extends EntityDao<GiftCertificate> {
    void insert(long giftCertificateId, long tagId);
}
