package org.example.gc.controller;

import org.example.gc.dto.GiftCertificateInsertDto;
import org.example.gc.dto.GiftCertificateUpdateDto;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.parameters.GiftCertificateParameters;
import org.example.gc.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {
    private static final String GIFT_CERTIFICATE_ID = "giftCertificateId";
    @Autowired
    private GiftCertificateService giftCertificateService;

    @GetMapping
    public List<GiftCertificate> getAllGiftCertificates(GiftCertificateParameters giftCertificateParameters) {
        return giftCertificateService.getAll(giftCertificateParameters);
    }

    @GetMapping("/{giftCertificateId}")
    public GiftCertificate getGiftCertificateIdById(@PathVariable(GIFT_CERTIFICATE_ID) Long id) {
        return giftCertificateService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificate addGiftCertificate(
            @RequestBody(required = false) GiftCertificateInsertDto dto) {
        return giftCertificateService.add(dto);
    }

    @PutMapping("/{giftCertificateId}")
    public GiftCertificate updateGiftCertificate(
            @PathVariable(GIFT_CERTIFICATE_ID) Long id,
            @RequestBody(required = false) GiftCertificateUpdateDto dto) {
        return giftCertificateService.update(id, dto);
    }

    @DeleteMapping("/{giftCertificateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGiftCertificate(@PathVariable(GIFT_CERTIFICATE_ID) Long id) {
        giftCertificateService.remove(id);
    }
}