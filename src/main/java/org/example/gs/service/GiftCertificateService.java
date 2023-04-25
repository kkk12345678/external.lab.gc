package org.example.gs.service;

import org.example.gs.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {
    long add(GiftCertificate giftCertificate);

    void remove(GiftCertificate giftCertificate);

    void update(GiftCertificate giftCertificate);

    List<GiftCertificate> getAll();
}
