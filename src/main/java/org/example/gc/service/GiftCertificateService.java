package org.example.gc.service;

import org.example.gc.dto.GiftCertificateRequestInsertDto;
import org.example.gc.dto.GiftCertificateRequestUpdateDto;
import org.example.gc.dto.GiftCertificateResponseDto;
import org.example.gc.entity.GiftCertificateParameters;

import java.util.List;

public interface GiftCertificateService {
    GiftCertificateResponseDto getById(Long id);
    GiftCertificateResponseDto getByName(String name);
    Long add(GiftCertificateRequestInsertDto giftCertificateRequestInsertDto);
    void remove(Long id);
    void update(Long id, GiftCertificateRequestUpdateDto giftCertificateRequestUpdateDto);
    List<GiftCertificateResponseDto> getAll(GiftCertificateParameters giftCertificateParameters);
}