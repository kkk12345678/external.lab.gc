package org.example.gs.controller;

import org.example.gs.dto.GiftCertificateRequestDto;
import org.example.gs.model.GiftCertificateParameters;
import org.example.gs.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {
    @Autowired
    private GiftCertificateService giftCertificateService;

    @GetMapping
    public Object getAllGiftCertificates(GiftCertificateParameters giftCertificateParameters) {
        try {
            return new ResponseEntity<>(giftCertificateService.getAll(giftCertificateParameters), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @GetMapping("/{giftCertificateId}")
    public Object getGiftCertificateIdById(@PathVariable("giftCertificateId") String id) {
        try {
            return new ResponseEntity<>(giftCertificateService.getById(Long.parseLong(id)), HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Illegal parameter giftCertificateId = '%s'. Must be positive integer.", id));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("There is no gift certificate with id = '%s'.", id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @PostMapping
    public Object addGiftCertificate(
            @RequestBody(required = false) GiftCertificateRequestDto giftCertificateRequestDto) {
        try {
            return new ResponseEntity<>(giftCertificateService.add(giftCertificateRequestDto), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @PutMapping("/{giftCertificateId}")
    public Object updateGiftCertificate(
            @PathVariable("giftCertificateId") String id,
            @RequestBody(required = false) GiftCertificateRequestDto giftCertificateRequestDto) {
        try {
            giftCertificateService.update(Long.parseLong(id), giftCertificateRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Illegal parameter 'giftCertificateId'. Must be positive integer.");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "There is no gift certificate with id " + id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @DeleteMapping("/{giftCertificateId}")
    public Object deleteGiftCertificate(@PathVariable("giftCertificateId") String id) {
        try {
            giftCertificateService.remove(Long.parseLong(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Illegal parameter 'giftCertificateId'. Must be positive integer.");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "There is no gift certificate with id " + id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
}