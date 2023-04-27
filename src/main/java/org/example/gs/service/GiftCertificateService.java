package org.example.gs.service;

import org.example.gs.model.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    long add(GiftCertificate giftCertificate);

    void remove(long id);

    void update(GiftCertificate giftCertificate);

    List<GiftCertificate> getAll(String parameters);
    Optional<GiftCertificate> getById(long id);
    Optional<GiftCertificate> getByName(String name);
}
