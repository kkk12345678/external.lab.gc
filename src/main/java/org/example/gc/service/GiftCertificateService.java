package org.example.gc.service;

import org.example.gc.dto.GiftCertificateInsertDto;
import org.example.gc.dto.GiftCertificateUpdateDto;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.parameters.GiftCertificateParameters;

import java.util.List;

public interface GiftCertificateService {
    GiftCertificate getById(Long id);
    GiftCertificate getByName(String name);
    GiftCertificate add(GiftCertificateInsertDto dto);
    void remove(Long id);
    GiftCertificate update(Long id, GiftCertificateUpdateDto dto);
    List<GiftCertificate> getAll(GiftCertificateParameters giftCertificateParameters);
}