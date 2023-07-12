package org.example.gc.unit.service;

import org.example.gc.dto.GiftCertificateInsertDto;
import org.example.gc.dto.GiftCertificateUpdateDto;
import org.example.gc.dto.TagDto;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.entity.Tag;
import org.example.gc.parameters.GiftCertificateParameters;
import org.example.gc.parameters.TagParameters;
import org.example.gc.repository.GiftCertificateRepository;
import org.example.gc.repository.TagRepository;
import org.example.gc.service.GiftCertificateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {
    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Test
    void testGetByIdWhenExists() {
        GiftCertificate giftCertificate =
                new GiftCertificate(1L, "gc1", "d1", 10.2, 10, Instant.now(), Instant.now(), new HashSet<>());
        given(giftCertificateRepository.getById(1L)).willReturn(giftCertificate);
        assertEquals(giftCertificate, giftCertificateService.getById(1L));
    }

    @Test
    void testGetByIdWhenDoesNotExist() {
        given(giftCertificateRepository.getById(1L)).willReturn(null);
        assertThrows(NoSuchElementException.class, () -> giftCertificateService.getById(1L));
    }

    @Test
    void testGetByNameWhenExists() {
        GiftCertificate giftCertificate =
                new GiftCertificate(1L, "gc1", "d1", 10.2, 10, Instant.now(), Instant.now(), new HashSet<>());
        given(giftCertificateRepository.getByName("gc1")).willReturn(giftCertificate);
        assertEquals(giftCertificate, giftCertificateService.getByName("gc1"));
    }

    @Test
    void testGetByNameWhenDoesNotExist() {
        given(giftCertificateRepository.getByName("name")).willReturn(null);
        assertThrows(NoSuchElementException.class, () -> giftCertificateService.getByName("name"));
    }

    @Test
    void testAddWhenExists() {
        GiftCertificate giftCertificate =
                new GiftCertificate(1L, "gc1", "d1", 10.2, 10, Instant.now(), Instant.now(), new HashSet<>());
        given(giftCertificateRepository.getByName("gc1")).willReturn(giftCertificate);
        assertThrows(IllegalArgumentException.class,
                () -> giftCertificateService.add(new GiftCertificateInsertDto("gc1", "d1", 23.0, 20, new HashSet<>())));
    }

    @Test
    void testAddWhenDoesNotExist() {
        Instant instant = Instant.parse("2023-06-22T06:28:55.755938500Z");
        GiftCertificate giftCertificate =
                new GiftCertificate(null, "gc1", "d1", 10.2, 10, instant, instant,
                        new HashSet<>());
        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            given(tagRepository.getByNames(new HashSet<>())).willReturn(new ArrayList<>());
            given(giftCertificateRepository.insertOrUpdate(giftCertificate)).willReturn(giftCertificate);
            GiftCertificate savedGiftCertificate
                    = giftCertificateService.add(new GiftCertificateInsertDto("gc1", "d1", 10.2, 10,
                    new HashSet<>()));
            assertNotNull(savedGiftCertificate);
            assertEquals(giftCertificate, savedGiftCertificate);
        }
    }

    @Test
    void testRemoveWhenExists() {
        Instant instant = Instant.parse("2023-06-22T06:28:55.755938500Z");
        GiftCertificate giftCertificate =
                new GiftCertificate(1L, "gc1", "d1", 10.2, 10, instant, instant,
                        new HashSet<>());
        given(giftCertificateRepository.getById(1L)).willReturn(giftCertificate);
        doNothing().when(giftCertificateRepository).delete(giftCertificate);
        giftCertificateService.remove(1L);
    }

    @Test
    void testRemoveWhenDoesNotExist() {
        given(giftCertificateRepository.getById(1L)).willReturn(null);
        assertThrows(NoSuchElementException.class, () -> giftCertificateService.remove(1L));

    }

    @Test
    void testUpdateWhenExists() {
        Instant instant = Instant.parse("2023-06-22T06:28:55.755938500Z");
        GiftCertificate giftCertificate =
                new GiftCertificate(1L, "gc1", "d1", 10.2, 10, instant, instant,
                        new HashSet<>());
        given(giftCertificateRepository.getById(1L)).willReturn(giftCertificate);
        given(giftCertificateRepository.getByName("gc1")).willReturn(giftCertificate);
        given(giftCertificateRepository.insertOrUpdate(giftCertificate)).willReturn(giftCertificate);
        GiftCertificate updated = giftCertificateService.update(1L, new GiftCertificateUpdateDto("gc1", "d1", 10.2, 10, new HashSet<>()));
        assertEquals(giftCertificate, updated);
    }

    @Test
    void testUpdateWhenDoesNotExist() {
        given(giftCertificateRepository.getById(1L)).willReturn(null);
        assertThrows(NoSuchElementException.class,
                () -> giftCertificateService.update(1L, new GiftCertificateUpdateDto()));
    }

    @Test
    void getAll() {
        List<GiftCertificate> giftCertificates = List.of(
                new GiftCertificate(1L, "gc1", "d1", 10.2, 10, Instant.now(), Instant.now(), null),
                new GiftCertificate(2L, "gc2", "d2", 10.2, 10, Instant.now(), Instant.now(), null),
                new GiftCertificate(3L, "gc3", "d3", 10.2, 10, Instant.now(), Instant.now(), null));
        GiftCertificateParameters parameters = new GiftCertificateParameters();
        given(giftCertificateRepository.getAll(parameters)).willReturn(giftCertificates);
        assertEquals(giftCertificates, giftCertificateService.getAll(parameters));
    }
}
