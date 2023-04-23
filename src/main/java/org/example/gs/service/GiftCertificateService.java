package org.example.gs.service;

import org.example.gs.model.GiftCertificate;
import org.example.gs.util.*;

import java.util.List;

public interface GiftCertificateService {
    long add(GiftCertificate giftCertificate, String[] tags);
    void remove(GiftCertificate giftCertificate);
    void update(GiftCertificate giftCertificate);
    List<GiftCertificate> getAll(SortType sortType, SortOrder sortOrder);
    List<GiftCertificate> findByTagName(String tagName, SortType sortType, SortOrder sortOrder);
    List<GiftCertificate> findByName(String name, SortType sortType, SortOrder sortOrder);
    List<GiftCertificate> findByDescription(String description, SortType sortType, SortOrder sortOrder);
}
