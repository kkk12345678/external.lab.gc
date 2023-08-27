package org.example.gc.start;

import org.example.gc.Application;
import org.example.gc.controller.GiftCertificateController;
import org.example.gc.dto.GiftCertificateInsertDto;
import org.example.gc.dto.TagDto;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.entity.Tag;
import org.example.gc.parameters.GiftCertificateParameters;
import org.example.gc.service.GiftCertificateService;
import org.example.gc.service.GiftCertificateServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = Application.class)
class CreateCertificatesTest {
    @Autowired
    GiftCertificateController giftCertificateController;

    @Autowired
    GiftCertificateService giftCertificateService;

    private static final String URL = "http://localhost:8080/gift-certificates";

    @Test
    void test() {
        assertTrue(true);
    }

    @Test
    void createGiftCertificates() {
        Random random = new Random();
        GiftCertificateInsertDto dto = new GiftCertificateInsertDto();
        Set<TagDto> tags = new HashSet<>();
        IntStream.range(0, 1000).forEach(i -> {
            tags.clear();
            IntStream.range(0, random.nextInt(5)).forEach(j -> tags.add(new TagDto("Tag" + (random.nextInt(999) + 1))));
            dto.setName("gc" + (i + 1));
            dto.setDescription("Some description");
            dto.setPrice(random.nextDouble(150) + 10);
            dto.setDuration(random.nextInt(30) + 1);
            dto.setTags(tags);
            giftCertificateService.add(dto);
        });
        assertEquals(1000, giftCertificateService.getAll(new GiftCertificateParameters()).size());
    }
}
