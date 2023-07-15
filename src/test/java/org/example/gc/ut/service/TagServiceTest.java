package org.example.gc.ut.service;

import org.example.gc.dto.TagDto;
import org.example.gc.entity.Tag;
import org.example.gc.exception.AlreadyExistsException;
import org.example.gc.parameters.TagParameters;
import org.example.gc.repository.TagRepository;
import org.example.gc.service.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
@AutoConfigureMockMvc
class TagServiceTest {
    private static final List<Tag> tags = List.of(
            new Tag(1L, "tag1"),
            new Tag(2L, "tag2"),
            new Tag(3L, "tag3")
    );
    private static final List<Tag> emptyList = new ArrayList<>();
    private static final long ID = 4L;
    private static final String NAME = "tag4";

    private static final Tag tag = new Tag(ID, NAME);

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void testGetAllWithoutPagination() {
        TagParameters tagParameters = new TagParameters();
        Mockito.when(tagRepository.getAll(tagParameters)).thenReturn(tags);
        assertEquals(tags, tagService.getAll(tagParameters));
    }

    @Test
    void testGetAllWitValidPagination() {
        TagParameters tagParameters = new TagParameters();
        tagParameters.setLimit(1);
        tagParameters.setPage(2);
        Mockito.when(tagRepository.getAll(tagParameters)).thenReturn(List.of(tags.get(1)));
        assertEquals(List.of(tags.get(1)), tagService.getAll(tagParameters));
    }

    @Test
    void testGetAllWitInvalidLimit() {
        TagParameters tagParameters = new TagParameters();
        tagParameters.setLimit(-1);
        assertEquals(emptyList, tagService.getAll(tagParameters));
        tagParameters.setLimit(0);
        assertEquals(emptyList, tagService.getAll(tagParameters));
    }

    @Test
    void testAddNewTag() {
        tag.setId(null);
        Mockito.when(tagRepository.getByName(NAME)).thenReturn(null);
        Mockito.when(tagRepository.insertOrUpdate(tag)).thenReturn(tag);
        assertEquals(tag, tagService.add(new TagDto(NAME)));
        tag.setId(ID);
    }

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
}