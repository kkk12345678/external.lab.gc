package org.example.gc.ut.service;

import org.example.gc.dto.GiftCertificateInsertDto;
import org.example.gc.dto.TagDto;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.entity.Tag;
import org.example.gc.exception.AlreadyExistsException;
import org.example.gc.parameters.GiftCertificateParameters;
import org.example.gc.repository.GiftCertificateRepository;
import org.example.gc.repository.TagRepository;
import org.example.gc.service.GiftCertificateServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class GiftCertificateServiceTest {
    private static final List<GiftCertificate> emptyList = new ArrayList<>();
    private static final long ID = 4L;
    private static final String NAME = "gc4";
    private static final Set<Tag> tags = Set.of(
            new Tag(1L, "tag1"),
            new Tag(2L, "tag2"),
            new Tag(3L, "tag3")
    );

    private static final List<GiftCertificate> giftCertificates = List.of(
            new GiftCertificate(1L, "gc1", "some description", 10.0, 10, Instant.now(), Instant.now(), tags),
            new GiftCertificate(2L, "gc2", "some description", 11.0, 10, Instant.now(), Instant.now(), tags),
            new GiftCertificate(3L, "gc3", "some description", 12.0, 10, Instant.now(), Instant.now(), tags)
    );

    private static final GiftCertificate giftCertificate =
            new GiftCertificate(4L, "gc4", "some description", 14.0, 10, Instant.now(), Instant.now(), new HashSet<>());

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Test
    void testGetAllWithoutPagination() {
        GiftCertificateParameters giftCertificateParameters = new GiftCertificateParameters();
        Mockito.when(giftCertificateRepository.getAll(giftCertificateParameters)).thenReturn(giftCertificates);
        assertEquals(giftCertificates, giftCertificateService.getAll(giftCertificateParameters));
    }

    @Test
    void testGetAllWitValidPagination() {
        GiftCertificateParameters giftCertificateParameters = new GiftCertificateParameters();
        giftCertificateParameters.setLimit(1);
        giftCertificateParameters.setPage(2);
        Mockito.when(giftCertificateRepository.getAll(giftCertificateParameters)).thenReturn(List.of(giftCertificates.get(1)));
        assertEquals(List.of(giftCertificates.get(1)), giftCertificateService.getAll(giftCertificateParameters));
    }

    @Test
    void testGetAllWitInvalidLimit() {
        GiftCertificateParameters giftCertificateParameters = new GiftCertificateParameters();
        giftCertificateParameters.setLimit(-1);
        assertEquals(emptyList, giftCertificateService.getAll(giftCertificateParameters));
        giftCertificateParameters.setLimit(0);
        assertEquals(emptyList, giftCertificateService.getAll(giftCertificateParameters));
    }

    @Test
    void testAddNewTag() {
        giftCertificate.setId(null);
        Mockito.when(giftCertificateRepository.getByName(NAME)).thenReturn(null);
        Mockito.when(giftCertificateRepository.insertOrUpdate(giftCertificate)).thenReturn(giftCertificate);
        assertEquals(giftCertificate, giftCertificateService.add(new GiftCertificateInsertDto(NAME, "some description", 14.0, 10, new HashSet<>())));
        giftCertificate.setId(ID);
    }
/*
    @Test
    void testAddWhenExists() {
        TagDto dto = new TagDto(NAME);
        Mockito.when(tagRepository.getByName(NAME)).thenReturn(tag);
        assertThrows(AlreadyExistsException.class, () -> tagService.add(dto));
    }

    @Test
    void testRemoveWhenExists() {
        Mockito.when(tagRepository.getById(ID)).thenReturn(tag);
        Mockito.doNothing().when(tagRepository).delete(tag);
        assertDoesNotThrow(() -> tagService.remove(ID));
    }

    @Test
    void testRemoveWhenDoesNotExist() {
        Mockito.when(tagRepository.getById(ID)).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> tagService.remove(ID));
    }

    @Test
    void testGetByIdWhenExists() {

        Mockito.when(tagRepository.getById(ID)).thenReturn(tag);
        assertEquals(tag, tagService.getById(ID));
    }

    @Test
    void testGetByIdWhenDoesNotExist() {
        Mockito.when(tagRepository.getById(ID)).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> tagService.getById(ID));
    }

    @Test
    void testGetMostValuable() {
        Tag tag = new Tag(ID, NAME);
        Mockito.when(tagRepository.getMostValuable()).thenReturn(tag);
        assertEquals(tag, tagService.getMostValuable());
    }

    
 */
}
