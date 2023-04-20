package org.example.gs;

import org.example.gs.dao.TagJdbcTemplate;
import org.example.gs.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TagCrudTest {
    private final TagJdbcTemplate tagJdbcTemplate;
    private final Tag generatedTag;

    public TagCrudTest() {
        this.tagJdbcTemplate =
                (TagJdbcTemplate) new ClassPathXmlApplicationContext("beans.xml")
                        .getBean("tagJdbcTemplate");
        this.generatedTag = generateNewTag();
    }

    @Test
    void testTagCrud() {
        testTagInsertAndGet();
        testUpdate();
        testDelete();
    }

    private void testTagInsertAndGet() {
        int count = tagJdbcTemplate.getAll().size();
        tagJdbcTemplate.insert(generatedTag);
        assertEquals(count + 1, tagJdbcTemplate.getAll().size());
        Optional<Tag> optional = tagJdbcTemplate.getByName(generatedTag.getName());
        assertTrue(optional.isPresent());
        assertThrows(DuplicateKeyException.class, () -> tagJdbcTemplate.insert(generatedTag));
        generatedTag.setId(optional.get().getId());
        assertEquals(generatedTag, optional.get());
        optional = tagJdbcTemplate.getById(generatedTag.getId());
        assertTrue(optional.isPresent());
        assertEquals(generatedTag, optional.get());
    }

    private void testUpdate() {
        generatedTag.setName(generateNewTag().getName());
        tagJdbcTemplate.update(generatedTag);
        Optional<Tag> optional = tagJdbcTemplate.getById(generatedTag.getId());
        assertTrue(optional.isPresent());
        assertEquals(generatedTag, optional.get());
    }

    private void testDelete() {
        int count = tagJdbcTemplate.getAll().size();
        tagJdbcTemplate.delete(generatedTag.getId());
        assertEquals(count - 1, tagJdbcTemplate.getAll().size());
        Optional<Tag> optional = tagJdbcTemplate.getByName(generatedTag.getName());
        assertTrue(optional.isEmpty());
        optional = tagJdbcTemplate.getById(generatedTag.getId());
        assertTrue(optional.isEmpty());
    }

    private Tag generateNewTag() {
        Tag tag = new Tag();
        String name;
        do {
            name = randomString();
        } while (tagJdbcTemplate.getByName(name).isPresent());
        tag.setName(name);
        return tag;
    }

    private static String randomString() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}