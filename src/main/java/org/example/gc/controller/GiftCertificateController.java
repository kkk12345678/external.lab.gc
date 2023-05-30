package org.example.gc.controller;

import org.example.gc.dto.GiftCertificateRequestInsertDto;
import org.example.gc.dto.GiftCertificateRequestUpdateDto;
import org.example.gc.dao.GiftCertificateParametersHandler;
import org.example.gc.model.GiftCertificateParameters;
import org.example.gc.service.GiftCertificateService;
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
    public ResponseEntity<Object> getAllGiftCertificates(GiftCertificateParameters giftCertificateParameters) {
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
            @RequestBody(required = false) GiftCertificateRequestInsertDto giftCertificateRequestInsertDto) {
        return new ResponseEntity<>(
                giftCertificateService.add(giftCertificateRequestInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{giftCertificateId}")
    public ResponseEntity<Object> updateGiftCertificate(
            @PathVariable(GIFT_CERTIFICATE_ID) Long id,
            @RequestBody(required = false) GiftCertificateRequestUpdateDto giftCertificateRequestUpdateDto) {
        giftCertificateService.update(id, giftCertificateRequestUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{giftCertificateId}")
    public ResponseEntity<Object> deleteGiftCertificate(
            @PathVariable("giftCertificateId") Long id) {
        giftCertificateService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}