package org.example.gc.service;

import org.example.gc.dto.GiftCertificateRequestInsertDto;
import org.example.gc.dto.GiftCertificateRequestUpdateDto;
import org.example.gc.dto.GiftCertificateResponseDto;
import org.example.gc.model.GiftCertificateParameters;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface GiftCertificateService {
    GiftCertificateResponseDto getById(long id);
    GiftCertificateResponseDto getByName(String name);
    @Transactional
    long add(GiftCertificateRequestInsertDto giftCertificateRequestInsertDto);
    void remove(long id);
    void update(long l, GiftCertificateRequestUpdateDto giftCertificateRequestUpdateDto);
    Collection<GiftCertificateResponseDto> getAll(GiftCertificateParameters giftCertificateParameters);
}