package org.example.gc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.example.gc.Application;
import org.example.gc.dto.GiftCertificateInsertDto;
import org.example.gc.dto.TagDto;
import org.example.gc.dto.UserDto;
import org.example.gc.parameters.TagParameters;
import org.example.gc.service.GiftCertificateService;
import org.example.gc.service.TagService;
import org.example.gc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = Application.class)
public class TagCrudTest {
    private static final String URL = "http://localhost:8080/tags";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TagService tagService;

    @Autowired
    private GiftCertificateService giftCertificateService;

    @Autowired
    private UserService userService;

    //@Test
    void create1000TagsAndUsers() {
        userService.add(new UserDto("Admin", "admin", "111"));
        IntStream.range(0, 1000).forEach(i -> {
            tagService.add(new TagDto(String.format("tag%0,3d", i)));
            userService.add(new UserDto(String.format("user%0,3d", i), "user", "111"));
        });
    }

    @Test
    void create10000GiftCertificates() {
        IntStream.range(0, 10000).forEach(i -> {
            Random random = new Random();
            GiftCertificateInsertDto dto = new GiftCertificateInsertDto();
            dto.setName(String.format("gc%0,4d", i + 1));
            dto.setDescription("some description");
            dto.setDuration(random.nextInt(150) + 1);
            dto.setPrice(random.nextDouble() * 300);
            Set<TagDto> tags = new HashSet<>();
            IntStream.range(0, random.nextInt(5)).
                    forEach(j -> tags.add(new TagDto(String.format("tag%0,3d", random.nextInt(1000)))));
            dto.setTags(tags);
            giftCertificateService.add(dto);
        });
    }
    @Test
    void getAllTags() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new Gson().toJson(tagService.getAll(new TagParameters()))));
    }

    @Test
    void createTagTest() throws Exception {
        TagDto tagRequestDto = new TagDto("tag10000");

        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(tagRequestDto)))
                .andExpect(status().isCreated());
        //tagService.add(new TagRequestDto());
    }
}
