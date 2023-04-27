package org.example.gs.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.gs.model.GiftCertificate;
import org.example.gs.model.Tag;
import org.example.gs.service.GiftCertificateService;
import org.example.gs.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {
    @Autowired
    private TagService tagService;
    @Autowired
    private GiftCertificateService giftCertificateService;

    @GetMapping
    public List<GiftCertificate> getAllGiftCertificates(@RequestParam(required = false) String[] sort,
                                         @RequestParam(required = false) String[] search,
                                         @RequestParam(required = false) String limit,
                                         @RequestParam(required = false) String page) {
        try {
            return giftCertificateService.getAll(getParameters(sort, search, limit, page));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), e);
        }
    }

    @GetMapping("/{giftCertificateId}")
    public GiftCertificate getGiftCertificateIdById(@PathVariable("giftCertificateId") String id) {
        try {
            return giftCertificateService.getById(Long.parseLong(id)).get();
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Illegal parameter 'giftCertificateId'. Must be positive integer.", e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "There is no gift certificate with id " + id, e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    e.getMessage(), e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addGiftCertificate(@RequestBody(required = false) GiftCertificate giftCertificate) {
        if (giftCertificate == null || giftCertificate.getName() == null || giftCertificate.getName().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Parameter 'name' is not specified.");
        }
        String name = giftCertificate.getName();
        //TODO validate other fields
        if (giftCertificateService.getByName(name).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Gift certificate with name '%s' already exists.", name));
        }
        try {
            return giftCertificateService.add(giftCertificate);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    e.getMessage(), e);
        }
    }

    @PutMapping("/{giftCertificateId}")
    @ResponseStatus(HttpStatus.OK)
    public void addGiftCertificate(
            @PathVariable("giftCertificateId") Long id,
            @RequestBody GiftCertificate giftCertificate) {
        //TODO validation
        giftCertificateService.update(giftCertificate);
    }

    @DeleteMapping("/{giftCertificateId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGiftCertificate(@PathVariable("giftCertificateId") String id) {
        try {
            giftCertificateService.remove(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Illegal parameter 'giftCertificateId'. Must be positive integer.", e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "There is no gift certificate with id " + id, e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    e.getMessage(), e);
        }
    }

    private static String getParameters(String[] sort, String[] search, String limit, String page) {

        /* where gift_certificate_name like %gs_name%
         * and gift_certificate_description like %description%
         * and tag_name like %tag_name%
         * order by gift_certificate_name asc
         * limit 1 offset 1;
         */
        return getWhereClause(search) + getOrderByClause(sort) + getLimitOffsetClause(limit, page);
    }

    private static String getOrderByClause(String[] sort) {
        //TODO
        return "";
    }

    private static String getWhereClause(String[] search) {
        //TODO
        return "";
    }

    private static String getLimitOffsetClause(String limit, String page) {
        if (limit == null && page == null) {
            return "";
        }
        if (limit == null) {
            throw new IllegalArgumentException("'limit' parameter is not specified while 'page' is");
        }
        if (page == null) {
            throw new IllegalArgumentException("'page' parameter is not specified while 'limit' is");
        }
        int l = Integer.parseInt(limit);
        int p = Integer.parseInt(page);
        if (l < 1) {
            throw new IllegalArgumentException("'limit' parameter must be positive");
        }
        if (p < 1) {
            throw new IllegalArgumentException("'page' parameter must be positive");
        }
        return String.format(" limit %d offset %d", l, (p - 1) * l);
    }
}
