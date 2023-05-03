package org.example.gs.controller;

import org.example.gs.dto.GiftCertificateRequestDto;
import org.example.gs.model.GiftCertificateParameters;
import org.example.gs.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {
    private static final String GIFT_CERTIFICATE_ID = "giftCertificateId";
    @Autowired
    private GiftCertificateService giftCertificateService;

    @GetMapping
    public ResponseEntity<Object> getAllGiftCertificates(
            GiftCertificateParameters giftCertificateParameters) {
        return new ResponseEntity<>(
                giftCertificateService.getAll(giftCertificateParameters), HttpStatus.OK);
    }

    @GetMapping("/{giftCertificateId}")
    public ResponseEntity<Object> getGiftCertificateIdById(
            @PathVariable(GIFT_CERTIFICATE_ID) Long id) {
        return new ResponseEntity<>(
                giftCertificateService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addGiftCertificate(
            @RequestBody(required = false) GiftCertificateRequestDto giftCertificateRequestDto) {
        return new ResponseEntity<>(
                giftCertificateService.add(giftCertificateRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{giftCertificateId}")
    public ResponseEntity<Object> updateGiftCertificate(
            @PathVariable(GIFT_CERTIFICATE_ID) Long id,
            @RequestBody(required = false) GiftCertificateRequestDto giftCertificateRequestDto) {
        giftCertificateService.update(id, giftCertificateRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{giftCertificateId}")
    public ResponseEntity<Object> deleteGiftCertificate(
            @PathVariable("giftCertificateId") Long id) {
        giftCertificateService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}