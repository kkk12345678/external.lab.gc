package org.example.gs.controller;

import org.example.gs.dto.GiftCertificateRequestDto;
import org.example.gs.model.Parameters;
import org.example.gs.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {
    @Autowired
    private GiftCertificateService giftCertificateService;

    @GetMapping
    public Object getAllGiftCertificates(@RequestParam(required = false) String[] sort,
                                         @RequestParam(required = false) String[] search,
                                         @RequestParam(required = false) String tagName,
                                         @RequestParam(required = false) String limit,
                                         @RequestParam(required = false) String page) {
        try {
            Parameters parameters = new Parameters();
            parameters.setPagination(parsePaginationParameters(limit, page));
            parameters.setSort(parseSortParameters(sort));
            parameters.setSearch(parseSearchParameters(search, tagName));
            return new ResponseEntity<>(giftCertificateService.getAll(parameters), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/{giftCertificateId}")
    public Object getGiftCertificateIdById(@PathVariable("giftCertificateId") String id) {
        try {
            return new ResponseEntity<>(
                    giftCertificateService.getById(Long.parseLong(id)),
                    HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(
                    String.format("Illegal parameter giftCertificateId = '%s'. Must be positive integer.", id),
                    HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    String.format("There is no gift certificate with id = '%s'.", id),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping
    public Object addGiftCertificate(
            @RequestBody(required = false) GiftCertificateRequestDto giftCertificateRequestDto) {
        try {
            return new ResponseEntity<>(giftCertificateService.add(giftCertificateRequestDto), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
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
            return new ResponseEntity<>(
                    "Illegal parameter 'giftCertificateId'. Must be positive integer.",
                    HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    "There is no gift certificate with id " + id,
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @DeleteMapping("/{giftCertificateId}")
    public Object deleteGiftCertificate(@PathVariable("giftCertificateId") String id) {
        try {
            giftCertificateService.remove(Long.parseLong(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(
                    "Illegal parameter 'giftCertificateId'. Must be positive integer.",
                    HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    "There is no gift certificate with id " + id,
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private Parameters.Pagination parsePaginationParameters(String limit, String page) {
        if (limit == null && page == null) {
            return null;
        }
        if (limit == null) {
            throw new IllegalArgumentException("'limit' parameter is not specified while 'page' is.");
        }
        if (page == null) {
            throw new IllegalArgumentException("'page' parameter is not specified while 'limit' is.");
        }
        int l = Integer.parseInt(limit);
        int p = Integer.parseInt(page);
        if (l < 1) {
            throw new IllegalArgumentException("'limit' parameter must be positive.");
        }
        if (p < 1) {
            throw new IllegalArgumentException("'page' parameter must be positive.");
        }
        return new Parameters.Pagination(l, l * (p - 1));
    }

    private Parameters.Sort parseSortParameters(String[] sortParameters) {
        if (sortParameters == null) {
            return null;
        }
        int length = sortParameters.length;
        if (length > 2) {
            throw new IllegalArgumentException("'sort' parameter has more values than allowed.");
        }
        String[] fields = new String[length];
        String[] sort = new String[length];

        for (int i = 0; i < length; i++) {
            String[] parts = sortParameters[i].split(",");
            if (parts.length != 2 ||
                    !(parts[0].equals("name") || parts[0].equals("date")) ||
                    !(parts[1].equals("asc") || parts[1].equals("desc"))) {
                throw new IllegalArgumentException(
                        String.format("'sort' parameter '%s' does not match the pattern", sortParameters[i]));
            }
            fields[i] = (parts[0].equals("name") ? "gift_certificate_" : "create_") + parts[0];
            sort[i] = parts[1];
        }
        return new Parameters.Sort(fields, sort);
    }

    private Parameters.Search parseSearchParameters(String[] searchParameters, String tagName) {
        if (searchParameters == null && (tagName == null || tagName.isBlank())) {
            return null;
        }
        if (searchParameters == null) {
            return new Parameters.Search(new String[]{"tag_name"}, new String[]{tagName});
        }
        int length = searchParameters.length;
        String[] fields;
        String[] search;
        if (tagName != null && !tagName.isBlank()) {
            fields = new String[length + 1];
            search = new String[length + 1];
            fields[length] = "tag_name";
            search[length] = tagName;
        } else {
            fields = new String[length];
            search = new String[length];
        }
        for (int i = 0; i < length; i++) {
            String[] parts = searchParameters[i].split(",");
            if (parts.length != 2 ||
                    !(parts[0].equals("name") || parts[0].equals("description"))) {
                throw new IllegalArgumentException(
                        String.format("'sort' parameter '%s' does not match the pattern", searchParameters[i]));
            }
            fields[i] = parts[0];
            search[i] = parts[1];
        }
        return new Parameters.Search(fields, search);
    }
}