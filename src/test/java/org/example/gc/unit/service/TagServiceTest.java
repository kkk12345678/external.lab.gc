package org.example.gc.unit.service;

import org.example.gc.dto.TagDto;
import org.example.gc.entity.Tag;
import org.example.gc.parameters.TagParameters;
import org.example.gc.repository.TagRepository;
import org.example.gc.service.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void testGetAllWithoutParameters() {
        List<Tag> tags = List.of(
                new Tag(1L, "tag1"),
                new Tag(2L, "tag2"),
                new Tag(3L, "tag3"));
        TagParameters parameters = new TagParameters();
        given(tagRepository.getAll(parameters)).willReturn(tags);
        assertEquals(tags, tagService.getAll(parameters));
    }

    @Test
    void testGetByIdWhenExists() {
        Tag tag = new Tag(1L,"Tag");
        given(tagRepository.getById(1L)).willReturn(tag);
        assertEquals(tag, tagService.getById(1L));
    }

    @Test
    void testGetByIdWhenDoesNotExist() {
        given(tagRepository.getById(1L)).willReturn(null);
        assertThrows(NoSuchElementException.class, () -> tagService.getById(1L));
    }

    @Test
    void testAddWhenDoesNotExist() {
        Tag tag = new Tag("Tag");
        given(tagRepository.insertOrUpdate(tag)).willReturn(tag);
        Tag savedTag = tagService.add(new TagDto("Tag"));
        assertThat(savedTag).isNotNull();
        assertEquals("Tag", savedTag.getName());
    }

    @Test
    void testAddWhenExists() {
        Tag tag = new Tag(1L,"Tag");
        given(tagRepository.getByName("Tag")).willReturn(tag);
        assertThrows(IllegalArgumentException.class, () -> tagService.add(new TagDto("Tag")));
    }

    @Test
    void testRemoveWhenExists() {
        Tag tag = new Tag(1L,"Tag");
        given(tagRepository.getById(1L)).willReturn(tag);
        doNothing().when(tagRepository).delete(tag);
        tagService.remove(1L);
    }

    @Test
    void testRemoveWhenDoesNotExist() {
        given(tagRepository.getById(1L)).willReturn(null);
        assertThrows(NoSuchElementException.class, () -> tagService.remove(1L));
    }

    @Test
    void testGetMostValuableTag() {
        Tag tag = new Tag(1L,"Tag");
        given(tagRepository.getMostValuable()).willReturn(tag);
        assertEquals(tag, tagService.getMostValuable());
    }
}
