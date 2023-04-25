package org.example.gs.dao;

import org.example.gs.model.GiftCertificate;
import org.example.gs.model.Tag;

public interface GiftCertificateDao extends EntityDao<GiftCertificate> {
    void insert(long giftCertificateId, long tagId);
}
