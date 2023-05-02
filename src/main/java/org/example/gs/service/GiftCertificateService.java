package org.example.gs.service;

import org.example.gs.dto.GiftCertificateRequestDto;
import org.example.gs.dto.GiftCertificateResponseDto;
import org.example.gs.model.Parameters;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface GiftCertificateService {
    GiftCertificateResponseDto getById(long id);
    GiftCertificateResponseDto getByName(String name);
    @Transactional
    long add(GiftCertificateRequestDto giftCertificate);
    void remove(long id);
    void update(long l, GiftCertificateRequestDto giftCertificate);
    Collection<GiftCertificateResponseDto> getAll(Parameters parameters);
}