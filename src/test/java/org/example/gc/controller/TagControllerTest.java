package org.example.gc.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TagControllerTest {
    /*
    private static Tag createdTag = new Tag();
    private static final String URL = "http://localhost:8080/tags";
    private static final String NAME = RandomStringUtils.randomAlphanumeric(10);
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private TagDao tagDao;
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAllTags() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new Gson().toJson(tagDao.getAll(new TagParametersHandler(null)))));
    }

    @Test
    void testGetById_whenIdExists() {
        tagDao.getAll(null).stream().limit(10).forEach(tag -> {
            try {
                mockMvc.perform(get(URL + "/" + tag.getId()))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(new Gson().toJson(tag)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testGetById_whenIdDoesNotExist() throws Exception {
        mockMvc.perform(get(URL + "/" + 0))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetById_whenIdIsNotValid() throws Exception {
        mockMvc.perform(get(URL + "/f"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(1)
    void testPost_whenTagIsValid() throws Exception {
        TagRequestDto tagRequestDto = new TagRequestDto();
        tagRequestDto.setName(NAME);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(tagRequestDto)))
                .andExpect(status().isCreated());

        createdTag = tagDao.getByName(NAME);
    }

    @Test
    @Order(2)
    void testPost_whenTagExists() throws Exception {
        TagRequestDto tagRequestDto = new TagRequestDto();
        tagRequestDto.setName(createdTag.getName());
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(tagRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPost_whenTagNameIsNull() throws Exception {
        TagRequestDto tagRequestDto = new TagRequestDto();
        tagRequestDto.setName(null);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(tagRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPost_whenTagNameIsEmpty() throws Exception {
        TagRequestDto tagRequestDto = new TagRequestDto();
        tagRequestDto.setName("");
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(tagRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPost_whenTagIsNull() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void testDelete_whenTagExists() throws Exception {
        long id = createdTag.getId();
        mockMvc.perform(delete(URL + "/" + id)).andExpect(status().isNoContent());
    }

    @Test
    void testDelete_whenTagDoesNotExist() throws Exception {
        mockMvc.perform(delete(URL + "/" + 0))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_whenIdIsNotValid() throws Exception {
        mockMvc.perform(delete(URL + "/l"))
                .andExpect(status().isBadRequest());
    }

     */
}