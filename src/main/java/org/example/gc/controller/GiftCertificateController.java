package org.example.gc.controller;

import org.example.gc.dto.GiftCertificateInsertDto;
import org.example.gc.dto.GiftCertificateUpdateDto;
import org.example.gc.dto.TagDto;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.parameters.GiftCertificateParameters;
import org.example.gc.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {
    Random random = new Random();
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

    @GetMapping("/count")
    public long countGiftCertificates(GiftCertificateParameters giftCertificateParameters) {
        return giftCertificateService.count(giftCertificateParameters);
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

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createGiftCertificates() {

        GiftCertificateInsertDto dto = new GiftCertificateInsertDto();
        Set<TagDto> tags = new HashSet<>();
        IntStream.range(0, 1000).forEach(i -> {
            tags.clear();
            IntStream.range(0, random.nextInt(5)).forEach(j -> tags.add(new TagDto("Tag" + random.nextInt(1000))));
            dto.setName("gc" + i);
            dto.setDescription("Some description");
            dto.setPrice(random.nextDouble(150) + 10.0);
            dto.setDuration(random.nextInt(30) + 1);
            dto.setTags(tags);
            giftCertificateService.add(dto);
        });
    }
}