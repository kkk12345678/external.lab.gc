package org.example.gs.service;

import org.example.gs.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateTagsService {
    List<String> getTagNamesByGiftCertificate(GiftCertificate giftCertificate);
}
