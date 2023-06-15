package org.example.gc.service;

import org.example.gc.dto.GiftCertificateRequestInsertDto;
import org.example.gc.dto.GiftCertificateRequestUpdateDto;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.parameters.GiftCertificateParameters;

import java.util.List;

public interface GiftCertificateService {
    GiftCertificate getById(Long id);
    GiftCertificate getByName(String name);
    GiftCertificate add(GiftCertificateRequestInsertDto giftCertificateRequestInsertDto);
    void remove(Long id);
    GiftCertificate update(Long id, GiftCertificateRequestUpdateDto giftCertificateRequestUpdateDto);
    List<GiftCertificate> getAll(GiftCertificateParameters giftCertificateParameters);
}